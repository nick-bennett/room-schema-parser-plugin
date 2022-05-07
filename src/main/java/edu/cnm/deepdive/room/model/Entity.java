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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents an entity in a Room SQLite database schema. This will generally be read from one of
 * the elements of the {@code database.entities} array property in a JSON file with the structure
 * described in {@link edu.cnm.deepdive.room.service.Parser}.
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public class Entity implements Streamable {

  private static final String PLACEHOLDER = "${TABLE_NAME}";

  @Expose
  @SerializedName("tableName")
  private String name;

  @Expose
  @SerializedName("createSql")
  private String ddl;

  @Expose
  private List<Index> indices = new LinkedList<>();

  /**
   * Returns the name of the Room entity (also the SQLite table name) that this instance represents.
   * This value will generally be read from a {@code database.entities[].tableName} property in a
   * Room-generated JSON schema file.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the Room entity (also the SQLite table name) that this instance represents.
   *
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the {@code CREATE TABLE} SQL statement used to create the SQLite table represented by
   * this instance. Note that the return value of this method includes a {@code ${TABLE_NAME}}
   * placeholder; the {@link #stream()} method replaces the placeholder with the value returned by
   * {@link #getName()}.
   */
  public String getDdl() {
    return ddl;
  }

  /**
   * Sets the {@code CREATE TABLE} SQL statement used to create the SQLite table represented by this
   * instance. If {@code ddl} contains any {@code ${TABLE_NAME}} placeholders, they'll be included
   * in the value returned by {@link #getDdl()}, but not by that returned by {@link #stream()}.
   *
   * @param ddl
   */
  public void setDdl(String ddl) {
    this.ddl = ddl;
  }

  /**
   * Returns the {@link List} of {@link Index} instances representing the indices declared on this
   * entity, corresponding to the value of the {@code database.entities[].indices} array property in
   * the JSON schema file. The JSON {@code indices} elements correspond, in turn, to any
   * {@code @Index} elements included in the {@code indices} property of an
   * {@code @Entity}-annotated class; they also include any indices declared on entity attributes
   * via the {@code index} attribute of the {code ColumnInfo} annotation on a field in an entity
   * class.
   */
  public List<Index> getIndices() {
    return indices;
  }

  /**
   * Sets the {@link List} of {@link Index} instances representing the indices on the table
   * corresponding to this entity.
   *
   * @param indices
   */
  public void setIndices(List<Index> indices) {
    this.indices = indices;
  }

  /**
   * Gathers into a {@link Stream} and returns the DDL statements to create the SQLite table
   * corresponding to this instance, along with all of its indices. As part of this process, all
   * {@code ${TABLE_NAME}} placeholders in the DDL are replaced by the value returned by
   * {@link #getName()}.
   */
  @Override
  public Stream<String> stream() {
    return Stream
        .concat(
            Stream.of(ddl),
            indices
                .stream()
                .flatMap(Index::stream)
        )
        .map((sql) -> sql.replace(PLACEHOLDER, name));
  }

}
