/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.threeten.bp.chrono;

import org.threeten.bp.*;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import static org.threeten.bp.temporal.ChronoField.*;

/**
 * A date in the Solar Hijrah calendar system.
 * <p>
 * This date operates using the {@linkplain SolarHijrahChronology Solar Hijrah calendar}.
 * This calendar system is primarily used in I.R. Iran and Afghanistan.
 * Dates are aligned such that {@code 01-01-01 (Solar Hijrah)} is {@code 622-03-22 (ISO)}.
 *
 * <h3>Specification for implementors</h3>
 * This class is immutable and thread-safe.
 */
public final class SolarHijrahDate
        extends ChronoDateImpl<SolarHijrahDate>
        implements Serializable {

    public static final LocalDate SOLAR_HIJRAH_START_DATE = LocalDate.of(622, 3, 22).minusYears(1);

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -8722293800195731463L;

    /**
     * The number of days in a 400 year cycle.
     */
    private final int DAYS_PER_CYCLE = 146097;
    /**
     * The number of days from year zero to year 1970.
     * There are five 400 year cycles from year zero to 2000.
     * There are 7 leap years from 1970 to 2000.
     */
    private final long DAYS_0000_TO_1970 = (DAYS_PER_CYCLE * 5L) - (30L * 365L + 7L);

    /**
     * The underlying date.
     */
    private final LocalDate isoDate;
    /**
     * for handling operations related to Solar Hijrah Leap years
     */
    private final SolarHijrahLeapYears leapYears;

    private int year;
    /**
     * The month of year in Solar Hijrah calendar system
     */
    private int month;
    /**
     * The day of month in Solar Hijrah calendar system
     */
    private int day;

    /**
     * The days passed from 01-01-01 Solar Date
     */
    private long solarEpochDays;
    private long dayOfYear;

    //-----------------------------------------------------------------------

    /**
     * Obtains the current {@code SolarHijrahDate} from the system clock in the default time-zone.
     * <p>
     * This will query the {@link Clock#systemDefaultZone() system clock} in the default
     * time-zone to obtain the current date.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @return the current date using the system clock and default time-zone, not null
     */
    public static SolarHijrahDate now() {
        return now(Clock.systemDefaultZone());
    }

    /**
     * Obtains the current {@code SolarHijrahDate} from the system clock in the specified time-zone.
     * <p>
     * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date.
     * Specifying the time-zone avoids dependence on the default time-zone.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @param zone the zone ID to use, not null
     * @return the current date using the system clock, not null
     */
    public static SolarHijrahDate now(ZoneId zone) {
        return now(Clock.system(zone));
    }

    /**
     * Obtains the current {@code SolarHijrahDate} from the specified clock.
     * <p>
     * This will query the specified clock to obtain the current date - today.
     * Using this method allows the use of an alternate clock for testing.
     * The alternate clock may be introduced using {@linkplain Clock dependency injection}.
     *
     * @param clock the clock to use, not null
     * @return the current date, not null
     * @throws DateTimeException if the current date cannot be obtained
     */
    public static SolarHijrahDate now(Clock clock) {
        return new SolarHijrahDate(LocalDate.now(clock));
    }

    /**
     * Obtains a {@code SolarHijrahDate} representing a date in the Solar Hijrah calendar
     * system from the proleptic-year, month-of-year and day-of-month fields.
     * <p>
     * This returns a {@code SolarHijrahDate} with the specified fields.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     *
     * @param prolepticYear the Gregorian proleptic-year
     * @param month         the Gregorian month-of-year, from 1 to 12
     * @param dayOfMonth    the Gregorian day-of-month, from 1 to 31
     * @return the date in Solar Hijrah calendar system, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static SolarHijrahDate of(int prolepticYear, int month, int dayOfMonth) {
        return SolarHijrahChronology.INSTANCE.date(prolepticYear, month, dayOfMonth);
    }

    /**
     * Obtains a {@code SolarHijrahDate} from a temporal object.
     * <p>
     * This obtains a date in the Solar Hijrah calendar system based on the specified temporal.
     * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
     * which this factory converts to an instance of {@code SolarHijrahDate}.
     * <p>
     * The conversion typically uses the {@link ChronoField#EPOCH_DAY EPOCH_DAY}
     * field, which is standardized across calendar systems.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery}
     * allowing it to be used as a query via method reference, {@code SolarHijrahDate::from}.
     *
     * @param temporal the temporal object to convert, not null
     * @return the date in Solar Hijrah calendar system, not null
     * @throws DateTimeException if unable to convert to a {@code SolarHijrahDate}
     */
    public static SolarHijrahDate from(TemporalAccessor temporal) {
        return SolarHijrahChronology.INSTANCE.date(temporal);
    }

    //-----------------------------------------------------------------------

    /**
     * Creates an instance from an ISO date.
     *
     * @param date the standard local date, validated not null
     */
    SolarHijrahDate(LocalDate date) {
        Jdk8Methods.requireNonNull(date, "date");
        this.isoDate = date;
        leapYears = SolarHijrahLeapYears.getInstance();
        calculateSolarHijrahDate(date);
    }

    private void calculateSolarHijrahDate(LocalDate date) {
        long epochDay = date.toEpochDay();
        long solarStart = SOLAR_HIJRAH_START_DATE.toEpochDay();
        solarEpochDays = epochDay - solarStart;
        int yearEst = (int) (solarEpochDays / 365);
        int leapYearsCount = leapYears.leapYearsTo(yearEst - 1);
        yearEst = (int) ((solarEpochDays - leapYearsCount) / 365);
        leapYearsCount = leapYears.leapYearsTo(yearEst - 1);
        yearEst += leapYearsCount / 365; // this will ensure that division by 365 for years will be accurate even if years that have one extra day (leap years) are more than 365
        year = yearEst;
        dayOfYear = solarEpochDays - year * 365 - leapYearsCount;
        if (dayOfYear >= 365 && leapYears.isLeapYear(year)) {
            year++;
            dayOfYear -= 365;
        }
        if (dayOfYear <= 31 * 6) {
            month = (int) ((dayOfYear) / 31) + 1;
            day = (int) (dayOfYear + (month > 1 ? 2 : 1) - (month - 1) * 31);
        } else {
            month = (int) ((dayOfYear - 31 * 6) / 30) + 7;
            day = (int) (dayOfYear + (month > 7 ? 2 : 1) - 31 * 6 - (month - 7) * 30);
        }
    }

    //-----------------------------------------------------------------------
    @Override
    public SolarHijrahChronology getChronology() {
        return SolarHijrahChronology.INSTANCE;
    }

    @Override
    public SolarHijrahEra getEra() {
        return (SolarHijrahEra) super.getEra();
    }

    @Override
    public int lengthOfMonth() {
        switch (month) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                return 30;
            case 12:
                return isLeapYear() ? 30 : 29;
            default:
                return 31;
        }
    }

    @Override
    public int lengthOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    @Override
    public ValueRange range(TemporalField field) {
        if (field instanceof ChronoField) {
            if (isSupported(field)) {
                ChronoField f = (ChronoField) field;
                switch (f) {
                    case DAY_OF_MONTH:
                        return ValueRange.of(1, lengthOfMonth());
                    case DAY_OF_YEAR:
                        return ValueRange.of(1, lengthOfYear());
                    case ALIGNED_WEEK_OF_MONTH:
                        return isoDate.range(field);
                    case YEAR_OF_ERA: {
                        ValueRange range = YEAR.range();
                        long max = (getProlepticYear() <= 0 ? -(range.getMinimum()) + 1 : range.getMaximum());
                        return ValueRange.of(1, max);
                    }
                }
                return getChronology().range(f);
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.rangeRefinedBy(this);
    }


    @Override
    public long getLong(TemporalField field) {
        if (field instanceof ChronoField) {
            switch ((ChronoField) field) {
                case PROLEPTIC_MONTH:
                    return getProlepticMonth();
                case YEAR_OF_ERA: {
                    int prolepticYear = getProlepticYear();
                    return (prolepticYear >= 1 ? prolepticYear : 1 - prolepticYear);
                }
                case YEAR:
                    return getProlepticYear();
                case ERA:
                    return (getProlepticYear() >= 1 ? 1 : 0);
                case EPOCH_DAY:
                    return toEpochDay();
                case DAY_OF_MONTH:
                    return day;
                case MONTH_OF_YEAR:
                    return month;
                case DAY_OF_YEAR:
                    return dayOfYear;
            }
            return isoDate.getLong(field);
        }
        return field.getFrom(this);
    }

    private long getProlepticMonth() {
        return getProlepticYear() * 12L + month - 1;
    }

    private int getProlepticYear() {
        return year;
    }

    //-----------------------------------------------------------------------
    @Override
    public SolarHijrahDate with(TemporalAdjuster adjuster) {
        return (SolarHijrahDate) super.with(adjuster);
    }

    @Override
    public SolarHijrahDate with(TemporalField field, long newValue) {
        if (field instanceof ChronoField) {
            ChronoField f = (ChronoField) field;
            if (getLong(f) == newValue) {
                return this;
            }
            switch (f) {
                case PROLEPTIC_MONTH:
                    getChronology().range(f).checkValidValue(newValue, f);
                    return plusMonths(newValue - getProlepticMonth());
                case YEAR_OF_ERA:
                case YEAR:
                case ERA: {
                    int nvalue = getChronology().range(f).checkValidIntValue(newValue, f);
                    switch (f) {
                        case YEAR_OF_ERA:
                            return with(isoDate.withYear((getProlepticYear() >= 1 ? nvalue : 1 - nvalue)));
                        case YEAR:
                            return with(isoDate.withYear(nvalue));
                        case ERA:
                            return with(isoDate.withYear((1 - getProlepticYear())));
                    }
                }
            }
            return with(isoDate.with(field, newValue));
        }
        return field.adjustInto(this, newValue);
    }

    @Override
    public SolarHijrahDate plus(TemporalAmount amount) {
        return (SolarHijrahDate) super.plus(amount);
    }

    @Override
    public SolarHijrahDate plus(long amountToAdd, TemporalUnit unit) {
        return (SolarHijrahDate) super.plus(amountToAdd, unit);
    }

    @Override
    public SolarHijrahDate minus(TemporalAmount amount) {
        return (SolarHijrahDate) super.minus(amount);
    }

    @Override
    public SolarHijrahDate minus(long amountToAdd, TemporalUnit unit) {
        return (SolarHijrahDate) super.minus(amountToAdd, unit);
    }

    //-----------------------------------------------------------------------
    @Override
    SolarHijrahDate plusYears(long years) {
        return with(isoDate.plusYears(years));
    }

    @Override
    SolarHijrahDate plusMonths(long months) {
        return with(isoDate.plusMonths(months));
    }

    @Override
    SolarHijrahDate plusDays(long days) {
        return with(isoDate.plusDays(days));
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public long getDayOfYear() {
        return dayOfYear;
    }

    private SolarHijrahDate with(LocalDate newDate) {
        return (newDate.equals(isoDate) ? this : new SolarHijrahDate(newDate));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ChronoLocalDateTime<SolarHijrahDate> atTime(LocalTime localTime) {
        return (ChronoLocalDateTime<SolarHijrahDate>) super.atTime(localTime);
    }

    @Override
    public ChronoPeriod until(ChronoLocalDate endDate) {
        Period period = isoDate.until(endDate);
        return getChronology().period(period.getYears(), period.getMonths(), period.getDays());
    }

    @Override  // override for performance
    public long toEpochDay() {
        return solarEpochDays;
    }

    //-------------------------------------------------------------------------
    @Override  // override for performance
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SolarHijrahDate) {
            SolarHijrahDate otherDate = (SolarHijrahDate) obj;
            return this.isoDate.equals(otherDate.isoDate);
        }
        return false;
    }

    @Override  // override for performance
    public int hashCode() {
        return getChronology().getId().hashCode() ^ isoDate.hashCode();
    }

    //-----------------------------------------------------------------------
    private Object writeReplace() {
        return new Ser(Ser.SOLAR_HIJRAH_DATE_TYPE, this);
    }

    void writeExternal(DataOutput out) throws IOException {
        out.writeInt(this.get(YEAR));
        out.writeByte(this.get(MONTH_OF_YEAR));
        out.writeByte(this.get(DAY_OF_MONTH));
    }

    static ChronoLocalDate readExternal(DataInput in) throws IOException {
        int year = in.readInt();
        int month = in.readByte();
        int dayOfMonth = in.readByte();
        return SolarHijrahChronology.INSTANCE.date(year, month, dayOfMonth);
    }

}
