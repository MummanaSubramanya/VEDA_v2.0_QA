package com.viteos.veda.master.investoraccountmastertestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.InvestorAccountMasterAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class InvestorAccountMaster_MakerOperations_TC1 {
	static boolean bStatus;
	static String sSheetName = "AccountTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="InvestorAccount Master";
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
	public static void testMaker(){
		try{
			String investorAccountId = "";
			Map<String, Map<String, String>> mapAllInvestorAccountDetails = Utilities.readMultipleTestData(Global.sInvestorAccountTestDataFilePath,sSheetName,"Y");
			for(int index = 1;index <= mapAllInvestorAccountDetails.size();index++){

				Map<String, String> mapInvestorAccountDetails = mapAllInvestorAccountDetails.get("Row"+index);

				Reporting.Testcasename = mapInvestorAccountDetails.get("TestcaseName");
				
				NewUICommonFunctions.refreshThePage();
				
				//Navigate to Investor Account Page
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Account");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to InvestorAccount  Mater Under Investor Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to InvestorAccount Mater Under Investor Setup", "Account Menu selected succesfully");

				
				//Click on Add New Button
				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					Reporting.logResults("Fail", "Click on Add New button to add new Investor Account", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Click on Add New button to Add  New  InvestorAccount", "Add New Button clicked succesfully");

				
				//Add Investor Account 
				
				bStatus = InvestorAccountMasterAppFunctions.addNewAccount(mapInvestorAccountDetails);
				
				if(mapInvestorAccountDetails .get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){
					
					//Export to xml file to save the ID
					if(mapInvestorAccountDetails .get("OperationType").equalsIgnoreCase("Save")){
						investorAccountId = NewUICommonFunctions.getIDFromSuccessMessage();
						mapInvestorAccountDetails .put("AccountID", investorAccountId);
						Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapInvestorAccountDetails.get("TestcaseName"));
						if(mapXMLAccountMasterDetails != null){
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sInvestorAccountXMLFilePath, "AccountID", investorAccountId, "TestcaseName", mapInvestorAccountDetails.get("TestcaseName"), "InvestorAccount");
						}else{
							XMLLibrary.writeInvestorToXML(mapInvestorAccountDetails);
						}						
					}
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapInvestorAccountDetails .get("OperationType"), "Performed maker Operation successfull for the scenario with test data " + mapInvestorAccountDetails );
				}

				if(mapInvestorAccountDetails .get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+mapInvestorAccountDetails .get("OperationType"), "Cannot perform maker Operations. Failed for the scenario with test data "     + mapInvestorAccountDetails  +  " ERROR : "   + Messages.errorMsg);
					continue;
				}

				if(mapInvestorAccountDetails .get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapInvestorAccountDetails .get("OperationType"), "Negative Testcase - Cannot perform maker operations for the scenario with test data " + mapInvestorAccountDetails  + " ERROR : "   + Messages.errorMsg);
				}

				if(mapInvestorAccountDetails .get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+mapInvestorAccountDetails .get("OperationType"), "performed maker operations for the scenario with Negative test data " + mapInvestorAccountDetails );
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


