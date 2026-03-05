<!doctype html>
<html>
<head>
    <title>LinkGenerator Repro</title>
    <%-- This asset:stylesheet call triggers AssetProcessorService which needs grailsLinkGenerator --%>
    <asset:stylesheet src="application.css"/>
</head>
<body>
    <h1>Grails LinkGenerator Reproduction</h1>
    <p>If you see this page, the grailsLinkGenerator is correctly wired.</p>
    <p>If you get a NullPointerException on contextPath, the bug is reproduced.</p>
</body>
</html>

