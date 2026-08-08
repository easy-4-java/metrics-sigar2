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
 * Enumeration of operating system related system property keys.
 *
 * <p>Duplicates the keys in {@link JVMOSProperty} for backward
 * compatibility.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see JVMOSProperty
 */
public enum OSProperty {

	/** Operating system name. */
	OS_NAME("os.name"),
	/** Operating system architecture. */
	OS_ARCH("os.arch"),
	/** Operating system version. */
	OS_VERSION("os.version"),
	/** File separator character. */
	FILE_SEPARATOR("file.separator"),
	/** Path separator character. */
	PATH_SEPARATOR("path.separator"),
	/** Line separator string. */
	LINE_SEPARATOR("line.separator"),
	/** Current user's account name. */
	USER_NAME("user.name"),
	/** Current user's home directory. */
	USER_HOME("user.home"),
	/** Current user's working directory. */
	USER_DIR("user.dir");

	protected String key;

	OSProperty(String key){
		this.key = key;
	}

	/**
	 * Returns the system property key for this entry.
	 *
	 * @return the property key string
	 */
	public String getKey() {
		return key;
	}

}
