/*
 * SonarQube Config Plugin
 * Copyright (C) 2021-2021 SonarSource SA
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
package org.sonar.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class YamlTest extends TestBase {

  @Test
  public void yaml_measures() {
    final String projectKey = "yamlProject";
    ORCHESTRATOR.executeBuild(getSonarScanner(projectKey, "projects/yaml-project", "yaml", "yaml-profile"));

    assertThat(getMeasureAsInt(projectKey, "files")).isEqualTo(3);

    final String file1 = projectKey + ":file1.yaml";

    // No metric is pushed to SonarQube
    assertThat(getMeasureAsInt(file1, "ncloc")).isNull();
    assertThat(getMeasureAsInt(file1, "comment_lines")).isNull();
    assertThat(getMeasure(file1, "ncloc_data")).isNull();
  }

}
