package com.codahale.metrics.sigar.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI remote interface for retrieving a snapshot of system metrics
 * (memory in MB, CPU combined usage, and network speed in KB/s).
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see SigarRMIInfo
 * @see com.codahale.metrics.sigar.rmi.impl.DefaultSigarRemoteServiceImpl
 */
public interface SigarRemoteService extends Remote{

    /**
     * Retrieves a snapshot of the current system runtime metrics.
     *
     * @return a {@link SigarRMIInfo} containing memory, CPU, and network metrics
     * @throws RemoteException if a network or RMI communication error occurs
     */
    public SigarRMIInfo getRuntime() throws RemoteException;

}
