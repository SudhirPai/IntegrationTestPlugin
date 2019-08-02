package com.cerner.plugins

class pluginExtension {

    @PackageScope
    static final String NAME = 'integrationTest'

    String srcDir = 'src/integrationTest/java'
    String resourcesDir = "src/integrationTest/resources"
    boolean markAsTestSources = true

    @PackageScope
    OptionsExtension optionsExtension

    private final Project project

    ItPluginExtension(Project project) {
        this.project = project
        def task = project.tasks[ItTask.NAME] as ItTask
        this.optionsExtension = new OptionsExtension(task)
    }

    OptionsExtension options(Closure closure) {
        optionsExtension = project.configure(optionsExtension, closure) as OptionsExtension
    }



}