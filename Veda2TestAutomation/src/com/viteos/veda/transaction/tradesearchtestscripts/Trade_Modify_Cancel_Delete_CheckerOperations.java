package com.viteos.veda.transaction.tradesearchtestscripts;

import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeSearchAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeTransferAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class Trade_Modify_Cancel_Delete_CheckerOperations {
	static boolean bStatus;
	static String sSheetName = "TradeCancelDeleteModifyTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Trade Cancel Delete or Modify";
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

			Map<String, Map<String, String>> mapAllTradeSearchDetails = Utilities.readMultipleTestData(Global.sTradeSearchTestData, sSheetName, "Y");

			for (int index = 1; index <= mapAllTradeSearchDetails.size() ; index++) {
				Map<String, String> mapTradeSearchDetails = mapAllTradeSearchDetails.get("Row"+index);
				Reporting.Testcasename = mapTradeSearchDetails.get("TestCaseName");
				bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
				if (!bStatus) {
					Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
					continue;
				}
				String TransactionID = TradeSearchAppFunctions.getTranasactionIDFromRespectiveXMLFiles(mapTradeSearchDetails);
				String TradeType = TradeSearchAppFunctions.getMasterType(mapTradeSearchDetails.get("TradeType"));
				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("OperationOnSearchedTrade") != null) {
					bStatus=NewUICommonFunctions.performOperationsOnTableWithTransactionIDAndMasterType(dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, TransactionID, TradeType, "");
					if (!bStatus) {								
						Reporting.logResults("Fail", "Get the created record from dashboard to verify and perform checker operations.", "Unable to Get the created record from dashboard to verify and perform checker operations."+Messages.errorMsg);
						continue;
					}
					bStatus = TradeSearchAppFunctions.performCheckerOperationsOnSerachedTrade(mapTradeSearchDetails, TransactionID);
					if (!bStatus) {
						Reporting.logResults("Fail", "Checker Operations on Searched Trade", "Unable to perform checker operations on Searched Trade with TransactionID:"+TransactionID+"\n"+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass", "Checker Operations on Searched Trade", "Successfully performed the Checker Operations on Searched Trade for the Transaction:"+TransactionID);
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
