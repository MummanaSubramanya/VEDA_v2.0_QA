package com.viteos.veda.master.seriestestscripts;

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

public class SeriesMaster_CheckerOperations_TC2 {

	static boolean bStatus;
	static String sSheetName = "SeriesTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Series Master";
		Reporting.Testcasename = "Open Application";

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void testCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllSeriesDetails = Utilities.readMultipleTestData(Global.sSeriesTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllSeriesDetails.size();index++){

				Map<String, String> mapSeriesDetails = mapAllSeriesDetails.get("Row"+index);
				if(!mapSeriesDetails.get("OperationType").equalsIgnoreCase("Save") || mapSeriesDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}

				Map<String, String> innerMap = new HashMap<String, String>();

				Reporting.Testcasename = mapSeriesDetails.get("TestcaseName");

				// Navigate to Dash board

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				// Perform Checker OPeration

				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,mapSeriesDetails.get("SeriesName"),mapSeriesDetails.get("CheckerOperations"));
				if(bStatus && mapSeriesDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapSeriesDetails.get("CheckerOperations"), "Successfully Performed checker operations for series: "+mapSeriesDetails.get("SeriesName"));
				}

				if(!bStatus && mapSeriesDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapSeriesDetails.get("CheckerOperations"), "Cannot Perform Checker Operations. Error: "+Messages.errorMsg+" for series: "+mapSeriesDetails.get("SeriesName"));
					continue;
				}

				if(!bStatus && mapSeriesDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapSeriesDetails.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations for sereies: "+mapSeriesDetails.get("SeriesName")+". Error: "+Messages.errorMsg);
				}

				if(bStatus && mapSeriesDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapSeriesDetails.get("CheckerOperations"), "Peformed Checker operations with negative testdata. Series Name: "+mapSeriesDetails.get("SeriesName"));
					continue;
				}	

				// Approve Series
				if(mapSeriesDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "active");
					innerMap.put("SeriesName", mapSeriesDetails.get("SeriesName"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
				}

				//Reject Series
				if(mapSeriesDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){
					innerMap.put("Status", "inactive");
					innerMap.put("SeriesName", mapSeriesDetails.get("SeriesName"));
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
		Reporting.Testcasename = "Close app";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application.");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
	
	
	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyClient) {
		try{

			for (Entry<String, Map<String, String>> test : verifyClient.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Series Master
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Series");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Series Master","Cannot Navigate to Series Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Series Master", "Navigated to Series Master");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Series in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Series Name", test.getValue().get("SeriesName"), test.getValue().get("Status"),"Series", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Series Name shouldnot be in active state",test.getKey()+" Series Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Series Name shouldnot be in active state",test.getKey()+" Series Name is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Series Name should be in active state",test.getKey()+" Series Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Series Name should be in active state",test.getKey()+" Series Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
