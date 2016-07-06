package com.viteos.veda.master.clienttestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.ClientAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class ClientMaster_ModifyMakerOperations_TC3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Client Master";
		Reporting.Testcasename = "Open Application";

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
			Map<String, Map<String, String>> mapAllClientDetails = Utilities.readMultipleTestData(Global.sClientTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllClientDetails.size();index++){
				Map<String, String> mapClientSearchDetails = mapAllClientDetails.get("Row"+index);
				Reporting.Testcasename = mapClientSearchDetails.get("TestcaseNameRef");

				//read testdata
				Map<String, String> clientDetails = ExcelUtils.readDataABasedOnCellName(Global.sClientTestDataFilePath,"ClientTestData", mapClientSearchDetails.get("TestcaseNameRef"));

				if(mapClientSearchDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapClientSearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}
				if (!clientDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && !clientDetails.get("CheckerOperations").equalsIgnoreCase("Return")) {
					Reporting.logResults("Fail", "Trying to Modify Checker Rejected Client record.", "TEST DATA CHECK : Checker Rejected items can't be edited by Maker please change the test data in modify Client sheet.");
					continue;
				}

				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Client");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Client Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Client Mater Under Fund Setup", "Client Menu selected succesfully");

				//get operation type to be done to modify
				if(mapClientSearchDetails.get("Operation").equalsIgnoreCase("Modify")){

					//do modify for the approved clients
					if(clientDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", mapClientSearchDetails);
						bStatus = NewUICommonFunctions.modifyMasterDetails("Client Name", clientDetails.get("ClientName"), "ClientMaster", tempmap);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Client Data", "Client cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Client Data", "Client modified with the Tesdata: "+mapClientSearchDetails);
						continue;
					}

					//do modify for the Return clients
					if(clientDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						//select the dashboard
						NewUICommonFunctions.selectMenu("Dashboard", "None");
						bStatus = ClientAppFunctions.modifyReturnClient(clientDetails.get("ClientName"),mapClientSearchDetails);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Client Data", "Client cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Client Data", "Client modified with the Tesdata: "+mapClientSearchDetails);
						continue;
					}
				}

				//do validate the data when clicked n modify the data
				if(mapClientSearchDetails.get("Operation").equalsIgnoreCase("Validate")){

					if(clientDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", clientDetails);
						
						//validate the screen data
						bStatus = NewUICommonFunctions.verifyMasterDetails("Client Name", clientDetails.get("ClientName"), "ClientMaster",tempmap);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
						continue;
					}
					
					if(clientDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						
						//select the dashboard
						NewUICommonFunctions.selectMenu("Dashboard", "None");
						
						//search in dash board table for the client
						bStatus  = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,clientDetails.get("ClientName"),"");
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Error: "+Messages.errorMsg);
							continue;
						}
						
						//verify the details
						bStatus = ClientAppFunctions.verifyClientDetailsInEditScreen(clientDetails);
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
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error ");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
