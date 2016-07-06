package com.viteos.veda.master.legalentitytestscripts;

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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class LegalEntityMaster_CheckerOperations_TC2 {

	static boolean bStatus;
	static String sRefrenceLESheetName = "LegalEntityDetailsTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Legal Entity Master";
		Reporting.Testcasename = "Open Application";


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

			Map<String, Map<String, String>> mapAllLegalEntityDetails = Utilities.readMultipleTestData(Global.sLegalEntityTestDataFilePath,sRefrenceLESheetName,"Y");
			Map<String, Map<String, String>> VerifyMap = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllLegalEntityDetails.size();index++){

				Map<String, String> mapLegalEntityDetails = mapAllLegalEntityDetails.get("Row"+index);
				
				
				if(mapLegalEntityDetails.get("VerifyCloneData")!=null && mapLegalEntityDetails.get("VerifyCloneData").equalsIgnoreCase("Yes")){
					continue;
				}
				
				if(mapLegalEntityDetails.get("Entity Type")!=null && mapLegalEntityDetails.get("Clone")!=null){
					if(mapLegalEntityDetails.get("Entity Type").equalsIgnoreCase("Feeder") || mapLegalEntityDetails.get("Clone").equalsIgnoreCase("Yes")){
						continue;
					}
				}
				if(mapLegalEntityDetails.get("Entity Type") == null){
					if(mapLegalEntityDetails.get("Clone")!=null && mapLegalEntityDetails.get("Clone").equalsIgnoreCase("Yes")){
						continue;
					}
				}
				if(mapLegalEntityDetails.get("Clone") == null){
					if(mapLegalEntityDetails.get("Entity Type") != null && mapLegalEntityDetails.get("Entity Type").equalsIgnoreCase("Feeder")){
						continue;
					}
				}
				if(!mapLegalEntityDetails.get("OperationType").equalsIgnoreCase("Save") || mapLegalEntityDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}
				
				Map<String, String> innerMap = new HashMap<String, String>();

				Reporting.Testcasename = mapLegalEntityDetails.get("TestcaseName");

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to DashBoard", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to DashBoard", "DashBoard Menu selected succesfully");

				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,mapLegalEntityDetails.get("Legal Entity Name"),mapLegalEntityDetails.get("CheckerOperations"));
				if(bStatus && mapLegalEntityDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapLegalEntityDetails.get("CheckerOperations"), "Successfully Performed checker operations for Legal Entity: "+mapLegalEntityDetails.get("Legal Entity Name"));
				}

				if(!bStatus && mapLegalEntityDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapLegalEntityDetails.get("CheckerOperations"), "Cannot Perform Checker Operations. Error: "+Messages.errorMsg+". For Legal Entity: "+mapLegalEntityDetails.get("Legal Entity Name"));
					continue;
				}

				if(!bStatus && mapLegalEntityDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapLegalEntityDetails.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations for Legal Entity "+mapLegalEntityDetails.get("Legal Entity Name"));
				}

				if(bStatus && mapLegalEntityDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapLegalEntityDetails.get("CheckerOperations"), "Peformed Checker operations with negative testdata. for Legal Entity Name: "+mapLegalEntityDetails.get("Legal Entity Name"));
					continue;
				}

				if(mapLegalEntityDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "active");
					innerMap.put("Legal Entity Name", mapLegalEntityDetails.get("Legal Entity Name"));
					VerifyMap.put(Reporting.Testcasename,innerMap );
				}
				if(mapLegalEntityDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){
					innerMap.put("Status", "inactive");
					innerMap.put("Legal Entity Name", mapLegalEntityDetails.get("Legal Entity Name"));
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

				//Navigate to Series Master
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Legal Entity");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Legal Entity Master","Cannot Navigate to Legal Entity Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Legal Entity Master", "Navigated to Legal Entity Master");


				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Series in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"),"LegalEntity", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Legal Entity Name shouldnot be in active state",test.getValue().get("Legal Entity Name")+" Legal Entity Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Legal Entity Name shouldnot be in active state",test.getValue().get("Legal Entity Name")+" Legal Entity Name is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Legal Entity Name should be in active state",test.getValue().get("Legal Entity Name")+" Legal Entity Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Legal Entity Name should be in active state",test.getValue().get("Legal Entity Name")+" Legal Entity Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


}
