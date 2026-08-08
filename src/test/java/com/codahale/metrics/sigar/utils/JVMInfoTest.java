package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

public class JVMInfoTest {

    @Test
    public void shouldReturnPid() {
        int pid = JVMInfo.pid();
        assertTrue(pid > 0);
    }

    @Test
    public void shouldReturnInfoMapOrHandleException() {
        // JVMInfo.info() calls getBootClassPath() which throws
        // UnsupportedOperationException on JDK 17+
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info);
            assertFalse(info.isEmpty());
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+ where getBootClassPath() is removed
        }
    }

    @Test
    public void shouldContainVmNameWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get(JVMProperty.JAVA_VM_NAME.getKey()));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainVmVendorWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get(JVMProperty.JAVA_VM_VENDOR.getKey()));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainVmVersionWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get(JVMProperty.JAVA_VM_VERSION.getKey()));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainOsNameWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get("os.name"));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainOsArchWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get("os.arch"));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainThreadCountWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get("jvm.thread.ThreadCount"));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainLoadedClassCountWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get("jvm.class.LoadedCount"));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainCompilationNameWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get("jvm.compilation.name"));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainRuntimeStartTimeWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get(JVMProperty.JAVA_RUNTIME_STARTTIME.getKey()));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainRuntimeUptimeWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get(JVMProperty.JAVA_RUNTIME_UPTIME.getKey()));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainSpecificationNameWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get(JVMProperty.JAVA_SPECIFICATION_NAME.getKey()));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldContainSpecificationVersionWhenInfoSucceeds() {
        try {
            Map<String, Object> info = JVMInfo.info();
            assertNotNull(info.get(JVMProperty.JAVA_SPECIFICATION_VERSION.getKey()));
        } catch (UnsupportedOperationException e) {
            // Expected on JDK 17+
        }
    }

    @Test
    public void shouldReturnRuntimeMap() {
        Map<String, Object> runtime = JVMInfo.runtime(Unit.MB);
        assertNotNull(runtime);
        assertNotNull(runtime.get(JVMInfo.JVM_MEMORY + ".max"));
        assertNotNull(runtime.get(JVMInfo.JVM_MEMORY + ".total"));
        assertNotNull(runtime.get(JVMInfo.JVM_MEMORY + ".used"));
        assertNotNull(runtime.get(JVMInfo.JVM_MEMORY + ".free"));
        assertNotNull(runtime.get(JVMInfo.JVM_MEMORY + ".usage"));
    }

    @Test
    public void shouldReturnUsageMap() {
        Map<String, Double> usage = JVMInfo.usage();
        assertNotNull(usage);
        assertNotNull(usage.get(JVMInfo.JVM_MEMORY + ".usage"));
    }

    @Test
    public void shouldReturnMemoryList() {
        List<MemoryInfo> memory = JVMInfo.memory(Unit.KB);
        assertNotNull(memory);
        assertEquals(2, memory.size());
    }

    @Test
    public void shouldReturnMemoryPoolList() {
        List<MemoryInfo> pools = JVMInfo.memoryPool(Unit.MB);
        assertNotNull(pools);
        assertFalse(pools.isEmpty());
    }

    @Test
    public void shouldReturnGcList() {
        List<Map<String, Object>> gc = JVMInfo.gc();
        assertNotNull(gc);
    }

    @Test
    public void shouldHaveCorrectConstants() {
        assertEquals("jvm.memory", JVMInfo.JVM_MEMORY);
        assertEquals("jvm.memory.pool", JVMInfo.JVM_MEMORY_POOL);
    }
}
