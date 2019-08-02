package com.cerner.plugins


class ItTask extends Test {

    @PackageScope
    static final String NAME = 'integration-test'

    @PackageScope
    static final String GROUP = 'verification'

    @PackageScope
    static final String DESCRIPTION = 'Runs the integration tests'

    ItTask() {
        group = GROUP
        description = DESCRIPTION
    }

}