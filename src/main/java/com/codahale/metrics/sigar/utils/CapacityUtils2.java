/*
 * Copyright (c) 2018 (https://github.com/easy-4-java).
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
	
	/*
	 * 计算指定数值单位对应的字节数：如 1KB 计算得到 1024
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
	
	public static BigDecimal getCapacity(long value,Unit unit){
		return getCapacity(value, unit, 0);
	}
	
	public static String getCapacityString(long value,Unit unit){
		return getCapacityString(value, unit, 0);
	}
	
	/*
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
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
	
	public static String getCapacityString(long value,Unit unit, int scale){
		BigDecimal val = getCapacity(value, unit, scale);
		return val.toPlainString()   + "" + unit.getKey();
	}
	
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1  被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
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
