package com.test.automation.framework.saucedemo;

import org.testng.annotations.Test;

import com.test.automation.framework.core.Browser;
import com.test.automation.framework.core.Log;
import com.test.automation.framework.dataobjects.saucedemo.SauceDemo_Data;
import com.test.automation.framework.pageobjects.saucedemo.LoginPage;
import com.test.automation.framework.pageobjects.saucedemo.HomePage;


public class SmokeTesting extends Browser{
	
	@Test
	public static void TC0003() throws Exception{
		
		Log.setStoryName("SwagLabs Sauce Demo");
		Log.setTestScriptName("Purchase Swag Labs products");
		Log.setTestScriptDescription("User is purchases Swag Labs products");

		LoginPage.LoginSection.loginUser(SauceDemo_Data.TestDataTC0001.getUsername(), SauceDemo_Data.TestDataTC0001.getPassword());
		HomePage.Header.verifySwagLabsLogo();
		HomePage.InventoryItems.clickAddToCart(SauceDemo_Data.TestDataTC0001.getProduct());
		HomePage.Header.clickCart();

		Thread.sleep(5000);
		
	}
	
}
