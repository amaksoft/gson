package com.google.gson;

import com.google.gson.annotations.CustomDateFormat;
import com.google.gson.annotations.Parametrized;
import junit.framework.TestCase;

import java.util.Date;
import java.util.TimeZone;

/**
 * A basic unit test for the {@link DefaultDateTypeAdapterFactory} class.
 * Created by amak on 6/17/17.
 */
public class DefaultDateTypeAdapterFactoryTest extends TestCase {
  private static class DateTestClass {

    public DateTestClass(Date date) {
      this.fullDate = date;
      this.longDate = date;
      this.mediumDate = date;
      this.shortDate = date;
      this.fullDateTime = date;
      this.longDateTime = date;
      this.mediumDateTime = date;
      this.shortDateTime = date;
      this.isoDateTimeNoTz = date;
      this.nonCustom = date;
      this.nonParametrized = date;
    }

    @Parametrized
    @CustomDateFormat("EEEE, MMMM d, yyyy")
    Date fullDate;

    @Parametrized
    @CustomDateFormat("MMMM d, yyyy")
    Date longDate;

    @Parametrized
    @CustomDateFormat("MMM d, yyyy")
    Date mediumDate;

    @Parametrized
    @CustomDateFormat("M/d/yy")
    Date shortDate;

    @Parametrized
    @CustomDateFormat(value = "EEEE, MMMM d, yyyy h:mm:ss a z", locale = "FR_fr")
    Date fullDateTime;

    @Parametrized
    @CustomDateFormat("MMMM d, yyyy h:mm:ss a z")
    Date longDateTime;

    @Parametrized
    @CustomDateFormat("MMM d, yyyy h:mm:ss a")
    Date mediumDateTime;

    @Parametrized
    @CustomDateFormat("M/d/yy h:mm a")
    Date shortDateTime;

    @Parametrized
    @CustomDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    Date isoDateTimeNoTz;

    @CustomDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    Date nonParametrized;

    Date nonCustom;
  }

  public void testObjectSerialization() {

    TimeZone defaultTimeZone = TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    String dateTestClassJson = gson.toJson(new DateTestClass(new Date(0)));

    assertEquals("{" +
        "\"fullDate\":\"Thursday, January 1, 1970\"," +
        "\"longDate\":\"January 1, 1970\"," +
        "\"mediumDate\":\"Jan 1, 1970\"," +
        "\"shortDate\":\"1/1/70\"," +
        "\"fullDateTime\":\"jeudi, janvier 1, 1970 12:00:00 AM UTC\"," +
        "\"longDateTime\":\"January 1, 1970 12:00:00 AM UTC\"," +
        "\"mediumDateTime\":\"Jan 1, 1970 12:00:00 AM\"," +
        "\"shortDateTime\":\"1/1/70 12:00 AM\"," +
        "\"isoDateTimeNoTz\":\"1970-01-01T00:00:00\"," +
        "\"nonParametrized\":\"1970-01-01T00:00:00+0000\"," +
        "\"nonCustom\":\"1970-01-01T00:00:00+0000\"" +
        "}",

      dateTestClassJson
    );

    TimeZone.setDefault(defaultTimeZone);
  }

  public void testObjectDeserialization() {

    TimeZone defaultTimeZone = TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    String jsonString = "{" +
      "\"fullDate\":\"Thursday, January 1, 1970\"," +
      "\"longDate\":\"January 1, 1970\"," +
      "\"mediumDate\":\"Jan 1, 1970\"," +
      "\"shortDate\":\"1/1/70\"," +
      "\"fullDateTime\":\"jeudi, janvier 1, 1970 12:00:00 AM UTC\"," +
      "\"longDateTime\":\"January 1, 1970 12:00:00 AM UTC\"," +
      "\"mediumDateTime\":\"Jan 1, 1970 12:00:00 AM\"," +
      "\"shortDateTime\":\"1/1/70 12:00 AM\"," +
      "\"isoDateTimeNoTz\":\"1970-01-01T00:00:00\"," +
      "\"nonParametrized\":\"1970-01-01T00:00:00+0000\"," +
      "\"nonCustom\":\"1970-01-01T00:00:00+0000\"" +
      "}";

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    DateTestClass dateTestClass = gson.fromJson(jsonString, DateTestClass.class);

    assertEquals(dateTestClass.fullDate.getTime(), 0);
    assertEquals(dateTestClass.longDate.getTime(), 0);
    assertEquals(dateTestClass.mediumDate.getTime(), 0);
    assertEquals(dateTestClass.shortDate.getTime(), 0);
    assertEquals(dateTestClass.fullDateTime.getTime(), 0);
    assertEquals(dateTestClass.longDateTime.getTime(), 0);
    assertEquals(dateTestClass.mediumDateTime.getTime(), 0);
    assertEquals(dateTestClass.shortDateTime.getTime(), 0);
    assertEquals(dateTestClass.isoDateTimeNoTz.getTime(), 0);
    assertEquals(dateTestClass.nonParametrized.getTime(), 0);
    assertEquals(dateTestClass.nonCustom.getTime(), 0);

    TimeZone.setDefault(defaultTimeZone);
  }
}