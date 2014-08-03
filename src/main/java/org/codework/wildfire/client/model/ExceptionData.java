package org.codework.wildfire.client.model;

import com.google.gson.annotations.SerializedName;
import org.codework.wildfire.client.ThrowableFingerprint;
import org.joda.time.DateTime;

/**
 * A Gson bean for transmitting exception data to Wildfire.
 *
 * @author Steven Benitez
 * @since 1.0.0
 */
public class ExceptionData {
  @SerializedName("api_key")
  private String apiKey;
  private String project;
  private String environment;
  private String fingerprint;
  private ExceptionDetails error;
  private ExceptionDetails cause;
  @SerializedName("occurred_at")
  private DateTime occurredAt;
  private String revision;
  private String branch;
  private Object details;

  public ExceptionData() {
  }

  public ExceptionData(Throwable throwable) {
    this(throwable, null);
  }

  public ExceptionData(Throwable throwable, Object details) {
    this.apiKey = null;
    this.environment = null;
    this.project = null;
    this.fingerprint = null;
    this.revision = null;
    this.branch = null;

    this.fingerprint = new ThrowableFingerprint(throwable).toString();
    Throwable cause = throwable.getCause();
    if (cause != null) {
      this.cause = new ExceptionDetails(cause);
    }

    this.error = new ExceptionDetails(throwable);
    this.occurredAt = DateTime.now();
    this.details = details;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public String getProject() {
    return project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public String getFingerprint() {
    return fingerprint;
  }

  public void setFingerprint(String fingerprint) {
    this.fingerprint = fingerprint;
  }

  public ExceptionDetails getError() {
    return error;
  }

  public void setError(ExceptionDetails error) {
    this.error = error;
  }

  public ExceptionDetails getCause() {
    return cause;
  }

  public void setCause(ExceptionDetails cause) {
    this.cause = cause;
  }

  public DateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(DateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  public String getRevision() {
    return revision;
  }

  public void setRevision(String revision) {
    this.revision = revision;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public Object getDetails() {
    return details;
  }

  public void setDetails(Object details) {
    this.details = details;
  }
}
