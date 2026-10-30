# Controladores siguiendo el ejemplo del profesor

Se compararon ClienteControler, ClienteFormControler, HabitacionControler, HabitacionFormControler y MainControler del proyecto ProyectFinal_gp1.

## Tabla y operaciones

Los siete módulos siguen el mismo esquema:

- `initialize`: enlaza las columnas con las propiedades del modelo y llama a `cargardatos`.
- `cargardatos`: obtiene `listAll`, crea la `ObservableList` y la asigna a la tabla.
- `handleNew`: llama a `showForm(null)`.
- `handleEdit`: obtiene el seleccionado, busca sus datos completos y abre el formulario.
- `handleDelete`: pide confirmación con `Alerts`, comprueba el `Optional<ButtonType>` y llama a `delete`. Si se elimina, retira el elemento de la lista, como el profesor. Si tiene dependencias, muestra el motivo sin borrar datos relacionados.
- `showForm`: carga el FXML, entrega el modelo al formulario, abre el diálogo y espera. Si `isSaved()` es verdadero, obtiene los datos con `getCliente`, `getCita`, etc. Usa un `if` para insertar cuando el modelo original es null y otro caso para actualizar. Finalmente recarga la tabla.

Cliente conserva su método `agregar`; los otros módulos utilizan `insert`. Los métodos SQL y las interfaces no se cambiaron en esta adaptación.

## Formulario

`setCliente`, `setCita`, etc. llenan los campos al editar. `validated()` comprueba los campos requeridos y devuelve true o false. `handleSaved()` llama a esa validación; si pasa, crea el modelo solo si es nuevo, copia los campos, pone `saved = true` y llama a `cerrar()`. Cancelar solo llama a `cerrar()`.

Los modelos conservan las iniciales solicitadas: c, v, m, t y u. Los combos de relaciones seleccionan el registro por su ID, porque la veterinaria tiene relaciones entre clientes, mascotas, citas, consultas y tratamientos.

## Ventanas y alertas

Se usa `APPLICATION_MODAL`, igual que el profesor: mientras se edita un formulario, hay que guardarlo o cancelarlo antes de volver a las demás ventanas de la aplicación. `initOwner` indica a qué ventana pertenece. `showAndWait()` espera el cierre antes de consultar `isSaved()`.

Se conserva la clase `Alerts` del profesor. El método corto `mostrarMensaje` escribe el texto en `lblMensaje`, crea el aviso con `Alerts.viewAlerts` y lo muestra. No utiliza llamadas diferidas ni recursión. Las confirmaciones de eliminar permiten Aceptar o Cancelar; los avisos informativos solo necesitan Aceptar.

## Buscar y Mostrar todos

Estos son los métodos adicionales al ejemplo: `handleSearch` usa la consulta disponible en el Impl y muestra el resultado. Cliente admite ID o DNI; los demás módulos buscan por ID. `handleListAll` limpia la búsqueda y vuelve a llamar a `cargardatos`.

## Login y menú

Los botones del menú enlazan desde FXML a métodos `showCliente`, `showMascota`, etc., como el profesor. Cada método carga su vista dentro del contenedor central.

Al entrar o cerrar sesión se usa `stage.getScene().setRoot(...)`: se cambia el contenido de la escena existente. Ya no se crea otra escena con medidas fijas ni se vuelve a centrar la ventana. Por eso conserva tamaño, posición y estado maximizado. Se mantienen admin/admin123 y el acceso con usuarios registrados.

## Comprobación

Las pruebas usan una base temporal. Cubren las siete operaciones CRUD, búsquedas, validaciones, cancelar, registros duplicados, relaciones que impiden eliminar, login y cierre de sesión. Se añadió una comprobación de tamaño, posición y maximización al entrar y salir.

Resultado: `mvnw.cmd -B clean verify` completado; 13 pruebas correctas.
