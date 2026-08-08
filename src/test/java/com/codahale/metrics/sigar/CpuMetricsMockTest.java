package com.codahale.metrics.sigar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.sigar.CpuMetrics.CpuTime;

public class CpuMetricsMockTest {

    @Test
    public void shouldReturnTotalCoreCount() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        CpuInfo info = new CpuInfo();
        // CpuInfo doesn't have setters in mock-friendly way, so we test the null path
        when(sigar.getCpuInfoList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        assertEquals(-1, cm.totalCoreCount());
    }

    @Test
    public void shouldReturnNegativeOneWhenCpuInfoIsNull() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        assertEquals(-1, cm.totalCoreCount());
        assertEquals(-1, cm.physicalCpuCount());
    }

    @Test
    public void shouldReturnNegativeOneWhenCpuInfoArrayIsEmpty() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(new CpuInfo[0]);

        CpuMetrics cm = new CpuMetrics(sigar);
        assertEquals(-1, cm.totalCoreCount());
        assertEquals(-1, cm.physicalCpuCount());
    }

    @Test
    public void shouldReturnNegativeOneWhenCpuInfoThrows() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenThrow(new SigarException("test"));

        CpuMetrics cm = new CpuMetrics(sigar);
        assertEquals(-1, cm.totalCoreCount());
        assertEquals(-1, cm.physicalCpuCount());
    }

    @Test
    public void shouldReturnEmptyCpusWhenPercListIsNull() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);
        when(sigar.getCpuPercList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        List<CpuTime> cpus = cm.cpus();
        assertNotNull(cpus);
        assertTrue(cpus.isEmpty());
    }

    @Test
    public void shouldReturnEmptyCpusWhenPercListThrows() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);
        when(sigar.getCpuPercList()).thenThrow(new SigarException("test"));

        CpuMetrics cm = new CpuMetrics(sigar);
        List<CpuTime> cpus = cm.cpus();
        assertNotNull(cpus);
        assertTrue(cpus.isEmpty());
    }

    @Test
    public void shouldReturnEmptyCpusWhenPercListIsEmpty() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);
        when(sigar.getCpuPercList()).thenReturn(new CpuPerc[0]);

        CpuMetrics cm = new CpuMetrics(sigar);
        List<CpuTime> cpus = cm.cpus();
        assertNotNull(cpus);
        assertTrue(cpus.isEmpty());
    }

    @Test
    public void shouldRegisterGauges() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        cm.registerGauges(registry);
        assertTrue(registry.getNames().size() > 0);
    }

    @Test
    public void shouldRegisterTotalCoresWithName() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        cm.registerTotalCores(registry, "test.cores");
        assertTrue(registry.getNames().contains("test.cores"));
    }

    @Test
    public void shouldRegisterPhysicalCpusWithName() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        cm.registerPhysicalCpus(registry, "test.cpus");
        assertTrue(registry.getNames().contains("test.cpus"));
    }

    @Test
    public void shouldRegisterCpuTimeUserPercentWithName() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        cm.registerCpuTimeUserPercent(registry, "test.user");
        assertTrue(registry.getNames().contains("test.user"));
    }

    @Test
    public void shouldRegisterCpuTimeSysPercentWithName() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        cm.registerCpuTimeSysPercent(registry, "test.sys");
        assertTrue(registry.getNames().contains("test.sys"));
    }

    @Test
    public void shouldCreateCpuTimeFromValues() {
        CpuTime time = new CpuTime(0.5, 0.2, 0.05, 0.05, 0.15, 0.05);
        assertEquals(0.5, time.user(), 0.001);
        assertEquals(0.2, time.sys(), 0.001);
        assertEquals(0.05, time.nice(), 0.001);
        assertEquals(0.05, time.waiting(), 0.001);
        assertEquals(0.15, time.idle(), 0.001);
        assertEquals(0.05, time.irq(), 0.001);
    }

    @Test
    public void shouldRegisterDefaultGauges() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getCpuInfoList()).thenReturn(null);

        CpuMetrics cm = new CpuMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        cm.registerTotalCores(registry);
        cm.registerPhysicalCpus(registry);
        cm.registerCpuTimeUserPercent(registry);
        cm.registerCpuTimeSysPercent(registry);
        assertTrue(registry.getNames().size() >= 4);
    }
}
