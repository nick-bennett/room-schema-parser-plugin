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

/**
 * Provides configuration properties for {@link Plugin}, and consequently for the `extractRoomDdl`
 * Gradle task. Currently, these properties are minimal, supporting only the specification of
 * {@code source} and {@code destination} properties; these are set in the {@code roomDdl} section
 * of {@code build.gradle}.
 *
 * @see Plugin#TASK_NAME
 * @see Plugin#CONFIGURATION_CLOSURE
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public class Extension {

  /** Default output file path, relative to the consumer project's {@code build.gradle} location. */
  public static final String DEFAULT_DESTINATION = "build/ddl/ddl.sql";

  private String source;
  private String destination = DEFAULT_DESTINATION;

  /**
   * Returns the value of the <em>required</em> {@code source} property, as set in the
   * {@code roomDdl} section of {@code build.gradle}.
   */
  public String getSource() {
    return source;
  }

  /**
   * Sets the value of the {@code source} property. This will be interpreted relative to the
   * directory where the consuming {@code build.gradle} is located. If no file exists at the
   * specified location, execution of the {@code extractRoomDdl} task will fail.
   *
   * @param source
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Returns the value of the {@code destination} property, as set in the {@code roomDdl} section of
   * {@code build.gradle}. If not set, the value of {@link #DEFAULT_DESTINATION} is used.
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Sets the value of the {@code destination} property. This will be interpreted relative to the
   * directory where the consuming {@code build.gradle} is located. If this value includes any
   * directories that don't exist, the {@code extractRoomDdl} task will attempt to create them.
   *
   * @param destination
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

}
