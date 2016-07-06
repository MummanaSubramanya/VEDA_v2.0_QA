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
import com.viteos.veda.master.lib.ClassAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class ClassClone_MakerOperations_TC3 {
	static boolean bStatus;
	static String sRefrenceClassSheetName = "ClassDetailsTabTestData";

	@BeforeMethod
	public static void setup(){
		
		Reporting.Functionality ="Class Master";
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
	public static void addNewClassWithClone()
	{
		Map<String,Map<String,String>> mapAllClassDetails=Utilities.readMultipleTestData(Global.sClassTestDataFilePath, sRefrenceClassSheetName, "Y");
		for(int index = 1;index <= mapAllClassDetails.size();index++){
			
			Map<String, Map<String, String>> objMapAllTabsClassCreationDetails = new HashMap<String, Map<String,String>>();
			
			Map<String, String> mapClassDetails = mapAllClassDetails.get("Row"+index);
			
			if(mapClassDetails.get("VerifyCloneAndLEData")!=null && mapClassDetails.get("VerifyCloneAndLEData").equalsIgnoreCase("Yes")){
				continue;
			}
			//System.out.println(mapClassDetails.get("Entity Type").equalsIgnoreCase("Feeder") || mapClassDetails.get("Clone").equalsIgnoreCase("Yes"));
			if(mapClassDetails.get("Clone") == null || !mapClassDetails.get("Clone").equalsIgnoreCase("Yes")){
				continue;
			}
			
			Reporting.Testcasename = mapClassDetails.get("TestcaseName");
		
			Map<String , String> mapGeneralDetails = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassGeneralDetailsTabTestData", mapClassDetails.get("TestcaseName"));
			Map<String , String> mapSubscriptionDetials = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassSubscriptionTabTestData", mapClassDetails.get("TestcaseName"));
			Map<String , String> mapOtherDetials = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassOtherDetailsTabTestData", mapClassDetails.get("TestcaseName"));
			Map<String , String> mapRedemption = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassRedemptionTabTestData", mapClassDetails.get("TestcaseName"));
			Map<String , String> mapTransfer = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassTransferTabTestData", mapClassDetails.get("TestcaseName"));
			Map<String , String> mapSwitch = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassSwitchTabTestData", mapClassDetails.get("TestcaseName"));
			Map<String , String> mapExchange = Utilities.readTestData(Global.sClassTestDataFilePath, "ClassExchangeTabTestData", mapClassDetails.get("TestcaseName"));
			
			objMapAllTabsClassCreationDetails.put("ClassDetails", mapClassDetails);
			objMapAllTabsClassCreationDetails.put("GeneralDetails", mapGeneralDetails);
			objMapAllTabsClassCreationDetails.put("SubscriptionDetails", mapSubscriptionDetials);
			objMapAllTabsClassCreationDetails.put("OtherDetails", mapOtherDetials);
			objMapAllTabsClassCreationDetails.put("RedemptionDetails", mapRedemption);
			objMapAllTabsClassCreationDetails.put("TransferDetails", mapTransfer);
			objMapAllTabsClassCreationDetails.put("SwitchDetails", mapSwitch);
			objMapAllTabsClassCreationDetails.put("ExchangeDetails", mapExchange);			
			
		
			
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Class");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Class Master Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Class Master Under Fund Setup", "Class Menu selected succesfully");
			
			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on Add New button to add new Class", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Click on Add New button to add new Class", "Add New Button clicked succesfully");

			bStatus = ClassAppFunctions.addNewClass(objMapAllTabsClassCreationDetails);
			if(mapClassDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){
				
				if(mapClassDetails.get("OperationType").equalsIgnoreCase("Save")){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapClassDetails.get("OperationType"), "Performed maker Operation successfully Class Created Class Name : " + mapClassDetails.get("Class Name"));
				}
				if(mapClassDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapClassDetails.get("OperationType"), "Performed maker Operation "+mapClassDetails.get("OperationType")+" Successfully");
				}
				if(mapClassDetails.get("OperationType").equalsIgnoreCase("Save As Draft")){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapClassDetails.get("OperationType"), "Performed maker Operation "+mapClassDetails.get("OperationType")+" successfully");
				}
				
			}
			
			if(mapClassDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapClassDetails.get("OperationType"), "Cannot perform maker Operations. Failed for the Class " + mapClassDetails.get("Class Name"));
				continue;
			}
			
			if(mapClassDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
				Reporting.logResults("Pass", "Perform Maker Operation: "+mapClassDetails.get("OperationType"), "Negative Testcase - Cannot perform maker operations for Class " + mapClassDetails.get("Class Name"));
			}
			
			if(mapClassDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapClassDetails.get("OperationType"), "performed maker operations for the scenario with Negative test data " + mapClassDetails.get("Class Name"));
				continue;
			}

			if(mapClassDetails.get("OperationType").equalsIgnoreCase("Save As Draft") && bStatus){
				NewUICommonFunctions.selectMenu("Dashboard","None");
				bStatus = NewUICommonFunctions.searchValueinMastersAndTransactionTableWithFilters(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.DRAFTS,mapClassDetails.get("Class Name"));
				if (!bStatus) {
					Reporting.logResults("Fail","Verify Class in Saved As Draft Items", mapClassDetails.get("Class Name")+" Class is not visible in Saved as Draft Items");
					continue;
				}
				Reporting.logResults("Pass","Verify Class in Saved As Draft Items", mapClassDetails.get("Class Name")+" Class is visible in Saved as Draft Items");
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
