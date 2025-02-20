/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.chengxianglibra.fmpp;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;

public class FmppPlugin implements Plugin<Project> {
  public static final String FMPP_CLASSPATH_CONFIGURATION_NAME = "fmppClaspath";

  @Override
  public void apply(Project target) {
    configureFmpp(target);
  }

  private void configureFmpp(Project project) {
    Configuration configuration = project.getConfigurations().create(FMPP_CLASSPATH_CONFIGURATION_NAME, conf -> {
      conf.setCanBeConsumed(false);
    });
    configuration.defaultDependencies(dependencies -> {
      dependencies.add(project.getDependencies().create("org.freemarker:freemarker:2.3.29"));
      dependencies.add(project.getDependencies().create("net.sourceforge.fmpp:fmpp:0.9.16"));
    });
  }
}
