package com.test.automation.framework.core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class API {

    private static Map<String, String> headers = new HashMap<>();
    private static Map<String, String> parameters = new HashMap<>();
    private static Response response;
    private static String endpoint;
    private static String apiBaseURL;
    private static String requestBody;
    private static String method;
    private static String responseBody;
    private static int responseStatusCode;
    private static String responseStatusLine;
    private static FileInputStream fileInputStream;
    private static Properties properties;
    private static Response responseAPI;
    protected static ExtentTest test;
//    public static ExtentSparkReporter spark;
//    public static ExtentReports extent;
    private static String environment;
//    private static String extentSparkConfigPath = System.getProperty("user.dir").replace("\\", "/")
//            + "/src/main/resources/Config/spark-config.xml";
//    private static String extentSparkReportPath = System.getProperty("user.dir").replace("\\", "/")
//            + "/target/extent-reports/ContractTesting_" + DateUtilities.getCurrentDate() + "_" + DateUtilities.getTimeStamp()
//            + "_" + "ExtentSparkReports.html";

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
//        extent = new ExtentReports();
//        spark = new ExtentSparkReporter(extentSparkReportPath);
//        final File CONF = new File(extentSparkConfigPath);
//        spark.loadXMLConfig(CONF);
//        extent.attachReporter(spark);
        Log.declareExtentReportConfigurations();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) throws Exception {
        test = Log.getExtent().createTest(this.getClass().getSimpleName() + "::" + method.getName().toString())
                .assignCategory("Automation Testing");
        Log.setExtentTest(test);
        //TODO: ExtentTest test instance should be in Log.java
        setEnvironment();
        setAPIBaseURL(null);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result, Method method) throws Exception {
        if (ITestResult.FAILURE == result.getStatus()) {
            Log.getExtentTest().fail(result.getThrowable());
        } else if (ITestResult.SUCCESS == result.getStatus()) {

        }

        Log.testStep("INFO",
                "Test Execution of " + this.getClass().getSimpleName() + "::" + method.getName().toString()
                        + " completed",
                "Test Execution of " + this.getClass().getSimpleName() + "::" + method.getName().toString()
                        + " completed");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        Log.getExtent().flush();
    }

    public static void setAPIBaseURL(String url) throws Exception{
        String environment = System.getProperty("environment");
        if(url != null){
            RestAssured.baseURI = url;
            Log.testStep("INFO", "Setting the API environment to " + environment,
                    "Setting the API environment to " + environment);
        } else if (environment.equals("dev")) {
            fileInputStream = new FileInputStream(
                    System.getProperty("user.dir").replace("\\", "/") + "/src/main/resources/AppConfig/config.properties");
            properties = new Properties();
            properties.load(fileInputStream);
            apiBaseURL = properties.getProperty("api_dev_url").toString();
            RestAssured.baseURI = apiBaseURL;
            Log.testStep("INFO", "Setting the API environment to " + environment,
                    "Setting the API environment to " + environment);
            Log.testStep("INFO", "Setting the API Base URL to " + apiBaseURL,
                    "Setting the API Base URL to " + apiBaseURL);
        } else if (environment.equals("test")) {
            fileInputStream = new FileInputStream(
                    System.getProperty("user.dir").replace("\\", "/") + "/src/main/resources/AppConfig/config.properties");
            properties = new Properties();
            properties.load(fileInputStream);
            apiBaseURL = properties.getProperty("api_test_url").toString();
            RestAssured.baseURI = apiBaseURL;
            Log.testStep("INFO", "Setting the API environment to " + environment,
                    "Setting the API environment to " + environment);
            Log.testStep("INFO", "Setting the API Base URL to " + apiBaseURL,
                    "Setting the API Base URL to " + apiBaseURL);
        }
    }

    public static void setRequestEndpoint(String value) throws Exception {
        fileInputStream = new FileInputStream(
                System.getProperty("user.dir").replace("\\", "/") + "/src/main/resources/AppConfig/config.properties");
        properties = new Properties();
        properties.load(fileInputStream);
        endpoint = properties.getProperty(value);
        Log.testStep("INFO", "Setting the API endpoint to " + endpoint,
                "Setting the API endpoint to " + endpoint);
    }
    public static void setRequestParameters(String[] parameterNames, String[] parameterValues){
        for(int pidx = 0; pidx < parameterNames.length; pidx++){
            parameters.put(parameterNames[pidx], parameterValues[pidx]);
            Log.testStep("INFO", "Setting Request parameters: " + parameters,
                    "Setting Request parameters: " + parameters);
        }
    }

    public static void setRequestHeaders(String[] headerNames, String[] headerValues) {
        for(int hidx = 0; hidx < headerNames.length; hidx++){
            headers.put(headerNames[hidx], headerValues[hidx]);
            Log.testStep("INFO", "Setting Request headers: " + headerNames[hidx] + " = ************************",
                    "Setting Request headers: " + headerNames[hidx] + " = ************************");
        }
    }

    public static void setRequestBody(String body){
        requestBody = body;
        Log.testStep("INFO", "Setting Request body: " + requestBody,
                "Setting Request body: " + requestBody);
    }

    public static Response triggerRequest(String methodValue) throws Exception {

        fileInputStream = new FileInputStream(
                System.getProperty("user.dir").replace("\\", "/") + "/src/main/resources/AppConfig/config.properties");
        properties = new Properties();
        properties.load(fileInputStream);
        method = properties.getProperty(methodValue).toString();
        System.out.println("METHOD is " + method);

        if(method.equalsIgnoreCase("GET")){
            if(parameters.isEmpty()){
                response = RestAssured.given()
                        .headers(headers)
                        .when()
                        .get(endpoint);
            }else{
                response = RestAssured.given()
                        .headers(headers)
                        .params(parameters)
                        .when()
                        .get(endpoint);
            }

        } else if (method.equalsIgnoreCase("POST")){
            if(parameters.isEmpty()){
                response = RestAssured.given()
                        .headers(headers)
                        .body(requestBody)
                        .when()
                        .post(endpoint);
            }else {
                response = RestAssured.given()
                        .headers(headers)
                        .params(parameters)
                        .body(requestBody)
                        .when()
                        .post(endpoint);
            }

        } else if (method.equalsIgnoreCase("PUT")){
            if(parameters.isEmpty()){
                response = RestAssured.given()
                        .headers(headers)
                        .body(requestBody)
                        .when()
                        .put(endpoint);
            }else {
                response = RestAssured.given()
                        .headers(headers)
                        .params(parameters)
                        .body(requestBody)
                        .when()
                        .put(endpoint);
            }
        } else if (method.equalsIgnoreCase("PATCH")){
            if(parameters.isEmpty()){
                response = RestAssured.given()
                        .headers(headers)
                        .body(requestBody)
                        .when()
                        .patch(endpoint);
            }else {
                response = RestAssured.given()
                        .headers(headers)
                        .params(parameters)
                        .body(requestBody)
                        .when()
                        .patch(endpoint);
            }
        } else if (method.equalsIgnoreCase("DELETE")){
            if(parameters.isEmpty()){
                response = RestAssured.given()
                        .headers(headers)
                        .body(requestBody)
                        .when()
                        .delete(endpoint);
            }else {
                response = RestAssured.given()
                        .headers(headers)
                        .params(parameters)
                        .body(requestBody)
                        .when()
                        .delete(endpoint);
            }
        }

        return response;
    }

    public static void triggerRequestAndRetrieveResponse(String method) throws Exception{

        responseAPI = triggerRequest(method);

        responseStatusCode = responseAPI.getStatusCode();
        Log.testStep("INFO", "Response Status Code: " + responseStatusCode,
                "Response Status Code: " + responseStatusCode);
        responseStatusLine = responseAPI.getStatusLine();
        Log.testStep("INFO", "Response Status Line: " + responseStatusLine,
                "Response Status Line: " + responseStatusLine);
        responseBody = responseAPI.getBody().jsonPath().prettyPrint();
        Log.testStep("INFO", "Response Body: " + responseBody,
                "Response Status Code: " + responseBody);

    }

    public static int getResponseStatusCode(){
        return responseStatusCode;

    }

    public static String getResponseBody(){
        return responseBody;
    }

    public static void assertStatusCode(int statusCode){
        try{
            response.then().assertThat().statusCode(statusCode);
            Log.testStep("PASSED", "Response Status Code is " + responseStatusCode,  "Response Status Code is " + statusCode);
        }catch(Exception e){
            Log.testStep("FAILED", "Response Status Code is " + responseStatusCode,  "Response Status Code should be " + statusCode);
        }
    }

    public static void assertResponseBody(String[] jsonField, String[] jsonValue){
        for(int idx = 0; idx < jsonField.length; idx++){
            assertDynamicValue(responseBody, jsonField[idx], jsonValue[idx]);
        }
    }

    // Method to perform dynamic assertions for nested paths
    private static <T> void assertDynamicValue(String responseBody, String nestedPath, T expectedValue) {
        try{
            // Perform assertions using Hamcrest matchers and JSONPath expressions
            org.hamcrest.MatcherAssert.assertThat(
                    "Dynamic assertion PASSED for nested path: " + nestedPath,
                    responseBody, Matchers.containsString("\"" + nestedPath + "\": \"" + expectedValue + "\""));
//            Log.testStep("PASSED", ",  "Response Status Code is " + statusCode);
        }catch(Exception e){
//            Log.testStep("FAILED", "Response Status Code is " + responseStatusCode,  "Response Status Code should be " + statusCode);
        }
    }

    public static String getKeyValue(String keyName){
        JSONObject jsonObject = new JSONObject(responseBody);
        String keyValue = jsonObject.getString(keyName);
        return keyValue;
    }

    public static Object returnResponseJSONObject(){
        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse;
    }

    public static Object getNestedValue(JSONObject jsonObject, Object... keys) {

        // Parse JSON response
        JSONObject currentObject = jsonObject;

        for (int i = 0; i < keys.length - 1; i++) {
            Object key = keys[i];
            if (currentObject.has(key.toString())) {
                Object element = currentObject.get(key.toString());
                if (element instanceof JSONObject) {
                    currentObject = (JSONObject) element;
                } else if (element instanceof JSONArray) {
                    int index = (int) keys[i + 1];
                    return getNestedValueFromArray((JSONArray) element, index, keys, i + 2);
                } else {
                    return null; // Key not found or incorrect type
                }
            } else {
                return null; // Key not found
            }
        }

        // Last key
        String lastKey = keys[keys.length - 1].toString();
        if (currentObject.has(lastKey)) {
            return currentObject.get(lastKey);
        } else {
            return null; // Key not found
        }
    }

    // Method to get nested value from array
    private static Object getNestedValueFromArray(JSONArray jsonArray, int index, Object[] keys, int nextIndex) {
        if (index < jsonArray.length()) {
            Object arrayElement = jsonArray.get(index);
            if (arrayElement instanceof JSONObject && nextIndex < keys.length) {
                System.out.println("arrayElement = " + arrayElement);
                System.out.println("keys[nextIndex] = " + keys[nextIndex]);
                return getNestedValue((JSONObject) arrayElement, keys[nextIndex]);
            } else {

                return arrayElement;
            }
        } else {
            return null; // Index out of bounds
        }
    }

    private static void setEnvironment() throws Exception {
        environment = System.getProperty("environment").toString();
        if (environment.equals("dev")) {
//            DataAccessLayer.setSheetName(environment);
//            Log.testStep("INFO", "Setting the environment to " + environment,
//                    "Setting the environment to " + environment);
            Log.getExtentTest().info("Setting the environment to " + environment);
        } else if (environment.equals("test")) {
//            DataAccessLayer.setSheetName(environment);
//            Log.testStep("INFO", "Setting the environment to " + environment,
//                    "Setting the environment to " + environment);
            Log.getExtentTest().info("Setting the environment to " + environment);
        } else if (environment.equals("staging")) {
//            DataAccessLayer.setSheetName(environment);
//            Log.testStep("INFO", "Setting the environment to " + environment,
//                    "Setting the environment to " + environment);
            Log.getExtentTest().info("Setting the environment to " + environment);
        }
    }

}
