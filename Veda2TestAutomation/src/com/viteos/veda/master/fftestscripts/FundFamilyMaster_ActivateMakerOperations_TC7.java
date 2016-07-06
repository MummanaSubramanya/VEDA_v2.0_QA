package com.viteos.veda.master.fftestscripts;

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

public class FundFamilyMaster_ActivateMakerOperations_TC7 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";


	@BeforeMethod
	public static void setup(){


		Reporting.Functionality ="Fund Family Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

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
			Map<String, Map<String, String>> mapAllFundFamilyDetails = Utilities.readMultipleTestData(Global.sFundFamilyTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllFundFamilyDetails.size();index++){

				Map<String, String> mapFundFamilySearchDetails = mapAllFundFamilyDetails.get("Row"+index);
				
				if(mapFundFamilySearchDetails.get("Operation").equalsIgnoreCase("Validate") || mapFundFamilySearchDetails.get("Operation").equalsIgnoreCase("Modify") || mapFundFamilySearchDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}
				
				Reporting.Testcasename = mapFundFamilySearchDetails.get("TestcaseNameRef");
				
				// Navigate to FundFamily
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Fund Family");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Fund Family Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Fund Family Mater Under Fund Setup", "Fund Family Menu selected succesfully");

				if(mapFundFamilySearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					boolean bNameModifyStatus = false;
					Map<String, String> mapCreatedFundFamilyDetails = ExcelUtils.readDataABasedOnCellName(Global.sFundFamilyTestDataFilePath,"FundFamilyTestData", mapFundFamilySearchDetails.get("TestcaseNameRef"));
					
					Map<String, String> modifiedFundFamilyDetails = ExcelUtils.readDataABasedOnCellName(Global.sFundFamilyTestDataFilePath,"ModifyTestdata", mapFundFamilySearchDetails.get("TestcaseNameRef"));
					if(modifiedFundFamilyDetails!=null && modifiedFundFamilyDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedFundFamilyDetails.containsKey("FundFamilyName")){
							bNameModifyStatus = true;
							mapFundFamilySearchDetails.put("FundFamilyName", modifiedFundFamilyDetails.get("FundFamilyName"));
						}
					}
					if(!bNameModifyStatus){
						mapFundFamilySearchDetails.put("FundFamilyName", mapCreatedFundFamilyDetails.get("FundFamilyName"));
					}

					bStatus = NewUICommonFunctions.activateMaster("Fund Family Name", mapFundFamilySearchDetails.get("FundFamilyName"), "FundFamily");
					if(!bStatus){
						Reporting.logResults("Fail","Validate FundFamily has been activated", "Fund Family has not be activated. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate FundFamily has been activated", "Fund Family :"+mapFundFamilySearchDetails.get("FundFamilyName")+" Has been activated By Maker");

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
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
