package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class JVMOSPropertyTest {

    @Test
    public void shouldHaveOsNameKey() {
        assertEquals("os.name", JVMOSProperty.OS_NAME.getKey());
    }

    @Test
    public void shouldHaveOsArchKey() {
        assertEquals("os.arch", JVMOSProperty.OS_ARCH.getKey());
    }

    @Test
    public void shouldHaveOsVersionKey() {
        assertEquals("os.version", JVMOSProperty.OS_VERSION.getKey());
    }

    @Test
    public void shouldHaveFileSeparatorKey() {
        assertEquals("file.separator", JVMOSProperty.FILE_SEPARATOR.getKey());
    }

    @Test
    public void shouldHavePathSeparatorKey() {
        assertEquals("path.separator", JVMOSProperty.PATH_SEPARATOR.getKey());
    }

    @Test
    public void shouldHaveLineSeparatorKey() {
        assertEquals("line.separator", JVMOSProperty.LINE_SEPARATOR.getKey());
    }

    @Test
    public void shouldHaveUserNameKey() {
        assertEquals("user.name", JVMOSProperty.USER_NAME.getKey());
    }

    @Test
    public void shouldHaveUserHomeKey() {
        assertEquals("user.home", JVMOSProperty.USER_HOME.getKey());
    }

    @Test
    public void shouldHaveUserDirKey() {
        assertEquals("user.dir", JVMOSProperty.USER_DIR.getKey());
    }

    @Test
    public void shouldHaveNineValues() {
        assertEquals(9, JVMOSProperty.values().length);
    }
}
