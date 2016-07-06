package com.viteos.veda.master.holdertestscripts;

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

public class HolderMaster_ActivateMakerOperations_TS7 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="InvestorHolder Setup";
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
			Map<String, Map<String, String>> mapAllHolderGeneralDetails = Utilities.readMultipleTestData(Global.sHolderModifyTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllHolderGeneralDetails.size();index++){
				Map<String, String> mapSearchHolderDetails = mapAllHolderGeneralDetails.get("Row"+index);
				String sHolderFirstName = "";
				String sHolderLastName = "";
				String sHolderMiddleName = "";
				String sHolderName = ""; 

				if (mapSearchHolderDetails.get("Operation") == null || !mapSearchHolderDetails.get("Operation").equalsIgnoreCase("Activate")) {
					continue;
				}

				Reporting.Testcasename = mapSearchHolderDetails.get("TestcaseNameRef");

				// read the modified data if changed
				Map<String, String> mapActualGeneralDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, "GeneralDetails", mapSearchHolderDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedGeneralDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sHolderModifyTestDataFilePath, "ModifyGeneralDetails", mapSearchHolderDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedMainTabData = ExcelUtils.readDataABasedOnCellName(Global.sHolderModifyTestDataFilePath, "ModifyTestData", mapSearchHolderDetails.get("TestcaseNameRef"));

				if(mapSearchHolderDetails.get("Operation").equalsIgnoreCase("Activate")){
					if (mapActualGeneralDetailsTabData.get("HolderType") != null && mapActualGeneralDetailsTabData.get("HolderType").equalsIgnoreCase("Entity")) {
						if (mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify") && mapModifiedGeneralDetailsTabData.containsKey("RegistrationName")) {
							mapSearchHolderDetails.put("RegistrationName", mapModifiedGeneralDetailsTabData.get("RegistrationName"));	
							sHolderName = mapModifiedGeneralDetailsTabData.get("RegistrationName");
						}
						else {
							sHolderName = mapActualGeneralDetailsTabData.get("RegistrationName");
						}
					}
					if (mapActualGeneralDetailsTabData.get("HolderType") != null && mapActualGeneralDetailsTabData.get("HolderType").equalsIgnoreCase("Individual")) {
						if (mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify") && mapModifiedGeneralDetailsTabData.get("FirstName") != null) {
							sHolderFirstName = mapModifiedGeneralDetailsTabData.get("FirstName");
							sHolderName = sHolderFirstName;
						}
						else if (mapActualGeneralDetailsTabData.get("FirstName") != null) {
							sHolderFirstName = mapActualGeneralDetailsTabData.get("FirstName");
							sHolderName = sHolderFirstName;
						}						
						if (mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify") && mapModifiedGeneralDetailsTabData.get("MiddleName") != null) {
							sHolderMiddleName = mapModifiedGeneralDetailsTabData.get("MiddleName");
							sHolderName = sHolderName+" "+sHolderMiddleName;
						}
						else if (mapActualGeneralDetailsTabData.get("MiddleName") != null) {
							sHolderMiddleName = mapActualGeneralDetailsTabData.get("MiddleName");
							sHolderName = sHolderName+" "+sHolderMiddleName;
						}
						if (mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify") && mapModifiedGeneralDetailsTabData.get("LastName") != null) {
							sHolderLastName = mapModifiedGeneralDetailsTabData.get("LastName");
							sHolderName = sHolderName+" "+sHolderLastName;
						}
						else if (mapActualGeneralDetailsTabData.get("LastName") != null) {
							sHolderLastName = mapActualGeneralDetailsTabData.get("LastName");
							sHolderName = sHolderName+" "+sHolderLastName;
						}
						sHolderName = sHolderName.trim();
					}
				}
				if(sHolderName.equalsIgnoreCase("") || sHolderName.equalsIgnoreCase(" ")) {
					Reporting.logResults("Fail", "[ TEST DATA ISSUE : Test Data wasn't given properly HolderName required for search and Activate.]", "[ TEST DATA ISSUE : Test Data wasn't given properly HolderName required for search and Activate.]");
					continue;
				}

				//navigate to Client Module
				bStatus=NewUICommonFunctions.selectMenu("Investor Setup", "Holder");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Holder Under Investor Setup", "Holder Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Holder Under Investor Setup", "Holder Menu selected succesfully");

				Global.driver.navigate().refresh();

				if(mapSearchHolderDetails.get("Operation").equalsIgnoreCase("Activate")){
					bStatus = NewUICommonFunctions.activateMaster("Investor Holder Name", sHolderName, "InvestorHolder");
					if(!bStatus){
						Reporting.logResults("Fail","Maker : Validate Holder Activate request has been raised.", "Maker : Holder Activate request has NOT been raised. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Maker : Validate Holder Activate request has been raised.", "Maker : Holder Activate request has been raised Successfully.");
					continue;
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
