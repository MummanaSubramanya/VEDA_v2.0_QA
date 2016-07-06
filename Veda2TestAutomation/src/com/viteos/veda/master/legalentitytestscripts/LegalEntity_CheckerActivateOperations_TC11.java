package com.viteos.veda.master.legalentitytestscripts;

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
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class LegalEntity_CheckerActivateOperations_TC11 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Legal Entity Master";
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
			Map<String, Map<String, String>> mapAllModifyLegalEntityDetails = Utilities.readMultipleTestData(Global.sModifyLegalEntityTestDataFilePath, sSheetName,"Y");
			Map<String, Map<String, String>> verificationMap = new HashMap<String, Map<String,String>>();
			for(int index = 1;index <= mapAllModifyLegalEntityDetails.size();index++){

				Map<String, String> mapLeagalEntitySearchDetails = mapAllModifyLegalEntityDetails.get("Row"+index);	
				
				Map<String, String> innerMap = new HashMap<String, String>();

				if (mapLeagalEntitySearchDetails.get("Operation") == null || !mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Activate")) {
					continue;
				}

				Reporting.Testcasename = mapLeagalEntitySearchDetails.get("TestcaseNameRef");
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Legal Entity Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Legal Entity Mater Under Fund Setup", "Legal Entity Menu selected succesfully");

				boolean bNameStatus = false;
				Map<String, String> mapActualLEDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "LegalEntityDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedLEDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyLegalEntityTestDataFilePath, "ModifyLEDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedMainTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyLegalEntityTestDataFilePath, "ModifyTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));

				if(mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Activate")){

					if (mapModifiedLEDetailsTabData!= null && mapModifiedMainTabData!=null && mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify")) {
						if (mapModifiedLEDetailsTabData.containsKey("Legal Entity Name")) {
							bNameStatus = true;
							mapLeagalEntitySearchDetails.put("Legal Entity Name", mapModifiedLEDetailsTabData.get("Legal Entity Name"));
							//mapLeagalEntitySearchDetails.put("CheckerOperations", mapModifiedLEDetailsTabData.get("CheckerOperations"));
						}
					}
					if(!bNameStatus){
						mapLeagalEntitySearchDetails.put("Legal Entity Name", mapActualLEDetailsTabData.get("Legal Entity Name"));
						//mapLeagalEntitySearchDetails.put("CheckerOperations", mapActualLEDetailsTabData.get("CheckerOperations"));
					}

					//get the checker operations
					Map<String, String> mapModifiedCurLEDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyLegalEntityTestDataFilePath, "ModifyLEDetailsTestData", mapLeagalEntitySearchDetails.get("TCID"));
					mapLeagalEntitySearchDetails.put("CheckerOperations", mapModifiedCurLEDetailsTabData.get("CheckerOperations"));


					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapLeagalEntitySearchDetails.get("Legal Entity Name"), mapLeagalEntitySearchDetails.get("CheckerOperations"));
					if(!bStatus){
						Reporting.logResults("Fail","Checker : Activating Legal Entity", "Checker : Legal Entity : "+mapLeagalEntitySearchDetails.get("Legal Entity Name")+" has not been Activated");
						continue;
					}
					
					Reporting.logResults("Pass","Checker : Validate Legal Entity has been Activated", "Checker : Legal Entity : "+mapLeagalEntitySearchDetails.get("Legal Entity Name")+" Has been Activated");
					if(mapLeagalEntitySearchDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						innerMap.put("Status", "active");
						innerMap.put("Legal Entity Name", mapLeagalEntitySearchDetails.get("Legal Entity Name"));
						verificationMap.put(Reporting.Testcasename,innerMap);
					}				
					
				}
			}
			verifyValuesinSearchPanel(verificationMap);
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

	public static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyLE) {
		try{

			for (Entry<String, Map<String, String>> test : verifyLE.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Series Master
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Legal Entity");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Legal Entity Master","Cannot Navigate to Legal Entity Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Legal Entity Master", "Navigated to Legal Entity Master");


				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Series in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"),"LegalEntity", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Legal Entity Name shouldnot be in active state",test.getValue().get("Legal Entity Name")+" Legal Entity Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Legal Entity Name shouldnot be in active state",test.getValue().get("Legal Entity Name")+" Legal Entity Name is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Legal Entity Name should be in active state",test.getValue().get("Legal Entity Name")+" Legal Entity Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Legal Entity Name should be in active state",test.getValue().get("Legal Entity Name")+" Legal Entity Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
