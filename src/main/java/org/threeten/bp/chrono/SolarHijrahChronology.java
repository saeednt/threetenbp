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
import org.threeten.bp.format.ResolverStyle;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.ValueRange;

import java.io.Serializable;
import java.util.*;

import static org.threeten.bp.temporal.ChronoField.*;
import static org.threeten.bp.temporal.ChronoUnit.*;
import static org.threeten.bp.temporal.TemporalAdjusters.nextOrSame;

/**
 * The Persian calendar system.
 * <p>
 * This chronology defines the rules of the Solar Hijrah calendar system which is based on the Jalali calendar system.
 * This calendar system is primarily used in Islamic Republic of Iran.
 * Dates are aligned such that {@code 01-01-01 (Solar Hijrah)} is {@code 622-03-22 (ISO)}.
 * <p>
 * The fields are defined as follows:
 * <p><ul>
 * <li>era - There are two eras, the current 'After Hijrah' (AH) and the previous era 'Before Hijrah' (BH).
 * <li>year-of-era - The year-of-era for the current era increases uniformly from the epoch at year one.
 *  For the previous era the year increases from one as time goes backwards.
 *  The value for the current era is equal to the ISO proleptic-year minus 622 or 621 depending on the time of the year.
 * <li>proleptic-year - The proleptic year is the same as the year-of-era for the
 *  current era. For the previous era, years have zero, then negative values.
 *  The value is equal to the ISO proleptic-year minus 622 or 621.
 * <li>month-of-year - The Solar Hijrah month-of-year exactly matches ISO.
 * <p><ul>
 *  * <li>A year has 12 months.</li>
 *  * <li>There are 31 days in month number 1, 2, 3, 4, 5, and 6,
 *  * and 30 days in month number 7, 8, 9, 10, 11 and 29 days in month number 12.</li>
 *  * <li>In a leap year month 12 has 30 days.</li>
 *  * </ul><p>
 *  * <P>
 *  * The table shows the features described above.
 *  * <blockquote>
 *  * <table border="1">
 *  *   <tbody>
 *  *     <tr>
 *  *       <th># of month</th>
 *  *       <th>Name of month</th>
 *  *       <th>Number of days</th>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>1</td>
 *  *       <td>Farvardin</td>
 *  *       <td>31</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>2</td>
 *  *       <td>Ordibehesht</td>
 *  *       <td>31</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>3</td>
 *  *       <td>Khordad</td>
 *  *       <td>31</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>4</td>
 *  *       <td>Tir</td>
 *  *       <td>31</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>5</td>
 *  *       <td>Mordad</td>
 *  *       <td>31</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>6</td>
 *  *       <td>Shahrivar</td>
 *  *       <td>31</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>7</td>
 *  *       <td>Mehr</td>
 *  *       <td>30</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>8</td>
 *  *       <td>Aban</td>
 *  *       <td>30</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>9</td>
 *  *       <td>Azar</td>
 *  *       <td>30</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>10</td>
 *  *       <td>Dey</td>
 *  *       <td>30</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>11</td>
 *  *       <td>Bahman</td>
 *  *       <td>30</td>
 *  *     </tr>
 *  *     <tr>
 *  *       <td>12</td>
 *  *       <td>Esfand</td>
 *  *       <td>29, but 30 days in leap years</td>
 *  *     </tr>
 *  *   </tbody>
 *  * </table>
 *  * </blockquote>
 *
 * <li>day-of-month
 * <li>day-of-year
 * <li>leap-year
 * </ul><p>
 *
 * <h3>Specification for implementors</h3>
 * This class is immutable and thread-safe.
 */
public final class SolarHijrahChronology extends Chronology implements Serializable {

    /**
     * Singleton instance of the Solar Hijrah chronology.
     */
    public static final SolarHijrahChronology INSTANCE = new SolarHijrahChronology();

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 2775954514031616474L;
    /**
     * Narrow names for eras.
     */
    private static final HashMap<String, String[]> ERA_NARROW_NAMES = new HashMap<String, String[]>();
    /**
     * Short names for eras.
     */
    private static final HashMap<String, String[]> ERA_SHORT_NAMES = new HashMap<String, String[]>();
    /**
     * Full names for eras.
     */
    private static final HashMap<String, String[]> ERA_FULL_NAMES = new HashMap<String, String[]>();
    /**
     * Fallback language for the era names.
     */
    private static final String FALLBACK_LANGUAGE = "en";
    /**
     * Language that has the era names.
     */
    private static final String TARGET_LANGUAGE = "fa";

    static {
        ERA_NARROW_NAMES.put(FALLBACK_LANGUAGE, new String[]{"BH", "AH"});
        ERA_NARROW_NAMES.put(TARGET_LANGUAGE, new String[]{"ق.ه.", "ب.ه."});
        ERA_SHORT_NAMES.put(FALLBACK_LANGUAGE, new String[]{"B.H.", "A.H."});
        ERA_SHORT_NAMES.put(TARGET_LANGUAGE,
                new String[]{"\u0647.\u0642.",
                "\u0647.\u0628"});
        ERA_FULL_NAMES.put(FALLBACK_LANGUAGE, new String[]{"Before Hijrah", "After Hijrah"});
        ERA_FULL_NAMES.put(TARGET_LANGUAGE,
                new String[]{"\u0642\u0628\u0644\20\u0627\u0632\20\u0647\u062C\u0631\u062A",
                "\u0628\u0639\u062F\20\u0627\u0632\20\u0647\u062C\u0631\u062A"});
    }

    /**
     * Restricted constructor.
     */
    private SolarHijrahChronology() {
    }

    /**
     * Resolve singleton.
     *
     * @return the singleton instance, not null
     */
    private Object readResolve() {
        return INSTANCE;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the ID of the chronology - 'Solar Hijrah'.
     * <p>
     * The ID uniquely identifies the {@code Chronology}.
     * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
     *
     * @return the chronology ID - 'SolarHijrah'
     * @see #getCalendarType()
     */
    @Override
    public String getId() {
        return "SolarHijrah";
    }

    /**
     * Gets the calendar type of the underlying calendar system - 'Solar Hijrah'.
     * <p>
     * The calendar type is an identifier defined by the
     * <em>Unicode Locale Data Markup Language (LDML)</em> specification.
     * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
     * It can also be used as part of a locale, accessible via
     * {@link Locale#getUnicodeLocaleType(String)} with the key 'ca'.
     *
     * @return the calendar system type - 'Solar Hijrah'
     * @see #getId()
     */
    @Override
    public String getCalendarType() {
        return "Solar Hijrah";
    }

    //-----------------------------------------------------------------------
    @Override  // override with covariant return type
    public SolarHijrahDate date(Era era, int yearOfEra, int month, int dayOfMonth) {
        return (SolarHijrahDate) super.date(era, yearOfEra, month, dayOfMonth);
    }

    @Override  // override with covariant return type
    public SolarHijrahDate date(int prolepticYear, int month, int dayOfMonth) {
        return new SolarHijrahDate(LocalDate.of(prolepticYear, month, dayOfMonth));
    }

    @Override  // override with covariant return type
    public SolarHijrahDate dateYearDay(Era era, int yearOfEra, int dayOfYear) {
        return (SolarHijrahDate) super.dateYearDay(era, yearOfEra, dayOfYear);
    }

    @Override  // override with covariant return type
    public SolarHijrahDate dateYearDay(int prolepticYear, int dayOfYear) {
        return new SolarHijrahDate(LocalDate.ofYearDay(prolepticYear, dayOfYear));
    }

    @Override
    public SolarHijrahDate dateEpochDay(long epochDay) {
        return new SolarHijrahDate(LocalDate.ofEpochDay(epochDay));
    }

    //-----------------------------------------------------------------------
    @Override  // override with covariant return type
    public SolarHijrahDate date(TemporalAccessor temporal) {
        if (temporal instanceof SolarHijrahDate) {
            return (SolarHijrahDate) temporal;
        }
        return new SolarHijrahDate(LocalDate.from(temporal));
    }

    @SuppressWarnings("unchecked")
    @Override  // override with covariant return type
    public ChronoLocalDateTime<SolarHijrahDate> localDateTime(TemporalAccessor temporal) {
        return (ChronoLocalDateTime<SolarHijrahDate>) super.localDateTime(temporal);
    }

    @SuppressWarnings("unchecked")
    @Override  // override with covariant return type
    public ChronoZonedDateTime<SolarHijrahDate> zonedDateTime(TemporalAccessor temporal) {
        return (ChronoZonedDateTime<SolarHijrahDate>) super.zonedDateTime(temporal);
    }

    @SuppressWarnings("unchecked")
    @Override  // override with covariant return type
    public ChronoZonedDateTime<SolarHijrahDate> zonedDateTime(Instant instant, ZoneId zone) {
        return (ChronoZonedDateTime<SolarHijrahDate>) super.zonedDateTime(instant, zone);
    }

    //-----------------------------------------------------------------------
    @Override  // override with covariant return type
    public SolarHijrahDate dateNow() {
        return (SolarHijrahDate) super.dateNow();
    }

    @Override  // override with covariant return type
    public SolarHijrahDate dateNow(ZoneId zone) {
        return (SolarHijrahDate) super.dateNow(zone);
    }

    @Override  // override with covariant return type
    public SolarHijrahDate dateNow(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        return (SolarHijrahDate) super.dateNow(clock);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the specified year is a leap year.
     * <p>
     * Solar Hijrah leap years are somewhat different from the Gregorian leap years
     * The leap years occur every 4 years but in cycles of 29,33,39 which are calculated with
     * the exact position of the earth one year is skipped and the leap year occurs after 5 years
     * This method does not validate the year passed in, and only has a
     * well-defined result for years in the supported range.
     *
     * @param prolepticYear  the proleptic-year to check, not validated for range
     * @return true if the year is a leap year
     */
    @Override
    public boolean isLeapYear(long prolepticYear) {
        return SolarHijrahLeapYears.getInstance().isLeapYear((int) prolepticYear);
    }

    @Override
    public int prolepticYear(Era era, int yearOfEra) {
        if (!(era instanceof SolarHijrahEra)) {
            throw new ClassCastException("Era must be SolarHijrahEra");
        }
        return (era == SolarHijrahEra.AH ? yearOfEra : 1 - yearOfEra);
    }

    @Override
    public SolarHijrahEra eraOf(int eraValue) {
        return SolarHijrahEra.of(eraValue);
    }

    @Override
    public List<Era> eras() {
        return Arrays.<Era>asList(SolarHijrahEra.values());
    }

    //-----------------------------------------------------------------------
    @Override
    public ValueRange range(ChronoField field) {
        switch (field) {
            case PROLEPTIC_MONTH: {
                return PROLEPTIC_MONTH.range();
            }
            case YEAR_OF_ERA: {
                ValueRange range = YEAR.range();
                return ValueRange.of(1, -(range.getMinimum()) + 1, range.getMaximum());
            }
            case YEAR: {
                return YEAR.range();
            }
        }
        return field.range();
    }

    @Override
    public SolarHijrahDate resolveDate(Map<TemporalField, Long> fieldValues, ResolverStyle resolverStyle) {
        if (fieldValues.containsKey(EPOCH_DAY)) {
            return dateEpochDay(fieldValues.remove(EPOCH_DAY));
        }

        // normalize fields
        Long prolepticMonth = fieldValues.remove(PROLEPTIC_MONTH);
        if (prolepticMonth != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                PROLEPTIC_MONTH.checkValidValue(prolepticMonth);
            }
            updateResolveMap(fieldValues, MONTH_OF_YEAR, Jdk8Methods.floorMod(prolepticMonth, 12) + 1);
            updateResolveMap(fieldValues, YEAR, Jdk8Methods.floorDiv(prolepticMonth, 12));
        }

        // eras
        Long yoeLong = fieldValues.remove(YEAR_OF_ERA);
        if (yoeLong != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                YEAR_OF_ERA.checkValidValue(yoeLong);
            }
            Long era = fieldValues.remove(ERA);
            if (era == null) {
                Long year = fieldValues.get(YEAR);
                if (resolverStyle == ResolverStyle.STRICT) {
                    // do not invent era if strict, but do cross-check with year
                    if (year != null) {
                        updateResolveMap(fieldValues, YEAR, (year > 0 ? yoeLong: Jdk8Methods.safeSubtract(1, yoeLong)));
                    } else {
                        // reinstate the field removed earlier, no cross-check issues
                        fieldValues.put(YEAR_OF_ERA, yoeLong);
                    }
                } else {
                    // invent era
                    updateResolveMap(fieldValues, YEAR, (year == null || year > 0 ? yoeLong: Jdk8Methods.safeSubtract(1, yoeLong)));
                }
            } else if (era == 1L) {
                updateResolveMap(fieldValues, YEAR, yoeLong);
            } else if (era == 0L) {
                updateResolveMap(fieldValues, YEAR, Jdk8Methods.safeSubtract(1, yoeLong));
            } else {
                throw new DateTimeException("Invalid value for era: " + era);
            }
        } else if (fieldValues.containsKey(ERA)) {
            ERA.checkValidValue(fieldValues.get(ERA));  // always validated
        }

        // build date
        if (fieldValues.containsKey(YEAR)) {
            if (fieldValues.containsKey(MONTH_OF_YEAR)) {
                if (fieldValues.containsKey(DAY_OF_MONTH)) {
                    int y = YEAR.checkValidIntValue(fieldValues.remove(YEAR));
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        long months = Jdk8Methods.safeSubtract(fieldValues.remove(MONTH_OF_YEAR), 1);
                        long days = Jdk8Methods.safeSubtract(fieldValues.remove(DAY_OF_MONTH), 1);
                        return date(y, 1, 1).plusMonths(months).plusDays(days);
                    } else {
                        int moy = range(MONTH_OF_YEAR).checkValidIntValue(fieldValues.remove(MONTH_OF_YEAR), MONTH_OF_YEAR);
                        int dom = range(DAY_OF_MONTH).checkValidIntValue(fieldValues.remove(DAY_OF_MONTH), DAY_OF_MONTH);
                        if (resolverStyle == ResolverStyle.SMART && dom > 28) {
                            dom = Math.min(dom, date(y, moy, 1).lengthOfMonth());
                        }
                        return date(y, moy, dom);
                    }
                }
                if (fieldValues.containsKey(ALIGNED_WEEK_OF_MONTH)) {
                    if (fieldValues.containsKey(ALIGNED_DAY_OF_WEEK_IN_MONTH)) {
                        int y = YEAR.checkValidIntValue(fieldValues.remove(YEAR));
                        if (resolverStyle == ResolverStyle.LENIENT) {
                            long months = Jdk8Methods.safeSubtract(fieldValues.remove(MONTH_OF_YEAR), 1);
                            long weeks = Jdk8Methods.safeSubtract(fieldValues.remove(ALIGNED_WEEK_OF_MONTH), 1);
                            long days = Jdk8Methods.safeSubtract(fieldValues.remove(ALIGNED_DAY_OF_WEEK_IN_MONTH), 1);
                            return date(y, 1, 1).plus(months, MONTHS).plus(weeks, WEEKS).plus(days, DAYS);
                        }
                        int moy = MONTH_OF_YEAR.checkValidIntValue(fieldValues.remove(MONTH_OF_YEAR));
                        int aw = ALIGNED_WEEK_OF_MONTH.checkValidIntValue(fieldValues.remove(ALIGNED_WEEK_OF_MONTH));
                        int ad = ALIGNED_DAY_OF_WEEK_IN_MONTH.checkValidIntValue(fieldValues.remove(ALIGNED_DAY_OF_WEEK_IN_MONTH));
                        SolarHijrahDate date = date(y, moy, 1).plus((aw - 1) * 7 + (ad - 1), DAYS);
                        if (resolverStyle == ResolverStyle.STRICT && date.get(MONTH_OF_YEAR) != moy) {
                            throw new DateTimeException("Strict mode rejected date parsed to a different month");
                        }
                        return date;
                    }
                    if (fieldValues.containsKey(DAY_OF_WEEK)) {
                        int y = YEAR.checkValidIntValue(fieldValues.remove(YEAR));
                        if (resolverStyle == ResolverStyle.LENIENT) {
                            long months = Jdk8Methods.safeSubtract(fieldValues.remove(MONTH_OF_YEAR), 1);
                            long weeks = Jdk8Methods.safeSubtract(fieldValues.remove(ALIGNED_WEEK_OF_MONTH), 1);
                            long days = Jdk8Methods.safeSubtract(fieldValues.remove(DAY_OF_WEEK), 1);
                            return date(y, 1, 1).plus(months, MONTHS).plus(weeks, WEEKS).plus(days, DAYS);
                        }
                        int moy = MONTH_OF_YEAR.checkValidIntValue(fieldValues.remove(MONTH_OF_YEAR));
                        int aw = ALIGNED_WEEK_OF_MONTH.checkValidIntValue(fieldValues.remove(ALIGNED_WEEK_OF_MONTH));
                        int dow = DAY_OF_WEEK.checkValidIntValue(fieldValues.remove(DAY_OF_WEEK));
                        SolarHijrahDate date = date(y, moy, 1).plus(aw - 1, WEEKS).with(nextOrSame(DayOfWeek.of(dow)));
                        if (resolverStyle == ResolverStyle.STRICT && date.get(MONTH_OF_YEAR) != moy) {
                            throw new DateTimeException("Strict mode rejected date parsed to a different month");
                        }
                        return date;
                    }
                }
            }
            if (fieldValues.containsKey(DAY_OF_YEAR)) {
                int y = YEAR.checkValidIntValue(fieldValues.remove(YEAR));
                if (resolverStyle == ResolverStyle.LENIENT) {
                    long days = Jdk8Methods.safeSubtract(fieldValues.remove(DAY_OF_YEAR), 1);
                    return dateYearDay(y, 1).plusDays(days);
                }
                int doy = DAY_OF_YEAR.checkValidIntValue(fieldValues.remove(DAY_OF_YEAR));
                return dateYearDay(y, doy);
            }
            if (fieldValues.containsKey(ALIGNED_WEEK_OF_YEAR)) {
                if (fieldValues.containsKey(ALIGNED_DAY_OF_WEEK_IN_YEAR)) {
                    int y = YEAR.checkValidIntValue(fieldValues.remove(YEAR));
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        long weeks = Jdk8Methods.safeSubtract(fieldValues.remove(ALIGNED_WEEK_OF_YEAR), 1);
                        long days = Jdk8Methods.safeSubtract(fieldValues.remove(ALIGNED_DAY_OF_WEEK_IN_YEAR), 1);
                        return date(y, 1, 1).plus(weeks, WEEKS).plus(days, DAYS);
                    }
                    int aw = ALIGNED_WEEK_OF_YEAR.checkValidIntValue(fieldValues.remove(ALIGNED_WEEK_OF_YEAR));
                    int ad = ALIGNED_DAY_OF_WEEK_IN_YEAR.checkValidIntValue(fieldValues.remove(ALIGNED_DAY_OF_WEEK_IN_YEAR));
                    SolarHijrahDate date = date(y, 1, 1).plusDays((aw - 1) * 7 + (ad - 1));
                    if (resolverStyle == ResolverStyle.STRICT && date.get(YEAR) != y) {
                        throw new DateTimeException("Strict mode rejected date parsed to a different year");
                    }
                    return date;
                }
                if (fieldValues.containsKey(DAY_OF_WEEK)) {
                    int y = YEAR.checkValidIntValue(fieldValues.remove(YEAR));
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        long weeks = Jdk8Methods.safeSubtract(fieldValues.remove(ALIGNED_WEEK_OF_YEAR), 1);
                        long days = Jdk8Methods.safeSubtract(fieldValues.remove(DAY_OF_WEEK), 1);
                        return date(y, 1, 1).plus(weeks, WEEKS).plus(days, DAYS);
                    }
                    int aw = ALIGNED_WEEK_OF_YEAR.checkValidIntValue(fieldValues.remove(ALIGNED_WEEK_OF_YEAR));
                    int dow = DAY_OF_WEEK.checkValidIntValue(fieldValues.remove(DAY_OF_WEEK));
                    SolarHijrahDate date = date(y, 1, 1).plus(aw - 1, WEEKS).with(nextOrSame(DayOfWeek.of(dow)));
                    if (resolverStyle == ResolverStyle.STRICT && date.get(YEAR) != y) {
                        throw new DateTimeException("Strict mode rejected date parsed to a different month");
                    }
                    return date;
                }
            }
        }
        return null;
    }

}
