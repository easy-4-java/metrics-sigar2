package com.codahale.metrics.sigar;

import org.hyperic.sigar.Sigar;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

/**
 * Singleton entry point for all Sigar-based OS metrics.
 *
 * <p>Aggregates {@link CpuMetrics}, {@link MemoryMetrics},
 * {@link FilesystemMetrics}, and {@link UlimitMetrics} into a single
 * facade. Use {@link #getInstance()} to obtain the shared instance.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see CpuMetrics
 * @see MemoryMetrics
 * @see FilesystemMetrics
 * @see UlimitMetrics
 */
public class SigarMetrics implements CanRegisterGauges {
    private static final SigarMetrics instance = new SigarMetrics();

    /**
     * Returns the singleton {@code SigarMetrics} instance.
     *
     * @return the shared {@code SigarMetrics} instance; never {@code null}
     */
    public static SigarMetrics getInstance() {
        return instance;
    }

    private final Sigar sigar = new Sigar();
    private final CpuMetrics cpu = new CpuMetrics(sigar);
    private final MemoryMetrics memory = new MemoryMetrics(sigar);
    private final FilesystemMetrics fs = new FilesystemMetrics(sigar);
    private final UlimitMetrics ulimit = new UlimitMetrics(sigar);

    private SigarMetrics() {
        // singleton
    }

    /**
     * Registers a PID gauge and delegates to all sub-metric collectors
     * to register their gauges.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerGauges(MetricRegistry registry) {
        registry.register(MetricRegistry.name(getClass(), "pid"), new Gauge<Long>() {
          public Long getValue() {
            return pid();
          }
        });

        cpu.registerGauges(registry);
        memory.registerGauges(registry);
        fs.registerGauges(registry);
        ulimit.registerGauges(registry);
    }

    /**
     * Returns the current process ID.
     *
     * @return the PID of the current process
     */
    public long pid() {
        return sigar.getPid();
    }

    /**
     * Returns the CPU metrics collector.
     *
     * @return the {@link CpuMetrics} instance; never {@code null}
     */
    public CpuMetrics cpu() {
        return cpu;
    }

    /**
     * Returns the memory metrics collector.
     *
     * @return the {@link MemoryMetrics} instance; never {@code null}
     */
    public MemoryMetrics memory() {
        return memory;
    }

    /**
     * Returns the filesystem metrics collector.
     *
     * @return the {@link FilesystemMetrics} instance; never {@code null}
     */
    public FilesystemMetrics filesystems() {
        return fs;
    }

    /**
     * Returns the ulimit metrics collector.
     *
     * @return the {@link UlimitMetrics} instance; never {@code null}
     */
    public UlimitMetrics ulimit() {
        return ulimit;
    }
}
