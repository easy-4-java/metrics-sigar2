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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

public class MemoryInfo {
	
	protected String prefix;
	protected String type;
	protected Map<String, Long> usage;
	protected Unit unit;
	
	public MemoryInfo(final String prefix,final String type,final Map<String, Long> usage,final Unit unit) {
		this.prefix = prefix;
		this.type = type;
		this.usage = usage;
		this.unit = unit;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getType() {
		return type;
	}
	
	public Map<String, Long> getUsage() {
		return usage;
	}

	public Unit getUnit() {
		return unit;
	}
	
	public Map<String, String> toMap() {
		Map<String, String> dataMap = new HashMap<String, String>();
		for (String key : usage.keySet()) {
			dataMap.put(StringUtils.join(new String[]{prefix, type, key}, "."), CapacityUtils.getCapacityString(usage.get(key), unit) );
		}
		return dataMap;
	}
	
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