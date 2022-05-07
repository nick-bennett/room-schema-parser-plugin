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
package edu.cnm.deepdive.room.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.stream.Stream;

/**
 * Represents a view in a Room SQLite database schema. This will generally be read from one of the
 * elements of the {@code database.views} array property in a JSON file with the structure described
 * in {@link edu.cnm.deepdive.room.service.Parser}.
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public class View implements Streamable {

  private static final String PLACEHOLDER = "${VIEW_NAME}";

  @Expose
  @SerializedName("viewName")
  private String name;

  @Expose
  @SerializedName("createSql")
  private String ddl;

  /**
   * Returns the name of the Room view (also the SQLite view name) that this instance represents.
   * This value will generally be read from a {@code database.views[].viewName} property in a
   * Room-generated JSON schema file.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the Room view (also the SQLite view name) that this instance represents.
   *
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the {@code CREATE VIEW} SQL statement used to create the SQLite view represented by
   * this instance. Note that the return value of this method includes a {@code ${VIEW_NAME}}
   * placeholder; the {@link #stream()} method replaces the placeholder with the value returned by
   * {@link #getName()}.
   */
  public String getDdl() {
    return ddl;
  }

  /**
   * Sets the {@code CREATE VIEW} SQL statement used to create the SQLite table represented by this
   * instance. If {@code ddl} contains any {@code ${VIEW_NAME}} placeholders, they'll be included
   * in the value returned by {@link #getDdl()}, but not by that returned by {@link #stream()}.
   *
   * @param ddl
   */
  public void setDdl(String ddl) {
    this.ddl = ddl;
  }

  /**
   * Returns a {@link Stream} containing the DDL statement capable of creating the SQLite view
   * corresponding to this instance. As part of this process, all {@code ${VIEW_NAME}} placeholders
   * in the DDL are replaced by the value returned by {@link #getName()}.
   */
  @Override
  public Stream<String> stream() {
    return Stream
        .of(ddl)
        .map((sql) -> sql.replace(PLACEHOLDER, name));
  }

}
