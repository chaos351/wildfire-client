package org.codework.wildfire.client;

import com.google.common.base.Throwables;
import org.jetbrains.annotations.NotNull;

/**
 * Produces a unique, immutable fingerprint for a {@link Throwable}. Used to
 * easily identify and aggregate exceptions that occur.
 *
 * @author Steven Benitez
 * @since 1.0.0
 */
public final class ThrowableFingerprint {
  private final int stackTraceHashCode;
  private final int classNameHashCode;

  public ThrowableFingerprint(@NotNull Throwable throwable) {
    Throwable rootCause = Throwables.getRootCause(throwable);
    stackTraceHashCode = getStackTraceHashCode(rootCause.getStackTrace());
    classNameHashCode = rootCause.getClass().getName().hashCode();
  }

  /**
   * Returns a unique identifier representing the root cause's exception stack
   * trace.
   *
   * @return A unique identifier representing the root cause exception stack
   * trace.
   */
  public int getStackTraceHashCode() {
    return stackTraceHashCode;
  }

  /**
   * Returns a unique identifier representing the root cause's exception
   * class. This identifier is guaranteed to be unique across JVM invocations
   * for the same exception class. For example, all instances of
   * {@link NullPointerException} will have the same {@code classNameHashCode}.
   *
   * @return A unique identifier representing the root cause exception class.
   */
  public int getClassNameHashCode() {
    return classNameHashCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ThrowableFingerprint that = (ThrowableFingerprint) o;

    if (classNameHashCode != that.classNameHashCode) {
      return false;
    }
    if (stackTraceHashCode != that.stackTraceHashCode) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return stackTraceHashCode + classNameHashCode;
  }

  @Override
  public String toString() {
    return "[" + stackTraceHashCode + "]-[" + classNameHashCode + "]";
  }

  /**
   * Calculates the hashCode of a stack trace. This method differs from
   * {@link java.util.Arrays#hashCode(Object[])} in that it will exclude any
   * stack trace elements for which no source file is available, such as
   * Spring AOP proxies. This is necessary to ensure that stack trace
   * hashCodes don't change across JVM invocations when the dynamic class name
   * of our AOP proxies change. e.g., proxy class names such as "$Proxy129",
   * "$Proxy143", etc.
   *
   * @param elements The stack trace elements to obtain a hashCode for.
   * @return The hashCode of the stack trace.
   */
  private static int getStackTraceHashCode(StackTraceElement[] elements) {
    int result = 1;

    for (StackTraceElement element : elements) {
      // sb: ignore any elements with a null filename. element.getFileName()
      // returns null for stack trace elements that refer to dynamically
      // created proxies and JVM generated reflection accessors.
      if (element.getFileName() != null) {
        result = 31 * result + element.hashCode();
      }
    }
    return result;
  }
}
