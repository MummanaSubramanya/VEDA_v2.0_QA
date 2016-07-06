package com.viteos.veda.master.parametertestscripts;

import java.util.Map;

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
import com.viteos.veda.master.lib.XMLLibrary;

public class ParameterMaster_DeActivateCheckerOperations_TC8 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Parameter Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		//XMLLibrary.sFundSetupXMLFilePath = "XMLMessages//Parameters01-16-2016081001.xml";
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

			for(int index = 1;index <= mapAllParameterDetails.size();index++){

				Map<String, String> mapModifyParameterDetails = mapAllParameterDetails.get("Row"+index);
				Map<String, String> createdParameterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFundSetupXMLFilePath, "parameter",mapModifyParameterDetails.get("TestcaseNameRef")) ;

				if(createdParameterDetails == null){
					continue;
				}
				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Validate") || mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Modify")||mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Activate")){
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
				
				
				//mapModifyParameterDetails.put("parameterID", createdParameterDetails.get("parameterID"));
				createdParameterDetails.putAll(mapModifyParameterDetails);

				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,createdParameterDetails.get("parameterID"), createdParameterDetails.get("CheckerOperations"));

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
						Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Formula Name: "+createdParameterDetails.get("FeeRuleName"));
						continue;
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
		Reporting.Testcasename = "Close app";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
