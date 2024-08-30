package global;

import io.restassured.response.Response;

public class GlobalResponse {

    private static Response response = null;

    public static Response getResponse() {
        return response;
    }

    public static void setResponse(Response response) {
        GlobalResponse.response = response;
    }
}
