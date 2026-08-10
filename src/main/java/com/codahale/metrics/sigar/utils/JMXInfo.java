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

import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

/**
 * Collects JVM metrics using JMX ManagementFactory beans.
 *
 * <p>Provides static methods to retrieve runtime properties, memory
 * usage (heap and non-heap), memory pool details, OS info, thread
 * stats, JIT compilation stats, and garbage collector stats.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see JVMInfo
 * @see MemoryInfo
 */
public class JMXInfo {

	/** Prefix key for JVM memory metrics. */
	public static final String JVM_MEMORY = "jvm.memory";
	/** Prefix key for JVM memory pool metrics. */
	public static final String JVM_MEMORY_POOL = "jvm.memory.pool";

	/**
	 * Returns JVM runtime properties as reported by
	 * {@link RuntimeMXBean}.
	 *
	 * @return a map of property keys to values; never {@code null}
	 */
	public static Map<String, Object> runtime(){

		System.out.println("==========================Runtime=========================");

		Map<String, Object> dataMap = new HashMap<String, Object>();

        RuntimeMXBean runtimeMBean = ManagementFactory.getRuntimeMXBean();

        dataMap.put(JVMProperty.JAVA_VM_NAME.getKey(), runtimeMBean.getVmName() );
        dataMap.put(JVMProperty.JAVA_VM_VERSION.getKey(), runtimeMBean.getVmVersion() );
        dataMap.put(JVMProperty.JAVA_VM_OPTIONS.getKey(), StringUtils.join(runtimeMBean.getInputArguments().iterator(), " ") );
        dataMap.put(JVMProperty.JAVA_CLASS_PATH.getKey(), runtimeMBean.getClassPath() );
        dataMap.put(JVMProperty.JAVA_LIBRARY_PATH.getKey(), runtimeMBean.getLibraryPath() );
        dataMap.put(JVMProperty.JAVA_RUNTIME_STARTTIME.getKey(),  runtimeMBean.getStartTime() );
        dataMap.put(JVMProperty.JAVA_RUNTIME_UPTIME.getKey(),  runtimeMBean.getUptime() );
        dataMap.put(JVMProperty.JAVA_SPECIFICATION_NAME.getKey(),  runtimeMBean.getSpecName() );
        dataMap.put(JVMProperty.JAVA_SPECIFICATION_VERSION.getKey(),  runtimeMBean.getSpecVersion() );
        dataMap.put(JVMProperty.JAVA_SPECIFICATION_VENDER.getKey(),  runtimeMBean.getSpecVendor() );

        return dataMap;

	}

	/**
	 * Returns heap and non-heap memory usage information.
	 *
	 * @param unit the capacity unit for the returned values
	 * @return a list of {@link MemoryInfo} entries; never {@code null}
	 */
	public static List<MemoryInfo> memory(Unit unit){

		List<MemoryInfo> dataList = new ArrayList<MemoryInfo>();

		//==========================Memory=========================

		MemoryMXBean memoryMBean = ManagementFactory.getMemoryMXBean();

		MemoryUsage usage = memoryMBean.getHeapMemoryUsage();

		Map<String, Long> heapUsageMap = new HashMap<String, Long>();

		heapUsageMap.put(MemProperty.MEM_INIT.getKey(), usage.getInit());
		heapUsageMap.put(MemProperty.MEM_USED.getKey(), usage.getUsed());
		heapUsageMap.put(MemProperty.MEM_COMMITTED.getKey(), usage.getCommitted());
		heapUsageMap.put(MemProperty.MEM_MAX.getKey(), usage.getMax());

		dataList.add(new MemoryInfo(JVM_MEMORY, "HeapMemoryUsage", heapUsageMap, unit));

		MemoryUsage nousage = memoryMBean.getNonHeapMemoryUsage();
		Map<String, Long> nonHeapUsageMap = new HashMap<String, Long>();

		nonHeapUsageMap.put(MemProperty.MEM_INIT.getKey(), nousage.getInit());
		nonHeapUsageMap.put(MemProperty.MEM_USED.getKey(), nousage.getUsed());
		nonHeapUsageMap.put(MemProperty.MEM_COMMITTED.getKey(), nousage.getCommitted());
		nonHeapUsageMap.put(MemProperty.MEM_MAX.getKey(), nousage.getMax());

		dataList.add(new MemoryInfo(JVM_MEMORY, "NonHeapMemoryUsage", nonHeapUsageMap, unit));

		return dataList;

	}

	/**
	 * Returns memory pool usage and peak usage for all memory pools.
	 *
	 * @param unit the capacity unit for the returned values
	 * @return a list of {@link MemoryInfo} entries; never {@code null}
	 */
	public static List<MemoryInfo> memoryPool(Unit unit){

		List<MemoryInfo> dataList = new ArrayList<MemoryInfo>();

		//==========================MemoryPool=========================

        List<MemoryPoolMXBean> mpMBeanList = ManagementFactory.getMemoryPoolMXBeans();
        for(MemoryPoolMXBean mpMBean : mpMBeanList){

        	String prefix = JVM_MEMORY_POOL + "." + StringUtils.join(mpMBean.getName().split(" "),"_");

        	MemoryUsage usage = mpMBean.getUsage();
        	Map<String, Long> usageMap = new HashMap<String, Long>();

        	usageMap.put(MemProperty.MEM_INIT.getKey(), usage.getInit());
        	usageMap.put(MemProperty.MEM_USED.getKey(), usage.getUsed());
        	usageMap.put(MemProperty.MEM_COMMITTED.getKey(), usage.getCommitted());
        	usageMap.put(MemProperty.MEM_MAX.getKey(), usage.getMax());

        	dataList.add(new MemoryInfo(prefix, "Usage", usageMap, unit));

        	MemoryUsage peakUsage = mpMBean.getPeakUsage();
        	Map<String, Long> peakUsageMap = new HashMap<String, Long>();

        	peakUsageMap.put(MemProperty.MEM_INIT.getKey(), peakUsage.getInit());
        	peakUsageMap.put(MemProperty.MEM_USED.getKey(), peakUsage.getUsed());
        	peakUsageMap.put(MemProperty.MEM_COMMITTED.getKey(), peakUsage.getCommitted());
        	peakUsageMap.put(MemProperty.MEM_MAX.getKey(), peakUsage.getMax());

        	dataList.add(new MemoryInfo(prefix, "PeakUsage", peakUsageMap, unit));

        }

		return dataList;

	}

	/**
	 * Returns basic operating system information.
	 *
	 * @return a map of OS property keys to values; never {@code null}
	 */
	public static Map<String, Object> os(){

		//==========================OperatingSystem=========================
		Map<String, Object> dataMap = new HashMap<String, Object>();

        OperatingSystemMXBean osMBean = ManagementFactory.getOperatingSystemMXBean();
        dataMap.put("os.name", osMBean.getName());
        dataMap.put("os.arch", osMBean.getArch());
        dataMap.put("os.version", osMBean.getVersion());
        dataMap.put("os.cores", osMBean.getAvailableProcessors());


        return dataMap;

	}

	/**
	 * Returns thread-related metrics from the current JVM.
	 *
	 * @return a map of thread metric keys to values; never {@code null}
	 */
	public static Map<String, Object> thread(){

		//==========================Thread=========================
		Map<String, Object> dataMap = new HashMap<String, Object>();

        ThreadMXBean threadMBean = ManagementFactory.getThreadMXBean();

        dataMap.put("jvm.thread.CurrentThreadCpuTime", threadMBean.getCurrentThreadCpuTime());
        dataMap.put("jvm.thread.CurrentThreadUserTime", threadMBean.getCurrentThreadUserTime());
        dataMap.put("jvm.thread.DaemonThreadCount", threadMBean.getDaemonThreadCount());
        dataMap.put("jvm.thread.PeakThreadCount", threadMBean.getPeakThreadCount());
        dataMap.put("jvm.thread.ThreadCount", threadMBean.getThreadCount());
        dataMap.put("jvm.thread.TotalStartedThreadCount", threadMBean.getTotalStartedThreadCount());

        return dataMap;

	}

	/**
	 * Returns JIT compilation statistics.
	 *
	 * @return a map containing the compiler name and total compilation time;
	 *         never {@code null}
	 */
	public static Map<String, Object> compilation(){

		//==========================Compilation=========================
		Map<String, Object> dataMap = new HashMap<String, Object>();

        CompilationMXBean compilMBean = ManagementFactory.getCompilationMXBean();

        dataMap.put("jvm.compilation.name", compilMBean.getName());
        dataMap.put("jvm.compilation.totalCompilationTime", compilMBean.getTotalCompilationTime());

        return dataMap;

	}

	/**
	 * Returns garbage collector statistics. Note: if multiple GCs
	 * exist, only the last one's stats are retained in the map.
	 *
	 * @return a map of GC metric keys to values; never {@code null}
	 */
	public static Map<String, Object> gc(){

		 //==========================GarbageCollector=========================
		Map<String, Object> dataMap = new HashMap<String, Object>();

        List<GarbageCollectorMXBean> gcMBeanList = ManagementFactory.getGarbageCollectorMXBeans();
        for(GarbageCollectorMXBean gcMBean : gcMBeanList){
            dataMap.put("jvm.gc.name", gcMBean.getName());
            dataMap.put("jvm.gc.count", gcMBean.getCollectionCount());
            dataMap.put("jvm.gc.time", gcMBean.getCollectionTime());
        }

        return dataMap;

	}

}
