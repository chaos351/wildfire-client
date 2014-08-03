package org.codework.wildfire.client;

/**
 * @author Steven Benitez
 * @since 1.0.0
 */
public class WildfireProjectConfig {
  private final String apiKey;
  private final String project;
  private final String environment;
  private final String revision;
  private final String branch;

  public WildfireProjectConfig(String apiKey, String project, String environment, String revision, String branch) {
    this.apiKey = apiKey;
    this.project = project;
    this.environment = environment;
    this.revision = revision;
    this.branch = branch;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getProject() {
    return project;
  }

  public String getEnvironment() {
    return environment;
  }

  public String getRevision() {
    return revision;
  }

  public String getBranch() {
    return branch;
  }
}
