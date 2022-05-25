package com.test.automation.framework.webelements;

import com.test.automation.framework.core.Browser;
import com.test.automation.framework.core.Browser;

public class Alerts extends Browser {

	public static void acceptAlert() {
		driver.switchTo().alert().accept();
	}

}
