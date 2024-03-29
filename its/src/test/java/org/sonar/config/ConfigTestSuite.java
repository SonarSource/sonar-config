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
package org.sonar.config;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.locator.FileLocation;
import java.io.File;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  ConfigTest.class,
})
public class ConfigTestSuite {

  @ClassRule
  public static final Orchestrator ORCHESTRATOR;
  public static final FileLocation CONFIG_PLUGIN_LOCATION = FileLocation.byWildcardMavenFilename(new File("../sonar-config-plugin/target"), "sonar-config-plugin-*.jar");
  static final String SQ_VERSION_PROPERTY = "sonar.runtimeVersion";
  static final String DEFAULT_SQ_VERSION = "LATEST_RELEASE[8.9]";

  static {
    ORCHESTRATOR = Orchestrator.builderEnv()
      .useDefaultAdminCredentialsForBuilds(true)
      .setSonarVersion(System.getProperty(SQ_VERSION_PROPERTY, DEFAULT_SQ_VERSION))
      .addPlugin(CONFIG_PLUGIN_LOCATION)
      .restoreProfileAtStartup(FileLocation.of("src/test/resources/yaml-profile.xml"))
      .restoreProfileAtStartup(FileLocation.of("src/test/resources/json-profile.xml"))
      .build();
  }
}
