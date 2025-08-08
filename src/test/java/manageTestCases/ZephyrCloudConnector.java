package manageTestCases;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import logger.Log;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZephyrCloudConnector {

    static String zephyrBaseUrl = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyrBaseUrl");
    static String zephyrAccessKey = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zapiAccessKey");
    static String zephyrSecretKey = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zapiSecretKey");
    static String accountId = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("accountId");
    static String testProjectId = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.projectId");
    static String testVersionId = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.versionId");
    static String testCycleName = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.cycleName");
    static String testFolderName = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.folderName");
    static String cycleId = null;
    static String folderId = null;
    static String executionId = null;
    static Integer issueId = null;

    // Zephyr cloud connector constructor to execute and update the testcases status
    public ZephyrCloudConnector(List<String> testCaseIssueKeyList, Boolean testExecutionStatus, String executionFlag) throws URISyntaxException {
        if(executionFlag.toUpperCase().equals("YES"))
        {
            this.executeTestCases(testCaseIssueKeyList, testExecutionStatus);
        }
    }

    // This method call update testcases status method to execute the testcases
    public void executeTestCases(List<String> testCaseIssueKeyList, Boolean testExecutionStatus) throws URISyntaxException {
        getJobProgressTicketOnAddTestInFolder(testCaseIssueKeyList);
        int issueKeyCountZephyr = testCaseIssueKeyList.size();
        int issueKeyCountResponse = getIssueKeyList().size();
        for(int j = 0; j < issueKeyCountZephyr; j++)
        {
           for(int i = 0; i < issueKeyCountResponse; i++)
           {
              if(testCaseIssueKeyList.get(j).equalsIgnoreCase(getIssueKeyList().get(i)))
              {
                 updateZephyrTestCasesStatus(i, testExecutionStatus);
                 break;
              }
           }
        }
    }

    // This method will update the testcase status based on test execution status
    public static void updateZephyrTestCasesStatus(int i, Boolean testExecutionStatus) throws URISyntaxException {
        String endPoint = null;
        ZephyrCloudConnector.executionId = getExecutionId(i);
        ZephyrCloudConnector.issueId = getIssueId(i);
        RestAssured.baseURI = zephyrBaseUrl;
        endPoint = "/public/rest/api/1.0/execution/" + ZephyrCloudConnector.executionId;
        String jwtToken = generateNewToken("PUT", endPoint);
        RequestSpecification updateExecutionRequest = RestAssured.given();
        JSONObject parentLoad = new JSONObject();
        parentLoad.put("versionId", testVersionId);
        parentLoad.put("projectId", testProjectId);
        parentLoad.put("assigneeType", "currentUser");
        parentLoad.put("id", ZephyrCloudConnector.executionId);
        parentLoad.put("cycleId", ZephyrCloudConnector.cycleId != null ? ZephyrCloudConnector.cycleId : getCycleId(testCycleName));
        parentLoad.put("issueId", ZephyrCloudConnector.issueId);

        JSONObject childLoad = new JSONObject();
        childLoad.put("id",testExecutionStatus ? 1 : 2);
        childLoad.put("description",testExecutionStatus ? "TEST EXECUTED AND PASSED"
                : "TEST EXECUTED AND FAILED");
        parentLoad.put("status", childLoad);
        int retryCount = 3;
        while(retryCount > 0)
        {
            try{
                Response updateExecutionResponse = updateExecutionRequest.when().given().header("Authorization", jwtToken).header("zapiAccessKey", zephyrAccessKey).contentType("application/json").body(parentLoad.toString()).put(endPoint);
                Log.info("Inside Try: "+ i + " th test case status successfully updated having issueId: "+ZephyrCloudConnector.issueId);
                Log.info("With endpoint: "+endPoint);
                break;
            }catch (Exception e){
                retryCount--;
                Response updateExecutionResponse = updateExecutionRequest.when().given().header("Authorization", jwtToken).header("zapiAccessKey", zephyrAccessKey).contentType("application/json").body(parentLoad.toString()).put(endPoint);
                Log.info("Inside Catch: "+ i + " th test case status successfully updated having issueId: "+ZephyrCloudConnector.issueId);
                Log.info("With endpoint: "+endPoint);
                Log.info("Retries left: "+retryCount);
            }
        }
    }

    // This method will return the zephyr execution id
    private static String getExecutionId(int i) throws URISyntaxException {
        String endPoint = null;
        RestAssured.baseURI = zephyrBaseUrl;
        if((ZephyrCloudConnector.folderId == null) || (ZephyrCloudConnector.cycleId == null))
        {
            endPoint = "/public/rest/api/2.0/executions/search/folder/" + getFolderId(testFolderName) + "?projectId=" + testProjectId + "&versionId=" + testVersionId + "&cycleId=" + getCycleId(testCycleName);
        }else
        {
            endPoint = "/public/rest/api/2.0/executions/search/folder/" + ZephyrCloudConnector.folderId + "?projectId=" + testProjectId + "&versionId=" + testVersionId + "&cycleId=" + ZephyrCloudConnector.cycleId;
        }
        Log.info(i+" th execution");
        Log.info("Endpoint: "+endPoint);
        Log.info("Get Execution Id URL: "+zephyrBaseUrl+endPoint);
        String jwtToken = generateNewToken("GET", endPoint);
        RequestSpecification getFolderRequest = RestAssured.given();
        Response getExecutionResponse = getFolderRequest.when().given().header("Authorization", jwtToken).header("zapiAccessKey", zephyrAccessKey).get(endPoint);
        String zephyrExecutionId = null;
        JsonPath jsonPath = getExecutionResponse.jsonPath();
        zephyrExecutionId = jsonPath.get("searchResult.searchObjectList[" +i+"].execution.id");
        ZephyrCloudConnector.executionId = zephyrExecutionId;
        return zephyrExecutionId;
    }

    // This method will return the zephyr issue id
    private static Integer getIssueId(int i) throws URISyntaxException {
        String endPoint = null;
        RestAssured.baseURI = zephyrBaseUrl;
        if((ZephyrCloudConnector.folderId == null) || (ZephyrCloudConnector.cycleId == null))
        {
            endPoint = "/public/rest/api/2.0/executions/search/folder/" + getFolderId(testFolderName) + "?projectId=" + testProjectId + "&versionId=" + testVersionId + "&cycleId=" + getCycleId(testCycleName);
        }else
        {
            endPoint = "/public/rest/api/2.0/executions/search/folder/" + ZephyrCloudConnector.folderId + "?projectId=" + testProjectId + "&versionId=" + testVersionId + "&cycleId=" + ZephyrCloudConnector.cycleId;
        }
        Log.info(i+" th execution");
        Log.info("Endpoint: "+endPoint);
        Log.info("Get Issue Id URL: "+zephyrBaseUrl+endPoint);
        String jwtToken = generateNewToken("GET", endPoint);
        RequestSpecification getFolderRequest = RestAssured.given();
        Response getExecutionResponse = getFolderRequest.when().given().header("Authorization", jwtToken).header("zapiAccessKey", zephyrAccessKey).get(endPoint);
        Integer zephyrIssueId = null;
        JsonPath jsonPath = getExecutionResponse.jsonPath();
        zephyrIssueId = jsonPath.get("searchResult.searchObjectList[" +i+"].execution.issueId");
        ZephyrCloudConnector.issueId = zephyrIssueId;
        return zephyrIssueId;
    }

    // This method will return the zephyr issue key list
    private List<String> getIssueKeyList() throws URISyntaxException {
        RestAssured.baseURI = zephyrBaseUrl;
        String endPoint = "/public/rest/api/2.0/executions/search/folder/" + getFolderId(testFolderName) + "?projectId=" + testProjectId + "&versionId=" + testVersionId + "&cycleId=" + getCycleId(testCycleName);
        String jwtToken = generateNewToken("GET", endPoint);
        RequestSpecification getFolderRequest = RestAssured.given();
        Response getExecutionResponse = getFolderRequest.when().given().header("Authorization", jwtToken).header("zapiAccessKey", zephyrAccessKey).get(endPoint);
        List<String> zephyrIssueKeyList = null;
        JsonPath jsonPath = getExecutionResponse.jsonPath();
        zephyrIssueKeyList = jsonPath.get("searchResult.searchObjectList.issueKey");
        return zephyrIssueKeyList;
    }

    // This method will return the job progress ticket by adding the testcases in a folder
    public static void getJobProgressTicketOnAddTestInFolder(List<String> testCaseIssueKeyList) throws URISyntaxException {
        String endPoint;
        RestAssured.baseURI = zephyrBaseUrl;
        if(ZephyrCloudConnector.folderId == null){
            ZephyrCloudConnector.folderId = getFolderId(testFolderName);
        }
        if(ZephyrCloudConnector.cycleId == null){
            ZephyrCloudConnector.cycleId = getCycleId(testCycleName);
        }
        endPoint = "/public/rest/api/1.0/executions/add/folder/" + ZephyrCloudConnector.folderId;
        String jwtToken = generateNewToken("POST", endPoint);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("versionId", testVersionId);
        jsonObject.put("projectId", testProjectId);
        jsonObject.put("assigneeType","currentUser");
        jsonObject.put("method",1);
        jsonObject.put("cycleId",ZephyrCloudConnector.cycleId);

        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(testCaseIssueKeyList);

        jsonObject.put("issues", jsonArray);
        int retryCount = 3;
        while(retryCount > 0) {
            try {
                RequestSpecification addTestCasesToFolderRequest = RestAssured.given();
                Response addTestCasesToFolderResponse = addTestCasesToFolderRequest.when().given().header("Authorization", jwtToken).body(jsonObject).header("zapiAccessKey", zephyrAccessKey).contentType("application/json").post(endPoint);
                Log.info(addTestCasesToFolderResponse.getBody().asString());
                break;
            }catch (Exception e)
            {
                retryCount--;
                RequestSpecification addTestCasesToFolderRequest = RestAssured.given();
                Response addTestCasesToFolderResponse = addTestCasesToFolderRequest.when().given().header("Authorization", jwtToken).body(jsonObject).header("zapiAccessKey", zephyrAccessKey).contentType("application/json").post(endPoint);
                Log.info(addTestCasesToFolderResponse.getBody().asString());
            }
        }

    }

    // This method will return zephyr folder id
    public static String getFolderId(String testFolderName) throws URISyntaxException {
        boolean isFolderFound = false;
        String folderId = null;
        String endPoint;
        if(ZephyrCloudConnector.folderId == null) {
            if(ZephyrCloudConnector.cycleId == null) {
                RestAssured.baseURI = zephyrBaseUrl;
                endPoint = "/public/rest/api/1.0/folders?versionId=" + testVersionId + "&cycleId=" + getCycleId(testCycleName) + "&projectId=" + testProjectId;
            }else
            {
                endPoint = "/public/rest/api/1.0/folders?versionId=" + testVersionId + "&cycleId=" + ZephyrCloudConnector.cycleId + "&projectId=" + testProjectId;
            }
            String jwtToken = generateNewToken("GET", endPoint);
            RequestSpecification getFolderRequest = RestAssured.given();
            Response getFolderResponse = getFolderRequest.when().given().header("Authorization", jwtToken).header("zapiAccessKey", zephyrAccessKey).get(endPoint);
            JsonPath jsonPath = getFolderResponse.jsonPath();
            List<Map<String, String>> parentMap = jsonPath.get("$");
            Map<String, String> getFolderInformationMap = new HashMap<>();
            for (int i = parentMap.size() - 1; i >= 0; i--) {
                if (parentMap.get(i).get("name").equalsIgnoreCase(testFolderName)) {
                    getFolderInformationMap = parentMap.get(i);
                    for (Map.Entry<String, String> myValueMap : getFolderInformationMap.entrySet()) {
                        if (myValueMap.getKey().equalsIgnoreCase("id")) {
                            folderId = myValueMap.getValue();
                            isFolderFound = true;
                            ZephyrCloudConnector.folderId = folderId;
                            break;
                        }
                    }
                    break;
                }
            }
            if (!isFolderFound) {
                // Call create zephyr test folder method and then return folder Id for the same
                Map<String, String> createdFolderMap = new HashMap<>();
                createdFolderMap = createdNewZephyrTestFolder(testFolderName);
                for (Map.Entry<String, String> myValueMap : createdFolderMap.entrySet()) {
                    if (myValueMap.getKey().equalsIgnoreCase("id")) {
                        folderId = myValueMap.getValue();
                        ZephyrCloudConnector.folderId = folderId;
                        break;
                    }
                }

            }
            return folderId;
        }else
        {
            return ZephyrCloudConnector.folderId;
        }
    }

    // This method will create new zephyr test folder
    public static Map<String, String> createdNewZephyrTestFolder(String testFolderName) throws URISyntaxException {
        HashMap<String, String> createdFolderMap = new HashMap<>();
        RestAssured.baseURI = zephyrBaseUrl;
        String endPoint = "/public/rest/api/1.0/folder?expand=&clonedFolderId=";
        String jwtToken = generateNewToken("POST", endPoint);
        HashMap<String, String> bodyRequest = new HashMap<>();
        bodyRequest.put("name",testFolderName);
        bodyRequest.put("versionId", String.valueOf(testVersionId));
        bodyRequest.put("projectId", String.valueOf(testProjectId));
        if(ZephyrCloudConnector.cycleId == null){
            bodyRequest.put("cycleId",String.valueOf(getCycleId(testCycleName)));
        }else{
            bodyRequest.put("cycleId",ZephyrCloudConnector.cycleId);
        }
        RequestSpecification createZephyrTestFolderRequest = RestAssured.given();
        Response createZephyrTestFolderResponse = createZephyrTestFolderRequest.when().given().header("Authorization",jwtToken).body(bodyRequest).header("zapiAccessKey", zephyrAccessKey).contentType("application/json").post(endPoint);
        if(createZephyrTestFolderResponse.statusCode() == 200)
        {
            JsonPath jsonPath = createZephyrTestFolderResponse.jsonPath();
            createdFolderMap = jsonPath.get("$");
            createdFolderMap.put("folderId", jsonPath.get("id"));
            createdFolderMap.put("folderName", jsonPath.get("name"));
        }else
        {
            System.out.println("<---------------Zephyr test folder not created--------------->");
        }
        return createdFolderMap;
    }

    // This method will return zephyr test cycle id
    public static String getCycleId(String testCycleName) throws URISyntaxException {
        boolean isCycleFound = false;
        String cycleId = null;
        if(ZephyrCloudConnector.cycleId == null) {
            RestAssured.baseURI = zephyrBaseUrl;
            String endPoint = "/public/rest/api/1.0/cycles/search?projectId=" + testProjectId + "&versionId=" + testVersionId;
            String jwtToken = generateNewToken("GET", endPoint);
            RequestSpecification getTestCycleRequest = RestAssured.given();
            Response getCycleIdResponse = getTestCycleRequest.when().given().header("Authorization", jwtToken).header("zapiAccessKey", zephyrAccessKey).get(endPoint);
            JsonPath jsonPath = getCycleIdResponse.jsonPath();
            List<Map<String, String>> parentMap = jsonPath.get("$");
            Map<String, String> getCycleInformationMap;
            for (int i = parentMap.size() - 1; i >= 0; i--) {
                if(parentMap.get(i).get("name").equalsIgnoreCase(testCycleName)) {
                    getCycleInformationMap = parentMap.get(i);
                    for (Map.Entry<String, String> myValueMap : getCycleInformationMap.entrySet()) {
                        if (myValueMap.getKey().equalsIgnoreCase("id")) {
                            cycleId = myValueMap.getValue();
                            isCycleFound = true;
                            ZephyrCloudConnector.cycleId = cycleId;
                            break;
                        }
                    }
                    break;
                }
            }
            if (!isCycleFound) {
                // Call create zephyr test cycle method and then return cycle Id for the same
                Map<String, String> createdCycleMap;
                createdCycleMap = createdNewZephyrTestCycle(testCycleName);
                for (Map.Entry<String, String> myValueMap : createdCycleMap.entrySet()) {
                    if (myValueMap.getKey().equalsIgnoreCase("id")) {
                        cycleId = myValueMap.getValue();
                        ZephyrCloudConnector.cycleId = cycleId;
                        break;
                    }
                }

            }
            return cycleId;
        }else
        {
            return ZephyrCloudConnector.cycleId;
        }
    }

    // This method will create new Zephyr Test Cycle
    public static Map<String, String> createdNewZephyrTestCycle(String testCycleName) throws URISyntaxException {
        HashMap<String, String> createdCycleMap = new HashMap<>();
        RestAssured.baseURI = zephyrBaseUrl;
        String endPoint = "/public/rest/api/1.0/cycle?expand=&clonedCycleId=";
        String jwtToken = generateNewToken("POST", endPoint);
        HashMap<String, String> bodyRequest = new HashMap<>();
        bodyRequest.put("name",testCycleName);
        bodyRequest.put("versionId", String.valueOf(testVersionId));
        bodyRequest.put("projectId", String.valueOf(testProjectId));
        RequestSpecification createZephyrTestCycleRequest = RestAssured.given();
        Response createZephyrTestCycleResponse = createZephyrTestCycleRequest.when().given().header("Authorization",jwtToken).body(bodyRequest).header("zapiAccessKey", zephyrAccessKey).contentType("application/json").post(endPoint);
        if(createZephyrTestCycleResponse.statusCode() == 200)
        {
            JsonPath jsonPath = createZephyrTestCycleResponse.jsonPath();
            createdCycleMap = jsonPath.get("$");
            createdCycleMap.put("cycleId", jsonPath.get("id"));
            createdCycleMap.put("cycleName", jsonPath.get("name"));
        }else
        {
            System.out.println("<---------------Zephyr test cycle not created--------------->");
        }
        return createdCycleMap;
    }

    // This method will return JWT Token on providing zephyr base url, access key, secret key and account id
    public static String generateNewToken(String requestType, String endPoint) throws URISyntaxException {
        String tokenId = null;
        ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, zephyrAccessKey, zephyrSecretKey, accountId).build();
        JwtGenerator jwtGenerator = client.getJwtGenerator();
        String endPointWithBaseUrl = zephyrBaseUrl + endPoint;
        URI uri = new URI(endPointWithBaseUrl);
        int expirationInSeconds = 3600;
        String jwtToken = jwtGenerator.generateJWT(requestType, uri, expirationInSeconds);
        tokenId = jwtToken;
        return tokenId;
    }
}
