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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AcceptanceTestSuite extends SerenityStories {

    ZephyrCloudConnector zephyrCloudConnector;
    JiraDefectCreator jiraDefectCreator;
    EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    String executionFlag;

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
            TestOutcome latestTestOutCome = StepEventBus.getEventBus().getBaseStepListener().latestTestOutcome().orElse(null);
            if ("YES".equalsIgnoreCase(environmentVariables.getProperty("zephyr.connectionFlag"))) {
                Log.info("<--------Start of marking the testcases status in Zephyr-------->");
                Map<String, String> metaDataMap = Serenity.getCurrentSession().getMetaData();
                Log.info("Meta Data ---->" + metaDataMap);
                List<String> testCaseNameList = CommonUtils.getTestCaseList(metaDataMap);
                List<String> testCaseIssueKeyList = CommonUtils.getIssueKeyList(testCaseNameList);
                List<String> testCaseIssueKeyListAfterScenario = CommonUtils.getIssueKeyListAfterScenario(testCaseNameList);
                executionFlag = environmentVariables.getProperty("zephyr.executionFlag");
                if (latestTestOutCome != null) {
                    if ("YES".equalsIgnoreCase(environmentVariables.getProperty(executionFlag))) {
                        CommonUtils.setIssueKeyWithStatus(testCaseIssueKeyListAfterScenario,latestTestOutCome);
                        ZephyrCloudConnector.getJobProgressTicketOnAddTestInFolder(testCaseIssueKeyListAfterScenario);
                        zephyrCloudConnector = new ZephyrCloudConnector(testCaseIssueKeyList, latestTestOutCome.isSuccess(), executionFlag);
                    }
                    CommonUtils.generateAppInsightData(testCaseNameList, latestTestOutCome);
                }
                Log.info("<--------End of marking the testcases status in Zephyr-------->");
            }
            if (latestTestOutCome != null && !latestTestOutCome.isSuccess()) {
                if(environmentVariables.getProperty("ExecutionType").equalsIgnoreCase("android"))
                {
                    Log.info("---------------------------Resetting Chrome Starts--------------------------------------");
                    Runtime.getRuntime().exec("adb shell pm clear com.android.chrome");
                    Log.info("---------------------------Resetting Chrome Ends--------------------------------------");
                }
                CommonUtils.setFailedTestCasesAuthorDetails(latestTestOutCome);
            }
            if ("YES".equalsIgnoreCase(environmentVariables.getProperty("jira.connectionFlag")))
            {
                Log.info("<--------Checking the failed testcases for creating the defect in JIRA-------->");
                Optional.ofNullable(latestTestOutCome).ifPresent(testOutcome -> {
                    String defectSummary = "AutoBug: " + testOutcome.getTitle();
                    String defectCreationFlag = environmentVariables.getProperty("jira.defectCreationFlag");
                    jiraDefectCreator = new JiraDefectCreator(defectSummary,testOutcome.getTestFailureMessage(),testOutcome.isSuccess(),defectCreationFlag);
                });
                Log.info("<--------Defects created for the failed testcases in JIRA-------->");
            }
        }catch (NullPointerException | IOException e)
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

    @AfterStories
    public void testcasesIssueKeyWithStatus()
    {
        Log.info("<--------------------------Issue Key With Status Starts---------------------------------->");
        if ("YES".equalsIgnoreCase(environmentVariables.getProperty(executionFlag)))
        {
         Map<String, Boolean> issueKeyWithStatus = CommonUtils.getIssueKeyWithStatus();
         CommonUtils.markTestCasesExecutionStatus(issueKeyWithStatus);
        }
        Log.info("<--------------------------Issue Key With Status Ends---------------------------------->");
    }

}
