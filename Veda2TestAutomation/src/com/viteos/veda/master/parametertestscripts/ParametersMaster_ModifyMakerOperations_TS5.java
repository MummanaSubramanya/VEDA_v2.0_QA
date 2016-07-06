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
import com.viteos.veda.master.lib.ParameterAppFunctions;
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class ParametersMaster_ModifyMakerOperations_TS5 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Parameter Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;
		//XMLLibrary.sFundSetupXMLFilePath = "XMLMessages//Parameters01-16-2016082845.xml";
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
			Map<String, Map<String, String>> mapAllParameterDetails = Utilities.readMultipleTestData(Global.sParameterTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllParameterDetails.size();index++){
				Map<String, String> mapModifyParameterDetails = mapAllParameterDetails.get("Row"+index);
				
				//read testdata
				Map<String, String> createdParameterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFundSetupXMLFilePath,"parameter", mapModifyParameterDetails.get("TestcaseNameRef")) ;
				if(createdParameterDetails == null){
					continue ;
				}
				NewUICommonFunctions.refreshThePage();
				
				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Deactivate")||mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				Reporting.Testcasename = mapModifyParameterDetails.get("TestcaseNameRef");
				
				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Parameter");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Parameter Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Parameter Under Fund Setup", "Parameter selected succesfully");

				//get operation type to be done to modify
				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Modify")){

					//do modify for the approved formula
					
					if(createdParameterDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						bStatus = TempFunctions.modifyFeeTypeDetailsScreen("Parameter Name", createdParameterDetails.get("ParameterName"), "Parameter Id", createdParameterDetails.get("parameterID"), "Parameter", mapModifyParameterDetails);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Parameter Details", "Parameter cannot be modified. Error: " +  Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Parameter Details", "Parameter modified with the Tesdata: "+mapModifyParameterDetails);
						continue;
					}

					//do modify for the Return formula
					if(createdParameterDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						//select the dashboard
						NewUICommonFunctions.selectMenu("Dashboard", "None");
						bStatus = ParameterAppFunctions.modifyReturnParametersDetails(createdParameterDetails.get("parameterID"),mapModifyParameterDetails);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Parameter Details", "Parameter Details cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Parameter Details", "Parameter Details modified with the Tesdata: "+mapModifyParameterDetails);
						continue;
					}
				}

				//do validate the data when clicked n modify the data
				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Validate")){
					if(createdParameterDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						bStatus = TempFunctions.verifyFeeTypeDetailsScreen("Parameter Name", createdParameterDetails.get("ParameterName"),"Parameter Id", createdParameterDetails.get("parameterID"), "Parameter", createdParameterDetails);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
						continue;
					}

					if(createdParameterDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						bStatus = ParameterAppFunctions.verifyReturnParameterDetailsInEditScreen(createdParameterDetails);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
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

		Reporting.Testcasename = "Close Application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
