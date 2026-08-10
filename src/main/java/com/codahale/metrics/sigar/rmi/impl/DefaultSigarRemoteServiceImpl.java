package com.codahale.metrics.sigar.rmi.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.codahale.metrics.sigar.rmi.SigarRMIInfo;
import com.codahale.metrics.sigar.rmi.SigarRemoteService;
import com.codahale.metrics.sigar.rmi.SystemRuntime;

/**
 * Default RMI implementation of {@link SigarRemoteService}.
 *
 * <p>Gathers memory, CPU, and network metrics using {@link SystemRuntime}
 * and packages them into a {@link SigarRMIInfo} data transfer object.
 * Memory values are reported in megabytes; network speeds in KB/s.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see SigarRemoteService
 * @see SystemRuntime
 * @see SigarRMIInfo
 */
@SuppressWarnings("serial")
public class DefaultSigarRemoteServiceImpl extends UnicastRemoteObject implements SigarRemoteService {

	/** Date format used for logging invocation timestamps. */
	protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** The IP address of the network interface to monitor. */
	protected String address_ip;

	/**
	 * Constructs a new remote service bound to the specified IP address.
	 *
	 * @param address_ip the IP address of the network interface to monitor;
	 *                   must not be {@code null}
	 * @throws RemoteException if the RMI infrastructure cannot export this object
	 */
	public DefaultSigarRemoteServiceImpl(String address_ip) throws RemoteException {
		super();
		this.address_ip = address_ip;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Gathers current memory (total/used), CPU combined usage, and
	 * network RX/TX speeds, packaging them into a {@link SigarRMIInfo}.</p>
	 */
	@Override
	public SigarRMIInfo getRuntime() throws RemoteException {
		System.out.println("调用服务");
		SigarRMIInfo sp = new SigarRMIInfo();
		try {

			System.out.println("当前时间：" + df.format(new Date()));
			SystemRuntime sy = new SystemRuntime();
			sp.setMemory_total(sy.memory().getTotal() / 1024L / 1024L);
			System.out.println("总共内存(M)：" + sp.getMemory_total());
			sp.setMemory_uesd(sy.memory().getUsed() / 1024L / 1024L);
			System.out.println("使用内存(M)：" + sp.getMemory_uesd());
			sp.setCpu_combined(sy.cpu().getCombined());
			System.out.println("CPU(%)：" + sp.getCpu_combined());
			float[] net = sy.net(this.address_ip);
			sp.setRx_speed(net[0]);
			System.out.println("下载(kb/s)：" + sp.getRx_speed());
			sp.setTx_speed(net[1]);
			System.out.println("上传(kb/s)：" + sp.getTx_speed());
		} catch (Exception e) {
			System.out.println(e);
		}
		return sp;
	}

}
