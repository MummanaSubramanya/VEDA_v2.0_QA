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

public class LegalEntityMaster_ModifyCheckerOperations_TC6 {

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
	public static void testCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllModifiedDetails = Utilities.readMultipleTestData(Global.sModifyLegalEntityTestDataFilePath, sSheetName, "Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();
			Map<String, Map<String, String>> objLEModifyTabsMaps = new HashMap<String, Map<String,String>>();

			for(int index = 1;index <= mapAllModifiedDetails.size();index++){

				Map<String, String> mapModifiedDetails = mapAllModifiedDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();

				if(mapModifiedDetails.get("Operation").equalsIgnoreCase("Validate") || mapModifiedDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapModifiedDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				Reporting.Testcasename = mapModifiedDetails.get("TestcaseNameRef");

				Map<String, String> modfiedLegalEntityMap =  Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath,"ModifyLEDetailsTestData", mapModifiedDetails.get("TestcaseNameRef"));

				if(modfiedLegalEntityMap.get("ExpectedResults")!=null && modfiedLegalEntityMap.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}

				if(modfiedLegalEntityMap.get("OperationType")!=null && modfiedLegalEntityMap.get("OperationType").equalsIgnoreCase("Cancel")){
					continue;
				}

				Map<String, String> mapCreatedLegalEntityDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath,"LegalEntityDetailsTestData", mapModifiedDetails.get("TestcaseNameRef"));

				Map<String , String> mapModifyGeneralDetails = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyGeneralDetailsTestData", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifySubscriptionDetials = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifySubscriptionTestData", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifyOtherDetials = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyOtherDetailsTestData", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifyRedemption = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyRedemptionTestData", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifyTransfer = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyTransferTestData", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifySwitch = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifySwitchTestData", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifyExchange = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyExchangeTestData", mapModifiedDetails.get("TestcaseNameRef"));

				objLEModifyTabsMaps.put("LegalEntityDetails", modfiedLegalEntityMap);
				objLEModifyTabsMaps.put("GeneralDetails", mapModifyGeneralDetails);
				objLEModifyTabsMaps.put("SubscriptionDetails", mapModifySubscriptionDetials);
				objLEModifyTabsMaps.put("OtherDetails", mapModifyOtherDetials);
				objLEModifyTabsMaps.put("RedemptionDetails", mapModifyRedemption);
				objLEModifyTabsMaps.put("TransferDetails", mapModifyTransfer);
				objLEModifyTabsMaps.put("SwitchDetails", mapModifySwitch);
				objLEModifyTabsMaps.put("ExchangeDetails", mapModifyExchange);

				if(modfiedLegalEntityMap.get("Legal Entity Name")==null){
					modfiedLegalEntityMap.put("Legal Entity Name",mapCreatedLegalEntityDetails.get("Legal Entity Name"));
				}

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");


				if(mapModifiedDetails.get("Operation").equalsIgnoreCase("Modify")){
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,modfiedLegalEntityMap.get("Legal Entity Name"),modfiedLegalEntityMap.get("CheckerOperations"));
					if(bStatus && modfiedLegalEntityMap.get("CheckerOperations").equalsIgnoreCase("Approve")){
						NewUICommonFunctions.selectMenu("Fund Setup","Legal Entity");

						boolean bTestStatus = NewUICommonFunctions.verifyMasterDetails("Legal Entity Name", modfiedLegalEntityMap.get("Legal Entity Name"), "LegalEntity", objLEModifyTabsMaps);

						if(!bTestStatus){
							Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved.Error: "+Messages.errorMsg);
						}
					}

					if(bStatus && modfiedLegalEntityMap.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+modfiedLegalEntityMap.get("CheckerOperations"));
					}

					if(!bStatus && modfiedLegalEntityMap.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+modfiedLegalEntityMap.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(!bStatus && modfiedLegalEntityMap.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+modfiedLegalEntityMap.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					}

					if(bStatus && modfiedLegalEntityMap.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Legal Entity Name: "+modfiedLegalEntityMap.get("Legal Entity Name"));
						continue;
					}	

					if(modfiedLegalEntityMap.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						innerMap.put("Status", "active");
						innerMap.put("Legal Entity Name", modfiedLegalEntityMap.get("Legal Entity Name"));
						VerifyMap.put(Reporting.Testcasename,innerMap );
					}
					if(modfiedLegalEntityMap.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){
						innerMap.put("Status", "inactive");
						innerMap.put("Legal Entity Name", modfiedLegalEntityMap.get("Legal Entity Name"));
						VerifyMap.put(Reporting.Testcasename,innerMap );
					}
				}				
			}

			verifyValuesinSearchPanel(VerifyMap);
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
