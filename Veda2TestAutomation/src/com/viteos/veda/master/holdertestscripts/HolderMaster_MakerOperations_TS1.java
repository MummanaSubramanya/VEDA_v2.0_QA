package com.viteos.veda.master.holdertestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.HolderMasterAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class HolderMaster_MakerOperations_TS1 {
	static boolean bStatus;
	static String sRefrenceSheetName = "GeneralDetails";

	@BeforeMethod
	public static void setup(){

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
	public static void addNewLegalEntity(){

		Map<String,Map<String,String>> mapAllHoldersDetails = Utilities.readMultipleTestData(Global.sHolderTestDataFilePath, sRefrenceSheetName, "Y");
		for(int index = 1;index <= mapAllHoldersDetails.size();index++){

			Map<String, Map<String, String>> objHolderCreationTabsMaps = new HashMap<String, Map<String,String>>();

			Map<String, String> mapHolderGeneralDetails = mapAllHoldersDetails.get("Row"+index);
			String sHolderFirstName = "";
			String sHolderLastName = "";
			String sHolderMiddleName = "";
			String sHolderName = "";		
			
			Reporting.Testcasename = mapHolderGeneralDetails.get("TestcaseName");
			
			HolderMasterAppFunctions.HolderType = mapHolderGeneralDetails.get("HolderType");

			if (mapHolderGeneralDetails.get("HolderType") != null && mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity")) {
				Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "RegisteredAddressDetails", mapHolderGeneralDetails.get("TestcaseName"));
				objHolderCreationTabsMaps.put("RegisteredAddressDetails", mapRegisteredAddressDetails);
			}
			Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sHolderTestDataFilePath, "AddressofCorrespondence", mapHolderGeneralDetails.get("TestcaseName"));					
			Map<String , String> mapHolderDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "HolderDetails", mapHolderGeneralDetails.get("TestcaseName"));
			Map<String , String> mapFatcaDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "FatcaDetails", mapHolderGeneralDetails.get("TestcaseName"));
			Map<String , String> mapKYCDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "KYCDetails", mapHolderGeneralDetails.get("TestcaseName"));

			objHolderCreationTabsMaps.put("GeneralDetails", mapHolderGeneralDetails);
			objHolderCreationTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);		
			objHolderCreationTabsMaps.put("HolderDetails", mapHolderDetails);
			objHolderCreationTabsMaps.put("FatcaDetails", mapFatcaDetails);
			objHolderCreationTabsMaps.put("KYCDetails", mapKYCDetails);					

			if (mapHolderGeneralDetails.get("HolderType") != null) {
				if (mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity") && mapHolderGeneralDetails.get("RegistrationName") != null) {
					sHolderName = mapHolderGeneralDetails.get("RegistrationName");
				}				
				if (mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Individual")) {
					if (mapHolderGeneralDetails.get("FirstName") != null) {
						sHolderFirstName = mapHolderGeneralDetails.get("FirstName");
						sHolderName = sHolderFirstName;
					}
					if (mapHolderGeneralDetails.get("MiddleName") != null) {
						sHolderMiddleName = mapHolderGeneralDetails.get("MiddleName");
						sHolderName = sHolderName+" "+sHolderMiddleName;
					}
					if (mapHolderGeneralDetails.get("LastName") != null) {
						sHolderLastName = mapHolderGeneralDetails.get("LastName");
						sHolderName = sHolderName+" "+sHolderLastName;
					}
					sHolderName = sHolderName.trim();
				}
			}

			NewUICommonFunctions.refreshThePage();
			bStatus=NewUICommonFunctions.selectMenu("Investor Setup", "Holder");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Holder Under Investor Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Holder Under Investor Setup", "Holder Menu selected succesfully");

			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on Add New button to add new Holder", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Click on Add New button to add new Holder", "Add New Button clicked succesfully");

			HolderMasterAppFunctions.isHolderGeneralDetailsModifyFlag = false;

			bStatus = HolderMasterAppFunctions.createNewHolder(objHolderCreationTabsMaps);

			if(mapHolderGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus)
			{				
				if(mapHolderGeneralDetails.get("OperationType").equalsIgnoreCase("Save"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapHolderGeneralDetails.get("OperationType"), "Performed maker Operation successfull Holder Created Holder Name : " + sHolderName);
				}
				if(mapHolderGeneralDetails.get("OperationType").equalsIgnoreCase("Cancel"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapHolderGeneralDetails.get("OperationType"), "Performed maker Operation : "+mapHolderGeneralDetails.get("OperationType")+" Successfully.");
				}
				if(mapHolderGeneralDetails.get("OperationType").equalsIgnoreCase("Save As Draft"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapHolderGeneralDetails.get("OperationType"), "Performed maker Operation : "+mapHolderGeneralDetails.get("OperationType")+" successfully.");
				}				
			}			
			if(mapHolderGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapHolderGeneralDetails.get("OperationType"), "Cannot perform maker Operations. Failed for the Holder : " + sHolderName+".ERROR: "+Messages.errorMsg);
				continue;
			}			
			if(mapHolderGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
				Reporting.logResults("Pass", "Perform Maker Operation: "+mapHolderGeneralDetails.get("OperationType"), "Negative Testcase - Cannot perform maker operations for Holder : " + sHolderName+".ERROR: "+Messages.errorMsg);
			}			
			if(mapHolderGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapHolderGeneralDetails.get("OperationType"), "performed maker operations for the scenario with Negative test data : " + sHolderName);
				continue;
			}
			if(mapHolderGeneralDetails.get("OperationType").equalsIgnoreCase("Save As Draft") && bStatus){

				NewUICommonFunctions.selectMenu("Dashboard","None");
				bStatus = NewUICommonFunctions.searchValueinMastersAndTransactionTableWithFilters(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.DRAFTS, sHolderName);

				if (!bStatus) {
					Reporting.logResults("Fail","Verify Holder in Saved As Draft Items", sHolderName+" Holder is not visible in Saved as Draft Items");
					continue;
				}
				Reporting.logResults("Pass","Verify Holder in Saved As Draft Items", sHolderName+" Holder is visible in Saved as Draft Items");
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
