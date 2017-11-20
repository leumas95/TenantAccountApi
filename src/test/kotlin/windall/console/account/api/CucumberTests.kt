package windall.console.account.api

import org.junit.runner.RunWith
import cucumber.api.junit.Cucumber
import cucumber.api.CucumberOptions

@RunWith(Cucumber::class)
@CucumberOptions(features = arrayOf("src/test/resources"))
class CucumberTests {
}