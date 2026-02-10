# 📦 TermPaqueteria

Proyecto Java de **gestión de envíos de paquetería** con persistencia en **SQLite** usando JDBC. El sistema permite crear, almacenar, consultar, actualizar y eliminar envíos, garantizando la **unicidad del código de seguimiento** y respetando reglas básicas de negocio.

---

## 🚀 Características principales

* Gestión completa de envíos (**CRUD**)
* Persistencia en **SQLite** (base de datos por fichero)
* Generación automática de **códigos de seguimiento únicos** con patrón:

  ```
  ENV-XXXXXXXXXXXX
  ```

  (12 caracteres alfanuméricos, 6 letras + 6 números)
* Uso de **DAO + Repository pattern**
* Modelo de dominio claro (`Envio`, `Direccion`, `Transporte`)
* Desarrollado con Java 25.
* Compatible con **Java 17 / Java 21+**

---

## 🧱 Estructura del proyecto

```
TermPaqueteria/
├── src/
│   ├── app/
│   │   └── Main.java
│   ├── dao/
│   │   ├── DaoEnvioSqlite.java
│   │   └── EnvioRepository.java
│   ├── db/
│   │   └── SQLite.java
│   ├── bibliotecas/
│   │   └── GeneradorCodigoSeguimiento.java
│   └── oop/
│       ├── Envio.java
│       ├── Direccion.java
│       └── Transporte.java
├── lib/
│   └── sqlite-jdbc-3.51.1.0.jar
└── README.md
```

---

## 🧩 Modelo de dominio

### ✉️ Envio

* Código de seguimiento (**único**)
* Dirección de destinatario
* Dirección de remitente
* Transporte asignado
* Fecha de inicio (automática)
* Fecha de fin (cuando se entrega)
* Estado (`INICIO`, `TRANSITO`, `FIN`, etc.)

### 🚚 Transporte

* Código identificador
* Tipo de vehículo (`CAMION`, `FURGONETA`, `COCHE`)

### 🏠 Direccion

* Calle, número, piso
* Localidad, provincia, comunidad

---

## 🗄️ Base de datos

* **SQLite** mediante JDBC
* El fichero `.db` se crea automáticamente en el *working directory*
* Tabla principal: `envios`

### Esquema SQL simplificado

```sql
CREATE TABLE envios (
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
```

---

## 🔐 Generación de códigos de seguimiento

El proyecto incluye un generador que:

* Añade el prefijo fijo `ENV-`
* Genera 12 caracteres alfanuméricos
* Garantiza **no colisionar** con códigos existentes en la BD

Ejemplo:

```
ENV-A7M9Q2K8ZP4H
```

Clase:

```java
bibliotecas.GeneradorCodigoSeguimiento
```

---

## ▶️ Ejecución del proyecto

### Requisitos

* **Java 17** o **Java 21+**
* Eclipse / IntelliJ / VS Code
* Driver SQLite JDBC incluido en `/lib`

### Ejecución

1. Importar el proyecto como **Java Project**
2. Asegurarse de que `sqlite-jdbc-*.jar` está en el *Build Path*
3. Ejecutar `app.Main`

---

## ⚠️ Advertencia Java 21+

Si usas Java 21 o superior, añade este argumento VM para evitar warnings:

```
--enable-native-access=org.xerial.sqlitejdbc
```

En Eclipse:

```
Run Configurations → Arguments → VM Arguments
```

---

## 🧪 Ejemplo de uso (Main)

El `Main` incluye un **menú por consola** con varias opciones para interactuar con el sistema. Desde ese menú puedes **introducir datos por teclado** (por ejemplo, crear un envío, listar, buscar, actualizar estado/finalizar y borrar).

A modo orientativo, el flujo típico es:

1. **Crear envío** → el programa solicita los datos (destinatario, remitente, transporte, etc.) y genera un **código de seguimiento único** con el patrón `ENV-...`.
2. **Buscar envío por seguimiento** → consulta un envío concreto usando su `codigo_seguimiento`.
3. **Listar todos los envíos** → muestra el histórico completo de envíos almacenados.
4. **Actualizar estado de un envío** → permite cambiar el estado (por ejemplo, a `TRANSITO` o `FIN`) y, en caso de finalización, asigna la `fechaFin`.

> ⚠️ **No existe opción de eliminar envíos**: el sistema conserva el **histórico completo** para trazabilidad y control.

> Nota: la lógica de persistencia se implementa en `DaoEnvioSqlite` (SQLite/JDBC) y la generación de códigos en `GeneradorCodigoSeguimiento`.

---

## 📌 Buenas prácticas aplicadas

* Separación **dominio / persistencia / aplicación**
* Validación por base de datos (`UNIQUE`)
* Código defensivo en DAO
* Uso de `Optional` para búsquedas

---

## 🛠️ Posibles mejoras futuras

* Tests automáticos con SQLite en memoria
* Normalización de tablas (`direccion`, `transporte`)
* Capa `Service` para reglas de negocio
* Interfaz gráfica o API REST

---

## 👤 Autor

Proyecto académico / práctico desarrollado en Java para aprendizaje de:

* JDBC
* SQLite
* Diseño por capas

---

¡Cualquier mejora o sugerencia es bienvenida! 🚀