/*
 *  Copyright 2022 Nicholas Bennett.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.nickbenn.room.gradle;

import com.nickbenn.room.service.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.gradle.api.Project;

/**
 * Implements a simple DDL parser/extractor Gradle plugin, intended for use in Android projects that
 * use the Room ORM to define (or map to) and access a local SQLite database. If the
 * {@code RoomDatabase} subclass in such project is configured to write a schema file (the default
 * behavior), and a schema file location is specified in the {@code app}-level {@code build.gradle}
 * file, a schema file will be written to the specific location when the database class is
 * implemented by the Room annotation processor. In that file, the schema is represented in a JSON
 * object, structured as shown in {@link Parser}.
 */
public class Plugin implements org.gradle.api.Plugin<Project> {

  public static final String TASK_NAME = "extractRoomDdl";
  public static final String CONFIGURATION_CLOSURE = "roomDdl";

  /**
   * Initializes this plugin instance. Currently, this plugin has no mutable state, nor any
   * immutable state set on initialization, so this constructor does nothing; further, it is invoked
   * only by Gradle itself.
   */
  public Plugin() {
  }

  @Override
  public void apply(Project project) {

    Extension extension = project
        .getExtensions()
        .create(CONFIGURATION_CLOSURE, Extension.class);

    project
        .getTasks()
        .register(TASK_NAME, (task) ->
            task.doLast((t) -> {
              File source = project.file(extension.getSource());
              File destination = project.file(extension.getDestination());
              try (
                  InputStream input = new FileInputStream(source);
                  PrintStream output = prepareDestination(destination)
              ) {
                Parser parser = new Parser();
                parser.parse(input, output);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
        );

  }

  private PrintStream prepareDestination(File destination) throws IOException {
    //noinspection ResultOfMethodCallIgnored
    destination
        .getCanonicalFile()
        .getParentFile()
        .mkdirs();
    return new PrintStream(destination);
  }

}
