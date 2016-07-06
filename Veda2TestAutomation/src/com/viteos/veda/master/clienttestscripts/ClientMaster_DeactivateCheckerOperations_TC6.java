package com.viteos.veda.master.clienttestscripts;

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

public class ClientMaster_DeactivateCheckerOperations_TC6 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Client Master";
		Reporting.Testcasename = "Open Application";

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

			Map<String, Map<String, String>> mapAllClientDetails = Utilities.readMultipleTestData(Global.sClientTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllClientDetails.size();index++){

				Map<String, String> mapClientDetails = mapAllClientDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();

				if(mapClientDetails.get("Operation").equalsIgnoreCase("Validate") || mapClientDetails.get("Operation").equalsIgnoreCase("Modify") || mapClientDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				Reporting.Testcasename = mapClientDetails.get("TestcaseNameRef");

				//Navigate to dashboard 
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				if(mapClientDetails.get("Operation").equalsIgnoreCase("DeActivate")){

					Map<String, String> createdClientDetails = ExcelUtils.readDataABasedOnCellName(Global.sClientTestDataFilePath,"ClientTestData", mapClientDetails.get("TestcaseNameRef"));
					boolean bNameModifyStatus = false;
					Map<String, String> modifiedClientDetails = ExcelUtils.readDataABasedOnCellName(Global.sClientTestDataFilePath,"ModifyTestdata", mapClientDetails.get("TestcaseNameRef"));
					if(modifiedClientDetails!=null && modifiedClientDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedClientDetails.containsKey("ClientName")){
							bNameModifyStatus = true;
							mapClientDetails.put("ClientName", modifiedClientDetails.get("ClientName"));
						}
					}
					if(!bNameModifyStatus){
						mapClientDetails.put("ClientName", createdClientDetails.get("ClientName"));
					}


					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapClientDetails.get("ClientName"),mapClientDetails.get("CheckerOperations"));
					if(bStatus && mapClientDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapClientDetails.get("CheckerOperations"));
					}

					if(!bStatus && mapClientDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapClientDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(!bStatus && mapClientDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+mapClientDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(bStatus && mapClientDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Cleint Name: "+mapClientDetails.get("ClientName"));
						continue;
					}	

					if(mapClientDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						
						innerMap.put("Status", "inactivate");
						innerMap.put("Client Name", mapClientDetails.get("ClientName"));
						VerifyMap.put(Reporting.Testcasename,innerMap);
					}
				}
			}

			//verifyValuesinSearchPanel(VerifyMap);
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
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error ");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}



	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyClient) {
		try{

			for (Entry<String, Map<String, String>> test : verifyClient.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Client Master
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Client");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to Client Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Client in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Client Name", test.getValue().get("ClientName"), test.getValue().get("Status"),"ClientMaster", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify client shouldnot be in active state",test.getKey()+" Client is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify client shouldnot be in active state",test.getKey()+" Client is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify client should be in active state",test.getKey()+" Client is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify client should be in active state",test.getKey()+" Client is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
