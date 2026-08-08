package com.codahale.metrics.sigar;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.codahale.metrics.MetricRegistry;

/**
 * Collects filesystem-level metrics via Sigar, providing information about
 * mounted filesystems including device name, mount point, type, total size,
 * and free space.
 *
 * <p>This class does not register any gauges by default; callers should
 * use {@link #filesystems()} to obtain filesystem snapshots.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see AbstractSigarMetric
 * @see FileSystem
 */
public class FilesystemMetrics extends AbstractSigarMetric {

    /**
     * Generic filesystem type enumeration whose ordinals mirror the
     * constants in {@code org.hyperic.sigar.FileSystem}.
     */
    public enum FSType {
        // Ordered so that ordinals match the constants
        // in org.hyperic.sigar.FileSystem
        Unknown, None, LocalDisk, Network, Ramdisk, Cdrom, Swap
    }

    /**
     * Immutable value object representing a single filesystem entry
     * together with its capacity information.
     */
    public static final class FileSystem {
        private final String deviceName;
        private final String mountPoint;
        private final FSType genericFSType;
        private final String osSpecificFSType;
        private final long totalSizeKB;
        private final long freeSpaceKB;

        /**
         * Constructs a new {@code FileSystem} snapshot.
         *
         * @param deviceName      the device name (e.g. {@code /dev/sda1})
         * @param mountPoint      the mount point (e.g. {@code /})
         * @param genericFSType   the generic filesystem type
         * @param osSpecificFSType the OS-specific filesystem type name (e.g. {@code ext4})
         * @param totalSizeKB     total size in kilobytes
         * @param freeSpaceKB     free space in kilobytes
         */
        public FileSystem( //
                String deviceName, String mountPoint, //
                FSType genericFSType, String osSpecificFSType, //
                long totalSizeKB, long freeSpaceKB) {
            this.deviceName = deviceName;
            this.mountPoint = mountPoint;
            this.genericFSType = genericFSType;
            this.osSpecificFSType = osSpecificFSType;
            this.totalSizeKB = totalSizeKB;
            this.freeSpaceKB = freeSpaceKB;
        }

        /**
         * Creates a {@code FileSystem} from a Sigar {@code FileSystem} bean
         * and the associated usage data.
         *
         * @param fs          the Sigar filesystem bean; must not be {@code null}
         * @param totalSizeKB total filesystem size in kilobytes
         * @param freeSpaceKB free space in kilobytes
         * @return a new {@code FileSystem} instance
         */
        public static FileSystem fromSigarBean(org.hyperic.sigar.FileSystem fs,
                long totalSizeKB, long freeSpaceKB) {
            return new FileSystem( //
                    fs.getDevName(), fs.getDirName(), //
                    FSType.values()[fs.getType()], fs.getSysTypeName(), //
                    totalSizeKB, freeSpaceKB);
        }

        /** @return the device name */
        public String deviceName() { return deviceName; }
        /** @return the mount point */
        public String mountPoint() { return mountPoint; }
        /** @return the generic filesystem type */
        public FSType genericFSType() { return genericFSType; }
        /** @return the OS-specific filesystem type name */
        public String osSpecificFSType() { return osSpecificFSType; }
        /** @return total size in kilobytes */
        public long totalSizeKB() { return totalSizeKB; }
        /** @return free space in kilobytes */
        public long freeSpaceKB() { return freeSpaceKB; }
    }

    /**
     * Registers gauges for filesystem metrics. This implementation is a
     * no-op; override to add custom gauges.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerGauges(MetricRegistry registry) {
        // Do not register any gauges
    }

    /**
     * Constructs a new {@code FilesystemMetrics} collector.
     *
     * @param sigar the Sigar instance; must not be {@code null}
     */
    protected FilesystemMetrics(Sigar sigar) {
        super(sigar);
    }

    /**
     * Returns a snapshot of all mounted filesystems with their capacity
     * information.
     *
     * @return a list of {@link FileSystem} entries; never {@code null}
     *         but may be empty on error
     */
    public List<FileSystem> filesystems() {
        List<FileSystem> result = new ArrayList<FileSystem>();
        org.hyperic.sigar.FileSystem[] fss = null;
        try {
            fss = sigar.getFileSystemList();
        } catch (SigarException e) {
            // give up
            return result;
        }

        if (fss == null) {
            return result;
        }

        for (org.hyperic.sigar.FileSystem fs: fss) {
            long totalSizeKB = 0L;
            long freeSpaceKB = 0L;
            try {
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                totalSizeKB = usage.getTotal();
                freeSpaceKB = usage.getFree();
            } catch (SigarException e) {
                // ignore
            }
            result.add(FileSystem.fromSigarBean(fs, totalSizeKB, freeSpaceKB));
        }
        return result;
   }

}
