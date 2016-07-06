package com.viteos.veda.master.classtestscripts;

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
import com.viteos.veda.master.lib.ClassAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class ClassMaster_VerifyClonedAndLEDataForClass_TC7 {
	static boolean bStatus;
	static String sRefrenceClassSheetName = "ClassDetailsTabTestData";	

	@BeforeMethod
	public static void setup(){

		//XMLLibrary.sFundSetupXMLFilePath = "XMLMessages//FundSetup12-19-2015065451.xml";
		Reporting.Functionality ="Cloned Class and Class Master Values Verification";
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
	public static void testClonedClassAndPrepopulatedLEData()
	{
		Map<String,Map<String,String>> mapAllClassDetails = Utilities.readMultipleTestData(Global.sClassTestDataFilePath, sRefrenceClassSheetName, "Y");
		for(int index = 1;index <= mapAllClassDetails.size();index++){

			Map<String, Map<String, String>> objClassCreationTabsMaps = new HashMap<String, Map<String,String>>();

			Map<String, String> mapClassDetails = mapAllClassDetails.get("Row"+index);
			
			System.out.println(mapClassDetails);
			
			if(mapClassDetails.get("VerifyCloneAndLEData") == null || !mapClassDetails.get("VerifyCloneAndLEData").equalsIgnoreCase("Yes")){
				continue;
			}
			
			Reporting.Testcasename = mapClassDetails.get("TestcaseName");
			
			Map<String, String> mapReferringPopulatedLEDetailsTab, mapReferringPopulatedLEGeneralDetailsTab, mapReferringPopulatedLEOtherDetailsTab, mapReferringPopulatedLESubscriptionTab, mapReferringPopulatedLERedemptionTab, mapReferringPopulatedLETransferTab, mapReferringPopulatedLESwitchTab, mapReferringPopulatedLEExchangeTab; 
			Map<String, String> mapDoFillClassDetailsTab = new HashMap<String,String>();
			
			if(mapClassDetails.get("Clone") == null || mapClassDetails.get("Clone").equalsIgnoreCase("No")){
				mapReferringPopulatedLEDetailsTab = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "LegalEntityDetailsTestData", mapClassDetails.get("Legal Entity Name"));
				System.out.println(mapReferringPopulatedLEDetailsTab.get("TestCaseName"));
				//This is LE details
				mapReferringPopulatedLEGeneralDetailsTab = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "GeneralDetailsTestData", mapReferringPopulatedLEDetailsTab.get("TestcaseName"));
				mapReferringPopulatedLEOtherDetailsTab = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "OtherDetailsTestData", mapReferringPopulatedLEDetailsTab.get("TestcaseName"));
				mapReferringPopulatedLESubscriptionTab = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "SubscriptionTestData", mapReferringPopulatedLEDetailsTab.get("TestcaseName"));
				mapReferringPopulatedLERedemptionTab = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "RedemptionTestData", mapReferringPopulatedLEDetailsTab.get("TestCaseName"));
				mapReferringPopulatedLETransferTab = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "TransferTestData", mapReferringPopulatedLEDetailsTab.get("TestcaseName"));
				mapReferringPopulatedLESwitchTab = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "SwitchTestData", mapReferringPopulatedLEDetailsTab.get("TestcaseName"));
				mapReferringPopulatedLEExchangeTab = Utilities.readTestData(Global.sLegalEntityTestDataFilePath, "ExchangeTestData", mapReferringPopulatedLEDetailsTab.get("TestcaseName"));
			
				mapReferringPopulatedLEDetailsTab.put("Client Name", mapClassDetails.get("Client Name"));
				mapReferringPopulatedLEDetailsTab.put("Fund Family Name", mapClassDetails.get("Fund Family Name"));
				mapReferringPopulatedLEDetailsTab.put("Class Name", mapClassDetails.get("Class Name"));
	
				mapDoFillClassDetailsTab .put("Client Name", mapClassDetails.get("Client Name"));
				mapDoFillClassDetailsTab.put("Fund Family Name", mapClassDetails.get("Fund Family Name"));
				mapDoFillClassDetailsTab.put("Legal Entity Name", mapClassDetails.get("Legal Entity Name"));
				mapDoFillClassDetailsTab.put("Class Name", mapClassDetails.get("Class Name"));				
				mapDoFillClassDetailsTab.put("Clone Button", mapClassDetails.get("Clone Button"));
				
				objClassCreationTabsMaps.put("ClassDetails", mapReferringPopulatedLEDetailsTab);
				objClassCreationTabsMaps.put("GeneralDetails", mapReferringPopulatedLEGeneralDetailsTab);
				objClassCreationTabsMaps.put("SubscriptionDetails", mapReferringPopulatedLEOtherDetailsTab);
				objClassCreationTabsMaps.put("OtherDetails", mapReferringPopulatedLESubscriptionTab);
				objClassCreationTabsMaps.put("RedemptionDetails", mapReferringPopulatedLERedemptionTab);
				objClassCreationTabsMaps.put("TransferDetails", mapReferringPopulatedLETransferTab);
				objClassCreationTabsMaps.put("SwitchDetails", mapReferringPopulatedLESwitchTab);
				objClassCreationTabsMaps.put("ExchangeDetails", mapReferringPopulatedLEExchangeTab);
				
			}
			
			if(mapClassDetails.get("Clone") != null && mapClassDetails.get("Clone").equalsIgnoreCase("Yes")){				

				//Reporting.Testcasename = mapClassDetails.get("TestcaseName");				
				
				Map<String, String> mapReferringClonedClassDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, sRefrenceClassSheetName, mapClassDetails.get("Class Name for Clone"));
				
				//this is class details
				Map<String , String> mapGeneralDetails = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassGeneralDetailsTabTestData", mapReferringClonedClassDetails.get("TestcaseName"));
				Map<String , String> mapSubscriptionDetials = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassSubscriptionTabTestData", mapReferringClonedClassDetails.get("TestcaseName"));
				Map<String , String> mapOtherDetials = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassOtherDetailsTabTestData", mapReferringClonedClassDetails.get("TestcaseName"));
				Map<String , String> mapRedemption = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassRedemptionTabTestData", mapReferringClonedClassDetails.get("TestcaseName"));
				Map<String , String> mapTransfer = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassTransferTabTestData", mapReferringClonedClassDetails.get("TestcaseName"));
				Map<String , String> mapSwitch = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassSwitchTabTestData", mapReferringClonedClassDetails.get("TestcaseName"));
				Map<String , String> mapExchange = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassExchangeTabTestData", mapReferringClonedClassDetails.get("TestcaseName"));

				mapReferringClonedClassDetails.put("Client Name", mapClassDetails.get("Client Name"));
				mapReferringClonedClassDetails.put("Fund Family Name", mapClassDetails.get("Fund Family Name"));
				mapReferringClonedClassDetails.put("Class Name", mapClassDetails.get("Class Name"));
				if(mapClassDetails.get("Class Short Name")==null){
					mapReferringClonedClassDetails.remove("Class Short Name");
				}
				else{
					mapReferringClonedClassDetails.put("Class Short Name", mapClassDetails.get("Class Short Name"));
				}
				mapReferringClonedClassDetails.put("Class Name for Clone", mapClassDetails.get("Class Name for Clone"));

				mapDoFillClassDetailsTab.put("Client Name", mapClassDetails.get("Client Name"));
				mapDoFillClassDetailsTab.put("Fund Family Name", mapClassDetails.get("Fund Family Name"));
				mapDoFillClassDetailsTab.put("Legal Entity Name", mapClassDetails.get("Legal Entity Name"));
				mapDoFillClassDetailsTab.put("Class Short Name", mapClassDetails.get("Class Short Name"));
				mapDoFillClassDetailsTab.put("Class Name", mapClassDetails.get("Class Name"));
				mapDoFillClassDetailsTab.put("Class Name for Clone", mapClassDetails.get("Class Name for Clone"));
				mapDoFillClassDetailsTab.put("Clone", mapClassDetails.get("Clone"));
				mapDoFillClassDetailsTab.put("Clone Button", mapClassDetails.get("Clone Button"));

				objClassCreationTabsMaps.put("ClassDetails", mapReferringClonedClassDetails);
				objClassCreationTabsMaps.put("GeneralDetails", mapGeneralDetails);
				objClassCreationTabsMaps.put("SubscriptionDetails", mapSubscriptionDetials);
				objClassCreationTabsMaps.put("OtherDetails", mapOtherDetials);
				objClassCreationTabsMaps.put("RedemptionDetails", mapRedemption);
				objClassCreationTabsMaps.put("TransferDetails", mapTransfer);
				objClassCreationTabsMaps.put("SwitchDetails", mapSwitch);
				objClassCreationTabsMaps.put("ExchangeDetails", mapExchange);			
			}					
			
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Class");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Class Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Class Mater Under Fund Setup", "Class Menu selected succesfully");

			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on Add New button to add new Class", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Click on Add New button to add new Class", "Add New Button clicked succesfully");
						
			bStatus = ClassAppFunctions.doFillClassDetails(mapDoFillClassDetailsTab);
			if (!bStatus) {
				Reporting.logResults("Fail", "Filling Class Details to Verify the cloning Details","Class details cannot be filled");
				continue;
			}

			//Verifying All Class Cloned details populated into respective fields.
			bStatus = ClassAppFunctions.verifyAllTabsInClassDetailsEditScreen(objClassCreationTabsMaps);
			if(!bStatus){
				Reporting.logResults("Fail","Validate Class Details", "Validating The Details Of Actual With Expected Is FAILED And The results are NOT matching with EXPECTED. ");
				continue;
			}
			Reporting.logResults("Pass","Validate Class Details", "Validating The Details Of Actual With Expected Is SUCCESSFULL And The results matching with EXPECTED.");
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
