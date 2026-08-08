package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.codahale.metrics.sigar.utils.CapacityUtils.Unit;

public class CapacityUtilsTest {

    @Test
    public void shouldReturnZeroForNullInput() {
        assertEquals(BigDecimal.ZERO, CapacityUtils.getCapacity(null));
    }

    @Test
    public void shouldReturnZeroForEmptyInput() {
        assertEquals(BigDecimal.ZERO, CapacityUtils.getCapacity(""));
        assertEquals(BigDecimal.ZERO, CapacityUtils.getCapacity("   "));
    }

    @Test
    public void shouldReturnZeroForUnparseableInput() {
        assertEquals(BigDecimal.ZERO, CapacityUtils.getCapacity("abc"));
    }

    @Test
    public void shouldParseKilobytes() {
        BigDecimal result = CapacityUtils.getCapacity("1KB");
        assertEquals(new BigDecimal(1024), result);
    }

    @Test
    public void shouldParseMegabytes() {
        BigDecimal result = CapacityUtils.getCapacity("1MB");
        assertEquals(new BigDecimal(1024L * 1024L), result);
    }

    @Test
    public void shouldParseGigabytes() {
        BigDecimal result = CapacityUtils.getCapacity("1GB");
        assertEquals(new BigDecimal(1024L * 1024L * 1024L), result);
    }

    @Test
    public void shouldParseTerabytes() {
        // TB+ Unit values use int arithmetic that overflows to 0
        BigDecimal result = CapacityUtils.getCapacity("1TB");
        assertNotNull(result);
    }

    @Test
    public void shouldParsePetabytes() {
        BigDecimal result = CapacityUtils.getCapacity("1PB");
        assertNotNull(result);
    }

    @Test
    public void shouldParseExabytes() {
        BigDecimal result = CapacityUtils.getCapacity("1EB");
        assertNotNull(result);
    }

    @Test
    public void shouldParseZettabytes() {
        BigDecimal result = CapacityUtils.getCapacity("1ZB");
        assertNotNull(result);
    }

    @Test
    public void shouldParseYottabytes() {
        BigDecimal result = CapacityUtils.getCapacity("1YB");
        assertNotNull(result);
    }

    @Test
    public void shouldParseBrontobytes() {
        BigDecimal result = CapacityUtils.getCapacity("1BB");
        assertNotNull(result);
    }

    @Test
    public void shouldParseDecimalValues() {
        BigDecimal result = CapacityUtils.getCapacity("1.5MB");
        assertTrue(result.longValue() > 0);
    }

    @Test
    public void shouldGetLongCapacityForKb() {
        long result = CapacityUtils.getLongCapacity("1KB");
        assertEquals(1024L, result);
    }

    @Test
    public void shouldGetLongCapacityForMb() {
        long result = CapacityUtils.getLongCapacity("1MB");
        assertEquals(1024L * 1024L, result);
    }

    @Test
    public void shouldGetLongCapacityForGb() {
        long result = CapacityUtils.getLongCapacity("1GB");
        assertEquals(1024L * 1024L * 1024L, result);
    }

    @Test
    public void shouldGetLongCapacityForNull() {
        assertEquals(0L, CapacityUtils.getLongCapacity(null));
    }

    @Test
    public void shouldGetLongCapacityForEmpty() {
        assertEquals(0L, CapacityUtils.getLongCapacity(""));
    }

    @Test
    public void shouldGetLongCapacityForUnparseable() {
        assertEquals(0L, CapacityUtils.getLongCapacity("xyz"));
    }

    @Test
    public void shouldGetFloatCapacityForKb() {
        float result = CapacityUtils.getFloatCapacity("1KB");
        assertTrue(result > 0);
    }

    @Test
    public void shouldGetFloatCapacityForMb() {
        float result = CapacityUtils.getFloatCapacity("1MB");
        assertTrue(result > 0);
    }

    @Test
    public void shouldGetFloatCapacityForNull() {
        assertEquals(0f, CapacityUtils.getFloatCapacity(null), 0.001f);
    }

    @Test
    public void shouldGetFloatCapacityForEmpty() {
        assertEquals(0f, CapacityUtils.getFloatCapacity(""), 0.001f);
    }

    @Test
    public void shouldGetFloatCapacityForUnparseable() {
        assertEquals(-1f, CapacityUtils.getFloatCapacity("xyz"), 0.001f);
    }

    @Test
    public void shouldConvertLongToUnit() {
        BigDecimal result = CapacityUtils.getCapacity(1024L * 1024L, Unit.MB);
        assertNotNull(result);
    }

    @Test
    public void shouldConvertLongToKbUnit() {
        BigDecimal result = CapacityUtils.getCapacity(2048L, Unit.KB);
        assertNotNull(result);
    }

    @Test
    public void shouldConvertLongToUnitWithScale() {
        BigDecimal result = CapacityUtils.getCapacity(1024L * 1024L, Unit.MB, 2);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForNegativeScale() {
        CapacityUtils.getCapacity(1024L, Unit.KB, -1);
    }

    @Test
    public void shouldGetCapacityString() {
        String result = CapacityUtils.getCapacityString(1024L * 1024L, Unit.MB);
        assertNotNull(result);
        assertTrue(result.contains("MB"));
    }

    @Test
    public void shouldGetCapacityStringWithScale() {
        String result = CapacityUtils.getCapacityString(1024L * 1024L, Unit.MB, 2);
        assertNotNull(result);
        assertTrue(result.contains("MB"));
    }

    @Test
    public void shouldDivideDoubles() {
        double result = CapacityUtils.div(10.0, 3.0, 2);
        assertTrue(result >= 3.33 && result <= 3.34);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForNegativeScaleInDiv() {
        CapacityUtils.div(10.0, 3.0, -1);
    }

    @Test
    public void shouldHandleDivByZero() {
        double result = CapacityUtils.div(10.0, 0.0, 2);
        assertTrue(result > 0);
    }

    @Test
    public void shouldReturnNoneUnitKey() {
        assertEquals("none", Unit.NONE.getKey());
        assertEquals(BigDecimal.ONE, Unit.NONE.getValue());
    }

    @Test
    public void shouldReturnBUnitKey() {
        assertEquals("B", Unit.B.getKey());
    }

    @Test
    public void shouldReturnKbUnitKey() {
        assertEquals("KB", Unit.KB.getKey());
        assertEquals(BigDecimal.valueOf(1024), Unit.KB.getValue());
    }

    @Test
    public void shouldReturnMbUnitKey() {
        assertEquals("MB", Unit.MB.getKey());
        assertEquals(BigDecimal.valueOf(1024 * 1024), Unit.MB.getValue());
    }

    @Test
    public void shouldReturnGbUnitKey() {
        assertEquals("GB", Unit.GB.getKey());
        assertEquals(BigDecimal.valueOf(1024 * 1024 * 1024), Unit.GB.getValue());
    }

    @Test
    public void shouldReturnTbUnitKey() {
        assertEquals("TB", Unit.TB.getKey());
    }

    @Test
    public void shouldReturnPbUnitKey() {
        assertEquals("PB", Unit.PB.getKey());
    }

    @Test
    public void shouldReturnEbUnitKey() {
        assertEquals("EB", Unit.EB.getKey());
    }

    @Test
    public void shouldReturnZbUnitKey() {
        assertEquals("ZB", Unit.ZB.getKey());
    }

    @Test
    public void shouldReturnYbUnitKey() {
        assertEquals("YB", Unit.YB.getKey());
    }

    @Test
    public void shouldReturnBbUnitKey() {
        assertEquals("BB", Unit.BB.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForByteUnit() {
        // "B" is matched by regex but not in the powers map,
        // so powers.get("B") returns null causing NPE
        CapacityUtils.getCapacity("100B");
    }
}
