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
import static org.junit.Assume.*;

import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.junit.BeforeClass;
import org.junit.Test;

import com.codahale.metrics.sigar.utils.OSEnvInfo;

import kamon.sigar.SigarProvisioner;

public class OSEnvInfo_Test extends CheckSigarLoadsOk {

	@Test
	public void testInfo() throws Exception {
		Sigar sigar = new Sigar();
		Map<String, Object> infoMap = OSEnvInfo.info(sigar);
		assertNotNull(infoMap);
		assertNotNull(infoMap.get("host.ip"));
		assertNotNull(infoMap.get("host.name"));
		assertNotNull(infoMap.get("os.name"));
		assertNotNull(infoMap.get("os.arch"));
		assertNotNull(infoMap.get("os.version"));
	}

	@Test
	public void testMemory() throws Exception {
		Sigar sigar = new Sigar();
		Map<String, Object> infoMap = OSEnvInfo.memory(sigar);
		assertNotNull(infoMap);
		assertFalse(infoMap.isEmpty());
	}

	@Test
	public void testUsage() throws Exception {
		Sigar sigar = new Sigar();
		Map<String, Double> infoMap = OSEnvInfo.usage(sigar);
		assertNotNull(infoMap);
		assertFalse(infoMap.isEmpty());
	}

	@Test
	public void testCpuInfos() throws Exception {
		Sigar sigar = new Sigar();
		List<Map<String, Object>> infoMap = OSEnvInfo.cpuInfos(sigar);
		assertNotNull(infoMap);
		assertFalse(infoMap.isEmpty());
	}

	@Test
	public void testDiskInfos() throws Exception {
		Sigar sigar = new Sigar();
		List<Map<String, Object>> infoMap = OSEnvInfo.diskInfos(sigar);
		assertNotNull(infoMap);
	}

}
