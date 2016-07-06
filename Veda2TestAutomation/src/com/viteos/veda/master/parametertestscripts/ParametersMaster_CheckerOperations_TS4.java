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
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;
import com.viteos.veda.master.lib.ParameterAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class ParametersMaster_CheckerOperations_TS4 {

	static boolean bStatus;
	static String sSheetName = "ParameterTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Parameter Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		//XMLLibrary.sFundSetupXMLFilePath = "XMLMessages//Parameters01-16-2016063104.xml";
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

			Map<String, Map<String, String>> mapAllParametersDetails = XMLLibrary.getDetailsMap(XMLLibrary.sFundSetupXMLFilePath, "//parameters/parameter[@Rule='All Fund Rules']");

			Map<String, Map<String, String>> verifyParameter = new HashMap<String, Map<String, String>>();

			for(int index = 0;index < mapAllParametersDetails.size();index++){

				Map<String, String> innerMap = new HashMap<String, String>();

				Map<String, String> mapParameterDetails = mapAllParametersDetails.get("Row"+index);

				if(!mapParameterDetails.get("Rule").equals("All Fund Rules")){
					continue;
				}

				if(!mapParameterDetails.get("OperationType").equalsIgnoreCase("Save") || mapParameterDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}

				Reporting.Testcasename = mapParameterDetails.get("TestcaseName");
				
				NewUICommonFunctions.refreshThePage();
				
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");
				bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,mapParameterDetails.get("parameterID"),mapParameterDetails.get("CheckerOperations"));
				if(bStatus && mapParameterDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapParameterDetails.get("CheckerOperations"));
					if(!mapParameterDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						continue;
					}
					NewUICommonFunctions.selectMenu("Fund Setup", "Parameter");
					NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);
					mapParameterDetails.put("Rule", "Existing Rules");
					boolean bExistStatus = ParameterAppFunctions.addNewParameter(mapParameterDetails);
					if(!bExistStatus){
						Reporting.logResults("Fail","Verify in Existing Formulas", "Formula not visible in exsiting formulas. Error: "+Messages.errorMsg);
					}				
				}

				if(!bStatus && mapParameterDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapParameterDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && mapParameterDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+mapParameterDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
				}

				if(bStatus && mapParameterDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. ParameterName: "+mapParameterDetails.get("ParameterName"));
					continue;
				}	

				if(mapParameterDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("LabelValue", mapParameterDetails.get("ParameterName"));
					innerMap.put("ParameterID",mapParameterDetails.get("parameterID"));
					verifyParameter.put(Reporting.Testcasename,innerMap);
				}

				if(mapParameterDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){				
					innerMap.put("Status", "inactive");
					innerMap.put("LabelValue", mapParameterDetails.get("ParameterName"));
					innerMap.put("ParameterID",mapParameterDetails.get("parameterID"));
					verifyParameter.put(Reporting.Testcasename,innerMap );
				}
			}

			verifyValuesinSearchPanel(verifyParameter);
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
