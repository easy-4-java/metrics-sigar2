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

public abstract class CapacityUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(CapacityUtils.class);
	protected static Pattern pattern_find = Pattern.compile("^([1-9]\\d*|[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)(B|KB|MB|GB|TB|PB|EB|ZB|YB|BB)$");
	protected static Map<String,Unit> powers = new HashMap<String, Unit>();
	
	public static enum Unit {
		/**
		 * 未指定单位
		 */
		NONE("none" , BigDecimal.ONE),
		/**
		 * 1B = 1024bit * 8
		 */
		B("B" , BigDecimal.valueOf(1024 * 8)),
		/**
		 * 1KB(Kilobyte 千字节)=1024Byte
		 */
		KB("KB" , BigDecimal.valueOf(1024)),
		/**
		 * 1MB(Megabyte 兆字节 简称“兆”)=1024KB
		 */
		MB("MB" , BigDecimal.valueOf(1024 * 1024)),
		/**
		 * 1GB(Gigabyte 吉字节 又称“千兆”)=1024MB
		 */
		GB("GB" , BigDecimal.valueOf(1024 * 1024 * 1024)),
		/**
		 * 1TB(Trillionbyte 万亿字节 太字节)=1024GB
		 */
		TB("TB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024)),
		/**
		 * 1PB（Petabyte 千万亿字节 拍字节）=1024TB
		 */
		PB("PB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024)),
		/**
		 * 1EB（Exabyte 百亿亿字节 艾字节）=1024PB
		 */
		EB("EB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024 * 1024)),
		/**
		 * 1ZB(Zettabyte 十万亿亿字节 泽字节)= 1024 EB
		 */
		ZB("ZB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024)),
		/**
		 * 1YB(Yottabyte 一亿亿亿字节 尧字节)= 1024 ZB
		 */
		YB("YB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024)),
		/**
		 * 1BB(Brontobyte 一千亿亿亿字节)= 1024 YB 
		 */
		BB("BB" , BigDecimal.valueOf(1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024));
		
		protected String key;
		protected BigDecimal value;
		
		Unit(String key,BigDecimal value){
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		
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
			BigDecimal mult = powers.get(matcher.group(2)).getValue();
			return num.multiply(mult);
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
			BigDecimal mult = powers.get(matcher.group(2)).getValue();
			return num.longValue() * mult.longValue();
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
			BigDecimal mult = powers.get(matcher.group(2)).getValue();
			return num.floatValue() * mult.floatValue();
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
	 *  提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
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
