package com.viteos.veda.master.seriestestscripts;

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
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;
import com.viteos.veda.master.lib.SeriesAppFunctions;

public class SeriesMaster_MakerOperations_TC1 {

	static boolean bStatus;
	static String sSheetName = "SeriesTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Series Master";
		Reporting.Testcasename = "Open Application";

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}


	@Test
	public static void test(){

		Map<String, Map<String, String>> mapAllSeriesDetails = Utilities.readMultipleTestData(Global.sSeriesTestDataFilePath,sSheetName,"Y");

		for(int index = 1;index <= mapAllSeriesDetails.size();index++){

			Map<String, String> mapSeriesDetails = mapAllSeriesDetails.get("Row"+index);
			Reporting.Testcasename = mapSeriesDetails.get("TestcaseName");
			
			NewUICommonFunctions.refreshThePage();
			
			// Navigate to Series Master
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Series");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Series Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Series Mater Under Fund Setup", "Series Menu selected succesfully");

			// Click On Add Button
			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on 'Add New' button for Series creation", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Click on 'Add New' button for Series creation", "Add New Button clicked succesfully");

			//Add New Series Details
			bStatus = SeriesAppFunctions.addNewSeries(mapSeriesDetails);
			if(mapSeriesDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){

				if(mapSeriesDetails.get("OperationType").equalsIgnoreCase("Save")){
					Reporting.logResults("Pass","Perform Maker Operation: "+mapSeriesDetails.get("OperationType"), "Succesfully Performed maker operations for Series: "+mapSeriesDetails.get("SeriesName"));
				}
				if(mapSeriesDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapSeriesDetails.get("OperationType"), "Performed maker Operation "+mapSeriesDetails.get("OperationType")+" Successfully");
				}
			}
			if(mapSeriesDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapSeriesDetails.get("OperationType"), "Cannot perform Maker Operation. Error: "+Messages.errorMsg+" for Series: "+mapSeriesDetails.get("SeriesName"));
				Global.driver.navigate().refresh();
				continue;
			}
			if(mapSeriesDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
				Reporting.logResults("Pass", "Perform Maker Operation: "+mapSeriesDetails.get("OperationType"), "Negative testcase-Cannot perform Maker Action for Series: "+mapSeriesDetails.get("SeriesName"));
				Global.driver.navigate().refresh();
				continue;
			}
			if(mapSeriesDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
				Reporting.logResults("Fail", "Perform Maker Operation: "+mapSeriesDetails.get("OperationType"), "Performed Maker Action with negative testdata. Series Name: "+mapSeriesDetails.get("SeriesName"));
			}
		}
	}

	@AfterMethod
	public static void tearDown(){
		
		Reporting.Testcasename = "Close Application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
