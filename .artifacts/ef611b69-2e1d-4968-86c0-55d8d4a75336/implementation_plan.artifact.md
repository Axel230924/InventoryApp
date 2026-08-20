# Plan de Corrección y Renombramiento de Clases

Este plan detalla los pasos para corregir los errores actuales y renombrar las actividades del proyecto para que sigan las convenciones de nomenclatura de Kotlin (PascalCase).

## Análisis de Errores

1.  **Errores de Compilación**: `moduloProducto.kt` tiene referencias no resueltas a `RecyclerView` y `LinearLayoutManager`.
2.  **Error en Tiempo de Ejecución (Crash)**: La clase `moduloProducto` intenta usar `drawerLayout` sin haberlo inicializado, y dicho elemento no existe en `activity_producto.xml`.
3.  **Convenciones de Nombres**: Las clases `moduloProducto` y `detalleProducto` deben renombrarse a `ModuloProducto` y `DetalleProducto`.

## Cambios Propuestos

### 1. Renombramiento (PascalCase)

- `moduloProducto.kt` -> `ModuloProducto.kt` (y renombrar la clase interna)
- `detalleProducto.kt` -> `DetalleProducto.kt` (y renombrar la clase interna)
- Actualizar todas las referencias en `AndroidManifest.xml`, `MainActivity.kt`, `Dashboard.kt` y los archivos XML correspondientes.

### 2. Corrección de Lógica y Dependencias

#### [MODIFY] [ModuloProducto.kt](file:///C:/UNANA-MANAGUA/A%C3%B1o%203/Semestre%202/Programaci%C3%B3n%20movil/semana%203/app%20movil/InventoryApp/app/src/main/java/com/example/inventoryapp/ModuloProducto.kt)
- Corregir los imports de `RecyclerView` y `LinearLayoutManager`.
- **Importante**: Inicializar `drawerLayout` o eliminar la lógica de menú si no es necesaria en esta pantalla. Dado que el diseño actual no tiene `DrawerLayout`, eliminaré temporalmente la llamada a `openDrawer` para evitar el crash, o añadiré el `DrawerLayout` al XML.

#### [MODIFY] [activity_producto.xml](file:///C:/UNANA-MANAGUA/A%C3%B1o%203/Semestre%202/Programaci%C3%B3n%20movil/semana%203/app%20movil/InventoryApp/app/src/main/res/layout/activity_producto.xml)
- Si el usuario desea el menú lateral en esta pantalla, se debe envolver el contenido en un `androidx.drawerlayout.widget.DrawerLayout`.

## Plan de Verificación

### Pruebas Automatizadas
- Ejecutar `gradlew assembleDebug` para verificar la compilación.

### Verificación Manual
- Navegar desde el Dashboard a "Productos" y verificar que la lista se cargue y no haya cierres inesperados al presionar botones.
