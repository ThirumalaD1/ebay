package operations;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import selectedProduct.ProductDetail;
import utilities.Constants;
import utilities.TakeScreenshot;

public class APIs {
	private WebDriver driver;
	private Properties OR;
	private static short counter = 0;
	private String filename;
	ProductDetail product = new ProductDetail();
	private String temp;

	public APIs(WebDriver driver, Properties or) {
		this.driver = driver;
		this.OR = or;
	}

	// Find element by using object type and value
	public By getObject(String objectName, String objectType) throws Exception {
		// Find by id
		if (objectType.equalsIgnoreCase("ID")) {

			return By.id(OR.getProperty(objectName));
		}
		// Find by xpath
		if (objectType.equalsIgnoreCase("XPATH")) {

			return By.xpath(OR.getProperty(objectName));
		}
		// find by class
		else if (objectType.equalsIgnoreCase("CLASSNAME")) {

			return By.className(OR.getProperty(objectName));

		}
		// find by name
		else if (objectType.equalsIgnoreCase("NAME")) {

			return By.name(OR.getProperty(objectName));

		}
		// Find by css
		else if (objectType.equalsIgnoreCase("CSS")) {

			return By.cssSelector(OR.getProperty(objectName));

		}
		// find by link
		else if (objectType.equalsIgnoreCase("LINK")) {

			return By.linkText(OR.getProperty(objectName));

		}
		// find by partial link
		else if (objectType.equalsIgnoreCase("PARTIALLINK")) {

			return By.partialLinkText(OR.getProperty(objectName));

		}
		// Find by tagname
		else if (objectType.equalsIgnoreCase("TAGNAME")) {

			return By.tagName(OR.getProperty(objectName));
		}
		// Find by text
		else if (objectType.equalsIgnoreCase("TEXT")) {

			return By.tagName(OR.getProperty(objectName));
		} else {
			System.out.println(objectName);
			throw new Exception("Wrong object type");
		}
	}

	// save screenshot

	public void saveScreenshot(String filename) {
		TakeScreenshot.captureScreenshot(driver, filename);
		System.out.println("Inside " + filename);
	}

	// enter text into the text box
	public String setText(String testcase, String object, String type, String value) throws IOException {

		try {
			driver.findElement(getObject(object, type)).sendKeys(value);
			saveScreenshot(testcase);
			return Constants.PASS;
	
		} catch (Exception e) {
			saveScreenshot(testcase);
			System.out.println(e.getMessage());
			return Constants.FAIL;
		}

	}

	// click button
	public String clickBtn(String testcase, String object, String type, String value) throws IOException {

		try {
			WebElement btn = driver.findElement(getObject(object, type));
			new Actions(driver).moveToElement(btn).perform();
			saveScreenshot(testcase);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			btn.click();

			return Constants.PASS;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			saveScreenshot(testcase);
			return Constants.FAIL;
		}

	}

	// fetches text data
	public String getTextData(String testcase, String object, String type, String value) throws IOException {
		saveScreenshot(testcase);
		try {
			temp = driver.findElement(getObject(object, type)).getText();
			return Constants.PASS;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Constants.FAIL;
		}

	}

	// compares text data with given value
	public String compareTextData(String testcase, String object, String type, String value) throws IOException {
		saveScreenshot(testcase);
		try {
			if (driver.findElement(getObject(object, type)).getText().equals(value)) {
				return Constants.PASS;
			}
			return Constants.FAIL;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Constants.FAIL;
		}

	}

	// click link
	public String clickLink(String testcase, String object, String type, String value) throws IOException {

		try {
			WebElement link = driver.findElement(getObject(object, type));
			new Actions(driver).moveToElement(link).perform();
			saveScreenshot(testcase);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			link.click();

			return Constants.PASS;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			saveScreenshot(testcase);
			return Constants.FAIL;
		}

	}

	// Checks if the page title is the desired one
	public String checkPageTitle(String testcase, String object, String type, String value) throws IOException {
		saveScreenshot(testcase);
		try {
			if (driver.getTitle().equals(value)) {
				return Constants.PASS;
			}
			return Constants.FAIL;
		} catch (Exception e) {
			return Constants.FAIL;
		}
	}

	// fetch title of each item in the list
	public String getListTitles(String testcase, String object, String type, String value) throws IOException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 500);");
		saveScreenshot(testcase);
		try {
			List<WebElement> linkElems = driver.findElements(getObject(object, type));
			for (WebElement le : linkElems) {
				System.out.println(le.getAttribute("title"));
				if (!containsKey(le.getAttribute("title"), Constants.SEARCHKEYS)) {
					return Constants.FAIL;
				}
			}
			return Constants.PASS;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Constants.FAIL;
		}

	}

	// select an item from size search result
	public String selectItemRandomly(String testcase, String object, String type, String value) throws IOException {
		try {
			List<WebElement> linkElems = driver.findElements(getObject(object, type));
			Random rand = new Random();
			int randValue = rand.nextInt(linkElems.size()) + 1;
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", linkElems.get(randValue));
			Thread.sleep(500);
			new Actions(driver).moveToElement(linkElems.get(randValue)).perform();
			saveScreenshot(testcase);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			linkElems.get(randValue).click();

			return Constants.PASS;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			saveScreenshot(testcase);
			return Constants.FAIL;
		}
	}

	// checks if a string contains a specified keyword
	public boolean containsKey(String sentence, String[] keys) {
		if (keys.length != 0) {
			for (int i = 0; i < keys.length; i++) {
				if (sentence.toLowerCase().indexOf(keys[i].toLowerCase()) == -1) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	// checks if data is present
	public boolean isEmpty(String textValue) {

		if (textValue.equals("")) {
			return true;
		}
		return false;

	}

	// checks string format
	public boolean isValidFormat(String format, String value) {

		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	// verifies product details
	public String checkProductDetails(String testcase, String object, String type, String value) throws IOException {
		try {
			boolean isItemConditionEmpty;
			boolean isValidDate = true;
			boolean isValidPrice;
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			product.setProductName(driver.findElement(By.id(OR.getProperty("productName"))).getText());

			product.setSellerName(driver.findElement(By.id(OR.getProperty("sellerName"))).getText());

			WebElement priceLabel = driver.findElement(By.id(OR.getProperty("price")));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			WebElement parentElement = (WebElement) executor.executeScript("return arguments[0].parentNode;",
					priceLabel);
			List<WebElement> childElems = parentElement.findElements(By.tagName("span"));
			for (WebElement ce : childElems) {
				if (ce.getAttribute("id").toLowerCase().contains("prc")) {
					product.setPrice(ce.getText());
				}
			}
			getTextData(testcase, "itemCondition", "id", value);
			isItemConditionEmpty = isEmpty(temp);
			temp = "";
			try {
				getTextData(testcase, "productTimeLeft", "id", value);
			} catch (NoSuchElementException e) {

			}
			if (temp != "") {
				isValidDate = isValidFormat("^[0-9]{2}(d|h|m) [0-9]{2}(h|m|s)$", temp);
			}

			String checkValue = product.getPrice().split(" ")[1];
			isValidPrice = isValidFormat("^[$][0-9],[0-9]{3}.[0-9]{2}$", checkValue);

			clickBtn(testcase, object, type, value);
			if (!isItemConditionEmpty && isValidPrice && isValidDate) {
				return Constants.PASS;
			}
			if (isItemConditionEmpty) {
				System.out.println("Item condition is empty");
			}
			if (!isValidPrice) {
				System.out.println("Price is not valid");
			}
			if (!isValidDate) {
				System.out.println("Date is not valid");
			}
			return Constants.FAIL;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Constants.FAIL;
		}
	}

	// verifies cart details
	public String checkCartDetails(String testcase, String object, String type, String value) throws IOException {
		try {
			String productName = driver.findElement(By.className(OR.getProperty("productNameLink")))
					.getAttribute("title");
			WebElement priceContainer = driver.findElement(By.className(OR.getProperty("priceContainer")));
			String productPrice = "US " + priceContainer.findElement(By.tagName("span")).getText();
			WebElement sellerContainer = driver.findElement(By.className(OR.getProperty("sellerContainer")));
			String sellerName = sellerContainer.findElement(By.tagName("a")).getText();
			clickBtn(testcase, object, type, value);
			if (productName.equals(product.getProductName()) && productPrice.equals(product.getPrice())
					&& sellerName.equals(product.getSellerName())) {
				return Constants.PASS;
			}
			return Constants.FAIL;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Constants.FAIL;
		}
	}

	public String checkCheckoutDetails(String testcase, String object, String type, String value) throws IOException {
		try {
			checkPageTitle(testcase, object, type, value);
			String productName = driver.findElement(By.className(OR.getProperty("finalProductLink"))).getText();
			String price = "US " + driver.findElement(By.className(OR.getProperty("finalProductPrice"))).getText();
			String totalPrice = "US " + driver.findElement(By.xpath(OR.getProperty("totalPrice"))).getText();

			if (productName.equals(product.getProductName()) && price.equals(product.getPrice())
					&& totalPrice.equals(product.getPrice())) {
				return Constants.PASS;
			}
			return Constants.FAIL;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return Constants.FAIL;
		}
	}
}
