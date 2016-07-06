package com.viteos.veda.usermanagement.grouptestscripts;

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

public class GroupCreationTestScript_TC1 {
	
	static boolean bStatus;
	static String sSheetName = "Group";
	
	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="User Management Group";
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
	public static void groupCreation(){
		
		Map<String , Map<String ,String>> mapAllGroupDetails = Utilities.readMultipleTestData(Global.sGroupTestDataFilePath, sSheetName, "Y");
		
		for(int index =1 ; index<=mapAllGroupDetails.size() ; index++){
			
			Map<String ,String > mapGroupDetails = mapAllGroupDetails.get("Row"+index);
			Reporting.Testcasename = mapGroupDetails.get("TestCaseName");
			
			NewUICommonFunctions.refreshThePage();
			
			bStatus = NewUICommonFunctions.selectMenu("User Management", "Manage");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to User Mangement Manage page", "Failed to Navigate to User Mangement Page");
				continue;
			}
			Reporting.logResults("Pass", "Navigate to User Mangement Manage page", "Navigated to User Management Page");
			
 			bStatus = NewUICommonFunctions.addNewUserManagementPortal(mapGroupDetails , "Group");
			if(bStatus && mapGroupDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
				Reporting.logResults("Pass", "Create Group", "Group Created Successfully with Group Name "+mapGroupDetails.get("Group Name"));
				continue;
			}
			if(!bStatus && mapGroupDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Create Group", "Failed to Create Group ERROR: "+Messages.errorMsg);
				continue;
			}
			if(!bStatus && mapGroupDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Create Group", "Negative Test Case Cannot Perform Group Operation. ERROR: "+Messages.errorMsg);
				continue;
			}
			if(bStatus && mapGroupDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Create Group", "Group Created with Negative TestData.");
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
