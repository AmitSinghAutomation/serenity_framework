package serenity.howSteps;

import global.GlobalRequest;
import global.GlobalResponse;
import io.restassured.response.Response;
import logger.Log;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import utils.ApiUtils;
import utils.JsonUtils;

import java.util.HashMap;

public class CommonAPIHowSteps {

    private String hostReqRes = System.getProperty("hostReqRes");
    ApiUtils apiUtils = new ApiUtils();
    JsonUtils jsonUtils = new JsonUtils();

    public void getRequestWithNoTokenNoHeader(String endPoint)
    {
        Log.info(GlobalRequest.getHeaders().toString());
        Response response = apiUtils.getRequestWithHeaders(this.hostReqRes, endPoint, GlobalRequest.getHeaders());
        GlobalResponse.setResponse(response);
        Log.info("Log for GET response with No Token and No Header: "+response.getBody().asString());
    }

    public void getRequestWithHeaderNoToken(String endPoint)
    {
        Log.info(GlobalRequest.getHeaders().toString());
        Response response = apiUtils.getRequestWithHeaders(this.hostReqRes, endPoint, GlobalRequest.getHeaders());
        GlobalResponse.setResponse(response);
        Log.info("Log for GET response with Header and No Token: "+response.getBody().asString());
    }

    public HashMap<String, String> validateResponseBody(String fileNameForResponseBody)
    {
        JSONObject jsonObject = new JSONObject(fileNameForResponseBody);
        HashMap<String, String> jsonHashMap = this.jsonUtils.listJson(jsonObject);
        return jsonHashMap;
    }

    public void postRequestWithNoTokenNoHeader(String endPoint, String inputJSON)
    {
        Response response = apiUtils.postRequestWithHeaders(this.hostReqRes, endPoint, inputJSON, GlobalRequest.getHeaders());
        GlobalResponse.setResponse(response);
        Log.info("Response for POST api: "+response.getBody().asString());
    }

    public void getValueFromResponseKey(String key, String value, String actualResponseBody)
    {
        String keyValue = getValueFromActualResponseJSON(actualResponseBody, key);
        keyValue = keyValue.substring(keyValue.indexOf(":") + 1);
        System.setProperty(value, keyValue);
        Log.info("Value stored for key: "+value+" is: "+keyValue);
    }

    private String getValueFromActualResponseJSON(String actualResponseBody, String key)
    {
        String getValueFromKey = "";
        if((actualResponseBody != null) || (key != null))
        {
           String result = actualResponseBody.contains(key)
                           ? key + StringUtils.substringAfter(actualResponseBody, key)
                           : actualResponseBody;
           String [] stringArray = result.split(",");
           String searchResult = stringArray[0];
           String removeQuotesFromString = searchResult.replaceAll("\"","");
           getValueFromKey = removeQuotesFromString;
        }
        return getValueFromKey;
    }

    public void deleteRequestWithNoTokenAndWithHeader(String endPoint)
    {
        Response response = apiUtils.deleteRequestWithHeaders(this.hostReqRes, endPoint, GlobalRequest.getHeaders());
        GlobalResponse.setResponse(response);
        Log.info("Response for DELETE api: "+response.getBody().asString());
    }

    public void putRequestWithNoTokenAndWithHeader(String endPointAPI, String requestPayloadWithAPI)
    {
        Response response = apiUtils.putRequestWithHeaders(this.hostReqRes, endPointAPI, requestPayloadWithAPI, GlobalRequest.getHeaders());
        GlobalResponse.setResponse(response);
        Log.info("Response for PUT api: "+response.getBody().asString());
    }

    public void patchRequestWithNoTokenAndWithHeader(String endPointValue, String requestPayloadWithAPI)
    {
        Response response = apiUtils.patchRequestWithHeaders(this.hostReqRes, endPointValue, requestPayloadWithAPI, GlobalRequest.getHeaders());
        GlobalResponse.setResponse(response);
        Log.info("Response for PATCH api: "+response.getBody().asString());
    }
}
