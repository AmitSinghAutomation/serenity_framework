package manageTestCases;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import logger.Log;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import utils.ApiUtils;
import utils.FileUtils;
import utils.JsonUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JiraDefectCreator {

    ApiUtils apiUtils = new ApiUtils();
    FileUtils fileUtils = new FileUtils();
    JsonUtils jsonUtils = new JsonUtils();
    static EnvironmentVariables envVars = SystemEnvironmentVariables.createEnvironmentVariables();

    private String requestBody;
    private String JSONPATH_DEFECT_COUNT = "$.total";
    int defectCount;

    private static String jiraUserName = envVars.getProperty("jira.userName");
    private static String jiraApiKey = envVars.getProperty("jira.apiKey");

    private String jiraCloudUrl = "https://company-corp.atlassian.net";

    private String post_CreateIssue_Endpoint = "/rest/api/3/issue";
    private String post_SearchIssue_Endpoint = "/rest/api/3/search";

    private static String credentials = jiraUserName + ":" + jiraApiKey;
    private static String encodedAuth = "Basic" + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

    private static Map<String, String> headersForRest_Post = new HashMap<String,String>();

    static
    {
        headersForRest_Post.put("Accept","application/json");
        headersForRest_Post.put("Content-Type","application/json");
        headersForRest_Post.put("Authorization",encodedAuth);
    }

    public JiraDefectCreator(String defectSummary, String defectDescription, boolean scenarioStatus, String createDefectFlag)
    {
      if((createDefectFlag.toUpperCase().equals("YES")) && (scenarioStatus == false))
      {
        this.defectCount = searchDefectOnJira(defectSummary);

        if(this.defectCount != 0)
        {
            Log.info("Defect already available for the test failures and the count is: "+ this.defectCount);
        }else
        {
            this.createDefectOnJira(defectSummary,defectDescription);
        }
      }
    }

    private void createDefectOnJira(String defectSummary, String defectDescription) {

        this.requestBody = fileUtils.readFileFromGivenLocation(jsonUtils.getValueFromSerenityproperties("jira.filePath") + "JiraDefect" + File.separator + "JiraDefectRequestBody.json");
        this.requestBody = String.format(this.requestBody,defectSummary,defectDescription);

        Response response = apiUtils.postRequestWithHeaders(jiraCloudUrl,post_CreateIssue_Endpoint,requestBody,headersForRest_Post);
        Log.info("Defect created successfully having response: "+ response);
    }

    private int searchDefectOnJira(String defectSummary) {

        this.requestBody = fileUtils.readFileFromGivenLocation(jsonUtils.getValueFromSerenityproperties("jira.filePath") + "JiraDefect" + File.separator + "SearchJiraDefectRequestBody.json");
        this.requestBody = String.format(this.requestBody,defectSummary);

        Response response = apiUtils.postRequestWithHeaders(jiraCloudUrl,post_SearchIssue_Endpoint,requestBody,headersForRest_Post);
        DocumentContext documentContext = JsonPath.parse(response.getBody().asString());

        return documentContext.read(JSONPATH_DEFECT_COUNT);
    }

}
