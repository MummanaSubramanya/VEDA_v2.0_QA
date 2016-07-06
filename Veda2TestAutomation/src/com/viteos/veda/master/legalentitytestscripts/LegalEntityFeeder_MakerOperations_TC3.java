package com.viteos.veda.master.legalentitytestscripts;

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
import com.viteos.veda.master.lib.LegalEntityAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class LegalEntityFeeder_MakerOperations_TC3 {
	static boolean bStatus;
	static String sRefrenceLESheetName = "LegalEntityDetailsTestData";

	@BeforeMethod
	public static void setup(){
		
		//XMLLibrary.sFundSetupXMLFilePath = "XMLMessages//FundSetup12-19-2015065451.xml";
		Reporting.Functionality ="Legal Entity Master";
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
	public static void addNewLegalEntity()
	{
		Map<String,Map<String,String>> mapAllLegalEntityDetails=Utilities.readMultipleTestData(Global.sLegalEntityTestDataFilePath, sRefrenceLESheetName, "Y");
		for(int index = 1;index <= mapAllLegalEntityDetails.size();index++){
			
			Map<String, Map<String, String>> objLECreationTabsMaps = new HashMap<String, Map<String,String>>();
			
			Map<String, String> mapLegalEntityDetails = mapAllLegalEntityDetails.get("Row"+index);
			if(mapLegalEntityDetails.get("VerifyCloneData")!=null && mapLegalEntityDetails.get("VerifyCloneData").equalsIgnoreCase("Yes")){
				continue;
			}
			if(mapLegalEntityDetails.get("Entity Type")!=null && mapLegalEntityDetails.get("Clone")!=null){
				if(!(mapLegalEntityDetails.get("Entity Type").equalsIgnoreCase("Feeder") || mapLegalEntityDetails.get("Clone").equalsIgnoreCase("Yes"))){
					continue;
				}
			}
			if(mapLegalEntityDetails.get("Entity Type") == null){
				if(mapLegalEntityDetails.get("Clone")!=null && mapLegalEntityDetails.get("Clone").equalsIgnoreCase("No")){
					continue;
				}
			}
			if(mapLegalEntityDetails.get("Clone") == null){
				if(mapLegalEntityDetails.get("Entity Type") != null && !mapLegalEntityDetails.get("Entity Type").equalsIgnoreCase("Feeder")){
					continue;
				}
			}
			Reporting.Testcasename = mapLegalEntityDetails.get("TestcaseName");
		
			Map<String , String> mapGeneralDetails = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "GeneralDetailsTestData", mapLegalEntityDetails.get("TestcaseName"));
			Map<String , String> mapSubscriptionDetials = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "SubscriptionTestData", mapLegalEntityDetails.get("TestcaseName"));
			Map<String , String> mapOtherDetials = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "OtherDetailsTestData", mapLegalEntityDetails.get("TestcaseName"));
			Map<String , String> mapRedemption = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "RedemptionTestData", mapLegalEntityDetails.get("TestcaseName"));
			Map<String , String> mapTransfer = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "TransferTestData", mapLegalEntityDetails.get("TestcaseName"));
			Map<String , String> mapSwitch = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "SwitchTestData", mapLegalEntityDetails.get("TestcaseName"));
			Map<String , String> mapExchange = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "ExchangeTestData", mapLegalEntityDetails.get("TestcaseName"));
			
			objLECreationTabsMaps.put("LegalEntityDetails", mapLegalEntityDetails);
			objLECreationTabsMaps.put("GeneralDetails", mapGeneralDetails);
			objLECreationTabsMaps.put("SubscriptionDetails", mapSubscriptionDetials);
			objLECreationTabsMaps.put("OtherDetails", mapOtherDetials);
			objLECreationTabsMaps.put("RedemptionDetails", mapRedemption);
			objLECreationTabsMaps.put("TransferDetails", mapTransfer);
			objLECreationTabsMaps.put("SwitchDetails", mapSwitch);
			objLECreationTabsMaps.put("ExchangeDetails", mapExchange);			
			
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Legal Entity");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Legal Entity Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Legal Entity Mater Under Fund Setup", "Legal Entity Menu selected succesfully");
			
			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on Add New button to add new Legal Entity", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Click on Add New button to add new Legal Entity", "Add New Button clicked succesfully");

			bStatus = LegalEntityAppFunctions.AddNewLegalEntity(objLECreationTabsMaps);
			if(mapLegalEntityDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){
				
				if(mapLegalEntityDetails.get("OperationType").equalsIgnoreCase("Save"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapLegalEntityDetails.get("OperationType"), "Performed maker Operation successfull Legal Entity Created Legal Entity Name : " + mapLegalEntityDetails.get("Legal Entity Name"));
				}
				if(mapLegalEntityDetails.get("OperationType").equalsIgnoreCase("Cancel"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapLegalEntityDetails.get("OperationType"), "Performed maker Operation "+mapLegalEntityDetails.get("OperationType")+" Successfully");
				}
				if(mapLegalEntityDetails.get("OperationType").equalsIgnoreCase("Save As Draft"))
				{
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapLegalEntityDetails.get("OperationType"), "Performed maker Operation "+mapLegalEntityDetails.get("OperationType")+" successfully");
				}
				
			}
			
			if(mapLegalEntityDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapLegalEntityDetails.get("OperationType"), "Cannot perform maker Operations. Failed for the Legal Entity " + mapLegalEntityDetails.get("Legal Entity Name"));
				continue;
			}
			
			if(mapLegalEntityDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
				Reporting.logResults("Pass", "Perform Maker Operation: "+mapLegalEntityDetails.get("OperationType"), "Negative Testcase - Cannot perform maker operations for Legal Entity " + mapLegalEntityDetails.get("Legal Entity Name"));
			}
			
			if(mapLegalEntityDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapLegalEntityDetails.get("OperationType"), "performed maker operations for the scenario with Negative test data " + mapLegalEntityDetails.get("Legal Entity Name"));
				continue;
			}

			if(mapLegalEntityDetails.get("OperationType").equalsIgnoreCase("Save As Draft") && bStatus){
				NewUICommonFunctions.selectMenu("Dashboard","None");
				bStatus = NewUICommonFunctions.searchValueinMastersAndTransactionTableWithFilters(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.DRAFTS, mapLegalEntityDetails.get("Legal Entity Name"));
				if (!bStatus) {
					Reporting.logResults("Fail","Verify Legal Entity in Saved As Draft Items", mapLegalEntityDetails.get("Legal Entity Name")+" Legal Entity is not visible in Saved as Draft Items");
					continue;
				}
				Reporting.logResults("Pass","Verify Legal Entity in Saved As Draft Items", mapLegalEntityDetails.get("Legal Entity Name")+" Legal Entity is visible in Saved as Draft Items");
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
