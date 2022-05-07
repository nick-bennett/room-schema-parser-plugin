/*
 *  Copyright 2022 CNM Ingenuity, Inc.
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
package edu.cnm.deepdive.room.gradle;

import edu.cnm.deepdive.room.service.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.gradle.api.Project;

public class Plugin implements org.gradle.api.Plugin<Project> {

  @Override
  public void apply(Project project) {

    Extension extension = project
        .getExtensions()
        .create("roomDdl", Extension.class);

    File source = project.file(extension.source().get());
    File destination = project.file(extension.destination().get());

    project
        .getTasks()
        .register("extractRoomDdl", (task) ->
            task.doLast((t) -> {
              try (
                  InputStream input = new FileInputStream(source);
                  PrintStream output = new PrintStream(destination)
              ) {
                Parser parser = new Parser();
                parser.parse(input, output);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
        );

  }

}
