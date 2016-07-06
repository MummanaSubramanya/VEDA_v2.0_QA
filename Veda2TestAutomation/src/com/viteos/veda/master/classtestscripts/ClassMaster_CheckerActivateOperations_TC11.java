package com.viteos.veda.master.classtestscripts;

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

public class ClassMaster_CheckerActivateOperations_TC11 {
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
			
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();
			
			for(int index = 1;index <= mapAllModifyClassDetails.size();index++){

				Map<String, String> mapClassSearchDetails = mapAllModifyClassDetails.get("Row"+index);	
				
				Map<String, String> innerMap = new HashMap<String, String>();
						
				if (mapClassSearchDetails.get("Operation") == null || !mapClassSearchDetails.get("Operation").equalsIgnoreCase("Activate")) {
					continue;
				}
				Reporting.Testcasename = mapClassSearchDetails.get("TestcaseNameRef");
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Dashboard", "Cannot be navigated to Dashboard. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Dashboard", "Succesfully Navigated to Dashboard.");
				
				boolean bStatusName = false;
				
				Map<String, String> mapActualClassDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassDetailsTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedClassDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyClassTestDataFilePath, "ModifyClassDetails", mapClassSearchDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedMainTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyClassTestDataFilePath, "ModifyTestData", mapClassSearchDetails.get("TestcaseNameRef"));
				
				if(mapClassSearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					if (mapModifiedClassDetailsTabData.get("Class Name") != null && mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify")) {
						if (mapModifiedClassDetailsTabData.containsKey("Class Name")) {
							bStatusName = true;
							mapClassSearchDetails.put("Class Name", mapModifiedClassDetailsTabData.get("Class Name"));
							//mapClassSearchDetails.put("CheckerOperations", mapModifiedClassDetailsTabData.get("CheckerOperations"));
						}
					}
					if(!bStatusName) {
						mapClassSearchDetails.put("Class Name", mapActualClassDetailsTabData.get("Class Name"));
						//mapClassSearchDetails.put("CheckerOperations", mapActualClassDetailsTabData.get("CheckerOperations"));
					}
					
					//do activate from checker
					Map<String, String> mapModifiedCurClassDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyClassTestDataFilePath, "ModifyClassDetails", mapClassSearchDetails.get("TCID"));
					mapClassSearchDetails.put("CheckerOperations", mapModifiedCurClassDetailsTabData.get("CheckerOperations"));

					
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapClassSearchDetails.get("Class Name"), mapClassSearchDetails.get("CheckerOperations"));
					if(!bStatus){
						Reporting.logResults("Fail","Checker : Activating Class Name  ", "Checker : Class Name : "+mapClassSearchDetails.get("Class Name")+" Has not been Activated");
						continue;
					}
					Reporting.logResults("Pass","Checker : Validate Class Name has been Activated", "Class Name : "+mapClassSearchDetails.get("Class Name")+" Has been Activated");
					
					
					if(mapClassSearchDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
						innerMap.put("Status", "active");
						innerMap.put("Class Name", mapClassSearchDetails.get("Class Name"));
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

		Reporting.Testcasename = "Close Application";
		NewUICommonFunctions.refreshThePage();
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
	
	public static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyLE) {
		try{

			for (Entry<String, Map<String, String>> test : verifyLE.entrySet()) {

				Reporting.Testcasename = test.getKey();

				//Navigate to Class Master
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Class");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Class Master","Cannot Navigate to Class Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Class Master", "Navigated to Class Master");


				int time =10 ;

				// Search the Class in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Class Name", test.getValue().get("Class Name"), test.getValue().get("Status"),"Class", time);

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Class Name should be in active state",test.getKey()+" Class Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify  Class Name should be in active state",test.getKey()+" Class Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
