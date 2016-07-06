package com.viteos.veda.master.parametertestscripts;

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
import com.viteos.veda.master.lib.XMLLibrary;

public class ParameterMaster_ActivateCheckerOperations_TC10 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Parameter Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;
		//XMLLibrary.sFundSetupXMLFilePath = "XMLMessages//Parameters01-16-2016064332.xml";
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

			Map<String, Map<String, String>> mapAllParameterDetails = Utilities.readMultipleTestData(Global.sParameterTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllParameterDetails.size();index++){

				Map<String, String> mapModifyParameterDetails = mapAllParameterDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();
				
				Map<String, String> createdParameterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFundSetupXMLFilePath, "parameter",mapModifyParameterDetails.get("TestcaseNameRef")) ;
				if(createdParameterDetails == null){
					continue;
				}
				
				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Validate") || mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Modify")|| mapModifyParameterDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}
				
				if(mapModifyParameterDetails.get("ExpectedResults")!=null && mapModifyParameterDetails.get("ExpectedResults").equalsIgnoreCase("Fail") ||mapModifyParameterDetails.get("OperationType") !=null && mapModifyParameterDetails.get("OperationType").equalsIgnoreCase("Cancel") ){
					continue;
				}
				Reporting.Testcasename = mapModifyParameterDetails.get("TestcaseNameRef");

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");
				
				
				mapModifyParameterDetails.put("parameterID", createdParameterDetails.get("parameterID"));
				mapModifyParameterDetails.put("ParameterName", createdParameterDetails.get("ParameterName"));
				
				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Activate")){
					bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW, mapModifyParameterDetails.get("parameterID"),mapModifyParameterDetails.get("CheckerOperations"));

					if(bStatus && mapModifyParameterDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapModifyParameterDetails.get("CheckerOperations"));
					}

					if(!bStatus && mapModifyParameterDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapModifyParameterDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(!bStatus && mapModifyParameterDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+mapModifyParameterDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					}

					if(bStatus && mapModifyParameterDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Formula Name: "+mapModifyParameterDetails.get("FeeRuleName"));
						continue;
					}	

					if(mapModifyParameterDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						innerMap.put("Status", "active");
						innerMap.put("LabelValue", mapModifyParameterDetails.get("ParameterName"));
						innerMap.put("ParameterID",mapModifyParameterDetails.get("parameterID"));
						VerifyMap.put(Reporting.Testcasename,innerMap);
					}
				}
			}
			//search for the parameter has to be implmented
			verifyValuesinSearchPanel(VerifyMap);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@AfterMethod
	public static void tearDown(){
		Reporting.Testcasename = "Close app";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}



	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyParameter) {
		try{

			for (Entry<String, Map<String, String>> test : verifyParameter.entrySet()) {

					Reporting.Testcasename = test.getKey();

					bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Parameter");
					if(!bStatus){
						Reporting.logResults("Fail", "Navigate To Parameter","Cannot Navigate to Parameter Page");
						continue;
					}
					Reporting.logResults("Pass","Navigate To Parameter", "Navigated to Parameter page");

					//search function
					bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Parameter Name", test.getValue().get("LabelValue"),test.getValue().get("Status"), "Parameter Id",test.getValue().get("ParameterID") ,"Parameter", 10);
					if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
						Reporting.logResults("Pass", "Verify parameter is  Inactive stage",test.getValue().get("ParameterID")+" Parameter is Not Inactive stage. ");
						//continue;
					}
					if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
						Reporting.logResults("Fail", "Verify parameter is  Inactive stage",test.getValue().get("ParameterID")+" Parameter is  InActive stage");
					}

					if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
						Reporting.logResults("Fail", "Verify parameter is  Active stage",test.getValue().get("ParameterID")+" Parameter is Not Active stage. Error: "+Messages.errorMsg);
						//continue;
					}
					if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
						Reporting.logResults("Pass", "Verify parameter is in active stage",test.getValue().get("ParameterID")+" Parameter is  Active stage");
					}
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
