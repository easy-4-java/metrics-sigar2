package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemPropertyTest {

    @Test
    public void shouldHaveInitKey() {
        assertEquals("init", MemProperty.MEM_INIT.getKey());
    }

    @Test
    public void shouldHaveUsedKey() {
        assertEquals("used", MemProperty.MEM_USED.getKey());
    }

    @Test
    public void shouldHaveCommittedKey() {
        assertEquals("committed", MemProperty.MEM_COMMITTED.getKey());
    }

    @Test
    public void shouldHaveMaxKey() {
        assertEquals("max", MemProperty.MEM_MAX.getKey());
    }

    @Test
    public void shouldHaveFourValues() {
        assertEquals(4, MemProperty.values().length);
    }
}
