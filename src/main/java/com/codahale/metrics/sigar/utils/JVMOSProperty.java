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
 * Enumeration of operating system related JVM system property keys.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see JVMProperty
 */
public enum JVMOSProperty {

	/** Operating system name (e.g. "Linux", "Mac OS X"). */
	OS_NAME("os.name"),
	/** Operating system architecture (e.g. "amd64", "aarch64"). */
	OS_ARCH("os.arch"),
	/** Operating system version. */
	OS_VERSION("os.version"),
	/** File separator character (e.g. "/" on Unix, "\\" on Windows). */
	FILE_SEPARATOR("file.separator"),
	/** Path separator character (e.g. ":" on Unix, ";" on Windows). */
	PATH_SEPARATOR("path.separator"),
	/** Line separator string (e.g. "\n" on Unix, "\r\n" on Windows). */
	LINE_SEPARATOR("line.separator"),
	/** Current user's account name. */
	USER_NAME("user.name"),
	/** Current user's home directory. */
	USER_HOME("user.home"),
	/** Current user's working directory. */
	USER_DIR("user.dir");

	protected String key;

	JVMOSProperty(String key){
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
