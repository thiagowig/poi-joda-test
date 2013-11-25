package br.com.zaul.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 
 * @author thiago
 *
 */
public class Main {
	
	private final int FIRST_ROW = 5;
	
	private final int CELL_DATE = 1;
	
	private final int CELL_ENTRANCE = 2;
	
	private final int CELL_LUNCH = 3;
	
	private final int CELL_BACK = 4;
	
	private final int CELL_END = 5;
	
	DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException, ParseException {
		Main main = new Main();
		main.execute(args[0], args[1]);
	}
	
	/**
	 * 
	 * @param filePath
	 * @param sheetName
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public String execute(String filePath, String sheetName) throws IOException, ParseException {
		StringBuilder result = new StringBuilder();
		Sheet sheet = this.retrieveSheet(filePath, sheetName);
		
		for (int i = FIRST_ROW; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			String day = row.getCell(CELL_DATE).toString();
			
			if (this.validateValidValue(day)) {
				result.append(this.generateResultLine(row, day));
				
			} else {
				break;
			}
		}
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param filePath
	 * @param sheetName
	 * @return
	 * @throws IOException 
	 */
	private Sheet retrieveSheet(String filePath, String sheetName) throws IOException {
		InputStream stream = new FileInputStream(new File(filePath));
		
		Workbook workbook = new XSSFWorkbook(stream);
		return workbook.getSheet(sheetName);
	}
	
	/**
	 * 
	 * @param row
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	private StringBuilder generateResultLine(Row row, String day) throws ParseException {
		StringBuilder resultLine = new StringBuilder();
		
		DateTime entrance = getDateTime(day, row.getCell(CELL_ENTRANCE).toString());
		DateTime lunch = getDateTime(day, row.getCell(CELL_LUNCH).toString());
		DateTime back = getDateTime(day, row.getCell(CELL_BACK).toString());
		DateTime end = getDateTime(day, row.getCell(CELL_END).toString());
		
		int minutes = Minutes.minutesBetween(lunch, back).getMinutes();
		if (minutes < 60) {
			back = back.plusMinutes(60 - minutes);
			end = end.plusMinutes(60 - minutes);
		}
		
		resultLine.append("\n" + day + " " + dateTimeFormatter.print(entrance));
		resultLine.append(" " + dateTimeFormatter.print(lunch));
		resultLine.append(" " + dateTimeFormatter.print(back));
		resultLine.append(" " + dateTimeFormatter.print(end));
		
		return resultLine;
	}
	
	/**
	 * 
	 * @param day
	 * @param hour
	 * @return
	 * @throws ParseException
	 */
	private DateTime getDateTime(String day, String hour) throws ParseException {
		return new DateTime(dateFormatter.parse(day + " " + hour));
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private boolean validateValidValue(String value) {
		return value != null && !value.equals("");
	}
	
}
