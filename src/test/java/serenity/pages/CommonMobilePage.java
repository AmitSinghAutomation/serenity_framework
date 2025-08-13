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
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CommonMobilePage extends PageObject {
   @Managed(driver = "android")
   AppiumDriver<MobileElement> driver;
    DesiredCapabilities capabilities;
    JsonUtils jsonUtils = new JsonUtils();
    public static AppiumDriverLocalService appiumService;
    File file;
    Process process;
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

    public void switchView(String input)
    {
        if(mobileExecutionType.toLowerCase().contentEquals("android"))
        {
            java.util.Set<String> contextHandles = null;
            if(input.contains("Web"))
            {
               do {
                   contextHandles = driver.getContextHandles();
                   Log.info("Context handles from mobile: "+contextHandles.toString()+"Size: "+contextHandles.size());
                   if(super.element(By.id("com.android.chrome:id/signin_fre_dismiss_button")).isPresent()){
                       int retryCount = 2;
                       while(retryCount > 0){
                           try {
                               driver.findElement(By.id("com.android.chrome:id/signin_fre_dismiss_button")).click();
                               break;
                           }catch (StaleElementReferenceException e){
                               retryCount--;
                               driver.findElement(By.id("com.android.chrome:id/signin_fre_dismiss_button")).click();
                           }
                       }
                   }
               }while((contextHandles.toString().contains("WEBVIEW_chrome")));
               driver.context("WEBVIEW_chrome");
               driver.manage().deleteAllCookies();
            }else{
                driver.context("NATIVE_APP");
            }
        }
    }

    public void switchToMobileApp(){
        driver.context("NATIVE_APP");
        driver.activateApp("com.company.package");
    }

    public void uploadFile(String objectName,String pageName, String type){
        String filePath = null;
        String deviceFilePath = null;
        switch(type.toUpperCase())
        {
            case "PNG":
                filePath = environmentVariables.getProperty("image.png");
                break;
            case "JPG":
                filePath = environmentVariables.getProperty("image.jpg");
                break;
            case "SVG":
                filePath = environmentVariables.getProperty("image.svg");
                break;
            case "EXCEL":
                filePath = environmentVariables.getProperty("image.excel");
                break;
            case "DOC":
                filePath = environmentVariables.getProperty("image.doc");
                break;
            case "JPEG":
                filePath = environmentVariables.getProperty("image.jpeg");
                break;
            case "TXT":
                filePath = environmentVariables.getProperty("image.txt");
                break;
            case "GIF":
                filePath = environmentVariables.getProperty("image.gif");
                break;
            case "PDF":
                filePath = environmentVariables.getProperty("image.pdf");
                break;
            default:
                Log.info("Unsupported file type");
        }
        file = new File(filePath);
        if(!file.exists())
        {
            Log.error("File does not exist: "+filePath);
        }
        deviceFilePath = "/sdcard/Download/" + file.getName();
        Log.info("File upload path: "+deviceFilePath);
        try {
            String [] pushCmd = {"adb","push",filePath,deviceFilePath};
            process = Runtime.getRuntime().exec(pushCmd);
            int pushExitCode = process.waitFor();
            if(pushExitCode == 0)
            {
                Log.info("File uploaded successfully: "+deviceFilePath);
                String [] scanCmd = {"adb","shell","am","broadcast",
                        "-a","android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                        "-d","file://" + deviceFilePath};
                process = Runtime.getRuntime().exec(scanCmd);
                int scanExitCode = process.waitFor();
                if(scanExitCode == 0)
                {
                    Log.info("Media scanner notified successfully for: "+deviceFilePath);
                }else{
                    Log.error("Failed to notify media scanner. Exit code: " + scanExitCode);
                }
            }else{
                Log.error("Failed to push file. Exit Code: " + pushExitCode);
            }
        } catch (Exception e) {
            Log.error("Error during file push or media scan notification: " + e.getMessage());
        }
    }

    public void removeFile(String objectName,String pageName, String type){
        String filePath = null;
        String deviceFilePath = null;
        switch(type.toUpperCase())
        {
            case "PNG":
                filePath = environmentVariables.getProperty("image.png");
                break;
            case "JPG":
                filePath = environmentVariables.getProperty("image.jpg");
                break;
            case "SVG":
                filePath = environmentVariables.getProperty("image.svg");
                break;
            case "EXCEL":
                filePath = environmentVariables.getProperty("image.excel");
                break;
            case "DOC":
                filePath = environmentVariables.getProperty("image.doc");
                break;
            case "JPEG":
                filePath = environmentVariables.getProperty("image.jpeg");
                break;
            case "TXT":
                filePath = environmentVariables.getProperty("image.txt");
                break;
            case "GIF":
                filePath = environmentVariables.getProperty("image.gif");
                break;
            case "PDF":
                filePath = environmentVariables.getProperty("image.pdf");
                break;
            default:
                Log.info("Unsupported file type");
        }
        file = new File(filePath);
        if(!file.exists())
        {
            Log.error("File does not exist: "+filePath);
        }
        deviceFilePath = "/sdcard/Download/" + file.getName();
        Log.info("File to remove from device path: "+deviceFilePath);
        try {
            String [] rmCmd = {"adb","shell","rm",deviceFilePath};
            process = Runtime.getRuntime().exec(rmCmd);
            int rmExitCode = process.waitFor();
            if(rmExitCode == 0)
            {
                Log.info("File removed successfully from path: "+deviceFilePath);
            }else{
                Log.error("Failed to remove file. Exit Code: " + rmExitCode);
            }
        } catch (Exception e) {
            Log.error("Error during file removal: " + e.getMessage());
        }
    }
}
