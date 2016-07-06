package com.viteos.veda.master.classtestscripts;

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

public class ClassMaster_CheckerOperations_TC2 {

	static boolean bStatus;
	static String sRefrenceClassSheetName = "ClassDetailsTabTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Class Master";
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
	public static void testClassCheckerOperationsWithoutClone(){
		try{

			Map<String, Map<String, String>> mapAllClassDetails = Utilities.readMultipleTestData(Global.sClassTestDataFilePath,sRefrenceClassSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllClassDetails.size();index++){

				Map<String, String> mapClassDetails = mapAllClassDetails.get("Row"+index);
				Map<String, String> innerMap = new HashMap<String, String>();

				if(mapClassDetails.get("Clone") != null && mapClassDetails.get("Clone").equalsIgnoreCase("Yes")){
					continue;
				}

				if(mapClassDetails.get("VerifyCloneAndLEData")!=null && mapClassDetails.get("VerifyCloneAndLEData").equalsIgnoreCase("Yes")){
					continue;
				}

				if(!mapClassDetails.get("OperationType").equalsIgnoreCase("Save") || mapClassDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}

				Reporting.Testcasename = mapClassDetails.get("TestcaseName");

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to DashBoard", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to DashBoard", "DashBoard Menu selected succesfully");

				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,mapClassDetails.get("Class Name"),mapClassDetails.get("CheckerOperations"));

				if(bStatus && mapClassDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapClassDetails.get("CheckerOperations"), "Successfully Performed checker operations for ClassName : "+mapClassDetails.get("Class Name"));
				}

				if(!bStatus && mapClassDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapClassDetails.get("CheckerOperations"), "Cannot Perform Checker Operations. **Error : "+Messages.errorMsg+". For ClassName : "+mapClassDetails.get("Class Name"));
					continue;
				}

				if(!bStatus && mapClassDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapClassDetails.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations for ClassName "+mapClassDetails.get("Class Name"));
				}

				if(bStatus && mapClassDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapClassDetails.get("CheckerOperations"), "Peformed Checker operations with negative testdata. for ClassName : "+mapClassDetails.get("Class Name"));
					continue;
				}

				if(mapClassDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "active");
					innerMap.put("Class Name", mapClassDetails.get("Class Name"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
				}

				if(mapClassDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){
					innerMap.put("Status", "inactive");
					innerMap.put("Class Name", mapClassDetails.get("Class Name"));
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
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}

	public static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyLE) {
		try{

			for (Entry<String, Map<String, String>> test : verifyLE.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Class Master
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Class");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Class Master","Cannot Navigate to Class Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Class Master", "Navigated to Class Master");


				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Class in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Class Name", test.getValue().get("Class Name"), test.getValue().get("Status"),"Class", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Class Name should not be in active state",test.getKey()+" Class Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Class Name should not be in active state",test.getKey()+" Class Name is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Class Name should be in active state",test.getKey()+" Class Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify  Class Name should be in active state",test.getKey()+" Class Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


}
