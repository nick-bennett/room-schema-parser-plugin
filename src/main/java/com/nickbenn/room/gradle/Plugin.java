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

import org.gradle.api.Project;

/**
 * Implements a simple DDL parser/extractor Gradle plugin, intended for use in Android projects that
 * use the Room ORM to define (or map to) and access a local SQLite database. The plugin defines a
 * single task ({@code extractRoomDdl}), implemented (mostly) in {@link Task}, and a configuration
 * section ({@code extractDdl}), implemented in {@link Extension}.
 */
public class Plugin implements org.gradle.api.Plugin<Project> {

  /** Name of the Gradle task added by this plugin to the consumer project. */
  public static final String TASK_NAME = "extractRoomDdl";

  /** Name of the configuration section where the task properties can be set. */
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
        .register(TASK_NAME, Task.class, (task) -> {
          task
              .getSource()
              .set(extension.getSource());
          task
              .getDestination()
              .set(extension.getDestination());
        });
  }

}
