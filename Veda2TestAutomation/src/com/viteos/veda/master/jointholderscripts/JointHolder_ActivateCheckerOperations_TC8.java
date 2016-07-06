package com.viteos.veda.master.jointholderscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.TempFunctions;

public class JointHolder_ActivateCheckerOperations_TC8 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestData";
	
	@BeforeMethod
	public static void setup(){

		Reporting.Functionality ="JointHolder Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application as Checker", "failed");
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application as Checker", "Passed");
	}

	@Test
	public static void activateCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllJointHolderDetails = Utilities.readMultipleTestData(Global.sJointHolderTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> verifyJointHolder = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllJointHolderDetails.size();index++){

				Map<String, String> mapJointHolderDetails = mapAllJointHolderDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();

				if(mapJointHolderDetails.get("Operation").equalsIgnoreCase("Validate") || mapJointHolderDetails.get("Operation").equalsIgnoreCase("Modify") || mapJointHolderDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}

				Reporting.Testcasename = mapJointHolderDetails.get("TestcaseNameRef");

				//Navigate to Dash board 
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard for activateCheckerOperations ","Cannot Navigate to DashBoard activateCheckerOperations");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard activateCheckerOperations ", "Navigated to DashBoard activateCheckerOperations");

				if(mapJointHolderDetails.get("Operation").equalsIgnoreCase("Activate")){

					//Read the Test Data
					Map<String, String> createdJointHolderDetails = ExcelUtils.readDataABasedOnCellName(Global.sJointHolderTestDataFilePath,"JointHolderTestData", mapJointHolderDetails.get("TestcaseNameRef"));
					boolean bNameModifyStatus = false;
					String sFirstName = "";
					Map<String, String> modifiedJointHolderDetails = ExcelUtils.readDataABasedOnCellName(Global.sJointHolderTestDataFilePath,"ModifyTestData", mapJointHolderDetails.get("TestcaseNameRef"));
					if(modifiedJointHolderDetails!=null && modifiedJointHolderDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedJointHolderDetails.containsKey("First Name") || modifiedJointHolderDetails.containsKey("Middle Name") || modifiedJointHolderDetails.containsKey("Last Name")){
							bNameModifyStatus = true;
							String Name = "";
							if( modifiedJointHolderDetails.get("First Name")!= null){
								Name = modifiedJointHolderDetails.get("First Name");
								sFirstName = Name;
							}
							if(modifiedJointHolderDetails.get("First Name")== null){
								Name = createdJointHolderDetails.get("First Name");
								sFirstName = Name;
							}
							if(modifiedJointHolderDetails.get("Middle Name") != null){
								Name = Name+" "+ modifiedJointHolderDetails.get("Middle Name");
							}
							if(modifiedJointHolderDetails.get("Middle Name")== null && createdJointHolderDetails.get("Middle Name")!= null) {
								Name = Name+" "+createdJointHolderDetails.get("Middle Name");
							}
							if(modifiedJointHolderDetails.get("Last Name") != null){
								Name = Name+" "+ modifiedJointHolderDetails.get("Last Name");
							}
							if(modifiedJointHolderDetails.get("Last Name")== null && createdJointHolderDetails.get("Last Name")!= null){
								Name = Name+" "+createdJointHolderDetails.get("Last Name");
							}
							mapJointHolderDetails.put("Name", Name);
						}
					}
					if(!bNameModifyStatus){
						String Name = "";
						Name = createdJointHolderDetails.get("First Name").trim();
						sFirstName = Name;

						if(createdJointHolderDetails.get("Middle Name") != null){
							Name = Name+" "+ createdJointHolderDetails.get("Middle Name").trim();
						}

						if(createdJointHolderDetails.get("Last Name") != null){
							Name = Name+" "+ createdJointHolderDetails.get("Last Name").trim();
						}

						mapJointHolderDetails.put("Name", Name);
					}

					// Perform Checker operation
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,sFirstName,mapJointHolderDetails.get("CheckerOperations"));
					if(bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation for Activate Joint Holder", "Successfully Performed checker operations for Activate Joint Holder : "+mapJointHolderDetails.get("CheckerOperations"));
					}

					if(!bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){

						Reporting.logResults("Fail","Perform Checker Operation for Activate Joint Holder", "Cannot Perform Checker Operations for Activate Joint Holder  "+mapJointHolderDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(!bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation for Activate Joint Holder", "Negative testcase - Cannot Perform Checker Operations for Activate Joint Holder: "+mapJointHolderDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation for Activate Joint Holder", "Peformed Checker operations with negative testdata. Investor Name: "+mapJointHolderDetails.get("Investor Name"));
						continue;
					}	
					if(mapJointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						innerMap.put("Status", "active");
						innerMap.put("Joint Holder Name", mapJointHolderDetails.get("Name"));
						verifyJointHolder.put(Reporting.Testcasename,innerMap );
					}
				}
			}

			verifyValuesinSearchPanel(verifyJointHolder);
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
			NewUICommonFunctions.refreshThePage();
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error ");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}

	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyJointHolder) {
		try{

			for (Entry<String, Map<String, String>> test : verifyJointHolder.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Client Master
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Joint Holder");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Joint Holder", "Navigated to Joint Holder");

		
				// Search the Joint Holder in drop down
				bStatus  = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Joint Holder Name", test.getValue().get("Joint Holder Name"), test.getValue().get("Status"), "Investor Joint Holder Name", test.getValue().get("Joint Holder Name"), "JointHolder", 5);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Joint Holdershouldnot be in active state",test.getKey()+" Joint Holder Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Joint Holder shouldnot be in active state",test.getKey()+" Joint Holder is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Joint Holdershould be in active state",test.getKey()+" Joint Holder is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Joint Holder should be in active state",test.getKey()+" Joint Holder is in active state");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}


