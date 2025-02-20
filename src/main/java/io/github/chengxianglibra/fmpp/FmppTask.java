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

import java.util.HashMap;
import java.util.Map;
import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;

import javax.inject.Inject;

@CacheableTask
public class FmppTask extends DefaultTask {

  private final Property<Configuration> fmppClasspath;
  private final RegularFileProperty config;
  private final RegularFileProperty defaultConfig;
  private final DirectoryProperty templates;
  private final DirectoryProperty output;

  @Inject
  public FmppTask(ObjectFactory objectFactory) {
    this.fmppClasspath = objectFactory.property(Configuration.class)
        .convention(
            getProject().getConfigurations().named(FmppPlugin.FMPP_CLASSPATH_CONFIGURATION_NAME));
    this.config = objectFactory.fileProperty();
    this.templates = objectFactory.directoryProperty();
    this.defaultConfig = objectFactory.fileProperty()
        .convention(templates.file("../default_config.fmpp"));
    this.output = objectFactory.directoryProperty()
        .convention(getProject().getLayout().getBuildDirectory().dir("fmpp/" + getName()));
  }

  @Classpath
  public Property<Configuration> getFmppClasspath() {
    return fmppClasspath;
  }

  @InputFile
  @PathSensitive(PathSensitivity.NONE)
  public RegularFileProperty getConfig() {
    return config;
  }

  @InputFile
  @PathSensitive(PathSensitivity.NONE)
  public RegularFileProperty getDefaultConfig() {
    return defaultConfig;
  }

  @InputDirectory
  @PathSensitive(PathSensitivity.RELATIVE)
  public DirectoryProperty getTemplates() {
    return templates;
  }

  @OutputDirectory
  public DirectoryProperty getOutput() {
    return output;
  }

  private String tddString(String path) {
    return "\"" + path.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
  }

  @TaskAction
  public void run() {
    getProject().delete(output.getAsFileTree());
    Map<String, String> taskDefParameters = new HashMap<>();
    taskDefParameters.put("name", "fmpp");
    taskDefParameters.put("classname", "fmpp.tools.AntTask");
    taskDefParameters.put("classpath", fmppClasspath.get().getAsPath());

    Map<String, String> fmppParameters = new HashMap<>();
    fmppParameters.put("configuration", config.get().getAsFile().toString());
    fmppParameters.put("sourceRoot", templates.get().getAsFile().toString());
    fmppParameters.put("outputRoot", output.get().getAsFile().toString());
    fmppParameters.put("data", "tdd(" + tddString(config.get().getAsFile().toString()) + "),"+
        "default: tdd(" + tddString(defaultConfig.get().getAsFile().toString()) + ")");
    getProject().ant(ant -> {
      ant.invokeMethod("taskdef", new Object[]{taskDefParameters});
      ant.invokeMethod("fmpp", new Object[]{fmppParameters});
    });
  }

  private Map<String, String> toMap(String key, String value) {
    Map<String, String> map = new HashMap<>();
    map.put(key, value);
    return map;
  }
}