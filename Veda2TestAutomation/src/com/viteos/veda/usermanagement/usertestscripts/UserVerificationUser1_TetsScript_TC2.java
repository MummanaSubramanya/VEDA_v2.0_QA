package com.viteos.veda.usermanagement.usertestscripts;

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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.UserAppFunctios;

public class UserVerificationUser1_TetsScript_TC2 {

	static boolean bStatus;
	static String sSheetName = "UserTestData";
	static String sUserName = "";
	static String sPassword = ""; 
	static Map<String ,String> mapUserDetails = new HashMap<String, String>();
	
	
	@BeforeMethod
	public static void setUp(){
		
		mapUserDetails = ExcelUtils.readDataABasedOnCellName(Global.sUserTestDataFilePath, sSheetName,"User_Role_Assign_TC1");
		sUserName = mapUserDetails.get("LoginID");
		sPassword= mapUserDetails.get("Password");
		
		
		Reporting.Functionality ="User Management User";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;


		bStatus = NewUICommonFunctions.loginToApplication(sUserName, sPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void verifyUser1(){

		    Reporting.Testcasename = mapUserDetails.get("TestCaseName");
			bStatus = UserAppFunctios.verifyFundSetupScreensAndLegalEntitiesAssignedToUser(mapUserDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verify User For Assigned Roles", "Verification Failed.ERROR: "+Messages.errorMsg);
			}else{
				Reporting.logResults("Pass", "Verify User For Assigned Roles", "Assigned Roles For User Verified Successfully");
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
