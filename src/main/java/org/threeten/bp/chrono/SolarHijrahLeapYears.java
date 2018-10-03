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

import java.util.Arrays;
import java.util.List;

/**
 * This class stores Solar Hijrah leap years.
 * Solar Hijrah leap years are somewhat different from the Gregorian leap years
 * The leap years occur every 4 years but in cycles of 29,33,39 which are calculated with
 * the exact position of the earth one year is skipped and the leap year occurs after 5 years
 * the list {@code years} contains the leap years number
 * e.g. you can see that first exceptional leap year occurs in year 9
 * the first leap year is 4 and then instead of leap year happening in year 8
 * it happens at year 9
 */
public class SolarHijrahLeapYears {
    private static SolarHijrahLeapYears instance;

    private List<Integer> years;

    public static SolarHijrahLeapYears getInstance() {
        if (instance == null) {
            synchronized (SolarHijrahLeapYears.class) {
                if (instance == null) {
                    return new SolarHijrahLeapYears();
                }
            }
        }
        return instance;
    }

    private SolarHijrahLeapYears() {
        years = Arrays.asList(4, 9, 13, 17, 21, 25, 29, 33, 37, 42, 46, 50, 54, 58, 62, 66, 71, 75, 79, 83, 87, 91, 95, 99, 104, 108, 112, 116, 120, 124, 128, 132, 137, 141, 145, 149, 153, 157, 161, 165, 170, 174, 178, 182, 186, 190, 194, 198, 203, 207, 211, 215, 219, 223, 227, 231, 236, 240, 244, 248, 252, 256, 260, 264, 269, 273, 277, 281, 285, 289, 293, 297, 302, 306, 310, 314, 318, 322, 326, 331, 335, 339, 343, 347, 351, 355, 359, 364, 368, 372, 376, 380, 384, 388, 392, 397, 401, 405, 409, 413, 417, 421, 425, 430, 434, 438, 442, 446, 450, 454, 458, 463, 467, 471, 475, 479, 483, 487, 491, 496, 500, 504, 508, 512, 516, 520, 524, 529, 533, 537, 541, 545, 549, 553, 558, 562, 566, 570, 574, 578, 582, 586, 591, 595, 599, 603, 607, 611, 615, 619, 624, 628, 632, 636, 640, 644, 648, 652, 656, 661, 665, 669, 673, 677, 681, 685, 690, 694, 698, 702, 706, 710, 714, 718, 723, 727, 731, 735, 739, 743, 747, 751, 756, 760, 764, 768, 772, 776, 780, 784, 789, 793, 797, 801, 805, 809, 813, 817, 822, 826, 830, 834, 838, 842, 846, 850, 855, 859, 863, 867, 871, 875, 879, 883, 888, 892, 896, 900, 904, 908, 912, 916, 921, 925, 929, 933, 937, 941, 945, 949, 954, 958, 962, 966, 970, 974, 978, 983, 987, 991, 995, 999, 1003, 1007, 1011, 1016, 1020, 1024, 1028, 1032, 1036, 1040, 1044, 1049, 1053, 1057, 1061, 1065, 1069, 1073, 1077, 1082, 1086, 1090, 1094, 1098, 1102, 1106, 1110, 1115, 1119, 1123, 1127, 1131, 1135, 1139, 1143, 1148, 1152, 1156, 1160, 1164, 1168, 1172, 1176, 1181, 1181, 1185, 1189, 1193, 1197, 1201, 1205, 1209, 1214, 1218, 1222, 1226, 1230, 1234, 1238, 1243, 1247, 1251, 1255, 1259, 1263, 1267, 1271, 1275, 1280, 1284, 1288, 1292, 1296, 1300, 1304, 1308, 1313, 1317, 1321, 1325, 1329, 1333, 1337, 1341, 1346, 1350, 1354, 1358, 1362, 1366, 1370, 1375, 1379, 1383, 1387, 1391, 1395, 1399, 1403, 1408, 1412, 1416, 1420, 1424, 1428, 1432, 1436, 1441, 1445, 1449, 1453, 1457, 1461, 1465, 1469, 1473, 1478, 1483);
    }

    public boolean isLeapYear(int year) {
        return years.contains(year);
    }

    public int leapYearsTo(int year) {
        int mYear = year;
        while (mYear > 0) {
            if (years.contains(mYear)) {
                return years.indexOf(mYear) + 1;
            } else {
                mYear--;
            }
        }
        return 0;
    }


}
