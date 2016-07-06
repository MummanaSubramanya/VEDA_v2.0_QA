package com.viteos.veda.master.openingbalancetestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class OpeningBalanceMaster_ActivateMakerOperations_TS7 {
	static boolean bStatus;

	@BeforeMethod
	public void beforeMethod() {
		Reporting.Functionality ="Opening Balance Master Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName	, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public void VerifyOpeneingBalanceMakerOperations() {
		Map<String, Map<String, String>> mapAllOpeningBalanceDetails = Utilities.readMultipleTestData(Global.sOpeningBalanceTestDataFilePath, "ModifyTestData", "Y");
		for (int index = 1; index <= mapAllOpeningBalanceDetails.size(); index++) {
			Map<String, String> mapOpeningBalanceDetails = mapAllOpeningBalanceDetails.get("Row"+index);
			if (mapOpeningBalanceDetails.get("Operation") == null || !mapOpeningBalanceDetails.get("Operation").equalsIgnoreCase("Activate")) {
				continue;
			}
			Reporting.Testcasename = mapOpeningBalanceDetails.get("TestcaseNameRef");			
			Map<String, String> mapXMLOpeningBalanceDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "OpeningBalance", mapOpeningBalanceDetails.get("TestcaseNameRef"));
			if (mapXMLOpeningBalanceDetails ==null || mapXMLOpeningBalanceDetails.isEmpty()) {
				continue;
			}
			NewUICommonFunctions.refreshThePage();
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 15);
			bStatus = NewUICommonFunctions.selectMenu("TRADES", "Opening Balance");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigating to Opening Balance Under Trades", "Unable to Navigate to Opening Balance Under Trades");
				continue;
			}
			if (mapOpeningBalanceDetails.get("Operation") !=null && !mapOpeningBalanceDetails.isEmpty()) {
				if (mapOpeningBalanceDetails.get("Operation").trim().equalsIgnoreCase("Activate")) {					
					bStatus = NewUICommonFunctions.verifyTheRecordInSearchPanelWithStatusAndTxnIDAlongWithOperation("inactive", "Opening balance Id", mapXMLOpeningBalanceDetails.get("SearchID"), "OpeningBalance", 5, mapOpeningBalanceDetails.get("Operation"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Maker : Verify Activation request raised for Opening Balance with Txn ID : "+mapXMLOpeningBalanceDetails.get("SearchID"), "Maker : Unable to raise Activation request for the Opening Balance with Txn ID : "+mapXMLOpeningBalanceDetails.get("SearchID"));
						continue;
					}
					Reporting.logResults("Pass", "Maker : Verify Activation request raised for Opening Balance with Txn ID : "+mapXMLOpeningBalanceDetails.get("SearchID"), "Maker : Successfully raised Activation request for the Opening Balance with Txn ID : "+mapXMLOpeningBalanceDetails.get("SearchID"));
					continue;		
				}
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
