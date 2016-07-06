package com.viteos.veda.master.fftestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.FundFamilyAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class FundFamilyMaster_MakerOperations_TC1 {
	static boolean bStatus;
	static String sSheetName = "FundFamilyTestData";

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
	public static void addNewFundFamily()
	{
		Map<String,Map<String,String>> mapAllFundFamilyDetails=Utilities.readMultipleTestData(Global.sFundFamilyTestDataFilePath, sSheetName, "Y");
		for(int index = 1;index <= mapAllFundFamilyDetails.size();index++){

			Map<String, String> mapFundFamilyDetails = mapAllFundFamilyDetails.get("Row"+index);
			Reporting.Testcasename = mapFundFamilyDetails.get("TestcaseName");


			NewUICommonFunctions.refreshThePage();

			//navigate to the fundfamily
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Fund Family");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Fund Family Master Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Fund Family Master Under Fund Setup", "Fund Family Menu selected succesfully");


			// Click on Add New Button
			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on Add New button to add new Fund Family", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Click on Add New button to add new Fund Family", "Add New Button clicked succesfully");

			// Add New Fund Family Details

			bStatus = FundFamilyAppFunctions.addNewFundfamily(mapFundFamilyDetails);

			if(mapFundFamilyDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){

				if(mapFundFamilyDetails.get("OperationType").equalsIgnoreCase("Save")){

					Reporting.logResults("Pass", "Perform Maker Operation: "+mapFundFamilyDetails.get("OperationType"), "Performed maker Operation successfull for the scenario with test data " + mapFundFamilyDetails.get("FundFamilyName"));
				}
				if(mapFundFamilyDetails.get("OperationType").equalsIgnoreCase("Cancel")){

					Reporting.logResults("Pass", "Perform Maker Operation: "+mapFundFamilyDetails.get("OperationType"), "Performed maker Operation "+mapFundFamilyDetails.get("OperationType")+" Successfully");
				}
			}

			if(mapFundFamilyDetails.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){

				Reporting.logResults("Fail", "Perform Maker Operation: "+mapFundFamilyDetails.get("OperationType"), "Cannot perform maker Operations."+Messages.errorMsg);
				continue;
			}

			if(mapFundFamilyDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
				Reporting.logResults("Pass", "Perform Maker Operation: "+mapFundFamilyDetails.get("OperationType"), "Negative Testcase - Cannot perform maker operation " + mapFundFamilyDetails);
				continue ;
			}

			if(mapFundFamilyDetails.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){

				Reporting.logResults("Fail", "Perform Maker Operation: "+mapFundFamilyDetails.get("OperationType"), "performed maker operations for the scenario with Negative test data " + mapFundFamilyDetails);
				continue;
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
