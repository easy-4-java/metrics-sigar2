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

public enum JVMOSProperty {
	
	/**
	 * 操作系统名称
	 */
	OS_NAME("os.name"),
	/**
	 * 操作系统构架
	 */
	OS_ARCH("os.arch"),
	/**
	 * 操作系统的版本
	 */
	OS_VERSION("os.version"),
	/**
	 * 文件分隔符
	 */
	FILE_SEPARATOR("file.separator"),
	/**
	 * 路径分隔符
	 */
	PATH_SEPARATOR("path.separator"),
	/**
	 * 行分隔符
	 */
	LINE_SEPARATOR("line.separator"),
	/**
	 * 用户的账户名称
	 */
	USER_NAME("user.name"),
	/**
	 * 用户的主目录
	 */
	USER_HOME("user.home"),
	/**
	 * 用户的当前工作目录
	 */
	USER_DIR("user.dir");
	
	protected String key;
	
	JVMOSProperty(String key){
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
}