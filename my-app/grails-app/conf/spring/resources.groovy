// Spring resources configuration
beans = {
    // WORKAROUND: Uncomment the following to fix the NPE in AssetProcessorService.
    // This forces autowiring on the AssetProcessorService bean so that
    // grailsLinkGenerator gets injected by name.
    //
    // assetProcessorService(asset.pipeline.grails.AssetProcessorService) { bean ->
    //     bean.autowire = true
    // }
}

