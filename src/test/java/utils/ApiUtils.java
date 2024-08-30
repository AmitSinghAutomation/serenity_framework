package utils;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import logger.Log;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

import java.io.File;
import java.util.Map;

public class ApiUtils {

    public ApiUtils()
    {
        System.out.println("runLocally: " +runLocally);
        if(this.runLocally.toUpperCase().equals("Yes"))
        {
            System.out.println("This proxy hostname: "+this.proxyHostName);
            System.out.println("This proxy port name: "+this.proxyHostPort);
            RestAssured.proxy(this.proxyHostName,this.proxyHostPort,"https");
            RestAssured.proxy(this.proxyHostName,this.proxyHostPort,"http");
        }
    }

    public EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    public String proxyHostName = this.variables.getProperty("proxyHost");
    public int proxyHostPort = Integer.valueOf(this.variables.getProperty("proxyPort"));
    public String runLocally = this.variables.getProperty("runLocally");

    public void setProxy(String host, int port, String protocol)
    {
       RestAssured.proxy(host,port,protocol);
    }
    /* This is a rest api GET method which return status code and response body with headers and encoding*/
    public Response getRequest(String baseURL, String endPoint, Map<String, Object> headers)
    {
        System.out.println("<-- Inside getRequest method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        if(headers != null)
        {
            Log.info("In not null - Headers");
            requestSpecification.headers(headers);
            requestSpecification.relaxedHTTPSValidation();
        }
        try
        {
            Log.info("<-- Endpoint --> "+ endPoint);
            requestSpecification.relaxedHTTPSValidation();
            response = requestSpecification.urlEncodingEnabled(false).when().baseUri(baseURL).get(endPoint);
            return response;
        }catch (Exception e)
        {
            return null;
        }
    }
    /* This is a rest api GET method which return status code and response body with headers*/
    public Response getRequestWithHeaders(String baseURL, String endPoint, Map<String, String> headers)
    {
        System.out.println("<-- Inside getRequestWithHeaders method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        if(headers != null)
        {
            Log.info("In not null - Headers");
            requestSpecification.headers(headers);
            requestSpecification.relaxedHTTPSValidation();
        }
        try
        {
            Log.info("<-- Endpoint --> "+ endPoint);
            requestSpecification.relaxedHTTPSValidation();
            response = requestSpecification.urlEncodingEnabled(false).when().baseUri(baseURL).get(endPoint);
            return response;
        }catch (Exception e)
        {
            return null;
        }
    }
    /* This is a rest api GET method which return status code and response body with headers and query parameter without encoding*/
    public Response getRequestWithHeadersAndParametersWithoutEncoding(String baseURL, String endPoint, Map<String, String> headers, Map<String, String> queryParams)
    {
        System.out.println("<-- Inside getRequestWithHeadersAndParametersWithoutEncoding method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        if(headers != null)
        {
            Log.info("In not null - Headers");
            requestSpecification.headers(headers);
            requestSpecification.relaxedHTTPSValidation();
        }
        if(queryParams != null)
        {
            Log.info("In not null - Query Params");
            requestSpecification.queryParams(queryParams);
        }
        try
        {
            Log.info("<-- Endpoint --> "+ endPoint);
            requestSpecification.relaxedHTTPSValidation();
            response = requestSpecification.when().baseUri(baseURL).get(endPoint);
            return response;
        }catch (Exception e)
        {
            return null;
        }
    }
    /* This is a rest api GET method which return status code and response body with headers and without encoding*/
    public Response getRequestWithHeadersWithoutEncoding(String baseURL, String endPoint, Map<String, String> headers)
    {
        System.out.println("<-- Inside getRequestWithHeadersWithoutEncoding method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        if(headers != null)
        {
            Log.info("In not null - Headers");
            requestSpecification.headers(headers);
            requestSpecification.relaxedHTTPSValidation();
        }
        try
        {
            Log.info("<-- Endpoint --> "+ endPoint);
            requestSpecification.relaxedHTTPSValidation();
            response = requestSpecification.when().baseUri(baseURL).get(endPoint);
            return response;
        }catch (Exception e)
        {
            return null;
        }
    }
    /* This is a rest api GET method which return status code and response body with header and query parameter*/
    public Response getRequestWithHeadersAndParameters(String baseURL, String endPoint, Map<String, String> headers, Map<String, String> queryParams)
    {
        System.out.println("<-- Inside getRequestWithHeaderAndParameters method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        if(headers != null)
        {
            Log.info("In not null - Headers");
            requestSpecification.headers(headers);
            requestSpecification.relaxedHTTPSValidation();
        }
        if(queryParams != null)
        {
            Log.info("In not null - Query params");
            requestSpecification.queryParams(queryParams);
        }
        try
        {
            Log.info("<-- Endpoint --> "+ endPoint);
            requestSpecification.relaxedHTTPSValidation();
            response = requestSpecification.urlEncodingEnabled(false).when().baseUri(baseURL).get(endPoint);
            return response;
        }catch (Exception e)
        {
            return null;
        }
    }
    /* This is a rest api GET method which return status code and response body providing credentials in it*/
    public static Response getRequestWithBasicAuth(String baseURL, String endPoint, Map<String, Object> headers, String userName, String password)
    {
        System.out.println("<-- Inside getRequestWithBasicAuth method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        if(headers != null)
        {
            Log.info("In not null - Headers");
            requestSpecification.headers(headers);
            requestSpecification.relaxedHTTPSValidation();
        }
        try
        {
            Log.info("<-- Endpoint --> "+ endPoint);
            requestSpecification.relaxedHTTPSValidation();
            response = requestSpecification.urlEncodingEnabled(false).when().baseUri(baseURL).auth().basic(userName,password).get(endPoint);
            return response;
        }catch (Exception e)
        {
            return null;
        }
    }
    /* This is a rest api POST method which return status code and response body providing credentials in it*/
    public static Response postRequestWithJsonBodyAndBasicAuth(String baseURL, String endPoint, String inputJson, Map<String, Object> headers, String contentType, String userName, String password)
    {
        System.out.println("<-- Inside postRequestWithJsonBodyAndBasicAuth method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
               requestSpecification.relaxedHTTPSValidation();
               response = requestSpecification.headers(headers).when().baseUri(baseURL).auth().basic(userName,password).contentType(contentType.toLowerCase()).body(inputJson).when().post(endPoint);
            }else
            {
                Log.info("<-- Endpoint --> "+ endPoint);
               response = requestSpecification.when().baseUri(baseURL).contentType(contentType).body(inputJson).post(endPoint);
            }
            return response;
        }catch (Exception e)
        {
          return null;
        }
    }
    /* This is a rest api PUT method which return status code and response body providing credentials in it*/
    public static Response putRequestWithJsonBodyAndBasicAuth(String baseURL, String endPoint, String inputJson, Map<String, Object> headers, String contentType, String userName, String password)
    {
        System.out.println("<-- Inside putRequestWithJsonBodyAndBasicAuth method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
                requestSpecification.relaxedHTTPSValidation();
                response = requestSpecification.headers(headers).when().baseUri(baseURL).auth().basic(userName,password).contentType(contentType.toLowerCase()).body(inputJson).when().put(endPoint);
            }else
            {
                Log.info("<-- Endpoint --> "+ endPoint);
                response = requestSpecification.when().baseUri(baseURL).contentType(contentType).body(inputJson).put(endPoint);
            }
            return response;
        }catch (Exception e)
        {
            return null;
        }
    }
    /*This is a rest api POST method to get status code and response of the body*/
    public Response postRequestWithHeaders(String baseURL, String endPoint, String inputJson, Map<String, String> headers)
    {
        System.out.println("<-- Inside postRequestWithHeaders method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(headers.get("Content-Type"), ContentType.TEXT)));
        try
        {
         if(headers != null)
         {
             Log.info("In not null - Headers");
           requestSpecification.relaxedHTTPSValidation();
           response = requestSpecification.headers(headers).when().baseUri(baseURL).body(inputJson).when().post(endPoint);
         }else
         {
             Log.info("<-- Endpoint --> "+ endPoint);
             response = requestSpecification.when().baseUri(baseURL).body(inputJson).when().post(endPoint);
         }
         return response;
        }catch (Exception e) {
            return null;
        }
    }
    /*This is a rest api PUT method to get status code and response of the body*/
    public Response putRequestWithHeaders(String baseURL, String endPoint, String inputJson, Map<String, String> headers)
    {
        System.out.println("<-- Inside putRequestWithHeaders method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
                requestSpecification.relaxedHTTPSValidation();
                response = requestSpecification.headers(headers).when().baseUri(baseURL).body(inputJson).when().put(endPoint);
            }else
            {
                Log.info("<-- Endpoint --> "+ endPoint);
                response = requestSpecification.when().baseUri(baseURL).body(inputJson).when().put(endPoint);
            }
            return response;
        }catch (Exception e) {
            return null;
        }
    }
    /*This is a rest api PATCH method to get status code and response of the body*/
    public Response patchRequestWithHeaders(String baseURL, String endPoint, String inputJson, Map<String, String> headers)
    {
        System.out.println("<-- Inside patchRequestWithHeaders method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
                requestSpecification.relaxedHTTPSValidation();
                response = requestSpecification.headers(headers).when().baseUri(baseURL).body(inputJson).when().patch(endPoint);
            }else
            {
                Log.info("<-- Endpoint --> "+ endPoint);
                response = requestSpecification.when().baseUri(baseURL).body(inputJson).when().patch(endPoint);
            }
            return response;
        }catch (Exception e) {
            return null;
        }
    }
    /*This is a rest api PATCH method to get status code and response of the body*/
    public Response patchRequestWithHeadersAndParametersWithoutEncoding(String baseURL, String endPoint, Map<String, String> queryParams, Map<String, String> headers)
    {
        System.out.println("<-- Inside patchRequestWithHeadersAndParametersWithoutEncoding method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        if(headers != null)
        {
            Log.info("In not null - Headers");
           requestSpecification.headers(headers);
           requestSpecification.relaxedHTTPSValidation();
        }
        if(queryParams != null)
        {
            Log.info("In not null - Query Params");
          requestSpecification.queryParams(queryParams);
        }
        try
        {
            Log.info("<-- Endpoint --> "+ endPoint);
         requestSpecification.relaxedHTTPSValidation();
         response = requestSpecification.when().baseUri(baseURL).patch(endPoint);
         return response;
        }catch (Exception e) {
            return null;
        }
    }
    /*This is a rest api DELETE method to get status code and response of the body*/
    public Response deleteRequestWithHeaders(String baseURL, String endPoint, Map<String, String> headers)
    {
        System.out.println("<-- Inside deleteRequestWithHeaders method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
                requestSpecification.relaxedHTTPSValidation();
                response = requestSpecification.headers(headers).when().baseUri(baseURL).when().delete(endPoint);
            }else
            {
                Log.info("<-- Endpoint --> "+ endPoint);
                response = requestSpecification.when().baseUri(baseURL).when().delete(endPoint);
            }
            return response;
        }catch (Exception e) {
            return null;
        }
    }
    /*This is a rest api DELETE method to get status code and response of the body*/
    public Response deleteRequestWithHeadersAndInputBody(String baseURL, String endPoint, Map<String, String> headers, String inputJson)
    {
        System.out.println("<-- Inside deleteRequestWithHeadersAndInputBody method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
                requestSpecification.relaxedHTTPSValidation();
                response = requestSpecification.headers(headers).when().baseUri(baseURL).body(inputJson).when().delete(endPoint);
            }else
            {
                Log.info("<-- Endpoint --> "+ endPoint);
                response = requestSpecification.when().baseUri(baseURL).body(inputJson).when().delete(endPoint);
            }
            return response;
        }catch (Exception e) {
            return null;
        }
    }
    /*This is a rest api DELETE method to get status code and response of the body*/
    public Response deleteRequestWithHeadersAndQueryParams(String baseURL, String endPoint, Map<String, String> headers,Map<String, String> queryParams)
    {
        System.out.println("<-- Inside deleteRequestWithHeadersAndQueryParams method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
                requestSpecification.relaxedHTTPSValidation();
                requestSpecification.headers(headers);
            }
            if(queryParams != null)
            {
                Log.info("In not null - Query Params");
                requestSpecification.queryParams(queryParams);
            }
                Log.info("<-- Endpoint --> "+ endPoint + queryParams);
                response = requestSpecification.when().baseUri(baseURL).when().delete(endPoint);

            return response;
        }catch (Exception e) {
            return null;
        }
    }
    /*This is a rest api PUT method to upload the image using query parameter*/
    public Response putRequestWithHeadersImageUploadQueryParams(String baseURL, String endPoint, Map<String, String> headers,Map<String, String> queryParams, String imagePath)
    {
        System.out.println("<-- Inside putRequestWithHeadersImageUploadQueryParams method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
                requestSpecification.relaxedHTTPSValidation();
                requestSpecification.headers(headers);
            }
            if(queryParams != null)
            {
                Log.info("In not null - Query Params");
                requestSpecification.queryParams(queryParams);
            }
            Log.info("<-- Endpoint --> "+ endPoint + queryParams);
            response = requestSpecification.multiPart(new File(imagePath)).when().baseUri(baseURL).when().put(endPoint);

            return response;
        }catch (Exception e) {
            return null;
        }
    }
    /*This is a rest api PATCH method to upload the image using query parameter*/
    public Response patchRequestWithHeadersImageUpload(String baseURL, String endPoint, Map<String, String> headers,String imageType, String imagePath)
    {
        System.out.println("<-- Inside patchRequestWithHeadersImageUpload method -->");
        Log.info("<-- Before Calling --> "+ baseURL + endPoint);
        Response response = null;
        RequestSpecification requestSpecification = SerenityRest.given();
        try
        {
            if(headers != null)
            {
                Log.info("In not null - Headers");
                requestSpecification.relaxedHTTPSValidation();
                requestSpecification.headers(headers);
            }
            Log.info("<-- Endpoint --> "+ endPoint);
            response = requestSpecification.multiPart(imageType,new File(imagePath)).when().baseUri(baseURL).when().patch(endPoint);

            return response;
        }catch (Exception e) {
            return null;
        }
    }
}
