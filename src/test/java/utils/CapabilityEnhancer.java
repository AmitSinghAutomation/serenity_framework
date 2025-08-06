package utils;

import net.serenitybdd.core.webdriver.enhancers.BeforeAWebdriverScenario;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import net.thucydides.core.webdriver.SupportedWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.file.FileSystems;
import java.util.HashMap;

public class CapabilityEnhancer implements BeforeAWebdriverScenario {
    @Override
    public DesiredCapabilities apply(EnvironmentVariables environmentVariables, SupportedWebDriver supportedWebDriver, TestOutcome testOutcome, DesiredCapabilities desiredCapabilities) {
        ChromeOptions options = new ChromeOptions();
        HashMap<String,Object> prefs = new HashMap<String,Object>();
        prefs.put("download.default_directory", FileSystems.getDefault().getPath(SystemEnvironmentVariables.createEnvironmentVariables().getProperty("chrome_preferences.download.default_directory")).normalize().toAbsolutePath().toAbsolutePath());
        options.setExperimentalOption("prefs",prefs);
        //options.setHeadless(false);
        options.addArguments("disable-popup-blocking");
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY,options);
        return desiredCapabilities;
    }
}
