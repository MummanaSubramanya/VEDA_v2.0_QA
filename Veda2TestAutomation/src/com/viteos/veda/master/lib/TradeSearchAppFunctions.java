package com.viteos.veda.master.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.functors.MapTransformer;
import org.jboss.netty.util.Timeout;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class TradeSearchAppFunctions {
	static boolean bStatus;
	public static String sTradeID = "";
	public static String sTradeType = "";

	/*************************************** Integrating functions to perform trade search operations. ********************/
	public static boolean doTriggerIntegratedTradeSearchOperations(Map<String,String >mapTradeSearchDetails){
		try {
			bStatus = searchTrades(mapTradeSearchDetails);
			if (!bStatus) {
				return false;
			}
			/*bStatus = performOperationsOnSerachedTrade(mapTradeSearchDetails);
			if (!bStatus) {
				return false;
			}*/
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*************************************** Function to Apply Search Criteria. *******************************************/
	public static boolean searchTrades(Map<String,String >mapTradeSearch){
		try{
			bStatus= Wait.waitForElementVisibility(Global.driver, By.xpath("//label[text()='Client Name']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg ="[ Error : Trade Search panel is Not visible .]\n";
				return false;
			}

			bStatus = Elements.click(Global.driver, By.xpath("//a[contains(@onclick,'showAdvanceSearch')]"));
			if(!bStatus){
				Messages.appErrorMsg = "[ Error : Not clicked on Advanced Search Link .]\n";
				return false;
			}
			TimeUnit.SECONDS.sleep(2);
			//Select Client from drop down
			if(mapTradeSearch.get("Client Name") != null){
				bStatus= NewUICommonFunctions.selectFromDropDownOfApplication(mapTradeSearch.get("Client Name"), By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg ="[ Error : "+ mapTradeSearch.get("Client Name")+" is not available in the dropdown ]\n";
					return false;
				}
			}
			//Select Fund Family from drop down
			if(mapTradeSearch.get("Fund Family Name") != null){
				bStatus= NewUICommonFunctions.selectFromDropDownOfApplication(mapTradeSearch.get("Fund Family Name"), By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : "+mapTradeSearch.get("Fund Family Name")+" is not available in the dropdown ]\n";
					return false;
				}
			}
			//Select Legal Entity Name from drop down
			if(mapTradeSearch.get("Legal Entity Name") != null){
				bStatus= NewUICommonFunctions.selectFromDropDownOfApplication(mapTradeSearch.get("Legal Entity Name"), By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : "+ mapTradeSearch.get("Legal Entity Name")+" is not available in the dropdown ]\n";
					return false;
				}
			}
			//Select Class Name from drop down
			if(mapTradeSearch.get("Class Name") != null){
				bStatus= NewUICommonFunctions.selectFromDropDownOfApplication(mapTradeSearch.get("Class Name"), By.xpath("//div[@id='s2id_cmbClassName']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : "+ mapTradeSearch.get("Class Name")+" is not available in the dropdown ]\n";
					return false;
				}
			}			
			//Enter  Order Id 
			if(mapTradeSearch.get("Order Id") != null){
				String transactionID = TradeSearchAppFunctions.getTranasactionIDFromRespectiveXMLFiles(mapTradeSearch);
				if (transactionID == null || transactionID.equalsIgnoreCase("") ) {
					Messages.errorMsg ="[ Error : Tranasaction Id  contains : '"+transactionID+"' , Vaule in XML file, and respective Trade may failed with the the given test case ID : '"+mapTradeSearch.get("Order Id")+"' ,please cross check.]\n";
					return false;
				}
				sTradeID = transactionID;
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@id='orderSysId']"), transactionID);
				if(!bStatus){
					Messages.errorMsg = "[ Error :  Unable to input Order Id : '"+transactionID+"' into the field.]\n";
					return false;
				}
			}
			//Enter Investor
			if(mapTradeSearch.get("Investor") != null){
				bStatus= NewUICommonFunctions.selectFromDropDownOfApplication(mapTradeSearch.get("Investor"), By.xpath("//div[@id='s2id_investor.investorIdPk']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : Investor Name : '"+mapTradeSearch.get("Investor")+"' wasn't able to select.]\n";
					return false;
				}
			}
			//Select Subscription Check box
			if(mapTradeSearch.get("Subscription") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@class='checked']//parent::div[@id='uniform-chk_sub']"));
				if((mapTradeSearch.get("Subscription").equalsIgnoreCase("Yes") && !bStatus) || (mapTradeSearch.get("Subscription").equalsIgnoreCase("No") && bStatus)){
					bStatus = Elements.click(Global.driver,By.xpath("//div[@id='uniform-chk_sub']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Unable to 'Check' / 'Un-Check' Subscription : '"+mapTradeSearch.get("Subscription")+"' check box in Advanced Search.]\n";
						return false;
					}
					sTradeType = "Subscription";
				}
			}
			//Select Redemption check box
			if(mapTradeSearch.get("Redemption") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@class='checked']//parent::div[@id='uniform-chk_redem']"));
				if((mapTradeSearch.get("Redemption").equalsIgnoreCase("Yes") && !bStatus) || (mapTradeSearch.get("Redemption").equalsIgnoreCase("No") && bStatus)){
					bStatus = Elements.click(Global.driver,By.xpath("//div[@id='uniform-chk_redem']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Unable to 'Check' / 'Un-Check' Redemption : '"+mapTradeSearch.get("Redemption")+"' check box in Advanced Search.]\n";
						return false;
					}
					sTradeType = "Redemption";
				}
			}
			//Select Exchange check box
			if(mapTradeSearch.get("Exchange") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@class='checked']//parent::div[@id='uniform-chk_exch']"));
				if((mapTradeSearch.get("Exchange").equalsIgnoreCase("Yes") && !bStatus) || (mapTradeSearch.get("Exchange").equalsIgnoreCase("No") && bStatus)){
					bStatus = Elements.click(Global.driver,By.xpath("//div[@id='uniform-chk_exch']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Unable to 'Check' / 'Un-Check' Exchange : '"+mapTradeSearch.get("Exchange")+"' check box in Advanced Search.]\n";
						return false;
					}
					sTradeType = "Exchange";
				}
			}
			//Select Switch check box
			if(mapTradeSearch.get("Switch") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@class='checked']//parent::div[@id='uniform-chk_swt']"));
				if((mapTradeSearch.get("Switch").equalsIgnoreCase("Yes") && !bStatus) || (mapTradeSearch.get("Switch").equalsIgnoreCase("No") && bStatus)){
					bStatus = Elements.click(Global.driver,By.xpath("//div[@id='uniform-chk_swt']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Unable to 'Check' / 'Un-Check' Switch : '"+mapTradeSearch.get("Switch")+"' check box in Advanced Search.]\n";
						return false;
					}
					sTradeType = "Switch";
				}
			}
			//Select Transfer check box
			if(mapTradeSearch.get("Transfer") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@class='checked']//parent::div[@id='uniform-chk_trn']"));
				if((mapTradeSearch.get("Transfer").equalsIgnoreCase("Yes") && !bStatus) || (mapTradeSearch.get("Transfer").equalsIgnoreCase("No") && bStatus)){
					bStatus = Elements.click(Global.driver,By.xpath("//div[@id='uniform-chk_trn']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Unable to 'Check' / 'Un-Check' Transfer : '"+mapTradeSearch.get("Transfer")+"' check box in Advanced Search.]\n";
						return false;
					}
					sTradeType = "Transfer";
				}
			}
			//Select Feeder Subscription
			if(mapTradeSearch.get("Feeder Subscription") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@class='checked']//parent::div[@id='uniform-chk_fdSub']"));
				if((mapTradeSearch.get("Feeder Subscription").equalsIgnoreCase("Yes") && !bStatus) || (mapTradeSearch.get("Feeder Subscription").equalsIgnoreCase("No") && bStatus)){
					bStatus = Elements.click(Global.driver,By.xpath("//div[@id='uniform-chk_fdSub']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Unable to 'Check' / 'Un-Check' Feeder Subscription : '"+mapTradeSearch.get("Feeder Subscription")+"' check box in Advanced Search.]\n";
						return false;
					}
					sTradeType = "Feeder Subscription";
				}
			}
			//Select Feeder Redemption
			if(mapTradeSearch.get("Feeder Redemption") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@class='checked']//parent::div[@id='uniform-chk_fdRedem']"));
				if((mapTradeSearch.get("Feeder Redemption").equalsIgnoreCase("Yes") && !bStatus) || (mapTradeSearch.get("Feeder Redemption").equalsIgnoreCase("No") && bStatus)){
					bStatus = Elements.click(Global.driver,By.xpath("//div[@id='uniform-chk_fdRedem']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Unable to 'Check' / 'Un-Check' Feeder Redemption : '"+mapTradeSearch.get("Feeder Redemption")+"' check box in Advanced Search.]\n";
						return false;
					}
					sTradeType = "Feeder Redemption";
				}
			}
			//Enter From Trade Date
			if(mapTradeSearch.get("FromTradeDate")!=null){
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@id='fromDateRange']"), mapTradeSearch.get("FromTradeDate"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : From Trade Date  was Not Enterd ]\n";
					return false;
				}
			}
			//Enter From Trade Date
			if(mapTradeSearch.get("ToTradeDate")!=null){
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@id='toDateRange']"), mapTradeSearch.get("ToTradeDate"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : To Trade Date  was Not Enterd ]\n";
					return false;
				}
			}
			//Enter From Amount
			if(mapTradeSearch.get("FromAmount")!=null){
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@id='fromAmount']"), mapTradeSearch.get("FromAmount"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : From Amount  was Not Enterd ]\n";
					return false;
				}
			}
			//Enter To Amount
			if(mapTradeSearch.get("ToAmount")!=null){
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@id='toAmount']"), mapTradeSearch.get("ToAmount"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : To Amount  was Not Enterd ]\n";
					return false;
				}
			}
			//click on Sub operation button
			if(mapTradeSearch.get("SubOperation")!=null){
				if(mapTradeSearch.get("SubOperation").equalsIgnoreCase("Search")){
					bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[normalize-space()='Search']"));
					if(!bStatus){
						Messages.errorMsg ="[ Error : Search button is not clicked ]\n";
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//div[@id='contentjqxgridSearchForAllTrades']/div[1]"),Constants.iSearchTableTimeout);
					if(!bStatus){
						bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//div[@id='contenttablejqxgridSearchForAllTrades']"),Constants.iSearchTableTimeout);
						if(!bStatus){
							Messages.errorMsg = "[ Error : Table with search data wasn't displayed ]\n";
							return false;
						}
					}
				}
				if(mapTradeSearch.get("SubOperation").equalsIgnoreCase("Clear")){
					bStatus=Elements.click(Global.driver, By.xpath("//button[normalize-space()='Clear']"));
					if(!bStatus){
						Messages.errorMsg ="[ Error : Clear button is not clicked ]\n";
						return false;
					}
				}
				if(mapTradeSearch.get("SubOperation").equalsIgnoreCase("AddNew")){
					bStatus=Elements.click(Global.driver, By.xpath("//a[normalize-space()='Add New']"));
					if(!bStatus){
						Messages.errorMsg ="[ Error : Add New button is not clicked ]\n";
						return false;
					}
				}
			}
			return true;	
		}
		catch(Exception e){
			return false;
		}
	}

	//*************************************** Read Order ID for respective trade and parse it to input in search criteria. *
	public static String getTranasactionIDFromRespectiveXMLFiles(Map<String,String > mapTradeSearch){
		try {
			String sTradeTypeXMLFileName = "";
			String sTradeTypeName = "";
			String sOrderID = "";
			String sTransID="";

			//XMLLibrary. sTradeTypeREDDetailsXMLFilePath = "XMLMessages//TradeSearch.xml";

			if(mapTradeSearch.get("Order Id") != null){
				sOrderID = mapTradeSearch.get("Order Id");
				if(mapTradeSearch.get("TradeType") != null && mapTradeSearch.get("TradeType").equalsIgnoreCase("Subscription") ){
					sTradeTypeXMLFileName = XMLLibrary.sTradeTypeSUBXMLFilePath;
					sTradeTypeName = "TradeTypeSUB";
				}
				if(mapTradeSearch.get("TradeType") != null && mapTradeSearch.get("TradeType").equalsIgnoreCase("Redemption") ){
					sTradeTypeXMLFileName = XMLLibrary.sTradeTypeREDDetailsXMLFilePath;
					sTradeTypeName = "TradeTypeRED";
				}
				if(mapTradeSearch.get("TradeType") != null && mapTradeSearch.get("TradeType").equalsIgnoreCase("Transfer") ){
					sTradeTypeXMLFileName = XMLLibrary.sTradeTypeTRANDetailsXMLFilePath;;
					sTradeTypeName = "TradeTypeTRAN";
				}
				if(mapTradeSearch.get("TradeType") != null && mapTradeSearch.get("TradeType").equalsIgnoreCase("Exchange") ){
					sTradeTypeXMLFileName = XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath;;
					sTradeTypeName = "TradeTypeEXCN";
				}
				if(mapTradeSearch.get("TradeType") != null && mapTradeSearch.get("TradeType").equalsIgnoreCase("Switch") ){
					sTradeTypeXMLFileName = XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath;;
					sTradeTypeName = "TradeTypeSWITCH";
				}
				if(mapTradeSearch.get("TradeType") != null && mapTradeSearch.get("TradeType").equalsIgnoreCase("Feeder Subscription") ){
					sTradeTypeXMLFileName = XMLLibrary.sFeederSUBAccountXMLFilePath;
					sTradeTypeName = "FeederSUBAccount";
				}
				if(mapTradeSearch.get("TradeType") != null && mapTradeSearch.get("TradeType").equalsIgnoreCase("Feeder Redemption") ){
					sTradeTypeXMLFileName = XMLLibrary.sFeederREDAccountXMLFilePath;
					sTradeTypeName = "FeederREDAccount";
				}

				Map<String ,String> mapXMLTradeDetails = XMLLibrary.getCreatedMasterDataFromXML(sTradeTypeXMLFileName, sTradeTypeName, sOrderID);
				if (mapXMLTradeDetails != null && !mapXMLTradeDetails.isEmpty() && mapXMLTradeDetails.get("TestcaseName") != null) {
					sTransID = mapXMLTradeDetails.get("TransactionID");
					if(sTransID == null){
						sTransID = mapXMLTradeDetails.get("SearchID");
					}
				}
				else {
					return null;
				}
			}
			return sTransID;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	//**************************************** Get Master Type *********************************************************
	public static String getMasterType(String TradeType)
	{
		String MasterType = "";
		try {
			switch (TradeType){

			case "Subscription":
				MasterType = "Transaction_SUB";
				break;
			case "Transfer":
				MasterType = "Transaction_TRA";
				break;
			case "Exchange":
				MasterType = "Transaction_EXC";
				break;
			case "Redemption":
				MasterType = "Transaction_RED"; 
				break;
			case "Switch":
				MasterType = "Transaction_SWI";
				break;
			case "Feeder Subscription":
				MasterType = "Feeder SUB";
				break;
			case "Feeder Redemption":
				MasterType = "Feeder RED";
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return MasterType;

	}

	//*************************************** Functions to Trade search edit and Modify ************************************
	public static boolean doModifyTheTradeTypeEntry(Map<String, String> mapTradeSearchDetails)
	{
		try {			
			if (mapTradeSearchDetails != null && !mapTradeSearchDetails.isEmpty()) {
				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Subscription")) {
					/*bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(@href,'Transaction_SUB') and contains(@href,'modifyTrade') and contains(@href,'"+sTradeID+"')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[ Error: unable to click on the Edit button for Searched Subscription Trade with Txn ID : '"+sTradeID+"']";
						return false;
					}*/
					Map<String, String> mapSubscriptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sSubscriptionTestData, "CheckerReviewedTestData", mapTradeSearchDetails.get("Order Id"));
					TradeTypeSubscriptionAppFunctions.isInkindFromCheckerReviewedScreen = true;
					bStatus = TradeTypeSubscriptionAppFunctions.doFillSubscriptionDetails(mapSubscriptionDetails);
					if (!bStatus) {
						return false;
					}
					return true;
				}				
				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Redemption")) {					
					Map<String, String> mapRedemptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sRedemptionTestData, "TestDataForCheckerReviewd", mapTradeSearchDetails.get("Order Id"));
					TradeTypeRedemptionAppFunctions.isRedemptionForCheckerReviewedCase = true;					
					bStatus = TradeTypeRedemptionAppFunctions.doFillCheckerReviewdTransactios(mapRedemptionDetails);
					if (!bStatus) {
						return false;
					}
					return true;
				}				
				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Transfer")) {
					Map<String, String> mapTransferDetails = ExcelUtils.readDataABasedOnCellName(Global.sTransferTestData, "TestDataForCheckerReviewd", mapTradeSearchDetails.get("Order Id"));
					TradeTypeTransferAppFunctions.isTransferForCheckerReviewedScreen = true;
					bStatus = TradeTypeTransferAppFunctions.doMakerFillCheckerReviewedTransferTrade(mapTransferDetails);
					if (!bStatus) {
						return false;
					}
					return true;
				}				
				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Exchange")) {					
					Map<String, String> mapExchangeDetails = ExcelUtils.readDataABasedOnCellName(Global.sExchangeTestData, "TestDataForCheckerReviewed", mapTradeSearchDetails.get("Order Id"));
					TradeTypeExchangeAppFunction.isExchangeFromChekerReviewed = true;
					bStatus = TradeTypeExchangeAppFunction.doFillExchangeCheckerReviewedTradeDetails(mapExchangeDetails);
					if (!bStatus) {
						return false;
					}
					return true;
				}
				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Switch")) {
					Map<String, String> mapSwitchDetails = ExcelUtils.readDataABasedOnCellName(Global.sSwitchTestData, "CheckerReviewedMaker", mapTradeSearchDetails.get("Order Id"));
					bStatus = TradeTypeSwitchAppFunctions.doFillCheckerReviewedOrReturnedMakerFunctions(mapSwitchDetails);
					if (!bStatus) {
						return false;
					}
					return true;
				}
				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Feeder Subscription")) {
					Map<String, String> mapFeederSubDetails = ExcelUtils.readDataABasedOnCellName(Global.sFeederSubscriptionTestDataFilePath, "CheckerReturnedMakerData", mapTradeSearchDetails.get("Order Id"));
					bStatus = FeederSubscriptionAppFunctions.AddFeederSubscriptionDetails(mapFeederSubDetails);
					if (!bStatus) {
						return false;
					}
					return true;
				}				
				if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Feeder Redemption")) {
					Map<String, String> mapFeederRedDetails = ExcelUtils.readDataABasedOnCellName(Global.sFeedeerRedemptionTestDataFilePath, "CheckerReturnedMakerData", mapTradeSearchDetails.get("Order Id"));
					bStatus = FeederRedemptionAppFunctions.AddFeederRedemptionDetails(mapFeederRedDetails);
					if (!bStatus) {
						return false;
					}
					return true;
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/*************************************** function to performOperationsOnSerachedTrade **********************************/
	public static boolean performOperationsOnSerachedTrade(Map<String, String> mapTradeSearchDetails, String sTradeID)
	{
		try {
			if (mapTradeSearchDetails.get("OperationOnSearchedTrade") != null) {			
				if (mapTradeSearchDetails.get("OperationOnSearchedTrade").equalsIgnoreCase("Modify Trade")) {

					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[normalize-space()='"+sTradeID+"']//parent::div[@role='gridcell']//following-sibling::div//a[contains(@href,'modifyTrade')]"));
					//bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(@href,'Transaction_SUB') and contains(@href,'modifyTrade') and contains(@href,'"+sTradeID+"')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[ Erro: unable to click Modify Trade in Trade Search with TXN ID : '"+sTradeID+"'. ]\n";
						return false;
					}
					TimeUnit.SECONDS.sleep(2);
					if (mapTradeSearchDetails.get("TradeType") != null && !mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("")) {
						NewUICommonFunctions.waitUntilSpinnerGoInvisible(5, 30);
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'"+mapTradeSearchDetails.get("TradeType")+"')]"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : When Attempted to 'Modify' the trade type '"+sTradeType+"' ,with Txn ID : '"+sTradeID+"' ,the Edit page wasn't interactable/not navigated to Edit page.]\n";
							return false;
						}
						bStatus = doModifyTheTradeTypeEntry(mapTradeSearchDetails);
						if (!bStatus) {
							return false;
						}
					}
				}
				if (mapTradeSearchDetails.get("OperationOnSearchedTrade").equalsIgnoreCase("Cancel Trade")) {
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[normalize-space()='"+sTradeID+"']//parent::div[@role='gridcell']//following-sibling::div//a[contains(@href,'viewTrade')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[ Erro: unable to click on View Trade in Trade Search with TXN ID : '"+sTradeID+"'. to Cancel Selected Trade.]\n";
						return false;
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick, 'cancelTrade') and (normalize-space()= 'Cancel Trade')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ Erro: unable to click on Cancel Trade button in Trade Search with TXN ID : '"+sTradeID+"'. to Cancel Selected Trade.]\n";
						return false;
					}					
				}
				if(mapTradeSearchDetails.get("OperationOnSearchedTrade").equalsIgnoreCase("Delete Trade"))
				{
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[normalize-space()='"+sTradeID+"']//parent::div[@role='gridcell']//following-sibling::div//a[contains(@href,'viewTrade')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[ Error: Unable to click view Trade in Trade Search with TXN ID : '"+sTradeID+"'. to Delete Selected Trade.]\n";
						return false;
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick, 'deleteTrade') and (normalize-space()= 'Delete Trade')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ Error: Unable to click on Delete Trade in Trade Search with TXN ID : '"+sTradeID+"'. to Delete Selected Trade.]\n";
						return false;
					}
				}
				if (mapTradeSearchDetails.get("OperationOnSearchedTrade").equalsIgnoreCase("Delete Trade") || mapTradeSearchDetails.get("OperationOnSearchedTrade").equalsIgnoreCase("Cancel Trade")) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[@data-bb-handler='confirm']"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Confirmation dialogue wasn't displayed when attempted to '"+mapTradeSearchDetails.get("OperationOnSearchedTrade")+"',with Txn ID : '"+sTradeID+"'.]\n";
						return false;
					}
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[@data-bb-handler='confirm']"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to click on 'confirm' button on the dialogue box for the Operation : '"+mapTradeSearchDetails.get("OperationOnSearchedTrade")+"' ,with Txn ID : '"+sTradeID+"'.]\n";
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	//****************************************Search Transaction in the Grid with Transaction ID and Master Type *******************************************

	public static boolean performOperationsOnTableWithTransactionIDandMasterType(String sOrderID, String sMasterType)
	{
		try {

			for (int i = 0; i < 8; i++) {
				bStatus = searchValueInGridWithTransactionID(sOrderID);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;					
				}
				if(bStatus){
					break;
				}				
			}

			if (!bStatus) {
				Messages.errorMsg = " ERROR : The Expected values : "+sOrderID+" is not visible in the serach filter after 10 iterations.";
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@role='row']//div//div[contains(text(),'"+sMasterType+"')]//..//..//parent::div[@role='row']//div[1]//div[normalize-space(text()='"+sOrderID+"')]"), 5);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Record wasnt identified with Txn ID : "+sOrderID+" ,and Master Type : "+sMasterType+" ] ";
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//******************************************* Search Operations on the Grid with TransactionID and Master Type for Feeder
	public static boolean performOperationsOnTableWithTransactionIDandMasterTypeForFeeder(String sOrderID, String sMasterType)
	{
		try {

			for (int i = 0; i < 8; i++) {
				bStatus = searchValueInGridWithTransactionIDForFeeder(sOrderID);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;					
				}
				if(bStatus){
					break;
				}				
			}

			if (!bStatus) {
				Messages.errorMsg = " ERROR : The Expected values : "+sOrderID+" is not visible in the serach filter after 10 iterations.";
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@role='row']//div//div[contains(text(),'"+sMasterType+"')]//..//..//parent::div[@role='row']//div[1]//div[normalize-space(text()='"+sOrderID+"')]"), 5);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Record wasnt identified with Txn ID : "+sOrderID+" ,and Master Type : "+sMasterType+" ] ";
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	//-------------------------------------------------------------------------------------
	public static boolean searchValueInGridWithTransactionID(String sOrderID)
	{
		String sTableContentLocator="";

		Elements.enterText(Global.driver, By.xpath("//div[@id='filterrow.jqxgridSearchForAllTrades']/div/div[1]//input[@type='textarea']"), sOrderID);

		sTableContentLocator = "//div[@id='contenttablejqxgridSearchForAllTrades']/div[1]/div[1]//div[text()='Value']";

		sTableContentLocator = sTableContentLocator.replace("Value", sOrderID);
		bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableContentLocator),Constants.iSearchActionTable);
		if(!bStatus){
			Messages.errorMsg = " ERORR : Record is not visible in the Table";
			return false;
		}
		return true;
	}
	//-------------------------------------------------------------------------------------
	public static boolean searchValueInGridWithTransactionIDForFeeder(String sOrderID)
	{
		String sTableContentLocator="";

		Elements.enterText(Global.driver, By.xpath("//div[@id='filterrow.jqxgridSearchForAllFeederTrades']/div/div[1]//input[@type='textarea']"), sOrderID);

		sTableContentLocator = "//div[@id='contenttablejqxgridSearchForAllFeederTrades']/div[1]/div[1]//div[text()='Value']";

		sTableContentLocator = sTableContentLocator.replace("Value", sOrderID);
		bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableContentLocator),Constants.iSearchActionTable);
		if(!bStatus){
			Messages.errorMsg = " ERORR : Record is not visible in the Table";
			return false;
		}
		return true;
	}


	//***************************************************performOperationsOnSerachedTrade at Checker Side *****************************************************
	public static boolean performCheckerOperationsOnSerachedTrade(Map<String, String> mapTradeSearchDetails, String sTradeId)
	{
		try {

			if (mapTradeSearchDetails.get("OperationOnSearchedTrade").equalsIgnoreCase("Cancel Trade")) {
				if (mapTradeSearchDetails.get("OperationAtChecker") !=null && mapTradeSearchDetails.get("OperationAtChecker").equalsIgnoreCase("Cancel Trade")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div//button[1][contains(@onclick, 'CRCancel')]"), Constants.iSpinnerTime);
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Button"+mapTradeSearchDetails.get("OperationAtChecker")+" is not present";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div//button[1][contains(@onclick, 'CRCancel')]"));
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Unable to click on " +mapTradeSearchDetails.get("OperationAtChecker") ;
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ERROR :Succesfull Message is Not visible ]\n";
						return false;
					}
				}

				if (mapTradeSearchDetails.get("OperationAtChecker") !=null && mapTradeSearchDetails.get("OperationAtChecker").equalsIgnoreCase("Cancel Trade Reject")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div//button[2][contains(@onclick, 'CRCancelReject')]"), Constants.iSpinnerTime);
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Button"+mapTradeSearchDetails.get("OperationAtChecker")+" is not present";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div//button[2][contains(@onclick, 'CRCancelReject')]"));
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Unable to click on " +mapTradeSearchDetails.get("OperationAtChecker") ;
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ERROR :Succesfull Message is Not visible ]\n";
						return false;
					}
				}
				if (mapTradeSearchDetails.get("OperationAtChecker") !=null && mapTradeSearchDetails.get("OperationAtChecker").equalsIgnoreCase("Cancel")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div//button[contains(@onclick, 'cancelDashboard')]"), Constants.iSpinnerTime);
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Button"+mapTradeSearchDetails.get("OperationAtChecker")+" is not present";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'cancelDashboard')]"));
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Unable to click on " +mapTradeSearchDetails.get("OperationAtChecker") ;
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//i[contains(@class, 'icon-home')]"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Unable to Navigate to Dashbaord on Canceling Trade";
					}
				}
			}
			if (mapTradeSearchDetails.get("OperationOnSearchedTrade").equalsIgnoreCase("Delete Trade")) {
				if (mapTradeSearchDetails.get("OperationAtChecker") !=null && mapTradeSearchDetails.get("OperationAtChecker").equalsIgnoreCase("Delete Trade")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div/button[1][contains(@onclick, 'CRDelete')]"), Constants.iSpinnerTime);
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Button"+mapTradeSearchDetails.get("OperationAtChecker")+" is not present";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div/button[1][contains(@onclick, 'CRDelete')]"));
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Unable to click on " +mapTradeSearchDetails.get("OperationAtChecker") ;
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ERROR :Succesfull Message is Not visible ]\n";
						return false;
					}
				}
				if (mapTradeSearchDetails.get("OperationAtChecker") !=null && mapTradeSearchDetails.get("OperationAtChecker").equalsIgnoreCase("Delete Trade Reject")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div/button[2][contains(@onclick, 'CRDeleteReject')]"), Constants.iSpinnerTime);
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Button"+mapTradeSearchDetails.get("OperationAtChecker")+" is not present";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div/button[2][contains(@onclick, 'CRDeleteReject')]"));
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Unable to click on " +mapTradeSearchDetails.get("OperationAtChecker") ;
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ERROR :Succesfull Message is Not visible ]\n";
						return false;
					}
				}
				if (mapTradeSearchDetails.get("OperationAtChecker") !=null && mapTradeSearchDetails.get("OperationAtChecker").equalsIgnoreCase("Cancel")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div//button[contains(@onclick, 'cancelDashboard')]"), Constants.iSpinnerTime);
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Button"+mapTradeSearchDetails.get("OperationAtChecker")+" is not present";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'cancelDashboard')]"));
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Unable to click on " +mapTradeSearchDetails.get("OperationAtChecker") ;
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//i[contains(@class, 'icon-home')]"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "ERROR: Unable to Navigate to Dashbaord on Canceling Trade";
					}
				}	
			}
			if (mapTradeSearchDetails.get("OperationOnSearchedTrade").equalsIgnoreCase("Modify Trade")) {


				if (mapTradeSearchDetails.get("OperationAtChecker") != null) {
					if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Subscription")) {
						Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapTradeSearchDetails.get("Order Id"));
						bStatus = SubscriptionModifyAtChecker(mapTradeSearchDetails.get("Order Id"), mapXMLSubscriptionDetails);
						if (!bStatus) {
							Messages.errorMsg = "ERROR: Verification failed for Modify Subscription at Checker";
							return false;
						}
					}
					if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Redemption")) {
						Map<String ,String> mapXMLRedemptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "TradeTypeRED", mapTradeSearchDetails.get("Order Id"));
						bStatus = RedemptionModifyAtChecker(mapTradeSearchDetails.get("Order Id"), mapXMLRedemptionDetails);
						if (!bStatus) {
							Messages.errorMsg = "ERROR: Verification failed for Modify Redemption at Checker";
							return false;
						}

					}
					if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Transfer")) {
						Map<String ,String> mapXMLTransferDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "TradeTypeTRAN", mapTradeSearchDetails.get("Order Id"));
						bStatus = TransferModifyAtChecker(mapTradeSearchDetails.get("Order Id"), mapXMLTransferDetails);
						if (!bStatus) {
							Messages.errorMsg = "ERROR: Verification failed for Modify Transfer at Checker";
							return false;
						}
					}
					if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Exchange")) {
						Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "TradeTypeEXCN", mapTradeSearchDetails.get("Order Id"));
						bStatus = ExchangeModifyAtChecker(mapTradeSearchDetails.get("Order Id"), mapXMLExchangeDetails);
						if (!bStatus) {
							Messages.errorMsg = "ERROR: Verification failed for Modify Exchange at Checker";
							return false;
						}
					}
					if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Switch")) {
						Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TradeTypeSWITCH", mapTradeSearchDetails.get("Order Id"));
						bStatus = SwitchModifyAtChecker(mapTradeSearchDetails.get("Order Id"), mapXMLExchangeDetails);
						if (!bStatus) {
							Messages.errorMsg = "ERROR: Verification failed for Modify Switch at Checker";
							return false;
						}
					}
					if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Feeder Subscription")) {
						Map<String ,String> mapXMLFeederSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", mapTradeSearchDetails.get("Order Id"));
						bStatus = feederSubscriptionModifyAtChecker(mapTradeSearchDetails.get("Order Id"), mapXMLFeederSUBDetails);
						if (!bStatus) {
							Messages.errorMsg = "ERROR: Verification failed for Modify Feeder Subscription at Checker";
							return false;
						}
					}
					if (mapTradeSearchDetails.get("TradeType") != null && mapTradeSearchDetails.get("TradeType").equalsIgnoreCase("Feeder Redemption")) {
						Map<String ,String> mapXMLFeederREDDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederREDAccountXMLFilePath, "FeederREDAccount", mapTradeSearchDetails.get("Order Id"));
						bStatus = feederRedemptionModifyAtChecker(mapTradeSearchDetails.get("Order Id"), mapXMLFeederREDDetails);
						if (!bStatus) {
							Messages.errorMsg = "ERROR: Verification failed for Modify Feeder Redemption at Checker";
							return false;
						}
					}
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//**************************************************************************************************************************************************************
	public static boolean RedemptionModifyAtChecker(String testCaseName, Map<String,String>mapXMLSubscriptionDetails){
		try {

			Map<String, String> mapCheckerReviewdDetails = ExcelUtils.readDataABasedOnCellName(Global.sRedemptionTestData, "TestDataCheckerReviewdAtChecker", testCaseName);
			if (mapCheckerReviewdDetails.get("Account ID") != null) {
				String accountId = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapCheckerReviewdDetails.get("Account ID"));
				if(accountId!=null && !accountId.equalsIgnoreCase("")){
					mapCheckerReviewdDetails.put("Account ID", accountId);
				}
			}
			if(mapXMLSubscriptionDetails.get("TransactionID")!=null){
				mapCheckerReviewdDetails.put("Transaction ID", mapXMLSubscriptionDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}					
			//Overriding Checker side verification values of Available Balances and pending Trades with the new column values.
			mapCheckerReviewdDetails.put("ExcpectedActualBalance", mapCheckerReviewdDetails.get("CheckerExpectedActualBalance"));
			mapCheckerReviewdDetails.put("ExcpectedTotalActualBalance", mapCheckerReviewdDetails.get("CheckerExpectedTotalActualBalance"));
			mapCheckerReviewdDetails.put("ExcpectedActualShares", mapCheckerReviewdDetails.get("CheckerExpectedActualShares"));
			mapCheckerReviewdDetails.put("ExcpectedTotalActualShares", mapCheckerReviewdDetails.get("CheckerExpectedTotalActualShare"));
			mapCheckerReviewdDetails.put("ExcpectedAvailableShares", mapCheckerReviewdDetails.get("CheckerExpectedAvailableShares"));
			mapCheckerReviewdDetails.put("ExcpectedAvailableAmount", mapCheckerReviewdDetails.get("CheckerExpectedAvailableAmount"));
			mapCheckerReviewdDetails.put("ExcpectedTotalAvailableShares", mapCheckerReviewdDetails.get("CheckerExpectedTotalAvailableShares"));
			mapCheckerReviewdDetails.put("ExcpectedTotalAvailableAmount", mapCheckerReviewdDetails.get("CheckerExpectedTotalAvailableAmount"));
			mapCheckerReviewdDetails.put("ExpectedPendingRequestAmount", mapCheckerReviewdDetails.get("CheckerExpectedPendingReqAmount"));
			mapCheckerReviewdDetails.put("ExpectedPendingRequestedShares", mapCheckerReviewdDetails.get("CheckerExpectedPendingReqShares"));
			mapCheckerReviewdDetails.put("ExcpectedIsUnderLockupStatus", mapCheckerReviewdDetails.get("CheckerExpectedUnderLockUpStatus"));


			bStatus = TradeTypeRedemptionAppFunctions.doVerifyRedemptionInCheckerScreen(mapCheckerReviewdDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verify Redemption Details in Checker", "Verification of Redemption Details Failed in Checker.ERROR : "+Messages.errorMsg);
				return false;
			}
			Reporting.logResults("Pass", "Verify Redemption Details in Checker", "Redemption details verified successfully in Checker");


			bStatus = TradeTypeRedemptionAppFunctions.doCheckerActionTypesOnTradeRedemption(mapCheckerReviewdDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				return false;
			}
			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//****************************************************************************************************************************************************************
	public static boolean SubscriptionModifyAtChecker(String testCaseName, Map<String, String> mapXMLSubscriptionDetails)
	{
		try {
			//Map<String ,String> mapSubscriptionMakerData = ExcelUtils.readDataABasedOnCellName(Global.sSubscriptionTestData, "SubscriptionTestData", testCaseName);
			Map<String, String> mapSubscriptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sSubscriptionTestData, "CheckerReviewdTestDataAtChecker", testCaseName);

			//Map<String,String> mapClubbedDetails = new HashMap<String, String>();

			String sInvestorName = getNewInvestor(mapSubscriptionDetails);
			if (sInvestorName != null && !sInvestorName.equalsIgnoreCase("")) {
				mapSubscriptionDetails.put("Investor Name",sInvestorName);
			}

			String sHolderName = getNewHolder(mapSubscriptionDetails);
			if (sHolderName !=null && !sHolderName.equalsIgnoreCase("")) {
				mapSubscriptionDetails.put("Holder Name",sHolderName);
			}
			//get the series map
			if(mapSubscriptionDetails.get("New Series") == null && mapSubscriptionDetails.get("New Series TestCaseName") == null && mapSubscriptionDetails.get("Series Name") == null){
				if(mapSubscriptionDetails.get("New Series")!=null && mapSubscriptionDetails.get("New Series TestCaseName")!=null){
					Map<String,  String> mapSeriesDetails = ExcelUtils.readDataABasedOnCellName(Global.sSeriesTestDataFilePath, "SeriesTestData" ,mapSubscriptionDetails.get("New Series TestCaseName"));
					if(mapSeriesDetails.get("SeriesName")!=null){
						mapSubscriptionDetails.put("Series Name", mapSeriesDetails.get("SeriesName"));
					}
				}				
			}
			if(mapSubscriptionDetails.get("New Series")!=null && mapSubscriptionDetails.get("New Series TestCaseName")!=null){
				Map<String,  String> mapSeriesDetails = ExcelUtils.readDataABasedOnCellName(Global.sSeriesTestDataFilePath, "SeriesTestData" ,mapSubscriptionDetails.get("New Series TestCaseName"));
				if(mapSeriesDetails.get("SeriesName")!=null){
					mapSubscriptionDetails.put("Series Name", mapSeriesDetails.get("SeriesName"));
				}
			}
			/*			//if selecting the created Account ID than nullify the Maker Test Data NewAccount value
			if(mapSubscriptionDetails.get("Account ID")!=null){
				mapSubscriptionMakerData.remove("NewAccount");
			}
			if(mapSubscriptionDetails.get("NewAccount")!=null){
				mapSubscriptionMakerData.remove("Account ID");
			}
			if(mapSubscriptionDetails.get("Account ID") == null && mapSubscriptionDetails.get("NewAccount") == null){
				if(mapSubscriptionMakerData.get("NewAccount") != null){
					mapSubscriptionDetails.put("Account ID", Reporting.Testcasename);
				}				
			}
			 */			//get the xml map

			if(mapSubscriptionDetails.get("NewAccount")!=null && mapXMLSubscriptionDetails.get("AccountID")!=null){
				mapSubscriptionDetails.put("Account ID", mapXMLSubscriptionDetails.get("AccountID"));
			}
			if (mapSubscriptionDetails.get("NewAccount") == null && mapSubscriptionDetails.get("Account ID") != null) {
				Map<String ,String> mapXMLSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapSubscriptionDetails.get("Account ID"));
				if (mapXMLSUBDetails != null && !mapXMLSUBDetails.isEmpty() && mapXMLSUBDetails.get("AccountID") != null) {
					mapSubscriptionDetails.put("Account ID", mapXMLSUBDetails.get("AccountID"));
				}
				else {
					Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapSubscriptionDetails.get("Account ID"));
					if (mapXMLAccountMasterDetails != null && !mapXMLAccountMasterDetails.isEmpty() && mapXMLAccountMasterDetails.get("AccountID") != null) {
						mapSubscriptionDetails.put("Account ID", mapXMLAccountMasterDetails.get("AccountID"));
					}
				}				
			}
			if(mapXMLSubscriptionDetails.get("TransactionID")!=null){
				mapSubscriptionDetails.put("Transaction ID", mapXMLSubscriptionDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}

			//get the details of respective map and put to subscription map
			TradeTypeSubscriptionAppFunctions.isChckerReviewedCheckerOperation = true;

			bStatus = TradeTypeSubscriptionAppFunctions.doVerifySubscriptionDetailsInChecker(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verify Subscription Details in Checker", "Verification of Subscription Details Failed in Checker.ERROR : "+Messages.errorMsg);
				return false;
			}
			Reporting.logResults("Pass", "Verify Subscription Details in Checker", "Subscription details verified successfully in Checker");

			bStatus = TradeTypeSubscriptionAppFunctions.doCheckerActionTypesOnTrade(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Perform Checker Operation for SubScription Modify: '"+mapSubscriptionDetails.get("CheckerOperations")+"'", "Checker Operation Failed .ERROR : "+Messages.errorMsg);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//**************************************************************************************************************************************************************
	public static boolean TransferModifyAtChecker(String testCaseName, Map<String, String>mapXMLTransferDetails)
	{
		try {
			Map<String, String> mapTransferDetails = ExcelUtils.readDataABasedOnCellName(Global.sTransferTestData, "TestDataCheckerReviewdAtChecker", testCaseName);
			if (mapTransferDetails.get("New Investor TestCaseName") != null) {
				String ToInvestorNameCheckerReviewed = TradeTypeTransferAppFunctions.getInvestorNameFromTheInvestorTestData(mapTransferDetails);
				mapTransferDetails.put("To Investor Name",ToInvestorNameCheckerReviewed);
			}

			if(mapTransferDetails.get("New Holder")!=null && mapTransferDetails.get("New Holder TestCaseName")!=null){
				String ToHolderNameCheckerReviewedSheet = TradeTypeTransferAppFunctions.getHolderNameFromHolderTestData(mapTransferDetails);
				mapTransferDetails.put("To Holder Name",ToHolderNameCheckerReviewedSheet);
			}

			if(mapXMLTransferDetails.get("TransactionID")!=null){
				mapTransferDetails.put("TransactionID", mapXMLTransferDetails.get("TransactionID"));
			}

			/*			//if selecting the created Account ID than nullify the Maker Test Data NewAccount value
			if(mapTransferDetails.get("To Account ID")!=null){
				mapTransferMakerData.remove("NewAccount");
			}
			if(mapTransferDetails.get("NewAccount")!=null){
				mapTransferMakerData.remove("To Account ID");
			}
			if(mapTransferDetails.get("To Account ID") == null && mapTransferDetails.get("NewAccount") == null){
				if(mapTransferMakerData.get("NewAccount")!=null && mapTransferMakerData.get("NewAccount").equalsIgnoreCase("Yes"))
				{
					mapTransferDetails.put("To Account ID", Reporting.Testcasename);
				}
			}
			 */
			//	mapClubbedDetails.putAll(mapTransferDetails);
			mapTransferDetails.put("ExpectedAvailableAmount",mapTransferDetails.get("CheckerExpectedAvailableAmount"));
			mapTransferDetails.put("ExpectedTotalAvailableAmount",mapTransferDetails.get("CheckerExpectedTotalAvailableAmount"));
			mapTransferDetails.put("ExpectedAvailableShares",mapTransferDetails.get("CheckerExpectedAvailableShares"));
			mapTransferDetails.put("ExpectedTotalAvailableShares",mapTransferDetails.get("CheckerExpectedTotalAvailableShares"));
			mapTransferDetails.put("ExpectedPendingRequestAmount",mapTransferDetails.get("CheckerExpectedPendingReqAmount"));
			mapTransferDetails.put("ExpectedPendingRequestedShares",mapTransferDetails.get("CheckerExpectedPendingReqShares"));
			mapTransferDetails.put("ExpectedActualBalance",mapTransferDetails.get("CheckerExpectedActualBalance"));
			mapTransferDetails.put("ExpectedTotalActualBalance",mapTransferDetails.get("CheckerExpectedTotalActualBalance"));
			mapTransferDetails.put("ExpectedActualShares",mapTransferDetails.get("CheckerExpectedActualShares"));
			mapTransferDetails.put("ExpectedTotalActualShares",mapTransferDetails.get("CheckerExpectedTotalActualShares"));


			bStatus = TradeTypeTransferAppFunctions.doVerifyTransferDetailsInChecker(mapTransferDetails, mapXMLTransferDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verifying Trade Type Transfer Details in Checker", "Verification of Trade Type Transfer Details Failed in Checker.ERROR : "+Messages.errorMsg);
				return false;
			}
			Reporting.logResults("Pass", "Verifying Trade Type Transfer Details in Checker", "Verification of Trade Type Transfer details verified successfully in Checker");

			bStatus= TradeTypeTransferAppFunctions.doCheckerActionTypesOnTradeTypeTransfer(mapTransferDetails);						
			if(!bStatus){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//************************************************************************************************************************************************************
	public static boolean ExchangeModifyAtChecker(String testCaseName, Map<String, String> mapXMLExchangeDetails)
	{
		try {
			Map<String, String> mapExchangeDetails = ExcelUtils.readDataABasedOnCellName(Global.sExchangeTestData, "TestDataCheckerReviewdAtChecker", testCaseName);
			//Overriding Checker side verification values of Available Balances and pending Trades with the new column values.
			mapExchangeDetails.put("ExcpectedAvailableShares", mapExchangeDetails.get("CheckerExpectedAvailableShares"));
			mapExchangeDetails.put("ExcpectedAvailableAmount", mapExchangeDetails.get("CheckerExpectedAvailableAmount"));
			mapExchangeDetails.put("ExcpectedTotalAvailableShares", mapExchangeDetails.get("CheckerExpectedTotalAvailableShares"));
			mapExchangeDetails.put("ExcpectedTotalAvailableAmount", mapExchangeDetails.get("CheckerExpectedTotalAvailableAmount"));
			mapExchangeDetails.put("ExpectedPendingRequestAmount", mapExchangeDetails.get("CheckerExpectedPendingReqAmount"));
			mapExchangeDetails.put("ExpectedPendingRequestedShares", mapExchangeDetails.get("CheckerExpectedPendingReqShares"));
			mapExchangeDetails.put("ExpectedActualShares", mapExchangeDetails.get("CheckerExpectedActualShares"));
			mapExchangeDetails.put("ExpectedTotalActualShares", mapExchangeDetails.get("CheckerExpectedTotalActualShares"));
			mapExchangeDetails.put("ExpetedActualBalance", mapExchangeDetails.get("CheckerExpectedActualBalance"));
			mapExchangeDetails.put("ExpectedTotalActualBalance", mapExchangeDetails.get("CheckerExpectedTotalActualBalance"));

			if(mapXMLExchangeDetails.get("TransactionID")!=null){
				mapExchangeDetails.put("Transaction ID", mapXMLExchangeDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}

			bStatus = TradeTypeExchangeAppFunction.doVerifyExchangeTradeAtChecker(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verifying Trade Type Exchange Details in Checker", "Verification of Trade Type Exchange Details Failed in Checker.ERROR : "+Messages.errorMsg);
				return false;
			}
			Reporting.logResults("Pass", "Verifying Trade Type Exchange Details in Checker", "Verification of Trade Type Exchange details verified successfully in Checker");


			bStatus = TradeTypeExchangeAppFunction.doCheckerOperationsForExchangeTrade(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//*********************************************************************************************************************************************************
	public static boolean SwitchModifyAtChecker(String testCaseName, Map<String, String> mapXMLSwitchDetails)
	{
		try {

			Map<String, String> mapSwitchDetails = ExcelUtils.readDataABasedOnCellName(Global.sSwitchTestData, "CRCheckerDetailsToVerify", testCaseName);

			if(mapXMLSwitchDetails.get("TransactionID")!=null){
				mapSwitchDetails.put("Transaction ID", mapXMLSwitchDetails.get("TransactionID"));
			}
			//Overriding Checker side verification values of Available Balances and pending Trades with the new column values.

			mapSwitchDetails.put("ExpectedActualBalance", mapSwitchDetails.get("CheckerExpectedActulaBalance"));
			mapSwitchDetails.put("ExpectedTotalActualBalance", mapSwitchDetails.get("CheckerExpectedTotalActulaBalance"));
			mapSwitchDetails.put("ExpectedActualShares", mapSwitchDetails.get("CheckerExpectedActualShare"));
			mapSwitchDetails.put("ExpectedTotalActualShares", mapSwitchDetails.get("CheckerExpectedTotalActualShare"));			
			mapSwitchDetails.put("ExpectedAvailableShares", mapSwitchDetails.get("CheckerExpectedAvailableShares"));
			mapSwitchDetails.put("ExpectedAvailableAmount", mapSwitchDetails.get("CheckerExpectedAvailableAmount"));
			mapSwitchDetails.put("ExpectedTotalAvailableShares", mapSwitchDetails.get("CheckerExpectedTotalAvailableShares"));
			mapSwitchDetails.put("ExpectedTotalAvailableAmount", mapSwitchDetails.get("CheckerExpectedTotalAvailableAmount"));
			mapSwitchDetails.put("ExpectedPendingRequestAmount", mapSwitchDetails.get("CheckerExpectedPendingReqAmount"));
			mapSwitchDetails.put("ExpectedPendingRequestedShares", mapSwitchDetails.get("CheckerExpectedPendingReqShares"));

			bStatus = TradeTypeSwitchAppFunctions.doTriggerCheckerValidationtionFunctions(mapSwitchDetails);			
			if(!bStatus){
				Reporting.logResults("Fail", "Verify Switch Details in Checker", "Verification of Switch Details Failed in Checker.ERROR : "+Messages.errorMsg);
				return false;				
			}

			bStatus = TradeTypeSwitchAppFunctions.performCheckerOperationOnTrade(mapSwitchDetails);			
			if(!bStatus){
				Reporting.logResults("Fail", "Perform Switch Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//****************************************************************************************************************************************************
	public static boolean feederSubscriptionModifyAtChecker(String sTestCaseName, Map<String, String> mapXMLSubscriptionDetails)
	{
		try {
			Map<String, String> mapFeederSUBDetails = ExcelUtils.readDataABasedOnCellName(Global.sFeederSubscriptionTestDataFilePath, "CheckerReturnedMakerData", sTestCaseName);
			if(mapXMLSubscriptionDetails.get("SearchID") != null){
				mapFeederSUBDetails.put("SearchID", mapXMLSubscriptionDetails.get("SearchID"));
			}
			Map<String, String> mapFeederSUBMakerTestData = ExcelUtils.readDataABasedOnCellName(Global.sFeederSubscriptionTestDataFilePath, "FeederSubscriptionTestData", sTestCaseName);

			Map<String,String> mapClubbedDetails = new HashMap<String, String>();
			//Navigate to DashBoard
			if(mapFeederSUBDetails.get("Cash") != null){
				mapFeederSUBMakerTestData.remove("Expected Cash");
				mapFeederSUBMakerTestData.remove("Units");
			}

			if(mapFeederSUBDetails.get("Units") != null){
				mapFeederSUBMakerTestData.remove("Cash");
				mapFeederSUBMakerTestData.remove("Expected Units");
			}

			mapClubbedDetails.putAll(mapFeederSUBMakerTestData);
			mapClubbedDetails.putAll(mapFeederSUBDetails);	


			//Perform Checker Verification Operation
			bStatus = FeederSubscriptionAppFunctions.doVerifyFeederSubscriptionDetails(mapClubbedDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Verification Of Created Feeder SubscriptionDetails.", "Verification Of Created Feeder Subscription Details are NOT matching with Expected ."+Messages.errorMsg);
				return false;
			}

			Reporting.logResults("Pass", "Verification Of Created Feeder SubscriptionDetails.", "Verification Of Created Feeder Subscription Details are matching with Expected .");

			//Perform Checker OPeration
			bStatus = FeederSubscriptionAppFunctions.doCheckerOperationsForFeederSubscription(mapClubbedDetails.get("CheckerOperations"));

			if(bStatus && mapClubbedDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){

				Reporting.logResults("Pass","Perform Checker Operation: "+mapClubbedDetails.get("CheckerOperations"), "Successfully Performed checker operations for transaction ID : "+mapClubbedDetails.get("SearchID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true ;
	}

	//*******************************************************************************************************************************************************
	public static boolean feederRedemptionModifyAtChecker(String sTestCaseName, Map<String, String> mapXMLSubscriptionDetails)
	{
		try {
			Map<String, String> mapFeederREDDetails = ExcelUtils.readDataABasedOnCellName(Global.sFeedeerRedemptionTestDataFilePath, "CheckerReturnedMakerData", sTestCaseName);
			Map<String,String> mapClubbedDetails = new HashMap<>();
			if(mapXMLSubscriptionDetails.get("SearchID") != null){
				mapFeederREDDetails.put("SearchID", mapXMLSubscriptionDetails.get("SearchID"));
			}

			Map<String, String> mapFeederREDMakerTestData = ExcelUtils.readDataABasedOnCellName(Global.sFeedeerRedemptionTestDataFilePath, "FeederRedemptionTestData", sTestCaseName);

			if(mapFeederREDDetails.get("Cash") != null){
				mapFeederREDMakerTestData.remove("Expected Cash");
				mapFeederREDMakerTestData.remove("Units");
			}

			if(mapFeederREDDetails.get("Units") != null){
				mapFeederREDMakerTestData.remove("Cash");
				mapFeederREDMakerTestData.remove("Expected Units");
			}
			mapClubbedDetails.putAll(mapFeederREDMakerTestData);
			mapClubbedDetails.putAll(mapFeederREDDetails);				

			//Perform Checker Verification Operation.
			bStatus = FeederRedemptionAppFunctions.doVerifyFeederRedemptionDetails(mapClubbedDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Verification Of Feeder Redemption Details.", "Verification Of Feeder Redemption Details are NOT matching with Expected ."+Messages.errorMsg);
				return false;
			}
			Reporting.logResults("Pass", "Verification Of Feeder Redemption Details.", "Verification Of Feeder Redemption Details are matching with Expected .");	


			//Perform Checker OPeration
			bStatus = FeederRedemptionAppFunctions.doCheckerOperationsForFeederRedemption(mapClubbedDetails.get("CheckerOperations"));

			if(bStatus && mapClubbedDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){

				Reporting.logResults("Pass","Perform Checker Operation: "+mapClubbedDetails.get("CheckerOperations"), "Successfully Performed checker operations for transaction ID : "+mapClubbedDetails.get("SearchID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//*************************************************************Creating New Investor for subscription
	public static String getNewInvestor(Map<String, String> mapSubscriptionMakerData)
	{
		String sInvestorFirstName = "";
		String sInvestorLastName = "";
		String sInvestorMiddleName = "";
		String sInvestorName = "";	

		try {
			//get the investor map details if available
			if(mapSubscriptionMakerData.get("New Investor")!=null && mapSubscriptionMakerData.get("New Investor TestCaseName")!=null)
			{
				Map<String, String> mapInvestorGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, "GeneralDetails" , mapSubscriptionMakerData.get("New Investor TestCaseName"));

				if (mapInvestorGeneralDetails.get("InvestorType") != null) 
				{
					if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity") && mapInvestorGeneralDetails.get("RegistrationName") != null) {
						sInvestorName = mapInvestorGeneralDetails.get("RegistrationName");
					}	
					//if Investor Type is Individual Holder Name should be Combination of First,Middle,Last
					if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Individual")) 
					{
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
					//put the Investor Name From Investor Test data in Subscription Map
					sInvestorName = mapSubscriptionMakerData.put("Investor Name",sInvestorName);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		return sInvestorName;
	}
	//*************************************************************Creating New Holder
	public static String getNewHolder(Map<String, String> mapSubscriptionMakerData)
	{
		String sHolderFirstName = "";
		String sHolderLastName = "";
		String sHolderMiddleName = "";
		String sHolderName = "";								
		try {
			if(mapSubscriptionMakerData.get("New Holder")!=null && mapSubscriptionMakerData.get("New Holder TestCaseName")!=null)
			{
				Map<String, String> mapHolderGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, "GeneralDetails" , mapSubscriptionMakerData.get("New Holder TestCaseName"));

				if (mapHolderGeneralDetails.get("HolderType") != null) 
				{
					if (mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity") && mapHolderGeneralDetails.get("RegistrationName") != null) 
					{
						sHolderName = mapHolderGeneralDetails.get("RegistrationName");
					}	
					//if Holder Type is Individual Holder Name should be Combination of First,Middle,Last
					if (mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Individual")) 
					{
						if (mapHolderGeneralDetails.get("FirstName") != null) {
							sHolderFirstName = mapHolderGeneralDetails.get("FirstName");
							sHolderName = sHolderFirstName;
						}
						if (mapHolderGeneralDetails.get("MiddleName") != null) {
							sHolderMiddleName = mapHolderGeneralDetails.get("MiddleName");
							sHolderName = sHolderName+" "+sHolderMiddleName;
						}
						if (mapHolderGeneralDetails.get("LastName") != null) {
							sHolderLastName = mapHolderGeneralDetails.get("LastName");
							sHolderName = sHolderName+" "+sHolderLastName;
						}
						sHolderName = sHolderName.trim();
					}
					sHolderName = mapSubscriptionMakerData.put("Holder Name",sHolderName);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null ;
		}
		return sHolderName; 
	}
}
