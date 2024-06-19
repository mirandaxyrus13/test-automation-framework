package com.test.automation.framework.saucedemo;

import com.test.automation.framework.core.API;
import com.test.automation.framework.core.Log;
import org.testng.annotations.Test;

public class ContractTesting extends API {

    @Test
    public static void TC0003() throws Exception{
        Log.setStoryName("OpenWeather API");
        Log.setTestScriptName("Get and Verify Weather Details");
        Log.setTestScriptDescription("Verify if user is able to retrieve weather data");

//        OpenWeather.setOpenWeatherEndpoint("/data/2.5/weather");
//        OpenWeather.setOpenWeatherHeaders("x-api-key","ac1634cd0369751c55eb8d24aadaa8a7");
//        OpenWeather.setOpenWeatherParameters("q|units", "New York City|metric");
//        OpenWeather.triggerOpenWeatherRequest("get_method");
//        OpenWeather.validateResponseStatusCode("200");
//        OpenWeather.getNestedKeyValue();
    }







}
