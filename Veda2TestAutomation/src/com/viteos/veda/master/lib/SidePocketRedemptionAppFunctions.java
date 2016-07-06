package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Alerts;
import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.checkerActionTypes;

public class SidePocketRedemptionAppFunctions {
	static boolean bStatus;
	public static boolean isMakerOperationForCheckerReturned = false;

	public static boolean doFillSidePocaketRedemprionDetails(Map<String,String> mapSidePocketRedemptionDetails){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='effectiveDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Effective Date Field not available Page is Not visible";
				return false;
			}


			bStatus = doFillFundDetailsForSidePocketRedemption(mapSidePocketRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillRequestDetailsForSidePocketRedemption(mapSidePocketRedemptionDetails);
			if(!bStatus){
				return false;
			}

			if(mapSidePocketRedemptionDetails.get("GetrecordsOperation") != null){
				if(mapSidePocketRedemptionDetails.get("GetrecordsOperation").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'applySubmit') and contains(normalize-space(),'Get Investors Records')]"));
					if(!bStatus){
						Messages.errorMsg = "Get Investors Records Button Cannot be Clickable";
						return false;
					}
				}
			}

			if(mapSidePocketRedemptionDetails.get("TaxSlotTransactionID") != null && mapSidePocketRedemptionDetails.get("AmountOrPercentage") != null && mapSidePocketRedemptionDetails.get("AmountToEnterOrVerify") != null && mapSidePocketRedemptionDetails.get("PercentageToEnterOrVerify") != null && mapSidePocketRedemptionDetails.get("AllocatedShareToVerify") != null && mapSidePocketRedemptionDetails.get("AccountIDs") != null){
				bStatus = doFillTaxSlotsForSidePocketRedemption(mapSidePocketRedemptionDetails);
				if(!bStatus){
					return false;
				}
			}			

			if(mapSidePocketRedemptionDetails.get("OperationType") != null){
				if(isMakerOperationForCheckerReturned && mapSidePocketRedemptionDetails.get("OperationType").contains("Cancel")){
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'cancel') and normalize-space()='Cancel']"));
					if(!bStatus){
						Messages.errorMsg = "Unable to Click on Cancel button";
						return false;
					}
					return true;
				}
				bStatus = SidePocketSubscriptionAppFunctions.doOperationsOnSidePocketMasters(mapSidePocketRedemptionDetails.get("OperationType"));
			}

			return bStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doFillTaxSlotsForSidePocketRedemption(Map<String, String> mapSidePocketRedemptionDetails) {
		try {

			bStatus = UncheckTheCheckBoxForTaxSlotsIfAnyChecked();
			if(!bStatus){
				return false;
			}

			//Get the Values to The list
			List<String> transactionIdlist = Arrays.asList(mapSidePocketRedemptionDetails.get("TaxSlotTransactionID").split(","));
			List<String> accountIDlist = Arrays.asList(mapSidePocketRedemptionDetails.get("AccountIDs").split(","));
			List<String> availableBalancelist = Arrays.asList(mapSidePocketRedemptionDetails.get("AvailableBalance").split(","));
			List<String> availableSharelist = Arrays.asList(mapSidePocketRedemptionDetails.get("AvailableShare").split(","));
			List<String> AmountOrSharelist = Arrays.asList(mapSidePocketRedemptionDetails.get("AmountOrPercentage").split(","));
			List<String> Amountlist = Arrays.asList(mapSidePocketRedemptionDetails.get("AmountToEnterOrVerify").split(","));
			List<String> Percentagelist = Arrays.asList(mapSidePocketRedemptionDetails.get("PercentageToEnterOrVerify").split(","));
			List<String> AllocatedSharelist = Arrays.asList(mapSidePocketRedemptionDetails.get("AllocatedShareToVerify").split(","));

			String rowXpath = "//input[@id='taxSlotSize']//following-sibling::div//tbody//tr";
			int taxSlotsXpathCount = Elements.getXpathCount(Global.driver, By.xpath(rowXpath));
			if(transactionIdlist.size() <= taxSlotsXpathCount){

				for(int i =0; i<transactionIdlist.size(); i++){
					String transactionIDXpath = "";
					//Get the Xpath's  depending on the Transaction ID ,Account Id ,available balance and available Share for filling and verifying the Required fields
					if(availableSharelist.get(i).equalsIgnoreCase("None")){
						transactionIDXpath = rowXpath+"//td[normalize-space()='"+transactionIdlist.get(i)+"']//following-sibling::td[normalize-space()='"+accountIDlist.get(i)+"']//following-sibling::td[normalize-space()='"+availableBalancelist.get(i)+"' or contains(normalize-space(),'"+availableBalancelist.get(i)+"')]";
					}else{
						transactionIDXpath = rowXpath+"//td[normalize-space()='"+transactionIdlist.get(i)+"']//following-sibling::td[normalize-space()='"+accountIDlist.get(i)+"']//following-sibling::td[normalize-space()='"+availableBalancelist.get(i)+"' or contains(normalize-space(),'"+availableBalancelist.get(i)+"')]//following-sibling::td[normalize-space()='"+availableSharelist.get(i)+"' or contains(normalize-space(),'"+availableSharelist.get(i)+"')]";
					}					
					
					String checkBoxXpath = transactionIDXpath+"//preceding-sibling::td//span/input";
					String amountFieldXapth = transactionIDXpath+"//following-sibling::td//input[contains(@id,'spAmount')]";
					String PercentageFieldXapth = transactionIDXpath+"//following-sibling::td//input[contains(@id,'spPercentage')]";
					String ShareFieldXapth = transactionIDXpath+"//following-sibling::td//div[contains(@id,'spShares')]";

					//Check the check box if Unchecked
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath(checkBoxXpath));
					if(!bStatus){
						bStatus = Elements.click(Global.driver, By.xpath(checkBoxXpath));
						if(!bStatus){
							Messages.errorMsg = "Check box Cannot be Checked for Transaction ID: "+transactionIdlist.get(i);
							return false;
						}
					}

					if(!AmountOrSharelist.get(i).equalsIgnoreCase("None")){
						//if AmountOrShare is Amount Enter the Amount and Verify the Other fields
						if(AmountOrSharelist.get(i).equalsIgnoreCase("Amount")){								
							bStatus = doEnterAmountOrPercentageAndVerfiyOthers(Amountlist.get(i), amountFieldXapth, "Amount", Percentagelist.get(i), PercentageFieldXapth, "Percentage", transactionIdlist.get(i), Integer.parseInt(mapSidePocketRedemptionDetails.get("NoOfDecimalsExpectedForShares")));
							if(!bStatus){
								return false;
							}							
						}

						//if AmountOrShare is Percentage Enter the Percentage and Verify the Other fields
						if(AmountOrSharelist.get(i).equalsIgnoreCase("Percentage")){							
							bStatus = doEnterAmountOrPercentageAndVerfiyOthers(Percentagelist.get(i), PercentageFieldXapth, "Percentage",Amountlist.get(i), amountFieldXapth, "Amount", transactionIdlist.get(i), Integer.parseInt(mapSidePocketRedemptionDetails.get("NoOfDecimalsExpectedForAmounts")));
							if(!bStatus){
								return false;
							}							
						}

						//Verify the Allocated Share field
						if(!AllocatedSharelist.get(i).equalsIgnoreCase("None")){
							bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Allocated Share For Transaction ID:"+transactionIdlist.get(i)+" ", By.xpath(ShareFieldXapth), AllocatedSharelist.get(i), "Yes", true, Integer.parseInt(mapSidePocketRedemptionDetails.get("NoOfDecimalsExpectedForShares")));
							if(!bStatus){
								return false;
							}
						}
					}
				}


			}else{
				Messages.errorMsg = "Expected Tax Slots Count is: "+transactionIdlist.size()+" Not matching with the Actual Tax Slots :"+taxSlotsXpathCount;
				return false;
			}


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doEnterAmountOrPercentageAndVerfiyOthers(String valueToEnter, String entryValueXpath,String entryValueLabel,String valueToVerify, String VerifyValueXpath,String VerifyValueLabel,String transactionID, int noOfDecimlasToVerify) {
		try {

			String appendMsg = "";
			boolean validateStatus = true;

			NewUICommonFunctions.scrollToView(By.xpath(entryValueXpath));
			if(!valueToEnter.equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver, By.xpath(entryValueXpath), valueToEnter);
				if(!bStatus){
					Messages.errorMsg = ""+entryValueLabel+" :"+valueToEnter+" Cannot be Entered for Transaction ID :"+transactionID;
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			}

			if(!valueToVerify.equalsIgnoreCase("None")){
				String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath(VerifyValueXpath);
				String expectedValue = valueToVerify;

				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay(""+VerifyValueLabel+" Value for the Transaction ID: "+transactionID, noOfDecimlasToVerify, actualValue);
				if (!bStatus) {
					validateStatus = false;
					appendMsg =  appendMsg + Messages.errorMsg;
				}
				if(actualValue == null || Float.parseFloat(actualValue) != Float.parseFloat(expectedValue)){
					validateStatus = false;
					appendMsg =  appendMsg+"[ Actual "+VerifyValueLabel+"  Value "+actualValue+" for the Transaction ID: "+transactionID+" is Not matched with the Expected "+expectedValue+" ]\n";
				}

			}
			return validateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean UncheckTheCheckBoxForTaxSlotsIfAnyChecked() {
		try {

			String locatorForTaxSlotTables = "//input[@id='taxSlotSize']//following-sibling::div//tbody";

			int xpathCountOfCheckedRows = Elements.getXpathCount(Global.driver, By.xpath(locatorForTaxSlotTables+"//tr/td//span[@class='checked']"));
			if(xpathCountOfCheckedRows == 0){
				return true;
			}
			//Get the Table Count
			int xpathCountForTable = Elements.getXpathCount(Global.driver, By.xpath(locatorForTaxSlotTables));

			for(int tableCount = 0; tableCount<xpathCountForTable;tableCount++){
				//Get the Row Count for the Table
				int rowXpathCount = Elements.getXpathCount(Global.driver, By.xpath("//input[contains(@id,'isSelect_"+tableCount+"')]//parent::span"));

				//Iteration for Removing Check boxes based on Row Number
				for(int rowCount = 0 ; rowCount<rowXpathCount;rowCount++){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='isSelect_"+tableCount+"_"+rowCount+"']"));
					if(bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isSelect_"+tableCount+"_"+rowCount+"']"));
						if(!bStatus){
							Messages.errorMsg = "Check for the Tabel: "+tableCount+" and Row: "+rowCount+" is cannot be Unchecked";
							return false;
						}
					}
				}				
			}		

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doFillRequestDetailsForSidePocketRedemption(Map<String, String> mapSidePocketRedemptionDetails) {
		try {

			if(mapSidePocketRedemptionDetails.get("Effective Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapSidePocketRedemptionDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "Effective Date Cannot be Entered.";
					return false;
				}
			}

			if(mapSidePocketRedemptionDetails.get("Performance Fee On Crystalization") != null){
				if(mapSidePocketRedemptionDetails.get("Performance Fee On Crystalization").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(normalize-space(),'Yes')]/div[@id='uniform-isPerFeeCrystalization']/span"));
					if(!bStatus){
						Messages.errorMsg = "Performance Fee On Crystalization 'Yes' Radio button cannot be selected";
						return false;
					}
				}

				if(mapSidePocketRedemptionDetails.get("Performance Fee On Crystalization").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(normalize-space(),'No')]/div[@id='uniform-isPerFeeCrystalization']/span"));
					if(!bStatus){
						Messages.errorMsg = "Performance Fee On Crystalization 'No' Radio button cannot be selected";
						return false;
					}
				}
			}			

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doFillFundDetailsForSidePocketRedemption(Map<String, String> mapSidePocketRedemptionDetails) {
		try {


			if(mapSidePocketRedemptionDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name",mapSidePocketRedemptionDetails.get("Client Name"));
				if(!bStatus){
					Messages.errorMsg = "Client Name Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
			}

			if(mapSidePocketRedemptionDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name",mapSidePocketRedemptionDetails.get("Fund Family Name"));
				if(!bStatus){
					Messages.errorMsg = "Fund Family Name Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
			}

			if(mapSidePocketRedemptionDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name",mapSidePocketRedemptionDetails.get("Legal Entity Name"));
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Name Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
			}

			if(mapSidePocketRedemptionDetails.get("Side Pocket Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Side Pocket Name",mapSidePocketRedemptionDetails.get("Side Pocket Name"));
				if(!bStatus){
					Messages.errorMsg = "Side Pocket Name Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
			}


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifySidePocketRedemptionAtChecker(Map<String, String> mapSDPCKTRedemptiontionDetails) {
		try {
			String appendMsg = "";
			boolean validateStatus = true;

			bStatus = doVerifyFundDetailsAtChecker(mapSDPCKTRedemptiontionDetails);
			if(!bStatus){
				appendMsg = appendMsg+"["+Messages.errorMsg+"]";
				validateStatus = false;
			}

			bStatus = doVerifyRequestDetailsAtChecker(mapSDPCKTRedemptiontionDetails);
			if(!bStatus){
				appendMsg = appendMsg+"["+Messages.errorMsg+"]";
				validateStatus = false;
			}

			bStatus = doVerifyTaxSlotsAtChecker(mapSDPCKTRedemptiontionDetails);
			if(!bStatus){
				appendMsg = appendMsg+"["+Messages.errorMsg+"]";
				validateStatus = false;
			}


			Messages.errorMsg = appendMsg;
			return validateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doVerifyTaxSlotsAtChecker(Map<String, String> mapSDPCKTRedemptiontionDetails) {
		try {
			String appendMsg = "";
			boolean validateStatus = true;

			int noOfDecimalsForAmount = Integer.parseInt(mapSDPCKTRedemptiontionDetails.get("NoOfDecimalsExpectedForAmounts"));
			int noOfDecimalsForShare = Integer.parseInt(mapSDPCKTRedemptiontionDetails.get("NoOfDecimalsExpectedForShares"));

			//Get the Values to The list
			List<String> transactionIdlist = Arrays.asList(mapSDPCKTRedemptiontionDetails.get("TaxSlotTransactionID").split(","));
			List<String> accountIDlist = Arrays.asList(mapSDPCKTRedemptiontionDetails.get("AccountIDs").split(","));
			List<String> availableBalancelist = Arrays.asList(mapSDPCKTRedemptiontionDetails.get("AvailableBalance").split(","));
			//List<String> availableSharelist = Arrays.asList(mapSDPCKTRedemptiontionDetails.get("AvailableShare").split(","));
			List<String> Amountlist = Arrays.asList(mapSDPCKTRedemptiontionDetails.get("AmountToEnterOrVerify").split(","));
			List<String> Percentagelist = Arrays.asList(mapSDPCKTRedemptiontionDetails.get("PercentageToEnterOrVerify").split(","));
			List<String> AllocatedSharelist = Arrays.asList(mapSDPCKTRedemptiontionDetails.get("AllocatedShareToVerify").split(","));

			String rowXpath = "//input[@id='taxSlotSize']//following-sibling::div//tbody//tr";
			for(int i =0; i<transactionIdlist.size(); i++){
				String transactionIDXpath = rowXpath+"//td[normalize-space()='"+transactionIdlist.get(i)+"']//following-sibling::td[normalize-space()='"+accountIDlist.get(i)+"']//following-sibling::td[normalize-space()='"+availableBalancelist.get(i)+"' or contains(normalize-space(),'"+availableBalancelist.get(i)+"')]";
				String checkBoxXpath = transactionIDXpath+"//preceding-sibling::td//span[@class='checked']/input";
				String amountFieldXapth = transactionIDXpath+"//following-sibling::td[3]/input";
				String PercentageFieldXapth = transactionIDXpath+"//following-sibling::td[2]/input";
				String ShareFieldXapth = transactionIDXpath+"//following-sibling::td[4]";

				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(checkBoxXpath), Constants.lTimeOut);
				if(!bStatus){
					appendMsg = appendMsg+"Check box for the Transaction ID: "+transactionIdlist.get(i)+" is Not Checked at checker Screen";
					validateStatus = false;
				}

				if(!Percentagelist.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Percentage Value for the Transaction Id:"+transactionIdlist.get(i)+" ", By.xpath(PercentageFieldXapth), Percentagelist.get(i), "No", true , noOfDecimalsForShare);
					if(!bStatus){
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
						validateStatus = false;
					}
				}

				if(!Amountlist.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Amount Value for the Transaction Id:"+transactionIdlist.get(i)+" ", By.xpath(amountFieldXapth), Amountlist.get(i), "No", true, noOfDecimalsForAmount);
					if(!bStatus){
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
						validateStatus = false;
					}
				}

				if(!AllocatedSharelist.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share Value for the Transaction Id:"+transactionIdlist.get(i)+" ", By.xpath(ShareFieldXapth), AllocatedSharelist.get(i), "Yes", true, noOfDecimalsForShare);
					if(!bStatus){
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
						validateStatus = false;
					}
				}

			}

			Messages.errorMsg = appendMsg;
			return validateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doVerifyRequestDetailsAtChecker(Map<String, String> mapSDPCKTRedemptiontionDetails) {
		try {

			String appendErrMsg = "";
			boolean bValidStatus = true;

			if(mapSDPCKTRedemptiontionDetails.get("Effective Date") != null){
				String actualDate = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Effective Date' or normalize-space(text())='Effective Date']/..//input"), "value");
				actualDate = TradeTypeSubscriptionAppFunctions.formatDate(actualDate);
				if(actualDate == null || !actualDate.equalsIgnoreCase(mapSDPCKTRedemptiontionDetails.get("Effective Date"))){
					appendErrMsg = "Effective Date Actual Value: "+actualDate+" is Not Matching with the Expected Effective Date: "+mapSDPCKTRedemptiontionDetails.get("Effective Date");
					bValidStatus = false;
				}
			}
			if(mapSDPCKTRedemptiontionDetails.get("Performance Fee On Crystalization") != null){
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[text()='Performance Fee On Crystalization' or normalize-space(text())='Performance Fee On Crystalization']//following::label[normalize-space()='"+mapSDPCKTRedemptiontionDetails.get("Performance Fee On Crystalization")+"']"), Constants.lTimeOut);
				if(!bStatus){
					appendErrMsg = appendErrMsg+"[Performance Fee On Crystalization Actual Value is Not Matching with the Expected: "+mapSDPCKTRedemptiontionDetails.get("Performance Fee On Crystalization")+"]";
					bValidStatus = false;
				}
			}		

			Messages.errorMsg = appendErrMsg;
			return bValidStatus; 			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doVerifyFundDetailsAtChecker(Map<String, String> mapSDPCKTRedemptiontionDetails) {
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;

			if(mapSDPCKTRedemptiontionDetails.get("Client Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Client Name", mapSDPCKTRedemptiontionDetails.get("Client Name"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}

			if(mapSDPCKTRedemptiontionDetails.get("Fund Family Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Fund Family Name", mapSDPCKTRedemptiontionDetails.get("Fund Family Name"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}

			if(mapSDPCKTRedemptiontionDetails.get("Legal Entity Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Legal Entity Name", mapSDPCKTRedemptiontionDetails.get("Legal Entity Name"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}

			if(mapSDPCKTRedemptiontionDetails.get("Side Pocket Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Side Pocket Name", mapSDPCKTRedemptiontionDetails.get("Side Pocket Name"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}					
			Messages.errorMsg = appendErrMsg;
			return bValidStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static String getTheTransactionIdsFromXmlFiles(String sTrasactionId){
		try {
			String transactionID = "";

			List<String> aTransactionID = Arrays.asList(sTrasactionId.split(","));

			for(int i=0;i<aTransactionID.size();i++){
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", aTransactionID.get(i));
				if(mapXMLSubscriptionDetails != null && mapXMLSubscriptionDetails.get("TransactionID") != null){
					transactionID = transactionID+mapXMLSubscriptionDetails.get("TransactionID")+",";
				}
				else
				{
					Map<String, String> mapXMLOpeningBalanceDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "OpeningBalance", aTransactionID.get(i));
					if(mapXMLOpeningBalanceDetails != null && mapXMLOpeningBalanceDetails.get("SearchID") != null){
						transactionID = transactionID+mapXMLOpeningBalanceDetails.get("SearchID")+",";
					}
					else
					{						
						transactionID = transactionID+aTransactionID.get(i)+",";

					}
				}
			}
			transactionID = RoleAppFunctions.removeUnwantedCommas(transactionID);
			return transactionID;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String getTheTransactionIdFromSPSubscriptionXMLFile(String sTrasactionId){
		try {
			String transactionID = "";

			List<String> aTransactionID = Arrays.asList(sTrasactionId.split(","));

			for(int i=0;i<aTransactionID.size();i++){
				Map<String ,String> mapXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", aTransactionID.get(i));
				if(mapXMLSPSUBDetails != null){
					transactionID = transactionID+mapXMLSPSUBDetails.get("TransactionID")+",";
				}
				else
				{
					transactionID = transactionID+aTransactionID.get(i)+",";
				}
			}
			transactionID = RoleAppFunctions.removeUnwantedCommas(transactionID);
			return transactionID;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getTheAccountIdListFromXMLFiles(String sTrasactionId){
		try {
			String transactionID = "";
			List<String> aTransactionID = Arrays.asList(sTrasactionId.split(","));

			for(int i=0;i<aTransactionID.size();i++){
				String accountId = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(aTransactionID.get(i));
				transactionID = transactionID+accountId+",";
			}
			
			transactionID = RoleAppFunctions.removeUnwantedCommas(transactionID);
			return transactionID;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static boolean performCheckerOperationsOnSidePocketTrade(String OperationType){
		try {
			String buttonLocaor = "";
			if (OperationType.equalsIgnoreCase("Cancel")) {
				buttonLocaor = "//button[contains(@onclick,'cancel') and contains(normalize-space(),'Cancel')]";
			}
			if (!OperationType.equalsIgnoreCase("Cancel")) {
				buttonLocaor = "//button[contains(@onclick,'"+OperationType+"') and contains(normalize-space(),'"+OperationType+"')]";
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(buttonLocaor));
			if (!bStatus) {
				Messages.errorMsg = "Cannot click on "+OperationType+" button at Checker Screen";
				return false;
			}				
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
			if(OperationType.equalsIgnoreCase("Reject"))
			{
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Rejected - Comments By QA");
				Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:saveReasonForRejection')]"));
			}
			if(OperationType.equalsIgnoreCase("Return"))
			{
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Returned - Comments By QA");
				Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:saveReasonForRejection')]"));
			}
			bStatus = Wait.waitForAlert(Global.driver, 5);
			if(bStatus){
				String alertText = Alerts.getAlertMessage(Global.driver);
				bStatus = Alerts.closeAlert(Global.driver);
				if(!bStatus){
					Messages.errorMsg = "Cannot Perform Checker Operation "+OperationType+" .Cannot close Alert ";
					return false;
				}
				Messages.errorMsg = "Cannot Perform Checker Operation "+OperationType+" .Alert Present Alert Text : "+alertText;
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : "+OperationType+"ed Successfully but success message Not Displayed .]\n";
				return false;
			}
			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


}
