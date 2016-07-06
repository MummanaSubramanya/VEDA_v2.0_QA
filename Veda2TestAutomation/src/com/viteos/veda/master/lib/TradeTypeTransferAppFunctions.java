package com.viteos.veda.master.lib;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.File;

import jxl.biff.ContinueRecord;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Alerts;
import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.checkerActionTypes;

public class TradeTypeTransferAppFunctions {
	public static boolean bStatus;
	public static String sAccountNo = "";
	public static String newAccountID = "";
	public static boolean isTransferForCheckerReviewedScreen = false;

	//Do fill Transfer Data

	public static boolean doFillTransferMasterDetails(Map<String, String> mapTransferDetails){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Received Date Field is Not Visible";
				return false;
			}
			bStatus = doFillRequestDetails(mapTransferDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillFromInvestorDetails(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doFillToInvestorDetails(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doFillFundDetailsTransfer(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			//Store the newly created Account ID in newAccountID string to Store that in XML file
			if(mapTransferDetails.get("NewAccount")!=null && mapTransferDetails.get("NewAccount").equalsIgnoreCase("Yes")){
				Thread.sleep(2000);
				newAccountID = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_accountNameDropdown']//span[contains(@id, 'select')]"));
				if(newAccountID==null || newAccountID.equalsIgnoreCase("") ){
					Messages.errorMsg = "New Account ID is not Generated";
					return false;
				}
			}
			bStatus = doFillTransferDetails(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			if(mapTransferDetails.get("OperationNext")!=null){
				bStatus = doSubOperationsOnTransactionTrades("Transfer", mapTransferDetails.get("OperationNext"));
				if(!bStatus){
					return false;
				}
			}
			bStatus = doMakerVerifyRequestDetailsTab(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyFromInvestorDetailsTab(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doVerifyToInvestorDetails(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyFundDetailsTab(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyTransferDetailsTab(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillAvailableBalanceDetails(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAvailableBalanceAllocatedeAmountOrShare(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAvailableBalanceDetails(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			if(mapTransferDetails.get("ExpectedPendingRequestAmount")!=null && mapTransferDetails.get("ExpectedPendingRequestedShares")!=null &&mapTransferDetails.get("ExpectedNumberOfShareDecimals")!=null && !mapTransferDetails.get("ExpectedPendingRequestAmount").contains("Records")){
				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapTransferDetails.get("ExpectedPendingRequestAmount"),mapTransferDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					return false;
				}
			}

			bStatus = doVerifyChargeDetails(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAmountDetailsTab(mapTransferDetails);			
			if(!bStatus){
				return false;
			}

			bStatus = doMakerFillOtherDetailsTab(mapTransferDetails);			
			if(!bStatus){
				return false;
			}

			//Do perform Maker operation
			if(mapTransferDetails.get("OperationType")!=null){
				bStatus = doSubOperationsOnTransactionTrades("Transfer", mapTransferDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapTransferDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ERROR : Request Received Date Field is Not Visible Cancel Operation Failed ] \n";
						return false;
					}
					return true;
				}
				//Verify Exception At checker side
				if(mapTransferDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Proceed')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ERROR :Proceed Button is Not Avaliable ]\n";
						return false;
					}
					bStatus = verifyExceptionsatMaker(mapTransferDetails.get("ExpectedExceptionsAtMaker"));
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with  Expected :'"+mapTransferDetails.get("ExpectedExceptionsAtMaker")+"' ");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "[ERROR : Cannot close the Exceptions ]\n ";
							return false;
						}
						Messages.errorMsg = errorMsg;
						return false;
					}
					if(mapTransferDetails.get("OperationType").equalsIgnoreCase("Save") || mapTransferDetails.get("OperationType").contains("Review"))
					{
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Proceed')]"));
						if(!bStatus){
							Messages.errorMsg = "[ERROR : "+mapTransferDetails.get("OperationType")+" Button  Cannot be clicked ] \n";
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
							return false;
						}
					}
				}
				/*else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Proceed')]"), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions are Not matching");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "[ERROR : Exceptions are Visible Even there are No Expected Exception and Cannot close the Exceptions ]\n";
							return false;
						}
						Messages.errorMsg = "[ERROR : Exceptions are Visible Even there are No Expected Exception ]\n";
						return false;
					}
				}*/
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Proceed')]"), Constants.iPopupWaitingTime);
				if(bStatus){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Proceed')]"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR : "+mapTransferDetails.get("OperationType")+" Button  Cannot be clicked ] \n";
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
						return false;
					}
				}

					
				//Wait for Success message
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ERROR :Succesfull Message is Not visible ]\n";
					return false;
				}
			}
			return bStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// function to add Request Details in Transfer
	public static boolean doFillRequestDetails(Map<String, String> mapTransferDetails)
	{
		try {
			if (mapTransferDetails != null && !mapTransferDetails.isEmpty()) {
				if (mapTransferDetails.get("Received Date") !=null) {
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("requestDate"), mapTransferDetails.get("Received Date"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: unable to enter:"+mapTransferDetails.get("Request Date")+", Request Date field in RequestDetils of TRansfer Module]";
						return false;
					}
				}

				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

				if (mapTransferDetails.get("Received Time") != null) {
					bStatus=NewUICommonFunctions.typeCharactersIntoTextBox(By.id("requestTime"), mapTransferDetails.get("Received Time"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: unable to enter:"+mapTransferDetails.get("Request Time")+", Request Time field in RequestDetils of TRansfer Module]";
						return false;

					}	
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
				if (mapTransferDetails.get("Order Received Office") != null) {
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("orderRecievedOffice"), mapTransferDetails.get("Order Received Office"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: unable to enter:"+mapTransferDetails.get("Order Received Office")+",Order Received Office field in RequestDetils of TRansfer Module]";	
						return false;
					}
				}
				if (mapTransferDetails.get("Time Zone") != null) {
					bStatus= NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//div/input[@data-original-title='Time Zone']"), mapTransferDetails.get("Time Zone"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: unable to enter:"+mapTransferDetails.get("Time Zone")+",Time Zone in RequestDetils field of TRansfer Module]";	
						return false;
					}	
				}
				if (mapTransferDetails.get("Source") != null) {
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("source"), mapTransferDetails.get("Source"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: unable to enter:"+mapTransferDetails.get("Source")+",Source in RequestDetils field of TRansfer Module]";	
						return false;
					}	
				}
				if (mapTransferDetails.get("Mode of Request") !=null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Mode of Request", mapTransferDetails.get("Mode of Request"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: unable to select:"+mapTransferDetails.get("Mode of Request")+", Mode of Request in RequestDetils field of TRansfer Module]";
						return false;
					}
				}
				if (mapTransferDetails.get("External ID Number") != null) {
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("externalId"), mapTransferDetails.get("External ID Number"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: unable to enter:"+mapTransferDetails.get("External ID Number")+", External ID Number in RequestDetils field of TRansfer Module]";
						return false;
					}
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//function to AddFromInvestroDetails
	public static boolean doFillFromInvestorDetails(Map<String, String> mapTransferDetails)
	{
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

			if (mapTransferDetails.get("From Investor Name") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("From Investor Name"),By.xpath("//div[contains(@id, 's2id_fromInvestorId')]//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ERROR: unable to Select:"+mapTransferDetails.get("From Investor Name")+", Investorn Name field in From Investor Details of TRansfer Module]";
					return false;
				}
			}
			if (mapTransferDetails.get("From Holder Name") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("From Holder Name"), By.xpath("//div[contains(@id, 's2id_fromHolderId')]//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ERROR: unable to Select:"+mapTransferDetails.get("From Holder Name")+", Holder Name field in From Investor Details of TRansfer Module]";
					return false;
				}
			}

			if (mapTransferDetails.get("From Account Id") != null) {
				String accountID = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapTransferDetails.get("From Account Id"));
				if(accountID != null){
					mapTransferDetails.put("From Account Id", accountID);
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("From Account Id"), By.xpath("//div[contains(@id, 's2id_fromAccountId')]//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ERROR: unable to Select:"+mapTransferDetails.get("From Account Id")+", Account Id in From Investor Details of TRansfer Module]";
					return false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//function to add To Investor Details
	public static boolean doFillToInvestorDetails(Map<String, String> mapTransferDetails)
	{
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

			//Create New Investor

			if(mapTransferDetails.get("New Investor")!=null && mapTransferDetails.get("New Investor").equalsIgnoreCase("Yes"))
			{
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
				bStatus = Elements.click(Global.driver, By.xpath("//span/a[contains(@onclick, 'investorAdd')]"));
				if(!bStatus)
				{
					Messages.errorMsg = "[ ERROR : New Investor Link cannot be clicked ]";
					return false;
				}
				if(mapTransferDetails.get("New Investor TestCaseName")!=null)
				{
					InvestorMasterAppFunctions.bTadingInvestorFlag = true;
					bStatus = TradeTypeSubscriptionAppFunctions.createNewInvestorFromTradeTypeSubscription(mapTransferDetails.get("New Investor TestCaseName"));
					if(!bStatus)
					{
						Messages.errorMsg = "[ERROR: unable to Create New Investor:"+mapTransferDetails.get("New Investor TestCaseName")+", Investor Name in To Investor Details of TRansfer Module]";
						return false;
					}
				}
			}
			//Select Investor from drop down
			if(mapTransferDetails.get("To Investor Name")!=null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("To Investor Name"), By.xpath("//div[contains(@id, 's2id_investorNameDropdown')]//span[contains(@id,'select2-chosen')]"));
				if(!bStatus)
				{
					Messages.errorMsg = "[ERROR: unable to Select:"+mapTransferDetails.get("To Investor Name")+", Investor Name in To Investor Details of TRansfer Module]";
					return false;
				}
			}
			//Create New Holder
			if(mapTransferDetails.get("New Holder")!=null && mapTransferDetails.get("New Holder").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='newHolderLink']"));
				if(!bStatus){
					Messages.errorMsg = "New Holder Link Cannot be clicked"; 
					return false;
				}
				if(mapTransferDetails.get("New Holder TestCaseName")!=null){
					HolderMasterAppFunctions.bTradingSubscription = true;
					bStatus = TradeTypeSubscriptionAppFunctions.createNewHolderFromTradeTypeSubscription(mapTransferDetails.get("New Holder TestCaseName"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR: unable to Create New Holder:"+mapTransferDetails.get("New Holder TestCaseName")+", Holder in To Holder Details of Transfer Module]";
						return false;
					}
				}
			}
			//Select Holder From drop down
			if(mapTransferDetails.get("To Holder Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("To Holder Name"), By.xpath("//div[contains(@id, 's2id_holderNameDropdown')]//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ERROR: unable to Select:"+mapTransferDetails.get("To Holder Name")+", Holder Name in To Investor Details of TRansfer Module]";
					return false;
				}
			}

			// New Account Creation 

			if(mapTransferDetails.get("NewAccount")!=null && mapTransferDetails.get("NewAccount").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='newAccountLink']"));
				if(!bStatus){
					Messages.errorMsg = "New Account Link Cannont be clicked";
					return false;
				}

				//Storing the Account ID in newAccountID string after filling the Fund Details because of the sync issue
			}
			//Select Account From Drop down.

			if(mapTransferDetails.get("To Account ID")!=null){
				String accountID = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapTransferDetails.get("To Account ID"));
				if(accountID != null){
					mapTransferDetails.put("To Account ID",accountID);
				}

				bStatus =NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("To Account ID"), By.xpath("//div[@id='s2id_accountNameDropdown']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					return false;
				}						
			}
			Thread.sleep(2000);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;

	}

	//function to Verify The To Investor Details

	public static boolean doVerifyToInvestorDetails(Map<String, String> mapTransferDetails)
	{
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus=doClickOnExpandOrCollapseButtonsOnTransferSecondTab("Yes", "3");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Tab is not clicked for ToInvestor Details ]" ;
			}

			if (mapTransferDetails.get("New Investor") != null && mapTransferDetails.get("New Investor").equalsIgnoreCase("yes")) {					
				String investorName = getInvestorNameFromTheInvestorTestData(mapTransferDetails);
				if(investorName != null){
					mapTransferDetails.put("To Investor Name",investorName);
				}
			}
			if (mapTransferDetails.get("To Investor Name") !=null) {
				String ActualToInvestorName = Elements.getText(Global.driver, By.xpath("//div[contains(@id, 's2id_investorNameDropdown')]//span[contains(@id,'select2-chosen')]"));
				String ExpectedToInvestorName = mapTransferDetails.get("To Investor Name");
				if (ActualToInvestorName == null || ActualToInvestorName.equalsIgnoreCase("") || !ActualToInvestorName.trim().equalsIgnoreCase(ExpectedToInvestorName)) {						
					sAppendErrorMsg = sAppendErrorMsg +"[ERROR: Expected To Investor Name"+mapTransferDetails.get("To Investor Name")+", is Not Matching with Actual To Invester Name.";
					bValidateStatus = false;
				}					
			}

			//creation of New Holder
			if (mapTransferDetails.get("New Holder") != null && mapTransferDetails.get("New Holder").equalsIgnoreCase("yes")) {					
				String holderName = getHolderNameFromHolderTestData(mapTransferDetails);
				if(holderName != null){
					mapTransferDetails.put("To Holder Name",holderName);
				}
			}
			if (mapTransferDetails.get("To Holder Name") != null) {
				String ActualToHolderName = Elements.getText(Global.driver, By.xpath("//div[contains(@id, 's2id_holderNameDropdown')]//span[contains(@id,'select2-chosen')]"));
				String ExpectedHolderName = mapTransferDetails.get("To Holder Name");
				if (ActualToHolderName == null || ActualToHolderName.equalsIgnoreCase("") || !ActualToHolderName.equalsIgnoreCase(ExpectedHolderName)) {
					sAppendErrorMsg = sAppendErrorMsg + "[ERROR: Expected To Holder Name : '"+mapTransferDetails.get("To Holder Name")+"', is Not Matching with Actual : '"+ActualToHolderName+"']\n";
					bValidateStatus = false;
				}
			}

			if (mapTransferDetails.get("To Account ID") != null) {
				String accountID = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapTransferDetails.get("To Account ID"));
				if(accountID != null){
					mapTransferDetails.put("To Account ID", accountID);
				}
				String ActualToAccountID = Elements.getText(Global.driver, By.xpath("//div[contains(@id, 's2id_accountNameDropdown')]//span[contains(@id,'select2-chosen')]"));
				String ExpectedToAccountID = mapTransferDetails.get("To Account ID");
				if (ActualToAccountID == null || ActualToAccountID.equalsIgnoreCase("") || !ActualToAccountID.equalsIgnoreCase(ExpectedToAccountID)) {
					sAppendErrorMsg = sAppendErrorMsg + "[ERROR: Expected To Account ID"+mapTransferDetails.get("To Account ID")+", is Not Matching with Actual To Account ID.";
					bValidateStatus = false;
				}
			}
			if (mapTransferDetails.get("NewAccount") != null && mapTransferDetails.get("NewAccount").equalsIgnoreCase("Yes")) {
				String ActualToAccountID = Elements.getText(Global.driver, By.xpath("//div[contains(@id, 's2id_accountNameDropdown')]//span[contains(@id,'select2-chosen')]"));
				if(!ActualToAccountID.equalsIgnoreCase(newAccountID)){
					Messages.errorMsg = "Created Account ID is Not Matched during verification";
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

	//function to verify ChargesDetails

	public static boolean doVerifyChargeDetails(Map<String, String> mapTransferDetails)
	{
		boolean validateStatus = true;
		String appendMsg="";

		try {
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsYes']/span"));
			String ActualNoticeCharges = "";
			String ExpectedNoticeCharge = "";

			//Notice period charges  Yes
			if(mapTransferDetails.get("NoticeChargesRadioButton")!=null && mapTransferDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){

				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-noticeChargesDetailsYes']/span"));
				if(!bStatus){
					appendMsg =  appendMsg +"[ ERROR : Notice Period Charges 'Yes'  Radio Button Not Clicked ]\n";
					return false;
				}

				// Verify Notice Charges

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-noticeChargesDetailsYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+ "[ERROR :  Notice Charge Default Radio button Yes is not selected  ]\n";
				}

				if(mapTransferDetails.get("ExpectedNoticeCharges")!=null){
					ActualNoticeCharges = Elements.getElementAttribute(Global.driver, By.id("noticeChargesAmount"), "value");
					ExpectedNoticeCharge = mapTransferDetails.get("ExpectedNoticeCharges");

					if (ActualNoticeCharges != null && !ActualNoticeCharges.equalsIgnoreCase("")) {
						ActualNoticeCharges = ActualNoticeCharges.replaceAll(",", "");

						bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Notice Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualNoticeCharges);
						if(!bStatus){
							validateStatus = false;
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
						}
						if (Float.parseFloat(ActualNoticeCharges) != Float.parseFloat(ExpectedNoticeCharge)) {
							appendMsg = appendMsg+"[ERROR : Actual Notice Charges : "+ActualNoticeCharges+" for Type Cash is not matching with Expected : "+mapTransferDetails.get("ExpectedNoticeCharges")+" .] \n";
							validateStatus = false;
						}
					}
				}
			}

			//Notice period charges  No
			if(mapTransferDetails.get("NoticeChargesRadioButton")!=null && mapTransferDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){

				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-noticeChargesDetailsNo']/span"));
				if(!bStatus){
					appendMsg =  appendMsg +"[ ERROR : Notice Period Charges No Radio Button Not Clicked ]\n";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), Constants.lTimeOut);
				if(!bStatus){
					appendMsg =  appendMsg +"[ ERROR :Notice Period Charges New Charges field is not visible ]\n";
					return false;
				}
				//New Notice Period Charges 
				if(mapTransferDetails.get("NewAmountForNoticePeriod")!=null && mapTransferDetails.get("ExpectedNewNoticeCharges")!=null){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), mapTransferDetails.get("NewAmountForNoticePeriod"));
					if(!bStatus){
						appendMsg =  appendMsg +"[ ERROR : New Notice period Charges not Entered ]\n";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@class, 'showNewNoticeCharges')]"));
					if(!bStatus){
						appendMsg =  appendMsg +"[ ERROR : New Notice Period Charges Save Button not clicked ] \n";
						return false;
					}
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
					Thread.sleep(1000);

					String ActualNoticeChargeAmount = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='noticeChargesAmount']"), "value");
					String ExpectedNoticeChargeAmount = mapTransferDetails.get("ExpectedNewNoticeCharges");

					if (ActualNoticeChargeAmount != null && !ActualNoticeChargeAmount.equalsIgnoreCase("")) {
						ActualNoticeChargeAmount = ActualNoticeChargeAmount.replaceAll(",", "");

						bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Notice Charge Amount ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualNoticeChargeAmount);
						if(!bStatus){
							validateStatus = false;
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
						}
						if (Float.parseFloat(ActualNoticeChargeAmount) != Float.parseFloat(ExpectedNoticeChargeAmount)) {
							appendMsg = appendMsg+"[ERROR : Actual Notice Charges Amount : "+ActualNoticeChargeAmount+" is not matching with Expected  : "+mapTransferDetails.get("ExpectedNewNoticeCharges")+" .] \n";
							validateStatus = false;
						}
					}
				}
			}
			//Verify TransactionCharges with option Yes

			if(mapTransferDetails.get("TransactionChargesRadioButton")!=null && mapTransferDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsYes']/span"));

				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsYes']/span"));
				if(!bStatus){
					appendMsg =  appendMsg +"[ERROR : Fixed Transaction Charges Yes Radio Button is Not Clicked ]\n";
					return false;
				}

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+ "[ERROR : Transaction Charge Default Radio button Yes is not selected  ]\n";
				}

				if (mapTransferDetails.get("ExpectedTransactionCharges") != null) {
					// Verify Transaction Charges
					String ActualTransactionCharges = Elements.getElementAttribute(Global.driver, By.id("fixedTransactionChargesAmount"), "value");
					String ExpectedTransactionCharges = mapTransferDetails.get("ExpectedTransactionCharges");

					if (ActualTransactionCharges != null && !ActualTransactionCharges.equalsIgnoreCase("")) {
						ActualTransactionCharges = ActualTransactionCharges.replaceAll(",", "");

						bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Transaction Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualTransactionCharges);
						if(!bStatus){
							validateStatus = false;
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
						}
						if (Float.parseFloat(ActualTransactionCharges) != Float.parseFloat(ExpectedTransactionCharges)) {
							appendMsg = appendMsg+"[ERROR :  Actual Transaction Charges : "+ActualTransactionCharges+" is not matching with Expected  : "+mapTransferDetails.get("ExpectedTransactionCharges")+" .] \n";
							validateStatus = false;
						}
					}
				}
			}

			//Verify TransactionCharges with option No
			if(mapTransferDetails.get("TransactionChargesRadioButton")!=null && mapTransferDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){

				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsNo']/span"));
				if(!bStatus){
					appendMsg =  appendMsg +"[ERROR : Fixed Transaction Charges No Radio Button Not Clicked ]\n";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), Constants.lTimeOut);
				if(!bStatus){
					appendMsg =  appendMsg +"[ERROR : Tranasaction Charges New Charges field not visible]\n";
					return false;
				}
				if(mapTransferDetails.get("NewTransactionCharges") !=null && mapTransferDetails.get("NewExpectedTransactionCharges") !=null){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), mapTransferDetails.get("NewTransactionCharges"));
					if(!bStatus){
						appendMsg =  appendMsg +"[ERROR : New Transaction Charges are not Entered]\n";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@class, 'showNewTransactionCharges')]"));
					if(!bStatus){
						appendMsg =  appendMsg +"[ERROR :New Transaction Charges Save butotn not clicked ]\n";
						return false;
					}
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
					Thread.sleep(1000);

					String ActualTransactionCharges = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='fixedTransactionChargesAmount']"), "value");
					String ExpectedTransactionCharges = mapTransferDetails.get("NewExpectedTransactionCharges");
					if (ActualTransactionCharges != null && !ActualTransactionCharges.equalsIgnoreCase("")) {
						ActualTransactionCharges = ActualTransactionCharges.replaceAll(",", "");

						bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("NewExpectedTransactionCharges", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualNoticeCharges);
						if(!bStatus){
							validateStatus = false;
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
						}
						if (Float.parseFloat(ActualTransactionCharges) != Float.parseFloat(ExpectedTransactionCharges)) {
							appendMsg = appendMsg+"[ERROR : Actual Transaction Charges : "+ActualTransactionCharges+" for Type Cash is not matching with Expected : "+mapTransferDetails.get("NewExpectedTransactionCharges")+" .] \n";
							validateStatus = false;
						}
					}					
				}
			}


			// Enter Adhoc Charges
			if (mapTransferDetails.get("Adhoc Charges") != null && !mapTransferDetails.get("Adhoc Charges").equalsIgnoreCase("None")) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='adhocCharges']"), mapTransferDetails.get("Adhoc Charges"));
				if (!bStatus) {
					appendMsg = appendMsg + "[Unable to Enter Adhoc Charges in Charge Details" + mapTransferDetails.get("Adhoc Charges")+"]\n";
					return validateStatus = false ;
				}
			}

			// Verify Total Charges 
			if(mapTransferDetails.get("ExpectedTotalCharges")!=null){
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
				String ActualTotal =  Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='totalCharges']"), "value");
				String ExpectedTotal = mapTransferDetails.get("ExpectedTotalCharges");

				if (ActualTotal != null && !ActualTotal.equalsIgnoreCase("")) {
					ActualTotal = ActualTotal.replaceAll(",", "");

					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Actual Total Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualTotal);
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					}
					if (Float.parseFloat(ActualTotal) != Float.parseFloat(ExpectedTotal)) {
						appendMsg = appendMsg+"[ERROR : Actual Total Charges : "+ActualTotal+" for Type Cash is not matching with Expected  : "+mapTransferDetails.get("ExpectedTotalCharges")+" .] \n";
						validateStatus = false;
					}
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
	public static boolean doFillFundDetailsTransfer(Map<String, String> mapTransferDetails) {
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			//select Client Name
			if(mapTransferDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Client Name"), By.xpath("//label[normalize-space()='Client Name']/following::div[@id='s2id_cmbClientName']"));				
				if(!bStatus){
					return false;
				}
			}

			//Select Fund Family
			if(mapTransferDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Fund Family Name"), By.xpath("//label[normalize-space()='Fund Family Name']/following::div[@id='s2id_cmbFundFamilyName']"));
				if(!bStatus){
					return false;
				}
			}
			//Select Legal Entity
			if(mapTransferDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Legal Entity Name"), By.xpath("//label[normalize-space()='Legal Entity']/following::div[@id='s2id_cmbFundName']"));
				if(!bStatus){
					return false;
				}
			}

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));


			//Select Class Name
			if(mapTransferDetails.get("Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Class Name"), By.xpath("//label[normalize-space()='Class']/following::div[@id='s2id_cmbClassName']"));
				if(!bStatus){
					return false;
				}
			}

			// Select the Sereies Name
			if(mapTransferDetails.get("Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Series Name"), By.xpath("//label[normalize-space()='Series']/following::div[@id='s2id_cmbSeriesName']"));
				if(!bStatus){
					return false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doFillTransferDetails(Map<String, String> mapTransferDetails){
		try{

			//Select Full Transfer  Radio button

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

			NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-TORA_radioButton']"));
			if(mapTransferDetails.get("Full Transfer")!=null){
				if(mapTransferDetails.get("Full Transfer").equalsIgnoreCase("Yes")){

					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-fullTransferYesId']"));
					if(!bStatus){
						Messages.errorMsg = "Full Transfer Radio button Yes is Not clicked";
						return false;
					}
					String percentageValue = "100";
					String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='transferPercentage']");
					if(!actualValue.equalsIgnoreCase(percentageValue)){
						Messages.errorMsg = "Percentage Value is not Changed to 100 when Full redemption Yes button selected";
						return false;
					}
				}
				if(mapTransferDetails.get("Full Transfer").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-fullTransferNoId']"));
					if(!bStatus){
						Messages.errorMsg = "Full Transfer Radio button No is Not clicked";
						return false;
					}
				}
			}

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

			//Select Amount/Share Radio button
			if(isTransferForCheckerReviewedScreen){
				if(mapTransferDetails.get("AmountorShares")!=null && mapTransferDetails.get("Percentage")==null){
					if(mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-TORA_radioButton']"));
						if(!bStatus){
							Messages.errorMsg = "Amount Radio button cannot be selected";
							return false;
						}
						if(mapTransferDetails.get("Cash")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapTransferDetails.get("Cash"));
							if(!bStatus){
								Messages.errorMsg = "Cash is  Not Entered";
								return false;
							}
						}
					}
					if(mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-TORS_radioButton']"));
						if(!bStatus){
							Messages.errorMsg = "Shares Radio button cannot be selected";
							return false;
						}
						if(mapTransferDetails.get("Share")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='shares']"), mapTransferDetails.get("Share"));
							if(!bStatus){
								Messages.errorMsg = "Share Value  Not Entered";
								return false;
							}
						}
					}
				}
			}else{
				if(mapTransferDetails.get("AmountorShares")!=null && mapTransferDetails.get("Percentage")==null && mapTransferDetails.get("Full Transfer") != null&& !mapTransferDetails.get("Full Transfer").equalsIgnoreCase("Yes")){
					if(mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-TORA_radioButton']"));
						if(!bStatus){
							Messages.errorMsg = "Amount Radio button cannot be selected";
							return false;
						}
						if(mapTransferDetails.get("Cash")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapTransferDetails.get("Cash"));
							if(!bStatus){
								Messages.errorMsg = "Cash is  Not Entered";
								return false;
							}
						}
					}
					if(mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-TORS_radioButton']"));
						if(!bStatus){
							Messages.errorMsg = "Shares Radio button cannot be selected";
							return false;
						}
						if(mapTransferDetails.get("Share")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='shares']"), mapTransferDetails.get("Share"));
							if(!bStatus){
								Messages.errorMsg = "Share Value  Not Entered";
								return false;
							}
						}
					}
				}
			}

			//Enter Effective Date
			if( mapTransferDetails.get("Effective Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.id("effectiveDate"), mapTransferDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "Effective Date is Not Entered";
					return false;
				}
			}
			//Enter Percentage
			if(mapTransferDetails.get("Percentage")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='transferPercentage']"), mapTransferDetails.get("Percentage"));
				if(!bStatus){
					Messages.errorMsg = "Percentage Cannot be entered";
					return false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			return true;
		}

		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	// function to Click on Expand or Collapse Button		
	public static boolean doClickOnExpandOrCollapseButtonsOnTransferSecondTab(String sYesForExpandNoCollapse, String sCollapsableTabIndexNumber){
		String sAppendErrorMsg = "";
		try {			
			boolean bExpandStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='expand']"), 3);
			boolean bCollpaseStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='collapse']"), 3);
			if (bExpandStatus == true && bCollpaseStatus == false && sYesForExpandNoCollapse.equalsIgnoreCase("Yes")) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='expand']"));
				if (!bStatus) {	
					sAppendErrorMsg = "[ ERROR : Failed to Perform Expnad Operation on Tab with Index : '"+sCollapsableTabIndexNumber+"'.]\n";
					return false;
				}
				Thread.sleep(2000);
				return true;
			}
			if (bExpandStatus == false && bCollpaseStatus == true && sYesForExpandNoCollapse.equalsIgnoreCase("No")) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='collapse']"));
				if (!bStatus) {
					sAppendErrorMsg = "[ ERROR : Failed to Perform Collpase Operation on Tab with Index : '"+sCollapsableTabIndexNumber+"'.]\n";
					return false;
				}
				Thread.sleep(2000);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messages.errorMsg = sAppendErrorMsg;
			return false;
		}
		return true;
	}	

	public static boolean doMakerVerifyRequestDetailsTab(Map<String, String> mapTransferDetails){
		try {
			boolean bValidatreStatus = true;
			String sAppendErrorMsg = "";
			bStatus = doClickOnExpandOrCollapseButtonsOnTransferSecondTab("Yes", "1");
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg+ "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Request Details' Tab.]\n";
				bValidatreStatus = false;
			}

			//Verify Request Date
			if (mapTransferDetails.get("Received Date") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.id("requestDate"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Received Date"))) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Actual Request Recieved Date : "+sValue+", is not matching with expected : "+mapTransferDetails.get("Request Received Date")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Received Time
			if (mapTransferDetails.get("Received Time") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='requestTime']"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Received Time"))) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Actual Request Received Time : "+sValue+", is not matching with expected : "+mapTransferDetails.get("Request Received Time")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Received office
			if (mapTransferDetails.get("Order Received Office") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='orderRecievedOffice']"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Order Received Office"))) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Actual Order Received Office : "+sValue+", is not matching with expected : "+mapTransferDetails.get("Order Received Office")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Time Zone
			if (mapTransferDetails.get("Time Zone") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@placeholder='Time Zone']"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Time Zone"))) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Actual Time Zone : "+sValue+", is not matching with expected : "+mapTransferDetails.get("Time Zone")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Source
			if (mapTransferDetails.get("Mode of Request") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Mode of Request", By.xpath("//div[contains(@id,'modeOfRequest')]//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Mode of Request"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Mode of Request, is not matching with expected : "+mapTransferDetails.get("Mode of Request")+"] \n";
					bValidatreStatus = false;
				}
			}
			//Verify External Id
			if (mapTransferDetails.get("External ID Number") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("External ID", By.xpath("//input[contains(@id,'externalId')]"), mapTransferDetails.get("External ID Number"), "No", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : External ID Number, is not matching with expected : "+mapTransferDetails.get("External ID Number")+"] \n";
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

	public static boolean doMakerVerifyFromInvestorDetailsTab(Map<String, String> mapTransferDetails){
		try {
			boolean bValidatreStatus = true;
			String sAppendErrorMsg = "";
			bStatus = doClickOnExpandOrCollapseButtonsOnTransferSecondTab("Yes", "2");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Investor Details' Tab.]\n";
				bValidatreStatus = false;
			}
			if (mapTransferDetails.get("From Investor Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Investor Name", By.xpath("//div[@id='s2id_fromInvestorId']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("From Investor Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : From Investor Name, is not matching with expected : "+mapTransferDetails.get("From Investor Name")+"] \n";
					bValidatreStatus = false;
				}
			}
			if (mapTransferDetails.get("From Holder Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Holder Name", By.xpath("//div[@id='s2id_fromHolderId']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("From Holder Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : From Holder Name, is not matching with expected : "+mapTransferDetails.get("From Holder Name")+"] \n";
					bValidatreStatus = false;
				}
			}
			if (mapTransferDetails.get("From Account Id") != null) {	
				String accountID = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapTransferDetails.get("From Account Id"));
				if(accountID != null){
					mapTransferDetails.put("From Account Id", accountID);
				}
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Account ID", By.xpath("//div[@id='s2id_fromAccountId']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("From Account Id"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : From Account Id, is not matching with expected : "+mapTransferDetails.get("From Account Id")+"] \n";
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

	public static boolean doMakerVerifyFundDetailsTab(Map<String, String> mapTransferDetails){
		try {
			boolean bValidatreStatus = true;
			String sAppendErrorMsg = "";



			bStatus = doClickOnExpandOrCollapseButtonsOnTransferSecondTab("Yes", "4");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Fund Details' Tab.]\n";
				return false;
			}
			bStatus= Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"), Constants.lTimeOut);
			//Verify Client Name
			if (mapTransferDetails.get("Client Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Client Name", By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Client Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Client Name, is not matching with expected : "+mapTransferDetails.get("Client Name")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Fund Family Name
			if (mapTransferDetails.get("Fund Family Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Fund Family Name", By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Fund Family Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Fund Family Name, is not matching with expected : "+mapTransferDetails.get("Fund Family Name")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Legal Entity Name
			if (mapTransferDetails.get("Legal Entity Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Legal Entity Name", By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Legal Entity Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Legal Entity Name, is not matching with expected : "+mapTransferDetails.get("Legal Entity Name")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Class Name
			if (mapTransferDetails.get("Class Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Class Name", By.xpath("//div[@id='s2id_cmbClassName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Class Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Class Name, is not matching with expected : "+mapTransferDetails.get("Class Name")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Series Name
			if (mapTransferDetails.get("Series Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Series Name", By.xpath("//div[@id='s2id_cmbSeriesName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Series Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Series Name, is not matching with expected : "+mapTransferDetails.get("Series Name")+"] \n";
					bValidatreStatus = false;
				}
			}

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

			Messages.errorMsg = sAppendErrorMsg;
			return bValidatreStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	public static boolean doMakerVerifyTransferDetailsTab(Map<String, String> mapTransferDetails){
		try {
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-fullTransferYesId']/span"));
			boolean bValidatreStatus = true;
			String sAppendErrorMsg = "";
			//Verify Full Transfer Radio button
			if (mapTransferDetails.get("Full Transfer") != null) {
				if (mapTransferDetails.get("Full Transfer").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-fullTransferYesId']//span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Full Transfer  Radio button wasn't marked to 'Yes'.]\n";
						bValidatreStatus = false;
					}
				}
				if (mapTransferDetails.get("Full Transfer").equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-fullTransferNoId']//span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Full Transfer Radio button  wasn't marked to 'No'.]\n";
						bValidatreStatus = false;
					}
				}
			}


			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

			//Verify Amount or shares

			if (mapTransferDetails.get("AmountorShares") != null) {
				if (mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Amount")) {

					//Verify the Amount
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-TORA_radioButton']//span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : AmountorShares Radio button  wasn't marked to 'Amount'.]\n";
						bValidatreStatus = false;
					}
					if (mapTransferDetails.get("Cash") != null) {

						if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Cash", By.xpath("//input[@id='amount']"), mapTransferDetails.get("Cash"), "No", true,Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")))) {
							sAppendErrorMsg = sAppendErrorMsg+ "[ERROR :  Cash, is not matching with expected : "+mapTransferDetails.get("Cash")+"] \n";
							bValidatreStatus = false;
						}
					}

				}
				if (mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Shares")) {

					//Verify the Shares
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-TORS_radioButton']//span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : AmountorShares Radio button   wasn't marked to 'Shares'.]\n";
						bValidatreStatus = false;
					}
					if (mapTransferDetails.get("Share") != null) {				
						if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share", By.xpath("//input[@id='shares']"), mapTransferDetails.get("Share"), "No", true,Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals")))) {
							sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Share, is not matching with expected : "+mapTransferDetails.get("Share")+"] \n";
							bValidatreStatus = false;
						}
					}
				}
			}


			//Verify Effective Date
			if (mapTransferDetails.get("Effective Date") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Effective Date", By.xpath("//input[@id='effectiveDate']"), mapTransferDetails.get("Effective Date"), "No", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Effective Date, is not matching with expected : "+mapTransferDetails.get("Effective Date")+"] \n";
					bValidatreStatus = false;
				}
			}

			//Verify Percentage 
			if (mapTransferDetails.get("Percentage") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Percentage", By.xpath("//input[@id='transferPercentage']"), mapTransferDetails.get("Percentage"), "No", true, -1)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Percentage, is not matching with expected : "+mapTransferDetails.get("Percentage")+"] \n";
					bValidatreStatus = false;
				}
			}

			if (mapTransferDetails.get("Expected NAV Date") != null) {	
				String actualDate =Elements.getText(Global.driver, By.xpath("//input[@id='navDate']/../label"));
				String date = TradeTypeSubscriptionAppFunctions.formatDate(actualDate);

				bStatus = Verify.verifyElementPresent(Global.driver, By.xpath("//input[@id='navDate' and @value='"+date+"']/preceding-sibling::label"));
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : NAV Date is not Matched with Expected Nav Date" +mapTransferDetails.get("Expected NAV Date")+" ] \n";
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

	public static boolean doMakerVerifyAmountDetailsTab(Map<String, String> mapTransferDetails){
		try {

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));

			boolean bValidatreStatus = true;
			String sAppendErrorMsg = "";
			if (mapTransferDetails.get("Gross Amount") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Gross Amount", By.xpath("//input[@id='grossAmount']"), mapTransferDetails.get("Gross Amount"), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")))) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Gross Amount is not Matched with Expected  "  +mapTransferDetails.get("Gross Amount")+" ] \n";
					bValidatreStatus = false;
				}
			}
			if (mapTransferDetails.get("Charges") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Charges", By.xpath("//input[@id='amountCharges']"), mapTransferDetails.get("Charges"), "No", true,Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")))){
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Charges is not Matched with Expected "  +mapTransferDetails.get("Charges")+" ] \n";
					bValidatreStatus = false;
				}
			}
			if (mapTransferDetails.get("Transfer Amount") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Amount", By.xpath("//input[@id='finalAmount']"), mapTransferDetails.get("Transfer Amount"), "No", true,Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")))){
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Transfer Amount is not Matched with Expected " +mapTransferDetails.get("Transfer Amount")+" ] \n";
					bValidatreStatus = false;
				}
			}
			if (mapTransferDetails.get("Net Units") != null) {
				if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Net Units", By.xpath("//input[@id='finalUnits']"), mapTransferDetails.get("Net Units"), "No", true,Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals")))) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR : Net Units is not Matched with Expected  "  +mapTransferDetails.get("Net Units")+" ] \n";
					bValidatreStatus = false;
				}
			}
			Thread.sleep(2000);
			Messages.errorMsg = sAppendErrorMsg;
			return bValidatreStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doMakerFillOtherDetailsTab(Map<String,String>mapTransferDetails){
		try{
			if(mapTransferDetails.get("Crystalize Fee")!=null){

				if(mapTransferDetails.get("Crystalize Fee").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-crystalizeFeeYes']"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR : Crystalize Fee Radio button 'Yes' is not clicked ]\n";
						return false;
					}
				}
				if(mapTransferDetails.get("Crystalize Fee").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-crystalizeFeeNo']"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR : Crystalize Fee Radio button 'No' is not clicked ]\n";
						return false;
					}
				}
			}

			if(mapTransferDetails.get("Cumulative Return")!=null){

				if(mapTransferDetails.get("Cumulative Return").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver , By.xpath("//div[@id='uniform-cumulativeReturnYes']"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR :Cumulative Return Radio button 'Yes' is not clicked ]\n";
						return false;
					}	
				}
				if(mapTransferDetails.get("Cumulative Return").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver , By.xpath("//div[@id='uniform-cumulativeReturnNo']"));
					if(!bStatus){
						Messages.errorMsg = "[ERROR : Cumulative Return Radio button 'NO' is not clicked ]\n";
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

	//Perform operation on Maker
	public static boolean doSubOperationsOnTransactionTrades(String master , String operation){
		try {
			/*
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//em[@class='fa fa-save']") , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Save Button is Not Visible";
				return false;
			}*/
			if(operation.equalsIgnoreCase("Save")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick, 'save') and contains(normalize-space(),'Save')]"));
				if(!bStatus){
					bStatus=NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'saveTransactionForMaker')]"));
					if(!bStatus){
						Messages.errorMsg = master+" Save button cannot be clicked";
						return false;
					}
				}
			}

			if(operation.contains("Review")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//em[@class='icon icon-share-alt']//parent::button[contains(normalize-space(),'Send for Review')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Send For Review button cannot be clicked";
					return false;
				}
			}

			if(operation.equalsIgnoreCase("Cancel")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-times-circle']//parent::a[contains(normalize-space(),'Cancel')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Cancel button cannot be clicked";
					return false;
				}
			}

			if(operation.equalsIgnoreCase("Proceed")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'proceedToTaxSlot')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Proceed button cannot be clicked";
					return false;
				}
			}

			if(operation.equalsIgnoreCase("Clear")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-times-circle']//parent::a[contains(normalize-space(),'Clear')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Clear button cannot be clicked";
					return false;
				}
			}
			if (operation.equalsIgnoreCase("Apply")) {
				NewUICommonFunctions.scrollToView(By.xpath("//button[contains(@onclick,'proceedToTaxSlot')]"));
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'proceedToTaxSlot')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click on 'Apply' button to get the tax lots for the selection criteria of TRADE : '"+master+"'.]\n";
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

	//Verify Exception At Maker
	public static boolean verifyExceptionsatMaker(String exceptionList){
		try {
			String sAppendErrMsg = "";
			boolean bValidStatus = true;
			String sClubbegExeptionsString = "";
			List<String> sActualExceptions = new ArrayList<String>();
			List<String> aException = Arrays.asList(exceptionList.split(":"));
			int actualExceptionCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@class='alert alert-danger' and contains(@style,'block')]"));
			if(aException.size()!=actualExceptionCount){
				Messages.errorMsg = "Actual Exception count is "+actualExceptionCount+"  not Matching with the Expected Exceptions count "+aException.size()+" Expected Exceptions are  :"+exceptionList;
				return false;
				//			return bValidStatus;
			}
			for(int j =0;j<actualExceptionCount;j++){

				String Actualexception = Elements.getText(Global.driver, By.xpath("//div[@class='alert alert-danger' and contains(@style,'block')]["+(j+1)+"]/strong")).trim();
				sClubbegExeptionsString = sClubbegExeptionsString + " [ "+Actualexception+" ]";				
			}
			for(int i=0;i<aException.size();i++){				
				if(!sClubbegExeptionsString.contains(aException.get(i))){

					sAppendErrMsg = sAppendErrMsg+"Actual Exception is :"+sClubbegExeptionsString+" ,which is not matching with Expected Exceptions :"+exceptionList;
					bValidStatus = false;										
				}
			}
			Messages.errorMsg = sAppendErrMsg;
			return bValidStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doFillAvailableBalanceDetails(Map<String, String> mapRedemptionDetails){
		int noOfAmountOrShareLots = 0;
		List<String> sAmountOrSharesList = null;
		List<String> sAmountOrSharesIndexList = null;
		List<String> sAmountOrSharesIndexesEditableStatusList = null;
		try {
			String sIsAmountOrShare = "";
			/*if (mapRedemptionDetails.get("Redemption Method Applied") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapRedemptionDetails.get("Redemption Method Applied"), By.xpath("//div[@id='s2id_redmethodApplied']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to select the 'Redemption Method Applied' option : "+mapRedemptionDetails.get("Redemption Method Applied")+" ]\n";
					return false;
				}
			}*/
			if (mapRedemptionDetails.get("TaxLotsAmounts") != null || mapRedemptionDetails.get("TaxLotsShares") != null) {
				if (mapRedemptionDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsAmounts").split(","));					
				}
				else if (mapRedemptionDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsShares").split(","));					
				}
			}
			if (mapRedemptionDetails.get("TaxLotsIndexes") != null) {
				sAmountOrSharesIndexList = Arrays.asList(mapRedemptionDetails.get("TaxLotsIndexes").split(","));
			}
			if (mapRedemptionDetails.get("IsTaxLotsAmountOrShareEditable") != null) {
				sAmountOrSharesIndexesEditableStatusList = Arrays.asList(mapRedemptionDetails.get("IsTaxLotsAmountOrShareEditable").split(","));
				noOfAmountOrShareLots = sAmountOrSharesIndexesEditableStatusList.size();
			}
			if (!sIsAmountOrShare.equalsIgnoreCase("")) {
				//String NoOfRecords = Elements.getElementAttribute(Global.driver, By.id("detailsListSize"), "value");
				//Wait.waitForElementPresence(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-amountShare')]//input[contains(@id,'amountShare')]"), 3);
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-amountShare')]"), 3);
				if (!bStatus && noOfAmountOrShareLots != 0) {
					Messages.errorMsg = "[ ERROR : No Of lots expected ]";
				}
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-amountShare')]//input[contains(@id,'amountShare')]"));				
				//int iNoOfLots = Integer.parseInt(NoOfRecords);
				if (NoOfRecords != noOfAmountOrShareLots) {
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+noOfAmountOrShareLots+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				for (int i = 0; i < noOfAmountOrShareLots; i++) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='amountShareCheckBox_"+i+"']//parent::span[@class='checked']"), 3);
					if (sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes")) {
						if (sAmountOrSharesIndexList != null && !sAmountOrSharesIndexList.contains(String.valueOf(i)) && bStatus) {
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='amountShareCheckBox_"+i+"']//parent::span[@class='checked']"));
							if (!bStatus) {
								Messages.errorMsg = "[ERROR : Unable to unselect the Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
								return false;
							}
						}
						if (sAmountOrSharesIndexList != null && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
							if (!bStatus) {
								bStatus = Elements.click(Global.driver, By.xpath("//input[@id='redLot_"+i+"']"));
								if (!bStatus) {
									Messages.errorMsg = "[ERROR : Unable to select the Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
									return false;
								}
							}
							bStatus = Elements.enterText(Global.driver, By.id("allocated"+sIsAmountOrShare+"_"+i+""), sAmountOrSharesList.get(i));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to enter the text into the Transfer Lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
								return false;
							}
						}							
					}
					if (!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes") && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
						bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.id("allocated"+sIsAmountOrShare+"_"+i+""), sAmountOrSharesList.get(i), "No", true);
						if (!bStatus) {
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

	public static boolean doCheckerVerifyAvailableBalanceDetails(Map<String, String> mapRedemptionDetails){
		int iNoOfDecimalsToVerify = -1;
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		int noOfAmountOrShareLots = 0;
		List<String> sAmountOrSharesList = null;
		List<String> sAmountOrSharesIndexList = null;
		List<String> sAmountOrSharesIndexesEditableStatusList = null;
		try {
			String sIsAmountOrShare = "";
			/*if (mapRedemptionDetails.get("Redemption Method Applied") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapRedemptionDetails.get("Redemption Method Applied"), By.xpath("//div[@id='s2id_redmethodApplied']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to select the 'Redemption Method Applied' option : "+mapRedemptionDetails.get("Redemption Method Applied")+" ]\n";
					return false;
				}
			}*/
			if (mapRedemptionDetails.get("TaxLotsAmounts") != null || mapRedemptionDetails.get("TaxLotsShares") != null) {
				if (mapRedemptionDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsAmounts").split(","));					
				}
				else if (mapRedemptionDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsShares").split(","));					
				}
			}
			if (mapRedemptionDetails.get("TaxLotsIndexes") != null) {
				sAmountOrSharesIndexList = Arrays.asList(mapRedemptionDetails.get("TaxLotsIndexes").split(","));
			}
			if (mapRedemptionDetails.get("IsTaxLotsAmountOrShareEditable") != null) {
				sAmountOrSharesIndexesEditableStatusList = Arrays.asList(mapRedemptionDetails.get("IsTaxLotsAmountOrShareEditable").split(","));
				noOfAmountOrShareLots = sAmountOrSharesIndexesEditableStatusList.size();
			}

			if (!sIsAmountOrShare.equalsIgnoreCase("")) {
				String sAmountOrShareIndex = "";
				if (sIsAmountOrShare.equalsIgnoreCase("Share")) {
					sAmountOrShareIndex = "11";
					if (mapRedemptionDetails.get("ExpectedNumberOfShareDecimals") != null) {
						iNoOfDecimalsToVerify = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals"));
					}
				}
				if (sIsAmountOrShare.equalsIgnoreCase("Amount")) {
					sAmountOrShareIndex = "10";
					if (mapRedemptionDetails.get("ExpectedNumberOfDecimals") != null) {
						iNoOfDecimalsToVerify = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals"));
					}
				}
				//String NoOfRecords = Elements.getElementAttribute(Global.driver, By.id("detailsListSize"), "value");				
				Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td["+sAmountOrShareIndex+"]//span"), 3);
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td["+sAmountOrShareIndex+"]//span"));				
				//int iNoOfLots = Integer.parseInt(NoOfRecords);
				if (NoOfRecords != noOfAmountOrShareLots) {
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+noOfAmountOrShareLots+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				for (int i = 1; i <= noOfAmountOrShareLots; i++) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td["+sAmountOrShareIndex+"]//span"), 3);
					if(!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("None")){

						if (sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes")) {
							/*if (sAmountOrSharesIndexList != null && !sAmountOrSharesIndexList.contains(i) && bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + "[ERROR : Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' is supposed to be in Unselected state but it is appeared to be selected. ]\n";
							bValidateStatus = false;
						}*/
							if (sAmountOrSharesIndexList != null && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
								if (!bStatus) {
									sAppendErrorMsg = sAppendErrorMsg + "[ERROR : Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' is not visible. ]\n";
									bValidateStatus = false;
								}
								bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td["+sAmountOrShareIndex+"]//span"), sAmountOrSharesList.get(i), "Yes", true, iNoOfDecimalsToVerify);
								if (!bStatus) {
									sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
									bValidateStatus = false;
								}
							}							
						}
						if (!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes") && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td["+sAmountOrShareIndex+"]//span"), sAmountOrSharesList.get(i), "Yes", true, iNoOfDecimalsToVerify);
							if (!bStatus) {
								sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
								bValidateStatus = false;
							}							
						}
					}
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	public static boolean doMakerVerifyPendingTrades(Map<String, String> mapRedemptionDetails){
		int iNoOfDecimalsToVerify = -1;
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		int noOfAmountOrShareLots = 0;
		List<String> sAmountOrSharesList = null;
		List<String> sAmountOrSharesIndexList = null;
		List<String> sAmountOrSharesIndexesEditableStatusList = null;
		try {
			String sIsAmountOrShare = "";
			/*if (mapRedemptionDetails.get("Redemption Method Applied") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapRedemptionDetails.get("Redemption Method Applied"), By.xpath("//div[@id='s2id_redmethodApplied']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to select the 'Redemption Method Applied' option : "+mapRedemptionDetails.get("Redemption Method Applied")+" ]\n";
					return false;
				}
			}*/
			if (mapRedemptionDetails.get("TaxLotsAmounts") != null || mapRedemptionDetails.get("TaxLotsShares") != null) {
				if (mapRedemptionDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsAmounts").split(","));					
				}
				else if (mapRedemptionDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsShares").split(","));					
				}
			}
			if (mapRedemptionDetails.get("TaxLotsIndexes") != null) {
				sAmountOrSharesIndexList = Arrays.asList(mapRedemptionDetails.get("TaxLotsIndexes").split(","));
			}
			if (mapRedemptionDetails.get("IsTaxLotsAmountOrShareEditable") != null) {
				sAmountOrSharesIndexesEditableStatusList = Arrays.asList(mapRedemptionDetails.get("IsTaxLotsAmountOrShareEditable").split(","));
				noOfAmountOrShareLots = sAmountOrSharesIndexesEditableStatusList.size();
			}

			if (!sIsAmountOrShare.equalsIgnoreCase("")) {
				String sAmountOrShareIndex = "";
				if (sIsAmountOrShare.equalsIgnoreCase("Share")) {
					sAmountOrShareIndex = "11";
					if (mapRedemptionDetails.get("ExpectedNumberOfShareDecimals") != null) {
						iNoOfDecimalsToVerify = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals"));
					}
				}
				if (sIsAmountOrShare.equalsIgnoreCase("Amount")) {
					sAmountOrShareIndex = "10";
					if (mapRedemptionDetails.get("ExpectedNumberOfDecimals") != null) {
						iNoOfDecimalsToVerify = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals"));
					}
				}
				//String NoOfRecords = Elements.getElementAttribute(Global.driver, By.id("detailsListSize"), "value");				
				Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td["+sAmountOrShareIndex+"]//span"), 3);
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td["+sAmountOrShareIndex+"]//span"));				
				//int iNoOfLots = Integer.parseInt(NoOfRecords);
				if (NoOfRecords != noOfAmountOrShareLots) {
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+noOfAmountOrShareLots+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				for (int i = 1; i <= noOfAmountOrShareLots; i++) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td["+sAmountOrShareIndex+"]//span"), 3);
					if (sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes")) {
						/*if (sAmountOrSharesIndexList != null && !sAmountOrSharesIndexList.contains(i) && bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + "[ERROR : Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' is supposed to be in Unselected state but it is appeared to be selected. ]\n";
							bValidateStatus = false;
						}*/
						if (sAmountOrSharesIndexList != null && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
							if (!bStatus) {
								sAppendErrorMsg = sAppendErrorMsg + "[ERROR : Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' is not visible. ]\n";
								bValidateStatus = false;
							}
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td["+sAmountOrShareIndex+"]//span"), sAmountOrSharesList.get(i), "Yes", true, iNoOfDecimalsToVerify);
							if (!bStatus) {
								sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
								bValidateStatus = false;
							}
						}							
					}
					if (!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes") && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+i+"]//td["+sAmountOrShareIndex+"]//span"), sAmountOrSharesList.get(i), "Yes", true, iNoOfDecimalsToVerify);
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}							
					}
				}		
			}
			Messages.errorMsg = sAppendErrorMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}


	//---------------------------------**************Transfer Functions Checker Side**************-----------------------------
	public static boolean doVerifyTransferDetailsInChecker(Map<String, String> mapTransferDetails, Map<String, String> mapXMLTransferDetails)
	{
		try {
			String appendErrorMsg = "";
			boolean bValidStatus = true;

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Detail')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Detail Label is not visible";
				return false;
			}

			bStatus = verifyRequestDetailsInChecker(mapTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+Messages.errorMsg;
				bValidStatus = false;

			}
			bStatus = verifyFromInvestorDetailsInChecker(mapTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+Messages.errorMsg;
				bValidStatus = false;
			}

			bStatus = verifyToInvestorDetailsChecker(mapTransferDetails, mapXMLTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+Messages.errorMsg;
				bValidStatus = false;
			}

			bStatus = verifyFundDetailsInChecker(mapTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+Messages.errorMsg;
				bValidStatus = false;
			}

			bStatus = verifyTransferDetailsInChecker(mapTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+Messages.errorMsg;
				bValidStatus = false;
			}


			//function to call Available Balance and Pending Trades
			/*bStatus = chekerVerifyAvailableBalancAllocatedeAMountOrShare(mapTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}*/
			//To Verify Allocated Amount and Share in Tax lots
			bStatus = doVerifyCheckerAllocatedAmountandShare(mapTransferDetails);
			if (!bStatus) {
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doCheckerVerifyTransferAvailableBalanceDetails(mapTransferDetails);
			if (!bStatus) {
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			if(mapTransferDetails.get("ExpectedPendingRequestAmount")!=null && mapTransferDetails.get("ExpectedPendingRequestedShares")!=null && !mapTransferDetails.get("ExpectedPendingRequestAmount").contains("Records") && !mapTransferDetails.get("ExpectedPendingRequestedShares").contains("Records")){

				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapTransferDetails.get("ExpectedPendingRequestAmount"), mapTransferDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals")));
				if (!bStatus) {
					appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}
			bStatus = verifyChargesDetailsInChecker(mapTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}
			bStatus = verifyAmountDetailsInChecker(mapTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}
			bStatus = verifyOtherDetailsInChecker(mapTransferDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}
			if(mapTransferDetails.get("ExpectedExceptionsAtChecker")!=null){
				bStatus = doVerifyExceptionsAtCheckerForTradeTypeTransfer(mapTransferDetails.get("ExpectedExceptionsAtChecker"));
				if(!bStatus){
					appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}

			Messages.errorMsg = appendErrorMsg;
			return bValidStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static String formatDate(String initialDate){
		try {

			DateFormat parser = new SimpleDateFormat("dd-MMM-yyyy");
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");    
			String finalDeliveryDate = formatter.format(parser.parse(initialDate));

			return finalDeliveryDate;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	//function to verify RequestDetails in Checker
	private static boolean verifyRequestDetailsInChecker(Map<String, String> mapTransferDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if(mapTransferDetails.get("Received Date")!=null){
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Received Date']//following-sibling::label/input"), 10);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to load the Transfer Checker operations page]";
					return false;
				}
				String sValue=Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Received Date']//following-sibling::label/input"), "value");

				sValue = formatDate(sValue);
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Received Date")))
				{

					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Request Received Date : "+sValue+" , is not matching with Expected : "+mapTransferDetails.get("Received Date")+" ]\n";
					bValidateStatus = false;

				}
			}

			if (mapTransferDetails.get("Received Time") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Received Time']//following-sibling::label/input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapTransferDetails.get("Received Time"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Request Received Time : "+sValue+" , is not matching with Expected : "+mapTransferDetails.get("Received Time")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapTransferDetails.get("Order Received Office") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Order Received Office']//following-sibling::label/input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapTransferDetails.get("Order Received Office"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Order Received Office : "+sValue+" , is not matching with Expected : "+mapTransferDetails.get("Order Received Office")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapTransferDetails.get("Time Zone") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Time Zone']//following-sibling::label/input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapTransferDetails.get("Time Zone"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Time Zone : "+sValue+" , is not matching with Expected : "+mapTransferDetails.get("Time Zone")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapTransferDetails.get("Source") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Source']//following-sibling::label/input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapTransferDetails.get("Source"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Source : "+sValue+" , is not matching with Expected : "+mapTransferDetails.get("Source")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapTransferDetails.get("Mode of Request") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Mode of Request']//following-sibling::label/input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapTransferDetails.get("Mode of Request"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Mode of Request : "+sValue+" , is not matching with Expected : "+mapTransferDetails.get("Mode of Request")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapTransferDetails.get("External ID Number") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='External ID Number']//following-sibling::label/input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapTransferDetails.get("External ID Number"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual External ID : "+sValue+" , is not matching with Expected : "+mapTransferDetails.get("External ID")+" ]\n";
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

	// function to verify FromInvestorDetails at checker
	public static boolean verifyFromInvestorDetailsInChecker(Map<String, String> mapTransferDetails){
		try{		
			String appndErrMsg = "";
			boolean ValidbStatus = true;

			if(mapTransferDetails.get("From Investor Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Investor Name", mapTransferDetails.get("From Investor Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}

			if(mapTransferDetails.get("From Holder Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Holder Name",mapTransferDetails.get("From Holder Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}

			if(mapTransferDetails.get("From Account Id")!=null){
				String accountId= TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapTransferDetails.get("From Account Id"));
				if(accountId != null){
					mapTransferDetails.put("From Account Id", accountId);
				}
				if(!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Account ID' of From Investor, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'From Investor')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div//label[contains(normalize-space(),'Account')]//following-sibling::label//input"), mapTransferDetails.get("From Account Id"), "No", false)){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}		
			Messages.errorMsg = appndErrMsg;
			return ValidbStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//function to verify ToInvestorDetails at checker
	public static boolean verifyToInvestorDetailsChecker(Map<String, String> mapTransferDetails, Map<String, String> mapXMLTransferDetails)
	{
		
		boolean ValidbStatus = true;
		String appndErrMsg = "";
		try {

			if (mapTransferDetails.get("To Investor Name") !=null) {
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Investor Name", mapTransferDetails.get("To Investor Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}				
			}

			if (mapTransferDetails.get("To Holder Name") != null) {
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Holder Name",mapTransferDetails.get("To Holder Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}

			if(mapTransferDetails.get("NewAccount")!=null && mapTransferDetails.get("NewAccount").equalsIgnoreCase("Yes") && mapXMLTransferDetails.get("AccountID")!=null ){
				mapTransferDetails.put("To Account ID", mapXMLTransferDetails.get("AccountID"));
			}
			if (mapTransferDetails.get("NewAccount") == null && mapTransferDetails.get("To Account ID") != null) {
				String accountID = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapTransferDetails.get("To Account ID"));
				if(accountID != null){
					mapTransferDetails.put("To Account ID", accountID);
				}
			}
			if(!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Account ID' of To Investor, ", By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'To Investor')]//parent::div[contains(@class,'title')]//following-sibling::div[contains(@class,'portlet')]//div//label[contains(normalize-space(),'Account')]//following-sibling::label//input"), mapTransferDetails.get("To Account ID"), "No", false)){
				appndErrMsg = appndErrMsg + Messages.errorMsg;
				ValidbStatus = false;
			}
		} 

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = appndErrMsg;
		return ValidbStatus;
	}

	//function to verify FundDetails at checker
	public static boolean verifyFundDetailsInChecker(Map<String, String> mapTransferDetails){
		String expectedValue="";
		String actualValue="";
		String appndErrMsg = "";
		boolean ValidbStatus = true;

		try {
			NewUICommonFunctions.scrollToView(By.xpath("//Label[text()='Client Name']"));
			//Verify Client Name
			if (mapTransferDetails.get("Client Name") != null) {				
				actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Client Name' or normalize-space(text())='Client Name']/following-sibling::label/input"), "value");
				expectedValue = mapTransferDetails.get("Client Name");
				if (!actualValue.trim().equalsIgnoreCase(expectedValue)) {
					appndErrMsg = appndErrMsg + "[ERROR: Expected Client Name is not matching the "+mapTransferDetails.get("Client Name")+", is not matcing with actual";
					return ValidbStatus = false;
				}

			}

			//Verify Fund Family Name
			if (mapTransferDetails.get("Fund Family Name") != null) {				
				actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Fund Family Name' or normalize-space(text())='Fund Family Name']/following-sibling::label/input"), "value");
				expectedValue = mapTransferDetails.get("Fund Family Name");
				if (!actualValue.trim().equalsIgnoreCase(expectedValue)) {
					appndErrMsg = appndErrMsg + "[ERROR: Expected Fund Family Name is not matching the "+mapTransferDetails.get("Fund Family Name")+", is not matcing with actual";
					return ValidbStatus = false;
				}
			}

			//Verify Legal Entity Name
			if (mapTransferDetails.get("Legal Entity Name") != null) {				
				actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Legal Entity' or normalize-space(text())='Legal Entity']/following-sibling::label/input"), "value");
				expectedValue = mapTransferDetails.get("Legal Entity Name");
				if (!actualValue.trim().equalsIgnoreCase(expectedValue)) {
					appndErrMsg = appndErrMsg + "[ERROR: Expected Legal Entity Name is not matching the "+mapTransferDetails.get("Legal Entity Name")+", is not matcing with actual";
					return ValidbStatus = false;
				}
			}

			//Verify Class Name
			if (mapTransferDetails.get("Class Name") != null) {
				actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Class Name' or normalize-space(text())='Class Name']/following-sibling::label/input"), "value");
				expectedValue = mapTransferDetails.get("Class Name");
				if (!actualValue.trim().equalsIgnoreCase(expectedValue)) {
					appndErrMsg = appndErrMsg + "[ERROR: Expected Class Name is not matching the "+mapTransferDetails.get("Class Name")+", is not matcing with actual";
					return ValidbStatus = false;
				}
			}

			//Verify Series Name
			if (mapTransferDetails.get("Series Name") != null) {				
				actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Series Name' or normalize-space(text())='Series Name']/following-sibling::label/input"), "value");
				expectedValue = mapTransferDetails.get("Series Name");
				if (!actualValue.trim().equalsIgnoreCase(expectedValue)) {
					appndErrMsg = appndErrMsg + "[ERROR: Expected Series Name is not matching the "+mapTransferDetails.get("Series Name")+", is not matcing with actual";
					return ValidbStatus = false;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return ValidbStatus;
	}

	// function to verify TransferDetails at Checker
	public static boolean verifyTransferDetailsInChecker(Map<String, String> mapTransferDetails){
		String expectedValue="";
		String actualValue="";

		String appndErrMsg = "";
		boolean validbStatus = true;

		try {		
			//Verify Full Transfer Radio button
			if (mapTransferDetails.get("Full Transfer") != null) {
				NewUICommonFunctions.scrollToView(By.xpath("//Label[text()='Full Transfer']"));
				if (mapTransferDetails.get("Full Transfer").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//Label[text()='Full Transfer']/following-sibling::label[normalize-space(text())='Yes']"), 2);
					if (!bStatus) {
						appndErrMsg = appndErrMsg + "[ERROR : Full Transfer Radio button 'Yes' is Not Selected.]\n";
						validbStatus = false;
					}
				}
				if (mapTransferDetails.get("Full Transfer").equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//Label[text()='Full Transfer']/following-sibling::label[normalize-space(text())='No']"), 2);
					if (!bStatus) {
						appndErrMsg = appndErrMsg + "[ERROR : Full Transfer Radio button 'No' is Not Selected]\n";
						validbStatus = false;
					}
				}
			}
			//Verify Amount or shares
			if (mapTransferDetails.get("AmountorShares") != null) {
				//Verify the Amount
				if (mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
					bStatus=Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Amount/Share']/following-sibling::label/input"), "value").trim().equalsIgnoreCase("Amount");
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : AmountorShares Radio button  'Amount' is Not Selected.]\n";
						return false;
					}
					if (mapTransferDetails.get("Cash") != null) {				
						if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Cash", By.xpath("//label[text()='Cash']/following-sibling::label/input"), mapTransferDetails.get("Cash"), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")))) {
							return false;
						}
					}
				}
				if (mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Shares")) {

					//Verify the Shares
					bStatus = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Amount/Share']/following-sibling::label/input"), "value").trim().equalsIgnoreCase("Shares");
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : AmountorShares Radio button  'Shares' is Not Selected.]\n";
						return false;
					}
					if (mapTransferDetails.get("Share") != null) {				
						if (!NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share", By.xpath("//label[text()='Share']//following-sibling::label//input"), mapTransferDetails.get("Share"), "No", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals")))) {
							return false;
						}
					}
				}
			}
			//Verify Effective Date
			if (mapTransferDetails.get("Effective Date") != null) {				
				actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//Label[text()='Effective Date']/following-sibling::label/input"), "value");
				actualValue = formatDate(actualValue);
				expectedValue = mapTransferDetails.get("Effective Date");
				if (!actualValue.trim().equalsIgnoreCase(expectedValue)) {
					appndErrMsg = appndErrMsg + "[ERROR: Expected Effective Date is not matching the "+mapTransferDetails.get("Effective Date")+", is not matcing with actual :"+actualValue;
					validbStatus = false;
				}
			}
			//Verify Percentage 
			if (mapTransferDetails.get("Percentage") != null) {	
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Percentage, ", By.xpath("//Label[text()='Percentage']//following-sibling::label//input"), mapTransferDetails.get("Percentage"), "No", true, -1);
				/*actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//Label[text()='Percentage']/following-sibling::label/input"), "value");
				expectedValue = mapTransferDetails.get("Percentage");*/
				if (!bStatus) {
					appndErrMsg = appndErrMsg + Messages.errorMsg;
					validbStatus = false;
				}
			}
			if (mapTransferDetails.get("Expected NAV Date") != null) {	
				actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//Label[text()='NAV Date']/following-sibling::label/input"), "value");
				actualValue = formatDate(actualValue);
				expectedValue = mapTransferDetails.get("Expected NAV Date");
				if (!actualValue.trim().equalsIgnoreCase(expectedValue)) {
					appndErrMsg = appndErrMsg + "[ERROR: Expected NAV Date is not matching the "+mapTransferDetails.get("Expected NAV Date")+", is not matcing with actual :"+actualValue;
					validbStatus = false;
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = appndErrMsg;
		return validbStatus;
	}

	//function to verify ChargeDetails at Checker 
	public static boolean verifyChargesDetailsInChecker(Map<String, String> mapTransferDetails)
	{
		boolean validateStatus = true;
		String appendMsg="";
		try {
			if (mapTransferDetails != null && !mapTransferDetails.isEmpty()) {
				//verify Notice Charges for Notice Charge Default=YES

				if(mapTransferDetails.get("NoticeChargesRadioButton")!=null && mapTransferDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[text()='Notice Charge Default']/following-sibling::label[normalize-space(text())='Yes']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+ "[ Notice Charge Default Radio button Yes is not selected  ]\n";
					}

					if (mapTransferDetails.get("ExpectedNoticeCharges") != null) {
						String ActualNoticeCharges = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Notice Charge Amount']/following-sibling::label/input"), "value");
						String ExpectedNoticeCharge = mapTransferDetails.get("ExpectedNoticeCharges");
						if (ActualNoticeCharges != null && !ActualNoticeCharges.equalsIgnoreCase("")) {
							ActualNoticeCharges = ActualNoticeCharges.replaceAll(",", "");
							bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Notice Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualNoticeCharges);
							if(!bStatus){
								validateStatus = false;
								appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
							}
							if (Float.parseFloat(ActualNoticeCharges) != Float.parseFloat(ExpectedNoticeCharge)) {
								appendMsg = appendMsg+"[ERROR : Checker verification : Actual Notice Charges : "+ActualNoticeCharges+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapTransferDetails.get("ExpectedNewNoticeCharges")+" .] \n";
								validateStatus = false;
							}
						}
					}
				}
				//verify Notice Charges for Notice Charge Default=NO
				if(mapTransferDetails.get("NoticeChargesRadioButton")!=null && mapTransferDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){

					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[text()='Notice Charge Default']/following-sibling::label[normalize-space(text())='No']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+ "[ Notice Charge Default Radio button No is not selected  ]\n";
					}
					if (mapTransferDetails.get("ExpectedNewNoticeCharges") != null) {
						String ActualNoticeCharges = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Notice Charge Amount']/following-sibling::label/input"), "value");
						String ExpectedNoticeCharge = mapTransferDetails.get("ExpectedNewNoticeCharges");

						if (ActualNoticeCharges != null && !ActualNoticeCharges.equalsIgnoreCase("")) {
							ActualNoticeCharges = ActualNoticeCharges.replaceAll(",", "");
							bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Notice Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualNoticeCharges);
							if(!bStatus){
								validateStatus = false;
								appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
							}
							if (Float.parseFloat(ActualNoticeCharges) != Float.parseFloat(ExpectedNoticeCharge)) {
								appendMsg = appendMsg+"[ERROR : Checker verification : Actual Notice Charges : "+ActualNoticeCharges+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapTransferDetails.get("ExpectedNewNoticeCharges")+" .] \n";
								validateStatus = false;
							}
						}
					}
				}
				//Verify TransactionCharges for Transaction Charge Default =YES
				if( mapTransferDetails.get("TransactionChargesRadioButton")!=null){

					if(mapTransferDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){

						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[text()='Fixed Transaction Charge Default']/following-sibling::label[normalize-space(text())='Yes']"));
						if(!bStatus){
							validateStatus = false;
							appendMsg = appendMsg+ "[ Transaction Charge Default Radio button Yes is not selected  ]\n";
						}
						// Verify Notice Charges
						if (mapTransferDetails.get("ExpectedTransactionCharges") != null) {
							String ActualTransactionCharges = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Fixed Tranaction Charge Amount']/following-sibling::label/input"), "value");
							String ExpectedTransactionCharges = mapTransferDetails.get("ExpectedTransactionCharges");

							if (ActualTransactionCharges != null && !ActualTransactionCharges.equalsIgnoreCase("")) {
								ActualTransactionCharges = ActualTransactionCharges.replaceAll(",", "");
								bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Actual Transaction Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualTransactionCharges);
								if(!bStatus){
									validateStatus = false;
									appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
								}
								if (Float.parseFloat(ActualTransactionCharges) != Float.parseFloat(ExpectedTransactionCharges)) {
									appendMsg = appendMsg+"[ERROR : Checker verification : Actual Transaction Charges : "+ActualTransactionCharges+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapTransferDetails.get("ExpectedTransactionCharges")+" .] \n";
									validateStatus = false;
								}
							}
						}
					}
					//Verify TransactionCharges for Transaction Charge Default =NO
					if(mapTransferDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){

						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[text()='Fixed Transaction Charge Default']/following-sibling::label[normalize-space(text())='No']"));
						if(!bStatus){
							validateStatus = false;
							appendMsg = appendMsg+ "[ Transaction Charge Default Radio button No is not selected  ]\n";
						}
						// Verify Notice Charges
						if (mapTransferDetails.get("NewExpectedTransactionCharges") != null) {
							String ActualTransactionCharges = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Fixed Tranaction Charge Amount']/following-sibling::label/input"), "value");
							String ExpectedTransactionCharges = mapTransferDetails.get("NewExpectedTransactionCharges");

							if (ActualTransactionCharges != null && !ActualTransactionCharges.equalsIgnoreCase("")) {
								ActualTransactionCharges = ActualTransactionCharges.replaceAll(",", "");
								bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Actual Transaction Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualTransactionCharges);
								if(!bStatus){
									validateStatus = false;
									appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
								}
								if (Float.parseFloat(ActualTransactionCharges) != Float.parseFloat(ExpectedTransactionCharges)) {
									appendMsg = appendMsg+"[ERROR : Checker verification : Actual Transaction Charges : "+ActualTransactionCharges+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapTransferDetails.get("ExpectedTransactionCharges")+" .] \n";
									validateStatus = false;
								}
							}
						}
					}
				}
				// Verify Adhoc Charges
				if (mapTransferDetails.get("Adhoc Charges") != null) {
					String actualAdhocCharges = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Adhoc Charges Amount']/following-sibling::label/input"), "Value");
					String expectedAdhocCharges = mapTransferDetails.get("Adhoc Charges");
					if (actualAdhocCharges != null && !actualAdhocCharges.equalsIgnoreCase("")) {
						actualAdhocCharges = actualAdhocCharges.replaceAll(",", "");
						bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Actual Adhoc Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), actualAdhocCharges);
						if(!bStatus){
							validateStatus = false;
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
						}
						if (Float.parseFloat(actualAdhocCharges) != Float.parseFloat(expectedAdhocCharges)) {
							appendMsg = appendMsg+"[ERROR : Checker verification : Actual Adhoc Charges : "+actualAdhocCharges+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapTransferDetails.get("Adhoc Charges")+" .] \n";
							validateStatus = false;
						}
					}
				}
				// Verify Total Charges 
				String ActualTotalcharges =  Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Total Charges']/following-sibling::label/input"), "value");
				String ExpectedTotalCharges = mapTransferDetails.get("ExpectedTotalCharges");
				if (ActualTotalcharges != null && !ActualTotalcharges.equalsIgnoreCase("")) {
					ActualTotalcharges = ActualTotalcharges.replaceAll(",", "");
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Actutal Total Charges ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), ActualTotalcharges);
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					}
					if (Float.parseFloat(ActualTotalcharges ) != Float.parseFloat(ExpectedTotalCharges)) {
						appendMsg = appendMsg+"[ERROR : Checker verification : Actual Total Charges: "+ActualTotalcharges +" for Type Cash is not matching with ExpectedNoOfUnits : "+ mapTransferDetails.get("ExpectedTotalCharges")+" .] \n";
						validateStatus = false;
					}
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

	//function to verify AmountDetails at Checker 
	public static boolean verifyAmountDetailsInChecker(Map<String, String> mapTransferDetails)
	{
		try {
			boolean bValidateStatus = true;
			String appendMsg = "";
			if (mapTransferDetails.get("Gross Amount") != null) {
				NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='Gross Amount']"));
				String actualGrossAmount = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Gross Amount']/following-sibling::label/input"), "value");
				String expectedGrossAmount = mapTransferDetails.get("Gross Amount");
				actualGrossAmount = actualGrossAmount.replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Gross Amount ", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), actualGrossAmount);
				if(!bStatus){
					bValidateStatus = false;
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
				}
				if (Float.parseFloat(actualGrossAmount ) != Float.parseFloat(expectedGrossAmount)) {
					appendMsg = appendMsg+"[ERROR : Checker verification : Actual Gross Amount: "+actualGrossAmount +" for Type Cash is not matching with Expected : "+ mapTransferDetails.get("Gross Amount")+" .] \n";
					bValidateStatus = false;
				}
			}
			if (mapTransferDetails.get("Charges") != null) {
				String actualCharges = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Charges']/following-sibling::label/input"), "value");
				String expectedCharges = mapTransferDetails.get("Charges");
				actualCharges = actualCharges.replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Charges", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), actualCharges);
				if(!bStatus){
					bValidateStatus = false;
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
				}
				if (Float.parseFloat(actualCharges) != Float.parseFloat(expectedCharges)) {
					appendMsg = appendMsg+"[ERROR : Checker verification : Actual Charges: "+actualCharges +" for Type Cash is not matching with Expected: "+ mapTransferDetails.get("Charges")+" .] \n";
					bValidateStatus = false;
				}
			}
			if (mapTransferDetails.get("Transfer Amount") != null) {
				String actualTransferAmount = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Transfer Amount']/following-sibling::label/input"), "value");
				String expectedTransferAmount = mapTransferDetails.get("Transfer Amount");
				actualTransferAmount = actualTransferAmount.replace(",","");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Transfer Amount", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), actualTransferAmount);
				if(!bStatus){
					bValidateStatus = false;
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
				}
				if (Float.parseFloat(actualTransferAmount) != Float.parseFloat(expectedTransferAmount)) {
					appendMsg = appendMsg+"[ERROR : Checker verification : Actual Transfer Amount: "+actualTransferAmount +" for Type Cash is not matching with Expected: "+ mapTransferDetails.get("Transfer Amount")+" .] \n";
					bValidateStatus = false;
				}
			}
			if (mapTransferDetails.get("Net Units") != null) {
				String actualUnits =  Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Net Units']/following-sibling::label/input"), "value");
				String expectedUnits = mapTransferDetails.get("Net Units");
				actualUnits = actualUnits.replace(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Net Units", Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), actualUnits);
				if(!bStatus){
					bValidateStatus = false;
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
				}
				if (Float.parseFloat(actualUnits) != Float.parseFloat(expectedUnits)) {
					appendMsg = appendMsg+"[ERROR : Checker verification : Actual Net Units: "+actualUnits +" for Type Net Units is not matching with Expected: "+ mapTransferDetails.get("Net Units")+" .] \n";
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = appendMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//function to Verify OtherDetails at Checker
	public static boolean verifyOtherDetailsInChecker(Map<String, String> mapTransferDetails)
	{
		try {
			boolean bValidateStatus = true;
			String sAppendErrorMsg = "";
			if (mapTransferDetails.get("Crystalize Fee") != null) {

				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Crystalize Fee", By.xpath("//label[normalize-space()='Crystalize Fee']/following-sibling::label[normalize-space()]"), mapTransferDetails.get("Crystalize Fee"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg+ "[ERROR :Other Details Crystalize fee is not Matched with Expected"  +mapTransferDetails.get("Crystalize Fee")+" ] \n";
					bValidateStatus = false;
				}
			}
			if (mapTransferDetails.get("Cumulative Return") != null) {
				String sValue = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Cumulative Return']//following-sibling::div"));
				if (mapTransferDetails.get("Cumulative Return").equalsIgnoreCase("No")) {
					if (!sValue.trim().equalsIgnoreCase("N/A")) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ERROR :Other Details Cumulative Return is not Matched with Expected"  +mapTransferDetails.get("Cumulative Return")+" ] \n";
						bValidateStatus = false;	
					}
				}
				if (mapTransferDetails.get("Cumulative Return").equalsIgnoreCase("Yes")) {
					if (!sValue.trim().equalsIgnoreCase("yes")) {
						sAppendErrorMsg = sAppendErrorMsg+ "[ERROR :Other Details Cumulative Return is not Matched with Expected"  +mapTransferDetails.get("Cumulative Return")+" ] \n";
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

	//function to verify MasterApproval at checker
	public static boolean doOpeartionsonMasterCreatedFromTrade(Map<String, String> mapTransferDetails)
	{
		//Approve for New Investor Created for Trading
		if(mapTransferDetails.get("New Investor")!=null && mapTransferDetails.get("New Investor").equalsIgnoreCase("Yes") && mapTransferDetails.get("New Investor Operation") !=null)
		{
			bStatus = Elements.click(Global.driver, By.id("approveInvestor"));
			if (!bStatus) 
			{
				Messages.errorMsg = "[Error : Unable to click on  View button for Investor ]";
				return false;
			}
			//call function to approve or return or reject
			if (mapTransferDetails.get("New Investor Operation")!=null && mapTransferDetails.get("New Investor Operation").equalsIgnoreCase("Approve")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.APPROVE);
				if (!bStatus) 
				{
					return false;
				}
			}
			if (mapTransferDetails.get("New Investor Operation")!=null && mapTransferDetails.get("New Investor Operation").equalsIgnoreCase("Return")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.RETURN);
				if (!bStatus) 
				{
					return false;
				}
			}
			if (mapTransferDetails.get("New Investor Operation")!=null && mapTransferDetails.get("New Investor Operation").equalsIgnoreCase("Cancel")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.CANCEL);
				if (!bStatus) 
				{
					return false;
				}
			}

			if (mapTransferDetails.get("New Investor Operation")!=null && mapTransferDetails.get("New Investor Operation").equalsIgnoreCase("Reject")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.REJECT);
				if (!bStatus) 
				{
					return false;
				}
			}
		}
		//Approve for New Holder Created for Trading
		if (mapTransferDetails.get("New Holder")!=null && mapTransferDetails.get("New Holder").equalsIgnoreCase("Yes")) {
			bStatus = Elements.click(Global.driver, By.id("approveHolder"));
			if (!bStatus) {
				Messages.errorMsg = "Unable to click on View button for Holder";
				return false;
			}
			//call function to approve or return or reject
			if (mapTransferDetails.get("New Holder Operation") != null && mapTransferDetails.get("New Holder Operation").equalsIgnoreCase("Approve")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.APPROVE);
				if (!bStatus) {
					return false;
				}
			}
			if (mapTransferDetails.get("New Holder Operation") != null && mapTransferDetails.get("New Holder Operation").equalsIgnoreCase("Return")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.RETURN);
				if (!bStatus) {
					return false;
				}
			}
			if (mapTransferDetails.get("New Holder Operation") != null && mapTransferDetails.get("New Holder Operation").equalsIgnoreCase("Reject")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.REJECT);
				if (!bStatus) {
					return false;
				}
			}
			if (mapTransferDetails.get("New Holder Operation") != null && mapTransferDetails.get("New Holder Operation").equalsIgnoreCase("Cancel")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.CANCEL);
				if (!bStatus) {
					return false;
				}
			}
		}
		return true;
	}

	//Checker Actions on Masters.
	public static boolean doCheckerActionTypesOnMasters(checkerActionTypes ActionType)
	{
		try{

			String locator=null;
			switch (ActionType) {
			case APPROVE:
				locator = "//button[contains(@onclick, 'javascript:submitWorkflow')and contains(@onclick, 'Approve')]";
				break;
			case RETURN:
				locator = "//button[contains(@onclick, 'javascript:openCommentsModal')and contains(@onclick, 'Return')]";
				break;
			case REJECT:
				locator = "//button[contains(@onclick, 'javascript:openCommentsModal')and contains(@onclick, 'Reject')]";
				break;
			case CANCEL:
				locator = "//button[contains(@onclick, 'javascript:cancelDashboard')]";
				break;
			}

			/*bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//strong[text()='General Details' or text()='Class Details']"),Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "Page is not visible for checker operations";
							return false;
						}*/
			bStatus = Elements.click(Global.driver, By.xpath(locator));
			if(!bStatus){
				Messages.errorMsg = ActionType+" button is not visible/Clickable";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//Verify Exception at checker level
	public static boolean doVerifyExceptionsAtCheckerForTradeTypeTransfer(String sExceptions){		
		try {	
			boolean isExceptionsMismatch = false;
			List<String> sActualExceptions = new ArrayList<String>();
			if (sExceptions != null && !sExceptions.equalsIgnoreCase("")) {
				List<String> sExceptionList = Arrays.asList(sExceptions.split(":"));

				int iNoOfExceptions = Elements.getXpathCount(Global.driver, By.xpath("//label[contains(text(),'Exceptions')]//following-sibling::div/div[contains(@class, 'col-md-10')]"));

				if (sExceptionList.size() != iNoOfExceptions) {
					isExceptionsMismatch = true;
				}
				for (int i = 1; i <= iNoOfExceptions; i++) {
					String sExceptionText = Elements.getText(Global.driver, By.xpath("//label[contains(text(),'Exceptions')]//following-sibling::div//div["+i+"]//label"));
					sActualExceptions.add(sExceptionText);
					if (sExceptionText == null && !sExceptionList.contains(sExceptionText.trim())) {
						isExceptionsMismatch = true;
					}
				}
				if (isExceptionsMismatch == true) {
					Messages.errorMsg = "[ ERROR : No Of Exceptions expected : "+sExceptionList.size()+" i.e.["+sExceptions+"] is not matching with Actual i.e. : "+iNoOfExceptions+" ["+sActualExceptions+"]]";
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Over ride Transaction charges at checker Level
	public static boolean doVerifyOrChangeOverridedChargesTypeForTradeTypeTransfer(String sOverridedChargeName, String isChangeStatusYesToNo){
		try {
			String sChargeTypeNameSubString = "";
			if (sOverridedChargeName != null) {
				NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='Override']"));
				if (sOverridedChargeName.toLowerCase().contains("notice")) {
					sChargeTypeNameSubString = "Notice";
				}

				if (sOverridedChargeName.toLowerCase().contains("transaction")) {
					sChargeTypeNameSubString = "Transaction";
				}
				if (sOverridedChargeName.toLowerCase().contains("incentive")) {
					sChargeTypeNameSubString = "Incentive";
				}

			}
			if (sChargeTypeNameSubString != null && !sChargeTypeNameSubString.equalsIgnoreCase("")) 
			{

				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(text(),'Override')]//following-sibling::div//label[contains(text(), '"+sChargeTypeNameSubString+"')]//following-sibling::div//span[@class='checked']//input[contains(@id,'Yes')]"), 3);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Overrided 'Notice Period Charges' aren't marked to 'YES' in Checker verification.] \n";
					return false;
				}
				if (bStatus && isChangeStatusYesToNo != null && isChangeStatusYesToNo.equalsIgnoreCase("No")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//label[contains(text(),'Override')]//following-sibling::div//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//input[contains(@id,'No')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to change the status of Overrided chanrges of "+sChargeTypeNameSubString+" to 'NO'.] \n";
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

	//Perform Checker Operation on TRADE(Main)
	public static boolean doCheckerActionTypesOnTradeTypeTransfer(Map<String, String> mapTransferDetails)
	{

		bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Detail')]"),Constants.lTimeOut);
		if(!bStatus){
			Messages.errorMsg = "[Error : Request  Detail Label is not visible for Checker Operations to Perfrom Master Approval for 'Investor' and 'Holder' ] ";
			return false;
		}
		bStatus = doOpeartionsonMasterCreatedFromTrade(mapTransferDetails);
		if(!bStatus){
			Messages.errorMsg ="[Error : Cannot Perform Checker Operation to Perfrom Master Approval for 'Investor' and 'Holder' .Error: ] "+Messages.errorMsg ;
			return false;
		}
	
		
		//download OA and Delete
		if(mapTransferDetails.get("isOAClient") != null){
			bStatus = verifyOrderAcknowledgementDownload(mapTransferDetails);
			if(!bStatus && mapTransferDetails.get("isOAClient").equalsIgnoreCase("Yes")){
				return false;
			}
			if(bStatus && mapTransferDetails.get("isOAClient").equalsIgnoreCase("No")){
				Messages.errorMsg = "Order Acknowldegement Downloaded for Non OA Client";
				return false;
			}
		}
		

		if(mapTransferDetails.get("Approve OA")!=null && mapTransferDetails.get("Approve OA").equalsIgnoreCase("Yes")){
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-approveOANo']/span[@class='checked']/input"),3);
			if(bStatus){
				bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-approveOAYes']/span/input"));
				if(!bStatus){
					Messages.errorMsg = "Cannot be clicked on Approve OA Yes Radio button";
					return false;
				}
			}
			//Check Yes button is checked or not
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-approveOAYes']/span[@class='checked']/input"),Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Approve OA Yes Radio button Not Selected";
				return false;
			}
		}


		bStatus = changeOverrideStatus(mapTransferDetails);
		if(!bStatus){
			return false;
		}

		if(mapTransferDetails.get("CheckerOperations").equalsIgnoreCase("Approve"))
		{
			//Checker Operation for Subscription 
			bStatus = doCheckerActionTypesForTranSaction(checkerActionTypes.APPROVE);
			if (!bStatus) {
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Approved Successfully but success message Not Displayed ";
				return false;
			}
			return true;
		}

		if(mapTransferDetails.get("CheckerOperations").equalsIgnoreCase("Cancel")){
			bStatus = doCheckerActionTypesForTranSaction(checkerActionTypes.CANCEL);
			if(!bStatus){
				Messages.errorMsg = "[Error : Calcel button is Not Clicked ] ";
				return false;
			}

		}
		if(mapTransferDetails.get("CheckerOperations").equalsIgnoreCase("Reject"))
		{
			bStatus = doCheckerActionTypesForTranSaction(checkerActionTypes.REJECT);
			if(!bStatus){
				return false;
			}
			Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
			Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Rejected - Comments By QA");
			Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:saveReasonForRejection')]"));
			bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = " Rejected Successfully Message Not Displayed ";
				return false;
			}
			return true;
		}

		if(mapTransferDetails.get("CheckerOperations").equalsIgnoreCase("Return"))
		{
			bStatus = doCheckerActionTypesForTranSaction(checkerActionTypes.RETURN);
			if(!bStatus){
				return false;
			}
			Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
			Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Returned - Comments By QA");
			Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:saveReasonForRejection')]"));
			bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = " Returned Successfully Message Not Displayed ";
				return false;
			}
			return true;
		}
		if(mapTransferDetails.get("CheckerOperations").contains("Review"))
		{
			bStatus = doCheckerActionTypesForTranSaction(checkerActionTypes.CheckerReview);
			if(!bStatus){
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = " Reviewed Successfully Message Not Displayed ";
				return false;
			}
			return true;
		}
		return false;
	}

	//View order Acknowledgment
	public static boolean verifyOrderAcknowledgementDownload(Map<String , String> mapTransferDetails) 
	{
		try {
			bStatus = Elements.click(Global.driver, By.xpath("//div[contains(@class, 'input-group')]/a[contains(@onclick, 'javascript:viewDocuments')]"));
			if (!bStatus) {
				Messages.errorMsg = "Unable to click View Oder Acknowledgement.";
				return false ;
			}
			TimeUnit.SECONDS.sleep(5);
			File folder = new File(System.getProperty("user.home")+"/Downloads");
			String investorName = mapTransferDetails.get("From Investor Name").replaceAll(" ", "_").replaceAll("\\W","");
			File[] fileList = folder.listFiles();
			for (int index = 0; index < fileList.length; index++) {
				if (fileList[index].getName().contains(investorName)) {
					fileList[index].delete();
					return true;
				}
			}
			Messages.errorMsg = "[ Error :Unable to download the file ]";
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messages.errorMsg = Messages.errorMsg + e.getMessage();
			return false;
		}

	}

	//Charge override function in checker Side
	public static boolean changeOverrideStatus(Map<String , String> mapTransferDetails){
		try {

			if(mapTransferDetails.get("NewAmountForNoticePeriod")!=null && mapTransferDetails.get("NoticePeriodOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeTransfer("Notice Period", mapTransferDetails.get("NoticePeriodOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}
			if(mapTransferDetails.get("NewTransactionCharges")!=null && mapTransferDetails.get("TransactionOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeTransfer("Transaction", mapTransferDetails.get("TransactionOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}

			if(mapTransferDetails.get("Crystalize Fee")!=null && mapTransferDetails.get("CrystalizeOverrideAtChecker")!=null && mapTransferDetails.get("Crystalize Fee").equalsIgnoreCase("Yes")){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeTransfer("Incentive Crystalize", mapTransferDetails.get("CrystalizeOverrideAtChecker"));
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

	//function to Click Approve, Reject, Return, Cancel on Masters Approval window for Transfer.....
	public static boolean doCheckerActionTypesForTranSaction(checkerActionTypes ActionType)
	{
		try{

			String locator=null;
			switch (ActionType) {
			case APPROVE:
				locator = "//button[contains(@onclick, 'javascript:submitCheckerRequestForTransfer')and contains(@onclick, 'Done')]";
				break;
			case RETURN:
				locator = "//button[contains(@onclick, 'javascript:submitCheckerRequestForTransfer')and contains(@onclick, 'Return')]";
				break;
			case REJECT:
				locator = "//button[contains(@onclick, 'javascript:submitCheckerRequestForTransfer')and contains(@onclick, 'Reject')]";
				break;
			case CANCEL:
				locator = "//button[contains(@onclick, 'javascript:cancelDashboard')]";
				break;
			case CheckerReview:
				locator = "//button[contains(@onclick, 'CRReviewed')]";
				break;

			}

			/*bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Detail')]"),Constants.lTimeOut);
							if(!bStatus){
								Messages.errorMsg = "Page is not visible for checker operations";
								return false;
							}*/
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(locator));
			if(!bStatus){
				Messages.errorMsg = ActionType+" button is not visible/Clickable."+Messages.errorMsg;
				return false;
			}
			bStatus = Wait.waitForAlert(Global.driver, 5);
			if(bStatus){
				String alertText = Alerts.getAlertMessage(Global.driver);
				bStatus = Alerts.closeAlert(Global.driver);
				if(!bStatus){
					Messages.errorMsg = "Cannot Perform Checker Operation "+ActionType+" .Cannot close Alert ";
					return false;
				}
				Messages.errorMsg = "Cannot Perform Checker Operation "+ActionType+" .Alert Present Alert Text : "+alertText;
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
		NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
		int noOfAmountOrShareLots = 0;
		List<String> sAmountOrSharesList = null;
		List<String> sAmountOrSharesIndexList = null;
		List<String> sAmountOrSharesIndexesEditableStatusList = null;
		try {
			String sIsAmountOrShare = "";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]"));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Page cannot scroll to Available Balance Details . ]\n";
				return false;
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
			if (mapTransferDetails.get("TaxLotsIndexes") != null &&  !mapTransferDetails.get("TaxLotsIndexes").equalsIgnoreCase("None")) {
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
					if(!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("None")){
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
								bStatus = TradeTypeRedemptionAppFunctions.ClearAndSetText(locator, sAmountOrSharesList.get(i));
								NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]"));
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
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Maker Verifying Available balance Details.
	public static boolean doMakerVerifyAvailableBalanceDetails(Map<String, String> mapTransferDetails){
		try {
			int iExpectedNoOfDecimals = -1;
			int iExpectedNoOfShareDecimals = -1;
			if (mapTransferDetails.get("ExpectedNumberOfDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfDecimals").equalsIgnoreCase("")) {
				iExpectedNoOfDecimals = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals"));
			}
			if (mapTransferDetails.get("ExpectedNumberOfShareDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfShareDecimals").equalsIgnoreCase("")) {
				iExpectedNoOfShareDecimals = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals"));
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			boolean bValidStatus = true;
			String appendErrMsg = "";
			if(mapTransferDetails.get("ExpectedActualBalance") != null && mapTransferDetails.get("ExpectedActualShares") != null && mapTransferDetails.get("ExpectedAvailableAmount") != null && mapTransferDetails.get("ExpectedAvailableShares") != null )
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
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Actual Balance' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[5]"), actualBalance.get(i), "Yes", true, iExpectedNoOfDecimals);						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Actual Shares' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[6]"), actualShares.get(i), "Yes", true, iExpectedNoOfShareDecimals);						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Available Amount' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[7]//input"), availableAmount.get(i), "No", true, iExpectedNoOfDecimals);						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Available Shares' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[8]//input"), avaialableShares.get(i), "No", true, iExpectedNoOfShareDecimals);						
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
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '2' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[2]//input"), mapTransferDetails.get("ExpectedTotalActualBalance"), "No", true, iExpectedNoOfDecimals);						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalActualShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualShares' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '3' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[3]//input"), mapTransferDetails.get("ExpectedTotalActualShares"), "No", true, iExpectedNoOfShareDecimals);						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalAvailableAmount").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalAvailableAmount' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '4' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[4]//input"), mapTransferDetails.get("ExpectedTotalAvailableAmount"), "No", true, iExpectedNoOfDecimals);						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalAvailableShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalAvailableShares' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '5' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[5]//input"), mapTransferDetails.get("ExpectedTotalAvailableShares"), "No", true, iExpectedNoOfShareDecimals);						
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
			int iExpectedNoOfDecimals = -1;
			int iExpectedNoOfShareDecimals = -1;
			if (mapTransferDetails.get("ExpectedNumberOfDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfDecimals").equalsIgnoreCase("")) {
				iExpectedNoOfDecimals = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals"));
			}
			if (mapTransferDetails.get("ExpectedNumberOfShareDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfShareDecimals").equalsIgnoreCase("")) {
				iExpectedNoOfShareDecimals = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals"));
			}
			boolean bValidStatus = true;
			String appendErrMsg = "";
			if(mapTransferDetails.get("ExpectedActualBalance") != null && mapTransferDetails.get("ExpectedActualShares") != null && mapTransferDetails.get("ExpectedAvailableAmount") != null && mapTransferDetails.get("ExpectedAvailableShares") != null )
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
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Actual Balance' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[4]//span"), actualBalance.get(i), "Yes", true, iExpectedNoOfDecimals);						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Actual Shares' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[5]//span"), actualShares.get(i), "Yes", true, iExpectedNoOfShareDecimals);						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Available Amount' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[6]//span"), availableAmount.get(i), "Yes", true, iExpectedNoOfDecimals);						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'Available Shares' at index '"+i+"' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[7]//span"), avaialableShares.get(i), "Yes", true, iExpectedNoOfShareDecimals);						
						if(!bStatus){
							appendErrMsg = appendErrMsg + Messages.errorMsg;
							bValidStatus = false;
						}
					}
				}
			}

			if(mapTransferDetails.get("ExpectedTotalActualBalance") != null && mapTransferDetails.get("ExpectedTotalActualShares") != null && mapTransferDetails.get("ExpectedTotalAvailableAmount") != null && mapTransferDetails.get("ExpectedTotalAvailableShares") != null){
				NewUICommonFunctions.scrollToView(By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr"));
				int lotsCountInclusiveOfTotalsRow = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr"));
				if(!mapTransferDetails.get("ExpectedTotalActualBalance").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '2' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[2]//span"), mapTransferDetails.get("ExpectedTotalActualBalance"), "Yes", true, iExpectedNoOfDecimals);						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalActualShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '3' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[3]//span"), mapTransferDetails.get("ExpectedTotalActualShares"), "Yes", true, iExpectedNoOfShareDecimals);						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalAvailableAmount").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '4' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[4]//span"), mapTransferDetails.get("ExpectedTotalAvailableAmount"), "Yes", true, iExpectedNoOfDecimals);						
					if(!bStatus){
						appendErrMsg = appendErrMsg + Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapTransferDetails.get("ExpectedTotalAvailableShares").equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer Available Balance Lot for 'TotalActualBalance' at Row : '"+lotsCountInclusiveOfTotalsRow+"' Col index '5' ", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+lotsCountInclusiveOfTotalsRow+"]//td[5]//span"), mapTransferDetails.get("ExpectedTotalAvailableShares"), "Yes", true, iExpectedNoOfShareDecimals);						
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
	public static boolean doMakerAndCheckerVerifyPendingTradesDetails(String sPTRequestedAmounts, String sPTRequestedShares, int iNoOfDecimalsExpected, int iNoOfShareDecimals){
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
			for (int i = 0; i <sAmountsList.size(); i++) {
				if(!sAmountsList.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Pending Trade' in Transfer screen for lot 'Amount' value at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr["+(i+1)+"]//td[7]"), sAmountsList.get(i), "Yes", true, iNoOfDecimalsExpected);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if(!sSharesList.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Pending Trade' in Transfer screen for lot 'Share' value at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr["+(i+1)+"]//td[8]"), sSharesList.get(i), "Yes", true, iNoOfShareDecimals);
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

	//----------------------------------------functions to VerifyviewButtons for Transfer----------------------------------------

	public static boolean verifyViewButtonsFunctionality(Map<String, String> mapTransferDetails){
		boolean bValidStatus= true;

		try{

			bStatus = doFillRequestDetails(mapTransferDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Request Details","Request Details cannot be filled. Error: "+Messages.errorMsg);
				return false;

			}
			bStatus = doFillFromInvestorDetails(mapTransferDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill From Investor Details","From Investor Details cannot be filled. Error: "+Messages.errorMsg);
				return false;

			}
			bStatus = doFillToInvestorDetails(mapTransferDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill To Investor Details","To Investor Details cannot be filled. Error: "+Messages.errorMsg);
				return false;
			}

			bStatus = doFillFundDetailsTransfer(mapTransferDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Fund Details","Fund Details cannot be filled. Error: "+Messages.errorMsg);
				return false;

			}

			bStatus = doFillTransferDetails(mapTransferDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Transfer Details","Transfer details filled. Error: "+Messages.errorMsg);
				return false;

			}

			if(mapTransferDetails.get("OperationNext")!=null){
				bStatus = doSubOperationsOnTransactionTrades("Transfer", mapTransferDetails.get("OperationNext"));
				if(!bStatus){
					return false;
				}
			}

			bStatus = verifyViewButtonForNoticePeriods(mapTransferDetails);
			if(!bStatus){
				bValidStatus =  false;
				Reporting.logResults("Fail","Verify Notice period Charge details","Verification Failed. Error: "+Messages.errorMsg);
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Notice Period Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'noticeCharges')]//following-sibling::button[normalize-space(text()='Close')]"));
				}

			}		

			if(mapTransferDetails.get("NPView")!= null && bStatus){
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Notice Period Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'noticeCharges')]//following-sibling::button[normalize-space(text()='Close')]"));
				}
				Reporting.logResults("Pass","Verify Notice period details","Notice period Charges verified successfully");
			}


			bStatus = verifyViewButtonForTransactionCharge(mapTransferDetails);
			if(!bStatus){
				bValidStatus =  false;
				Reporting.logResults("Fail","Verify Transaction period details","Verification Failed. Error: "+Messages.errorMsg);
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Transaction Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'transactionChanges')]//following-sibling::button[normalize-space(text()='Close')]"));
				}
			}
			if(bStatus && mapTransferDetails.get("TCEffDate")!=null){
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Transaction Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'transactionChanges')]//following-sibling::button[normalize-space(text()='Close')]"));
				}
				Reporting.logResults("Pass","Verify Transaction period details","Transaction period Charges verified successfully");
			}

			return bValidStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}


	public static boolean verifyViewButtonForNoticePeriods(Map<String, String> ViewMapDetails){
		try{
			String appendErrorMsg = " ";
			boolean bValidStatus = true ;

			if(ViewMapDetails.get("NPView")== null){
				return bValidStatus;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space(text())='Notice Charge Amount']"));
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space(text())='Notice Charge Amount']"));
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
			return bValidStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}


	public static boolean verifyViewButtonForTransactionCharge(Map<String, String> ViewMapDetails){
		try{
			String appendErrorMsg = " ";
			boolean bValidStatus = true;


			if(ViewMapDetails.get("TCEffDate")==null){
				return bValidStatus;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Fixed Tranaction Charge Amount']"));
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
				Messages.errorMsg = "Actual value Transaction Charge Type is "+sType+" which is not equal to expected value: "+ViewMapDetails.get("TCType");
				bValidStatus = false;
			}

			String sMethod = Elements.getText(Global.driver, By.xpath("//p[@id='transactionRateMethodView']"));
			if(sMethod!=null && !sMethod.equalsIgnoreCase(ViewMapDetails.get("TCRateMethod"))){
				appendErrorMsg = appendErrorMsg + "Actual value Rate Method type is "+sMethod+" which is not equal to expected value: "+ViewMapDetails.get("TCRateMethod");
				bValidStatus = false;
			}

			String CalculationType = Elements.getText(Global.driver, By.xpath("//p[@id='transactionCalculationBaseView']"));
			if(CalculationType!=null && !CalculationType.equalsIgnoreCase(ViewMapDetails.get("TCCalculationType"))){
				Messages.errorMsg = "Actual value Calculation Type is "+CalculationType+" which is not equal to expected value: "+ViewMapDetails.get("TCCalculationType");
				bValidStatus = false;
			}
			return bValidStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean chekerVerifyAvailableBalancAllocatedeAMountOrShare(Map<String ,String>mapTransferDetails){
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		List<String> sAmountOrSharesList = null;
		List<String> sVerifyAmountOrSharesList = null;
		List<String> sNoOfRecords = null;
		try {
			String sIsAmountOrShare = "";
			String verifyAmountOrShare = "";
			String columnNumber = "";
			String verifyColumnNumber = "";
			if (mapTransferDetails.get("TaxLotsAmounts") != null || mapTransferDetails.get("TaxLotsShares") != null) {
				if (mapTransferDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					columnNumber ="10";
					sAmountOrSharesList =  Arrays.asList(mapTransferDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShare = "Share";
					verifyColumnNumber ="11";

				}
				else if (mapTransferDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					columnNumber ="11";
					sAmountOrSharesList =  Arrays.asList(mapTransferDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShare = "Amount";
					verifyColumnNumber ="10";
				}
			}

			if(mapTransferDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList = Arrays.asList(mapTransferDetails.get("ExpectedTaxLotAmountOrShare").split(","));
			}

			if(mapTransferDetails.get("IsTaxLotsAmountOrShareEditable") != null){
				sNoOfRecords = Arrays.asList(mapTransferDetails.get("IsTaxLotsAmountOrShareEditable").split(","));
			}
			int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td[7]//span"));				
			//int iNoOfLots = Integer.parseInt(NoOfRecords);
			if (NoOfRecords != sNoOfRecords.size()) {
				Messages.errorMsg = "[ ERROR : Expected No lots : '"+sAmountOrSharesList.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
				return false;
			}
			if (mapTransferDetails.get("Full Transfer")!=null && mapTransferDetails.get("Full Transfer").equalsIgnoreCase("No")) {
				for (int i = 0; i < sAmountOrSharesList.size(); i++) {
					if(!sAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td["+columnNumber+"]//span"), sAmountOrSharesList.get(i), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
					if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot '"+verifyAmountOrShare+"' at index : '"+i+"'", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td["+verifyColumnNumber+"]//span"), sVerifyAmountOrSharesList.get(i), "Yes", true, Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")));
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}		

				}	
			}

			if (mapTransferDetails.get("Full Transfer")!=null && mapTransferDetails.get("Full Transfer").equalsIgnoreCase("Yes")) {
				bStatus = doVerifyFullTransferValuesInAvaialableBalance(mapTransferDetails);
				if(!bStatus){
					return false;
				}
			}

			Messages.errorMsg = sAppendErrorMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	private static boolean doVerifyFullTransferValuesInAvaialableBalance(Map<String, String> mapTransferDetails) {
		try {

			if(mapTransferDetails.get("ExcpectedAvailableAmount") != null && mapTransferDetails.get("ExcpectedAvailableShares") != null){
				List<String> allocatedAmount = Arrays.asList(mapTransferDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapTransferDetails.get("ExcpectedAvailableShares").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally";
					return false;

				}

				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td[7]//span"));
				if(allocatedAmount.size() != NoOfRecords){
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+allocatedAmount.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}

				int noOfDecimalsToDispaly = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals"));

				for(int i=0; i<allocatedAmount.size(); i++){
					if(!allocatedAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot 'Amount' at index : '"+i+"'",  By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[10]//span"), allocatedAmount.get(i), "Yes", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							return false;
						}
					}

					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot 'Share' at index : '"+i+"'",  By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[11]//span"), allocatedShares.get(i), "Yes", true, noOfDecimalsToDispaly);
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

	public static boolean doVerifyCheckerAllocatedAmountandShare(Map<String,String>mapTransferDetails){
		try {
			boolean bValidateStatus = true;
			if(mapTransferDetails.get("CheckerExpectedAllocatedAmount") != null && mapTransferDetails.get("CheckerExpectedAllocatedShare") != null){
				List<String> allocatedAmount = Arrays.asList(mapTransferDetails.get("CheckerExpectedAllocatedAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapTransferDetails.get("CheckerExpectedAllocatedShare").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally";
					bValidateStatus = false;

				}

				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td[7]//span"));
				if(allocatedAmount.size() != NoOfRecords){
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+allocatedAmount.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					bValidateStatus = false;
				}

				int noOfDecimalsToDispaly = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals"));
				int noOfShareDecimalsToDispaly = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals"));

				for(int i=0; i<allocatedAmount.size(); i++){
					if(!allocatedAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot 'Amount' at index : '"+i+"'",  By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[10]//span"), allocatedAmount.get(i), "Yes", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							bValidateStatus = false;
						}
					}

					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot 'Share' at index : '"+i+"'",  By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[11]//span"), allocatedShares.get(i), "Yes", true, noOfShareDecimalsToDispaly);
						if (!bStatus) {
							bValidateStatus = false;
						}
					}
				}

			}
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doMakerVerifyAvailableBalanceAllocatedeAmountOrShare(Map<String ,String>mapTransferDetails){
		int iNoOfDecimals1 = -1;
		int iNoOfDecimals2 = -1;
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		List<String> sAmountOrSharesList = null;
		List<String> sVerifyAmountOrSharesList = null;
		List<String> sNoOfRecords = null;
		try {
			String sIsAmountOrShare = "";
			String verifyAmountOrShare = "";
			String columnNumber = "";
			String verifyColumnNumber = "";
			if (mapTransferDetails.get("TaxLotsAmounts") != null || mapTransferDetails.get("TaxLotsShares") != null) {
				if (mapTransferDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					columnNumber = "11";
					sAmountOrSharesList =  Arrays.asList(mapTransferDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShare = "Share";
					verifyColumnNumber = "12";					
				}
				else if (mapTransferDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					columnNumber = "12";
					sAmountOrSharesList =  Arrays.asList(mapTransferDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShare = "Amount";
					verifyColumnNumber = "11";
				}
			}
			if(mapTransferDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList = Arrays.asList(mapTransferDetails.get("ExpectedTaxLotAmountOrShare").split(","));
			}
			if(mapTransferDetails.get("IsTaxLotsAmountOrShareEditable") != null){
				sNoOfRecords = Arrays.asList(mapTransferDetails.get("IsTaxLotsAmountOrShareEditable").split(","));
			}
			int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td[7]"));				
			//int iNoOfLots = Integer.parseInt(NoOfRecords);
			if(sNoOfRecords!=null){
				if (NoOfRecords != sNoOfRecords.size()) {
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+sAmountOrSharesList.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
			}

			if (mapTransferDetails.get("Full Transfer")!=null && mapTransferDetails.get("Full Transfer").equalsIgnoreCase("No")) {
				for (int i = 0; i < sAmountOrSharesList.size(); i++) {
					if(!sAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						if (sIsAmountOrShare.equalsIgnoreCase("amount") && mapTransferDetails.get("ExpectedNumberOfDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfDecimals").equalsIgnoreCase("")) {
							iNoOfDecimals1 = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals"));
						}
						if (sIsAmountOrShare.equalsIgnoreCase("share") && mapTransferDetails.get("ExpectedNumberOfShareDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfShareDecimals").equalsIgnoreCase("")) {
							iNoOfDecimals1 = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals"));
						}
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td["+columnNumber+"]//input"), sAmountOrSharesList.get(i), "No", true, iNoOfDecimals1);
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
					if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						if (verifyAmountOrShare.equalsIgnoreCase("Amount") && mapTransferDetails.get("ExpectedNumberOfDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfDecimals").equalsIgnoreCase("")) {
							iNoOfDecimals2 = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals"));
						}
						if (verifyAmountOrShare.equalsIgnoreCase("Share") && mapTransferDetails.get("ExpectedNumberOfShareDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfShareDecimals").equalsIgnoreCase("")) {
							iNoOfDecimals2 = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals"));
						}
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot '"+verifyAmountOrShare+"' at index : '"+i+"'", By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td["+verifyColumnNumber+"]//input"), sVerifyAmountOrSharesList.get(i), "No", true, iNoOfDecimals2);
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
				}	
			}
			if (mapTransferDetails.get("Full Transfer")!=null && mapTransferDetails.get("Full Transfer").equalsIgnoreCase("Yes")) {
				bStatus = doVerifyFullTransferMakerInAvaialableBalance(mapTransferDetails);
				if(!bStatus){
					return false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	private static boolean doVerifyFullTransferMakerInAvaialableBalance(Map<String, String> mapTransferDetails) {
		try {
			int iNoOfDecimals = -1;
			int iNoOfShareDecimals = -1;
			if (mapTransferDetails.get("ExpectedNumberOfDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfDecimals").equalsIgnoreCase("")) {
				iNoOfDecimals = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals"));
			}
			if (mapTransferDetails.get("ExpectedNumberOfShareDecimals") != null && !mapTransferDetails.get("ExpectedNumberOfShareDecimals").equalsIgnoreCase("")) {
				iNoOfShareDecimals = Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals"));
			}
			if(mapTransferDetails.get("ExpectedAvailableAmount") != null && mapTransferDetails.get("ExpectedAvailableShares") != null){
				List<String> allocatedAmount = Arrays.asList(mapTransferDetails.get("ExpectedAvailableAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapTransferDetails.get("ExpectedAvailableShares").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally";
					return false;
				}
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr//td[7]"));
				if(allocatedAmount.size() != NoOfRecords){
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+allocatedAmount.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				for(int i=0; i<allocatedAmount.size(); i++){
					if(!allocatedAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot 'Amount' at index : '"+i+"'",  By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[11]//input"), allocatedAmount.get(i), "No", true, iNoOfDecimals);
						if (!bStatus) {
							return false;
						}
					}
					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Transfer tax lot 'Share' at index : '"+i+"'",  By.xpath("//input[@id='taxSlotSize']//following-sibling::div//tbody//tr["+(i+1)+"]//td[12]//input"), allocatedShares.get(i), "No", true, iNoOfShareDecimals);
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

	/*************MAKER OPERATIONS ON CHECKER REVIEWED TRANSACTIONS***********/

	//Checker Reviewed Maker flow covering function.
	public static boolean doMakerFillCheckerReviewedTransferTrade(Map<String, String> mapTransferDetails) {
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Received Date Field is Not Visible";
				return false;
			}
			bStatus = doFillRequestDetails(mapTransferDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillFromInvestorDetails(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doFillToInvestorDetails(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doFillFundDetailsTransfer(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doFillTransferDetails(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			if(mapTransferDetails.get("OperationNext")!=null){

				bStatus = doSubOperationsOnTransactionTrades("Transfer", mapTransferDetails.get("OperationNext"));
				if(!bStatus){
					return false;
				}
			}
			bStatus = doMakerFillAvailableBalanceDetails(mapTransferDetails);			
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyAvailableBalanceAllocatedeAmountOrShare(mapTransferDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAvailableBalanceDetails(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			if(mapTransferDetails.get("ExpectedPendingRequestAmount")!=null && mapTransferDetails.get("ExpectedPendingRequestedShares")!=null && !mapTransferDetails.get("ExpectedPendingRequestAmount").contains("Records")){
				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapTransferDetails.get("ExpectedPendingRequestAmount"),mapTransferDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfDecimals")), Integer.parseInt(mapTransferDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					return false;
				}
			}
			bStatus = doVerifyChargeDetails(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			bStatus = doMakerVerifyAmountDetailsTab(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			bStatus = doMakerFillOtherDetailsTab(mapTransferDetails);			
			if(!bStatus){
				return false;
			}
			//Do perform Maker operation
			if(mapTransferDetails.get("OperationType")!=null){
				bStatus = doSubOperationsOnTransactionTrades("Transfer", mapTransferDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapTransferDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ERROR : Request Received Date Field is Not Visible Cancel Operation Failed ] \n";
						return false;
					}
					return true;
				}
				//Verify Exception At checker side
				if(mapTransferDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Proceed')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ERROR :Proceed Button is Not Avaliable ]\n";
						return false;
					}
					bStatus = verifyExceptionsatMaker(mapTransferDetails.get("ExpectedExceptionsAtMaker"));
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with  Expected :'"+mapTransferDetails.get("ExpectedExceptionsAtMaker")+"' ");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "[ERROR : Cannot close the Exceptions ]\n ";
							return false;
						}
						Messages.errorMsg = errorMsg;
						return false;
					}
					if(mapTransferDetails.get("OperationType").equalsIgnoreCase("Save") || mapTransferDetails.get("OperationType").contains("Review"))
					{
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Proceed')]"));
						if(!bStatus){
							Messages.errorMsg = "[ERROR : "+mapTransferDetails.get("OperationType")+" Button  Cannot be clicked ] \n";
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
							return false;
						}
					}
				}
				else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='excMasgModelForm']//button[contains(text(),'Proceed')]"), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions are Not matching");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "[ERROR : Exceptions are Visible Even there are No Expected Exception and Cannot close the Exceptions ]\n";
							return false;
						}
						Messages.errorMsg = "[ERROR : Exceptions are Visible Even there are No Expected Exception ]\n";
						return false;
					}
				}
				//Wait for Success message
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ERROR :Succesfull Message is Not visible ]\n";
					return false;
				}
			}
			return bStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getInvestorNameFromTheInvestorTestData(Map<String,String> mapTransferDetails){
		try {
			String sInvestorSheetName= "GeneralDetails";
			String sInvestorName = "";	
			if (mapTransferDetails.get("New Investor") != null && mapTransferDetails.get("New Investor").equalsIgnoreCase("Yes")) {
				if (mapTransferDetails.get("New Investor TestCaseName") != null) {
					String sInvestorFirstName = "";
					String sInvestorLastName = "";
					String sInvestorMiddleName = "";

					Map<String, String> mapInvestorGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, sInvestorSheetName , mapTransferDetails.get("New Investor TestCaseName")); 
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
						mapTransferDetails.put("To Investor Name",sInvestorName);
					}	
				}
			}


			return sInvestorName;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String getHolderNameFromHolderTestData(Map<String,String>mapTransferDetails){
		try {
			String sHolderSheetName = "GeneralDetails";
			String sHolderName = "";
			//creation of New Holder
			if (mapTransferDetails.get("New Holder") != null && mapTransferDetails.get("New Holder").equalsIgnoreCase("Yes")) {
				if(mapTransferDetails.get("New Holder")!=null && mapTransferDetails.get("New Holder TestCaseName")!=null)
				{
					Map<String, String> mapHolderGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, sHolderSheetName , mapTransferDetails.get("New Holder TestCaseName"));

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
						mapTransferDetails.put("Holder Name",sHolderName);
					}
				}

			}

			return sHolderName;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
}