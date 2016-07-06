package com.viteos.veda.master.investortestscripts;

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
import com.viteos.veda.master.lib.InvestorMasterAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;

public class InvestorMaster_ModifyMakerOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setup(){

		Reporting.Functionality ="Investor Master";
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
			String sInvestorFirstName = "";
			String sInvestorLastName = "";
			String sInvestorMiddleName = "";
			String sInvestorName = "";

			Map<String, Map<String, String>> mapAllInvestorDetails = Utilities.readMultipleTestData(Global.sInvestorModifyTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> objInvestorTabsMaps = new HashMap<String, Map<String,String>>();
			for(int index = 1;index <= mapAllInvestorDetails.size();index++){
				Map<String, String> mapModifyInvestorDetailsReference = mapAllInvestorDetails.get("Row"+index);

				if(mapModifyInvestorDetailsReference.get("Operation") == null || mapModifyInvestorDetailsReference.get("Operation").equalsIgnoreCase("Activate") || mapModifyInvestorDetailsReference.get("Operation").equalsIgnoreCase("Deactivate")){
					continue;
				}

				Map<String, String> mapInvestorGeneralDetails =  ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, "GeneralDetails", mapModifyInvestorDetailsReference.get("TestcaseNameRef"));
				if(mapInvestorGeneralDetails.get("InvestorType") != null){
					InvestorMasterAppFunctions.InvestorType = mapInvestorGeneralDetails.get("InvestorType");
				}
				if (mapModifyInvestorDetailsReference.get("Operation").equalsIgnoreCase("Modify")) {
					Map<String, String> mapModifyInvestorGeneralDetails =  ExcelUtils.readDataABasedOnCellName(Global.sInvestorModifyTestDataFilePath, "ModifyGeneralDetails", mapModifyInvestorDetailsReference.get("TestcaseNameRef"));
					if (mapInvestorGeneralDetails.get("InvestorType") != null && mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity")) {
						Map<String , String> mapModifyRegisteredAddressDetails = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyRegisteredAddressDetails", mapInvestorGeneralDetails.get("TestcaseName"));
						objInvestorTabsMaps.put("RegisteredAddressDetails", mapModifyRegisteredAddressDetails);
					}
					Map<String , String> mapModifyAddressofCorrespondence = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyAddressofCorrespondence", mapInvestorGeneralDetails.get("TestcaseName"));					
					Map<String , String> mapModifyInvestorDetails = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyInvestorDetails", mapInvestorGeneralDetails.get("TestcaseName"));
					Map<String , String> mapModifyFatcaDetails = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyFatcaDetails", mapInvestorGeneralDetails.get("TestcaseName"));
					Map<String , String> mapModifyKYCDetails = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyKYCDetails", mapInvestorGeneralDetails.get("TestcaseName"));

					objInvestorTabsMaps.put("GeneralDetails", mapModifyInvestorGeneralDetails);
					objInvestorTabsMaps.put("AddressofCorrespondence", mapModifyAddressofCorrespondence);			
					objInvestorTabsMaps.put("InvestorDetails", mapModifyInvestorDetails);
					objInvestorTabsMaps.put("FatcaDetails", mapModifyFatcaDetails);
					objInvestorTabsMaps.put("KYCDetails", mapModifyKYCDetails);
				}

				if (mapModifyInvestorDetailsReference.get("Operation").equalsIgnoreCase("Validate")) {
					if (mapInvestorGeneralDetails.get("InvestorType") != null && mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity")) {
						Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "RegisteredAddressDetails", mapInvestorGeneralDetails.get("TestcaseName"));
						objInvestorTabsMaps.put("RegisteredAddressDetails", mapRegisteredAddressDetails);
					}
					Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sInvestorTestDataFilePath, "AddressofCorrespondence", mapInvestorGeneralDetails.get("TestcaseName"));					
					Map<String , String> mapInvestorDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "InvestorDetails", mapInvestorGeneralDetails.get("TestcaseName"));
					Map<String , String> mapFatcaDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "FatcaDetails", mapInvestorGeneralDetails.get("TestcaseName"));
					Map<String , String> mapKYCDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "KYCDetails", mapInvestorGeneralDetails.get("TestcaseName"));

					objInvestorTabsMaps.put("GeneralDetails", mapInvestorGeneralDetails);
					objInvestorTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);			
					objInvestorTabsMaps.put("InvestorDetails", mapInvestorDetails);
					objInvestorTabsMaps.put("FatcaDetails", mapFatcaDetails);
					objInvestorTabsMaps.put("KYCDetails", mapKYCDetails);
				}			

				Reporting.Testcasename = mapModifyInvestorDetailsReference.get("TestcaseNameRef");

				if (mapInvestorGeneralDetails.get("InvestorType") != null) {
					if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity") && mapInvestorGeneralDetails.get("RegistrationName") != null) {
						sInvestorName = mapInvestorGeneralDetails.get("RegistrationName");
					}				
					if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Individual")) {
						if (mapInvestorGeneralDetails.get("FirstName") != null) {
							sInvestorFirstName = mapInvestorGeneralDetails.get("FirstName");
							sInvestorName = sInvestorFirstName;
						}
						if (mapInvestorGeneralDetails.get("MiddleName") != null) {
							sInvestorMiddleName = mapInvestorGeneralDetails.get("MiddleName");
							sInvestorName = sInvestorName+" "+sInvestorMiddleName;
						}
						if (mapInvestorGeneralDetails.get("LastName") != null) {
							sInvestorLastName = mapInvestorGeneralDetails.get("LastName");
							sInvestorName = sInvestorName+" "+sInvestorLastName;
						}
						sInvestorName = sInvestorName.trim();
					}
				}
				if (sInvestorName == null || sInvestorName.equalsIgnoreCase("")) {
					continue;
				}
				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Investor");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Investor Under Investor Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Investor Under Investor Setup", "Investor Menu selected succesfully");

				//get operation type to be done to modify
				if(mapModifyInvestorDetailsReference.get("Operation").equalsIgnoreCase("Modify")){					

					//do modify for the approved formula and Fee
					if(mapInvestorGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Approve"))
					{							
						InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag = true;

						bStatus = NewUICommonFunctions.modifyMasterDetails("Investor Name", sInvestorName, "Investor", objInvestorTabsMaps);						

						if(!bStatus && objInvestorTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Investor Data", "Investor cannot be modified. Error: "+Messages.errorMsg +mapInvestorGeneralDetails);
							continue;
						}

						if(!bStatus && objInvestorTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Investor Data", "Negative testdata - Modification of Investor Failed");
							continue;
						}

						if(bStatus && objInvestorTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The Investor Data", "Investor modified with the Tesdata: "+mapInvestorGeneralDetails);
							continue;
						}

						if(bStatus && objInvestorTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The Investor Data", "Modification of Investor got created with negative testdata");
							continue;
						}												
					}

					//do modify for the Approved Formula and returned Incentive Fee.
					if(mapInvestorGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Return"))
					{
						//select the dash board.
						NewUICommonFunctions.refreshThePage();
						NewUICommonFunctions.selectMenu("Dashboard", "None");

						InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag = true;

						bStatus = InvestorMasterAppFunctions.modifyReturnInvestorDetails(objInvestorTabsMaps, sInvestorName);

						if(!bStatus && objInvestorTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Returned Investor should get saved.", "Returned Investor didn't get saved with test data. Error: "+Messages.errorMsg +" ] "+ mapInvestorGeneralDetails);
							continue;
						}

						if(!bStatus && objInvestorTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Returned Investor Data should not get saved", "Modification of Returned Investor did not saved with negative test data : "+mapInvestorGeneralDetails);
							continue;
						}

						if(bStatus && objInvestorTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify Returned Investor should get saved.", "Returned Investor modified with the Tesdata: "+mapInvestorGeneralDetails);
							continue;
						}

						if(bStatus && objInvestorTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify Returned Investor should not get saved with Negative Data.", "Modification of Returned Investor got saved with negative testdata"+mapInvestorGeneralDetails);
							continue;
						}			
					}
				}

				//do validate the data when clicked on modify the data
				if(mapModifyInvestorDetailsReference.get("Operation").equalsIgnoreCase("Validate")){
					//Setting the flag to false as to get the Fee Distribution data from Actual sheet istead of modify sheet.
					InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag = false;
					bStatus = NewUICommonFunctions.verifyMasterDetails("Investor Name", sInvestorName, "Investor", objInvestorTabsMaps);
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
		NewUICommonFunctions.refreshThePage();
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
