package com.codahale.metrics.sigar;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;

/**
 * Collects CPU-related metrics via Sigar, including total core count,
 * physical CPU count, and per-core time breakdown (user, system, nice,
 * waiting, idle, IRQ).
 *
 * <p>This class also registers {@link Gauge} and {@link RatioGauge}
 * instances for CPU metrics when {@link #registerGauges(MetricRegistry)}
 * is invoked.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see AbstractSigarMetric
 * @see CpuTime
 */
public class CpuMetrics extends AbstractSigarMetric {
    /** Delay (in milliseconds) used as a workaround for Sigar returning NaN values. */
    private static final long HACK_DELAY_MILLIS = 1000;

    /** Cached CPU info obtained during construction. */
    private final CpuInfo info;

    /**
     * Immutable value object representing the time breakdown of a single CPU.
     * All values are fractions in the range {@code [0.0, 1.0]} and should
     * sum to approximately {@code 1.0}.
     */
    public static final class CpuTime {
        private final double user;
        private final double sys;
        private final double nice;
        private final double waiting;
        private final double idle;
        private final double irq;

        /**
         * Constructs a new {@code CpuTime} with the given time fractions.
         *
         * @param user    fraction of time spent in user mode
         * @param sys     fraction of time spent in system/kernel mode
         * @param nice    fraction of time spent on low-priority (niced) processes
         * @param waiting fraction of time spent waiting for I/O
         * @param idle    fraction of time spent idle
         * @param irq     fraction of time spent servicing hardware interrupts
         */
        public CpuTime( //
                double user, double sys, //
                double nice, double waiting, //
                double idle, double irq) {
            this.user = user;
            this.sys = sys;
            this.nice = nice;
            this.waiting = waiting;
            this.idle = idle;
            this.irq = irq;
        }

        /**
         * Creates a {@code CpuTime} instance from a Sigar {@link CpuPerc} bean.
         *
         * @param cp the Sigar CPU percentage bean; must not be {@code null}
         * @return a new {@code CpuTime} populated from the given bean
         */
        public static CpuTime fromSigarBean(CpuPerc cp) {
            return new CpuTime( //
                    cp.getUser(), cp.getSys(), //
                    cp.getNice(), cp.getWait(), //
                    cp.getIdle(), cp.getIrq());
        }

        /** @return fraction of time spent in user mode */
        public double user() { return user; }
        /** @return fraction of time spent in system/kernel mode */
        public double sys() { return sys; }
        /** @return fraction of time spent on low-priority processes */
        public double nice() { return nice; }
        /** @return fraction of time spent waiting for I/O */
        public double waiting() { return waiting; }
        /** @return fraction of time spent idle */
        public double idle() { return idle; }
        /** @return fraction of time spent servicing hardware interrupts */
        public double irq() { return irq; }
    }

    /**
     * Registers all CPU gauges (total cores, physical CPUs, user/sys time)
     * with the given registry.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerGauges(MetricRegistry registry) {
        registerTotalCores(registry);
        registerPhysicalCpus(registry);
        registerCpuTimeUserPercent(registry);
        registerCpuTimeSysPercent(registry);
    }

    /**
     * Registers a gauge for the total core count using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerTotalCores(MetricRegistry registry) {
        registerTotalCores(registry, MetricRegistry.name(getClass(), "total-cores"));
    }

    /**
     * Registers a gauge for the total core count with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerTotalCores(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Integer>() {
            public Integer getValue() {
                return totalCoreCount();
            }
        });
    }

    /**
     * Registers a gauge for the physical CPU count using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerPhysicalCpus(MetricRegistry registry) {
        registerPhysicalCpus(registry, MetricRegistry.name(getClass(), "physical-cpus"));
    }

    /**
     * Registers a gauge for the physical CPU count with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerPhysicalCpus(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Integer>() {
            public Integer getValue() {
                return physicalCpuCount();
            }
        });
    }

    /**
     * Registers a ratio gauge for the aggregated user-mode CPU time
     * percentage using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerCpuTimeUserPercent(MetricRegistry registry) {
        registerCpuTimeUserPercent(registry, MetricRegistry.name(getClass(), "cpu-time-user-percent"));
    }

    /**
     * Registers a ratio gauge for the aggregated user-mode CPU time
     * percentage with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerCpuTimeUserPercent(MetricRegistry registry, String name) {
        registry.register(name, new RatioGauge() {

            @Override
            protected Ratio getRatio() {
                return Ratio.of(getNumerator(), 1.0);
            }

            private double getNumerator() {
                List<CpuTime> cpus = cpus();
                double userTime = 0.0;
                for (CpuTime cpu : cpus) {
                    userTime += cpu.user();
                }
                return userTime;
            }
        });
    }

    /**
     * Registers a ratio gauge for the aggregated system-mode CPU time
     * percentage using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerCpuTimeSysPercent(MetricRegistry registry) {
        registerCpuTimeSysPercent(registry, MetricRegistry.name(getClass(), "cpu-time-sys-percent"));
    }

    /**
     * Registers a ratio gauge for the aggregated system-mode CPU time
     * percentage with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerCpuTimeSysPercent(MetricRegistry registry, String name) {
        registry.register(name, new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(getNumerator(), 1.0);
            }

            private double getNumerator() {
                List<CpuTime> cpus = cpus();
                double userTime = 0.0;
                for (CpuTime cpu : cpus) {
                    userTime += cpu.sys();
                }
                return userTime;
            }
        });
    }

    /**
     * Constructs a new {@code CpuMetrics} collector backed by the given Sigar instance.
     *
     * @param sigar the Sigar instance; must not be {@code null}
     */
    protected CpuMetrics(Sigar sigar) {
        super(sigar);
        info = cpuInfo();
    }

    /**
     * Returns the total number of logical CPU cores.
     *
     * @return the total core count, or {@code -1} if CPU info is unavailable
     */
    public int totalCoreCount() {
        if (info == null) {
            return -1;
        }
        return info.getTotalCores();
    }

    /**
     * Returns the number of physical CPU sockets.
     *
     * @return the physical CPU count, or {@code -1} if CPU info is unavailable
     */
    public int physicalCpuCount() {
        if (info == null) {
            return -1;
        }
       return info.getTotalSockets();
    }

    /**
     * Returns the current CPU time breakdown for every logical CPU.
     *
     * <p>If Sigar returns NaN values (a known intermittent issue), this
     * method sleeps for one second and retries once before returning an
     * empty list.</p>
     *
     * @return a list of {@link CpuTime} objects, one per logical CPU;
     *         never {@code null} but may be empty on error
     */
    public List<CpuTime> cpus() {
        List<CpuTime> result = new ArrayList<CpuTime>();
        CpuPerc[] cpus = cpuPercList();
        if (cpus == null) {
            return result;
        }

        if (Double.isNaN(cpus[0].getIdle())) {
            /*
             * XXX: Hacky workaround for strange Sigar behaviour.
             * If you call sigar.getCpuPerfList() too often(?),
             * it returns a steaming pile of NaNs.
             *
             * See suspicious code here:
             * https://github.com/hyperic/sigar/blob/master/bindings/java/src/org/hyperic/sigar/Sigar.java#L345-348
             *
             */
            try {
                Thread.sleep(HACK_DELAY_MILLIS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return result;
            }
            cpus = cpuPercList();
            if (cpus == null) {
                return result;
            }
        }
        for (CpuPerc cp : cpus) {
            result.add(CpuTime.fromSigarBean(cp));
        }
        return result;
   }

    /**
     * Retrieves the first {@link CpuInfo} entry from Sigar.
     *
     * @return the first {@link CpuInfo}, or {@code null} on error or empty list
     */
    private CpuInfo cpuInfo() {
        try {
            CpuInfo[] infos = sigar.getCpuInfoList();
            if (infos == null || infos.length == 0) {
                return null;
            }
            return infos[0];
        } catch (SigarException e) {
            // give up
            return null;
        }
    }

    /**
     * Retrieves the current CPU percentage list from Sigar.
     *
     * @return an array of {@link CpuPerc}, or {@code null} on error or empty result
     */
    private CpuPerc[] cpuPercList() {
        CpuPerc[] cpus = null;
        try {
            cpus = sigar.getCpuPercList();
        } catch (SigarException e) {
            // give up
        }
        if (cpus == null || cpus.length == 0) {
            return null;
        }
        return cpus;
    }

}
