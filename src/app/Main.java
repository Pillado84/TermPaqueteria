package app;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import dao.DaoEnvioSqlite;
import dao.EnvioRepository;
import oop.Direccion;
import oop.Envio;
import oop.Transporte;

public class Main {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		EnvioRepository repo = new DaoEnvioSqlite();
		int opcion;

		do {
			System.out.println("\n===== GESTIÓN DE ENVÍOS =====");
			System.out.println("1. Crear envío");
			System.out.println("2. Buscar envío por Seguimiento");
			System.out.println("3. Listar todos los envíos");
			System.out.println("4. Actualizar estado de un envío");
			System.out.println("0. Salir");
			System.out.print("Selecciona una opción: ");

			opcion = sc.nextInt();
			sc.nextLine(); // limpiar buffer

			switch (opcion) {
			case 1:
				crearEnvio(sc, repo);
				break;
			case 2:
				buscarEnvio(sc, repo);
				break;
			case 3:
				listarEnvios(repo);
				break;
			case 4:
				actualizarEnvio(sc, repo);
				break;
			case 0:
				System.out.println("Saliendo del sistema...");
				break;
			default:
				System.out.println("Opción no válida.");
			}
		} while (opcion != 0);

		sc.close();
	}

	private static void actualizarEnvio(Scanner sc, EnvioRepository repo) {
		Optional<Envio> opt = buscarEnvio(sc, repo);
		if (opt.isEmpty()) {
			return;
		}

		Envio envio = opt.get();

		if (envio.getFechaFin() != null) {
			System.out.println("\nEste envío ya tiene fecha de fin (" + envio.getFechaFin() + ").");
			System.out.println("El estado no puede modificarse.");
			return;
		}

		System.out.println("\nEstados disponibles:");
		for (Envio.Estado e : Envio.Estado.values()) {
			System.out.println(" - " + e.name());
		}

		System.out.print("Nuevo estado: ");
		String nuevoEstado = sc.nextLine().trim().toUpperCase();
		Envio.Estado estadoNuevo;

		try {
			estadoNuevo = Envio.Estado.valueOf(nuevoEstado);
		} catch (IllegalArgumentException ex) {
			System.out.println("Estado no válido.");
			return;
		}

		envio.setEstado(estadoNuevo);

		if (estadoNuevo == Envio.Estado.FIN || estadoNuevo == Envio.Estado.CANCELADO) {
			envio.setFechaFin(java.time.LocalDate.now());
			System.out.println("Fecha de fin establecida automáticamente: " + envio.getFechaFin());
		}

		boolean ok = repo.actualizar(envio);

		if (ok) {
			System.out.println("\nEnvío actualizado correctamente.");
		} else {
			System.out.println("\nNo se pudo actualizar el envío.");
		}
	}

	private static void listarEnvios(EnvioRepository repo) {
		System.out.println("\n--- Lista de envíos ---");
		List<Envio> lista = repo.listarTodos();

		if (lista.isEmpty()) {
			System.out.println("No hay envíos registrados.");
			return;
		}

		for (Envio e : lista) {
			System.out.println(e);
		}
	}

	private static Optional<Envio> buscarEnvio(Scanner sc, EnvioRepository repo) {
		System.out.print("\nIntroduce el código de seguimiento: ");
		String codigo = sc.nextLine();

		if (codigo.isBlank()) {
			System.out.println("Hay que introducir un codigo.");
			return null;
		}

		Optional<Envio> envio = repo.buscarPorCodigoSeguimiento(codigo);

		if (envio.isPresent()) {
			System.out.println("\n=== ENVÍO ENCONTRADO ===");
			System.out.println(envio.get());
		} else {
			System.out.println("\nNo existe ningún envío con ese código de seguimiento.");
		}

		return envio;
	}

	private static void crearEnvio(Scanner sc, EnvioRepository repo) {
		System.out.println("\n--- Crear Envío ---");
		System.out.println("Dirección del remitente:");
		Direccion remitente = leerDireccion(sc);

		System.out.println("\nDirección del destinatario:");
		Direccion destinatario = leerDireccion(sc);

		// Transporte
		Transporte transporte = new Transporte("001");

		// Envio
		Envio envio = new Envio(destinatario, remitente, transporte);

		// Registrar envio en la base de datos
		repo.insertar(envio);
	}

	private static Direccion leerDireccion(Scanner sc) {
		System.out.print("Calle: ");
		String calle = sc.nextLine();

		System.out.print("Número: ");
		String numero = sc.nextLine();

		System.out.print("Piso: ");
		String piso = sc.nextLine();

		System.out.print("Ciudad: ");
		String ciudad = sc.nextLine();

		System.out.print("Provincia: ");
		String provincia = sc.nextLine();

		System.out.print("Comunidad: ");
		String comunidad = sc.nextLine();

		Direccion direccion = new Direccion(calle, numero, ciudad, provincia, comunidad);

		if (piso != null && piso != "") {
			direccion.setPiso(piso);
		}

		return direccion;
	}
}
