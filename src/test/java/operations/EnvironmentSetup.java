package operations;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import utilities.PathUtility;

public class EnvironmentSetup {
	// initialise driver
	protected static WebDriver DRIVER;

	// instantiate driver
	public static void setDriver(String browser, String url) {
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", PathUtility.CHROME_DRIVER_PATH);
			DRIVER = new ChromeDriver();
			openWebPage(url);
			break;

		case "ie":
			System.setProperty("webdriver.ie.driver", PathUtility.IE_DRIVER_PATH);
			DRIVER = new InternetExplorerDriver();
			openWebPage(url);
			break;

		case "firefox":
			System.setProperty("webdriver.gecko.driver", PathUtility.FIREFOX_DRIVER_PATH);
			DRIVER = new FirefoxDriver();
			openWebPage(url);
			break;
		}

	}

	// open web page
	public static void openWebPage(String webURL) {
		DRIVER.get(webURL);
		DRIVER.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		DRIVER.manage().window().maximize();
	}

	// return driver
	public WebDriver getDriver() {
		return DRIVER;
	}
}
