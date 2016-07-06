package com.viteos.veda.unusedwork.templibrary;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.master.lib.Constants;
import com.viteos.veda.master.lib.Global;

public class SampleProgram {

	public static boolean bStatus = true;

	public static void copy(String text)
	{
		Clipboard clipboard = getSystemClipboard();

		clipboard.setContents(new StringSelection(text), null);
	}

	public static void paste() throws AWTException
	{
		Robot robot = new Robot();

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
	}

	public static String get() throws HeadlessException,
	UnsupportedFlavorException, IOException
	{
		Clipboard systemClipboard = getSystemClipboard();
		Object text = systemClipboard.getData(DataFlavor.stringFlavor);

		return (String) text;
	}

	private static Clipboard getSystemClipboard()
	{
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

		return systemClipboard;
	}

	public static boolean doPickDateFromCalender(String dateToSelect, String sLocatorOfDateField){
		try {
			String[] sMonths = {"January","February","March","April","May","June","July","August","September","October","November","December"};
			
			String[] sShortMonths = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

			String sMonthIndex = Arrays.asList(dateToSelect.split("/")).get(0);

			String sOnMonth = sMonths[Integer.parseInt(sMonthIndex)-1];

			String sOnDay = Arrays.asList(dateToSelect.split("/")).get(1);
			if (sOnDay.startsWith("0")) {
				sOnDay = sOnDay.replace("0", "");
			}
			//System.out.println(sOnDay);
			String sOnYear = Arrays.asList(dateToSelect.split("/")).get(2);

			String sMiddleLinkToSwitchToYears = "//div[@class='datepicker-days' and contains(@style,'block')]//th[@class='datepicker-switch']";
			//DAte and Time to be set in textbox
			String dateTime = dateToSelect;
			WebDriver driver = Global.driver;
			//button to open calendar
			bStatus = Elements.click(Global.driver, By.xpath(sLocatorOfDateField));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Failed to click on calender text box to open calender.]\n";
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sMiddleLinkToSwitchToYears), Constants.iDropdown);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Calender wasn't opened.]\n";
				return false;
			}
			String sActiveMonthAndYear = Elements.getText(Global.driver, By.xpath(sMiddleLinkToSwitchToYears));
			if (sActiveMonthAndYear == null || sActiveMonthAndYear.isEmpty() || sActiveMonthAndYear.equalsIgnoreCase("")) {
				Messages.errorMsg = "[ ERROR : wasn't able to retrieve the opened calender currently active month and date.]\n";
				return false;
			}
			if (sActiveMonthAndYear.contains(sOnMonth) && sActiveMonthAndYear.contains(sOnYear)) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days']//tbody//tr//td[@class='day' and normalize-space()='"+sOnDay+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to select the day : '"+sOnDay+"' for the year and month : '"+sActiveMonthAndYear+"'.]\n";
					return false;
				}
				return true;
			}
			if (sActiveMonthAndYear.contains(sOnYear) && !sActiveMonthAndYear.contains(sOnMonth)) {
				int sActiveCalenderMonthIndex = Arrays.asList(sMonths).indexOf(Arrays.asList(sActiveMonthAndYear.split(" ")).get(0).trim()) + 1;
				int sRequiredCalenderMonthIndex = Integer.parseInt(sMonthIndex);
				if (sActiveCalenderMonthIndex < sRequiredCalenderMonthIndex) {
					for (int i = 0; i < sRequiredCalenderMonthIndex - sActiveCalenderMonthIndex ; i++) {
						bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days' and contains(@style,'block')]//thead//th[@class='next']"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Wasn't able to move the month to next till the given month appears.]\n";
							return false;
						}
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days']//tbody//tr//td[@class='day' and normalize-space()='"+sOnDay+"']"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to select the day : '"+sOnDay+"' for the year and month : '"+sActiveMonthAndYear+"'.]\n";
						return false;
					}
					return true;
				}

				if (sActiveCalenderMonthIndex > sRequiredCalenderMonthIndex) {
					for (int i = 0; i < sActiveCalenderMonthIndex - sRequiredCalenderMonthIndex; i++) {
						bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days' and contains(@style,'block')]//thead//th[@class='prev']"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Wasn't able to move the month to next till the given month appears.]\n";
							return false;
						}
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days']//tbody//tr//td[@class='day' and normalize-space()='"+sOnDay+"']"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to select the day : '"+sOnDay+"' for the year and month : '"+sActiveMonthAndYear+"'.]\n";
						return false;
					}
					return true;
				}
			}
			if (!sActiveMonthAndYear.contains(sOnYear)) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days' and contains(@style,'block')]//thead//th[@class='datepicker-switch']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on calender 'datepicker-days' view header to bring the months view.]\n";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-months' and contains(@style,'block')]//thead//th[@class='datepicker-switch']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on calender 'datepicker-months' view header to bring the years view.]\n";
					return false;
				}
				bStatus = doSelectGivenYearBySortingRange(sOnYear);
				if (!bStatus) {
					return false;
				}
				String sMonthToPick = Arrays.asList(sShortMonths).get(Integer.parseInt(sMonthIndex) - 1);
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-months']//tbody//tr//td//span[@class='month' and normalize-space()='"+sMonthToPick+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on month : '"+sMonthToPick+"' from calender 'datepicker-months' view.]\n";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days']//tbody//tr//td[@class='day' and normalize-space()='"+sOnDay+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on day : '"+sOnDay+"' from calender 'datepicker-days' view.]\n";
					return false;
				}
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			Messages.errorMsg = "[ ERROR : Wasn't able to select the given date : '"+dateToSelect+"' from the date picker.]\n";
			return false;
		}		
	}
	
	private static boolean doSelectGivenYearBySortingRange(String sOnYear){
		for ( ; ; ) {
			String sYearsRange = Elements.getText(Global.driver, By.xpath("//div[@class='datepicker-years' and contains(@style,'block')]//thead//th[@class='datepicker-switch']"));
			if (sYearsRange == null || sYearsRange.equalsIgnoreCase("")) {
				Messages.errorMsg = "[ ERROR : Wasn't able to retrieve the years range from the calender]\n";
				return false;
			}
			int iYearsAppearFrom = Integer.parseInt(Arrays.asList(sYearsRange.split("-")).get(0).trim());
			int iYearsAppearTo = Integer.parseInt(Arrays.asList(sYearsRange.split("-")).get(1).trim());
			if (Integer.parseInt(sOnYear) >= iYearsAppearFrom && Integer.parseInt(sOnYear) <= iYearsAppearTo) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-years' and contains(@style,'block')]//tbody//td//span[@class='year' and normalize-space()='"+sOnYear+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on the year : '"+sOnYear+"' on calender 'datepicker-years' view.]\n";
					return false;
				}
				return true;
			}
			if (iYearsAppearFrom > Integer.parseInt(sOnYear)) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-years' and contains(@style,'block')]//thead//th[@class='prev' and contains(@style,'visible')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on the left arrow to go down in year range.]\n";
					return false;
				}
			}
			if (iYearsAppearTo < Integer.parseInt(sOnYear)) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-years' and contains(@style,'block')]//thead//th[@class='next' and contains(@style,'visible')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on the right arrow to go up in year range.]\n";
					return false;
				}
			}
		}
	}
}
