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
 * Enumeration of JVM-related system property keys, covering the
 * Java installation path, VM specification, runtime paths, class
 * version, and library paths.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see JVMOSProperty
 */
public enum JVMProperty {

	/** Java installation directory. */
	JAVA_HOME("java.home"),
	/** Java runtime version. */
	JAVA_VERSION("java.version"),
	/** Java runtime vendor. */
	JAVA_VENDOR("java.vendor"),
	/** Java vendor URL. */
	JAVA_VENDOR_URL("java.vendor.url"),
	/** JVM specification version. */
	JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),
	/** JVM specification vendor. */
	JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
	/** JVM specification name. */
	JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
	/** JVM process ID (custom key). */
	JAVA_VM_PID("java.vm.pid"),
	/** JVM implementation name. */
	JAVA_VM_NAME("java.vm.name"),
	/** JVM implementation vendor. */
	JAVA_VM_VENDOR("java.vm.vendor"),
	/** JVM implementation version. */
	JAVA_VM_VERSION("java.vm.version"),
	/** JVM input arguments (custom key). */
	JAVA_VM_OPTIONS("java.vm.options"),
	/** JVM start time in milliseconds (custom key). */
	JAVA_RUNTIME_STARTTIME("jvm.runtime.StartTime"),
	/** JVM uptime in milliseconds (custom key). */
	JAVA_RUNTIME_UPTIME("jvm.runtime.Uptime"),
	/** Java runtime specification name. */
	JAVA_SPECIFICATION_NAME("java.specification.name"),
	/** Java runtime specification vendor. */
	JAVA_SPECIFICATION_VENDER("java.specification.vender"),
	/** Java runtime specification version. */
	JAVA_SPECIFICATION_VERSION("java.specification.version"),
	/** Management specification version. */
	JAVA_MANAGEMENT_SPECIFICATION_VERSION("java.management.specification.version"),
	/** Java class file format version. */
	JAVA_CLASS_VERSION("java.class.version"),
	/** Bootstrap class path. */
	JAVA_BOOT_CLASS_PATH("java.boot.class.path"),
	/** Java class path. */
	JAVA_CLASS_PATH("java.class.path"),
	/** Java native library path. */
	JAVA_LIBRARY_PATH("java.library.path"),
	/** Default temporary file directory. */
	JAVA_IO_TMPDIR("java.io.tmpdir"),
	/** Extension directories path. */
	JAVA_EXT_DIRS("java.ext.dirs");

	protected String key;

	JVMProperty(String key){
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
