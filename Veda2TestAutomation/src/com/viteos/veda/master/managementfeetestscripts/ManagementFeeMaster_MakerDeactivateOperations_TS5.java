package com.viteos.veda.master.managementfeetestscripts;

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

public class ManagementFeeMaster_MakerDeactivateOperations_TS5 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";


	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Management Fee Master";
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
		Map<String, Map<String, String>> mapAllManagementFeeDetails = Utilities.readMultipleTestData(Global.sManagementFeeTestDataFilePath,sSheetName,"Y");

		for(int index = 1;index <= mapAllManagementFeeDetails.size();index++){
			Map<String, String> mapModifyManagementFeeDetails = mapAllManagementFeeDetails.get("Row"+index);

			if(mapModifyManagementFeeDetails.get("Operation") == null || !mapModifyManagementFeeDetails.get("Operation").equalsIgnoreCase("Deactivate")){
				continue;
			}

			Reporting.Testcasename = mapModifyManagementFeeDetails.get("TestcaseNameRef");

			//read testdata
			Map<String, String> createdXMLManagementFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sManagementXMLFilePath, "ManagementFee",mapModifyManagementFeeDetails.get("TestcaseNameRef"));
			Map<String, String> createdManagementFeeDetails =  ExcelUtils.readDataABasedOnCellName(Global.sManagementFeeTestDataFilePath, "ManagementFeeDetails", mapModifyManagementFeeDetails.get("TestcaseNameRef"));
			if (createdXMLManagementFeeDetails == null || createdXMLManagementFeeDetails.get("ManagementFeeID").equalsIgnoreCase("")) {
				continue;
			}
			mapModifyManagementFeeDetails.put("ManagementFeeID", createdXMLManagementFeeDetails.get("ManagementFeeID"));
			createdManagementFeeDetails.put("ManagementFeeID", createdXMLManagementFeeDetails.get("ManagementFeeID"));

			//navigate to Client Module
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Management Fee");

			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to ManagementFee Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to ManagementFee Under Fund Setup", "ManagementFee selected succesfully");

			if(mapModifyManagementFeeDetails.get("Operation").equalsIgnoreCase("DeActivate")){
				bStatus = TempFunctions.deActivateFeeTye("Legal Entity Name", createdManagementFeeDetails.get("Legal Entity Name"),"Management Fee Id", createdManagementFeeDetails.get("ManagementFeeID"), "ManagementFee");
				
				
				//.deactivateManagementFee(createdManagementFeeDetails);
				if(!bStatus){
					Reporting.logResults("Fail","Maker : Validate Management Fee has been DeActivate", " Maker : Management Fee has not been DeActivate : "+createdManagementFeeDetails.get("ManagementFeeID")+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass","Maker : Validate Management Fee has been DeActivate", " Maker : Management Fee Has been DeActivate : "+createdManagementFeeDetails.get("ManagementFeeID"));
				continue;

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