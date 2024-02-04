package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import operations.APIs;
import operations.EnvironmentSetup;
import utilities.Constants;
import utilities.ExcelReader;
import utilities.PathUtility;

public class ExecuteTest extends EnvironmentSetup {

	public static Properties OR;
	public static FileInputStream fis;
	public static APIs apis;
	public static Method[] method;
	public static int totalAPIs;
	public static ExcelReader excelFile;

	public static void main(String args[]) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		// setting driver
		setDriver(Constants.CHROMEBROWSER, PathUtility.URL);

		// load OR properties file
		fis = new FileInputStream("./src/test/java/objectRepository/or.properties");
		OR = new Properties();
		OR.load(fis);
		fis.close();

		// load all APIs
		apis = new APIs(DRIVER, OR);
		method = apis.getClass().getMethods();
		totalAPIs = method.length;

		// load excel sheet
		excelFile = new ExcelReader("/Users/prabhu/Downloads/Testing_Project-master/src/test/resources/excelFiles/ebayTestData.xlsx");

		// Fetch total rows in the specified sheet
		int totalRows = excelFile.getTotalRows(Constants.SHEET1);

		// check if sheet is not empty
		if (totalRows != 0) {

			// iterate through each row in the sheet
			for (int rowindex = 1; rowindex <= totalRows; rowindex++) {
				// Fetch the Test Case, API, Object, ObjectType, Value
				String currentTestCase = excelFile.getCellValue(Constants.SHEET1, Constants.TESTCASE, rowindex);
				// System.out.println(currentTestCase);
				String currentAPI = excelFile.getCellValue(Constants.SHEET1, Constants.API, rowindex);
				// System.out.println(currentAPI);
				String currentObject = excelFile.getCellValue(Constants.SHEET1, Constants.OBJECT, rowindex);
				// System.out.println(currentObject);
				String currentObjectType = excelFile.getCellValue(Constants.SHEET1, Constants.OBJECTTYPE, rowindex);
				// System.out.println(currentObjectType);
				String currentValueIndex = excelFile.getCellValue(Constants.SHEET1, Constants.VALUE, rowindex);
				String currentValue = "";
				// System.out.println(currentValue);
				if (currentValueIndex != "") {
					int index = Integer.parseInt(currentValueIndex.trim()) - 1;
					// System.out.println(index);
					currentValue = excelFile.getCellValue(Constants.SHEET2, Constants.SEARCHKEY, index);

				}

				if (currentAPI != null) {
					for (int apiIndex = 0; apiIndex < totalAPIs; apiIndex++) {
						if (method[apiIndex].getName().equals(currentAPI)) {
							if (method[apiIndex]
									.invoke(apis, currentTestCase, currentObject, currentObjectType, currentValue)
									.equals(Constants.FAIL)) {
								System.out.println("Failed");
							} else {
								System.out.println("Passed");
							}
						}
					}
				}
			}
		}

		// closing driver
		DRIVER.close();
	}
}
