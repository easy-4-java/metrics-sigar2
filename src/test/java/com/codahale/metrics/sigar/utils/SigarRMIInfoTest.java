package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.codahale.metrics.sigar.rmi.SigarRMIInfo;

public class SigarRMIInfoTest {

    @Test
    public void shouldSetAndGetMemoryUsed() {
        SigarRMIInfo info = new SigarRMIInfo();
        info.setMemory_uesd(512L);
        assertEquals(512L, info.getMemory_uesd());
    }

    @Test
    public void shouldSetAndGetMemoryTotal() {
        SigarRMIInfo info = new SigarRMIInfo();
        info.setMemory_total(1024L);
        assertEquals(1024L, info.getMemory_total());
    }

    @Test
    public void shouldSetAndGetCpuCombined() {
        SigarRMIInfo info = new SigarRMIInfo();
        info.setCpu_combined(0.75);
        assertEquals(0.75, info.getCpu_combined(), 0.001);
    }

    @Test
    public void shouldSetAndGetRxSpeed() {
        SigarRMIInfo info = new SigarRMIInfo();
        info.setRx_speed(1.5f);
        assertEquals(1.5f, info.getRx_speed(), 0.001f);
    }

    @Test
    public void shouldSetAndGetTxSpeed() {
        SigarRMIInfo info = new SigarRMIInfo();
        info.setTx_speed(2.5f);
        assertEquals(2.5f, info.getTx_speed(), 0.001f);
    }

    @Test
    public void shouldDefaultToZeroValues() {
        SigarRMIInfo info = new SigarRMIInfo();
        assertEquals(0L, info.getMemory_uesd());
        assertEquals(0L, info.getMemory_total());
        assertEquals(0.0, info.getCpu_combined(), 0.001);
        assertEquals(0f, info.getRx_speed(), 0.001f);
        assertEquals(0f, info.getTx_speed(), 0.001f);
    }
}
