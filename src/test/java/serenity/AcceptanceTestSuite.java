package serenity;

import logger.Log;
import manageTestCases.ZephyrCloudConnector;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.jbehave.SerenityStories;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.ScenarioType;
import utils.JsonUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @AfterScenario(uponType = ScenarioType.EXAMPLE)
    public void executeTestsAndMarkStatus() throws URISyntaxException {
        String connectionFlag = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.connectionFlag");
        Log.info("Connection flag for execute and mark testcases status is: "+connectionFlag);
        if(connectionFlag.toUpperCase().equals("YES"))
        {
            Log.info("<--------Start of marking the testcases status in Zephyr-------->");
            Map <String, String> metaDataMap = Serenity.getCurrentSession().getMetaData();
            List <String> testCaseNameList = new ArrayList<String>();
            List <String> testCaseIssueKeyList = new ArrayList<String>();
            for(Map.Entry<String, String> entry : metaDataMap.entrySet())
            {
               if(entry.getKey().equalsIgnoreCase("Tests"))
               {
                 testCaseNameList = Arrays.asList(entry.getValue().toString().split("\n"));
               }
            }
            for(int i = 0; i < testCaseNameList.size(); i++)
            {
              String [] issueKey = testCaseNameList.get(i).toString().split(":");
                testCaseIssueKeyList.add(issueKey[0]);
            }
            TestOutcome result = StepEventBus.getEventBus().getBaseStepListener().latestTestOutcome().get();
            String executionFlag = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.executionFlag");
            ZephyrCloudConnector zephyrCloudConnector = new ZephyrCloudConnector(testCaseIssueKeyList, result.isSuccess(), executionFlag);
            Log.info("<--------End of marking the testcases status in Zephyr-------->");
        }
    }

}
