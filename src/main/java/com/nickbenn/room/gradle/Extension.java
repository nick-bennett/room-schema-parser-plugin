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

import javax.inject.Inject;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;

/**
 * Provides configuration properties for the {@code extractRoomDdl} Gradle task. Currently, these
 * properties are minimal, supporting only the specification of {@code source} and
 * {@code destination} properties; these are set in the {@code roomDdl} section of
 * {@code build.gradle}.
 */
@SuppressWarnings("JavadocDeclaration")
public class Extension {

  /** Default output file path, relative to the consumer project's {@code build.gradle} location. */
  public static final String DEFAULT_DESTINATION = "build/ddl/ddl.sql";

  private final Project project;
  private final RegularFileProperty source;
  private final RegularFileProperty destination;

  /**
   * Initializes this extension instance. This is only invoked by Gradle itself, not by the plugin
   * or its consumers.
   *
   * @param project {@link Project} instance injected by Gradle.
   */
  @Inject
  public Extension(Project project) {
    this.project = project;
    ObjectFactory factory = project.getObjects();
    source = factory.fileProperty();
    destination = factory.fileProperty();
    destination
        .convention(
            project
                .getLayout()
                .getProjectDirectory()
                .file(DEFAULT_DESTINATION)
        );
  }

  /**
   * Returns the value of the <em>required</em> {@code source} property, as set in the
   * {@code roomDdl} section of {@code build.gradle}.
   */
  public RegularFileProperty getSource() {
    return source;
  }

  /**
   * Sets the {@code source} property to the value returned from
   * {@link Project#file(Object) project.file(sourceStr)}.
   *
   * @param sourceStr
   */
  public void setSource(String sourceStr) {
    this.source.set(project.file(sourceStr));
  }

  /**
   * Returns the value of the {@code destination} property, as set in the {@code roomDdl} section of
   * {@code build.gradle}. If not set, then the default value is read from
   * {@link #DEFAULT_DESTINATION}.
   */
  public RegularFileProperty getDestination() {
    return destination;
  }

  /**
   * Sets the {@code destination} property to the value returned from
   * {@link Project#file(Object) project.file(destinationStr)}.
   *
   * @param destinationStr
   */
  public void setDestination(String destinationStr) {
    this.destination.set(project.file(destinationStr));
  }

}
