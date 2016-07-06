package com.viteos.veda.master.openingbalancetestscripts;

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
import com.viteos.veda.master.lib.OpeningBalanceMasterAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class OpeningBalanceMaster_ModifyMakerOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setup(){

		Reporting.Functionality ="Opening Balance Master Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;
		
		//XMLLibrary.sOpeningBalanceDetailsXMLFilePath = "XMLMessages//OpeningBalanceDetails02-25-2016225914.xml";
		
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
			Map<String, Map<String, String>> mapAllOpeningBalanceDetails = Utilities.readMultipleTestData(Global.sOpeningBalanceTestDataFilePath,sSheetName,"Y");
			for(int index = 1;index <= mapAllOpeningBalanceDetails.size();index++){
				Map<String, String> mapModifyOpeningBalanceDetailsReference = mapAllOpeningBalanceDetails.get("Row"+index);

				if(mapModifyOpeningBalanceDetailsReference.get("Operation") == null || mapModifyOpeningBalanceDetailsReference.get("Operation").equalsIgnoreCase("Activate") || mapModifyOpeningBalanceDetailsReference.get("Operation").equalsIgnoreCase("Deactivate")){
					continue;
				}

				Map<String, String> mapXMLOpeningBalanceDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "OpeningBalance", mapModifyOpeningBalanceDetailsReference.get("TestcaseNameRef"));

				if (mapXMLOpeningBalanceDetails == null || mapXMLOpeningBalanceDetails.isEmpty()) {
					continue;
				}

				Map<String, String> mapActualOpeningBalanceDetails = ExcelUtils.readDataABasedOnCellName(Global.sOpeningBalanceTestDataFilePath, "OpeningBalanceTestData", mapModifyOpeningBalanceDetailsReference.get("TestcaseNameRef"));

				Reporting.Testcasename = mapModifyOpeningBalanceDetailsReference.get("TestcaseNameRef");

				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("TRADE", "Opening Balance");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to OpeningBalance Under TRADE", "OpeningBalance Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to OpeningBalance Under TRADE", "OpeningBalance Menu selected succesfully");
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 15);
				//get operation type to be done to modify
				if(mapModifyOpeningBalanceDetailsReference.get("Operation").equalsIgnoreCase("Modify")){					

					//do modify for the approved formula and Fee
					if(mapActualOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Approve"))
					{	
						bStatus = NewUICommonFunctions.verifyTheRecordInSearchPanelWithStatusAndTxnIDAlongWithOperation("active", "Opening balance Id", mapXMLOpeningBalanceDetails.get("SearchID"), "OpeningBalance", 5, "Modify");
						if (!bStatus) {
							Reporting.logResults("Fail", "Search for the record and Click on Edit button to modify.", "Failed to Edit the Opening Balance Record with TXN-ID : "+mapXMLOpeningBalanceDetails.get("SearchID")+" ERROR: [ "+Messages.errorMsg+" ]");
							continue;
						}
						bStatus = OpeningBalanceMasterAppFunctions.doAddOpeningBalanceDetails(mapModifyOpeningBalanceDetailsReference);

						if(!bStatus && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The OpeningBalance Data", "OpeningBalance cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}

						if(!bStatus && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The OpeningBalance Data", "Negative testdata - Modification of OpeningBalance Failed.");
							continue;
						}

						if(bStatus && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The OpeningBalance Data", "OpeningBalance - modified with the Tesdata");
							continue;
						}

						if(bStatus && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The OpeningBalance Data", "Modification of OpeningBalance got created with negative testdata.");
							continue;
						}												
					}

					//do modify for the Approved Formula and returned Incentive Fee.
					if(mapActualOpeningBalanceDetails.get("CheckerOperations").equalsIgnoreCase("Return"))
					{
						//select the dash board.
						NewUICommonFunctions.refreshThePage();
						NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 15);
						NewUICommonFunctions.selectMenu("Dashboard", "None");

						bStatus = OpeningBalanceMasterAppFunctions.modifyReturnedOpeningBalanceDetails(mapModifyOpeningBalanceDetailsReference, mapXMLOpeningBalanceDetails.get("SearchID"));

						if(!bStatus && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Returned OpeningBalance should get saved.", "Returned OpeningBalance didn't get saved with test data. Error: "+Messages.errorMsg );
							continue;
						}

						if(!bStatus && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Returned OpeningBalance Data should not get saved", "Modification of Returned OpeningBalance did not saved with negative test data.");
							continue;
						}

						if(bStatus && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify Returned OpeningBalance should get saved.", "Returned OpeningBalance modified with the Tesdata.");
							continue;
						}

						if(bStatus && mapModifyOpeningBalanceDetailsReference.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify Returned OpeningBalance should not get saved with Negative Data.", "Modification of Returned OpeningBalance got saved with negative testdata.");
							continue;
						}			
					}
				}

				//do validate the data when clicked on modify the data
				if(mapModifyOpeningBalanceDetailsReference.get("Operation").equalsIgnoreCase("Validate")){
					bStatus = NewUICommonFunctions.verifyTheRecordInSearchPanelWithStatusAndTxnIDAlongWithOperation("active", "Opening balance Id", mapXMLOpeningBalanceDetails.get("SearchID"), "OpeningBalance", 5, "Validate");
					if (!bStatus) {
						Reporting.logResults("Fail", "Search for the record and Click on Edit button to Validate the details.", "Failed to Edit the Opening Balance Record to validate the details with TXN-ID: "+mapXMLOpeningBalanceDetails.get("SearchID")+" ][ "+Messages.errorMsg);
						continue;
					}
					bStatus = OpeningBalanceMasterAppFunctions.doVerifyOpeningBalanceDetails(mapActualOpeningBalanceDetails);
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
