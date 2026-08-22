# Plan de Implementación: Borrado de Producto desde Detalle

Este plan detalla cómo implementar la funcionalidad del botón "Eliminar" exclusivamente en la pantalla de detalle del producto (`DetalleProductoActivity`), siguiendo la solicitud del usuario de manejar la lógica en esta pantalla y comentar cada paso.

## Cambios Propuestos

### [Componente: UI de Detalle]

#### [MODIFICAR] [DetalleProductoActivity.kt](file:///C:/UNANA-MANAGUA/A%C3%B1o%203/Semestre%202/Programaci%C3%B3n%20movil/semana%203/app%20movil/InventoryApp/app/src/main/java/com/example/inventoryapp/ui/productos/DetalleProductoActivity.kt)
- **Configuración del ViewModel**: Inicializar el flujo de datos necesario (Database -> Dao -> Repository -> ViewModel).
- **Vincular el Botón**: Obtener la referencia de `btnEliminar` desde el layout.
- **Evento Click**:
    - Implementar `AlertDialog` para confirmación.
    - En caso de confirmación ("Sí"):
        - Crear un objeto `Producto` con los datos actuales (especialmente el `id`).
        - Llamar a `viewModel.eliminarProducto(producto)`.
        - Mostrar un `Toast` de confirmación.
        - Ejecutar `finish()` para cerrar la pantalla y regresar a la lista.

## Plan de Verificación

### Pruebas Manuales
1.  **Confirmación**: Al presionar el botón rojo "Eliminar" en el detalle, debe aparecer el mensaje de confirmación.
2.  **Cancelación**: Al presionar "No", la pantalla debe permanecer igual.
3.  **Ejecución**: Al presionar "Sí", la app debe cerrar el detalle, volver a la lista y el producto ya no debe aparecer en ella.

---

¿Procedo con la implementación en `DetalleProductoActivity.kt`?
