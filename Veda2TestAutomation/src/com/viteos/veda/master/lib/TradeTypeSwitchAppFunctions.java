package com.viteos.veda.master.lib;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.checkerActionTypes;

public class TradeTypeSwitchAppFunctions {
	static boolean bStatus;
	public static String newAccountID = "";

	/**************MAKER FUNCTIONS INTEGRATION*******************/

	//Maker main function to fill / verify SWITCH details.
	public static boolean doTriggerMakerFillAndVerificationFunctions(Map<String , String> mapSwitchDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Request Received Date Field is Not Visible .]\n";
				return false;
			}
			bStatus = doMakerFillSwitchRequestDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillSwitchFromInvestorDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			if(mapSwitchDetails.get("Switch To Another Investor") != null){
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Switch')]"));
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isSwitchToAnotherInvestor1']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Switch To Another Investor 'Yes' radio button cannot be selected. ]\n";
						return false;
					}
					TimeUnit.SECONDS.sleep(1);
					bStatus = doMakerFillSwitchToInvestorDetails(mapSwitchDetails);
					if(!bStatus){
						return false;
					}
				}
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isSwitchToAnotherInvestor2']//span"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Switch To Another Investor 'No' radio button cannot be selected .]\n";
						return false;
					}
				}
			}
			bStatus = doMakerFillFromFundDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}

			//Putting the Created New Account Id in 
			if(mapSwitchDetails.get("NewAccount") != null && mapSwitchDetails.get("NewAccount").equalsIgnoreCase("Yes")){
				newAccountID = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_toInvestorAccount']//span[contains(@id,'select2-chosen')]"));
				if(newAccountID == null || newAccountID.equalsIgnoreCase("") ){
					Messages.errorMsg = "[ ERROR : New Account ID is not Generated .]\n";
					return false;
				}
			}

			bStatus = doMakerVerifyAndFillToFundDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillSwitchDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			String proceedLocator = "//button[contains(@onclick,'javascript:proceed') and contains(normalize-space(),'Proceed')]";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(proceedLocator));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Cannot scroll to Proceed button .]\n";
				return false;
			}
			bStatus = Elements.click(Global.driver, By.xpath(proceedLocator));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Proceed button cannot be clicked .]\n";
				return false;
			}
			if (!Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'javascript:proceed') and contains(normalize-space(),'Apply')]"), Constants.iSpinnerTime)) {
				Messages.errorMsg = "[ ERROR : Apply Button is not visible in second screen.]\n";
				return false;
			}
			bStatus = doMakerVerifySwitchRequestDetailsTab(mapSwitchDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifySwitchFromInvestorDetailsTab(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			if(mapSwitchDetails.get("Switch To Another Investor") != null){
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("Yes")){
					if(!Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isSwitchToAnotherInvestor1']//span[@class='checked']"), 2000)){
						Messages.errorMsg = "[ ERROR : Switch To Another Investor 'Yes' radio button wasn't marked to be selected. ]\n";
						return false;
					}
					bStatus = doMakerVerifySwitchToInvestorDetailsTab(mapSwitchDetails);
					if(!bStatus){
						return false;
					}
				}
			}
			bStatus = doMakerVerifyFromFundDetailsTab(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyToFundDetailsTab(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifySwitchDetailsTab(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillAvailableBalanceIntegratedFunction(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAvailableBalanceDetailsForTotalsColumns(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			if (mapSwitchDetails != null && mapSwitchDetails.get("ExpectedPendingRequestAmount") != null && mapSwitchDetails.get("ExpectedPendingRequestedShares") != null && mapSwitchDetails.get("ExpectedNumberOfDecimals") != null) {
				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapSwitchDetails.get("ExpectedPendingRequestAmount"), mapSwitchDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					return false;
				}				
			}
			bStatus = doMakerFillAndVerifyChargeDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAmountDetailsTab(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerSelectOtherDetailsRadioButtons(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			if(mapSwitchDetails.get("OperationType")!=null){
				if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("None")){
					return true;
				}
				bStatus = doSubOperationsOnTransactionTrades("Switch", mapSwitchDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapSwitchDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Proceed for Approval Button is Not Visible .]\n";
						return false;
					}
					bStatus = doMakerVerifyExceptions(mapSwitchDetails);
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with Expected");
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
						if(!bStatus){
							Messages.errorMsg = errorMsg + "[ ERROR : Cannot close the Exceptions .]\n";
							return false;
						}
						Messages.errorMsg = errorMsg;
						return false;
					}
					if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("Save") || mapSwitchDetails.get("OperationType").contains("Review"))
					{
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Proceed Button Cannot be clicked .]\n";
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
							return false;
						}
					}
				}/*else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions raised when there are no expected Exception.");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Exceptions are Visible Even there are No Expected Exception and Cannot close the Exceptions .]\n";
							return false;
						}
						Messages.errorMsg = "[ ERROR : Exceptions are Visible Even there are No Expected Exception .]\n";
						return false;
					}
				}*/
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"), Constants.iPopupWaitingTime);
				if(bStatus){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Proceed Button Cannot be clicked .]\n";
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
						return false;
					}
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Succesfull Message is Not visible .]\n";
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean doFillCheckerReviewedOrReturnedMakerFunctions(Map<String , String> mapSwitchDetails){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Request Received Date Field is Not Visible .]\n";
				return false;
			}
			bStatus = doMakerFillSwitchRequestDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillSwitchFromInvestorDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			if(mapSwitchDetails.get("Switch To Another Investor") != null){
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Switch')]"));
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isSwitchToAnotherInvestor1']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Switch To Another Investor 'Yes' radio button cannot be selected. ]\n";
						return false;
					}
					TimeUnit.SECONDS.sleep(1);
					bStatus = doMakerFillSwitchToInvestorDetails(mapSwitchDetails);
					if(!bStatus){
						return false;
					}
				}
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isSwitchToAnotherInvestor2']//span"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Switch To Another Investor 'No' radio button cannot be selected .]\n";
						return false;
					}
				}
			}
			bStatus = doMakerFillFromFundDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}

			//Putting the Created New Account Id in 
			if(mapSwitchDetails.get("NewAccount") != null && mapSwitchDetails.get("NewAccount").equalsIgnoreCase("Yes")){
				newAccountID = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_toInvestorAccount']//span[contains(@id,'select2-chosen')]"));
				if(newAccountID == null || newAccountID.equalsIgnoreCase("") ){
					Messages.errorMsg = "[ ERROR : New Account ID is not Generated .]\n";
					return false;
				}
			}

			bStatus = doMakerVerifyAndFillToFundDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillSwitchDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}

			String applyLocator = "//button[contains(@onclick,'javascript:proceed') and contains(normalize-space(),'Apply')]";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(applyLocator));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Cannot scroll to Proceed button .]\n";
				return false;
			}
			bStatus = Elements.click(Global.driver, By.xpath(applyLocator));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Proceed button cannot be clicked .]\n";
				return false;
			}

			bStatus = doMakerFillAvailableBalanceIntegratedFunction(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAvailableBalanceDetailsForTotalsColumns(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			if (mapSwitchDetails != null && mapSwitchDetails.get("ExpectedPendingRequestAmount") != null && mapSwitchDetails.get("ExpectedPendingRequestedShares") != null && mapSwitchDetails.get("ExpectedNumberOfDecimals") != null) {
				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapSwitchDetails.get("ExpectedPendingRequestAmount"), mapSwitchDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					return false;
				}				
			}
			bStatus = doMakerFillAndVerifyChargeDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAmountDetailsTab(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerSelectOtherDetailsRadioButtons(mapSwitchDetails);
			if(!bStatus){
				return false;
			}

			if(mapSwitchDetails.get("OperationType")!=null){
				if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("None")){
					return true;
				}
				bStatus = doSubOperationsOnTransactionTrades("Switch", mapSwitchDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//button[contains(@onclick,'javascript:cancelDashboard')]"), 5);
					if(!bStatus){
						return true;
					}
					Messages.errorMsg = "Cancel button click Operation Failed";
					return false;
				}
				if(mapSwitchDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'makerModifyProceed') and contains(normalize-space(),'Proceed')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Proceed for Approval Button is Not Visible .]\n";
						return false;
					}
					bStatus = doMakerVerifyExceptions(mapSwitchDetails);
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with Expected");
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
						if(!bStatus){
							Messages.errorMsg = errorMsg + "[ ERROR : Cannot close the Exceptions .]\n";
							return false;
						}
						Messages.errorMsg = errorMsg;
						return false;
					}
					if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("Save") || mapSwitchDetails.get("OperationType").contains("Review"))
					{
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'makerModifyProceed') and contains(normalize-space(),'Proceed')]"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Proceed Button Cannot be clicked .]\n";
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
							return false;
						}
					}
				}else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'makerModifyProceed') and contains(normalize-space(),'Proceed')]"), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions raised when there are no expected Exception.");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Exceptions are Visible Even there are No Expected Exception and Cannot close the Exceptions .]\n";
							return false;
						}
						Messages.errorMsg = "[ ERROR : Exceptions are Visible Even there are No Expected Exception .]\n";
						return false;
					}
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Succesfull Message is Not visible .]\n";
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
	/**************CHECKER FUNCTIONS INTEGRATION*******************/

	//Checker main function to verify SWITCH details.
	public static boolean doTriggerCheckerValidationtionFunctions(Map<String , String> mapSwitchDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Details')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Details Tab is Not Visible";
				return false;
			}
			bStatus = doCheckerVerifySwitchRequestDetails(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			bStatus = doCheckerVerifySwitchFromInvestorDetailsTab(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			if(mapSwitchDetails.get("Switch To Another Investor") != null){
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(normalize-space(),'Another Investor')]//following-sibling::div//label[contains(normalize-space(),'Yes')]//span[@class='checked']"), 2000);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Switch To Another Investor 'Yes' radio button wasn't marked as selected. ]\n";
						bValidateStatus = false;
					}
					bStatus = doCheckerVerifySwitchToInvestorDetailsTab(mapSwitchDetails);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(normalize-space(),'Another Investor')]//following-sibling::div//label[contains(normalize-space(),'No')]//span[@class='checked']"), 2000);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Switch To Another Investor 'No' radio button wasn't marked as selected. ]\n";
						bValidateStatus = false;
					}
				}
			}
			bStatus = doCheckerVerifySwitchFromFundDetailsTab(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			bStatus = doCheckerVerifySwitchToFundDetailsTab(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			bStatus = doCheckerVerifySwitchDetailsTab(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			bStatus = doVerifyAllocatedAmountAndShare(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}

			bStatus = doCheckerVerifyAllSwitchAvailableBalanceDetails(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}

			if (mapSwitchDetails.get("ExpectedPendingRequestAmount") != null && !mapSwitchDetails.get("ExpectedPendingRequestAmount").contains("Records") && mapSwitchDetails.get("ExpectedPendingRequestedShares") != null && mapSwitchDetails.get("ExpectedNumberOfDecimals") != null) {
				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapSwitchDetails.get("ExpectedPendingRequestAmount"), mapSwitchDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}				
			}
			bStatus = doCheckerVerifySwitchChargesDetails(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			bStatus = doCheckerVerifySwitchAmountDetailsTab(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}			
			if(mapSwitchDetails.get("ExpectedExceptionsAtChecker") != null){
				bStatus = doCheckerVerifyExceptions(mapSwitchDetails.get("ExpectedExceptionsAtChecker"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			bStatus = doCheckerVerifyOtherDetailsRadioButtons(mapSwitchDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			if(mapSwitchDetails.get("Transaction ID") != null && !OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Order Number, ", By.xpath("//input[@data-original-title='Order No']"), mapSwitchDetails.get("Transaction ID"), "No", false)){
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = sAppendErrorMsg;
		return bValidateStatus;
	}

	/**************VIEW BUTTONS INTEGRATION **********************/

	//Maker main function to  View Buttons
	public static boolean verifyViewButtonsFunctionality(Map<String, String> mapSwitchDetails)
	{

		boolean bValidStatus= true;

		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Request Received Date Field is Not Visible .]\n";
				return false;
			}
			bStatus = doMakerFillSwitchRequestDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillSwitchFromInvestorDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			if(mapSwitchDetails.get("Switch To Another Investor") != null){
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isSwitchToAnotherInvestor1']//span"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Switch To Another Investor 'Yes' radio button cannot be selected. ]\n";
						return false;
					}
					TimeUnit.SECONDS.sleep(1);
					bStatus = doMakerFillSwitchToInvestorDetails(mapSwitchDetails);
					if(!bStatus){
						return false;
					}
				}
				if(mapSwitchDetails.get("Switch To Another Investor").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isSwitchToAnotherInvestor2']//span"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Switch To Another Investor 'No' radio button cannot be selected .]\n";
						return false;
					}
				}
			}
			bStatus = doMakerFillFromFundDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAndFillToFundDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillSwitchDetails(mapSwitchDetails);
			if(!bStatus){
				return false;
			}
			String proceedLocator = "//button[contains(@onclick,'javascript:proceed') and contains(normalize-space(),'Proceed')]";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(proceedLocator));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Cannot scroll to Proceed button .]\n";
				return false;
			}
			bStatus = Elements.click(Global.driver, By.xpath(proceedLocator));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Proceed button cannot be clicked .]\n";
				return false;
			}
			if (!Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]"), Constants.iSpinnerTime)) {
				Messages.errorMsg = "[ ERROR : Wasn't navigated to 2nd screen after click on Proceed button .]\n";
				return false;
			}

			bStatus = doMakerVerifyViewButtonForNoticePeriods(mapSwitchDetails);
			if(!bStatus){
				bValidStatus = false;
				Reporting.logResults("Fail","Verify Notice Charge details","Verification Failed. Error: "+Messages.errorMsg);
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Notice Period Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'NC')]//following-sibling::button[normalize-space(text()='Close')]"));
				}			
			}

			if(bStatus && mapSwitchDetails.get("NPView") != null)
			{
				Reporting.logResults("Pass","Verify Notice Charges ","Notice Charges verified successfully");
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Notice Period Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'NC')]//following-sibling::button[normalize-space(text()='Close')]"));
				}

			}

			bStatus = doMakerVerifyViewButtonForTransactionCharge(mapSwitchDetails);
			if(!bStatus){
				bValidStatus = false;
				Reporting.logResults("Fail","Verify Transaction period details","Verification Failed. Error: "+Messages.errorMsg);
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Transaction Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'TC')]//following-sibling::button[normalize-space(text()='Close')]"));
				}
			}
			if(bStatus && mapSwitchDetails.get("TCEffDate")!=null){
				Reporting.logResults("Pass","Verify Transaction period details","Transaction period Charges verified successfully");
				Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'TC')]//following-sibling::button[normalize-space(text()='Close')]"));				
			}

			return bValidStatus;

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}


	}

	/**************MAKER FUNCTIONS*******************/

	//Filling FromFund Details.
	public static boolean doMakerFillFromFundDetails(Map<String, String> mapSwitchDetails) {
		try{	
			NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Fund Details')]"));
			//select Client Name
			if(mapSwitchDetails.get("From Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("From Client Name"), By.xpath("//div[@id='s2id_fromClient']//span[contains(@id, 'select2-chosen')]"));				
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Client Name' i.e : '"+mapSwitchDetails.get("From Client Name")+"' from dropdown is failed.]";
					return false;
				}
			}
			//Select Fund Family
			if(mapSwitchDetails.get("From Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("From Fund Family Name"), By.xpath("//div[@id='s2id_fromFundFamily']//span[contains(@id, 'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Fund Family' i.e : '"+mapSwitchDetails.get("From Fund Family Name")+"' from dropdown is failed.]";
					return false;
				}
			}
			//Select Legal Entity
			if(mapSwitchDetails.get("From Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("From Legal Entity Name"), By.xpath("//div[@id='s2id_fromLegalEntity']//span[contains(@id, 'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Legal Entity Name' i.e : '"+mapSwitchDetails.get("From Legal Entity Name")+"' from dropdown is failed.]";
					return false;
				}
			}
			//Select Class Name
			if(mapSwitchDetails.get("From Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("From Class Name"), By.xpath("//div[@id='s2id_fromClass']//span[contains(@id, 'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Class Name' i.e : '"+mapSwitchDetails.get("From Class Name")+"' from dropdown is failed.]";
					return false;
				}
			}
			// Select the Series Name
			if(mapSwitchDetails.get("From Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("From Series Name"), By.xpath("//div[@id='s2id_fromSeries']//span[contains(@id, 'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Series Name' i.e : '"+mapSwitchDetails.get("From Series Name")+"' from dropdown is failed.]";
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

	//Filling/Creating To-Investor Details.
	public static boolean doMakerFillSwitchToInvestorDetails(Map<String, String> mapSwitchDetails) {
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			//Create New Investor
			if(mapSwitchDetails.get("New Investor") != null && mapSwitchDetails.get("New Investor").equalsIgnoreCase("Yes"))
			{
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//span//a[contains(@onclick,'investorAdd')]"));
				if(!bStatus)
				{
					Messages.errorMsg = "[ ERROR : New Investor Link cannot be clicked.]\n";
					return false;
				}
				if(mapSwitchDetails.get("New Investor TestCaseName") != null)
				{
					InvestorMasterAppFunctions.bTadingInvestorFlag = true;
					bStatus = TradeTypeSubscriptionAppFunctions.createNewInvestorFromTradeTypeSubscription(mapSwitchDetails.get("New Investor TestCaseName"));
					if(!bStatus)
					{
						Messages.errorMsg = "[ERROR: Unable to Create New Investor : '"+mapSwitchDetails.get("New Investor TestCaseName")+"',in To Investor Details of Switch Module .]\n";
						return false;
					}
				}
			}
			//Select Investor from drop down
			if(mapSwitchDetails.get("To Investor Name") != null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("To Investor Name"), By.xpath("//div[contains(@id,'s2id_toInvestorMaster')]//span[contains(@id,'select2-chosen')]"));
				if(!bStatus)
				{
					Messages.errorMsg = "[ERROR: Unable to Select To Investor : '"+mapSwitchDetails.get("To Investor Name")+"',in To Investor Details of Switch Module .]\n";
					return false;
				}
			}
			//Create New Holder
			if(mapSwitchDetails.get("New Holder") != null && mapSwitchDetails.get("New Holder").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='newHolderLink']"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : New Holder Link Cannot be clicked. ]\n"; 
					return false;
				}
				if(mapSwitchDetails.get("New Holder TestCaseName") != null){
					HolderMasterAppFunctions.bTradingSubscription = true;
					bStatus = TradeTypeSubscriptionAppFunctions.createNewHolderFromTradeTypeSubscription(mapSwitchDetails.get("New Holder TestCaseName"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR: Unable to Create New Holder : '"+mapSwitchDetails.get("New Holder TestCaseName")+"',in To Holder Details of switch Module .]\n";
						return false;
					}
				}
			}
			//Select Holder From drop down
			if(mapSwitchDetails.get("To Holder Name") != null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("To Holder Name"), By.xpath("//div[contains(@id, 's2id_toInvestorHolder')]//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ERROR: Unable to Select Holder : '"+mapSwitchDetails.get("To Holder Name")+"',in To Investor Details of switch Module]\n";
					return false;
				}
			}
			// New Account Creation 
			if(mapSwitchDetails.get("NewAccount") != null && mapSwitchDetails.get("NewAccount").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='newAccountLink']"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : New Account Link Cannont be clicked .]\n";
					return false;
				}				
			}
			//Select Account From Drop down.
			if(mapSwitchDetails.get("To Account ID") != null){
				String accountId = getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapSwitchDetails.get("To Account ID"));
				if(accountId != null && !accountId.equalsIgnoreCase("")){
					mapSwitchDetails.put("To Account ID", accountId);
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("To Account ID"), By.xpath("//div[@id='s2id_toInvestorAccount']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					return false;
				}						
			}
			TimeUnit.SECONDS.sleep(2);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;	
	}

	//Filling From-Investor Details.
	public static boolean doMakerFillSwitchFromInvestorDetails(Map<String, String> mapSwitchDetails) {
		try {

			if(mapSwitchDetails.get("From Investor Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("From Investor Name"), By.xpath("//div[@id='s2id_fromInvestorMaster']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "From Investor Name :"+Messages.errorMsg;
					return false;
				}
			}
			if(mapSwitchDetails.get("From Holder Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("From Holder Name"), By.xpath("//div[@id='s2id_fromInvestorHolder']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "From Holder Name :"+Messages.errorMsg;
					return false;
				}
			}
			if(mapSwitchDetails.get("From Account Id")!=null){
				String accountId = getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapSwitchDetails.get("From Account Id"));
				if(accountId != null && !accountId.equalsIgnoreCase("")){
					mapSwitchDetails.put("From Account Id", accountId);
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("From Account Id"), By.xpath("//div[@id='s2id_fromInvestorAccount']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "From Account Id :"+Messages.errorMsg;
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

	//Filling Request Details.
	public static boolean doMakerFillSwitchRequestDetails(Map<String, String> mapSwitchDetails) {
		try {
			if(mapSwitchDetails.get("Received Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='requestDate']"), mapSwitchDetails.get("Received Date"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Request Received Date Not Entered. ]\n";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Switch')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Failed to click on subscription Label. ]\n";
					return false;
				}
			}
			if(mapSwitchDetails.get("Received Time")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='tblOrderTransferMaster.requestTime']"), mapSwitchDetails.get("Received Time"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Request Received Time Not Entered. ]\n";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Switch')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Failed to click on subscription Label. ]\n";
					return false;
				}
			}
			if(mapSwitchDetails.get("Order Received Office")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[contains(text(),'Order Received Office')]//following-sibling::div/input"), mapSwitchDetails.get("Order Received Office"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Order Received Office Not Entered. ]\n";
					return false;
				}
			}
			if(mapSwitchDetails.get("Time Zone")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[contains(text(),'Time Zone')]//following-sibling::div/input"), mapSwitchDetails.get("Time Zone"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Time Zone Not Entered. ]\n";
					return false;
				}
			}
			if(mapSwitchDetails.get("Source")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='tblOrderTransferMaster.source']"), mapSwitchDetails.get("Source"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Source Not Entered. ]\n";
					return false;
				}
			}
			if(mapSwitchDetails.get("Mode of Request")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("Mode of Request"), By.xpath("//div[@id='s2id_tblOrderTransferMaster.modeOfRequest.referenceIdPk']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Mode of Request Drop down Not Selected. ]\n";
					return false;
				}
			}
			if(mapSwitchDetails.get("External ID Number")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='tblOrderTransferMaster.externalId']"), mapSwitchDetails.get("External ID Number"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : External ID Not Entered. ]\n";
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

	// Filling/Verifying FromFund/ToFund Details in 1st screen.
	public static boolean doMakerVerifyAndFillToFundDetails(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			//select Client Name
			if(mapSwitchDetails.get("From Client Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Client Name ", By.xpath("//div[@id='s2id_toClient']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Client Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Fund Family
			if(mapSwitchDetails.get("From Fund Family Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Fund Family Name ", By.xpath("//div[@id='s2id_toFundFamily']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Fund Family Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Legal Entity
			if(mapSwitchDetails.get("From Legal Entity Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Legal Entity Name ", By.xpath("//div[@id='s2id_toLegalEntity']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Legal Entity Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Class Name
			if(mapSwitchDetails.get("To Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("To Class Name"), By.xpath("//div[@id='s2id_toClass']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : 'To Class Name' value : '"+mapSwitchDetails.get("To Class Name")+"' ,wasn't able to be selected from dropdown. ]\n";					
					return false;
				}
			}
			// Select the Series Name
			if(mapSwitchDetails.get("To Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSwitchDetails.get("To Series Name"), By.xpath("//div[@id='s2id_toSeries']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : 'To Series Name' value : '"+mapSwitchDetails.get("To Series Name")+"' ,wasn't able to be selected from dropdown. ]\n";					
					return false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Filling Exchange Fund Details.
	public static boolean doMakerFillSwitchDetails(Map<String, String> mapSwitchDetails){
		try{
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='Yes']//div[@id='uniform-fullSwitch']//span"));
			if(!bStatus){
				Messages.errorMsg = "[ Error : Cannot scroll the page to Switch Details .]\n";
				return false;
			}
			//Select Full Transfer  Radio button
			if(mapSwitchDetails.get("Full Switch")!=null){
				if(mapSwitchDetails.get("Full Switch").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='Yes']//div[@id='uniform-fullSwitch']//span"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Full Switch Radio button 'Yes' is Not clicked ]\n";
						return false;
					}
					String percentageValue = "100";
					String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='percentage']");
					if(Float.parseFloat(actualValue) != Float.parseFloat(percentageValue)){
						Messages.errorMsg = "[ Error : Percentage Value is not Changed to 100 when Full Switch Yes button selected ]\n";
						return false;
					}
				}
				if(mapSwitchDetails.get("Full Switch").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='No']//div[@id='uniform-fullSwitch']//span"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Full Switch Radio button 'No' is Not clicked ]\n";
						return false;
					}
				}
			}
			//Enter Effective Date
			if( mapSwitchDetails.get("Effective Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapSwitchDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : Effective Date is Not Entered ]\n";
					return false;
				}
			}
			//Enter Percentage
			if(mapSwitchDetails.get("Percentage")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='percentage']"), mapSwitchDetails.get("Percentage"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : Percentage Cannot be entered ]\n";
					return false;
				}
			}
			//Select Amount/Share Radio button
			if(mapSwitchDetails.get("AmountorShares")!=null && mapSwitchDetails.get("Percentage") == null && mapSwitchDetails.get("Full Switch")!=null && !mapSwitchDetails.get("Full Switch").equalsIgnoreCase("Yes")){
				if(mapSwitchDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare1']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Amount Radio button cannot be selected ]\n";
						return false;
					}
					if(mapSwitchDetails.get("Amount Value")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapSwitchDetails.get("Amount Value"));
						if(!bStatus){
							Messages.errorMsg = "[ Error : Amount Value is  Not Entered ]\n";
							return false;
						}
					}
				}
				if(mapSwitchDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare2']"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Shares Radio button cannot be selected ]\n";
						return false;
					}
					if(mapSwitchDetails.get("Share Value")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='share']"), mapSwitchDetails.get("Share Value"));
						if(!bStatus){
							Messages.errorMsg = "[ Error : Share Value  Not Entered ]\n";
							return false;
						}
					}
				}
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//Maker Verify Request Details.
	public static boolean doMakerVerifySwitchRequestDetailsTab(Map<String, String> mapSwitchDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "1");
			if (!bStatus) {
				return false;
			}
			if(mapSwitchDetails.get("Received Date")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Received Date ", By.xpath("//input[@id='requestDate']"), mapSwitchDetails.get("Received Date"), "No", false);				
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("Received Time")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Received Time", By.id("tblOrderTransferMaster.requestTime"), mapSwitchDetails.get("Received Time"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("Order Received Office")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Order Received Office", By.xpath("//input[@placeholder='Order Received Office']"), mapSwitchDetails.get("Order Received Office"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("Time Zone")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Time Zone", By.xpath("//input[@placeholder='Time Zone']"), mapSwitchDetails.get("Time Zone"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}			
			if(mapSwitchDetails.get("Source")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Source", By.id("tblOrderTransferMaster.source"), mapSwitchDetails.get("Source"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}				
			}			
			if(mapSwitchDetails.get("Mode of Request")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Mode Of Request ", By.xpath("//div[contains(@id,'modeOfRequest')]//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("Mode of Request"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("External ID Number")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("External ID Number", By.id("tblOrderTransferMaster.externalId"), mapSwitchDetails.get("External ID Number"), "No", false);
				if(!bStatus){
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

	//Maker Verify From-Investor Details.
	public static boolean doMakerVerifySwitchFromInvestorDetailsTab(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "From Investor");
			if (!bStatus) {
				return false;
			}			
			if(mapSwitchDetails.get("From Investor Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Investor Name, ", By.xpath("//div[@id='s2id_fromInvestorMaster']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Investor Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("From Holder Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Holder Name, ", By.xpath("//div[@id='s2id_fromInvestorHolder']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Holder Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("From Account Id")!=null){
				String accountId = getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapSwitchDetails.get("From Account Id"));
				if(accountId != null && !accountId.equalsIgnoreCase("")){
					mapSwitchDetails.put("From Account Id", accountId);
				}

				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Account Id, ", By.xpath("//div[@id='s2id_fromInvestorAccount']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Account Id"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}

			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Maker Verify To-Investor Details.
	public static boolean doMakerVerifySwitchToInvestorDetailsTab(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "To Investor");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for To-Investor Details. ]\n";
				return false;
			}

			if (mapSwitchDetails.get("New Investor") != null && mapSwitchDetails.get("New Investor").equalsIgnoreCase("yes") && mapSwitchDetails.get("New Investor TestCaseName") != null) {
				String investorName = getTheInvestorNameCreatedFromSwitch(mapSwitchDetails);
				mapSwitchDetails.put("To Investor Name", investorName);
			}
			if (mapSwitchDetails.get("To Investor Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Investor Name, ", By.xpath("//div[@id= 's2id_toInvestorMaster']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("To Investor Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//verify created New Holder
			if (mapSwitchDetails.get("New Holder") != null && mapSwitchDetails.get("New Holder").equalsIgnoreCase("yes") && mapSwitchDetails.get("New Holder TestCaseName")!=null) {
				String holderName = getTheHolderNameCreatedFromSwtitch(mapSwitchDetails);
				mapSwitchDetails.put("To Holder Name", holderName);
			}
			if (mapSwitchDetails.get("To Holder Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Holder Name, ", By.xpath("//div[@id='s2id_toInvestorHolder']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("To Holder Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}


			if (mapSwitchDetails.get("To Account ID") != null) {

				String accountId = getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapSwitchDetails.get("To Account ID"));
				if(accountId != null && !accountId.equalsIgnoreCase("")){
					mapSwitchDetails.put("To Account ID", accountId);
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Account ID, ", By.xpath("//div[@id='s2id_toInvestorAccount']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("To Account ID"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapSwitchDetails.get("NewAccount") != null && mapSwitchDetails.get("NewAccount").equalsIgnoreCase("Yes")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Account ID, ", By.xpath("//div[@id='s2id_toInvestorAccount']//span[contains(@id,'select2-chosen')]"), newAccountID, "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = sAppendErrorMsg;
		return bValidateStatus;
	}



	//collapse / Expand function based on tab name.
	public static boolean doClickOnExpandOrCollapseButtonsBasedOnTabName(String sYesForExpandNoCollapse, String sCollapsableTabName){
		try {
			boolean bExpandStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'"+sCollapsableTabName+"')]//following-sibling::div//a[@class='expand']"), 2);
			boolean bCollpaseStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'"+sCollapsableTabName+"')]//following-sibling::div//a[@class='collapse']"), 2);
			if (bExpandStatus == true && bCollpaseStatus == false && sYesForExpandNoCollapse.equalsIgnoreCase("Yes")) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'"+sCollapsableTabName+"')]//following-sibling::div//a[@class='expand']"));
				if (!bStatus) {					
					return false;
				}
				TimeUnit.SECONDS.sleep(1);
				return true;
			}
			if (bExpandStatus == false && bCollpaseStatus == true && sYesForExpandNoCollapse.equalsIgnoreCase("No")) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'"+sCollapsableTabName+"')]//following-sibling::div//a[@class='collapse']"));
				if (!bStatus) {
					return false;
				}
				TimeUnit.SECONDS.sleep(1);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Verify FromFund Details.
	public static boolean doMakerVerifyFromFundDetailsTab(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try{

			NewUICommonFunctions.scrollToView(By.xpath("//div[@id='s2id_fromClient']/ancestor::div[@class='portlet box grey-gallery']//div[contains(normalize-space(),'From Fund Details')]/span"));

			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "From Fund Details");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for From-Fund Details. ]\n";
				return false;
			}
			//verify Client Name
			if(mapSwitchDetails.get("From Client Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Client Name, ", By.xpath("//div[@id='s2id_fromClient']//span[contains(@id, 'select2-chosen')]"), mapSwitchDetails.get("From Client Name"), "Yes", false);				
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//verify Fund Family
			if(mapSwitchDetails.get("From Fund Family Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Fund Family Name, ", By.xpath("//div[@id='s2id_fromFundFamily']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Fund Family Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//verify Legal Entity
			if(mapSwitchDetails.get("From Legal Entity Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Legal Entity Name, ", By.xpath("//div[@id='s2id_fromLegalEntity']//span[contains(@id, 'select2-chosen')]"), mapSwitchDetails.get("From Legal Entity Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//verify Class Name
			if(mapSwitchDetails.get("From Class Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Class Name, ", By.xpath("//div[@id='s2id_fromClass']//span[contains(@id, 'select2-chosen')]"), mapSwitchDetails.get("From Class Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//verify the Series Name
			if(mapSwitchDetails.get("From Series Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Series Name, ", By.xpath("//div[@id='s2id_fromSeries']//span[contains(@id, 'select2-chosen')]"), mapSwitchDetails.get("From Series Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	// Filling/Verifying FromFund/ToFund Details in 1st screen.
	public static boolean doMakerVerifyToFundDetailsTab(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "To Fund Details");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for To-fund Details. ]\n";
				return false;
			}
			//select Client Name
			if(mapSwitchDetails.get("From Client Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Client Name ", By.xpath("//div[@id='s2id_toClient']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Client Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//Select Fund Family
			if(mapSwitchDetails.get("From Fund Family Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Fund Family Name ", By.xpath("//div[@id='s2id_toFundFamily']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Fund Family Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Legal Entity
			if(mapSwitchDetails.get("From Legal Entity Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Legal Entity Name ", By.xpath("//div[@id='s2id_toLegalEntity']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("From Legal Entity Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Class Name
			if(mapSwitchDetails.get("To Class Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Class Name, ", By.xpath("//div[@id='s2id_toClass']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("To Class Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			// Select the Series Name
			if(mapSwitchDetails.get("To Series Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Series Name, ", By.xpath("//div[@id='s2id_toSeries']//span[contains(@id,'select2-chosen')]"), mapSwitchDetails.get("To Series Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	//Verifying SWITCH details Tab
	public static boolean doMakerVerifySwitchDetailsTab(Map<String, String> mapSwitchDetails){
		boolean bValidatreStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Switch Details");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for To-fund Details. ]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-fullSwitch']//span[@class='checked']"));			
			//Verify Full Transfer Radio button
			if (mapSwitchDetails.get("Full Switch") != null) {
				if (mapSwitchDetails.get("Full Switch").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//span[@class='checked']//input[@id='fullSwitch' and @value='1']//..//parent::div[@id='uniform-fullSwitch']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ ERROR : Full SWITCH wasn't marked to 'Yes'.]\n";
						bValidatreStatus = false;
					}
				}
				if (mapSwitchDetails.get("Full Switch").equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//span[@class='checked']//input[@id='fullSwitch' and @value='0']//..//parent::div[@id='uniform-fullSwitch']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ ERROR : Full SWITCH wasn't marked to 'No'.]\n";
						bValidatreStatus = false;
					}
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
				}
			}
			//Verify Amount or shares
			if (mapSwitchDetails.get("AmountorShares") != null) {
				if (mapSwitchDetails.get("AmountorShares").equalsIgnoreCase("Amount")) {
					//Verify the Amount
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-amountShare1']//span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : AmountorShares wasn't marked to 'Amount'.]\n";
						bValidatreStatus = false;
					}
					if (mapSwitchDetails.get("Amount Value") != null) {
						if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Cash", By.xpath("//input[@id='amountValue']"), mapSwitchDetails.get("Amount Value"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")))) {
							sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Cash, is not matching with expected : "+mapSwitchDetails.get("Amount Value")+"] \n";
							bValidatreStatus = false;
						}
					}
				}
				if (mapSwitchDetails.get("AmountorShares").equalsIgnoreCase("Shares")) {
					//Verify the Shares
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-amountShare2']//span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : AmountorShares wasn't marked to 'Shares'.]\n";
						bValidatreStatus = false;
					}
					if (mapSwitchDetails.get("Share Value") != null) {				
						if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share", By.xpath("//input[@id='share']"), mapSwitchDetails.get("Share Value"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")))) {
							sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Share, is not matching with expected : "+mapSwitchDetails.get("Share Value")+"] \n";
							bValidatreStatus = false;
						}
					}
				}
			}


			//Verify Effective Date
			if (mapSwitchDetails.get("Effective Date") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Effective Date", By.xpath("//input[@id='effectiveDate']"), mapSwitchDetails.get("Effective Date"), "No", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Effective Date, is not matching with expected : "+mapSwitchDetails.get("Effective Date")+"] \n";
					bValidatreStatus = false;
				}
			}
			//Verify Percentage 
			if (mapSwitchDetails.get("Percentage") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Percentage", By.xpath("//input[@id='percentage']"), mapSwitchDetails.get("Percentage"), "No", true, -1)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Percentage, is not matching with expected : "+mapSwitchDetails.get("Percentage")+"] \n";
					bValidatreStatus = false;
				}
			}			
			if (mapSwitchDetails.get("Expected NAV Date") != null) {	
				String actualDate =Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Nav Date']//following-sibling::label"));
				String date = TradeTypeSubscriptionAppFunctions.formatDate(actualDate);
				if(!date.equalsIgnoreCase(mapSwitchDetails.get("Expected NAV Date"))){
					sAppendErrorMsg = sAppendErrorMsg+ "[ ERROR : Actual NAV Date : '"+actualDate+"' is not Matched with Expected Nav Date : "+mapSwitchDetails.get("Expected NAV Date")+" .] \n";
					bValidatreStatus = false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			Messages.errorMsg = sAppendErrorMsg;
			return bValidatreStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Filling and verifying all Available balance details.
	public static boolean doMakerFillAvailableBalanceIntegratedFunction(Map<String ,String> mapSwitchDetails){
		List<String> sAmountOrSharesList = null;
		List<String> sVerifyAmountOrSharesList = null;
		try {
			String sIsAmountOrShare = "";
			String verifyAmountOrShares = "";
			bStatus = doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Available Balance");
			if (!bStatus) {
				return false;
			}
			//Split Taxlots Amount or Share
			if (mapSwitchDetails.get("TaxLotsAmounts") != null || mapSwitchDetails.get("TaxLotsShares") != null) {
				if (mapSwitchDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "amount";
					sAmountOrSharesList =  Arrays.asList(mapSwitchDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShares = "share";
				}
				else if (mapSwitchDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "share";
					sAmountOrSharesList =  Arrays.asList(mapSwitchDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShares = "amount";
				}
			}
			//Split Expected Taxlots Amount or Share
			if(mapSwitchDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList =  Arrays.asList(mapSwitchDetails.get("ExpectedTaxLotAmountOrShare").split(","));
			}
			if (!sIsAmountOrShare.equalsIgnoreCase("") && mapSwitchDetails.get("Full Switch") != null && mapSwitchDetails.get("Full Switch").equalsIgnoreCase("No")) {			
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"), 3);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Tax lots are not visible.]\n";
					return false;
				}
				bStatus = doMakerFillAvailableBalanceDetails(sAmountOrSharesList, sIsAmountOrShare, verifyAmountOrShares, sVerifyAmountOrSharesList, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")), Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					return false;
				}				
			}
			if (mapSwitchDetails.get("Full Switch") != null && mapSwitchDetails.get("Full Switch").equalsIgnoreCase("Yes")) {
				bStatus = doMakerVerifyFullSwitchValuesInAvaialableBalance(mapSwitchDetails);
				if(!bStatus){
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Maker Verify full Switch details.
	private static boolean doMakerVerifyFullSwitchValuesInAvaialableBalance(Map<String, String> mapSwitchDetails) {
		try {
			if(mapSwitchDetails.get("ExpectedAvailableAmount") != null && mapSwitchDetails.get("ExpectedAvailableShares") != null){
				List<String> allocatedAmount = Arrays.asList(mapSwitchDetails.get("ExpectedAvailableAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapSwitchDetails.get("ExpectedAvailableShares").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "[ ERROR : Avialable Amounts : "+allocatedAmount+" and Avaiable Shares : "+allocatedShares+" Values are Not Provided equally.]\n";
					return false;
				}
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));				
				if(allocatedAmount.size() != NoOfRecords){
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+allocatedAmount.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				int noOfDecimalsToDispaly = Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals"));
				int noOfShareDecimalsToDispaly = Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals"));
				for(int i=0; i<allocatedAmount.size(); i++){
					String amountlocator = "//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div//following::div//table//tbody//tr["+i+1+"]//td[11]//input";
					String sharelocator = "//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div//following::div//table//tbody//tr["+i+1+"]//td[12]//input";
					if(!allocatedAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot 'Allocated Amount' at index : '"+i+"'", By.xpath(amountlocator), allocatedAmount.get(i), "No", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							return false;
						}
					}
					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot 'Allocated Share' at index : '"+i+"'", By.xpath(sharelocator), allocatedShares.get(i), "No", true, noOfShareDecimalsToDispaly);
						if (!bStatus) {
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

	//Maker filling Available Balance Details.	
	public static boolean doMakerFillAvailableBalanceDetails(List<String> sAmountOrSharesList, String sIsAmountOrShare, String verifyAmountOrShares, List<String> sVerifyAmountOrSharesList, int noOfDecimalsToDispaly, int noOfShareDecimalsToDispaly) {
		try {

			String sVerifyColumnIndex = "";
			String sInputColumnIndex = "";
			if (verifyAmountOrShares != null && verifyAmountOrShares.toLowerCase().contains("amount")) {
				sVerifyColumnIndex = "11";
				sInputColumnIndex = "12";
			}
			if (verifyAmountOrShares != null && verifyAmountOrShares.toLowerCase().contains("share")) {
				sVerifyColumnIndex = "12";
				sInputColumnIndex = "11";
				noOfDecimalsToDispaly = noOfShareDecimalsToDispaly;
			}			
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]"));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Page cannot scroll to Available Balance Details . ]\n";
				return false;
			}
			int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
			if(sAmountOrSharesList.size() != NoOfRecords){
				Messages.errorMsg = "[ ERROR : Expected No lots : '"+sAmountOrSharesList.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
				return false;
			}

			for(int i=0; i<sAmountOrSharesList.size();i++)
			{
				String inputlocator = "//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div//following::div//table//tbody//tr["+i+1+"]//td["+sInputColumnIndex+"]//input";
				String verifylocator = "//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div//following::div//table//tbody//tr["+i+1+"]//td["+sVerifyColumnIndex+"]//input";
				if(!sAmountOrSharesList.get(i).equalsIgnoreCase("None"))
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='check_"+(i+1)+"']//parent::span[@class='checked']"), 3);
					if(bStatus)
					{						
						bStatus = TradeTypeRedemptionAppFunctions.ClearAndSetText(By.xpath(inputlocator), sAmountOrSharesList.get(i));							
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to enter the text into the Switch tax Lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
							return false;
						}
						if (sVerifyAmountOrSharesList == null) {
							Messages.errorMsg = "[ ERROR : Expected Test Data To fill the Tax slots wasn't given Properly Please verify the given Test data for 'ExpectedTaxLotAmountOrShare']\n";
							return false;
						}
						bStatus = Elements.click(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]"));
						if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.xpath(verifylocator), sVerifyAmountOrSharesList.get(i), "No", true, noOfDecimalsToDispaly);
							if (!bStatus) {
								return false;
							}
						}					
					}
					else
					{
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='check_"+(i+1)+"']//parent::span"));
						if (!bStatus) {
							Messages.errorMsg = "[ERROR : Unable to Select the Switch tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
							return false;
						}
						bStatus = TradeTypeRedemptionAppFunctions.ClearAndSetText(By.xpath(inputlocator), sAmountOrSharesList.get(i));							
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to enter the text into the Switch Lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
							return false;
						}
						if (sVerifyAmountOrSharesList == null) {
							Messages.errorMsg = "[ ERROR : Expected Test Data To fill the Tax slots wasn't given Properly Please verify the given Test data for 'ExpectedTaxLotAmountOrShare']\n";
							return false;
						}
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]"));
						if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.xpath(verifylocator), sVerifyAmountOrSharesList.get(i), "No", true, noOfDecimalsToDispaly);
							if (!bStatus) {
								return false;
							}
						}
					}
				}
				else
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='check_"+(i+1)+"']//parent::span[@class='checked']"), 3);
					if(bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='check_"+(i+1)+"']//parent::span[@class='checked']"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Cannot Uncheck the Check box at Index '"+i+"']\n";
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


	//Maker Verifying main Available balances Details and totals.
	public static boolean doMakerVerifyAvailableBalanceDetailsForTotalsColumns(Map<String, String> mapSwitchDetails){
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			boolean bValidStatus = true;
			String appendErrMsg = "";
			if(mapSwitchDetails.get("ExpectedActualBalance") != null && mapSwitchDetails.get("ExpectedActualShares") != null && mapSwitchDetails.get("ExpectedAvailableAmount") != null && mapSwitchDetails.get("ExpectedAvailableShares") != null )
			{
				List<String> actualBalance = Arrays.asList(mapSwitchDetails.get("ExpectedActualBalance").split(","));
				List<String> actualShares = Arrays.asList(mapSwitchDetails.get("ExpectedActualShares").split(","));
				List<String> availableAmount = Arrays.asList(mapSwitchDetails.get("ExpectedAvailableAmount").split(","));
				List<String> avaialableShares = Arrays.asList(mapSwitchDetails.get("ExpectedAvailableShares").split(","));				

				int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));				
				if(xpathCount != actualBalance.size()){
					Messages.errorMsg = "Actual Count of Tax lots in Switch is "+xpathCount+" , which is not matching with the Expected Atual Balance Count "+actualBalance.size()+" ";
					return false;
				}
				for(int i=0 ; i< actualBalance.size() ; i++){
					if(!actualBalance.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'Actual Balance' at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[5]"), actualBalance.get(i), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'Actual Shares' at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[6]"), actualShares.get(i), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'Available Amount' at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[7]"), availableAmount.get(i), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'Available Shares' at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[8]"), avaialableShares.get(i), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}					
				}
			}
			if(mapSwitchDetails.get("ExpectedTotalActualBalance") != null && mapSwitchDetails.get("ExpectedTotalActualShares") != null && mapSwitchDetails.get("ExpectedTotalAvailableAmount") != null && mapSwitchDetails.get("ExpectedTotalAvailableShares") != null){
				int lotsCountInclusiveOfTotalsRow = Elements.getXpathCount(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr"));
				if(!mapSwitchDetails.get("ExpectedTotalActualBalance").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '2' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[2]"), mapSwitchDetails.get("ExpectedTotalActualBalance"), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapSwitchDetails.get("ExpectedTotalActualShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'TotalActualShares' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '3' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[3]"), mapSwitchDetails.get("ExpectedTotalActualShares"), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapSwitchDetails.get("ExpectedTotalAvailableAmount").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'TotalAvailableAmount' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '4' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[4]"), mapSwitchDetails.get("ExpectedTotalAvailableAmount"), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapSwitchDetails.get("ExpectedTotalAvailableShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'TotalAvailableShares' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '5' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[5]"), mapSwitchDetails.get("ExpectedTotalAvailableShares"), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
			}
			Messages.errorMsg = appendErrMsg;
			return bValidStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Maker and Checker verifying Pending Trades Common function for both.
	public static boolean doMakerAndCheckerVerifyPendingTradesDetails(String sPTRequestedAmounts, String sPTRequestedShares, int iNoOfDecimalsExpected ,int iNoOfShareDecimalsExpected){
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
				Messages.errorMsg = "[ ERROR : Page cannot scroll to Pending Trades Details.]\n";
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr"), 4);			
			noOfAmountOrShareLots = Elements.getXpathCount(Global.driver, By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr"));
			if (!bStatus && sAmountsList.size() != noOfAmountOrShareLots) {
				Messages.errorMsg = "[ ERROR : Actual No Of Records for 'Pending Trades' : '"+noOfAmountOrShareLots+"' in Switch screen is not matching with Expected no : '"+sAmountsList.size()+"' ]\n";
				return false;
			}
			for (int i = 0; i <sAmountsList.size(); i++) {
				if(!sAmountsList.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Pending Trade' in Switch screen for lot 'Amount' value at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr["+(i+1)+"]//td[7]"), sAmountsList.get(i), "Yes", true, iNoOfDecimalsExpected);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if(!sSharesList.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Pending Trade' in Switch screen for lot 'Share' value at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr["+(i+1)+"]//td[8]"), sSharesList.get(i), "Yes", true, iNoOfShareDecimalsExpected);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
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

	//Verify / Modify the Charge details.
	public static boolean doMakerFillAndVerifyChargeDetails(Map<String, String> mapSwitchDetails)
	{
		boolean validateStatus = true;
		String appendMsg="";
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Charge Details");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for Charge Details. ]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-ncDefaultYes']//span"));

			//Notice Period Charges with option  "Yes"
			if(mapSwitchDetails.get("NoticeChargesRadioButton")!=null && mapSwitchDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){

				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-ncDefaultYes']//span"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+ "[ ERROR : Notice Charge Default Radio button Yes is not selected . ]\n";
				}
			}

			if (mapSwitchDetails.get("ExpectedNoticeCharges") != null) {
				// Verify Notice Charges
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Notice Charges, ", By.id("noticeChargeAmount"), mapSwitchDetails.get("ExpectedNoticeCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if(!bStatus){							
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}


			//Notice Period Charges with option  "No"
			if(mapSwitchDetails.get("NoticeChargesRadioButton")!=null && mapSwitchDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-ncDefaultNo']//span"));
				if(!bStatus){
					appendMsg =  appendMsg +"[ ERROR : Notice Period Charges 'No' Radio Button Not Clicked. ]\n";
					return false;
				}

				if(mapSwitchDetails.get("NewAmountForNoticePeriod") !=null && mapSwitchDetails.get("ExpectedNewNoticeCharges") !=null){

					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), Constants.lTimeOut);
					if(!bStatus){
						appendMsg =  appendMsg +"[ ERROR : Notice Period Charges 'New Charges' field not visible.]\n";
						return false;
					}
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), mapSwitchDetails.get("NewAmountForNoticePeriod"));
					if(!bStatus){
						appendMsg =  appendMsg +"[ ERROR : New Notice period Charges not Entered.]\n";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[@id='npSave']"));
					if(!bStatus){
						appendMsg =  appendMsg +"[ ERROR : New Notice Period Charges Save butotn not clicked.]\n";
						return false;
					}
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
					Thread.sleep(1000);
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("New Notice Charge Amount ", By.id("noticeChargeAmount"), mapSwitchDetails.get("ExpectedNewNoticeCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
					if (!bStatus) {
						appendMsg = appendMsg + Messages.errorMsg;
						validateStatus = false;
					}			
				}
			}

			//TransactionChargesCharges with option  "Yes"
			if(mapSwitchDetails.get("TransactionChargesRadioButton")!=null && mapSwitchDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){

				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-tcDefaultYes']//span"));
				bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tcDefaultYes']//span"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+ "[ Error : Transaction Charge Default Radio button Yes is not selected . ]\n";
				}
			}
			if (mapSwitchDetails.get("ExpectedTransactionCharges") != null) {				
				// Verify Transaction Charges
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transaction Charges, ", By.id("transactionChargeAmount"), mapSwitchDetails.get("ExpectedTransactionCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}

			//TransactionChargesCharges with option  "No"
			if(mapSwitchDetails.get("TransactionChargesRadioButton")!=null && mapSwitchDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-tcDefaultNo']//span"));
				if(!bStatus){
					appendMsg =  appendMsg +"[ ERROR : Fixed Transaction Charges No Radio Button Not Clicked . ]\n";
					return false;
				}
				if(mapSwitchDetails.get("NewTransactionCharges") != null && mapSwitchDetails.get("NewExpectedTransactionCharges") != null){					
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), Constants.lTimeOut);
					if(!bStatus){
						appendMsg =  appendMsg +"[ ERROR : Tranasaction Charges New Charges field not visible . ]\n";
						return false;
					}
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), mapSwitchDetails.get("NewTransactionCharges"));
					if(!bStatus){
						appendMsg =  appendMsg +"[ ERROR : New Transaction Charges are not Entered . ]\n";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.id("tpSave"));
					if(!bStatus){
						appendMsg =  appendMsg +"[ ERROR : New Transaction Charges Save butotn not clicked . ]\n";
						return false;
					}
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
					TimeUnit.SECONDS.sleep(1);
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transaction Charges, ", By.id("transactionChargeAmount"), mapSwitchDetails.get("NewExpectedTransactionCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
					if (!bStatus) {
						appendMsg = appendMsg + Messages.errorMsg;
						validateStatus = false;
					}			
				}
			}
			// Enter Adhoc Charges
			if (mapSwitchDetails.get("Adhoc Charges") != null && !mapSwitchDetails.get("Adhoc Charges").equalsIgnoreCase("None")) {
				bStatus = Elements.enterText(Global.driver, By.id("adHocCharges"), mapSwitchDetails.get("Adhoc Charges"));
				if (!bStatus) {
					appendMsg = appendMsg + "[ ERROR : Unable to Enter Adhoc Charges in Charge Details" + mapSwitchDetails.get("Adhoc Charges")+" .]\n";
					return validateStatus = false ;
				}
			}
			// Verify Total Charges 
			if (mapSwitchDetails.get("ExpectedTotalCharges") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Total Charges, ", By.xpath("//input[@id='totalCharges']"), mapSwitchDetails.get("ExpectedTotalCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}			
			Messages.errorMsg = appendMsg;
			return validateStatus;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Verify Amount Details Tab.
	public static boolean doMakerVerifyAmountDetailsTab(Map<String, String> mapSwitchDetails){
		boolean bValidatreStatus = true;
		String sAppendErrorMsg = "";
		try {			
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Amount Details");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for Amount Details. ]\n";
				return false;
			}			
			if (mapSwitchDetails.get("Gross Amount") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Gross Amount, ", By.id("grossAmountView"), mapSwitchDetails.get("Gross Amount"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")))) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Gross Amount is not Matched with Expected"  +mapSwitchDetails.get("Gross Amount")+" ] \n";
					bValidatreStatus = false;
				}
			}
			if (mapSwitchDetails.get("Charges") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Charges, ", By.id("totalChargesView"), mapSwitchDetails.get("Charges"), "No", true,Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")))){
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Charges is not Matched with Expected"  +mapSwitchDetails.get("Charges")+" ] \n";
					bValidatreStatus = false;
				}
			}
			if (mapSwitchDetails.get("Switch Amount") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Amount, ", By.id("exchangeAmountView"), mapSwitchDetails.get("Switch Amount"), "No", true,Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")))){
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Switch Amount is not Matched with Expected " +mapSwitchDetails.get("Switch Amount")+" ] \n";
					bValidatreStatus = false;
				}
			}
			if (mapSwitchDetails.get("Net Units") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Net Units, ", By.id("netUnitView"), mapSwitchDetails.get("Net Units"), "No", true,Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")))) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Net Units is not Matched with Expected"  +mapSwitchDetails.get("Net Units")+" ] \n";
					bValidatreStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidatreStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Fill Other Details.
	public static boolean doMakerSelectOtherDetailsRadioButtons(Map<String ,String> mapSwitchDetails){
		try {
			if(mapSwitchDetails.get("Crystalize Fee")!=null){
				if(mapSwitchDetails.get("Crystalize Fee").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isCrystalizedFee1']//span"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : Crystalize Fee 'Yes' Radio Button cannot be selected.]\n";
						return false;
					}
				}
				if(mapSwitchDetails.get("Crystalize Fee").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isCrystalizedFee2']//span"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : Crystalize Fee 'No' Radio Button cannot be selected.]\n";
						return false;
					}
				}
			}
			if(mapSwitchDetails.get("Cumulative Return")!=null){
				if(mapSwitchDetails.get("Cumulative Return").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isCummulative1']//span"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : Cumulative Return 'Yes' Radio Button cannot be selected.]\n";
						return false;
					}
				}
				if(mapSwitchDetails.get("Cumulative Return").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isCummulative2']//span"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : Cumulative Return 'No' Radio Button cannot be selected.]\n";
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

	//verify Exceptions.
	public static boolean doMakerVerifyExceptions(Map<String ,String> mapSwitchDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (mapSwitchDetails.get("ExpectedExceptionsAtMaker") != null) {
				List<String> sExceptionsList = Arrays.asList(mapSwitchDetails.get("ExpectedExceptionsAtMaker").split(":"));
				int iExpectedNoOfExceptions = sExceptionsList.size();
				int iActualNoOfExceptions = Elements.getXpathCount(Global.driver, By.xpath("//div[@class='alert alert-danger']"));				
				if (iActualNoOfExceptions != iExpectedNoOfExceptions) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : The Actual No Of Exceptions : '"+iActualNoOfExceptions+"' are not matching with Expected : '"+iExpectedNoOfExceptions+"' .]\n";
					bValidateStatus = false;
				}
				for (int i = 0; i < iExpectedNoOfExceptions; i++) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-danger' and contains(normalize-space(),'"+sExceptionsList.get(i)+"')]"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Expected Exception : '"+sExceptionsList.get(i)+"' is not displayed .]\n";
						bValidateStatus = false;
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


	/********************CHECKER FUNCTIONS*******************/

	//Checker Verify request Details.
	public static boolean doCheckerVerifySwitchRequestDetails(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if(mapSwitchDetails.get("Received Date")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Request Recieved Date, ",By.xpath("//input[@id='requestDate']"), mapSwitchDetails.get("Received Date"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("Received Time")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Received Time, ",By.xpath("//input[@id='requestTime']"), mapSwitchDetails.get("Received Time"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("Order Received Office")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Order Received Office, ",By.xpath("//label[normalize-space()='Order Received Office']//following-sibling::div//input"), mapSwitchDetails.get("Order Received Office"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("Time Zone")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Time Zone, ",By.xpath("//label[normalize-space()='Time Zone']//following-sibling::div//input"), mapSwitchDetails.get("Time Zone"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("Source")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Source, ",By.xpath("//input[@placeholder='Source']"), mapSwitchDetails.get("Source"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("Mode of Request")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Mode of Request, ",By.xpath("//label[normalize-space()='Mode of Request']//following-sibling::input"), mapSwitchDetails.get("Mode of Request"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("External ID Number")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("External ID Number, ",By.xpath("//label[normalize-space()='External ID Number']//following-sibling::input"), mapSwitchDetails.get("External ID Number"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//verify From-Investor Details.
	public static boolean doCheckerVerifySwitchFromInvestorDetailsTab(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "From Investor");
			if (!bStatus) {
				return false;
			}			
			if(mapSwitchDetails.get("From Investor Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Investor Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Investor')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Investor Name']//input"), mapSwitchDetails.get("From Investor Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("From Holder Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Holder Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Investor')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Holder Name']//input"), mapSwitchDetails.get("From Holder Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapSwitchDetails.get("From Account Id")!=null){
				String accountId = getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapSwitchDetails.get("From Account Id"));
				if(accountId != null && !accountId.equalsIgnoreCase("")){
					mapSwitchDetails.put("From Account Id", accountId);
				}

				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Account Id, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Investor')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Account ID']//input"), mapSwitchDetails.get("From Account Id"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}									
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Checker Verify To-Investor Details.
	public static boolean doCheckerVerifySwitchToInvestorDetailsTab(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "To Investor");
			if (!bStatus) {
				return false;
			}	

			if (mapSwitchDetails.get("New Investor") != null && mapSwitchDetails.get("New Investor").equalsIgnoreCase("yes") && mapSwitchDetails.get("New Investor TestCaseName") != null) {
				String investorName = getTheInvestorNameCreatedFromSwitch(mapSwitchDetails);
				mapSwitchDetails.put("To Investor Name", investorName);
			}

			if(mapSwitchDetails.get("To Investor Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Investor Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Investor')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Investor Name']//input"), mapSwitchDetails.get("To Investor Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//verify created New Holder
			if (mapSwitchDetails.get("New Holder") != null && mapSwitchDetails.get("New Holder").equalsIgnoreCase("yes") && mapSwitchDetails.get("New Holder TestCaseName")!=null) {
				String holderName = getTheHolderNameCreatedFromSwtitch(mapSwitchDetails);
				mapSwitchDetails.put("To Holder Name", holderName);
			}
			if(mapSwitchDetails.get("To Holder Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Holder Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Investor')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Holder Name']//input"), mapSwitchDetails.get("To Holder Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapSwitchDetails.get("NewAccount") != null && mapSwitchDetails.get("NewAccount").equalsIgnoreCase("Yes")) {
				mapSwitchDetails.put("To Account ID", mapSwitchDetails.get("TestCaseName"));
			}
			if(mapSwitchDetails.get("To Account ID")!=null){
				String accountId = getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapSwitchDetails.get("To Account ID"));
				if(accountId != null && !accountId.equalsIgnoreCase("")){
					mapSwitchDetails.put("To Account ID", accountId);
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Account ID, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Investor')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Account ID']//input"), mapSwitchDetails.get("To Account ID"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Checker Verify FromFund Details.
	public static boolean doCheckerVerifySwitchFromFundDetailsTab(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try{
			NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Fund Details')]"));			
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "From Fund Details");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for From-Fund Details. ]\n";
				return false;
			}
			//verify Client Name
			if(mapSwitchDetails.get("From Client Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Client Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Client Name']//input"), mapSwitchDetails.get("From Client Name"), "No", false);				
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//verify Fund Family
			if(mapSwitchDetails.get("From Fund Family Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Fund Family Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Fund Family Name']//input"), mapSwitchDetails.get("From Fund Family Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//verify Legal Entity
			if(mapSwitchDetails.get("From Legal Entity Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Legal Entity Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Legal Entity Name']//input"), mapSwitchDetails.get("From Legal Entity Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//verify Class Name
			if(mapSwitchDetails.get("From Class Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Class Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Class Name']//input"), mapSwitchDetails.get("From Class Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//verify the Series Name
			if(mapSwitchDetails.get("From Series Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Series Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Series Name']//input"), mapSwitchDetails.get("From Series Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Checker Verify ToFund Details.
	public static boolean doCheckerVerifySwitchToFundDetailsTab(Map<String, String> mapSwitchDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Fund Details')]"));
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "To Fund Details");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for To-fund Details. ]\n";
				return false;
			}
			//select Client Name
			if(mapSwitchDetails.get("From Client Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Client Name ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Client Name']//input"), mapSwitchDetails.get("From Client Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//Select Fund Family
			if(mapSwitchDetails.get("From Fund Family Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Fund Family Name ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Fund Family Name']//input"), mapSwitchDetails.get("From Fund Family Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Legal Entity
			if(mapSwitchDetails.get("From Legal Entity Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Legal Entity Name ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Legal Entity Name']//input"), mapSwitchDetails.get("From Legal Entity Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Class Name
			if(mapSwitchDetails.get("To Class Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Class Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Class Name']//input"), mapSwitchDetails.get("To Class Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			// Select the Series Name
			if(mapSwitchDetails.get("To Series Name") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Series Name, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Fund Details')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div[@data-original-title='Series Name']//input"), mapSwitchDetails.get("To Series Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Checker Verify Switch Details.
	public static boolean doCheckerVerifySwitchDetailsTab(Map<String, String> mapSwitchDetails){
		String expectedValue="";
		String actualValue="";

		String appndErrMsg = "";
		boolean bValidateStatus = true;				
		try {
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[contains(normalize-space(),'Full Switch')]//following-sibling::div//label//input"));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for Switch Details. ]\n";
				return false;
			}
			//Verify Full Transfer Radio button
			if (mapSwitchDetails.get("Full Switch") != null) {
				if (mapSwitchDetails.get("Full Switch").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(normalize-space(),'Full Switch')]//following-sibling::div//label//input[@value='1']//parent::span[@class='checked']"), 2);
					if (!bStatus) {
						appndErrMsg = appndErrMsg + "[ ERROR : Full Switch wasn't marked to 'Yes'.]\n";
						bValidateStatus = false;
					}
					if (bStatus) {
						boolean bDisabledStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(normalize-space(),'Full Switch')]//following-sibling::div//label//input[@value='1' and @disabled='disabled']//parent::span[@class='checked']"), 2);
						if (!bDisabledStatus) {
							appndErrMsg = appndErrMsg + "[ ERROR : Radio Button 'Yes' for Full Switch wasn't in disabled state at Checker Screen.]\n";
							bValidateStatus = false;
						}
					}
				}
				if (mapSwitchDetails.get("Full Switch").equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(normalize-space(),'Full Switch')]//following-sibling::div//label//input[@value='0']//parent::span[@class='checked']"), 2);
					if (!bStatus) {
						appndErrMsg = appndErrMsg + "[ ERROR : Full Switch wasn't marked to 'No'.]\n";
						bValidateStatus = false;
					}
					if (bStatus) {
						boolean bDisabledStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(normalize-space(),'Full Switch')]//following-sibling::div//label//input[@value='0' and @disabled='disabled']//parent::span[@class='checked']"), 2);
						if (!bDisabledStatus) {
							appndErrMsg = appndErrMsg + "[ ERROR : Radio Button 'No' for Full Switch wasn't in disabled state at Checker Screen.]\n";
							bValidateStatus = false;
						}
					}
				}
			}			
			//Verify Amount or shares
			if (mapSwitchDetails.get("AmountorShares") != null) {
				//Verify the Amount
				if (mapSwitchDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-amountShare1']//span[@class='checked']"), 2);
					if (!bStatus) {
						appndErrMsg = appndErrMsg + "[ ERROR : AmountorShares wasn't marked to 'Amount'.]\n";
						bValidateStatus = false;
					}
					if (mapSwitchDetails.get("Amount Value") != null) {				
						if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Amount to Switch, ", By.xpath("//input[@id='amountValue']"), mapSwitchDetails.get("Amount Value"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")))) {
							appndErrMsg = appndErrMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
				}
				if (mapSwitchDetails.get("AmountorShares").equalsIgnoreCase("Shares")) {
					//Verify the Shares
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-amountShare2']//span[@class='checked']"), 2);
					if (!bStatus) {
						appndErrMsg = appndErrMsg + "[ ERROR : AmountorShares wasn't marked to 'Shares'.]\n";
						bValidateStatus = false;
					}
					if (mapSwitchDetails.get("Share Value") != null) {				
						if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Shares to Switch, ", By.xpath("//input[@id='share']"), mapSwitchDetails.get("Share Value"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")))) {
							appndErrMsg = appndErrMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
				}
			}
			//Verify Effective Date
			if (mapSwitchDetails.get("Effective Date") != null) {
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Effective Date, ", By.xpath("//input[@id='effectiveDate']"), mapSwitchDetails.get("Effective Date"), "No", false)) {
					appndErrMsg = appndErrMsg + Messages.errorMsg;
					bValidateStatus = false;
				}				
			}
			//Verify Percentage 
			if (mapSwitchDetails.get("Percentage") != null) {				
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Percentage, ", By.xpath("//input[@id='percentage']"), mapSwitchDetails.get("Percentage"), "No", true, -1)) {
					appndErrMsg = appndErrMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapSwitchDetails.get("Expected NAV Date") != null) {	
				actualValue = Elements.getText(Global.driver, By.xpath("//Label[normalize-space()='NAV Date']//following-sibling::label"));
				if (actualValue != null && !actualValue.equalsIgnoreCase("")) {
					actualValue = TradeTypeSubscriptionAppFunctions.formatDate(actualValue);
					expectedValue = mapSwitchDetails.get("Expected NAV Date");
					if (!actualValue.trim().equalsIgnoreCase(expectedValue)) {
						appndErrMsg = appndErrMsg + "[ ERROR: Expected NAV Date : "+mapSwitchDetails.get("Expected NAV Date")+" ,is not matching with actual : '"+actualValue+"']\n";
						bValidateStatus = false;
					}
				}
				else {
					appndErrMsg = appndErrMsg + "[ Actual Value :'"+actualValue+"' is Empty or Null]\n";
					bValidateStatus = false;
				}				
			}
			Messages.errorMsg = appndErrMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	//Checker Verify filled Lots Balance Details.
	private static boolean doCheckerVerifyAllocatedAmountAndShare(Map<String, String> mapExchangeDetails) {
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		List<String> sAmountOrSharesList = null;
		List<String> sVerifyAmountOrSharesList = null;
		try {
			String sIsAmountOrShare = "";
			String verifyAmountOrShares = "";
			String columnNumber = "";
			String verifyColumnNumber = "";
			NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]"));
			bStatus = doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Available Balance");
			if (!bStatus) {
				return false;
			}
			//Split Taxlots Amount or Share
			if (mapExchangeDetails.get("TaxLotsAmounts") != null || mapExchangeDetails.get("TaxLotsShares") != null) 
			{
				if (mapExchangeDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "amount";
					columnNumber = "11";
					sAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShares = "share";
					verifyColumnNumber = "12";
				}
				else if (mapExchangeDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "share";
					columnNumber = "12";
					sAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShares = "amount";
					verifyColumnNumber = "11";
				}
			}
			//Split Expected Taxlots Amount or Share
			if(mapExchangeDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("ExpectedTaxLotAmountOrShare").split(","));
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"), 3);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tax lots are not visible.]\n";
				return false;
			}
			int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
			if(sAmountOrSharesList.size() != NoOfRecords){
				Messages.errorMsg = "[ ERROR : Expected No lots : '"+sAmountOrSharesList.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
				return false;
			}
			if (!sIsAmountOrShare.equalsIgnoreCase("") && mapExchangeDetails.get("Full Switch") != null && mapExchangeDetails.get("Full Switch").equalsIgnoreCase("No")) 
			{
				for (int i = 0; i < sAmountOrSharesList.size(); i++) {
					if(!sAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div[contains(@class,'portlet')]//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td["+columnNumber+"]//span"), sAmountOrSharesList.get(i), "Yes", true, Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")));
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
					if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div[contains(@class,'portlet')]//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td["+verifyColumnNumber+"]//span"), sVerifyAmountOrSharesList.get(i), "Yes", true, Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")));
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
				}			
			}
			if (mapExchangeDetails.get("Full Switch") != null && mapExchangeDetails.get("Full Switch").equalsIgnoreCase("Yes")) {
				bStatus = doCheckerVerifyFullSwitchValuesInAvaialableBalance(mapExchangeDetails);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	//Checker Verify Available Balance Details.
	private static boolean doCheckerVerifyFullSwitchValuesInAvaialableBalance(Map<String, String> mapExchangeDetails) {
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;
			if(mapExchangeDetails.get("ExcpectedAvailableAmount") != null && mapExchangeDetails.get("ExcpectedAvailableShares") != null){
				List<String> allocatedAmount = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableShares").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "[ ERROR : Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally.]\n";
					return false;
				}
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
				if(allocatedAmount.size() != NoOfRecords){
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+allocatedAmount.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				int noOfDecimalsToDispaly = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals"));
				for(int i=0; i<allocatedAmount.size(); i++){
					if(!allocatedAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot '"+allocatedAmount.get(i)+"' at index : '"+i+"'", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div[contains(@class,'portlet')]//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[11]//span"), allocatedAmount.get(i), "Yes", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot '"+allocatedShares.get(i)+"' at index : '"+i+"'", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div[contains(@class,'portlet')]//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[12]//span"), allocatedShares.get(i), "Yes", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
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


	public static boolean doVerifyAllocatedAmountAndShare(Map<String,String> mapExchangeDetails){
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;
			if(mapExchangeDetails.get("CheckerExpectedAllocatedAmount") != null && mapExchangeDetails.get("CheckerExpectedAllocatedShare") != null){
				List<String> allocatedAmount = Arrays.asList(mapExchangeDetails.get("CheckerExpectedAllocatedAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapExchangeDetails.get("CheckerExpectedAllocatedShare").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "[ ERROR : Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally.]\n";
					return false;
				}
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
				if(allocatedAmount.size() != NoOfRecords){
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+allocatedAmount.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				int noOfDecimalsToDispaly = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals"));
				int noOfShareDecimalsToDispaly = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals"));
				for(int i=0; i<allocatedAmount.size(); i++){
					if(!allocatedAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot Allocated Amount '"+allocatedAmount.get(i)+"' at index : '"+i+"'", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div[contains(@class,'portlet')]//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[11]//span"), allocatedAmount.get(i), "Yes", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch tax lot Allocated Share '"+allocatedShares.get(i)+"' at index : '"+i+"'", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div[contains(@class,'portlet')]//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[12]//span"), allocatedShares.get(i), "Yes", true, noOfShareDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
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
	//Checker Verifying tax lots Available balance Details.
	public static boolean doCheckerVerifyAllSwitchAvailableBalanceDetails(Map<String, String> mapSwitchDetails){
		try {
			boolean bValidStatus = true;
			String appendErrMsg = "";
			if(mapSwitchDetails.get("ExpectedActualBalance") != null && mapSwitchDetails.get("ExpectedActualShares") != null && mapSwitchDetails.get("ExpectedAvailableAmount") != null && mapSwitchDetails.get("ExpectedAvailableShares") != null )
			{
				List<String> actualBalance = Arrays.asList(mapSwitchDetails.get("ExpectedActualBalance").split(","));
				List<String> actualShares = Arrays.asList(mapSwitchDetails.get("ExpectedActualShares").split(","));
				List<String> availableAmount = Arrays.asList(mapSwitchDetails.get("ExpectedAvailableAmount").split(","));
				List<String> avaialableShares = Arrays.asList(mapSwitchDetails.get("ExpectedAvailableShares").split(","));				

				int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr//td//div[contains(@id,'check')]"));				
				if(xpathCount != actualBalance.size()){
					Messages.errorMsg = "[ ERROR : Actual Count of Tax lots in Switch is "+xpathCount+" , which is not matching with the Expected Atual Balance Count "+actualBalance.size()+" ]\n";
					return false;
				}

				for(int i=0 ; i< actualBalance.size() ; i++){
					if(!actualBalance.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'Actual Balance' at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[5]//span"), actualBalance.get(i), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'Actual Shares' at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[6]//span"), actualShares.get(i), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'Available Amount' at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[7]//span"), availableAmount.get(i), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'Available Shares' at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+(i+1)+"]//td[8]//span"), avaialableShares.get(i), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}					
				}
			}

			if(mapSwitchDetails.get("ExpectedTotalActualBalance") != null && mapSwitchDetails.get("ExpectedTotalActualShares") != null && mapSwitchDetails.get("ExpectedTotalAvailableAmount") != null && mapSwitchDetails.get("ExpectedTotalAvailableShares") != null){
				int lotsCountInclusiveOfTotalsRow = Elements.getXpathCount(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr"));
				if(!mapSwitchDetails.get("ExpectedTotalActualBalance").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '2' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[2]//span"), mapSwitchDetails.get("ExpectedTotalActualBalance"), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapSwitchDetails.get("ExpectedTotalActualShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'TotalActualShares' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '3' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[3]//span"), mapSwitchDetails.get("ExpectedTotalActualShares"), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapSwitchDetails.get("ExpectedTotalAvailableAmount").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'TotalAvailableAmount' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '4' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[4]//span"), mapSwitchDetails.get("ExpectedTotalAvailableAmount"), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapSwitchDetails.get("ExpectedTotalAvailableShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Switch Available Balance Lot for 'TotalAvailableShares' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '5' ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//..//following-sibling::div[contains(@class,'portlet')]//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[5]//span"), mapSwitchDetails.get("ExpectedTotalAvailableShares"), "Yes", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")));						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
			}

			Messages.errorMsg = appendErrMsg;
			return bValidStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Checker Verify Charge Details.
	public static boolean doCheckerVerifySwitchChargesDetails(Map<String, String> mapSwitchDetails)
	{
		boolean validateStatus = true;
		String appendMsg="";
		try {
			NewUICommonFunctions.scrollToView(By.xpath("//label[text()='Notice Charge Default']"));

			//verify Notice Charges for Notice Charge Default=YES
			if (mapSwitchDetails.get("NoticeChargesRadioButton") != null && mapSwitchDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")) {
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[text()='Notice Charge Default']//following-sibling::div//label[contains(normalize-space(),'Yes')]//input//parent::span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+ "[ ERROR : Notice Charge Default Radio button 'Yes' is not selected .]\n";
				}
			}
			if (mapSwitchDetails.get("ExpectedNoticeCharges") != null) {					
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Notice Charges, ", By.xpath("//label[normalize-space()='Notice Charges']//following-sibling::div//input"), mapSwitchDetails.get("ExpectedNoticeCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}

			//verify Notice Charges for Notice Charge Default=NO
			if (mapSwitchDetails.get("ExpectedNewNoticeCharges") != null) {
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[text()='Notice Charge Default']//following-sibling::div//label[contains(normalize-space(),'No')]//input//parent::span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+ "[ ERROR : Notice Charge Default Radio button 'No' is not selected  ]\n";
				}
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("New Notice Charges, ", By.xpath("//label[normalize-space()='Notice Charges']//following-sibling::div//input"), mapSwitchDetails.get("ExpectedNewNoticeCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}
			//Verify TransactionCharges for Transaction Charge Default =YES
			if (mapSwitchDetails.get("TransactionChargesRadioButton") != null && mapSwitchDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")) {
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[text()='Transaction Charge Default']//following-sibling::div//label[contains(normalize-space(),'Yes')]//input//parent::span[@class='checked']"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : Transaction Charge Default Radio button 'Yes' is not selected  ]\n";
					validateStatus = false;
				}
			}
			if (mapSwitchDetails.get("ExpectedTransactionCharges") != null) {				
				// Verify Transaction Charges
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transaction Charges, ", By.xpath("//label[normalize-space()='Transaction Charges']//following-sibling::div//input"), mapSwitchDetails.get("ExpectedTransactionCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}
			//Verify TransactionCharges for Transaction Charge Default =NO
			if (mapSwitchDetails.get("NewExpectedTransactionCharges") != null) {
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[text()='Transaction Charge Default']//following-sibling::div//label[contains(normalize-space(),'No')]//input//parent::span[@class='checked']"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : Transaction Charge Default Radio button 'No' is not selected  ]\n";
					validateStatus = false;
				}
				// Verify Transaction Charges
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("New Transaction Charges, ", By.xpath("//label[normalize-space()='Transaction Charges']//following-sibling::div//input"), mapSwitchDetails.get("NewExpectedTransactionCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}
			// Verify Adhoc Charges
			if (mapSwitchDetails.get("Adhoc Charges") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Adhoc Charges, ", By.xpath("//label[normalize-space()='Adhoc Charges']//following-sibling::div//input"), mapSwitchDetails.get("Adhoc Charges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}
			// Verify Total Charges 
			if (mapSwitchDetails.get("ExpectedTotalCharges") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Total Charges, ", By.xpath("//label[normalize-space()='Total Charges']//following-sibling::div//input"), mapSwitchDetails.get("ExpectedTotalCharges"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendMsg = appendMsg + Messages.errorMsg;
					validateStatus = false;
				}
			}	
			Messages.errorMsg = appendMsg;
			return validateStatus;
		}	
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Verify Amount Details Tab.
	public static boolean doCheckerVerifySwitchAmountDetailsTab(Map<String, String> mapSwitchDetails){
		boolean bValidatreStatus = true;
		String sAppendErrorMsg = "";
		try {			
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Amount Details");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab Expando Icon is not clicked for Amount Details. ]\n";
				return false;
			}			
			if (mapSwitchDetails.get("Gross Amount") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Gross Amount in Amount Details, ", By.id("grossAmountView"), mapSwitchDetails.get("Gross Amount"), "No", true, Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")))) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidatreStatus = false;
				}
			}
			if (mapSwitchDetails.get("Charges") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Charges in Amount Details, ", By.id("totalChargesView"), mapSwitchDetails.get("Charges"), "No", true,Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")))){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidatreStatus = false;
				}
			}
			if (mapSwitchDetails.get("Switch Amount") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange Amount in Amount Details, ", By.id("exchangeAmountView"), mapSwitchDetails.get("Switch Amount"), "No", true,Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfDecimals")))){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidatreStatus = false;
				}
			}
			if (mapSwitchDetails.get("Net Units") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Net Units in Amount Details, ", By.id("netUnitView"), mapSwitchDetails.get("Net Units"), "No", true,Integer.parseInt(mapSwitchDetails.get("ExpectedNumberOfShareDecimals")))) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidatreStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidatreStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Verify Other Details.
	public static boolean doCheckerVerifyOtherDetailsRadioButtons(Map<String ,String> mapSwitchDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if(mapSwitchDetails.get("Crystalize Fee")!=null){
				if(mapSwitchDetails.get("Crystalize Fee").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@name='crystalizeFee' and @value='1']//parent::span[@class='checked']"), 2);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Crystalize Fee 'Yes' Radio Button is not appeared to be selected .]\n";
						bValidateStatus = false;
					}
				}
				if(mapSwitchDetails.get("Crystalize Fee").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@name='crystalizeFee' and @value='0']//parent::span[@class='checked']"), 2);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Crystalize Fee 'No' Radio Button is not appeared to be selected .]\n";
						bValidateStatus = false;
					}
				}
			}
			if(mapSwitchDetails.get("Cumulative Return")!=null){
				if(mapSwitchDetails.get("Cumulative Return").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@name='crystalizeReturn' and @value='1']//parent::span[@class='checked']"), 2);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Cumulative Return 'Yes' Radio Button is not appeared to be selected .]\n";
						bValidateStatus = false;
					}
				}
				if(mapSwitchDetails.get("Cumulative Return").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@name='crystalizeReturn' and @value='0']//parent::span[@class='checked']"), 2);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Cumulative Return 'No' Radio Button is not appeared to be selected .]\n";
						bValidateStatus = false;
					}
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Checker Approve created Masters.
	public static boolean doCheckerOpeartionsOnMastersCreatedFromTrade(Map<String, String> mapSwitchDetails)
	{
		//Approve for New Investor Created for Trading
		if(mapSwitchDetails.get("New Investor")!=null && mapSwitchDetails.get("New Investor").equalsIgnoreCase("Yes"))
		{
			if (mapSwitchDetails.get("NewInvestorOperation")!=null){
				bStatus = NewUICommonFunctions.scrollToView(By.id("approveInvestor"));
				bStatus = Elements.click(Global.driver, By.id("approveInvestor"));
				if (!bStatus) 
				{
					Messages.errorMsg = "[ ERROR : Unable to click on Approve button for 'Investor Master' ]\n";
					return false;
				}
			}

			//Call functions to APPROVE or REJECT or RETURN the created Masters.
			if (mapSwitchDetails.get("NewInvestorOperation")!=null && mapSwitchDetails.get("NewInvestorOperation").equalsIgnoreCase("Approve")) {
				if(!TradeTypeTransferAppFunctions.doCheckerActionTypesOnMasters(checkerActionTypes.APPROVE))
				{
					return false;
				}
			}
			if (mapSwitchDetails.get("NewInvestorOperation")!=null && mapSwitchDetails.get("NewInvestorOperation").equalsIgnoreCase("Return")) 
			{
				if(!TradeTypeTransferAppFunctions.doCheckerActionTypesOnMasters(checkerActionTypes.RETURN))				 
				{
					return false;
				}
			}
			if (mapSwitchDetails.get("NewInvestorOperation")!=null && mapSwitchDetails.get("NewInvestorOperation").equalsIgnoreCase("Cancel")) 
			{
				if(!TradeTypeTransferAppFunctions.doCheckerActionTypesOnMasters(checkerActionTypes.CANCEL))				
				{
					return false;
				}
			}
			if (mapSwitchDetails.get("NewInvestorOperation")!=null && mapSwitchDetails.get("NewInvestorOperation").equalsIgnoreCase("Reject")) 
			{
				if(!TradeTypeTransferAppFunctions.doCheckerActionTypesOnMasters(checkerActionTypes.REJECT)) 
				{
					return false;
				}
			}
		}

		//Approve for New Holder Created for Trading
		if (mapSwitchDetails.get("New Holder")!=null && mapSwitchDetails.get("New Holder").equalsIgnoreCase("Yes")) {
			if (mapSwitchDetails.get("NewHolderOperation") != null){
				bStatus = NewUICommonFunctions.scrollToView(By.id("approveHolder"));
				bStatus = Elements.click(Global.driver, By.id("approveHolder"));
				if (!bStatus) {
					Messages.errorMsg = "unable to click on Approve button for Holder";
					return false;
				}
			}
			//call function to approve or return or reject
			if (mapSwitchDetails.get("NewHolderOperation") != null && mapSwitchDetails.get("NewHolderOperation").equalsIgnoreCase("Approve")) {
				if(!TradeTypeTransferAppFunctions.doCheckerActionTypesOnMasters(checkerActionTypes.APPROVE)){
					return false;
				}
			}
			if (mapSwitchDetails.get("NewHolderOperation") != null && mapSwitchDetails.get("NewHolderOperation").equalsIgnoreCase("Return")) {
				if (!TradeTypeTransferAppFunctions.doCheckerActionTypesOnMasters(checkerActionTypes.RETURN)){
					return false;
				}
			}
			if (mapSwitchDetails.get("NewHolderOperation") != null && mapSwitchDetails.get("NewHolderOperation").equalsIgnoreCase("Reject")) {
				if (!TradeTypeTransferAppFunctions.doCheckerActionTypesOnMasters(checkerActionTypes.REJECT)){
					return false;
				}
			}
			if (mapSwitchDetails.get("NewHolderOperation") != null && mapSwitchDetails.get("NewHolderOperation").equalsIgnoreCase("Cancel")) {
				if (!TradeTypeTransferAppFunctions.doCheckerActionTypesOnMasters(checkerActionTypes.CANCEL)){
					return false;
				}
			}
		}
		return true;
	}

	//Override charges approval/verification at checker screen.
	public static boolean doCheckerVerifyOrChangeOverridedDetails(String sOverridedChargeName, String isChangeStatusNoToYes){
		try {
			String noXpath = "";
			String yesXpath = "";
			String sChargeTypeNameSubString = "";
			if (sOverridedChargeName != null) {
				NewUICommonFunctions.scrollToView(By.xpath("//strong[normalize-space()='Override']"));
				if (sOverridedChargeName.toLowerCase().contains("notice")) {
					sChargeTypeNameSubString = "Notice";
					noXpath = "//input[@name='npOverride' and @value='0']//parent::span";
					yesXpath = "//input[@name='npOverride' and @value='1']//parent::span";					
				}
				if (sOverridedChargeName.toLowerCase().contains("transaction")) {
					sChargeTypeNameSubString = "Transaction";
					noXpath = "//input[@name='transOverride' and @value='0']//parent::span";
					yesXpath = "//input[@name='transOverride' and @value='1']//parent::span";
				}
				if (sOverridedChargeName.toLowerCase().contains("incentive")) {
					sChargeTypeNameSubString = "Incentive";
					noXpath = "//input[@name='crysOverride' and @value='0']//parent::span";
					yesXpath = "//input[@name='crysOverride' and @value='1']//parent::span";
				}
			}
			if (sChargeTypeNameSubString != null && !sChargeTypeNameSubString.equalsIgnoreCase("")) 
			{
				if (isChangeStatusNoToYes != null && isChangeStatusNoToYes.equalsIgnoreCase("No")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath(noXpath));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to change the status of Overrided chanrges of '"+sChargeTypeNameSubString+"' to 'No'.] \n";
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(noXpath+"[@class='checked']"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Override charges approval for type : '"+sChargeTypeNameSubString+"' ,wasn't able to change from 'Yes' to 'No' .]\n";
						return false;
					}
				}
				if (isChangeStatusNoToYes != null && isChangeStatusNoToYes.equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath(yesXpath));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to change the status of Overrided chanrges of '"+sChargeTypeNameSubString+"' to 'YES'.] \n";
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(yesXpath+"[@class='checked']"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Override charges approval for type : '"+sChargeTypeNameSubString+"' ,wasn't able to change from 'No' to 'Yes' .]\n";
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Verify Exceptions.
	public static boolean doCheckerVerifyExceptions(String sExceptions){
		String appendErrMsg = "";
		boolean bValidStatus = true;
		try {	
			String locatorOverridePresent = "//strong[normalize-space()='Exception']//..//following-sibling::h4[contains(normalize-space(),'Override')]//preceding-sibling::div//label";
			String locatorOnlyExceptionsPresent = "//h4[normalize-space()='Exception']//following-sibling::div//label";
			int xpathCount = 0;
			String totalExceptions = "";
			List<String> sMissingExceptionList = new ArrayList<String>();
			if (sExceptions != null && !sExceptions.equalsIgnoreCase("")) {
				List<String> sExceptionList = Arrays.asList(sExceptions.split(":"));
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(locatorOverridePresent), 3);
				if(bStatus)
				{
					xpathCount = Elements.getXpathCount(Global.driver, By.xpath(locatorOverridePresent));		
					for(int j=1; j<=xpathCount ;j++)
					{
						String exceptionValue = Elements.getText(Global.driver, By.xpath("//strong[normalize-space()='Exception']//..//following-sibling::h4[contains(normalize-space(),'Override')]//preceding-sibling::div["+j+"]//label"));
						if(exceptionValue != null && !exceptionValue.equalsIgnoreCase("")){
							totalExceptions = totalExceptions+"["+exceptionValue+"]";
						}
					}
				}else{
					xpathCount = Elements.getXpathCount(Global.driver, By.xpath(locatorOnlyExceptionsPresent));
					for(int j=1; j<=xpathCount ;j++)
					{
						String exceptionValue = Elements.getText(Global.driver, By.xpath("//h4[normalize-space()='Exception']//following-sibling::div["+j+"]//label"));
						if(exceptionValue != null && !exceptionValue.equalsIgnoreCase("")){
							totalExceptions = totalExceptions+"["+exceptionValue+"]";
						}
					}
				}
				if(xpathCount != sExceptionList.size()){
					Messages.errorMsg = "[ ERROR : Actual Exception count : '"+xpathCount+"' is not matching with the Expected Exception count : '"+sExceptionList.size()+"' ,Actual Exceptions are : '"+totalExceptions+"' ,Expected exceptions are : '"+sExceptionList+"' ]\n";
					return false;
				}
				for(int i=0 ; i<sExceptionList.size(); i++){
					if(!totalExceptions.contains(sExceptionList.get(i))){
						sMissingExceptionList.add(sExceptionList.get(i));						
						bValidStatus = false;
					}					
				}
				if (!bValidStatus) {
					appendErrMsg = appendErrMsg + "[ ERROR : Expected exception list : '"+sExceptionList+"' ,is not matching with the Actual exception list : '"+totalExceptions+"', missed exceptions are : '"+sMissingExceptionList+"' ]\n";
				}
			}
			Messages.errorMsg = appendErrMsg;
			return bValidStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	//Verify Order Acknowledgment file download.
	public static boolean doCheckerVerifyOrderAcknowledgementDownload(Map<String , String> mapSwitchDetails) 
	{
		try {
			bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[contains(@class, 'input-group')]/a[contains(@onclick, 'javascript:viewDocuments')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to click View Oder Acknowledgement.]\n";
				return false ;
			}
			TimeUnit.SECONDS.sleep(5);
			File folder = new File(System.getProperty("user.home")+"/Downloads");
			File[] fileList = folder.listFiles();
			String investorName = mapSwitchDetails.get("From Investor Name").replaceAll(" ", "_").replaceAll("\\W", "");
			for (int index = 0; index < fileList.length; index++) {
				if (fileList[index].getName().contains(investorName)) {
					fileList[index].delete();
					return true;
				}
			}
			Messages.errorMsg = "[ ERROR : Unable to download the OA file.]\n";
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Verify Order Status Tab Details.
	public static boolean doCheckerVerifyOrderStatusDetails(Map<String , String> mapSwitchDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if(mapSwitchDetails.get("Transaction ID") != null && !OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Order Number, ", By.xpath("//input[@data-original-title='Order No']"), mapSwitchDetails.get("Transaction ID"), "No", false)){
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			/*if(mapSwitchDetails.get("Order Date") != null && !OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Order Date, ", By.xpath("//input[@data-original-title='Order Date']"), mapSwitchDetails.get("Order Date"), "No", false)){
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}*/
			if(mapSwitchDetails.get("Approve OA") != null){
				if (mapSwitchDetails.get("Approve OA").equalsIgnoreCase("Yes")) {
					if(!Elements.click(Global.driver, By.xpath("//input[@id='approveOAYes']"))){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to select 'YES' to Approve OA .]\n";
						bValidateStatus = false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='approveOAYes']//parent::span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to select 'YES' to Approve OA .]\n";
						bValidateStatus = false;
					}
				}
				if (mapSwitchDetails.get("Approve OA").equalsIgnoreCase("No")) {
					if(!Elements.click(Global.driver, By.xpath("//input[@id='approveOANo']"))){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to select 'NO' to Approve OA .]\n";
						bValidateStatus = false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='approveOANo']//parent::span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to select 'NO' to Approve OA .]\n";
						bValidateStatus = false;
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

	//sub operations on trade.
	public static boolean doSubOperationsOnTransactionTrades(String master , String operation){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//button[contains(@onclick,'javascript:submitSwitch') and contains(normalize-space(),'Save')]") , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : '"+master+"' Save Button is Not Visible.]\n";
				return false;
			}
			if(operation.equalsIgnoreCase("Save")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitSwitch') and contains(normalize-space(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : '"+master+"' 'Save' button cannot be clicked.]\n";
					return false;
				}
				return true;

			}
			if(operation.contains("Cancel")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:cancelDashboard') and contains(normalize-space(),'Cancel')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : '"+master+"' 'Cancel' button cannot be clicked.]\n";
					return false;
				}
				return true;
			}

			if(operation.contains("Review")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitSwitch') and contains(normalize-space(),'Send for Review')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : '"+master+"' 'Send For Review' button cannot be clicked.]\n";
					return false;
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Charge override function in checker Side
	public static boolean doCheckerChangeOverrideStatus(Map<String , String> mapSwitchDetails){
		try {
			if(mapSwitchDetails.get("NewAmountForNoticePeriod")!=null && mapSwitchDetails.get("NoticePeriodOverrideAtChecker")!=null){
				bStatus = doCheckerVerifyOrChangeOverridedDetails("Notice Period", mapSwitchDetails.get("NoticePeriodOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}
			if(mapSwitchDetails.get("NewTransactionCharges")!=null && mapSwitchDetails.get("TransactionOverrideAtChecker")!=null){
				bStatus = doCheckerVerifyOrChangeOverridedDetails("Transaction", mapSwitchDetails.get("TransactionOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}
			if(mapSwitchDetails.get("Crystalize Fee")!=null && !mapSwitchDetails.get("Crystalize Fee").equalsIgnoreCase("No") && mapSwitchDetails.get("CrystalizeOverrideAtChecker")!=null){
				bStatus = doCheckerVerifyOrChangeOverridedDetails("Incentive Crystalize", mapSwitchDetails.get("CrystalizeOverrideAtChecker"));
				if(!bStatus){
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

	//Checker Operation on SWITCH trade.
	public static boolean performCheckerOperationOnTrade(Map<String , String> mapSwitchDetails) {
		try {

			bStatus = doCheckerOpeartionsOnMastersCreatedFromTrade(mapSwitchDetails);
			if (!bStatus) {
				return false;
			}
		
			//download OA and Delete
			if(mapSwitchDetails.get("isOAClient") != null){
				bStatus = doCheckerVerifyOrderAcknowledgementDownload(mapSwitchDetails);
				if(!bStatus && mapSwitchDetails.get("isOAClient").equalsIgnoreCase("Yes")){
					return false;
				}
				if(bStatus && mapSwitchDetails.get("isOAClient").equalsIgnoreCase("No")){
					Messages.errorMsg = "Order Acknowldegement Downloaded for Non OA Client";
					return false;
				}
			}
			if(mapSwitchDetails.get("Approve OA")!=null && mapSwitchDetails.get("Approve OA").equalsIgnoreCase("Yes")){
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-approveOANo']/span[@class='checked']/input"),Constants.lTimeOut);
				if(bStatus){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-approveOAYes']/span/input"));
					if(!bStatus){
						Messages.errorMsg = "Cannot be clicked on Approve OA Yes Radio button";
						return false;
					}
				}
				//Check Yes button is checked or not
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-approveOAYes']/span[@class='checked']/input"),Constants.lTimeOut);
				//bStatus = Verify.verifyChecked(Global.driver, By.xpath("//div[@id='uniform-approveOAYes']/span/input"));
				if(!bStatus){
					Messages.errorMsg = "Approve OA Yes Radio button Not Selected";
					return false;
				}
			}
			/*bStatus= doCheckerVerifyOrderStatusDetails(mapSwitchDetails);
			if (!bStatus) {
				return false;
			}*/

			bStatus = doCheckerChangeOverrideStatus(mapSwitchDetails);
			if (!bStatus) {
				return false;
			}

			bStatus = checkerOperationOnTrade(mapSwitchDetails.get("CheckerOperations"));

			return bStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean checkerOperationOnTrade(String OperationType) {
		try {

			if(OperationType.equalsIgnoreCase("Approve"))
			{
				//Checker Operation for Subscription 
				bStatus = TradeTypeSubscriptionAppFunctions.doCheckerActionTypesForTranSaction(checkerActionTypes.APPROVE);
				if (!bStatus) {
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Approved Successfully but success message Not Displayed .]\n";
					return false;
				}
				return true;
			}
			if(OperationType.equalsIgnoreCase("Reject"))
			{
				bStatus = TradeTypeSubscriptionAppFunctions.doCheckerActionTypesForTranSaction(checkerActionTypes.REJECT);
				if(!bStatus){
					return false;
				}
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Rejected - Comments By QA");
				Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:saveReasonForRejection')]"));
				//bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Rejected Successfully Message Not Displayed .]\n";
					return false;
				}
				return true;
			}
			if(OperationType.equalsIgnoreCase("Return"))
			{
				bStatus = TradeTypeSubscriptionAppFunctions.doCheckerActionTypesForTranSaction(checkerActionTypes.RETURN);
				if(!bStatus){
					return false;
				}
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Returned - Comments By QA");
				Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:saveReasonForRejection')]"));
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Returned Successfully Message Not Displayed .]\n";
					return false;
				}
				return true;
			}
			if(OperationType.contains("Review"))
			{
				bStatus = TradeTypeSubscriptionAppFunctions.doCheckerActionTypesForTranSaction(checkerActionTypes.CheckerReview);
				if(!bStatus){
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Reviewed Successfully Message Not Displayed .]\n";
					return false;
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	/********************** View Buttons Functions************************/

	// function to verify view buttons for Notice Charge Details	
	public static boolean doMakerVerifyViewButtonForNoticePeriods(Map<String, String> ViewMapDetails){
		try{
			String appendErrorMsg = " ";
			boolean bValidStatus = true ;

			if(ViewMapDetails.get("NPView")== null){
				appendErrorMsg = appendErrorMsg + "[No Notice charges Defined]";
				return bValidStatus;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space(text())='Notice Charges']"));
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space(text())='Notice Charges']"));
			if(!bStatus){
				bValidStatus=false;
			}
			bStatus = Elements.click(Global.driver, By.id("viewDefaultNoticeCharges"));
			if(!bStatus){
				appendErrorMsg = appendErrorMsg + "[Cannot click on View button for notice charges]";
				bValidStatus = false;
			}
			Thread.sleep(2000);
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//h4[text()='Notice Period Charges']"), Constants.lTimeOut);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg + "[View Notice period charges pop-up is not displayed]";
				bValidStatus = false;
			}

			String sValue = Elements.getText(Global.driver, By.xpath("//div/p[@id='noticePeriodView']"));
			if(sValue.trim()!=null && !sValue.equalsIgnoreCase(ViewMapDetails.get("NPView"))){
				appendErrorMsg = appendErrorMsg + "[Atual value Notice period value is "+sValue+" which is not equal to expected value: "+ViewMapDetails.get("NPView")+"]";
				bValidStatus = false;
			}

			String sType = Elements.getText(Global.driver, By.xpath("//div/p[@id='noticePeriodTypeView']"));
			if(sType.trim()!=null && !sType.equalsIgnoreCase(ViewMapDetails.get("NPType"))){
				appendErrorMsg = appendErrorMsg + "[Atual value Notice period Type is "+sType+" which is not equal to expected value: "+ViewMapDetails.get("NPType")+"]";
				bValidStatus = false;
			}

			String sCalendar = Elements.getText(Global.driver, By.xpath("//div/p[@id='noticePeriodCalendarView']"));
			if(sCalendar.trim()!=null && !sCalendar.equalsIgnoreCase(ViewMapDetails.get("NPCalendarView"))){
				appendErrorMsg = appendErrorMsg + "[Atual value Notice period Calendar type is "+sCalendar+" which is not equal to expected value: "+ViewMapDetails.get("NPCalendarView")+"]";
				bValidStatus = false;
			}

			String ChargesType = Elements.getText(Global.driver, By.xpath("//div/p[@id='noticePeriodChargesTypeView']"));
			if(ChargesType!=null && !ChargesType.equalsIgnoreCase(ViewMapDetails.get("NPChargesType"))){
				appendErrorMsg = appendErrorMsg + "[Atual value Notice period Charges Type is "+ChargesType+" which is not equal to expected value: "+ViewMapDetails.get("NPChargesType")+"]";
				bValidStatus = false;
			}

			Messages.errorMsg = appendErrorMsg ;
			return bValidStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	// function to verify view buttons for Transaction charges Details
	public static boolean doMakerVerifyViewButtonForTransactionCharge(Map<String, String> ViewMapDetails){
		try{
			String appendErrorMsg = " ";
			boolean bValidStatus = true;


			if(ViewMapDetails.get("TCEffDate")==null){
				return bValidStatus;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Transaction Charges']"));
			if(!bStatus){
				bValidStatus= false;
			}
			bStatus = Elements.click(Global.driver, By.id("viewDefaultTransactionCharges"));
			if(!bStatus){
				appendErrorMsg = appendErrorMsg + "Cannot click on View button for Transaction charges";
				bValidStatus = false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//h4[text()='Transaction Charges']"), Constants.lTimeOut);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg + "View Transaction charges pop-up is not displayed";
				bValidStatus = false;
			}

			String sEffDate = Elements.getText(Global.driver, By.xpath("//div/p[@id='effectiveDateView']"));
			if(sEffDate.trim()!=null && !sEffDate.equalsIgnoreCase(ViewMapDetails.get("TCEffDate"))){
				appendErrorMsg = appendErrorMsg + "Actual value Effective Date value is "+sEffDate+" which is not equal to expected value: "+ViewMapDetails.get("TCEffDate");
				bValidStatus = false;
			}

			String sType = Elements.getText(Global.driver, By.xpath("//p[@id='transactionChargeTypeView']"));
			if(sType!=null && !sType.equalsIgnoreCase(ViewMapDetails.get("TCType"))){
				appendErrorMsg = appendErrorMsg + "Actual value Transaction Charge Type is "+sType+" which is not equal to expected value: "+ViewMapDetails.get("TCType");
				bValidStatus = false;
			}

			String sMethod = Elements.getText(Global.driver, By.xpath("//p[@id='transactionRateMethodView']"));
			if(sMethod!=null && !sMethod.equalsIgnoreCase(ViewMapDetails.get("TCRateMethod"))){
				appendErrorMsg = appendErrorMsg + "Actual value Rate Method type is "+sMethod+" which is not equal to expected value: "+ViewMapDetails.get("TCRateMethod");
				bValidStatus = false;
			}

			String CalculationType = Elements.getText(Global.driver, By.xpath("//p[@id='transactionCalculationBaseView']"));
			if(CalculationType!=null && !CalculationType.equalsIgnoreCase(ViewMapDetails.get("TCCalculationType"))){
				appendErrorMsg = appendErrorMsg + "Actual value Calculation Type is "+CalculationType+" which is not equal to expected value: "+ViewMapDetails.get("TCCalculationType");
				bValidStatus = false;
			}
			Messages.errorMsg = appendErrorMsg ;
			return bValidStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static String getTheInvestorNameCreatedFromSwitch(Map<String,String> mapSwitchDetails){
		try {
			String sInvestorSheetName= "GeneralDetails";
			String sInvestorName = "";	
			if (mapSwitchDetails.get("New Investor TestCaseName") != null) {
				String sInvestorFirstName = "";
				String sInvestorLastName = "";
				String sInvestorMiddleName = "";

				Map<String, String> mapInvestorGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, sInvestorSheetName , mapSwitchDetails.get("New Investor TestCaseName")); 
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
					//put the Investor Name From Investor Test data in SWITCH Map
					mapSwitchDetails.put("To Investor Name",sInvestorName);
				}
			}


			return sInvestorName;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String getTheHolderNameCreatedFromSwtitch(Map<String, String> mapSwitchDetails) {
		try {
			String sHolderSheetName = "GeneralDetails";
			String sHolderName = "";
			Map<String, String> mapHolderGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, sHolderSheetName , mapSwitchDetails.get("New Holder TestCaseName"));

			String sHolderFirstName = "";
			String sHolderLastName = "";
			String sHolderMiddleName = "";


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
				mapSwitchDetails.put("To Holder Name",sHolderName);
			}

			return sHolderName;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(String TestCaseId) {
		try {
			String accountID = "";

			//Get the Account ID From Investor Account Master XML File
			Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", TestCaseId);
			if (mapXMLAccountMasterDetails != null && !mapXMLAccountMasterDetails.isEmpty() && mapXMLAccountMasterDetails.get("AccountID") != null) {
				accountID = mapXMLAccountMasterDetails.get("AccountID");
			}
			else{
				//Get the Account ID From Subscription XML File
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", TestCaseId);
				if (mapXMLSubscriptionDetails != null && !mapXMLSubscriptionDetails.isEmpty() && mapXMLSubscriptionDetails.get("AccountID") != null) {
					accountID =  mapXMLSubscriptionDetails.get("AccountID");
				}
				else{
					Map<String ,String> mapXMLSwitchDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TradeTypeSWITCH", TestCaseId);
					if (mapXMLSwitchDetails != null && !mapXMLSwitchDetails.isEmpty() && mapXMLSwitchDetails.get("AccountID") != null) {
						accountID = mapXMLSwitchDetails.get("AccountID");
					}
					else{
						accountID = TestCaseId;
					}
				}
			}

			return accountID;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

}
