package com.viteos.veda.usermanagement.usertestscripts;

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

public class CreateUser_TestScript_TS1 {

	static boolean bStatus;
	static String sSheetName = "UserTestData";
	
	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="User Management User";
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
	public static void addUser(){
		Map<String ,Map<String ,String>> mapUserDetails = Utilities.readMultipleTestData(Global.sUserTestDataFilePath, sSheetName, "Y");
		
		for(int index=1 ;index<=mapUserDetails.size();index++){
			
			Map<String ,String> mapCreateuserDetails = mapUserDetails.get("Row"+index);
			
			Reporting.Testcasename = mapCreateuserDetails.get("TestCaseName");
			
			bStatus = NewUICommonFunctions.selectMenu("User Management", "Manage");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate To User Management", "Failed To Navigate to User Management");
			}
			Reporting.logResults("Pass", "Navigate to User Mangement Manage page", "Navigated to User Management Page");
			
			bStatus = NewUICommonFunctions.addNewUserManagementPortal(mapCreateuserDetails,"User");
			
			//bStatus = UserAppFunctios.editCreatedUser(mapCreateuserDetails);
			
			if(bStatus && mapCreateuserDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
				if(mapCreateuserDetails.get("OperationType").equalsIgnoreCase("Save")){
					Reporting.logResults("Pass", "Modify User", "User Modified Successfully with User Name "+mapCreateuserDetails.get("LoginID"));
					continue;
				}
				if(mapCreateuserDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					Reporting.logResults("Pass", "Perform Cancel Operation", "Cancel Button Clicked Successfully ");
					continue;
				}
				
			}
			if(!bStatus && mapCreateuserDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Modify User", "Failed to Modify User ERROR: "+Messages.errorMsg);
				continue;
			}
			if(!bStatus && mapCreateuserDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Modify User", "Negative Test Case Cannot Perform User Operation. ERROR: "+Messages.errorMsg);
				continue;
			}
			if(bStatus && mapCreateuserDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Modify User", "User Modified with Negative Test Data ");
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
