package com.viteos.veda.transaction.switchtestscripts;

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
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class CheckerReviewedOrReturnedMakerOperation_TC3 {
	static boolean bStatus;
	static String sSheetName = "SwitchTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Switch";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SwitchMakerUserName"), Global.mapCredentials.get("SwitchMakerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Maker Login into application", "Maker Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Maker Login into application ", "Maker Login into application is successfull");
	}


	@Test
	public static void vefifyCheckerOperations(){
		Map<String, Map<String, String>> mapAllSwitchDetails = Utilities.readMultipleTestData(Global.sSwitchTestData,sSheetName,"Y");

		for (int index = 1; index <= mapAllSwitchDetails.size() ; index++){
			Map<String, String> mapSwitchDetails = mapAllSwitchDetails.get("Row"+index);
			Reporting.Testcasename = mapSwitchDetails.get("TestCaseName");

			Map<String ,String> mapXMLSwitchDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TradeTypeSWITCH", mapSwitchDetails.get("TestCaseName"));
			if(mapXMLSwitchDetails == null || !mapXMLSwitchDetails.get("CheckerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			if(mapXMLSwitchDetails.get("TransactionID")!=null){
				mapSwitchDetails.put("Transaction ID", mapXMLSwitchDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}
			String makerStatus = "";
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				makerStatus = "Fail";
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "MakerStatus", makerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard", "Successfully Navigate to Dashboard");

			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Switch",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapSwitchDetails.get("Transaction ID"), mapSwitchDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Switch", "Failed to Select the Created Switch. "+Messages.errorMsg);
				makerStatus = "Fail";
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "MakerStatus", makerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
				continue;
			}

			bStatus = TradeTypeSwitchAppFunctions.doFillCheckerReviewedOrReturnedMakerFunctions(mapSwitchDetails);
			
			if(bStatus && mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				
				if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("Save") || mapSwitchDetails.get("OperationType").equalsIgnoreCase("Send For Review") ){					
					if(mapSwitchDetails.get("NewAccount")!=null && mapSwitchDetails.get("NewAccount").equalsIgnoreCase("Yes") && !TradeTypeSwitchAppFunctions.newAccountID.equalsIgnoreCase("")){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "AccountID", TradeTypeSwitchAppFunctions.newAccountID, "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
					}
					makerStatus = "Pass";
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "MakerStatus", makerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
					Reporting.logResults("Pass", "Perform Switch Maker Operation :'"+mapSwitchDetails.get("OperationType")+"'", "Performed Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"' Successfully with Transaction ID : "+mapXMLSwitchDetails.get("TransactionID"));
					continue;
				}
				if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapSwitchDetails.get("OperationType").equalsIgnoreCase("Clear") ){
					Reporting.logResults("Pass", "Perform Switch Maker Operation: "+mapSwitchDetails.get("OperationType")+"", "Performed Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"' Successfully ");
					continue;
				}				
			}		
			if(bStatus && mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){	
				
				Reporting.logResults("Fail", "Perform Switch Maker Operation :'"+mapSwitchDetails.get("OperationType")+"'", "Performed Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"' with Negative Test Data");
				continue;				
			}			
			if(!bStatus && mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				
				Reporting.logResults("Fail", "Perform Switch Maker Operation :'"+mapSwitchDetails.get("OperationType")+"'", "Failed to Perform Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
				makerStatus = "Fail";
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "MakerStatus", makerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
				NewUICommonFunctions.handleAlert();
				continue;
			}			
			if(!bStatus && mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){	
				
				Reporting.logResults("Pass", "Perform Switch Maker Operation :'"+mapSwitchDetails.get("OperationType")+"'", "Negative Test Data Cannot Perform Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"' .ERROR : "+Messages.errorMsg);
				NewUICommonFunctions.handleAlert();
				continue;				
			}			
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
