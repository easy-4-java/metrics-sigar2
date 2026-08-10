package com.codahale.metrics.sigar;

import com.codahale.metrics.MetricRegistry;

/**
 * Contract for metric collectors that can register one or more
 * {@link com.codahale.metrics.Gauge Gauge} instances with a
 * {@link MetricRegistry}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see com.codahale.metrics.Gauge
 * @see MetricRegistry
 */
interface CanRegisterGauges {

    /**
     * Register zero or more Gauges in the given registry.
     *
     * @param registry the metric registry to register gauges with; must not be {@code null}
     */
    public void registerGauges(MetricRegistry registry);

}
