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
 * Comprehensive JVM summary utility that replicates the information
 * visible in JConsole's VM Summary tab.
 *
 * <p>Provides static methods to collect JVM properties, runtime info,
 * class loading stats, compilation stats, OS info, thread stats,
 * memory usage (heap/non-heap), memory pool details, GC stats, and
 * runtime memory with unit conversion.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see JMXInfo
 * @see JVMProperty
 * @see JVMOSProperty
 */
public class JVMInfo {

	/** Prefix key for JVM memory metrics. */
	public static final String JVM_MEMORY = "jvm.memory";
	/** Prefix key for JVM memory pool metrics. */
	public static final String JVM_MEMORY_POOL = "jvm.memory.pool";

	/**
	 * Returns the current process ID.
	 *
	 * @return the PID, or {@code -1} if it cannot be determined
	 */
	public static final int pid() {
        try {
            return (int) ProcessHandle.current().pid();
        } catch (Exception e) {
            return -1;
        }
    }

	/**
	 * Returns a comprehensive map of JVM information including system
	 * properties, runtime data, class loading, compilation, OS info,
	 * and thread metrics.
	 *
	 * @return a map of metric keys to values; never {@code null}
	 */
	public static Map<String, Object> info() {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		Properties props = System.getProperties();
		for (JVMProperty vm : JVMProperty.values()) {
			dataMap.put(vm.getKey(), props.getProperty(vm.getKey()));
		}

		for (JVMOSProperty vm : JVMOSProperty.values()) {
			dataMap.put(vm.getKey(), props.getProperty(vm.getKey()));
		}

		// ==========================Runtime=========================

		RuntimeMXBean runtimeMBean = ManagementFactory.getRuntimeMXBean();

		dataMap.put(JVMProperty.JAVA_VM_PID.getKey(), runtimeMBean.getName());
		dataMap.put(JVMProperty.JAVA_VM_NAME.getKey(), runtimeMBean.getVmName());
		dataMap.put(JVMProperty.JAVA_VM_VENDOR.getKey(), runtimeMBean.getVmVendor());
		dataMap.put(JVMProperty.JAVA_VM_VERSION.getKey(), runtimeMBean.getVmVersion());
		dataMap.put(JVMProperty.JAVA_VM_OPTIONS.getKey(), StringUtils.join(runtimeMBean.getInputArguments().iterator(), " "));
		dataMap.put(JVMProperty.JAVA_BOOT_CLASS_PATH.getKey(), runtimeMBean.getBootClassPath());
		dataMap.put(JVMProperty.JAVA_CLASS_PATH.getKey(), runtimeMBean.getClassPath());
		dataMap.put(JVMProperty.JAVA_LIBRARY_PATH.getKey(), runtimeMBean.getLibraryPath());
		dataMap.put(JVMProperty.JAVA_RUNTIME_STARTTIME.getKey(), runtimeMBean.getStartTime());
		dataMap.put(JVMProperty.JAVA_RUNTIME_UPTIME.getKey(), runtimeMBean.getUptime());
		dataMap.put(JVMProperty.JAVA_SPECIFICATION_NAME.getKey(), runtimeMBean.getSpecName());
		dataMap.put(JVMProperty.JAVA_SPECIFICATION_VENDER.getKey(), runtimeMBean.getSpecVendor());
		dataMap.put(JVMProperty.JAVA_SPECIFICATION_VERSION.getKey(), runtimeMBean.getSpecVersion());
		dataMap.put(JVMProperty.JAVA_MANAGEMENT_SPECIFICATION_VERSION.getKey(), runtimeMBean.getManagementSpecVersion());

		// ==========================ClassLoading=========================

		ClassLoadingMXBean mxBean = ManagementFactory.getClassLoadingMXBean();
		dataMap.put("jvm.class.LoadedCount", mxBean.getLoadedClassCount());
		dataMap.put("jvm.class.TotalLoadedCount", mxBean.getTotalLoadedClassCount());
		dataMap.put("jvm.class.UnloadedCount", mxBean.getUnloadedClassCount());

		// ==========================Compilation=========================

		CompilationMXBean compilMBean = ManagementFactory.getCompilationMXBean();
		dataMap.put("jvm.compilation.name", compilMBean.getName());
		dataMap.put("jvm.compilation.totalCompilationTime", compilMBean.getTotalCompilationTime());

		// ==========================OperatingSystem=========================

		OperatingSystemMXBean osMBean = ManagementFactory.getOperatingSystemMXBean();
		dataMap.put("os.name", osMBean.getName());
		dataMap.put("os.arch", osMBean.getArch());
		dataMap.put("os.version", osMBean.getVersion());
		dataMap.put("os.cores", osMBean.getAvailableProcessors());
		dataMap.put("os.loadaverage", osMBean.getSystemLoadAverage());

		// ==========================Thread=========================

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
	 * Returns JVM runtime memory statistics (max, total, used, free,
	 * usage ratio) converted to the specified unit.
	 *
	 * @param unit the target capacity unit
	 * @return a map of memory metric keys to values; never {@code null}
	 */
	public static Map<String, Object> runtime(Unit unit){

		Runtime r = Runtime.getRuntime();
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put(JVM_MEMORY + ".max", CapacityUtils.getCapacity( r.maxMemory(), unit) );
		dataMap.put(JVM_MEMORY + ".total", CapacityUtils.getCapacity( r.totalMemory(), unit) );
		dataMap.put(JVM_MEMORY + ".used", CapacityUtils.getCapacity( r.totalMemory() - r.freeMemory(), unit));
		dataMap.put(JVM_MEMORY + ".free", CapacityUtils.getCapacity( r.freeMemory(), unit));
		dataMap.put(JVM_MEMORY + ".usage", CapacityUtils.div(r.totalMemory() - r.freeMemory(), r.totalMemory(), 2));

		return dataMap;

	}

	/**
	 * Returns the JVM memory usage ratio.
	 *
	 * @return a map containing {@code "jvm.memory.usage"} as a
	 *         {@link Double}; never {@code null}
	 */
	public static Map<String, Double> usage() {

		Map<String, Double> dataMap = new HashMap<String, Double>();

		Runtime r = Runtime.getRuntime();
		dataMap.put(JVM_MEMORY + ".usage", CapacityUtils.div(r.totalMemory()-r.freeMemory(), r.totalMemory(), 2));

		return dataMap;
	}

	/**
	 * Returns heap and non-heap memory usage information converted
	 * to the specified unit.
	 *
	 * @param unit the target capacity unit
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
	 * Returns memory pool usage and peak usage for all memory pools,
	 * converted to the specified unit.
	 *
	 * @param unit the target capacity unit
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
	 * Returns garbage collector statistics for all registered GC beans.
	 *
	 * @return a list of maps, each containing {@code jvm.gc.name},
	 *         {@code jvm.gc.count}, and {@code jvm.gc.time}; never
	 *         {@code null}
	 */
	public static List<Map<String, Object>> gc(){

		//==========================GarbageCollector=========================

		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();

		Map<String, Object> dataMap = null;

        List<GarbageCollectorMXBean> gcMBeanList = ManagementFactory.getGarbageCollectorMXBeans();
        for(GarbageCollectorMXBean gcMBean : gcMBeanList){
        	dataMap = new HashMap<String, Object>();
        	dataMap.put("jvm.gc.name", gcMBean.getName());
            dataMap.put("jvm.gc.count", gcMBean.getCollectionCount());
            dataMap.put("jvm.gc.time", gcMBean.getCollectionTime());
            dataList.add(dataMap);
        }

        return dataList;

	}

}
