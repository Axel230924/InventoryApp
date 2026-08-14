# Plan de Renombramiento de Clases

Este plan detalla los pasos para renombrar las actividades del proyecto para que sigan las convenciones de nomenclatura de Kotlin (PascalCase), comenzando con una letra mayúscula.

## Cambios Propuestos

### Actividades Core

Renombraremos las siguientes clases y sus archivos correspondientes:
1. `moduloProducto` -> `ModuloProducto`
2. `detalleProducto` -> `DetalleProducto`

#### [Renombrar Archivos]
- `moduloProducto.kt` -> `ModuloProducto.kt`
- `detalleProducto.kt` -> `DetalleProducto.kt`

#### [Modificar Código Kotlin]
- Actualizar la declaración de la clase en [ModuloProducto.kt](file:///C:/UNANA-MANAGUA/A%C3%B1o%203/Semestre%202/Programaci%C3%B3n%20movil/semana%203/app%20movil/InventoryApp/app/src/main/java/com/example/inventoryapp/moduloProducto.kt).
- Actualizar la declaración de la clase en [DetalleProducto.kt](file:///C:/UNANA-MANAGUA/A%C3%B1o%203/Semestre%202/Programaci%C3%B3n%20movil/semana%203/app%20movil/InventoryApp/app/src/main/java/com/example/inventoryapp/detalleProducto.kt).
- Actualizar las referencias de Intent en `MainActivity.kt` (apuntando a `ModuloProducto`).
- Actualizar las referencias de Intent en `ModuloProducto.kt` (apuntando a `DetalleProducto`).

#### [Modificar Manifiesto y Layouts]
- Actualizar las entradas `<activity>` en [AndroidManifest.xml](file:///C:/UNANA-MANAGUA/A%C3%B1o%203/Semestre%202/Programaci%C3%B3n%20movil/semana%203/app%20movil/InventoryApp/app/src/main/AndroidManifest.xml).
- Actualizar el atributo `tools:context` en [activity_producto.xml](file:///C:/UNANA-MANAGUA/A%C3%B1o%203/Semestre%202/Programaci%C3%B3n%20movil/semana%203/app%20movil/InventoryApp/app/src/main/res/layout/activity_producto.xml).
- Actualizar el atributo `tools:context` en [activity_detalle_producto.xml](file:///C:/UNANA-MANAGUA/A%C3%B1o%203/Semestre%202/Programaci%C3%B3n%20movil/semana%203/app%20movil/InventoryApp/app/src/main/res/layout/activity_detalle_producto.xml).

## Plan de Verificación

### Pruebas Automatizadas
- Ejecutar `gradlew assembleDebug` para verificar que el proyecto compila correctamente con los nuevos nombres.

### Verificación Manual
- Abrir la aplicación y verificar que la navegación entre `MainActivity` -> `ModuloProducto` -> `DetalleProducto` funciona correctamente.
