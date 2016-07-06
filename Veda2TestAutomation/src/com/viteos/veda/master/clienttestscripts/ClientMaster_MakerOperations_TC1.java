package com.viteos.veda.master.clienttestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.ClientAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class ClientMaster_MakerOperations_TC1 {

	static boolean bStatus;
	static String sSheetName = "ClientTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Client Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;


		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@org.testng.annotations.Test
	public static void test(){

		Map<String, Map<String, String>> mapAllClientDetails = Utilities.readMultipleTestData(Global.sClientTestDataFilePath,sSheetName,"Y");
		
		for(int index = 1;index <= mapAllClientDetails.size();index++){

			Map<String, String> mapClientDetails = mapAllClientDetails.get("Row"+index);
			
			Reporting.Testcasename = mapClientDetails.get("TestcaseName");
			
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Client");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Client Master", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Client Master", "Client Menu selected succesfully");
			
			//Click on Add Button
			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on 'Add New' button for client creation", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Click on 'Add New' button for client creation", "Add New Button clicked succesfully");

			// Add New Client Details
			bStatus = ClientAppFunctions.addNewClient(mapClientDetails);
			if(bStatus && mapClientDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				
				if(mapClientDetails.get("OperationType").equalsIgnoreCase("Save")){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapClientDetails.get("OperationType"), "Performed maker Operation Successfull Client Created Client Name : " + mapClientDetails.get("ClientName"));
				}
				if(mapClientDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapClientDetails.get("OperationType"), "Performed maker Operation "+mapClientDetails.get("OperationType")+" Successfully");
				}
			}

			if(!bStatus && mapClientDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail","Perform Maker Operation: "+mapClientDetails.get("OperationType"), "Cannot perform Maker Operation. Error: "+Messages.errorMsg+" for client: "+mapClientDetails.get("ClientName"));
				continue;
			}

			if(!bStatus && mapClientDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass","Perform Maker Operation: "+mapClientDetails.get("OperationType"), "Negative testcase-Cannot perform Maker Action for Client: "+mapClientDetails.get("ClientName"));
				continue;
			}

			if(bStatus && mapClientDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail","Perform Maker Operation: "+mapClientDetails.get("OperationType"), "Performed Maker Action with negative testdata. Client Name: "+mapClientDetails.get("ClientName"));
				continue;
			}	
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
}
