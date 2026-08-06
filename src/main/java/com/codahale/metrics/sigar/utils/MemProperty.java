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

public enum MemProperty {
	
	/**
	 * 表示 Java 虚拟机在启动期间从操作系统请求的用于内存管理的初始内存容量（以字节为单位）。
	 * Java 虚拟机可能在运行过程中从操作系统请求更多的内存，也可能将内存释放给系统。init 的值可以是不明确的
	 */
	MEM_INIT("init"),
	/**
	 * 表示当前已经使用的内存量（以字节为单位）。 
	 */
	MEM_USED("used"),
	/**
	 * 表示保证可以由 Java 虚拟机使用的内存量（以字节为单位）。
	 * 已提交的内存量可以随时间而变化（增加或减少）。
	 * Java 虚拟机可能会将内存释放给系统，committed 可以小于 init。committed 将始终大于或等于 used。
	 */
	MEM_COMMITTED("committed"),
	/**
	 * 表示可以用于内存管理的最大内存量（以字节为单位）。
	 * 可以不定义其值。如果定义了该值，最大内存量可能随时间而更改。已使用的内存量和已提交的内存量将始终小于或等于 max（如果定义了 max）。
	 * 如果内存分配试图增加满足以下条件的已使用内存将会失败：used &gt; committed，即使 used &lt;= max 仍然为 true（例如，当系统的虚拟内存不足时）。 
	 */
	MEM_MAX("max");
	
	protected String key;
	
	MemProperty(String key){
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
}