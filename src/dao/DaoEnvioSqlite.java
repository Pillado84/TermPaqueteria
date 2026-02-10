package dao;

import db.SQLite;
import oop.Direccion;
import oop.Envio;
import oop.Transporte;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoEnvioSqlite implements EnvioRepository {

	private final SQLite db = new SQLite("jdbc:sqlite:paqueteria.db");

	public DaoEnvioSqlite() {
		this.db.initSchema();
	}

	@Override
	public long insertar(Envio e) {
		String sql = """
				  INSERT INTO envios (
				    codigo_seguimiento,
				    dest_calle, dest_numero, dest_piso, dest_localidad, dest_provincia, dest_comunidad,
				    rem_calle, rem_numero, rem_piso, rem_localidad, rem_provincia, rem_comunidad,
				    transporte_codigo, transporte_vehiculo,
				    fecha_inicio, fecha_fin, estado
				  ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
				""";

		try (Connection c = db.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			bindInsertOrUpdate(ps, e, false); // false = INSERT (sin id)
			ps.executeUpdate();

			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					long id = keys.getLong(1);
					e.setId(id);
					return id;
				}
			}
			throw new SQLException("No se generó ID al insertar Envio");

		} catch (SQLException ex) {
			throw new RuntimeException("Error insertando Envio", ex);
		}
	}

	@Override
	public boolean actualizar(Envio e) {
		if (e.getId() == null) {
			throw new IllegalArgumentException("Envio sin id: no se puede actualizar");
		}

		String sql = """
				  UPDATE envios SET
				    codigo_seguimiento=?,
				    dest_calle=?, dest_numero=?, dest_piso=?, dest_localidad=?, dest_provincia=?, dest_comunidad=?,
				    rem_calle=?, rem_numero=?, rem_piso=?, rem_localidad=?, rem_provincia=?, rem_comunidad=?,
				    transporte_codigo=?, transporte_vehiculo=?,
				    fecha_inicio=?, fecha_fin=?, estado=?
				  WHERE id=?
				""";

		try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			bindInsertOrUpdate(ps, e, true); // true = UPDATE (incluye id al final)
			int n = ps.executeUpdate();
			return n == 1;

		} catch (SQLException ex) {
			throw new RuntimeException("Error actualizando Envio", ex);
		}
	}

	@Override
	public boolean borrarPorId(long id) {
		String sql = "DELETE FROM envios WHERE id=?";
		try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			return ps.executeUpdate() == 1;
		} catch (SQLException ex) {
			throw new RuntimeException("Error borrando Envio", ex);
		}
	}

	@Override
	public Optional<Envio> buscarPorId(long id) {
		String sql = "SELECT * FROM envios WHERE id=?";
		try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException ex) {
			throw new RuntimeException("Error buscando Envio por id", ex);
		}
	}

	@Override
	public Optional<Envio> buscarPorCodigoSeguimiento(String codigo) {
		String sql = "SELECT * FROM envios WHERE codigo_seguimiento=?";
		try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, codigo);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException ex) {
			throw new RuntimeException("Error buscando Envio por codigo", ex);
		}
	}

	@Override
	public List<Envio> listarTodos() {
		String sql = "SELECT * FROM envios ORDER BY id DESC";
		List<Envio> out = new ArrayList<>();
		try (Connection c = db.getConnection();
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				out.add(map(rs));
			return out;
		} catch (SQLException ex) {
			throw new RuntimeException("Error listando Envios", ex);
		}
	}

	// ----------------- helpers -----------------

	private void bindInsertOrUpdate(PreparedStatement ps, Envio e, boolean includeIdAtEnd) throws SQLException {
		int i = 1;

		ps.setString(i++, e.getCodigoSeguimiento());

		Direccion d = e.getDestinatario();
		ps.setString(i++, d != null ? d.getCalle() : null);
		ps.setString(i++, d != null ? d.getNumero() : null);
		ps.setString(i++, d != null ? d.getPiso() : null);
		ps.setString(i++, d != null ? d.getLocalidad() : null);
		ps.setString(i++, d != null ? d.getProvincia() : null);
		ps.setString(i++, d != null ? d.getComunidad() : null);

		Direccion r = e.getRemitente();
		ps.setString(i++, r != null ? r.getCalle() : null);
		ps.setString(i++, r != null ? r.getNumero() : null);
		ps.setString(i++, r != null ? r.getPiso() : null);
		ps.setString(i++, r != null ? r.getLocalidad() : null);
		ps.setString(i++, r != null ? r.getProvincia() : null);
		ps.setString(i++, r != null ? r.getComunidad() : null);

		Transporte t = e.getTransporte();
		ps.setString(i++, t != null ? t.getCodigo() : null);
		ps.setString(i++, t != null ? t.getVehiculo().name() : null);

		ps.setString(i++, e.getFechaInicio() != null ? e.getFechaInicio().toString() : LocalDate.now().toString());
		ps.setString(i++, e.getFechaFin() != null ? e.getFechaFin().toString() : null);
		ps.setString(i++, e.getEstado() != null ? e.getEstado().name() : Envio.Estado.INICIO.name());

		if (includeIdAtEnd) {
			ps.setLong(i++, e.getId());
		}
	}

	private Envio map(ResultSet rs) throws SQLException {
		// Construimos el objeto con setters para no depender del constructor de Envio
		Envio e = new Envio(rs.getString("codigo_seguimiento"),
				new Direccion(rs.getString("dest_calle"), rs.getString("dest_numero"), rs.getString("dest_localidad"),
						rs.getString("dest_provincia"), rs.getString("dest_comunidad")),
				new Direccion(rs.getString("rem_calle"), rs.getString("rem_numero"), rs.getString("rem_localidad"),
						rs.getString("rem_provincia"), rs.getString("rem_comunidad")),
				new Transporte(rs.getString("transporte_codigo")));

		e.setId(rs.getLong("id"));

		// piso (si tu Direccion lo permite con setPiso)
		if (e.getDestinatario() != null)
			e.getDestinatario().setPiso(rs.getString("dest_piso"));
		if (e.getRemitente() != null)
			e.getRemitente().setPiso(rs.getString("rem_piso"));

		// vehiculo (si Transporte tiene setVehiculo)
		if (e.getTransporte() != null) {
			String veh = rs.getString("transporte_vehiculo");
			if (veh != null)
				e.getTransporte().setVehiculo(Transporte.Vehiculo.valueOf(veh));
		}

		String fi = rs.getString("fecha_inicio");
		if (fi != null)
			e.setFechaInicio(LocalDate.parse(fi));

		String ff = rs.getString("fecha_fin");
		if (ff != null)
			e.setFechaFin(LocalDate.parse(ff));

		String est = rs.getString("estado");
		if (est != null)
			e.setEstado(Envio.Estado.valueOf(est));

		return e;
	}
}
