/*
 * Copyright (c) 2018 (https://github.com/easy-4-java).
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

public class JMXInfo {
    
	public static final String JVM_MEMORY = "jvm.memory";
	public static final String JVM_MEMORY_POOL = "jvm.memory.pool";

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
	
	public static List<MemoryInfo> memoryPool(Unit unit){
		
		List<MemoryInfo> dataList = new ArrayList<MemoryInfo>();
		
		//==========================MemoryPool=========================
		
        //获取多个内存池的使用情况  
        List<MemoryPoolMXBean> mpMBeanList = ManagementFactory.getMemoryPoolMXBeans();  
        for(MemoryPoolMXBean mpMBean : mpMBeanList){
        	
        	String prefix = JVM_MEMORY_POOL + "." + StringUtils.join(mpMBean.getName().split(" "),"_");
        	
        	// 返回此内存池的内存使用量的估计数。
        	MemoryUsage usage = mpMBean.getUsage();   
        	Map<String, Long> usageMap = new HashMap<String, Long>();
        	
        	usageMap.put(MemProperty.MEM_INIT.getKey(), usage.getInit());
        	usageMap.put(MemProperty.MEM_USED.getKey(), usage.getUsed());
        	usageMap.put(MemProperty.MEM_COMMITTED.getKey(), usage.getCommitted());
        	usageMap.put(MemProperty.MEM_MAX.getKey(), usage.getMax());
        	
        	dataList.add(new MemoryInfo(prefix, "Usage", usageMap, unit));
        	
            // 返回自 Java 虚拟机启动以来或自峰值重置以来此内存池的峰值内存使用量。
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
	
	public static Map<String, Object> os(){
		
		//==========================OperatingSystem=========================
		Map<String, Object> dataMap = new HashMap<String, Object>();
        
        OperatingSystemMXBean osMBean = ManagementFactory.getOperatingSystemMXBean();  
        //获取操作系统相关信息  
        dataMap.put("os.name", osMBean.getName());
        dataMap.put("os.arch", osMBean.getArch());
        dataMap.put("os.version", osMBean.getVersion());
        dataMap.put("os.cores", osMBean.getAvailableProcessors());
        
        
        return dataMap;
        
	}
	
	public static Map<String, Object> thread(){
		
		//==========================Thread=========================
		Map<String, Object> dataMap = new HashMap<String, Object>();

        //获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况  
        ThreadMXBean threadMBean = ManagementFactory.getThreadMXBean(); 
        
        dataMap.put("jvm.thread.CurrentThreadCpuTime", threadMBean.getCurrentThreadCpuTime());
        dataMap.put("jvm.thread.CurrentThreadUserTime", threadMBean.getCurrentThreadUserTime());
        dataMap.put("jvm.thread.DaemonThreadCount", threadMBean.getDaemonThreadCount());
        dataMap.put("jvm.thread.PeakThreadCount", threadMBean.getPeakThreadCount());
        dataMap.put("jvm.thread.ThreadCount", threadMBean.getThreadCount());
        dataMap.put("jvm.thread.TotalStartedThreadCount", threadMBean.getTotalStartedThreadCount());
        
        return dataMap;
        
	}
	
	public static Map<String, Object> compilation(){
		
		//==========================Compilation=========================
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
        CompilationMXBean compilMBean = ManagementFactory.getCompilationMXBean();   
        
        dataMap.put("jvm.compilation.name", compilMBean.getName());
        dataMap.put("jvm.compilation.totalCompilationTime", compilMBean.getTotalCompilationTime());
        
        return dataMap;
        
	}
	
	public static Map<String, Object> gc(){
		
		 //==========================GarbageCollector=========================
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		//获取GC的次数以及花费时间之类的信息  
        List<GarbageCollectorMXBean> gcMBeanList = ManagementFactory.getGarbageCollectorMXBeans();  
        for(GarbageCollectorMXBean gcMBean : gcMBeanList){  
            dataMap.put("jvm.gc.name", gcMBean.getName());
            dataMap.put("jvm.gc.count", gcMBean.getCollectionCount());
            dataMap.put("jvm.gc.time", gcMBean.getCollectionTime());
            //名称 = 'PS Scavenge', 收集 = 16, 总花费时间 = 0.066 秒
        }   
        
        return dataMap;
        
	}
	
}
