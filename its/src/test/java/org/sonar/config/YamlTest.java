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

public class YamlTest extends TestBase {

  private static final String PROJECT_KEY = "yamlProject";
  private static final String LANGUAGE_KEY = "yaml";
  private static final String PROFILE_NAME = "yaml-profile";

  @BeforeClass
  public static void setup() {
    ORCHESTRATOR.getServer().provisionProject(PROJECT_KEY, PROJECT_KEY);
    ORCHESTRATOR.getServer().associateProjectToQualityProfile(PROJECT_KEY, LANGUAGE_KEY, PROFILE_NAME);
  }

  @AfterClass
  public static void teardown() {
    if (ORCHESTRATOR != null) {
      ORCHESTRATOR.stop();
    }
  }

  @Test
  public void yaml_measures() {
    ORCHESTRATOR.executeBuild(getSonarScanner(PROJECT_KEY, "projects/yaml-project"));

    assertThat(getMeasureAsInt(PROJECT_KEY, "files")).isEqualTo(3);

    final String file1 = PROJECT_KEY + ":file1.yaml";

    // No metric is pushed to SonarQube
    assertThat(getMeasureAsInt(file1, "ncloc")).isNull();
    assertThat(getMeasureAsInt(file1, "comment_lines")).isNull();
    assertThat(getMeasure(file1, "ncloc_data")).isNull();
  }

  @Test
  public void yaml_measures_with_custom_file_suffixes() {
    final String projectKey = "yamlProject";
    SonarScanner scanner = getSonarScanner(projectKey, "projects/yaml-project");
    scanner.setProperty("sonar.yaml.file.suffixes", ".yaml,.yml,.raml");
    ORCHESTRATOR.executeBuild(scanner);

    assertThat(getMeasureAsInt(projectKey, "files")).isEqualTo(4);
  }

}
