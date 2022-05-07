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

import org.gradle.api.provider.Property;

public abstract class Extension {

  public static final String DEFAULT_DESTINATION = "build/ddl/ddl.sql";

  public Extension() {
    destination().convention(DEFAULT_DESTINATION);
  }

  abstract Property<String> source();
  abstract Property<String> destination();

}
