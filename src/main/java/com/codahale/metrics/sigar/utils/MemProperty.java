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
 * Enumeration of JVM memory property keys corresponding to the fields
 * of a {@link java.lang.management.MemoryUsage MemoryUsage} object.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see java.lang.management.MemoryUsage
 * @see MemoryInfo
 */
public enum MemProperty {

	/**
	 * Initial memory requested from the OS during JVM startup (bytes).
	 * May be undefined.
	 */
	MEM_INIT("init"),
	/**
	 * Currently used memory (bytes).
	 */
	MEM_USED("used"),
	/**
	 * Memory committed for use by the JVM (bytes). Always &gt;= used.
	 */
	MEM_COMMITTED("committed"),
	/**
	 * Maximum memory that can be used for memory management (bytes).
	 * May be undefined (-1).
	 */
	MEM_MAX("max");

	protected String key;

	MemProperty(String key){
		this.key = key;
	}

	/**
	 * Returns the property key string (e.g. "init", "used").
	 *
	 * @return the key string
	 */
	public String getKey() {
		return key;
	}

}
