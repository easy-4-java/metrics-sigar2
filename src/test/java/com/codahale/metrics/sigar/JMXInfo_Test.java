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
package com.codahale.metrics.sigar;

import java.util.List;
import java.util.Map;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;
import com.codahale.metrics.sigar.utils.JMXInfo;
import com.codahale.metrics.sigar.utils.MemoryInfo;

import junit.framework.TestCase;

public class JMXInfo_Test extends TestCase {

	public void testOs() throws Exception {
		
		System.out.println("======================OS=============================");
		
		Map<String, Object> infoMap = JMXInfo.os();
		for (String key : infoMap.keySet()) {
			System.out.println(key + " : " + infoMap.get(key));
		}
	}
	
	public void testRuntime() throws Exception {
		
		System.out.println("======================runtime=============================");
		Map<String, Object> infoMap = JMXInfo.runtime();
		for (String key : infoMap.keySet()) {
			System.out.println(key + " : " + infoMap.get(key));
		}
	}
	
	public void testMemory() throws Exception {
		
		System.out.println("======================memory=============================");
		
		List<MemoryInfo> infoList = JMXInfo.memory(Unit.KB);
		for (MemoryInfo memoryMap : infoList) {
			System.out.println( memoryMap.getType() + ":" + memoryMap.toMap());
			System.out.println( memoryMap.getType() + ":" +memoryMap.toString());
            System.out.println("===================================================");
		}
	}
	
	public void testMemoryPool() throws Exception {
		
		System.out.println("======================memoryPool=============================");
		 
		List<MemoryInfo> infoList = JMXInfo.memoryPool(Unit.KB);
		for (MemoryInfo memoryMap : infoList) {
			System.out.println( memoryMap.getType() + ":" + memoryMap.toMap());
			System.out.println( memoryMap.getType() + ":" +memoryMap.toString());
            System.out.println("===================================================");
		}
	}
	
	public void testCompilation() throws Exception {
		System.out.println("==========================Compilation=========================");
		Map<String, Object> infoMap = JMXInfo.compilation();
		for (String key : infoMap.keySet()) {
			System.out.println(key + " : " + infoMap.get(key));
		}
	}
	
	public void testGc() throws Exception {
		System.out.println("==========================GarbageCollector=========================");
		Map<String, Object> infoMap = JMXInfo.gc();
		for (String key : infoMap.keySet()) {
			System.out.println(key + " : " + infoMap.get(key));
		}
	}
	
}
