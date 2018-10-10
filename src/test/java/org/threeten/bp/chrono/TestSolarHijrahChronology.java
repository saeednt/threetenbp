package org.threeten.bp.chrono;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoField;

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

    @DataProvider(name = "samples")
    Object[][] dataSamples() {
        return new Object[][]
                {
                        {SolarHijrahDate.ofSolar(1365, 6, 3), SolarHijrahDate.of(1986, 8, 25)},
                        {SolarHijrahDate.ofSolar(1396, 1, 1), SolarHijrahDate.of(2017, 3, 21)},
                        {SolarHijrahDate.ofSolar(1397, 8, 8), SolarHijrahDate.of(2018, 10, 30)},
                        {SolarHijrahDate.ofSolar(1395, 12, 30), SolarHijrahDate.of(2017, 3, 20)},
                        {SolarHijrahDate.ofSolar(1250, 6, 31), SolarHijrahDate.of(1871, 9, 22)},
                        {SolarHijrahDate.of(2019, 3, 21), SolarHijrahDate.ofSolar(1398, 1, 1)},
                        {SolarHijrahDate.of(2019, 3, 20), SolarHijrahDate.ofSolar(1397, 12, 29)},
                        {SolarHijrahDate.of(2019, 4, 21), SolarHijrahDate.ofSolar(1398, 2, 1)},
                        {SolarHijrahDate.of(2019, 3, 20).plusDays(1), SolarHijrahDate.ofSolar(1398, 1, 1)},
                        {SolarHijrahDate.ofSolar(1397, 6, 31).with(ChronoField.DAY_OF_MONTH, 1), SolarHijrahDate.ofSolar(1397, 6, 1)}
                };
    }

    @Test(dataProvider = "samples")
    public void test(SolarHijrahDate date, SolarHijrahDate date2){
        Assert.assertEquals(date, date2);
    }

    @Test
    public void test_solar_hijrah_year() {
        SolarHijrahDate now = SolarHijrahDate.of(2018, 10, 2);
        long aLong = now.getLong(ChronoField.YEAR);
        Assert.assertEquals(aLong, 1397);
    }

    @Test
    public void test_solar_hijrah_month() {
        SolarHijrahDate now = SolarHijrahDate.of(2018, 10, 2);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 7);
    }

    @Test
    public void test_solar_hijrah_day() {
        SolarHijrahDate now = SolarHijrahDate.of(1986, 8, 25);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 3);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 6);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1365);
    }

    @Test
    public void test_solar_hijrah_day_minus() {
        SolarHijrahDate now = SolarHijrahDate.of(1986, 8, 25);
        Assert.assertEquals(now.minusDays(1).getLong(ChronoField.DAY_OF_MONTH), 2);
        Assert.assertEquals(now.minusMonths(1).getLong(ChronoField.MONTH_OF_YEAR), 5);
        Assert.assertEquals(now.minusYears(1).getLong(ChronoField.YEAR), 1364);
    }

    @Test
    public void test_solar_hijrah_day_leap() {
        SolarHijrahDate now = SolarHijrahDate.of(2017, 3, 20);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 30);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 12);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1395);
    }

    @Test
    public void test_solar_hijrah_day_leap2() {
        SolarHijrahDate now = SolarHijrahDate.of(2017, 3, 21);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 1);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1396);
    }

    @Test
    public void test_solar_hijrah_day_leap3() {
        SolarHijrahDate now = SolarHijrahDate.of(1947, 3, 21);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 30);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 12);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1325);
    }

    @Test
    public void test_solar_hijrah_day_leap4() {
        SolarHijrahDate now = SolarHijrahDate.of(1947, 3, 22);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 1);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1326);
    }

    @Test
    public void test_solar_hijrah_day_future() {
        SolarHijrahDate now = SolarHijrahDate.of(2020, 11, 7);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 17);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 8);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1399);
    }

    @Test
    public void test_solar_hijrah_one_birthday() {
        SolarHijrahDate now = SolarHijrahDate.of(1981, 9, 21);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 30);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 6);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1360);
    }


    @Test
    public void test_2018_dates() {
        SolarHijrahDate now = SolarHijrahDate.of(2018, 3, 21);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 1);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1397);
    }

    @Test
    public void test_month_6_and_7_dates() {
        SolarHijrahDate now = SolarHijrahDate.of(1871, 9, 23);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 7);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1250);
    }

    @Test
    public void test_month_6_and_7_dates_2() {
        SolarHijrahDate now = SolarHijrahDate.of(1871, 9, 22);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 31);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 6);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1250);
    }

    @Test
    public void test_month_6_and_7_dates_3() {
        SolarHijrahDate now = SolarHijrahDate.of(1911, 9, 24);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 7);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1290);
    }

    @Test
    public void test_month_6_and_7_dates_4() {
        SolarHijrahDate now = SolarHijrahDate.of(1911, 9, 23);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 31);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 6);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1290);
    }


    @Test
    public void test_2018_dates_2() {
        SolarHijrahDate now = SolarHijrahDate.of(2018, 10, 1);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 9);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 7);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1397);
    }


    @Test
    public void test_2019_dates() {
        SolarHijrahDate now = SolarHijrahDate.of(2019, 3, 21);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 1);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1398);
    }


    @Test
    public void test_2019_dates_2() {
        SolarHijrahDate now = SolarHijrahDate.of(2019, 3, 20);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 29);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 12);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1397);
    }


    @Test
    public void test_2019_dates_3() {
        SolarHijrahDate now = SolarHijrahDate.of(2019, 4, 21);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 2);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1398);
    }

    @Test
    public void test_count_of_days() {
        SolarHijrahDate now = SolarHijrahDate.now();
        for (int i = 0; i < 380; i++) {
            SolarHijrahDate solarHijrahDate = now.plusDays(1);
            assert solarHijrahDate.getLong(ChronoField.DAY_OF_MONTH) < 32;
        }
    }

    @Test
    public void test_epoch_day() {
        LocalDate of = LocalDate.of(1970, 1, 2);
        Assert.assertEquals(of.toEpochDay(), 1);
    }

    @Test
    public void test_day_of_week() {
        SolarHijrahDate of = SolarHijrahDate.of(2018, 10, 1);
        Assert.assertEquals(of.getDay(), 9);
        Assert.assertEquals(of.get(ChronoField.DAY_OF_WEEK), DayOfWeek.MONDAY.getValue());
    }

    @Test
    public void test_day_of_month_after_adding_years() {
        SolarHijrahDate now = SolarHijrahDate.of(2018, 10, 23);
        Assert.assertEquals(now.getLong(ChronoField.DAY_OF_MONTH), 1);
        Assert.assertEquals(now.getLong(ChronoField.MONTH_OF_YEAR), 8);
        Assert.assertEquals(now.getLong(ChronoField.YEAR), 1397);
    }

    @Test
    public void test_now() {
        SolarHijrahDate of = SolarHijrahDate.of(2018, 10, 6);
        ChronoDateImpl<SolarHijrahDate> years = of.minusYears(200);
        ChronoDateImpl<SolarHijrahDate> date = years.minusDays(13);
        Assert.assertEquals(date.get(ChronoField.DAY_OF_MONTH) < 31, true);
    }
}
