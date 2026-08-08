package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

public class MemoryInfoTest {

    @Test
    public void shouldConstructWithAllFields() {
        Map<String, Long> usage = new HashMap<String, Long>();
        usage.put("init", 1024L);
        usage.put("used", 512L);
        usage.put("committed", 768L);
        usage.put("max", 2048L);

        MemoryInfo info = new MemoryInfo("jvm.memory", "HeapMemoryUsage", usage, Unit.KB);

        assertEquals("jvm.memory", info.getPrefix());
        assertEquals("HeapMemoryUsage", info.getType());
        assertEquals(Unit.KB, info.getUnit());
        assertEquals(usage, info.getUsage());
    }

    @Test
    public void shouldConvertToMap() {
        Map<String, Long> usage = new HashMap<String, Long>();
        usage.put("init", 1024L);
        usage.put("used", 512L);
        usage.put("committed", 768L);
        usage.put("max", 2048L);

        MemoryInfo info = new MemoryInfo("jvm.memory", "HeapMemoryUsage", usage, Unit.KB);

        Map<String, String> map = info.toMap();
        assertNotNull(map);
        assertEquals(4, map.size());
        assertTrue(map.containsKey("jvm.memory.HeapMemoryUsage.init"));
        assertTrue(map.containsKey("jvm.memory.HeapMemoryUsage.used"));
        assertTrue(map.containsKey("jvm.memory.HeapMemoryUsage.committed"));
        assertTrue(map.containsKey("jvm.memory.HeapMemoryUsage.max"));
    }

    @Test
    public void shouldFormatToString() {
        Map<String, Long> usage = new HashMap<String, Long>();
        usage.put("init", 1024L);
        usage.put("used", 512L);
        usage.put("committed", 768L);
        usage.put("max", 2048L);

        MemoryInfo info = new MemoryInfo("jvm.memory", "HeapMemoryUsage", usage, Unit.KB);

        String str = info.toString();
        assertNotNull(str);
        assertTrue(str.contains("init = 1024"));
        assertTrue(str.contains("used = 512"));
        assertTrue(str.contains("committed = 768"));
        assertTrue(str.contains("max = 2048"));
    }

    @Test
    public void shouldReturnCorrectPrefix() {
        MemoryInfo info = new MemoryInfo("test.prefix", "Type", new HashMap<String, Long>(), Unit.NONE);
        assertEquals("test.prefix", info.getPrefix());
    }

    @Test
    public void shouldReturnCorrectType() {
        MemoryInfo info = new MemoryInfo("prefix", "MyType", new HashMap<String, Long>(), Unit.NONE);
        assertEquals("MyType", info.getType());
    }

    @Test
    public void shouldReturnCorrectUnit() {
        MemoryInfo info = new MemoryInfo("prefix", "Type", new HashMap<String, Long>(), Unit.GB);
        assertEquals(Unit.GB, info.getUnit());
    }

    @Test
    public void shouldHandleEmptyUsageMap() {
        MemoryInfo info = new MemoryInfo("prefix", "Type", new HashMap<String, Long>(), Unit.KB);
        Map<String, String> map = info.toMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }
}
