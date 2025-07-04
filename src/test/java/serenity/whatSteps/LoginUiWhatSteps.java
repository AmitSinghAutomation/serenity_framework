package serenity.whatSteps;

import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Given;
import serenity.howSteps.LoginUiHowSteps;
import utils.JsonUtils;

public class LoginUiWhatSteps {

    JsonUtils jsonUtils = new JsonUtils();

    @Steps
    LoginUiHowSteps loginUiHowSteps;

    @Given("User launches the application with <keyForUrl>")
    public void launchApplication(String keyForUrl)
    {
      loginUiHowSteps.openApplication(keyForUrl);
    }

}
