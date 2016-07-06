package com.viteos.veda.master.clienttestscripts;

import java.util.Map;

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

public class ClientMaster_DeactivateMakerOperations_TC5 {
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
				
				if(mapClientSearchDetails.get("Operation").equalsIgnoreCase("Validate") || mapClientSearchDetails.get("Operation").equalsIgnoreCase("Modify") || mapClientSearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}
				
				Reporting.Testcasename = mapClientSearchDetails.get("TestcaseNameRef");
				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Client");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Client Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Client Mater Under Fund Setup", "Client Menu selected succesfully");	

				if(mapClientSearchDetails.get("Operation").equalsIgnoreCase("DeActivate")){

					//read testdata
					Map<String, String> createdClientDetails = ExcelUtils.readDataABasedOnCellName(Global.sClientTestDataFilePath,"ClientTestData", mapClientSearchDetails.get("TestcaseNameRef"));
					boolean bNameModifyStatus = false;
					Map<String, String> modifiedClientDetails = ExcelUtils.readDataABasedOnCellName(Global.sClientTestDataFilePath,"ModifyTestdata", mapClientSearchDetails.get("TestcaseNameRef"));
					if(modifiedClientDetails!=null && modifiedClientDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedClientDetails.containsKey("ClientName")){
							bNameModifyStatus = true;
							mapClientSearchDetails.put("ClientName", modifiedClientDetails.get("ClientName"));
						}
					}
					if(!bNameModifyStatus){
						mapClientSearchDetails.put("ClientName", createdClientDetails.get("ClientName"));
					}

					bStatus = NewUICommonFunctions.deactivateMaster("Client Name",mapClientSearchDetails.get("ClientName"),"ClientMaster");
					if(!bStatus){
						Reporting.logResults("Fail","Validate Client has been deactivated "+ mapClientSearchDetails.get("ClientName"), "Client has not be deactivated. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate Client has been deactivated "+mapClientSearchDetails.get("ClientName"), "Client Has been deactivated");
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
