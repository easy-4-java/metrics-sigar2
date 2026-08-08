package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class AcreageUnitTest {

    @Test
    public void shouldHaveNoValues() {
        assertEquals(0, AcreageUnit.values().length);
    }
}
