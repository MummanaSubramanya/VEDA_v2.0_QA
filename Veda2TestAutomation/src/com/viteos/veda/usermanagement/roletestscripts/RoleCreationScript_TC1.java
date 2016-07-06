package com.viteos.veda.usermanagement.roletestscripts;

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

public class RoleCreationScript_TC1 {
	
	static boolean bStatus;
	static String sSheetName = "Role";
	
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
	public static void addNewRole(){
		Map<String , Map<String ,String>> mapAllRoleDetails = Utilities.readMultipleTestData(Global.sRoleTestDataFilePath, sSheetName, "Y");
		
		for(int index =1 ; index<=mapAllRoleDetails.size() ; index++){
			
			Map<String ,String > mapRoleDetails = mapAllRoleDetails.get("Row"+index);
			Reporting.Testcasename = mapRoleDetails.get("TestCaseName");
			NewUICommonFunctions.refreshThePage();
			
			bStatus = NewUICommonFunctions.selectMenu("User Management", "Manage");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to User Mangement Manage page", "Failed to Navigate to User Mangement Page");
				continue;
			}
			Reporting.logResults("Pass", "Navigate to User Mangement Manage page", "Navigated to User Management Page");
			
			bStatus = NewUICommonFunctions.addNewUserManagementPortal(mapRoleDetails , "Role");
			
			if(bStatus && mapRoleDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
				if(mapRoleDetails.get("OperationType").equalsIgnoreCase("Save")){
					Reporting.logResults("Pass", "Create Role", "Role Created Successfully with RoleCode "+mapRoleDetails.get("RoleCode"));
					continue;
				}
				if(mapRoleDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					Reporting.logResults("Pass", "Perform Cancel Operation", "Cancel Button Clicked Successfully ");
					continue;
				}
				
			}
			if(!bStatus && mapRoleDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Create Role", "Failed to Create Role ERROR: "+Messages.errorMsg);
				continue;
			}
			if(!bStatus && mapRoleDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Create Role", "Negative Test Case Cannot Perform Role Operation. ERROR: "+Messages.errorMsg);
				continue;
			}
			if(bStatus && mapRoleDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Create Role", "Role Created with Negative Test Data ");
				continue;
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
