package serenity.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class CommonMobilePage extends PageObject {
   @Managed(driver = "android")
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
        }else if (mobileExecutionType.toLowerCase().contentEquals("iosSimulator"))
        {
          File file = new File(this.jsonUtils.getValueFromSerenityproperties("appium.appFileLocation"));
          capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,this.jsonUtils.getValueFromSerenityproperties("appium.iosMobileName"));
          capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,this.jsonUtils.getValueFromSerenityproperties("appium.iosPlatformName"));
          capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,this.jsonUtils.getValueFromSerenityproperties("appium.iosPlatformVersion"));
          capabilities.setCapability(MobileCapabilityType.APP,file.getAbsolutePath());
          capabilities.setCapability(MobileCapabilityType.UDID,this.jsonUtils.getValueFromSerenityproperties("appium.iosDeviceName"));
          capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,this.jsonUtils.getValueFromSerenityproperties("appium.automationType"));
          capabilities.setCapability(IOSMobileCapabilityType.SIMPLE_ISVISIBLE_CHECK,true);
          capabilities.setCapability(MobileCapabilityType.NO_RESET,this.jsonUtils.getValueFromSerenityproperties("appium.noReset"));
          capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS,this.jsonUtils.getValueFromSerenityproperties("appium.autoAcceptAlerts"));
          capabilities.setCapability("appium:waitForIdleTimeout",this.jsonUtils.getValueFromSerenityproperties("appium.waitForIdleTimeout"));
          try {
              driver = new AppiumDriver<MobileElement>(new URL(appiumHub),capabilities);
          } catch (MalformedURLException e) {
              Log.info(e.getMessage());
          }
        }else if(mobileExecutionType.toLowerCase().contentEquals("iosRealDevice"))
        {
            File file = new File(this.jsonUtils.getValueFromSerenityproperties("appium.appFileLocation"));
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,this.jsonUtils.getValueFromSerenityproperties("appium.iosMobileName"));
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,this.jsonUtils.getValueFromSerenityproperties("appium.iosPlatformName"));
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,this.jsonUtils.getValueFromSerenityproperties("appium.iosPlatformVersion"));
            // comment following capability when you want to execute scripts by installing new app
            // And comment out get Absolute Path capability
            capabilities.setCapability(MobileCapabilityType.APP,file.getAbsolutePath());
            capabilities.setCapability(MobileCapabilityType.UDID,this.jsonUtils.getValueFromSerenityproperties("appium.iosDeviceName"));
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,this.jsonUtils.getValueFromSerenityproperties("appium.automationType"));
            // Uncomment following capability when you want to execute scripts on already installed app
            // And comment out get Absolute Path capability
            capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID,this.jsonUtils.getValueFromSerenityproperties("appium.iosBundleId"));
            capabilities.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID,this.jsonUtils.getValueFromSerenityproperties("appium.iosXCodeOrgId"));
            capabilities.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID,this.jsonUtils.getValueFromSerenityproperties("appium.iosXCodeSigningId"));
            capabilities.setCapability(IOSMobileCapabilityType.UPDATE_WDA_BUNDLEID,this.jsonUtils.getValueFromSerenityproperties("appium.iosUpdatedWdaBundleId"));
            capabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG,this.jsonUtils.getValueFromSerenityproperties("appium.showIosLog"));
            capabilities.setCapability("appium:settings[snapshotMaxDepth]",62);
            capabilities.setCapability(MobileCapabilityType.FULL_RESET,this.jsonUtils.getValueFromSerenityproperties("appium.fullReset"));
            capabilities.setCapability(MobileCapabilityType.NO_RESET,this.jsonUtils.getValueFromSerenityproperties("appium.noReset"));
            capabilities.setCapability(IOSMobileCapabilityType.SIMPLE_ISVISIBLE_CHECK,true);
            capabilities.setCapability(IOSMobileCapabilityType.WDA_STARTUP_RETRIES,3);
            capabilities.setCapability(IOSMobileCapabilityType.WDA_LAUNCH_TIMEOUT,70000);
            try {
                driver = new AppiumDriver<MobileElement>(new URL(appiumHub),capabilities);
            } catch (MalformedURLException e) {
                Log.info(e.getMessage());
            }
        }
    }

    public AppiumDriver<MobileElement> getAppiumDriver()
    {
        return driver;
    }
}
