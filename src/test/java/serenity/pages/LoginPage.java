package serenity.pages;

import logger.Log;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.Dimension;

public class LoginPage extends PageObject {

    private String browserResolution = System.getProperty("resolution");
    private int width, height;

    public void launchApplication(String Url)
    {
        try{

            super.getDriver().manage().deleteAllCookies();
            if(browserResolution != null)
            {
                String [] parts = browserResolution.split(",");
                width = Integer.parseInt(parts[0].trim());
                Log.info("Width given from maven command line "+width);
                height = Integer.parseInt(parts[1].trim());
                Log.info("Height given from maven command line "+height);
                super.getDriver().manage().window().setSize(new Dimension(width,height));
            }else
            {
                super.getDriver().manage().window().maximize();
            }
            super.getDriver().navigate().to(Url);
            Log.info("Current browser resolution is: "+ super.getDriver().manage().window().getSize().getWidth() + "*" + super.getDriver().manage().window().getSize().getHeight());

        }catch (Exception e)
        {
            Log.error("Error occurred while launching the browser: "+ e.getMessage());
        }
    }

}
