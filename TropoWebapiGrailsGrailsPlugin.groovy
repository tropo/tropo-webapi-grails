class TropoWebapiGrailsGrailsPlugin {
    // the plugin version
    def version = "0.1.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.6 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Martin Perez"
    def authorEmail = "mpermar@gmail.com"
    def title = "Plugin to interact with the Tropo Cloud platform"
    def description = '''\\
The Tropo Cloud platform allows to create voice and SMS powered applications very easily. 

This plugin implements the Tropo WebApi and allows people to create with very few lines of code applications that 
can send and receive SMSs and calls, record conversations, send MP3s to other people and many other cool things. 

The plugin also implements a client for the Tropo REST API which allows us to configure our Tropo applications.  
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/tropo-webapi-grails"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
