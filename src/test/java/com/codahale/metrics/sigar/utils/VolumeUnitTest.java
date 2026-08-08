package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class VolumeUnitTest {

    @Test
    public void shouldInstantiateVolumeUnit() {
        VolumeUnit unit = new VolumeUnit();
        assertNotNull(unit);
    }
}
