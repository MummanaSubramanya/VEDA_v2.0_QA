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
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class SeriesMaster_ModifyCheckerOperations_TC4 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Series Master";
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
	public static void testCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllSeriesDetails = Utilities.readMultipleTestData(Global.sSeriesTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllSeriesDetails.size();index++){

				Map<String, String> mapSeriesSearchDetails = mapAllSeriesDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();

				Reporting.Testcasename = mapSeriesSearchDetails.get("TestcaseNameRef");

				
				if(mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Validate") || mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				if(mapSeriesSearchDetails.get("ExpectedResults")!=null && mapSeriesSearchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}
				if(mapSeriesSearchDetails.get("OperationType")!=null && mapSeriesSearchDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					continue;
				}

				// Navigate to Dashboard
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				Map<String, String> mapSeriesDetails = ExcelUtils.readDataABasedOnCellName(Global.sSeriesTestDataFilePath,"SeriesTestData", mapSeriesSearchDetails.get("TestcaseNameRef"));

/*				if(mapSeriesSearchDetails.get("SeriesName")==null){
					mapSeriesSearchDetails.put("SeriesName", mapSeriesDetails.get("SeriesName"));
				}*/
				
				mapSeriesDetails.putAll(mapSeriesSearchDetails);
				

				// Verify Modify Details After Modification
				if(mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Modify")){
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,mapSeriesDetails.get("SeriesName"),mapSeriesDetails.get("CheckerOperations"));
					if(bStatus && mapSeriesSearchDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Series");
						if(!bStatus){
							Reporting.logResults("Fail", "Navigate To Series","Cannot Navigate to Series");
							continue;
						}
						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", mapSeriesDetails);
						
						boolean bTestStatus = NewUICommonFunctions.verifyMasterDetails("Series Name", mapSeriesDetails.get("SeriesName"), "Series", tempmap);
						if(!bTestStatus){
							Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved.Error: "+Messages.errorMsg);
						}
					}

					if(bStatus && mapSeriesSearchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapSeriesSearchDetails.get("CheckerOperations"));
					}

					if(!bStatus && mapSeriesSearchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapSeriesSearchDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
						continue;
					}

					if(!bStatus && mapSeriesSearchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+mapSeriesSearchDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					}

					if(bStatus && mapSeriesSearchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Cleint Name: "+mapSeriesSearchDetails.get("ClientName"));
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
			}

			verifyValuesinSearchPanel(VerifyMap);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@AfterMethod
	public static void tearDown(){
		Reporting.Testcasename = "Close application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
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
