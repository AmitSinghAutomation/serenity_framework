package serenity.whatSteps;

import global.GlobalRequest;
import global.GlobalResponse;
import logger.Log;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import serenity.howSteps.CommonAPIHowSteps;
import utils.FileUtils;
import utils.JsonUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CommonAPIWhatSteps {

    private String userID=null;
    private String endPointAPI;
    private String endPointValue;
    private int actualResponseCode;
    private String actualResponseBody;
    private String dataNotToCompare;

    private JsonUtils jsonUtils = new JsonUtils();
    CommonAPIHowSteps commonAPIHowSteps = new CommonAPIHowSteps();
    private FileUtils fileUtils = new FileUtils();

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

}
