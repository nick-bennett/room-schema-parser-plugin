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
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

/**
 * Implements a Gradle task capable of extracting and replacing placeholders in DDL statements
 * embedded in the JSON database schema files created by the Room ORM, and writing the results to a
 * SQL script file.
 * <p>If the {@code RoomDatabase} subclass in an Android project that uses Room is configured to
 * write a schema file (the default behavior), and a schema file location is specified in the
 * {@code app}-level {@code build.gradle} file, a schema file will be written to the specific
 * location when the database class is implemented by the Room annotation processor. In that file,
 * the database schema is represented in a JSON object, structured as shown in {@link Parser}. This
 * task reads the {@code createSql} property values from the database schema file (the location of
 * which must be specified in a {@code source} configuration property, and which is returned by
 * {@link #getSource()}), replaces the {@code ${TABLE_NAME}} and {@code ${VIEW_NAME}}
 * placeholders, and writes properly terminated DDL SQL statements to the output file specified in
 * the {@code destination} property (which defaults to the value of
 * {@link Extension#DEFAULT_DESTINATION}), and returned from {@link #getDestination()}.</p>
 */
public abstract class Task extends DefaultTask {

  /**
   * Returns the value of the <em>required</em> {@code source} property.
   */
  @InputFile
  public abstract RegularFileProperty getSource();

  /**
   * Returns the value of the {@code destination} property.
   */
  @OutputFile
  public abstract RegularFileProperty getDestination();

  /**
   * Parses the contents of the file referenced by the {@link #getSource()} return value, replaces
   * the embedded placeholders with the appropriate table and view names, terminates each statement
   * with the semicolon (`;`) character and two line breaks, and writes the result to the location
   * referenced by {@link #getDestination()}.
   */
  @TaskAction
  public void extract() {
    Project project = getProject();
    File source = project.file(getSource());
    File destination = project.file(getDestination());
    try (
        InputStream input = new FileInputStream(source);
        PrintStream output = prepareDestination(destination)
    ) {
      Parser parser = new Parser();
      parser.parse(input, output);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
