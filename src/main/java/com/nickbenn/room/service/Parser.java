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
package com.nickbenn.room.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.nickbenn.room.model.Entity;
import com.nickbenn.room.model.Index;
import com.nickbenn.room.model.View;
import com.nickbenn.room.model.Schema;
import com.nickbenn.room.model.Streamable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides a simple parsing service that extracts DDL from a Room-generated JSON schema file. This
 * class is not a Java application (i.e. it does not provide an entry point {@code main} method),
 * but is suitable for consumption by many different types of applications or plugins.
 * <p>
 * The JSON schema produced by Room is structured as shown below. Property values, properties that
 * are not read by this implementation, and repeated properties in arrays are indicated by
 * "{@code ...}" placeholders.
 * </p>
 * <pre id="json-schema-structure"><code>
 * {
 *   "database": {
 *     "version": ...,
 *     "entities": [
 *       {
 *         "tableName": "...",
 *         "createSql": "...",
 *         indices: [
 *           {
 *             "name": "...",
 *             "createSql": "...",
 *             ...
 *           },
 *           ...
 *         ],
 *         ...
 *       },
 *       ...
 *     ],
 *     "views": [
 *       {
 *         "viewName": "...",
 *         "createSql": "..."
 *       },
 *       ...
 *     ],
 *   },
 *   ...
 * }
 * </code></pre>
 * <p>
 * In general, the SQL fragments in the values of the {@code createSql} properties have placeholder
 * tokens referencing the name of the relevant table or view for each. The
 * {@link Entity}, {@link Index}, and
 * {@link View} data-transfer object (DTO) classes replace these
 * placeholders in their implementations of the {@link Streamable#stream()} methods.
 * </p>
 */
public class Parser {

  private static final Pattern COMMENT_STATEMENT_PATTERN = Pattern.compile("^\\s*--\\s.*$");
  private static final String COMMENT_STATEMENT_FORMAT = "%s";
  private static final String CODE_STATEMENT_FORMAT = "%s;";
  private static final String STATEMENT_SEPARATOR = String.format("%n%n");

  /**
   * Initializes this instance. Currently, since this class has no mutable state, nor even any
   * {@code final} fields set on instance initialization, this constructor does nothing.
   */
  public Parser() {
  }

  /**
   * Parses Room schema DDL from JSON content obtained from {@code input}, and writes the extracted
   * DDL (with placeholders replaced by the appropriate table and view names) to {@code output}.
   *
   * @param input  {@link InputStream} source from which Room schema will be read.
   * @param output {@link PrintStream}
   * @throws IOException         If unable to read from {@code input} or unable to write to
   *                             {@code output}.
   * @throws JsonIOException     If unable to read JSON from a {@link Reader} attached to
   *                             {@code input}.
   * @throws JsonSyntaxException If content read from {@code input} does not contain valid JSON.
   */
  public void parse(InputStream input, OutputStream output)
      throws IOException, JsonIOException, JsonSyntaxException {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
    try (
        Reader reader = new InputStreamReader(input);
        PrintWriter writer = new PrintWriter(output)
    ) {
      String content = gson
          .fromJson(reader, Schema.class)
          .stream()
          .map(this::terminate)
          .collect(Collectors.joining(STATEMENT_SEPARATOR));
      writer.print(content);
    }
  }

  private String terminate(String statement) {
    return String
        .format(
            COMMENT_STATEMENT_PATTERN
                .matcher(statement)
                .matches()
                ? COMMENT_STATEMENT_FORMAT
                : CODE_STATEMENT_FORMAT,
            statement
        );
  }

}
