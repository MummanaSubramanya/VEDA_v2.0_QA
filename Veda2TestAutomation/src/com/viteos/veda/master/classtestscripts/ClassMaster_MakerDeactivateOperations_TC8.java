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

public class ClassMaster_MakerDeactivateOperations_TC8 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Class Master";
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
			Map<String, Map<String, String>> mapAllModifyClassDetails = Utilities.readMultipleTestData(Global.sModifyClassTestDataFilePath, sSheetName,"Y");

			for(int index = 1;index <= mapAllModifyClassDetails.size();index++){

				Map<String, String> mapClassSearchDetails = mapAllModifyClassDetails.get("Row"+index);				
						
				if (mapClassSearchDetails.get("Operation") == null || !mapClassSearchDetails.get("Operation").equalsIgnoreCase("Deactivate")) {
					continue;
				}
				Reporting.Testcasename = mapClassSearchDetails.get("TestcaseNameRef");
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Class");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Class Master Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Class Master Under Fund Setup", "Class Menu selected succesfully");
				
				boolean bNameStatus = false;
				
				// read the modified data if changed
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
					}
					
					//deactivate the class functions
					bStatus = NewUICommonFunctions.deactivateMaster("Class Name", mapClassSearchDetails.get("Class Name"), "Class");
					if(!bStatus){
						Reporting.logResults("Fail","Maker : Deactivating Class Name", "Maker : Class Name : "+mapClassSearchDetails.get("Class Name")+" has not been Deactivated");
						continue;
					}
					Reporting.logResults("Pass","Maker : Validate Class Name has been Deactivated", "Maker : Class Name : "+mapClassSearchDetails.get("Class Name")+" Has been Deactivated");
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
