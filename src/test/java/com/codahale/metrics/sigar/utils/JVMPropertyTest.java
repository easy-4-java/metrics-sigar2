package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class JVMPropertyTest {

    @Test
    public void shouldHaveJavaHomeKey() {
        assertEquals("java.home", JVMProperty.JAVA_HOME.getKey());
    }

    @Test
    public void shouldHaveJavaVersionKey() {
        assertEquals("java.version", JVMProperty.JAVA_VERSION.getKey());
    }

    @Test
    public void shouldHaveJavaVendorKey() {
        assertEquals("java.vendor", JVMProperty.JAVA_VENDOR.getKey());
    }

    @Test
    public void shouldHaveVmSpecificationVersionKey() {
        assertEquals("java.vm.specification.version", JVMProperty.JAVA_VM_SPECIFICATION_VERSION.getKey());
    }

    @Test
    public void shouldHaveVmSpecificationVendorKey() {
        assertEquals("java.vm.specification.vendor", JVMProperty.JAVA_VM_SPECIFICATION_VENDOR.getKey());
    }

    @Test
    public void shouldHaveVmSpecificationNameKey() {
        assertEquals("java.vm.specification.name", JVMProperty.JAVA_VM_SPECIFICATION_NAME.getKey());
    }

    @Test
    public void shouldHaveVmPidKey() {
        assertEquals("java.vm.pid", JVMProperty.JAVA_VM_PID.getKey());
    }

    @Test
    public void shouldHaveVmNameKey() {
        assertEquals("java.vm.name", JVMProperty.JAVA_VM_NAME.getKey());
    }

    @Test
    public void shouldHaveVmVendorKey() {
        assertEquals("java.vm.vendor", JVMProperty.JAVA_VM_VENDOR.getKey());
    }

    @Test
    public void shouldHaveVmVersionKey() {
        assertEquals("java.vm.version", JVMProperty.JAVA_VM_VERSION.getKey());
    }

    @Test
    public void shouldHaveClassVersionKey() {
        assertEquals("java.class.version", JVMProperty.JAVA_CLASS_VERSION.getKey());
    }

    @Test
    public void shouldHaveClassPathKey() {
        assertEquals("java.class.path", JVMProperty.JAVA_CLASS_PATH.getKey());
    }

    @Test
    public void shouldHaveLibraryPathKey() {
        assertEquals("java.library.path", JVMProperty.JAVA_LIBRARY_PATH.getKey());
    }

    @Test
    public void shouldHaveTmpdirKey() {
        assertEquals("java.io.tmpdir", JVMProperty.JAVA_IO_TMPDIR.getKey());
    }

    @Test
    public void shouldHaveExtDirsKey() {
        assertEquals("java.ext.dirs", JVMProperty.JAVA_EXT_DIRS.getKey());
    }

    @Test
    public void shouldHaveBootClassPathKey() {
        assertEquals("java.boot.class.path", JVMProperty.JAVA_BOOT_CLASS_PATH.getKey());
    }

    @Test
    public void shouldHaveManagementSpecVersionKey() {
        assertEquals("java.management.specification.version", JVMProperty.JAVA_MANAGEMENT_SPECIFICATION_VERSION.getKey());
    }

    @Test
    public void shouldHaveSpecificationNameKey() {
        assertEquals("java.specification.name", JVMProperty.JAVA_SPECIFICATION_NAME.getKey());
    }

    @Test
    public void shouldHaveSpecificationVersionKey() {
        assertEquals("java.specification.version", JVMProperty.JAVA_SPECIFICATION_VERSION.getKey());
    }

    @Test
    public void shouldHaveSpecificationVenderKey() {
        assertEquals("java.specification.vender", JVMProperty.JAVA_SPECIFICATION_VENDER.getKey());
    }

    @Test
    public void shouldHaveAllValues() {
        assertTrue(JVMProperty.values().length >= 20);
    }
}
