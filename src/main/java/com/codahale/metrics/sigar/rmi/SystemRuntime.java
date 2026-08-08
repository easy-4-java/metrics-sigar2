package com.codahale.metrics.sigar.rmi;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import kamon.sigar.SigarProvisioner;

/**
 * Utility class that provisions the Sigar native library and exposes
 * convenience methods for querying memory, CPU, filesystem, and
 * network statistics.
 *
 * <p>Each instance lazily provisions the native Sigar libraries on
 * construction. Call {@link #update()} to re-initialize the underlying
 * {@link Sigar} instance.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see Sigar
 * @see SigarProvisioner
 */
public class SystemRuntime {

	private Sigar sigar = null;

	/**
	 * Re-provisions the Sigar native library and re-creates the
	 * internal {@link Sigar} instance.
	 *
	 * @throws Exception if provisioning or Sigar initialization fails
	 */
	public void update() throws Exception {
		SigarProvisioner.provision();
		sigar = new Sigar();
	}

	/**
	 * Constructs a new {@code SystemRuntime}, provisioning the Sigar
	 * native library if necessary.
	 *
	 * @throws Exception if provisioning or Sigar initialization fails
	 */
	public SystemRuntime() throws Exception {
		SigarProvisioner.provision();
		sigar = new Sigar();
	}

	/**
	 * Returns a snapshot of physical memory usage.
	 *
	 * @return a Sigar {@link Mem} bean
	 * @throws SigarException if the memory information cannot be retrieved
	 */
	public Mem memory() throws SigarException {
		Mem mem = sigar.getMem();
		return mem;
	}

	/**
	 * Returns a snapshot of overall CPU usage percentages.
	 *
	 * @return a Sigar {@link CpuPerc} bean
	 * @throws SigarException if the CPU information cannot be retrieved
	 */
	public CpuPerc cpu() throws SigarException {
		CpuPerc perc = sigar.getCpuPerc();
		return perc;
	}

	/**
	 * Prints filesystem information for all mounted file systems to
	 * standard output. Intended for diagnostic / debugging use.
	 *
	 * @throws Exception if filesystem information cannot be retrieved
	 */
	public void file() throws Exception {
		FileSystem fslist[] = sigar.getFileSystemList();
		for (int i = 0; i < fslist.length; i++) {
			System.out.println("分区的盘符名称" + i);
			FileSystem fs = fslist[i];
			// 分区的盘符名称
			System.out.println("盘符名称:    " + fs.getDevName());
			// 分区的盘符名称
			System.out.println("盘符路径:    " + fs.getDirName());
			System.out.println("盘符标志:    " + fs.getFlags());//
			// 文件系统类型，比如 FAT32、NTFS
			System.out.println("盘符类型:    " + fs.getSysTypeName());
			// 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
			System.out.println("盘符类型名:    " + fs.getTypeName());
			// 文件系统类型
			System.out.println("盘符文件系统类型:    " + fs.getType());
			FileSystemUsage usage = null;
			usage = sigar.getFileSystemUsage(fs.getDirName());
			System.out.println(fs.getDevName() + "读出：    " + usage.getDiskReads());
			System.out.println(fs.getDevName() + "写入：    " + usage.getDiskWrites());
		}
		return;
	}

	/**
	 * Measures the network receive and transmit throughput for the
	 * interface bound to the given IP address over a 500 ms sampling
	 * window.
	 *
	 * @param ip the IP address of the network interface to measure
	 * @return a two-element float array where {@code [0]} is the
	 *         receive speed in KB/ms and {@code [1]} is the transmit
	 *         speed in KB/ms; returns {@code {0f, 0f}} if the
	 *         interface is not found
	 * @throws Exception if network statistics cannot be retrieved
	 */
	public float[] net(String ip) throws Exception {
		float[] result = { 0f, 0f };
		if (netBytes(ip) == null){
			return result;
		}
		update();
		long time = System.currentTimeMillis();
		long rx = netBytes(ip).getRxBytes();
		long tx = netBytes(ip).getTxBytes();
		Thread.sleep(500);
		update();
		time = System.currentTimeMillis() - time;
		rx = netBytes(ip).getRxBytes() - rx;
		tx = netBytes(ip).getTxBytes() - tx;
		result[0] = rx * 1f / time;// kb/sec
		result[1] = tx * 1f / time;
		return result;
	}

	/**
	 * Returns the network interface statistics for the interface bound
	 * to the given IP address.
	 *
	 * @param ip the IP address to look up
	 * @return a Sigar {@link NetInterfaceStat} for the matching interface,
	 *         or {@code null} if no interface matches the given IP
	 * @throws Exception if network interface information cannot be retrieved
	 */
	public NetInterfaceStat netBytes(String ip) throws Exception {
		String ifNames[] = sigar.getNetInterfaceList();
		NetInterfaceStat result = null;
		for (int i = 0; i < ifNames.length; i++) {
			String name = ifNames[i];
			NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
			if (ifconfig.getAddress().equals(ip)) {
				result = sigar.getNetInterfaceStat(name);
				break;
			}
		}
		return result;
	}
}
