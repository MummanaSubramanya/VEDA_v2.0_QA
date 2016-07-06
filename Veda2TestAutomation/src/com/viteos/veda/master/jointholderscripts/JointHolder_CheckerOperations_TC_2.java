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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.TempFunctions;

public class JointHolder_CheckerOperations_TC_2 {
	static boolean bStatus;
	static String sRefrenceJointHolderSheetName = "JointHolderTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="JointHolder Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void jointHolderCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllJointHolderDetails = Utilities.readMultipleTestData(Global.sJointHolderTestDataFilePath, sRefrenceJointHolderSheetName, "Y");
			Map<String, Map<String, String>> verifyJointHolder = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllJointHolderDetails.size();index++){

				Map<String, String> innerMap = new HashMap<String, String>();

				Map<String, String> mapJointHolderDetails = mapAllJointHolderDetails.get("Row"+index);

				if(!mapJointHolderDetails.get("OperationType").equalsIgnoreCase("Save") || mapJointHolderDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}

				Reporting.Testcasename = mapJointHolderDetails.get("TestcaseName");

				//Navigate to Dashboard
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard Joint Holder Checker operation","Cannot Navigate to DashBoard Joint Holder Checker operation");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard Joint Holder Checker operation", "Navigated to DashBoard Joint Holder Checker operation");

				// Perform checker Operation
				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,mapJointHolderDetails.get("First Name"),mapJointHolderDetails.get("CheckerOperations"));
				if(bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation Joint Holder Checker operation", "Successfully Performed checker operations Joint Holder Checker operation : "+mapJointHolderDetails.get("CheckerOperations"));
				}

				if(!bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){

					Reporting.logResults("Fail","Perform Checker Operation Joint Holder Checker operation", "Cannot Perform Checker Operations Joint Holder Checker operation : "+mapJointHolderDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation Joint Holder Checker operation ", "Negative testcase - Cannot Perform Checker Operations: "+mapJointHolderDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(bStatus && mapJointHolderDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation Joint Holder Checker operation ", "Peformed Checker operations with negative testdata. Investor Name: "+mapJointHolderDetails.get("Investor Name"));
					continue;
				}	
				
				if(mapJointHolderDetails.containsKey("First Name")){
				
					//mapJointHolderDetails.put("First Name", modifiedJointHolderDetails.get("First Name"));
					String Name = "";
					Name = mapJointHolderDetails.get("First Name");

					if(mapJointHolderDetails.get("Middle Name") != null){
						Name = Name+" "+ mapJointHolderDetails.get("Middle Name");
					}

					if(mapJointHolderDetails.get("Last Name") != null){
						Name = Name+" "+ mapJointHolderDetails.get("Last Name");
					}

					mapJointHolderDetails.put("Name", Name);
				}
				
				

				// Approve the Join Holder
				if(mapJointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "active");
					innerMap.put("Joint Holder Name", mapJointHolderDetails.get("Name"));
					verifyJointHolder.put(Reporting.Testcasename,innerMap );
				}

				//Reject the Joint Holder
				if(mapJointHolderDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){
					innerMap.put("Status", "inactive");
					innerMap.put("Joint Holder Name", mapJointHolderDetails.get("Name"));
					verifyJointHolder.put(Reporting.Testcasename,innerMap );
				}
			}

			verifyValuesinSearchPanel(verifyJointHolder);
		}
		catch(Exception e){
			e.printStackTrace();
		}
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
	@AfterMethod
	public static void tearDown(){
		Reporting.Testcasename = "Close Application";
		NewUICommonFunctions.refreshThePage();
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}



