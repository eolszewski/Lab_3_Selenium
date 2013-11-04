import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class TestTempConversion {
	WebDriver driver;

	@Before
	public void setUp() {
		driver = new HtmlUnitDriver();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	}

	@Test
	public void testLoginCapitalizedUsername() {
		assertTrue(login("ANDY", "apple").equals(
				"Online temperature conversion calculator"));
		assertTrue(login("BOB", "bathtub").equals(
				"Online temperature conversion calculator"));
		assertTrue(login("CHARLEY", "china").equals(
				"Online temperature conversion calculator"));
	}

	@Test
	public void testLoginPasswordSpaces() {
		assertTrue(login("andy", " apple ").equals(
				"Online temperature conversion calculator"));
		assertTrue(login("bob", " bathtub ").equals(
				"Online temperature conversion calculator"));
		assertTrue(login("charley", " china ").equals(
				"Online temperature conversion calculator"));
	}

	@Test
	public void testPasswordCaseSensitivity() {
		assertTrue(login("ANDY", "aPple").equals("Bad Login"));
		assertTrue(login("BOB", "bathtUb").equals("Bad Login"));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(login("CHARLEY", "China").equals("Bad Login"));
	}

	@Test
	public void testNoTemperature() {
		login("Andy", "apple");
		String text = submitTemperature("farenheitTemperature", "");
		assertTrue(text
				.equals("Need to enter a valid temperature!Got a NumberFormatException on"));
	}

	@Test
	public void testInvalidTemperature() {
		login("Andy", "apple");
		String text = submitTemperature("farenheitTemperature", "forty");
		assertTrue(text
				.equals("Need to enter a valid temperature!Got a NumberFormatException on forty"));
	}

	@Test
	public void testInvalidTemperatureFormat() {
		login("Andy", "apple");
		String text = submitTemperature("farenheitTemperature", "9.73E2");
		assertTrue(text
				.equals("Need to enter a valid temperature!Got a NumberFormatException on 9.73E2"));
	}

	@Test
	public void testCaseInsensitiveFahrenheit() {
		login("Andy", "apple");
		String text = submitTemperature("FarenheitTemperature", "9.73E2");
		assertTrue(text
				.equals("Need to enter a valid temperature!Got a NumberFormatException on 9.73E2"));
	}

	@Test
	public void testTwoDecimalPlaces() {
		login("Andy", "apple");
		String text = submitTemperature("farenheitTemperature", "212");
		String[] parts = text.split(" ");
		assertTrue(parts[3].equals("100.00"));
	}

	@Test
	public void testOneDecimalPlace() {
		login("Andy", "apple");
		String text = submitTemperature("farenheitTemperature", "392");
		String[] parts = text.split(" ");
		assertTrue(parts[3].equals("200.0"));
	}

	public String submitTemperature(String parameter, String temp) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement element = driver.findElement(By.name(parameter));
		element.clear();
		element.sendKeys(temp);
		element.submit();
		return driver.findElement(By.tagName("h2")).getText();
	}

	public String login(String user, String pass) {

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		driver.get("http://adnan.appspot.com/testing-lab-login.html");
		WebElement element = driver.findElement(By.name("userId"));
		element.clear();
		element.sendKeys(user);
		element = driver.findElement(By.name("userPassword"));
		element.clear();
		element.sendKeys(pass);
		element.submit();

		return driver.getTitle();
	}

}
