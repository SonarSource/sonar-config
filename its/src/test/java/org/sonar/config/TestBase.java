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

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.SonarScanner;
import com.sonar.orchestrator.locator.FileLocation;
import java.io.File;
import java.util.List;
import javax.annotation.CheckForNull;
import org.jetbrains.annotations.Nullable;
import org.junit.ClassRule;
import org.sonarqube.ws.Measures.ComponentWsResponse;
import org.sonarqube.ws.Measures.Measure;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.measures.ComponentRequest;

import static java.util.Collections.singletonList;

public abstract class TestBase {

  @ClassRule
  public static final Orchestrator ORCHESTRATOR = ConfigTestSuite.ORCHESTRATOR;

  protected SonarScanner getSonarScanner(String projectKey, String directoryToScan) {
    return SonarScanner.create()
      .setProjectDir(new File(directoryToScan))
      .setProjectKey(projectKey)
      .setProjectName(projectKey)
      .setProjectVersion("1")
      .setSourceDirs(".");
  }

  @CheckForNull
  protected Measure getMeasure(String projectKey, String metricKey) {
    return getMeasure(projectKey, null, metricKey);
  }

  @CheckForNull
  protected Measure getMeasure(String projectKey, @Nullable String componentKey, String metricKey) {
    String component;
    if (componentKey != null) {
      component = projectKey + ":" + componentKey;
    } else {
      component = projectKey;
    }
    ComponentWsResponse response = newWsClient().measures().component(new ComponentRequest()
      .setComponent(component)
      .setMetricKeys(singletonList(metricKey)));
    List<Measure> measures = response.getComponent().getMeasuresList();
    return measures.size() == 1 ? measures.get(0) : null;
  }

  @CheckForNull
  protected Integer getMeasureAsInt(String componentKey, String metricKey) {
    Measure measure = getMeasure(componentKey, metricKey);
    return (measure == null) ? null : Integer.parseInt(measure.getValue());
  }

  protected static WsClient newWsClient() {
    return WsClientFactories.getDefault().newClient(HttpConnector.newBuilder()
      .url(ORCHESTRATOR.getServer().getUrl())
      .build());
  }

}
