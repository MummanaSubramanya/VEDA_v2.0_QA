package com.viteos.veda.master.jointholderscripts;
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
import com.viteos.veda.master.lib.JointHolderAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class JointHolder_ModifyMakerOperations_TC3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="JointHolder Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus=NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application as Checker", "failed");
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application as Checker", "Passed");
	}
	@Test
	public static void jointHolderModify(){
		try{
			Map<String, Map<String, String>> mapAllJointHolderDetails = Utilities.readMultipleTestData(Global.sJointHolderTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllJointHolderDetails.size();index++){
				Map<String, String> mapJointHolderSearchDetails = mapAllJointHolderDetails.get("Row"+index);
				Reporting.Testcasename = mapJointHolderSearchDetails.get("TestcaseNameRef");

				//Read Test data
				Map<String, String> JointHolderDetails = ExcelUtils.readDataABasedOnCellName(Global.sJointHolderTestDataFilePath,"JointHolderTestData", mapJointHolderSearchDetails.get("TestcaseNameRef"));

				if(mapJointHolderSearchDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapJointHolderSearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				//Navigate to Joint Holder Module
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Joint Holder");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Joint Holder Master ", "Failed to Navigate to Joint Holder Master");
				}
				Reporting.logResults("Pass", "Navigate To Joint Holder Master", "Navigated to Joint Holder Master");

				// 
				String Name = "";
				Name = JointHolderDetails.get("First Name");

				if(JointHolderDetails.get("Middle Name") != null){
					Name = Name+" "+ JointHolderDetails.get("Middle Name");
				}

				if(JointHolderDetails.get("Last Name") != null){
					Name = Name+" "+ JointHolderDetails.get("Last Name");
				}

				JointHolderDetails.put("Name", Name);

				//Get operation type to be done to modify
				if(mapJointHolderSearchDetails.get("Operation").equalsIgnoreCase("Modify")){

					//do modify for the approved Joint Holder
					if(JointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){

						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", mapJointHolderSearchDetails);
						bStatus = NewUICommonFunctions.modifyMasterDetails("Joint Holder Name", JointHolderDetails.get("Name"), "JointHolder", tempmap);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Joint Holder Name", "Joint Holder Name cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Joint Holder Namet Data", "Joint Holder Namemodified with the Tesdata: "+mapJointHolderSearchDetails);
						continue;
					}

					//do modify for the Return Joint Holder
					if(JointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						//select the Dash board
						NewUICommonFunctions.selectMenu("Dashboard", "None");
						bStatus = JointHolderAppFunctions.modifyReturnJointHolder(JointHolderDetails.get("First Name"),mapJointHolderSearchDetails);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Joint Holder Name", "Joint Holder Name cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Joint Holder Namet Data", "Joint Holder Namemodified with the Tesdata: "+mapJointHolderSearchDetails);
						continue;
					}
				}

				//do validate the data when clicked n modify the data
				if(mapJointHolderSearchDetails.get("Operation").equalsIgnoreCase("Validate")){

					if(JointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){

						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", JointHolderDetails);

						//validate the screen data
						bStatus = NewUICommonFunctions.verifyMasterDetails("Joint Holder Name", JointHolderDetails.get("Name"), "JointHolder", tempmap);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
						continue;
					}

					if(JointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Return")){

						//select the Dash board
						NewUICommonFunctions.selectMenu("Dashboard", "None");

						//search in dash board table for the client
						bStatus  = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,JointHolderDetails.get("First Name"),"");
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Error: "+Messages.errorMsg);
							continue;
						}

						//verify the details
						JointHolderAppFunctions.verifyJointHolderDetailsInEditScreen(JointHolderDetails);
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
		NewUICommonFunctions .refreshThePage();
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error ");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}

