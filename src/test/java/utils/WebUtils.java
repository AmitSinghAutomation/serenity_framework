package utils;

import logger.Log;
import org.openqa.selenium.By;

public class WebUtils
{
    public static By returnBy(String locatorType, String locatorValue)
    {
        By by = null;
        switch (locatorType.toUpperCase())
        {
            case "XPATH":
                by = By.xpath(locatorValue);
                break;
            case "NAME":
                by = By.name(locatorValue);
                break;
            case "ID" :
                by = By.id(locatorValue);
                break;
            case "CSS" :
                by = By.cssSelector(locatorValue);
                break;
            case "TAG" :
                by = By.tagName(locatorValue);
                break;
            case "CLASS" :
                by = By.className(locatorValue);
                break;
            case "LINK" :
                by = By.linkText(locatorValue);
                break;
            case "PARTIAL" :
                by = By.partialLinkText(locatorValue);
                break;

        }
        return by;
    }

    public static By returnByBasedOnPageNameAndObjectName(String pageName, String objectName)
    {
       String locatorType = JsonUtils.getLocatorType(pageName,objectName);
       String locatorValue = JsonUtils.getLocatorValue(pageName,objectName);
       return WebUtils.returnBy(locatorType,locatorValue);
    }

    public static By returnByBasedOnPageNameAndObjectName(String pageName, String objectName, String... args)
    {
        String locatorType = JsonUtils.getLocatorType(pageName,objectName);
        String locatorValue = JsonUtils.getLocatorValue(pageName,objectName);
        String modifiedLocatorValue = "";
        if(args.length <= 1)
        {
          modifiedLocatorValue = locatorValue.replaceAll("%s",args[0]);
        }else
        {
            modifiedLocatorValue = String.format(locatorValue, (Object) args);
        }
        Log.info("Locator Value -------- "+modifiedLocatorValue);
        return WebUtils.returnBy(locatorType,modifiedLocatorValue);
    }
}
