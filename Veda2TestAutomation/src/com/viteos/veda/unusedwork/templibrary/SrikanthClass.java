package com.viteos.veda.unusedwork.templibrary;

import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Constants;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeExchangeAppFunction;
import com.viteos.veda.master.lib.TradeTypeRedemptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.TradeTypeTransferAppFunctions;

public class SrikanthClass {
	public static boolean bStatus;
	public static String sAccountNo = "";
	public static String newAccountID = "";

	// functions to Trade search edit 
	public static boolean verfiyTradeSearchAndEdit(Map<String, String> mapTradeSearchDetails)
	{
		try {			
			if (mapTradeSearchDetails != null && !mapTradeSearchDetails.isEmpty()) {
				if (mapTradeSearchDetails.get("Subscription") != null && mapTradeSearchDetails.get("Subscription").equalsIgnoreCase("yes")) {
					bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(@href, 'Transaction_SUB')and contains(@href, 'modifyTrade')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[ Error: unable to click on the Edit button for TradeSearch Subscription]";
						return false;
					}
					bStatus = modifySubscriptionMaker(mapTradeSearchDetails);
					if (!bStatus) {
						Messages.errorMsg = "Error: Subscription canot be modified in Trade Search";
					}
				}				
				if (mapTradeSearchDetails.get("Redemption") != null && mapTradeSearchDetails.get("Redemption").equalsIgnoreCase("yes")) {
					bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(@href, 'Transaction_RED')and contains(@href, 'modifyTrade')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[Error: unable to click on the Edit button for TradeSearch Redemption]";
						return false;
					}
					Map<String, String> mapRedemptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sTradeSearchFilePath, "RedemptionTradeSearch", mapTradeSearchDetails.get("TestcaseName"));
					bStatus = modifyRedemptionMaker(mapTradeSearchDetails, mapRedemptionDetails);
					if (!bStatus) {
						Messages.errorMsg = "Error: Redemption canot be modified in Trade Search";
					}
				}				
				if (mapTradeSearchDetails.get("Transfer") != null && mapTradeSearchDetails.get("Transfer").equalsIgnoreCase("yes")) {
					bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(@href, 'Transaction_TRA')and contains(@href, 'modifyTrade')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[Error: unable to click on the Edit button for TradeSearch Transfer]";
						return false;
					}
					Map<String, String> mapRedemptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sTradeSearchFilePath, "TransferTradeSearch", mapTradeSearchDetails.get("TestcaseName"));
					bStatus = modifyRedemptionMaker(mapTradeSearchDetails, mapRedemptionDetails);
					if (!bStatus) {
						Messages.errorMsg = "Error: Transfer canot be modified in Trade Search";
					}
				}				
				if (mapTradeSearchDetails.get("Exchange") != null && mapTradeSearchDetails.get("Exchange").equalsIgnoreCase("yes")) {
					bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(@href, 'Transaction_EXC')and contains(@href, 'modifyTrade')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[Error: unable to click on the Edit button for TradeSearch Exchange]";
						return false;
					}
					Map<String, String> mapRedemptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sTradeSearchFilePath, "ExchangeTradeSearch", mapTradeSearchDetails.get("TestcaseName"));
					bStatus = modifyRedemptionMaker(mapTradeSearchDetails, mapRedemptionDetails);
					if (!bStatus) {
						Messages.errorMsg = "Error: Exchange canot be modified in Trade Search";
					}
				}				
				if (mapTradeSearchDetails.get("Feeder Subscription") != null && mapTradeSearchDetails.get("Feeder Subscription").equalsIgnoreCase("yes")) {
					bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(@href, 'Feeder SUB')and contains(@href, 'modifyTrade')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[Error: unable to click on the Edit button for TradeSearch Feeder Subscription]";
						return false;
					}
					bStatus = modifyFeederSubscriptionMaker(mapTradeSearchDetails);
					if (!bStatus) {
						Messages.errorMsg = "Error: Feeder Subscription canot be modified in Trade Search";
					}
				}				
				if (mapTradeSearchDetails.get("Feeder Redemption") != null && mapTradeSearchDetails.get("Feeder Redemption").equalsIgnoreCase("yes")) {
					bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(@href, 'Feeder RED')and contains(@href, 'modifyTrade')]//em"));
					if (!bStatus) {
						Messages.errorMsg = "[Error: unable to click on the Edit button for TradeSearch Feeder Redemption]";
						return false;
					}
					bStatus = modifyFeederSubscriptionMaker(mapTradeSearchDetails);
					if (!bStatus) {
						Messages.errorMsg = "Error: Feeder Redemption canot be modified in Trade Search";
					}
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	//************ function to modify Subscription at Maker************************

	public static boolean modifySubscriptionMaker(Map<String, String> mapTradeSearchDetails)
	{
		if (mapTradeSearchDetails.get("ActionType") !=null && mapTradeSearchDetails.get("Actiontype").equalsIgnoreCase("Edit")) {


			if (mapTradeSearchDetails.get("EffectiveDate") !=null) {
				bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@id='effectiveDate']"), mapTradeSearchDetails.get("Effective Date"));
				if (!bStatus) {
					Messages.errorMsg = "[ERROR: unable to modify the Effective Date in Trade Search Subscription]\n";
					return false;
				}
			}
			if (mapTradeSearchDetails.get("CashOrUnits") != null) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-isAmount']/span"));
				if(!bStatus){
					Messages.errorMsg = "[ERROR:Page Not Scrolled to Cash or Units Radio Button.]\n";
					return false;
				}
				if(mapTradeSearchDetails.get("CashOrUnits").equalsIgnoreCase("cash") && mapTradeSearchDetails.get("Cash") != null)
				{
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-isAmount']/span"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR: Cash Radio button cannot be selected in Trade Search Subscription]\n";
						return false;
					}
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapTradeSearchDetails.get("Cash"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR: Unable to modify Cash in Trade Search Subscription]\n";
						return false;
					}
				}

				/*	 bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Cash')]"));
				if(!bStatus){
					Messages.errorMsg = "Failed to Click on Cash Label";
					return false;
				}*/

				if(mapTradeSearchDetails.get("CashOrUnits").equalsIgnoreCase("Units") && mapTradeSearchDetails.get("Units") !=null){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-isUnits']/span"));
					if(!bStatus){
						Messages.errorMsg ="[ ERROR : Units Radio button not selected.]\n";
						return false;
					}
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='units']"), mapTradeSearchDetails.get("Units"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Units values Cannot be modified in Trade Search Subscription.]\n";
						return false;
					}
				}				
			}
		}
		return true;
	}

	//*************************************** function to modify Redemption at Maker **************************

	public static boolean modifyRedemptionMaker(Map<String, String> mapTradeSearchDetails, Map<String, String> mapRedemptionDetails)
	{
		try {
			if (mapTradeSearchDetails.get("ActionType") != null && mapTradeSearchDetails.get("Actiontype").equalsIgnoreCase("Edit"))
			{
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='effectiveDate']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Effective Date field is not visible in Trade Search Redemption Module. ]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Full Redemption']"));
				if(!bStatus){
					return false;
				}			
				if(mapTradeSearchDetails.get("Effective Date") != null){
					bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Effective Date']//following::input[contains(@id,'effectiveDate')]"), mapTradeSearchDetails.get("Effective Date"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Unable to modify Effective Date filed in Trade Search Redemption.]\n";
						return false;
					}
				}
				if(mapRedemptionDetails.get("AmountorShares") != null){
					if(mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-rbAmount']/span/input"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Amount Radio button cannot be selected in Trade Search Redemption.]\n";
							return false;
						}
						if(mapRedemptionDetails.get("Redemption Amount") != null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapRedemptionDetails.get("Redemption Amount"));
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Unable to modify Redemption Amount in Trade Search Redemption.]\n";
								return false;
							}
						}
					}
					if(mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-rbShares']/span/input"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Shares Radio button cannot be selected in Trade Search Redemption.]\n";
							return false;
						}
						if(mapRedemptionDetails.get("Share Value") != null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='unitValue']"), mapRedemptionDetails.get("Share Value"));
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Unable to modify Share Value in Trade Search Redemption.]\n";
								return false;
							}
						}
					}
				}
				if (mapRedemptionDetails != null && !mapRedemptionDetails.isEmpty() && (mapRedemptionDetails.get("Full Redemption") == null || mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("No"))) {
					bStatus = TradeTypeRedemptionAppFunctions.doFillAvailableBalanceAtMaker(mapRedemptionDetails);
					if (!bStatus) {
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

	//********************* function to modifyTransfer at Maker
	public static boolean modifyTransferMaker(Map<String, String> mapTradeSearchDetails)
	{
		try {			
			if (mapTradeSearchDetails.get("ActionType") !=null && mapTradeSearchDetails.get("Actiontype").equalsIgnoreCase("Edit"))
			{
				NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Transfer Details')]"));
				bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Transfer Details");
				if (!bStatus) {
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='effectiveDate']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Effective Date field is not visible in Trade Search Transfer Module. ]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Full Transfer']"));
				if(!bStatus){
					return false;
				}			
				if(mapTradeSearchDetails.get("Effective Date") != null){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapTradeSearchDetails.get("Effective Date"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Unable to modify Effective Date field in Trade Search Transfer.]\n";
						return false;
					}
				}
				if(mapTradeSearchDetails.get("AmountorShares")!=null){
					if(mapTradeSearchDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='TORA_radioButton']"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Amount Radio button cannot be selected in Trade Search Transfer.]\n";
							return false;
						}
						if(mapTradeSearchDetails.get("Transfer Amount")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapTradeSearchDetails.get("Transfer Amount"));
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Unable to modify Transfer Amount in Trade Search Transfer.]\n";
								return false;
							}
						}
					}
					if(mapTradeSearchDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='TORS_radioButton']"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Shares Radio button cannot be selected in Trade Search Transfer.]\n";
							return false;
						}
						if(mapTradeSearchDetails.get("Share Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='shares']"), mapTradeSearchDetails.get("Share Value"));
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Unable to modify Share Value in Trade Search Transfer.]\n";
								return false;
							}
						}
					}
				}
				bStatus = TradeTypeTransferAppFunctions.doMakerFillAvailableBalanceDetails(mapTradeSearchDetails);
				if (!bStatus) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	//******************************************	function to modify Exchange at Maker*****************************
	public static boolean modifyExchangeMaker(Map<String, String> mapTradeSearchDetails)
	{
		try {			
			if (mapTradeSearchDetails.get("ActionType") !=null && mapTradeSearchDetails.get("Actiontype").equalsIgnoreCase("Edit"))
			{
				NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Fund Details')]//span[text()='5']"));
				bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Fund Details");
				if (!bStatus) {
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='effectiveDate']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Effective Date field is not visible in Trade Search Exchange Module. ]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Full Exchange']"));
				if(!bStatus){
					return false;
				}			
				if(mapTradeSearchDetails.get("Effective Date") != null){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapTradeSearchDetails.get("Effective Date"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Unable to modify Effective Date field in Trade Search Exchange.]\n";
						return false;
					}
				}
				if(mapTradeSearchDetails.get("AmountorShares")!=null){
					if(mapTradeSearchDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare1']"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Amount Radio button cannot be selected in Trade Search Exchange.]\n";
							return false;
						}
						if(mapTradeSearchDetails.get("Exchange Amount")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapTradeSearchDetails.get("Exchange Amount"));
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Unable to modify Transfer Amount in Trade Search Exchange.]\n";
								return false;
							}
						}
					}
					if(mapTradeSearchDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare2']"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Shares Radio button cannot be selected in Trade Search Exchange.]\n";
							return false;
						}
						if(mapTradeSearchDetails.get("Share Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='share']"), mapTradeSearchDetails.get("Share Value"));
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Unable to modify Share Value in Trade Search Exchange.]\n";
								return false;
							}
						}
					}
				}
				bStatus= TradeTypeExchangeAppFunction.doFillAvailableBalanceAtMaker(mapTradeSearchDetails);
				if (!bStatus) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		

	}

	//******************** function to modify Switch at Maker ***************************
	public static boolean modifySwitchMaker(Map<String, String> mapTradeSearchDetails)
	{
		try {			
			if (mapTradeSearchDetails.get("ActionType") !=null && mapTradeSearchDetails.get("Actiontype").equalsIgnoreCase("Edit"))
			{
				NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Switch Details')]//span[text()='5']"));
				bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Switch Details");
				if (!bStatus) {
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='effectiveDate']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Effective Date field is not visible in Trade Search Switch Module. ]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Full Switch']"));
				if(!bStatus){
					return false;
				}			
				if(mapTradeSearchDetails.get("Effective Date") != null){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapTradeSearchDetails.get("Effective Date"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Unable to modify Effective Date field in Trade Search Switch.]\n";
						return false;
					}
				}
				if(mapTradeSearchDetails.get("AmountorShares")!=null){
					if(mapTradeSearchDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare1']"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Amount Radio button cannot be selected in Trade Search Switch.]\n";
							return false;
						}
						if(mapTradeSearchDetails.get("Switch Amount")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapTradeSearchDetails.get("Switch Amount"));
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Unable to modify Transfer Amount in Trade Search Switch.]\n";
								return false;
							}
						}
					}
					if(mapTradeSearchDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare2']"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Shares Radio button cannot be selected in Trade Search Switch.]\n";
							return false;
						}
						if(mapTradeSearchDetails.get("Share Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='share']"), mapTradeSearchDetails.get("Share Value"));
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Unable to modify Share Value in Trade Search Switch.]\n";
								return false;
							}
						}
					}
				}
				/*bStatus= TradeTypeSwitchAppFunctions.doMakerFillAvailableBalanceDetails(mapTradeSearchDetails);
				if (!bStatus) {
					return false;
				}*/
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//********************* function to modify Feeder subscription at Maker ***************
	public static boolean modifyFeederSubscriptionMaker(Map<String, String> mapTradeSearchDetails)
	{
		try {			
			if (mapTradeSearchDetails.get("ActionType") !=null && mapTradeSearchDetails.get("Actiontype").equalsIgnoreCase("Edit"))
			{
				bStatus = Wait.waitForElementVisibility(Global.driver, By.id("datepicker1"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Effective Date field is not visible in Trade Search Feeder Subscription. ]\n";
					return false;
				}
				if(mapTradeSearchDetails.get("Effective Date") != null){
					bStatus = Elements.enterText(Global.driver, By.id("datepicker1"), mapTradeSearchDetails.get("Effective Date"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Unable to modify Effective Date field in Trade Search Feeder Subscription.]\n";
						return false;
					}
				}
				if (mapTradeSearchDetails.get("CashOrUnits") != null) {
					if(mapTradeSearchDetails.get("CashOrUnits").equalsIgnoreCase("cash") && mapTradeSearchDetails.get("Cash") != null)
					{
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapTradeSearchDetails.get("Cash"));
						if(!bStatus){
							Messages.errorMsg = "[ERROR: Unable to modify Cash in Trade Search Feeder Subscription]\n";
							return false;
						}
					}
					if(mapTradeSearchDetails.get("CashOrUnits").equalsIgnoreCase("Units") && mapTradeSearchDetails.get("Units") !=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='units']"), mapTradeSearchDetails.get("Units"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Units values Cannot be modified in Trade Search Feeder Subscription.]\n";
							return false;
						}
					}
					Elements.click(Global.driver, By.xpath("//label[contains(normalize-space(), 'Units')]"));
				}				
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}

	//*********************function to modify Feeder Redemption at Maker*************************
	public static boolean modifyFeederRedemptionMaker(Map<String, String> mapTradeSearchDetails)
	{
		try {			
			if (mapTradeSearchDetails.get("ActionType") !=null && mapTradeSearchDetails.get("Actiontype").equalsIgnoreCase("Edit"))
			{
				bStatus = Wait.waitForElementVisibility(Global.driver, By.id("datepicker1"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Effective Date field is not visible in Trade Search Feeder Redemption. ]\n";
					return false;
				}
				if(mapTradeSearchDetails.get("Effective Date") != null){
					bStatus = Elements.enterText(Global.driver, By.id("datepicker1"), mapTradeSearchDetails.get("Effective Date"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Unable to modify Effective Date field in Trade Search Feeder Redemption.]\n";
						return false;
					}
				}
				if (mapTradeSearchDetails.get("CashOrUnits") != null) {
					if(mapTradeSearchDetails.get("CashOrUnits").equalsIgnoreCase("cash") && mapTradeSearchDetails.get("Cash") != null)
					{
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapTradeSearchDetails.get("Cash"));
						if(!bStatus){
							Messages.errorMsg = "[ERROR: Unable to modify Cash in Trade Search Feeder Redemption]\n";
							return false;
						}
					}
					if(mapTradeSearchDetails.get("CashOrUnits").equalsIgnoreCase("Units") && mapTradeSearchDetails.get("Units") !=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='units']"), mapTradeSearchDetails.get("Units"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Units values Cannot be modified in Trade Search Feeder Redemption.]\n";
							return false;
						}
					}
					Elements.click(Global.driver, By.xpath("//label[contains(normalize-space(), 'Units')]"));
				}				
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}	

	//Function for Checker Operations.
	//function to modify Subscription at Checker.
	public static boolean modifySubscriptionChecker(Map<String, String> mapTradeSearchDetails)
	{
		try {
			if (mapTradeSearchDetails.get("ActionType") !=null && mapTradeSearchDetails.get("Actiontype").equalsIgnoreCase("Edit"))
			{

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false ;
		}
		return true;
	}


	//***********************function to performOperationssOnTradeSearch
	public static boolean performOperationsOnTradeSerach(Map<String, String> mapTradeSearchDetails)
	{
		if (mapTradeSearchDetails.get("OperationType") != null) {
			if (mapTradeSearchDetails.get("OperationType").equalsIgnoreCase("save")) {
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick, 'SAVE')]"));
				if (!bStatus) {
					Messages.errorMsg = "Error: Unable to Save the modified Trade Search]";
					return false;
				}
			}
			if (mapTradeSearchDetails.get("OperationType").equalsIgnoreCase("Cancel Trade")) {
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick, 'cancelTrade')]"));
				if (!bStatus) {
					Messages.errorMsg = "[Erro: unable to Cancel the Trade in Trade Search]";
					return false;
				}				
			}
			if(mapTradeSearchDetails.get("OperationType").equalsIgnoreCase("Delete Trade"))
			{
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick, 'deleteTrade')]"));
				if (!bStatus) {
					Messages.errorMsg = "[Error: Unable to Delete Trade in Trade Search]";
					return false;
				}
			}
			if (mapTradeSearchDetails.get("OperationType").equalsIgnoreCase("Cancel")) {
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick, 'searchResult')]"));
				if (!bStatus) {
					Messages.errorMsg = "[Error: Unable to click on cancel in Trade Search]";
					return false;
				}
			}						
		}
		return true;
	}
}