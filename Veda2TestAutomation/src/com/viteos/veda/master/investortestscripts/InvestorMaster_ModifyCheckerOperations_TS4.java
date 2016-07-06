package com.viteos.veda.master.investortestscripts;

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
import com.viteos.veda.master.lib.InvestorMasterAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class InvestorMaster_ModifyCheckerOperations_TS4 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setup(){

		Reporting.Functionality ="Investor Master";
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
			String sInvestorFirstName = "";
			String sInvestorLastName = "";
			String sInvestorMiddleName = "";
			String sInvestorName = "";			
			Map<String, Map<String, String>> mapAllInvestorDetails = Utilities.readMultipleTestData(Global.sInvestorModifyTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> objInvestorTabsMaps = new HashMap<String, Map<String,String>>();
			Map<String, Map<String, String>> verifyInvestor = new HashMap<>();
			for(int index = 1;index <= mapAllInvestorDetails.size();index++){
				Map<String, String> innerMap = new HashMap<>();
				Map<String, String> mapModifyInvestorDetailsReference = mapAllInvestorDetails.get("Row"+index);

				if(mapModifyInvestorDetailsReference.get("Operation") == null || !mapModifyInvestorDetailsReference.get("Operation").equalsIgnoreCase("Modify")){
					continue;
				}
				
				Map<String, String> mapInvestorGeneralDetails =  ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, "GeneralDetails", mapModifyInvestorDetailsReference.get("TestcaseNameRef"));
				Map<String, String> mapModifyInvestorGeneralDetails =  ExcelUtils.readDataABasedOnCellName(Global.sInvestorModifyTestDataFilePath, "ModifyGeneralDetails", mapModifyInvestorDetailsReference.get("TestcaseNameRef"));
								
				if (mapInvestorGeneralDetails == null || mapModifyInvestorGeneralDetails == null) {
					Reporting.logResults("Fail", "[ *** TEST DATA MAY NOT BE GIVEN PROPERLY PLEASE CHECK IT for modification of :"+mapModifyInvestorDetailsReference.get("TestcaseNameRef")+" ]", "Fail");
					continue;
				}

				Reporting.Testcasename = mapModifyInvestorDetailsReference.get("TestcaseNameRef");

				if (mapInvestorGeneralDetails.get("InvestorType") != null && mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity")) {
					if (mapModifyInvestorGeneralDetails != null && mapModifyInvestorGeneralDetails.containsKey("RegistrationName")) {
						sInvestorName = mapModifyInvestorGeneralDetails.get("RegistrationName");
					}
					else if (mapInvestorGeneralDetails != null && mapInvestorGeneralDetails.containsKey("RegistrationName")) {
						sInvestorName = mapInvestorGeneralDetails.get("RegistrationName");
					}
				}
				if (mapInvestorGeneralDetails.get("InvestorType") != null && mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Individual")) {
					if (mapModifyInvestorGeneralDetails != null && mapModifyInvestorGeneralDetails.get("FirstName") != null) {
						sInvestorFirstName = mapModifyInvestorGeneralDetails.get("FirstName");
						sInvestorName = sInvestorFirstName;
					}
					else if (mapInvestorGeneralDetails.get("FirstName") != null) {
						sInvestorFirstName = mapInvestorGeneralDetails.get("FirstName");
						sInvestorName = sInvestorFirstName;
					}						
					if (mapModifyInvestorGeneralDetails != null && mapModifyInvestorGeneralDetails.get("MiddleName") != null) {
						sInvestorMiddleName = mapModifyInvestorGeneralDetails.get("MiddleName");
						sInvestorName = sInvestorName+" "+sInvestorMiddleName;
					}
					else if (mapInvestorGeneralDetails.get("MiddleName") != null) {
						sInvestorMiddleName = mapInvestorGeneralDetails.get("MiddleName");
						sInvestorName = sInvestorName+" "+sInvestorMiddleName;
					}
					if (mapModifyInvestorGeneralDetails != null && mapModifyInvestorGeneralDetails.get("LastName") != null) {
						sInvestorLastName = mapModifyInvestorGeneralDetails.get("LastName");
						sInvestorName = sInvestorName+" "+sInvestorLastName;
					}
					else if (mapInvestorGeneralDetails.get("LastName") != null) {
						sInvestorLastName = mapInvestorGeneralDetails.get("LastName");
						sInvestorName = sInvestorName+" "+sInvestorLastName;
					}
					sInvestorName = sInvestorName.trim();
				}
				if (sInvestorName == null || sInvestorName.equalsIgnoreCase("")) {
					Reporting.logResults("Fail", "[ *** TEST DATA For 'User Name'  MAY NOT BE GIVEN PROPERLY PLEASE CHECK IT for modification for Test Case :"+mapModifyInvestorDetailsReference.get("TestcaseNameRef")+" ]", "Fail");
					continue;
				}				
				
				if (mapInvestorGeneralDetails != null) {
					if (mapModifyInvestorGeneralDetails != null) {
						mapInvestorGeneralDetails.putAll(mapModifyInvestorGeneralDetails);
					}
					objInvestorTabsMaps.put("GeneralDetails", mapInvestorGeneralDetails);
				}
				
				Map<String , String> mapModifyAddressofCorrespondence = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyAddressofCorrespondence", mapInvestorGeneralDetails.get("TestcaseName"));
				Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sInvestorTestDataFilePath, "AddressofCorrespondence", mapInvestorGeneralDetails.get("TestcaseName"));									
				if (mapModifyAddressofCorrespondence != null) {
					Map<String, String> mapClubbedCorrAddrDetails = new HashMap<>();
					mapClubbedCorrAddrDetails.putAll(mapAddressofCorrespondence);
					mapClubbedCorrAddrDetails.putAll(mapModifyAddressofCorrespondence);
					if (mapModifyAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame") == null) {
						mapClubbedCorrAddrDetails.put("CorrespondenceAndRegisteredAddrSame", null);
					}
					objInvestorTabsMaps.put("AddressofCorrespondence", mapClubbedCorrAddrDetails);
				}
				else if (mapModifyAddressofCorrespondence == null) {
					objInvestorTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);
				}
				
				Map<String , String> mapModifyRegisteredAddressDetails = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyRegisteredAddressDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "RegisteredAddressDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				if (mapInvestorGeneralDetails.get("InvestorType") != null) {
					InvestorMasterAppFunctions.InvestorType = mapInvestorGeneralDetails.get("InvestorType");
				}
				if (mapInvestorGeneralDetails.get("InvestorType") != null && mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity")) {
					
					Map<String, String> mapClubbedRegAddrDetails = new HashMap<>();
					
					if (mapAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame") != null && mapAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")) {
						mapClubbedRegAddrDetails.putAll(mapAddressofCorrespondence);						
						if (mapRegisteredAddressDetails != null) {
							mapClubbedRegAddrDetails.putAll(mapRegisteredAddressDetails);
						}
						if (mapModifyAddressofCorrespondence != null && mapModifyAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame") != null && mapModifyAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")) {
							mapClubbedRegAddrDetails.putAll(mapModifyAddressofCorrespondence);
						}
						if (mapModifyRegisteredAddressDetails != null) {
							mapClubbedRegAddrDetails.putAll(mapModifyRegisteredAddressDetails);
						}
					}
					if (mapAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame") == null || mapAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("No")) {
						if (mapRegisteredAddressDetails != null) {
							mapClubbedRegAddrDetails.putAll(mapRegisteredAddressDetails);
						}
						if (mapModifyAddressofCorrespondence != null && mapModifyAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame") != null && mapModifyAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")) {
							mapClubbedRegAddrDetails.putAll(mapModifyAddressofCorrespondence);
						}
						if (mapModifyRegisteredAddressDetails != null) {
							mapClubbedRegAddrDetails.putAll(mapModifyRegisteredAddressDetails);
						}
					}
					objInvestorTabsMaps.put("RegisteredAddressDetails", mapClubbedRegAddrDetails);
				}	
				/*Map<String , String> mapModifyRegisteredAddressDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "ModifyRegisteredAddressDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "RegisteredAddressDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				if (mapInvestorGeneralDetails.get("InvestorType") != null) {
					InvestorMasterAppFunctions.InvestorType = mapInvestorGeneralDetails.get("InvestorType");
				}
				if (mapInvestorGeneralDetails.get("InvestorType") != null && mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity")) {					
					if (mapModifyRegisteredAddressDetails != null) {
						mapRegisteredAddressDetails.putAll(mapModifyRegisteredAddressDetails);
						objInvestorTabsMaps.put("RegisteredAddressDetails", mapRegisteredAddressDetails);
					}
					else if (mapModifyRegisteredAddressDetails == null) {
						objInvestorTabsMaps.put("RegisteredAddressDetails", mapRegisteredAddressDetails);
					}
				}
				Map<String , String> mapModifyAddressofCorrespondence = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyAddressofCorrespondence", mapInvestorGeneralDetails.get("TestcaseName"));
				Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sInvestorTestDataFilePath, "AddressofCorrespondence", mapInvestorGeneralDetails.get("TestcaseName"));									
				if (mapModifyAddressofCorrespondence != null) {
					mapAddressofCorrespondence.putAll(mapModifyAddressofCorrespondence);
					objInvestorTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);
				}
				
				else if (mapModifyAddressofCorrespondence == null) {
					objInvestorTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);
				}*/				
				
				
				Map<String , String> mapModifyInvestorDetails = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyInvestorDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				Map<String , String> mapInvestorDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "InvestorDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				if (mapModifyInvestorDetails != null) {
					mapInvestorDetails.putAll(mapModifyInvestorDetails);
					objInvestorTabsMaps.put("InvestorDetails", mapInvestorDetails);
				}
				else if (mapModifyInvestorDetails == null) {
					objInvestorTabsMaps.put("InvestorDetails", mapInvestorDetails);
				}
				Map<String , String> mapModifyFatcaDetails = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyFatcaDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				Map<String , String> mapFatcaDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "FatcaDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				if (mapModifyFatcaDetails != null) {
					mapFatcaDetails.putAll(mapModifyFatcaDetails);
					objInvestorTabsMaps.put("FatcaDetails", mapFatcaDetails);
				}
				else if (mapModifyFatcaDetails == null) {
					objInvestorTabsMaps.put("FatcaDetails", mapFatcaDetails);
				}				
				Map<String , String> mapModifyKYCDetails = Utilities.readTestData(Global.sInvestorModifyTestDataFilePath, "ModifyKYCDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				Map<String , String> mapKYCDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "KYCDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				if (mapModifyKYCDetails != null) {
					mapKYCDetails.putAll(mapModifyKYCDetails);
					objInvestorTabsMaps.put("KYCDetails", mapKYCDetails);
				}
				else if (mapModifyKYCDetails == null) {
					objInvestorTabsMaps.put("KYCDetails", mapKYCDetails);
				}

				//Navigate to Dash board.
				bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Dashboard", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Dashboard", "Dashboard Menu selected succesfully");

				// Perform Checker Operations on modified Investor details.
				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sInvestorName, mapModifyInvestorGeneralDetails.get("CheckerOperations"));

				//get operation type to be done to modify
				if(mapModifyInvestorDetailsReference.get("Operation").equalsIgnoreCase("Modify") && mapModifyInvestorGeneralDetails.get("CheckerOperations") != null){									

					if(mapModifyInvestorGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						//Navigate to Investor Setup module to verify the approved modify details of Investor.
						NewUICommonFunctions.selectMenu("Investor Setup", "Investor");
						//Setting it to true inorder to identify from which records it should get the data to verify the data of fee Distribution.
						InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag = true;
						boolean bVerificationStatus = NewUICommonFunctions.verifyMasterDetails("Investor Name", sInvestorName, "Investor", objInvestorTabsMaps);
						if (!bVerificationStatus) {
							Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved for Investor : "+sInvestorName+" Error: "+Messages.errorMsg);							
						}
					}			
					if(!bStatus && mapModifyInvestorGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
						Reporting.logResults("Fail", "Verify Modify Approved Investor Data.", "The modified details are NOT matching with actual. Error: "+Messages.errorMsg);
						continue;
					}
					if(!bStatus && mapModifyInvestorGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass", "Verify Modify Approved Investor Data.", "Negative testdata - Modification of Investor Failed");						
					}

					if(bStatus && mapModifyInvestorGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass", "Verify Modify Approved Investor Data.", "Investor modified with the Tesdata: "+mapModifyInvestorDetailsReference);
					}

					if(bStatus && mapModifyInvestorGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail", "Verify Modify Approved Investor Data.", "Modification of Investor got created with negative testdata");
						continue;
					}
					if(mapModifyInvestorGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
						innerMap.put("Status", "active");
						innerMap.put("Investor Name", sInvestorName);
						verifyInvestor.put(Reporting.Testcasename, innerMap);
					}
					if((mapModifyInvestorGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapModifyInvestorGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Return")) && bStatus){		
						innerMap.put("Status", "inactive");
						innerMap.put("Investor Name", sInvestorName);
						verifyInvestor.put(Reporting.Testcasename, innerMap);
					}
				}
			}
			verifyValuesinSearchPanel(verifyInvestor);
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

	public static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyLE) {
		try{
			for (Entry<String, Map<String, String>> test : verifyLE.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Investor Master
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup","Investor");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Investor Master","Cannot Navigate to Investor Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Investor Master", "Navigated to Investor Master");


				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Investor in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Investor Name", test.getValue().get("Investor Name"), test.getValue().get("Status"), "Investor", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Investor Name shouldnot be in active state",test.getValue().get("Investor Name")+" Investor Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Investor Name shouldnot be in active state",test.getValue().get("Investor Name")+" Investor Name is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Investor Name should be in active state",test.getValue().get("Investor Name")+" Investor Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Investor Name should be in active state",test.getValue().get("Investor Name")+" Investor Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
