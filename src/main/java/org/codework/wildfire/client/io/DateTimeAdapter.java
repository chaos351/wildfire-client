package org.codework.wildfire.client.io;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * A Gson adapter for Joda {@link DateTime} objects. Not part of the public API.
 *
 * @author Steven Benitez
 * @since 1.0.0
 */
class DateTimeAdapter implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat
      .dateTime()
      .withZone(DateTimeZone.UTC);

  @Override
  public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    String dateAsString = json.getAsString();
    if (!isEmpty(dateAsString)) {
      return DATE_TIME_FORMATTER.parseDateTime(dateAsString);
    }

    return null;
  }

  @Override
  public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src == null ? StringUtils.EMPTY : DATE_TIME_FORMATTER.print(src));
  }
}
