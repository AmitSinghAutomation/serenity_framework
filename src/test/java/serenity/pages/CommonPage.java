package serenity.pages;

import logger.Log;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import utils.WebUtils;

import java.util.Random;

public class CommonPage extends PageObject {

    JavascriptExecutor js = (JavascriptExecutor) super.getDriver();
    Random random = new Random();

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

}
