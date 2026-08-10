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
 * Enumeration of digital storage capacity units providing mutual
 * conversion methods.
 *
 * <p>Covers units from {@code BYTES} through {@code DOGGABYTES}
 * (10^33 bytes), with each constant overriding conversion methods
 * for all other units. Includes overflow protection via
 * {@link #x(double, double, double)}.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see CapacityUtils
 */
public enum CapacityUnit {



	/** No specific unit specified. */
	NONE,
	/**
	 * 1B = 8bit.
	 */
	BYTES{

		public double toBits(double d) { return d * 8; }
	    public double toBytes(double d) { return d; }
	    public double toKilobytes(double d) { return d/(KB/B); }
	    public double toMebibytes(double d) { return d/(MB/B); }
	    public double toGigabytes(double d) { return d/(GB/B); }
	    public double toTerabytes(double d) { return d/(TB/B); }
	    public double toPetabytes(double d) { return d/(PB/B); }
	    public double toExabytes(double d) { return d/(EB/B); }
	    public double toZettabytes(double d) { return d/(ZB/B); }
	    public double toYottabytes(double d) { return d/(YB/B); }
	    public double toBrontobytes(double d) { return d/(BB/B); }
	    public double toNonaBytes(double d) { return d/(NB/B); }
	    public double toDoggaBytes(double d) { return d/(DB/B); }
        public double convert(double d, CapacityUnit u) { return u.toKilobytes(d); }

	},
	/**
	 * 1KB (Kilobyte) = 1024 Bytes.
	 */
	KILOBYTES{

		public double toBits(double d) { return x(d, KB/B, MAX/(KB/B)) * 8; }
	    public double toBytes(double d) { return x(d, KB/B, MAX/(KB/B)); }
	    public double toKilobytes(double d) { return d; }
	    public double toMebibytes(double d) { return d/(MB/KB); }
	    public double toGigabytes(double d) { return d/(GB/KB); }
	    public double toTerabytes(double d) { return d/(TB/KB); }
	    public double toPetabytes(double d) { return d/(PB/KB); }
	    public double toExabytes(double d) { return d/(EB/KB); }
	    public double toZettabytes(double d) { return d/(ZB/KB); }
	    public double toYottabytes(double d) { return d/(YB/KB); }
	    public double toBrontobytes(double d) { return d/(BB/KB); }
	    public double toNonaBytes(double d) { return d/(NB/KB); }
	    public double toDoggaBytes(double d) { return d/(DB/KB); }
        public double convert(double d, CapacityUnit u) { return u.toKilobytes(d); }

	},
	/**
	 * 1MB (Megabyte) = 1024 KB.
	 */
	MEGABYTES{

		public double toBits(double d) { return x(d, MB/B, MAX/(MB/B)) * 8; }
	    public double toBytes(double d) { return x(d, MB/B, MAX/(MB/B)); }
	    public double toKilobytes(double d) { return x(d, MB/KB, MAX/(MB/KB)); }
	    public double toMebibytes(double d) { return d; }
	    public double toGigabytes(double d) { return d/(GB/MB); }
	    public double toTerabytes(double d) { return d/(TB/MB); }
	    public double toPetabytes(double d) { return d/(PB/MB); }
	    public double toExabytes(double d) { return d/(EB/MB); }
	    public double toZettabytes(double d) { return d/(ZB/MB); }
	    public double toYottabytes(double d) { return d/(YB/MB); }
	    public double toBrontobytes(double d) { return d/(BB/MB); }
	    public double toNonaBytes(double d) { return d/(NB/MB); }
	    public double toDoggaBytes(double d) { return d/(DB/MB); }
        public double convert(double d, CapacityUnit u) { return u.toMebibytes(d); }

	},
	/**
	 * 1GB (Gigabyte) = 1024 MB.
	 */
	GIGABYTES{

		public double toBits(double d) { return x(d, GB/B, MAX/(GB/B)) * 8; }
	    public double toBytes(double d) { return x(d, GB/B, MAX/(GB/B)); }
	    public double toKilobytes(double d) { return x(d, GB/KB, MAX/(GB/KB)); }
	    public double toMebibytes(double d) { return x(d, GB/MB, MAX/(GB/MB)); }
	    public double toGigabytes(double d) { return d; }
	    public double toTerabytes(double d) { return d/(TB/GB); }
	    public double toPetabytes(double d) { return d/(PB/GB); }
	    public double toExabytes(double d) { return d/(EB/GB); }
	    public double toZettabytes(double d) { return d/(ZB/GB); }
	    public double toYottabytes(double d) { return d/(YB/GB); }
	    public double toBrontobytes(double d) { return d/(BB/GB); }
	    public double toNonaBytes(double d) { return d/(NB/GB); }
	    public double toDoggaBytes(double d) { return d/(DB/GB); }
        public double convert(double d, CapacityUnit u) { return u.toGigabytes(d); }

	},
	/**
	 * 1TB (Terabyte) = 1024 GB.
	 */
	TRILLIONBYTES{

		public double toBits(double d) { return x(d, TB/B, MAX/(TB/B)) * 8; }
	    public double toBytes(double d) { return x(d, TB/B, MAX/(TB/B)); }
	    public double toKilobytes(double d) { return x(d, TB/KB, MAX/(TB/KB)); }
	    public double toMebibytes(double d) { return x(d, TB/MB, MAX/(TB/MB)); }
	    public double toGigabytes(double d) { return x(d, TB/GB, MAX/(TB/GB)); }
	    public double toTerabytes(double d) { return d; }
	    public double toPetabytes(double d) { return d/(PB/TB); }
	    public double toExabytes(double d) { return d/(EB/TB); }
	    public double toZettabytes(double d) { return d/(ZB/TB); }
	    public double toYottabytes(double d) { return d/(YB/TB); }
	    public double toBrontobytes(double d) { return d/(BB/TB); }
	    public double toNonaBytes(double d) { return d/(NB/TB); }
	    public double toDoggaBytes(double d) { return d/(DB/TB); }
        public double convert(double d, CapacityUnit u) { return u.toTerabytes(d); }

	},
	/**
	 * 1PB (Petabyte) = 1024 TB.
	 */
	PETABYTES{

		public double toBits(double d) { return x(d, PB/B, MAX/(PB/B)) * 8; }
	    public double toBytes(double d) { return x(d, PB/B, MAX/(PB/B)); }
	    public double toKilobytes(double d) { return x(d, PB/KB, MAX/(PB/KB)); }
	    public double toMebibytes(double d) { return x(d, PB/MB, MAX/(PB/MB)); }
	    public double toGigabytes(double d) { return x(d, PB/GB, MAX/(PB/GB)); }
	    public double toTerabytes(double d) { return x(d, PB/TB, MAX/(PB/TB)); }
	    public double toPetabytes(double d) { return d; }
	    public double toExabytes(double d) { return d/(EB/PB); }
	    public double toZettabytes(double d) { return d/(ZB/PB); }
	    public double toYottabytes(double d) { return d/(YB/PB); }
	    public double toBrontobytes(double d) { return d/(BB/PB); }
	    public double toNonaBytes(double d) { return d/(NB/PB); }
	    public double toDoggaBytes(double d) { return d/(DB/PB); }
        public double convert(double d, CapacityUnit u) { return u.toPetabytes(d); }

	},
	/**
	 * 1EB (Exabyte) = 1024 PB.
	 */
	EXABYTES{

		public double toBits(double d) { return x(d, EB/B, MAX/(EB/B)) * 8; }
	    public double toBytes(double d) { return x(d, EB/B, MAX/(EB/B)); }
	    public double toKilobytes(double d) { return x(d, EB/KB, MAX/(EB/KB)); }
	    public double toMebibytes(double d) { return x(d, EB/MB, MAX/(EB/MB)); }
	    public double toGigabytes(double d) { return x(d, EB/GB, MAX/(EB/GB)); }
	    public double toTerabytes(double d) { return x(d, EB/TB, MAX/(EB/TB)); }
	    public double toPetabytes(double d) { return x(d, EB/PB, MAX/(EB/PB)); }
	    public double toExabytes(double d) { return d; }
	    public double toZettabytes(double d) { return d/(ZB/EB); }
	    public double toYottabytes(double d) { return d/(YB/EB); }
	    public double toBrontobytes(double d) { return d/(BB/EB); }
	    public double toNonaBytes(double d) { return d/(NB/EB); }
	    public double toDoggaBytes(double d) { return d/(DB/EB); }
        public double convert(double d, CapacityUnit u) { return u.toExabytes(d); }

	},
	/**
	 * 1ZB (Zettabyte) = 1024 EB.
	 */
	ZETTABYTES{

		public double toBits(double d) { return x(d, ZB/B, MAX/(ZB/B)) * 8; }
	    public double toBytes(double d) { return x(d, ZB/B, MAX/(ZB/B)); }
	    public double toKilobytes(double d) { return x(d, ZB/KB, MAX/(ZB/KB)); }
	    public double toMebibytes(double d) { return x(d, ZB/MB, MAX/(ZB/MB)); }
	    public double toGigabytes(double d) { return x(d, ZB/GB, MAX/(ZB/GB)); }
	    public double toTerabytes(double d) { return x(d, ZB/TB, MAX/(ZB/TB)); }
	    public double toPetabytes(double d) { return x(d, ZB/PB, MAX/(ZB/PB)); }
	    public double toExabytes(double d) { return x(d, ZB/EB, MAX/(ZB/EB)); }
	    public double toZettabytes(double d) { return d; }
	    public double toYottabytes(double d) { return d/(YB/ZB); }
	    public double toBrontobytes(double d) { return d/(BB/ZB); }
	    public double toNonaBytes(double d) { return d/(NB/ZB); }
	    public double toDoggaBytes(double d) { return d/(DB/ZB); }
        public double convert(double d, CapacityUnit u) { return u.toZettabytes(d); }

	},
	/**
	 * 1YB (Yottabyte) = 1024 ZB.
	 */
	YOTTABYTES{

		public double toBits(double d) { return x(d, YB/B, MAX/(YB/B)) * 8; }
	    public double toBytes(double d) { return x(d, YB/B, MAX/(YB/B)); }
	    public double toKilobytes(double d) { return x(d, YB/KB, MAX/(YB/KB)); }
	    public double toMebibytes(double d) { return x(d, YB/MB, MAX/(YB/MB)); }
	    public double toGigabytes(double d) { return x(d, YB/GB, MAX/(YB/GB)); }
	    public double toTerabytes(double d) { return x(d, YB/TB, MAX/(YB/TB)); }
	    public double toPetabytes(double d) { return x(d, YB/PB, MAX/(YB/PB)); }
	    public double toExabytes(double d) { return x(d, YB/EB, MAX/(YB/EB)); }
	    public double toZettabytes(double d) { return x(d, YB/ZB, MAX/(YB/ZB)); }
	    public double toYottabytes(double d) { return d; }
	    public double toBrontobytes(double d) { return d/(BB/YB); }
	    public double toNonaBytes(double d) { return d/(NB/YB); }
	    public double toDoggaBytes(double d) { return d/(DB/YB); }
        public double convert(double d, CapacityUnit u) { return u.toYottabytes(d); }

	},
	/**
	 * 1BB (Brontobyte) = 1024 YB.
	 */
	BRONTOBYTES{

		public double toBits(double d) { return x(d, BB/B, MAX/(BB/B)) * 8; }
	    public double toBytes(double d) { return x(d, BB/B, MAX/(BB/B)); }
	    public double toKilobytes(double d) { return x(d, BB/KB, MAX/(BB/KB)); }
	    public double toMebibytes(double d) { return x(d, BB/MB, MAX/(BB/MB)); }
	    public double toGigabytes(double d) { return x(d, BB/GB, MAX/(BB/GB)); }
	    public double toTerabytes(double d) { return x(d, BB/TB, MAX/(BB/TB)); }
	    public double toPetabytes(double d) { return x(d, BB/PB, MAX/(BB/PB)); }
	    public double toExabytes(double d) { return x(d, BB/EB, MAX/(BB/EB)); }
	    public double toZettabytes(double d) { return x(d, BB/ZB, MAX/(BB/ZB)); }
	    public double toYottabytes(double d) { return x(d, BB/YB, MAX/(BB/YB)); }
	    public double toBrontobytes(double d) { return d; }
	    public double toNonaBytes(double d) { return d/(NB/BB); }
	    public double toDoggaBytes(double d) { return d/(DB/BB); }
        public double convert(double d, CapacityUnit u) { return u.toBrontobytes(d); }

	},
	/**
	 * 1NB (NonaByte) = 1024 BB.
	 */
	NONABYTES{

		public double toBits(double d) { return x(d, NB/B, MAX/(NB/B)) * 8; }
	    public double toBytes(double d) { return x(d, NB/B, MAX/(NB/B)); }
	    public double toKilobytes(double d) { return x(d, NB/KB, MAX/(NB/KB)); }
	    public double toMebibytes(double d) { return x(d, NB/MB, MAX/(NB/MB)); }
	    public double toGigabytes(double d) { return x(d, NB/GB, MAX/(NB/GB)); }
	    public double toTerabytes(double d) { return x(d, NB/TB, MAX/(NB/TB)); }
	    public double toPetabytes(double d) { return x(d, NB/PB, MAX/(NB/PB)); }
	    public double toExabytes(double d) { return x(d, NB/EB, MAX/(NB/EB)); }
	    public double toZettabytes(double d) { return x(d, NB/ZB, MAX/(NB/ZB)); }
	    public double toYottabytes(double d) { return x(d, NB/YB, MAX/(NB/YB)); }
	    public double toBrontobytes(double d) { return x(d, NB/BB, MAX/(NB/BB)); }
	    public double toNonaBytes(double d) { return d; }
	    public double toDoggaBytes(double d) { return d/(DB/NB); }
        public double convert(double d, CapacityUnit u) { return u.toNonaBytes(d); }

	},
	/**
	 * 1DB (DoggaByte) = 1024 NB.
	 */
	DOGGABYTES{

		public double toBits(double d) { return x(d, DB/B, MAX/(DB/B)) * 8; }
	    public double toBytes(double d) { return x(d, DB/B, MAX/(DB/B)); }
	    public double toKilobytes(double d) { return x(d, DB/KB, MAX/(DB/KB)); }
	    public double toMebibytes(double d) { return x(d, DB/MB, MAX/(DB/MB)); }
	    public double toGigabytes(double d) { return x(d, DB/GB, MAX/(DB/GB)); }
	    public double toTerabytes(double d) { return x(d, DB/TB, MAX/(DB/TB)); }
	    public double toPetabytes(double d) { return x(d, DB/PB, MAX/(DB/PB)); }
	    public double toExabytes(double d) { return x(d, DB/EB, MAX/(DB/EB)); }
	    public double toZettabytes(double d) { return x(d, DB/ZB, MAX/(DB/ZB)); }
	    public double toYottabytes(double d) { return x(d, DB/YB, MAX/(DB/YB)); }
	    public double toBrontobytes(double d) { return x(d, DB/BB, MAX/(DB/BB)); }
	    public double toNonaBytes(double d) { return x(d, DB/NB, MAX/(DB/NB)); }
	    public double toDoggaBytes(double d) { return d; }
        public double convert(double d, CapacityUnit u) { return u.toDoggaBytes(d); }

	};

	// Handy constants for conversion methods

	/** 1 Byte. */
    static final double B = 1L;
    /** 1 Kilobyte = 1024 Bytes. */
    static final double KB = B * 1024L;
    /** 1 Megabyte = 1024 KB. */
    static final double MB = KB * 1024L;
    /** 1 Gigabyte = 1024 MB. */
    static final double GB = MB * 1024L;
    /** 1 Terabyte = 1024 GB. */
    static final double TB = GB * 1024L;
    /** 1 Petabyte = 1024 TB. */
    static final double PB = TB * 1024L;
    /** 1 Exabyte = 1024 PB. */
    static final double EB = PB * 1024L;
    /** 1 Zettabyte = 1024 EB. */
    static final double ZB = EB * 1024L;
	/** 1 Yottabyte = 1024 ZB. */
    static final double YB = ZB * 1024L;
	/** 1 Brontobyte = 1024 YB. */
    static final double BB = YB * 1024L;
    /** 1 NonaByte = 1024 BB. */
    static final double NB = BB * 1024L;
    /** 1 DoggaByte = 1024 NB. */
    static final double DB = NB * 1024L;

    static final double MAX = Double.MAX_VALUE;

    /**
     * Scales {@code d} by {@code m}, checking for overflow.
     * Returns {@link Double#MAX_VALUE} or {@link Double#MIN_VALUE}
     * on overflow.
     *
     * @param d     the value to scale
     * @param m     the multiplier
     * @param over  the overflow threshold
     * @return the scaled value, clamped to safe bounds
     */
    static double x(double d, double m, double over) {
        if (d >  over) return Double.MAX_VALUE;
        if (d < -over) return Double.MIN_VALUE;
        return d * m;
    }

    /**
     * Converts a value from this unit to the given target unit.
     *
     * @param sourceDuration the value in this unit
     * @param sourceUnit     the target capacity unit
     * @return the converted value in the target unit
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double convert(double sourceDuration, CapacityUnit sourceUnit) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to bits.
     *
     * @param duration the value in this unit
     * @return the equivalent in bits
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toBits(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to bytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in bytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toBytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to kilobytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in kilobytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toKilobytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to megabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in megabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toMebibytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to gigabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in gigabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toGigabytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to terabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in terabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toTerabytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to petabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in petabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toPetabytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to exabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in exabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toExabytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to zettabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in zettabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toZettabytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to yottabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in yottabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toYottabytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to brontobytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in brontobytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toBrontobytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to nonabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in nonabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toNonaBytes(double duration) {
        throw new AbstractMethodError();
    }

    /**
     * Converts a value from this unit to doggabytes.
     *
     * @param duration the value in this unit
     * @return the equivalent in doggabytes
     * @throws AbstractMethodError if called on {@link #NONE}
     */
    public double toDoggaBytes(double duration) {
        throw new AbstractMethodError();
    }

}
