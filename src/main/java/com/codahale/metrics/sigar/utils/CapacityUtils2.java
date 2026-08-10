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

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

/**
 * Alternative utility class for parsing and converting digital storage
 * capacity strings using {@link CapacityUnit} for conversions instead
 * of raw BigDecimal multipliers.
 *
 * <p>Supports units: KB, MB, GB, TB.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see CapacityUtils
 * @see CapacityUnit
 */
public abstract class CapacityUtils2 {

	protected static Logger LOG = LoggerFactory.getLogger(CapacityUtils2.class);
	protected static Pattern pattern_find = Pattern.compile("^([1-9]\\d*|[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)(B|KB|MB|GB|TB|PB|EB|ZB|YB|BB)$");
	protected static Map<String,CapacityUnit> powers = new HashMap<String, CapacityUnit>();


	static{

		powers.put("KB", CapacityUnit.KILOBYTES);
		powers.put("MB", CapacityUnit.MEGABYTES);
		powers.put("GB", CapacityUnit.GIGABYTES);
		powers.put("TB", CapacityUnit.TRILLIONBYTES);

	}

	/**
	 * Parses a capacity string (e.g. {@code "10MB"}) and returns the
	 * equivalent value in kilobytes as a {@link BigDecimal}.
	 *
	 * @param value the capacity string to parse; may be {@code null} or blank
	 * @return the kilobyte equivalent, or {@link BigDecimal#ZERO} if
	 *         the input is empty or cannot be parsed
	 */
	public static BigDecimal getCapacity(String value){
		if (value==null||value.trim().length() == 0) {
			return BigDecimal.ZERO;
		}
		value =  value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if(matcher.find()) {
			BigDecimal num = new BigDecimal(matcher.group(1));
			return new BigDecimal(powers.get(matcher.group(2)).toKilobytes(num.doubleValue()));
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * Parses a capacity string and returns the kilobyte equivalent as
	 * a {@code long}.
	 *
	 * @param value the capacity string to parse; may be {@code null} or blank
	 * @return the kilobyte equivalent, or {@code 0} if the input is
	 *         empty or cannot be parsed
	 */
	public static long getLongCapacity(String value){
		if (value==null||value.trim().length() == 0) {
			return 0;
		}
		value =  value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if(matcher.find()) {
			Long num = Long.valueOf(matcher.group(1));
			return new BigDecimal(powers.get(matcher.group(2)).toKilobytes(num.doubleValue())).longValue();
		} else {
			return 0;
		}
	}

	/**
	 * Parses a capacity string and returns the kilobyte equivalent as
	 * a {@code float}.
	 *
	 * @param value the capacity string to parse; may be {@code null} or blank
	 * @return the kilobyte equivalent, or {@code -1} if the input is
	 *         empty or cannot be parsed
	 */
	public static float getFloatCapacity(String value){
		if (value==null||value.trim().length() == 0) {
			return 0;
		}
		value =  value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if(matcher.find()) {
			Float num = Float.valueOf(matcher.group(1));
			return new BigDecimal(powers.get(matcher.group(2)).toKilobytes(num.doubleValue())).floatValue();
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
