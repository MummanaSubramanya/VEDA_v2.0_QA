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

public class Group_Activate_TestScript_TC4 {

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
	public static void activate(){

		Map<String ,Map<String ,String>> mapModifyDetails = Utilities.readMultipleTestData(Global.sGroupTestDataFilePath, sSheetName, "Y");

		for(int index=1 ; index<=mapModifyDetails.size() ; index++){
			Map<String ,String> mapSearchGroupDetails = mapModifyDetails.get("Row"+index);
			if(mapSearchGroupDetails.get("Operation").equalsIgnoreCase("Validate") || mapSearchGroupDetails.get("Operation").equalsIgnoreCase("Modify") || mapSearchGroupDetails.get("Operation").equalsIgnoreCase("DeActivate")){
				continue;
			}

			Reporting.Testcasename = mapSearchGroupDetails.get("TestCaseName");
			
			bStatus = NewUICommonFunctions.selectMenu("User Management", "Manage");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to User Mangement Manage page", "Failed to Navigate to User Mangement Page");
				continue;
			}
			Reporting.logResults("Pass", "Navigate to User Mangement Manage page", "Navigated to User Management Page");

			if(mapSearchGroupDetails.get("Operation").equalsIgnoreCase("Activate")){
				Map<String ,String> mapCreatedGroupDetails = ExcelUtils.readDataABasedOnCellName(Global.sGroupTestDataFilePath, "Group", mapSearchGroupDetails.get("TestCaseName"));

				Map<String ,String> mapModifiedGroupDetails = ExcelUtils.readDataABasedOnCellName(Global.sGroupTestDataFilePath, sSheetName, mapSearchGroupDetails.get("TestCaseName"));
				//mapSearchGroupDetails.putAll(arg0);

				boolean bSearchName = false;
				if(mapModifiedGroupDetails.get("Group Name")!=null  && mapModifiedGroupDetails.get("Operation").equalsIgnoreCase("Modify")){
					bSearchName = true;
					mapSearchGroupDetails.put("Group Name", mapModifiedGroupDetails.get("Group Name"));
				}
				if(!bSearchName && mapCreatedGroupDetails.get("Group Name")!=null){

					mapSearchGroupDetails.put("Group Name", mapCreatedGroupDetails.get("Group Name"));
				}
				
				bStatus = NewUICommonFunctions.activatePortal(mapSearchGroupDetails.get("Group Name"), "Group");
				
				if(bStatus && mapSearchGroupDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass", "Activate Created Group", "Group Activated Successfully for Group Name "+mapSearchGroupDetails.get("Group Name"));
					continue;
				}
				if(!bStatus && mapSearchGroupDetails.get("ExpectedResult").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail", "Activate Created Group", "Failed to Activate Group Name "+mapSearchGroupDetails.get("Group Name") +" ERROR: "+Messages.errorMsg);
					continue;
				}
				if(!bStatus && mapSearchGroupDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass", "Activate Created Group", "Negative Test Case Cannot Perform Group Operation. ERROR: "+Messages.errorMsg);
					continue;
				}
				if(bStatus && mapSearchGroupDetails.get("ExpectedResult").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail", "Activate Created Group", "Group Activated for Negative TestCase.");
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
