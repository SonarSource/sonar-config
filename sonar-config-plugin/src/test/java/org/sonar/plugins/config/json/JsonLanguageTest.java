/*
 * SonarQube Config Plugin
 * Copyright (C) 2021-2023 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.config.json;

import org.junit.jupiter.api.Test;
import org.sonar.api.config.internal.MapSettings;

import static org.assertj.core.api.Assertions.assertThat;

class JsonLanguageTest {

  @Test
  void should_return_yaml_file_suffixes() {
    MapSettings settings = new MapSettings();
    JsonLanguage language = new JsonLanguage(settings.asConfig());
    assertThat(language.getFileSuffixes()).containsExactly(".json");

    settings.setProperty(JsonLanguage.FILE_SUFFIXES_KEY, "");
    assertThat(language.getFileSuffixes()).containsExactly(".json");

    settings.setProperty(JsonLanguage.FILE_SUFFIXES_KEY, ".bar, .foo");
    assertThat(language.getFileSuffixes()).containsOnly(".bar", ".foo");

    settings.setProperty(JsonLanguage.FILE_SUFFIXES_KEY, ".foo, , ");
    assertThat(language.getFileSuffixes()).containsOnly(".foo");
  }
  
  @Test
  void should_not_publish_all_files() {
    MapSettings settings = new MapSettings();
    JsonLanguage language = new JsonLanguage(settings.asConfig());
    
    assertThat(language.publishAllFiles()).isFalse();
  }

}
