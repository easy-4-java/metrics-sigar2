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

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;
import com.codahale.metrics.sigar.utils.JVMInfo;
import com.codahale.metrics.sigar.utils.JVMProperty;
import com.codahale.metrics.sigar.utils.MemoryInfo;

public class JVMInfo_Test {

	@Test
	public void testInfo() throws Exception {
		try {
			Map<String, Object> infoMap = JVMInfo.info();
			assertNotNull(infoMap);
			assertFalse(infoMap.isEmpty());
		} catch (UnsupportedOperationException e) {
			// getBootClassPath() is not supported on JDK 17+
		}
	}

	@Test
	public void testMemory_MB() throws Exception {
		Map<String, Object> runtime = JVMInfo.runtime(Unit.MB);
		assertNotNull(runtime);
		assertFalse(runtime.isEmpty());
	}

	@Test
	public void testMemory() throws Exception {
		List<MemoryInfo> infoList = JVMInfo.memory(Unit.KB);
		assertNotNull(infoList);
		assertEquals(2, infoList.size());
		for (MemoryInfo memoryMap : infoList) {
			assertNotNull(memoryMap.getType());
			assertNotNull(memoryMap.toMap());
			assertNotNull(memoryMap.toString());
		}
	}

	@Test
	public void testMemoryPool() throws Exception {
		List<MemoryInfo> infoList = JVMInfo.memoryPool(Unit.KB);
		assertNotNull(infoList);
		assertFalse(infoList.isEmpty());
		for (MemoryInfo memoryMap : infoList) {
			assertNotNull(memoryMap.getType());
			assertNotNull(memoryMap.toMap());
			assertNotNull(memoryMap.toString());
		}
	}

	@Test
	public void testGc() throws Exception {
		List<Map<String, Object>> gcList = JVMInfo.gc();
		assertNotNull(gcList);
	}

}
