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
        try
        {
            if(!System.getProperty("storyName").isEmpty())
            {
                findStoriesCalled(System.getProperty("storyName"));
            }
        }catch (Exception e)
        {
            System.out.println("Story file name not found running through meta filter only!:-->"+e.getMessage());
        }
    }

}
