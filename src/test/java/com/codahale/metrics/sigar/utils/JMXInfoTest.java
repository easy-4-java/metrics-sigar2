package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

public class JMXInfoTest {

    @Test
    public void shouldReturnRuntimeMap() {
        Map<String, Object> runtime = JMXInfo.runtime();
        assertNotNull(runtime);
        assertFalse(runtime.isEmpty());
    }

    @Test
    public void shouldContainVmNameInRuntime() {
        Map<String, Object> runtime = JMXInfo.runtime();
        assertNotNull(runtime.get(JVMProperty.JAVA_VM_NAME.getKey()));
    }

    @Test
    public void shouldContainVmVersionInRuntime() {
        Map<String, Object> runtime = JMXInfo.runtime();
        assertNotNull(runtime.get(JVMProperty.JAVA_VM_VERSION.getKey()));
    }

    @Test
    public void shouldContainClassPathInRuntime() {
        Map<String, Object> runtime = JMXInfo.runtime();
        assertNotNull(runtime.get(JVMProperty.JAVA_CLASS_PATH.getKey()));
    }

    @Test
    public void shouldContainStartTimeInRuntime() {
        Map<String, Object> runtime = JMXInfo.runtime();
        assertNotNull(runtime.get(JVMProperty.JAVA_RUNTIME_STARTTIME.getKey()));
    }

    @Test
    public void shouldContainUptimeInRuntime() {
        Map<String, Object> runtime = JMXInfo.runtime();
        assertNotNull(runtime.get(JVMProperty.JAVA_RUNTIME_UPTIME.getKey()));
    }

    @Test
    public void shouldReturnMemoryInfoList() {
        List<MemoryInfo> memory = JMXInfo.memory(Unit.KB);
        assertNotNull(memory);
        assertEquals(2, memory.size());
    }

    @Test
    public void shouldContainHeapMemoryInfo() {
        List<MemoryInfo> memory = JMXInfo.memory(Unit.KB);
        assertEquals("HeapMemoryUsage", memory.get(0).getType());
    }

    @Test
    public void shouldContainNonHeapMemoryInfo() {
        List<MemoryInfo> memory = JMXInfo.memory(Unit.KB);
        assertEquals("NonHeapMemoryUsage", memory.get(1).getType());
    }

    @Test
    public void shouldReturnMemoryPoolList() {
        List<MemoryInfo> pools = JMXInfo.memoryPool(Unit.MB);
        assertNotNull(pools);
        assertFalse(pools.isEmpty());
    }

    @Test
    public void shouldReturnOsMap() {
        Map<String, Object> os = JMXInfo.os();
        assertNotNull(os);
        assertNotNull(os.get("os.name"));
        assertNotNull(os.get("os.arch"));
        assertNotNull(os.get("os.version"));
        assertNotNull(os.get("os.cores"));
    }

    @Test
    public void shouldReturnThreadMap() {
        Map<String, Object> thread = JMXInfo.thread();
        assertNotNull(thread);
        assertTrue(thread.size() > 0);
        assertNotNull(thread.get("jvm.thread.ThreadCount"));
    }

    @Test
    public void shouldReturnCompilationMap() {
        Map<String, Object> compilation = JMXInfo.compilation();
        assertNotNull(compilation);
        assertNotNull(compilation.get("jvm.compilation.name"));
    }

    @Test
    public void shouldReturnGcMap() {
        Map<String, Object> gc = JMXInfo.gc();
        assertNotNull(gc);
    }

    @Test
    public void shouldHaveCorrectMemoryConstants() {
        assertEquals("jvm.memory", JMXInfo.JVM_MEMORY);
        assertEquals("jvm.memory.pool", JMXInfo.JVM_MEMORY_POOL);
    }
}
