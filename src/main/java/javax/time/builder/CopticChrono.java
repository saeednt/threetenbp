/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.builder;

import static javax.time.MathUtils.checkNotNull;
import static javax.time.MathUtils.safeToInt;
import static javax.time.builder.StandardDateTimeField.DAY_OF_MONTH;
import static javax.time.builder.StandardDateTimeField.DAY_OF_YEAR;
import static javax.time.builder.StandardDateTimeField.EPOCH_DAY;
import static javax.time.builder.StandardDateTimeField.MONTH_OF_YEAR;
import static javax.time.builder.StandardDateTimeField.YEAR;
import static javax.time.builder.StandardPeriodUnit.MONTHS;
import static javax.time.builder.StandardPeriodUnit.YEARS;

import javax.time.CalendricalException;
import javax.time.Duration;
import javax.time.LocalDate;
import javax.time.LocalDateTime;
import javax.time.LocalTime;
import javax.time.calendrical.DateTimeRuleRange;

/**
 * The Coptic calendar system.
 * <p>
 * This chronology defines the rules of the Coptic calendar system.
 * The Coptic calendar has twelve months of 30 days followed by an additional
 * period of 5 or 6 days, modeled as the thirteenth month in this implementation.
 * <p>
 * Years are measured in the 'Era of the Martyrs' - AM.
 * 0001-01-01 (Coptic) equals 0284-08-29 (ISO).
 * The supported range is from 1 to 99999999 (inclusive) in both eras.
 * <p>
 * This class is immutable and thread-safe.
 *
 * NB: currently exploratory, represented ranges should be rechecked, ignores eras, doesn't check ranges in arithmetic
 *
 * @author Richard Warburton
 */
public class CopticChrono extends StandardChrono implements Chrono, DateTimeRules, PeriodRules {

    /**
     * Singleton instance.
     */
    public static final CopticChrono INSTANCE = new CopticChrono();

    private static final int MONTHS_PER_YEAR = 13;

    /**
     * This is a Non-proleptic Calendar, and it starts at year 1.
     */
    private static final int MIN_YEAR = 1;
    /**
     * The maximum permitted year.
     */
    private static final int MAX_YEAR = 999999999;
    /**
     * The minimum permitted epoch-month.
     */
    private static final long MIN_EPOCH_MONTH = (MIN_YEAR - 1970L) * MONTHS_PER_YEAR;
    /**
     * The maximum permitted epoch-month.
     */
    private static final long MAX_EPOCH_MONTH = (MAX_YEAR - 1970L) * MONTHS_PER_YEAR - 1L;
    /**
     * The minimum permitted epoch-day.
     */
    private static final long MIN_EPOCH_DAY = 0;
    /**
     * The maximum permitted epoch-day.
     */
    private static final long MAX_EPOCH_DAY = (long) (MAX_YEAR * 365.25);
    
    private static final long EPOCH_DAYS_OFFSET = 615558;
    
    private static final long YEAR_BLOCK = 3 * 365 + 366;

    @Override
    public String getName() {
        return "Coptic";
    }

    //-----------------------------------------------------------------------
    @Override
    public DateTimeRuleRange getRange(DateTimeField field) {
        if (field instanceof StandardDateTimeField) {
            StandardDateTimeField standardField = (StandardDateTimeField) field;
            switch (standardField) {
                case ERA: return DateTimeRuleRange.of(0, 1);
                case YEAR: return DateTimeRuleRange.of(MIN_YEAR, MAX_YEAR);
                case YEAR_OF_ERA: return DateTimeRuleRange.of(1, MAX_YEAR);
                case EPOCH_MONTH: return DateTimeRuleRange.of(MIN_EPOCH_MONTH, MAX_EPOCH_MONTH);
                case MONTH_OF_YEAR: return DateTimeRuleRange.of(1, 13);
                case EPOCH_DAY: return DateTimeRuleRange.of(MIN_EPOCH_DAY, MAX_EPOCH_DAY);
                case DAY_OF_MONTH: return DateTimeRuleRange.of(1, 5, 30);
                case DAY_OF_YEAR: return DateTimeRuleRange.of(1, 365, 366);
                case DAY_OF_WEEK: return DateTimeRuleRange.of(1, 7);
                default: return getTimeRange(standardField);
            }
        }
        return field.implementationRules(this).getRange(field);
    }

    @Override
    public DateTimeRuleRange getRange(DateTimeField field, LocalDate date, LocalTime time) {
        if (field instanceof StandardDateTimeField) {
            if (date != null) {
                switch ((StandardDateTimeField) field) {
                    case DAY_OF_MONTH:
                        if (getMonthOfYear(date) == 13) {
                            return DateTimeRuleRange.of(1, isLeapYear(date) ? 6 : 5);
                        }
                        return DateTimeRuleRange.of(1, 30);
                    case DAY_OF_YEAR: return DateTimeRuleRange.of(1, isLeapYear(date) ? 366 : 365);
                }
            }
            return getRange(field);
        }
        return field.implementationRules(this).getRange(field, date, time);
    }

    private boolean isLeapYear(LocalDate date) {
        return (getYear(date) % 4) == 0;
    }

    //-----------------------------------------------------------------------
    @Override
    public long getDateValue(LocalDate date, DateTimeField field) {
        if (field instanceof StandardDateTimeField) {
            switch ((StandardDateTimeField) field) {
                case ERA: return (getYear(date) > 0 ? 1 : 0);   // TODO
                case YEAR: return getYear(date);
                case YEAR_OF_ERA: return (getYear(date) > 0 ? getYear(date) : 1 - getYear(date));   // TODO
                case EPOCH_MONTH: return ((getYear(date) - 1970) * 13L) + getMonthOfYear(date);
                case MONTH_OF_YEAR: return getMonthOfYear(date);
                case EPOCH_DAY: return getEpochDay(date);
                case DAY_OF_MONTH: return getDayOfMonth(date);
                case DAY_OF_YEAR: return getDayOfYear(date);
                case DAY_OF_WEEK: return date.getDayOfWeek().getValue();
            }
            throw new CalendricalException("Unsupported field");
        }
        return field.implementationRules(this).getDateValue(date, field);
    }

    /**
     * Abstracted common logic, readability.
     */
    private long getMonthOfYear(LocalDate date) {
        return (getDayOfYear(date) / 30) + 1;
    }
    
    private long getYear(LocalDate date) {
        long epochDay = getEpochDay(date);
        long yearPrefix = (epochDay / YEAR_BLOCK) * 4;
        long yearSuffix = (epochDay % YEAR_BLOCK) / 365;
        return 1 + (yearPrefix + yearSuffix);
    }
    
    private long getDayOfYear(LocalDate date) {
        long blockNumber = getEpochDay(date) % YEAR_BLOCK;
        long leapSum = (blockNumber / 365) == 3 ? 0 : 1;
        return leapSum + (blockNumber % 365);
    }
    
    private long getEpochDay(LocalDate date) {
        return EPOCH_DAYS_OFFSET + date.toEpochDay();
    }
    
    private long getDayOfMonth(LocalDate date) {
        return ((getDayOfYear(date) - 1) % 30) + 1;
    }

    //-----------------------------------------------------------------------
    @Override
    public LocalDate setDate(LocalDate date, DateTimeField field, long newValue) {
        if (field instanceof StandardDateTimeField) {
            if (getRange(field, date, null).isValidValue(newValue) == false) {
                throw new IllegalArgumentException();  // TODO
            }
            switch ((StandardDateTimeField) field) {
                case ERA: {   // TODO
                    if ((getYear(date) > 0 && newValue == 0) && (getYear(date)) <= 0 && newValue == 1) {
                        return date.withYear(1 - safeToInt(getYear(date)));
                    }
                    return date;
                }
                case YEAR: return setYear(date, newValue);
                case YEAR_OF_ERA: throw new UnsupportedOperationException("Not implemented yet");
                case EPOCH_MONTH: return setMonthOfYear(setYear(date, newValue / MONTHS_PER_YEAR), newValue % MONTHS_PER_YEAR);
                case MONTH_OF_YEAR: return setMonthOfYear(date, newValue);
                case EPOCH_DAY: return LocalDate.ofEpochDay(newValue - 615558);
                case DAY_OF_MONTH: return setDayOfYear(date, safeToInt((getMonthOfYear(date) - 1) * 30 + newValue));
                case DAY_OF_YEAR: return setDayOfYear(date, newValue);
                case DAY_OF_WEEK: return date.plusDays(newValue - date.getDayOfWeek().getValue());
            }
            throw new CalendricalException("Unsupported field on LocalDate: " + field);
        }
        return field.implementationRules(this).setDate(date, field, newValue);
    }
    
    private LocalDate setYear(LocalDate date, long newValue) {
        long diff = newValue - getYear(date);
        return date.plusYears(diff);
    }
    
    private LocalDate setDayOfYear(LocalDate date, long newValue) {
        long diff = newValue - getDayOfYear(date);
        return date.plusDays(diff);
    }
    
    private LocalDate setMonthOfYear(LocalDate date, long newValue) {
        long diff = newValue - getMonthOfYear(date);
        return date.plusMonths(diff);
    }

    //-----------------------------------------------------------------------
    @Override
    public LocalDate setDateLenient(LocalDate date, DateTimeField field, long newValue) {
        return null;
    }

    //-----------------------------------------------------------------------    
    @Override
    public LocalDate addToDate(LocalDate date, PeriodUnit unit, long amount) {
        if (amount == 0) {
            return date;
        }
        
        if (unit instanceof StandardPeriodUnit) {
            StandardPeriodUnit periodUnit = (StandardPeriodUnit) unit;
            StandardDateTimeField field;
            switch (periodUnit) {
            case DAYS:      field = DAY_OF_YEAR; break;
            case WEEKS:     field = DAY_OF_YEAR; amount *= 7; break;
            case MONTHS:    {
                field = DAY_OF_YEAR;
                long month = getDateValue(date, MONTH_OF_YEAR), added = month + amount;
                amount *= 30;
                if (month <= 13 && added >= 13) {
                    amount -= isLeapYear(date) ? 24 : 25;
                }
                // TODO: year overflow
                break;
            }
            case YEARS:     field = YEAR; break;
            case DECADES:   field = YEAR; amount *= 10; break;
            case CENTURIES: field = YEAR; amount *= 100; break;
            case MILLENIA:  field = YEAR; amount *= 1000; break;
            case FOREVER:
            default:
                throw new IllegalArgumentException(); // TODO
            }
            DateTimeRuleRange range = getRange(field, date, null);
            long max = range.getMaximum(), min = range.getMinimum(), diff = max - min;
            long summed = getDateValue(date, field) + amount;
            long newValue = summed % max, addNext = summed / diff;
            LocalDate newDate = setDate(date, field, newValue);
            if (addNext > 0) {
                if (field == YEAR) {
                    throw new ArithmeticException("Year overflow"); // TODO
                }
                newDate = addToDate(newDate, YEARS, addNext);
            }
            return newDate;
        } else {
            return unit.implementationRules(this).addToDate(date, unit, amount);
        }
    }

    //-----------------------------------------------------------------------
    @Override
    public long getPeriodBetweenDates(PeriodUnit unit, LocalDate date1, LocalDate date2) {
        return 0;
    }

    @Override
    public long getPeriodBetweenTimes(PeriodUnit unit, LocalTime time1, LocalTime time2) {
        return 0;
    }

    @Override
    public long getPeriodBetweenDateTimes(PeriodUnit unit, LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return 0;
    }
    
    @Override
    public Duration getEstimatedDuration(PeriodUnit unit) {
        if (unit instanceof StandardPeriodUnit) {
            StandardPeriodUnit standardUnit = (StandardPeriodUnit) unit;
            if (standardUnit == MONTHS) {
                return Duration.ofSeconds(31556952L / 13);
            } else {
                return standardUnit.getEstimatedDuration();
            }
        } else {
            return unit.implementationRules(this).getEstimatedDuration(unit);
        }
    }

    @Override
    public LocalDate buildDate(DateTimeBuilder builder) {
        checkNotNull(builder, "builder cannot be null");
        if (builder.hasAllFields(YEAR, MONTH_OF_YEAR, DAY_OF_MONTH)) {
            LocalDate date = setDate(LocalDate.now(), YEAR, builder.getInt(YEAR));
            date = setDate(date, MONTH_OF_YEAR, builder.getInt(MONTH_OF_YEAR));
            return setDate(date, DAY_OF_MONTH, builder.getInt(DAY_OF_MONTH));
        } else if (builder.hasAllFields(EPOCH_DAY)) {
            return LocalDate.ofEpochDay(builder.getValue(EPOCH_DAY));
        } else if (builder.hasAllFields(YEAR, DAY_OF_YEAR)) {
            LocalDate date = setDate(LocalDate.now(), YEAR, builder.getInt(YEAR));
            return setDate(date, DAY_OF_YEAR, builder.getInt(DAY_OF_YEAR));
        }
        throw new CalendricalException("Unable to build Date due to missing fields"); // TODO
    }
    
}
