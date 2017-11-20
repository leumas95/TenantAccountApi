package windall.console.account.api

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(features = arrayOf("src/test/resources"))
class CucumberTests {
}