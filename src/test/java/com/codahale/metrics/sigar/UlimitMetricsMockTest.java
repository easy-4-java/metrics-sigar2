package com.codahale.metrics.sigar;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.mockito.Mockito.*;

import org.hyperic.sigar.ResourceLimit;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.sigar.UlimitMetrics.Ulimit;

public class UlimitMetricsMockTest {

    @BeforeClass
    public static void checkSigarAvailable() {
        try {
            // ResourceLimit.INFINITY() is a native method - skip if Sigar native is not loaded
            ResourceLimit.INFINITY();
        } catch (UnsatisfiedLinkError e) {
            assumeNoException("Sigar native library not available", e);
        }
    }

    @Test
    public void shouldReturnUndefWhenSigarThrows() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getResourceLimit()).thenThrow(new SigarException("test"));

        UlimitMetrics um = new UlimitMetrics(sigar);
        Ulimit ulimit = um.ulimit();
        assertEquals(-1L, ulimit.coreFileSize());
        assertEquals(-1L, ulimit.dataSegSize());
        assertEquals(-1L, ulimit.fileSize());
        assertEquals(-1L, ulimit.pipeSize());
        assertEquals(-1L, ulimit.memSize());
        assertEquals(-1L, ulimit.openFiles());
        assertEquals(-1L, ulimit.stackSize());
        assertEquals(-1L, ulimit.cpuTime());
        assertEquals(-1L, ulimit.processes());
        assertEquals(-1L, ulimit.virtMemSize());
    }

    @Test
    public void shouldReturnRealUlimitValues() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        ResourceLimit lim = mock(ResourceLimit.class);
        when(lim.getCoreCur()).thenReturn(1024L);
        when(lim.getDataCur()).thenReturn(2048L);
        when(lim.getFileSizeCur()).thenReturn(4096L);
        when(lim.getPipeSizeCur()).thenReturn(8192L);
        when(lim.getMemoryCur()).thenReturn(16384L);
        when(lim.getOpenFilesCur()).thenReturn(1024L);
        when(lim.getStackCur()).thenReturn(32768L);
        when(lim.getCpuCur()).thenReturn(65536L);
        when(lim.getProcessesCur()).thenReturn(512L);
        when(lim.getVirtualMemoryCur()).thenReturn(131072L);
        when(sigar.getResourceLimit()).thenReturn(lim);

        UlimitMetrics um = new UlimitMetrics(sigar);
        Ulimit ulimit = um.ulimit();
        assertEquals(1024L, ulimit.coreFileSize());
        assertEquals(2048L, ulimit.dataSegSize());
        assertEquals(4096L, ulimit.fileSize());
        assertEquals(8192L, ulimit.pipeSize());
        assertEquals(16384L, ulimit.memSize());
        assertEquals(1024L, ulimit.openFiles());
        assertEquals(32768L, ulimit.stackSize());
        assertEquals(65536L, ulimit.cpuTime());
        assertEquals(512L, ulimit.processes());
        assertEquals(131072L, ulimit.virtMemSize());
    }

    @Test
    public void shouldReplaceInfinityWithNegativeOne() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        ResourceLimit lim = mock(ResourceLimit.class);
        long inf = ResourceLimit.INFINITY();
        when(lim.getCoreCur()).thenReturn(inf);
        when(lim.getDataCur()).thenReturn(inf);
        when(lim.getFileSizeCur()).thenReturn(inf);
        when(lim.getPipeSizeCur()).thenReturn(inf);
        when(lim.getMemoryCur()).thenReturn(inf);
        when(lim.getOpenFilesCur()).thenReturn(inf);
        when(lim.getStackCur()).thenReturn(inf);
        when(lim.getCpuCur()).thenReturn(inf);
        when(lim.getProcessesCur()).thenReturn(inf);
        when(lim.getVirtualMemoryCur()).thenReturn(inf);
        when(sigar.getResourceLimit()).thenReturn(lim);

        UlimitMetrics um = new UlimitMetrics(sigar);
        Ulimit ulimit = um.ulimit();
        assertEquals(-1L, ulimit.coreFileSize());
        assertEquals(-1L, ulimit.dataSegSize());
        assertEquals(-1L, ulimit.fileSize());
        assertEquals(-1L, ulimit.pipeSize());
        assertEquals(-1L, ulimit.memSize());
        assertEquals(-1L, ulimit.openFiles());
        assertEquals(-1L, ulimit.stackSize());
        assertEquals(-1L, ulimit.cpuTime());
        assertEquals(-1L, ulimit.processes());
        assertEquals(-1L, ulimit.virtMemSize());
    }

    @Test
    public void shouldRegisterGauges() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        ResourceLimit lim = mock(ResourceLimit.class);
        when(lim.getOpenFilesCur()).thenReturn(1024L);
        when(lim.getStackCur()).thenReturn(8192L);
        when(sigar.getResourceLimit()).thenReturn(lim);

        UlimitMetrics um = new UlimitMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        um.registerGauges(registry);
        assertTrue(registry.getNames().size() >= 2);
    }

    @Test
    public void shouldRegisterOpenFilesGauge() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        ResourceLimit lim = mock(ResourceLimit.class);
        when(lim.getOpenFilesCur()).thenReturn(1024L);
        when(sigar.getResourceLimit()).thenReturn(lim);

        UlimitMetrics um = new UlimitMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        um.registerUlimitOpenFiles(registry, "test.open-files");
        assertTrue(registry.getNames().contains("test.open-files"));
    }

    @Test
    public void shouldRegisterStackSizeGauge() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        ResourceLimit lim = mock(ResourceLimit.class);
        when(lim.getStackCur()).thenReturn(8192L);
        when(sigar.getResourceLimit()).thenReturn(lim);

        UlimitMetrics um = new UlimitMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        um.registerUlimitStackSize(registry, "test.stack-size");
        assertTrue(registry.getNames().contains("test.stack-size"));
    }

    @Test
    public void shouldRegisterDefaultGauges() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        ResourceLimit lim = mock(ResourceLimit.class);
        when(lim.getOpenFilesCur()).thenReturn(1024L);
        when(lim.getStackCur()).thenReturn(8192L);
        when(sigar.getResourceLimit()).thenReturn(lim);

        UlimitMetrics um = new UlimitMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        um.registerUlimitOpenFiles(registry);
        um.registerUlimitStackSize(registry);
        assertTrue(registry.getNames().size() >= 2);
    }

    @Test
    public void shouldCreateUndefUlimit() {
        Ulimit ulimit = Ulimit.undef();
        assertEquals(-1L, ulimit.coreFileSize());
        assertEquals(-1L, ulimit.dataSegSize());
        assertEquals(-1L, ulimit.fileSize());
        assertEquals(-1L, ulimit.pipeSize());
        assertEquals(-1L, ulimit.memSize());
        assertEquals(-1L, ulimit.openFiles());
        assertEquals(-1L, ulimit.stackSize());
        assertEquals(-1L, ulimit.cpuTime());
        assertEquals(-1L, ulimit.processes());
        assertEquals(-1L, ulimit.virtMemSize());
    }
}
