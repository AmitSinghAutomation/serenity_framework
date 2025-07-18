package serenity.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import logger.Log;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.JsonUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class CommonMobilePage extends PageObject {
    @Managed(driver = "appium")
    AppiumDriver<MobileElement> driver;
    DesiredCapabilities capabilities;
    JsonUtils jsonUtils = new JsonUtils();
    public static AppiumDriverLocalService appiumService;
    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    public String mobileExecutionType = environmentVariables.getProperty("ExecutionType");
    public String appiumHub = environmentVariables.getProperty("appium.hub");
    public String androidId = environmentVariables.getProperty("appium.deviceName");
    public String androidVersion = environmentVariables.getProperty("appium.platformVersion");
    public String androidPlatformName = environmentVariables.getProperty("appium.platformName");
    public String appAndroidPackage = environmentVariables.getProperty("appium.appPackage");
    public String appAndroidActivity = environmentVariables.getProperty("appium.appActivity");
    public String noResetAndroid = environmentVariables.getProperty("appium.noReset");
    public String androidAutoName = environmentVariables.getProperty("appium.automationName");
    String androidCommandTimeOut = environmentVariables.getProperty("appium.newCommandTimeOut");

    public void setMobileCapabilities()
    {
        if(mobileExecutionType.toLowerCase().contentEquals("android"))
        {
            Log.info("Setting capabilities for: "+mobileExecutionType);
            //appiumService.start();
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName","My Android Phone");
            capabilities.setCapability("udid",androidId);
            capabilities.setCapability("platformVersion",androidVersion);
            capabilities.setCapability("platformName",androidPlatformName);
            capabilities.setCapability("appPackage",appAndroidPackage);
            capabilities.setCapability("appActivity",appAndroidActivity);
            capabilities.setCapability("noReset",noResetAndroid);
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,androidAutoName);
            capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,androidCommandTimeOut);
            capabilities.setCapability("appium:waitForIdleTimeout",0);
            capabilities.setCapability("autoGrantPermissions",this.jsonUtils.getValueFromSerenityproperties("appium.autoGrantPermission"));
            capabilities.setCapability("unicodeKeyboard",this.jsonUtils.getValueFromSerenityproperties("appium.unicodeKeyboard"));
            capabilities.setCapability("resetKeyboard",this.jsonUtils.getValueFromSerenityproperties("appium.resetKeyboard"));
            capabilities.setCapability("fullReset",this.jsonUtils.getValueFromSerenityproperties("appium.fullReset"));
            try {
                driver = new AndroidDriver<MobileElement>(new URL(appiumHub),capabilities);
                //driver = new AppiumDriver<MobileElement>(appiumService.getUrl(),capabilities);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
