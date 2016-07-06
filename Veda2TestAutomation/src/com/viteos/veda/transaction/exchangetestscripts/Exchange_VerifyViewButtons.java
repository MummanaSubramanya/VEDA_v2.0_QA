package com.viteos.veda.transaction.exchangetestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeExchangeAppFunction;
import com.viteos.veda.master.lib.TradeTypeTransferAppFunctions;

public class Exchange_VerifyViewButtons {
	static boolean bStatus;
	static String sSheetName = "ViewButtonsTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Exchange";
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

		Map<String, Map<String, String>> mapAllExchangeDetails = Utilities.readMultipleTestData(Global.sExchangeTestData,sSheetName,"Y");
		//Map<String, Map<String, String>> mapAllExchangeDetails = Utilities.readMultipleTestData("D:\\SampleTest\\ExchangeTestData.xls",sSheetName,"Y");

		for(int index = 1;index <= mapAllExchangeDetails.size();index++){
			Map<String, String> mapExchangeDetails = mapAllExchangeDetails.get("Row"+index);

			Reporting.Testcasename = mapExchangeDetails.get("TestCaseName");

			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Exchange");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Exchange Master", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Exchange Master", "Exchange Menu selected succesfully");

			bStatus = TradeTypeExchangeAppFunction.verifyViewButtonsFunctionality(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verify View Buttons in Exchagne Trade", "View Buttons Verification Failed "+Messages.errorMsg);				
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
