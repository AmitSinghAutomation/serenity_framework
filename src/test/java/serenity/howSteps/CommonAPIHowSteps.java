package serenity.howSteps;

import global.GlobalRequest;
import global.GlobalResponse;
import io.restassured.response.Response;
import logger.Log;
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

}
