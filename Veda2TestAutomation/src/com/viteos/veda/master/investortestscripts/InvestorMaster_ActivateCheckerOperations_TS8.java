package com.viteos.veda.master.investortestscripts;

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

public class InvestorMaster_ActivateCheckerOperations_TS8 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Investor Master";
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
			Map<String, Map<String, String>> mapAllInvestorGeneralDetails = Utilities.readMultipleTestData(Global.sInvestorModifyTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> verificationMap = new HashMap<String, Map<String,String>>();

			for(int index = 1;index <= mapAllInvestorGeneralDetails.size();index++){
				Map<String, String> mapSearchInvestorDetails = mapAllInvestorGeneralDetails.get("Row"+index);
				String sInvestorFirstName = "";
				String sInvestorMiddleName = "";
				String sInvestorLastName = "";
				String sInvestorName = ""; 

				Map<String, String> innerMap = new HashMap<String, String>();

				if (mapSearchInvestorDetails.get("Operation") == null || !mapSearchInvestorDetails.get("Operation").equalsIgnoreCase("Activate")) {
					continue;
				}

				Reporting.Testcasename = mapSearchInvestorDetails.get("TestcaseNameRef");

				// read the modified data if changed
				Map<String, String> mapActualGeneralDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, "GeneralDetails", mapSearchInvestorDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedGeneralDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sInvestorModifyTestDataFilePath, "ModifyGeneralDetails", mapSearchInvestorDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedMainTabData = ExcelUtils.readDataABasedOnCellName(Global.sInvestorModifyTestDataFilePath, "ModifyTestData", mapSearchInvestorDetails.get("TestcaseNameRef"));

				if(mapSearchInvestorDetails.get("Operation").equalsIgnoreCase("Activate")){
					if (mapActualGeneralDetailsTabData.get("InvestorType") != null && mapActualGeneralDetailsTabData.get("InvestorType").equalsIgnoreCase("Entity")) {
						if (mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify") && mapModifiedGeneralDetailsTabData.containsKey("RegistrationName")) {
							mapSearchInvestorDetails.put("RegistrationName", mapModifiedGeneralDetailsTabData.get("RegistrationName"));	
							sInvestorName = mapModifiedGeneralDetailsTabData.get("RegistrationName");
						}
						else if(mapActualGeneralDetailsTabData.get("RegistrationName") != null){
							sInvestorName = mapActualGeneralDetailsTabData.get("RegistrationName");
						}
					}
					if (mapActualGeneralDetailsTabData.get("InvestorType") != null && mapActualGeneralDetailsTabData.get("InvestorType").equalsIgnoreCase("Individual")) {
						if (mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify") && mapModifiedGeneralDetailsTabData.get("FirstName") != null) {
							sInvestorFirstName = mapModifiedGeneralDetailsTabData.get("FirstName");
							sInvestorName = sInvestorFirstName;
						}
						else if (mapActualGeneralDetailsTabData.get("FirstName") != null) {
							sInvestorFirstName = mapActualGeneralDetailsTabData.get("FirstName");
							sInvestorName = sInvestorFirstName;
						}						
						if (mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify") && mapModifiedGeneralDetailsTabData.get("MiddleName") != null) {
							sInvestorMiddleName = mapModifiedGeneralDetailsTabData.get("MiddleName");
							sInvestorName = sInvestorName+" "+sInvestorMiddleName;
						}
						else if (mapActualGeneralDetailsTabData.get("MiddleName") != null) {
							sInvestorMiddleName = mapActualGeneralDetailsTabData.get("MiddleName");
							sInvestorName = sInvestorName+" "+sInvestorMiddleName;
						}
						if (mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify") && mapModifiedGeneralDetailsTabData.get("LastName") != null) {
							sInvestorLastName = mapModifiedGeneralDetailsTabData.get("LastName");
							sInvestorName = sInvestorName+" "+sInvestorLastName;
						}
						else if (mapActualGeneralDetailsTabData.get("LastName") != null) {
							sInvestorLastName = mapActualGeneralDetailsTabData.get("LastName");
							sInvestorName = sInvestorName+" "+sInvestorLastName;
						}
						sInvestorName = sInvestorName.trim();
					}
				}
				if(sInvestorName.equalsIgnoreCase("") || sInvestorName.equalsIgnoreCase(" ")) {
					Reporting.logResults("Fail", "[ TEST DATA ISSUE : Test Data wasn't given properly InvestorName required for search and Activate.]", "[ TEST DATA ISSUE : Test Data wasn't given properly InvestorName required for search and Activate.]");
					continue;
				}
				Map<String, String> mapModifiedCurGeneralDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sInvestorModifyTestDataFilePath, "ModifyGeneralDetails", mapSearchInvestorDetails.get("TCID"));
				mapSearchInvestorDetails.put("CheckerOperations", mapModifiedCurGeneralDetailsTabData.get("CheckerOperations"));
				//navigate to Client Module
				bStatus=NewUICommonFunctions.selectMenu("Dashboard", "None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Investor Under Investor Setup", "Investor Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Investor Under Investor Setup", "Investor Menu selected succesfully");

				Global.driver.navigate().refresh();

				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sInvestorName, mapSearchInvestorDetails.get("CheckerOperations"));
				if(!bStatus){
					Reporting.logResults("Fail","Checker : Validate Investor Activate request has been : "+mapSearchInvestorDetails.get("OperationType"), "Checker : Investor Activate request has NOT been : "+mapSearchInvestorDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass","Checker : Validate Investor Activate request has been : "+mapSearchInvestorDetails.get("OperationType"), "Checker : Investor Activate request has been : "+mapSearchInvestorDetails.get("CheckerOperations")+" Successfully.");
				if(mapSearchInvestorDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){
					innerMap.put("Status", "active");
					innerMap.put("Investor Name", sInvestorName);
					verificationMap.put(Reporting.Testcasename,innerMap);
				}	
			}

			verifyValuesinSearchPanel(verificationMap);
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

				//Navigate to Investor Master
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup","Investor");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Investor Master","Cannot Navigate to Investor Master");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Investor Master", "Navigated to Investor Master");


				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}

				// Search the Investor in drop down
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Investor Name", test.getValue().get("Investor Name"), test.getValue().get("Status"), "Investor", time);
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Investor Name shouldnot be in active state",test.getValue().get("Investor Name")+" Investor Name is not active state");
					//continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Investor Name shouldnot be in active state",test.getValue().get("Investor Name")+" Investor Name is in active state");
				}

				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Investor Name should be in active state",test.getValue().get("Investor Name")+" Investor Name is not in active state");
					//continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Investor Name should be in active state",test.getValue().get("Investor Name")+" Investor Name is in active state");

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
