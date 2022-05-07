# Room Schema Parser Plugin

This project implements a Gradle plugin that extracts DDL from an Android Room SQLite database schema file.

## Usage

### Include plugin in `build.gradle`

Reference the plugin in the `dependencies` task of the `buildscript`, in the project-level `build.grade` file of an Android project, e.g.

```groovy
buildscript {
    // ...
    dependencies {
        // ... 
        classpath 'edu.cnm.deepdive:room-schema-parser-plugin:1.0.1'
    }
}
```

### Configure the plugin

In the `app`-module level `build.gradle` file, include a `roomDdl` closure with `source` and `destination` properties:

`source`

: String specifying the path to the schema file generated by Room. Typically, the base directory for this is the same as that specified as the value corresponding to the `room.schemaLocation` key of the `arguments` property in `android.defaultConfig.javaCompileOptions.annotationProcessorOptions`, e.g.

    ```groovy
    'room.schemaLocation'  : "$projectDir/schemas".toString(),
    ```
    
    Within the `schemas` directory, the Room annotation processor creates (when building) a directory named with the fully qualified class name of the database class; inside that directory, room creates a JSON file, named according to the version number of the database. The value specified for `source` must refer to that file.
    
`destination`
: String specifying the path to the output file for the extracted DDL. Defaults to `"$projectDir/build/ddl/ddl.sql"`. Any directories included (implicitly or explicitly) will be created, if necessary; if such a directory cannot be created, or if the file itself cannot be written to, the task will fail with an exception. 

For example:

```groovy
roomDdl {
    source "$projectDir/schemas/fully.qualified.database.class.name/1.json"
    destination "$projectDir/../docs/sql/ddl.sql"
}
```

### Execute the task

After loading any Gradle build script changes, the `extractRoomDdl` will be available for execution&mdash;eithdr from the Gradle tool window of Android Studio or IntelliJ IDEA, or from from the command line:

```bash
./gradlew extractRoomDdl
```

## Credits, copyrights, and license information

Written by Nicholas Bennett. 

&copy; 2022 CNM Ingenuity, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
