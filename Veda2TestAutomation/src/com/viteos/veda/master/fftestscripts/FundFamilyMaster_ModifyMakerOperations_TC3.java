package com.viteos.veda.master.fftestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.FundFamilyAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class FundFamilyMaster_ModifyMakerOperations_TC3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";


	@BeforeMethod
	public static void setup(){


		Reporting.Functionality ="Fund Family Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}


	@Test
	public static void testSearchFunctions(){
		try{
			Map<String, Map<String, String>> mapAllFundFamilyDetails = Utilities.readMultipleTestData(Global.sFundFamilyTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllFundFamilyDetails.size();index++){

				Map<String, String> mapFundFamilySearchDetails = mapAllFundFamilyDetails.get("Row"+index);


				if(mapFundFamilySearchDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapFundFamilySearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}
				
				NewUICommonFunctions.refreshThePage();
				
				Reporting.Testcasename = mapFundFamilySearchDetails.get("TestcaseNameRef");

				Map<String, String> FundFamilyDetails = ExcelUtils.readDataABasedOnCellName(Global.sFundFamilyTestDataFilePath,"FundFamilyTestData", mapFundFamilySearchDetails.get("TestcaseNameRef"));
				
				if (!FundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && !FundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Return")) {
					Reporting.logResults("Fail", "Trying to Modify Checker Rejected FF record.", "TEST DATA CHECK : Checker Rejected items can't be edited by Maker please change the test data in modify FF sheet.");
					continue;
				}
				NewUICommonFunctions.refreshThePage();
				
				// Navigate to FundFamily
				
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Fund Family");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Fund Family Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Fund Family Mater Under Fund Setup", "Fund Family Menu selected succesfully");

				//Get operation type to be done to modify
				if(mapFundFamilySearchDetails.get("Operation").equalsIgnoreCase("Modify")){
					//Get operation type to be done to Approve
					if(FundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){						
						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", mapFundFamilySearchDetails);
						//Do modify for the approved Fund Family
						bStatus = NewUICommonFunctions.modifyMasterDetails("Fund Family Name",FundFamilyDetails.get("FundFamilyName") , "FundFamily", tempmap);
						if(!bStatus && mapFundFamilySearchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Fund Family Data", "Fund Family cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						if(!bStatus && mapFundFamilySearchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Fund Family Data", "Modification of Fund Family Failed");
							continue;
						}
						if(bStatus && mapFundFamilySearchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The Fund Family Data", "Fund Family modified with the Tesdata: "+mapFundFamilySearchDetails);
							continue;
						}
						if(bStatus && mapFundFamilySearchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The Fund Family Data", "Modification of Fund Family got created with negative testdata");
							continue;
						}
					}

					//Do modify for the Return Fund Family
					if(FundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Return")){

						//select the dashboard
						NewUICommonFunctions.selectMenu("Dashboard", "none");

						bStatus = FundFamilyAppFunctions.modifyReturnFundFamilyDetails(FundFamilyDetails.get("FundFamilyName"),mapFundFamilySearchDetails);

						if(!bStatus && mapFundFamilySearchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Fund Family Data", "Fund Family cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						if(!bStatus && mapFundFamilySearchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Fund Family Data", "Modification of Fund Family Failed");
							continue;
						}
						if(bStatus && mapFundFamilySearchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The Fund Family Data", "Fund Family modified with the Tesdata: "+mapFundFamilySearchDetails);
							continue;
						}
						if(bStatus && mapFundFamilySearchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The Fund Family Data", "Modification of Fund Family Failed.Error: "+Messages.errorMsg);
							continue;
						}
					}
				}

				//Do validate the data when clicked on modify the data
				if(mapFundFamilySearchDetails.get("Operation").equalsIgnoreCase("Validate")){

					if(FundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", FundFamilyDetails);
						
						bStatus = NewUICommonFunctions.verifyMasterDetails("Fund Family Name",FundFamilyDetails.get("FundFamilyName") , "FundFamily", tempmap);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
					}

					if(FundFamilyDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						//select the dashboard
						NewUICommonFunctions.selectMenu("Dashboard", "none");
						
						//search the fundfamily in dash board table
						bStatus  = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,FundFamilyDetails.get("FundFamilyName"),"");
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen",Messages.errorMsg);
							continue;
						}

						bStatus = FundFamilyAppFunctions.verifyFundFamilyDetailsInEditScreen(FundFamilyDetails);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
					}
				}
			}
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
}
