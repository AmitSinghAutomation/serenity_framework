package serenity.howSteps;

import net.thucydides.core.annotations.Managed;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import serenity.pages.LoginPage;
import utils.JsonUtils;

public class LoginUiHowSteps extends PageFactory {

    JsonUtils jsonUtils = new JsonUtils();

    @Managed
    public WebDriver webDriver;

    LoginPage loginPage = PageFactory.initElements(webDriver, LoginPage.class);

    public void openApplication(String keyForUrl)
    {
        loginPage.launchApplication(System.getProperty(keyForUrl));
    }

}
