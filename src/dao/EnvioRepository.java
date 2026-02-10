package dao;

import java.util.List;
import java.util.Optional;

import oop.Envio;

public interface EnvioRepository {
	long insertar(Envio envio);

	boolean actualizar(Envio envio); // por id

	boolean borrarPorId(long id);

	Optional<Envio> buscarPorId(long id);

	Optional<Envio> buscarPorCodigoSeguimiento(String codigo);

	List<Envio> listarTodos();
}
