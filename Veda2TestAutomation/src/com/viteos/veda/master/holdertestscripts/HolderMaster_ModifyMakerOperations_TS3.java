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
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.HolderMasterAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;

public class HolderMaster_ModifyMakerOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

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
	public static void testSearchFunctions(){
		try{
			String sHolderFirstName = "";
			String sHolderLastName = "";
			String sHolderMiddleName = "";
			String sHolderName = "";

			Map<String, Map<String, String>> mapAllHolderDetails = Utilities.readMultipleTestData(Global.sHolderModifyTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> objHolderTabsMaps = new HashMap<String, Map<String,String>>();
			for(int index = 1;index <= mapAllHolderDetails.size();index++){
				Map<String, String> mapModifyHolderDetailsReference = mapAllHolderDetails.get("Row"+index);

				if(mapModifyHolderDetailsReference.get("Operation") == null || mapModifyHolderDetailsReference.get("Operation").equalsIgnoreCase("Activate") || mapModifyHolderDetailsReference.get("Operation").equalsIgnoreCase("Deactivate")){
					continue;
				}

				Map<String, String> mapHolderGeneralDetails =  ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, "GeneralDetails", mapModifyHolderDetailsReference.get("TestcaseNameRef"));
				if(mapHolderGeneralDetails.get("HolderType") != null){
					HolderMasterAppFunctions.HolderType = mapHolderGeneralDetails.get("HolderType");
				}
				if (mapModifyHolderDetailsReference.get("Operation").equalsIgnoreCase("Modify")) {
					Map<String, String> mapModifyHolderGeneralDetails =  ExcelUtils.readDataABasedOnCellName(Global.sHolderModifyTestDataFilePath, "ModifyGeneralDetails", mapModifyHolderDetailsReference.get("TestcaseNameRef"));
					if (mapHolderGeneralDetails.get("HolderType") != null && mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity")) {
						Map<String , String> mapModifyRegisteredAddressDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "ModifyRegisteredAddressDetails", mapHolderGeneralDetails.get("TestcaseName"));
						objHolderTabsMaps.put("RegisteredAddressDetails", mapModifyRegisteredAddressDetails);
					}
					Map<String , String> mapModifyAddressofCorrespondence = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyAddressofCorrespondence", mapHolderGeneralDetails.get("TestcaseName"));					
					Map<String , String> mapModifyHolderDetails = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyHolderDetails", mapHolderGeneralDetails.get("TestcaseName"));
					Map<String , String> mapModifyFatcaDetails = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyFatcaDetails", mapHolderGeneralDetails.get("TestcaseName"));
					Map<String , String> mapModifyKYCDetails = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyKYCDetails", mapHolderGeneralDetails.get("TestcaseName"));

					objHolderTabsMaps.put("GeneralDetails", mapModifyHolderGeneralDetails);
					objHolderTabsMaps.put("AddressofCorrespondence", mapModifyAddressofCorrespondence);			
					objHolderTabsMaps.put("HolderDetails", mapModifyHolderDetails);
					objHolderTabsMaps.put("FatcaDetails", mapModifyFatcaDetails);
					objHolderTabsMaps.put("KYCDetails", mapModifyKYCDetails);
				}

				if (mapModifyHolderDetailsReference.get("Operation").equalsIgnoreCase("Validate")) {
					if (mapHolderGeneralDetails.get("HolderType") != null && mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity")) {
						Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "RegisteredAddressDetails", mapHolderGeneralDetails.get("TestcaseName"));
						objHolderTabsMaps.put("RegisteredAddressDetails", mapRegisteredAddressDetails);
					}
					Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sHolderTestDataFilePath, "AddressofCorrespondence", mapHolderGeneralDetails.get("TestcaseName"));					
					Map<String , String> mapHolderDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "HolderDetails", mapHolderGeneralDetails.get("TestcaseName"));
					Map<String , String> mapFatcaDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "FactaDetails", mapHolderGeneralDetails.get("TestcaseName"));
					Map<String , String> mapKYCDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "KYCDetails", mapHolderGeneralDetails.get("TestcaseName"));

					objHolderTabsMaps.put("GeneralDetails", mapHolderGeneralDetails);
					objHolderTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);			
					objHolderTabsMaps.put("HolderDetails", mapHolderDetails);
					objHolderTabsMaps.put("FatcaDetails", mapFatcaDetails);
					objHolderTabsMaps.put("KYCDetails", mapKYCDetails);
				}			

				Reporting.Testcasename = mapModifyHolderDetailsReference.get("TestcaseNameRef");

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
				if (sHolderName == null || sHolderName.equalsIgnoreCase("")) {
					continue;
				}
				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Holder");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Holder Under Investor Setup", "Holder Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Holder Under Investor Setup", "Holder Menu selected succesfully");

				//get operation type to be done to modify
				if(mapModifyHolderDetailsReference.get("Operation").equalsIgnoreCase("Modify")){					

					//do modify for the approved formula and Fee
					if(mapHolderGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Approve"))
					{							
						HolderMasterAppFunctions.isHolderGeneralDetailsModifyFlag = true;

						bStatus = NewUICommonFunctions.modifyMasterDetails("Investor Holder Name", sHolderName, "InvestorHolder", objHolderTabsMaps);						

						if(!bStatus && objHolderTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Holder Data", "Holder cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}

						if(!bStatus && objHolderTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Holder Data", "Negative testdata - Modification of Holder Failed");
							continue;
						}

						if(bStatus && objHolderTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The Holder Data", "Holder modified with the Tesdata: "+mapHolderGeneralDetails);
							continue;
						}

						if(bStatus && objHolderTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The Holder Data", "Modification of Holder got created with negative testdata");
							continue;
						}												
					}

					//do modify for the Approved Formula and returned Incentive Fee.
					if(mapHolderGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Return"))
					{
						//select the dash board.
						NewUICommonFunctions.refreshThePage();
						NewUICommonFunctions.selectMenu("Dashboard", "None");

						HolderMasterAppFunctions.isHolderGeneralDetailsModifyFlag = true;

						bStatus = HolderMasterAppFunctions.modifyReturnHolderDetails(objHolderTabsMaps, sHolderName);

						if(!bStatus && objHolderTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Returned Holder should get saved.", "Returned Holder didn't get saved with test data. Error: "+Messages.errorMsg +" ] "+ mapHolderGeneralDetails);
							continue;
						}

						if(!bStatus && objHolderTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Returned Holder Data should not get saved", "Modification of Returned Holder did not saved with negative test data : "+mapHolderGeneralDetails);
							continue;
						}

						if(bStatus && objHolderTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify Returned Holder should get saved.", "Returned Holder modified with the Tesdata: "+mapHolderGeneralDetails);
							continue;
						}

						if(bStatus && objHolderTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify Returned Holder should not get saved with Negative Data.", "Modification of Returned Holder got saved with negative testdata"+mapHolderGeneralDetails);
							continue;
						}			
					}
				}

				//do validate the data when clicked on modify the data
				if(mapModifyHolderDetailsReference.get("Operation").equalsIgnoreCase("Validate")){
					bStatus = NewUICommonFunctions.verifyMasterDetails("Investor Holder Name", sHolderName, "InvestorHolder", objHolderTabsMaps);
					//("Legal Entity Name", mapInvestorGeneralDetails.get("Legal Entity Name"), "Incentive Fee Id", mapInvestorGeneralDetails.get("InvestorID"), "Investor", mapInvestorGeneralDetails);
					if(!bStatus){
						Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
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
