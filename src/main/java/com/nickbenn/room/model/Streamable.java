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

import java.util.stream.Stream;

/**
 * Declares the simple capability (implemented by all the {@link com.nickbenn.room.model model}
 * classes) of emitting an instance's relevant content (DDL, in this case) as a
 * {@link Stream Stream&lt;String&gt;}.
 */
public interface Streamable {

  /**
   * Constructs (as appropriate) and returns a {@link Stream Stream&lt;String&gt;} containing the
   * relevant content of the current instance, along with that of any child objects.
   */
  Stream<String> stream();

}
