package com.viteos.veda.master.openingbalancetestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.OpeningBalanceMasterAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class OpeningBalanceMaster_MakerOperations_TS1 {
	static boolean bStatus;
	static String sSheetName = "OpeningBalanceTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Opening Balance Master Setup";
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
	public static void testMakerOperations(){
		try{
			Map<String, Map<String, String>> mapAllOpeningBalDetailsTestData = Utilities.readMultipleTestData(Global.sOpeningBalanceTestDataFilePath, sSheetName, "Y");			
			
			for(int index = 1;index <= mapAllOpeningBalDetailsTestData.size();index++){

				Map<String, String> mapOpeningBalanceDetailsTestData = mapAllOpeningBalDetailsTestData.get("Row"+index);

				Reporting.Testcasename = mapOpeningBalanceDetailsTestData.get("TestCaseName");

				//Navigate to DashBoard

				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Opening Balance");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Opening Balance","Cannot Navigate to Opening Balance");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To Opening Balance", "Navigated to Opening Balance");

				//Adding new feeder subscription.
				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);
				if (!bStatus) {
					Reporting.logResults("Fail", "Click on Add New button of Opening Balance Master.", "Failed to perform operation Add New :"+Messages.errorMsg);
					continue;
				}
				bStatus = OpeningBalanceMasterAppFunctions.doAddOpeningBalanceDetails(mapOpeningBalanceDetailsTestData);

				if (bStatus && mapOpeningBalanceDetailsTestData.get("ExpectedResults") != null && mapOpeningBalanceDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass") && mapOpeningBalanceDetailsTestData.get("OperationType") != null && mapOpeningBalanceDetailsTestData.get("OperationType").equalsIgnoreCase("Save")) {
					String sTransactionID = NewUICommonFunctions.getIDFromSuccessMessage();
					if (sTransactionID == null || sTransactionID.equalsIgnoreCase("")) {
						Reporting.logResults("Fail", "Verify Transaction ID generated for the Opening Balance addition.", "Transaction ID wasn't generated for the Opening Balance addition."+Messages.errorMsg);
						continue;
					}
					Map<String, String> mapXMLLibWriteDetails = new HashMap<String, String>();
					mapXMLLibWriteDetails.put("TestcaseName", mapOpeningBalanceDetailsTestData.get("TestCaseName"));
					mapXMLLibWriteDetails.put("SearchID", sTransactionID);
					mapXMLLibWriteDetails.put("MakerStatus", "Pass");
					mapXMLLibWriteDetails.put("CheckerStatus", "None");
					Map<String, String> mapXMLOpeningBalanceDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "OpeningBalance", mapOpeningBalanceDetailsTestData.get("TestCaseName"));
					if(mapXMLOpeningBalanceDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "SearchID", sTransactionID, "TestcaseName", mapOpeningBalanceDetailsTestData.get("TestCaseName"), "OpeningBalance");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapOpeningBalanceDetailsTestData.get("TestCaseName"), "OpeningBalance");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapOpeningBalanceDetailsTestData.get("TestCaseName"), "OpeningBalance");
					}else{
						XMLLibrary.writeOpeningBalanceDetailsToXML(mapXMLLibWriteDetails);
					}					
				}

				if(bStatus && mapOpeningBalanceDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){

					Reporting.logResults("Pass","Perform Maker Operation: "+mapOpeningBalanceDetailsTestData.get("OperationType"), "Successfully Performed Maker operations.");
					continue;
				}
				if(!bStatus && mapOpeningBalanceDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){					
					Reporting.logResults("Fail","Perform Maker Operation: "+mapOpeningBalanceDetailsTestData.get("OperationType"), "Cannot Perform Maker operations : "+Messages.errorMsg);
					Map<String, String> mapXMLOpeningBalanceDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "OpeningBalance", mapOpeningBalanceDetailsTestData.get("TestCaseName"));
					if(mapXMLOpeningBalanceDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapOpeningBalanceDetailsTestData.get("TestCaseName"), "OpeningBalance");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapOpeningBalanceDetailsTestData.get("TestCaseName"), "OpeningBalance");
					}
					NewUICommonFunctions.handleAlert();
					continue;
				}
				if(!bStatus && mapOpeningBalanceDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){

					Reporting.logResults("Pass","Perform Maker Operation: "+mapOpeningBalanceDetailsTestData.get("OperationType"), "Negative testcase - Cannot Perform Maker operations : "+Messages.errorMsg);
					NewUICommonFunctions.handleAlert();
					continue;
				}

				if(bStatus && mapOpeningBalanceDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){

					Reporting.logResults("Fail","Perform Maker Operation: "+mapOpeningBalanceDetailsTestData.get("OperationType"), "Able to Peformed Maker operations with negative testdata.");
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
