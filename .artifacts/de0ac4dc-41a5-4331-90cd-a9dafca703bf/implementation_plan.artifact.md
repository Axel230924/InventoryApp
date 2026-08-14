# Fix Project Path ASCII Error and Build Errors

The user is encountering a Gradle sync failure due to non-ASCII characters in the project path (`Año`, `Programación`, `movil`). Additionally, there is a syntax error in the `app/build.gradle.kts` file that prevents the build from completing even after addressing the path issue.

## Proposed Changes

### Configuration

#### [MODIFY] [gradle.properties](file:///C:/UNANA-MANAGUA/Año 3/Semestre 2/Programación movil/semana 3/app movil/InventoryApp/gradle.properties)
- Ensure `android.overridePathCheck=true` is correctly set to bypass the non-ASCII path warning/error.

### Build Scripts

#### [MODIFY] [build.gradle.kts](file:///C:/UNANA-MANAGUA/Año 3/Semestre 2/Programación movil/semana 3/app movil/InventoryApp/app/build.gradle.kts)
- Fix the syntax error in `defaultConfig` where `targetSdk` and `versionCode` are on the same line without a separator.
- Correct the invalid `targetSdk` value (`376` -> `35`) and the non-standard `compileSdk` configuration.

## Verification Plan

### Automated Tests
- Run `gradle sync` to ensure the project structure is correctly recognized and the non-ASCII path issue is bypassed.
- Run `./gradlew assembleDebug` (or equivalent task) to verify the build script is syntactically correct and the project compiles.
