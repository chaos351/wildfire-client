package org.codework.wildfire.client.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Steven Benitez
 * @since 1.0.0
 */
public interface WildfireClient {
  void notify(@NotNull Throwable throwable);

  void notify(@NotNull Throwable throwable, @Nullable Object details);
}
