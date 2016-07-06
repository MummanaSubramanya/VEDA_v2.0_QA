package com.viteos.veda.transaction.transferscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeTransferAppFunctions;

public class Transfer_VerifyViewButtons {
	static boolean bStatus;
	static String sSheetName = "ViewButtonsTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="TransferTrade";
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

		Map<String, Map<String, String>> mapAllTransferDetails = Utilities.readMultipleTestData(Global.sTransferTestData,sSheetName,"Y");
		//Map<String, Map<String, String>> mapAllTransferDetails = Utilities.readMultipleTestData("D:\\SampleTest\\TransferTestData1.xls",sSheetName,"Y");

		for(int index = 1;index <= mapAllTransferDetails.size();index++){
			Map<String, String> mapTransferDetails = mapAllTransferDetails.get("Row"+index);

			Reporting.Testcasename = mapTransferDetails.get("TestCaseName");

			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Transfer");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Transfer Trade ", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Transfer Trade", "Transfer Menu selected succesfully");

			bStatus = TradeTypeTransferAppFunctions.verifyViewButtonsFunctionality(mapTransferDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verify View Button Details For Transfer Trade", "Details verification in View Button Failed"+Messages.errorMsg);
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
