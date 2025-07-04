package utils;

import com.microsoft.applicationinsights.TelemetryClient;
import logger.Log;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.util.SystemEnvironmentVariables;

import java.time.Instant;
import java.util.*;

public class CommonUtils {

    static List<String> testCaseNameList = new ArrayList<String>();
    static List<String> issueKeyList = new ArrayList<String>();
    static HashMap<String, String> failingTests = new HashMap<String,String>();
    static String appInsightKey = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("appInsights.instrumentationKey");
    private static String ciJob = System.getProperty("CI-Job");
    static TelemetryClient telemetryClient = new TelemetryClient();
    static HashMap<String, String> appInsightMap = new HashMap<String,String>();
    static HashMap<String, Double> appInsightDuration = new HashMap<String,Double>();

    public static List<String> getTestCaseList(Map<String, String> metaDataMap) {

        for (Map.Entry<String, String> entry : metaDataMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("Tests")) {
                testCaseNameList = Arrays.asList(entry.getValue().toString().split("\n"));
            }
        }
        Log.info("Test Case Name List ---->" + testCaseNameList);
        return testCaseNameList;
    }

    public static List<String> getIssueKeyList(List<String> testCaseNameList) {
        Log.info("<-------Entry Get Issue Key List Method ----->");
        for (int i = 0; i < testCaseNameList.size(); i++) {
            String[] issueKey = testCaseNameList.get(i).toString().split(":");
            issueKeyList.add(issueKey[0]);
        }
        Log.info("<-------Exit Get Issue Key List Method ------->");
        Log.info("Test Case Issue Key List ---->" + issueKeyList);
        return issueKeyList;
    }

    public static String findContentBetween(String parentStr, String startStr, String endStr) {
        if (parentStr == null || startStr == null || endStr == null) {
            throw new IllegalArgumentException("None of the input strings can be null");
        }
        int startIndex = parentStr.indexOf(startStr);
        if(startIndex == -1)
        {
            return null;
        }
        startIndex += startStr.length();

        int endIndex = parentStr.indexOf(endStr,startIndex);
        if(endIndex == -1)
        {
            return null;
        }
        return parentStr.substring(startIndex,endIndex);
    }

    public static void setFailedTestCasesAuthorDetails(TestOutcome latestTestOutCome)
    {
        String scenarioDetails = latestTestOutCome.getDataDrivenSampleScenario();
        String author = CommonUtils.findContentBetween(scenarioDetails.toLowerCase(),"author","\n").replace(":","").trim();
        String scenario = latestTestOutCome.getTitle();
        String story = latestTestOutCome.getUserStory().getPath();
        Log.info(author+" "+scenario+" "+story);
        failingTests.put(story+" -> "+scenario,author);
    }

    public static void getFailedTestCasesAuthorDetails()
    {
        for(String key : failingTests.keySet())
        {
           String author = failingTests.get(key);
           String[] keyParts = key.split("->");
           String story = keyParts[0];
           String scenario = keyParts[1];
           Log.error("Failing Tests: \t" +story+"-> \t\t\t"+scenario+"-> \t\t\t Author: "+author);
        }
    }

    public static void generateAppInsightData(List<String> testCaseNameList,TestOutcome latestTestOutCome)
    {
       if(ciJob != null)
       {
           //telemetryClient.getContext().setInstrumentationKey(appInsightKey);
           appInsightMap.put("Scenario-Name",latestTestOutCome.getName());
           for(int i = 0; i < testCaseNameList.size(); i++)
           {
               String testCasename = testCaseNameList.get(i);
               String status = null;
               appInsightMap.put("Test-Name",testCasename);
               appInsightMap.put("Test-Story",latestTestOutCome.getUserStory().getDisplayName());
               Instant timeStamp = Instant.now();
               Log.info("CI Job Name: "+ciJob);
               if(latestTestOutCome.isSuccess())
               {
                   status = "Passed";
               }else
               {
                   status = "Failed";
               }
               appInsightMap.put("CI-Job",ciJob);
               appInsightMap.put("Test-Status",status);
               appInsightMap.put("Execution-Time",timeStamp.toString());
               appInsightMap.put("Failure-Reason",latestTestOutCome.getTestFailureMessage());
               appInsightDuration.put("Duration",latestTestOutCome.getDurationInSeconds());
               Log.info("CI-Job----------- "+ciJob);
               Log.info("Scenario-Name----------- "+latestTestOutCome.getName());
               Log.info("Test-Name----------- "+testCasename);
               Log.info("Test-Story----------- "+latestTestOutCome.getUserStory().getDisplayName());
               Log.info("Test-Status----------- "+status);
               Log.info("Execution-Time----------- "+timeStamp.toString());
               Log.info("Failure-Reason----------- "+latestTestOutCome.getTestFailureMessage());
               Log.info("Duration----------- "+latestTestOutCome.getDurationInSeconds());
               //telemetryClient.trackEvent("Test Automation Execution",appInsightMap,appInsightDuration);
               //telemetryClient.flush();
           }
       }else
       {
           Log.info("--------Application insight logs are not recording as CI-JOB is NULL--------");
       }
    }

}