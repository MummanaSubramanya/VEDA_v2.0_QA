package com.viteos.veda.transaction.redemptiontestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeRedemptionAppFunctions;

public class Redemption_VerifyViewButtons {
	static boolean bStatus;
	static String sSheetName = "RedemptionTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Redemption";
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
		Map<String, Map<String, String>> mapAllRedemptionDetails = Utilities.readMultipleTestData(Global.sRedemptionTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllRedemptionDetails.size();index++){
			Map<String, String> mapRedemptionDetails = mapAllRedemptionDetails.get("Row"+index);

			Reporting.Testcasename = mapRedemptionDetails.get("TestCaseName");
			
			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Redemption");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Redemption Trade", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Redemption Trade", "Redemption Menu selected succesfully");
			
			TradeTypeRedemptionAppFunctions.verifyViewButtons(mapRedemptionDetails);
			
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
