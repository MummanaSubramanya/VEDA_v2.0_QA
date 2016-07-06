package com.viteos.veda.usermanagement.grouptestscripts;

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

public class Group_ModifyOrVerifyTestScript_TC2 {

	static boolean bStatus;
	static String sSheetName = "ModifyGroup";
	
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
	public static void modifyCreatedGroup(){
		String portalName = "Group";
		Map<String ,Map<String , String>> mapModifySheetGroupDetails = Utilities.readMultipleTestData(Global.sGroupTestDataFilePath, sSheetName, "Y");
		
		for(int index=1 ; index<=mapModifySheetGroupDetails.size() ; index++){
			Map<String ,String> mapModifyGroupDetails = mapModifySheetGroupDetails.get("Row"+index);
			if(mapModifyGroupDetails.get("Operation").equalsIgnoreCase("Activate") || mapModifyGroupDetails.get("Operation").equalsIgnoreCase("DeActivate")){
				continue;
			}
			
			Map<String , String> mapCreatedGroupDetails = ExcelUtils.readDataABasedOnCellName(Global.sGroupTestDataFilePath, "Group", mapModifyGroupDetails.get("TestCaseName"));
			
			if(mapCreatedGroupDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapCreatedGroupDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
				continue;
			}
			
			Reporting.Testcasename = mapModifyGroupDetails.get("TestCaseName");
			
			NewUICommonFunctions.refreshThePage();
			bStatus = NewUICommonFunctions.selectMenu("User Management", "Manage");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to User Mangement Manage page", "Failed to Navigate to User Mangement Page");
				continue;
			}
			Reporting.logResults("Pass", "Navigate to User Mangement Manage page", "Navigated to User Management Page");
			
			
			//Modify the Group if Operation is Modify
			if(mapModifyGroupDetails.get("Operation").equalsIgnoreCase("Modify")){
				
				String searchValue = mapCreatedGroupDetails.get("Group Name");
				
				//put all ModifyDetails in Created Group Details Sheet
				mapCreatedGroupDetails.putAll(mapModifyGroupDetails);
				
				//Modify the Created Group with given modifyDetails 
				bStatus = NewUICommonFunctions.modifyUserManagementPortal(mapModifyGroupDetails, searchValue ,portalName);
				
				if(bStatus && mapModifyGroupDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass", "Modify Created Group Details", "Group Modified Successfully with Group Name "+mapCreatedGroupDetails.get("Group Name"));
	
				}
				if(!bStatus && mapModifyGroupDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail", "Modify Created Group Details", "Failed to Modifiy Group Details for Group Name "+mapCreatedGroupDetails.get("Group Name") +" ERROR: "+Messages.errorMsg);
					continue;
				}
				if(!bStatus && mapModifyGroupDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass", "Modify Created Group Details", "Negative Test Case Cannot Perform Group Operation. ERROR: "+Messages.errorMsg);
					continue;
				}
				if(bStatus && mapModifyGroupDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail", "Modify Created Group Details", "Group Modified with Negative TestData.");
					continue;
				}
				
								
				//verify the Modified Details
				bStatus = NewUICommonFunctions.verifyCreatedUserManagementPortal(mapCreatedGroupDetails , mapCreatedGroupDetails.get("Group Name") , portalName);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify the Modified Group Details", "Verification Failed .ERROR: "+Messages.errorMsg);
					continue;
				}
				
				Reporting.logResults("Pass", "Verify the Modified Group Details", "Verified Successfully. Details are Matching with the Actual");
				
			}
			
			//Verify the Created Group if Operation is Validate
			if(mapModifyGroupDetails.get("Operation").equalsIgnoreCase("Validate")){
				
				//verify the Created Group Details
				bStatus = NewUICommonFunctions.verifyCreatedUserManagementPortal(mapCreatedGroupDetails,mapCreatedGroupDetails.get("Group Name"), portalName);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify the Created Group Details", "Verification Failed .ERROR: "+Messages.errorMsg);
					continue;
				}
				
				Reporting.logResults("Pass", "Verify the Created Group Details", "Verified Successfully. Details are Matching with the Actual");
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
