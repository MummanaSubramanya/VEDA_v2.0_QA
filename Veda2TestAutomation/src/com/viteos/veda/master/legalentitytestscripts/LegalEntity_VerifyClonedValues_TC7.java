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
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.LegalEntityAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class LegalEntity_VerifyClonedValues_TC7 {
	static boolean bStatus;
	static String sRefrenceLESheetName = "LegalEntityDetailsTestData";

	@BeforeMethod
	public static void setup(){
		
		Reporting.Functionality ="Legal Entity Master";
		Reporting.Testcasename = "Open Application";

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
			//System.out.println(mapLegalEntityDetails.get("Entity Type").equalsIgnoreCase("Feeder") || mapLegalEntityDetails.get("Clone").equalsIgnoreCase("Yes"));
			if(mapLegalEntityDetails.get("VerifyCloneData") == null || !mapLegalEntityDetails.get("VerifyCloneData").equalsIgnoreCase("Yes"))
			{
				continue;
			}
						
			Reporting.Testcasename = mapLegalEntityDetails.get("TestcaseName");
			
			Map<String, String> mapReferringClonedLEDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, sRefrenceLESheetName, mapLegalEntityDetails.get("Legal Entity Name for Clone"));
					
			Map<String , String> mapGeneralDetails = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "GeneralDetailsTestData", mapReferringClonedLEDetails.get("TestcaseName"));
			Map<String , String> mapSubscriptionDetials = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "SubscriptionTestData", mapReferringClonedLEDetails.get("TestcaseName"));
			Map<String , String> mapOtherDetials = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "OtherDetailsTestData", mapReferringClonedLEDetails.get("TestcaseName"));
			Map<String , String> mapRedemption = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "RedemptionTestData", mapReferringClonedLEDetails.get("TestcaseName"));
			Map<String , String> mapTransfer = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "TransferTestData", mapReferringClonedLEDetails.get("TestcaseName"));
			Map<String , String> mapSwitch = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "SwitchTestData", mapReferringClonedLEDetails.get("TestcaseName"));
			Map<String , String> mapExchange = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "ExchangeTestData", mapReferringClonedLEDetails.get("TestcaseName"));
			
			mapReferringClonedLEDetails.put("Client Name", mapLegalEntityDetails.get("Client Name"));
			mapReferringClonedLEDetails.put("Fund Family Name", mapLegalEntityDetails.get("Fund Family Name"));
			mapReferringClonedLEDetails.put("Legal Entity Name", mapLegalEntityDetails.get("Legal Entity Name"));
			mapReferringClonedLEDetails.put("Legal Entity Name for Clone", mapLegalEntityDetails.get("Legal Entity Name for Clone"));
			
			Map<String, String> mapDoFillLETabDetails = new HashMap<String, String>();
			mapDoFillLETabDetails.put("Client Name", mapLegalEntityDetails.get("Client Name"));
			mapDoFillLETabDetails.put("Fund Family Name", mapLegalEntityDetails.get("Fund Family Name"));
			mapDoFillLETabDetails.put("Legal Entity Name", mapLegalEntityDetails.get("Legal Entity Name"));
			mapDoFillLETabDetails.put("Legal Entity Name for Clone", mapLegalEntityDetails.get("Legal Entity Name for Clone"));
			mapDoFillLETabDetails.put("Clone", mapLegalEntityDetails.get("Clone"));
			mapDoFillLETabDetails.put("Clone Button", mapLegalEntityDetails.get("Clone Button"));
						
			objLECreationTabsMaps.put("LegalEntityDetails", mapReferringClonedLEDetails);
			objLECreationTabsMaps.put("GeneralDetails", mapGeneralDetails);
			objLECreationTabsMaps.put("SubscriptionDetails", mapSubscriptionDetials);
			objLECreationTabsMaps.put("OtherDetails", mapOtherDetials);
			objLECreationTabsMaps.put("RedemptionDetails", mapRedemption);
			objLECreationTabsMaps.put("TransferDetails", mapTransfer);
			objLECreationTabsMaps.put("SwitchDetails", mapSwitch);
			objLECreationTabsMaps.put("ExchangeDetails", mapExchange);			
			
			bStatus=NewUICommonFunctions.selectMenu("Fund Setup", "Legal Entity");
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

			bStatus = LegalEntityAppFunctions.doFillLegalEntityDetails(mapDoFillLETabDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Filling Legal Entity Details to Verify the cloning Details", Messages.errorMsg);
				continue;
			}
			
			//Verifying All Legal Entity Cloned details populated into respective fields.
			bStatus = LegalEntityAppFunctions.verifyAllTabsInLegalEntityDetailsEditScreen(objLECreationTabsMaps);
		
			
			if (!bStatus) {
				Reporting.logResults("Fail", "Validate Clone Data", "Failed to clone the  Data ERROR: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Validate Clone Data.", "Data Cloned Succesfully");
			
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
