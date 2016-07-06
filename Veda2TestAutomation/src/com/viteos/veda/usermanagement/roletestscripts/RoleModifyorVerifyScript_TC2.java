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

public class RoleModifyorVerifyScript_TC2 {
	
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
	public static void modifyCreatedRole(){
		String portalName = "Role";
		Map<String ,Map<String , String>> mapModifySheetRoleDetails = Utilities.readMultipleTestData(Global.sRoleTestDataFilePath, sSheetName, "Y");
		
		for(int index=1 ; index<=mapModifySheetRoleDetails.size() ; index++){
			Map<String ,String> mapModifyRoleDetails = mapModifySheetRoleDetails.get("Row"+index);
			if(mapModifyRoleDetails.get("Operation").equalsIgnoreCase("Activate") || mapModifyRoleDetails.get("Operation").equalsIgnoreCase("DeActivate")){
				continue;
			}
			
			Map<String , String> mapCreatedRoleDetails = ExcelUtils.readDataABasedOnCellName(Global.sRoleTestDataFilePath, "Role", mapModifyRoleDetails.get("TestCaseName"));
			
			if(mapCreatedRoleDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapCreatedRoleDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
				continue;
			}
			
			Reporting.Testcasename = mapModifyRoleDetails.get("TestCaseName");

			bStatus = NewUICommonFunctions.selectMenu("User Management", "Manage");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to User Mangement Manage page", "Failed to Navigate to User Mangement Page");
				continue;
			}
			Reporting.logResults("Pass", "Navigate to User Mangement Manage page", "Navigated to User Management Page");
			
			
			//Modify the Role if Operation is Modify
			if(mapModifyRoleDetails.get("Operation").equalsIgnoreCase("Modify")){
				
				String searchValue = mapCreatedRoleDetails.get("RoleCode");
				
				//put all ModifyDetails in Created Role Details Sheet
				mapCreatedRoleDetails.putAll(mapModifyRoleDetails);
				
				//Modify the Created Role with given modifyDetails 
				bStatus = NewUICommonFunctions.modifyUserManagementPortal(mapModifyRoleDetails, searchValue ,portalName);
				
				if(bStatus && mapModifyRoleDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass", "Modify Created Role Details", "Role Modified Successfully with RoleCode "+mapCreatedRoleDetails.get("RoleCode"));
				}
				if(!bStatus && mapModifyRoleDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail", "Modify Created Role Details", "Failed to Modifiy Role Details for RoleCode "+mapCreatedRoleDetails.get("RoleCode") +" ERROR: "+Messages.errorMsg);
					continue;
				}
				if(!bStatus && mapModifyRoleDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass", "Modify Created Role Details", "Negative Test Case Cannot Perform Role Operation. ERROR: "+Messages.errorMsg);
					continue;
				}
				if(bStatus && mapModifyRoleDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail", "Modify Created Role Details", "Role Modified with Negative TestData.");
					continue;
				}
				
			
				
				//verify the Modified Details
				bStatus = NewUICommonFunctions.verifyCreatedUserManagementPortal(mapCreatedRoleDetails , mapCreatedRoleDetails.get("RoleCode") , portalName);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify the Modified Role Details", "Verification Failed .ERROR: "+Messages.errorMsg);
					continue;
				}
				
				Reporting.logResults("Pass", "Verify the Modified Role Details", "Verified Successfully. Details are Matching with the Actual");
				
			}
			
			//Verify the Created Role if Operation is Validate
			if(mapModifyRoleDetails.get("Operation").equalsIgnoreCase("Validate")){
				
				//verify the Created Role Details
				bStatus = NewUICommonFunctions.verifyCreatedUserManagementPortal(mapCreatedRoleDetails,mapCreatedRoleDetails.get("RoleCode"), portalName);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify the Created Role Details", "Verification Failed .ERROR: "+Messages.errorMsg);
					continue;
				}
				
				Reporting.logResults("Pass", "Verify the Created Role Details", "Verified Successfully. Details are Matching with the Actual");
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
