package com.viteos.veda.unusedwork.templibrary;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;

public class Tempclass {
	static boolean bStatus;
	static String sSheetName = "SubscriptionTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Subscription";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@org.testng.annotations.Test
	public static void test(){
		try {
			/*com.viteos.veda.transaction.subscriptiontestscripts.CreateSubscription_TestScript_TS1.setup();
			com.viteos.veda.transaction.subscriptiontestscripts.CreateSubscription_TestScript_TS1.test();
			com.viteos.veda.transaction.subscriptiontestscripts.CreateSubscription_TestScript_TS1.tearDown();*/
			NewUICommonFunctions.selectMenu("INVESTOR SETUP", "Investor");

			TimeUnit.SECONDS.sleep(10);
			Map<String, String> mapSample = new HashMap<>();
			mapSample.put("clipboardtext1", "02/01/2000");
			mapSample.put("clipboardtext2", "02/01/2020");
			mapSample.put("clipboardtext3", "05/30/2016");
			mapSample.put("clipboardtext4", "05/31/2016");
			//SampleProgram.copy(mapSample.get("clipboardtext1"));
			SampleProgram.doPickDateFromCalender(mapSample.get("clipboardtext1"), "//input[@id='fromDateRange0']");			
			//Elements.click(Global.driver, By.id("fromDateRange0"));
			//SampleProgram.paste();
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			SampleProgram.doPickDateFromCalender(mapSample.get("clipboardtext2"), "//input[@id='toDateRange0']");	
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			SampleProgram.doPickDateFromCalender(mapSample.get("clipboardtext3"), "//input[@id='gpList0_fromDateRange_0']");	
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			SampleProgram.doPickDateFromCalender(mapSample.get("clipboardtext4"), "//input[@id='gpList0_toDateRange_0']");
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			//String sVal = NewUICommonFunctions.jsGetElementAttribute("toDate");
			//System.out.println(sVal);
			//bStatus = doTemepFunctions();
			TimeUnit.MINUTES.sleep(5);
			//System.out.println(bStatus);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@AfterMethod
	public static void tearDown(){

		Reporting.Testcasename = "Close Application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}

}
