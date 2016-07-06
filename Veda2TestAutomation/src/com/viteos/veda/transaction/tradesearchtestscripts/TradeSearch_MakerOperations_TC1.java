package com.viteos.veda.transaction.tradesearchtestscripts;


import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeSearchAppFunctions;


public class TradeSearch_MakerOperations_TC1 {
	static boolean bStatus;
	static String sSheetName = "TradeSearchTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="TradeSearch";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@org.testng.annotations.Test
	public static void test(){

		Map<String, Map<String, String>> mapAllTradeSearchDetails = Utilities.readMultipleTestData(Global.sTradeSearchTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllTradeSearchDetails.size();index++){
			Map<String, String> mapTradeSearchDetails = mapAllTradeSearchDetails.get("Row"+index);

			Reporting.Testcasename = mapTradeSearchDetails.get("TestcaseName");

			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Trade Search");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Trade Search home page", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Trade Search Home Page", "Trade Search Menu selected succesfully");
			bStatus = TradeSearchAppFunctions.doTriggerIntegratedTradeSearchOperations(mapTradeSearchDetails);
			if(bStatus && mapTradeSearchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Pass", "Perform TradeSearch  Operation  :'"+mapTradeSearchDetails.get("SubOperation")+"'", "Perform  TradeSearch Operation :'"+mapTradeSearchDetails.get("SubOperation")+"' the Trades Successfully ");
				continue;
			}

			if(bStatus && mapTradeSearchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform TradeSearchOperation :'"+mapTradeSearchDetails.get("SubOperation")+"'", "Perform TradeSearch Operation :'"+mapTradeSearchDetails.get("SubOperation")+"' with Negative Test Data");
				continue;
			}

			if(!bStatus && mapTradeSearchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform TradeSearch Operation :'"+mapTradeSearchDetails.get("SubOperation")+"'", "Failed to TradeSearch Operation :'"+mapTradeSearchDetails.get("SubOperation")+"'.ERROR : "+Messages.errorMsg);
				continue;
			}

			if(!bStatus && mapTradeSearchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform TradeSearch Operation :'"+mapTradeSearchDetails.get("SubOperation")+"'", "Negative Test Data Cannot Perform TradeSearch  Operation :'"+mapTradeSearchDetails.get("SubOperation")+"'.ERROR : "+Messages.errorMsg);
				continue;
			}
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
