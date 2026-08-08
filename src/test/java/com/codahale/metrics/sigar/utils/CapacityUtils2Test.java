package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

public class CapacityUtils2Test {

    @Test
    public void shouldReturnZeroForNullInput() {
        assertEquals(BigDecimal.ZERO, CapacityUtils2.getCapacity(null));
    }

    @Test
    public void shouldReturnZeroForEmptyInput() {
        assertEquals(BigDecimal.ZERO, CapacityUtils2.getCapacity(""));
        assertEquals(BigDecimal.ZERO, CapacityUtils2.getCapacity("   "));
    }

    @Test
    public void shouldReturnZeroForUnparseableInput() {
        assertEquals(BigDecimal.ZERO, CapacityUtils2.getCapacity("abc"));
    }

    @Test
    public void shouldParseKilobytes() {
        BigDecimal result = CapacityUtils2.getCapacity("1KB");
        assertTrue(result.longValue() > 0);
    }

    @Test
    public void shouldParseMegabytes() {
        BigDecimal result = CapacityUtils2.getCapacity("1MB");
        assertTrue(result.longValue() > 0);
    }

    @Test
    public void shouldParseGigabytes() {
        BigDecimal result = CapacityUtils2.getCapacity("1GB");
        assertTrue(result.longValue() > 0);
    }

    @Test
    public void shouldParseTerabytes() {
        BigDecimal result = CapacityUtils2.getCapacity("1TB");
        assertTrue(result.longValue() > 0);
    }

    @Test
    public void shouldParseDecimalValues() {
        BigDecimal result = CapacityUtils2.getCapacity("1.5MB");
        assertTrue(result.longValue() > 0);
    }

    @Test
    public void shouldGetLongCapacityForKb() {
        long result = CapacityUtils2.getLongCapacity("1KB");
        assertTrue(result > 0);
    }

    @Test
    public void shouldGetLongCapacityForNull() {
        assertEquals(0L, CapacityUtils2.getLongCapacity(null));
    }

    @Test
    public void shouldGetLongCapacityForEmpty() {
        assertEquals(0L, CapacityUtils2.getLongCapacity(""));
    }

    @Test
    public void shouldGetLongCapacityForUnparseable() {
        assertEquals(0L, CapacityUtils2.getLongCapacity("xyz"));
    }

    @Test
    public void shouldGetFloatCapacityForMb() {
        float result = CapacityUtils2.getFloatCapacity("1MB");
        assertTrue(result > 0);
    }

    @Test
    public void shouldGetFloatCapacityForNull() {
        assertEquals(0f, CapacityUtils2.getFloatCapacity(null), 0.001f);
    }

    @Test
    public void shouldGetFloatCapacityForEmpty() {
        assertEquals(0f, CapacityUtils2.getFloatCapacity(""), 0.001f);
    }

    @Test
    public void shouldGetFloatCapacityForUnparseable() {
        assertEquals(-1f, CapacityUtils2.getFloatCapacity("xyz"), 0.001f);
    }

    @Test
    public void shouldConvertLongToUnit() {
        BigDecimal result = CapacityUtils2.getCapacity(1024L * 1024L, Unit.MB);
        assertNotNull(result);
    }

    @Test
    public void shouldConvertLongToKbUnit() {
        BigDecimal result = CapacityUtils2.getCapacity(2048L, Unit.KB);
        assertNotNull(result);
    }

    @Test
    public void shouldConvertLongToUnitWithScale() {
        BigDecimal result = CapacityUtils2.getCapacity(1024L * 1024L, Unit.MB, 2);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForNegativeScale() {
        CapacityUtils2.getCapacity(1024L, Unit.KB, -1);
    }

    @Test
    public void shouldGetCapacityString() {
        String result = CapacityUtils2.getCapacityString(1024L * 1024L, Unit.MB);
        assertNotNull(result);
        assertTrue(result.contains("MB"));
    }

    @Test
    public void shouldGetCapacityStringWithScale() {
        String result = CapacityUtils2.getCapacityString(1024L * 1024L, Unit.MB, 2);
        assertNotNull(result);
        assertTrue(result.contains("MB"));
    }

    @Test
    public void shouldDivideDoubles() {
        double result = CapacityUtils2.div(10.0, 3.0, 2);
        assertTrue(result >= 3.33 && result <= 3.34);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForNegativeScaleInDiv() {
        CapacityUtils2.div(10.0, 3.0, -1);
    }

    @Test
    public void shouldHandleDivByZero() {
        double result = CapacityUtils2.div(10.0, 0.0, 2);
        assertTrue(result > 0);
    }
}
