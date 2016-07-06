package com.viteos.veda.master.investortestscripts;

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

public class InvestorMaster_ActivateMakerOperations_TS7 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Investor Master";
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
			Map<String, Map<String, String>> mapAllInvestorGeneralDetails = Utilities.readMultipleTestData(Global.sInvestorModifyTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllInvestorGeneralDetails.size();index++){
				Map<String, String> mapSearchInvestorDetails = mapAllInvestorGeneralDetails.get("Row"+index);
				String sInvestorFirstName = "";
				String sInvestorMiddleName = "";
				String sInvestorLastName = "";
				String sInvestorName = ""; 

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

				//navigate to Client Module
				bStatus=NewUICommonFunctions.selectMenu("Investor Setup", "Investor");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Investor Under Investor Setup", "Investor Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Investor Under Investor Setup", "Investor Menu selected succesfully");

				Global.driver.navigate().refresh();

				if(mapSearchInvestorDetails.get("Operation").equalsIgnoreCase("Activate")){
					bStatus = NewUICommonFunctions.activateMaster("Investor Name", sInvestorName, "Investor");
					if(!bStatus){
						Reporting.logResults("Fail","Maker : Validate Investor Activate request has been raised.", "Maker : Investor Activate request has NOT been raised. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Maker : Validate Investor Activate request has been raised.", "Maker : Investor Activate request has been raised Successfully.");
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
		NewUICommonFunctions.refreshThePage();
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
