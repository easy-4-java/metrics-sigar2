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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

/**
 * Data object representing a JVM memory segment (heap, non-heap, or
 * memory pool) with its init/used/committed/max values and a
 * capacity unit for formatting.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see JMXInfo
 * @see JVMInfo
 * @see MemProperty
 */
public class MemoryInfo {

	protected String prefix;
	protected String type;
	protected Map<String, Long> usage;
	protected Unit unit;

	/**
	 * Constructs a new {@code MemoryInfo}.
	 *
	 * @param prefix the metric prefix (e.g. "jvm.memory")
	 * @param type   the segment type (e.g. "HeapMemoryUsage")
	 * @param usage  a map of {@link MemProperty} keys to byte values
	 * @param unit   the capacity unit for formatting
	 */
	public MemoryInfo(final String prefix,final String type,final Map<String, Long> usage,final Unit unit) {
		this.prefix = prefix;
		this.type = type;
		this.usage = usage;
		this.unit = unit;
	}

	/**
	 * Returns the metric prefix.
	 *
	 * @return the prefix string
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Returns the segment type name.
	 *
	 * @return the type string
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the raw usage map (byte values).
	 *
	 * @return the usage map; never {@code null}
	 */
	public Map<String, Long> getUsage() {
		return usage;
	}

	/**
	 * Returns the capacity unit used for formatting.
	 *
	 * @return the unit
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * Converts the raw usage map to a formatted map where each value
	 * is expressed in the configured {@link Unit}.
	 *
	 * @return a map of dotted metric keys to formatted capacity strings;
	 *         never {@code null}
	 */
	public Map<String, String> toMap() {
		Map<String, String> dataMap = new HashMap<String, String>();
		for (String key : usage.keySet()) {
			dataMap.put(StringUtils.join(new String[]{prefix, type, key}, "."), CapacityUtils.getCapacityString(usage.get(key), unit) );
		}
		return dataMap;
	}

	/**
	 * Returns a human-readable representation showing init, used,
	 * committed, and max values in both bytes and kilobytes.
	 *
	 * @return a descriptive string; never {@code null}
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
        buf.append("init = " + usage.get("init") + "(" + (usage.get("init") >> 10) + "K) ");
        buf.append("used = " + usage.get("used") + "(" + ( usage.get("used") >> 10) + "K) ");
        buf.append("committed = " + usage.get("committed") + "(" + (usage.get("committed") >> 10) + "K) " );
        buf.append("max = " + usage.get("max") + "(" + (usage.get("max") >> 10) + "K)");
		return buf.toString();
	}


}
