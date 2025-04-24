package utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import logger.Log;
import net.thucydides.core.util.SystemEnvironmentVariables;

import java.util.HashMap;
import java.util.Map;

public class OktaServiceTokenManager
{
    private ApiUtils apiUtils = new ApiUtils();

    private String authRequestJson;
    private String authCode;
    private String authToken;
    private String sessionToken = null;

    private String appEnvironment = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.appEnvironment");
    private String issuerId_Dev = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.issuerId_DEV");
    private String issuerId_QA = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.issuerId_QA");

    private static String oktaStageUrl = "https://id-stage.domain.com";
    private static String oktaProdUrl = "https://id-prod.domain.com";

    private static String post_sessionToken_endPoint = "/api/v1/authn";
    private String get_authCodeEndPoint;
    private String post_authTokenEndPoint;

    public String JSONPATH_SessionTOKEN = "$.sessionToken";
    public String JSONPATH_AccessTOKEN = "$.access_Token";

    public static Map<String, String> headersForApi_Get = new HashMap<String, String>();

    static
    {
        headersForApi_Get.put("Accept","application/json");
    }

    public static Map<String, String> headersForApi_Post = new HashMap<String, String>();

    static
    {
        headersForApi_Get.put("Accept","application/json");
        headersForApi_Post.put("Accept","application/json");
        headersForApi_Post.put("Content-Type","application/json");
    }

    // This method will return session token by making authentication call
    public String getSessionToken(String inputJson)
    {
        try
        {
          this.authRequestJson = inputJson;
            Log.info(authRequestJson);
            Log.info(appEnvironment.toUpperCase());
            if(appEnvironment.toUpperCase().equals("STAGE"))
            {
                Response response = apiUtils.postRequestWithHeaders(oktaStageUrl,post_sessionToken_endPoint,authRequestJson,headersForApi_Post);
                if(response.getStatusCode() != 200)
                {
                  Log.error("Get_SessionToken API Response Code: "+String.valueOf(response.getStatusCode()));
                  throw new Exception();
                }
                DocumentContext documentContext = JsonPath.parse(response.getBody().asString());
                this.sessionToken = documentContext.read(JSONPATH_SessionTOKEN);
                Log.info("Session Token: "+this.sessionToken);
            }else if(appEnvironment.toUpperCase().equals("PROD"))
            {
                Response response = apiUtils.postRequestWithHeaders(oktaProdUrl,post_sessionToken_endPoint,authRequestJson,headersForApi_Post);
                if(response.getStatusCode() != 200)
                {
                    Log.error("Get_SessionToken API Response Code: "+String.valueOf(response.getStatusCode()));
                    throw new Exception();
                }
                DocumentContext documentContext = JsonPath.parse(response.getBody().asString());
                this.sessionToken = documentContext.read(JSONPATH_SessionTOKEN);
                Log.info("Session Token: "+this.sessionToken);
            }
        }catch (Exception exceptionFromRestCall)
        {
            Log.error("Failed to make Get_SessionToken call: "+exceptionFromRestCall);
        }
        return this.sessionToken;
    }

    private String authorizationCode(String sessionToken) throws Exception {
        if(appEnvironment.toUpperCase().equals("STAGE"))
        {
          this.get_authCodeEndPoint = "/oauth2" + this.issuerId_Dev + "/v1/authorize";
          Map<String, String> queryParamForRest_Get = new HashMap<String, String>();
          {
              queryParamForRest_Get.put("sessionToken", sessionToken);
              queryParamForRest_Get.put("client_id", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.clientId_DEV"));
              queryParamForRest_Get.put("scope", "openid groups profile email");
              queryParamForRest_Get.put("redirect_uri", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.redirectUri_Stage"));
              queryParamForRest_Get.put("response_type", "code");
              queryParamForRest_Get.put("state", "state");
              queryParamForRest_Get.put("code_challenge", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.codeChallenge"));
              queryParamForRest_Get.put("code_challenge_method", "S256");
          }
          for(Map.Entry<String, String> entry : queryParamForRest_Get.entrySet())
          {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.info("Key= "+key+", Value= "+value);
          }
            Response response = apiUtils.getRequestWithHeadersAndParameters(oktaStageUrl,this.get_authCodeEndPoint,headersForApi_Get,queryParamForRest_Get);
            Log.info("Get_AuthCode API call Response: "+String.valueOf(response.getStatusCode()));
            Headers response_Headers = response.getHeaders();
            Log.info("Response Headers: "+response_Headers.toString());
            if(response.getStatusCode() != 302)
            {
                Log.error("Get_AuthToken API Response Code: "+String.valueOf(response.getStatusCode()));
                throw new Exception();
            }
            String locationHeader = response.getHeaders().getValue("location");
            Log.info("Get_AuthCode API Location Header: "+locationHeader);
            this.authCode = locationHeader.split("=")[1].split("&")[0];
            Log.info("Authorization Code: "+authCode);
        }else if(appEnvironment.toUpperCase().equals("PROD"))
        {
            this.get_authCodeEndPoint = "/oauth2" + this.issuerId_QA + "/v1/authorize";
            Map<String, String> queryParamForRest_Get = new HashMap<String, String>();
            {
                queryParamForRest_Get.put("sessionToken", sessionToken);
                queryParamForRest_Get.put("client_id", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.clientId_QA"));
                queryParamForRest_Get.put("scope", "openid groups profile email");
                queryParamForRest_Get.put("redirect_uri", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.redirectUri_Prod"));
                queryParamForRest_Get.put("response_type", "code");
                queryParamForRest_Get.put("state", "state");
                queryParamForRest_Get.put("code_challenge", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.codeChallenge"));
                queryParamForRest_Get.put("code_challenge_method", "S256");
            }
            for(Map.Entry<String, String> entry : queryParamForRest_Get.entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.info("Key= "+key+", Value= "+value);
            }
            Response response = apiUtils.getRequestWithHeadersAndParameters(oktaProdUrl,this.get_authCodeEndPoint,headersForApi_Get,queryParamForRest_Get);
            Log.info("Get_AuthCode API call Response: "+String.valueOf(response.getStatusCode()));
            Headers response_Headers = response.getHeaders();
            Log.info("Response Headers: "+response_Headers.toString());
            if(response.getStatusCode() != 302)
            {
                Log.error("Get_AuthToken API Response Code: "+String.valueOf(response.getStatusCode()));
                throw new Exception();
            }
            String locationHeader = response.getHeaders().getValue("location");
            Log.info("Get_AuthCode API Location Header: "+locationHeader);
            this.authCode = locationHeader.split("=")[1].split("&")[0];
            Log.info("Authorization Code: "+authCode);
        }
        return this.authCode;
    }

    private String fetchAuthenticationToken(String authCode) throws Exception {
      Map<String, String> headersForRest_PostEncoded = new HashMap<String, String>();
        {
            headersForApi_Post.put("Accept","*/*");
            headersForApi_Post.put("Content-Type","application/x-www-form-urlencoded");
        }
        if(appEnvironment.toUpperCase().equals("STAGE") == true)
        {
            this.post_authTokenEndPoint = "/oauth2" + this.issuerId_Dev + "/v1/token";
            Map<String, String> formsForRest_Post = new HashMap<String, String>();
            {
                formsForRest_Post.put("client_id", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.clientId_DEV"));
                formsForRest_Post.put("grant_type", "authorization_code");
                formsForRest_Post.put("redirect_uri", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.redirectUri_Stage"));
                formsForRest_Post.put("code", authCode);
                formsForRest_Post.put("code_verifier", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.codeVerifier"));
            }
            Response response = apiUtils.postRequestWithURLEncodedKeys(oktaStageUrl,this.post_authTokenEndPoint,formsForRest_Post,headersForRest_PostEncoded);
            if(response.getStatusCode() != 200)
            {
                Log.error("Post_AuthToken API Response Code: "+String.valueOf(response.getStatusCode()));
                throw new Exception();
            }
            DocumentContext documentContext = JsonPath.parse(response.getBody().asString());
            this.authToken = "Bearer " + documentContext.read(JSONPATH_AccessTOKEN);
        }else if(appEnvironment.toUpperCase().equals("PROD") == true)
        {
            this.post_authTokenEndPoint = "/oauth2" + this.issuerId_QA + "/v1/token";
            Map<String, String> formsForRest_Post = new HashMap<String, String>();
            {
                formsForRest_Post.put("client_id", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.issuerId_QA"));
                formsForRest_Post.put("grant_type", "authorization_code");
                formsForRest_Post.put("redirect_uri", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.redirectUri_Prod"));
                formsForRest_Post.put("code", authCode);
                formsForRest_Post.put("code_verifier", SystemEnvironmentVariables.createEnvironmentVariables().getProperty("okta.codeVerifier"));
            }
            Response response = apiUtils.postRequestWithURLEncodedKeys(oktaProdUrl,this.post_authTokenEndPoint,formsForRest_Post,headersForRest_PostEncoded);
            if(response.getStatusCode() != 200)
            {
                Log.error("Post_AuthToken API Response Code: "+String.valueOf(response.getStatusCode()));
                throw new Exception();
            }
            DocumentContext documentContext = JsonPath.parse(response.getBody().asString());
            this.authToken = "Bearer " + documentContext.read(JSONPATH_AccessTOKEN);
        }
        Log.info("Authentication Token: "+this.authToken);
    }

    public String getAuthenticationToken(String inputJson) throws Exception
    {
        this.sessionToken = getSessionToken(inputJson);
        this.authCode = authorizationCode(this.sessionToken);
        this.authToken = fetchAuthenticationToken(this.authCode);
        return authToken;
    }

}
