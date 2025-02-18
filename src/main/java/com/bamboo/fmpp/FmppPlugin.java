package com.bamboo.fmpp;

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
