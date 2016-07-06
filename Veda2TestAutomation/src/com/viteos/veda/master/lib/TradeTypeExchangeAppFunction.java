package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;

public class TradeTypeExchangeAppFunction {
	static boolean bStatus;
	static String locator = "//div[contains(normalize-space(),'To Fund Details')]/span//parent::div//parent::div//following::div[@class='portlet-body form']//div[@data-original-title='ValueToChange']/input";
	static String checkerAvailableBalanceTableLocator = "//div[@class='sub-caption' and contains(normalize-space(),'Available Balance')]//parent::div//following::div//table//tbody";
	public static boolean isExchangeFromChekerReviewed = false;
	//public static boolean isExchangeForChekerReviewedMaker = false;
	//public static Map<String , String > mapOverrideExcahngeDetails = new HashMap<>();
	public static boolean doFillExchangeTradeDetails(Map<String , String>mapExchangeDetails){
		try {

			bStatus = doFillRequestDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillInvestorDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillFromFundDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doVerifyAndFillToFundDetails(mapExchangeDetails);

			if(!bStatus){
				return false;
			}

			bStatus = doFillExchangeDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			String proceedLocator = "//button[contains(@onclick,'javascript:proceed') and contains(normalize-space(),'Proceed')]";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(proceedLocator));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Cannot scroll to Proceed button ]\n";
				return false;
			}

			bStatus = Elements.click(Global.driver, By.xpath(proceedLocator));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Proceed butotn cannot be clicked ]\n";
				return false;
			}


			bStatus = doMakerVerifyRequestDetailsTab(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyInvestorDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyFromFundDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyToFundDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyExchangeDetailsAtSecondScreen(mapExchangeDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doFillAvailableBalanceAtMaker(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doVerifyAvailableBalanceDetailsAtMakerAndChecker(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			if(mapExchangeDetails.get("ExpectedPendingRequestAmount") !=null && mapExchangeDetails.get("ExpectedPendingRequestedShares") !=null && mapExchangeDetails.get("ExpectedNumberOfDecimals") !=null){
				bStatus = TradeTypeTransferAppFunctions.doMakerAndCheckerVerifyPendingTradesDetails(mapExchangeDetails.get("ExpectedPendingRequestAmount"), mapExchangeDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					return false;
				}		
			}

			bStatus = doOverrideOrVerifyChargeDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyAmountDetailsTab(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doSelectOtherDetailsRadioButtons(mapExchangeDetails);
			if(!bStatus){
				return false;
			}


			if(mapExchangeDetails.get("OperationType")!=null){
				bStatus = doSubOperationsOnTransactionTrades("Redemption", mapExchangeDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapExchangeDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Proceed for Approval Button is Not Visible";
						return false;
					}
					bStatus = doVerifyMakerExceptions(mapExchangeDetails.get("ExpectedExceptionsAtMaker"));
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with Expected");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
						if(!bStatus){
							Messages.errorMsg = "Cannot close the Exceptions ";
							return false;
						}
						Messages.errorMsg = errorMsg;
						return false;
					}
					if(mapExchangeDetails.get("OperationType").equalsIgnoreCase("Save") || mapExchangeDetails.get("OperationType").contains("Review"))
					{

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"));
						if(!bStatus){
							Messages.errorMsg = "Proceed Button Cannot be clicked";
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
							return false;
						}

					}

				}/*else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions are Not matching");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
						if(!bStatus){
							Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception and Cannot close the Exceptions ";
							return false;
						}
						Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception ";
						return false;
					}

				}*/
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"), Constants.iPopupWaitingTime);
				if(bStatus){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]"));
					if(!bStatus){
						Messages.errorMsg = "Proceed Button Cannot be clicked";
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
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
			return true;
		}
	}

	public static boolean doVerifyExchangeTradeAtChecker(Map<String , String> mapExchangeDetails){
		try {
			String appendErrorMsg = "";
			boolean bValidStatus = true;
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Checker Page is Not Visible";
				return false;
			}
			bStatus = doVerifyCheckerRequestDetails(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVerifyInvestorDetailsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVerifyFromFundDetialsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVerifyToFundDetailsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVerifyExchangeDetailsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			/*mapOverrideExcahngeDetails = dochangeOrderOfTableData(mapExchangeDetails);
			if(mapOverrideExcahngeDetails == null || mapOverrideExcahngeDetails.isEmpty()){
				appendErrorMsg = appendErrorMsg+"Available balance values Order is Not changed to verify";
				bValidStatus = false;
			}*/

			/*bStatus = doVerifyAllocatedAmountandShareAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}*/
			
			bStatus = doVerifyAllocatedAmountAndShare(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}
			
			bStatus = doverifyAvailableBalanceDetailsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			if (mapExchangeDetails.get("ExpectedPendingRequestAmount") != null && mapExchangeDetails.get("ExpectedPendingRequestedShares") != null && mapExchangeDetails.get("ExpectedNumberOfDecimals") != null) {
				bStatus = TradeTypeTransferAppFunctions.doMakerAndCheckerVerifyPendingTradesDetails(mapExchangeDetails.get("ExpectedPendingRequestAmount"), mapExchangeDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals")));
				if (!bStatus) {
					appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}

			bStatus = doVerifyChargeDetailsinChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doCheckerVerifyAmountDetailsTab(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doCheckerVerifyOtherDetails(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			if(mapExchangeDetails.get("ExpectedExceptionsAtChecker")!=null){
				bStatus =doCheckerVerifyExceptionsAtCheckerForTradeTypeExchange(mapExchangeDetails.get("ExpectedExceptionsAtChecker"));
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

	public static boolean doCheckerOperationsForExchangeTrade(Map<String ,String> mapExchangeDetails){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Detail')]"),Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not visible for checker operations";
				return false;
			}
			
			//download OA and Delete
			if(mapExchangeDetails.get("isOAClient") != null){
				bStatus = TradeTypeSubscriptionAppFunctions.verifyOrderAcknowledgementDownload(mapExchangeDetails);
				if(!bStatus && mapExchangeDetails.get("isOAClient").equalsIgnoreCase("Yes")){
					return false;
				}
				if(bStatus && mapExchangeDetails.get("isOAClient").equalsIgnoreCase("No")){
					Messages.errorMsg = "Order Acknowldegement Downloaded for Non OA Client";
					return false;
				}
			}

			if(mapExchangeDetails.get("Approve OA")!=null && mapExchangeDetails.get("Approve OA").equalsIgnoreCase("Yes")){
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

			bStatus = changeOverrideStatus(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = TradeTypeRedemptionAppFunctions.performCheckerOperationOnTrade(mapExchangeDetails.get("CheckerOperations"));
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

	//Verify Avaialble balance at Checker
	private static boolean doverifyAvailableBalanceDetailsAtChecker(Map<String, String> mapExchangeDetails) {
		try {
			boolean bValidStatus = true;
			String appendErrMsg = "";
			int noOfDecimalsToDispaly = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals"));
			int noOfShareDecimalsToDispaly = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals"));
			if(mapExchangeDetails.get("ExcpectedActualBalance") != null && mapExchangeDetails.get("ExcpectedActualShares") != null && mapExchangeDetails.get("ExcpectedAvailableAmount") != null && mapExchangeDetails.get("ExcpectedAvailableShares") != null)
			{
				List<String> actualBalance = Arrays.asList(mapExchangeDetails.get("ExcpectedActualBalance").split(","));
				List<String> actualShares = Arrays.asList(mapExchangeDetails.get("ExcpectedActualShares").split(","));
				List<String> availableAmount = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> avaialableShares = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableShares").split(","));


				int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
				if(xpathCount != actualBalance.size()){
					Messages.errorMsg = "Actual Count of Tax lots is "+xpathCount+" is not matching with the Expected Atual Balance Count "+actualBalance.size()+" ";
					return false;
				}

				for(int i=0 ; i<actualBalance.size() ; i++){
					if(!actualBalance.get(i).equalsIgnoreCase("None")){

						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Actual Balance' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]//td[5]/span"), actualBalance.get(i), "Yes", true, noOfDecimalsToDispaly);
						if(!bStatus){
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){

						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Actual Shares' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]//td[6]/span"), actualShares.get(i), "Yes", true, noOfShareDecimalsToDispaly);
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){

						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Available Amount' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]//td[7]/span"), availableAmount.get(i), "Yes", true,noOfDecimalsToDispaly);
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){

						bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Available Shares' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]//td[8]/span"), avaialableShares.get(i), "Yes", true,noOfShareDecimalsToDispaly);
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
				}

			}

			if(mapExchangeDetails.get("ExcpectedTotalActualBalance") != null && mapExchangeDetails.get("ExcpectedTotalActualShares") != null && mapExchangeDetails.get("ExcpectedTotalAvailableAmount") != null && mapExchangeDetails.get("ExcpectedTotalAvailableShares") != null){
				if(!mapExchangeDetails.get("ExcpectedTotalActualBalance").equalsIgnoreCase("None")){
					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalActualBalance' in 'Available Balance'", By.xpath("//strong[text()='Total']//parent::td//following-sibling::td[1]/span"), mapExchangeDetails.get("ExcpectedTotalActualBalance"), "Yes", true, noOfDecimalsToDispaly);
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapExchangeDetails.get("ExcpectedTotalActualShares").equalsIgnoreCase("None")){

					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalActualShares' in 'Available Balance'", By.xpath("//strong[text()='Total']//parent::td//following-sibling::td[2]/span"), mapExchangeDetails.get("ExcpectedTotalActualShares"), "Yes", true, noOfShareDecimalsToDispaly);
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapExchangeDetails.get("ExcpectedTotalAvailableAmount").equalsIgnoreCase("None")){

					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalAvailableAmount' in 'Available Balance'", By.xpath("//strong[text()='Total']//parent::td//following-sibling::td[3]/span"), mapExchangeDetails.get("ExcpectedTotalAvailableAmount"), "Yes", true, noOfDecimalsToDispaly);
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapExchangeDetails.get("ExcpectedTotalAvailableShares").equalsIgnoreCase("None")){

					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalAvailableShares' in 'Available Balance'", By.xpath("//strong[text()='Total']//parent::td//following-sibling::td[4]/span"), mapExchangeDetails.get("ExcpectedTotalAvailableShares"), "Yes", true, noOfShareDecimalsToDispaly);
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

	private static boolean doVerifyAllocatedAmountandShareAtChecker(Map<String, String> mapExchangeDetails) {
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		List<String> sAmountOrSharesList = null;
		List<String> sVerifyAmountOrSharesList = null;
		int noOfDecimalsDisplay = -1;
		int noOfVerifyDecimalsDisplay = -1;
		try {
			String sIsAmountOrShare = "";
			String verifyAmountOrShares = "";
			String columnNumber = "";
			String verifyColumnNumber = "";

			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "6");
			if (!bStatus) {
				return false;
			}
			//Split Taxlots Amount or Share
			if (mapExchangeDetails.get("TaxLotsAmounts") != null || mapExchangeDetails.get("TaxLotsShares") != null) 
			{
				if (mapExchangeDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "amount";
					columnNumber = "11";
					noOfDecimalsDisplay = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals"));
					sAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShares = "share";
					verifyColumnNumber = "12";
					noOfVerifyDecimalsDisplay = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals"));
				}
				else if (mapExchangeDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "share";
					columnNumber = "12";
					noOfDecimalsDisplay = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals"));
					sAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShares = "amount";
					verifyColumnNumber = "11";
					noOfVerifyDecimalsDisplay = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals"));
				}
			}

			//Split Expected Taxlots Amount or Share
			if(mapExchangeDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("ExpectedTaxLotAmountOrShare").split(","));
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"), 3);
			if(!bStatus){
				Messages.errorMsg = "Tax lots are not visible";
				return false;
			}

			int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
			if(sAmountOrSharesList.size() != NoOfRecords){
				Messages.errorMsg = "[ ERROR : Expected No lots : '"+sAmountOrSharesList.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
				return false;
			}

			if (!sIsAmountOrShare.equalsIgnoreCase("") && mapExchangeDetails.get("Full Exchange")!=null && mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("No")) 
			{			

				for (int i = 0; i < sAmountOrSharesList.size(); i++) {
					if(!sAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+sIsAmountOrShare+"' at index : '"+i+"'", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]/td["+columnNumber+"]"), sAmountOrSharesList.get(i), "Yes", true, noOfDecimalsDisplay);
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}

					if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]/td["+verifyColumnNumber+"]"), sVerifyAmountOrSharesList.get(i), "Yes", true, noOfVerifyDecimalsDisplay);
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
				}			
			}

			if (mapExchangeDetails.get("Full Exchange")!=null && mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("Yes")) {
				bStatus = doVerifyFullExchangeValuesInAvaialableBalanceAtChecker(mapExchangeDetails);
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

	private static boolean doVerifyFullExchangeValuesInAvaialableBalanceAtChecker(Map<String, String> mapExchangeDetails) {
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;
			if(mapExchangeDetails.get("ExcpectedAvailableAmount") != null && mapExchangeDetails.get("ExcpectedAvailableShares") != null){
				List<String> allocatedAmount = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableShares").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally";
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
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+allocatedAmount.get(i)+"' at index : '"+i+"'", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]/td[11]/span"), allocatedAmount.get(i), "Yes", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
							bValidStatus = false;
						}
					}

					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+allocatedShares.get(i)+"' at index : '"+i+"'", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]/td[12]/span"), allocatedShares.get(i), "Yes", true, noOfShareDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
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
	
	public static boolean doVerifyAllocatedAmountAndShare(Map<String,String>mapExchangeDetails){
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;
			if(mapExchangeDetails.get("CheckerExpectedAllocatedAmount") != null && mapExchangeDetails.get("CheckerExpectedAllocatedShare") != null){
				List<String> allocatedAmount = Arrays.asList(mapExchangeDetails.get("CheckerExpectedAllocatedAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapExchangeDetails.get("CheckerExpectedAllocatedShare").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally";
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
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+allocatedAmount.get(i)+"' at index : '"+i+"'", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]/td[11]/span"), allocatedAmount.get(i), "Yes", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
							bValidStatus = false;
						}
					}

					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+allocatedShares.get(i)+"' at index : '"+i+"'", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]/td[12]/span"), allocatedShares.get(i), "Yes", true, noOfShareDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
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

	public static boolean doVerifyAllocatedAmountInChecker(Map<String,String>mapExchangeDetails){
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;
			if(mapExchangeDetails.get("CheckerExpectedAllocatedAmount") != null && mapExchangeDetails.get("CheckerExpectedAllocatedShare") != null){
				List<String> allocatedAmount = Arrays.asList(mapExchangeDetails.get("CheckerExpectedAllocatedAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapExchangeDetails.get("CheckerExpectedAllocatedShare").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Allocated Amount "+allocatedAmount+" and Allocated Share "+allocatedShares+" Values are Not Provided equally";
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
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot 'Allocated Amount' value '"+allocatedAmount.get(i)+"' at index : '"+i+"'", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]/td[11]/span"), allocatedAmount.get(i), "Yes", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
							bValidStatus = false;
						}
					}

					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot 'Allocated Share' value '"+allocatedShares.get(i)+"' at index : '"+i+"'", By.xpath(checkerAvailableBalanceTableLocator+"//tr["+(i+1)+"]/td[12]/span"), allocatedShares.get(i), "Yes", true, noOfShareDecimalsToDispaly);
						if (!bStatus) {
							appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
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

	private static boolean doVerifyExchangeDetailsAtChecker(Map<String, String> mapExchangeDetails) {
		try{
			String appendErrMsg = "";
			boolean bValidStatus = true;
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "5");
			if (!bStatus) {
				return false;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='effectiveDate']"));
			if(!bStatus){
				Messages.errorMsg = "Page cannot scroll to Exchange Details in second screen";
				return false;
			}

			//Select Full Transfer  Radio button
			if(mapExchangeDetails.get("Full Exchange")!=null){
				if(mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(text(),'Full Exchange')]//following-sibling::div//label[contains(text(),'Yes')]//span[@class='checked']/input") , Constants.lTimeOut);
					if(!bStatus){
						appendErrMsg = appendErrMsg+"[ Error : Expected Full Exchange Radio button 'Yes' is Not matching with the Actual ]\n";
						bValidStatus = false;
					}
					String percentageValue = "100";
					String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='percentage']");
					/*bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Percentage", Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")), actualValue);
					if(!bStatus){
						appendErrMsg = appendErrMsg+Messages.errorMsg;
						bValidStatus = false;
					}*/
					if(Float.parseFloat(actualValue) != Float.parseFloat(percentageValue)){
						appendErrMsg = appendErrMsg+"[ Error : Percentage Value is not Changed to 100 even Full Exchange 'Yes' button selected]\n";
						bValidStatus = false;
					}
				}
				if(mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(text(),'Full Exchange')]//following-sibling::div//label[contains(text(),'No')]//span[@class='checked']/input") , Constants.lTimeOut);
					if(!bStatus){
						appendErrMsg = appendErrMsg+"[ Error : Expected Full Exchange Radio button 'No' is Not Maching with the Actual]\n";
						bValidStatus = false;
					}
				}
			}

			//Enter Effective Date
			if( mapExchangeDetails.get("Effective Date")!=null){
				String effectiveDate = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='effectiveDate']"), "value");

				if(!effectiveDate.equalsIgnoreCase(mapExchangeDetails.get("Effective Date"))){
					appendErrMsg = appendErrMsg+"[ Error : Effective Date Actual "+effectiveDate+" is Not matching with the Expected "+mapExchangeDetails.get("Effective Date")+" ]\n";
					bValidStatus = false;
				}
			}

			//Enter Percentage
			if(mapExchangeDetails.get("Percentage")!=null){
				String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='percentage']");
				/*	bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Percentage", Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appendErrMsg = appendErrMsg+Messages.errorMsg;
					bValidStatus = false;
				}*/

				if(Float.parseFloat(actualValue) != Float.parseFloat(mapExchangeDetails.get("Percentage"))){
					appendErrMsg = appendErrMsg+"[ Error : Expected Percentage Value is "+mapExchangeDetails.get("Percentage")+" not matching with the Actual value "+actualValue+" ]\n";
					bValidStatus = false;
				}
			}

			//Select Amount/Share Radio button
			if(mapExchangeDetails.get("AmountorShares")!=null){
				if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//span[@class='checked']/input[@id='amountShare1']") , Constants.lTimeOut);
					if(!bStatus){
						appendErrMsg = appendErrMsg+"[ Error : Expected Amount Radio button is not Matching with the Actual]\n";
						bValidStatus = false;
					}
					if(mapExchangeDetails.get("Amount Value")!=null){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Amount Value", By.xpath("//input[@id='amountValue']"), mapExchangeDetails.get("Amount Value"), "No", true, Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")));
						if(!bStatus){
							appendErrMsg = appendErrMsg+Messages.errorMsg;
							bValidStatus = false;
						}
					}
				}
				if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//span[@class='checked']/input[@id='amountShare2']") , Constants.lTimeOut);
					if(!bStatus){
						appendErrMsg = appendErrMsg+"[ Error :Expected Shares Radio button is not Matching with the Actual ]\n";
						bValidStatus = false;
					}
					if(mapExchangeDetails.get("Share Value")!=null){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share Value", By.xpath("//input[@id='share']"), mapExchangeDetails.get("Share Value"), "No", true, Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals")));
						if(!bStatus){
							appendErrMsg = appendErrMsg+Messages.errorMsg;
							bValidStatus = false;
						}
					}
				}
			}

			if (mapExchangeDetails.get("Expected NAV Date") != null) {				
				String sValue = Elements.getText(Global.driver, By.xpath("//label[contains(text(),'NAV Date')]//following-sibling::label"));
				if (sValue == null || sValue.equalsIgnoreCase("") || !TradeTypeSubscriptionAppFunctions.formatDate(sValue.trim()).equalsIgnoreCase(mapExchangeDetails.get("Expected NAV Date"))) {
					appendErrMsg = appendErrMsg+"Expected NAV Date is "+mapExchangeDetails.get("Expected NAV Date")+" Not matching with the Actual NAV Date "+sValue+"";
					bValidStatus = false;
				}
				/*if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("NAV Date", By.xpath("//input[contains(@id,'navDate')]"), mapRedemptionDetails.get("Expected NAV Date"), "No", false)) {
					return false;
				}*/
			}
			Messages.errorMsg = appendErrMsg;
			return bValidStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doVerifyToFundDetailsAtChecker(Map<String, String> mapExchangeDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "4");
			if (!bStatus) {
				return false;
			}

			//select Client Name
			if(mapExchangeDetails.get("From Client Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Client Name ", By.xpath(locator.replace("ValueToChange", "Client Name")), mapExchangeDetails.get("From Client Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Fund Family
			if(mapExchangeDetails.get("From Fund Family Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Fund Family Name ", By.xpath(locator.replace("ValueToChange", "Fund Family Name")), mapExchangeDetails.get("From Fund Family Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Legal Entity
			if(mapExchangeDetails.get("From Legal Entity Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Legal Entity Name ", By.xpath(locator.replace("ValueToChange", "Legal Entity Name")), mapExchangeDetails.get("From Legal Entity Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Class Name
			if(mapExchangeDetails.get("To Class Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Class Name ", By.xpath(locator.replace("ValueToChange", "Class Name")), mapExchangeDetails.get("To Class Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			// Select the Series Name
			if(mapExchangeDetails.get("To Series Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Series Name ", By.xpath(locator.replace("ValueToChange", "Series Name")), mapExchangeDetails.get("To Series Name"), "No", false);				
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

	private static boolean doVerifyFromFundDetialsAtChecker(Map<String, String> mapExchangeDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "3");
			if (!bStatus) {
				return false;
			}
			//Verify Client Name
			if(mapExchangeDetails.get("From Client Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Client Name ", By.xpath("//div[@data-original-title='Client Name']/input"), mapExchangeDetails.get("From Client Name"), "No", false);				
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Verify Fund Family
			if(mapExchangeDetails.get("From Fund Family Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Fund Family Name ", By.xpath("//div[@data-original-title='Fund Family Name']/input"), mapExchangeDetails.get("From Fund Family Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Verify Legal Entity
			if(mapExchangeDetails.get("From Legal Entity Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Legal Entity Name ", By.xpath("//div[@data-original-title='Legal Entity Name']/input"), mapExchangeDetails.get("From Legal Entity Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//Verify Class Name
			if(mapExchangeDetails.get("From Class Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Class Name ", By.xpath("//div[@data-original-title='Class Name']/input"), mapExchangeDetails.get("From Class Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			// Verify the Series Name
			if(mapExchangeDetails.get("From Series Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Series Name ", By.xpath("//div[@data-original-title='Series Name']/input"), mapExchangeDetails.get("From Series Name"), "No", false);
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

	private static boolean doVerifyInvestorDetailsAtChecker(Map<String, String> mapExchangeDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try{
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "2");
			if (!bStatus) {
				return false;
			}
			if(mapExchangeDetails.get("Investor Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Investor Name ", By.xpath("//div[@data-original-title='Investor Name']/input"), mapExchangeDetails.get("Investor Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Holder Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Holder Name ", By.xpath("//div[@data-original-title='Holder Name']/input"), mapExchangeDetails.get("Holder Name"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Account ID")!=null){
				Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapExchangeDetails.get("Account ID"));
				if (mapXMLExchangeDetails != null && !mapXMLExchangeDetails.isEmpty() && mapXMLExchangeDetails.get("AccountID") != null) {
					mapExchangeDetails.put("Account ID", mapXMLExchangeDetails.get("AccountID"));
				}else {
					Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapExchangeDetails.get("Account ID"));
					if (mapXMLAccountMasterDetails != null && !mapXMLAccountMasterDetails.isEmpty() && mapXMLAccountMasterDetails.get("AccountID") != null) {
						mapExchangeDetails.put("Account ID", mapXMLAccountMasterDetails.get("AccountID"));
					}
				}

				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Account ID", By.xpath("//div[@data-original-title='Account ID']/input"), mapExchangeDetails.get("Account ID"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}

			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doVerifyCheckerRequestDetails(Map<String, String> mapExchangeDetails) {
		try {
			boolean bValidateStatus = true;
			String sAppendErrorMsg = "";
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "1");
			if (!bStatus) {
				return false;
			}

			if(mapExchangeDetails.get("Request Received Date")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Request Received Date ", By.xpath("//input[@id='requestDate']"), mapExchangeDetails.get("Request Received Date"), "No", false);				
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg +"[ "+ Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(mapExchangeDetails.get("Request Received Time")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Request Received Time", By.id("requestTime"), mapExchangeDetails.get("Request Received Time"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg +"[ "+ Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Order Received Office")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Order Received Office", By.xpath("//input[@data-original-title='Order Received Office']"), mapExchangeDetails.get("Order Received Office"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Time Zone")!=null){
				String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@data-original-title='Time Zone']");
				if(!actualValue.equalsIgnoreCase(mapExchangeDetails.get("Time Zone"))){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}			
			if(mapExchangeDetails.get("Source")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Source", By.xpath("//input[@placeholder='Source']"), mapExchangeDetails.get("Source"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}				
			}			
			if(mapExchangeDetails.get("Mode of Request")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Mode Of Request ", By.xpath("//label[contains(text(),'Mode of Request')]//following-sibling::input"), mapExchangeDetails.get("Mode of Request"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("External ID Number")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("External ID Number", By.xpath("//input[@placeholder='External ID Number']"), mapExchangeDetails.get("External ID Number"), "No", false);
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

	//Filling Request Details.
	public static boolean doFillRequestDetails(Map<String, String> mapExchangeDetails) {
		try {

			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "1");
			if (!bStatus) {
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@class='sub-caption']/span[text()='1']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Request Details are Not visible ] \n";
				return false;
			}
			if(mapExchangeDetails.get("Request Received Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Received Date']/following::div/input[contains(@id,'requestDate')]"), mapExchangeDetails.get("Request Received Date"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Request Received Date Not Entered ] \n";
					return false;
				}
			}
			
			if(mapExchangeDetails.get("Request Received Time")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Received Time']/following::div/input[contains(@id,'requestTime')]"), mapExchangeDetails.get("Request Received Time"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Request Received Time Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Exchange')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Failed to click on Exchange Label ] \n";
					return false;
				}
			}
			if(mapExchangeDetails.get("Order Received Office")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Order Received Office']/following::div/input[@placeholder='Order Received Office']"), mapExchangeDetails.get("Order Received Office"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Order Received Office Not Entered ] \n";
					return false;
				}
			}
			if(mapExchangeDetails.get("Time Zone")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@placeholder='Time Zone']"), mapExchangeDetails.get("Time Zone"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Time Zone Not Entered ] \n";
					return false;
				}
			}
			if(mapExchangeDetails.get("Source")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Source']/following::input[@id='tblOrderTransferMaster.source']"), mapExchangeDetails.get("Source"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Source Not Entered ] \n";
					return false;
				}
			}
			if(mapExchangeDetails.get("Mode of Request")!=null){
				if(!isExchangeFromChekerReviewed){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("Mode of Request"), By.xpath("//label[normalize-space()='Mode Of Request']/following::div[@id='s2id_tblOrderTransferMaster.modeOfRequest.referenceIdPk']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Mode of Request Drop down Not Selected ] \n";
						return false;
					}
				}else{
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("Mode of Request"), By.xpath("//label[normalize-space()='Mode of Request']/following::div[@id='s2id_tblOrderTransferMaster.modeOfRequest.referenceIdPk']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Mode of Request Drop down Not Selected ] \n";
						return false;
					}
				}

			}
			if(mapExchangeDetails.get("External ID Number")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='External ID Number']/following::input[contains(@id,'externalId')]"), mapExchangeDetails.get("External ID Number"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : External ID Not Entered ] \n";
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

	// Verify Request Details.
	public static boolean doMakerVerifyRequestDetailsTab(Map<String, String> mapExchangeDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "1");
			if (!bStatus) {
				return false;
			}
			if(mapExchangeDetails.get("Request Received Date")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Request Received Date ", By.xpath("//input[@id='requestDate']"), mapExchangeDetails.get("Request Received Date"), "No", false);				
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Request Received Time")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Request Received Time", By.id("tblOrderTransferMaster.requestTime"), mapExchangeDetails.get("Request Received Time"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Order Received Office")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Order Received Office", By.xpath("//input[@placeholder='Order Received Office']"), mapExchangeDetails.get("Order Received Office"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Time Zone")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Time Zone", By.xpath("//input[@placeholder='Time Zone']"), mapExchangeDetails.get("Time Zone"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}			
			if(mapExchangeDetails.get("Source")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Source", By.id("tblOrderTransferMaster.source"), mapExchangeDetails.get("Source"), "No", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}				
			}			
			if(mapExchangeDetails.get("Mode of Request")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Mode Of Request ", By.xpath("//div[contains(@id,'modeOfRequest')]//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("Mode of Request"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("External ID Number")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("External ID Number", By.id("tblOrderTransferMaster.externalId"), mapExchangeDetails.get("External ID Number"), "No", false);
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

	//Verify Investor Details.
	public static boolean doMakerVerifyInvestorDetails(Map<String, String> mapExchangeDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try{
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "2");
			if (!bStatus) {
				return false;
			}
			if(mapExchangeDetails.get("Investor Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Investor Name ", By.xpath("//div[@id='s2id_investorMaster']//span[contains(@class,'select2-chosen')]"), mapExchangeDetails.get("Investor Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Holder Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Holder Name ", By.xpath("//div[@id='s2id_investorHolder']//span[contains(@class,'select2-chosen')]"), mapExchangeDetails.get("Holder Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if(mapExchangeDetails.get("Account ID")!=null){
				Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapExchangeDetails.get("Account ID"));
				if (mapXMLExchangeDetails != null && !mapXMLExchangeDetails.isEmpty() && mapXMLExchangeDetails.get("AccountID") != null) {
					mapExchangeDetails.put("Account ID", mapXMLExchangeDetails.get("AccountID"));
				}
				else {
					Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapExchangeDetails.get("Account ID"));
					if (mapXMLAccountMasterDetails != null && !mapXMLAccountMasterDetails.isEmpty() && mapXMLAccountMasterDetails.get("AccountID") != null) {
						mapExchangeDetails.put("Account ID", mapXMLAccountMasterDetails.get("AccountID"));
					}
				}

				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Account ID", By.xpath("//div[@id='s2id_investorAccount']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("Account ID"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Filling Investor Details.
	public static boolean doFillInvestorDetails(Map<String, String> mapExchangeDetails){
		try{
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "2");
			if (!bStatus) {
				return false;
			}
			if(mapExchangeDetails.get("Investor Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("Investor Name"), By.xpath("//div[@id='s2id_investorMaster']/a[contains(@class,'select2-choice')]"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : Investor Name is Not Entered "+Messages.errorMsg+"]\n";
					return false;
				}
			}

			if(mapExchangeDetails.get("Holder Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("Holder Name"), By.xpath("//div[@id='s2id_investorHolder']/a[contains(@class,'select2-choice')]"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : Holder Name is Not Selected "+Messages.errorMsg+"]\n";
					return false;
				}
			}
			if(mapExchangeDetails.get("Account ID")!=null){
				Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapExchangeDetails.get("Account ID"));
				if (mapXMLExchangeDetails != null && !mapXMLExchangeDetails.isEmpty() && mapXMLExchangeDetails.get("AccountID") != null) {
					mapExchangeDetails.put("Account ID", mapXMLExchangeDetails.get("AccountID"));
				}else {
					Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapExchangeDetails.get("Account ID"));
					if (mapXMLAccountMasterDetails != null && !mapXMLAccountMasterDetails.isEmpty() && mapXMLAccountMasterDetails.get("AccountID") != null) {
						mapExchangeDetails.put("Account ID", mapXMLAccountMasterDetails.get("AccountID"));
					}
				}

				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("Account ID"), By.xpath("//div[@id='s2id_investorAccount']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Account Id is Not Selected "+Messages.errorMsg+"] \n";
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

	//Filling Exchange Fund Details.
	public static boolean doFillExchangeDetails(Map<String, String> mapExchangeDetails){
		try{
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "5");
			if (!bStatus) {
				return false;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='Yes']/div[@id='uniform-fullExchange']/span"));
			if(!bStatus){
				Messages.errorMsg = "Cannot scroll the page to Exchange Details";
				return false;
			}
			//Select Full Transfer  Radio button
			if(mapExchangeDetails.get("Full Exchange")!=null){
				if(mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='Yes']/div[@id='uniform-fullExchange']/span"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Full Exchange Radio button 'Yes' is Not clicked ]\n";
						return false;
					}
					String percentageValue = "100";
					String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='percentage']");
					if(Float.parseFloat(actualValue) != Float.parseFloat(percentageValue)){
						Messages.errorMsg = "[ Error : Percentage Value is not Changed to 100 when Full Exchange Yes button selected ]\n";
						return false;
					}
				}
				if(mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='No']/div[@id='uniform-fullExchange']/span"));
					if(!bStatus){
						Messages.errorMsg = "[ Error : Full Exchange Radio button 'No' is Not clicked ]\n";
						return false;
					}
				}
			}

			//Enter Effective Date
			if( mapExchangeDetails.get("Effective Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapExchangeDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : Effective Date is Not Entered ]\n";
					return false;
				}
			}

			//Enter Percentage
			if(mapExchangeDetails.get("Percentage")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='percentage']"), mapExchangeDetails.get("Percentage"));
				if(!bStatus){
					Messages.errorMsg = "[ Error : Percentage Cannot be entered ]\n";
					return false;
				}
			}

			//Select Amount/Share Radio button
			if(isExchangeFromChekerReviewed){
				if(mapExchangeDetails.get("AmountorShares")!=null && mapExchangeDetails.get("Percentage") == null){
					if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare1']"));
						if(!bStatus){
							Messages.errorMsg = "[ Error : Amount Radio button cannot be selected ]\n";
							return false;
						}
						if(mapExchangeDetails.get("Amount Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapExchangeDetails.get("Amount Value"));
							if(!bStatus){
								Messages.errorMsg = "[ Error : Amount Value is  Not Entered ]\n";
								return false;
							}
						}
					}
					if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare2']"));
						if(!bStatus){
							Messages.errorMsg = "[ Error : Shares Radio button cannot be selected ]\n";
							return false;
						}
						if(mapExchangeDetails.get("Share Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='share']"), mapExchangeDetails.get("Share Value"));
							if(!bStatus){
								Messages.errorMsg = "[ Error : Share Value  Not Entered ]\n";
								return false;
							}
						}
					}
				}
			}else{
				if(mapExchangeDetails.get("AmountorShares")!=null && mapExchangeDetails.get("Percentage") == null && mapExchangeDetails.get("Full Exchange") != null && !mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("Yes")){
					if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare1']"));
						if(!bStatus){
							Messages.errorMsg = "[ Error : Amount Radio button cannot be selected ]\n";
							return false;
						}
						if(mapExchangeDetails.get("Amount Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapExchangeDetails.get("Amount Value"));
							if(!bStatus){
								Messages.errorMsg = "[ Error : Amount Value is  Not Entered ]\n";
								return false;
							}
						}
					}
					if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-amountShare2']"));
						if(!bStatus){
							Messages.errorMsg = "[ Error : Shares Radio button cannot be selected ]\n";
							return false;
						}
						if(mapExchangeDetails.get("Share Value")!=null){
							bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='share']"), mapExchangeDetails.get("Share Value"));
							if(!bStatus){
								Messages.errorMsg = "[ Error : Share Value  Not Entered ]\n";
								return false;
							}
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

	//Verifying Exchange Fund Details.
	public static boolean doMakerVerifyExchangeDetails(Map<String, String> mapExchangeDetails){		
		try{
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "5");
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Failed to expand the Fund Details tab.]\n";
				return false;
			}
			//Verify Full Transfer  Radio button
			if(mapExchangeDetails.get("Full Exchange")!=null){
				if(mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//input[@id='fullExchange' and @value='1']//parent::span[@class='checked']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Full Exchange Radio button 'Yes' is Not checked ]\n";
						return false;
					}
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
					String percentageValue = "100";
					String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='percentage']");
					if(Float.parseFloat(actualValue) != Float.parseFloat(percentageValue)){
						Messages.errorMsg = "[ ERROR : Percentage Value is not Changed to 100 when Full Exchange Yes button selected ]\n";
						return false;
					}
				}
				if(mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='fullExchange' and @value='0']//parent::span[@class='checked']"), 2);
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Full Exchange Radio button 'No' is Not checked ]\n";
						return false;
					}
				}
			}

			//Enter Effective Date
			if(mapExchangeDetails.get("Effective Date") != null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapExchangeDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Unable to enter the 'Effective Date' : '"+mapExchangeDetails.get("Effective Date")+"' into the field. ]\n";
					return false;
				}
			}

			//Enter Percentage
			if(mapExchangeDetails.get("Percentage")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='percentage']"), mapExchangeDetails.get("Percentage"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Unable to enter the 'Percentage' : '"+mapExchangeDetails.get("Percentage")+"' into the field. ]\n";
					return false;
				}
			}

			//Select Amount/Share Radio button
			if(mapExchangeDetails.get("AmountorShares")!=null){
				if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//input[@id='amountShare1']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Unable to select the 'Amount' radio button. ]\n";
						return false;
					}
					if(mapExchangeDetails.get("Amount Value")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapExchangeDetails.get("Amount Value"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Unable to enter the 'Amount Value' : '"+mapExchangeDetails.get("Amount Value")+"' into the field. ]\n";
							return false;
						}
					}
				}
				if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//input[@id='amountShare2']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Unable to select the 'Shares' radio button. ]\n";
						return false;
					}
					if(mapExchangeDetails.get("Share Value")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='share']"), mapExchangeDetails.get("Share Value"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Unable to enter the 'Share Value' : '"+mapExchangeDetails.get("Share Value")+"' into the field. ]\n";
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

	//Filling FromFund Details.
	public static boolean doFillFromFundDetails(Map<String, String> mapExchangeDetails) {		
		try{
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "3");
			if (!bStatus) {
				return false;
			}
			//select Client Name
			if(mapExchangeDetails.get("From Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("From Client Name"), By.xpath("//div[@id='s2id_fromClient']//span[contains(@id, 'select2-chosen')]"));				
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Client Name' i.e : '"+mapExchangeDetails.get("From Client Name")+"' from dropdown is failed.]";
					return false;
				}
			}
			//Select Fund Family
			if(mapExchangeDetails.get("From Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("From Fund Family Name"), By.xpath("//div[@id='s2id_fromFundFamily']//span[contains(@id, 'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Fund Family Name' i.e : '"+mapExchangeDetails.get("From Fund Family Name")+"' from dropdown is failed.]";
					return false;
				}
			}
			//Select Legal Entity
			if(mapExchangeDetails.get("From Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("From Legal Entity Name"), By.xpath("//div[@id='s2id_fromLegalEntity']//span[contains(@id, 'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Legal Entity Name' i.e : '"+mapExchangeDetails.get("From Legal Entity Name")+"' from dropdown is failed.]";
					return false;
				}
			}

			//Select Class Name
			if(mapExchangeDetails.get("From Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("From Class Name"), By.xpath("//div[@id='s2id_fromClass']//span[contains(@id, 'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Class Name' i.e : '"+mapExchangeDetails.get("From Class Name")+"' from dropdown is failed.]";
					return false;
				}
			}

			// Select the Series Name
			if(mapExchangeDetails.get("From Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("From Series Name"), By.xpath("//div[@id='s2id_fromSeries']//span[contains(@id, 'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Selection of 'From Series Name' i.e : '"+mapExchangeDetails.get("From Series Name")+"' from dropdown is failed.]";
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
	public static boolean doVerifyAndFillToFundDetails(Map<String, String> mapExchangeDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "4");
			if (!bStatus) {
				return false;
			}
			//select Client Name
			if(mapExchangeDetails.get("From Client Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Client Name ", By.xpath("//div[@id='s2id_toClient']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("From Client Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//Select Fund Family
			if(mapExchangeDetails.get("From Fund Family Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Fund Family Name ", By.xpath("//div[@id='s2id_toFundFamily']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("From Fund Family Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Legal Entity
			if(mapExchangeDetails.get("From Legal Entity Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Legal Entity Name ", By.xpath("//div[@id='s2id_toLegalEntity']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("From Legal Entity Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Class Name
			if(mapExchangeDetails.get("To Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("To Class Name"), By.xpath("//div[@id='s2id_toClass']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : 'To Class Name' value : '"+mapExchangeDetails.get("To Class Name")+"' ,wasn't able to be selected from dropdown. ]\n";					
					return false;
				}
			}

			// Select the Series Name
			if(mapExchangeDetails.get("To Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapExchangeDetails.get("To Series Name"), By.xpath("//div[@id='s2id_toSeries']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : 'To Series Name' value : '"+mapExchangeDetails.get("To Series Name")+"' ,wasn't able to be selected from dropdown. ]\n";					
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

	//Verifying From Fund Details in 2nd screen.
	public static boolean doMakerVerifyFromFundDetails(Map<String, String> mapExchangeDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "3");
			if (!bStatus) {
				return false;
			}
			//Verify Client Name
			if(mapExchangeDetails.get("From Client Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Client Name ", By.xpath("//div[@id='s2id_fromClient']//span[contains(@id, 'select2-chosen')]"), mapExchangeDetails.get("From Client Name"), "Yes", false);				
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Verify Fund Family
			if(mapExchangeDetails.get("From Fund Family Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Fund Family Name ", By.xpath("//div[@id='s2id_fromFundFamily']//span[contains(@id, 'select2-chosen')]"), mapExchangeDetails.get("From Fund Family Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Verify Legal Entity
			if(mapExchangeDetails.get("From Legal Entity Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Legal Entity Name ", By.xpath("//div[@id='s2id_fromLegalEntity']//span[contains(@id, 'select2-chosen')]"), mapExchangeDetails.get("From Legal Entity Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//Verify Class Name
			if(mapExchangeDetails.get("From Class Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Class Name ", By.xpath("//div[@id='s2id_fromClass']//span[contains(@id, 'select2-chosen')]"), mapExchangeDetails.get("From Class Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			// Verify the Series Name
			if(mapExchangeDetails.get("From Series Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("From Series Name ", By.xpath("//div[@id='s2id_fromSeries']//span[contains(@id, 'select2-chosen')]"), mapExchangeDetails.get("From Series Name"), "Yes", false);
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

	// Verifying ToFund Details in 2nd screen.
	public static boolean doMakerVerifyToFundDetails(Map<String, String> mapExchangeDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "4");
			if (!bStatus) {
				return false;
			}
			//select Client Name
			if(mapExchangeDetails.get("From Client Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Client Name ", By.xpath("//div[@id='s2id_toClient']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("From Client Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Fund Family
			if(mapExchangeDetails.get("From Fund Family Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Fund Family Name ", By.xpath("//div[@id='s2id_toFundFamily']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("From Fund Family Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Legal Entity
			if(mapExchangeDetails.get("From Legal Entity Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Legal Entity Name ", By.xpath("//div[@id='s2id_toLegalEntity']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("From Legal Entity Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Class Name
			if(mapExchangeDetails.get("To Class Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Class Name ", By.xpath("//div[@id='s2id_toClass']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("To Class Name"), "Yes", false);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			// Select the Series Name
			if(mapExchangeDetails.get("To Series Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("To Series Name ", By.xpath("//div[@id='s2id_toSeries']//span[contains(@id,'select2-chosen')]"), mapExchangeDetails.get("To Series Name"), "Yes", false);				
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

	//Verify Exchange Details.
	public static boolean doMakerVerifyExchangeDetailsAtSecondScreen(Map<String, String> mapExchangeDetails){
		try{

			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "5");
			if (!bStatus) {
				return false;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='effectiveDate']"));
			if(!bStatus){
				Messages.errorMsg = "Page cannot scroll to Exchange Details in second screen";
				return false;
			}

			//Select Full Transfer  Radio button
			if(mapExchangeDetails.get("Full Exchange")!=null){
				if(mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[normalize-space()='Yes']/div[@id='uniform-fullExchange']//span[@class='checked']/input") , Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ Error : Expected Full Exchange Radio button 'Yes' is Not matching with the Actual in Second screen ]\n";
						return false;
					}
					String percentageValue = "100";
					String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='percentage']");
					/*bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Percentage", Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")), actualValue);
					if(!bStatus){
						return false;
					}*/
					if(Float.parseFloat(actualValue) != Float.parseFloat(percentageValue)){
						Messages.errorMsg = "[ Error : Percentage Value is not Changed to 100 when Full Exchange Yes button selected in second screen]\n";
						return false;
					}
				}
				if(mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[normalize-space()='No']/div[@id='uniform-fullExchange']//span[@class='checked']/input") , Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ Error : Expected Full Exchange Radio button 'No' is Not Maching with the Actula in Second Screen]\n";
						return false;
					}
				}
			}

			//Enter Effective Date
			if( mapExchangeDetails.get("Effective Date")!=null){
				String effectiveDate = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='effectiveDate']"), "value");

				if(!effectiveDate.equalsIgnoreCase(mapExchangeDetails.get("Effective Date"))){
					Messages.errorMsg = "[ Error : Effective Date Actual "+effectiveDate+" is Not matching with the Expected "+mapExchangeDetails.get("Effective Date")+" in second screen ]\n";
					return false;
				}
			}

			//Enter Percentage
			if(mapExchangeDetails.get("Percentage")!=null){
				String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='percentage']");
				/*bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Percentage", Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					return false;
				}*/

				if(Float.parseFloat(actualValue) != Float.parseFloat(mapExchangeDetails.get("Percentage"))){
					Messages.errorMsg = "[ Error : Expected Percentage Value is "+mapExchangeDetails.get("Percentage")+" not matching with the Actual value "+actualValue+" in second screen ]\n";
					return false;
				}
			}

			//Select Amount/Share Radio button
			if(mapExchangeDetails.get("AmountorShares")!=null){
				if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//span[@class='checked']/input[@id='amountShare1']") , Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ Error : Expected Amount Radio button is not selected in second screen]\n";
						return false;
					}
					if(mapExchangeDetails.get("Amount Value")!=null){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Amount Value", By.xpath("//input[@id='amountValue']"), mapExchangeDetails.get("Amount Value"), "No", true, Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")));
						if(!bStatus){
							return false;
						}
					}
				}
				if(mapExchangeDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//span[@class='checked']/input[@id='amountShare2']") , Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ Error :Expected Shares Radio button is not selected in second screen ]\n";
						return false;
					}
					if(mapExchangeDetails.get("Share Value")!=null){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share Value", By.xpath("//input[@id='share']"), mapExchangeDetails.get("Share Value"), "No", true, Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals")));
						if(!bStatus){
							return false;
						}
					}
				}
			}

			if (mapExchangeDetails.get("Expected NAV Date") != null) {				
				String sValue = Elements.getText(Global.driver, By.xpath("//label[contains(text(),'Nav Date')]//following-sibling::label"));
				if (sValue == null || sValue.equalsIgnoreCase("") || !TradeTypeSubscriptionAppFunctions.formatDate(sValue.trim()).equalsIgnoreCase(mapExchangeDetails.get("Expected NAV Date"))) {
					Messages.errorMsg = "Expected NAV Date is "+mapExchangeDetails.get("Expected NAV Date")+" Not matching with the Actual NAV Date "+sValue+"";
					return false;
				}
				/*if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("NAV Date", By.xpath("//input[contains(@id,'navDate')]"), mapRedemptionDetails.get("Expected NAV Date"), "No", false)) {
					return false;
				}*/
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doFillAvailableBalanceAtMaker(Map<String ,String> mapExchangeDetails){
		List<String> sAmountOrSharesList = null;
		List<String> sVerifyAmountOrSharesList = null;
		int noOfDecimalsToVerify = -1;
		try {

			String sIsAmountOrShare = "";
			String verifyAmountOrShares = "";
			/*bStatus = NewUICommonFunctions.scrollToView(By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
			if(!bStatus){
				Messages.errorMsg = "Page caanot scroll to Available Balance Details";
				return false;
			}*/
			bStatus = TradeTypeRedemptionAppFunctions.doClickOnExpandOrCollapseButtonsOnRedemprionSecondTab("Yes", "6");
			if (!bStatus) {
				return false;
			}
			//Split Taxlots Amount or Share
			if (mapExchangeDetails.get("TaxLotsAmounts") != null || mapExchangeDetails.get("TaxLotsShares") != null) {
				if (mapExchangeDetails.get("TaxLotsAmounts") != null) {
					sIsAmountOrShare = "amount";
					sAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("TaxLotsAmounts").split(","));
					verifyAmountOrShares = "share";
					noOfDecimalsToVerify = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals"));
				}
				else if (mapExchangeDetails.get("TaxLotsShares") != null) {
					sIsAmountOrShare = "share";
					sAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("TaxLotsShares").split(","));		
					verifyAmountOrShares = "amount";
					noOfDecimalsToVerify = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals"));
				}
			}

			//Split Expected Taxlots Amount or Share
			if(mapExchangeDetails.get("ExpectedTaxLotAmountOrShare") != null){
				sVerifyAmountOrSharesList =  Arrays.asList(mapExchangeDetails.get("ExpectedTaxLotAmountOrShare").split(","));
			}

			if (!sIsAmountOrShare.equalsIgnoreCase("") && mapExchangeDetails.get("Full Exchange")!=null && mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("No")) {			
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"), 3);
				if(!bStatus){
					Messages.errorMsg = "Tax lots are not visible";
					return false;
				}

				bStatus = doEnterAmountOrShareInAvailableBalance(sAmountOrSharesList,sIsAmountOrShare,verifyAmountOrShares,sVerifyAmountOrSharesList,noOfDecimalsToVerify);
				if(!bStatus){
					return false;
				}				
			}

			if (mapExchangeDetails.get("Full Exchange")!=null && mapExchangeDetails.get("Full Exchange").equalsIgnoreCase("Yes")) {
				bStatus = doVerifyFullExchangeValuesInAvaialableBalance(mapExchangeDetails);
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

	private static boolean doVerifyFullExchangeValuesInAvaialableBalance(Map<String, String> mapExchangeDetails) {
		try {

			if(mapExchangeDetails.get("ExcpectedAvailableAmount") != null && mapExchangeDetails.get("ExcpectedAvailableShares") != null){
				List<String> allocatedAmount = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> allocatedShares = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableShares").split(","));
				if(allocatedAmount.size() != allocatedShares.size()){
					Messages.errorMsg = "Avialable Amount "+allocatedAmount+" and Avaiable Share "+allocatedShares+" Values are Not Provided equally";
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
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+allocatedAmount.get(i)+"' at index : '"+i+"'", By.id("amount_"+(i+1)+""), allocatedAmount.get(i), "No", true, noOfDecimalsToDispaly);
						if (!bStatus) {
							return false;
						}
					}

					if(!allocatedShares.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+allocatedShares.get(i)+"' at index : '"+i+"'", By.id("share_"+(i+1)+""), allocatedShares.get(i), "No", true, noOfShareDecimalsToDispaly);
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

	private static boolean doEnterAmountOrShareInAvailableBalance(List<String> sAmountOrSharesList, String sIsAmountOrShare,String verifyAmountOrShares, List<String> sVerifyAmountOrSharesList, int noOfDecimalsToDispaly) {
		try {

			int NoOfRecords = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
			if(sAmountOrSharesList.size() != NoOfRecords){
				Messages.errorMsg = "[ ERROR : Expected No lots : '"+sAmountOrSharesList.size()+"' is not matching with Actual no of Lots : '"+NoOfRecords+"']\n";
				return false;
			}

			for(int i=0; i<sAmountOrSharesList.size();i++)
			{
				if(!sAmountOrSharesList.get(i).equalsIgnoreCase("None"))
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='check_"+(i+1)+"']//parent::span[@class='checked']"), 3);
					if(bStatus)
					{
						By locator = By.id(sIsAmountOrShare+"_"+(i+1)+"");
						bStatus = TradeTypeRedemptionAppFunctions.ClearAndSetText(locator, sAmountOrSharesList.get(i));							
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to enter the text into the Exchange tax Lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
							return false;
						}
						bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Exchange')]"));
						if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Exchange tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.id(verifyAmountOrShares+"_"+(i+1)+""), sVerifyAmountOrSharesList.get(i), "No", true, noOfDecimalsToDispaly);
							if (!bStatus) {
								return false;
							}
						}					
					}
					else
					{
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='check_"+(i+1)+"']//parent::span"));
						if (!bStatus) {
							Messages.errorMsg = "[ERROR : Unable to Select the Excahnge tax lot '"+sIsAmountOrShare+"' at index : '"+i+"' ]\n";
							return false;
						}

						By locator = By.id(sIsAmountOrShare+"_"+(i+1)+"");
						bStatus = TradeTypeRedemptionAppFunctions.ClearAndSetText(locator, sAmountOrSharesList.get(i));							
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to enter the text into the Excahnge Lot '"+sIsAmountOrShare+"' value : '"+sAmountOrSharesList.get(i)+"' into index : '"+i+"' ]\n";
							return false;
						}
						bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Exchange')]"));
						if(!sVerifyAmountOrSharesList.get(i).equalsIgnoreCase("None")){
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Excahnge tax lot '"+verifyAmountOrShares+"' at index : '"+i+"'", By.id(verifyAmountOrShares+"_"+(i+1)+""), sVerifyAmountOrSharesList.get(i), "No", true, noOfDecimalsToDispaly);
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
							Messages.errorMsg = "Cannot Uncheck the Check box at Index '"+i+"'";
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

	//Maker/Checker verify available balance Details function.
	public static boolean doVerifyAvailableBalanceDetailsAtMakerAndChecker(Map<String , String> mapExchangeDetails){
		try {
			boolean bValidStatus = true;
			String appendErrMsg = "";
			int noOfDecimalsToDispaly = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals"));
			int noOfShareDecimalsToDispaly = Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals"));
			if(mapExchangeDetails.get("ExcpectedActualBalance") != null && mapExchangeDetails.get("ExcpectedActualShares") != null && mapExchangeDetails.get("ExcpectedAvailableAmount") != null && mapExchangeDetails.get("ExcpectedAvailableShares") != null)
			{
				List<String> actualBalance = Arrays.asList(mapExchangeDetails.get("ExcpectedActualBalance").split(","));
				List<String> actualShares = Arrays.asList(mapExchangeDetails.get("ExcpectedActualShares").split(","));
				List<String> availableAmount = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> avaialableShares = Arrays.asList(mapExchangeDetails.get("ExcpectedAvailableShares").split(","));


				int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//tr//td//div[contains(@id,'uniform-check')]"));
				if(xpathCount != actualBalance.size()){
					Messages.errorMsg = "Actual Count of Tax lots is "+xpathCount+" is not matching with the Expected Atual Balance Count "+actualBalance.size()+" ";
					return false;
				}

				for(int i=0 ; i<actualBalance.size() ; i++){
					if(!actualBalance.get(i).equalsIgnoreCase("None")){

						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Actual Balance' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath("//input[@id='fundChangedFlag']//following-sibling::div//tbody//tr["+(i+1)+"]//td[5]"), actualBalance.get(i), "Yes", true, noOfDecimalsToDispaly);
						if(!bStatus){
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!actualShares.get(i).equalsIgnoreCase("None")){

						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Actual Shares' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath("//input[@id='fundChangedFlag']//following-sibling::div//tbody//tr["+(i+1)+"]//td[6]"), actualShares.get(i), "Yes", true, noOfShareDecimalsToDispaly);
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){

						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Available Amount' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath("//input[@id='availableAmount_"+i+"']//parent::td"), availableAmount.get(i), "Yes", true,noOfDecimalsToDispaly);
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){

						bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Available Shares' in 'Available Balance' for the lots Row '"+i+"' ", By.xpath("//input[@id='availableUnits_"+i+"']//parent::td"), avaialableShares.get(i), "Yes", true,noOfShareDecimalsToDispaly);
						if(!bStatus)
						{
							appendErrMsg = appendErrMsg+ Messages.errorMsg;
							bValidStatus = false;
						}
					}
				}

			}

			if(mapExchangeDetails.get("ExcpectedTotalActualBalance") != null && mapExchangeDetails.get("ExcpectedTotalActualShares") != null && mapExchangeDetails.get("ExcpectedTotalAvailableAmount") != null && mapExchangeDetails.get("ExcpectedTotalAvailableShares") != null){
				if(!mapExchangeDetails.get("ExcpectedTotalActualBalance").equalsIgnoreCase("None")){
					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalActualBalance' in 'Available Balance'", By.xpath("//strong[text()='Total']//parent::td//following-sibling::td[1]"), mapExchangeDetails.get("ExcpectedTotalActualBalance"), "Yes", true, noOfDecimalsToDispaly);
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapExchangeDetails.get("ExcpectedTotalActualShares").equalsIgnoreCase("None")){

					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalActualShares' in 'Available Balance'", By.xpath("//strong[text()='Total']//parent::td//following-sibling::td[2]"), mapExchangeDetails.get("ExcpectedTotalActualShares"), "Yes", true, noOfShareDecimalsToDispaly);
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapExchangeDetails.get("ExcpectedTotalAvailableAmount").equalsIgnoreCase("None")){

					bStatus =NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalAvailableAmount' in 'Available Balance'", By.xpath("//strong[text()='Total']//parent::td//following-sibling::td[3]"), mapExchangeDetails.get("ExcpectedTotalAvailableAmount"), "Yes", true, noOfDecimalsToDispaly);
					if(!bStatus)
					{
						appendErrMsg = appendErrMsg+ Messages.errorMsg;
						bValidStatus = false;
					}
				}
				if(!mapExchangeDetails.get("ExcpectedTotalAvailableShares").equalsIgnoreCase("None")){

					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'TotalAvailableShares' in 'Available Balance'", By.xpath("//strong[text()='Total']//parent::td//following-sibling::td[4]"), mapExchangeDetails.get("ExcpectedTotalAvailableShares"), "Yes", true, noOfShareDecimalsToDispaly);
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

	//Maker Override Details verify/change function.
	private static boolean doOverrideOrVerifyChargeDetails(Map<String, String> mapRedemptionDetails ) {
		try {

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-ncDefaultYes']/span"));
			if(!bStatus){
				Messages.errorMsg = "Page Not Scrolled to Notice Period Charges Radio Button fields";
				return false;
			}

			if(mapRedemptionDetails.get("NoticeChargesRadioButton") != null)
			{
				bStatus = NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
				if(!bStatus){
					return false;
				}
				if(mapRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No"))
				{
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-ncDefaultNo']/span/input"));
					if(!bStatus){
						Messages.errorMsg = "Notice Period Charges No Radio Button Not Clicked";
						return false;
					}

					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "New amount for Notice Charges field not visible";
						return false;
					}
				}
				if (mapRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-ncDefaultYes']/span/input"));
					if(!bStatus){
						Messages.errorMsg = "Notice Period Charges Yes Radio Button Not Clicked";
						return false;
					}
				}

			}
			if (mapRedemptionDetails.get("NewAmountForNoticePeriod") !=null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), mapRedemptionDetails.get("NewAmountForNoticePeriod"));
				if(!bStatus){
					Messages.errorMsg = "New Amount for Notice Charges not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@id,'npSave') and contains(text(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "Notice Period Charges Save butotn not clicked";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-ncDefaultNo']/span"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Notice Charges Radio Buttons are Not visible";
					return false;
				}
			}


			if(mapRedemptionDetails.get("TransactionChargesRadioButton")!=null)
			{
				bStatus = NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
				if(!bStatus){
					return false;
				}
				if(mapRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No"))
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-tcDefaultYes']/span"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Transaction Charges Radio Buttons are Not visible";
						return false;
					}
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-tcDefaultNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Transaction Charges 'No' Radio Button Not Clicked";
						return false;
					}
				}
				if (mapRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='uniform-tcDefaultYes']/span"));
					if(!bStatus){
						Messages.errorMsg = "Transaction Charges 'Yes' Radio Button Not Clicked";
						return false;
					}	
				}
			}
			if(mapRedemptionDetails.get("NewTransactionCharges") != null)
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
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@id,'tpSave') and contains(text(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "Transaction charges Charges Save butotn not clicked";
					return false;
				}
			}

			if(mapRedemptionDetails.get("Adhoc Charges")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='adHocCharges']"), mapRedemptionDetails.get("Adhoc Charges"));
				if(!bStatus){
					Messages.errorMsg = "Adhoc Charges Not Entered";
					return false;
				}
				bStatus = NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
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

			Thread.sleep(2000);

			// Verify Notice Charges  with out modification

			if(mapVerifyRedemptionDetails.get("ExpectedNoticeCharges") != null )
			{
				// Verify Notice Charge Default

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-ncDefaultYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+ "[ Notice Charge Default Radio button 'Yes' is not selected  ]\n";
				}


				// Verify Notice Charges 
				String ActualNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("noticeChargeAmount");

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

			if(mapVerifyRedemptionDetails.get("NewAmountForNoticePeriod") !=null && mapVerifyRedemptionDetails.get("ExpectedNewNoticeCharges") !=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-ncDefaultNo']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "[ Notice Charge Default Radio button No is not selected  ]\n";
				}

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("noticeChargeAmount");
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
			//Verify the Transaction Charges with out modification

			if(mapVerifyRedemptionDetails.get("ExpectedTransactionCharges") != null ){
				// Verify Notice Charge Default

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-tcDefaultYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[Transaction Charge Default Radio button Yes is not selected  ]\n";
				}


				// Verify Notice Charges 
				String ActualTransCharges = NewUICommonFunctions.jsGetElementAttribute("transactionChargeAmount");

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
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-tcDefaultNo']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "[ Transaction  Charge Default Radio button No Radio button is not selected ]\n";
				}

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("transactionChargeAmount");
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

			//Verify  Total charges
			if(mapVerifyRedemptionDetails.get("ExpectedTotalCharges")!=null){
				String ActualToatal =  NewUICommonFunctions.jsGetElementAttribute("totalCharges");
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
			if (mapRedemptionDetails.get("ExpectedCharges") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("AmountDetailsTab 'Charges'", By.xpath("//input[@id='totalChargesView']"), mapRedemptionDetails.get("ExpectedCharges"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedExchangeAmount") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("AmountDetailsTab 'Amount Payable'", By.xpath("//input[@id='exchangeAmountView']"), mapRedemptionDetails.get("ExpectedExchangeAmount"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					appendErrMsg = appendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedShareInAmountDetails") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("AmountDetailsTab 'Share'", By.xpath("//input[@id='netUnitView']"), mapRedemptionDetails.get("ExpectedShareInAmountDetails"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
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

	public static boolean doSelectOtherDetailsRadioButtons(Map<String ,String> mapExchangeDetails){
		try {

			if(mapExchangeDetails.get("Crystalize Fee")!=null){
				if(mapExchangeDetails.get("Crystalize Fee").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isCrystalizedFee1']/span"));
					if(!bStatus){
						Messages.errorMsg = "Crystalize Fee 'Yes' Radio Button cannot be selected";
						return false;
					}
				}
				if(mapExchangeDetails.get("Crystalize Fee").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isCrystalizedFee2']/span"));
					if(!bStatus){
						Messages.errorMsg = "Crystalize Fee 'No' Radio Button cannot be selected";
						return false;
					}
				}
			}

			if(mapExchangeDetails.get("Cumulative Return")!=null){
				if(mapExchangeDetails.get("Cumulative Return").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isCummulative1']/span"));
					if(!bStatus){
						Messages.errorMsg = "Cumulative Return 'Yes' Radio Button cannot be selected";
						return false;
					}
				}

				if(mapExchangeDetails.get("Cumulative Return").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-tblOrderTransferMaster.isCummulative2']/span"));
					if(!bStatus){
						Messages.errorMsg = "Cumulative Return 'No' Radio Button cannot be selected";
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

	//Maker Exception Details verify function.
	public static boolean doVerifyMakerExceptions(String exceptionList){
		try {
			int exceptionCount = 0;
			List<String> aExceptionlist = Arrays.asList(exceptionList.split(":"));
			int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@id='exceptionModal']//div[@class='alert alert-danger']/strong"));
			String totalExceptions = "";
			for(int j=1; j<=xpathCount ;j++){
				String exceptionValue = Elements.getText(Global.driver, By.xpath("//div[@id='exceptionModal']//div[@class='alert alert-danger']["+j+"]"));
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

	//sub operations on trade.
	public static boolean doSubOperationsOnTransactionTrades(String master , String operation){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//button[contains(@onclick,'javascript:submitExchange') and contains(normalize-space(),'Save')]") , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Save Button is Not Visible";
				return false;
			}
			if(operation.equalsIgnoreCase("Save")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitExchange') and contains(normalize-space(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Save button cannot be clicked";
					return false;
				}
				return true;

			}

			if(operation.contains("Review")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitExchange') and contains(normalize-space(),'Send for Review')]"));
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

	//Checker Charge Details verify function.
	private static boolean doVerifyChargeDetailsinChecker(Map<String, String> mapRedemptionDetails) {
		try {

			String appndErrMsg = "";
			boolean ValidbStatus = true;
			if (mapRedemptionDetails.get("NoticeChargesRadioButton") != null) {
				if (mapRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Notice Charge Default')]//following-sibling::div//label[contains(text(),'No')]//span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						appndErrMsg  = appndErrMsg +"[Expected Value 'No' is not checked for Label: Notice Charges]";
						ValidbStatus  = false;
					}	
				}
				if (mapRedemptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Notice Charge Default')]//following-sibling::div//label[contains(text(),'Yes')]//span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						appndErrMsg  = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Notice Charges]";
						ValidbStatus  = false;
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
			else{
				if(mapRedemptionDetails.get("ExpectedNoticeCharges")!=null){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Notice Charge Default')]//following-sibling::div//label[contains(text(),'Yes')]//span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Notice Charges]";
						ValidbStatus = false;
					}

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

			}

			if (mapRedemptionDetails.get("TransactionChargesRadioButton") != null) {
				if(mapRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No"))
				{
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Transaction Charge Default')]//following-sibling::div//label[contains(text(),'No')]//span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'No' is not checked for Label: Transaction Charges]";
						ValidbStatus = false;
					}
				}
				if (mapRedemptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Transaction Charge Default')]//following-sibling::div//label[contains(text(),'Yes')]//span[@class='checked']"), Constants.lTimeOut);
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
			else{
				if(mapRedemptionDetails.get("ExpectedTransactionCharges")!=null){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Transaction Charge Default')]//following-sibling::div//label[contains(text(),'Yes')]//span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Transaction Charges]";
						ValidbStatus = false;
					}

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


			Messages.errorMsg = appndErrMsg;
			return ValidbStatus;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
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
			if (mapRedemptionDetails.get("ExpectedCharges") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Charges", By.xpath("//input[@id='totalChargesView']"), mapRedemptionDetails.get("ExpectedCharges"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedExchangeAmount") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Amount Payable", By.xpath("//input[@id='exchangeAmountView']"), mapRedemptionDetails.get("ExpectedExchangeAmount"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfDecimals")));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapRedemptionDetails.get("ExpectedShareInAmountDetails") != null) {
				bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Share", By.xpath("//input[@id='netUnitView']"), mapRedemptionDetails.get("ExpectedShareInAmountDetails"), "No", true, Integer.parseInt(mapRedemptionDetails.get("ExpectedNumberOfShareDecimals")));
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

	public static boolean doCheckerVerifyOtherDetails(Map<String,String>mapExchangeDetails){
		try {
			boolean bValidateStatus = true;
			String sAppendErrorMsg = "";
			if(mapExchangeDetails.get("Crystalize Fee")!=null){
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Crystalize Fee')]//following-sibling::div//label[contains(text(),'"+mapExchangeDetails.get("Crystalize Fee")+"')]//span[@class='checked']"), 3);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + "[ Expected Value '"+mapExchangeDetails.get("Crystalize Fee")+"' is Not Checked for Crystalize Fee]";
					bValidateStatus = false;
				}
			}

			if(mapExchangeDetails.get("Cumulative Return")!=null){
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Cumulative Return')]//following-sibling::div//label[contains(text(),'"+mapExchangeDetails.get("Cumulative Return")+"')]//span[@class='checked']"), 3);
				if(!bStatus){
					sAppendErrorMsg = sAppendErrorMsg + "[ Expected Value '"+mapExchangeDetails.get("Cumulative Return")+"' is Not Checked for Crystalize Fee]";
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

	//Checker Exceptions Details verify function.
	public static boolean doCheckerVerifyExceptionsAtCheckerForTradeTypeExchange(String sExceptions){		
		try {	
			String locatorOverridePresent = "//h4[contains(normalize-space(),'Exception')]//following-sibling::h4[contains(normalize-space(),'Override')]//preceding-sibling::div";
			String locatorOverrideNotPresent = "//h4[contains(normalize-space(),'Exception')]//following-sibling::div";
			String appendErrMsg = "";
			boolean bValidStatus = true;			
			int xpathCount =0;
			String totalExceptions = "";
			if (sExceptions != null && !sExceptions.equalsIgnoreCase("")) {
				List<String> sExceptionList = Arrays.asList(sExceptions.split(":"));
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(locatorOverridePresent), 3);
				if(bStatus)
				{
					xpathCount = Elements.getXpathCount(Global.driver, By.xpath(locatorOverridePresent));		
					for(int j=1; j<=xpathCount ;j++)
					{
						String exceptionValue = Elements.getText(Global.driver, By.xpath(locatorOverridePresent+"["+j+"]/label"));

						if(exceptionValue!=null && !exceptionValue.equalsIgnoreCase("")){
							totalExceptions = totalExceptions+"["+exceptionValue+"]";
						}
					}
				}
				else
				{
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(locatorOverrideNotPresent), 3);
					if(bStatus){
						xpathCount = Elements.getXpathCount(Global.driver, By.xpath(locatorOverrideNotPresent));		
						for(int j=1; j<=xpathCount ;j++)
						{
							String exceptionValue = Elements.getText(Global.driver, By.xpath(locatorOverrideNotPresent+"["+j+"]/label"));

							if(exceptionValue!=null && !exceptionValue.equalsIgnoreCase("")){
								totalExceptions = totalExceptions+"["+exceptionValue+"]";
							}
						}
					}
				}


				if(xpathCount != sExceptionList.size()){
					Messages.errorMsg = "[ Actual Exception count "+xpathCount+" is not matching with the Expected Exception count "+sExceptionList.size()+" ] "+"Actual Exceptions are "+totalExceptions+" Expected exceptions are ["+sExceptionList+"]";
					return false;
				}

				for(int i=0 ; i<sExceptionList.size(); i++){
					if(!totalExceptions.contains(sExceptionList.get(i))){
						appendErrMsg = appendErrMsg+"[ Expected exception "+sExceptionList.get(i)+" is not matching the Actual exception ]";
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


	//Charge override function in checker Side

	public static boolean changeOverrideStatus(Map<String , String> mapTransferDetails){
		try {

			if(mapTransferDetails.get("NewAmountForNoticePeriod")!=null && mapTransferDetails.get("NoticePeriodOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeExchange("Notice Period", mapTransferDetails.get("NoticePeriodOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}
			if(mapTransferDetails.get("NewTransactionCharges")!=null && mapTransferDetails.get("TransactionOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeExchange("Transaction", mapTransferDetails.get("TransactionOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}

			if(mapTransferDetails.get("Crystalize Fee")!=null && !mapTransferDetails.get("Crystalize Fee").equalsIgnoreCase("No") && mapTransferDetails.get("CrystalizeOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeExchange("Incentive Crystalize", mapTransferDetails.get("CrystalizeOverrideAtChecker"));
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

	//Over ride Transaction charges at checker Level

	public static boolean doVerifyOrChangeOverridedChargesTypeForTradeTypeExchange(String sOverridedChargeName, String isChangeStatusYesToNo){
		try {
			String sChargeTypeNameSubString = "";
			if (sOverridedChargeName != null) {
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
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[contains(normalize-space(),'Override')]//following-sibling::div//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//label[contains(text(),'No')]//span[@class='checked']"), 3);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Overrided 'Notice Period Charges' aren't marked to 'No' in Checker verification.] \n";
					return false;
				}
				if (bStatus && isChangeStatusYesToNo != null && isChangeStatusYesToNo.equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//h4[contains(normalize-space(),'Override')]//following-sibling::div//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//label[contains(text(),'Yes')]//span"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to change the status of Overrided chanrges of "+sChargeTypeNameSubString+" to 'Yes'.] \n";
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

	//------------------------------------------------------------------------------------------------------------------------------------------
	public static boolean verifyViewButtonsFunctionality(Map<String, String> mapExchangeDetails)
	{

		boolean bValidStatus= true;

		try {
			bStatus = doFillRequestDetails(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Request Details","Request Details cannot be filled. Error: "+Messages.errorMsg);
				return false;

			}

			bStatus = doFillInvestorDetails(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Investor Details","Investor Details cannot be filled. Error: "+Messages.errorMsg);
				return false;
			}

			bStatus = doFillFromFundDetails(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill From Fund Details","From Fund Details cannot be filled. Error: "+Messages.errorMsg);
				return false;
			}

			bStatus = doVerifyAndFillToFundDetails(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill To Fund Details","To Fund Details cannot be filled. Error: "+Messages.errorMsg);
				return false;
			}

			bStatus = doFillExchangeDetails(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Exchange Details","Exchange details filled. Error: "+Messages.errorMsg);
				return false;
			} 

			String proceedLocator = "//button[contains(@onclick,'javascript:proceed') and contains(normalize-space(),'Proceed')]";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(proceedLocator));
			if(!bStatus){
				Messages.errorMsg = "Cannot scroll to Proceed button";
				return false;
			}

			bStatus = Elements.click(Global.driver, By.xpath(proceedLocator));
			if(!bStatus){
				Messages.errorMsg = "Proceed butotn cannot be clicked";
				return false;
			}

			bStatus = verifyViewButtonForNoticePeriods(mapExchangeDetails);
			if(!bStatus){
				bValidStatus = false;
				Reporting.logResults("Fail","Verify Notice Charge details","Verification Failed. Error: "+Messages.errorMsg);
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Notice Period Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'NC')]//following-sibling::button[normalize-space(text()='Close')]"));
				}			
			}

			if(bStatus && mapExchangeDetails.get("NPView") != null)
			{
				Reporting.logResults("Pass","Verify Notice Charges ","Notice Charges verified successfully");
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Notice Period Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'NC')]//following-sibling::button[normalize-space(text()='Close')]"));
				}

			}

			bStatus = verifyViewButtonForTransactionCharge(mapExchangeDetails);
			if(!bStatus){
				bValidStatus = false;
				Reporting.logResults("Fail","Verify Transaction period details","Verification Failed. Error: "+Messages.errorMsg);
				if (Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[text()='Transaction Charges']"), Constants.lTimeOut)) {
					Elements.click(Global.driver, By.xpath("//div//button[contains(@onclick, 'TC')]//following-sibling::button[normalize-space(text()='Close')]"));
				}
			}
			if(bStatus && mapExchangeDetails.get("TCEffDate")!=null){
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

	// function to verify view buttons for Notice Charge Details	

	public static boolean verifyViewButtonForNoticePeriods(Map<String, String> ViewMapDetails){
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
	public static boolean verifyViewButtonForTransactionCharge(Map<String, String> ViewMapDetails){
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


	public static Map<String , String> dochangeOrderOfTableData(Map<String ,String >mapRedDetails){
		try {
			String sactualAmount = "";
			String sactualShares = "";
			String savailableShares = "";
			String savailableAmount = "";
			//String sIsUnderLockup = "";
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
			//	List<String> isUnderLockup = Arrays.asList(mapRedDetails.get("ExcpectedIsUnderLockupStatus").split(","));

			if(mapRedDetails.get("TaxLotsAmounts")!=null){
				aTaxLotsAmounts = Arrays.asList(mapRedDetails.get("TaxLotsAmounts").split(","));
				for(int j=0; j<aTaxLotsAmounts.size();j++){
					if(!aTaxLotsAmounts.get(j).equalsIgnoreCase("None")){
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
			}
			if(mapRedDetails.get("ExpectedTaxLotAmountOrShare")!=null){
				aExpectedTaxLotsSharesOrAmount = Arrays.asList(mapRedDetails.get("ExpectedTaxLotAmountOrShare").split(","));
				for(int j=0; j<aExpectedTaxLotsSharesOrAmount.size();j++){
					if(!aExpectedTaxLotsSharesOrAmount.get(j).equalsIgnoreCase("None")){
						aExpectedtaxlotSharesOrAmount = aExpectedtaxlotSharesOrAmount+ataxlotShares+aExpectedTaxLotsSharesOrAmount.get(j)+",";
					}
				}
			}


			for(int i=0 ;i<editable.size() ;i++){
				if(editable.get(i).equalsIgnoreCase("Yes")){
					sactualAmount = sactualAmount+actualAmount.get(i)+",";
					sactualShares = sactualShares+actualShares.get(i)+",";
					savailableAmount = savailableAmount+availableAmount.get(i)+",";
					savailableShares = savailableShares+availableShares.get(i)+",";
					//sIsUnderLockup = sIsUnderLockup+isUnderLockup.get(i)+",";
				}
			}
			for(int i=0 ;i<editable.size() ;i++){
				if(editable.get(i).equalsIgnoreCase("No")){
					sactualAmount = sactualAmount+actualAmount.get(i)+",";
					sactualShares = sactualShares+actualShares.get(i)+",";
					savailableAmount = savailableAmount+availableAmount.get(i)+",";
					savailableShares = savailableShares+availableShares.get(i)+",";
					//sIsUnderLockup = sIsUnderLockup+isUnderLockup.get(i)+",";
				}
			}
			sactualAmount = TradeTypeRedemptionAppFunctions.removeUnwantedCommas(sactualAmount);

			sactualShares = TradeTypeRedemptionAppFunctions.removeUnwantedCommas(sactualShares);

			savailableAmount = TradeTypeRedemptionAppFunctions.removeUnwantedCommas(savailableAmount);

			savailableShares = TradeTypeRedemptionAppFunctions.removeUnwantedCommas(savailableShares);

			ataxlotAmount = TradeTypeRedemptionAppFunctions.removeUnwantedCommas(ataxlotAmount);

			ataxlotShares = TradeTypeRedemptionAppFunctions.removeUnwantedCommas(ataxlotShares);

			//sIsUnderLockup = TradeTypeRedemptionAppFunctions.removeUnwantedCommas(sIsUnderLockup);

			aExpectedtaxlotSharesOrAmount = TradeTypeRedemptionAppFunctions.removeUnwantedCommas(aExpectedtaxlotSharesOrAmount);


			mapRedDetails.put("ExcpectedActualBalance",sactualAmount);
			mapRedDetails.put("ExcpectedActualShares",sactualShares);
			mapRedDetails.put("ExcpectedAvailableAmount",savailableAmount);	
			mapRedDetails.put("ExcpectedAvailableShares",savailableShares);
			//	mapRedDetails.put("ExcpectedIsUnderLockupStatus",sIsUnderLockup);

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

	//Checker reviewed Functions **************************************************************
	public static boolean doFillExchangeCheckerReviewedTradeDetails(Map<String , String>mapExchangeDetails){
		try {

			bStatus = doFillRequestDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillInvestorDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillFromFundDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doVerifyAndFillToFundDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillExchangeDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}


			String applyLocator = "//button[contains(@onclick, 'proceedModify')]";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(applyLocator));
			if (!bStatus) {
				Messages.errorMsg = "Error: Unable to scroll to Apply Button";
			}
			bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick, 'proceedModify')]"));
			if (!bStatus) {
				Messages.errorMsg = "Error:  Unable to click the Apply Button";
			}

			bStatus = doFillAvailableBalanceAtMaker(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doVerifyAvailableBalanceDetailsAtMakerAndChecker(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			if(mapExchangeDetails.get("ExpectedPendingRequestAmount") !=null && !mapExchangeDetails.get("ExpectedPendingRequestAmount").contains("Records")&&mapExchangeDetails.get("ExpectedPendingRequestedShares") !=null && mapExchangeDetails.get("ExpectedNumberOfDecimals") !=null){
				bStatus = TradeTypeTransferAppFunctions.doMakerAndCheckerVerifyPendingTradesDetails(mapExchangeDetails.get("ExpectedPendingRequestAmount"), mapExchangeDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals")));
				if(!bStatus){
					return false;
				}		
			}

			bStatus = doOverrideOrVerifyChargeDetails(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doMakerVerifyAmountDetailsTab(mapExchangeDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doSelectOtherDetailsRadioButtons(mapExchangeDetails);
			if(!bStatus){
				return false;
			}


			if(mapExchangeDetails.get("OperationType")!=null){
				String proceedlocator = "//button[contains(@onclick,'proceedToSave') and contains(normalize-space(),'Proceed')]";

				if (isExchangeFromChekerReviewed) {
					proceedlocator =  "//button[contains(@onclick,'Proceed') and contains(normalize-space(),'Proceed')]";
				}
				bStatus = doSubOperationsOnTransactionTrades("Exchange", mapExchangeDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapExchangeDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(proceedlocator), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Proceed for Approval Button is Not Visible";
						return false;
					}
					bStatus = doVerifyMakerExceptions(mapExchangeDetails.get("ExpectedExceptionsAtMaker"));
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with Expected");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
						if(!bStatus){
							Messages.errorMsg = "Cannot close the Exceptions ";
							return false;
						}
						Messages.errorMsg = errorMsg;
						return false;
					}
					if(mapExchangeDetails.get("OperationType").equalsIgnoreCase("Save") || mapExchangeDetails.get("OperationType").contains("Review"))
					{

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(proceedlocator));
						if(!bStatus){
							Messages.errorMsg = "Proceed Button Cannot be clicked";
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
							return false;
						}

					}

				}else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(proceedlocator), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions are Not matching");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionModal']//button[contains(text(),'Cancel')]"));
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
			return true;
		}
	}



	//******************* checkerReviewed Functions at Checker
	public static boolean doVerifyCheckerReviewedExchangeTradeAtChecker(Map<String , String> mapExchangeDetails){
		try {
			String appendErrorMsg = "";
			boolean bValidStatus = true;

			bStatus = doVerifyCheckerRequestDetails(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVerifyInvestorDetailsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVerifyFromFundDetialsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVerifyToFundDetailsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVerifyExchangeDetailsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			/*mapOverrideExcahngeDetails = dochangeOrderOfTableData(mapExchangeDetails);
			if(mapOverrideExcahngeDetails == null || mapOverrideExcahngeDetails.isEmpty()){
				appendErrorMsg = appendErrorMsg+"Available balance values Order is Not changed to verify";
				bValidStatus = false;
			}*/

			/*	bStatus = doVerifyAllocatedAmountandShareAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}*/

			bStatus = doVerifyAllocatedAmountInChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doverifyAvailableBalanceDetailsAtChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			if (mapExchangeDetails.get("ExpectedPendingRequestAmount") != null && !mapExchangeDetails.get("ExpectedPendingRequestAmount").contains("Records") && mapExchangeDetails.get("ExpectedPendingRequestedShares") != null && mapExchangeDetails.get("ExpectedNumberOfDecimals") != null) {
				bStatus = TradeTypeTransferAppFunctions.doMakerAndCheckerVerifyPendingTradesDetails(mapExchangeDetails.get("ExpectedPendingRequestAmount"), mapExchangeDetails.get("ExpectedPendingRequestedShares"), Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfDecimals")),Integer.parseInt(mapExchangeDetails.get("ExpectedNumberOfShareDecimals")));
				if (!bStatus) {
					appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;
				}
			}

			bStatus = doVerifyChargeDetailsinChecker(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doCheckerVerifyAmountDetailsTab(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doCheckerVerifyOtherDetails(mapExchangeDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			if(mapExchangeDetails.get("ExpectedExceptionsAtChecker")!=null){
				bStatus =doCheckerVerifyExceptionsAtCheckerForTradeTypeExchange(mapExchangeDetails.get("ExpectedExceptionsAtChecker"));
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


}