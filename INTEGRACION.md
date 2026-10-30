# Integración del sistema de veterinaria

## Avisos de versión y eliminación — 29/10/2026

Se corrigieron las cuatro vistas que declaraban JavaFX 26 para usar el namespace JavaFX 21 del proyecto. ConexionBD ya no imprime un mensaje por cada reapertura. Las pruebas de conexión siguen disponibles en Test.

Delete conserva boolean para indicar una fila eliminada o un ID inexistente. Los errores SQL se comunican al controlador con IllegalStateException. Si SQLite informa FOREIGN KEY constraint failed, se muestra la relación concreta que impide eliminar (mascotas, citas, consultas o tratamientos). No se desactivan las claves foráneas ni se borran datos en cascada. Para conservar historial, Cliente, Veterinario y Mascota pueden marcarse Inactivo desde Editar.

Verificación: clean verify, 12 pruebas correctas y sin avisos de versión FXML. Base original sin cambios.

## Estado actual — 29/10/2026

Esta actualización reemplaza las limitaciones indicadas en los apartados históricos siguientes. Los siete CRUD muestran Alerts del profesor para resultados, advertencias y errores. El login admite admin/admin123 y los usuarios registrados; el usuario conectado aparece en la cabecera. admin queda reservado para la demostración. Los errores SQL de lectura se comunican al controlador en lugar de simular una lista vacía.

Verificación: mvnw.cmd verify, 12 pruebas correctas. Se comprobaron también duplicados, cancelación de borrado, claves foráneas, cambio de contraseña y alertas visibles. La base de trabajo y ConexionBD no se modificaron. La explicación del código está en GUIA_CONTROLADORES.md.

## Prueba funcional con login (28/10/2026)

Este apartado reemplaza las limitaciones de controladores descritas en el historial inferior.

- Ejecutar `ejecutar.cmd` desde la raíz del proyecto o iniciar `VeterinariaApplication` desde el IDE. El lanzador usa JAVA_HOME y, si no está definido, el JDK 21 instalado en esta computadora.
- Acceso de demostración: usuario `admin`, contraseña `admin123`. Se comprueba en LoginControler y no depende de la tabla usuario. Cerrar sesión devuelve al login y limpia las credenciales.
- Los siete módulos tienen tabla, Nuevo, Editar, Eliminar con confirmación, Buscar y Mostrar todos. Los formularios validan campos obligatorios, DNI, hora y duración. Usuario ahora tiene modelo, interfaz e implementación sobre su tabla existente.
- Los controladores siguen el flujo del profesor: abrir un formulario modal, setEntidad, isSaved/getEntidad y llamada al DAO para insertar o actualizar. Alerts se conserva tal como fue copiado del profesor.
- Para probar desde cero, crear Cliente y Veterinario, luego Mascota, Cita, Consulta y Tratamiento. Seleccionar las relaciones en los ComboBox. La eliminación respeta las claves foráneas y no borra dependientes automáticamente.
- Las operaciones hechas desde la aplicación guardan en la base configurada (por defecto C:/sqlite/db/veterinaria.db). Las pruebas automatizadas usan una base temporal con el mismo esquema.
- Verificación: ocho pruebas de integración, incluyendo login/cierre de sesión y CRUD completo desde los botones de las siete vistas. No se insertaron registros de demostración en la base original.

## Actualización del CRUD (28/10/2026)

Esta sección describe el estado actual; las secciones siguientes conservan el historial de integración.

- Cliente, Veterinario, Mascota, Cita, Consulta y Tratamiento usan SQL con el patrón del profesor: PreparedStatement, boolean y List. Cliente conserva el nombre agregar; los demás usan insert. Todos tienen update, delete y listAll.
- searchById está implementado en los seis DAO; Cliente también tiene searchByDni. Las vistas incluyen Buscar y Mostrar todos (listAll). Cliente permite elegir ID o DNI; las otras cinco vistas buscan por el ID de su entidad. Los eventos de los controladores siguen pendientes.
- Los listados tienen Nuevo arriba y Editar/Eliminar debajo de la tabla. Cita no tiene acciones de atender ni cancelar cita. El estado sigue siendo un campo del formulario y se guarda con update; no se agrega updateStatus ni validación de horarios.
- No se implementaron controladores CRUD ni eventos de formularios. Los botones están preparados visualmente para la siguiente etapa de aprendizaje. MainControler solo perdió la referencia al módulo retirado.
- Se retiraron Vacunación, los menús Index de consola y las clases Lectura. Se conserva VeterinariaApplication y utils/ConexionBD.java. La base de datos original y su esquema se mantienen.
- utils/Alerts.java contiene la clase del profesor cambiando únicamente el paquete. Devuelve la alerta para que el futuro controlador invoque showAndWait().
- TestCliente, TestVeterinario, TestMascota, TestCita, TestConsulta y TestTratamiento son pruebas manuales con insertar, update, delete y listar. Ejecutar su main desde el IDE: inicialmente solo listan. Para probar escritura, ajustar los datos e IDs y habilitar una llamada. Las relaciones necesitan IDs existentes; consulta admite una sola fila por cita.
- Un delete de un registro con dependientes devuelve false y muestra el error de clave foránea. No se eliminan automáticamente mascotas, citas, consultas o tratamientos relacionados.
- Usuario conserva únicamente su vista y controlador vacío; no se añadió un modelo o DAO nuevo para ese módulo.

Verificación de esta actualización: Maven test pasó con 6 pruebas, 0 fallos y 0 errores. Después de restaurar las búsquedas, se verificaron también 103 comprobaciones de CRUD, búsquedas y restricciones sobre una base temporal con el esquema real, y se revisaron las vistas renderizadas. El archivo veterinaria.db y ConexionBD.java se comprobaron sin cambios.

## Ramas y alcance

Trabajo realizado únicamente en `integracion`. Se completó el merge pendiente de
`origin/modelo-property` (`ac680c2`). `conexionBd` (`8058120`) y
`creacionDeVistas` (`c7da558`) ya eran antecesores de `integracion`, al igual que
`master` (`7aa1091`): no se repitieron fusiones. La rama `Test` (`c8b7152`) está
excluida. No se modificaron las ramas originales ni se realizó push.
El archivo `Test/conexionTest.java` que ya existía pertenece a `conexionBd`,
no se importó de la rama excluida.

## Resolución de los conflictos

- `Persona`: se conservaron IntegerProperty/StringProperty, los accesores de
  propiedades y getters/setters escalares. Se mantuvieron constructor vacío y
  mostrarInfo. Se añadió una sobrecarga del constructor y de setTelefono para
  llamadas antiguas con int, convirtiendo a String sin duplicar almacenamiento.
- `Cliente`: direccion y estado son StringProperty. Se conservaron constructor
  vacío, constructor con datos y mostrarInfo; se admite teléfono String o int.
- `Veterinario`: se conserva la versión con propiedades y también su constructor
  anterior con teléfono int. getTelefono ahora devuelve String; los consumidores
  existentes son compatibles. Usar String permite conservar ceros y prefijos.
- Se incorporaron los modelos Cita (fecha LocalDate), Consulta, Mascota,
  Tratamiento y las adaptaciones de CitaLectura, ClienteLectura y
  VeterinarioLectura aportadas por modelo-property.

## Correcciones de integración

- module-info.java permite cargar controladores con FXMLLoader y consultar
  propiedades de modelos mediante javafx.base; declara el módulo SQLite.
- VeterinariaApplication inicia la ventana principal; MainControler conecta los
  nueve botones del menú con sus FXML. Index e IndexPrincipal conservan el menú
  anterior de consola y se pueden ejecutar desde IntelliJ.
- Se conserva hello-view.fxml y se añade el HelloController que le faltaba.
- login-view.fxml utiliza la versión JavaFX 21 del proyecto, en lugar de 26.
- ConexionBD admite la propiedad JVM veterinaria.db.url y conserva la causa de
  las excepciones. Sin configuración adicional usa la ruta original
  jdbc:sqlite:C:/sqlite/db/veterinaria.db. No se modificó esa base real.
- pom.xml inicia VeterinariaApplication y configura Surefire para ejecutar JUnit 5.

## Verificación

Con JDK 21, desde la raíz:

```powershell
.\mvnw.cmd verify
.\mvnw.cmd javafx:run
```

En IntelliJ también se puede ejecutar directamente
pe.edu.upeu.entregable1.viewcontrol.VeterinariaApplication.

Se añadieron pruebas propias de integración (no proceden de la rama Test):

- ModelIntegrationTest: constructores antiguos, propiedades observables,
  relaciones, LocalDate y CRUD de consola con los nuevos modelos.
- DatabaseIntegrationTest: SQLite en memoria, claves foráneas, reutilización y
  reapertura de conexión. No escribe sobre datos de trabajo.
- JavaFxIntegrationTest: carga de todos los FXML e imágenes, navegación y
  apertura/cierre de una ventana real. Requiere un entorno gráfico.

Resultado: mvnw verify correcto, 7 pruebas, 0 fallos y 0 errores. JAR generado
como target/entregable1-1.0-SNAPSHOT.jar (ejecutar mediante Maven con sus dependencias).
Advertencia no bloqueante del compilador: el nombre del módulo termina en un dígito.

## Trabajo que las ramas aún no implementan

El proyecto integrado compila, inicia JavaFX y permite navegar. No representa
un CRUD SQL terminado: las implementaciones conservadas usan ArrayList y los
controladores de los módulos/formularios continúan vacíos. Los botones de
registro, búsqueda, edición, eliminación y apertura de formularios aún requieren
su lógica. Tampoco hay autenticación implementada ni acción de cerrar sesión;
el arranque gráfico abre directamente main-view para revisar la integración.
Las vistas de usuario y vacunación están conservadas, pero esos modelos/DAO
no fueron aportados en las ramas incluidas. No se creó una base ni se inventaron
credenciales o datos para simular funcionalidades ausentes.

## Archivos incorporados o ajustados respecto al integracion inicial

- pom.xml; src/main/java/module-info.java.
- modelo/Persona.java, Cliente.java, Veterinario.java, Cita.java, Consulta.java,
  Mascota.java y Tratamiento.java.
- utils/CitaLectura.java, ClienteLectura.java, VeterinarioLectura.java y ConexionBD.java.
- Controladores/MainControler.java.
- viewcontrol/VeterinariaApplication.java y HelloController.java (nuevos).
- VIews/login-view.fxml (namespace).
- src/test/java/pe/edu/upeu/entregable1: los tres tests descritos arriba.
- Este documento INTEGRACION.md.
