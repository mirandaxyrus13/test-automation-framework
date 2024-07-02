package com.test.automation.framework.core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Browser {

    protected static String browser;
    private static FileInputStream fileInputStream;
    private static Properties properties;
    private static String environment;
    private static String url;
    public ExtentReports extent;
    public ExtentSparkReporter spark;
    public static ExtentTest test;
    public static WebDriver driver;
    private static Integer searchLoop = 1;

    private static String downloadFilepath = System.getProperty("user.dir").replace("\\", "/")
            + "/src/main/resources/downloads/";
    private static String extentSparkConfigPath = System.getProperty("user.dir").replace("\\", "/")
            + "/src/main/resources/Config/spark-config.xml";
    private static String extentSparkReportPath = System.getProperty("user.dir").replace("\\", "/")
            + "/target/extent-reports/SauceDemo_ " + DateUtilities.getCurrentDate() + "_" + DateUtilities.getTimeStamp()
            + "_" + "ExtentSparkReports.html";

    private static ArrayList<String> tabs;
    private static int tabCounter = 0;

    public static Duration setWaitDurationTime(){
        Duration waitDuration = Duration.ofSeconds(10);
        return waitDuration;
    }
    public static void delay(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {

        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    private static void getBrowser() throws Exception {
        browser = System.getProperty("browserInstance").toString();
    }

    public static String getBrowserProperty() throws Exception {
        fileInputStream = new FileInputStream(
                System.getProperty("user.dir").replace("\\", "/") + "/src/main/resources/AppConfig/config.properties");
        properties = new Properties();
        properties.load(fileInputStream);
        String browserValue = properties.getProperty("browser").toString();
        return browserValue;
    }

    public static void waitForBrowserToLoadCompletely(WebDriver driver) {
        String state = null;
        String oldstate = null;
        try {
            int i = 0;
            while (i < 5) {
                Thread.sleep(1000);
                state = ((JavascriptExecutor) driver).executeScript("return document.readyState;").toString();
                if (state.equals("interactive") || state.equals("loading"))
                    break;
                if (i == 1 && state.equals("complete")) {
                    // Log.setLog("Browser loading complete");
                    return;
                }
                i++;
            }
            i = 0;
            oldstate = null;
            Thread.sleep(1000);
            while (true) {
                state = ((JavascriptExecutor) driver).executeScript("return document.readyState;").toString();
                if (state.equals("complete"))
                    break;
                if (state.equals(oldstate))
                    i++;
                else
                    i = 0;
                if (i == 15 && state.equals("loading")) {
                    driver.navigate().refresh();
                    i = 0;
                } else if (i == 6 && state.equals("interactive")) {
                    return;
                }
                Thread.sleep(1000);
                oldstate = state;
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public static WebElement findElement(By by) throws WebDriverException {
        WebElement element = null;
        int i = 0;
        int attempts = 0;
        waitForBrowserToLoadCompletely(getDriver());
        while (i != searchLoop && element == null) {
            i += 1;
            try {
                element = new WebDriverWait(getDriver(), setWaitDurationTime()).ignoring(StaleElementReferenceException.class)
                        .until(ExpectedConditions.presenceOfElementLocated(by));
            } catch (Exception e) {
                element = null;
            }
        }
        if (element != null) {
            try {
                while (attempts < 2) {
                    try {
                        Actions actions = new Actions(getDriver());
                        actions.moveToElement(element);
                        actions.perform();
                    } catch (StaleElementReferenceException e) {
                    }
                    attempts++;
                }
            } catch (Exception e) {
            }
        }
        return element;
    }

    public static List<WebElement> findElements(By by) throws WebDriverException {
        List<WebElement> elements = null;
        int i = 0;
        int attempts = 0;
        while (i != searchLoop && elements.size() == 0) {
            i += 1;
            try {
                elements = new WebDriverWait(getDriver(), setWaitDurationTime()).ignoring(StaleElementReferenceException.class)
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
            } catch (Exception e) {
                elements = null;
            }
        }
        if (elements != null) {
            try {
                while (attempts < 2) {
                    try {
                        Actions actions = new Actions(getDriver());
                        actions.moveToElement(elements.get(0));
                        actions.perform();
                    } catch (StaleElementReferenceException e) {
                    }
                    attempts++;
                }
            } catch (Exception e) {
            }
        }
        return elements;
    }

    public static List<WebElement> findElements(By parentBy, By childBy) throws WebDriverException {
        WebElement element = null;
        List<WebElement> elements = null;
        int i = 0;
        int attempts = 0;
        while (i != searchLoop && elements.size() == 0) {
            i += 1;
            try {
                element = new WebDriverWait(getDriver(), setWaitDurationTime()).ignoring(StaleElementReferenceException.class)
                        .until(ExpectedConditions.presenceOfElementLocated(parentBy));
                elements = element.findElements(childBy);
            } catch (Exception e) {
                elements = null;
            }
        }
        if (elements != null) {
            try {
                while (attempts < 2) {
                    try {
                        Actions actions = new Actions(getDriver());
                        actions.moveToElement(elements.get(0));
                        actions.perform();
                    } catch (StaleElementReferenceException e) {
                    }
                    attempts++;
                }
            } catch (Exception e) {
            }
        }
        return elements;
    }

    public static int getTabCount() {
        return new ArrayList<String>(getDriver().getWindowHandles()).size();
    }

    public static void switchTab() {
        // TODO switch tab
        tabs = new ArrayList<String>(driver.getWindowHandles());

        if (tabCounter == 0) {
            getDriver().switchTo().window(tabs.get(1));
            Log.testStep("INFO", "Switched to another Tab", "Switched to another Tab");
            tabCounter = 1;
        } else if (tabCounter == 1) {
            getDriver().close();
            Log.testStep("INFO", "Switched to Main Tab", "Switched to Main Tab");
            getDriver().switchTo().window(tabs.get(0));
        }

    }

    public static void navigateBackward() throws IOException {
        getDriver().navigate().back();
        Log.testStep("PASSED", "Navigate Backward", "Navigate Backward");
    }

    protected static void navigateForward() throws IOException {
        getDriver().navigate().forward();
        Log.testStep("PASSED", "Navigate Forward", "Navigate Forward");
    }

    public static void openBrowser() throws Exception {
        getBrowser();
        if (browser.equalsIgnoreCase("chrome")) {
            if (OSChecker.isWindows()) {
                WebDriverManager.chromedriver().setup();
                Log.testStep("INFO", "Setting up ChromeDriver... Running in Windows OS",
                        "Setting up ChromeDriver... Running in Windows OS");
            } else if (OSChecker.isMac()) {
                WebDriverManager.chromedriver().setup();
                Log.testStep("INFO", "Setting up ChromeDriver... Running in Mac OS",
                        "Setting up ChromeDriver... Running in Mac OS");
            }
            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.default_directory", downloadFilepath);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("prefs", chromePrefs);
            fileInputStream = new FileInputStream(System.getProperty("user.dir").replace("\\", "/")
                    + "/src/main/resources/AppConfig/config.properties");
            properties = new Properties();
            properties.load(fileInputStream);
            String chromeProfile = properties.getProperty("chrome_profile").toString();
            if (chromeProfile.equals("true")) {
                chromeOptions.addArguments("--user-data-dir=C:\\Browser");
                chromeOptions.addArguments("--profile-directory=Profile 5");
            } else {

            }
            chromeOptions.addArguments("start-maximized");
            chromeOptions.addArguments("mute-audio");
            chromeOptions.addArguments("disable-extensions");
            // chromeOptions.addArguments("headless");
            chromeOptions.addArguments("disable-gpu");
            // chromeOptions.addArguments("window-size=1280x1024");
            chromeOptions.setAcceptInsecureCerts(true);
            chromeOptions.setCapability("goog:chromeOptions", ImmutableMap.of("w3c", true));
            chromeOptions.setCapability("goog:chromeOptions", ImmutableMap.of("v126", true));
            chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            chromeOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            driver = new ChromeDriver(chromeOptions);
            Log.testStep("PASSED", "Browser Initialized", "Browser Initialized");
        } else if (browser.equalsIgnoreCase("firefox")) {
            if (OSChecker.isWindows()) {
                WebDriverManager.firefoxdriver().setup();
                Log.testStep("INFO", "Setting up ChromeDriver... Running in Windows OS",
                        "Setting up ChromeDriver... Running in Windows OS");
            } else if (OSChecker.isMac()) {
                WebDriverManager.firefoxdriver().setup();
                Log.testStep("INFO", "Setting up FirefoxDriver/GeckoDriver... Running in Mac OS",
                        "Setting up irefoxDriver/GeckoDriver... Running in Mac OS");
            }
            HashMap<String, Object> fireFoxMap = new HashMap<String, Object>();
            fireFoxMap.put("profile.default_content_settings.popups", 0);
            fireFoxMap.put("download.default_directory", downloadFilepath);
            FirefoxOptions fireFoxOptions = new FirefoxOptions();
            fireFoxOptions.addPreference("browser.download.folderList", 2);
            fireFoxOptions.addPreference("browser.download.dir", downloadFilepath);
            fireFoxOptions.addPreference("browser.fullscreen.autohide", false);
            fireFoxOptions.addPreference("browser.fullscreen.animateUp", 0);
//            fireFoxOptions.addArguments("-headless");  // Run Firefox in headless mode
            fireFoxOptions.addPreference("extensions.enabled", false);
            fireFoxOptions.addPreference("gfx.webrender.all", false);

            // Disable LoginRecipes and other related features
            fireFoxOptions.addPreference("signon.rememberSignons", false);
            fireFoxOptions.addPreference("signon.autofillForms", false);
            fireFoxOptions.addPreference("signon.autologin.proxy", false);
            fireFoxOptions.addPreference("signon.formlessCapture.enabled", false); // Disable formless capture
            fireFoxOptions.addPreference("signon.generation.enabled", false); // Disable password generation
            fireFoxOptions.addPreference("signon.management.page.breach-alerts.enabled", false); // Disable breach alerts
            fireFoxOptions.addPreference("signon.recipes.enabled", false); // Disable login recipes
            fireFoxOptions.addPreference("signon.suggestImportCount", 0); // Disable import suggestion
            fireFoxOptions.addPreference("signon.storeWhenAutocompleteOff", false); // Disable storing passwords when autocomplete is off
            fireFoxOptions.addPreference("signon.debug", false); // Disable debug logs for signon
            // Disable form fill features
            fireFoxOptions.addPreference("browser.formfill.enable", false);
            fireFoxOptions.addPreference("extensions.formautofill.addresses.enabled", false);
            fireFoxOptions.addPreference("extensions.formautofill.creditCards.enabled", false);
            // Disable other unnecessary features for a clean testing environment
            fireFoxOptions.addPreference("dom.webnotifications.enabled", false); // Disable web notifications
            fireFoxOptions.addPreference("media.volume_scale", "0.0"); // Mute audio
            fireFoxOptions.addPreference("fission.webContentIsolationStrategy", 0);
            fireFoxOptions.addPreference("fission.bfcacheInParent", false);
            fireFoxOptions.setAcceptInsecureCerts(true);
            fireFoxOptions.setCapability("moz:firefoxOptions", true);
            driver = new FirefoxDriver(fireFoxOptions);
            Log.testStep("PASSED", "Browser Initialized", "Browser Initialized");
        } else if (browser.equalsIgnoreCase("edge")) {
            //TODO
        } else{
            Log.testStep("INFO", "Browser Instance is null",
                    "Browser Instance is null");
        }
    }

    private static void setEnvironment() throws Exception {
        environment = System.getProperty("environment").toString();
        if (environment.equals("dev")) {
            DataAccessLayer.setSheetName(environment);
            Log.testStep("INFO", "Setting the environment to " + environment,
                    "Setting the environment to " + environment);
        } else if (environment.equals("test")) {
            DataAccessLayer.setSheetName(environment);
            Log.testStep("INFO", "Setting the environment to " + environment,
                    "Setting the environment to " + environment);
        } else if (environment.equals("staging")) {
            DataAccessLayer.setSheetName(environment);
            Log.testStep("INFO", "Setting the environment to " + environment,
                    "Setting the environment to " + environment);
        } else{
            DataAccessLayer.setSheetName(environment);
            Log.testStep("INFO", "Setting the environment to " + environment,
                    "Setting the environment to " + environment);
        }
    }

    private static void openApplication() throws Exception {
        fileInputStream = new FileInputStream(
                System.getProperty("user.dir").replace("\\", "/") + "/src/main/resources/AppConfig/config.properties");
        properties = new Properties();
        properties.load(fileInputStream);
        if (environment.equals("dev")) {
            url = properties.getProperty("dev_url").toString();
            Log.testStep("INFO", "Accessing " + url, "Accessing " + url);
        } else if (environment.equals("test")) {
            url = properties.getProperty("test_url").toString();
            Log.testStep("INFO", "Accessing " + url, "Accessing " + url);
        } else if (environment.equals("staging")) {
            url = properties.getProperty("staging_url").toString();
            Log.testStep("INFO", "Accessing " + url, "Accessing " + url);
        }
        getDriver().get(url);
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() throws IOException {
        extent = new ExtentReports();
        spark = new ExtentSparkReporter(extentSparkReportPath);
        final File CONF = new File(extentSparkConfigPath);
        spark.loadXMLConfig(CONF);
        extent.attachReporter(spark);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) throws Exception {
        test = extent.createTest(this.getClass().getSimpleName() + "::" + method.getName().toString())
                .assignCategory("Smoke").assignDevice(getBrowserProperty());
        setEnvironment();
        openBrowser();
        openApplication();
        getDriver().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result, Method method) throws Exception {
        if (ITestResult.FAILURE == result.getStatus()) {
            Screenshot.capture("Failed_" + result.getName() + "_");
            test.addScreenCaptureFromPath(Screenshot.getpath() + Screenshot.getname());
            test.fail(result.getThrowable());
        } else if (ITestResult.SUCCESS == result.getStatus()) {
            Screenshot.capture("Passed_" + result.getName() + "_");
            test.addScreenCaptureFromPath(Screenshot.getpath() + Screenshot.getname());
        }
        if (getDriver() != null) {
            getDriver().quit();
            Log.setLog(browser.replace("grid_", "") + " instance closed.");
        }
        Log.testStep("INFO", "Browser instance closed", "Browser instance closed");
        Log.testStep("INFO",
                "Test Execution of " + this.getClass().getSimpleName() + "::" + method.getName().toString()
                        + " completed",
                "Test Execution of " + this.getClass().getSimpleName() + "::" + method.getName().toString()
                        + " completed");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        extent.flush();
    }
}
