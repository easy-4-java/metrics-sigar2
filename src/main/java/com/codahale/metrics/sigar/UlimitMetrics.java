package com.codahale.metrics.sigar;

import org.hyperic.sigar.ResourceLimit;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

/**
 * Collects ulimit (resource limit) metrics via Sigar.
 *
 * <p>Provides access to current resource limits for the running process,
 * such as open file descriptors, stack size, core file size, etc.
 * Infinite limits reported by the OS are replaced with {@code -1}.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see AbstractSigarMetric
 * @see Ulimit
 */
public class UlimitMetrics extends AbstractSigarMetric {

    /** The OS-reported value representing an infinite resource limit. */
    private final long infinity;

    /**
     * Constructs a new {@code UlimitMetrics} collector.
     *
     * @param sigar the Sigar instance; must not be {@code null}
     */
    protected UlimitMetrics(Sigar sigar) {
        super(sigar);
        infinity = ResourceLimit.INFINITY();
    }

    /**
     * Immutable value object representing the current resource limits
     * (ulimit values) for the running process.
     */
    public static final class Ulimit {
        private final long coreFileSize, dataSegSize,
                fileSize, pipeSize,
                memSize, openFiles,
                stackSize, cpuTime,
                processes, virtMem;

        private Ulimit(//
                long coreFileSize, long dataSegSize, //
                long fileSize, long pipeSize, //
                long memSize, long openFiles, //
                long stackSize, long cpuTime, //
                long processes, long virtMem) {
            this.coreFileSize = coreFileSize;
            this.dataSegSize = dataSegSize;
            this.fileSize = fileSize;
            this.pipeSize = pipeSize;
            this.memSize = memSize;
            this.openFiles = openFiles;
            this.stackSize = stackSize;
            this.cpuTime = cpuTime;
            this.processes = processes;
            this.virtMem = virtMem;
        }

        /**
         * Creates a {@code Ulimit} from a Sigar {@link ResourceLimit} bean,
         * replacing infinite values with {@code -1}.
         *
         * @param lim      the Sigar resource limit bean; must not be {@code null}
         * @param infinity the value Sigar uses to represent infinity
         * @return a new {@code Ulimit} instance
         */
        public static Ulimit fromSigarBean(ResourceLimit lim, long infinity) {
            return new Ulimit( //
                    replaceInfinity(lim.getCoreCur(), infinity), //
                    replaceInfinity(lim.getDataCur(), infinity), //
                    replaceInfinity(lim.getFileSizeCur(), infinity), //
                    replaceInfinity(lim.getPipeSizeCur(), infinity), //
                    replaceInfinity(lim.getMemoryCur(), infinity), //
                    replaceInfinity(lim.getOpenFilesCur(), infinity), //
                    replaceInfinity(lim.getStackCur(), infinity), //
                    replaceInfinity(lim.getCpuCur(), infinity), //
                    replaceInfinity(lim.getProcessesCur(), infinity), //
                    replaceInfinity(lim.getVirtualMemoryCur(), infinity));
        }

        /**
         * Returns an undefined {@code Ulimit} instance with all values
         * set to {@code -1}.
         *
         * @return an undefined {@code Ulimit} sentinel
         */
        public static Ulimit undef() {
            return new Ulimit(-1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L);
        }

        /** @return current core file size limit */
        public long coreFileSize() { return coreFileSize; }
        /** @return current data segment size limit */
        public long dataSegSize() { return dataSegSize; }
        /** @return current file size limit */
        public long fileSize() { return fileSize; }
        /** @return current pipe size limit */
        public long pipeSize() { return pipeSize; }
        /** @return current memory size limit */
        public long memSize() { return memSize; }
        /** @return current open files limit */
        public long openFiles() { return openFiles; }
        /** @return current stack size limit */
        public long stackSize() { return stackSize; }
        /** @return current CPU time limit */
        public long cpuTime() { return cpuTime; }
        /** @return current process count limit */
        public long processes() { return processes; }
        /** @return current virtual memory size limit */
        public long virtMemSize() { return virtMem; }

        /**
         * Replaces an infinite value with {@code -1}.
         *
         * @param value    the raw limit value
         * @param infinity the value representing infinity
         * @return {@code -1} if {@code value} equals {@code infinity},
         *         otherwise the original value
         */
        private static long replaceInfinity(long value, long infinity) {
          if (value == infinity) {
            return -1L;
          } else {
            return value;
          }
        }
    }

    /**
     * Returns a snapshot of the current process resource limits.
     *
     * @return a {@link Ulimit} instance; on error returns an undefined
     *         instance with all values set to {@code -1}
     */
    public Ulimit ulimit() {
        try {
            return Ulimit.fromSigarBean(sigar.getResourceLimit(), infinity);
        } catch (SigarException e) {
            return Ulimit.undef();
        }
    }

    /**
     * Registers gauges for open files and stack size limits.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerGauges(MetricRegistry registry) {
        registerUlimitOpenFiles(registry);
        registerUlimitStackSize(registry);
    }

    /**
     * Registers a gauge for the open files limit using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerUlimitOpenFiles(MetricRegistry registry) {
        registerUlimitOpenFiles(registry, MetricRegistry.name(getClass(), "ulimit-open-files"));
    }

    /**
     * Registers a gauge for the open files limit with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerUlimitOpenFiles(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return ulimit().openFiles();
            }
        });
    }

    /**
     * Registers a gauge for the stack size limit using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerUlimitStackSize(MetricRegistry registry) {
        registerUlimitStackSize(registry, MetricRegistry.name(getClass(), "ulimit-stack-size"));
    }

    /**
     * Registers a gauge for the stack size limit with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerUlimitStackSize(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return ulimit().stackSize();
            }
        });
    }

}
