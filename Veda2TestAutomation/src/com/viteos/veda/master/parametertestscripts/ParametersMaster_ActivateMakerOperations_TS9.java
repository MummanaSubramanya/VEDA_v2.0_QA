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
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class ParametersMaster_ActivateMakerOperations_TS9 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Parameter Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;
		//XMLLibrary.sFundSetupXMLFilePath = "XMLMessages//Parameters01-16-2016064332.xml";
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
					continue;
				}
				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Validate") || mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Modify")|| mapModifyParameterDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}
				
				Reporting.Testcasename = mapModifyParameterDetails.get("TestcaseNameRef");
				
				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Parameter");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Parameter Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Parameter Under Fund Setup", "Parameter selected  succesfully");
				
				if(mapModifyParameterDetails.get("Operation").equalsIgnoreCase("Activate")){
					bStatus = TempFunctions.activateFeeTye("Parameter Name", createdParameterDetails.get("ParameterName"),"Parameter Id", createdParameterDetails.get("parameterID"),"Parameter");
					if(!bStatus){
						Reporting.logResults("Fail","Validate Parameter  has been activated", "Parameter has not be deactivated by Maker. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate Parameter has been activated", "Parameter Has been activated by Maker");
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
		Reporting.Testcasename = "Close app";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
