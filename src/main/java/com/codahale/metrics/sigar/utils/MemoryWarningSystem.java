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

import javax.management.*;
import java.lang.management.*;
import java.util.*;

/**
 * Memory warning system that notifies registered
 * {@link Listener listeners} when the JVM's tenured generation heap
 * exceeds a configurable usage threshold.
 *
 * <p>Only one threshold can be active at a time (as imposed by the
 * JMX memory pool API), so only one {@code MemoryWarningSystem}
 * instance should be created per JVM.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see Listener
 */
public class MemoryWarningSystem {

	private final Collection<Listener> listeners = new ArrayList<Listener>();

	/**
	 * Callback interface for memory usage threshold violations.
	 */
	public interface Listener {
		/**
		 * Called when memory usage exceeds the configured threshold.
		 *
		 * @param usedMemory the currently used memory in bytes
		 * @param maxMemory  the maximum available memory in bytes
		 */
		public void memoryUsageLow(long usedMemory, long maxMemory);
	}

	/**
	 * Constructs a new {@code MemoryWarningSystem} and registers a
	 * JMX notification listener on the memory MXBean.
	 */
	public MemoryWarningSystem() {
		MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
		NotificationEmitter emitter = (NotificationEmitter) mbean;
		emitter.addNotificationListener(new NotificationListener() {
			public void handleNotification(Notification n, Object hb) {
				if (n.getType().equals(MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED)) {
					long maxMemory = tenuredGenPool.getUsage().getMax();
					long usedMemory = tenuredGenPool.getUsage().getUsed();
					for (Listener listener : listeners) {
						listener.memoryUsageLow(usedMemory, maxMemory);
					}
				}
			}
		}, null, null);
	}

	/**
	 * Registers a listener to be notified when memory usage is low.
	 *
	 * @param listener the listener to add; must not be {@code null}
	 * @return {@code true} if the listener was added
	 */
	public boolean addListener(Listener listener) {
		return listeners.add(listener);
	}

	/**
	 * Removes a previously registered listener.
	 *
	 * @param listener the listener to remove
	 * @return {@code true} if the listener was removed
	 */
	public boolean removeListener(Listener listener) {
		return listeners.remove(listener);
	}

	/** The tenured generation memory pool bean. */
	private static final MemoryPoolMXBean tenuredGenPool = findTenuredGenPool();

	/**
	 * Sets the usage threshold as a percentage of maximum memory.
	 *
	 * @param percentage the threshold in the range (0.0, 1.0]
	 * @throws IllegalArgumentException if the percentage is out of range
	 */
	public static void setPercentageUsageThreshold(double percentage) {
		if (percentage <= 0.0 || percentage > 1.0) {
			throw new IllegalArgumentException("Percentage not in range");
		}
		long maxMemory = tenuredGenPool.getUsage().getMax();
		long warningThreshold = (long) (maxMemory * percentage);
		tenuredGenPool.setUsageThreshold(warningThreshold);
	}

	/**
	 * Finds the tenured generation memory pool -- the first HEAP pool
	 * that supports usage thresholds.
	 *
	 * @return the tenured generation pool bean
	 * @throws AssertionError if no suitable pool is found
	 */
	private static MemoryPoolMXBean findTenuredGenPool() {
		for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
			if (pool.getType() == MemoryType.HEAP && pool.isUsageThresholdSupported()) {
				return pool;
			}
		}
		throw new AssertionError("Could not find tenured space");
	}

}
