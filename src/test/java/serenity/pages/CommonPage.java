package serenity.pages;

import logger.Log;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WebUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CommonPage extends PageObject {

    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    JavascriptExecutor js = (JavascriptExecutor) super.getDriver();
    Random random = new Random();
    Robot robot = new Robot();
    Actions actions = new Actions(getDriver());
    ArrayList<String> tabs;
    Set<String> windows;
    List<WebElement> elementList;
    WebDriverWait webDriverWait;
    String filePath,imagePath = null;
    boolean result;

    public CommonPage() throws AWTException {
    }

    public boolean isElementVisible(String pageName, String locatorName, String stringToBeReplaced1, String stringToBeReplaced2)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName,locatorName,stringToBeReplaced1,stringToBeReplaced2);
        Log.info("Locator fetched: "+object);
        return super.element(object).isVisible();
    }

    public boolean isElementVisible(String pageName,String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName,locatorName);
        try{
          super.element(object).waitUntilVisible();
          Log.info("Element visible: "+object);
          return super.element(object).isVisible();
        }catch (Exception e){
            super.element(object).waitUntilVisible();
            Log.info("Element visible: "+object);
            return super.element(object).isVisible();
        }
    }

    public String getObjectText(String pageName,String locatorName)
    {
        try {
            By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
            return super.element(object).getText();
        }catch (Exception e)
        {
            By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
            return super.element(object).getText();
        }
    }

    public String getObjectText(String pageName, String locatorName, String strToBeReplaced)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName, strToBeReplaced);
        return super.element(object).getText();
    }

    public void clickOnObject(String pageName, String locatorName)
    {
        try {
            Log.info(pageName + " " + locatorName);
            By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
            super.element(object).waitUntilEnabled();
            Log.info("Element enabled: "+locatorName+" text "+ super.element(object).getAttribute("outerHTML"));
            super.element(object).click();
        }catch (Exception e)
        {
            By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
            super.element(object).waitUntilEnabled();
            js.executeScript("arguments[0].click()",super.element(object));
        }
    }

    public int generateRandomNumber()
    {
        return random.nextInt(100000);
    }

    public boolean isElementPresent(String pageName, String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        try
        {
            super.element(object).waitUntilPresent();
            return super.element(object).isPresent();
        }catch (Exception e)
        {
            super.element(object).waitUntilPresent();
            return super.element(object).isPresent();
        }
    }

    public boolean isElementPresent(String pageName, String locatorName, String[] stringToBeReplaced)
    {
        By object;
        if(stringToBeReplaced.length == 0)
        {
            object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        }else
        {
            object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName,locatorName,stringToBeReplaced);
        }
        try
        {
            super.element(object).waitUntilPresent();
            return super.element(object).isPresent();
        }catch (Exception e)
        {
            super.element(object).waitUntilPresent();
            return super.element(object).isPresent();
        }
    }

    public void enterData(String pageName,String locatorName,String value)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        super.element(object).waitUntilEnabled();
        super.element(object).clear();
        super.element(object).type(value);
    }

    public boolean isElementEnabled(String pageName, String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        super.element(object).waitUntilVisible();
        Log.info("Status "+String.valueOf(super.element(object).isEnabled()));
        return super.element(object).isEnabled();
    }

    public void refresh()
    {
        super.getDriver().navigate().refresh();
    }

    public int getTotalElementCount(String pageName, String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        return super.getDriver().findElements(object).size();
    }

    public String getObjectValueFromAttribute(String pageName, String locatorName, String attribute)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        return super.element(object).getAttribute(attribute);
    }

    public void maximize()
    {
        super.getDriver().manage().window().maximize();
    }

    public void navigateBack()
    {
        super.getDriver().navigate().back();
    }

    public void navigateForward()
    {
        super.getDriver().navigate().forward();
    }

    public void scrollDown()
    {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_END);
            robot.keyRelease(KeyEvent.VK_END);
            robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void enterKeySelectElement()
    {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void closePopUp()
    {
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
    }

    public String getObjectValueFromCss(String pageName, String locatorName, String cssValue)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        return super.element(object).getCssValue(cssValue);
    }

    public String getFormEnteredValue(String pageName, String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        return super.element(object).getValue();
    }

    public String getAllTextHiddenToo(String pageName, String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        return super.element(object).getTextContent();
    }

    public void hoverOnObject(String pageName, String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        actions.moveToElement(super.element(object)).perform();
    }

    public void rightClickOnObject(String pageName, String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        actions.contextClick(super.element(object)).perform();
    }

    public void clearAnElement(String pageName,String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        super.element(object).sendKeys(Keys.ENTER);
        super.element(object).sendKeys(Keys.CONTROL+"A");
        super.element(object).sendKeys(Keys.BACK_SPACE);
    }

    public void scrollUp()
    {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_HOME);
        robot.keyRelease(KeyEvent.VK_HOME);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void openBrowserNewTab()
    {
        js.executeScript("window.open('url','_blank');");
        tabs = new ArrayList<String>(super.getDriver().getWindowHandles());
        super.getDriver().switchTo().window(tabs.get(1));
    }

    public void switchToFrame(String locatorName)
    {
        super.getDriver().switchTo().frame(locatorName);
    }

    public void closeTab()
    {
        super.getDriver().close();
    }

    public void switchBackToDefaultWindow()
    {
        super.getDriver().switchTo().window(tabs.get(0));
    }

    public void switchToSpecificTab(String value)
    {
       windows = super.getDriver().getWindowHandles();
       if(!windows.isEmpty())
       {
         for(String windowId : windows)
         {
             String windowTitle = super.getDriver().switchTo().window(windowId).getTitle();
             if(windowTitle.contains(value))
             {
                 super.getDriver().switchTo().window(windowId);
             }
         }
       }
    }

    public void pasteAnElement(String pageName,String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        super.element(object).sendKeys(Keys.ENTER);
        super.element(object).sendKeys(Keys.CONTROL+"A");
        super.element(object).sendKeys(Keys.BACK_SPACE);
        super.element(object).sendKeys(Keys.CONTROL+"V");
    }

    public void enterKeys(String pageName,String locatorName,String input) {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        switch (input) {
            case "ENTER":
                super.element(object).sendKeys(Keys.ENTER);
                break;
            case "SPACE":
                super.element(object).sendKeys(Keys.SPACE);
                break;
            case "ESCAPE":
                super.element(object).sendKeys(Keys.ESCAPE);
                break;
            case "DOWN_KEY":
                super.element(object).sendKeys(Keys.ARROW_DOWN);
                break;
            case "UP_KEY":
                super.element(object).sendKeys(Keys.ARROW_UP);
                break;
            default:
                Log.info("Not a valid key as per condition");
        }
    }

    public List<WebElement> returnListOfElement(String pageName, String locatorName)
    {
            By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
            elementList = super.getDriver().findElements(object);
            return elementList;
    }

    public boolean waitForElementUntilInvisible(String pageName, String locatorName, long seconds, String [] strToBeReplaced)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        webDriverWait = new WebDriverWait(super.getDriver(),seconds);
        return webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(object));
    }

    public void dragAndDropElement(String pageName, String sourceObject, String targetObject)
    {
        By source = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, sourceObject);
        By target = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, targetObject);
        actions.clickAndHold(super.element(source)).moveToElement(super.element(target)).pause(Duration.ofSeconds(1)).release().build().perform();
    }

    public void scrollToElement(String pageName, String locatorName)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        WebElement webElement = super.getDriver().findElement(object);
        js.executeScript("arguments[0].scrollIntoView(true);",webElement);
        Log.info("Element found: "+webElement);
    }

    public void scrollToElement(String pageName, String locatorName, String strToBeReplaced)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName,strToBeReplaced);
        WebElement webElement = super.getDriver().findElement(object);
        js.executeScript("arguments[0].scrollIntoView(true);",webElement);
        Log.info("Element found: "+webElement);
    }

    public void checkElementState(String pageName, String locatorName, String key)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);

        switch (key.toUpperCase())
        {
            case "ENABLED":
                result = super.element(object).isEnabled();
                Log.info("Element is enabled: "+result);
                break;
            case "DISABLED":
                result = !super.element(object).isEnabled();
                Log.info("Element is disabled: "+result);
                break;
            case "VISIBLE":
                result = super.element(object).isVisible();
                Log.info("Element is visible: "+result);
                break;
            case "NOT_VISIBLE":
                result = !super.element(object).isVisible();
                Log.info("Element is not visible: "+result);
                break;
            case "PRESENT":
                result = super.element(object).isPresent();
                Log.info("Element is present: "+result);
                break;
            case "NOT_PRESENT":
                result = !super.element(object).isPresent();
                Log.info("Element is not present: "+result);
                break;
            default:
                Log.info("Invalid key provided: "+key);
        }
    }

    public void checkDriverElementState(String pageName, String locatorName, String key)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);

        switch (key.toUpperCase())
        {
            case "ENABLED":
                result = super.getDriver().findElement(object).isEnabled();
                Log.info("Element is enabled: "+result);
                break;
            case "DISABLED":
                result = !super.getDriver().findElement(object).isEnabled();
                Log.info("Element is disabled: "+result);
                break;
            case "VISIBLE":
                result = super.getDriver().findElement(object).isDisplayed();
                Log.info("Element is visible: "+result);
                break;
            case "NOT_VISIBLE":
                result = !super.getDriver().findElement(object).isDisplayed();
                Log.info("Element is not visible: "+result);
                break;
            case "PRESENT":
                try {
                    result = super.getDriver().findElement(object) != null;
                }catch (NoSuchElementException e)
                {
                  result = false;
                }
                Log.info("Element is present: "+result);
                break;
            case "NOT_PRESENT":
                try {
                    result = super.getDriver().findElement(object) == null;
                }catch (NoSuchElementException e)
                {
                    result = false;
                }
                Log.info("Element is not present: "+result);
                break;
            default:
                Log.info("Invalid key provided: "+key);
        }
    }

    public void uploadFile(String pageName, String locatorName, String type)
    {
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
              return;
      }
      imagePath = Paths.get(filePath).toAbsolutePath().toString();
      Log.info("Uploading the file...");
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        super.element(object).sendKeys(imagePath);
        Log.info("File Uploaded!!!");
    }

    public void dragAndDropElementByOffset(String pageName, String locatorName, int offset)
    {
        By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
        actions.clickAndHold(super.element(object)).moveByOffset(offset,0).release().build().perform();
    }
    public void clickOnObjectRandomError(String pageName, String locatorName)
    {
       try {
           Log.info("Checking for random error pop up on page:"+pageName+", object: "+locatorName);
           By randomPopUpLocator = By.xpath("//*[contains(text(),'Random error comes')]");
           webDriverWait = new WebDriverWait(super.getDriver(),10);
           boolean isErrorVisible = webDriverWait.until(webDriver -> {
               elementList = webDriver.findElements(randomPopUpLocator);
               return elementList.stream().anyMatch(WebElement :: isDisplayed);
           });
           if(isErrorVisible)
           {
               Log.info("Random error pop up found. Attempting to click on object: "+locatorName);
               By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
               super.element(object).waitUntilEnabled();

               Log.info("Element enabled: "+ locatorName +" | HTML: " +super.element(object).getAttribute("outerHTML"));
               super.element(object).click();

           }else
           {
               Log.info("Random error pop up screen do not come. Hence skipping the click action for: "+locatorName);
           }
       }catch (Exception e)
       {
           Log.error("Exception occurred while handling the random error pop up and clicking object: "+e.getMessage());
           try {
               By object = WebUtils.returnByBasedOnPageNameAndObjectName(pageName, locatorName);
               js.executeScript("arguments[0].click()",super.element(object));
           }catch (Exception jse)
           {
              Log.error("Java script executor click also failed: "+ jse.getMessage());
           }
       }
    }


}
