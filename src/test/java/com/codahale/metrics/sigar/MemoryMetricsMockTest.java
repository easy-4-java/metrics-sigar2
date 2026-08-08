package com.codahale.metrics.sigar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.sigar.MemoryMetrics.MainMemory;
import com.codahale.metrics.sigar.MemoryMetrics.SwapSpace;

public class MemoryMetricsMockTest {

    @Test
    public void shouldReturnUndefMemWhenSigarThrows() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getMem()).thenThrow(new SigarException("test"));

        MemoryMetrics mm = new MemoryMetrics(sigar);
        MainMemory mem = mm.mem();
        assertEquals(-1L, mem.total());
        assertEquals(-1L, mem.used());
        assertEquals(-1L, mem.free());
        assertEquals(-1L, mem.actualUsed());
        assertEquals(-1L, mem.actualFree());
        assertEquals(-1.0, mem.usedPercent(), 0.001);
        assertEquals(-1.0, mem.freePercent(), 0.001);
    }

    @Test
    public void shouldReturnUndefSwapWhenSigarThrows() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getSwap()).thenThrow(new SigarException("test"));

        MemoryMetrics mm = new MemoryMetrics(sigar);
        SwapSpace swap = mm.swap();
        assertEquals(-1L, swap.total());
        assertEquals(-1L, swap.used());
        assertEquals(-1L, swap.free());
        assertEquals(-1L, swap.pagesIn());
        assertEquals(-1L, swap.pagesOut());
    }

    @Test
    public void shouldReturnNegativeOneRamWhenSigarThrows() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getMem()).thenThrow(new SigarException("test"));

        MemoryMetrics mm = new MemoryMetrics(sigar);
        assertEquals(-1L, mm.ramInMB());
    }

    @Test
    public void shouldReturnRealMemValues() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        Mem mem = mock(Mem.class);
        when(mem.getTotal()).thenReturn(8L * 1024 * 1024 * 1024);
        when(mem.getUsed()).thenReturn(4L * 1024 * 1024 * 1024);
        when(mem.getFree()).thenReturn(4L * 1024 * 1024 * 1024);
        when(mem.getActualUsed()).thenReturn(3L * 1024 * 1024 * 1024);
        when(mem.getActualFree()).thenReturn(5L * 1024 * 1024 * 1024);
        when(mem.getUsedPercent()).thenReturn(50.0);
        when(mem.getFreePercent()).thenReturn(50.0);
        when(sigar.getMem()).thenReturn(mem);

        MemoryMetrics mm = new MemoryMetrics(sigar);
        MainMemory mainMem = mm.mem();
        assertEquals(8L * 1024 * 1024 * 1024, mainMem.total());
        assertEquals(4L * 1024 * 1024 * 1024, mainMem.used());
        assertEquals(4L * 1024 * 1024 * 1024, mainMem.free());
        assertEquals(3L * 1024 * 1024 * 1024, mainMem.actualUsed());
        assertEquals(5L * 1024 * 1024 * 1024, mainMem.actualFree());
        assertEquals(50.0, mainMem.usedPercent(), 0.001);
        assertEquals(50.0, mainMem.freePercent(), 0.001);
    }

    @Test
    public void shouldReturnRealSwapValues() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        Swap swap = mock(Swap.class);
        when(swap.getTotal()).thenReturn(4L * 1024 * 1024 * 1024);
        when(swap.getUsed()).thenReturn(1L * 1024 * 1024 * 1024);
        when(swap.getFree()).thenReturn(3L * 1024 * 1024 * 1024);
        when(swap.getPageIn()).thenReturn(1000L);
        when(swap.getPageOut()).thenReturn(500L);
        when(sigar.getSwap()).thenReturn(swap);

        MemoryMetrics mm = new MemoryMetrics(sigar);
        SwapSpace swapSpace = mm.swap();
        assertEquals(4L * 1024 * 1024 * 1024, swapSpace.total());
        assertEquals(1L * 1024 * 1024 * 1024, swapSpace.used());
        assertEquals(3L * 1024 * 1024 * 1024, swapSpace.free());
        assertEquals(1000L, swapSpace.pagesIn());
        assertEquals(500L, swapSpace.pagesOut());
    }

    @Test
    public void shouldReturnRamInMB() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        Mem mem = mock(Mem.class);
        when(mem.getRam()).thenReturn(8192L);
        when(sigar.getMem()).thenReturn(mem);

        MemoryMetrics mm = new MemoryMetrics(sigar);
        assertEquals(8192L, mm.ramInMB());
    }

    @Test
    public void shouldRegisterAllGauges() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        Mem mem = mock(Mem.class);
        when(mem.getTotal()).thenReturn(8L * 1024 * 1024 * 1024);
        when(mem.getUsed()).thenReturn(4L * 1024 * 1024 * 1024);
        when(mem.getFree()).thenReturn(4L * 1024 * 1024 * 1024);
        when(mem.getActualUsed()).thenReturn(3L * 1024 * 1024 * 1024);
        when(mem.getActualFree()).thenReturn(5L * 1024 * 1024 * 1024);
        when(mem.getUsedPercent()).thenReturn(50.0);
        when(mem.getFreePercent()).thenReturn(50.0);
        when(mem.getRam()).thenReturn(8192L);
        when(sigar.getMem()).thenReturn(mem);

        Swap swap = mock(Swap.class);
        when(swap.getTotal()).thenReturn(4L * 1024 * 1024 * 1024);
        when(swap.getUsed()).thenReturn(1L * 1024 * 1024 * 1024);
        when(swap.getFree()).thenReturn(3L * 1024 * 1024 * 1024);
        when(swap.getPageIn()).thenReturn(1000L);
        when(swap.getPageOut()).thenReturn(500L);
        when(sigar.getSwap()).thenReturn(swap);

        MemoryMetrics mm = new MemoryMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        mm.registerGauges(registry);
        assertTrue(registry.getNames().size() >= 10);
    }

    @Test
    public void shouldRegisterIndividualGauges() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        Mem mem = mock(Mem.class);
        when(mem.getTotal()).thenReturn(8L * 1024 * 1024 * 1024);
        when(mem.getUsed()).thenReturn(4L * 1024 * 1024 * 1024);
        when(mem.getFree()).thenReturn(4L * 1024 * 1024 * 1024);
        when(mem.getActualUsed()).thenReturn(3L * 1024 * 1024 * 1024);
        when(mem.getActualFree()).thenReturn(5L * 1024 * 1024 * 1024);
        when(mem.getUsedPercent()).thenReturn(50.0);
        when(mem.getFreePercent()).thenReturn(50.0);
        when(sigar.getMem()).thenReturn(mem);

        Swap swap = mock(Swap.class);
        when(swap.getTotal()).thenReturn(4L * 1024 * 1024 * 1024);
        when(swap.getUsed()).thenReturn(1L * 1024 * 1024 * 1024);
        when(swap.getFree()).thenReturn(3L * 1024 * 1024 * 1024);
        when(swap.getPageIn()).thenReturn(1000L);
        when(swap.getPageOut()).thenReturn(500L);
        when(sigar.getSwap()).thenReturn(swap);

        MemoryMetrics mm = new MemoryMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();

        mm.registerMemoryFree(registry, "mem.free");
        mm.registerMemoryActualFree(registry, "mem.actual-free");
        mm.registerMemoryUsed(registry, "mem.used");
        mm.registerMemoryActualUsed(registry, "mem.actual-used");
        mm.registerMemoryTotal(registry, "mem.total");
        mm.registerMemoryUsedPercent(registry, "mem.used-pct");
        mm.registerMemoryFreePercent(registry, "mem.free-pct");
        mm.registerSwapFree(registry, "swap.free");
        mm.registerSwapPagesIn(registry, "swap.pages-in");
        mm.registerSwapPagesOut(registry, "swap.pages-out");

        assertEquals(10, registry.getNames().size());
    }

    @Test
    public void shouldCreateMainMemoryFromSigarBean() {
        Mem mem = mock(Mem.class);
        when(mem.getTotal()).thenReturn(1024L);
        when(mem.getUsed()).thenReturn(512L);
        when(mem.getFree()).thenReturn(512L);
        when(mem.getActualUsed()).thenReturn(400L);
        when(mem.getActualFree()).thenReturn(624L);
        when(mem.getUsedPercent()).thenReturn(50.0);
        when(mem.getFreePercent()).thenReturn(50.0);

        MainMemory mm = MainMemory.fromSigarBean(mem);
        assertEquals(1024L, mm.total());
        assertEquals(512L, mm.used());
        assertEquals(512L, mm.free());
        assertEquals(400L, mm.actualUsed());
        assertEquals(624L, mm.actualFree());
        assertEquals(50.0, mm.usedPercent(), 0.001);
        assertEquals(50.0, mm.freePercent(), 0.001);
    }

    @Test
    public void shouldCreateSwapSpaceFromSigarBean() {
        Swap swap = mock(Swap.class);
        when(swap.getTotal()).thenReturn(2048L);
        when(swap.getUsed()).thenReturn(1024L);
        when(swap.getFree()).thenReturn(1024L);
        when(swap.getPageIn()).thenReturn(100L);
        when(swap.getPageOut()).thenReturn(50L);

        SwapSpace ss = SwapSpace.fromSigarBean(swap);
        assertEquals(2048L, ss.total());
        assertEquals(1024L, ss.used());
        assertEquals(1024L, ss.free());
        assertEquals(100L, ss.pagesIn());
        assertEquals(50L, ss.pagesOut());
    }
}
