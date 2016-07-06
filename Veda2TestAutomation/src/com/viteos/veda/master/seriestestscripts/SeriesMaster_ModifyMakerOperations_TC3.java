package com.viteos.veda.master.seriestestscripts;

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
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.SeriesAppFunctions;

public class SeriesMaster_ModifyMakerOperations_TC3 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";

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
	public static void testSearchFunctions(){
		try{

			Map<String, Map<String, String>> mapAllSeriesDetails = Utilities.readMultipleTestData(Global.sSeriesTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllSeriesDetails.size();index++){
				Map<String, String> mapSeriesSearchDetails = mapAllSeriesDetails.get("Row"+index);
				Reporting.Testcasename = mapSeriesSearchDetails.get("TestcaseNameRef");

				if(mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}
				//read testdata
				Map<String, String> createdSeriesDetails = ExcelUtils.readDataABasedOnCellName(Global.sSeriesTestDataFilePath,"SeriesTestData", mapSeriesSearchDetails.get("TestcaseNameRef"));
				NewUICommonFunctions.refreshThePage();
				//navigate to series Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Series");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Series Master Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Series Mater Under Fund Setup", "Series Menu selected succesfully");

				//get operation type to be done to modify
				if(mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Modify")){

					//do modify for the approved series
					if(createdSeriesDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", mapSeriesSearchDetails);
						
						bStatus = NewUICommonFunctions.modifyMasterDetails("Series Name", createdSeriesDetails.get("SeriesName"), "Series", tempmap);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Series Data for series "+createdSeriesDetails.get("SeriesName"), "Series cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Series Data for series "+createdSeriesDetails.get("SeriesName"), "Series modified with the Tesdata: "+mapSeriesSearchDetails);
						continue;
					}

					//do modify for the Return series
					if(createdSeriesDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						//select the dashboard
						NewUICommonFunctions.selectMenu("Dashboard", "None");
						bStatus = SeriesAppFunctions.modifyReturnSeriesDetails(createdSeriesDetails.get("SeriesName"), mapSeriesSearchDetails);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Series Data for Series "+createdSeriesDetails.get("SeriesName") , "Series cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Client Data for Series "+createdSeriesDetails.get("SeriesName"), "Client modified with the Tesdata: "+mapSeriesSearchDetails);
						continue;
					}
				}

				//do validate the data when clicked n modify the data
				if(mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Validate")){
					if(createdSeriesDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						
						Map<String, Map<String, String>> tempmap = new HashMap<String, Map<String,String>>();
						tempmap.put("1", createdSeriesDetails);
						
						bStatus = NewUICommonFunctions.verifyMasterDetails("Series Name", createdSeriesDetails.get("SeriesName"), "Series", tempmap);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen for Series "+createdSeriesDetails.get("SeriesName"), "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen for Series "+createdSeriesDetails.get("SeriesName"), "Validated the details Actual Details equal to Expected");
						continue;
					}

					if(createdSeriesDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						NewUICommonFunctions.selectMenu("Dashboard", "None");

						NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,createdSeriesDetails.get("SeriesName"),"");

						bStatus = SeriesAppFunctions.verifySeriesDetailsInEditScreen(createdSeriesDetails);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen for Series "+createdSeriesDetails.get("SeriesName"), "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen for Series "+createdSeriesDetails.get("SeriesName"), "Validated the details Actual Details equal to Expected");
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
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application.");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}

}
