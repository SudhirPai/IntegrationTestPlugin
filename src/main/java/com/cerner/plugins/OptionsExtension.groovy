
package com.cerner.plugins

import groovy.transform.PackageScope

/**
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
class OptionsExtension {

	private static final String MAX_HEAP_SIZE = '256m'

	private static final int MAX_PARALLEL_FORKS = 4

	@Delegate
	private final ItTask task

	@PackageScope
	OptionsExtension(ItTask task) {
		this.task = task
		setMaxHeapSize(MAX_HEAP_SIZE)
		setMaxParallelForks(MAX_PARALLEL_FORKS)
	}

}
