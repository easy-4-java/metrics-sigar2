package com.codahale.metrics.sigar.rmi;

import java.io.Serializable;

/**
 * Serializable data transfer object carrying a snapshot of system
 * metrics (memory, CPU, and network speeds) for RMI transport.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see SigarRemoteService
 */
@SuppressWarnings("serial")
public class SigarRMIInfo implements Serializable {

	private long memory_uesd;
	private long memory_total;
	private double cpu_combined;
	private float rx_speed;
	private float tx_speed;

	/**
	 * Returns the used memory in megabytes.
	 *
	 * @return used memory in MB
	 */
	public long getMemory_uesd() {
		return memory_uesd;
	}

	/**
	 * Sets the used memory in megabytes.
	 *
	 * @param memory_uesd used memory in MB
	 */
	public void setMemory_uesd(long memory_uesd) {
		this.memory_uesd = memory_uesd;
	}

	/**
	 * Returns the total memory in megabytes.
	 *
	 * @return total memory in MB
	 */
	public long getMemory_total() {
		return memory_total;
	}

	/**
	 * Sets the total memory in megabytes.
	 *
	 * @param memory_total total memory in MB
	 */
	public void setMemory_total(long memory_total) {
		this.memory_total = memory_total;
	}

	/**
	 * Returns the combined CPU usage as a fraction (0.0 - 1.0).
	 *
	 * @return combined CPU usage
	 */
	public double getCpu_combined() {
		return cpu_combined;
	}

	/**
	 * Sets the combined CPU usage.
	 *
	 * @param cpu_combined combined CPU usage as a fraction
	 */
	public void setCpu_combined(double cpu_combined) {
		this.cpu_combined = cpu_combined;
	}

	/**
	 * Returns the network receive speed in kilobytes per second.
	 *
	 * @return receive speed in KB/s
	 */
	public float getRx_speed() {
		return rx_speed;
	}

	/**
	 * Sets the network receive speed in kilobytes per second.
	 *
	 * @param rx_speed receive speed in KB/s
	 */
	public void setRx_speed(float rx_speed) {
		this.rx_speed = rx_speed;
	}

	/**
	 * Returns the network transmit speed in kilobytes per second.
	 *
	 * @return transmit speed in KB/s
	 */
	public float getTx_speed() {
		return tx_speed;
	}

	/**
	 * Sets the network transmit speed in kilobytes per second.
	 *
	 * @param tx_speed transmit speed in KB/s
	 */
	public void setTx_speed(float tx_speed) {
		this.tx_speed = tx_speed;
	}
}
