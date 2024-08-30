package global;

import java.util.HashMap;

public class GlobalRequest {

    private static HashMap<String, String> headers = new HashMap<String, String>();
    private static HashMap<String, String> queryParams = new HashMap<String, String>();
    private static String organizationName, locationName, siteName, deviceName, deviceChannelName;
    private static String version = null;

    public static HashMap<String, String> getQueryParams() {
        return queryParams;
    }

    public static void setQueryParams(String queryParamsName, String queryParamsValue) {
        GlobalRequest.queryParams.put(queryParamsName,queryParamsValue);
    }

    public static String getOrganizationName() {
        return organizationName;
    }

    public static void setOrganizationName(String organizationName) {
        GlobalRequest.organizationName = organizationName;
    }

    public static String getLocationName() {
        return locationName;
    }

    public static void setLocationName(String locationName) {
        GlobalRequest.locationName = locationName;
    }

    public static String getSiteName() {
        return siteName;
    }

    public static void setSiteName(String siteName) {
        GlobalRequest.siteName = siteName;
    }

    public static String getDeviceName() {
        return deviceName;
    }

    public static void setDeviceName(String deviceName) {
        GlobalRequest.deviceName = deviceName;
    }

    public static String getDeviceChannelName() {
        return deviceChannelName;
    }

    public static void setDeviceChannelName(String deviceChannelName) {
        GlobalRequest.deviceChannelName = deviceChannelName;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        GlobalRequest.version = version;
    }

    public static HashMap<String, String> getHeaders() {
        return headers;
    }

    public static void setHeaders(String headerName, String headerValue) {
        GlobalRequest.headers.put(headerName,headerValue);
                if(headerName.toUpperCase().equals("VERSION"))
                {
                    version = headerValue;
                }
    }
}
