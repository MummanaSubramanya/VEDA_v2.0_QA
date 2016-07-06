package com.viteos.veda.unusedwork.templibrary;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Constants;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.InvestorMasterAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.OpeningBalanceMasterAppFunctions;

public class MunniClass {
	static boolean bStatus;

	public static boolean createRedemptionTrade(Map<String, String> redemptionMapDetails){
		try{

			if(redemptionMapDetails.get("Received Date")!=null){

				bStatus = Elements.enterText(Global.driver, By.id("orderRedemptionMasters0.requestDate"),redemptionMapDetails.get("Received Date"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Received Date")+" value cannjot be entered in the textbox";
					return false;
				}
			}

			if(redemptionMapDetails.get("Received Time")!=null){
				bStatus = Elements.enterText(Global.driver, By.id("orderRedemptionMasters0.requestTime"),redemptionMapDetails.get("Received Time"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Received Time")+" value cannot be entered in the textbox";
					return false;
				}
			}

			if(redemptionMapDetails.get("Received Office")!=null){
				bStatus = Elements.enterText(Global.driver, By.id("orderRedemptionMasters0.orderReceivedOffice"),redemptionMapDetails.get("Received Office"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Received Office")+" value cannot be entered in the textbox";
					return false;
				}
			}

			if(redemptionMapDetails.get("Time Zone")!=null){
				bStatus = Elements.enterText(Global.driver, By.id("orderRedemptionMasters0.orderReceivedOffice"),redemptionMapDetails.get("Time Zone"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Time Zone")+" value cannot be entered in the textbox";
					return false;
				}
			}

			if(redemptionMapDetails.get("Mode of Request")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Mode of Request", redemptionMapDetails.get("Mode of Request"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Mode of Request")+" value cannot be selected from dropdown";
					return false;
				}
			}

			if(redemptionMapDetails.get("Investor Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Investor Name", redemptionMapDetails.get("Investor Name"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Investor Name")+" value cannot be selected from dropdown";
					return false;
				}
			}

			if(redemptionMapDetails.get("Holder Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Holder Name", redemptionMapDetails.get("Holder Name"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Holder Name")+" value cannot be selected from dropdown";
					return false;
				}
			}

			if(redemptionMapDetails.get("Account Id")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Account Id", redemptionMapDetails.get("Account Id"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Account Id")+" value cannot be selected from dropdown";
					return false;
				}
			}


			if(redemptionMapDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", redemptionMapDetails.get("Client Name"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Client Name")+" value cannot be selected from dropdown";
					return false;
				}
			}

			if(redemptionMapDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", redemptionMapDetails.get("Fund Family Name"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Fund Family Name")+" value cannot be selected from dropdown";
					return false;
				}
			}

			if(redemptionMapDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", redemptionMapDetails.get("Legal Entity Name"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Legal Entity Name")+" value cannot be selected from dropdown";
					return false;
				}
			}

			if(redemptionMapDetails.get("Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Class Name", redemptionMapDetails.get("Class Name"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Class Name")+" value cannot be selected from dropdown";
					return false;
				}
			}

			if(redemptionMapDetails.get("Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Series Name", redemptionMapDetails.get("Series Name"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Series Name")+" value cannot be selected from dropdown";
					return false;
				}
			}

			// Full redemption Between Fund Families
			if(redemptionMapDetails.get("Full Redemption")!=null){
				if(redemptionMapDetails.get("Full Redemption").equalsIgnoreCase("Yes")){
					bStatus= Elements.click(Global.driver,By.id("isTotalYes"));
					if(!bStatus){
						Messages.errorMsg = "Full Redemption Radio button Yes is not Clicked ";
						return false;
					}
				}
				if(redemptionMapDetails.get("Full Redemption").equalsIgnoreCase("No")){
					bStatus= Elements.click(Global.driver,  By.id("isTotalNo"));
					if(!bStatus){
						Messages.errorMsg = "Full Redemption Radio button No is not Clicked ";
						return false;
					}
				}
			}

			if(redemptionMapDetails.get("Effective Date")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("orderRedemptionMasters0.effectiveDate"),redemptionMapDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Effective Date")+" value cannot be selected from dropdown";
					return false;
				}
			}

			if(redemptionMapDetails.get("Percentage")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("percentage"),redemptionMapDetails.get("Percentage"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Percentage")+" value cannot be Entered in the TextBox";
					return false;
				}
			}

			if(redemptionMapDetails.get("AmountOrShare")!=null){
				if(redemptionMapDetails.get("AmountOrShare").equalsIgnoreCase("Amount")){
					bStatus= Elements.click(Global.driver,By.id("rbAmount"));
					if(!bStatus){
						Messages.errorMsg = "Amount Radio button is not Clicked ";
						return false;
					}
				}
				if(redemptionMapDetails.get("AmountOrShare").equalsIgnoreCase("Shares")){
					bStatus= Elements.click(Global.driver,  By.id("rbShares"));
					if(!bStatus){
						Messages.errorMsg = " Shares Radio button No is not Clicked ";
						return false;
					}
				}
			}

			if(redemptionMapDetails.get("Redemption Amount")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("amountValue"),redemptionMapDetails.get("Redemption Amount"));
				if(!bStatus){
					Messages.errorMsg = redemptionMapDetails.get("Redemption Amount")+" value cannot be selected from dropdown";
					return false;
				}
			}

			bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:getRedDetails')]"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Proceed Button";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[text()='Redemption Method Applied']"),Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Redemption Page is not displyed after clicking on Proceed button";
				return false;
			}







			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//Maker filling Available Balance Details.
	public static boolean doMakerFillAvailableBalanceDetails(Map<String, String> mapTransferDetails){
		int noOfAmountOrShareLots = 0;
		List<String> sAmountOrSharesList = null;
		List<String> sAmountOrSharesIndexList = null;
		List<String> sAmountOrSharesIndexesEditableStatusList = null;
		try {
			String sIsAmountOrShare = "";
			if (mapTransferDetails.get("Redemption Method Applied") != null) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(text(),'Available Balance')]"));
				if(!bStatus){
					Messages.errorMsg = "Page cannot scroll to Available Balance Details";
					return false;
				}
			}
			if (mapTransferDetails.get("TaxLotsAmounts") != null || mapTransferDetails.get("TaxLotsShares") != null) {
				if (mapTransferDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					sAmountOrSharesList =  Arrays.asList(mapTransferDetails.get("TaxLotsAmounts").split(","));					
				}
				else if (mapTransferDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					sAmountOrSharesList =  Arrays.asList(mapTransferDetails.get("TaxLotsShares").split(","));					
				}
			}
			if (mapTransferDetails.get("TaxLotsIndexes") != null) {
				sAmountOrSharesIndexList = Arrays.asList(mapTransferDetails.get("TaxLotsIndexes").split(","));
			}
			if (mapTransferDetails.get("IsTaxLotsAmountOrShareEditable") != null) {
				sAmountOrSharesIndexesEditableStatusList = Arrays.asList(mapTransferDetails.get("IsTaxLotsAmountOrShareEditable").split(","));
				noOfAmountOrShareLots = sAmountOrSharesIndexesEditableStatusList.size();
			}
			if (!sIsAmountOrShare.equalsIgnoreCase("")) {				
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-amountShareCheckBox')]"), 3);
				if(!bStatus && noOfAmountOrShareLots != 0){
					Messages.errorMsg = "Tax lots are not visible";
					return false;
				}
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-amountShareCheckBox')]"));				
				//int iNoOfLots = Integer.parseInt(NoOfRecords);
				if (NoOfRecords != noOfAmountOrShareLots) {
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+noOfAmountOrShareLots+"' is not matching with Actual no of Lots : '"+NoOfRecords+"' for Transfer Available Balance]\n";
					return false;
				}
				for (int i = 0; i < noOfAmountOrShareLots; i++) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='amountShareCheckBox_"+i+"']//parent::span[@class='checked']"), 3);
					if (sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes")) {
						if (sAmountOrSharesIndexList != null && !sAmountOrSharesIndexList.contains(String.valueOf(i)) && bStatus) {
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='amountShareCheckBox_"+i+"']//parent::span[@class='checked']"));
							if (!bStatus) {
								Messages.errorMsg = "[ERROR : Unable to unselect the Transfer Available Balance tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
								return false;
							}
						}
						if (sAmountOrSharesIndexList != null && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
							if (!bStatus) {
								bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Transfer')]"));
								bStatus = Elements.click(Global.driver, By.xpath("//input[@id='amountShareCheckBox_"+i+"']"));
								if (!bStatus) {
									Messages.errorMsg = "[ERROR : Unable to unselect the Transfer Available Balance tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
									return false;
								}
							}
							By locator = By.id("allocated"+sIsAmountOrShare+"_"+i+"");
							bStatus = ClearAndSetText(locator, sAmountOrSharesList.get(i));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to enter the text into the Transfer Available Balance tax lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
								return false;
							}
						}							
					}
					if (!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes") && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
						bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Transfer Available Balance tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.id("allocated"+sIsAmountOrShare+"_"+i+""), sAmountOrSharesList.get(i), "No", true);
						if (!bStatus) {
							return false;
						}							
					}
					if(!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes") && !sAmountOrSharesIndexList.contains(String.valueOf(i)) && bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='amountShareCheckBox_"+i+"']//parent::span[@class='checked']"));
						if (!bStatus) {
							Messages.errorMsg = "[ERROR : Unable to unselect the Transfer Available Balance tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
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

	//Maker Verifying Available balance Details.
	public static boolean doMakerVerifyAvailableBalanceDetails(Map<String, String> mapTransferDetails){
		try {
			boolean bValidStatus = true;
			String appendErrMsg = "";
			if(mapTransferDetails.get("ExpectedActualBalance") != null && mapTransferDetails.get("ExpectedActualShares") != null && mapTransferDetails.get("ExpectedAvailableAmount") != null && mapTransferDetails.get("ExpectedAvailableShares") != null && mapTransferDetails.get("ExpectedIsUnderLockupStatus") != null)
			{
				List<String> actualBalance = Arrays.asList(mapTransferDetails.get("ExpectedActualBalance").split(","));
				List<String> actualShares = Arrays.asList(mapTransferDetails.get("ExpectedActualShares").split(","));
				List<String> availableAmount = Arrays.asList(mapTransferDetails.get("ExpectedAvailableAmount").split(","));
				List<String> avaialableShares = Arrays.asList(mapTransferDetails.get("ExpectedAvailableShares").split(","));				

				int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-amountShareCheckBox')]"));				
				if(xpathCount != actualBalance.size()){
					Messages.errorMsg = "Actual Count of Tax lots in Transfer is "+xpathCount+" , which is not matching with the Expected Atual Balance Count "+actualBalance.size()+" ";
					return false;
				}

				for(int i=0 ; i<actualBalance.size() ; i++){
					if(!actualBalance.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Actual Balance' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td[5]"), actualBalance.get(i), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Actual Shares' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td[6]"), actualShares.get(i), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Available Amount' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td[7]//input"), availableAmount.get(i), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Available Shares' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td[8]//input"), avaialableShares.get(i), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}					
				}
			}

			if(mapTransferDetails.get("ExpectedTotalActualBalance") != null && mapTransferDetails.get("ExpectedTotalActualShares") != null && mapTransferDetails.get("ExpectedTotalAvailableAmount") != null && mapTransferDetails.get("ExpectedTotalAvailableShares") != null){
				int lotsCountInclusiveOfTotalsRow = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr"));
				if(!mapTransferDetails.get("ExpectedTotalActualBalance").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '2' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[2]//input"), mapTransferDetails.get("ExpectedTotalActualBalance"), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalActualShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '3' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[3]//input"), mapTransferDetails.get("ExpectedTotalActualShares"), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalAvailableAmount").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '4' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[4]//input"), mapTransferDetails.get("ExpectedTotalAvailableAmount"), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalAvailableShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '5' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[5]//input"), mapTransferDetails.get("ExpectedTotalAvailableShares"), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
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

	//Checker Verify Available Balance Details.
	public static boolean doCheckerVerifyTransferAvailableBalanceDetails(Map<String , String> mapTransferDetails){
		try {
			boolean bValidStatus = true;
			String appendErrMsg = "";
			if(mapTransferDetails.get("ExpectedActualBalance") != null && mapTransferDetails.get("ExpectedActualShares") != null && mapTransferDetails.get("ExpectedAvailableAmount") != null && mapTransferDetails.get("ExpectedAvailableShares") != null && mapTransferDetails.get("ExpectedIsUnderLockupStatus") != null)
			{
				List<String> actualBalance = Arrays.asList(mapTransferDetails.get("ExpectedActualBalance").split(","));
				List<String> actualShares = Arrays.asList(mapTransferDetails.get("ExpectedActualShares").split(","));
				List<String> availableAmount = Arrays.asList(mapTransferDetails.get("ExpectedAvailableAmount").split(","));
				List<String> avaialableShares = Arrays.asList(mapTransferDetails.get("ExpectedAvailableShares").split(","));				

				int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td[7]//span"));				
				if(xpathCount != actualBalance.size()){
					Messages.errorMsg = "Actual Count of Tax lots is "+xpathCount+" is not matching with the Expected Atual Balance Count "+actualBalance.size()+" ";
					return false;
				}

				for(int i=0 ; i<actualBalance.size() ; i++){
					if(!actualBalance.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Actual Balance' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td[4]//span"), actualBalance.get(i), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Actual Shares' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td[5]//span"), actualShares.get(i), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Available Amount' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td[6]//span"), availableAmount.get(i), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Available Shares' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td[7]//span"), avaialableShares.get(i), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					/*if(!isUnderLockup.get(i).equalsIgnoreCase("None")){
							String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='isUnderLockup_"+i+"']//parent::td")).trim().replaceAll(",", "");
							if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !isUnderLockup.get(i).equalsIgnoreCase(actualValue)){
								appendErrMsg = appendErrMsg+"[ Is Under Lockup Expected value "+isUnderLockup.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
								bValidStatus = false;
							}
						}*/
				}
			}

			if(mapTransferDetails.get("ExpectedTotalActualBalance") != null && mapTransferDetails.get("ExpectedTotalActualShares") != null && mapTransferDetails.get("ExpectedTotalAvailableAmount") != null && mapTransferDetails.get("ExpectedTotalAvailableShares") != null){
				int lotsCountInclusiveOfTotalsRow = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr"));
				if(!mapTransferDetails.get("ExpectedTotalActualBalance").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '2' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[2]//span"), mapTransferDetails.get("ExpectedTotalActualBalance"), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalActualShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '3' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[3]//span"), mapTransferDetails.get("ExpectedTotalActualShares"), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalAvailableAmount").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '4' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[4]//span"), mapTransferDetails.get("ExpectedTotalAvailableAmount"), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalAvailableShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '5' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[5]//span"), mapTransferDetails.get("ExpectedTotalAvailableShares"), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
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

	//Maker and Checker verifying Pending Trades Common function for both.
	public static boolean doMakerAndCheckerVerifyPendingTradesDetails(String sPTRequestedAmounts, String sPTRequestedShares, int iNoOfDecimalsExpected){
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		int noOfAmountOrShareLots = 0;
		List<String> sAmountsList = null;
		List<String> sSharesList = null;
		try {
			sAmountsList = Arrays.asList(sPTRequestedAmounts.split(","));
			sSharesList = Arrays.asList(sPTRequestedShares.split(","));
			if (sAmountsList.size() != sSharesList.size()) {
				Messages.errorMsg = "[ Test Data issue - ERROR : Test data wasn't given properly please check. Given Amount and Shares lists are not equal.]";
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]"));
			if(!bStatus){
				Messages.errorMsg = "Page cannot scroll to Pending Trades Details";
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr"), 4);			
			noOfAmountOrShareLots = Elements.getXpathCount(Global.driver, By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr"));
			if (!bStatus && sAmountsList.size() != noOfAmountOrShareLots) {
				Messages.errorMsg = "[ ERROR : Actual No Of Records for 'Pending Trades' : '"+noOfAmountOrShareLots+"' in Transfer screen is not matching with Expected no : '"+sAmountsList.size()+"' ]";
				return false;
			}
			for (int i = 1; i <= sAmountsList.size(); i++) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Pending Trade' in Transfer screen for lot 'Amount' value at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr["+i+"]//td[7]"), sAmountsList.get(i), "Yes", true, iNoOfDecimalsExpected);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Pending Trade' in Transfer screen for lot 'Share' value at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr["+i+"]//td[8]"), sSharesList.get(i), "Yes", true, iNoOfDecimalsExpected);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
		}		
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = sAppendErrorMsg;
		return bValidateStatus;
	}

	public static boolean ClearAndSetText(By by, String text)
	{
		try {
			WebElement element = Global.driver.findElement(by);
			Actions navigator = new Actions(Global.driver);
			navigator.click(element)
			.sendKeys(Keys.END)
			.keyDown(Keys.SHIFT)
			.sendKeys(Keys.HOME)
			.keyUp(Keys.SHIFT)
			.sendKeys(Keys.BACK_SPACE)
			.sendKeys(text)
			.perform();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	public static boolean doVerifyPageLoaded(){
		try {
			JavascriptExecutor js = (JavascriptExecutor)Global.driver; 
			if (js.executeScript("return document.readyState").toString().equals("complete"))
			{ 				
				return true; 
			}
			System.out.println("Page still loading....!!!");
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean doWaitUntillPageLoad(int iterations, int sleepInSEC){
		try {
			for (int i = 0; i < iterations; i++) {
				if (!doVerifyPageLoaded()) {
					TimeUnit.SECONDS.sleep(sleepInSEC);
					continue;
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**************************************   FEE DIS FUNS *********************************************/

	public static boolean doVerifyGPorIMDetails(Map<String, String> mapFeeDistribution){
		try {
			String sInvestorsTestDataFilePath = Global.sInvestorTestDataFilePath;
			String sSheetName = "FeeDistributionData";
			if (InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag = true) {
				sInvestorsTestDataFilePath = Global.sInvestorModifyTestDataFilePath;
				sSheetName = "ModifyFeeDistributionData";
			}
			if (mapFeeDistribution != null && !mapFeeDistribution.isEmpty() && mapFeeDistribution.get("GPorIMReferenceTC") != null && !mapFeeDistribution.get("GPorIMReferenceTC").equalsIgnoreCase("None")) {
				List<String> sTCListOfNoOfGPOrIMs = Arrays.asList(mapFeeDistribution.get("GPorIMReferenceTC").split(","));
				for (int gpOrIMIndex = 0; gpOrIMIndex < sTCListOfNoOfGPOrIMs.size(); gpOrIMIndex++) {					
					Map<String, String> mapGPorIMDetails = ExcelUtils.readDataABasedOnCellName(sInvestorsTestDataFilePath, sSheetName, sTCListOfNoOfGPOrIMs.get(gpOrIMIndex).trim());
					bStatus = doVerifySelectedFundDetailsForRespectiveGPorIM(mapGPorIMDetails, gpOrIMIndex);
					if (!bStatus) {
						return false;
					}				
					List<String> sFeePercentagesListOfEachDistribution = null;
					List<String> sFromDatesListOfEachDistribution = null;
					List<String> sToDatesListOfEachDistribution = null;
					if (mapGPorIMDetails.get("Fee Percentage") != null && mapGPorIMDetails.get("DateFrom") != null && mapGPorIMDetails.get("DateTo") != null) {
						sFeePercentagesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("Fee Percentage").split(":"));
						sFromDatesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("DateFrom").split(":"));
						sToDatesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("DateTo").split(":"));
					}

					List<String> sProvisionForDiffermentList = Arrays.asList(mapGPorIMDetails.get("Provision for Deferment").split(":"));
					List<String> sCalculateFeeList = Arrays.asList(mapGPorIMDetails.get("Calculate Fee").split(":"));

					for (int DistributionIndex = 0; DistributionIndex < sProvisionForDiffermentList.size(); DistributionIndex++) {						
						bStatus = doVerifyRespectiveIndexGPorIMFeeDistributionDetails(sFeePercentagesListOfEachDistribution, sFromDatesListOfEachDistribution, sToDatesListOfEachDistribution, gpOrIMIndex, DistributionIndex);
						if (!bStatus) {
							return false;
						}
						if (sProvisionForDiffermentList.get(DistributionIndex) != null){
							if(sProvisionForDiffermentList.get(DistributionIndex).equalsIgnoreCase("Yes")) {
								bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.defermentPercentageDetails0.isDeferment1')]//span[@class='checked']"), Constants.lTimeOut);
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Radio button for 'Provision For Differement' => 'YES' of GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' wasn't marked as selected.]\n";
									return false;
								}
								List<String> sDefermentAllowedLists = Arrays.asList(mapGPorIMDetails.get("DefermentAllowed").split(":"));
								List<String> sSidePocketAllowedLists = Arrays.asList(mapGPorIMDetails.get("SidePocketAllowed").split(":"));
								List<String> sDefermentDateFromLists = Arrays.asList(mapGPorIMDetails.get("Deferment Date From").split(":"));
								List<String> sDefermentDateToLists = Arrays.asList(mapGPorIMDetails.get("Deferment Date To").split(":"));
								List<String> sDefermentPartPnLLists = Arrays.asList(mapGPorIMDetails.get("Deferment Participient PL").split(":"));

								List<String> sDefermentAllowedSubList = Arrays.asList(sDefermentAllowedLists.get(DistributionIndex).split(","));
								List<String> sSidePocketAllowedSubList = Arrays.asList(sSidePocketAllowedLists.get(DistributionIndex).split(","));
								List<String> sDefermentDateFromSubList = Arrays.asList(sDefermentDateFromLists.get(DistributionIndex).split(","));
								List<String> sDefermentDateToSubList = Arrays.asList(sDefermentDateToLists.get(DistributionIndex).split(","));
								List<String> sDefermentPartPnLSubList = Arrays.asList(sDefermentPartPnLLists.get(DistributionIndex).split(","));

								for (int DefermentIndex = 0; DefermentIndex < sDefermentAllowedSubList.size(); DefermentIndex++) {									
									bStatus = doVerifyDefermentDetailsOfDistributionWithRespectiveIndexes(mapGPorIMDetails, sDefermentAllowedSubList.get(DefermentIndex), sSidePocketAllowedSubList.get(DefermentIndex), sDefermentDateFromSubList.get(DefermentIndex), sDefermentDateToSubList.get(DefermentIndex), sDefermentPartPnLSubList.get(DefermentIndex), gpOrIMIndex, DistributionIndex, DefermentIndex);
									if (!bStatus) {
										return false;
									}
								}
							}
							if (sProvisionForDiffermentList.get(DistributionIndex).equalsIgnoreCase("No")) {
								bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.defermentPercentageDetails0.isDeferment2')]//span[@class='checked']"), Constants.lTimeOut);
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Radio button for 'Provision For Differement' => 'NO' of GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' wasn't marked as selected.]\n";
									return false;
								}
							}
							if (sCalculateFeeList.get(DistributionIndex).equalsIgnoreCase("Yes")) {
								bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.isFeeCalculation1')]//span[@class='checked']"), Constants.lTimeOut);
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Radio button 'Calculate Fee' => 'Yes' for GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' wasn't marked as selected.]\n";
									return false;
								}
							}
							if (sCalculateFeeList.get(DistributionIndex).equalsIgnoreCase("No")) {
								bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.isFeeCalculation2')]//span[@class='checked']"), Constants.lTimeOut);
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Radio button 'Calculate Fee' => 'NO' for GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' wasn't marked as selected.]\n";
									return false;
								}
							}
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doVerifyDefermentDetailsOfDistributionWithRespectiveIndexes(Map<String, String> mapGPorIMDetails, String sDefermentAllowedVal,	String sSidePocketAllowedVal,	String sDefermentDateFromVal,	String sDefermentDateToVal, String sDefermentPartPnLVal, int iGPorIMIndex, int iDistributionIndex, int iDefermentIndex) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (sDefermentAllowedVal != null && !sDefermentAllowedVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercent']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the field 'Deferment % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";					
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Deferment % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' ", By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercent']"), sDefermentAllowedVal.trim(), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sSidePocketAllowedVal != null && !sSidePocketAllowedVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".sidePocketPercent']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the field 'Side Pocket % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Side Pocket % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' ", By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".sidePocketPercent']"), sSidePocketAllowedVal.trim(), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sDefermentDateFromVal != null && !sDefermentDateFromVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"startDate']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the field 'Deferment Date From' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Deferment Date From' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' ", By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".sidePocketPercent']"), sDefermentDateFromVal.trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sDefermentDateToVal != null && !sDefermentDateToVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"endDate']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the field 'Deferment Date To' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Deferment Date To' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' ", By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".sidePocketPercent']"), sDefermentDateToVal.trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}				
			}
			if (sDefermentPartPnLVal != null && !sDefermentPartPnLVal.trim().equalsIgnoreCase("None")) {
				if (sDefermentPartPnLVal.trim().equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercentageDetailsMappings0.isDefermentPnl1')]//span[@class='checked']"), Constants.lTimeOut);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : 'Deferment Participient P&L' => 'YES' button for the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' wasn't marked as selected.]\n";
						Messages.errorMsg = sAppendErrorMsg;
						return bValidateStatus = false;
					}
					if (mapGPorIMDetails != null && !mapGPorIMDetails.isEmpty() && mapGPorIMDetails.get("Participient FromDate") != null && mapGPorIMDetails.get("Participient ToDate") != null) {
						List<String> sGPorIMParticipientFromDateLists = Arrays.asList(mapGPorIMDetails.get("Participient FromDate").split(":"));
						List<String> sGPorIMParticipientToDateLists = Arrays.asList(mapGPorIMDetails.get("Participient ToDate").split(":"));

						List<String> sDistributionParticipientFromDateLists = Arrays.asList(sGPorIMParticipientFromDateLists.get(iGPorIMIndex).split(";"));
						List<String> sDistributionParticipientToDateLists = Arrays.asList(sGPorIMParticipientToDateLists.get(iGPorIMIndex).split(";"));

						List<String> sParticipientFromDateList = Arrays.asList(sDistributionParticipientFromDateLists.get(iDistributionIndex).split(","));
						List<String> sParticipientToDateList = Arrays.asList(sDistributionParticipientToDateLists.get(iDistributionIndex).split(","));

						for (int iParticipientIndex = 0; iParticipientIndex < sParticipientFromDateList.size(); iParticipientIndex++) {
							bStatus = doVerifyDefermentPnLDatesOfRespectiveIndexes(sParticipientFromDateList.get(iParticipientIndex),sParticipientToDateList.get(iParticipientIndex), iGPorIMIndex, iDistributionIndex, iDefermentIndex, iParticipientIndex);
							if (!bStatus) {
								return false;
							}
						}						
					}					
				}
				if (sDefermentPartPnLVal.trim().equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercentageDetailsMappings0.isDefermentPnl2')]//span[@class='checked']"), Constants.lTimeOut);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : 'Deferment Participient P&L' => 'NO' button for the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' wasn't marked as selected.]\n";
						Messages.errorMsg = sAppendErrorMsg;
						return bValidateStatus = false;
					}
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyDefermentPnLDatesOfRespectiveIndexes(String sDefPnLFromDate, String sDefPnLToDate, int iGPorIMIndex, int iDistributionIndex, int iDefermentIndex, int iDefermentPnLIndex) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (sDefPnLFromDate != null && !sDefPnLFromDate.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLFromDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the 'From Date' field at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'From Date' at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' ", By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLFromDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"), sDefPnLFromDate.trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sDefPnLToDate != null && !sDefPnLToDate.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLToDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the 'To Date' field at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'To Date' at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' ", By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLToDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"), sDefPnLToDate.trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifySelectedFundDetailsForRespectiveGPorIM(Map<String, String> mapGPorIMDetails, int indexOfGPorIM){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'s2id_clientid_')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = " ERROR : Fee Distribution Details are not displayed in Investor Tab when selected GP/IM option.";
				return false;
			}
			//Select  Client Name From drop down
			if (mapGPorIMDetails.get("Client Name") != null && !mapGPorIMDetails.get("Client Name").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Client Name in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//div[@id='s2id_clientid_"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"), mapGPorIMDetails.get("Client Name"), "Yes", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select FundFamily From drop down
			if (mapGPorIMDetails.get("Fund Family Name") != null && !mapGPorIMDetails.get("Fund Family Name").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Fund Family Name in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//div[@id='s2id_cmbFundFamilyName"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"), mapGPorIMDetails.get("Fund Family Name"), "Yes", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Legal Entity from drop down
			if (mapGPorIMDetails.get("Legal Entity Name") != null && !mapGPorIMDetails.get("Legal Entity Name").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Legal Entity Name in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//div[@id='s2id_cmbFundName"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"), mapGPorIMDetails.get("Legal Entity Name"), "Yes", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Fee Type
			if (mapGPorIMDetails.get("Fee Type") != null && !mapGPorIMDetails.get("Fee Type").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Fee Type in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//div[@id='s2id_feeType_"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"), mapGPorIMDetails.get("Fee Type"), "Yes", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select From Date of GP/IM.
			if (mapGPorIMDetails.get("GPorIMFromDate") != null && !mapGPorIMDetails.get("GPorIMFromDate").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("GPorIMFromDate in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//input[@id='fromDateRange"+indexOfGPorIM+"']"), mapGPorIMDetails.get("GPorIMFromDate"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select To Date of GP/IM.
			if (mapGPorIMDetails.get("GPorIMToDate") != null && !mapGPorIMDetails.get("GPorIMToDate").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("GPorIMToDate in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//input[@id='toDateRange"+indexOfGPorIM+"']"), mapGPorIMDetails.get("GPorIMToDate"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyRespectiveIndexGPorIMFeeDistributionDetails(List<String> sFeePercentagesListOfEachDistribution,	List<String> sFromDatesListOfEachDistribution, List<String> sToDatesListOfEachDistribution, int iGporIMIndex, int iRespectiveDistributionIndex){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Fee Percentage' for the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'", By.xpath("//input[@id='gpList"+iGporIMIndex+"_feeDistribution"+iRespectiveDistributionIndex+".feeDistributionDetails0.percentage']"), sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex).trim(), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'From Date' for the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'", By.xpath("//input[@id='gpList"+iGporIMIndex+"_fromDateRange_"+iRespectiveDistributionIndex+"']"), sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'To Date' for the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'", By.xpath("//input[@id='gpList"+iGporIMIndex+"_toDateRange_"+iRespectiveDistributionIndex+"']"), sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
