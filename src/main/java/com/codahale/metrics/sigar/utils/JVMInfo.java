package com.codahale.metrics.sigar.utils;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

/**
 * VM 概要,实现 jconsole看到的VM概要信息的获取
 */
public class JVMInfo {

	public static final String JVM_MEMORY = "jvm.memory";
	public static final String JVM_MEMORY_POOL = "jvm.memory.pool";
	
	public static final int pid() {
        try {
            return (int) ProcessHandle.current().pid();
        } catch (Exception e) {
            return -1;
        }
    }
	
	public static Map<String, Object> info() {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		Properties props = System.getProperties();
		// JVM属性
		for (JVMProperty vm : JVMProperty.values()) {
			dataMap.put(vm.getKey(), props.getProperty(vm.getKey()));
		}
		
		//获取操作系统相关信息  
		for (JVMOSProperty vm : JVMOSProperty.values()) {
			dataMap.put(vm.getKey(), props.getProperty(vm.getKey()));
		}

		// ==========================Runtime=========================

		RuntimeMXBean runtimeMBean = ManagementFactory.getRuntimeMXBean();

		dataMap.put(JVMProperty.JAVA_VM_PID.getKey(), runtimeMBean.getName());
		// 返回 Java 虚拟机实现名称。
		dataMap.put(JVMProperty.JAVA_VM_NAME.getKey(), runtimeMBean.getVmName());
		// 返回 Java 虚拟机实现供应商。
		dataMap.put(JVMProperty.JAVA_VM_VENDOR.getKey(), runtimeMBean.getVmVendor());
		// 返回 Java 虚拟机实现版本。
		dataMap.put(JVMProperty.JAVA_VM_VERSION.getKey(), runtimeMBean.getVmVersion());
		// 返回传递给 Java 虚拟机的输入变量，其中不包括传递给 main 方法的变量。
		dataMap.put(JVMProperty.JAVA_VM_OPTIONS.getKey(), StringUtils.join(runtimeMBean.getInputArguments().iterator(), " "));
		// 返回由引导类加载器用于搜索类文件的引导类路径。
		dataMap.put(JVMProperty.JAVA_BOOT_CLASS_PATH.getKey(), runtimeMBean.getBootClassPath());
		// 返回系统类加载器用于搜索类文件的 Java 类路径。
		dataMap.put(JVMProperty.JAVA_CLASS_PATH.getKey(), runtimeMBean.getClassPath());
		// 返回 Java 库路径。
		dataMap.put(JVMProperty.JAVA_LIBRARY_PATH.getKey(), runtimeMBean.getLibraryPath());
		// 返回 Java 虚拟机的启动时间（以毫秒为单位）。
		dataMap.put(JVMProperty.JAVA_RUNTIME_STARTTIME.getKey(), runtimeMBean.getStartTime());
		// 返回 Java 虚拟机的正常运行时间（以毫秒为单位）。
		dataMap.put(JVMProperty.JAVA_RUNTIME_UPTIME.getKey(), runtimeMBean.getUptime());
		// 返回 Java 虚拟机规范名称。
		dataMap.put(JVMProperty.JAVA_SPECIFICATION_NAME.getKey(), runtimeMBean.getSpecName());
		// 返回 Java 虚拟机规范供应商。
		dataMap.put(JVMProperty.JAVA_SPECIFICATION_VENDER.getKey(), runtimeMBean.getSpecVendor());
		// 返回 Java 虚拟机规范版本。
		dataMap.put(JVMProperty.JAVA_SPECIFICATION_VERSION.getKey(), runtimeMBean.getSpecVersion());
		// 返回正在运行的 Java 虚拟机实现的管理接口的规范版本。
		dataMap.put(JVMProperty.JAVA_MANAGEMENT_SPECIFICATION_VERSION.getKey(), runtimeMBean.getManagementSpecVersion());

		// ==========================ClassLoading=========================

		ClassLoadingMXBean mxBean = ManagementFactory.getClassLoadingMXBean();
		// 返回当前加载到 Java 虚拟机中的类的数量。
		dataMap.put("jvm.class.LoadedCount", mxBean.getLoadedClassCount());
		// 返回自 Java 虚拟机开始执行到目前已经加载的类的总数。
		dataMap.put("jvm.class.TotalLoadedCount", mxBean.getTotalLoadedClassCount());
		// 返回自 Java 虚拟机开始执行到目前已经卸载的类的总数。
		dataMap.put("jvm.class.UnloadedCount", mxBean.getUnloadedClassCount());
		
		// ==========================Compilation=========================

		CompilationMXBean compilMBean = ManagementFactory.getCompilationMXBean();
		// 返回即时 (JIT) 编译器的名称。
		dataMap.put("jvm.compilation.name", compilMBean.getName());
		// 返回在编译上花费的累积耗费时间的近似值（以毫秒为单位）。
		dataMap.put("jvm.compilation.totalCompilationTime", compilMBean.getTotalCompilationTime());

		// ==========================OperatingSystem=========================
		
		OperatingSystemMXBean osMBean = ManagementFactory.getOperatingSystemMXBean();
		// 获取操作系统名称
		dataMap.put("os.name", osMBean.getName());
		// 返回操作系统的架构。
		dataMap.put("os.arch", osMBean.getArch());
		// 返回操作系统的版本。
		dataMap.put("os.version", osMBean.getVersion());
		// 返回 Java 虚拟机可以使用的处理器数目。
		dataMap.put("os.cores", osMBean.getAvailableProcessors());
		// 返回最后一分钟内系统加载平均值。
		dataMap.put("os.loadaverage", osMBean.getSystemLoadAverage());
		/*if (osMBean instanceof com.sun.management.OperatingSystemMXBean) {  
           com.sun.management.OperatingSystemMXBean sunOSMXBean = (com.sun.management.OperatingSystemMXBean) osMBean;  
           dataMap.put("os.ProcessCpuTime", sunOSMXBean.getProcessCpuTime());
		} */ 

		// ==========================Thread=========================

		// 获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况
		ThreadMXBean threadMBean = ManagementFactory.getThreadMXBean();

		// 返回当前线程的总 CPU 时间（以毫微秒为单位）。
		dataMap.put("jvm.thread.CurrentThreadCpuTime", threadMBean.getCurrentThreadCpuTime());
		// 返回当前线程在用户模式中执行的 CPU 时间（以毫微秒为单位）。
		dataMap.put("jvm.thread.CurrentThreadUserTime", threadMBean.getCurrentThreadUserTime());
		// 返回活动守护线程的当前数目。
		dataMap.put("jvm.thread.DaemonThreadCount", threadMBean.getDaemonThreadCount());
		// 返回自从 Java 虚拟机启动或峰值重置以来峰值活动线程计数。
		dataMap.put("jvm.thread.PeakThreadCount", threadMBean.getPeakThreadCount());
		// 返回活动线程的当前数目，包括守护线程和非守护线程。
		dataMap.put("jvm.thread.ThreadCount", threadMBean.getThreadCount());
		// 返回自从 Java 虚拟机启动以来创建和启动的线程总数目。
		dataMap.put("jvm.thread.TotalStartedThreadCount", threadMBean.getTotalStartedThreadCount());
		
		return dataMap;

	}
	
	public static Map<String, Object> runtime(Unit unit){
		
		Runtime r = Runtime.getRuntime();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//Java 虚拟机中的内存总量,以字节为单位  
		
		// 返回 Java 虚拟机试图使用的最大内存量。
		dataMap.put(JVM_MEMORY + ".max", CapacityUtils.getCapacity( r.maxMemory(), unit) );// JVM（这个进程）能够从操作系统那里分配到的最大的内存
		// 返回 Java 虚拟机中的内存总量。
		dataMap.put(JVM_MEMORY + ".total", CapacityUtils.getCapacity( r.totalMemory(), unit) );// JVM现在已经从操作系统那里分配到的内存
		dataMap.put(JVM_MEMORY + ".used", CapacityUtils.getCapacity( r.totalMemory() - r.freeMemory(), unit));// JVM使用内存
		// 返回 Java 虚拟机中的空闲内存量。
		dataMap.put(JVM_MEMORY + ".free", CapacityUtils.getCapacity( r.freeMemory(), unit));// JVM剩余内存
		dataMap.put(JVM_MEMORY + ".usage", CapacityUtils.div(r.totalMemory() - r.freeMemory(), r.totalMemory(), 2));// JVM使用率
		
		return dataMap;
		
	}
	
	public static Map<String, Double> usage() {
		
		Map<String, Double> dataMap = new HashMap<String, Double>();
		
		Runtime r = Runtime.getRuntime();
		dataMap.put(JVM_MEMORY + ".usage", CapacityUtils.div(r.totalMemory()-r.freeMemory(), r.totalMemory(), 2));// JVM使用率

		return dataMap;
	}
	
	public static List<MemoryInfo> memory(Unit unit){
		
		List<MemoryInfo> dataList = new ArrayList<MemoryInfo>();
		
		//==========================Memory=========================
		
		MemoryMXBean memoryMBean = ManagementFactory.getMemoryMXBean();  
		
		/*
    	
    	MemoryUsage 对象包含四个值： 

    	init  表示 Java 虚拟机在启动期间从操作系统请求的用于内存管理的初始内存容量（以字节为单位）。Java 虚拟机可能在运行过程中从操作系统请求更多的内存，也可能将内存释放给系统。init 的值可以是不明确的。  
    	used  表示当前已经使用的内存量（以字节为单位）。  
    	committed  表示保证可以由 Java 虚拟机使用的内存量（以字节为单位）。已提交的内存量可以随时间而变化（增加或减少）。Java 虚拟机可能会将内存释放给系统，committed 可以小于 init。committed 将始终大于或等于 used。  
    	max  表示可以用于内存管理的最大内存量（以字节为单位）。可以不定义其值。如果定义了该值，最大内存量可能随时间而更改。已使用的内存量和已提交的内存量将始终小于或等于 max（如果定义了 max）。如果内存分配试图增加满足以下条件的已使用内存将会失败：used > committed，即使 used <= max 仍然为 true（例如，当系统的虚拟内存不足时）。  

    	*/
		
		MemoryUsage usage = memoryMBean.getHeapMemoryUsage();   
		
		Map<String, Long> heapUsageMap = new HashMap<String, Long>();
		// 返回 Java 虚拟机最初从操作系统请求用于内存管理的内存量（以字节为单位）。
		heapUsageMap.put(MemProperty.MEM_INIT.getKey(), usage.getInit());
		// 返回已使用的内存量（以字节为单位）。
		heapUsageMap.put(MemProperty.MEM_USED.getKey(), usage.getUsed());
		// 返回已提交给 Java 虚拟机使用的内存量（以字节为单位）。
		heapUsageMap.put(MemProperty.MEM_COMMITTED.getKey(), usage.getCommitted());
		// 返回可以用于内存管理的最大内存量（以字节为单位）。
		heapUsageMap.put(MemProperty.MEM_MAX.getKey(), usage.getMax());
		
		dataList.add(new MemoryInfo(JVM_MEMORY, "HeapMemoryUsage", heapUsageMap, unit));
		
		MemoryUsage nousage = memoryMBean.getNonHeapMemoryUsage(); 
		
		Map<String, Long> nonHeapUsageMap = new HashMap<String, Long>();
		// 返回 Java 虚拟机最初从操作系统请求用于内存管理的内存量（以字节为单位）。
		nonHeapUsageMap.put(MemProperty.MEM_INIT.getKey(), nousage.getInit());
		// 返回已使用的内存量（以字节为单位）。
		nonHeapUsageMap.put(MemProperty.MEM_USED.getKey(), nousage.getUsed());
		// 返回已提交给 Java 虚拟机使用的内存量（以字节为单位）。
		nonHeapUsageMap.put(MemProperty.MEM_COMMITTED.getKey(), nousage.getCommitted());
		// 返回可以用于内存管理的最大内存量（以字节为单位）。
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
        	
        	/*
        	
        	MemoryUsage 对象包含四个值： 

        	init  表示 Java 虚拟机在启动期间从操作系统请求的用于内存管理的初始内存容量（以字节为单位）。Java 虚拟机可能在运行过程中从操作系统请求更多的内存，也可能将内存释放给系统。init 的值可以是不明确的。  
        	used  表示当前已经使用的内存量（以字节为单位）。  
        	committed  表示保证可以由 Java 虚拟机使用的内存量（以字节为单位）。已提交的内存量可以随时间而变化（增加或减少）。Java 虚拟机可能会将内存释放给系统，committed 可以小于 init。committed 将始终大于或等于 used。  
        	max  表示可以用于内存管理的最大内存量（以字节为单位）。可以不定义其值。如果定义了该值，最大内存量可能随时间而更改。已使用的内存量和已提交的内存量将始终小于或等于 max（如果定义了 max）。如果内存分配试图增加满足以下条件的已使用内存将会失败：used > committed，即使 used <= max 仍然为 true（例如，当系统的虚拟内存不足时）。  

        	*/
        	
        	// 返回此内存池的内存使用量的估计数。
        	MemoryUsage usage = mpMBean.getUsage();   
        	Map<String, Long> usageMap = new HashMap<String, Long>();
        	
        	// 返回 Java 虚拟机最初从操作系统请求用于内存管理的内存量（以字节为单位）。
        	usageMap.put(MemProperty.MEM_INIT.getKey(), usage.getInit());
        	// 返回已使用的内存量（以字节为单位）。
        	usageMap.put(MemProperty.MEM_USED.getKey(), usage.getUsed());
        	// 返回已提交给 Java 虚拟机使用的内存量（以字节为单位）。
        	usageMap.put(MemProperty.MEM_COMMITTED.getKey(), usage.getCommitted());
        	// 返回可以用于内存管理的最大内存量（以字节为单位）。
        	usageMap.put(MemProperty.MEM_MAX.getKey(), usage.getMax());
        	
        	dataList.add(new MemoryInfo(prefix, "Usage", usageMap, unit));
        	
            // 返回自 Java 虚拟机启动以来或自峰值重置以来此内存池的峰值内存使用量。
        	MemoryUsage peakUsage = mpMBean.getPeakUsage();   
        	Map<String, Long> peakUsageMap = new HashMap<String, Long>();
        	
        	// 返回 Java 虚拟机最初从操作系统请求用于内存管理的内存量（以字节为单位）。
        	peakUsageMap.put(MemProperty.MEM_INIT.getKey(), peakUsage.getInit());
        	// 返回已使用的内存量（以字节为单位）。
        	peakUsageMap.put(MemProperty.MEM_USED.getKey(), peakUsage.getUsed());
        	// 返回已提交给 Java 虚拟机使用的内存量（以字节为单位）。
        	peakUsageMap.put(MemProperty.MEM_COMMITTED.getKey(), peakUsage.getCommitted());
        	// 返回可以用于内存管理的最大内存量（以字节为单位）。
        	peakUsageMap.put(MemProperty.MEM_MAX.getKey(), peakUsage.getMax());
        	
        	dataList.add(new MemoryInfo(prefix, "PeakUsage", peakUsageMap, unit));
            
        }
        
		return dataList;
        
	}
	
	
	public static List<Map<String, Object>> gc(){
		
		//==========================GarbageCollector=========================
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> dataMap = null;
		
		//获取GC的次数以及花费时间之类的信息  
        List<GarbageCollectorMXBean> gcMBeanList = ManagementFactory.getGarbageCollectorMXBeans();  
        for(GarbageCollectorMXBean gcMBean : gcMBeanList){  
        	dataMap = new HashMap<String, Object>();
        	// 回收器名称
        	dataMap.put("jvm.gc.name", gcMBean.getName());
        	// 返回已发生的回收的总次数。
            dataMap.put("jvm.gc.count", gcMBean.getCollectionCount());
            // 返回近似的累积回收时间（以毫秒为单位）。
            dataMap.put("jvm.gc.time", gcMBean.getCollectionTime());
            //名称 = 'PS Scavenge', 收集 = 16, 总花费时间 = 0.066 秒
            dataList.add(dataMap);
        }   
        
        return dataList;
        
	}
	
}
