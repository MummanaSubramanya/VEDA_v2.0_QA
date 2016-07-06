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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class InvestorMaster_CheckerOperations_TS2 {
	static boolean bStatus;
	static String sRefrenceSheetName = "GeneralDetails";

	@BeforeMethod
	public static void setup(){
		
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
	public static void addNewLegalEntity(){
		try {
			Map<String,Map<String,String>> mapAllInvestorsDetails = Utilities.readMultipleTestData(Global.sInvestorTestDataFilePath, sRefrenceSheetName, "Y");
			Map<String, Map<String, String>> verifyIncentiveFee = new HashMap<>();
			for(int index = 1;index <= mapAllInvestorsDetails.size();index++){				
				Map<String, String> innerMap = new HashMap<String, String>();
				Map<String, String> mapInvestorGeneralDetails = mapAllInvestorsDetails.get("Row"+index);
				if (mapInvestorGeneralDetails.get("CheckerOperations") == null || mapInvestorGeneralDetails.get("OperationType") == null || mapInvestorGeneralDetails.get("OperationType").equalsIgnoreCase("Cancel")) {
					continue;
				}
				String sInvestorFirstName = "";
				String sInvestorLastName = "";
				String sInvestorMiddleName = "";
				String sInvestorName = "";			
				
				Reporting.Testcasename = mapInvestorGeneralDetails.get("TestcaseName");						
				
				if (mapInvestorGeneralDetails.get("InvestorType") != null) {
					if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity") && mapInvestorGeneralDetails.get("RegistrationName") != null) {
						sInvestorName = mapInvestorGeneralDetails.get("RegistrationName");
					}	
					if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Individual")) {
						if (mapInvestorGeneralDetails.get("FirstName") != null) {
							sInvestorFirstName = mapInvestorGeneralDetails.get("FirstName");
							sInvestorName = sInvestorFirstName;
						}
						if (mapInvestorGeneralDetails.get("MiddleName") != null) {
							sInvestorMiddleName = mapInvestorGeneralDetails.get("MiddleName");
							sInvestorName = sInvestorName+" "+sInvestorMiddleName;
						}
						if (mapInvestorGeneralDetails.get("LastName") != null) {
							sInvestorLastName = mapInvestorGeneralDetails.get("LastName");
							sInvestorName = sInvestorName+" "+sInvestorLastName;
						}
						sInvestorName = sInvestorName.trim();
					}
				}
				
				NewUICommonFunctions.refreshThePage();
				bStatus=NewUICommonFunctions.selectMenu("Dashboard", "None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Dashboard", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Dashboard", "Dashboard selected succesfully");
				
				bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sInvestorName, mapInvestorGeneralDetails.get("CheckerOperations"));
				
				if(bStatus && mapInvestorGeneralDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapInvestorGeneralDetails.get("CheckerOperations"));
				}

				if(!bStatus && mapInvestorGeneralDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapInvestorGeneralDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && mapInvestorGeneralDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations : "+mapInvestorGeneralDetails.get("CheckerOperations")+".");
				}

				if(bStatus && mapInvestorGeneralDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Error: "+Messages.errorMsg+ " : "+mapInvestorGeneralDetails);
					continue;
				}	

				if(mapInvestorGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("InvestorName", sInvestorName);
					verifyIncentiveFee.put(Reporting.Testcasename,innerMap);
				}
				if(mapInvestorGeneralDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){				
					innerMap.put("Status", "inactive");
					innerMap.put("InvestorName", sInvestorName);
					verifyIncentiveFee.put(Reporting.Testcasename,innerMap);
				}			
			}
			//Verify Checker Operations are taken place.
			verifyValuesinSearchPanel(verifyIncentiveFee);		
		} catch (Exception e) {
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
	
	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyIncentiveFee) {
		try{
			for (Entry<String, Map<String, String>> test : verifyIncentiveFee.entrySet()) {				

				Reporting.Testcasename = test.getKey();

				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Investor");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Investor","Cannot Navigate to Investor Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Investor", "Navigated to Investor page");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				//search function for Incentive Fee.
				bStatus = NewUICommonFunctions.verifyRecordIsDisplayedInTheGridTable("Investor Name", test.getValue().get("InvestorName"), test.getValue().get("Status"), "Investor", time);			
				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Checker : Verify Investor should Not be Active in Search Grid.",test.getValue().get("InvestorName")+" : Investor is Not-Active in Search Grid.");
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Checker : Verify Investor should be Inactive in Search Grid.",test.getValue().get("InvestorName")+" : Investor is Active in Search Grid.");
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Checker : Verify Investor should be Active in Search Grid.",test.getValue().get("InvestorName")+" : Investor is Not-Active in Search Grid.");
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Checker : Verify Investor should be Active in Search Grid.",test.getValue().get("InvestorName")+" : Investor is Active in Search Grid.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
