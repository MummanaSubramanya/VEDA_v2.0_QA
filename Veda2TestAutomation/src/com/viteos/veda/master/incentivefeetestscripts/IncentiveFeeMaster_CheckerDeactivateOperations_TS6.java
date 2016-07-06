package com.viteos.veda.master.incentivefeetestscripts;

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
import com.viteos.veda.master.lib.XMLLibrary;

public class IncentiveFeeMaster_CheckerDeactivateOperations_TS6 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality = "Incentive Fee Master";
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
	public static void testSearchFunctions(){
		Map<String, Map<String, String>> mapAllIncentiveFeeDetails = Utilities.readMultipleTestData(Global.sIncentiveFeeTestDataFilePath,sSheetName,"Y");

		for(int index = 1;index <= mapAllIncentiveFeeDetails.size();index++){
			Map<String, String> mapModifyIncentiveFeeDetails = mapAllIncentiveFeeDetails.get("Row"+index);

			if(mapModifyIncentiveFeeDetails.get("Operation") == null || !mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Deactivate")){
				continue;
			}

			Reporting.Testcasename = mapModifyIncentiveFeeDetails.get("TestcaseNameRef");

			//read testdata
			Map<String, String> createdXMLIncentiveFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sIncentiveXMLFilePath, "IncentiveFee",mapModifyIncentiveFeeDetails.get("TestcaseNameRef"));
			Map<String, String> createdIncentiveFeeDetails =  ExcelUtils.readDataABasedOnCellName(Global.sIncentiveFeeTestDataFilePath, "IncentiveFeeDetails", mapModifyIncentiveFeeDetails.get("TestcaseNameRef"));
			if (createdXMLIncentiveFeeDetails == null || createdXMLIncentiveFeeDetails.get("IncentiveFeeID").equalsIgnoreCase("")) {
				continue;
			}
			mapModifyIncentiveFeeDetails.put("IncentiveFeeID", createdXMLIncentiveFeeDetails.get("IncentiveFeeID"));
			createdIncentiveFeeDetails.put("IncentiveFeeID", createdXMLIncentiveFeeDetails.get("IncentiveFeeID"));
			createdIncentiveFeeDetails.putAll(mapModifyIncentiveFeeDetails);

			//navigate to Client Module
			bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
			
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Dashboard", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Dashboard", "Navigated To Dashboard succesfully");
			
			if(mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("DeActivate")){
				if(mapModifyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
					bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, createdIncentiveFeeDetails.get("IncentiveFeeID"), createdIncentiveFeeDetails.get("CheckerOperations"));
					if(!bStatus){
						Reporting.logResults("Fail","Checker : Validate Incentive Fee Deactivate request has been approved.", " Checker : is unable to approve the Incentive Fee Deactivate request : "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Checker : Validate Incentive Fee Deactivate request has been approved.", " Checker : Incentive Fee Deactivate request has been approved Successfully with ID : "+mapModifyIncentiveFeeDetails.get("IncentiveFeeID"));
					continue;
				}
			}
		}
	}

	@AfterMethod
	public static void tearDown(){
		Reporting.Testcasename = "Close app";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
