package com.cerner.plugins

import com.intellij.openapi.project.Project
import org.gradle.api.Plugin
import org.gradle.api.Project

public class integrationTestPlugin implements Plugin <Project>
{
    @PackageScope
    static final String SOURCE_SET_NAME = 'integrationTest'
    private Project project

    @override
    void apply(Project project)
    {
        project.tasks.create(ItTask.NAME, ItTask)

        project.extensions.create(ItPluginExtension.NAME, ItPluginExtension, project)
        project.sourceSets.create(SOURCE_SET_NAME)

        project.afterEvaluate {
            def extension = project.extensions.getByType(ItPluginExtension)
            createDirectories(extension)
            configureSourceSet(extension)
            configureConfiguration()
            configureTask()
        }
    }

    private def createDirectories(ItPluginExtension extension) {
        def create = { path ->
            def dir = project.file(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }
        }
        create(extension.srcDir)
        create(extension.resourcesDir)

        if (extension.markAsTestSources) {

            def ideaModule = project.idea.module as IdeaModule
            ideaModule.testSourceDirs += project.file(extension.srcDir)
        }
    }

    private def configureSourceSet(pluginExtension extension) {
        def sourceSet = project.sourceSets[SOURCE_SET_NAME] as SourceSet

        def mainSourceSet = project.sourceSets[SourceSet.MAIN_SOURCE_SET_NAME] as SourceSet
        def testSourceSet = project.sourceSets[SourceSet.TEST_SOURCE_SET_NAME] as SourceSet

        sourceSet.compileClasspath += mainSourceSet.output + testSourceSet.compileClasspath + testSourceSet.output
        sourceSet.runtimeClasspath += mainSourceSet.output + testSourceSet.runtimeClasspath + testSourceSet.output

        sourceSet.java.setSrcDirs(project.files(extension.srcDir))
        sourceSet.resources.setSrcDirs(project.files(extension.resourcesDir))

    }

    private def configureConfiguration() {
        def sourceSet = project.sourceSets[SOURCE_SET_NAME] as SourceSet
        def testSourceSet = project.sourceSets[SourceSet.TEST_SOURCE_SET_NAME] as SourceSet

        project.configurations.getByName(sourceSet.compileConfigurationName)
                .extendsFrom(project.configurations.getByName(testSourceSet.compileConfigurationName))


        project.configurations.getByName(sourceSet.runtimeConfigurationName)
                .extendsFrom(project.configurations.getByName(testSourceSet.runtimeConfigurationName))

    }

    private def configureTask() {
        def task = project.tasks[ItTask.NAME] as ItTask

        task.dependsOn(project.tasks.findByName('itClasses'))

        def sourceSet = project.sourceSets[SOURCE_SET_NAME] as SourceSet
        task.classpath = sourceSet.runtimeClasspath
        task.testClassesDirs = sourceSet.output.classesDirs

    }

}
