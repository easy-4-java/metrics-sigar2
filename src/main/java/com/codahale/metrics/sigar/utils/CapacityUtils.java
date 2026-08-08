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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for parsing and converting digital storage capacity
 * strings (e.g. {@code "10MB"}, {@code "1.5GB"}) to their byte
 * equivalents, and for performing precise division.
 *
 * <p>Supports units: B, KB, MB, GB, TB, PB, EB, ZB, YB, BB.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see CapacityUtils.Unit
 * @see CapacityUnit
 */
public abstract class CapacityUtils {

	protected static Logger LOG = LoggerFactory.getLogger(CapacityUtils.class);
	protected static Pattern pattern_find = Pattern.compile("^([1-9]\\d*|[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)(B|KB|MB|GB|TB|PB|EB|ZB|YB|BB)$");
	protected static Map<String,Unit> powers = new HashMap<String, Unit>();

	/**
	 * Enumeration of capacity units with their byte-multiplier values.
	 */
	public static enum Unit {
		/** No specific unit. */
		NONE("none" , BigDecimal.ONE),
		/** 1 Byte = 8 bits. */
		B("B" , BigDecimal.valueOf(1024 * 8)),
		/** 1 Kilobyte = 1024 Bytes. */
		KB("KB" , BigDecimal.valueOf(1024)),
		/** 1 Megabyte = 1024 KB. */
		MB("MB" , BigDecimal.valueOf(1024 * 1024)),
		/** 1 Gigabyte = 1024 MB. */
		GB("GB" , BigDecimal.valueOf(1024 * 1024 * 1024)),
		/** 1 Terabyte = 1024 GB. */
		TB("TB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024)),
		/** 1 Petabyte = 1024 TB. */
		PB("PB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024)),
		/** 1 Exabyte = 1024 PB. */
		EB("EB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024 * 1024)),
		/** 1 Zettabyte = 1024 EB. */
		ZB("ZB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024)),
		/** 1 Yottabyte = 1024 ZB. */
		YB("YB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024)),
		/** 1 Brontobyte = 1024 YB. */
		BB("BB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024));

		protected String key;
		protected BigDecimal value;

		Unit(String key,BigDecimal value){
			this.key = key;
			this.value = value;
		}

		/** @return the string key for this unit (e.g. "KB") */
		public String getKey() {
			return key;
		}

		/** @return the byte-multiplier value for this unit */
		public BigDecimal getValue() {
			return value;
		}
	}


	static{
		powers.put(Unit.KB.getKey(), Unit.KB);
		powers.put(Unit.MB.getKey(), Unit.MB);
		powers.put(Unit.GB.getKey(), Unit.GB);
		powers.put(Unit.TB.getKey(), Unit.TB);

		powers.put(Unit.PB.getKey(), Unit.PB);
		powers.put(Unit.EB.getKey(), Unit.EB);
		powers.put(Unit.ZB.getKey(), Unit.ZB);
		powers.put(Unit.YB.getKey(), Unit.YB);
		powers.put(Unit.BB.getKey(), Unit.BB);

	}

	/**
	 * Parses a capacity string (e.g. {@code "10MB"}) and returns the
	 * equivalent value in bytes as a {@link BigDecimal}.
	 *
	 * @param value the capacity string to parse; may be {@code null} or blank
	 * @return the byte equivalent, or {@link BigDecimal#ZERO} if the
	 *         input is empty or cannot be parsed
	 */
	public static BigDecimal getCapacity(String value){
		if (value==null||value.trim().length() == 0) {
			return BigDecimal.ZERO;
		}
		value =  value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if(matcher.find()) {
			BigDecimal num = new BigDecimal(matcher.group(1));
			BigDecimal mult = powers.get(matcher.group(2)).getValue();
			return num.multiply(mult);
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * Parses a capacity string and returns the byte equivalent as a
	 * {@code long}.
	 *
	 * @param value the capacity string to parse; may be {@code null} or blank
	 * @return the byte equivalent, or {@code 0} if the input is empty
	 *         or cannot be parsed
	 */
	public static long getLongCapacity(String value){
		if (value==null||value.trim().length() == 0) {
			return 0;
		}
		value =  value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if(matcher.find()) {
			Long num = Long.valueOf(matcher.group(1));
			BigDecimal mult = powers.get(matcher.group(2)).getValue();
			return num.longValue() * mult.longValue();
		} else {
			return 0;
		}
	}

	/**
	 * Parses a capacity string and returns the byte equivalent as a
	 * {@code float}.
	 *
	 * @param value the capacity string to parse; may be {@code null} or blank
	 * @return the byte equivalent, or {@code -1} if the input is empty
	 *         or cannot be parsed
	 */
	public static float getFloatCapacity(String value){
		if (value==null||value.trim().length() == 0) {
			return 0;
		}
		value =  value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if(matcher.find()) {
			Float num = Float.valueOf(matcher.group(1));
			BigDecimal mult = powers.get(matcher.group(2)).getValue();
			return num.floatValue() * mult.floatValue();
		} else {
			return -1;
		}
	}

	/**
	 * Converts a byte value to the specified unit with a scale of 0.
	 *
	 * @param value the byte value
	 * @param unit  the target unit
	 * @return the converted value
	 */
	public static BigDecimal getCapacity(long value,Unit unit){
		return getCapacity(value, unit, 0);
	}

	/**
	 * Converts a byte value to a human-readable string in the specified
	 * unit with a scale of 0.
	 *
	 * @param value the byte value
	 * @param unit  the target unit
	 * @return a string such as {@code "10MB"}
	 */
	public static String getCapacityString(long value,Unit unit){
		return getCapacityString(value, unit, 0);
	}

	/**
	 * Converts a byte value to the specified unit with the given
	 * decimal scale.
	 *
	 * @param value the byte value
	 * @param unit  the target unit
	 * @param scale the number of decimal places; must be &gt;= 0
	 * @return the converted value
	 * @throws IllegalArgumentException if {@code scale} is negative
	 */
	public static BigDecimal getCapacity(long value,Unit unit, int scale){
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("value :{} , unit {}, scale {}", value, unit.getKey(), scale);
		}
		if(unit.getKey().equals(Unit.KB.getKey())){
			BigDecimal num = new BigDecimal((value >> 10));
			return num.divide(BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_DOWN);
		}
		BigDecimal num = new BigDecimal(value);
		return num.divide( unit.getValue(), scale, BigDecimal.ROUND_HALF_DOWN);
	}

	/**
	 * Converts a byte value to a human-readable string in the specified
	 * unit with the given decimal scale.
	 *
	 * @param value the byte value
	 * @param unit  the target unit
	 * @param scale the number of decimal places; must be &gt;= 0
	 * @return a string such as {@code "1.50MB"}
	 */
	public static String getCapacityString(long value,Unit unit, int scale){
		BigDecimal val = getCapacity(value, unit, scale);
		return val.toPlainString()   + "" + unit.getKey();
	}

	/**
	 * Performs a precise division of two doubles with the specified
	 * scale and rounding mode.
	 *
	 * @param v1    the dividend
	 * @param v2    the divisor
	 * @param scale the number of decimal places; must be &gt;= 0
	 * @return the quotient, or {@code 0} if an error occurs
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("v1 :{} , v2 {}, scale {}", v1, v2, scale);
		}
		try {
			BigDecimal b1 = new BigDecimal(String.valueOf(v1));
			BigDecimal b2 = new BigDecimal(String.valueOf(v2));
			return b1.divide(b2.compareTo(BigDecimal.ZERO) == 0  ? BigDecimal.ONE : b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception e) {
			LOG.error("v1 :{} , v2 {}, scale {}", v1, v2, scale);
			LOG.error(e.getMessage());
			return 0;
		}
	}


}
