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
import com.viteos.veda.master.lib.HolderMasterAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class HolderMaster_ModifyCheckerOperations_TS4 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setup(){

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
			String sHolderFirstName = "";
			String sHolderLastName = "";
			String sHolderMiddleName = "";
			String sHolderName = "";

			Map<String, Map<String, String>> mapAllHolderDetails = Utilities.readMultipleTestData(Global.sHolderModifyTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> objHolderTabsMaps = new HashMap<String, Map<String,String>>();
			Map<String, Map<String, String>> verifyHolder = new HashMap<>();
			for(int index = 1;index <= mapAllHolderDetails.size();index++){
				Map<String, String> innerMap = new HashMap<>();
				Map<String, String> mapModifyHolderDetailsReference = mapAllHolderDetails.get("Row"+index);

				if(mapModifyHolderDetailsReference.get("Operation") == null || !mapModifyHolderDetailsReference.get("Operation").equalsIgnoreCase("Modify")){
					continue;
				}

				Map<String, String> mapHolderGeneralDetails =  ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, "GeneralDetails", mapModifyHolderDetailsReference.get("TestcaseNameRef"));
				Map<String, String> mapModifyHolderGeneralDetails =  ExcelUtils.readDataABasedOnCellName(Global.sHolderModifyTestDataFilePath, "ModifyGeneralDetails", mapModifyHolderDetailsReference.get("TestcaseNameRef"));

				if (mapHolderGeneralDetails == null || mapModifyHolderGeneralDetails == null) {
					Reporting.logResults("Fail", "[ *** TEST DATA MAY NOT BE GIVEN PROPERLY PLEASE CHECK IT for modification of :"+mapModifyHolderDetailsReference.get("TestcaseNameRef")+" ]", "Fail");
					continue;
				}

				Reporting.Testcasename = mapModifyHolderDetailsReference.get("TestcaseNameRef");

				if (mapHolderGeneralDetails.get("HolderType") != null && mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity")) {
					if (mapModifyHolderGeneralDetails != null && mapModifyHolderGeneralDetails.containsKey("RegistrationName")) {
						sHolderName = mapModifyHolderGeneralDetails.get("RegistrationName");
					}
					else if (mapHolderGeneralDetails != null && mapHolderGeneralDetails.containsKey("RegistrationName")) {
						sHolderName = mapHolderGeneralDetails.get("RegistrationName");
					}
				}
				if (mapHolderGeneralDetails.get("HolderType") != null && mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Individual")) {
					if (mapModifyHolderGeneralDetails != null && mapModifyHolderGeneralDetails.get("FirstName") != null) {
						sHolderFirstName = mapModifyHolderGeneralDetails.get("FirstName");
						sHolderName = sHolderFirstName;
					}
					else if (mapHolderGeneralDetails.get("FirstName") != null) {
						sHolderFirstName = mapHolderGeneralDetails.get("FirstName");
						sHolderName = sHolderFirstName;
					}						
					if (mapModifyHolderGeneralDetails != null && mapModifyHolderGeneralDetails.get("MiddleName") != null) {
						sHolderMiddleName = mapModifyHolderGeneralDetails.get("MiddleName");
						sHolderName = sHolderName+" "+sHolderMiddleName;
					}
					else if (mapHolderGeneralDetails.get("MiddleName") != null) {
						sHolderMiddleName = mapHolderGeneralDetails.get("MiddleName");
						sHolderName = sHolderName+" "+sHolderMiddleName;
					}
					if (mapModifyHolderGeneralDetails != null && mapModifyHolderGeneralDetails.get("LaststName") != null) {
						sHolderLastName = mapModifyHolderGeneralDetails.get("LaststName");
						sHolderName = sHolderName+" "+sHolderLastName;
					}
					else if (mapHolderGeneralDetails.get("LastName") != null) {
						sHolderLastName = mapHolderGeneralDetails.get("LastName");
						sHolderName = sHolderName+" "+sHolderLastName;
					}
					sHolderName = sHolderName.trim();
				}
				if (sHolderName == null || sHolderName.equalsIgnoreCase("")) {
					Reporting.logResults("Fail", "[ *** TEST DATA For 'User Name'  MAY NOT BE GIVEN PROPERLY PLEASE CHECK IT for modification for Test Case :"+mapModifyHolderDetailsReference.get("TestcaseNameRef")+" ]", "Fail");
					continue;
				}
				
				if (mapHolderGeneralDetails != null) {
					if (mapModifyHolderGeneralDetails != null) {
						mapHolderGeneralDetails.putAll(mapModifyHolderGeneralDetails);
					}
					objHolderTabsMaps.put("GeneralDetails", mapHolderGeneralDetails);
				}
				
				Map<String , String> mapModifyAddressofCorrespondence = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyAddressofCorrespondence", mapHolderGeneralDetails.get("TestcaseName"));
				Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sHolderTestDataFilePath, "AddressofCorrespondence", mapHolderGeneralDetails.get("TestcaseName"));									
				if (mapModifyAddressofCorrespondence != null) {
					Map<String, String> mapClubbedCorrAddrDetails = new HashMap<>();
					mapClubbedCorrAddrDetails.putAll(mapAddressofCorrespondence);
					mapClubbedCorrAddrDetails.putAll(mapModifyAddressofCorrespondence);
					if (mapModifyAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame") == null) {
						mapClubbedCorrAddrDetails.put("CorrespondenceAndRegisteredAddrSame", null);
					}
					objHolderTabsMaps.put("AddressofCorrespondence", mapClubbedCorrAddrDetails);
				}
				else if (mapModifyAddressofCorrespondence == null) {
					objHolderTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);
				}

				Map<String , String> mapModifyRegisteredAddressDetails = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyRegisteredAddressDetails", mapHolderGeneralDetails.get("TestcaseName"));
				Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "RegisteredAddressDetails", mapHolderGeneralDetails.get("TestcaseName"));
				if (mapHolderGeneralDetails.get("HolderType") != null) {
					HolderMasterAppFunctions.HolderType = mapHolderGeneralDetails.get("HolderType");
				}

				if (mapHolderGeneralDetails.get("HolderType") != null && mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity")) {
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
					objHolderTabsMaps.put("RegisteredAddressDetails", mapClubbedRegAddrDetails);
				}			

			/*	if (mapAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame") != null && mapAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")) {
						Map<String, String> mapClubbedRegAddrDetails = new HashMap<>();
						if (objHolderTabsMaps.get("AddressofCorrespondence").get("CorrespondenceAndRegisteredAddrSame") != null && objHolderTabsMaps.get("AddressofCorrespondence").get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")) {
							mapClubbedRegAddrDetails.putAll(objHolderTabsMaps.get("AddressofCorrespondence"));
							if (mapModifyRegisteredAddressDetails != null) {
								mapClubbedRegAddrDetails.putAll(mapModifyRegisteredAddressDetails);
							}
						}						
						objHolderTabsMaps.put("RegisteredAddressDetails", mapClubbedRegAddrDetails);
					}
					else if (mapAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame") == null || mapAddressofCorrespondence.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("No")) {
						Map<String, String> mapClubbedRegAddrDetails = new HashMap<>();
						if (objHolderTabsMaps.get("AddressofCorrespondence").get("CorrespondenceAndRegisteredAddrSame") == null || objHolderTabsMaps.get("AddressofCorrespondence").get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("No")) {
							mapClubbedRegAddrDetails.putAll(objHolderTabsMaps.get("AddressofCorrespondence"));
							if (mapModifyRegisteredAddressDetails != null) {
								mapClubbedRegAddrDetails.putAll(mapModifyRegisteredAddressDetails);
							}
						}		
						if (mapRegisteredAddressDetails != null) {
							mapClubbedRegAddrDetails.putAll(mapRegisteredAddressDetails);
						}
						if (mapModifyRegisteredAddressDetails != null) {
							mapClubbedRegAddrDetails.putAll(mapModifyRegisteredAddressDetails);
						}
						objHolderTabsMaps.put("RegisteredAddressDetails", mapClubbedRegAddrDetails);
					}
				}*/

			Map<String , String> mapModifyHolderDetails = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyHolderDetails", mapHolderGeneralDetails.get("TestcaseName"));
			Map<String , String> mapHolderDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "HolderDetails", mapHolderGeneralDetails.get("TestcaseName"));
			if (mapModifyHolderDetails != null) {
				mapHolderDetails.putAll(mapModifyHolderDetails);
				objHolderTabsMaps.put("HolderDetails", mapHolderDetails);
			}
			else if (mapModifyHolderDetails == null) {
				objHolderTabsMaps.put("HolderDetails", mapHolderDetails);
			}
			Map<String , String> mapModifyFatcaDetails = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyFactaDetails", mapHolderGeneralDetails.get("TestcaseName"));
			Map<String , String> mapFatcaDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "FactaDetails", mapHolderGeneralDetails.get("TestcaseName"));
			if (mapModifyFatcaDetails != null) {
				mapFatcaDetails.putAll(mapModifyFatcaDetails);
				objHolderTabsMaps.put("FatcaDetails", mapFatcaDetails);
			}
			else if (mapModifyFatcaDetails == null) {
				objHolderTabsMaps.put("FatcaDetails", mapFatcaDetails);
			}				
			Map<String , String> mapModifyKYCDetails = Utilities.readTestData(Global.sHolderModifyTestDataFilePath, "ModifyKYCDetails", mapHolderGeneralDetails.get("TestcaseName"));
			Map<String , String> mapKYCDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "KYCDetails", mapHolderGeneralDetails.get("TestcaseName"));
			if (mapModifyKYCDetails != null) {
				mapKYCDetails.putAll(mapModifyKYCDetails);
				objHolderTabsMaps.put("KYCDetails", mapKYCDetails);
			}
			else if (mapModifyKYCDetails == null) {
				objHolderTabsMaps.put("KYCDetails", mapKYCDetails);
			}

			//Navigate to Dash board.
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Dashboard", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Dashboard", "Dashboard Menu selected succesfully");

			// Perform Checker Operations on modified Investor details.
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sHolderName, mapModifyHolderGeneralDetails.get("CheckerOperations"));

			//get operation type to be done to modify
			if(mapModifyHolderDetailsReference.get("Operation").equalsIgnoreCase("Modify") && mapModifyHolderGeneralDetails.get("CheckerOperations") != null){									

				if(mapModifyHolderGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					//Navigate to Investor Setup module to verify the approved modify details of Investor.
					NewUICommonFunctions.selectMenu("Investor Setup", "Holder");
					boolean bVerificationStatus = NewUICommonFunctions.verifyMasterDetails("Investor Holder Name", sHolderName, "InvestorHolder", objHolderTabsMaps);
					if (!bVerificationStatus) {
						Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved for InvestorHolder : "+sHolderName+" Error: "+Messages.errorMsg);							
					}
				}			
				if(!bStatus && mapModifyHolderGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail", "Verify Modify Approved InvestorHolder Data.", "The modified details are NOT matching with actual. Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && mapModifyHolderGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass", "Verify Modify Approved InvestorHolder Data.", "Negative testdata - Modification of InvestorHolder Failed");						
				}

				if(bStatus && mapModifyHolderGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass", "Verify Modify Approved Investor Data.", "InvestorHolder modified with the Tesdata: "+mapModifyHolderDetailsReference);
				}

				if(bStatus && mapModifyHolderGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail", "Verify Modify Approved InvestorHolder Data.", "Modification of InvestorHolder got created with negative testdata");
					continue;
				}
				if(mapModifyHolderGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("Holder Name", sHolderName);
					verifyHolder.put(Reporting.Testcasename, innerMap);
				}
				if((mapModifyHolderGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapModifyHolderGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Return")) && bStatus){		
					innerMap.put("Status", "inactive");
					innerMap.put("Holder Name", sHolderName);
					verifyHolder.put(Reporting.Testcasename, innerMap);
				}
			}
		}
		verifyValuesinSearchPanel(verifyHolder);
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

public static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyHolder) {
	try{
		for (Entry<String, Map<String, String>> test : verifyHolder.entrySet()) {

			Reporting.Testcasename = test.getKey();

			//Navigate to Investor Master
			bStatus = NewUICommonFunctions.selectMenu("Investor Setup","Holder");				
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate To InvestorHolder Master","Cannot Navigate to InvestorHolder Master");
				continue;
			}
			Reporting.logResults("Pass","Navigate To InvestorHolder Master", "Navigated to InvestorHolder Master");

			int time =10 ;
			if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
				time = 4;
			}

			// Search the Investor in drop down
			bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Investor Holder Name", test.getValue().get("Holder Name"), test.getValue().get("Status"), "InvestorHolder", time);
			if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
				Reporting.logResults("Pass", "Verify InvestorHolder Name shouldnot be in active state",test.getValue().get("Holder Name")+" InvestorHolder Name is not active state");
				//continue;
			}
			if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
				Reporting.logResults("Fail", "Verify InvestorHolder Name shouldnot be in active state",test.getValue().get("Holder Name")+" InvestorHolder Name is in active state");
			}

			if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
				Reporting.logResults("Fail", "Verify InvestorHolder Name should be in active state",test.getValue().get("Holder Name")+" InvestorHolder Name is not in active state");
				//continue;
			}
			if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
				Reporting.logResults("Pass", "Verify InvestorHolder Name should be in active state",test.getValue().get("Holder Name")+" InvestorHolder Name is in active state");

			}
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
}
