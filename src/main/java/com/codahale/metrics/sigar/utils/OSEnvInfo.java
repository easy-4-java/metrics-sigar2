package com.codahale.metrics.sigar.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

/**
 * Collects operating system environment and hardware information
 * using Sigar and standard Java APIs.
 *
 * <p>Provides static methods for host info, OS properties, memory
 * usage (RAM + swap + JVM), CPU usage, and disk usage.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see Sigar
 * @see JVMInfo
 */
public class OSEnvInfo {

	/**
	 * Returns a map of host and OS environment properties including
	 * IP address, hostname, OS name/arch/version, and vendor info.
	 *
	 * @param sigar the Sigar instance; must not be {@code null}
	 * @return a map of property keys to values; never {@code null}
	 */
	public static Map<String, Object> info(Sigar sigar){

		Map<String, Object> infoMap = new HashMap<String, Object>();

		//==========================OperatingSystem=========================

        InetAddress addr = null;
		String ip = "127.0.0.1";
		String hostName = "localhost";
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
		if (null != addr) {
			try {
				ip = addr.getHostAddress();
			} catch (Exception e) {
			}
			try {
				hostName = addr.getHostName();
			} catch (Exception e) {
			}
		}
		infoMap.put("host.ip", ip);
		infoMap.put("host.name", hostName);


        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");
        String computerName = map.get("COMPUTERNAME");
        String userDomain = map.get("USERDOMAIN");

        infoMap.put("host.username", userName);
        infoMap.put("host.computer.name", computerName);
        infoMap.put("host.userdomain", userDomain);

        OperatingSystem OS = OperatingSystem.getInstance();

        infoMap.put("os.cpu.endian", OS.getCpuEndian());
        infoMap.put("os.datamodel", OS.getDataModel());
        infoMap.put("os.machine", OS.getMachine());

        infoMap.put("os.name", OS.getName());
        infoMap.put("os.arch", OS.getArch());
        infoMap.put("os.desc", OS.getDescription());
        infoMap.put("os.version", OS.getVersion());
        infoMap.put("os.patch.level", OS.getPatchLevel());
        infoMap.put("os.vendor", OS.getVendor());
        infoMap.put("os.vendor.name", OS.getVendorName());
        infoMap.put("os.vendor.code", OS.getVendorCodeName());
        infoMap.put("os.vendor.version", OS.getVendorVersion());

        return infoMap;

	}

	/**
	 * Returns memory information (JVM + OS RAM + swap) using
	 * {@link Unit#NONE}.
	 *
	 * @param sigar the Sigar instance; must not be {@code null}
	 * @return a map of memory metric keys to values; never {@code null}
	 */
	public static Map<String, Object> memory(Sigar sigar) {
		return memory(sigar, Unit.NONE);
	}

	/**
	 * Returns memory information (JVM + OS RAM + swap) converted to
	 * the specified unit.
	 *
	 * @param sigar the Sigar instance; must not be {@code null}
	 * @param unit  the target capacity unit
	 * @return a map of memory metric keys to values; never {@code null}
	 */
	public static Map<String, Object> memory(Sigar sigar, Unit unit) {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {

			dataMap.putAll(JVMInfo.runtime(unit));

			Mem mem = sigar.getMem();
			dataMap.put("os.ram.total", CapacityUtils.getCapacity( mem.getTotal(), unit));
			dataMap.put("os.ram.used", CapacityUtils.getCapacity( mem.getUsed(), unit));
			dataMap.put("os.ram.free", CapacityUtils.getCapacity( mem.getFree(), unit));
			dataMap.put("os.ram.usage", CapacityUtils.div(mem.getUsed(), mem.getTotal(), 2));

			Swap swap = sigar.getSwap();
			dataMap.put("os.swap.total", CapacityUtils.getCapacity( swap.getTotal(), unit));
			dataMap.put("os.swap.used", CapacityUtils.getCapacity( swap.getUsed(), unit));
			dataMap.put("os.swap.free", CapacityUtils.getCapacity( swap.getFree(), unit));
			dataMap.put("os.swap.usage", CapacityUtils.div(swap.getUsed(), swap.getTotal(), 2));
			dataMap.put("os.timestamp", System.currentTimeMillis());
		} catch (Exception e) {
		}
		return dataMap;
	}

	/**
	 * Returns JVM and OS usage ratios (memory usage, CPU usage).
	 *
	 * @param sigar the Sigar instance; must not be {@code null}
	 * @return a map of usage metric keys to ratio values; never
	 *         {@code null}
	 */
	public static Map<String, Double> usage(Sigar sigar) {

		Map<String, Double> dataMap = new HashMap<String, Double>();

		try {

			dataMap.putAll(JVMInfo.usage());

			Mem mem = sigar.getMem();
			dataMap.put("os.ram.usage", CapacityUtils.div(mem.getUsed(), mem.getTotal(), 2));

 			List<Map<String, Object>> cpu = cpuInfos(sigar);
			double b = 0.0;
			for (Map<String, Object> m : cpu) {
				b += Double.valueOf(m.get("os.cpu.total")+"");
			}
			dataMap.put("os.cpu.usage", CapacityUtils.div(b, cpu.size(), 2));
			dataMap.put("os.timestamp", Double.valueOf(System.currentTimeMillis()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	/**
	 * Returns per-CPU time breakdown information.
	 *
	 * @param sigar the Sigar instance; must not be {@code null}
	 * @return a list of maps, each containing CPU time percentages for
	 *         one logical CPU (user, system, idle, wait, nice, irq,
	 *         softIrq, stolen, total); never {@code null}
	 */
	public static List<Map<String, Object>> cpuInfos(Sigar sigar) {
		List<Map<String, Object>> monitorMaps = new ArrayList<Map<String, Object>>();
		try {
			CpuPerc cpuList[] = sigar.getCpuPercList();
			for (CpuPerc cpuPerc : cpuList) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("os.cpu.irq", cpuPerc.getIrq());
				dataMap.put("os.cpu.softIrq", cpuPerc.getSoftIrq());
				dataMap.put("os.cpu.stolen", cpuPerc.getStolen());
				dataMap.put("os.cpu.nice", cpuPerc.getNice());
				dataMap.put("os.cpu.user",  cpuPerc.getUser());
				dataMap.put("os.cpu.system",  cpuPerc.getSys());
				dataMap.put("os.cpu.wait",  cpuPerc.getWait());
				dataMap.put("os.cpu.idle",  cpuPerc.getIdle());
				dataMap.put("os.cpu.total", cpuPerc.getCombined());
				monitorMaps.add(dataMap);
			}
		} catch (Exception e) {
		}
		return monitorMaps;
	}

	/**
	 * Returns disk usage information for all local disk partitions.
	 *
	 * @param sigar the Sigar instance; must not be {@code null}
	 * @return a list of maps, each containing disk metrics for one
	 *         local partition; never {@code null}
	 * @throws Exception if filesystem information cannot be retrieved
	 */
	public static List<Map<String, Object>> diskInfos(Sigar sigar) throws Exception {
		List<Map<String, Object>> monitorMaps = new ArrayList<Map<String, Object>>();
		FileSystem fslist[] = sigar.getFileSystemList();
		for (int i = 0; i < fslist.length; i++) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			FileSystem fs = fslist[i];
			FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
			switch (fs.getType()) {
				case 0: // TYPE_UNKNOWN
					break;
				case 1: // TYPE_NONE
					break;
				case 2: // TYPE_LOCAL_DISK

					dataMap.put("os.disk.name", fs.getDevName());
					dataMap.put("os.disk.type", fs.getSysTypeName());
					dataMap.put("os.disk.options", fs.getOptions());
					dataMap.put("os.disk.flags", fs.getFlags());
					dataMap.put("os.disk.total", usage.getTotal());
					dataMap.put("os.disk.free", usage.getFree());
					dataMap.put("os.disk.used", usage.getUsed());
					dataMap.put("os.disk.usage", usage.getUsePercent() * 100D);
					monitorMaps.add(dataMap);
					break;
				case 3: // TYPE_NETWORK
					break;
				case 4: // TYPE_RAM_DISK
					break;
				case 5: // TYPE_CDROM
					break;
				case 6: // TYPE_SWAP
					break;
			}
		}
		return monitorMaps;
	}


}
