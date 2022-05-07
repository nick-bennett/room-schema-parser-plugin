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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents the {@code database} in a Room SQLite database schema. This will generally be read
 * from a JSON file with the structure described in {@link edu.cnm.deepdive.room.service.Parser}.
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public class Database implements Streamable {

  private static final String VERSION_FORMAT =
      "-- Generated %1$tF %1$tT%1$tz for database version %2$d";

  @Expose
  private int version;
  @Expose
  private List<Entity> entities = new LinkedList<>();

  @Expose
  private List<View> views = new LinkedList<>();

  /**
   * Returns the {@link List} of {@link Entity} instances representing the tables in the database
   * schema, corresponding to the {@code database.entities} JSON property. The latter correspond, in
   * turn, to the {@code @Entity}-annotated classes included in the {@code entities} property of a
   * {@code @Database}-annotated class.
   */
  public List<Entity> getEntities() {
    return entities;
  }

  /**
   * Sets the {@link List} of {@link Entity} instances representing the tables in the database
   * schema.
   *
   * @param entities
   */
  public void setEntities(List<Entity> entities) {
    this.entities = entities;
  }

  /**
   * Returns the {@link List} of {@link View} instances representing the views in the database
   * schema, corresponding to the {@code database.views} JSON property.The latter correspond, in
   * turn, to the {@code @DatabaseView}-annotated classes included in the {@code views} property of
   * a {@code @Database}-annotated class.
   */
  public List<View> getViews() {
    return views;
  }

  /**
   * Sets the {@link List} of {@link View} instances representing the views in the database
   * schema.
   *
   * @param views
   */
  public void setViews(List<View> views) {
    this.views = views;
  }

  /**
   * Returns the {@code int}-valued version number of the database schema, as declared in the
   * {@code version} attribute of the {@code @Database} annotation, and serialized as the
   * {@code database.version} property in the JSON schema.
   */
  public int getVersion() {
    return version;
  }

  /**
   * Sets the version number of the database schema.
   *
   * @param version
   */
  public void setVersion(int version) {
    this.version = version;
  }

  /**
   * Gathers into a {@link Stream} and returns the DDL statements capable of creating the database
   * corresponding to this instance, along with all of its tables, indices, and views.
   */
  @Override
  public Stream<String> stream() {
    return Stream
        .concat(
            Stream.of(
                String.format(VERSION_FORMAT, new Date(), version)
            ),
            Stream
                .concat(
                    entities.stream(),
                    views.stream()
                )
                .flatMap(Streamable::stream)
        );
  }

}
