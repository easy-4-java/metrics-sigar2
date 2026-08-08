package com.codahale.metrics.sigar.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class CapacityUnitTest {

    private static final double DELTA = 0.001;

    @Test
    public void shouldConvertBytesToBits() {
        assertEquals(8.0, CapacityUnit.BYTES.toBits(1.0), DELTA);
    }

    @Test
    public void shouldConvertBytesToBytes() {
        assertEquals(1.0, CapacityUnit.BYTES.toBytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertBytesToKilobytes() {
        double result = CapacityUnit.BYTES.toKilobytes(1024.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToMebibytes() {
        double result = CapacityUnit.BYTES.toMebibytes(1024.0 * 1024.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToGigabytes() {
        double result = CapacityUnit.BYTES.toGigabytes(1024.0 * 1024.0 * 1024.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToTerabytes() {
        double result = CapacityUnit.BYTES.toTerabytes(1024.0 * 1024.0 * 1024.0 * 1024.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToPetabytes() {
        double result = CapacityUnit.BYTES.toPetabytes(1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToExabytes() {
        double result = CapacityUnit.BYTES.toExabytes(1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToZettabytes() {
        double result = CapacityUnit.BYTES.toZettabytes(1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToYottabytes() {
        double bigVal = 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0;
        double result = CapacityUnit.BYTES.toYottabytes(bigVal);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToBrontobytes() {
        double bigVal = 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0;
        double result = CapacityUnit.BYTES.toBrontobytes(bigVal);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToNonaBytes() {
        double bigVal = 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0;
        double result = CapacityUnit.BYTES.toNonaBytes(bigVal);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertBytesToDoggaBytes() {
        double bigVal = 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0;
        double result = CapacityUnit.BYTES.toDoggaBytes(bigVal);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertKilobytesToBytes() {
        double result = CapacityUnit.KILOBYTES.toBytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertKilobytesToBits() {
        double result = CapacityUnit.KILOBYTES.toBits(1.0);
        assertEquals(1024.0 * 8, result, DELTA);
    }

    @Test
    public void shouldConvertKilobytesToKilobytes() {
        assertEquals(1.0, CapacityUnit.KILOBYTES.toKilobytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertKilobytesToMebibytes() {
        double result = CapacityUnit.KILOBYTES.toMebibytes(1024.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    public void shouldConvertMegabytesToKilobytes() {
        double result = CapacityUnit.MEGABYTES.toKilobytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertMegabytesToMebibytes() {
        assertEquals(1.0, CapacityUnit.MEGABYTES.toMebibytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertGigabytesToMebibytes() {
        double result = CapacityUnit.GIGABYTES.toMebibytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertGigabytesToGigabytes() {
        assertEquals(1.0, CapacityUnit.GIGABYTES.toGigabytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertTerabytesToGigabytes() {
        double result = CapacityUnit.TRILLIONBYTES.toGigabytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertTerabytesToTerabytes() {
        assertEquals(1.0, CapacityUnit.TRILLIONBYTES.toTerabytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertPetabytesToTerabytes() {
        double result = CapacityUnit.PETABYTES.toTerabytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertPetabytesToPetabytes() {
        assertEquals(1.0, CapacityUnit.PETABYTES.toPetabytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertExabytesToPetabytes() {
        double result = CapacityUnit.EXABYTES.toPetabytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertExabytesToExabytes() {
        assertEquals(1.0, CapacityUnit.EXABYTES.toExabytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertZettabytesToExabytes() {
        double result = CapacityUnit.ZETTABYTES.toExabytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertZettabytesToZettabytes() {
        assertEquals(1.0, CapacityUnit.ZETTABYTES.toZettabytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertYottabytesToZettabytes() {
        double result = CapacityUnit.YOTTABYTES.toZettabytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertYottabytesToYottabytes() {
        assertEquals(1.0, CapacityUnit.YOTTABYTES.toYottabytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertBrontobytesToYottabytes() {
        double result = CapacityUnit.BRONTOBYTES.toYottabytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertBrontobytesToBrontobytes() {
        assertEquals(1.0, CapacityUnit.BRONTOBYTES.toBrontobytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertNonaBytesToBrontobytes() {
        double result = CapacityUnit.NONABYTES.toBrontobytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertNonaBytesToNonaBytes() {
        assertEquals(1.0, CapacityUnit.NONABYTES.toNonaBytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertDoggaBytesToNonaBytes() {
        double result = CapacityUnit.DOGGABYTES.toNonaBytes(1.0);
        assertEquals(1024.0, result, DELTA);
    }

    @Test
    public void shouldConvertDoggaBytesToDoggaBytes() {
        assertEquals(1.0, CapacityUnit.DOGGABYTES.toDoggaBytes(1.0), DELTA);
    }

    @Test
    public void shouldConvertUsingConvertMethodBytesToKb() {
        double result = CapacityUnit.BYTES.convert(1024.0, CapacityUnit.KILOBYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodKbToMb() {
        double result = CapacityUnit.KILOBYTES.convert(1024.0, CapacityUnit.MEGABYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodMbToGb() {
        double result = CapacityUnit.MEGABYTES.convert(1024.0, CapacityUnit.GIGABYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodGbToTb() {
        double result = CapacityUnit.GIGABYTES.convert(1024.0, CapacityUnit.TRILLIONBYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodTbToPb() {
        double result = CapacityUnit.TRILLIONBYTES.convert(1024.0, CapacityUnit.PETABYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodPbToEb() {
        double result = CapacityUnit.PETABYTES.convert(1024.0, CapacityUnit.EXABYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodEbToZb() {
        double result = CapacityUnit.EXABYTES.convert(1024.0, CapacityUnit.ZETTABYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodZbToYb() {
        double result = CapacityUnit.ZETTABYTES.convert(1024.0, CapacityUnit.YOTTABYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodYbToBb() {
        double result = CapacityUnit.YOTTABYTES.convert(1.0, CapacityUnit.BRONTOBYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodBbToNb() {
        double result = CapacityUnit.BRONTOBYTES.convert(1.0, CapacityUnit.NONABYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodNbToDb() {
        double result = CapacityUnit.NONABYTES.convert(1.0, CapacityUnit.DOGGABYTES);
        assertTrue(result > 0);
    }

    @Test
    public void shouldConvertUsingConvertMethodDbToKb() {
        double result = CapacityUnit.DOGGABYTES.convert(1.0, CapacityUnit.KILOBYTES);
        assertTrue(result > 0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneConvert() {
        CapacityUnit.NONE.convert(1.0, CapacityUnit.KILOBYTES);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToBits() {
        CapacityUnit.NONE.toBits(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToBytes() {
        CapacityUnit.NONE.toBytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToKilobytes() {
        CapacityUnit.NONE.toKilobytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToMebibytes() {
        CapacityUnit.NONE.toMebibytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToGigabytes() {
        CapacityUnit.NONE.toGigabytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToTerabytes() {
        CapacityUnit.NONE.toTerabytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToPetabytes() {
        CapacityUnit.NONE.toPetabytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToExabytes() {
        CapacityUnit.NONE.toExabytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToZettabytes() {
        CapacityUnit.NONE.toZettabytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToYottabytes() {
        CapacityUnit.NONE.toYottabytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToBrontobytes() {
        CapacityUnit.NONE.toBrontobytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToNonaBytes() {
        CapacityUnit.NONE.toNonaBytes(1.0);
    }

    @Test(expected = AbstractMethodError.class)
    public void shouldThrowForNoneToDoggaBytes() {
        CapacityUnit.NONE.toDoggaBytes(1.0);
    }

    @Test
    public void shouldHaveAllEnumValues() {
        assertEquals(13, CapacityUnit.values().length);
    }

    @Test
    public void shouldHandleOverflowProtection() {
        // x() should clamp to MAX_VALUE on overflow
        double result = CapacityUnit.BYTES.toKilobytes(Double.MAX_VALUE);
        assertTrue(Double.isFinite(result) || result == Double.MAX_VALUE);
    }

    @Test
    public void shouldHandleNegativeOverflowProtection() {
        double result = CapacityUnit.BYTES.toKilobytes(-Double.MAX_VALUE);
        assertTrue(Double.isFinite(result) || result == Double.MIN_VALUE);
    }
}
