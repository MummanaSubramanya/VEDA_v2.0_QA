package com.viteos.veda.master.jointholderscripts;

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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class JointHolder_DeactivateCheckerOperations_TC6 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";
	
	@BeforeMethod
	public static void setup(){

		Reporting.Functionality ="JointHolder Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application as Checker", "failed");
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application as Checker", "Passed");
	}

	@Test
	public static void deactivateCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllJointHolderDetails = Utilities.readMultipleTestData(Global.sJointHolderTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllJointHolderDetails.size();index++){

				Map<String, String> mapJointHolderDetails = mapAllJointHolderDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();

				if(mapJointHolderDetails.get("Operation").equalsIgnoreCase("Validate") || mapJointHolderDetails.get("Operation").equalsIgnoreCase("Modify") || mapJointHolderDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				Reporting.Testcasename = mapJointHolderDetails.get("TestcaseNameRef");

				//Navigate to dashboard 
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard for DeactivateCheckerOperations ","Cannot Navigate to DashBoard for DeactivateCheckerOperations");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard for DeactivateCheckerOperations", "Navigated to DashBoard for DeactivateCheckerOperations");

				if(mapJointHolderDetails.get("Operation").equalsIgnoreCase("DeActivate")){

					//Read the Test Data
					Map<String, String> createdJointHolderDetails = ExcelUtils.readDataABasedOnCellName(Global.sJointHolderTestDataFilePath,"JointHolderTestData", mapJointHolderDetails.get("TestcaseNameRef"));
					boolean bNameModifyStatus = false;
					Map<String, String> modifiedJointHolderDetails = ExcelUtils.readDataABasedOnCellName(Global.sJointHolderTestDataFilePath,"ModifyTestData", mapJointHolderDetails.get("TestcaseNameRef"));
					if(modifiedJointHolderDetails!=null && modifiedJointHolderDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedJointHolderDetails.containsKey("First Name")){
							bNameModifyStatus = true;
							mapJointHolderDetails.put("First Name", modifiedJointHolderDetails.get("First Name"));
						}
					}
					if(!bNameModifyStatus){
						mapJointHolderDetails.put("First Name", createdJointHolderDetails.get("First Name"));
					}

					// Perform Checker operation
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,mapJointHolderDetails.get("First Name"),mapJointHolderDetails.get("CheckerOperations"));
					if(bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation for Deactivate Joint Holder", "Successfully Performed checker operations for  Deactivate Joint Holder : "+mapJointHolderDetails.get("CheckerOperations"));
					}

					if(!bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){

						Reporting.logResults("Fail","Perform Checker Operation for  Deactivate Joint Holder", "Cannot Perform Checker Operations for  Deactivate Joint Holder : "+mapJointHolderDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(!bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation for  Deactivate Joint Holder", "Negative testcase - Cannot Perform Checker Operations for  Deactivate Joint Holder : "+mapJointHolderDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation for  Deactivate Joint Holder ", "Peformed Checker operations with negative testdata for Deactivate Joint Holder: "+mapJointHolderDetails.get("Investor Name"));
						continue;
					}	

					if(mapJointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){

						innerMap.put("Status", "inactivate");
						innerMap.put("First Name", mapJointHolderDetails.get("First Name"));
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
		NewUICommonFunctions.refreshThePage();
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error ");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}


}

