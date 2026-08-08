package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class OSPropertyTest {

    @Test
    public void shouldHaveOsNameKey() {
        assertEquals("os.name", OSProperty.OS_NAME.getKey());
    }

    @Test
    public void shouldHaveOsArchKey() {
        assertEquals("os.arch", OSProperty.OS_ARCH.getKey());
    }

    @Test
    public void shouldHaveOsVersionKey() {
        assertEquals("os.version", OSProperty.OS_VERSION.getKey());
    }

    @Test
    public void shouldHaveFileSeparatorKey() {
        assertEquals("file.separator", OSProperty.FILE_SEPARATOR.getKey());
    }

    @Test
    public void shouldHavePathSeparatorKey() {
        assertEquals("path.separator", OSProperty.PATH_SEPARATOR.getKey());
    }

    @Test
    public void shouldHaveLineSeparatorKey() {
        assertEquals("line.separator", OSProperty.LINE_SEPARATOR.getKey());
    }

    @Test
    public void shouldHaveUserNameKey() {
        assertEquals("user.name", OSProperty.USER_NAME.getKey());
    }

    @Test
    public void shouldHaveUserHomeKey() {
        assertEquals("user.home", OSProperty.USER_HOME.getKey());
    }

    @Test
    public void shouldHaveUserDirKey() {
        assertEquals("user.dir", OSProperty.USER_DIR.getKey());
    }

    @Test
    public void shouldHaveNineValues() {
        assertEquals(9, OSProperty.values().length);
    }
}
