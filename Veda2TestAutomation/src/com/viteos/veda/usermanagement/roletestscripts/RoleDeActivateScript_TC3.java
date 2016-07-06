package com.viteos.veda.usermanagement.roletestscripts;

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

public class RoleDeActivateScript_TC3 {

	static boolean bStatus;
	static String sSheetName = "ModifyRole";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="User Management Role";
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
	public static void deActivate(){

		Map<String ,Map<String ,String>> mapModifyDetails = Utilities.readMultipleTestData(Global.sRoleTestDataFilePath, sSheetName, "Y");

		for(int index=1 ; index<=mapModifyDetails.size() ; index++){
			Map<String ,String> mapSearchRoleDetails = mapModifyDetails.get("Row"+index);
			if(mapSearchRoleDetails.get("Operation").equalsIgnoreCase("Validate") || mapSearchRoleDetails.get("Operation").equalsIgnoreCase("Modify") || mapSearchRoleDetails.get("Operation").equalsIgnoreCase("Activate")){
				continue;
			}

			Reporting.Testcasename = mapSearchRoleDetails.get("TestCaseName");

			bStatus = NewUICommonFunctions.selectMenu("User Management", "Manage");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to User Mangement Manage page", "Failed to Navigate to User Mangement Page");
				continue;
			}
			Reporting.logResults("Pass", "Navigate to User Mangement Manage page", "Navigated to User Management Page");

			if(mapSearchRoleDetails.get("Operation").equalsIgnoreCase("DeActivate")){
				Map<String ,String> mapCreatedRoleDetails = ExcelUtils.readDataABasedOnCellName(Global.sRoleTestDataFilePath, "Role", mapSearchRoleDetails.get("TestCaseName"));

				Map<String ,String> mapModifiedRoleDetails = ExcelUtils.readDataABasedOnCellName(Global.sRoleTestDataFilePath, sSheetName, mapSearchRoleDetails.get("TestCaseName"));
				//mapSearchRoleDetails.putAll(arg0);

				boolean bSearchName = false;
				if(mapModifiedRoleDetails.get("RoleCode")!=null  && mapModifiedRoleDetails.get("Operation").equalsIgnoreCase("Modify")){
					bSearchName = true;
					mapSearchRoleDetails.put("RoleCode", mapModifiedRoleDetails.get("RoleCode"));
				}
				if(!bSearchName && mapCreatedRoleDetails.get("RoleCode")!=null){

					mapSearchRoleDetails.put("RoleCode", mapCreatedRoleDetails.get("RoleCode"));
				}
				
				bStatus = NewUICommonFunctions.deactivatePortal(mapSearchRoleDetails.get("RoleCode"), "Role");
				
				if(bStatus && mapSearchRoleDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass", "DeActivate Created Role", "Role DeActivated Successfully for Role Name "+mapSearchRoleDetails.get("RoleCode"));
					continue;
				}
				if(!bStatus && mapSearchRoleDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail", "DeActivate Created Role", "Failed to DeActivate Role Name "+mapSearchRoleDetails.get("RoleCode") +" ERROR: "+Messages.errorMsg);
					continue;
				}
				if(!bStatus && mapSearchRoleDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass", "DeActivate Created Role", "Negative Test Case Cannot Perform Role Operation. ERROR: "+Messages.errorMsg);
					continue;
				}
				if(bStatus && mapSearchRoleDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail", "DeActivate Created Role", "Role DeActivated for Negative TestCase.");
					continue;
				}
				
			}
		}

	}
	
	@AfterMethod
	public static void tearDown(){
		NewUICommonFunctions.refreshThePage();
		Reporting.Testcasename = "Close Application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}

}
