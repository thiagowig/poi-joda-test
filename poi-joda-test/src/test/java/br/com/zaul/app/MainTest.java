package br.com.zaul.app;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MainTest {
   
	private Main main;
	
	private final String EXPECTED_RESULT = "10/10/2013 09:00:00 12:00:00 13:00:00 18:00:00\n11/10/2013 09:00:00 12:00:00 13:00:00 18:00:00\n";
	
	@Before
	public void init() {
		this.main = new Main();
	}
	
	@Test
	public void testSheetParser() throws IOException, ParseException {
		String result = this.main.execute("C:\\Users\\thiago\\Desktop\\file.xlsx", "Outubro");
		Assert.assertEquals(EXPECTED_RESULT, result);
	}
}
