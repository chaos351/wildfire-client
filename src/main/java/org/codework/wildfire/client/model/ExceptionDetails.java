package org.codework.wildfire.client.model;

import com.google.common.base.Throwables;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 * @author Steven Benitez
 * @since 1.0.0
 */
public class ExceptionDetails {
  @SerializedName("class_name")
  private String className;
  @SerializedName("stack_trace")
  private String stackTrace;
  private String message;

  public ExceptionDetails() {
  }

  public ExceptionDetails(@NotNull Throwable throwable) {
    this.className = throwable.getClass().getName();
    this.stackTrace = Throwables.getStackTraceAsString(throwable);
    this.message = throwable.getMessage();
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public void setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
