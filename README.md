# Grails 7 - AssetProcessorService.grailsLinkGenerator is null

Minimal reproduction project for [asset-pipeline#444](https://github.com/wondrify/asset-pipeline/issues/444).

## Problem

When a Grails plugin registers a custom `grailsLinkGenerator` bean via `doWithSpring()`,
the `AssetProcessorService` from asset-pipeline does not get this bean injected. At runtime,
`grailsLinkGenerator` is `null` in `AssetProcessorService`, causing a `NullPointerException`
when `contextPath` is accessed.

This happens because `AssetProcessorService` uses a plain property (no `@Autowired` / `@Resource`)
and relies on Spring BY_NAME autowiring. However, the custom bean registered in the plugin descriptor
does not enable autowiring, so `AssetProcessorService` never receives the `grailsLinkGenerator`.

## Structure

- **my-plugin**: A Grails plugin that:
  - Registers a `CustomLinkGenerator` as `grailsLinkGenerator`
  - Contains views (GSP files) that use `<asset:stylesheet>`
  - Contains assets (CSS files)
- **my-app**: A Grails web application that:
  - Uses asset-pipeline
  - Depends on `my-plugin`
  - Has a controller that renders views from the plugin

## Reproduce the Bug

```bash
./gradlew my-app:bootRun
```

Open http://localhost:8080 → You should see a `NullPointerException` with:

```
Cannot get property 'contextPath' on null object
```

at `AssetProcessorService.assetBaseUrl()`.

The error occurs because:
1. The plugin provides views with `<asset:stylesheet src="application.css"/>` tags
2. The plugin provides the `application.css` asset
3. When rendering, asset-pipeline's `AssetProcessorService` tries to generate URLs
4. But `grailsLinkGenerator` is null because the plugin didn't set `bean.autowire = true`

## Fix A: `bean.autowire = true` on the custom grailsLinkGenerator

In `my-plugin/src/main/groovy/repro/plugin/MyPluginGrailsPlugin.groovy`, change:

```groovy
grailsLinkGenerator(CustomLinkGenerator, serverUrl)
```

to:

```groovy
grailsLinkGenerator(CustomLinkGenerator, serverUrl) { bean ->
    bean.autowire = true
}
```

## Fix B: Re-register AssetProcessorService with autowiring (workaround)

In `my-app/grails-app/conf/spring/resources.groovy`, uncomment:

```groovy
assetProcessorService(asset.pipeline.grails.AssetProcessorService) { bean ->
    bean.autowire = true
}
```

## Environment

- Grails 7.0.7
- asset-pipeline-grails (from grails-bom)
- Gradle 8.x
- Java 17+

