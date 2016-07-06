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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.InvestorMasterAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class InvestorMaster_MakerOperations_TS1 {
	static boolean bStatus;
	static String sRefrenceSheetName = "GeneralDetails";

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
	public static void addNewInvestorMaster(){
		
		Map<String,Map<String,String>> mapAllInvestorsDetails = Utilities.readMultipleTestData(Global.sInvestorTestDataFilePath, sRefrenceSheetName, "Y");
		for(int index = 1;index <= mapAllInvestorsDetails.size();index++){
			
			Map<String, Map<String, String>> objInvestorCreationTabsMaps = new HashMap<String, Map<String,String>>();
			
			Map<String, String> mapInvestorGeneralDetails = mapAllInvestorsDetails.get("Row"+index);
			String sInvestorFirstName = "";
			String sInvestorLastName = "";
			String sInvestorMiddleName = "";
			String sInvestorName = "";			
			
			Reporting.Testcasename = mapInvestorGeneralDetails.get("TestcaseName");
			
			if (mapInvestorGeneralDetails.get("InvestorType") != null && mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity")) {
				Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "RegisteredAddressDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				objInvestorCreationTabsMaps.put("RegisteredAddressDetails", mapRegisteredAddressDetails);
			}
			Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sInvestorTestDataFilePath, "AddressofCorrespondence", mapInvestorGeneralDetails.get("TestcaseName"));					
			Map<String , String> mapInvestorDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "InvestorDetails", mapInvestorGeneralDetails.get("TestcaseName"));
			Map<String , String> mapFatcaDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "FatcaDetails", mapInvestorGeneralDetails.get("TestcaseName"));
			Map<String , String> mapKYCDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "KYCDetails", mapInvestorGeneralDetails.get("TestcaseName"));
			
			objInvestorCreationTabsMaps.put("GeneralDetails", mapInvestorGeneralDetails);
			objInvestorCreationTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);			
			objInvestorCreationTabsMaps.put("InvestorDetails", mapInvestorDetails);
			objInvestorCreationTabsMaps.put("FatcaDetails", mapFatcaDetails);
			objInvestorCreationTabsMaps.put("KYCDetails", mapKYCDetails);					
			
			if (mapInvestorGeneralDetails.get("InvestorType") != null) {
				if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity") && mapInvestorGeneralDetails.get("RegistrationName") != null) {
					sInvestorName = mapInvestorGeneralDetails.get("RegistrationName").trim();
				}				
				if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Individual")) {
					if (mapInvestorGeneralDetails.get("FirstName") != null) {
						sInvestorFirstName = mapInvestorGeneralDetails.get("FirstName").trim();
						sInvestorName = sInvestorFirstName;
					}
					if (mapInvestorGeneralDetails.get("MiddleName") != null) {
						sInvestorMiddleName = mapInvestorGeneralDetails.get("MiddleName").trim();
						sInvestorName = sInvestorName+" "+sInvestorMiddleName;
					}
					if (mapInvestorGeneralDetails.get("LastName") != null) {
						sInvestorLastName = mapInvestorGeneralDetails.get("LastName").trim();
						sInvestorName = sInvestorName+" "+sInvestorLastName;
					}
					sInvestorName = sInvestorName.trim();
				}
			}
			
			NewUICommonFunctions.refreshThePage();
			bStatus=NewUICommonFunctions.selectMenu("Investor Setup", "Investor");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Investor Mater Under Investor Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Investor Mater Under Investor Setup", "Investor Menu selected succesfully");
			
			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on Add New button to add new Investor", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Click on Add New button to add new Investor", "Add New Button clicked succesfully");
			
			InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag = false;
			
			bStatus = InvestorMasterAppFunctions.createNewInvestor(objInvestorCreationTabsMaps);
			if(mapInvestorGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus)
			{
				
				if(mapInvestorGeneralDetails.get("OperationType").equalsIgnoreCase("Save"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapInvestorGeneralDetails.get("OperationType"), "Performed maker Operation successfull Investor Created Investor Name : " + sInvestorName);
				}
				if(mapInvestorGeneralDetails.get("OperationType").equalsIgnoreCase("Cancel"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapInvestorGeneralDetails.get("OperationType"), "Performed maker Operation : "+mapInvestorGeneralDetails.get("OperationType")+" Successfully.");
				}
				if(mapInvestorGeneralDetails.get("OperationType").equalsIgnoreCase("Save As Draft"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapInvestorGeneralDetails.get("OperationType"), "Performed maker Operation : "+mapInvestorGeneralDetails.get("OperationType")+" successfully.");
				}
				
			}
			
			if(mapInvestorGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapInvestorGeneralDetails.get("OperationType"), "Cannot perform maker Operations. Failed for the Investor : " + sInvestorName+".ERROR: "+Messages.errorMsg);
				continue;
			}
			
			if(mapInvestorGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
				Reporting.logResults("Pass", "Perform Maker Operation: "+mapInvestorGeneralDetails.get("OperationType"), "Negative Testcase - Cannot perform maker operations for Investor : " + sInvestorName+".ERROR: "+Messages.errorMsg);
				continue;
			}
			
			if(mapInvestorGeneralDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapInvestorGeneralDetails.get("OperationType"), "performed maker operations for the scenario with Negative test data : " + sInvestorName + Messages.errorMsg);
				continue;
			}

			if(mapInvestorGeneralDetails.get("OperationType").equalsIgnoreCase("Save As Draft") && bStatus){
				NewUICommonFunctions.selectMenu("Dashboard","None");
				bStatus = NewUICommonFunctions.searchValueinMastersAndTransactionTableWithFilters(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.DRAFTS, sInvestorName);
				if (!bStatus) {
					Reporting.logResults("Fail","Verify Investor in Saved As Draft Items", sInvestorName+" Investor is not visible in Saved as Draft Items" + Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass","Verify Investor in Saved As Draft Items", sInvestorName+" Investor is visible in Saved as Draft Items");
				continue;
			}
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
