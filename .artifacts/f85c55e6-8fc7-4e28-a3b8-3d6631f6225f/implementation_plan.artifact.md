# Reparar error de navegación en Reportes

El usuario reporta que al hacer clic en el CardView de `Reportes.kt`, la aplicación se cierra (pantalla blanca y regresa al inicio). Esto se debe a un error en `activity_motorep_reportes.kt` donde se intenta acceder a una vista con ID `main` que no existe en su respectivo layout.

## User Review Required

> [!IMPORTANT]
> Se identificó que `activity_motorep_reportes.kt` intenta usar `findViewById(R.id.main)`, pero en `activity_motorep_reportes.xml` el ID de la vista raíz es `DrawerLayout`. Esto causa un `NullPointerException` al iniciar la actividad.

## Proposed Changes

### [app]

#### [MODIFY] [activity_motorep_reportes.kt](file:///C:/Users/elkin/OneDrive/Desktop/InventoryApp/app/src/main/java/com/example/inventoryapp/activity_motorep_reportes.kt)
- Cambiar `R.id.main` por `R.id.DrawerLayout` para que coincida con el ID definido en el archivo XML.

## Verification Plan

### Manual Verification
- Compilar y ejecutar la aplicación.
- Navegar a la pantalla de Reportes.
- Hacer clic en "MotoRep Enmanuel".
- Verificar que la pantalla `activity_motorep_reportes` se abra correctamente sin cerrarse.
