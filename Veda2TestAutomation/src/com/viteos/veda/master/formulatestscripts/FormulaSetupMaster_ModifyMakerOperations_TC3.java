package com.viteos.veda.master.formulatestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.FormulaSetupAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TempFunctions;

public class FormulaSetupMaster_ModifyMakerOperations_TC3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Formula Setup Master";
		Reporting.Testcasename = "Open Application";


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
			Map<String, Map<String, String>> mapAllFormulaDetails = Utilities.readMultipleTestData(Global.sFormulaSetupTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllFormulaDetails.size();index++){
				Map<String, String> mapModifyFormulaDetails = mapAllFormulaDetails.get("Row"+index);
				
				if(mapModifyFormulaDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapModifyFormulaDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}
				
				NewUICommonFunctions.refreshThePage();
				Reporting.Testcasename = mapModifyFormulaDetails.get("TestcaseNameRef");
				
				//read testdata
				Map<String, String> createdFormulaDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath,"FormulaTestData", mapModifyFormulaDetails.get("TestcaseNameRef"));
				
				//navigate to Formula Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Formula");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Formula Setup Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Formula Setup Mater Under Fund Setup", "Formula Setup Menu selected succesfully");

				//get operation type to be done to modify
				if(mapModifyFormulaDetails.get("Operation").equalsIgnoreCase("Modify")){
					
					//do modify for the approved formula
					if(createdFormulaDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						bStatus = TempFunctions.modifyFeeTypeDetailsScreen("Fee Type", createdFormulaDetails.get("FeeType"), "Fee Rule Name", createdFormulaDetails.get("FeeRuleName"),"Rule", mapModifyFormulaDetails);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Formula Details", "Formula cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Formula Details", "Formula modified with the Tesdata: "+mapModifyFormulaDetails);
						continue;
					}
					
					//do modify for the Return formula
					if(createdFormulaDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						//select the dashboard
						NewUICommonFunctions.selectMenu("Dashboard", "None");
						
						bStatus = FormulaSetupAppFunctions.modifyReturnFormulaDetails(createdFormulaDetails.get("FeeRuleName"),mapModifyFormulaDetails);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Formula Details", "Formula Details cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Formula Details", "Formula Details modified with the Tesdata: "+mapModifyFormulaDetails);
						continue;
					}
				}

				//do validate the data when clicked n modify the data
				if(mapModifyFormulaDetails.get("Operation").equalsIgnoreCase("Validate")){
					
					if(createdFormulaDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						bStatus = TempFunctions.verifyFeeTypeDetailsScreen("Fee Type",createdFormulaDetails.get("FeeType"),"Fee Rule Name",createdFormulaDetails.get("FeeRuleName"), "Rule", createdFormulaDetails);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
						continue;
					}
					if(createdFormulaDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						NewUICommonFunctions.selectMenu("Dashboard", "None");
						bStatus = FormulaSetupAppFunctions.verifyReturnFormulaDetails(createdFormulaDetails.get("FeeRuleName"), createdFormulaDetails);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details of Checker Returned Formula", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details of Checker Returned Formula", "Validated the details Actual Details equal to Expected");
						continue;						
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
