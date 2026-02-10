package bibliotecas;

import java.security.SecureRandom;
import java.util.Objects;

import dao.DaoEnvioSqlite;
import dao.EnvioRepository;

public class GeneradorCodigoSeguimiento {
	private static final String PREFIJO = "ENV-";
	private static final char[] LETRAS = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ".toCharArray();
	private static final char[] NUMEROS = "0123456789".toCharArray();
	private static final SecureRandom RNG = new SecureRandom();

	protected EnvioRepository repo;
	private final int maxAttempts;

	public GeneradorCodigoSeguimiento(int maxAttempts) {
		this.repo = new DaoEnvioSqlite();
		
		this.repo = Objects.requireNonNull(repo, "repo");
		if (maxAttempts < 1)
			throw new IllegalArgumentException("maxAttempts invalido");
		this.maxAttempts = maxAttempts;
	}

	public GeneradorCodigoSeguimiento() {
		this(10_000);
	}

	public String generarUnico() {
		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			String codigo = PREFIJO + generarBloqueAlfanumerico();

			if (repo.buscarPorCodigoSeguimiento(codigo).isEmpty()) {
				return codigo;
			}
		}
		throw new IllegalStateException("No se pudo generar un codigo unico tras " + maxAttempts + " intentos");
	}

	private static String generarBloqueAlfanumerico() {
		char[] buffer = new char[12];

		// 6 letras
		for (int i = 0; i < 6; i++) {
			buffer[i] = LETRAS[RNG.nextInt(LETRAS.length)];
		}

		// 6 numeros
		for (int i = 6; i < 12; i++) {
			buffer[i] = NUMEROS[RNG.nextInt(NUMEROS.length)];
		}

		// Mezclamos (Fisher–Yates shuffle)
		for (int i = buffer.length - 1; i > 0; i--) {
			int j = RNG.nextInt(i + 1);
			char tmp = buffer[i];
			buffer[i] = buffer[j];
			buffer[j] = tmp;
		}

		return new String(buffer);
	}
}
