/*
 * Copyright (c) 2018 (https://github.com/loong10k).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.codahale.metrics.sigar.utils;

/**
 * Metric length unit constants and conversion stubs.
 *
 * <p>Defines constants from attometers (am) through gigameters (Gm),
 * light-years (ly), and astronomical units (au).</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see LengthBritishUnit
 */
public enum LengthMetricUnit {

	/** Attometer constant (10^-18 m). */
	Attometer{



	};

	// Handy constants for conversion methods

	/** 1 Attometer (10^-18 m). */
    static final long AM = 1L;
	/** 1 Femtometer = 1000 am. */
    static final long FM = AM * 1000L;
	/** 1 Picometer = 1000 fm. */
    static final long PM = FM * 1000L;
    /** 1 Nanometer = 1000 pm. */
    static final long NM = PM * 1000L;
    /** 1 Micrometer = 1000 nm. */
    static final long UM = NM * 1000L;
    /** 1 Centimillimeter = 10 um. */
    static final long CMM = UM * 10L;
    /** 1 Decimillimeter = 10 cmm. */
    static final long DMM = CMM * 10L;
	/** 1 Millimeter = 10 dmm. */
    static final long MM = DMM * 10L;
    /** 1 Centimeter = 10 mm. */
    static final long CM = MM * 10L;
    /** 1 Decimeter = 10 cm. */
    static final long DM = CM * 10L;
    /** 1 Meter = 10 dm. */
    static final long M = DM * 10L;
    /** 1 Kilometer = 1000 m. */
    static final long KM = M * 1000L;
    /** 1 Megameter = 1000 km. */
    static final long Mm = KM * 1000L;
    /** 1 Gigameter = 1000 Mm. */
    static final long Gm = Mm * 1000L;
    /** 1 Light-year = 9,460,730,472,581 km. */
    static final long LY = KM * 9460730472581L;
    /** 1 Astronomical Unit = 0.0000158 light-years. */
    static final float AU = LY * 0.0000158F;

    /**
     * Scales {@code d} by {@code m}, checking for overflow.
     *
     * @param d    the value to scale
     * @param m    the multiplier
     * @param over the overflow threshold
     * @return the scaled value, clamped to safe bounds
     */
    static long x(long d, long m, long over) {
        if (d >  over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }

    /**
     * Converts a value from the given source unit to this unit.
     *
     * @param sourceDuration the value to convert
     * @param sourceUnit     the source unit
     * @return the converted value
     * @throws AbstractMethodError always (not yet implemented)
     */
    public long convert(long sourceDuration, LengthMetricUnit sourceUnit) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to bits.
     *
     * @param duration the value in this unit
     * @return the equivalent in bits
     * @throws AbstractMethodError always (not yet implemented)
     */
    public long toBits(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to bytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in bytes
     * @throws AbstractMethodError always (not yet implemented)
     */
    public long toBytes(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to kilobytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in kilobytes
     * @throws AbstractMethodError always (not yet implemented)
     */
    public long toKilobytes(long duration) {
        throw new AbstractMethodError();
    }


}
