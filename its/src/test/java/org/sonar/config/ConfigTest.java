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

import com.sonar.orchestrator.build.SonarScanner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigTest extends TestBase {

  private static final String PROJECT_KEY = "configProject";

  @BeforeClass
  public static void setup() {
    ORCHESTRATOR.getServer().provisionProject(PROJECT_KEY, PROJECT_KEY);
    ORCHESTRATOR.getServer().associateProjectToQualityProfile(PROJECT_KEY, "yaml", "yaml-profile");
    ORCHESTRATOR.getServer().associateProjectToQualityProfile(PROJECT_KEY, "json", "json-profile");
  }

  @AfterClass
  public static void teardown() {
    if (ORCHESTRATOR != null) {
      ORCHESTRATOR.stop();
    }
  }

  @Test
  public void yaml_measures() {
    ORCHESTRATOR.executeBuild(getSonarScanner(PROJECT_KEY, "projects/config-project"));

    assertThat(getMeasureAsInt(PROJECT_KEY, "files")).isEqualTo(4);

    final String file1 = PROJECT_KEY + ":file1.yaml";

    // No metric is pushed to SonarQube, only the "lines" metric computed by SonarQube is available
    assertThat(getMeasureAsInt(file1, "lines")).isEqualTo(8);
    assertThat(getMeasureAsInt(file1, "ncloc")).isNull();
    assertThat(getMeasureAsInt(file1, "comment_lines")).isNull();
    assertThat(getMeasure(file1, "ncloc_data")).isNull();
  }

  @Test
  public void yaml_measures_with_custom_file_suffixes() {
    SonarScanner scanner = getSonarScanner(PROJECT_KEY, "projects/config-project");
    scanner.setProperty("sonar.yaml.file.suffixes", ".yml,.raml");
    scanner.setProperty("sonar.json.file.suffixes", ".jsn");
    ORCHESTRATOR.executeBuild(scanner);

    assertThat(getMeasureAsInt(PROJECT_KEY, "files")).isEqualTo(3);
    assertThat(getMeasureAsInt(PROJECT_KEY + ":file.jsn", "lines")).isEqualTo(10);
  }

}
