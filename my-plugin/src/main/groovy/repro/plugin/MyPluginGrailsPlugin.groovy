package repro.plugin

import grails.plugins.Plugin
import org.grails.web.mapping.CachingLinkGenerator

/**
 * Grails plugin that registers a custom grailsLinkGenerator bean.
 * This reproduces the issue where AssetProcessorService.grailsLinkGenerator
 * is null at runtime in Grails 7 when a plugin overrides this bean
 * without setting bean.autowire = true.
 */
class MyPluginGrailsPlugin extends Plugin {

    def grailsVersion = "7.0 > *"
    def title = "My Plugin"
    def description = "Reproduces grailsLinkGenerator autowiring issue"

    Closure doWithSpring() {
        { ->
            def serverUrl = grailsApplication.config.getProperty('grails.serverURL', String, '')

            // Custom grailsLinkGenerator bean WITHOUT bean.autowire = true
            // This causes AssetProcessorService.grailsLinkGenerator to be null
            // because the AssetProcessorService relies on BY_NAME autowiring.
            // Views and assets are now in the plugin, which triggers the NPE
            grailsLinkGenerator(CustomLinkGenerator, serverUrl)

            // FIX: Uncomment the following block and comment out the line above
            // to demonstrate that adding bean.autowire = true resolves the issue.
            //
            // grailsLinkGenerator(CustomLinkGenerator, serverUrl) { bean ->
            //     bean.autowire = true
            // }
        }
    }
}

