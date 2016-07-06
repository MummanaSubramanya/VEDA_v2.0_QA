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
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class IncentiveFeeMaster_MakerDeactivateOperations_TS5 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality = "Incentive Fee Master";
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
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Incentive Fee");
			
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to IncentiveFee Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to IncentiveFee Under Fund Setup", "IncentiveFee selected succesfully");
			
			if(mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("DeActivate")){
				if(mapModifyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
					bStatus = TempFunctions.deActivateFeeTye("Legal Entity Name", createdIncentiveFeeDetails.get("Legal Entity Name"), "Incentive Fee Id", createdIncentiveFeeDetails.get("IncentiveFeeID"), "IncentiveFee");
					//bStatus = IncentiveFeeAppFunctions.deactivateIncentiveFee("Legal Entity Name", createdIncentiveFeeDetails.get("Legal Entity Name"), "active", createdIncentiveFeeDetails.get("IncentiveFeeID"));
					if(!bStatus){
						Reporting.logResults("Fail","Maker : Validate Incentive Fee has been requested to Deactivate", " Maker : is unable to request Incentive Fee to be Deactivated : "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Maker : Validate Incentive Fee has been requested to Deactivate", " Maker : hss successfully raised request to Deactivate Incentive Fee with ID : "+createdIncentiveFeeDetails.get("IncentiveFeeID"));
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
