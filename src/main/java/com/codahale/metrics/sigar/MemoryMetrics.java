package com.codahale.metrics.sigar;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

/**
 * Collects memory-related metrics via Sigar, including physical RAM
 * usage and swap space usage.
 *
 * <p>Provides methods to register {@link Gauge} instances for memory
 * and swap metrics with a {@link MetricRegistry}.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractSigarMetric
 * @see MainMemory
 * @see SwapSpace
 */
public class MemoryMetrics extends AbstractSigarMetric {

    /**
     * Constructs a new {@code MemoryMetrics} collector.
     *
     * @param sigar the Sigar instance; must not be {@code null}
     */
    protected MemoryMetrics(Sigar sigar) {
        super(sigar);
    }

    /**
     * Abstract base class for memory segments (physical RAM or swap)
     * providing total, used, and free values.
     */
    public static abstract class MemSegment {
        protected final long total;
        protected final long used;
        protected final long free;

        /**
         * Constructs a new memory segment.
         *
         * @param total total memory in bytes
         * @param used  used memory in bytes
         * @param free  free memory in bytes
         */
        private MemSegment(long total, long used, long free) {
            this.total = total;
            this.used = used;
            this.free = free;
        }
        /** @return total memory in bytes */
        public long total() { return total; }
        /** @return used memory in bytes */
        public long used() { return used; }
        /** @return free memory in bytes */
        public long free() { return free; }
    }

    /**
     * Represents the state of physical main memory (RAM) as reported
     * by Sigar, including actual used/free values and percentages.
     */
    public static final class MainMemory extends MemSegment {
        private final long actualUsed, actualFree;
        private final double usedPercent, freePercent;

        private MainMemory(//
                long total, long used, long free, //
                long actualUsed, long actualFree,
                double usedPercent, double freePercent) {
            super(total, used, free);
            this.actualUsed = actualUsed;
            this.actualFree = actualFree;
            this.usedPercent = usedPercent;
            this.freePercent = freePercent;
        }

        /**
         * Creates a {@code MainMemory} from a Sigar {@link Mem} bean.
         *
         * @param mem the Sigar memory bean; must not be {@code null}
         * @return a new {@code MainMemory} instance
         */
        public static MainMemory fromSigarBean(Mem mem) {
            return new MainMemory( //
                    mem.getTotal(), mem.getUsed(), mem.getFree(), //
                    mem.getActualUsed(), mem.getActualFree(),
                    mem.getUsedPercent(), mem.getFreePercent());
        }

        /**
         * Returns an undefined {@code MainMemory} instance with all
         * values set to {@code -1}.
         *
         * @return an undefined {@code MainMemory} sentinel
         */
        private static MainMemory undef() {
            return new MainMemory(-1L, -1L, -1L, -1L, -1L, -1, -1);
        }

        /** @return actual used memory in bytes */
        public long actualUsed() { return actualUsed; }
        /** @return actual free memory in bytes */
        public long actualFree() { return actualFree; }
        /** @return used memory as a percentage (0-100) */
        public double usedPercent() { return usedPercent; }
        /** @return free memory as a percentage (0-100) */
        public double freePercent() { return freePercent; }
    }

    /**
     * Represents the state of swap space as reported by Sigar,
     * including page-in and page-out counts.
     */
    public static final class SwapSpace extends MemSegment {
        private final long pagesIn, pagesOut;

        private SwapSpace( //
                long total, long used, long free, //
                long pagesIn, long pagesOut) {
            super(total, used, free);
            this.pagesIn = pagesIn;
            this.pagesOut = pagesOut;
        }

        /**
         * Creates a {@code SwapSpace} from a Sigar {@link Swap} bean.
         *
         * @param swap the Sigar swap bean; must not be {@code null}
         * @return a new {@code SwapSpace} instance
         */
        public static SwapSpace fromSigarBean(Swap swap) {
            return new SwapSpace( //
                    swap.getTotal(), swap.getUsed(), swap.getFree(), //
                    swap.getPageIn(), swap.getPageOut());
        }

        /**
         * Returns an undefined {@code SwapSpace} instance with all
         * values set to {@code -1}.
         *
         * @return an undefined {@code SwapSpace} sentinel
         */
        private static SwapSpace undef() {
            return new SwapSpace(-1L, -1L, -1L, -1L, -1L);
        }

        /** @return total pages paged in */
        public long pagesIn() { return pagesIn; }
        /** @return total pages paged out */
        public long pagesOut() { return pagesOut; }
    }

    /**
     * Returns a snapshot of main memory (RAM) usage.
     *
     * @return a {@link MainMemory} instance; on error returns an
     *         undefined instance with all values set to {@code -1}
     */
    public MainMemory mem() {
        try {
            return MainMemory.fromSigarBean(sigar.getMem());
        } catch (SigarException e) {
            return MainMemory.undef();
        }
    }

    /**
     * Returns a snapshot of swap space usage.
     *
     * @return a {@link SwapSpace} instance; on error returns an
     *         undefined instance with all values set to {@code -1}
     */
    public SwapSpace swap() {
        try {
            return SwapSpace.fromSigarBean(sigar.getSwap());
        } catch (SigarException e) {
            return SwapSpace.undef();
        }
    }

    /**
     * Returns the amount of physical RAM in megabytes.
     *
     * @return RAM size in MB, or {@code -1} on error
     */
    public long ramInMB() {
        try {
            return sigar.getMem().getRam();
        } catch (SigarException e) {
            return -1L;
        }
    }

    /**
     * Registers all memory and swap gauges with the given registry.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    @Override
    public void registerGauges(MetricRegistry registry) {
        registerMemoryFree(registry);
        registerMemoryActualFree(registry);
        registerMemoryUsed(registry);
        registerMemoryActualUsed(registry);
        registerMemoryTotal(registry);
        registerMemoryUsedPercent(registry);
        registerMemoryFreePercent(registry);
        registerSwapFree(registry);
        registerSwapPagesIn(registry);
        registerSwapPagesOut(registry);
    }

    /**
     * Registers a gauge for free memory using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerMemoryFree(MetricRegistry registry) {
        registerMemoryFree(registry, MetricRegistry.name(getClass(), "memory-free"));
    }

    /**
     * Registers a gauge for free memory with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerMemoryFree(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return mem().free();
            }
        });
    }

    /**
     * Registers a gauge for actual free memory using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerMemoryActualFree(MetricRegistry registry) {
        registerMemoryActualFree(registry, MetricRegistry.name(getClass(), "memory-actual-free"));
    }

    /**
     * Registers a gauge for actual free memory with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerMemoryActualFree(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return mem().actualFree();
            }
        });
    }

    /**
     * Registers a gauge for used memory using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerMemoryUsed(MetricRegistry registry) {
        registerMemoryUsed(registry, MetricRegistry.name(getClass(), "memory-used"));
    }

    /**
     * Registers a gauge for used memory with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerMemoryUsed(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return mem().used();
            }
        });
    }

    /**
     * Registers a gauge for actual used memory using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerMemoryActualUsed(MetricRegistry registry) {
        registerMemoryActualUsed(registry, MetricRegistry.name(getClass(), "memory-actual-used"));
    }

    /**
     * Registers a gauge for actual used memory with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerMemoryActualUsed(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return mem().actualUsed();
            }
        });
    }

    /**
     * Registers a gauge for total memory using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerMemoryTotal(MetricRegistry registry) {
        registerMemoryTotal(registry, MetricRegistry.name(getClass(), "memory-total"));
    }

    /**
     * Registers a gauge for total memory with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerMemoryTotal(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return mem().total();
            }
        });
    }

    /**
     * Registers a gauge for memory used percentage using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerMemoryUsedPercent(MetricRegistry registry) {
        registerMemoryUsedPercent(registry, MetricRegistry.name(getClass(), "memory-used-percent"));
    }

    /**
     * Registers a gauge for memory used percentage with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerMemoryUsedPercent(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Double>() {
            public Double getValue() {
                return mem().usedPercent();
            }
        });
    }

    /**
     * Registers a gauge for memory free percentage using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerMemoryFreePercent(MetricRegistry registry) {
        registerMemoryFreePercent(registry, MetricRegistry.name(getClass(), "memory-free-percent"));
    }

    /**
     * Registers a gauge for memory free percentage with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerMemoryFreePercent(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Double>() {
            public Double getValue() {
                return mem().freePercent();
            }
        });
    }

    /**
     * Registers a gauge for free swap using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerSwapFree(MetricRegistry registry) {
        registerSwapFree(registry, MetricRegistry.name(getClass(), "swap-free"));
    }

    /**
     * Registers a gauge for free swap with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerSwapFree(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return swap().free();
            }
        });
    }

    /**
     * Registers a gauge for swap pages-in count using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerSwapPagesIn(MetricRegistry registry) {
        registerSwapPagesIn(registry, MetricRegistry.name(getClass(), "swap-pages-in"));
    }

    /**
     * Registers a gauge for swap pages-in count with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerSwapPagesIn(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return swap().pagesIn();
            }
        });
    }

    /**
     * Registers a gauge for swap pages-out count using the default name.
     *
     * @param registry the metric registry; must not be {@code null}
     */
    public void registerSwapPagesOut(MetricRegistry registry) {
        registerSwapPagesOut(registry, MetricRegistry.name(getClass(), "swap-pages-out"));
    }

    /**
     * Registers a gauge for swap pages-out count with a custom name.
     *
     * @param registry the metric registry; must not be {@code null}
     * @param name     the metric name; must not be {@code null}
     */
    public void registerSwapPagesOut(MetricRegistry registry, String name) {
        registry.register(name, new Gauge<Long>() {
            public Long getValue() {
                return swap().pagesOut();
            }
        });
    }

}
