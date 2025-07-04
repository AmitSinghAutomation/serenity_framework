package serenity.whatSteps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import global.GlobalRequest;
import global.GlobalResponse;
import logger.Log;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
import serenity.howSteps.CommonAPIHowSteps;
import utils.FileUtils;
import utils.JsonUtils;
import utils.OktaServiceTokenManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommonAPIWhatSteps {

    private String userID=null;
    private String endPointAPI;
    private String endPointValue;
    private int actualResponseCode;
    private String actualResponseBody;
    private String dataNotToCompare;
    private String requestPayloadWithAPI;
    private String tokenValue = null;

    private JsonUtils jsonUtils = new JsonUtils();
    CommonAPIHowSteps commonAPIHowSteps = new CommonAPIHowSteps();
    private FileUtils fileUtils = new FileUtils();
    @Steps
    OktaServiceTokenManager oktaServiceTokenManager;
    private static String envFileName = System.getProperty("exeEnvironment")+".json";
    private static String envFilePath = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("environment.filepath")+envFileName;

    @Given("User has a valid ID from properties")
    public void getValidUserIDFromProperties()
    {
        this.userID = System.getProperty("validUserID");
        Log.info("Valid user ID: "+userID);
    }

    @Given("User prepares endpoint $EndPoint and writes $value as $ValueToBeAddedInEndPoint")
    public void prepareEndPointForAPIReplaceValue(String endPoint, @Named("IdToBeReplacedWithEndPoint") String idToBeReplacedWithEndPoint, String var)
    {
        Log.info("Value coming from Json file side: " + System.getProperty(var));
        this.endPointAPI = jsonUtils.readJsonFile(endPoint);
        this.endPointValue = endPointAPI.replaceAll("\\{" + idToBeReplacedWithEndPoint + "}",System.getProperty(var));
        Log.info("Complete endpoint is: "+endPointValue);
    }

    @Given("User prepares endpoint as $EndPoint and request body using $RequestBody")
    public void prepareEndPointWithRequestBody(String endPoint, String fileNameRequestBody,@Named("RequestBodyFolderName") String requestBodyFolderName)
    {
        Map<String, String> version = GlobalRequest.getHeaders();
        if(version.containsKey("version") == true)
        {
         this.requestPayloadWithAPI = this.fileUtils.readFileFromGivenLocation(this.jsonUtils.getValueFromSerenityproperties("apiRequestBody.filePath") + requestBodyFolderName + File.separator + version.get("version") + File.separator + fileNameRequestBody);
        }else
        {
            this.requestPayloadWithAPI = this.fileUtils.readFileFromGivenLocation(this.jsonUtils.getValueFromSerenityproperties("apiRequestBody.filePath") + requestBodyFolderName  + File.separator + fileNameRequestBody);
        }
        this.endPointAPI = jsonUtils.readJsonFile(endPoint);
        Log.info("Request payload with api: "+this.requestPayloadWithAPI);
        Log.info("Api endpoint is: "+this.endPointAPI);
    }

    @Given("User has a request header for $HeaderName as $HeaderValue")
    public void setHeaderInGlobalRequest(String headerName, String headerValue)
    {
        GlobalRequest.setHeaders(headerName, headerValue);
    }

    @When("User makes GET request with empty token and empty header")
    public void getRequestWithNoTokenNoHeader()
    {
        commonAPIHowSteps.getRequestWithNoTokenNoHeader(this.endPointValue);
    }

    @When("User makes GET request with header and empty token")
    public void getRequestWithHeaderNoToken()
    {
        commonAPIHowSteps.getRequestWithNoTokenNoHeader(this.endPointValue);
    }

    @When("User makes POST request")
    public void postRequestWithNoTokenNoHeader()
    {
        commonAPIHowSteps.postRequestWithNoTokenNoHeader(this.endPointAPI, this.requestPayloadWithAPI);
    }

    @When("User makes PUT request")
    public void putRequestWithNoTokenAndWithHeader()
    {
        commonAPIHowSteps.putRequestWithNoTokenAndWithHeader(this.endPointValue, this.requestPayloadWithAPI);
    }

    @When("User makes PATCH request")
    public void patchRequestWithNoTokenAndWithHeader()
    {
        commonAPIHowSteps.patchRequestWithNoTokenAndWithHeader(this.endPointValue, this.requestPayloadWithAPI);
    }

    @When("User makes DELETE request")
    public void deleteRequestWithNoTokenAndWithHeader()
    {
        commonAPIHowSteps.deleteRequestWithNoTokenAndWithHeader(this.endPointValue);
    }

    @Then("Response should have a response code as $ExpectedResponseCode")
    public void expectedResponseCode(int expectedResponseCode)
    {
       this.actualResponseCode = GlobalResponse.getResponse().getStatusCode();
       Log.info("Log for actual response code: "+this.actualResponseCode);
        Assert.assertEquals("Actual response code is not matching with expected response code", expectedResponseCode, this.actualResponseCode);
    }

    @Then("Response should have a response body as $ExpectedResponseBody")
    public void expectedResponseBody(String fileNameForExpectedResponseBody, @Named("ResponseBodyFolderName") String responseBodyFolderName, @Named("DataNotToCompare") String dataNotToCompare)
    {
        Map<String, String> version = GlobalRequest.getHeaders();
        String expectedResponseBody;
        if(version.containsKey("version") == true)
        {
            expectedResponseBody = this.fileUtils.readFileFromLocation(this.jsonUtils.getValueFromSerenityproperties("apiExpectedBody.filePath") + responseBodyFolderName + File.separator + version.get("version") + File.separator + fileNameForExpectedResponseBody);
        }else
        {
            expectedResponseBody = this.fileUtils.readFileFromLocation(this.jsonUtils.getValueFromSerenityproperties("apiExpectedBody.filePath")+responseBodyFolderName+File.separator+fileNameForExpectedResponseBody);
        }
        HashMap<String, String> expectedJsonHashMap = commonAPIHowSteps.validateResponseBody(expectedResponseBody.trim());
        this.actualResponseBody = GlobalResponse.getResponse().getBody().asString();
        HashMap<String, String> actualJsonHashMap = commonAPIHowSteps.validateResponseBody(this.actualResponseBody.trim());
        this.dataNotToCompare = jsonUtils.readJsonFile(dataNotToCompare);
        Log.info("Data not to compare: "+ this.dataNotToCompare);
        Assert.assertTrue("Actual response is not matching with the expected response", this.jsonUtils.compareHashMaps(expectedJsonHashMap,actualJsonHashMap,this.dataNotToCompare));
        Log.info("Data Matched");


    }

    @Then("User store the parameter from response for $Key as $Value")
    public void getValueFromResponseKey(String Key, String Value)
    {
        if(Key.equals("Blank") && Value.equals("Blank"))
        {
            return;
        }
        commonAPIHowSteps.getValueFromResponseKey(Key, Value, this.actualResponseBody);
        Log.info("Value for: "+Key+" node is: "+System.getProperty(Value));
    }

    @Then("Required actual response get copied in environment for $entity")
    public void updateMatchingValueInMap(String entity)
    {
        try{
            ObjectMapper mapper = null;
            JsonNode jsonNode = null;
            File file = new File(envFilePath);
            String expectedEnvJsonBody;

            expectedEnvJsonBody = this.fileUtils.readFileFromLocation(envFilePath);

            HashMap<String, String> expectedEnvJsonHashMap = commonAPIHowSteps.validateResponseBody(expectedEnvJsonBody.trim());

            Map<String, String> expectedEntityMatchedJsonMap = new HashMap<String, String>();

            for(Map.Entry<String,String> keyValueInEnvironmentJson : expectedEnvJsonHashMap.entrySet())
            {
              if(keyValueInEnvironmentJson.getKey().startsWith(entity))
              {
                  expectedEntityMatchedJsonMap.put(keyValueInEnvironmentJson.getKey(),keyValueInEnvironmentJson.getValue());
              }else
              {
                  Log.error("Provided entity value not present under environment file: " + entity);
              }
            }

            this.actualResponseBody = GlobalResponse.getResponse().getBody().asString();
            HashMap<String,String> actualJsonHashmap = commonAPIHowSteps.validateResponseBody(this.actualResponseBody.trim());

            mapper = new ObjectMapper();
            jsonNode = mapper.readTree(file);

            for(Map.Entry<String, String> expectedKey : expectedEntityMatchedJsonMap.entrySet())
            {
                for(Map.Entry<String, String> actualKey : actualJsonHashmap.entrySet())
                {
                    String entityKeySet = (entity+"_"+actualKey.getKey());
                    if(expectedKey.getKey().trim().equals(entityKeySet.trim()))
                    {
                        ((ObjectNode) jsonNode).put(expectedKey.getKey(),actualKey.getValue());
                        Log.info(expectedKey.getValue() + " updated with "+actualKey.getValue());
                        break;
                    }
                }
            }
                mapper.writerWithDefaultPrettyPrinter().writeValue(file,jsonNode);

        }catch (IOException e){
               e.printStackTrace();
        }

    }

    @Given("User prepares okta request body")
    public void preparesOktaRequestBody(ExamplesTable fieldsTable)
    {
      for(Map<String,String> row: fieldsTable.getRows())
      {
          String bodyRequest = row.get("RequestBody");
          String requestBodyFolderName = row.get("RequestBodyFolderName");
          this.requestPayloadWithAPI = this.fileUtils.readFileFromGivenLocation(this.jsonUtils.getValueFromSerenityproperties("oktaAuthRequest.filePath") + requestBodyFolderName + File.separator + bodyRequest);
      }
    }

    @Given("User has a valid okta token for $username and $password")
    public void prepareValidOktaTokenForUser(String username, String password) throws Exception {
        this.requestPayloadWithAPI = String.format(this.requestPayloadWithAPI, jsonUtils.readJsonFile(username), jsonUtils.readJsonFile(password));
        Log.info(requestPayloadWithAPI);
        this.tokenValue = oktaServiceTokenManager.getAuthenticationToken(this.requestPayloadWithAPI);
    }

}
