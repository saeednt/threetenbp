package org.threeten.bp.chrono;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalField;

@Test
public class TestSolarHijrahChronology {
    //-----------------------------------------------------------------------
    // Chrono.ofName("SolarHijrah")  Lookup by name
    //-----------------------------------------------------------------------
    @Test
    public void test_chrono_byName() {
        Chronology c = SolarHijrahChronology.INSTANCE;
        Chronology test = Chronology.of("SolarHijrah");
        Assert.assertNotNull(test, "The Solar Hijrah calendar could not be found byName");
        Assert.assertEquals(test.getId(), "SolarHijrah", "ID mismatch");
        Assert.assertEquals(test.getCalendarType(), "Solar Hijrah", "Type mismatch");
        Assert.assertEquals(test, c);
    }

    @Test
    public void test_solar_hijrah_year(){
        SolarHijrahDate now = SolarHijrahDate.of(2018, 10, 2);
        long aLong = now.getLong(ChronoField.YEAR);
        Assert.assertEquals(aLong, 1397);
    }

    @Test
    public void test_solar_hijrah_month(){
        SolarHijrahDate now = SolarHijrahDate.of(2018, 10, 2);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 7);
    }

    @Test
    public void test_solar_hijrah_day(){
        SolarHijrahDate now = SolarHijrahDate.of(1986, 8, 25);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 3);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 6);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1365);
    }

    @Test
    public void test_solar_hijrah_day_minus(){
        SolarHijrahDate now = SolarHijrahDate.of(1986, 8, 25);
        Assert.assertEquals(now.minusDays(1).getLong(ChronoField.DAY_OF_MONTH), 2);
        Assert.assertEquals(now.minusMonths(1).getLong(ChronoField.MONTH_OF_YEAR), 5);
        Assert.assertEquals(now.minusYears(1).getLong(ChronoField.YEAR), 1364);
    }

    @Test
    public void test_solar_hijrah_day_leap(){
        SolarHijrahDate now = SolarHijrahDate.of(2017, 3, 20);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 30);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 12);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1395);
    }

    @Test
    public void test_solar_hijrah_day_leap2(){
        SolarHijrahDate now = SolarHijrahDate.of(2017, 3, 21);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 1);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1396);
    }

    @Test
    public void test_solar_hijrah_day_leap3(){
        SolarHijrahDate now = SolarHijrahDate.of(1947, 3, 21);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 30);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 12);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1325);
    }

    @Test
    public void test_solar_hijrah_day_leap4(){
        SolarHijrahDate now = SolarHijrahDate.of(1947, 3, 22);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 1);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1326);
    }

    @Test
    public void test_solar_hijrah_day_future(){
        SolarHijrahDate now = SolarHijrahDate.of(2020, 11, 7);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 17);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 8);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1399);
    }
}
