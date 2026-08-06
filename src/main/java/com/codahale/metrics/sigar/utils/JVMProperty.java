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

public enum JVMProperty {
	
	/**
	 * Java的安装路径
	 */
	JAVA_HOME("java.home"),
	/**
	 * Java的运行环境版本
	 */
	JAVA_VERSION("java.version"),
	/**
	 * Java的运行环境供应商
	 */
	JAVA_VENDOR("java.vendor"),
	/**
	 * Java供应商的URL
	 */
	JAVA_VENDOR_URL("java.vendor.url"),
	/**
	 * Java的虚拟机规范版本
	 */
	JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),
	/**
	 * Java的虚拟机规范供应商
	 */
	JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
	/**
	 * Java的虚拟机规范名称
	 */
	JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
	/**
	 * Java的虚拟机PID
	 */
	JAVA_VM_PID("java.vm.pid"),
	/**
	 * Java的虚拟机实现名称
	 */
	JAVA_VM_NAME("java.vm.name"),
	/**
	 * Java的虚拟机实现供应商
	 */
	JAVA_VM_VENDOR("java.vm.vendor"),
	/**
	 * Java的虚拟机实现版本
	 */
	JAVA_VM_VERSION("java.vm.version"),
	/**
	 * Java的虚拟机参数
	 */
	JAVA_VM_OPTIONS("java.vm.options"),
	/**
	 * Java的虚拟机启动时间
	 */
	JAVA_RUNTIME_STARTTIME("jvm.runtime.StartTime"),
	/**
	 * Java的虚拟机进程CPU时间
	 */
	JAVA_RUNTIME_UPTIME("jvm.runtime.Uptime"),
	/**
	 * Java运行时环境规范名称
	 */
	JAVA_SPECIFICATION_NAME("java.specification.name"),
	/**
	 * Java运行时环境规范供应商
	 */
	JAVA_SPECIFICATION_VENDER("java.specification.vender"),
	/**
	 * Java运行时环境规范版本
	 */
	JAVA_SPECIFICATION_VERSION("java.specification.version"),
	/**
	 * Java运行时虚拟机实现的管理接口的规范版本
	 */
	JAVA_MANAGEMENT_SPECIFICATION_VERSION("java.management.specification.version"),
	/**
	 * Java的类格式版本号
	 */
	JAVA_CLASS_VERSION("java.class.version"),
	/**
	 * Java的引导类加载器用于搜索类文件的引导类路径
	 */
	JAVA_BOOT_CLASS_PATH("java.boot.class.path"),
	/**
	 * Java的类路径
	 */
	JAVA_CLASS_PATH("java.class.path"),
	/**
	 * Java的类格式版本号
	 */
	JAVA_LIBRARY_PATH("java.library.path"),
	/**
	 * 默认的临时文件路径
	 */
	JAVA_IO_TMPDIR("java.io.tmpdir"),
	/**
	 * 一个或多个扩展目录的路径
	 */
	JAVA_EXT_DIRS("java.ext.dirs");
	
	protected String key;
	
	JVMProperty(String key){
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
}