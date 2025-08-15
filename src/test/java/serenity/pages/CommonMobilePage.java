package serenity.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import logger.Log;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.JsonUtils;
import utils.WebUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class CommonMobilePage extends PageObject {
   @Managed(driver = "android")
   AppiumDriver<MobileElement> driver;
    DesiredCapabilities capabilities;
    JsonUtils jsonUtils = new JsonUtils();
    public static AppiumDriverLocalService appiumService;
    File file;
    Process process;
    List<MobileElement> elementList;
    List<String> actualList, sortedList;
    String parent, child;
    Set<String> windowIds;
    Iterator<String> stringIterator;
    Actions actions;
    TouchAction touchAction;
    private int startX;
    private int startY;
    private int endY;
    private int edgeScrollIteration;
    private double edgeScroll1,edgeScroll2;
    private int maxScrollIteration;
    private double scroll1,scroll2;
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

    public boolean verifySorting(String objectName, String pageName, String sortType){
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, objectName);
        elementList = driver.findElements(object);
        actualList = new ArrayList<>();
        for(WebElement element : elementList){
            String text = element.getText();
            if((!text.equals("-")) && (!text.isEmpty()) && (!text.equals("YOU")))
            {
                actualList.add(text);
            }
        }
        sortedList = new ArrayList<>(actualList);
        if(sortType.equalsIgnoreCase("ascending")){
            Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
        }else if(sortType.equalsIgnoreCase("descending")){
            Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
            Collections.reverse(sortedList);
        }else {
            return false;
        }
        boolean isSorted = sortedList.equals(actualList);
        Log.info("Sorted in " +sortType+ " order: "+sortedList);
        return isSorted;
    }

    public void scrollToElementAndroid()
    {
        ((AndroidDriver<MobileElement>)driver).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                        + "new UiSelector().text(\"ElementTextToScrollTo\"))");

    }

    public void scrollOutside(){
        Dimension size = driver.manage().window().getSize();

        int startX = (int) (size.width * 0.02); // 2 % from the left edge
        int startY = (int) (size.height * 0.6); // Start from 60% of the screen
        int endY =  (int) (size.height * 0.2); // Move to 20% (scrolling upward)

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH,"finger");

        Sequence scroll = new Sequence(finger,1)
                .addAction(finger.createPointerMove(Duration.ZERO,PointerInput.Origin.viewport(),startX,startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg())) // Press down
                .addAction(finger.createPointerMove(Duration.ofSeconds(1),PointerInput.Origin.viewport(),startX,endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg())); // Lift finger
        driver.perform(Arrays.asList(scroll));
    }

    public void switchToWindowHandles(){
        parent = driver.getWindowHandle();
        windowIds = driver.getWindowHandles();
        stringIterator = windowIds.iterator();
        while (stringIterator.hasNext())
        {
            child = stringIterator.next();
            if(!child.equals(parent))
            {
                driver.switchTo().window(child);
                System.out.println(driver.switchTo().window(child).getTitle());
            }
        }
    }

    public void softKeyboardHideAndroid(){
        ((AndroidDriver<MobileElement>)driver).pressKey(new KeyEvent(AndroidKey.ENTER));
    }

    public void pressAndHold(String pageName, String locatorName){
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        actions = new Actions(driver);
        actions.moveToElement(driver.findElement(object))
                .clickAndHold()
                .pause(Duration.ofSeconds(2))
                .release().build().perform();
    }

    public void setScrollingParametersToAvoidEdgeOfElement(int maxIterations, double percentage){
        edgeScrollIteration = maxIterations;
        edgeScroll1 = 0.2;
        edgeScroll2 = edgeScroll1 +(percentage/100);
    }

    public void setCoordinatesForScroll(double start, double end){
        Dimension size = driver.manage().window().getSize();

        startX = (int) (size.width / 2); // 2 % from the left edge
        startY = (int) (size.height * start); // Start from 60% of the screen
        endY =  (int) (size.height * end); // Move to 20% (scrolling upward)
    }

    public void scrollAtLastToAvoidEdgeOfElement(String direction){
        if(direction.equalsIgnoreCase("down")){
           setCoordinatesForScroll(edgeScroll2,edgeScroll1);
        }else if(direction.equalsIgnoreCase("up")){
            setCoordinatesForScroll(edgeScroll1,edgeScroll2);
        }else{
            Log.info("Invalid direction for scroll");
        }
        for(int i = 0; i < edgeScrollIteration; i++){
           mobileScroll(startX,startY,endY);
        }
        setScrollingParametersToAvoidEdgeOfElement(1,10);
    }

    public void mobileScroll(int startX, int startY, int endY){
        touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(startX,startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(startX,endY)).release().perform();
    }

    public boolean scrollUntilElement(String direction, String objectName, String pageName, String [] stringToBeReplaceFromLocator){
        By object;

        if(direction.equalsIgnoreCase("down")){
            setCoordinatesForScroll(edgeScroll2,edgeScroll1);
        }else if(direction.equalsIgnoreCase("up")){
            setCoordinatesForScroll(edgeScroll1,edgeScroll2);
        }else{
            Log.info("Invalid direction for scroll");
        }
        boolean flag = true;
        touchAction = new TouchAction(driver);

        if(stringToBeReplaceFromLocator.length == 0) {
            object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, objectName);
        }else{
            object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, objectName,stringToBeReplaceFromLocator);
        }

        int counter = 0;
        while(counter <= maxScrollIteration){
            try {
                Log.info("Finding Element");
                if(mobileExecutionType.toLowerCase().contentEquals("ios")){
                    if(driver.findElement(object).getAttribute("visible").equalsIgnoreCase("false")){
                        throw new org.openqa.selenium.NoSuchElementException("");
                    }
                    if(counter != 0){
                        scrollAtLastToAvoidEdgeOfElement(direction);
                    }
                    break;
                }else{
                    driver.findElement(object);
                    Log.info("scrolled till particular element");
                    if(counter != 0){
                        scrollAtLastToAvoidEdgeOfElement(direction);
                    }
                    break;
                }
            }catch (org.openqa.selenium.NoSuchElementException e){
                counter++;
                switch (direction.toUpperCase()){
                    case "UP":
                        Log.info("Scrolling UP to find element");
                        mobileScroll(startX,startY,endY);
                        break;
                    case "DOWN":
                        Log.info("Scrolling DOWN to find element");
                        mobileScroll(startX,startY,endY);
                        break;
                }
            }
            if(counter >= maxScrollIteration){
                flag = false;
            }
        }
        setScrollingParameters(70,20);
        return flag;
    }

    public void setScrollingParameters(int maxIterations, double percentage){
        maxScrollIteration = maxIterations;
        scroll1 = 0.2;
        scroll2 = scroll1 + (percentage/100);
    }
}
