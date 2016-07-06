package com.viteos.veda.master.formulatestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.FormulaSetupAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class FormulaSetupMaster_MakerOperations_TC1 {
	static boolean bStatus;
	static String sSheetName = "FormulaTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Formula Setup Master";
		Reporting.Testcasename = "Open Application";


		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@org.testng.annotations.Test
	public static void test(){

		Map<String, Map<String, String>> mapAllDetails = Utilities.readMultipleTestData(Global.sFormulaSetupTestDataFilePath,sSheetName,"Y");

		for(int index = 1;index <= mapAllDetails.size();index++){

			Map<String, String> mapFormulaDetails = mapAllDetails.get("Row"+index);
			Reporting.Testcasename = mapFormulaDetails.get("TestcaseName");

			NewUICommonFunctions.refreshThePage();
			//Navigate to Formula Step Master
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Formula");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Formula Setup Master", Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Formula Setup Master", "Navigated successfully to Formula Setup");

			// Click on Add button

			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
			if(!bStatus){
				Reporting.logResults("Fail", "Click on 'Add New' button for New Rule creation", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass","Click on 'Add New' button for New Rule creation", "Add New Button clicked succesfully");

			//Add New Formula 

			bStatus = FormulaSetupAppFunctions.addNewFormulas(mapFormulaDetails);

			if(bStatus && mapFormulaDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapFormulaDetails.get("OperationType").equalsIgnoreCase("Save")){

					Reporting.logResults("Pass", "Perform Maker Operation: "+mapFormulaDetails.get("OperationType"), "Performed maker Operation successfull for"+ mapFormulaDetails.get("FeeType") + " with rule "  + mapFormulaDetails.get("FeeRuleName"));
				}
				if(mapFormulaDetails.get("OperationType").equalsIgnoreCase("Cancel")){

					Reporting.logResults("Pass", "Perform Maker Operation: "+mapFormulaDetails.get("OperationType"), "Performed maker Operation "+mapFormulaDetails.get("OperationType")+" Successfully");
				}
				continue;
			}

			if(!bStatus && mapFormulaDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail","Add a New formula", "New formula cannot be added. Error: "+Messages.errorMsg);
				Global.driver.navigate().refresh();
				continue;
			}

			if(!bStatus && mapFormulaDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass","Add a New formula", "Negative testcase - New formula cannot be added"  + mapFormulaDetails.get("FeeRuleName"));
				Global.driver.navigate().refresh();
				continue;
			}

			if(bStatus && mapFormulaDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass","Add a New formula", "Formula got created with negative testdata. Formula Name: "+mapFormulaDetails.get("FeeRuleName"));
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
