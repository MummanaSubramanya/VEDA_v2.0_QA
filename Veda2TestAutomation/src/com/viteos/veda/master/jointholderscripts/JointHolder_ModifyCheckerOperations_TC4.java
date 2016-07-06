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

public class JointHolder_ModifyCheckerOperations_TC4 {
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
	public static void jointHolderCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllJointHolderDetails = Utilities.readMultipleTestData(Global.sJointHolderTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllJointHolderDetails.size();index++){

				Map<String, String> mapJointHolderDetails = mapAllJointHolderDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();

				if(mapJointHolderDetails.get("Operation").equalsIgnoreCase("Validate")|| mapJointHolderDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapJointHolderDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				if(mapJointHolderDetails.get("ExpectedResults")!=null && mapJointHolderDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}

				if(mapJointHolderDetails.get("OperationType")!=null && mapJointHolderDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					continue;
				}

				Reporting.Testcasename = mapJointHolderDetails.get("TestcaseNameRef");

				//Navigate to dashboard 
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");
				
				Map<String, String> JointHolderDetails = ExcelUtils.readDataABasedOnCellName(Global.sJointHolderTestDataFilePath,"JointHolderTestData", mapJointHolderDetails.get("TestcaseNameRef"));
			
				
				JointHolderDetails.putAll(mapJointHolderDetails);
				String Name = "";
				Name = JointHolderDetails.get("First Name");

				if(JointHolderDetails.get("Middle Name") != null){
					Name = Name+" "+ JointHolderDetails.get("Middle Name");
				}

				if(JointHolderDetails.get("Last Name") != null){
					Name = Name+" "+ JointHolderDetails.get("Last Name");
				}

				JointHolderDetails.put("Name", Name);

				// Verify Modify Details After Modification
				
				if(mapJointHolderDetails.get("Operation").equalsIgnoreCase("Modify")){
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, JointHolderDetails.get("First Name"),JointHolderDetails.get("CheckerOperations"));
					if(bStatus && JointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						//Navigate to Joint Holder
						bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Joint Holder");
						
						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", JointHolderDetails);
						
						boolean bTestStatus = NewUICommonFunctions.verifyMasterDetails("Joint Holder Name", JointHolderDetails.get("Name"), "JointHolder", tempmap);
						if(!bTestStatus){
							Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved.Error: "+Messages.errorMsg);
						}
					}

				}

				if(bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Modify  Checker Operation", "Successfully Performed  Modify checker operations: "+mapJointHolderDetails.get("CheckerOperations"));
				}

				if(!bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Modify  Checker Operation", "Cannot Modify Perform Checker Operations: "+mapJointHolderDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Modify Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+mapJointHolderDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
				}

				if(bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Modify Checker Operation", "Peformed Modify Checker operations with negative testdata.  "+JointHolderDetails.get("First Name"));
					continue;
				}	
				
				// Approve the Modify joint holder
				if(JointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "active");
					innerMap.put("Joint Holder Name", JointHolderDetails.get("Name"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
				}
					//Reject the Modify JointHolder Details
					if(JointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){
					innerMap.put("Status", "inactive");
					innerMap.put("Joint Holder Name", JointHolderDetails.get("Name"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
				}
		}
		verifyValuesinSearchPanel(VerifyMap);
	}
	catch(Exception e){
		e.printStackTrace();
	}
}

@AfterMethod
public static void tearDown(){

	Reporting.Testcasename = "Close Application";
	NewUICommonFunctions.refreshThePage();
	bStatus = NewUICommonFunctions.logoutFromApplication();
	if(!bStatus){
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


