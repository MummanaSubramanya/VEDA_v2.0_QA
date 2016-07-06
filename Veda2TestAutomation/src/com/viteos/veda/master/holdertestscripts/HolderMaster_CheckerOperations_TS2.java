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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class HolderMaster_CheckerOperations_TS2 {
	static boolean bStatus;
	static String sRefrenceSheetName = "GeneralDetails";

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
	public static void testHolderCheckerOperations(){
		try {
			Map<String,Map<String,String>> mapAllHoldersDetails = Utilities.readMultipleTestData(Global.sHolderTestDataFilePath, sRefrenceSheetName, "Y");
			Map<String, Map<String, String>> verifyHolder = new HashMap<>();
			for(int index = 1;index <= mapAllHoldersDetails.size();index++){				
				Map<String, String> innerMap = new HashMap<String, String>();
				Map<String, String> mapHolderGeneralDetails = mapAllHoldersDetails.get("Row"+index);
				if (mapHolderGeneralDetails.get("CheckerOperations") == null || mapHolderGeneralDetails.get("OperationType") == null || mapHolderGeneralDetails.get("OperationType").equalsIgnoreCase("Cancel")) {
					continue;
				}
				String sHolderFirstName = "";
				String sHolderLastName = "";
				String sHolderMiddleName = "";
				String sHolderName = "";			
				
				Reporting.Testcasename = mapHolderGeneralDetails.get("TestcaseName");						
				
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
				bStatus=NewUICommonFunctions.selectMenu("Dashboard", "None");
				
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Dashboard", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Dashboard", "Dashboard selected succesfully");
				
				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sHolderName, mapHolderGeneralDetails.get("CheckerOperations"));
				
				if(bStatus && mapHolderGeneralDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapHolderGeneralDetails.get("CheckerOperations"));
				}

				if(!bStatus && mapHolderGeneralDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapHolderGeneralDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && mapHolderGeneralDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations : "+mapHolderGeneralDetails.get("CheckerOperations")+".");
				}

				if(bStatus && mapHolderGeneralDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Error: "+Messages.errorMsg+ " : "+mapHolderGeneralDetails);
					continue;
				}	

				if(mapHolderGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("Holder Name", sHolderName);
					verifyHolder.put(Reporting.Testcasename,innerMap);
				}
				if(mapHolderGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){				
					innerMap.put("Status", "inactive");
					innerMap.put("Holder Name", sHolderName);
					verifyHolder.put(Reporting.Testcasename,innerMap);
				}			
			}
			//Verify Checker Operations are taken place.
			verifyValuesinSearchPanel(verifyHolder);		
		} catch (Exception e) {
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
	
	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyHolder) {
		try{
			for (Entry<String, Map<String, String>> test : verifyHolder.entrySet()) {				

				Reporting.Testcasename = test.getKey();

				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Holder");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Holder","Cannot Navigate to Holder Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Holder", "Navigated to Holder page");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				//search function for Incentive Fee.
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Investor Holder Name", test.getValue().get("Holder Name"), test.getValue().get("Status"), "InvestorHolder", time);			
				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Checker : Verify Holder should Not be Active in Search Grid.",test.getValue().get("Holder Name")+" : Holder is Not-Active in Search Grid.");
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Checker : Verify Holder should be Inactive in Search Grid.",test.getValue().get("Holder Name")+" : Holder is Active in Search Grid.");
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Checker : Verify Holder should be Active in Search Grid.",test.getValue().get("Holder Name")+" : Holder is Not-Active in Search Grid.");
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Checker : Verify Holder should be Active in Search Grid.",test.getValue().get("Holder Name")+" : Holder is Active in Search Grid.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
