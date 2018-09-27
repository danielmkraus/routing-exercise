package org.danielmkraus.delivery.integration;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/resources/features"}, 
		plugin = {"pretty", 
				"html:target/reports/cucumber/html",
				"usage:target/usage.jsonx", 
				"junit:target/junit.xml"})
public class CucumberRunner {

}
