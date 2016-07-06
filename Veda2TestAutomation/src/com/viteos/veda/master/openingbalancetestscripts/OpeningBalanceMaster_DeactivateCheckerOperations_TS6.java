package com.viteos.veda.master.openingbalancetestscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.XMLLibrary;

public class OpeningBalanceMaster_DeactivateCheckerOperations_TS6 {
	static boolean bStatus;

	@BeforeMethod
	public void beforeMethod() {
		Reporting.Functionality ="Opening Balance Master Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName	, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public void VerifyOpeneingBalanceCheckerOperations() {
		Map<String, Map<String, String>> mapAllOpeningBalanceDetails = Utilities.readMultipleTestData(Global.sOpeningBalanceTestDataFilePath, "ModifyTestData", "Y");
		Map<String, Map<String, String>> mapVerifyOpeningBal = new HashMap<String, Map<String,String>>();
		for (int index = 1; index <= mapAllOpeningBalanceDetails.size(); index++) {
			Map<String, String> mapOpeningBalanceDetails = mapAllOpeningBalanceDetails.get("Row"+index);
			Map<String, String> innerMap = new HashMap<>();
			if (mapOpeningBalanceDetails.get("Operation") == null || !mapOpeningBalanceDetails.get("Operation").equalsIgnoreCase("Deactivate")) {
				continue;
			}
			Reporting.Testcasename = mapOpeningBalanceDetails.get("TestcaseNameRef");			
			Map<String, String> mapXMLOpeningBalanceDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "OpeningBalance", mapOpeningBalanceDetails.get("TestcaseNameRef"));
			if (mapXMLOpeningBalanceDetails == null || mapXMLOpeningBalanceDetails.isEmpty()) {
				continue;
			}			
			NewUICommonFunctions.refreshThePage();
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 15);
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigating to Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}			

			if (mapOpeningBalanceDetails.get("Operation") !=null && !mapOpeningBalanceDetails.isEmpty()) {
				if (mapOpeningBalanceDetails.get("Operation").trim().equalsIgnoreCase("Deactivate")) {					
					bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionIDAndMasterType(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapXMLOpeningBalanceDetails.get("SearchID"), "Opening Balance", mapOpeningBalanceDetails.get("CheckerOperations"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Checker : perform operation : "+mapOpeningBalanceDetails.get("CheckerOperations")+" ,against Deactivation request of Opening Balance with Txn ID : "+mapXMLOpeningBalanceDetails.get("SearchID"), "Checker : Unable to perform operation : "+mapOpeningBalanceDetails.get("CheckerOperations")+" ,against Deactivation request on the Opening Balance with Txn ID : "+mapXMLOpeningBalanceDetails.get("SearchID"));
						continue;
					}
					Reporting.logResults("Pass", "Checker : perform operation : "+mapOpeningBalanceDetails.get("CheckerOperations")+" ,against Deactivation request of Opening Balance with Txn ID : "+mapXMLOpeningBalanceDetails.get("SearchID"), "Checker : Successfully performed operation : "+mapOpeningBalanceDetails.get("CheckerOperations")+" ,against Deactivation request on the Opening Balance with Txn ID : "+mapXMLOpeningBalanceDetails.get("SearchID"));
					if(mapOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						innerMap.put("Status", "inactive");
						innerMap.put("SearchID", mapXMLOpeningBalanceDetails.get("SearchID"));
						mapVerifyOpeningBal.put(Reporting.Testcasename,innerMap);
					}
					if((mapOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Return")) && bStatus){
						innerMap.put("Status", "active");
						innerMap.put("SearchID", mapXMLOpeningBalanceDetails.get("SearchID"));
						mapVerifyOpeningBal.put(Reporting.Testcasename,innerMap);
					}					
				}
			}
		}
		verifyValuesinSearchPanel(mapVerifyOpeningBal);
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
	
	public static void verifyValuesinSearchPanel(Map<String, Map<String, String>> mapVerifyOpeningBal) {
		try{

			for (Entry<String, Map<String, String>> test : mapVerifyOpeningBal.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Investor Master
				bStatus = NewUICommonFunctions.selectMenu("TRADE","Opening Balance");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Opening Balance Master","Cannot Navigate to Opening Balance Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Opening Balance Master", "Navigated to Opening Balance Master");


				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Investor in drop down
				bStatus = NewUICommonFunctions.verifyTheRecordInSearchPanelWithStatusAndTxnIDAlongWithOperation(test.getValue().get("Status"), "Opening balance Id", test.getValue().get("SearchID"), "OpeningBalance", time, "");
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Checker : Verify Opening Balance shouldnot be in active state",test.getValue().get("SearchID")+" OpeningBalance is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Checker : Verify OpeningBalance shouldnot be in active state",test.getValue().get("SearchID")+" OpeningBalance is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Checker : Verify OpeningBalance should be in active state",test.getValue().get("SearchID")+" OpeningBalance is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Checker : Verify OpeningBalance should be in active state",test.getValue().get("SearchID")+" OpeningBalance is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
