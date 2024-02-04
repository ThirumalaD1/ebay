package utilities;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private FileInputStream fis;
	private String filepath;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private XSSFCell cell;

	public ExcelReader(String filepath) {

		this.filepath = filepath;
		try {
			fis = new FileInputStream(filepath);
			workbook = new XSSFWorkbook(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Checks if sheet exists
	public Boolean doesSheetExist(String sheetname) {
		if (workbook.getSheetIndex(sheetname) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	// Returns total number of rows in the sheet
	public int getTotalRows(String sheetname) {
		if (doesSheetExist(sheetname)) {
			sheet = workbook.getSheet(sheetname);
			return sheet.getLastRowNum() + 1;
		}

		return 0;
	}

	// Returns the cell value
	@SuppressWarnings("deprecation")
	public String getCellValue(String sheetname, String colname, int rownum) {
		if (doesSheetExist(sheetname)) {
			int colindex = -1;
			sheet = workbook.getSheet(sheetname);

			row = sheet.getRow(0);
			for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
				if (row.getCell(cellIndex).getStringCellValue().trim().equals(colname.trim())) {
					colindex = cellIndex;
				}
			}

			// return empty string if row does not exist
			row = sheet.getRow(rownum);
			if (row == null) {
				return "";
			}

			// return empty string if the cell value is null
			cell = row.getCell(colindex);
			if (cell == null) {
				return "";
			}

			// return cell value
			if (row.getCell(colindex).getCellType() == Cell.CELL_TYPE_STRING) {
				return row.getCell(colindex).getStringCellValue();
			} else if (row.getCell(colindex).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return String.valueOf((int) row.getCell(colindex).getNumericCellValue());
			}
		}
		return "";
	}

}
