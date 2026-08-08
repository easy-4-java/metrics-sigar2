package com.codahale.metrics.sigar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.sigar.FilesystemMetrics.FileSystem;
import com.codahale.metrics.sigar.FilesystemMetrics.FSType;

public class FilesystemMetricsMockTest {

    @Test
    public void shouldReturnEmptyListWhenSigarThrows() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getFileSystemList()).thenThrow(new SigarException("test"));

        FilesystemMetrics fsm = new FilesystemMetrics(sigar);
        List<FileSystem> fss = fsm.filesystems();
        assertNotNull(fss);
        assertTrue(fss.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenFssIsNull() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        when(sigar.getFileSystemList()).thenReturn(null);

        FilesystemMetrics fsm = new FilesystemMetrics(sigar);
        List<FileSystem> fss = fsm.filesystems();
        assertNotNull(fss);
        assertTrue(fss.isEmpty());
    }

    @Test
    public void shouldRegisterGaugesNoOp() throws SigarException {
        Sigar sigar = mock(Sigar.class);
        FilesystemMetrics fsm = new FilesystemMetrics(sigar);
        MetricRegistry registry = new MetricRegistry();
        fsm.registerGauges(registry);
        assertEquals(0, registry.getNames().size());
    }

    @Test
    public void shouldCreateFileSystem() {
        FileSystem fs = new FileSystem("/dev/sda1", "/", FSType.LocalDisk, "ext4", 100000L, 50000L);
        assertEquals("/dev/sda1", fs.deviceName());
        assertEquals("/", fs.mountPoint());
        assertEquals(FSType.LocalDisk, fs.genericFSType());
        assertEquals("ext4", fs.osSpecificFSType());
        assertEquals(100000L, fs.totalSizeKB());
        assertEquals(50000L, fs.freeSpaceKB());
    }

    @Test
    public void shouldHaveAllFSTypes() {
        assertEquals(7, FSType.values().length);
        assertEquals(FSType.Unknown, FSType.values()[0]);
        assertEquals(FSType.None, FSType.values()[1]);
        assertEquals(FSType.LocalDisk, FSType.values()[2]);
        assertEquals(FSType.Network, FSType.values()[3]);
        assertEquals(FSType.Ramdisk, FSType.values()[4]);
        assertEquals(FSType.Cdrom, FSType.values()[5]);
        assertEquals(FSType.Swap, FSType.values()[6]);
    }
}
