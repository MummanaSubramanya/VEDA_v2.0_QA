package com.viteos.veda.master.feedersubscriptiontestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.FeederSubscriptionAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class FeederSubscriptionMaster_MakerOperations_TS1 {

	static boolean bStatus;
	static String sSheetName = "FeederSubscriptionTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="FeederSubscription Master";
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
			Map<String, Map<String, String>> mapAllFeederSUBDetailsTestData = Utilities.readMultipleTestData(Global.sFeederSubscriptionTestDataFilePath, sSheetName, "Y");

			for(int index = 1;index <= mapAllFeederSUBDetailsTestData.size();index++){

				Map<String, String> mapFeederSUBDetailsTestData = mapAllFeederSUBDetailsTestData.get("Row"+index);

				Reporting.Testcasename = mapFeederSUBDetailsTestData.get("TestCaseName");

				//Navigate to DashBoard

				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Feeder Subscription");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Feeder Subscription","Cannot Navigate to Feeder Subscription");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To Feeder Subscription", "Navigated to Feeder Subscription");

				//Adding new feeder subscription.
				bStatus = FeederSubscriptionAppFunctions.AddFeederSubscriptionDetails(mapFeederSUBDetailsTestData);

				if (bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults") != null && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass") && mapFeederSUBDetailsTestData.get("OperationType") != null && mapFeederSUBDetailsTestData.get("OperationType").equalsIgnoreCase("Save")) {
					String sTransactionID = NewUICommonFunctions.getIDFromSuccessMessage();
					if (sTransactionID == null || sTransactionID.equalsIgnoreCase("")) {
						Reporting.logResults("Fail", "Verify Transaction ID generated for the Feeder Subscription addition.", "Transaction ID wasn't generated for the Feeder Subscription addition."+Messages.errorMsg);
						continue;
					}
					Map<String, String> mapXMLLibWriteDetails = new HashMap<String, String>();
					mapXMLLibWriteDetails.put("TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"));
					mapXMLLibWriteDetails.put("AccountID", FeederSubscriptionAppFunctions.sAccountNo);
					mapXMLLibWriteDetails.put("SearchID", sTransactionID);
					mapXMLLibWriteDetails.put("MakerStatus", "Pass");
					mapXMLLibWriteDetails.put("CheckerStatus", "None");
					Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", mapFeederSUBDetailsTestData.get("TestCaseName"));
					if(mapXMLSubscriptionDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "AccountID", FeederSubscriptionAppFunctions.sAccountNo, "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "SearchID", sTransactionID, "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
					}else{
						XMLLibrary.writeFeederSUBAccountDetailsToXML(mapXMLLibWriteDetails);
					}
					
				}

				if(bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){

					Reporting.logResults("Pass","Perform Maker Operation: "+mapFeederSUBDetailsTestData.get("OperationType"), "Successfully Performed Maker operations.");
					continue;
				}
				if(!bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Maker Operation: "+mapFeederSUBDetailsTestData.get("OperationType"), "Cannot Perform Maker operations : "+Messages.errorMsg);
					Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", mapFeederSUBDetailsTestData.get("TestCaseName"));
					if(mapXMLSubscriptionDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
					}
					continue;
				}
				if(!bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){

					Reporting.logResults("Pass","Perform Maker Operation: "+mapFeederSUBDetailsTestData.get("OperationType"), "Negative testcase - Cannot Perform Maker operations : "+Messages.errorMsg);
					continue;
				}

				if(bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){

					Reporting.logResults("Fail","Perform Maker Operation: "+mapFeederSUBDetailsTestData.get("OperationType"), "Able to Peformed Maker operations with negative testdata.");
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
