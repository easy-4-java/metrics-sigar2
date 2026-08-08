package com.codahale.metrics.sigar;

import static org.junit.Assume.assumeNoException;

import org.hyperic.sigar.Sigar;
import org.junit.BeforeClass;

/**
 * Base class for tests that require the Sigar native library.
 * Tests extending this class will be skipped if Sigar cannot be loaded.
 *
 * @author chris
 */
public abstract class CheckSigarLoadsOk {

    @BeforeClass
    public static final void canLoadSigarCheck() {
        try {
            Sigar.load();
        } catch (Throwable e) {
            assumeNoException(e);
        }
    }

}
