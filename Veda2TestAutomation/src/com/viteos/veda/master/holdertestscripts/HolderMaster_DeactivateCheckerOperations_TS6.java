package com.viteos.veda.master.holdertestscripts;

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

public class HolderMaster_DeactivateCheckerOperations_TS6 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="InvestorHolder Setup";
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
		try{
			Map<String, Map<String, String>> mapAllHolderGeneralDetails = Utilities.readMultipleTestData(Global.sHolderModifyTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> verificationMap = new HashMap<String, Map<String,String>>();

			for(int index = 1;index <= mapAllHolderGeneralDetails.size();index++){
				Map<String, String> mapSearchHolderDetails = mapAllHolderGeneralDetails.get("Row"+index);
				String sHolderFirstName = "";
				String sHolderLastName = "";
				String sHolderMiddleName = "";
				String sHolderName = ""; 

				if (mapSearchHolderDetails.get("Operation") == null || !mapSearchHolderDetails.get("Operation").equalsIgnoreCase("Deactivate")) {
					continue;
				}
				Map<String, String> innerMap = new HashMap<String, String>();

				Reporting.Testcasename = mapSearchHolderDetails.get("TestcaseNameRef");

				// read the modified data if changed
				Map<String, String> mapActualGeneralDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, "GeneralDetails", mapSearchHolderDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedGeneralDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sHolderModifyTestDataFilePath, "ModifyGeneralDetails", mapSearchHolderDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedMainTabData = ExcelUtils.readDataABasedOnCellName(Global.sHolderModifyTestDataFilePath, "ModifyTestData", mapSearchHolderDetails.get("TestcaseNameRef"));

				if(mapSearchHolderDetails.get("Operation").equalsIgnoreCase("Deactivate")){
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
					Reporting.logResults("Fail", "[ TEST DATA ISSUE : Test Data wasn't given properly Holder Name required for search and deactivate.]", "[ TEST DATA ISSUE : Test Data wasn't given properly Holder Name required for search and deactivate.]");
					continue;
				}

				Map<String, String> mapModifiedCurGeneralDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sHolderModifyTestDataFilePath, "ModifyGeneralDetails", mapSearchHolderDetails.get("TCID"));
				mapSearchHolderDetails.put("CheckerOperations", mapModifiedCurGeneralDetailsTabData.get("CheckerOperations"));
				//navigate to Client Module
				bStatus=NewUICommonFunctions.selectMenu("Dashboard", "None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Holder Under Investor Setup", "Holder Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Holder Under Investor Setup", "Holder Menu selected succesfully");

				Global.driver.navigate().refresh();

				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sHolderName, mapSearchHolderDetails.get("CheckerOperations"));
				if(!bStatus){
					Reporting.logResults("Fail","Checker : Validate Holder deactivate request has been : "+mapSearchHolderDetails.get("CheckerOperations"), "Checker : Holder deactivate request has not been raised. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass","Checker : Validate Holder deactivate request has been : "+mapSearchHolderDetails.get("CheckerOperations"), "Checker : Holder deactivate request has been raised successfully.");
				if(mapSearchHolderDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "inactive");
					innerMap.put("Holder Name", sHolderName);
					verificationMap.put(Reporting.Testcasename,innerMap);
				}	
			}
			verifyValuesinSearchPanel(verificationMap);
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

	public static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyLE) {
		try{

			for (Entry<String, Map<String, String>> test : verifyLE.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Investor Master
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup","Holder");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Holder Master","Cannot Navigate to Holder Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Holder Master", "Navigated to Holder Master");


				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Investor in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Investor Holder Name", test.getValue().get("Holder Name"), test.getValue().get("Status"), "InvestorHolder", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Holder Name shouldnot be in active state",test.getValue().get("Holder Name")+" Holder Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Holder Name shouldnot be in active state",test.getValue().get("Holder Name")+" Holder Name is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Holder Name should be in active state",test.getValue().get("Holder Name")+" Holder Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Holder Name should be in active state",test.getValue().get("Holder Name")+" Holder Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
