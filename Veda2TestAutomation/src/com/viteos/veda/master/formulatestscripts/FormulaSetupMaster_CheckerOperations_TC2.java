package com.viteos.veda.master.formulatestscripts;

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
import com.viteos.veda.master.lib.TempFunctions;

public class FormulaSetupMaster_CheckerOperations_TC2 {

	static boolean bStatus;
	static String sSheetName = "FormulaTestData";

	@BeforeMethod
	public static void setUp(){
		
		Reporting.Functionality ="Formula Setup Master";
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
			
			Map<String, Map<String, String>> mapAllClientDetails = Utilities.readMultipleTestData(Global.sFormulaSetupTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> verifyFormula = new HashMap<String, Map<String, String>>();
			

			for(int index = 1;index <= mapAllClientDetails.size();index++){
				
				Map<String, String> innerMap = new HashMap<String, String>();
				
				Map<String, String> mapFormulaDetails = mapAllClientDetails.get("Row"+index);
				if(!mapFormulaDetails.get("OperationType").equalsIgnoreCase("Save") || mapFormulaDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}
				if(mapFormulaDetails.get("CheckerOperations").equalsIgnoreCase("None") || mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("None")){
					continue;
				}
				Reporting.Testcasename = mapFormulaDetails.get("TestcaseName");
				
				//Navigate to DashBoard

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");
				
				//Perform Checker OPeration
				
				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW, mapFormulaDetails.get("FeeRuleName"), mapFormulaDetails.get("CheckerOperations"));
				
				if(bStatus && mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					
					Reporting.logResults("Pass","Perform Checker Operation: "+mapFormulaDetails.get("CheckerOperations"), "Successfully Performed checker operations for formula: "+mapFormulaDetails.get("FeeRuleName"));
				}
				if(!bStatus && mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					
					Reporting.logResults("Fail","Perform Checker Operation: "+mapFormulaDetails.get("CheckerOperations"), "Cannot Perform Checker Operations for formula: "+mapFormulaDetails.get("FeeRuleName")+". Error: "+Messages.errorMsg);
					continue;
				}
				if(!bStatus && mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					
					Reporting.logResults("Pass","Perform Checker Operation: "+mapFormulaDetails.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations .Error: "+Messages.errorMsg);
				}

				if(bStatus && mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					
					Reporting.logResults("Fail","Perform Checker Operation: "+mapFormulaDetails.get("CheckerOperations"), "Peformed Checker operations with negative testdata. Formula Name: "+mapFormulaDetails.get("FeeRuleName"));
					continue;
				}	
				
				//Approve the Formula
				
				if(mapFormulaDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){					
					innerMap.put("Status", "active");
					innerMap.put("FeeRuleName",mapFormulaDetails.get("FeeRuleName"));
					innerMap.put("FeeType", mapFormulaDetails.get("FeeType"));
					verifyFormula.put(Reporting.Testcasename,innerMap );
				}
				
				//Reject theFormula
				if(mapFormulaDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){					
					innerMap.put("Status", "inactive");
					innerMap.put("FeeRuleName",mapFormulaDetails.get("FeeRuleName"));
					innerMap.put("FeeType", mapFormulaDetails.get("FeeType"));
					verifyFormula.put(Reporting.Testcasename,innerMap );
				}
			}
			
			verifyValuesinSearchPanel(verifyFormula);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyFormula) {
		try{

			for (Entry<String, Map<String, String>> test : verifyFormula.entrySet()) {

					Reporting.Testcasename = test.getKey();

					bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Formula");
					if(!bStatus){
						Reporting.logResults("Fail", "Navigate To Formula Setup master Menu","Cannot Navigate to Formula Setup Master");
						continue;
					}
					Reporting.logResults("Pass","Navigate To Formula Setup master Menu", "Navigated to Formula Setup Master");

					//bStatus = FormulaSetupAppFunctions.searchFormulas(test.getValue().get("FeeType"), test.getKey(), test.getValue().get("Status"),15); 
					bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Fee Type", test.getValue().get("FeeType"), test.getValue().get("Status"),"Fee Rule Name",  test.getValue().get("FeeRuleName"), "Rule", 10);
					if(bStatus != true && test.getValue().get("Status").equalsIgnoreCase("inactive")){
						Reporting.logResults("Fail", "Verify formula should be Inactive",test.getValue().get("FeeRuleName")+" Formula is not in Inactive state yet.");
						//continue;
					}
					if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
						Reporting.logResults("Pass", "Verify formula should be Inactive",test.getValue().get("FeeRuleName")+" Formula is  Inactive state");
					}

					if(bStatus != true && test.getValue().get("Status").equalsIgnoreCase("active")){
						Reporting.logResults("Fail", "Verify formula should be Active",test.getValue().get("FeeRuleName")+" Formula is Not  Active state");
						//continue;
					}
					if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
						Reporting.logResults("Pass", "Verify formula should be Active",test.getValue().get("FeeRuleName")+" Formula is  Active state");
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
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
