package com.test.automation.framework.api;

import com.test.automation.framework.core.API;
import org.json.JSONObject;

public class OpenWeather extends API {

    public static void setOpenWeatherEndpoint(String endpoint) throws Exception {
        setRequestEndpoint(endpoint);
    }

    public static void setOpenWeatherHeaders(String headerKey, String headerValue) throws Exception {
        String[] headerKeyList = headerKey.split("\\|");
        String[] headerValueList = headerValue.split("\\|");
        setRequestHeaders(headerKeyList, headerValueList);
    }

    public static void setOpenWeatherParameters(String parameterKey, String parameterValue) throws Exception {
        String[] parameterKeyList = parameterKey.split("\\|");
        String[] parameterValueList = parameterValue.split("\\|");
        setRequestParameters(parameterKeyList, parameterValueList);
    }

    public static void triggerOpenWeatherRequest(String method) throws Exception {
        triggerRequestAndRetrieveResponse(method);
    }

    public static void validateResponseStatusCode(String statusCode) throws Exception {
        int responseStatusCode = Integer.parseInt(statusCode);
        assertStatusCode(responseStatusCode);
    }

    public static void validateResponseBody(String fieldKey, String fieldValue) throws Exception {
        String[] fieldKeyList = fieldKey.split("\\|");
        String[] fieldValueList = fieldValue.split("\\|");
        assertResponseBody(fieldKeyList, fieldValueList);
    }

    public static void getNestedKeyValue(){
        // Access nested value
        Object nestedValue = getNestedValue((JSONObject) returnResponseJSONObject(),"weather", 1, "id");
        System.out.println("Nested value: " + nestedValue);
    }



}
