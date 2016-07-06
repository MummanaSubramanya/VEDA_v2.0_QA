package com.viteos.veda.master.fftestscripts;

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

public class FundFamilyMaster_CheckerOperations_TC2 {

	static boolean bStatus;
	static String sSheetName = "FundFamilyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Fund Family Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void testCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllFundFamilyDetails = Utilities.readMultipleTestData(Global.sFundFamilyTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllFundFamilyDetails.size();index++){

				Map<String, String> mapFundFamilyDetails = mapAllFundFamilyDetails.get("Row"+index);
				if(!mapFundFamilyDetails.get("OperationType").equalsIgnoreCase("Save") || mapFundFamilyDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}

				Map<String, String> innerMap = new HashMap<String, String>();

				Reporting.Testcasename = mapFundFamilyDetails.get("TestcaseName");
				
				// Navigate to Dashboard
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to DashBoard", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to DashBoard", "DashBoard Menu selected succesfully");
				
				// Perform checker Operation
				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,mapFundFamilyDetails.get("FundFamilyName"),mapFundFamilyDetails.get("CheckerOperations"));
				
				if(bStatus && mapFundFamilyDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapFundFamilyDetails.get("CheckerOperations"), "Successfully Performed checker operations for FundFamily: "+mapFundFamilyDetails.get("FundFamilyName"));
				}

				if(!bStatus && mapFundFamilyDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapFundFamilyDetails.get("CheckerOperations"), "Cannot Perform Checker Operations. Error: "+Messages.errorMsg+". For FundFamily: "+mapFundFamilyDetails.get("FundFamilyName"));
					continue;
				}

				if(!bStatus && mapFundFamilyDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapFundFamilyDetails.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations for FundFamily "+mapFundFamilyDetails.get("FundFamilyName"));
				}

				if(bStatus && mapFundFamilyDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapFundFamilyDetails.get("CheckerOperations"), "Peformed Checker operations with negative testdata. for FundFamily Name: "+mapFundFamilyDetails.get("FundFamilyName"));
					continue;
				}
				
				//Approve the Fund Family
				if(mapFundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "active");
					innerMap.put("FundFamilyName",mapFundFamilyDetails.get("FundFamilyName"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
				}

				// Reject the Fund Family
				if(mapFundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){
					innerMap.put("Status", "inactive");
					innerMap.put("FundFamilyName",mapFundFamilyDetails.get("FundFamilyName"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
				}
			}

			verifyValuesinSearchPanel(VerifyMap);
		}
		catch(Exception e){
			e.printStackTrace();
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
	

	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyClient) {
		try{

			for (Entry<String, Map<String, String>> test : verifyClient.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Fund Family Master
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Fund Family");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To fund Family Master","Cannot Navigate to Fund Family Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Fund Family Master", "Navigated to Fund Family Master");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Fund Family in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Fund Family Name", test.getValue().get("FundFamilyName"), test.getValue().get("Status"),"FundFamily", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Fund Family Name shouldnot be in active state",test.getKey()+" Fund Family Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Fund Family Name shouldnot be in active state",test.getKey()+" Fund Family Name is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Fund Family Name should be in active state",test.getKey()+" Fund Family Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Fund Family Name should be in active state",test.getKey()+" Fund Family Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
