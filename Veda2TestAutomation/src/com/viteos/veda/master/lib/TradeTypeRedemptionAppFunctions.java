package com.viteos.veda.master.lib;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.checkerActionTypes;

public class TradeTypeRedemptionAppFunctions {
	public static boolean bStatus = true;
	public static Map<String , String > mapOverrideRedemptionDetails = new HashMap<>();
	public static boolean isRedemptionForCheckerReviewedCase = false;
	public static boolean doFillRedemptionTrade(Map<String ,String> mapRedemptionDetails){
		try {

			bStatus = doFillRequestDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillInvestorDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillFundDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFIllRedemptionDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			//Click Proceed for Next Screen

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//button[contains(@onclick,'javascript:getRedDetails') and contains(normalize-space(),'Proceed')]"));
			if(!bStatus){
				Messages.errorMsg = "Cannot scroll to Proceed button";
				return false;
			}
			bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:getRedDetails') and contains(normalize-space(),'Proceed')]"));
			if(!bStatus){
				Messages.errorMsg = "Cannot be clicked on Proceed button in First screen of Redemption";
				return false;
			}



			bStatus = doMakerVerifyRequestDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyInvestorDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyFundDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyRedemptionDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillAvailableBalanceAtMaker(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doVerifyAvailableBalanceDetailsAtMakerAndChecker(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			if (mapRedemptionDetails.get("ExpectedPendingRequestAmount") != null && mapRedemptionDetails.get("ExpectedPendingRequestedShares") != null && mapRedemptionDetails.get("ExpectedNumberOfDecimals") != null) {
				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapRedemptionDetails.get("ExpectedPendingRequestAmount"), mapRedemptionDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
				if (!bStatus) {
					return false;
				}
			}	


			bStatus = doOverrideOrVerifyChargeDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyAmountDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			if(mapRedemptionDetails.get("OperationType")!=null){
				bStatus = doSubOperationsOnTransactionTrades("Redemption", mapRedemptionDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapRedemptionDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'saveRedemptionFromPopup') and contains(normalize-space(),'Proceed for Approval')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Exceptions Popup is not visible even there is Expected Exceptions";
						return false;
					}
					bStatus = doVerifyMakerExceptions(mapRedemptionDetails.get("ExpectedExceptionsAtMaker"));
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with Expected");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "Cannot close the Exceptions ";
							return false;
						}
						Messages.errorMsg = errorMsg;
						return false;
					}
					if(mapRedemptionDetails.get("OperationType").equalsIgnoreCase("Save") || mapRedemptionDetails.get("OperationType").contains("Review"))
					{

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'saveRedemptionFromPopup') and contains(normalize-space(),'Proceed for Approval')]"));
						if(!bStatus){
							Messages.errorMsg = "Proceed Button Cannot be clicked";
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Close')]"));
							return false;
						}

					}

				}/*else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'saveRedemptionFromPopup') and contains(normalize-space(),'Proceed for Approval')]"), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions are Not matching");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception and Cannot close the Exceptions ";
							return false;
						}
						Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception ";
						return false;
					}

				}*/
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'saveRedemptionFromPopup') and contains(normalize-space(),'Proceed for Approval')]"), Constants.lTimeOut);
				if(bStatus){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'saveRedemptionFromPopup') and contains(normalize-space(),'Proceed for Approval')]"));
					if(!bStatus){
						Messages.errorMsg = "Proceed Button Cannot be clicked";
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Close')]"));
						return false;
					}
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Succesfull Message is Not visible";
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

	public static boolean doFillCheckerReviewdTransactios(Map<String ,String> mapRedemptionDetails){

		try {

			bStatus = doFillRequestDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillInvestorDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillFundDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFIllRedemptionDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			//Click Proceed for Next Screen

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//button[contains(@onclick,'javascript:getRedDetails') and contains(normalize-space(),'Apply')]"));
			if(!bStatus){
				Messages.errorMsg = "Cannot scroll to Apply button";
				return false;
			}
			bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:getRedDetails') and contains(normalize-space(),'Apply')]"));
			if(!bStatus){
				Messages.errorMsg = "Cannot be clicked on Apply button in First screen of Redemption";
				return false;
			}

			bStatus = doFillAvailableBalanceAtMaker(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doVerifyAvailableBalanceDetailsAtMakerAndChecker(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			if (mapRedemptionDetails.get("ExpectedPendingRequestAmount") != null && !mapRedemptionDetails.get("ExpectedPendingRequestAmount").contains("Records") && mapRedemptionDetails.get("ExpectedPendingRequestedShares") != null && mapRedemptionDetails.get("ExpectedNumberOfDecimals") != null) {
				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapRedemptionDetails.get("ExpectedPendingRequestAmount"), mapRedemptionDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
				if (!bStatus) {
					return false;
				}
			}	

			bStatus = doOverrideOrVerifyChargeDetails(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyAmountDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			if(mapRedemptionDetails.get("OperationType")!=null){
				bStatus = doSubOperationsOnTransactionTradesforCheckerReviewd("Redemption", mapRedemptionDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapRedemptionDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'saveRedemptionFromPopup') and contains(normalize-space(),'Proceed for Approval')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Proceed for Approval Button is Not Visible";
						return false;
					}
					bStatus = doVerifyMakerExceptions(mapRedemptionDetails.get("ExpectedExceptionsAtMaker"));
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with Expected");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "Cannot close the Exceptions ";
							return false;
						}
						Messages.errorMsg = errorMsg;
						return false;
					}
					if(mapRedemptionDetails.get("OperationType").equalsIgnoreCase("Save") || mapRedemptionDetails.get("OperationType").contains("Review"))
					{

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'saveRedemptionFromPopup') and contains(normalize-space(),'Proceed for Approval')]"));
						if(!bStatus){
							Messages.errorMsg = "Proceed Button Cannot be clicked";
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Close')]"));
							return false;
						}

					}

				}else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'saveRedemptionFromPopup') and contains(normalize-space(),'Proceed for Approval')]"), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions are Not matching");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception and Cannot close the Exceptions ";
							return false;
						}
						Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception ";
						return false;
					}

				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Succesfull Message is Not visible";
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


	private static boolean doSubOperationsOnTransactionTradesforCheckerReviewd(String master, String operation) {
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//button[contains(@onclick,'javascript:submit') and contains(normalize-space(),'Save')]") , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Save Button is Not Visible";
				return false;
			}
			if(operation.equalsIgnoreCase("Save")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submit') and contains(normalize-space(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Save button cannot be clicked";
					return false;
				}
				return true;

			}

			if(operation.contains("Cancel")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:cancelDashboard') and contains(normalize-space(),'Cancel')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Send For Review button cannot be clicked";
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

	public static boolean doVerifyRedemptionInCheckerScreen(Map<String , String> mapRedemptionDetails){
		try {
			String appendErrorMsg = "";
			boolean validStatus = true;

			bStatus = doCheckerVerifyRequestDetails(mapRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}

			bStatus = doCheckerVerifyInvestorDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}

			bStatus = doCheckerVerifyFundDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}

			bStatus = doCheckerVerifyRedemptionDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}

			/*mapOverrideRedemptionDetails = dochangeOrderOfTableData(mapRedemptionDetails);
			if(mapOverrideRedemptionDetails == null || mapOverrideRedemptionDetails.isEmpty()){
				appendErrorMsg = appendErrorMsg+"Available balance values Order is Not changed to verify";
				validStatus = false;
			}*/

			/*if(mapRedemptionDetails == null || mapRedemptionDetails.isEmpty()){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}*/
			/*bStatus = chekerVerifyAvailableBalancAllocatedeAMountOrShare(mapOverrideRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}*/
			bStatus = doVerifyAllocatedDetailsInTaxlots(mapRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}
			
			bStatus = doVerifyAvailableBalanceDetailsAtMakerAndChecker(mapRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}

			if (mapRedemptionDetails.get("ExpectedPendingRequestAmount") != null&& !mapRedemptionDetails.get("ExpectedPendingRequestAmount").contains("Records") && mapRedemptionDetails.get("ExpectedPendingRequestedShares") != null && mapRedemptionDetails.get("ExpectedNumberOfDecimals") != null) {
				bStatus = doMakerAndCheckerVerifyPendingTradesDetails(mapRedemptionDetails.get("ExpectedPendingRequestAmount"), mapRedemptionDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
				if (!bStatus) {
					appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
					validStatus = false;
				}
			}	

			bStatus = doVerifyChargeDetailsinChecker(mapRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}

			bStatus = doCheckerVerifyAmountDetailsTab(mapRedemptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				validStatus = false;
			}

			if(mapRedemptionDetails.get("ExpectedExceptionsAtChecker")!=null){
				bStatus =doCheckerVerifyExceptionsAtCheckerForTradeTypeRedemption(mapRedemptionDetails.get("ExpectedExceptionsAtChecker"));
				if(!bStatus){
					appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
					validStatus = false;
				}				
			}		
			Messages.errorMsg = appendErrorMsg;					
			return validStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	//function for Subscription CheckerActionTypes
	public static boolean doCheckerActionTypesOnTradeRedemption(Map<String, String> subscriptionTradingMapDetails)
	{
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Detail')]"),Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not visible for checker operations";
				return false;
			}

			
			//download OA and Delete
			if(subscriptionTradingMapDetails.get("isOAClient") != null){
				bStatus = TradeTypeSubscriptionAppFunctions.verifyOrderAcknowledgementDownload(subscriptionTradingMapDetails);
				if(!bStatus && subscriptionTradingMapDetails.get("isOAClient").equalsIgnoreCase("Yes")){
					return false;
				}
				if(bStatus && subscriptionTradingMapDetails.get("isOAClient").equalsIgnoreCase("No")){
					Messages.errorMsg = "Order Acknowldegement Downloaded for Non OA Client";
					return false;
				}
			}
			

			if(subscriptionTradingMapDetails.get("Approve OA")!=null && subscriptionTradingMapDetails.get("Approve OA").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-approveOANo']"));
				if(!bStatus){
					Messages.errorMsg = "Page cannot scroll to Approve OA button";
					return false;

				}					
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

			bStatus = changeOverrideStatus(subscriptionTradingMapDetails);
			if(!bStatus){
				return false;
			}


			bStatus = performCheckerOperationOnTrade(subscriptionTradingMapDetails.get("CheckerOperations"));
			if(!bStatus){
				return false;
			}


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}


	public static boolean performCheckerOperationOnTrade(String OperationType) {
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
					Messages.errorMsg = "Approved Successfully but success message Not Displayed ";
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
					Messages.errorMsg = " Rejected Successfully Message Not Displayed ";
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
					Messages.errorMsg = " Returned Successfully Message Not Displayed ";
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
					Messages.errorMsg = " Reviewed Successfully Message Not Displayed ";
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


	//Coolapse button
	public static boolean doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab(String sYesForExpandNoCollapse, String sCollapsableTabIndexNumber){
		try {
			boolean bExpandStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='expand']"), 1);
			boolean bCollpaseStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='collapse']"), 1);
			if (bExpandStatus == true && bCollpaseStatus == false && sYesForExpandNoCollapse.equalsIgnoreCase("Yes")) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='expand']"));
				if (!bStatus) {					
					return false;
				}
				Thread.sleep(1000);
				return true;
			}
			if (bExpandStatus == false && bCollpaseStatus == true && sYesForExpandNoCollapse.equalsIgnoreCase("No")) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='collapse']"));
				if (!bStatus) {
					return false;
				}
				Thread.sleep(2000);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Maker Second Tab Request Details verify function.
	public static boolean doMakerVerifyRequestDetailsTab(Map<String, String> mapRedemptionDetails){
		try {
			Thread.sleep(3000);
			boolean bValidStatus = true;
			String appendErrMsg = "";
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Redemptio')]"));
			if(!bStatus){
				return false;
			}
			bStatus = doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "1");
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Request Details' Tab.]\n";
				return false;
			}
			if (mapRedemptionDetails.get("Request Received Date") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.id("requestDate"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapRedemptionDetails.get("Request Received Date"))) {
					appendErrMsg = appendErrMsg+"[ERROR : Actual Request Recieved Date : "+sValue+", is not matching with expected : "+mapRedemptionDetails.get("Request Received Date")+"] \n";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Request Received Time") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[contains(@id,'requestTime')]"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapRedemptionDetails.get("Request Received Time"))) {
					appendErrMsg = appendErrMsg+"[ERROR : Actual Request Received Time : "+sValue+", is not matching with expected : "+mapRedemptionDetails.get("Request Received Time")+"] \n";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Order Received Office") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[contains(@id,'orderReceivedOffice')]"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapRedemptionDetails.get("Order Received Office"))) {
					appendErrMsg = appendErrMsg+"[ERROR : Actual Order Received Office : "+sValue+", is not matching with expected : "+mapRedemptionDetails.get("Order Received Office")+"] \n";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Time Zone") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.name("timeZone"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapRedemptionDetails.get("Time Zone"))) {
					appendErrMsg = appendErrMsg+"[ERROR : Actual Time Zone : "+sValue+", is not matching with expected : "+mapRedemptionDetails.get("Time Zone")+"] \n";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Mode of Request") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Mode of Request", By.xpath("//div[contains(@id,'modeOfRequest')]//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Mode of Request"), "Yes", false)) {
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("External ID") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("External ID", By.xpath("//input[contains(@id,'externalId')]"), mapRedemptionDetails.get("External ID"), "No", false)) {
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}
			Messages.errorMsg = appendErrMsg;
			return bValidStatus;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	//Maker Second Tab Investor Details verify function.
	public static boolean doMakerVerifyInvestorDetailsTab(Map<String, String> mapRedemptionDetails){
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "2");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Investor Details' Tab.]\n";
				return false;
			}
			if (mapRedemptionDetails.get("Investor Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Investor Name", By.xpath("//div[@id='s2id_investorName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Investor Name"), "Yes", false)) {
					return false;
				}
			}
			if (mapRedemptionDetails.get("Holder Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Holder Name", By.xpath("//div[@id='s2id_holderNameDropdown']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Holder Name"), "Yes", false)) {
					return false;
				}
			}
			if (mapRedemptionDetails.get("Account ID") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Account ID", By.xpath("//div[@id='s2id_accountNameDropDown']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Account ID"), "Yes", false)) {
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Maker Second Tab Fund Details verify function.
	public static boolean doMakerVerifyFundDetailsTab(Map<String, String> mapRedemptionDetails){
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "3");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Fund Details' Tab.]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
			if(!bStatus){
				Messages.errorMsg = "Spinner is still Available after Maximizing Fund Details in second screen";
				return true;
			}
			if (mapRedemptionDetails.get("Client Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Client Name", By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Client Name"), "Yes", false)) {
					return false;
				}
			}
			if (mapRedemptionDetails.get("Fund Family Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Fund Family Name", By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Fund Family Name"), "Yes", false)) {
					return false;
				}
			}
			if (mapRedemptionDetails.get("Legal Entity Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Legal Entity Name", By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Legal Entity Name"), "Yes", false)) {
					return false;
				}
			}
			if (mapRedemptionDetails.get("Class Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Class Name", By.xpath("//div[@id='s2id_cmbClassName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Class Name"), "Yes", false)) {
					return false;
				}
			}
			if (mapRedemptionDetails.get("Series Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Series Name", By.xpath("//div[@id='s2id_cmbSeriesName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Series Name"), "Yes", false)) {
					return false;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

	//Maker Second Tab Redemption Details verify function.
	public static boolean doMakerVerifyRedemptionDetailsTab(Map<String, String> mapRedemptionDetails){
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "4");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Redemption Details' Tab.]\n";
				return false;
			}			
			if (mapRedemptionDetails.get("Full Redemption") != null) {
				if (mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-isTotalYes']//span[@class='checked']"), 2);
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : Full Redemption wasn't marked to 'Yes'.]\n";
						return false;
					}
					if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Percentage", By.xpath("//input[@id='percentage']"), "100", "No", true)) {
						return false;
					}
				}
				if (mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-isTotalNo']//span[@class='checked']"), 2);
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : Full Redemption wasn't marked to 'No'.]\n";
						return false;
					}
				}
			}
			if (mapRedemptionDetails.get("AmountorShares") != null) {
				if (mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Amount")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-rbAmount']//span[@class='checked']"), 2);
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : AmountorShares wasn't marked to 'Amount'.]\n";
						return false;
					}
					if (mapRedemptionDetails.get("Redemption Amount") != null) {					
						if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Redemption Amount", By.id("amountValue"), mapRedemptionDetails.get("Redemption Amount"), "No", true)) {
							return false;
						}
					}
				}
				if (mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Shares")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-rbShares']//span[@class='checked']"), 2);
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : AmountorShares wasn't marked to 'Shares'.]\n";
						return false;
					}
					if (mapRedemptionDetails.get("Share Value") != null) {					
						if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Share Value", By.id("unitValue"), mapRedemptionDetails.get("Share Value"), "No", true)) {
							return false;
						}
					}
				}
			}
			if (mapRedemptionDetails.get("Effective Date") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Effective Date", By.xpath("//input[@id='effectiveDate']"), mapRedemptionDetails.get("Effective Date"), "No", false)) {
					return false;
				}
			}
			if (mapRedemptionDetails.get("Percentage") != null) {
				bStatus=NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Percentage", By.xpath("//input[@id='percentage']"), mapRedemptionDetails.get("Percentage"), "No", true, -1);
				if (!bStatus) {
					return false;
				}
			}			
			if (mapRedemptionDetails.get("Expected NAV Date") != null) {				
				String sValue = Elements.getText(Global.driver, By.xpath("//input[contains(@id,'navDate')]//parent::td"));
				if (sValue == null || sValue.equalsIgnoreCase("") || !TradeTypeSubscriptionAppFunctions.formatDate(sValue.trim()).equalsIgnoreCase(mapRedemptionDetails.get("Expected NAV Date"))) {
					return false;
				}
				/*if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("NAV Date", By.xpath("//input[contains(@id,'navDate')]"), mapRedemptionDetails.get("Expected NAV Date"), "No", false)) {
					return false;
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Maker Second Tab Available Balance Details verify function.
	public static boolean doMakerVerifyAvailableBalanceDetailsTab(Map<String, String> mapRedemptionDetails){
		try {
			if (mapRedemptionDetails.get("Redemption Method Applied") != null) {
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Redemption Method Applied", By.xpath("//div[@id='s2id_redmethodApplied']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Redemption Method Applied"), "Yes", false)) {
					return false;
				}
			}
			if (mapRedemptionDetails.get("Lockup End Date") != null) {				
				String sValue = Elements.getText(Global.driver, By.xpath("//input[contains(@id,'lockupEndDate')]//parent::td"));
				if (sValue == null || sValue.equalsIgnoreCase("") || !TradeTypeSubscriptionAppFunctions.formatDate(sValue.trim()).equalsIgnoreCase(mapRedemptionDetails.get("Lockup End Date"))) {
					return false;
				}
				/*if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Lockup End Date", By.xpath("//input[contains(@id,'lockupEndDate')]"), mapRedemptionDetails.get("Lockup End Date"), "No", false)) {
					return false;
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Maker Second Tab Amount Details verify function.
	public static boolean doMakerVerifyAmountDetailsTab(Map<String, String> mapRedemptionDetails){
		String appendErrMsg = "";
		boolean bValidStatus = true;
		try {
			if (mapRedemptionDetails.get("ExpectedGrossAmount") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("AmountDetailsTab 'Gross Amount'", By.xpath("//input[contains(@id,'grossAmount')]"), mapRedemptionDetails.get("ExpectedGrossAmount"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedGrossCharges") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("AmountDetailsTab 'Charges'", By.xpath("//input[@id='chargesView']"), mapRedemptionDetails.get("ExpectedGrossCharges"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedPenaltyForAmountDetails") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("AmountDetailsTab 'Penalty Amount'", By.xpath("//input[@id='penaltyAmountView']"), mapRedemptionDetails.get("ExpectedPenaltyForAmountDetails"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedAmountPayable") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("AmountDetailsTab 'Amount Payable'", By.xpath("//input[@id='payableAmountView']"), mapRedemptionDetails.get("ExpectedAmountPayable"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedShareInAmountDetails") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("AmountDetailsTab 'Share'", By.xpath("//input[@id='unitsView']"), mapRedemptionDetails.get("ExpectedShareInAmountDetails"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
				if (!bStatus) {
					appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = appendErrMsg;
		return bValidStatus;
	}

	//Checker Request Details verify function.
	public static boolean doCheckerVerifyRequestDetails(Map<String, String> mapRedemptionDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			//Map<String, String> mapXMLDetails = XMLLibrary.getCreatedMasterDataFromXML("XMLMessages//TradeTypeREDDetails02-23-2016235608.xml", "TradeTypeRED", mapRedemptionDetails.get("TestCaseName"));
			Map<String, String> mapXMLDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "TradeTypeRED", mapRedemptionDetails.get("TestCaseName"));
			if (mapXMLDetails == null || mapXMLDetails.isEmpty()) {
				Messages.errorMsg = "[ ERROR : XML Map is emty no record was found to be created.] \n";
				return false;
			}
			bStatus = doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "1");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Request Details' Tab.]\n";
				return false;
			}
			if (mapXMLDetails.get("TransactionID") != null) {
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Order ID", By.xpath("//input[contains(@id,'orderRedemptionSysId')]"), mapXMLDetails.get("TransactionID"), "No", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Request Received Date") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[contains(@id,'requestDate')]"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapRedemptionDetails.get("Request Received Date"))) {
					sAppendErrorMsg = sAppendErrorMsg +"[ERROR : Actual Request Recieved Date : "+sValue+", is not matching with expected : "+mapRedemptionDetails.get("Request Received Date")+"] \n";
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Request Received Time") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[contains(text(),'Received Time')]//following-sibling::div//input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapRedemptionDetails.get("Request Received Time"))) {
					sAppendErrorMsg = sAppendErrorMsg +"[ERROR : Actual Request Received Time : "+sValue+", is not matching with expected : "+mapRedemptionDetails.get("Request Received Time")+"] \n";
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Order Received Office") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[contains(@id,'orderReceivedOffice')]"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapRedemptionDetails.get("Order Received Office"))) {
					sAppendErrorMsg = sAppendErrorMsg +"[ERROR : Actual Order Received Office : "+sValue+", is not matching with expected : "+mapRedemptionDetails.get("Order Received Office")+"] \n";
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Time Zone") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.name("timeZone"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapRedemptionDetails.get("Time Zone"))) {
					sAppendErrorMsg = sAppendErrorMsg +"[ERROR : Actual Time Zone : "+sValue+", is not matching with expected : "+mapRedemptionDetails.get("Time Zone")+"] \n";
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Mode of Request") != null) {

				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Mode of Request", By.xpath("//select[contains(@id,'modeOfRequest')]//option[@selected='selected']"), mapRedemptionDetails.get("Mode of Request"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("External ID") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("External ID", By.xpath("//input[contains(@id,'externalId')]"), mapRedemptionDetails.get("External ID"), "No", false)) {
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

	//Checker Investor Details verify function.
	public static boolean doCheckerVerifyInvestorDetailsTab(Map<String, String> mapRedemptionDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "2");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Investor Details' Tab.]\n";
				return false;
			}
			if (mapRedemptionDetails.get("Investor Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Investor Name", By.xpath("//input[contains(@name,'investorName')]"), mapRedemptionDetails.get("Investor Name"), "No", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Holder Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Holder Name", By.xpath("//input[contains(@name,'holderName')]"), mapRedemptionDetails.get("Holder Name"), "No", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Account ID") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Account ID", By.xpath("//input[contains(@name,'accountSysId')]"), mapRedemptionDetails.get("Account ID"), "No", false)) {
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

	//Checker Fund Details verify function.
	public static boolean doCheckerVerifyFundDetailsTab(Map<String, String> mapRedemptionDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "3");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Fund Details' Tab.]\n";
				return false;
			}
			if (mapRedemptionDetails.get("Client Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Client Name", By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Client Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Fund Family Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Fund Family Name", By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Fund Family Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Legal Entity Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Legal Entity Name", By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Legal Entity Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Class Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Class Name", By.xpath("//div[@id='s2id_cmbClassName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Class Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Series Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Series Name", By.xpath("//div[@id='s2id_cmbSeriesName']//span[contains(@id,'select2-chosen')]"), mapRedemptionDetails.get("Series Name"), "Yes", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return bValidateStatus;
	}

	//Checker Redemption Details verify function.
	public static boolean doCheckerVerifyRedemptionDetailsTab(Map<String, String> mapRedemptionDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "4");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Redemption Details' Tab.]\n";
				return false;
			}			
			if (mapRedemptionDetails.get("Full Redemption") != null) {
				if (mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-isTotalYes']//span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + "[ERROR : Full Redemption wasn't marked to 'Yes'.]\n";
						bValidateStatus = false;
					}
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Percentage", By.xpath("//input[contains(@id,'percentage')]"), "100", "No", true, -1);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-isTotalNo']//span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + "[ERROR : Full Redemption wasn't marked to 'No'.]\n";
						bValidateStatus = false;
					}
				}
			}
			if (mapRedemptionDetails.get("AmountorShares") != null) {
				if (mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Amount")) {
					//bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-rbAmount']//span[@class='checked']"), 2);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Amount']//input//parent::span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg +"[ERROR : AmountorShares wasn't marked to 'Amount'.]\n";
						bValidateStatus = false;
					}
					if (mapRedemptionDetails.get("Redemption Amount") != null) {					
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption Amount", By.id("amountValue"), mapRedemptionDetails.get("Redemption Amount"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
				}
				if (mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Shares")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Shares']//input//parent::span[@class='checked']"), 2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg +"[ERROR : AmountorShares wasn't marked to 'Shares'.]\n";
						bValidateStatus = false;
					}
					if (mapRedemptionDetails.get("Share Value") != null) {					
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share Value", By.id("unitValue"), mapRedemptionDetails.get("Share Value"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
				}
			}
			if (mapRedemptionDetails.get("Effective Date") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Effective Date", By.xpath("//input[contains(@id,'effectiveDate')]"), mapRedemptionDetails.get("Effective Date"), "No", false)) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("Percentage") != null) {				
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Percentage", By.xpath("//input[contains(@id,'percentage')]"), mapRedemptionDetails.get("Percentage"), "No", true, -1);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
				if (mapRedemptionDetails.get("Redemption Amount") != null) {					
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption Amount", By.id("amountValue"), mapRedemptionDetails.get("Redemption Amount"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
			}
			if (mapRedemptionDetails.get("Expected NAV Date") != null) {				
				String sValue = Elements.getText(Global.driver, By.xpath("//input[contains(@id,'navDate')]//parent::td"));
				if (sValue == null || sValue.equalsIgnoreCase("") || !TradeTypeSubscriptionAppFunctions.formatDate(sValue.trim()).equalsIgnoreCase(mapRedemptionDetails.get("Expected NAV Date"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual NAV Date : "+sValue+" ,isn't matching with expected : "+mapRedemptionDetails.get("Expected NAV Date")+"]";
					bValidateStatus = false;
				}
				/*if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("NAV Date", By.xpath("//input[contains(@id,'navDate')]"), mapRedemptionDetails.get("Expected NAV Date"), "No", false)) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual NAV Date : "+sValue+" ,isn't matching with expected : "+mapRedemptionDetails.get("Expected NAV Date")+"]";
					bValidateStatus = false;
				}*/
			}
			Messages.errorMsg = sAppendErrorMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	//Checker Amount Details verify function.
	public static boolean doCheckerVerifyAmountDetailsTab(Map<String, String> mapRedemptionDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (mapRedemptionDetails.get("ExpectedGrossAmount") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Gross Amount", By.xpath("//input[contains(@id,'grossAmount')]"), mapRedemptionDetails.get("ExpectedGrossAmount"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedGrossCharges") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Charges", By.xpath("//input[@id='chargesView']"), mapRedemptionDetails.get("ExpectedGrossCharges"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedPenaltyForAmountDetails") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Penalty Amount", By.xpath("//input[@id='penaltyAmountView']"), mapRedemptionDetails.get("ExpectedPenaltyForAmountDetails"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedAmountPayable") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Amount Payable", By.xpath("//input[@id='payableAmountView']"), mapRedemptionDetails.get("ExpectedAmountPayable"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedShareInAmountDetails") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share", By.xpath("//input[@id='unitsView']"), mapRedemptionDetails.get("ExpectedShareInAmountDetails"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
				if (!bStatus) {
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

	//Checker Exceptions Details verify function.
	public static boolean doCheckerVerifyExceptionsAtCheckerForTradeTypeRedemption(String sExceptions){		
		try {	
			boolean isExceptionsMismatch = false;
			List<String> sActualExceptions = new ArrayList<String>();
			if (sExceptions != null && !sExceptions.equalsIgnoreCase("")) {
				List<String> sExceptionList = Arrays.asList(sExceptions.split(","));

				//int iNoOfExceptions = Elements.getXpathCount(Global.driver, By.xpath("//input[contains(@id,'isException') and @value='1']//preceding-sibling::div"));
				int iNoOfExceptions = Global.driver.findElements(By.xpath("//input[contains(@id,'isException') and @value='1']")).size();

				if (sExceptionList.size() != iNoOfExceptions) {
					isExceptionsMismatch = true;
				}
				for (int i = 1; i <= iNoOfExceptions; i++) {
					String sExceptionText = Elements.getText(Global.driver, By.xpath("//strong[contains(text(),'Exception')]//parent::h4//following-sibling::div[@class='form-group']["+i+"]"));
					sActualExceptions.add(sExceptionText);
					if (sExceptionText != null && !sExceptions.contains(sExceptionText.trim())) {
						isExceptionsMismatch = true;
					}
				}
				if (isExceptionsMismatch == true) {
					Messages.errorMsg = "[ ERROR : No Of Exceptions expected : "+sExceptionList.size()+" i.e.["+sExceptions+"] : is not matching with Actual i.e.: count "+iNoOfExceptions+"; Exceptions : ["+sActualExceptions+"]]";
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Checker Override Details verify function.
	public static boolean doCheckerVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription(String sOverridedChargeName, String isAllowToOverride){
		try {
			String sChargeTypeNameSubString = "";
			if (sOverridedChargeName != null) {
				if (sOverridedChargeName.toLowerCase().contains("notice")) {
					sChargeTypeNameSubString = "Notice";
				}
				if (sOverridedChargeName.toLowerCase().contains("penalty")) {
					sChargeTypeNameSubString = "Lockup";
				}
				if (sOverridedChargeName.toLowerCase().contains("transaction")) {
					sChargeTypeNameSubString = "Transaction";
				}
				/*if (sOverridedChargeName.toLowerCase().contains("incentive")) {
					sChargeTypeNameSubString = "Incentive";
				}*/				
			}
			if (sChargeTypeNameSubString != null && !sChargeTypeNameSubString.equalsIgnoreCase("")) 
			{				
				/*bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//strong[contains(text(),'Override')]//..//following-sibling::div//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//input[contains(@id,'No')]//parent::span[@class='checked']"), 3);
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Overrided '"+sChargeTypeNameSubString+"' isn't marked to 'No' in Checker verification.] \n";
					return false;
				}*/
				if (isAllowToOverride != null && isAllowToOverride.equalsIgnoreCase("No")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//strong[contains(text(),'Override')]//..//following-sibling::div//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//input[contains(@id,'No')]//parent::span"));
					if (!bStatus) {
						Messages.errorMsg = Messages.errorMsg +"[ ERROR : Unable to change the status of Overrided chanrges of "+sChargeTypeNameSubString+" to 'No'.] \n";
						return false;
					}
				}
				if (isAllowToOverride != null && isAllowToOverride.equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//strong[contains(text(),'Override')]//..//following-sibling::div//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//input[contains(@id,'Yes')]//parent::span"));
					if (!bStatus) {
						Messages.errorMsg = Messages.errorMsg +"[ ERROR : Unable to change the status of Overrided chanrges of "+sChargeTypeNameSubString+" to 'Yes'.] \n";
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

	//Maker Request Details fill function.
	public static boolean doFillRequestDetails(Map<String, String> mapRedemptionDetails) {
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Request Details']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Details are Not visible";
				return false;
			}


			if(mapRedemptionDetails.get("Request Received Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Received Date']/following::div/input[contains(@id,'requestDate')]"), mapRedemptionDetails.get("Request Received Date"));
				if(!bStatus){
					Messages.errorMsg = "Request Received Date Not Entered";
					return false;
				}
				Elements.click(Global.driver, By.xpath("//h4/p"));
			}

			if(mapRedemptionDetails.get("Request Received Time")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Received Time']/following::div/input[contains(@id,'requestTime')]"), mapRedemptionDetails.get("Request Received Time"));
				if(!bStatus){
					Messages.errorMsg = "Request Received Time Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
				if(!bStatus){
					Messages.errorMsg = "Failed to click on subscription Label";
					return false;
				}
			}
			if(mapRedemptionDetails.get("Order Received Office")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Received Office']/following::div/input[contains(@id,'ReceivedOffice')]"), mapRedemptionDetails.get("Order Received Office"));
				if(!bStatus){
					Messages.errorMsg = "Order Received Office Not Entered";
					return false;
				}
			}
			if(mapRedemptionDetails.get("Time Zone")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@placeholder='Time Zone']"), mapRedemptionDetails.get("Time Zone"));
				if(!bStatus){
					Messages.errorMsg = "Time Zone Not Entered";
					return false;
				}

			}
			if(mapRedemptionDetails.get("Mode of Request")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapRedemptionDetails.get("Mode of Request"), By.xpath("//label[normalize-space()='Mode of Request']/following::div[contains(@id,'modeOfRequest')]"));
				if(!bStatus){
					Messages.errorMsg = "Mode of Request Drop down Not Selected";
					return false;
				}
			}


			if(mapRedemptionDetails.get("External ID")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='External ID']/following::input[contains(@id,'externalId')]"), mapRedemptionDetails.get("External ID"));
				if(!bStatus){
					Messages.errorMsg = "External ID Not Entered";
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

	//Maker Investor Details fill function.
	public static boolean doFillInvestorDetails(Map<String, String> mapInvestorDetails){
		try{
			if(mapInvestorDetails.get("Investor Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapInvestorDetails.get("Investor Name"), By.xpath("//label[normalize-space()='Investor Name']/following::div[contains(@id,'s2id_investor')]"));
				if(!bStatus){
					Messages.errorMsg = "Investor Name is Not Entered";
					return false;
				}

				//View Investor Details
				if(mapInvestorDetails.get("Investor KYC")!=null && mapInvestorDetails.get("Investor KYC").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
					if(!bStatus){
						Messages.errorMsg = "Spinner is still available after selecting Investor";
						return false;
					}
					Global.driver.findElement(By.xpath("//a[contains(@onclick,'viewInvestorKyc')]")).click();
					java.util.Set<String> AllWindowHandles = Global.driver.getWindowHandles(); 
					String window1 = (String) AllWindowHandles.toArray()[0]; 
					System.out.print("window1 handle code = "+AllWindowHandles.toArray()[0]); 
					String window2 = (String) AllWindowHandles.toArray()[1]; 
					System.out.print("\nwindow2 handle code = "+AllWindowHandles.toArray()[1]);

					Global.driver.switchTo().window(window2);
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//h4/p[contains(text(),'KYC Details')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "View Investor KYC Page is Not Displayed";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'close')]"));
					if(!bStatus){
						Messages.errorMsg = "View Investor KYC Page is Not Closed";
						return false;
					}
					Global.driver.switchTo().window(window1);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Holder Name']/following::div[@id='s2id_holderNameDropdown']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Page is not able to Navigate to Redemption Page";
						return false;
					}		
				}
			}


			if(mapInvestorDetails.get("Holder Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapInvestorDetails.get("Holder Name"), By.xpath("//label[normalize-space()='Holder Name']/following::div[@id='s2id_holderNameDropdown']"));
				if(!bStatus){
					Messages.errorMsg = "Holder Name is Not Selected";
					return false;
				}
				//View Holder Kyc Details
				if(mapInvestorDetails.get("Holder KYC")!=null && mapInvestorDetails.get("Holder KYC").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Spinner is still available after selecting Investor.]\n";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//a[contains(@onclick,'viewHolderKyc')]"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : View Holder KYC link is Not clicked.]\n";
						return false;
					}
					java.util.Set<String> AllWindowHandles = Global.driver.getWindowHandles(); 
					String window1 = (String) AllWindowHandles.toArray()[0]; 
					System.out.print("window1 handle code = "+AllWindowHandles.toArray()[0]); 
					String window2 = (String) AllWindowHandles.toArray()[1]; 
					System.out.print("\nwindow2 handle code = "+AllWindowHandles.toArray()[1]);

					Global.driver.switchTo().window(window2);
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//p[contains(text(),'KYC Details')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "View Holder KYC Page is Not Displayed";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'close')]"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : View Holder KYC Page is Not Closed.]\n";
						return false;
					}
					Global.driver.switchTo().window(window1);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Account Id']/following::div[@id='s2id_accountNameDropDown']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Page is not able to Navigate to Redemption Page.]\n";
						return false;
					}		
				}
			}
			if(mapInvestorDetails.get("Account ID")!=null){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Spinner is still available after selecting Investor.]\n";
					return false;
				}				
				bStatus =NewUICommonFunctions.selectFromDropDownOfApplication(mapInvestorDetails.get("Account ID"), By.xpath("//label[normalize-space()='Account Id']/following::div[@id='s2id_accountNameDropDown']"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Account Id is Not Selected.]\n";
					return false;
				}						
			}

			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Maker fund Details fill function.
	public static boolean doFillFundDetails(Map<String, String> mapSubscriptionDetails) {
		try {

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
			if(!bStatus){
				return false;
			}
			if(mapSubscriptionDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Client Name"), By.xpath("//label[normalize-space()='Client Name']/following::div[@id='s2id_cmbClientName']"));				
				if(!bStatus){
					Messages.errorMsg = "Client Name is not Selected";
					return false;
				}
			}


			if(mapSubscriptionDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Fund Family Name"), By.xpath("//label[normalize-space()='Fund Family Name']/following::div[@id='s2id_cmbFundFamilyName']"));
				if(!bStatus){
					Messages.errorMsg = "Fund Family Name is not Selected";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Legal Entity Name"), By.xpath("//label[normalize-space()='Legal Entity Name']/following::div[@id='s2id_cmbFundName']"));
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Name is not selected";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Class Name"), By.xpath("//label[normalize-space()='Class Name']/following::div[@id='s2id_cmbClassName']"));
				if(!bStatus){
					Messages.appErrorMsg = "Class Name is Not Selected";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Series Name"), By.xpath("//label[normalize-space()='Series Name']/following::div[@id='s2id_cmbSeriesName']"));
				if(!bStatus){
					Messages.errorMsg = "Series Name is not selected  from drop down";
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

	//Maker Redemption Details fill function.
	public static boolean doFIllRedemptionDetails(Map<String , String> mapRedemptionDetails){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Effective Date']//following::input[contains(@id,'effectiveDate')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Effective Date field is not visible";
				return false;
			}

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Full Redemption']"));
			if(!bStatus){
				return false;
			}

			if(mapRedemptionDetails.get("Full Redemption")!= null){
				if(mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-isTotalYes']/span/input[@id='isTotalYes']"));
					if(!bStatus){
						Messages.errorMsg = "Full Redemption Yes Radio button cannot be clicked";
						return false;
					}
					String percentageValue = "100";
					String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='percentage']");
					if(!actualValue.equalsIgnoreCase(percentageValue)){
						Messages.errorMsg = "Percentage Value is not Changed to 100 when Full redemption Yes button selected";
						return false;
					}

				}
				if(mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-isTotalNo']/span/input[@id='isTotalNo']"));
					if(!bStatus){
						Messages.errorMsg = "Full Redemption No Radio button cannot be clicked";
						return false;
					}
				}
			}

			if(mapRedemptionDetails.get("Effective Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Effective Date']//following::input[contains(@id,'effectiveDate')]"), mapRedemptionDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "Effective Date Not Entered";
					return false;
				}
			}

			if(mapRedemptionDetails.get("Percentage")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='percentage']"), mapRedemptionDetails.get("Percentage"));
				if(!bStatus){
					Messages.errorMsg = "Percentage Cannot be entered";
					return false;
				}
			}
			if(isRedemptionForCheckerReviewedCase){
				if(mapRedemptionDetails.get("AmountorShares")!=null && mapRedemptionDetails.get("Percentage") == null){
					if(mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-rbAmount']/span/input"));
						if(!bStatus){
							Messages.errorMsg = "Amount Redio button cannot be selected";
							return false;
						}
						if(mapRedemptionDetails.get("Redemption Amount")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapRedemptionDetails.get("Redemption Amount"));
							if(!bStatus){
								Messages.errorMsg = "Redemption Amount Not Entered";
								return false;
							}
						}

					}
					if(mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-rbShares']/span/input"));
						if(!bStatus){
							Messages.errorMsg = "Shares Redio button cannot be selected";
							return false;
						}
						if(mapRedemptionDetails.get("Share Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='unitValue']"), mapRedemptionDetails.get("Share Value"));
							if(!bStatus){
								Messages.errorMsg = "Share Value  Not Entered";
								return false;
							}
						}
					}

				}
			}else{
				if(mapRedemptionDetails.get("AmountorShares")!=null && mapRedemptionDetails.get("Percentage") == null && mapRedemptionDetails.get("Full Redemption")!= null && !mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("Yes")){
					if(mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-rbAmount']/span/input"));
						if(!bStatus){
							Messages.errorMsg = "Amount Redio button cannot be selected";
							return false;
						}
						if(mapRedemptionDetails.get("Redemption Amount")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapRedemptionDetails.get("Redemption Amount"));
							if(!bStatus){
								Messages.errorMsg = "Redemption Amount Not Entered";
								return false;
							}
						}

					}
					if(mapRedemptionDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-rbShares']/span/input"));
						if(!bStatus){
							Messages.errorMsg = "Shares Redio button cannot be selected";
							return false;
						}
						if(mapRedemptionDetails.get("Share Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='unitValue']"), mapRedemptionDetails.get("Share Value"));
							if(!bStatus){
								Messages.errorMsg = "Share Value  Not Entered";
								return false;
							}
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

	//Maker Override Details verify/change function.
	private static boolean doOverrideOrVerifyChargeDetails(Map<String, String> mapRedemptionDetails ) {
		try {

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsNo']/span"));
			if(!bStatus){
				Messages.errorMsg = "Page Not Scrolled to Notice Period Charges Radio Button fields";
				return false;
			}
			if(mapRedemptionDetails.get("NoticeChargesRadioButton") != null){
				if(mapRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
					if(!bStatus){
						return false;
					}
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsNo']/span/input"));
					if(!bStatus){
						Messages.errorMsg = "Notice Period Charges No Radio Button Not Clicked";
						return false;
					}
				}
				if(mapRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsYes']/span/input"));
					if(!bStatus){
						Messages.errorMsg = "Notice Period Charges Yes Radio Button Not Clicked";
						return false;
					}
				}
			}
			if(mapRedemptionDetails.get("NewAmountForNoticePeriod")!=null)
			{

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "New amount for Notice Charges field not visible";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), mapRedemptionDetails.get("NewAmountForNoticePeriod"));
				if(!bStatus){
					Messages.errorMsg = "New Amount for Notice Charges not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@class,'showNewNoticeCharges') and contains(text(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "Notice Period Charges Save butotn not clicked";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsNo']/span"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Notice Charges Radio Buttons are Not visible";
					return false;
				}

			}

			if(mapRedemptionDetails.get("TransactionChargesRadioButton") != null){
				if(mapRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
					if(!bStatus){
						return false;
					}
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsNo']/span"));
					//bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Transaction Charges No Radio Button Not Clicked";
						return false;
					}
				}
				if(mapRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsYes']/span"));
					//bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Transaction Charges Yes Radio Button Not Clicked";
						return false;
					}
				}
			}

			if(mapRedemptionDetails.get("NewTransactionCharges")!=null)
			{

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Transaction Charges New Charges field not visible";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), mapRedemptionDetails.get("NewTransactionCharges"));
				if(!bStatus){
					Messages.errorMsg = "New Transaction Charges not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@class,'showNewTransactionCharges') and contains(text(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "Transaction charges Charges Save butotn not clicked";
					return false;
				}


			}

			if(mapRedemptionDetails.get("PenaltyChargesRadioButton") != null){
				if(mapRedemptionDetails.get("PenaltyChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
					if(!bStatus){
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-fixedLockupChargesDetailsNo']/span"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Penalty Charges Radio Buttons are Not visible";
						return false;
					}
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-fixedLockupChargesDetailsNo']/span"));
					//bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Penalty Charges No Radio Button Not Clicked";
						return false;
					}
				}
				if(mapRedemptionDetails.get("PenaltyChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-fixedLockupChargesDetailsYes']/span"));
					//bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Penalty Charges Yes Radio Button Not Clicked";
						return false;
					}
				}
			}

			if(mapRedemptionDetails.get("NewPenaltyAmount")!=null)
			{

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newLockupCharges']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Penalty Charges New Charges field not visible";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newLockupCharges']"), mapRedemptionDetails.get("NewPenaltyAmount"));
				if(!bStatus){
					Messages.errorMsg = "New Penalty Charges not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@class,'showNewLockupCharges') and contains(text(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "Penalty charges Charges Save butotn not clicked";
					return false;
				}


			}

			if(mapRedemptionDetails.get("Adhoc Charges")!=null){
				//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='adHoCharges']"), mapRedemptionDetails.get("Adhoc Charges"));
				Thread.sleep(3000);
				bStatus = ClearAndSetText(By.xpath("//input[@id='adHoCharges']"), mapRedemptionDetails.get("Adhoc Charges"));
				if(!bStatus){
					Messages.errorMsg = "Adhoc Charges Not Entered";
					return false;
				}
			}

			if(mapRedemptionDetails.get("ManagementFeeWavier")!=null){
				if(mapRedemptionDetails.get("ManagementFeeWavier").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-orderRedemptionMasters0.isManagementFeeWavier1']/span"));
					if(!bStatus){
						Messages.errorMsg = "Management Fee Wavier Yes Radio button not selected";
						return false;
					}
				}

				if(mapRedemptionDetails.get("ManagementFeeWavier").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-orderRedemptionMasters0.isManagementFeeWavier2']/span"));
					if(!bStatus){
						Messages.errorMsg = "Management Fee Wavier No Radio button not selected";
						return false;
					}
				}

			}


			bStatus = doVerifyChargesDetail(mapRedemptionDetails);
			if(!bStatus){
				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Maker Charge Details verify function.
	public static boolean doVerifyChargesDetail(Map<String,String>mapVerifyRedemptionDetails){
		try
		{
			boolean validateStatus = true;
			String appendMsg="";

			//Thread.sleep(2000);
			if(mapVerifyRedemptionDetails.get("NoticeChargesRadioButton") != null){
				if(mapVerifyRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsNo']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = "[ Notice Charge Default Radio button No is not selected  ]\n";
					}
				}
				if(mapVerifyRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){
					// Verify Notice Charge Default

					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsYes']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+ "[ Notice Charge Default Radio button Yes is not selected  ]\n";
					}

				}
			}
			// Verify Notice Charges  with out modification

			if(mapVerifyRedemptionDetails.get("ExpectedNoticeCharges") != null )
			{
				// Verify Notice Charges 
				String ActualNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("noticeChargesAmount");

				String ExpectedNoticeCharge = mapVerifyRedemptionDetails.get("ExpectedNoticeCharges");

				if(ActualNoticeCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("NoticeCharges in Charge Details ", Integer.parseInt(mapVerifyRedemptionDetails.get("ExpectedNumberOfDecimals")), ActualNoticeCharges);
					if (!bStatus) {
						validateStatus = false;
						appendMsg =  appendMsg + Messages.errorMsg;
					}
					if(Float.parseFloat(ActualNoticeCharges) != Float.parseFloat(ExpectedNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Actual Notice Charges "+ActualNoticeCharges+" are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedNoticeCharges")+" ]\n";
					}
				}
			}

			// Verify Notice Charges  with modification

			if(mapVerifyRedemptionDetails.get("ExpectedNewNoticeCharges") !=null){

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("noticeChargesAmount");
				String ExpectedNewNoticeCharge = mapVerifyRedemptionDetails.get("ExpectedNewNoticeCharges");
				if(ActualNewNoticeCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("NewNoticeCharges in Charge Details ", Integer.parseInt(mapVerifyRedemptionDetails.get("ExpectedNumberOfDecimals")), ActualNewNoticeCharges);
					if (!bStatus) {
						validateStatus = false;
						appendMsg =  appendMsg + Messages.errorMsg;
					}
					if(Float.parseFloat(ActualNewNoticeCharges) != Float.parseFloat(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Actual Notice Charges "+ActualNewNoticeCharges+" are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedNewNoticeCharges")+" ]\n";
					}
				}
			}

			if(mapVerifyRedemptionDetails.get("TransactionChargesRadioButton") != null){
				if(mapVerifyRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsNo']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = "[ Transaction  Charge Default Radio button No Radio button is not selected ]\n";
					}
				}
				if(mapVerifyRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){
					// Verify Notice Charge Default
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsYes']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg =  appendMsg+"[Transaction Charge Default Radio button Yes is not selected  ]\n";
					}

				}
			}

			//Verify the Transaction Charges with out modification

			if(mapVerifyRedemptionDetails.get("ExpectedTransactionCharges") != null ){
				// Verify Notice Charges 
				String ActualTransCharges = NewUICommonFunctions.jsGetElementAttribute("fixedTransactionChargesAmount");

				String ExpectedTransNoticeCharge = mapVerifyRedemptionDetails.get("ExpectedTransactionCharges");

				if(ActualTransCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("TransactionCharges in Charge Details ", Integer.parseInt(mapVerifyRedemptionDetails.get("ExpectedNumberOfDecimals")), ActualTransCharges);
					if (!bStatus) {
						validateStatus = false;
						appendMsg =  appendMsg + Messages.errorMsg;
					}
					if(Float.parseFloat(ActualTransCharges) != Float.parseFloat(ExpectedTransNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Actual Transaction  Charges "+ActualTransCharges+" are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedTransactionCharges")+" ]\n";
					}
				}
			}

			// Verify Transaction  Charges  with modification

			if(mapVerifyRedemptionDetails.get("NewTransactionCharges") !=null && mapVerifyRedemptionDetails.get("NewExpectedTransactionCharges") !=null){
				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("fixedTransactionChargesAmount");
				String ExpectedNewNoticeCharge = mapVerifyRedemptionDetails.get("NewExpectedTransactionCharges");

				if( ActualNewNoticeCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("NewTransactionCharges in Charge Details ", Integer.parseInt(mapVerifyRedemptionDetails.get("ExpectedNumberOfDecimals")), ActualNewNoticeCharges);
					if (!bStatus) {
						validateStatus = false;
						appendMsg =  appendMsg + Messages.errorMsg;
					}
					if(Float.parseFloat(ActualNewNoticeCharges) != Float.parseFloat(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Actual Transaction  Charges "+ActualNewNoticeCharges+" are Not matched with Expected "+mapVerifyRedemptionDetails.get("NewExpectedTransactionCharges")+" ]\n";
					}
				}
			}


			if(mapVerifyRedemptionDetails.get("PenaltyChargesRadioButton") != null){
				if(mapVerifyRedemptionDetails.get("PenaltyChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedLockupChargesDetailsNo']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = "[ Penalty  Charge Default Radio button No Radio button is not selected ]\n";
					}
				}
				if(mapVerifyRedemptionDetails.get("PenaltyChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedLockupChargesDetailsYes']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = "[ Penalty  Charge Default Radio button Yes Radio button is not selected ]\n";
					}
				}
			}

			if(mapVerifyRedemptionDetails.get("ExpectedPenaltyAmount") != null ){

				// Verify Notice Charges 
				String ActualPenaltyCharges = NewUICommonFunctions.jsGetElementAttribute("penaltyAmountId");

				String ExpectedPenaltyCharge = mapVerifyRedemptionDetails.get("ExpectedPenaltyAmount");

				if(ActualPenaltyCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("penaltyAmount in Charge Details ", Integer.parseInt(mapVerifyRedemptionDetails.get("ExpectedNumberOfDecimals")), ActualPenaltyCharges);
					if (!bStatus) {
						validateStatus = false;
						appendMsg =  appendMsg + Messages.errorMsg;
					}
					if(Float.parseFloat(ActualPenaltyCharges) != Float.parseFloat(ExpectedPenaltyCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Actual Penalty  Charges "+ActualPenaltyCharges+" are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedPenaltyAmount")+" ]\n";
					}
				}
			}

			// Verify Penalty Charges  with modification

			if(mapVerifyRedemptionDetails.get("NewPenaltyAmount") !=null && mapVerifyRedemptionDetails.get("NewExpectedPenaltyAmount") !=null){

				String ActualNewPenaltyCharges = NewUICommonFunctions.jsGetElementAttribute("penaltyAmountId");
				String ExpectedNewPenaltyCharge = mapVerifyRedemptionDetails.get("NewExpectedPenaltyAmount");

				if( ActualNewPenaltyCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("NewPenaltyAmount in Charge Details ", Integer.parseInt(mapVerifyRedemptionDetails.get("ExpectedNumberOfDecimals")), ActualNewPenaltyCharges);
					if (!bStatus) {
						validateStatus = false;
						appendMsg =  appendMsg + Messages.errorMsg;
					}
					if(Float.parseFloat(ActualNewPenaltyCharges) != Float.parseFloat(ExpectedNewPenaltyCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Actual Penalty  Charges "+ActualNewPenaltyCharges+" are Not matched with Expected "+mapVerifyRedemptionDetails.get("NewExpectedPenaltyAmount")+" ]\n";
					}
				}
			}


			//verify Adhoc charges

			/*	if(mapVerifySubscriptionDetails.get("Adhoc Charges") != null){
				String ActualAdhoc =  NewUICommonFunctions.jsGetElementAttribute("orderDetails0.adhocCharges");
				String ExpectedNewAdhocCharge = mapVerifySubscriptionDetails.get("Adhoc Charges");

				if(ActualAdhoc != null){
					if(ActualAdhoc.equalsIgnoreCase(ExpectedNewAdhocCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Adhoc  Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("Adhoc Charges")+" ]\n";
					}

				}
			}*/

			//Verify  Total charges
			if(mapVerifyRedemptionDetails.get("ExpectedTotalCharges")!=null){
				String ActualToatal =  NewUICommonFunctions.jsGetElementAttribute("totalChargesView");
				String ExpectedCharge = mapVerifyRedemptionDetails.get("ExpectedTotalCharges");

				if(ActualToatal != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("TotalCharges in Charge Details ", Integer.parseInt(mapVerifyRedemptionDetails.get("ExpectedNumberOfDecimals")), ActualToatal);
					if (!bStatus) {
						validateStatus = false;
						appendMsg =  appendMsg + Messages.errorMsg;
					}
					if(Float.parseFloat(ActualToatal) != Float.parseFloat(ExpectedCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Actual Total  Charges "+ActualToatal+" are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedTotalCharges")+" ]\n";
					}
				}	
			}
			Messages.errorMsg = appendMsg;
			return validateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//Maker View button tab Details verify/fill functions.
	public static void verifyViewButtons(Map<String, String> mapRedemptionDetails){
		try {

			bStatus = doFillRequestDetails(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Request Details","Request Details cannot be filled. Error: "+Messages.errorMsg);
				return;
			}else{
				Reporting.logResults("Pass","Fill Request Details","Request details filled succesfully");
			}

			bStatus = doFillInvestorDetails(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Investor Details","Investor Details cannot be filled. Error: "+Messages.errorMsg);
				return;
				//return false;
			}else{
				Reporting.logResults("Pass","Fill Investor Details","Investor details filled succesfully");
			}

			bStatus = doFillFundDetails(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Fund Details","Fund Details cannot be filled. Error: "+Messages.errorMsg);
				return;
			}else{
				Reporting.logResults("Pass","Fill Fund Details","Fund details filled succesfully");
			}

			bStatus = doFIllRedemptionDetails(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Redemption Details","Redemption Details cannot be filled. Error: "+Messages.errorMsg);
				return;
			}else{
				Reporting.logResults("Pass","Fill Redemption Details","Redemption details filled succesfully");
			}

			//Click Proceed for Next Screen
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//button[contains(@onclick,'javascript:getRedDetails') and contains(normalize-space(),'Proceed')]"));
			if(!bStatus){
				Reporting.logResults("Fail","Scroll to Proceed button","Cannot scroll to Proceed Button");
				return;
			}
			bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'javascript:getRedDetails') and contains(normalize-space(),'Proceed')]"));
			if(!bStatus){
				Reporting.logResults("Fail","Navigate to Next Screen","Cannot click on Proceed Button");
				return;
			}

			bStatus = verifyViewButtonForNoticePeriods(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Notice period details","Verification Failed. Error: "+Messages.errorMsg);
				Elements.click(Global.driver, By.xpath("//h4[normalize-space()='Notice Period Charges']//following::div[@class='modal-footer']//button[contains(text(),'Close')]"));
				return;
			}
			if(mapRedemptionDetails.get("NPNumberOfDaysOrMonthOrYear")!= null && bStatus){
				Elements.click(Global.driver, By.xpath("//h4[normalize-space()='Notice Period Charges']//following::div[@class='modal-footer']//button[contains(text(),'Close')]"));
				Reporting.logResults("Pass","Verify Notice period details","Notice period Charges verified successfully");
			}

			bStatus = verifyViewButtonForTransactionCharge(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Transaction period details","Verification Failed. Error: "+Messages.errorMsg);
				Elements.click(Global.driver, By.xpath("//h4[normalize-space()='Transaction Charges']//following::div//button[contains(text(),'Close')]"));
				return;
			}
			if(bStatus && mapRedemptionDetails.get("TCEffDate")!=null){
				Elements.click(Global.driver, By.xpath("//h4[normalize-space()='Transaction Charges']//following::div//button[contains(text(),'Close')]"));
				Reporting.logResults("Pass","Verify Transaction period details","Transaction period Charges verified successfully");
			}


			bStatus = verifyViewButtonForPenalty(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Lockup charge details","Verification Failed. Error: "+Messages.errorMsg);
				Elements.click(Global.driver, By.xpath("//div[@id='viewPenaltyCharges']//button[contains(text(),'Close')]"));
				return;
			}
			if(bStatus && mapRedemptionDetails.get("LPNumberOfDaysOrMonthOrYear")!=null){
				Elements.click(Global.driver, By.xpath("//div[@id='viewPenaltyCharges']//button[contains(text(),'Close')]"));
				Reporting.logResults("Pass","Verify Lockup Charge Details","Lockup Charge Details verified successfully");
			}

			return;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
	}

	private static boolean verifyViewButtonForPenalty(Map<String, String> ViewMapDetails) {
		try {

			if(ViewMapDetails.get("LPNumberOfDaysOrMonthOrYear")== null){
				return true;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='Penalty Amount']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot scroll to Penalty Charges";
				return false;
			}	
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Penalty Amount']"));
			if(!bStatus){
				return false;
			}
			bStatus = Elements.click(Global.driver, By.id("viewDefaultLockupCharges"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on View button for Penalty charges";
				return false;
			}
			Thread.sleep(2000);
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//h4[text()='Lockup Charges']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "View  Penalty Lockup Charges pop-up is not displayed";
				return false;
			}

			String sValue = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Lockup Period']//following::p[@id='lockupPeriodView']")).trim();
			if(sValue!=null && !sValue.equalsIgnoreCase(ViewMapDetails.get("LPNumberOfDaysOrMonthOrYear"))){
				Messages.errorMsg = "Atual Lockup period value is "+sValue+" which is not equal to expected value: "+ViewMapDetails.get("LPNumberOfDaysOrMonthOrYear");
				return false;
			}

			String sType = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Lockup Period']//following::p[@id='lockupPeriodTypeView']")).trim();
			if(sType!=null && !sType.equalsIgnoreCase(ViewMapDetails.get("LPDayOrMonthOrYear"))){
				Messages.errorMsg = "Atual value Lockup period Type is "+sType+" which is not equal to expected value: "+ViewMapDetails.get("LPDayOrMonthOrYear");
				return false;
			}

			String sCalendar = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Lockup Period']//following::p[@id='lockupPeriodCalendarView']")).trim();
			if(sCalendar!=null && !sCalendar.equalsIgnoreCase(ViewMapDetails.get("LPCalendarView"))){
				Messages.errorMsg = "Atual value Lockup period Calendar type is "+sCalendar+" which is not equal to expected value: "+ViewMapDetails.get("LPCalendarView").trim();
				return false;
			}

			String ChargesType = Elements.getText(Global.driver, By.xpath("//input[@id='redemptionCharges.lockupChargeType']//following-sibling::p[@id='noticePeriodChargesTypeView']"));
			if(ChargesType!=null && !ChargesType.equalsIgnoreCase(ViewMapDetails.get("LPChargesType"))){
				Messages.errorMsg = "Atual value Lockup period Charges Type is "+ChargesType+" which is not equal to expected value: "+ViewMapDetails.get("LPChargesType");
				return false;
			}

			if(ViewMapDetails.get("LPIncludeHolidays")!=null && ViewMapDetails.get("LPIncludeHolidays").equalsIgnoreCase("Yes")){
				String includeHolidays = Elements.getText(Global.driver, By.xpath("//input[@id='redemptionCharges.isHolidaysInclude']//following-sibling::div")).trim();
				if(includeHolidays!=null && !includeHolidays.equalsIgnoreCase(ViewMapDetails.get("LPIncludeHolidays"))){
					Messages.errorMsg = "Atual value Lockup Include Holiday Type is "+includeHolidays+" which is not equal to expected value: "+ViewMapDetails.get("LPIncludeHolidays");
					return false;
				}
			}

			String sMethod = Elements.getText(Global.driver, By.xpath("//p[@id='transactionRateMethodView']"));
			if(sMethod!=null && !sMethod.equalsIgnoreCase(ViewMapDetails.get("LPRateMethod"))){
				Messages.errorMsg = "Actual value Lockup Rate Method type is "+sMethod+" which is not equal to expected value: "+ViewMapDetails.get("LPRateMethod");
				return false;
			}



			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyViewButtonForTransactionCharge(Map<String, String> ViewMapDetails) {
		try {

			if(ViewMapDetails.get("TCEffDate")==null){
				return true;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='Transaction Charges']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot scroll to Transaction Charges";
				return false;
			}		
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Transaction Charges']"));
			if(!bStatus){
				return false;
			}

			bStatus = Elements.click(Global.driver, By.id("viewDefaultTransactionCharges"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on View button for Transaction charges";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//h4[text()='Transaction Charges']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "View Transaction charges pop-up is not displayed";
				return false;
			}

			String sEffDate = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Effective Date']//following::p[@id='effectiveDateView']")).trim();
			if(sEffDate!=null && !sEffDate.equalsIgnoreCase(ViewMapDetails.get("TCEffDate"))){
				Messages.errorMsg = "Actual value Effective Date value is "+sEffDate+" which is not equal to expected value: "+ViewMapDetails.get("TCEffDate");
				return false;
			}

			String sType = Elements.getText(Global.driver, By.xpath("//p[@id='transactionChargeTypeView']"));
			if(sType!=null && !sType.equalsIgnoreCase(ViewMapDetails.get("TCType"))){
				Messages.errorMsg = "Actual value Transaction Charge Type is "+sType+" which is not equal to expected value: "+ViewMapDetails.get("TCType");
				return false;
			}

			String sMethod = Elements.getText(Global.driver, By.xpath("//p[@id='transactionRateMethodView']"));
			if(sMethod!=null && !sMethod.equalsIgnoreCase(ViewMapDetails.get("TCRateMethod"))){
				Messages.errorMsg = "Actual value Rate Method type is "+sMethod+" which is not equal to expected value: "+ViewMapDetails.get("TCRateMethod");
				return false;
			}

			String CalculationType = Elements.getText(Global.driver, By.xpath("//p[@id='transactionCalculationBaseView']"));
			if(CalculationType!=null && !CalculationType.equalsIgnoreCase(ViewMapDetails.get("TCCalculationType"))){
				Messages.errorMsg = "Actual value Calculation Type is "+CalculationType+" which is not equal to expected value: "+ViewMapDetails.get("TCCalculationType");
				return false;
			}


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyViewButtonForNoticePeriods(Map<String, String> ViewMapDetails) {
		try {

			if(ViewMapDetails.get("NPNumberOfDaysOrMonthOrYear")== null){
				return true;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='Notice Charges']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot scroll to Notice Period Charges";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Notice Charges']"));
			if(!bStatus){
				return false;
			}
			bStatus = Elements.click(Global.driver, By.id("viewDefaultNoticeCharges"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on View button for notice charges";
				return false;
			}
			Thread.sleep(2000);
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//h4[text()='Notice Period Charges']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "View Notice period charges pop-up is not displayed";
				return false;
			}

			String sValue = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Notice Period']//following::p[@id='noticePeriodView']")).trim();
			if(sValue!=null && !sValue.equalsIgnoreCase(ViewMapDetails.get("NPNumberOfDaysOrMonthOrYear"))){
				Messages.errorMsg = "Atual value Notice period value is "+sValue+" which is not equal to expected value: "+ViewMapDetails.get("NPNumberOfDaysOrMonthOrYear");
				return false;
			}

			String sType = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Notice Period']//following::p[@id='noticePeriodTypeView']")).trim();
			if(sType!=null && !sType.equalsIgnoreCase(ViewMapDetails.get("NPDayOrMonthOrYear"))){
				Messages.errorMsg = "Atual value Notice period Type is "+sType+" which is not equal to expected value: "+ViewMapDetails.get("NPDayOrMonthOrYear");
				return false;
			}

			String sCalendar = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Notice Period']//following::p[@id='noticePeriodCalendarView']")).trim();
			if(sCalendar!=null && !sCalendar.equalsIgnoreCase(ViewMapDetails.get("NPCalendarView"))){
				Messages.errorMsg = "Atual value Notice period Calendar type is "+sCalendar+" which is not equal to expected value: "+ViewMapDetails.get("NPCalendarView").trim();
				return false;
			}

			String ChargesType = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Notice Period Charges']//following::p[@id='noticePeriodChargesTypeView']"));
			if(ChargesType!=null && !ChargesType.equalsIgnoreCase(ViewMapDetails.get("NPChargesType"))){
				Messages.errorMsg = "Atual value Notice period Charges Type is "+ChargesType+" which is not equal to expected value: "+ViewMapDetails.get("NPChargesType");
				return false;
			}

			if(ViewMapDetails.get("IncludeHolidays")!=null && ViewMapDetails.get("IncludeHolidays").equalsIgnoreCase("Yes")){
				String includeHolidays = Elements.getText(Global.driver, By.xpath("//input[@id='chargesList0.isHolidaysInclude']//following-sibling::div")).trim();
				if(includeHolidays!=null && !includeHolidays.equalsIgnoreCase(ViewMapDetails.get("IncludeHolidays"))){
					Messages.errorMsg = "Atual value Include Holiday Type is "+includeHolidays+" which is not equal to expected value: "+ViewMapDetails.get("IncludeHolidays");
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

	//Maker Exception Details verify function.
	public static boolean doVerifyMakerExceptions(String exceptionList){
		try {
			int exceptionCount = 0;
			List<String> aExceptionlist = Arrays.asList(exceptionList.split(":"));
			int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@id='exceptionModal']//div[@class='alert alert-danger']/strong/p"));
			String totalExceptions = "";
			for(int j=1; j<=xpathCount ;j++){
				String exceptionValue = Elements.getText(Global.driver, By.xpath("//div[@id='exceptionModal']//div[@class='alert alert-danger']/strong/p["+j+"]"));
				if(exceptionValue!=null && !exceptionValue.equalsIgnoreCase("")){
					totalExceptions = totalExceptions+"["+exceptionValue+"]";
					exceptionCount = exceptionCount+1;
				}

			}

			if(exceptionCount != aExceptionlist.size()){
				Messages.errorMsg = "[ Actual Exception count "+exceptionCount+" is not matching with the Expected Exception count "+aExceptionlist.size()+" ] "+"Actual Exceptions are "+totalExceptions+" Expected exceptions are ["+exceptionList+"]";
				return false;
			}

			for(int i=0 ; i<aExceptionlist.size(); i++){
				if(!totalExceptions.contains(aExceptionlist.get(i))){
					Messages.errorMsg = "Expected exception "+aExceptionlist.get(i)+" is not matching the Actual exception";
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

	//Checker Charge Details verify function.
	private static boolean doVerifyChargeDetailsinChecker(Map<String, String> mapRedemptionDetails) {
		try {

			String appndErrMsg = "";
			boolean ValidbStatus = true;
			
			if(mapRedemptionDetails.get("NoticeChargesRadioButton") != null){
				if(mapRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("fixedNoticeChargesDetailsNo"));
					if(!bStatus){
						appndErrMsg  = appndErrMsg +"[Expected Value 'No' is not checked for Label: Notice Charges]";
						ValidbStatus  = false;
					}
				}
				if(mapRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("fixedNoticeChargesDetailsYes"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Notice Charges]";
						ValidbStatus = false;
					}
				}
			}
			if(mapRedemptionDetails.get("NewAmountForNoticePeriod")!=null && mapRedemptionDetails.get("ExpectedNewNoticeCharges") != null){
			
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Notice Charges",mapSubscriptionDetails.get("ExpectedNewNoticeCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Notice Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("AmountForNoticePeriod in Charge Details ", Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if (!bStatus) {
					appndErrMsg =  appndErrMsg + Messages.errorMsg;
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapRedemptionDetails.get("ExpectedNewNoticeCharges"))){
					appndErrMsg = appndErrMsg +"[Notice Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapRedemptionDetails.get("ExpectedNewNoticeCharges")+"]";
					ValidbStatus = false;
				}
			}

			if(mapRedemptionDetails.get("ExpectedNoticeCharges")!=null){
				
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Notice Charges",mapSubscriptionDetails.get("ExpectedNoticeCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Notice Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("NoticeCharges in Charge Details ", Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if (!bStatus) {
					appndErrMsg =  appndErrMsg + Messages.errorMsg;
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapRedemptionDetails.get("ExpectedNoticeCharges"))){
					appndErrMsg = appndErrMsg +"[Notice Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapRedemptionDetails.get("ExpectedNoticeCharges")+"]";
					ValidbStatus = false;
				}
			}
			if(mapRedemptionDetails.get("TransactionChargesRadioButton") != null){
				if(mapRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("fixedTransactionChargesDetailsNo"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'No' is not checked for Label: Transaction Charges]";
						ValidbStatus = false;
					}
				}
				if(mapRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("fixedTransactionChargesDetailsYes"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Transaction Charges]";
						ValidbStatus = false;
					}
				}
			}
			if(mapRedemptionDetails.get("NewTransactionCharges")!=null && mapRedemptionDetails.get("NewExpectedTransactionCharges") != null){				

				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Transaction Charges",mapSubscriptionDetails.get("NewExpectedTransactionCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Transaction Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("TransactionCharges in Charge Details ", Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if (!bStatus) {
					appndErrMsg =  appndErrMsg + Messages.errorMsg;
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapRedemptionDetails.get("NewExpectedTransactionCharges"))){
					appndErrMsg = appndErrMsg +"[Transaction Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapRedemptionDetails.get("NewExpectedTransactionCharges")+"]";
					ValidbStatus = false;
				}
			}

			if(mapRedemptionDetails.get("ExpectedTransactionCharges")!=null){				

				//	bStatus = NewUICommonFunctions.verifyTextInTextBox("Transaction Charges",mapSubscriptionDetails.get("NewExpectedTransactionCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Transaction Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("TransactionCharges in Charge Details ", Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if (!bStatus) {
					appndErrMsg =  appndErrMsg + Messages.errorMsg;
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapRedemptionDetails.get("ExpectedTransactionCharges"))){
					appndErrMsg = appndErrMsg +"[Transaction Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapRedemptionDetails.get("ExpectedTransactionCharges")+"]";
					ValidbStatus = false;
				}
			}

			if(mapRedemptionDetails.get("Adhoc Charges")!=null){
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Adhoc Charges",mapSubscriptionDetails.get("Adhoc Charges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Adhoc Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Adhoc Charges in Charge Details ", Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if (!bStatus) {
					appndErrMsg =  appndErrMsg + Messages.errorMsg;
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapRedemptionDetails.get("Adhoc Charges"))){
					appndErrMsg = appndErrMsg +"[Adhoc Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapRedemptionDetails.get("Adhoc Charges")+"]";
					ValidbStatus = false;
				}
			}
			//ExpectedTotalCharges

			if(mapRedemptionDetails.get("ExpectedTotalCharges")!=null){
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Total Charges",mapSubscriptionDetails.get("ExpectedTotalCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Total Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("TotalCharges in Charge Details ", Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if (!bStatus) {
					appndErrMsg =  appndErrMsg + Messages.errorMsg;
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapRedemptionDetails.get("ExpectedTotalCharges"))){
					appndErrMsg = appndErrMsg +"[Total Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapRedemptionDetails.get("ExpectedTotalCharges")+"]";
					ValidbStatus = false;
				}
			}
			

			if(mapRedemptionDetails.get("PenaltyChargesRadioButton") != null){
				if(mapRedemptionDetails.get("PenaltyChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("fixedLockupChargesDetailsNo"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'No' is not checked for Label: Penalty Charges]";
						ValidbStatus = false;
					}
				}
				if(mapRedemptionDetails.get("PenaltyChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("fixedLockupChargesDetailsYes"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Penalty Charges]";
						ValidbStatus = false;
					}
				}
			}
			
			if(mapRedemptionDetails.get("NewPenaltyAmount")!=null && mapRedemptionDetails.get("NewExpectedPenaltyAmount") != null){
				
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Transaction Charges",mapSubscriptionDetails.get("NewExpectedTransactionCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Penalty Amount']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("PenaltyAmount in Charge Details ", Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if (!bStatus) {
					appndErrMsg =  appndErrMsg + Messages.errorMsg;
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapRedemptionDetails.get("NewExpectedPenaltyAmount"))){
					appndErrMsg = appndErrMsg +"[Penalty Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapRedemptionDetails.get("NewExpectedPenaltyAmount")+"]";
					ValidbStatus = false;
				}
			}

			if(mapRedemptionDetails.get("ExpectedPenaltyAmount")!=null){

				//	bStatus = NewUICommonFunctions.verifyTextInTextBox("Transaction Charges",mapSubscriptionDetails.get("NewExpectedTransactionCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Penalty Amount']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("PenaltyAmount in Charge Details ", Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if (!bStatus) {
					appndErrMsg =  appndErrMsg + Messages.errorMsg;
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapRedemptionDetails.get("ExpectedPenaltyAmount"))){
					appndErrMsg = appndErrMsg +"[Penalty Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapRedemptionDetails.get("ExpectedPenaltyAmount")+"]";
					ValidbStatus = false;
				}
			}

			if(mapRedemptionDetails.get("ManagementFeeWavier") != null){
				if(mapRedemptionDetails.get("ManagementFeeWavier").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("orderRedemptionMasters0.isManagementFeeWavier1"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Management Fee Wavier]";
						ValidbStatus = false;
					}
				}
				if(mapRedemptionDetails.get("ManagementFeeWavier").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("orderRedemptionMasters0.isManagementFeeWavier2"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'No' is not checked for Label: Management Fee Wavier]";
						ValidbStatus = false;
					}
				}
			}


			Messages.errorMsg = appndErrMsg;
			return ValidbStatus;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//sub operations on trade.
	public static boolean doSubOperationsOnTransactionTrades(String master , String operation){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//button[contains(@onclick,'javascript:saveRedDetails') and contains(normalize-space(),'Save')]") , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Save Button is Not Visible";
				return false;
			}
			if(operation.equalsIgnoreCase("Save")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:saveRedDetails') and contains(normalize-space(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Save button cannot be clicked";
					return false;
				}
				return true;

			}

			if(operation.contains("Review")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:sendForReview') and contains(normalize-space(),'Send for Review')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Send For Review button cannot be clicked";
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

	//Maker Fill available balance Details function.
	public static boolean doFillAvailableBalanceDetails(Map<String, String> mapRedemptionDetails){
		int noOfAmountOrShareLots = 0;
		List<String> sAmountOrSharesList = null;
		List<String> sAmountOrSharesIndexList = null;
		List<String> sAmountOrSharesIndexesEditableStatusList = null;
		List<String> sVerifyAmountOrSharesList = null;
		try {
			String sIsAmountOrShare = "";
			String verifyAmountOrShares = "";
			if (mapRedemptionDetails.get("Redemption Method Applied") != null) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='s2id_redmethodApplied']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Page cannot scroll to Available Balance Details";
					return false;
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapRedemptionDetails.get("Redemption Method Applied"), By.xpath("//div[@id='s2id_redmethodApplied']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to select the 'Redemption Method Applied' option : "+mapRedemptionDetails.get("Redemption Method Applied")+" ]\n";
					return false;
				}
			}
			if (mapRedemptionDetails.get("TaxLotsAmounts") != null || mapRedemptionDetails.get("TaxLotsShares") != null) {
				if (mapRedemptionDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShares = "Share";
				}
				else if (mapRedemptionDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShares = "Amount";
				}
			}
			if(mapRedemptionDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("ExpectedTaxLotAmountOrShare").split(","));
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
				//bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]//input[contains(@id,'redLot')]"), 3);
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]"), 3);
				if(!bStatus && noOfAmountOrShareLots != 0){
					Messages.errorMsg = "Tax lots are not visible";
					return false;
				}
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]"));				
				//int iNoOfLots = Integer.parseInt(NoOfRecords);
				if (NoOfRecords != noOfAmountOrShareLots) {
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+noOfAmountOrShareLots+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				for (int i = 0; i < noOfAmountOrShareLots; i++) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='redLot_"+i+"']//parent::span[@class='checked']"), 3);
					if (sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes")) {
						if (sAmountOrSharesIndexList != null && !sAmountOrSharesIndexList.contains(String.valueOf(i)) && bStatus) {
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='redLot_"+i+"']//parent::span[@class='checked']"));
							if (!bStatus) {
								Messages.errorMsg = "[ERROR : Unable to unselect the Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
								return false;
							}
						}
						if (sAmountOrSharesIndexList != null && sAmountOrSharesIndexList.contains(String.valueOf(i))) {
							if (!bStatus) {
								bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
								bStatus = Elements.click(Global.driver, By.xpath("//input[@id='redLot_"+i+"']"));
								if (!bStatus) {
									Messages.errorMsg = "[ERROR : Unable to select the Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
									return false;
								}
							}
							By locator = By.id("allocated"+sIsAmountOrShare+"_"+i+"");
							bStatus = ClearAndSetText(locator, sAmountOrSharesList.get(i));							
							//bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(locator, sAmountOrSharesList.get(i));
							//bStatus = Elements.enterText(Global.driver, By.id("allocated"+sIsAmountOrShare+"_"+i+""), sAmountOrSharesList.get(i));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to enter the text into the Redemption Lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
								return false;
							}
							bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.id("allocated"+verifyAmountOrShares+"_"+i+""), sVerifyAmountOrSharesList.get(i), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
							if (!bStatus) {
								return false;
							}
						}							
					}
					if (!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes") && sAmountOrSharesIndexList.contains(String.valueOf(i))) {						
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.id("allocated"+sIsAmountOrShare+"_"+i+""), sAmountOrSharesList.get(i), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
						if (!bStatus) {
							return false;
						}							
					}
					if(!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes") && !sAmountOrSharesIndexList.contains(String.valueOf(i)) && bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='redLot_"+i+"']//parent::span[@class='checked']"));
						if (!bStatus) {
							Messages.errorMsg = "[ERROR : Unable to unselect the Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
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

	//Checker verify available balance Details function.
	public static boolean doCheckerVerifyAvailableBalanceDetails(Map<String, String> mapRedemptionDetails){
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
				//String NoOfRecords = Elements.getElementAttribute(Global.driver, By.id("detailsListSize"), "value");
				Wait.waitForElementPresence(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]//input[contains(@id,'redLot')]"), 3);
				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]//input[contains(@id,'redLot')]"));				
				//int iNoOfLots = Integer.parseInt(NoOfRecords);
				if (NoOfRecords != noOfAmountOrShareLots) {
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+noOfAmountOrShareLots+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}
				for (int i = 0; i < noOfAmountOrShareLots; i++) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='redLot_"+i+"']//parent::span[@class='checked']"), 3);
					if (sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes")) {
						if (sAmountOrSharesIndexList != null && !sAmountOrSharesIndexList.contains(i) && bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + "[ERROR : Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' is supposed to be in Unselected state but it is appeared to be selected. ]\n";
							bValidateStatus = false;
						}
						if (sAmountOrSharesIndexList != null && sAmountOrSharesIndexList.contains(i)) {
							if (!bStatus) {
								sAppendErrorMsg = sAppendErrorMsg + "[ERROR : Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' is supposed to be in selected state but it is appeared to be Unselected. ]\n";
								bValidateStatus = false;
							}
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.id("allocated"+sIsAmountOrShare+"_"+i+""), sAmountOrSharesList.get(i), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
							if (!bStatus) {
								sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
								bValidateStatus = false;
							}
						}							
					}
					if (!sAmountOrSharesIndexesEditableStatusList.get(i).equalsIgnoreCase("Yes") && sAmountOrSharesIndexList.contains(i)) {
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.id("allocated"+sIsAmountOrShare+"_"+i+""), sAmountOrSharesList.get(i), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
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

	//changeOverride details function.
	public static boolean changeOverrideStatus(Map<String , String> mapSubscriptionDetails){
		try {

			if(mapSubscriptionDetails.get("NewAmountForNoticePeriod")!=null && mapSubscriptionDetails.get("NoticePeriodOverrideAtChecker")!=null){
				bStatus = doCheckerVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription("Notice Period", mapSubscriptionDetails.get("NoticePeriodOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}
			if(mapSubscriptionDetails.get("NewTransactionCharges")!=null && mapSubscriptionDetails.get("TransactionOverrideAtChecker")!=null){
				bStatus = doCheckerVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription("Transaction", mapSubscriptionDetails.get("TransactionOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}

			if(mapSubscriptionDetails.get("NewPenaltyAmount")!=null && mapSubscriptionDetails.get("PenaltyAmountOverrideAtChecker")!=null){
				bStatus = doCheckerVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription("Penalty Amount", mapSubscriptionDetails.get("PenaltyAmountOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}

			/*if(mapSubscriptionDetails.get("NewManagementFee")!=null && mapSubscriptionDetails.get("ManagementFeeOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription("Management Fee", mapSubscriptionDetails.get("ManagementFeeOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}*/
			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//To clear the text using backspace and to enter the given text.
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

	//Maker/Checker verify Pending Trades Details function.
	public static boolean doMakerAndCheckerVerifyPendingTradesDetails(String sPTRequestedAmounts, String sPTRequestedShares, int iNoOfDecimalsExpected,int iNoOfShareDecimals){
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
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr"), 4);			
			noOfAmountOrShareLots = Elements.getXpathCount(Global.driver, By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr"));
			if (sAmountsList.size() != noOfAmountOrShareLots) {
				Messages.errorMsg = "[ ERROR : Actual No Of Records for 'Pending Trades' : '"+noOfAmountOrShareLots+"' is not matching with Expected no : '"+sAmountsList.size()+"' ]";
				return false;
			}
			for (int i = 1; i <= sAmountsList.size(); i++) {
				if(!sAmountsList.get(i-1).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Pending Trade' lot 'Amount' value at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr["+i+"]//td[7]"), sAmountsList.get(i-1), "Yes", true, iNoOfDecimalsExpected);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if(!sSharesList.get(i-1).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Pending Trade' lot 'Share' value at index '"+i+"' ", By.xpath("//div[@class='sub-caption' and contains(text(),'Pending Trades')]//parent::div//following-sibling::div[contains(@class,'portlet')]//table//tbody//tr["+i+"]//td[8]"), sSharesList.get(i-1), "Yes", true, iNoOfShareDecimals);
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

	//Maker/Checker verify available balance Details function.
	public static boolean doVerifyAvailableBalanceDetailsAtMakerAndChecker(Map<String , String> mapRedemptionDetails){
		try {
			boolean bValidStatus = true;
			String appendErrMsg = "";
			if(mapRedemptionDetails.get("ExcpectedActualBalance") != null && mapRedemptionDetails.get("ExcpectedActualShares") != null && mapRedemptionDetails.get("ExcpectedAvailableAmount") != null && mapRedemptionDetails.get("ExcpectedAvailableShares") != null && mapRedemptionDetails.get("ExcpectedIsUnderLockupStatus") != null)
			{
				List<String> actualBalance = Arrays.asList(mapRedemptionDetails.get("ExcpectedActualBalance").split(","));
				List<String> actualShares = Arrays.asList(mapRedemptionDetails.get("ExcpectedActualShares").split(","));
				List<String> availableAmount = Arrays.asList(mapRedemptionDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> avaialableShares = Arrays.asList(mapRedemptionDetails.get("ExcpectedAvailableShares").split(","));
				List<String> isUnderLockup = Arrays.asList(mapRedemptionDetails.get("ExcpectedIsUnderLockupStatus").split(","));

				int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//input[contains(@id,'orderRedemptionMasters0.orderRedemptionDetails') and contains(@id,'actualAmount')]//parent::td"));
				if(xpathCount != actualBalance.size()){
					Messages.errorMsg = "Actual Count of Tax lots is "+xpathCount+" is not matching with the Expected Atual Balance Count "+actualBalance.size()+" ";
					return false;
				}

				for(int i=0 ; i<actualBalance.size() ; i++){
					if(!actualBalance.get(i).equalsIgnoreCase("None")){
						/*String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='orderRedemptionMasters0.orderRedemptionDetails"+i+".actualAmount']//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !actualBalance.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Actual Balance Expected value "+actualBalance.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}*/
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Actual Balance' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath("//input[@id='orderRedemptionMasters0.orderRedemptionDetails"+i+".actualAmount']//parent::td"), actualBalance.get(i), "Yes", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
						if(!bStatus){
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){
						/*String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='orderRedemptionMasters0.orderRedemptionDetails"+i+".actualUnits']//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !actualShares.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Actual Shares Expected value "+actualShares.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}*/
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Actual Shares' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath("//input[@id='orderRedemptionMasters0.orderRedemptionDetails"+i+".actualUnits']//parent::td"), actualShares.get(i), "Yes", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){
						/*String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='availableAmount_"+i+"']//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !availableAmount.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Available Amount Expected value "+actualShares.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}*/
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Available Amount' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath("//input[@id='availableAmount_"+i+"']//parent::td"), availableAmount.get(i), "Yes", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){
						/*String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='availableUnits_"+i+"']//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !avaialableShares.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Available Shares Expected value "+actualShares.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}*/
						bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Available Shares' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath("//input[@id='availableUnits_"+i+"']//parent::td"), avaialableShares.get(i), "Yes", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!isUnderLockup.get(i).equalsIgnoreCase("None")){
						String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='detailsListSize']//following::tr["+(i+1)+"]//td//input[contains(@id,'isUnderLockup')]//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !isUnderLockup.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Is Under Lockup Expected value "+isUnderLockup.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}
					}
				}

			}

			if(mapRedemptionDetails.get("ExcpectedTotalActualBalance") != null && mapRedemptionDetails.get("ExcpectedTotalActualShares") != null && mapRedemptionDetails.get("ExcpectedTotalAvailableAmount") != null && mapRedemptionDetails.get("ExcpectedTotalAvailableShares") != null){
				if(!mapRedemptionDetails.get("ExcpectedTotalActualBalance").equalsIgnoreCase("None")){
					/*String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='totalAmount']//parent::td")).trim().replaceAll(",", "");
					if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !mapRedemptionDetails.get("ExcpectedTotalActualBalance").equalsIgnoreCase(actualValue)){
						appendErrMsg = appendErrMsg+"[ Total Actual Balance Expected value "+mapRedemptionDetails.get("ExcpectedTotalActualBalance")+" is Not matching with the Actual "+actualValue+" value ]";
						bValidStatus = false;
					}*/
					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalActualBalance' in 'Available Balance'", By.xpath("//input[@id='totalAmount']//parent::td"), mapRedemptionDetails.get("ExcpectedTotalActualBalance"), "Yes", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapRedemptionDetails.get("ExcpectedTotalActualShares").equalsIgnoreCase("None")){
					/*String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='totalUnits']//parent::td")).trim().replaceAll(",", "");
					if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !mapRedemptionDetails.get("ExcpectedTotalActualShares").equalsIgnoreCase(actualValue)){
						appendErrMsg = appendErrMsg+"[ Total Actual Shares Expected value "+mapRedemptionDetails.get("ExcpectedTotalActualShares")+" is Not matching with the Actual "+actualValue+" value ]";
						bValidStatus = false;
					}*/
					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalActualShares' in 'Available Balance'", By.xpath("//input[@id='totalUnits']//parent::td"), mapRedemptionDetails.get("ExcpectedTotalActualShares"), "Yes", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapRedemptionDetails.get("ExcpectedTotalAvailableAmount").equalsIgnoreCase("None")){
					/*String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='totalAmountAvailable']//parent::td")).trim().replaceAll(",", "");
					if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !mapRedemptionDetails.get("ExcpectedTotalAvailableAmount").equalsIgnoreCase(actualValue)){
						appendErrMsg = appendErrMsg+"[ Total Available Amount Expected value "+mapRedemptionDetails.get("ExcpectedTotalAvailableAmount")+" is Not matching with the Actual "+actualValue+" value ]";
						bValidStatus = false;
					}*/
					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalAvailableAmount' in 'Available Balance'", By.xpath("//input[@id='totalAmountAvailable']//parent::td"), mapRedemptionDetails.get("ExcpectedTotalAvailableAmount"), "Yes", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapRedemptionDetails.get("ExcpectedTotalAvailableShares").equalsIgnoreCase("None")){
					/*String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='totalUnitsAvailable']//parent::td")).trim().replaceAll(",", "");
					if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !mapRedemptionDetails.get("ExcpectedTotalAvailableShares").equalsIgnoreCase(actualValue)){
						appendErrMsg = appendErrMsg+"[ Total Available Shares Expected value "+mapRedemptionDetails.get("ExcpectedTotalAvailableShares")+" is Not matching with the Actual "+actualValue+" value ]";
						bValidStatus = false;
					}*/
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalAvailableShares' in 'Available Balance'", By.xpath("//input[@id='totalUnitsAvailable']//parent::td"), mapRedemptionDetails.get("ExcpectedTotalAvailableShares"), "Yes", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
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

	public static Map<String , String> dochangeOrderOfTableData(Map<String ,String >mapRedDetails){
		try {
			String sactualAmount = "";
			String sactualShares = "";
			String savailableShares = "";
			String savailableAmount = "";
			String sIsUnderLockup = "";
			String ataxlotAmount = "";
			String ataxlotShares = "";	
			String aExpectedtaxlotSharesOrAmount = "";
			List<String> aTaxLotsAmounts = null;
			List<String> aTaxLotsShares = null;
			List<String> aExpectedTaxLotsSharesOrAmount = null;
			Map<String , String> mapRedemptionDetailsOverride = new HashMap<String, String>();

			List<String> editable = Arrays.asList(mapRedDetails.get("IsTaxLotsAmountOrShareEditable").split(","));
			List<String> actualAmount = Arrays.asList(mapRedDetails.get("ExcpectedActualBalance").split(","));
			List<String> actualShares = Arrays.asList(mapRedDetails.get("ExcpectedActualShares").split(","));
			List<String> availableAmount = Arrays.asList(mapRedDetails.get("ExcpectedAvailableAmount").split(","));
			List<String> availableShares = Arrays.asList(mapRedDetails.get("ExcpectedAvailableShares").split(","));
			List<String> isUnderLockup = Arrays.asList(mapRedDetails.get("ExcpectedIsUnderLockupStatus").split(","));

			if(mapRedDetails.get("TaxLotsAmounts")!=null){
				aTaxLotsAmounts = Arrays.asList(mapRedDetails.get("TaxLotsAmounts").split(","));
				for(int j=0; j<aTaxLotsAmounts.size();j++){
					if(!aTaxLotsAmounts.get(j).equalsIgnoreCase("None")){
						ataxlotAmount = ataxlotAmount+aTaxLotsAmounts.get(j)+",";
					}
				}
				for(int j=0; j<aTaxLotsAmounts.size();j++){
					if(aTaxLotsAmounts.get(j).equalsIgnoreCase("None")){
						ataxlotAmount = ataxlotAmount+aTaxLotsAmounts.get(j)+",";
					}
				}
			}
			if(mapRedDetails.get("TaxLotsShares")!=null){
				aTaxLotsShares = Arrays.asList(mapRedDetails.get("TaxLotsShares").split(","));
				for(int j=0; j<aTaxLotsShares.size();j++){
					if(!aTaxLotsShares.get(j).equalsIgnoreCase("None")){
						ataxlotShares = ataxlotShares+aTaxLotsShares.get(j)+",";
					}
				}
				for(int j=0; j<aTaxLotsShares.size();j++){
					if(aTaxLotsShares.get(j).equalsIgnoreCase("None")){
						ataxlotShares = ataxlotShares+aTaxLotsShares.get(j)+",";
					}
				}
			}
			if(mapRedDetails.get("ExpectedTaxLotAmountOrShare")!=null){
				aExpectedTaxLotsSharesOrAmount = Arrays.asList(mapRedDetails.get("ExpectedTaxLotAmountOrShare").split(","));
				for(int j=0; j<aExpectedTaxLotsSharesOrAmount.size();j++){
					if(!aExpectedTaxLotsSharesOrAmount.get(j).equalsIgnoreCase("None")){
						aExpectedtaxlotSharesOrAmount = aExpectedtaxlotSharesOrAmount+aExpectedTaxLotsSharesOrAmount.get(j)+",";
					}
				}
				for(int j=0; j<aExpectedTaxLotsSharesOrAmount.size();j++){
					if(aExpectedTaxLotsSharesOrAmount.get(j).equalsIgnoreCase("None")){
						aExpectedtaxlotSharesOrAmount = aExpectedtaxlotSharesOrAmount+aExpectedTaxLotsSharesOrAmount.get(j)+",";
					}
				}
			}


			for(int i=0 ;i<editable.size() ;i++){
				if(editable.get(i).equalsIgnoreCase("Yes")){
					sactualAmount = sactualAmount+actualAmount.get(i)+",";
					sactualShares = sactualShares+actualShares.get(i)+",";
					savailableAmount = savailableAmount+availableAmount.get(i)+",";
					savailableShares = savailableShares+availableShares.get(i)+",";
					sIsUnderLockup = sIsUnderLockup+isUnderLockup.get(i)+",";
				}
			}
			for(int i=0 ;i<editable.size() ;i++){
				if(editable.get(i).equalsIgnoreCase("No")){
					sactualAmount = sactualAmount+actualAmount.get(i)+",";
					sactualShares = sactualShares+actualShares.get(i)+",";
					savailableAmount = savailableAmount+availableAmount.get(i)+",";
					savailableShares = savailableShares+availableShares.get(i)+",";
					sIsUnderLockup = sIsUnderLockup+isUnderLockup.get(i)+",";
				}
			}
			sactualAmount = removeUnwantedCommas(sactualAmount);

			sactualShares = removeUnwantedCommas(sactualShares);

			savailableAmount = removeUnwantedCommas(savailableAmount);

			savailableShares = removeUnwantedCommas(savailableShares);

			ataxlotAmount = removeUnwantedCommas(ataxlotAmount);

			ataxlotShares = removeUnwantedCommas(ataxlotShares);

			sIsUnderLockup = removeUnwantedCommas(sIsUnderLockup);

			aExpectedtaxlotSharesOrAmount = removeUnwantedCommas(aExpectedtaxlotSharesOrAmount);


			mapRedDetails.put("ExcpectedActualBalance",sactualAmount);
			mapRedDetails.put("ExcpectedActualShares",sactualShares);
			mapRedDetails.put("ExcpectedAvailableAmount",savailableAmount);	
			mapRedDetails.put("ExcpectedAvailableShares",savailableShares);
			mapRedDetails.put("ExcpectedIsUnderLockupStatus",sIsUnderLockup);

			if(mapRedDetails.get("TaxLotsAmounts")!=null){
				mapRedDetails.put("TaxLotsAmounts", ataxlotAmount);
			}
			if(mapRedDetails.get("TaxLotsShares")!=null){
				mapRedDetails.put("TaxLotsShares", ataxlotShares);
			}

			if(mapRedDetails.get("ExpectedTaxLotAmountOrShare")!=null){
				mapRedDetails.put("ExpectedTaxLotAmountOrShare", aExpectedtaxlotSharesOrAmount);
			}
			mapRedemptionDetailsOverride.putAll(mapRedDetails);
			return mapRedemptionDetailsOverride;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static boolean chekerVerifyAvailableBalancAllocatedeAMountOrShare(Map<String ,String>mapRedemptionDetails){
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		List<String> sAmountOrSharesList = null;
		List<String> sVerifyAmountOrSharesList = null;
		List<String> sNoOfRecords = null;
		int noOfDecimalsDisplay = -1;
		int noOfVerifyDecimalsDisplay = -1;
		try {
			String sIsAmountOrShare = "";
			String verifyAmountOrShare = "";
			if (mapRedemptionDetails.get("TaxLotsAmounts") != null || mapRedemptionDetails.get("TaxLotsShares") != null) {
				if (mapRedemptionDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShare = "Share";
					noOfDecimalsDisplay = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals"));
					noOfVerifyDecimalsDisplay = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals"));
				}
				else if (mapRedemptionDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShare = "Amount";
					noOfDecimalsDisplay = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals"));
					noOfVerifyDecimalsDisplay = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals"));
				}
			}

			if(mapRedemptionDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList = Arrays.asList(mapRedemptionDetails.get("ExpectedTaxLotAmountOrShare").split(","));
			}

			if(mapRedemptionDetails.get("IsTaxLotsAmountOrShareEditable") != null){
				sNoOfRecords = Arrays.asList(mapRedemptionDetails.get("IsTaxLotsAmountOrShareEditable").split(","));
			}
			int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]//input[contains(@id,'redLot')]//parent::span"));				
			//int iNoOfLots = Integer.parseInt(NoOfRecords);
			if (NoOfRecords != sAmountOrSharesList.size()) {
				Messages.errorMsg = "[ ERROR : Expected No lots : '"+sAmountOrSharesList.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
				return false;
			}
			if (mapRedemptionDetails.get("Full Redemption")!=null && mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("No")) {
				//bStatus = doVerifyAllocatedDetailsInTaxlots(mapRedemptionDetails);
				for (int i = 0; i < sAmountOrSharesList.size(); i++) {
					if(!sAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.id("allocated"+sIsAmountOrShare+"_"+i+""), sAmountOrSharesList.get(i), "No", true, noOfDecimalsDisplay);
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
					if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot '"+verifyAmountOrShare+"' at index : '"+i+"'", By.id("allocated"+verifyAmountOrShare+"_"+i+""), sVerifyAmountOrSharesList.get(i), "No", true, noOfVerifyDecimalsDisplay);
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}		

				}	
			}

			if (mapRedemptionDetails.get("Full Redemption")!=null && mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("Yes")) {
				bStatus = doVerifyFullRedemptionValuesInAvaialableBalance(mapRedemptionDetails);
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

	private static boolean doVerifyAllocatedDetailsInTaxlots(Map<String, String> mapRedemptionDetails) {
		try {
			if(mapRedemptionDetails.get("CheckerExpectedAllocatedAmount") != null && mapRedemptionDetails.get("CheckerExpectedAllocatedShare") != null){
				List<String> allocatedAmount = Arrays.asList(mapRedemptionDetails.get("CheckerExpectedAllocatedAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapRedemptionDetails.get("CheckerExpectedAllocatedShare").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally";
					return false;

				}

				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]"));
				if(allocatedAmount.size() != NoOfRecords){
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+allocatedAmount.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}

				int noOfDecimalsToDispaly = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals"));
				int noOfShareDecimalsToDispaly = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals"));

				for(int i=0; i<allocatedAmount.size(); i++){
					if(!allocatedAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot 'Allocated Amount' at index : '"+i+"'", By.id("allocatedAmount_"+i+""), allocatedAmount.get(i), "No", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							return false;
						}
					}

					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot 'Allocated Share' at index : '"+i+"'", By.id("allocatedShare_"+i+""), allocatedShares.get(i), "No", true, noOfShareDecimalsToDispaly);
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

	public static boolean doFillAvailableBalanceAtMaker(Map<String ,String> mapRedemptionDetails){
		List<String> sAmountOrSharesList = null;
		List<String> sVerifyAmountOrSharesList = null;
		try {

			String sIsAmountOrShare = "";
			String verifyAmountOrShares = "";

			if (mapRedemptionDetails.get("Redemption Method Applied") != null) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='s2id_redmethodApplied']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Page cannot scroll to Available Balance Details";
					return false;
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapRedemptionDetails.get("Redemption Method Applied"), By.xpath("//div[@id='s2id_redmethodApplied']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to select the 'Redemption Method Applied' option : "+mapRedemptionDetails.get("Redemption Method Applied")+" ]\n";
					return false;
				}
			}

			//Split Taxlots Amount or Share
			if (mapRedemptionDetails.get("TaxLotsAmounts") != null || mapRedemptionDetails.get("TaxLotsShares") != null) {
				if (mapRedemptionDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "Amount";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShares = "Share";
				}
				else if (mapRedemptionDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "Share";
					sAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShares = "Amount";
				}
			}

			//Split Expected Taxlots Amount or Share
			if(mapRedemptionDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList =  Arrays.asList(mapRedemptionDetails.get("ExpectedTaxLotAmountOrShare").split(","));
			}

			if (!sIsAmountOrShare.equalsIgnoreCase("") && mapRedemptionDetails.get("Full Redemption")!=null && mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("No")) {			
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]"), 3);
				if(!bStatus){
					Messages.errorMsg = "Tax lots are not visible";
					return false;
				}

				bStatus = doEnterAmountOrShareInAvailableBalance(sAmountOrSharesList,sIsAmountOrShare,verifyAmountOrShares,sVerifyAmountOrSharesList,Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					return false;
				}				
			}

			if (mapRedemptionDetails.get("Full Redemption")!=null && mapRedemptionDetails.get("Full Redemption").equalsIgnoreCase("Yes")) {
				bStatus = doVerifyFullRedemptionValuesInAvaialableBalance(mapRedemptionDetails);
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

	private static boolean doVerifyFullRedemptionValuesInAvaialableBalance(Map<String, String> mapRedemptionDetails) {
		try {

			if(mapRedemptionDetails.get("ExcpectedAvailableAmount") != null && mapRedemptionDetails.get("ExcpectedAvailableShares") != null){
				List<String> allocatedAmount = Arrays.asList(mapRedemptionDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapRedemptionDetails.get("ExcpectedAvailableShares").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally";
					return false;

				}

				int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]"));
				if(allocatedAmount.size() != NoOfRecords){
					Messages.errorMsg = "[ ERROR : Expected No lots : '"+allocatedAmount.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
					return false;
				}

				int noOfDecimalsToDispaly = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals"));
				int noOfShareDecimalsToDispaly = Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals"));

				for(int i=0; i<allocatedAmount.size(); i++){
					if(!allocatedAmount.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot 'Amount' at index : '"+i+"'", By.id("allocatedAmount_"+i+""), allocatedAmount.get(i), "No", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							return false;
						}
					}

					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot 'Share' at index : '"+i+"'", By.id("allocatedShare_"+i+""), allocatedShares.get(i), "No", true, noOfShareDecimalsToDispaly);
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


	private static boolean doEnterAmountOrShareInAvailableBalance(List<String> sAmountOrSharesList, String sIsAmountOrShare,String verifyAmountOrShares, List<String> sVerifyAmountOrSharesList, int noOfDecimalsToDispaly,int noOfShareDecimals) {
		try {

			int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-redLot')]"));
			if(sAmountOrSharesList.size() != NoOfRecords){
				Messages.errorMsg = "[ ERROR : Expected No lots : '"+sAmountOrSharesList.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
				return false;
			}
			if(verifyAmountOrShares.equalsIgnoreCase("Share")){
				noOfDecimalsToDispaly = noOfShareDecimals;
			}
			for(int i=0; i<sAmountOrSharesList.size();i++)
			{
				if(!sAmountOrSharesList.get(i).equalsIgnoreCase("None"))
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='redLot_"+i+"']//parent::span[@class='checked']"), 3);
					if(bStatus)
					{
						By locator = By.id("allocated"+sIsAmountOrShare+"_"+i+"");
						bStatus = ClearAndSetText(locator, sAmountOrSharesList.get(i));							
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to enter the text into the Redemption Lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
							return false;
						}
						bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
						if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.id("allocated"+verifyAmountOrShares+"_"+i+""), sVerifyAmountOrSharesList.get(i), "No", true, noOfDecimalsToDispaly);
							if (!bStatus) {
								return false;
							}
						}

					}
					else
					{
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='redLot_"+i+"']//parent::span"));
						if (!bStatus) {
							Messages.errorMsg = "[ERROR : Unable to Select the Redemption tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
							return false;
						}

						By locator = By.id("allocated"+sIsAmountOrShare+"_"+i+"");
						bStatus = ClearAndSetText(locator, sAmountOrSharesList.get(i));							
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to enter the text into the Redemption Lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
							return false;
						}
						bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
						if(sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Redemption tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.id("allocated"+verifyAmountOrShares+"_"+i+""), sVerifyAmountOrSharesList.get(i), "No", true, noOfDecimalsToDispaly);
							if (!bStatus) {
								return false;
							}
						}						

					}
				}
				else
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='redLot_"+i+"']//parent::span[@class='checked']"), 3);
					if(bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='redLot_"+i+"']//parent::span[@class='checked']"));
						if(!bStatus){
							Messages.errorMsg = "Redemption Slot Cannot be Unchchecked";
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

	public static String removeUnwantedCommas(String valueToRemove){
		try {

			valueToRemove = valueToRemove.replaceFirst("^,","");
			valueToRemove = valueToRemove.replace(",,",",");
			valueToRemove = valueToRemove.replaceAll(",$", "");

			return valueToRemove;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
}
