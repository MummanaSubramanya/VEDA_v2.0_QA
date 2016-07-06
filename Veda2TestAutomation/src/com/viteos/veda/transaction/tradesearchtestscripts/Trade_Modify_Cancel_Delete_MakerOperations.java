package com.viteos.veda.transaction.tradesearchtestscripts;

import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.FeederSubscriptionAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeSearchAppFunctions;
import com.viteos.veda.master.lib.TradeTypeRedemptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class Trade_Modify_Cancel_Delete_MakerOperations {
	static boolean bStatus;
	static String sSheetName = "TradeCancelDeleteModifyTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Trade Cancel Delete or Modify";
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
	public static void testCheckerOperations(){
		try{

			Map<String, Map<String, String>> mapAllTradeSearchDetails = Utilities.readMultipleTestData(Global.sTradeSearchTestData, sSheetName, "Y");

			for (int index = 1; index <= mapAllTradeSearchDetails.size() ; index++) {
				Map<String, String> mapTradeSearchDetails = mapAllTradeSearchDetails.get("Row"+index);
				Reporting.Testcasename = mapTradeSearchDetails.get("TestCaseName");
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Trade Search");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Trade Search","Cannot Navigate to Trade Search Window");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To Trade Search", "Navigated to Trade Search Window");

				bStatus = NewUICommonFunctions.jsClick(By.xpath("//div//button//em[contains(@class, 'fa fa-search')]")); //Search button clicking
				if (!bStatus) {
					Reporting.logResults("Fail", "Clicking on Search Button in Trade Search", "Unable to click on Search Button in Trade Search");
				}

				String TransactionID = TradeSearchAppFunctions.getTranasactionIDFromRespectiveXMLFiles(mapTradeSearchDetails);
				String TradeType = TradeSearchAppFunctions.getMasterType(mapTradeSearchDetails.get("TradeType"));

				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("OperationOnSearchedTrade") != null) {

					if(mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Feeder Subscription") || mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Feeder Redemption"))
					{
						NewUICommonFunctions.scrollToView(By.xpath("//div[@id='filterrow.jqxgridSearchForAllFeederTrades']/div/div[1]//input[@type='textarea']"));
						bStatus = TradeSearchAppFunctions.performOperationsOnTableWithTransactionIDandMasterTypeForFeeder(TransactionID, TradeType);
						if (!bStatus) {								
							Reporting.logResults("Fail", "Search record for Trade Delete or Cancel or Modify in Trade Search", "Unable to Get the record "+Messages.errorMsg);
							continue;	
						}
					}
					else
					{
						bStatus = TradeSearchAppFunctions.performOperationsOnTableWithTransactionIDandMasterType(TransactionID, TradeType);					
						if (!bStatus) {								
							Reporting.logResults("Fail", "Search record for Trade Delete or Cancel or Modify in Trade Search", "Unable to Get the record "+Messages.errorMsg);
							continue;	
						}

					}
				}

				bStatus = TradeSearchAppFunctions.performOperationsOnSerachedTrade(mapTradeSearchDetails, TransactionID);
				if (!bStatus) {
					Reporting.logResults("Fail", "Operations On Trade Search", "Unable to Perform"+mapTradeSearchDetails.get("OperationOnSearchedTrade")+ "Operations on the Trade Type"+mapTradeSearchDetails.get("TradeType")+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Operations On Trade Search", "Successful to Perform "+mapTradeSearchDetails.get("OperationOnSearchedTrade")+ "Operations on the Trade Type"+mapTradeSearchDetails.get("TradeType"));
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
