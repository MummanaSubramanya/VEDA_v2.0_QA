package com.viteos.veda.master.openingbalancetestscripts;

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
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.OpeningBalanceMasterAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class OpeningBalanceMaster_ModifyCheckerOperations_TS4 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setup(){

		Reporting.Functionality ="Opening Balance Master Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		//XMLLibrary.sOpeningBalanceDetailsXMLFilePath = "XMLMessages//OpeningBalanceDetails02-25-2016202743.xml";
		
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
			Map<String, Map<String, String>> mapAllOpeningBalanceDetails = Utilities.readMultipleTestData(Global.sOpeningBalanceTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> verifyOpeningBalance = new HashMap<>();
			for(int index = 1;index <= mapAllOpeningBalanceDetails.size();index++){
				Map<String, String> mapModifyOpeningBalanceDetailsReference = mapAllOpeningBalanceDetails.get("Row"+index);
				Map<String, String> innerMap = new HashMap<String, String>();
				if(mapModifyOpeningBalanceDetailsReference.get("Operation") == null || !mapModifyOpeningBalanceDetailsReference.get("Operation").equalsIgnoreCase("Modify")){
					continue;
				}

				Map<String, String> mapXMLOpeningBalanceDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "OpeningBalance", mapModifyOpeningBalanceDetailsReference.get("TestcaseNameRef"));
				
				if (mapXMLOpeningBalanceDetails == null || mapXMLOpeningBalanceDetails.isEmpty()) {
					continue;
				}				
				
				Map<String, String> mapActualOpeningBalanceDetails = ExcelUtils.readDataABasedOnCellName(Global.sOpeningBalanceTestDataFilePath, "OpeningBalanceTestData", mapModifyOpeningBalanceDetailsReference.get("TestcaseNameRef"));
				
				Reporting.Testcasename = mapModifyOpeningBalanceDetailsReference.get("TestcaseNameRef");
				
				Map<String, String> mapClubbedOpeningBalanceDetails = new HashMap<String, String>();
				mapClubbedOpeningBalanceDetails.putAll(mapActualOpeningBalanceDetails);
				if (mapModifyOpeningBalanceDetailsReference != null && !mapModifyOpeningBalanceDetailsReference.isEmpty()) {
					mapClubbedOpeningBalanceDetails.putAll(mapModifyOpeningBalanceDetailsReference);
				}
				
				//navigate dash-board.
				NewUICommonFunctions.refreshThePage();
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 15);
				bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Dashboard", "Dashboard Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Dashboard", "Dashboard Menu selected succesfully");

				if(mapModifyOpeningBalanceDetailsReference.get("Operation").equalsIgnoreCase("Modify") && (mapActualOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Approve") || mapActualOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Return")))
				{	
					bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapXMLOpeningBalanceDetails.get("SearchID"), mapModifyOpeningBalanceDetailsReference.get("CheckerOperations"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Search for the Modified record and perform checker Operation : '"+mapModifyOpeningBalanceDetailsReference.get("CheckerOperations")+"' .", "Failed to perform checker operation : '"+mapModifyOpeningBalanceDetailsReference.get("CheckerOperations")+"', on Record with TXN-ID : "+mapXMLOpeningBalanceDetails.get("SearchID")+", Can not do further verification.][ "+Messages.errorMsg);
						continue;
					}
					else {
						Reporting.logResults("Pass", "Search for the Modified record and perform checker Operation : '"+mapModifyOpeningBalanceDetailsReference.get("CheckerOperations")+"' .", "Successfully performed checker operation : '"+mapModifyOpeningBalanceDetailsReference.get("CheckerOperations")+"', on Record with TXN-ID : "+mapXMLOpeningBalanceDetails.get("SearchID")+" ");							
						NewUICommonFunctions.refreshThePage();
					}
				}
				if(mapModifyOpeningBalanceDetailsReference.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("SearchID", mapXMLOpeningBalanceDetails.get("SearchID"));
					verifyOpeningBalance.put(Reporting.Testcasename,innerMap);
				}
				if(mapModifyOpeningBalanceDetailsReference.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){				
					innerMap.put("Status", "inactive");
					innerMap.put("SearchID", mapXMLOpeningBalanceDetails.get("SearchID"));
					verifyOpeningBalance.put(Reporting.Testcasename,innerMap);
				}	
				bStatus = NewUICommonFunctions.selectMenu("TRADE", "Opening Balance");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to OpeningBalance Under TRADE", "OpeningBalance Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				else {
					Reporting.logResults("Pass", "Navigate to OpeningBalance Under TRADE", "OpeningBalance Menu selected successfully.");
				}
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 15);
				//get operation type to be done to modify
				if(mapModifyOpeningBalanceDetailsReference.get("Operation").equalsIgnoreCase("Modify") && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Pass")){					

					//do modify for the approved formula and Fee
					if(mapActualOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && mapModifyOpeningBalanceDetailsReference.get("CheckerOperations").equalsIgnoreCase("Approve"))
					{						
						bStatus = NewUICommonFunctions.verifyTheRecordInSearchPanelWithStatusAndTxnIDAlongWithOperation("active", "Opening balance Id", mapXMLOpeningBalanceDetails.get("SearchID"), "OpeningBalance", 5, "Modify");
						if (!bStatus) {
							Reporting.logResults("Fail", "Search for the record and Click on Edit button to Verify the modified details.", "Failed to Edit the Opening Balance Record with TXN-ID : "+mapXMLOpeningBalanceDetails.get("SearchID")+", to Verify the modified details ERROR: "+Messages.errorMsg);
							continue;
						}
						
						bStatus = OpeningBalanceMasterAppFunctions.doVerifyOpeningBalanceDetails(mapClubbedOpeningBalanceDetails);
						
						if(!bStatus){
							Reporting.logResults("Fail", "Verify the modified OpeningBalance Data.", "Modified OpeningBalance Data is not reflected , Error: "+Messages.errorMsg );
							continue;
						}
						else {
							Reporting.logResults("Pass", "Verify the modified OpeningBalance Data.", "Modified OpeningBalance Data is reflected as expected.");							
						}											
					}

					//do modify for the Approved Formula and returned Incentive Fee.
					if(mapActualOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Return") && mapModifyOpeningBalanceDetailsReference.get("CheckerOperations").equalsIgnoreCase("Approve"))
					{											
						bStatus = NewUICommonFunctions.verifyTheRecordInSearchPanelWithStatusAndTxnIDAlongWithOperation("active", "Opening balance Id", mapXMLOpeningBalanceDetails.get("SearchID"), "OpeningBalance", 5, "Modify");
						if (!bStatus) {
							Reporting.logResults("Fail", "Search for the record and Click on Edit button to modify.", "Failed to Edit the Opening Balance Record with TXN-ID : "+mapXMLOpeningBalanceDetails.get("SearchID")+", to Verify the returned record modified details ERROR: "+Messages.errorMsg);
							continue;
						}
						bStatus = OpeningBalanceMasterAppFunctions.doVerifyOpeningBalanceDetails(mapClubbedOpeningBalanceDetails);

						if(!bStatus){
							Reporting.logResults("Fail", "Verify the modified OpeningBalance Data.", "Modified OpeningBalance Data is not reflected , Error: "+Messages.errorMsg );
							continue;
						}
						else {
							Reporting.logResults("Pass", "Verify the modified OpeningBalance Data.", "Modified OpeningBalance Data is reflected as expected.");							
						}			
					}
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
