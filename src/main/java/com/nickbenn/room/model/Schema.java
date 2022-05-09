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
package com.nickbenn.room.model;

import com.google.gson.annotations.Expose;
import com.nickbenn.room.service.Parser;
import java.util.stream.Stream;

/**
 * Represents the top-level database schema, as declared via Room {@code @Entity}-,
 * {@code @DatabaseView}-, and {@code @Database}-annotated classes, and as emitted by the Room
 * annotation processor in a JSON schema file. The structure of this class maps to the top-level
 * object shown in the documentation of the {@link Parser} class.
 */
@SuppressWarnings({"unused", "JavadocDeclaration"})
public class Schema implements Streamable {

  @Expose
  private Database database;

  /**
   * Returns the single {@link Database} instance of the schema, mapping to the {@code database}
   * element of the JSON schema file.
   */
  public Database getDatabase() {
    return database;
  }

  /**
   * Sets the value of the {@link Database} instance, as read from the JSON schema file.
   *
   * @param database
   */
  public void setDatabase(Database database) {
    this.database = database;
  }

  /**
   * Returns the DDL content returned from an invocation of
   * {@link Database#stream() getDatabase().stream()}.
   */
  @Override
  public Stream<String> stream() {
    return database.stream();
  }

}
