package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TakeScreenshot {

	public static void captureScreenshot(WebDriver driver, String filename) {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File desiredFile = new File("./src/test/resources/screenshots/" + filename + ".jpg");
		// System.out.println(desiredFile);
		try {
			Files.deleteIfExists(desiredFile.toPath());
			// System.out.println(Files.deleteIfExists(desiredFile.toPath()));
			FileUtils.copyFile(screenshot, new File("./src/test/resources/screenshots/" + filename + ".jpg"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
