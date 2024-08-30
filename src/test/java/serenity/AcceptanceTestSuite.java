package serenity;

import net.serenitybdd.jbehave.SerenityStories;
import org.jbehave.core.annotations.BeforeStories;
import utils.JsonUtils;

public class AcceptanceTestSuite extends SerenityStories {

    @BeforeStories
    public void initialize()
    {
        JsonUtils.loadEnvironmentProperties(System.getProperty("exeEnvironment"));
    }

    public AcceptanceTestSuite()
    {
        super();
        if(!System.getProperty("storyName").isEmpty())
        {
         findStoriesCalled(System.getProperty("storyName"));
        }
    }

}
