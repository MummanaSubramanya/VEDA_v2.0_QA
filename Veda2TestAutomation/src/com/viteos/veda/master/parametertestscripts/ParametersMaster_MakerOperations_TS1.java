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
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;
import com.viteos.veda.master.lib.ParameterAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class ParametersMaster_MakerOperations_TS1 {

	static boolean bStatus;
	static String sSheetName = "ParameterTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Parameter Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void testMaker(){
		try{
			String parameterId = "";
			NewUICommonFunctions.DeleteFileIfExists(XMLLibrary.sFundSetupXMLFilePath);
			Map<String, Map<String, String>> mapAllParametersDetails = Utilities.readMultipleTestData(Global.sParameterTestDataFilePath,sSheetName,"Y");
			for(int index = 1;index <= mapAllParametersDetails.size();index++){

				Map<String, String> mapParameterDetails = mapAllParametersDetails.get("Row"+index);

				if(mapParameterDetails.get("Rule").equals("All Fund Rules")){
					continue;
				}
				Reporting.Testcasename = mapParameterDetails.get("TestcaseName");
				
				NewUICommonFunctions.refreshThePage();
				
				//navigate to parameters screen
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Parameter");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Parameter Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Parameter Mater Under Fund Setup", "Parameter Menu selected succesfully");

				//click on new addition parameters
				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					Reporting.logResults("Fail", "Click on Add New button to add new Parameter", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Click on Add New button to add new Fund Parameter", "Add New Button clicked succesfully");

				//send the data to th addition data
				bStatus = ParameterAppFunctions.addNewParameter(mapParameterDetails);

				if(mapParameterDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){
					//export to xml file to save the ID
					if(mapParameterDetails.get("OperationType").equalsIgnoreCase("Save")){
						parameterId = NewUICommonFunctions.getIDFromSuccessMessage();
						mapParameterDetails.put("parameterID", parameterId);
						XMLLibrary.writeParametersToXML(mapParameterDetails);
					}
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapParameterDetails.get("OperationType"), "Performed maker Operation successfull for the scenario with test data " + mapParameterDetails);
				}

				if(mapParameterDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+mapParameterDetails.get("OperationType"), "Cannot perform maker Operations. Failed for the scenario with test data "     + mapParameterDetails +  " ERROR : "   + Messages.errorMsg);
					continue;
				}

				if(mapParameterDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapParameterDetails.get("OperationType"), "Negative Testcase - Cannot perform maker operations for the scenario with test data " + mapParameterDetails + " ERROR : "   + Messages.errorMsg);
				}

				if(mapParameterDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+mapParameterDetails.get("OperationType"), "performed maker operations for the scenario with Negative test data " + mapParameterDetails);
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
