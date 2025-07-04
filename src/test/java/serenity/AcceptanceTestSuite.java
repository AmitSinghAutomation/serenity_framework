package serenity;

import logger.Log;
import manageTestCases.JiraDefectCreator;
import manageTestCases.ZephyrCloudConnector;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.jbehave.SerenityStories;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.ScenarioType;
import utils.CommonUtils;
import utils.JsonUtils;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AcceptanceTestSuite extends SerenityStories {

    ZephyrCloudConnector zephyrCloudConnector;
    JiraDefectCreator jiraDefectCreator;
    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    @BeforeStories
    public void initialize()
    {
        if(environmentVariables.getProperty("environmentData.createFile").toUpperCase().equals("YES"))
        {
           JsonUtils.createEnvironmentJson();
        }
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

        try {
            EnvironmentVariables envVars = SystemEnvironmentVariables.createEnvironmentVariables();
            TestOutcome latestTestOutCome = StepEventBus.getEventBus().getBaseStepListener().latestTestOutcome().orElse(null);
            if ("YES".equalsIgnoreCase(envVars.getProperty("zephyr.connectionFlag"))) {
                Log.info("<--------Start of marking the testcases status in Zephyr-------->");
                Map<String, String> metaDataMap = Serenity.getCurrentSession().getMetaData();
                Log.info("Meta Data ---->" + metaDataMap);
                List<String> testCaseNameList = CommonUtils.getTestCaseList(metaDataMap);
                List<String> testCaseIssueKeyList = CommonUtils.getIssueKeyList(testCaseNameList);
                String executionFlag = envVars.getProperty("zephyr.executionFlag");
                if (latestTestOutCome != null) {
                    zephyrCloudConnector = new ZephyrCloudConnector(testCaseIssueKeyList, latestTestOutCome.isSuccess(), executionFlag);
                    CommonUtils.generateAppInsightData(testCaseNameList, latestTestOutCome);
                }
                Log.info("<--------End of marking the testcases status in Zephyr-------->");
            }
            if (latestTestOutCome != null && !latestTestOutCome.isSuccess()) {
                CommonUtils.setFailedTestCasesAuthorDetails(latestTestOutCome);
            }
            if ("YES".equalsIgnoreCase(envVars.getProperty("jira.connectionFlag")))
            {
                Log.info("<--------Checking the failed testcases for creating the defect in JIRA-------->");
                Optional.ofNullable(latestTestOutCome).ifPresent(testOutcome -> {
                    String defectSummary = "AutoBug: " + testOutcome.getTitle();
                    String defectCreationFlag = envVars.getProperty("jira.defectCreationFlag");
                    jiraDefectCreator = new JiraDefectCreator(defectSummary,testOutcome.getTestFailureMessage(),testOutcome.isSuccess(),defectCreationFlag);
                });
                Log.info("<--------Defects created for the failed testcases in JIRA-------->");
            }
        }catch (NullPointerException e)
        {
          Log.info("A Null Pointer Exception Occurred: "+e.getMessage());
        }
    }

    @AfterStories
    public void reportFailingTests()
    {
        Log.info("<--------------------------Start Of Failed Test Error Analysis---------------------------------->");
        CommonUtils.getFailedTestCasesAuthorDetails();
        Log.info("<--------------------------End Of Failed Test Error Analysis---------------------------------->");
    }

}
