package com.google.gson;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

/**
 * This type adapter supports three subclasses of date: Date, Timestamp, and
 * java.sql.Date.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
@SuppressWarnings({"unchecked", "WeakerAccess"})
class GeneralDateTypeAdapter<T> extends TypeAdapter<T> {

  TypeToken<T> type;
  private DateFormat enUsFormat;
  private DateFormat localFormat;
  private DateFormat customFormat;

  @SuppressWarnings("unused") // hide empty constructor
  private GeneralDateTypeAdapter() {}

  public GeneralDateTypeAdapter(TypeToken<T> type, DateFormat enUsFormat, DateFormat localFormat, DateFormat customFormat) {
    this.type = type;
    this.enUsFormat = enUsFormat;
    this.localFormat = localFormat;
    this.customFormat = customFormat;
  }

  @Override
  public synchronized void write(JsonWriter out, T value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    String dateFormatAsString;
    if (customFormat != null) {
      dateFormatAsString = customFormat.format(value);
    } else {
      dateFormatAsString = enUsFormat.format(value);
    }
    out.value(dateFormatAsString);
  }

  @Override
  public T read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    Date date = deserializeToDate(in.nextString());

    Class typeOfT = type.getRawType();
    if (typeOfT == Date.class) {
      return (T) date;
    } else if (typeOfT == Timestamp.class) {
      return (T) new Timestamp(date.getTime());
    } else if (typeOfT == java.sql.Date.class) {
      return (T) new java.sql.Date(date.getTime());
    } else {
      throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
    }
  }

  private synchronized Date deserializeToDate(String json) {
    try {
      if (customFormat != null) return customFormat.parse(json);
    } catch (ParseException ignored) {}
    try {
      return localFormat.parse(json);
    } catch (ParseException ignored) {}
    try {
      return enUsFormat.parse(json);
    } catch (ParseException ignored) {}
    try {
      return ISO8601Utils.parse(json, new ParsePosition(0));
    } catch (ParseException e) {
      throw new JsonSyntaxException(json, e);
    }
  }
}
