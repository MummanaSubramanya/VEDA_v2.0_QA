package com.viteos.veda.master.feederredemptiontestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.FeederRedemptionAppFunctions;
import com.viteos.veda.master.lib.FeederSubscriptionAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class FeederRedemptionMaster_MakerOperations_TS1 {
	static boolean bStatus;
	static String sSheetName = "FeederRedemptionTestData";
	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="FeederRedemption Master";
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
			Map<String, Map<String, String>> mapAllFeederREDMDetailsTestData = Utilities.readMultipleTestData(Global.sFeedeerRedemptionTestDataFilePath, sSheetName, "Y");

			for(int index = 1;index <= mapAllFeederREDMDetailsTestData.size();index++){

				Map<String, String> mapFeederREDMDetailsTestData = mapAllFeederREDMDetailsTestData.get("Row"+index);

				Reporting.Testcasename = mapFeederREDMDetailsTestData.get("TestCaseName");

				//Navigate to DashBoard

				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Feeder Redemption");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Feeder Redemption","Cannot Navigate to Feeder Redemption");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To Feeder Redemption", "Navigated to Feeder Redemption");

				//Adding new feeder Redemption.				
				bStatus =  FeederRedemptionAppFunctions.AddFeederRedemptionDetails(mapFeederREDMDetailsTestData);

				if (bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults") != null && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass") && mapFeederREDMDetailsTestData.get("OperationType") != null && mapFeederREDMDetailsTestData.get("OperationType").equalsIgnoreCase("Save")) {
					String sTransactionID = NewUICommonFunctions.getIDFromSuccessMessage();
					if (sTransactionID == null || sTransactionID.equalsIgnoreCase("")) {
						Reporting.logResults("Fail", "Verify Transaction ID generated for the Feeder Redemption addition.", "Transaction ID wasn't generated for the Feeder Redemption addition."+Messages.errorMsg);
						continue;
					}
					Map<String, String> mapXMLLibWriteDetails = new HashMap<String, String>();
					mapXMLLibWriteDetails.put("TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"));
					mapXMLLibWriteDetails.put("SearchID", sTransactionID);
					mapXMLLibWriteDetails.put("MakerStatus", "Pass");
					mapXMLLibWriteDetails.put("CheckerStatus", "None");
					Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederREDAccountXMLFilePath, "FeederREDAccount", mapFeederREDMDetailsTestData.get("TestCaseName"));
					if(mapXMLSubscriptionDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "SearchID", sTransactionID , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "MakerStatus", "Pass" , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "CheckerStatus", "None" , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");
					}else{
						XMLLibrary.writeFeederREDAccountDetailsToXML(mapXMLLibWriteDetails);
					}					
					Reporting.logResults("Pass","Perform Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Successfully Performed Maker operations.");
					continue;
				}

				if(bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Successfully Performed Maker operations.");
					continue;
				}
				if(!bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Cannot Perform Maker operations : "+Messages.errorMsg);
					Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederREDAccountXMLFilePath, "FeederREDAccount", mapFeederREDMDetailsTestData.get("TestCaseName"));
					if(mapXMLSubscriptionDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "MakerStatus", "Fail" , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "CheckerStatus", "None" , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");
					}
					continue;
				}
				if(!bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Negative testcase - Cannot Perform Maker operations : "+Messages.errorMsg);
					continue;
				}

				if(bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Able to Peformed Maker operations with negative testdata.");
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
