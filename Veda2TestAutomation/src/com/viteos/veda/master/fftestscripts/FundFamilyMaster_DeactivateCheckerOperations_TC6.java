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
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class FundFamilyMaster_DeactivateCheckerOperations_TC6 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";

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

				Map<String, String> mapModifiedFundFamilyDetails = mapAllFundFamilyDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();



				if(mapModifiedFundFamilyDetails.get("Operation").equalsIgnoreCase("Validate") || mapModifiedFundFamilyDetails.get("Operation").equalsIgnoreCase("Modify") || mapModifiedFundFamilyDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				if(mapModifiedFundFamilyDetails.get("ExpectedResults")!=null && mapModifiedFundFamilyDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}

				Reporting.Testcasename = mapModifiedFundFamilyDetails.get("TestcaseNameRef");

				// Navigate to Dashboard

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				//Checker Operation
				if(mapModifiedFundFamilyDetails.get("Operation").equalsIgnoreCase("DeActivate")){

					Map<String, String> mapCreatedFundFamilyDetails = ExcelUtils.readDataABasedOnCellName(Global.sFundFamilyTestDataFilePath,"FundFamilyTestData", mapModifiedFundFamilyDetails.get("TestcaseNameRef"));
					boolean bNameModifyStatus = false;
					Map<String, String> modifiedFundFamilyDetails = ExcelUtils.readDataABasedOnCellName(Global.sFundFamilyTestDataFilePath,"ModifyTestdata", mapModifiedFundFamilyDetails.get("TestcaseNameRef"));
					if(modifiedFundFamilyDetails!=null && modifiedFundFamilyDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedFundFamilyDetails.containsKey("FundFamilyName")){
							bNameModifyStatus = true;
							mapModifiedFundFamilyDetails.put("FundFamilyName", modifiedFundFamilyDetails.get("FundFamilyName"));
						}
					}
					if(!bNameModifyStatus){
						mapModifiedFundFamilyDetails.put("FundFamilyName", mapCreatedFundFamilyDetails.get("FundFamilyName"));
					}

					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapModifiedFundFamilyDetails.get("FundFamilyName"),mapModifiedFundFamilyDetails.get("CheckerOperations"));
					if(bStatus && mapModifiedFundFamilyDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapModifiedFundFamilyDetails.get("CheckerOperations"));
					}

					if(!bStatus && mapModifiedFundFamilyDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapModifiedFundFamilyDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(!bStatus && mapModifiedFundFamilyDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+mapModifiedFundFamilyDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					}

					if(bStatus && mapModifiedFundFamilyDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Fund Family Name: "+mapModifiedFundFamilyDetails.get("FundFamilyName"));
						continue;
					}	

					if(mapModifiedFundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						
						innerMap.put("Status", "inactive");
						innerMap.put("FundFamilyName", mapModifiedFundFamilyDetails.get("FundFamilyName"));
						VerifyMap.put(Reporting.Testcasename,innerMap);
					}

				}
			}

			//verifyValuesinSearchPanel(VerifyMap);
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

				// Search the Fund Family in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Fund Family Name", test.getValue().get("FundFamilyName"), test.getValue().get("Status"),"FundFamily", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Fund Family Name shouldnot be in active state",test.getKey()+" Fund Family Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Fund Family Name shouldnot be in active state",test.getKey()+" Fund Family Name is in active state");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
