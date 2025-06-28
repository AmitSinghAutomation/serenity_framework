package utils;

import logger.Log;
import net.thucydides.core.model.TestOutcome;

import java.util.*;

public class CommonUtils {

    static List<String> testCaseNameList = new ArrayList<String>();
    static List<String> issueKeyList = new ArrayList<String>();
    static HashMap<String, String> failingTests = new HashMap<String,String>();

    public static List<String> getTestCaseList(Map<String, String> metaDataMap) {
        Log.info("Meta Data ---->" + metaDataMap);
        for (Map.Entry<String, String> entry : metaDataMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("Tests")) {
                testCaseNameList = Arrays.asList(entry.getValue().toString().split("\n"));
            }
        }
        return testCaseNameList;
    }

    public static List<String> getIssueKeyList(List<String> testCaseNameList) {
        Log.info("<-------Entry Get Issue Key List Method ----->");
        for (int i = 0; i < testCaseNameList.size(); i++) {
            String[] issueKey = testCaseNameList.get(i).toString().split(":");
            issueKeyList.add(issueKey[0]);
        }
        Log.info("<-------Exit Get Issue Key List Method ------->");
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
        String author = CommonUtils.findContentBetween(scenarioDetails.toLowerCase(),"author","!--").replace(":","").trim();
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

}