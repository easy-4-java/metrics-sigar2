package com.codahale.metrics.sigar;

import org.hyperic.sigar.Sigar;

/**
 * Abstract base class for all Sigar-based metric collectors.
 *
 * <p>Holds a reference to a {@link Sigar} instance that subclasses use
 * to gather native operating-system metrics such as CPU, memory,
 * filesystem, and ulimit information.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see CanRegisterGauges
 * @see Sigar
 */
abstract class AbstractSigarMetric implements CanRegisterGauges {

	/** The Sigar instance used to query native OS metrics. */
	protected final Sigar sigar;

	/**
	 * Constructs a new metric collector backed by the given Sigar instance.
	 *
	 * @param sigar the Sigar instance used for native OS metric queries; must not be {@code null}
	 */
	protected AbstractSigarMetric(Sigar sigar) {
		this.sigar = sigar;
	}

}
