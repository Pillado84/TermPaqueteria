package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite {
	private final String url;

	// Ejemplo URL: "jdbc:sqlite:paqueteria.db"
	public SQLite(String url) {
		this.url = url;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}

	public void initSchema() {
		String sql = """
				  CREATE TABLE IF NOT EXISTS envios (
				    id INTEGER PRIMARY KEY AUTOINCREMENT,
				    codigo_seguimiento TEXT NOT NULL UNIQUE,

				    dest_calle TEXT,
				    dest_numero TEXT,
				    dest_piso TEXT,
				    dest_localidad TEXT,
				    dest_provincia TEXT,
				    dest_comunidad TEXT,

				    rem_calle TEXT,
				    rem_numero TEXT,
				    rem_piso TEXT,
				    rem_localidad TEXT,
				    rem_provincia TEXT,
				    rem_comunidad TEXT,

				    transporte_codigo TEXT,
				    transporte_vehiculo TEXT,

				    fecha_inicio TEXT NOT NULL,
				    fecha_fin TEXT,
				    estado TEXT NOT NULL
				  );
				""";

		try (Connection c = getConnection(); Statement st = c.createStatement()) {
			st.execute(sql);
		} catch (SQLException e) {
			throw new RuntimeException("Error creando schema SQLite", e);
		}
	}
}
