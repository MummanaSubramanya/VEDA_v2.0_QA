package com.viteos.veda.master.seriestestscripts;

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

public class SeriesMaster_ActivateMakerOperations_TC7 {

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
				
				if(mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Validate") || mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Modify") || mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}
				
				//navigate to series Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Series");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Series Master Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Series Mater Under Fund Setup", "Series Menu selected succesfully");

				Reporting.Testcasename = mapSeriesSearchDetails.get("TestcaseNameRef");	
				
				boolean namemodifystatus = false; 	
				if(mapSeriesSearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					Map<String, String> createdSeriesDetails = ExcelUtils.readDataABasedOnCellName(Global.sSeriesTestDataFilePath,"SeriesTestData", mapSeriesSearchDetails.get("TestcaseNameRef"));

					Map<String, String> modifiedSeriesDetails = ExcelUtils.readDataABasedOnCellName(Global.sSeriesTestDataFilePath,"ModifyTestdata", mapSeriesSearchDetails.get("TestcaseNameRef"));
					if(modifiedSeriesDetails != null && modifiedSeriesDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedSeriesDetails.containsKey("SeriesName")){
							namemodifystatus =  true;
							mapSeriesSearchDetails.put("SeriesName", modifiedSeriesDetails.get("SeriesName"));
						}
					}
					if(!namemodifystatus){
						mapSeriesSearchDetails.put("SeriesName", createdSeriesDetails.get("SeriesName"));
					}

					bStatus = NewUICommonFunctions.activateMaster("Series Name", mapSeriesSearchDetails.get("SeriesName"), "Series");
					if(!bStatus){
						Reporting.logResults("Fail","Validate Series has been Activated for Series : "+mapSeriesSearchDetails.get("SeriesName"), " Series has not been Activated. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate Series has been Activated for Series : "+mapSeriesSearchDetails.get("SeriesName"), " Series Has been Activated");
					continue;
				}
			}
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

}
