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
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.TempFunctions;

public class FormulaSetupMaster_DeActivateCheckerOperations_TC6 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";

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

			Map<String, Map<String, String>> mapAllFormulaDetails = Utilities.readMultipleTestData(Global.sFormulaSetupTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllFormulaDetails.size();index++){

				Map<String, String> mapFormulaDetails = mapAllFormulaDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();

				if(mapFormulaDetails.get("Operation").equalsIgnoreCase("Validate") || mapFormulaDetails.get("Operation").equalsIgnoreCase("Modify") || mapFormulaDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}
				
				if(mapFormulaDetails.get("ExpectedResults")!=null && mapFormulaDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}
				if(mapFormulaDetails.get("OperationType")!=null && mapFormulaDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					continue;
				}

				Reporting.Testcasename = mapFormulaDetails.get("TestcaseNameRef");

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");


				if(mapFormulaDetails.get("Operation").equalsIgnoreCase("DeActivate")){

					Map<String, String> createdFormulaDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath,"FormulaTestData", mapFormulaDetails.get("TestcaseNameRef"));

					Map<String, String> modifiedFormulaDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath,"ModifyTestdata", mapFormulaDetails.get("TestcaseNameRef"));
					
/*					boolean FeeType = false;
					if(modifiedFormulaDetails!=null && modifiedFormulaDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedFormulaDetails.containsKey("FeeType")){
							FeeType =true ;
							mapFormulaDetails.put("FeeType", modifiedFormulaDetails.get("FeeType"));
						}
					}
					if(!FeeType){
						mapFormulaDetails.put("FeeType", createdFormulaDetails.get("FeeType"));
					}
					boolean FeeRuleName = false;
					if(modifiedFormulaDetails!=null && modifiedFormulaDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedFormulaDetails.containsKey("FeeRuleName")){
							FeeRuleName =true;
							mapFormulaDetails.put("FeeRuleName", modifiedFormulaDetails.get("FeeRuleName"));
						}
					}
					if(!FeeRuleName){
						mapFormulaDetails.put("FeeRuleName", createdFormulaDetails.get("FeeRuleName"));
					}*/
					
					createdFormulaDetails.putAll(modifiedFormulaDetails);

					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,createdFormulaDetails.get("FeeRuleName"), createdFormulaDetails.get("CheckerOperations"));
					if(bStatus && mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed DeActive operation with checker operations: "+mapFormulaDetails.get("CheckerOperations"));
					}

					if(!bStatus && mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform DeActive operation with Checker Operations: "+mapFormulaDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(!bStatus && mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform DeActive operation with Checker Operations: "+mapFormulaDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					}

					if(bStatus && mapFormulaDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker DeActive operation with operations with negative testdata. Formula Name: "+mapFormulaDetails.get("FeeRuleName"));
						continue;
					}	

					if(mapFormulaDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						if(mapFormulaDetails.get("Operation").equalsIgnoreCase("DeActivate")){
							innerMap.put("Status", "inactive");
							innerMap.put("FeeRuleName",createdFormulaDetails.get("FeeRuleName"));
							innerMap.put("FeeType", createdFormulaDetails.get("FeeType"));
							VerifyMap.put(Reporting.Testcasename,innerMap );
							continue;
						}
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
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
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
}
