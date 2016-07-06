package com.viteos.veda.master.formulatestscripts;

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
import com.viteos.veda.master.lib.TempFunctions;

public class FormulaSetupMaster_ActivateMakerOperations_TC7 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestdata";


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

	@Test
	public static void testSearchFunctions(){
		try{
			Map<String, Map<String, String>> mapAllFormulaDetails = Utilities.readMultipleTestData(Global.sFormulaSetupTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllFormulaDetails.size();index++){
				Map<String, String> mapModifyFormulaDetails = mapAllFormulaDetails.get("Row"+index);

				if(mapModifyFormulaDetails.get("Operation").equalsIgnoreCase("Validate") || mapModifyFormulaDetails.get("Operation").equalsIgnoreCase("Modify") || mapModifyFormulaDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}

				Reporting.Testcasename = mapModifyFormulaDetails.get("TestcaseNameRef");

				//navigate to Client Module
				
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Formula");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Formula Setup Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Formula Setup Mater Under Fund Setup", "Formula Setup Menu selected succesfully");

				if(mapModifyFormulaDetails.get("Operation").equalsIgnoreCase("Activate")){

					Map<String, String> createdFormulaDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath,"FormulaTestData", mapModifyFormulaDetails.get("TestcaseNameRef"));

					Map<String, String> modifiedFormulaDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath,"ModifyTestdata", mapModifyFormulaDetails.get("TestcaseNameRef"));
					createdFormulaDetails.putAll(modifiedFormulaDetails);
					
					bStatus = TempFunctions.activateFeeTye("Fee Type", createdFormulaDetails.get("FeeType"),"Fee Rule Name", createdFormulaDetails.get("FeeRuleName"),"Rule");
					if(!bStatus){
						Reporting.logResults("Fail","Validate Formula has been Activated", "Formula has not be Activated. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate Formula has been Aeactivated", "Formula has been dActivated");
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

		Reporting.Testcasename = "Close Application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
