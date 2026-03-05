package repro.plugin

import org.grails.web.mapping.CachingLinkGenerator

/**
 * Custom LinkGenerator that extends CachingLinkGenerator.
 * In a real project this might add database-based URL resolution.
 * For reproduction purposes, it simply delegates to the parent.
 */
class CustomLinkGenerator extends CachingLinkGenerator {

    CustomLinkGenerator(String serverBaseURL) {
        super(serverBaseURL)
    }
}

