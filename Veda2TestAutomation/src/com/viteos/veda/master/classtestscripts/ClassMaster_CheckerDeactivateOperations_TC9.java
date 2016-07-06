package com.viteos.veda.master.classtestscripts;

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

public class ClassMaster_CheckerDeactivateOperations_TC9 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

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
	public static void testSearchFunctions(){
		try{
			Map<String, Map<String, String>> mapAllModifyClassDetails = Utilities.readMultipleTestData(Global.sModifyClassTestDataFilePath, sSheetName,"Y");

			for(int index = 1;index <= mapAllModifyClassDetails.size();index++){

				Map<String, String> mapClassSearchDetails = mapAllModifyClassDetails.get("Row"+index);				

				if (mapClassSearchDetails.get("Operation") == null || !mapClassSearchDetails.get("Operation").equalsIgnoreCase("Deactivate")) {
					continue;
				}
				Reporting.Testcasename = mapClassSearchDetails.get("TestcaseNameRef");
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Dashboard", "Cannot be navigated to Dashboard. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Dashboard", "Succesfully Navigated to Dashboard.");

				boolean bNameStatus = false;

				Map<String, String> mapActualClassDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassDetailsTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedClassDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyClassTestDataFilePath, "ModifyClassDetails", mapClassSearchDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedMainTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyClassTestDataFilePath, "ModifyTestData", mapClassSearchDetails.get("TestcaseNameRef"));

				if(mapClassSearchDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					if (mapModifiedClassDetailsTabData.get("Class Name") != null && mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify")) {
						if (mapModifiedClassDetailsTabData.containsKey("Class Name")) {
							bNameStatus = true;
							mapClassSearchDetails.put("Class Name", mapModifiedClassDetailsTabData.get("Class Name"));
						}
					}
					if(!bNameStatus) {
						mapClassSearchDetails.put("Class Name", mapActualClassDetailsTabData.get("Class Name"));
						//mapClassSearchDetails.put("CheckerOperations", mapActualClassDetailsTabData.get("CheckerOperations"));
					}
					
					//Get the checker operations
					Map<String, String> mapModifiedCurClassDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyClassTestDataFilePath, "ModifyClassDetails", mapClassSearchDetails.get("TCID"));
					mapClassSearchDetails.put("CheckerOperations", mapModifiedCurClassDetailsTabData.get("CheckerOperations"));

					
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapClassSearchDetails.get("Class Name"), mapClassSearchDetails.get("CheckerOperations"));
					if(!bStatus){
						Reporting.logResults("Fail","Checker : Deactivating Class Name", "Checker : Failed to Perform Checker operation"+mapClassSearchDetails.get("CheckerOperations")+"   for Class : "+mapClassSearchDetails.get("Class Name"));
						continue;
					}
					Reporting.logResults("Pass","Checker : Validate Class Name has been deactivated", "Checker : Performed Checker Operation "+mapClassSearchDetails.get("Class Name")+" Successfull for the class "+mapClassSearchDetails.get("Class Name"));
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
		NewUICommonFunctions.refreshThePage();
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
