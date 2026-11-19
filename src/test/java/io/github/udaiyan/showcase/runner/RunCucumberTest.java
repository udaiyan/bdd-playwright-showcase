package io.github.udaiyan.showcase.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Single entry point. Tag filtering at runtime:
 *   mvn verify -Dcucumber.filter.tags="@smoke and not @wip"
 *   This is the JUnit Platform Suite runner that boots Cucumber
 *   inside Maven's Surefire plugin. It contains no test logic itself -
 *   it's pure configuration that tells
 *   JUnit how to discover and run my BDD scenarios.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.github.udaiyan.showcase")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json")
public class RunCucumberTest {
}
