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

public class ClassMaster_ModifyCheckerOperations_TC6 {

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
	public static void testCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllModifiedDetails = Utilities.readMultipleTestData(Global.sModifyClassTestDataFilePath, sSheetName, "Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();
			Map<String, Map<String, String>> objClassModifyTabsMaps = new HashMap<String, Map<String,String>>();

			for(int index = 1;index <= mapAllModifiedDetails.size();index++){

				Map<String, String> mapModifiedDetails = mapAllModifiedDetails.get("Row"+index);

				Map<String, String> innerMap = new HashMap<String, String>();
				
				if(mapModifiedDetails.get("Operation").equalsIgnoreCase("Validate") || mapModifiedDetails.get("Operation").equalsIgnoreCase("DeActivate") || mapModifiedDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}
				

				Reporting.Testcasename = mapModifiedDetails.get("TestcaseNameRef");
				
				Map<String, String> modfiedClassMap =  Utilities.readTestData(Global.sModifyClassTestDataFilePath,"ModifyClassDetails", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String, String> mapCreatedClassDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath,"ClassDetailsTabTestData", mapModifiedDetails.get("TestcaseNameRef"));

				Map<String , String> mapModifyGeneralDetails = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassGeneralDetails", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifySubscriptionDetials = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassSubscription", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifyOtherDetials = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassOtherDetails", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifyRedemption = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassRedemption", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifyTransfer = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassTransfer", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifySwitch = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassSwitch", mapModifiedDetails.get("TestcaseNameRef"));
				Map<String , String> mapModifyExchange = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassExchange", mapModifiedDetails.get("TestcaseNameRef"));
				
				objClassModifyTabsMaps.put("ClassDetails", modfiedClassMap);
				objClassModifyTabsMaps.put("GeneralDetails", mapModifyGeneralDetails);
				objClassModifyTabsMaps.put("SubscriptionDetails", mapModifySubscriptionDetials);
				objClassModifyTabsMaps.put("OtherDetails", mapModifyOtherDetials);
				objClassModifyTabsMaps.put("RedemptionDetails", mapModifyRedemption);
				objClassModifyTabsMaps.put("TransferDetails", mapModifyTransfer);
				objClassModifyTabsMaps.put("SwitchDetails", mapModifySwitch);
				objClassModifyTabsMaps.put("ExchangeDetails", mapModifyExchange);
				
				if(modfiedClassMap.get("Class Name")==null){
					modfiedClassMap.put("Class Name",mapCreatedClassDetails.get("Class Name"));
				}

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");


				if(mapModifiedDetails.get("Operation").equalsIgnoreCase("Modify")){
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,modfiedClassMap.get("Class Name"),modfiedClassMap.get("CheckerOperations"));
					if(bStatus && modfiedClassMap.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						NewUICommonFunctions.selectMenu("Fund Setup","Class");
						boolean bTestStatus = NewUICommonFunctions.verifyMasterDetails("Class Name", modfiedClassMap.get("Class Name"), "Class", objClassModifyTabsMaps);//(objClassModifyTabsMaps);
						if(!bTestStatus){
							Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved.Error: "+Messages.errorMsg);
						}
					}
				}

				if(mapModifiedDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,mapCreatedClassDetails.get("Class Name"),modfiedClassMap.get("CheckerOperations"));
				}

				if(bStatus && modfiedClassMap.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+modfiedClassMap.get("CheckerOperations"));
				}

				if(!bStatus && modfiedClassMap.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+modfiedClassMap.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && modfiedClassMap.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+modfiedClassMap.get("CheckerOperations")+". Error: "+Messages.errorMsg);
				}

				if(bStatus && modfiedClassMap.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Class Name: "+modfiedClassMap.get("Class Name"));
					continue;
				}	

				if(modfiedClassMap.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "active");
					innerMap.put("Class Name", mapCreatedClassDetails.get("Class Name"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
				}

				if(modfiedClassMap.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){
					innerMap.put("Status", "inactive");
					innerMap.put("Class Name", mapCreatedClassDetails.get("Class Name"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
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
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Class in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Class Name", test.getValue().get("Class Name"), test.getValue().get("Status"),"Class", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Class Name should not be in active state",test.getKey()+" Class Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Class Name should not be in active state",test.getKey()+" Class Name is in active state");
				}

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
