package com.viteos.veda.master.lib;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.tenx.framework.lib.Alerts;
import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.thoughtworks.selenium.webdriven.commands.IsChecked;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.checkerActionTypes;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;


//SAVE	BUTTON CLICK FOR NOTICE PERIOD,TRANSACTION CHARGES

//ENTERING MANAGEMENTFEE ,INCENTIVEFEE


public class TradeTypeSubscriptionAppFunctions {
	static boolean bStatus;
	public static String newAccountID = "";
	public static boolean isInkindFromCheckerReviewedScreen = false;
	public static boolean isChckerReviewedCheckerOperation =  false;

	public static boolean doFillSubscriptionDetails(Map<String, String> mapSubscriptionDetails){
		try {
			Map<String,Map<String , String>> mapAllBankDetails = new HashMap<>();
			if(mapSubscriptionDetails.get("New Bank TestCaseName")!=null){
				mapAllBankDetails = getBankDetailsUsingIndex(mapSubscriptionDetails.get("New Bank TestCaseName"));
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Received Date Field is Not Visible";
				return false;
			}

			bStatus = doFillRequestDetails(mapSubscriptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillInvestorDetails(mapSubscriptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillFundDetails(mapSubscriptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillInvestmentDetails(mapSubscriptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = verifyInvestmentDetailsAtMaker(mapSubscriptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doOverrideOrVerifyChargeDetails(mapSubscriptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doVerifyAmountDetails(mapSubscriptionDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doFillBankDetails(mapSubscriptionDetails , mapAllBankDetails);
			if(!bStatus){
				return false;
			}

			bStatus = doVerifyOrOverrideFeeDetails(mapSubscriptionDetails);			
			if(!bStatus){
				return false;
			}


			if(mapSubscriptionDetails.get("OperationType")!=null){
				bStatus = doSubOperationsOnTransactionTrades("Subscription", mapSubscriptionDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
				if(mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4/p[contains(text(),'Dashboard')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Cancel Operation Failed";
						return false;
					}
					return true;
				}
				if(mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Clear")){
					return true;
				}
				
				//Remove this code till the Comment portion if Exception validation is required
				if(mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Save") || mapSubscriptionDetails.get("OperationType").contains("Review"))
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitForm') and contains(@onclick,'ExceptionApproved')]"), Constants.lTimeOut);
					if(bStatus){
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submit') and contains(@onclick,'ExceptionApproved')]"));

						if(!bStatus){
							Messages.errorMsg = "Proceed Button Cannot be clicked";
							Reporting.logResults("Fail", "Select Proceed For Save", "Cannot be Clicked in Proceed Button:"+Messages.errorMsg);

							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
							if(!isInkindFromCheckerReviewedScreen)
							{
								bStatus = doSubOperationsOnTransactionTrades("Subscription", "Clear");

							}
							else{
								bStatus = doSubOperationsOnTransactionTrades("Subscription", "Cancel");

							}
							return false;
						}
					}
				}
				//UnComment the Code when the Exception verification is required
				
				/*if(mapSubscriptionDetails.get("ExpectedExceptionsAtMaker")!=null)
				{	
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submit') and contains(@onclick,'ExceptionApproved')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Proceed Button is Not Visible";
						return false;
					}
					bStatus = verifyExceptionsatMaker(mapSubscriptionDetails.get("ExpectedExceptionsAtMaker"));
					if(!bStatus){
						String errorMsg = Messages.errorMsg;
						Reporting.logResults("Fail", "Validate Exceptions", "Exceptions are not matching with Expected");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "Cannot close the Exceptions ";
							return false;
						}
						if(!isInkindFromCheckerReviewedScreen){
							bStatus = doSubOperationsOnTransactionTrades("Subscription", "Clear");
							if(!bStatus){
								Messages.errorMsg = "Cannot Clear the Subscription Details ";
								return false;
							}
						}else{
							bStatus = doSubOperationsOnTransactionTrades("Subscription", "Cancel");
							if(!bStatus){
								Messages.errorMsg = "Cannot Clear the Subscription Details ";
								return false;
							}
						}

						Messages.errorMsg = errorMsg;

						return false;
					}
					if(mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Save") || mapSubscriptionDetails.get("OperationType").contains("Review"))
					{

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submit') and contains(@onclick,'ExceptionApproved')]"));

						if(!bStatus){
							Messages.errorMsg = "Proceed Button Cannot be clicked";
							Reporting.logResults("Fail", "Select Proceed For Save", "Cannot be Clicked in Proceed Button:"+Messages.errorMsg);

							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
							if(!isInkindFromCheckerReviewedScreen)
							{
								bStatus = doSubOperationsOnTransactionTrades("Subscription", "Clear");

							}
							else{
								bStatus = doSubOperationsOnTransactionTrades("Subscription", "Cancel");

							}
							return false;
						}

					}
				}else{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitForm') and contains(@onclick,'ExceptionApproved')]"), Constants.lTimeOut);
					if(bStatus){
						Reporting.logResults("Fail", "Validating Exceptions", "Exceptions are Not matching");

						bStatus = Elements.click(Global.driver, By.xpath("//div[@id='exceptionBlock']//button[contains(text(),'Close')]"));
						if(!bStatus){
							Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception and Cannot close the Exceptions ";
							return false;
						}
						if(!isInkindFromCheckerReviewedScreen)
						{
							bStatus = doSubOperationsOnTransactionTrades("Subscription", "Clear");
							if(!bStatus){
								Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception and Cannot Clear the Subscription Details ";
								return false;
							}

						}
						else{
							bStatus = doSubOperationsOnTransactionTrades("Subscription", "Cancel");
						}


						Messages.errorMsg = "Exceptions are Visible Even there are No Expected Exception ";
						return false;
					}

				}*/
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Succesfull Message is Not visible";
					return false;
				}
			}
			return bStatus;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	private static Map<String, Map<String, String>> getBankDetailsUsingIndex(String sTestCaseName) {
		try {
			List<String> aTestCase = Arrays.asList(sTestCaseName.split(","));
			Map<String ,Map<String,String>> mapBankDetailswithIndex = new HashMap<>();
			for(int i =0;i<aTestCase.size();i++){
				Map<String ,String> mapIndividulaBankDetails = new HashMap<>();
				if(!aTestCase.get(i).equalsIgnoreCase("None")){
					mapIndividulaBankDetails = ExcelUtils.readDataABasedOnCellName(Global.sSubscriptionTestData, "BankDetailsTestData", aTestCase.get(i));
				}else{
					mapIndividulaBankDetails=null;
				}
				mapBankDetailswithIndex.put(i+"", mapIndividulaBankDetails);

			}
			return mapBankDetailswithIndex;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}


	private static boolean doVerifyOrOverrideFeeDetails(Map<String, String> mapSubscriptionDetails) {
		try {

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-managementFeeNo']/span"));
			if(!bStatus){
				Messages.errorMsg = "Page is Not scrolled to Management Fee field";
				return false;
			}

			if(mapSubscriptionDetails.get("ManagementFeeRadioButton") != null){
				if(mapSubscriptionDetails.get("ManagementFeeRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-managementFeeYes']/span"));
					if(!bStatus){
						Messages.errorMsg = "Management Fee Yes Radio button cannot be clicked";
						return false;
					}
				}
				if(mapSubscriptionDetails.get("ManagementFeeRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-managementFeeNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Management Fee No Radio button cannot be clicked";
						return false;
					}
				}

			}

			if(mapSubscriptionDetails.get("NewManagementFee")!=null)
			{

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newManagementFee']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "New Mangement Fee Text box not visible";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newManagementFee']"), mapSubscriptionDetails.get("NewManagementFee"));
				if(!bStatus){
					Messages.errorMsg = "New Mangement Fee Not Entered in New Fee text box";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[@id='saveNewMngt']"));
				if(!bStatus){
					Messages.errorMsg = "New Mangement Fee Save Button cannot be clicked";
					return false;
				}

			}

			if(mapSubscriptionDetails.get("IncentiveFeeRadioButton") != null){
				if(mapSubscriptionDetails.get("IncentiveFeeRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-incentiveFeeYes']/span"));
					if(!bStatus){
						Messages.errorMsg = "Incentive Fee Yes Radio Button cannot be clicked";
						return false;
					}
				}
				if(mapSubscriptionDetails.get("IncentiveFeeRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-incentiveFeeNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Incentive Fee No Radio Button cannot be clicked";
						return false;
					}
				}

			}

			if(mapSubscriptionDetails.get("NewIncentiveFee")!=null){

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newIncentiveFee']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "New Incentive Fee Text box not visible";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newIncentiveFee']"), mapSubscriptionDetails.get("NewIncentiveFee"));
				if(!bStatus){
					Messages.errorMsg = "New Incentive Fee Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[@id='saveNewInct']"));
				if(!bStatus){
					Messages.errorMsg = "New Incentive Fee Save button cannot be clicked";
					return false;
				}


			}

			bStatus = verifyFeeDetails(mapSubscriptionDetails);
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


	private static boolean verifyFeeDetails(Map<String, String> mapSubscriptionDetails) {
		try {
			String sAppendErrorMessage = "";
			boolean bSValidtatus = true;
			if(mapSubscriptionDetails.get("ManagementFeeRadioButton") != null){
				if(mapSubscriptionDetails.get("ManagementFeeRadioButton").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-managementFeeYes']/span[@class='checked']") , Constants.lTimeOut);
					if(!bStatus){
						sAppendErrorMessage = sAppendErrorMessage+"[ Management Fee Yes Radio button Not Selected ]";
						bSValidtatus = false;
					}
				}
				if(mapSubscriptionDetails.get("ManagementFeeRadioButton").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-managementFeeNo']/span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						sAppendErrorMessage =sAppendErrorMessage+ "[ Management Fee No Radio button Not Selected ]";
						bSValidtatus = false;
					}
				}
			}	

			if(mapSubscriptionDetails.get("ExpectedManagementFee") != null || mapSubscriptionDetails.get("ExpectedNewManagementFee") != null){
				String sFeeTypeXpath = "//label[contains(text(),'Management Fee %')]//following-sibling::div//input[1]";
				String expectedValue = "";
				if(mapSubscriptionDetails.get("ExpectedManagementFee") != null){
					expectedValue = mapSubscriptionDetails.get("ExpectedManagementFee");
				}
				if(mapSubscriptionDetails.get("ExpectedNewManagementFee") != null){
					expectedValue = mapSubscriptionDetails.get("ExpectedNewManagementFee");
				}
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath(sFeeTypeXpath), "value").trim();
				sValue = sValue.replaceAll(",", "");
				if (sValue.equalsIgnoreCase("") || Float.parseFloat(expectedValue) != Float.parseFloat(sValue)) {
					sAppendErrorMessage = sAppendErrorMessage + "[ ERROR : The Actual Management Fee Percentage i.e : "+sValue+", is not matching with Expected i.e. : "+expectedValue+" .] \n";
					bSValidtatus = false;
				}
			}

			if(mapSubscriptionDetails.get("IncentiveFeeRadioButton") != null){
				if(mapSubscriptionDetails.get("IncentiveFeeRadioButton").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-incentiveFeeYes']/span[@class='checked']"), Constants.lTimeOut);
					if(!bStatus){
						sAppendErrorMessage = "Incentive Fee Yes Radio Button not selected";
						bSValidtatus = false;
					}
				}
				if(mapSubscriptionDetails.get("IncentiveFeeRadioButton").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-incentiveFeeNo']/span[@class='checked']"), Constants.lTimeOut);					
					if(!bStatus){
						sAppendErrorMessage = "Incentive Fee No Radio Button not selected";
						bSValidtatus = false;
					}
				}
			}

			if(mapSubscriptionDetails.get("ExpectedIncentiveFee") != null || mapSubscriptionDetails.get("ExectedNewIncentiveFee") != null){
				String sFeeTypeXpath = "//label[contains(text(),'Incentive Fee %')]//following-sibling::div//input";
				String expectedValue = "";
				if(mapSubscriptionDetails.get("ExpectedIncentiveFee") != null){
					expectedValue = mapSubscriptionDetails.get("ExpectedIncentiveFee");
				}
				if(mapSubscriptionDetails.get("ExectedNewIncentiveFee") != null){
					expectedValue = mapSubscriptionDetails.get("ExectedNewIncentiveFee");
				}
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath(sFeeTypeXpath), "value").trim();
				sValue = sValue.replaceAll(",", "");
				if (sValue.equalsIgnoreCase("") || Float.parseFloat(expectedValue) != Float.parseFloat(sValue)) {
					sAppendErrorMessage = sAppendErrorMessage + "[ ERROR : The Actual Incentive Fee Percentage i.e : "+sValue+", is not matching with Expected i.e. : "+expectedValue+" .] \n";
					bSValidtatus = false;
				}
			}


			Messages.errorMsg = sAppendErrorMessage;
			return bSValidtatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	private static boolean doFillBankDetails(Map<String, String> mapSubscriptionDetails , Map<String , Map<String ,String>> mapAllBankDetails) {
		try {

			if(mapSubscriptionDetails.get("BankDetailCash")!=null){
				if(mapSubscriptionDetails.get("BankDetailCash").equalsIgnoreCase("Yes"))
				{
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='bankDetails0.isMoneyReceived1']"));
					if(!bStatus){
						Messages.errorMsg = "Page Not Scrolled to Bank Details";
						return false;
					}
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='bankDetails0.isInkind1']"));
					if(!bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='bankDetails0.isInkind1']"));
						if(!bStatus){
							Messages.errorMsg = "Cash checkbox Not checked";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Money Received")!=null)
					{
						if(mapSubscriptionDetails.get("Money Received").equalsIgnoreCase("Yes"))
						{
							bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='bankDetails0.isMoneyReceived1']"));
							if(!bStatus){
								bStatus = Elements.click(Global.driver, By.xpath("//input[@id='bankDetails0.isMoneyReceived1']"));
								if(!bStatus){
									Messages.errorMsg = "Money Received Not Checked";
									return false;
								}
							}
							if(mapSubscriptionDetails.get("Received Amount")!=null && mapSubscriptionDetails.get("MoneyReceivedDate")!=null && mapSubscriptionDetails.get("Bank Name")!=null){
								bStatus = removeMoneyReceivedRemoveButtons();
								bStatus = addMoneyReceivedDetails(mapSubscriptionDetails , mapAllBankDetails);
								if(!bStatus){
									return false;
								}

							}

						}
						if(mapSubscriptionDetails.get("Money Received").equalsIgnoreCase("No"))
						{
							bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='bankDetails0.isMoneyReceived1']"));
							if(bStatus){
								bStatus = Elements.click(Global.driver, By.xpath("//input[@id='bankDetails0.isMoneyReceived1']"));
								if(!bStatus){
									Messages.errorMsg = "Money Received Not UnChecked";
									return false;
								}
							}
						}

					}

					if(mapSubscriptionDetails.get("IsAMLChecked")!=null)
					{
						bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='bankDetails0.isAMLCompleted1']"));
						if(!bStatus){
							Messages.errorMsg = "Page is not scrolled to Is AML Check box";
							return false;
						}
						if(mapSubscriptionDetails.get("IsAMLChecked").equalsIgnoreCase("Yes"))
						{

							bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='bankDetails0.isAMLCompleted1']"));
							if(!bStatus){
								bStatus = Elements.click(Global.driver, By.xpath("//input[@id='bankDetails0.isAMLCompleted1']"));
								if(!bStatus){
									Messages.errorMsg = "Is AML Not Checked";
									return false;
								}							
							}
							if(mapSubscriptionDetails.get("AMLCheckedDate")!=null)
							{
								Global.driver.findElement(By.xpath("//input[@id='bankDetails0.amlCompletedDate']")).clear();
								bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@id='bankDetails0.amlCompletedDate']"), mapSubscriptionDetails.get("AMLCheckedDate"));
								//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='bankDetails0.amlCompletedDate']"), mapSubscriptionDetails.get("AMLCheckedDate"));
								if(!bStatus){
									Messages.errorMsg = "AML Checked Date is not entered";
									return false;
								}
							}
						}
						if(mapSubscriptionDetails.get("IsAMLChecked").equalsIgnoreCase("No"))
						{
							bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='bankDetails0.isAMLCompleted1']"));
							if(bStatus){
								bStatus = Elements.click(Global.driver, By.xpath("//input[@id='bankDetails0.isAMLCompleted1']"));
								if(!bStatus){
									Messages.errorMsg = "Is AML Not UnChecked";
									return false;
								}							
							}
						}
					}

				}
				if(mapSubscriptionDetails.get("BankDetailCash").equalsIgnoreCase("No"))
				{
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='bankDetails0.isInkind1']"));
					if(bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='bankDetails0.isInkind1']"));
						if(!bStatus){
							Messages.errorMsg = "Cash checkbox Not UnChecked";
							return false;
						}
					}
				}
			}

			if(mapSubscriptionDetails.get("IsInkind")!=null)
			{
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='inkindDetails0.isInkind1']"));
				if(!bStatus){
					Messages.errorMsg = "Page Not Scrolled to Is Inkind Field";
					return false;
				}
				if(mapSubscriptionDetails.get("IsInkind").equalsIgnoreCase("Yes"))
				{
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='inkindDetails0.isInkind1']"));
					if(!bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='inkindDetails0.isInkind1']"));
						if(!bStatus){
							Messages.errorMsg = "IsInkind checkbox Not Checked";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Inkind Type")!=null && mapSubscriptionDetails.get("Inkind Value")!=null && mapSubscriptionDetails.get("Description")!=null){
						if(!isInkindFromCheckerReviewedScreen){
							bStatus = removeInkindDetailsRemoveButtons();
							if(!bStatus){
								return false;
							}
							bStatus = addInKindDetails(mapSubscriptionDetails);
							if(!bStatus){
								return false;
							}
						}else{
							bStatus = removeInkindDetailsRemoveButtons();
							if(!bStatus){
								return false;
							}
							bStatus = addInKindDetailsForCheckerReviewedSubscription(mapSubscriptionDetails);
							if(!bStatus){
								return false;
							}
						}

					}

				}
				if(mapSubscriptionDetails.get("IsInkind").equalsIgnoreCase("No"))
				{
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='inkindDetails0.isInkind1']"));
					if(bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='inkindDetails0.isInkind1']"));
						if(!bStatus){
							Messages.errorMsg = "IsInkind checkbox Not UnChecked";
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


	private static boolean removeMoneyReceivedRemoveButtons() {
		try {

			int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//span[@id='addMoreBlock']//div[@class='row']//button[@id='removeBank']"));
			if(xpathCount==0){
				return true;
			}
			for(int i=(xpathCount+1);i>1;i--){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//span[@id='addMoreBlock']//div[@class='row']["+i+"]//button[@id='removeBank']"));
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//span[@id='addMoreBlock']//div[@class='row']["+i+"]//button[@id='removeBank']"));	
				if(!bStatus){
					Messages.errorMsg = "Money Received Remove button cannot be clicked";
					return false;
				}
			}

			int xpathcountafterRemove = Elements.getXpathCount(Global.driver, By.xpath("//span[@id='addMoreBlock']//div[@class='row']//button[@id='removeBank']"));
			if(xpathcountafterRemove == xpathCount){
				Messages.errorMsg = "Remove buttons in Money Received Details not Removed";
				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean removeInkindDetailsRemoveButtons() {
		try {

			int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//span[@id='addMoreInkindBlock']//div[@class='row']//button[@id='removeInkindType']"));
			if(xpathCount==0){
				return true;
			}
			for(int i=(xpathCount+1);i>1;i--){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//span[@id='addMoreInkindBlock']//div[@class='row']["+i+"]//button[@id='removeInkindType']"));
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//span[@id='addMoreInkindBlock']//div[@class='row']["+i+"]//button[@id='removeInkindType']"));	
				if(!bStatus){
					Messages.errorMsg = "Inkind Details Remove button cannot be clicked";
					return false;
				}
			}

			int xpathcountafterRemove = Elements.getXpathCount(Global.driver, By.xpath("//span[@id='addMoreInkindBlock']//div[@class='row']//button[@id='removeInkindType']"));
			if(xpathcountafterRemove == xpathCount){
				Messages.errorMsg = "Remove buttons in Inkind Details not Removed";
				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	private static boolean addMoneyReceivedDetails(Map<String, String> mapSubscriptionDetails, Map<String, Map<String, String>> mapAllBankDetails) {
		try {

			List<String> aReceivedAmount = Arrays.asList(mapSubscriptionDetails.get("Received Amount").split(","));
			List<String> aReceivedDate = Arrays.asList(mapSubscriptionDetails.get("MoneyReceivedDate").split(","));
			List<String> aBankName = Arrays.asList(mapSubscriptionDetails.get("Bank Name").split(","));
			for(int i=0;i<aReceivedAmount.size();i++){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='bankDetails"+i+".received_Amount']"));
				if(!bStatus){
					Messages.errorMsg = "Page Cannot Scroll to Received Amount Fields";
					return false;
				}
				if(!aReceivedAmount.get(i).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='bankDetails"+i+".received_Amount']"), aReceivedAmount.get(i));
					if(!bStatus){
						Messages.errorMsg = aReceivedAmount.get(i)+" Received Amount Not Entered";
						return false;
					}
				}

				if(!aReceivedDate.get(i).equalsIgnoreCase("None")){
					Global.driver.findElement(By.xpath("//input[@id='bankDetails"+i+".receivedDate']")).clear();
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@id='bankDetails"+i+".receivedDate']"), aReceivedDate.get(i));
					if(!bStatus){
						Messages.errorMsg = aReceivedDate.get(i)+" Money Received Date Not Entered";
						return false;
					}
				}

				if(!aBankName.get(i).equalsIgnoreCase("None")){
					if(aBankName.get(i).equalsIgnoreCase("New"))
					{
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='newBank_"+i+"']"));
						if(!bStatus){
							Messages.errorMsg = "New Babk Link is cannont be Clicked at "+i;
							return false;
						}
						if(mapAllBankDetails.get(i+"")!=null){
							NewUICommonFunctions.isBankMasterBeingCreatedFromTrade = true;
							bStatus = doFillBankMaster(mapAllBankDetails.get(i+""));
							if(!bStatus){
								return false;
							}
						}

					}else
					{
						bStatus = TradeTypeSubscriptionAppFunctions.selectOptionByVisibleText(Global.driver,"//select[@id='bankDetails"+i+".fkBankDetailsIdPk.bankDetailsIdPk']", aBankName.get(i));
						if(!bStatus){
							Messages.errorMsg = aBankName.get(i)+" Bank Name Dropdown Value Not Selected";
							return false;
						}
					}

				}

				if(i<aReceivedAmount.size()-1){
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//button[@id='addMoreBank']"));
					if(!bStatus){
						Messages.errorMsg = "Page Cannot Scroll to Money Received Add More button";
						return false;
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[@id='addMoreBank']"));
					if(!bStatus){
						Messages.errorMsg = "Money Received Add More button cannot be clicked";
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


	private static boolean doOverrideOrVerifyChargeDetails(Map<String, String> mapSubscriptionDetails ) {
		try {

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-noticeChargeNo']/span"));
			if(!bStatus){
				Messages.errorMsg = "Page Not Scrolled to Notice Period Charges Radio Button fields";
				return false;
			}
			if(mapSubscriptionDetails.get("NoticeChargesRadioButton") != null){
				if(mapSubscriptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-noticeChargeNo']/span"));
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
				if(mapSubscriptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-noticeChargeYes']/span"));
					if(!bStatus){
						Messages.errorMsg = "Notice Period Charges Yes Radio Button Not Clicked";
						return false;
					}
				}
			}
			if(mapSubscriptionDetails.get("NewAmountForNoticePeriod")!=null)
			{		
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newNoticeCharges']"), mapSubscriptionDetails.get("NewAmountForNoticePeriod"));
				if(!bStatus){
					Messages.errorMsg = "New Amount for Notice Charges not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[@ng-show='showNewNoticeCharges' and contains(text(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "Notice Period Charges Save butotn not clicked";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-noticeChargeNo']/span"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Notice Charges Radio Buttons are Not visible";
					return false;
				}

			}

			if(mapSubscriptionDetails.get("TransactionChargesRadioButton") != null){
				if(mapSubscriptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Transaction Charges Radio Buttons are Not visible";
						return false;
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
					//bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Transaction Charges No Radio Button Not Clicked";
						return false;
					}
				}
				if(mapSubscriptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-transactionChargesYes']/span"));
					//bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
					if(!bStatus){
						Messages.errorMsg = "Transaction Charges Yes Radio Button Not Clicked";
						return false;
					}
				}
			}

			if(mapSubscriptionDetails.get("NewTransactionCharges")!=null)
			{

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Transaction Charges New Charges field not visible";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), mapSubscriptionDetails.get("NewTransactionCharges"));
				if(!bStatus){
					Messages.errorMsg = "New Transaction Charges not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[@ng-show='showNewTransactionCharges' and contains(text(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "Transaction charges Charges Save butotn not clicked";
					return false;
				}

			}

			if(mapSubscriptionDetails.get("Adhoc Charges")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='orderDetails0.adhocCharges']"), mapSubscriptionDetails.get("Adhoc Charges"));
				if(!bStatus){
					Messages.errorMsg = "Adhoc Charges Not Entered";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Interest")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='orderDetails0.interest']"), mapSubscriptionDetails.get("Interest"));
				if(!bStatus){
					Messages.errorMsg = "Interest Value Not Entered";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("InclusiveOrExclusive")!=null)
			{
				if(mapSubscriptionDetails.get("InclusiveOrExclusive").equalsIgnoreCase("Inclusive")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-inclusive']/span"));
					if(!bStatus){
						Messages.errorMsg = "Inclusive button Not clicked";
						return false;
					}					
				}
				if(mapSubscriptionDetails.get("InclusiveOrExclusive").equalsIgnoreCase("Exclusive")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-exclusive']/span"));
					if(!bStatus){
						Messages.errorMsg = "Exclusive button Not clicked";
						return false;
					}					
				}

			}

			bStatus = doVerifyChargesDetail(mapSubscriptionDetails);
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


	public static boolean doFillInvestmentDetails(Map<String, String> mapSubscriptionDetails) {
		try {
			Thread.sleep(2000);
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='effectiveDate']"));
			if(!bStatus){
				Messages.errorMsg = "Page Not Scrolled to Cash or Units Radio Button";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[text()='Class Currency']"));
			if(!bStatus){
				return false;
			}
			if(mapSubscriptionDetails.get("Effective Date")!=null){
				Global.driver.findElement(By.xpath("//input[@id='effectiveDate']")).clear();
				bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@id='effectiveDate']"), mapSubscriptionDetails.get("Effective Date"));
				//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapSubscriptionDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "Effective Date Not Entered";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Investment Type CashorUnits")!=null)
			{
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-isAmount']/span"));
				if(!bStatus){
					Messages.errorMsg = "Page Not Scrolled to Cash or Units Radio Button";
					return false;
				}
				if(mapSubscriptionDetails.get("Investment Type CashorUnits").equalsIgnoreCase("Cash"))
				{
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-isAmount']/span"));
					if(!bStatus){
						Messages.errorMsg ="Cash Radio button cannot be selected";
						return false;
					}
					if(mapSubscriptionDetails.get("CashOrNoOfUnits")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapSubscriptionDetails.get("CashOrNoOfUnits"));
						if(!bStatus){
							bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-isAmount']/span/input[@disabled='disabled']"), Constants.lTimeOut);
							if(bStatus){
								Messages.errorMsg = "Cash Radio button is in disable mode cannot enter Amount";
								return false;
							}
							Messages.errorMsg = "Cash value Not Entered";
							return false;
						}
					}

					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Cash')]"));
					if(!bStatus){
						Messages.errorMsg = "Failed to Click on Cash Label";
						return false;
					}

				}
				if(mapSubscriptionDetails.get("Investment Type CashorUnits").equalsIgnoreCase("Units"))
				{
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-isUnits']/span"));
					if(!bStatus){
						Messages.errorMsg ="Units Radio button not selected";
						return false;
					}
					if(mapSubscriptionDetails.get("CashOrNoOfUnits")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='units']"), mapSubscriptionDetails.get("CashOrNoOfUnits"));
						if(!bStatus){
							bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='uniform-isUnits']/span/input[@disabled='disabled']"), Constants.lTimeOut);
							if(bStatus){
								Messages.errorMsg = "Units Radio button is in disable mode cannot enter Units";
								return false;
							}
							Messages.errorMsg = "Units value Not Entered";
							return false;
						}
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'Cash')]"));
					if(!bStatus){
						Messages.errorMsg = "Failed to Click on Cash Label";
						return false;
					}
				}

			}

			if(mapSubscriptionDetails.get("Strategy")!=null){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='s2id_fkStrategyIdPk.strategyIdPk']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Page Not Scrolled to Strategy Field";
					return false;
				}
				NewUICommonFunctions.threadSleep();
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Strategy']"));
				if(!bStatus){
					Messages.errorMsg = "Cannot select Strategy Dropdown";
					return false;
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Strategy"), By.xpath("//div[@id='s2id_fkStrategyIdPk.strategyIdPk']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Strategy Dropdown cannot be selected";
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


	public static boolean doFillFundDetails(Map<String, String> mapSubscriptionDetails) {
		try {

			if(mapSubscriptionDetails.get("Client Name")!=null){				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Client Name"), By.xpath("//div[@id='s2id_clientNameDropdown']//span[contains(@id,'select2-chosen')]"));				
				if(!bStatus){
					return false;
				}
			}


			if(mapSubscriptionDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Fund Family Name"), By.xpath("//div[@id='s2id_fundFamilyNameDropdown']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Legal Entity Name")!=null){
				NewUICommonFunctions.threadSleep();
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Legal Entity Name"), By.xpath("//div[@id='s2id_legalEntityNameDropdown']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Class Name")!=null){
				NewUICommonFunctions.threadSleep();
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Class Name"), By.xpath("//div[@id='s2id_classNameDropdown']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					return false;
				}
			}

			if(mapSubscriptionDetails.get("New Series")!=null && mapSubscriptionDetails.get("New Series").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='newSeriesLink']"));
				if(!bStatus){
					Messages.errorMsg = "New Series Link cannot be clicked";
					return false;
				}
				if(mapSubscriptionDetails.get("New Series TestCaseName")!=null){
					NewUICommonFunctions.bTradeSubscription = true;
					bStatus = createNewSeriesFromTradeTypeSubscription(mapSubscriptionDetails.get("New Series TestCaseName"));
					if(!bStatus){
						return false;
					}
				}		
			}

			if(mapSubscriptionDetails.get("Series Name")!=null){
				TimeUnit.SECONDS.sleep(3);
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Series Name"), By.xpath("//div[@id='s2id_seriesNameDropdown']//span[contains(@id,'select2-chosen')]"));
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


	public static boolean doFillInvestorDetails(Map<String, String> mapSubscriptionDetails) {
		try {

			if(mapSubscriptionDetails.get("New Investor")!=null && mapSubscriptionDetails.get("New Investor").equalsIgnoreCase("Yes")){
				bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Investor Name')]//following-sibling::span/a[contains(text(),'New Investor')]"));
				if(!bStatus){
					Messages.errorMsg = "New Investor Link cannot be clicked";
					return false;
				}
				if(mapSubscriptionDetails.get("New Investor TestCaseName")!=null){
					InvestorMasterAppFunctions.bTadingInvestorFlag = true;
					bStatus = createNewInvestorFromTradeTypeSubscription(mapSubscriptionDetails.get("New Investor TestCaseName"));
					if(!bStatus){
						return false;
					}
				}

			}

			if(mapSubscriptionDetails.get("Investor Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Investor Name"), By.xpath("//div[@id='s2id_investorNameDropdown']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					return false;
				}
			}

			if(mapSubscriptionDetails.get("New Holder")!=null && mapSubscriptionDetails.get("New Holder").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='newHolderLink']"));
				if(!bStatus){
					Messages.errorMsg = "New Holder Link Cannot be clicked"; 
					return false;
				}
				if(mapSubscriptionDetails.get("New Holder TestCaseName")!=null){
					HolderMasterAppFunctions.bTradingSubscription = true;
					bStatus = createNewHolderFromTradeTypeSubscription(mapSubscriptionDetails.get("New Holder TestCaseName"));
					if(!bStatus){
						return false;
					}
				}
			}

			if(mapSubscriptionDetails.get("Holder Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Holder Name"), By.xpath("//div[@id='s2id_holderNameDropdown']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					return false;
				}
			}

			if(mapSubscriptionDetails.get("NewAccount")!=null && mapSubscriptionDetails.get("NewAccount").equalsIgnoreCase("Yes")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='newAccountLink']"));
				if(!bStatus){
					Messages.errorMsg = "New Account Link Cannont be clicked";
					return false;
				}
				Thread.sleep(2000);

				newAccountID = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_accountNameDropdown']//span"));
				if(newAccountID==null || newAccountID.equalsIgnoreCase("") ){
					Messages.errorMsg = "New Account ID is not Generated";
					return false;
				}

				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='s2id_accountNameDropdown']//span[text()='"+newAccountID+"']"), Constants.lTimeOut);
				if(!newAccountID.startsWith("AC0") || !bStatus){
					Thread.sleep(2000);
					newAccountID = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_accountNameDropdown']//span"));
					if(newAccountID==null || newAccountID.equalsIgnoreCase("") ){
						Messages.errorMsg = "New Account ID is not Generated";
						return false;
					}
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='s2id_accountNameDropdown']//span[text()='"+newAccountID+"']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = ""+newAccountID+" is Not Present in Account ID Dropdown Field";
						return false;
					}

				}

			}

			if(mapSubscriptionDetails.get("Account ID")!=null){
				String sAccountRetrieveTCName = mapSubscriptionDetails.get("Account ID");
				Thread.sleep(2000);
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapSubscriptionDetails.get("Account ID"));
				if (mapXMLSubscriptionDetails != null && !mapXMLSubscriptionDetails.isEmpty() && mapXMLSubscriptionDetails.get("AccountID") != null) {
					mapSubscriptionDetails.put("Account ID", mapXMLSubscriptionDetails.get("AccountID"));
				}
				else {
					Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapSubscriptionDetails.get("Account ID"));
					if (mapXMLAccountMasterDetails != null && !mapXMLAccountMasterDetails.isEmpty() && mapXMLAccountMasterDetails.get("AccountID") != null) {
						mapSubscriptionDetails.put("Account ID", mapXMLAccountMasterDetails.get("AccountID"));
					}
				}
				if (mapSubscriptionDetails.get("Account ID") == null ) {
					Messages.errorMsg = "[ ERROR : 'Account Id' wasn't available, respective 'Investor Account Master' creation might have failed please do cross check with Test case name : '"+sAccountRetrieveTCName+"'.]";
					return false;
				}
				bStatus =NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Account ID"), By.xpath("//div[@id='s2id_accountNameDropdown']//span[contains(@id,'select2-chosen')]"));
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


	public static boolean doFillRequestDetails(Map<String, String> mapSubscriptionDetails) {
		try {

			if(mapSubscriptionDetails.get("Request Received Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='requestDate']"), mapSubscriptionDetails.get("Request Received Date"));
				if(!bStatus){
					Messages.errorMsg = "Request Received Date Not Entered";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Request Received Time")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='requestTime']"), mapSubscriptionDetails.get("Request Received Time"));
				if(!bStatus){
					Messages.errorMsg = "Request Received Time Not Entered";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Subscription')]"));
				if(!bStatus){
					Messages.errorMsg = "Failed to click on subscription Label";
					return false;
				}
			}
			if(mapSubscriptionDetails.get("Order Received Office")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='orderReceivedOffice']"), mapSubscriptionDetails.get("Order Received Office"));
				if(!bStatus){
					Messages.errorMsg = "Order Received Office Not Entered";
					return false;
				}
			}
			if(mapSubscriptionDetails.get("Time Zone")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@placeholder='Time Zone']"), mapSubscriptionDetails.get("Time Zone"));
				if(!bStatus){
					Messages.errorMsg = "Time Zone Not Entered";
					return false;
				}

			}
			if(mapSubscriptionDetails.get("Source")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='sourceValue']"), mapSubscriptionDetails.get("Source"));
				if(!bStatus){
					Messages.errorMsg = "Source Not Entered";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Mode of Request")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Mode of Request"), By.xpath("//div[@id='s2id_fkModeOfRequestIdPk']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Mode of Request Drop down Not Selected";
					return false;
				}
			}


			if(mapSubscriptionDetails.get("External ID")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='externalId']"), mapSubscriptionDetails.get("External ID"));
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


	private static boolean addInKindDetails(Map<String, String> mapSubscriptionDetails) {
		try {

			List<String> aInkindType = Arrays.asList(mapSubscriptionDetails.get("Inkind Type").split(","));
			List<String> aInkindValue = Arrays.asList(mapSubscriptionDetails.get("Inkind Value").split(","));
			List<String> aDesCription = Arrays.asList(mapSubscriptionDetails.get("Description").split(","));

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='inkindDetails0.received_Amount']"));
			if(!bStatus){
				Messages.errorMsg = "Page Not Scrolled to inkind Value Field";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Subscription')]"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Subscription Label";
				return false;
			}
			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aInkindType.get(0), By.xpath("//div[@id='s2id_inkindDetails0.fkInKindTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]"));
			//bStatus = Elements.selectOptionByValue(Global.driver, "//select[@id='inkindDetails"+i+".fkInKindTypeIdPk.referenceIdPk']", aInkindType.get(i));					
			if(!bStatus){
				Messages.errorMsg = "Inkind Tyep"+aInkindType.get(0)+" Not Selected From Dropdown";
				return false;
			}

			if(!aInkindValue.get(0).equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='inkindDetails0.received_Amount']"), aInkindValue.get(0));
				if(!bStatus){
					Messages.errorMsg = "Inind Value "+aInkindValue.get(0)+" Not Entered";
					return false;
				}
			}

			if(!aDesCription.get(0).equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver, By.xpath("//div/textarea[@id='inkindDetails0.vcDescription']"), aDesCription.get(0));
				if(!bStatus){
					Messages.errorMsg = "Inind Description "+aDesCription.get(0)+" Not Entered";
					return false;
				}
			}

			for(int i=1;i<aInkindType.size();i++)
			{
				if(i<aInkindType.size())
				{
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//button[@id='addMoreInkindType']"));
					if(!bStatus){
						Messages.errorMsg = "Page Not scrolled to Is Inkind Add More Button";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[@id='addMoreInkindType']"));
					if(!bStatus){
						Messages.errorMsg = "Is Inkind Add More Button Not Clicked";
						return false;
					}
				}
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='inkindDetails"+i+".received_Amount']"));
				if(!bStatus){
					Messages.errorMsg = "Page Not Scrolled to inkind Value Field";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Subscription')]"));
				if(!bStatus){
					Messages.errorMsg = "Cannot click on Subscription Label";
					return false;
				}
				if(!aInkindType.get(i).equalsIgnoreCase("None"))
				{

					//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aInkindType.get(i), By.xpath("//div[@id='s2id_inkindDetails"+i+".fkInKindTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]"));
					bStatus = Elements.selectOptionByVisibleText(Global.driver, "//select[@id='inkindDetails"+i+".fkInKindTypeIdPk.referenceIdPk']", aInkindType.get(i));					
					if(!bStatus){
						Messages.errorMsg = "Inkind Tyep"+aInkindType.get(i)+" Not Selected From Dropdown";
						return false;
					}
				}

				if(!aInkindValue.get(i).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='inkindDetails"+i+".received_Amount']"), aInkindValue.get(i));
					if(!bStatus){
						Messages.errorMsg = "Inind Value "+aInkindValue.get(i)+" Not Entered";
						return false;
					}
				}

				if(!aDesCription.get(i).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//div/textarea[@id='inkindDetails"+i+".vcDescription']"), aDesCription.get(i));
					if(!bStatus){
						Messages.errorMsg = "Inind Description "+aDesCription.get(i)+" Not Entered";
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

	private static boolean addInKindDetailsForCheckerReviewedSubscription(Map<String, String> mapSubscriptionDetails) {
		try {

			List<String> aInkindType = Arrays.asList(mapSubscriptionDetails.get("Inkind Type").split(","));
			List<String> aInkindValue = Arrays.asList(mapSubscriptionDetails.get("Inkind Value").split(","));
			List<String> aDesCription = Arrays.asList(mapSubscriptionDetails.get("Description").split(","));

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='inkindDetails0.received_Amount']"));
			if(!bStatus){
				Messages.errorMsg = "Page Not Scrolled to inkind Value Field";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Subscription')]"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Subscription Label";
				return false;
			}

			for(int i=0;i<aInkindType.size();i++)
			{

				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='inkindDetails"+i+".received_Amount']"));
				if(!bStatus){
					Messages.errorMsg = "Page Not Scrolled to inkind Value Field";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Subscription')]"));
				if(!bStatus){
					Messages.errorMsg = "Cannot click on Subscription Label";
					return false;
				}
				if(!aInkindType.get(i).equalsIgnoreCase("None"))
				{

					//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aInkindType.get(i), By.xpath("//div[@id='s2id_inkindDetails"+i+".fkInKindTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]"));
					bStatus = Elements.selectOptionByVisibleText(Global.driver, "//select[@id='inkindDetails"+i+".fkInKindTypeIdPk.referenceIdPk']", aInkindType.get(i));					
					if(!bStatus){
						Messages.errorMsg = "Inkind Tyep"+aInkindType.get(i)+" Not Selected From Dropdown";
						return false;
					}
				}

				if(!aInkindValue.get(i).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='inkindDetails"+i+".received_Amount']"), aInkindValue.get(i));
					if(!bStatus){
						Messages.errorMsg = "Inind Value "+aInkindValue.get(i)+" Not Entered";
						return false;
					}
				}

				if(!aDesCription.get(i).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//div/textarea[@id='inkindDetails"+i+".vcDescription']"), aDesCription.get(i));
					if(!bStatus){
						Messages.errorMsg = "Inind Description "+aDesCription.get(i)+" Not Entered";
						return false;
					}
				}

				if(i<aInkindType.size()-1)
				{
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//button[@id='addMoreInkindType']"));
					if(!bStatus){
						Messages.errorMsg = "Page Not scrolled to Is Inkind Add More Button";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[@id='addMoreInkindType']"));
					if(!bStatus){
						Messages.errorMsg = "Is Inkind Add More Button Not Clicked";
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


	public static boolean createNewInvestorFromTradeTypeSubscription(String sInvestorTestCaseName){
		try {
			if (sInvestorTestCaseName.equalsIgnoreCase("") || sInvestorTestCaseName == null) {
				Messages.errorMsg = "[ ERROR : Investor Test Case Name wasn't Given inorder to create a new Investor from Trade type 'Subcription' .]";
				return false;
			}
			Map<String, Map<String,String>> objInvestorCreationTabsMaps = new HashMap<String, Map<String,String>>();
			Map<String, String> mapInvestorGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, "GeneralDetails", sInvestorTestCaseName);
			if (mapInvestorGeneralDetails.get("InvestorType") != null && mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity")) {
				Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "RegisteredAddressDetails", mapInvestorGeneralDetails.get("TestcaseName"));
				objInvestorCreationTabsMaps.put("RegisteredAddressDetails", mapRegisteredAddressDetails);
			}
			Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sInvestorTestDataFilePath, "AddressofCorrespondence", mapInvestorGeneralDetails.get("TestcaseName"));					
			Map<String , String> mapInvestorDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "InvestorDetails", mapInvestorGeneralDetails.get("TestcaseName"));
			Map<String , String> mapFatcaDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "FatcaDetails", mapInvestorGeneralDetails.get("TestcaseName"));
			Map<String , String> mapKYCDetails = Utilities.readTestData(Global.sInvestorTestDataFilePath, "KYCDetails", mapInvestorGeneralDetails.get("TestcaseName"));

			objInvestorCreationTabsMaps.put("GeneralDetails", mapInvestorGeneralDetails);
			objInvestorCreationTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);			
			objInvestorCreationTabsMaps.put("InvestorDetails", mapInvestorDetails);
			objInvestorCreationTabsMaps.put("FatcaDetails", mapFatcaDetails);
			objInvestorCreationTabsMaps.put("KYCDetails", mapKYCDetails);			
			if (mapAddressofCorrespondence == null) {
				Messages.errorMsg = "[ ERROR : No data exists with the given test case name in the Investor tets data sheet. ]";
			}
			if (objInvestorCreationTabsMaps != null) {
				bStatus = InvestorMasterAppFunctions.createNewInvestor(objInvestorCreationTabsMaps);
				if (!bStatus) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static boolean createNewHolderFromTradeTypeSubscription(String sHolderTestCaseName){
		try {			
			if (sHolderTestCaseName.equalsIgnoreCase("") || sHolderTestCaseName == null) {
				Messages.errorMsg = "[ ERROR : Holder Test Case Name wasn't Given inorder to create a new Investor from Trade type 'Subcription' .]";
				return false;
			}
			Map<String, Map<String,String>> objHolderCreationTabsMaps = new HashMap<String, Map<String,String>>();
			Map<String, String> mapHolderGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, "GeneralDetails", sHolderTestCaseName);
			HolderMasterAppFunctions.HolderType = mapHolderGeneralDetails.get("HolderType");
			HolderMasterAppFunctions.isHolderGeneralDetailsModifyFlag = false;
			if (mapHolderGeneralDetails.get("HolderType") != null && mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity")) {
				Map<String , String> mapRegisteredAddressDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "RegisteredAddressDetails", mapHolderGeneralDetails.get("TestcaseName"));
				objHolderCreationTabsMaps.put("RegisteredAddressDetails", mapRegisteredAddressDetails);
			}
			Map<String , String> mapAddressofCorrespondence = Utilities.readTestData(Global.sHolderTestDataFilePath, "AddressofCorrespondence", mapHolderGeneralDetails.get("TestcaseName"));					
			Map<String , String> mapHolderDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "HolderDetails", mapHolderGeneralDetails.get("TestcaseName"));
			Map<String , String> mapFatcaDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "FatcaDetails", mapHolderGeneralDetails.get("TestcaseName"));
			Map<String , String> mapKYCDetails = Utilities.readTestData(Global.sHolderTestDataFilePath, "KYCDetails", mapHolderGeneralDetails.get("TestcaseName"));

			objHolderCreationTabsMaps.put("GeneralDetails", mapHolderGeneralDetails);
			objHolderCreationTabsMaps.put("AddressofCorrespondence", mapAddressofCorrespondence);		
			objHolderCreationTabsMaps.put("HolderDetails", mapHolderDetails);
			objHolderCreationTabsMaps.put("FatcaDetails", mapFatcaDetails);
			objHolderCreationTabsMaps.put("KYCDetails", mapKYCDetails);

			if (mapHolderDetails == null) {
				Messages.errorMsg = "[ ERROR : No data exists with the given test case name in the Holder tets data sheet. ]";
			}

			bStatus = HolderMasterAppFunctions.createNewHolder(objHolderCreationTabsMaps);
			if (!bStatus) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static boolean createNewSeriesFromTradeTypeSubscription(String sSeriesTestCaseName){
		try {
			if (sSeriesTestCaseName.equalsIgnoreCase("") || sSeriesTestCaseName == null) {
				Messages.errorMsg = "[ ERROR : Series Test Case Name wasn't Given inorder to create a new Investor from Trade type 'Subcription' .]";
				return false;
			}

			Map<String, String> mapSeriesDetails = ExcelUtils.readDataABasedOnCellName(Global.sSeriesTestDataFilePath, "SeriesTestData", sSeriesTestCaseName);
			if (mapSeriesDetails == null) {
				Messages.errorMsg = "[ ERROR : No data exists with the given test case name in the Series tets data sheet. ]";
				return false;
			}

			bStatus = SeriesAppFunctions.addNewSeries(mapSeriesDetails);
			if (!bStatus) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static boolean doVerifyExceptionsAtCheckerForTradeTypeSubscription(String sExceptions){		
		try {	
			boolean isExceptionsMismatch = false;
			List<String> sActualExceptions = new ArrayList<String>();
			if (sExceptions != null && !sExceptions.equalsIgnoreCase("")) {
				List<String> sExceptionList = Arrays.asList(sExceptions.split(","));

				int iNoOfExceptions = Elements.getXpathCount(Global.driver, By.xpath("//h4[contains(text(),'Exception')]//following-sibling::div"));

				if (sExceptionList.size() != iNoOfExceptions) {
					isExceptionsMismatch = true;
				}
				for (int i = 1; i <= iNoOfExceptions; i++) {
					String sExceptionText = Elements.getText(Global.driver, By.xpath("//h4[contains(text(),'Exception')]//following-sibling::div["+i+"]//label"));
					sActualExceptions.add(sExceptionText);
					if (sExceptionText != null && !sExceptionList.contains(sExceptionText.trim())) {
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


	public static boolean doVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription(String sOverridedChargeName, String isChangeStatusYesToNo){
		try {
			String sChargeTypeNameSubString = "";
			if (sOverridedChargeName != null) {
				if (sOverridedChargeName.toLowerCase().contains("notice")) {
					sChargeTypeNameSubString = "Notice";
				}
				if (sOverridedChargeName.toLowerCase().contains("management")) {
					sChargeTypeNameSubString = "Management";
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
				/*bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4[contains(text(),'Override')]//following-sibling::div[contains(@ng-init,'override')]//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//input[contains(@id,'Yes')]//parent::span[@class='checked']"), 3);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Overrided 'Notice Period Charges' aren't marked to 'YES' in Checker verification.] \n";
					return false;
				}*/
				if (isChangeStatusYesToNo != null && isChangeStatusYesToNo.equalsIgnoreCase("No")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//h4[contains(text(),'Override')]//following-sibling::div[contains(@ng-init,'override')]//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//input[contains(@id,'Yes')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to change the status of Overrided chanrges of "+sChargeTypeNameSubString+" to 'Yes'.] \n";
						return false;
					}
				}
				if (isChangeStatusYesToNo != null && isChangeStatusYesToNo.equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//h4[contains(text(),'Override')]//following-sibling::div[contains(@ng-init,'override')]//label[contains(text(),'"+sChargeTypeNameSubString+"')]//following-sibling::div//input[contains(@id,'No')]"));
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


	public static boolean doVerifyFeeDetailsAtMakerAndChecker(String sExpectedPercentage, String sFeeType, String isFeeTypeDetailsOverrided ,int noOfDecimlas){
		boolean bOverridedStatus = true;
		try {
			String sAppendErrorMessage = "";
			String sFeeTypeXpath = "";			
			if (sFeeType.toLowerCase().contains("management")) {

				sFeeTypeXpath = "//label[contains(text(),'Management Fee %')]//following-sibling::div//input[1]";

				if (isFeeTypeDetailsOverrided != null && isFeeTypeDetailsOverrided.equalsIgnoreCase("Yes")) {
					//If overriding then verifying for the 'DEFAULT' values option checked to "NO".
					bOverridedStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='managementFeeNo']//parent::span[@class='checked']"), 3);
					if (!bOverridedStatus) {
						sAppendErrorMessage = sAppendErrorMessage + "[ ERROR : Management Fee Default is not marked to 'NO' when details are Overrided. ] \n";
						bOverridedStatus = false;
					}
				}
				if (isFeeTypeDetailsOverrided != null && isFeeTypeDetailsOverrided.equalsIgnoreCase("No")) {
					//If NOT overrided then verifying for the 'DEFAULT' values option checked to "YES".					
					if(!sExpectedPercentage.equalsIgnoreCase("0")){
						bOverridedStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='managementFeeYes']//parent::span[@class='checked']"), 3);
						if (!bOverridedStatus) {
							sAppendErrorMessage = sAppendErrorMessage + "[ ERROR : Management Fee Default is not marked to 'YES' when details are NOT Overrided. ] \n";
							bOverridedStatus = false;
						}
					}				
				}
			}
			if (sFeeType.toLowerCase().contains("incentive")) {

				sFeeTypeXpath = "//label[contains(text(),'Incentive Fee %')]//following-sibling::div//input";

				if (isFeeTypeDetailsOverrided != null && isFeeTypeDetailsOverrided.equalsIgnoreCase("Yes")) {
					//If overriding then verifying for the 'DEFAULT' values option checked to "NO".
					bOverridedStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='incentiveFeeNo']//parent::span[@class='checked']"), 3);					
					if (!bOverridedStatus) {
						sAppendErrorMessage = sAppendErrorMessage + "[ ERROR : Incentive Fee Default is not marked to 'NO' when details are Overrided. ] \n";
						bOverridedStatus = false;
					}
				}
				if (isFeeTypeDetailsOverrided != null && isFeeTypeDetailsOverrided.equalsIgnoreCase("No")) {
					//If NOT overriding then verifying for the 'DEFAULT' values option checked to "YES".
					if(!sExpectedPercentage.equalsIgnoreCase("0")){
						bOverridedStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='incentiveFeeYes']//parent::span[@class='checked']"), 3);					
						if (!bOverridedStatus) {
							sAppendErrorMessage = sAppendErrorMessage + "[ ERROR : Incentive Fee Default is not marked to 'YES' when details are NOT Overrided. ] \n";
							bOverridedStatus = false;
						}
					}				
				}
			}
			Wait.waitForElementPresence(Global.driver, By.xpath(sFeeTypeXpath), 5);
			String sValue = Elements.getElementAttribute(Global.driver, By.xpath(sFeeTypeXpath), "value").trim();
			sExpectedPercentage = sExpectedPercentage.replaceAll(",", "");
			sValue = sValue.replaceAll(",", "");
			bStatus = NewUICommonFunctions.verifyDecimalsToDisplay(sFeeType, noOfDecimlas, sValue);
			if(!bStatus){
				sAppendErrorMessage = sAppendErrorMessage + "[ "+Messages.errorMsg+" ]";
				bOverridedStatus = false;
			}
			if (sValue.equalsIgnoreCase("") || Float.parseFloat(sExpectedPercentage) != Float.parseFloat(sValue)) {
				sAppendErrorMessage = sAppendErrorMessage + "[ ERROR : The Actual "+sFeeType+" Fee Percentage i.e : "+sValue+", is not matching with Expected i.e. : "+sExpectedPercentage+" .] \n";
				bOverridedStatus = false;
			}
			Messages.errorMsg = sAppendErrorMessage;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bOverridedStatus;
	}


	public static boolean doVerifyChargesDetail(Map<String,String>mapVerifySubscriptionDetails){
		try
		{
			boolean validateStatus = true;
			String appendMsg="";

			//Thread.sleep(2000);

			if(mapVerifySubscriptionDetails.get("NoticeChargesRadioButton") != null){
				if(mapVerifySubscriptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){
					// Verify Notice Charge Default

					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-noticeChargeYes']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+ "[ Notice Charge Default Radio button Yes is not selected  ]\n";
					}
				}
				if(mapVerifySubscriptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-noticeChargeNo']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = "[ Notice Charge Default Radio button No is not selected  ]\n";
					}
				}
			}

			// Verify Notice Charges  with out modification
			if(mapVerifySubscriptionDetails.get("ExpectedNoticeCharges") != null )
			{
				// Verify Notice Charges 
				String ActualNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("orderDetails0.noticeCharges");

				String ExpectedNoticeCharge = mapVerifySubscriptionDetails.get("ExpectedNoticeCharges");

				if(ActualNoticeCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Notice Charges ", Integer.parseInt(mapVerifySubscriptionDetails.get("ExpectedNumberOfDecimals")), ActualNoticeCharges);
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					}
					if(Float.parseFloat(ActualNoticeCharges) != Float.parseFloat(ExpectedNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Notice Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("ExpectedNoticeCharges")+" ]\n";
					}
				}
			}

			// Verify Notice Charges  with modification
			if(mapVerifySubscriptionDetails.get("ExpectedNewNoticeCharges") !=null){

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("orderDetails0.noticeCharges");
				String ExpectedNewNoticeCharge = mapVerifySubscriptionDetails.get("ExpectedNewNoticeCharges");
				if(ActualNewNoticeCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Notice Charges ", Integer.parseInt(mapVerifySubscriptionDetails.get("ExpectedNumberOfDecimals")), ActualNewNoticeCharges);
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					}
					if(Float.parseFloat(ActualNewNoticeCharges) != Float.parseFloat(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Notice Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("ExpectedNewNoticeCharges")+" ]\n";
					}
				}
			}

			if(mapVerifySubscriptionDetails.get("TransactionChargesRadioButton") != null){
				if(mapVerifySubscriptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){
					// Verify Notice Charge Default
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-transactionChargesYes']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg =  appendMsg+"[Transaction Charge Default Radio button Yes is not selected  ]\n";
					}
				}
				if(mapVerifySubscriptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = "[ Transaction  Charge Default Radio button No Radio button is not selected ]\n";
					}
				}
			}
			//Verify the Transaction Charges with out modification

			if(mapVerifySubscriptionDetails.get("ExpectedTransactionCharges") != null ){		
				// Verify Notice Charges 
				String ActualTransCharges = NewUICommonFunctions.jsGetElementAttribute("orderDetails0.transactionCharges");

				String ExpectedTransNoticeCharge = mapVerifySubscriptionDetails.get("ExpectedTransactionCharges");

				if(ActualTransCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Transaction Charges ", Integer.parseInt(mapVerifySubscriptionDetails.get("ExpectedNumberOfDecimals")), ActualTransCharges);
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					}
					if(Float.parseFloat(ActualTransCharges) != Float.parseFloat(ExpectedTransNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Transaction  Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("ExpectedTransactionCharges")+" ]\n";
					}
				}
			}

			// Verify Transaction  Charges  with modification

			if(mapVerifySubscriptionDetails.get("NewExpectedTransactionCharges") !=null){
				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("orderDetails0.transactionCharges");
				String ExpectedNewNoticeCharge = mapVerifySubscriptionDetails.get("NewExpectedTransactionCharges");

				if( ActualNewNoticeCharges != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Transaction Charges ", Integer.parseInt(mapVerifySubscriptionDetails.get("ExpectedNumberOfDecimals")), ActualNewNoticeCharges);
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					}
					if(Float.parseFloat(ActualNewNoticeCharges) != Float.parseFloat(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Transaction  Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("NewExpectedTransactionCharges")+" ]\n";
					}
				}
			}

			//Verify  Total charges
			if(mapVerifySubscriptionDetails.get("ExpectedTotalCharges")!=null){
				String ActualToatal =  NewUICommonFunctions.jsGetElementAttribute("orderDetails0.charges");
				String ExpectedCharge = mapVerifySubscriptionDetails.get("ExpectedTotalCharges");

				if(ActualToatal != null){
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Total Charges ", Integer.parseInt(mapVerifySubscriptionDetails.get("ExpectedNumberOfDecimals")), ActualToatal);
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					}
					if(Float.parseFloat(ActualToatal) != Float.parseFloat(ExpectedCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Total  Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("ExpectedTotalCharges")+" ]\n";
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


	public static boolean doVerifyAmountDetails(Map<String,String>mapVerifyAmountDetail){
		try{
			boolean validateStatus = true;
			String appendMsg="";


			if (mapVerifyAmountDetail.get("ExpectedHighWaterMark") != null)
			{
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[contains(@id,'HWM')]"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("")) {
					appendMsg = appendMsg+"[ERROR : Actual HighWaterMark : "+sValue+" for Type Cash is not matching with Expected HighWaterMark : "+mapVerifyAmountDetail.get("ExpectedHighWaterMark")+" .] \n";
					validateStatus = false;
				}
				if (sValue != null && !sValue.equalsIgnoreCase("")) {
					sValue = sValue.replaceAll(",", "");
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("High Water Mark", Integer.parseInt(mapVerifyAmountDetail.get("ExpectedNumberOfDecimals")), sValue);
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					}
					if (Float.parseFloat(sValue) != Float.parseFloat(mapVerifyAmountDetail.get("ExpectedHighWaterMark"))) {
						appendMsg = appendMsg+"[ERROR : Actual HighWaterMark : "+sValue+" for Type Cash is not matching with Expected HighWaterMark : "+mapVerifyAmountDetail.get("ExpectedHighWaterMark")+" .] \n";
						validateStatus = false;
					}
				}
			}	

			//Verify charges
			if(mapVerifyAmountDetail.get("ExpectedAmountDetailCharges")!= null){
				bStatus  = TradeTypeSubscriptionAppFunctions.VerifyLabelHiddenText("Charges",mapVerifyAmountDetail.get("ExpectedAmountDetailCharges"),mapVerifyAmountDetail.get("ExpectedNumberOfDecimals"));
				if(!bStatus){
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
					validateStatus = false;
				}
			}

			//Verify Interest

			if(mapVerifyAmountDetail.get("ExpectedAmountDetailInterest")!= null){
				bStatus  = TradeTypeSubscriptionAppFunctions.VerifyLabelHiddenText("Interest",mapVerifyAmountDetail.get("ExpectedAmountDetailInterest"),mapVerifyAmountDetail.get("ExpectedNumberOfDecimals"));
				if(!bStatus){
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
					validateStatus = false;
				}
			}


			//Verify Amount Payable
			if(mapVerifyAmountDetail.get("ExpectedAmountDetailAmountPayable")!= null){
				bStatus  = TradeTypeSubscriptionAppFunctions.VerifyLabelHiddenText("Amount Payable",mapVerifyAmountDetail.get("ExpectedAmountDetailAmountPayable"),mapVerifyAmountDetail.get("ExpectedNumberOfDecimals"));
				if(!bStatus){
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
					validateStatus = false;
				}
			}


			//Verify Differences
			if(mapVerifyAmountDetail.get("ExpectedAmountDetailDiffrence")!= null){
				bStatus  = TradeTypeSubscriptionAppFunctions.VerifyLabelHiddenText("Difference",mapVerifyAmountDetail.get("ExpectedAmountDetailDiffrence"),mapVerifyAmountDetail.get("ExpectedNumberOfDecimals"));
				if(!bStatus){
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
					validateStatus = false;
				}
			}

			//Verify Net Amount
			if(mapVerifyAmountDetail.get("ExpectedAmountDetailNetAmount")!= null){
				bStatus  = TradeTypeSubscriptionAppFunctions.VerifyLabelHiddenText("Net Amount",mapVerifyAmountDetail.get("ExpectedAmountDetailNetAmount"),mapVerifyAmountDetail.get("ExpectedNumberOfDecimals"));
				if(!bStatus){
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
					validateStatus = false;
				}
			}

			//Verify Net Units
			if(mapVerifyAmountDetail.get("ExpectedAmountDetailNetUnits")!= null){
				bStatus  = TradeTypeSubscriptionAppFunctions.VerifyLabelHiddenText("Net Units",mapVerifyAmountDetail.get("ExpectedAmountDetailNetUnits"),mapVerifyAmountDetail.get("ExpectedNumberOfShareDecimals"));
				if(!bStatus){
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
					validateStatus = false;
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


	public static boolean doVeifyBankDetailsinChecker(Map<String ,String>mapSubscriptionDetails){
		try {
			boolean bValidStatus = true;
			String appendErrorMsg = "";

			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Bank Detail')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Bank Detail Label is Not Present";
				return false;
			}
			if(mapSubscriptionDetails.get("BankDetailCash")!=null)
			{
				String cashCheckBox = getYesOrNoValueFromCheckBox("//input[@ng-model='bankCashDetails']//parent::span[@class='checked']");
				if(cashCheckBox!=null && !cashCheckBox.equalsIgnoreCase("")){
					if(!mapSubscriptionDetails.get("BankDetailCash").equalsIgnoreCase(cashCheckBox)){
						appendErrorMsg = appendErrorMsg+"[Bank Details Cash checkbox actual "+mapSubscriptionDetails.get("BankDetailCash")+" is Not equal to Expected "+cashCheckBox+" in Checker]";
						bValidStatus = false;					
					}
				}

				if(cashCheckBox!=null && cashCheckBox.equalsIgnoreCase("Yes"))
				{
					if(mapSubscriptionDetails.get("Money Received")!=null)
					{
						String moneyRecievedCheckbox = getYesOrNoValueFromCheckBox("//input[@ng-model='moneyReceivedCheck']//parent::span[@class='checked']");
						if(moneyRecievedCheckbox!=null && !moneyRecievedCheckbox.equalsIgnoreCase("")){
							if(!mapSubscriptionDetails.get("Money Received").equalsIgnoreCase(moneyRecievedCheckbox)){
								appendErrorMsg = appendErrorMsg+"[Bank Details Money Received checkbox actual "+mapSubscriptionDetails.get("Money Received")+" is Not equal to Expected "+moneyRecievedCheckbox+" in Checker]";
								bValidStatus = false;					
							}
						}
						if(moneyRecievedCheckbox!=null && moneyRecievedCheckbox.equalsIgnoreCase("Yes"))
						{
							if(mapSubscriptionDetails.get("Received Amount")!=null && mapSubscriptionDetails.get("MoneyReceivedDate")!=null && mapSubscriptionDetails.get("Bank Name")!=null){

								bStatus = verifyMoneyReceivedDetails(mapSubscriptionDetails.get("Received Amount") , mapSubscriptionDetails.get("MoneyReceivedDate") ,mapSubscriptionDetails.get("Bank Name"),mapSubscriptionDetails.get("ExpectedNumberOfDecimals"));
								if(!bStatus){
									appendErrorMsg =appendErrorMsg+Messages.errorMsg;
									bValidStatus = false;									
								}
							}

						}
					}
					if(mapSubscriptionDetails.get("IsAMLChecked")!=null)
					{
						String isAMLCheckbox = getYesOrNoValueFromCheckBox("//label[contains(text(),'Is AML Checked')]//input//parent::span[@class='checked']");
						if(isAMLCheckbox!=null && !isAMLCheckbox.equalsIgnoreCase(""))
						{
							if(!isAMLCheckbox.equalsIgnoreCase(mapSubscriptionDetails.get("IsAMLChecked"))){
								appendErrorMsg = appendErrorMsg+"[IsAML checkbox Actual is "+isAMLCheckbox+" not Matching with the Expected]";
								bValidStatus = false;								
							}
						}
						if(isAMLCheckbox!=null && isAMLCheckbox.equalsIgnoreCase("Yes"))
						{
							if(mapSubscriptionDetails.get("AMLCheckedDate")!=null){
								String actualDate = NewUICommonFunctions.jsGetElementValueWithXpath("//label[text()='AML Check Date']/following-sibling::div[1]/input");
								actualDate = formatDate(actualDate);
								if(actualDate == null || actualDate.equalsIgnoreCase("") || !actualDate.equalsIgnoreCase(mapSubscriptionDetails.get("AMLCheckedDate"))){
									appendErrorMsg = appendErrorMsg +"AML Checkd Date Actual "+actualDate+" is  not matching with the Expected "+mapSubscriptionDetails.get("AMLCheckedDate")+" ]";
									bValidStatus = false;
								}
							}

						}
					}
				}			
			}

			if(mapSubscriptionDetails.get("IsInkind")!=null)
			{
				String isInkindCheckBox = getYesOrNoValueFromCheckBox("//label[contains(text(),'Is Inkind')]//input//parent::span[@class='checked']");
				if(isInkindCheckBox!=null && !isInkindCheckBox.equalsIgnoreCase("")){
					if(!isInkindCheckBox.equalsIgnoreCase(mapSubscriptionDetails.get("IsInkind"))){
						appendErrorMsg = appendErrorMsg+"IsInkind checkbox Actual value is not matching with the expected";
						bValidStatus = false;						
					}
				}
				if(isInkindCheckBox!=null && isInkindCheckBox.equalsIgnoreCase("Yes"))
				{

					if(mapSubscriptionDetails.get("Inkind Type")!=null && mapSubscriptionDetails.get("Inkind Value")!=null && mapSubscriptionDetails.get("Description")!=null){

						bStatus = verifyIsInkindDetailsInChecker(mapSubscriptionDetails.get("Inkind Type") , mapSubscriptionDetails.get("Inkind Value") , mapSubscriptionDetails.get("Description"),mapSubscriptionDetails.get("ExpectedNumberOfDecimals"));
						if(!bStatus){
							appendErrorMsg = appendErrorMsg+Messages.errorMsg;
							bValidStatus = false;
						}
					}

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


	private static boolean verifyIsInkindDetailsInChecker(String sInkindType,String sInkindValue, String sDescription,String noOfDecimals) {
		try {
			String appendErrorMsg = "";
			boolean bValidStatus = true;
			List<String> aInkindType = Arrays.asList(sInkindType.split(","));
			List<String> aInkindValue = Arrays.asList(sInkindValue.split(","));
			List<String> aDescription = Arrays.asList(sDescription.split(","));

			for(int i=0;i<aInkindType.size();i++){

				if(!aInkindType.get(i).equalsIgnoreCase("None")){
					//String inkindTypelocator = "//span[@ng-show='bankInkindDetails']/div[@class='row']["+(i+1)+"]//label[contains(text(),'Inkind Type')]//following-sibling::select/option[contains(text(),'"+aInkindType.get(i)+"')]";
					//bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(inkindTypelocator), Constants.lTimeOut);
					String inkindTypelocator = "//span[@ng-show='bankInkindDetails']/div[@class='row']["+(i+1)+"]//label[contains(text(),'Inkind Type')]//following-sibling::select/option";
					//String actualInkindType = NewUICommonFunctions.jsGetElementValueWithXpath(inkindTypelocator).trim();
					String actualInkindType = Elements.getText(Global.driver, By.xpath(inkindTypelocator));
					if(!actualInkindType.trim().equalsIgnoreCase(aInkindType.get(i))){
						appendErrorMsg = appendErrorMsg+" [Inkind Type actual value "+actualInkindType+" is Not matching with the Expected value "+aInkindType.get(i)+" ]";
						bValidStatus = false;

					}

				}

				if(!aInkindValue.get(i).equalsIgnoreCase("None")){

					String amountLocator = "//span[@ng-show='bankInkindDetails']/div[@class='row']["+(i+1)+"]//label[contains(text(),'Received Amount')]//following-sibling::input";
					String amountValue = NewUICommonFunctions.jsGetElementValueWithXpath(amountLocator).replaceAll(",", "").trim();
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Inkind Value", Integer.parseInt(noOfDecimals), amountValue);
					if(!bStatus){
						appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
						bValidStatus = false;
					}
					if(Float.parseFloat(amountValue) != Float.parseFloat(aInkindValue.get(i))){
						appendErrorMsg = appendErrorMsg +"[ Actual Inkind Received Amount Value "+amountValue+" is Not equal to the Expected value "+aInkindValue.get(i)+" ]";
						bValidStatus = false;						
					}				

				}

				if(!aDescription.get(i).equalsIgnoreCase("None")){

					String descriptionLocator = "//span[@ng-show='bankInkindDetails']/div[@class='row']["+(i+1)+"]//label[contains(text(),'Description')]//following-sibling::textarea[text()='"+aDescription.get(i)+"']";
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(descriptionLocator), Constants.lTimeOut);
					if(!bStatus){
						appendErrorMsg = appendErrorMsg +"[ Actual Description Value is Not matching with the Expected Description value "+aDescription.get(i)+" ]";
						bValidStatus = false;						
					}

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


	private static boolean verifyMoneyReceivedDetails(String sReceivedAmount,String sMoneyReceivedDate, String sBankName, String noOfDecimals) {
		try {
			boolean bValidStatus = true;
			String appendErrorMsg ="";

			List<String> aReceivedAmount = Arrays.asList(sReceivedAmount.split(","));
			List<String> aMoneyReceivedDate = Arrays.asList(sMoneyReceivedDate.split(","));
			List<String> aBankName = Arrays.asList(sBankName.split(","));

			for(int i=0;i<aReceivedAmount.size();i++){

				if(!aReceivedAmount.get(i).equalsIgnoreCase("None")){
					String valueLocator = "//span[@ng-show='moneyReceivedCheck']//div[@class='row']["+(i+1)+"]//label[contains(text(),'Received Amount')]//following-sibling::input";
					String receivedAmount = NewUICommonFunctions.jsGetElementValueWithXpath(valueLocator).replaceAll(",", "");
					bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Recieved Amount", Integer.parseInt(noOfDecimals), receivedAmount);
					if(!bStatus){
						appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
						bValidStatus = false;
					}
					if(Float.parseFloat(aReceivedAmount.get(i)) != Float.parseFloat(receivedAmount)){
						appendErrorMsg = appendErrorMsg +"[ Received Amount Expected Value "+aReceivedAmount.get(i)+" Not matching with the Actual value "+receivedAmount+" ]";
						bValidStatus = false;

					}
				}

				if(!aMoneyReceivedDate.get(i).equalsIgnoreCase("None")){
					String dateLocator = "//span[@ng-show='moneyReceivedCheck']//div[@class='row']["+(i+1)+"]//label[contains(text(),'Received Date')]//following-sibling::div/input";
					String dateValue = NewUICommonFunctions.jsGetElementValueWithXpath(dateLocator);
					dateValue = formatDate(dateValue);
					//bStatus = formateAndCompareDates(dateValue,aMoneyReceivedDate.get(i));
					if(dateValue == null || dateValue.equalsIgnoreCase("") || !dateValue.equalsIgnoreCase(aMoneyReceivedDate.get(i))){
						appendErrorMsg = appendErrorMsg+"[Money Recevied Date Actual "+dateValue+" is Not matching with the Expected "+aMoneyReceivedDate.get(i)+"]";
						bValidStatus = false;
					}
				}

				if(!aBankName.get(i).equalsIgnoreCase("None")){
					String bankLocator = "//span[@ng-show='moneyReceivedCheck']//div[@class='row']["+(i+1)+"]//label[contains(text(),'Bank Name')]//following-sibling::select/option[contains(text(),'"+aBankName.get(i)+"')]";
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(bankLocator), Constants.lTimeOut);
					if(!bStatus){
						appendErrorMsg = appendErrorMsg+"[ Expected Bank Name "+aBankName.get(i)+" is not matcihng with the Actual Bank Name at "+(i+1)+" index ]";
						bValidStatus = false;

					}
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


	public static String getYesOrNoValueFromCheckBox(String locator){
		try {
			String sValue="";
			bStatus = Verify.verifyElementPresent(Global.driver, By.xpath(locator));
			if(bStatus){
				sValue = "Yes";
			}
			if(!bStatus){
				sValue = "No";
			}

			return sValue;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}


	public static boolean formateAndCompareDates(String sActualDate, String sExpectedDate){
		try {
			// Changes sActualDate like e.g 01-Jan-2020 to 01/01/2020 and returns if the formatted date matches with actual returns true else false.
			Map<String, String> sMonths = new HashMap<String, String>();
			sMonths.put("Jan","01");sMonths.put("Feb","02");sMonths.put("Mar","03");sMonths.put("Apr","04");sMonths.put("May","05");sMonths.put("Jun","06");sMonths.put( "Jul","07");sMonths.put("Aug","08");sMonths.put("Sep","09");sMonths.put("Oct","10");sMonths.put("Nov","11");sMonths.put("Dec","12");
			sActualDate = sActualDate.replaceAll("-", "/");
			List<String> FormatedDateSplitList = Arrays.asList(sActualDate.split("/"));
			String sMonthName = FormatedDateSplitList.get(1).trim();
			String sMonthIndex = sMonths.get(sMonthName);
			sActualDate = sActualDate.replace(sMonthName, sMonthIndex);
			if (!sActualDate.equalsIgnoreCase(sExpectedDate)) {
				Messages.errorMsg = "Actula Date value "+sActualDate+" is not matching with the Expected value "+sExpectedDate;
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static boolean verifyInvestmentDetailsAtMaker(Map<String, String> mapInvestmentDetails){
		boolean bValidatreStatus = true;
		String sAppendErrorMsg = "";
		try {		
			Thread.sleep(3000);
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='effectiveDate']"));
			if(!bStatus){
				Messages.errorMsg = "Page Not Scrolled to Cash or Units Radio Button";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[text()='Class Currency']"));
			if(!bStatus){
				return false;
			}
			if (mapInvestmentDetails != null && !mapInvestmentDetails.isEmpty()) {				
				//For Type Cash
				if (mapInvestmentDetails.get("Investment Type CashorUnits") != null && mapInvestmentDetails.get("Investment Type CashorUnits").equalsIgnoreCase("Cash")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='isAmount']//parent::span[@class='checked']"), 3);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Investment Type Cash is not Checked.] \n";
						bValidatreStatus = false;
					}
					if (mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue") != null) {
						String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='units']"), "value");
						if (sValue == null || sValue.equalsIgnoreCase("")) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual No Of units : "+sValue+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue")+" .] \n";
							bValidatreStatus = false;
						}
						if (sValue != null && !sValue.equalsIgnoreCase("")) {
							sValue = sValue.replaceAll(",", "");
							bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected Number Of Units", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfShareDecimals")), sValue);
							if(!bStatus){
								sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
								bValidatreStatus = false;
							}
							if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue"))) {
								sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual No Of units : "+sValue+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue")+" .] \n";
								bValidatreStatus = false;
							}
						}
					}									
				}

				//For Type Units.
				if (mapInvestmentDetails.get("Investment Type CashorUnits") != null && mapInvestmentDetails.get("Investment Type CashorUnits").equalsIgnoreCase("Units")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='isUnits']//parent::span[@class='checked']"), 3);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Investment Type Units is not Checked.] \n";
						bValidatreStatus = false;
					}
					if (mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue") != null) {
						String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='amount']"), "value");
						if (sValue == null || sValue.equalsIgnoreCase("")) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual amount : "+sValue+" for Type Units is not matching with Expected amount : "+mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue")+" .] \n";
							bValidatreStatus = false;
						}
						if (sValue != null && !sValue.equalsIgnoreCase("")) {
							sValue = sValue.replaceAll(",", "");
							bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected Cash Value", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfDecimals")), sValue);
							if(!bStatus){
								sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
								bValidatreStatus = false;
							}
							if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue"))) {
								sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual amount : "+sValue+" for Type Units is not matching with Expected amount : "+mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue")+" .] \n";
								bValidatreStatus = false;
							}
						}
					}									
				}

				//common fields to verify.
				if (mapInvestmentDetails.get("ExpectedClassCurrency") != null) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.id("currencyName"), 3);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+"[ ERROR : Currency Name is not visible.] \n";
						bValidatreStatus = false;
					}
					if (bStatus) {
						String sCurrencyName = Elements.getText(Global.driver, By.id("currencyName"));
						if (sCurrencyName == null || sCurrencyName.equalsIgnoreCase("") || !sCurrencyName.trim().toLowerCase().equalsIgnoreCase(mapInvestmentDetails.get("ExpectedClassCurrency").toLowerCase())) {
							sAppendErrorMsg = sAppendErrorMsg+"[ ERROR : Expected Currency Name : "+mapInvestmentDetails.get("ExpectedClassCurrency")+", is not matching with expected : "+sCurrencyName+" .] \n";
							bValidatreStatus = false;
						}
					}					
				}

				if (mapInvestmentDetails.get("ExpectedNAVPerUnit") != null) {
					String sValue = Elements.getText(Global.driver, By.xpath("//label[contains(text(),'NAV Per Unit')]//following-sibling::label"));
					//String sValue = Elements.getElementAttribute(Global.driver, By.id("orderDetails0.nav"), "value");
					if (sValue == null || sValue.equalsIgnoreCase("")) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual NAVPerUnit : "+sValue+" for Type Cash is not matching with ExpectedNAVPerUnit : "+mapInvestmentDetails.get("ExpectedNAVPerUnit")+" .] \n";
						bValidatreStatus = false;
					}
					if (sValue != null && !sValue.equalsIgnoreCase("") && !sValue.equalsIgnoreCase("NA")) {
						sValue = sValue.replaceAll(",", "");
						bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected NAV Per Unit ", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfDecimals")), sValue);
						if(!bStatus){
							sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
							bValidatreStatus = false;
						}
						if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("ExpectedNAVPerUnit"))) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual NAVPerUnit : "+sValue+" for Type Cash is not matching with ExpectedNAVPerUnit : "+mapInvestmentDetails.get("ExpectedNAVPerUnit")+" .] \n";
							bValidatreStatus = false;
						}
					}
				}	

				if (mapInvestmentDetails.get("ExpectedNAVDate") != null) {
					String sValue = Elements.getElementAttribute(Global.driver, By.id("navDate"), "value");
					if (sValue == null || sValue.equalsIgnoreCase("")) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual NAVDate : "+sValue+" for Type Cash is not matching with ExpectedNAVDate : "+mapInvestmentDetails.get("ExpectedNAVDate")+" .] \n";
						bValidatreStatus = false;
					}
					if (sValue != null && !sValue.equalsIgnoreCase("")) {
						if (!mapInvestmentDetails.get("ExpectedNAVDate").equalsIgnoreCase(sValue)) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual NAVDate : "+sValue+" for Type Cash is not matching with ExpectedNAVDate : "+mapInvestmentDetails.get("ExpectedNAVDate")+" .] \n";
							bValidatreStatus = false;
						}
					}
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidatreStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean doVerifySubscriptionDetailsInChecker(Map<String ,String> mapSubscriptionDetails){
		try {
			String appendErrorMsg = "";
			boolean bValidStatus = true;

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Detail')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Detail Label is not visible";
				return false;
			}

			bStatus = doVeifyRequestDetailsinChecker(mapSubscriptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;

			}

			bStatus = verifyInvestorDetailsInChecker(mapSubscriptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVeifyFundDetailsinChecker(mapSubscriptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVeifyInvestmentDetailsinChecker(mapSubscriptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVeifyChargeDetailsinChecker(mapSubscriptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVeifyAmountDetailsinChecker(mapSubscriptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			bStatus = doVeifyBankDetailsinChecker(mapSubscriptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			//bStatus = doVeifyFeeDetailsinChecker(mapSubscriptionDetails);
			bStatus = verifyFeeDetails(mapSubscriptionDetails);
			if(!bStatus){
				appendErrorMsg = appendErrorMsg+"[ "+Messages.errorMsg+" ]";
				bValidStatus = false;
			}

			if(mapSubscriptionDetails.get("ExpectedExceptionsAtChecker")!=null){
				bStatus = doVerifyExceptionsAtCheckerForTradeTypeSubscription(mapSubscriptionDetails.get("ExpectedExceptionsAtChecker"));
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


	private static boolean doVeifyFeeDetailsinChecker(Map<String, String> mapSubscriptionDetails) {
		try {
			boolean bValidStaus = true;
			String appenErrorMsg ="";

			if(mapSubscriptionDetails.get("NewManagementFee")!=null && mapSubscriptionDetails.get("ExpectedNewManagementFee")!=null){

				bStatus = doVerifyFeeDetailsAtMakerAndChecker(mapSubscriptionDetails.get("ExpectedNewManagementFee"), "ManagementFee", "Yes",-1);
				if(!bStatus){
					appenErrorMsg = appenErrorMsg+Messages.errorMsg;
					bValidStaus = false;					
				}
			}else
			{
				if(mapSubscriptionDetails.get("ExpectedManagementFee")!=null){
					bStatus = doVerifyFeeDetailsAtMakerAndChecker(mapSubscriptionDetails.get("ExpectedManagementFee"), "ManagementFee", "No",-1);
					if(!bStatus){
						appenErrorMsg = appenErrorMsg+Messages.errorMsg;
						bValidStaus = false;					
					}
				}			
			}

			if(mapSubscriptionDetails.get("NewIncentiveFee")!=null && mapSubscriptionDetails.get("ExectedNewIncentiveFee")!=null){

				bStatus = doVerifyFeeDetailsAtMakerAndChecker(mapSubscriptionDetails.get("ExectedNewIncentiveFee"), "IncentiveFee", "Yes",-1);
				if(!bStatus){
					appenErrorMsg = appenErrorMsg+Messages.errorMsg;
					bValidStaus = false;

				}
			}
			else
			{
				if(mapSubscriptionDetails.get("ExpectedIncentiveFee")!=null){
					bStatus = doVerifyFeeDetailsAtMakerAndChecker(mapSubscriptionDetails.get("ExpectedIncentiveFee") , "IncentiveFee", "No",-1);
					if(!bStatus){
						appenErrorMsg = Messages.errorMsg;
						bValidStaus = false;
					}
				}
			}

			Messages.errorMsg = appenErrorMsg;
			return bValidStaus;


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	private static boolean doVeifyRequestDetailsinChecker(Map<String, String> mapSubscriptionDetails) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if(mapSubscriptionDetails.get("Request Received Date")!=null){
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Received Date']//following-sibling::div/i//following-sibling::input"), 10);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to load the Subscription Checker operations page]";
					return false;
				}
				String sValue=Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Received Date']//following-sibling::div/i//following-sibling::input"), "value");

				sValue = formatDate(sValue);

				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapSubscriptionDetails.get("Request Received Date")))
				{

					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Request Received Date : "+sValue+" , is not matching with Expected : "+mapSubscriptionDetails.get("Request Received Date")+" ]\n";
					bValidateStatus = false;

				}
			}
			if (mapSubscriptionDetails.get("Request Received Time") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Received Time']//following-sibling::div/i//following-sibling::input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapSubscriptionDetails.get("Request Received Time"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Request Received Time : "+sValue+" , is not matching with Expected : "+mapSubscriptionDetails.get("Request Received Time")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapSubscriptionDetails.get("Order Received Office") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Order Received Office']//following-sibling::div//input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapSubscriptionDetails.get("Order Received Office"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Order Received Office : "+sValue+" , is not matching with Expected : "+mapSubscriptionDetails.get("Order Received Office")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapSubscriptionDetails.get("Time Zone") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Time Zone']//following-sibling::div//input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapSubscriptionDetails.get("Time Zone"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Time Zone : "+sValue+" , is not matching with Expected : "+mapSubscriptionDetails.get("Time Zone")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapSubscriptionDetails.get("Source") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Source']//following-sibling::div//input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapSubscriptionDetails.get("Source"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Source : "+sValue+" , is not matching with Expected : "+mapSubscriptionDetails.get("Source")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapSubscriptionDetails.get("Mode of Request") != null) {
				String sValue = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='Mode Of Request']//following-sibling::div//option"));
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapSubscriptionDetails.get("Mode of Request"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual Mode of Request : "+sValue+" , is not matching with Expected : "+mapSubscriptionDetails.get("Mode of Request")+" ]\n";
					bValidateStatus = false;					
				}
			}
			if (mapSubscriptionDetails.get("External ID") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='External ID Number']//following-sibling::div//input"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapSubscriptionDetails.get("External ID"))) {									
					sAppendErrorMsg =  sAppendErrorMsg +"[ ERROR : Actual External ID : "+sValue+" , is not matching with Expected : "+mapSubscriptionDetails.get("External ID")+" ]\n";
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


	private static boolean doVeifyAmountDetailsinChecker(Map<String, String> mapSubscriptionDetails) {
		try {

			String appndErrMsg = "";
			boolean ValidbStatus = true;
			if(mapSubscriptionDetails.get("ExpectedAmountDetailCharges")!=null){
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Charges",mapSubscriptionDetails.get("ExpectedAmountDetailCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Amount Details Charges", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedAmountDetailCharges"))){
					appndErrMsg = appndErrMsg +"[Amount Details Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedAmountDetailCharges")+"]";
					ValidbStatus = false;
				}

			}


			if(mapSubscriptionDetails.get("ExpectedAmountDetailInterest")!=null){
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Interest",mapSubscriptionDetails.get("ExpectedAmountDetailInterest"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Interest']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Amount Detais Interest", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedAmountDetailInterest"))){
					appndErrMsg = appndErrMsg +"[Amount Details Interest Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedAmountDetailInterest")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("ExpectedAmountDetailAmountPayable")!=null){
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Amount Payable",mapSubscriptionDetails.get("ExpectedAmountDetailAmountPayable"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Amount Payable']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Amount Payable", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedAmountDetailAmountPayable"))){
					appndErrMsg = appndErrMsg +"[Amount Details Amount Payable Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedAmountDetailAmountPayable")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("ExpectedAmountDetailDiffrence")!=null){
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Difference']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Difference", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedAmountDetailDiffrence"))){
					appndErrMsg = appndErrMsg +"[Amount Details Difference Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedAmountDetailDiffrence")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("ExpectedAmountDetailNetAmount")!=null){
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Net Amount']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Net Amount", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedAmountDetailNetAmount"))){
					appndErrMsg = appndErrMsg +"[Amount Details Net Amount Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedAmountDetailNetAmount")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("ExpectedAmountDetailNetUnits")!=null){
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Net Units']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Net Units", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfShareDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedAmountDetailNetUnits"))){
					appndErrMsg = appndErrMsg +"[Amount Details Net Units Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedAmountDetailNetUnits")+"]";
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


	private static boolean doVeifyChargeDetailsinChecker(Map<String, String> mapSubscriptionDetails) {
		try {

			String appndErrMsg = "";
			boolean ValidbStatus = true;

			if(mapSubscriptionDetails.get("NoticeChargesRadioButton") != null){
				if(mapSubscriptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("Yes")){
					// Verify Notice Charge Default
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("noticeChargeYes"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Notice Charges]";
						ValidbStatus = false;
					}
				}
				if(mapSubscriptionDetails.get("NoticeChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("noticeChargeNo"));
					if(!bStatus){
						appndErrMsg  = appndErrMsg +"[Expected Value 'No' is not checked for Label: Notice Charges]";
						ValidbStatus  = false;
					}
				}
			}

			if(mapSubscriptionDetails.get("ExpectedNewNoticeCharges")!=null){

				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Notice Charges",mapSubscriptionDetails.get("ExpectedNewNoticeCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Notice Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Notice Charges", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedNewNoticeCharges"))){
					appndErrMsg = appndErrMsg +"[Notice Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedNewNoticeCharges")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("ExpectedNoticeCharges")!=null){

				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Notice Charges",mapSubscriptionDetails.get("ExpectedNoticeCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Notice Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Notice Charges", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedNoticeCharges"))){
					appndErrMsg = appndErrMsg +"[Notice Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedNoticeCharges")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("TransactionChargesRadioButton") != null){
				if(mapSubscriptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("Yes")){
					// Verify Notice Charge Default
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("transactionChargesYes"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'Yes' is not checked for Label: Transaction Charges]";
						ValidbStatus = false;
					}
				}
				if(mapSubscriptionDetails.get("TransactionChargesRadioButton").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("transactionChargesNo"));
					if(!bStatus){
						appndErrMsg = appndErrMsg +"[Expected Value 'No' is not checked for Label: Transaction Charges]";
						ValidbStatus = false;
					}
				}
			}

			if(mapSubscriptionDetails.get("NewExpectedTransactionCharges")!=null){

				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Transaction Charges",mapSubscriptionDetails.get("NewExpectedTransactionCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Transaction Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Transaction Charges", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("NewExpectedTransactionCharges"))){
					appndErrMsg = appndErrMsg +"[Transaction Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("NewExpectedTransactionCharges")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("ExpectedTransactionCharges")!=null){

				//	bStatus = NewUICommonFunctions.verifyTextInTextBox("Transaction Charges",mapSubscriptionDetails.get("NewExpectedTransactionCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Transaction Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Transaction Charges", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedTransactionCharges"))){
					appndErrMsg = appndErrMsg +"[Transaction Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedTransactionCharges")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("Adhoc Charges")!=null){
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Adhoc Charges",mapSubscriptionDetails.get("Adhoc Charges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Adhoc Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Adhoc Charges", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("Adhoc Charges"))){
					appndErrMsg = appndErrMsg +"[Adhoc Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("Adhoc Charges")+"]";
					ValidbStatus = false;
				}
			}
			//ExpectedTotalCharges

			if(mapSubscriptionDetails.get("ExpectedTotalCharges")!=null){
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Total Charges",mapSubscriptionDetails.get("ExpectedTotalCharges"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Total Charges']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Total Charges", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("ExpectedTotalCharges"))){
					appndErrMsg = appndErrMsg +"[Total Charges Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("ExpectedTotalCharges")+"]";
					ValidbStatus = false;
				}
			}


			if(mapSubscriptionDetails.get("Interest")!=null){
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Interest",mapSubscriptionDetails.get("Interest"));
				String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Interest']//following-sibling::div/input"), "value").replaceAll(",", "");
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Interest ", Integer.parseInt(mapSubscriptionDetails.get("ExpectedNumberOfDecimals")), actualValue);
				if(!bStatus){
					appndErrMsg = appndErrMsg+"[ "+Messages.errorMsg+" ]";
					ValidbStatus = false;
				}
				if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapSubscriptionDetails.get("Interest"))){
					appndErrMsg = appndErrMsg +"[Interest Actual Value "+actualValue+" is Not matching with the Expected Value "+mapSubscriptionDetails.get("Interest")+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("InclusiveOrExclusive")!=null){
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='"+mapSubscriptionDetails.get("InclusiveOrExclusive")+"']//input"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
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


	private static boolean doVeifyInvestmentDetailsinChecker(Map<String, String> mapInvestmentDetails) {
		boolean bValidatreStatus = true;
		String sAppendErrorMsg = "";
		try {			
			if (mapInvestmentDetails != null && !mapInvestmentDetails.isEmpty()) {	

				if(mapInvestmentDetails.get("Effective Date")!=null){
					String actualValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[contains(text(),'Effective Date')]//following-sibling::div//input[@id='effectiveDate']"), "value");
					actualValue = formatDate(actualValue);
					if(actualValue==null || actualValue.equalsIgnoreCase("") || !actualValue.equalsIgnoreCase(mapInvestmentDetails.get("Effective Date"))){
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR: Actual Effective Date "+actualValue+" is not matching with the Expected Date "+mapInvestmentDetails.get("Effective Date")+" ]";
						bValidatreStatus = false;

					}
				}

				//For Type Cash
				if (mapInvestmentDetails.get("Investment Type CashorUnits") != null && mapInvestmentDetails.get("Investment Type CashorUnits").equalsIgnoreCase("Cash")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='isAmount']//parent::span[@class='checked']"), 3);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Investment Type Cash is not Checked.] \n";
						bValidatreStatus = false;
					}

					if (mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue") != null) {
						String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='No of Units']//following-sibling::div//input"), "value");
						if (sValue == null || sValue.equalsIgnoreCase("")) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual No Of units : "+sValue+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue")+" .] \n";
							bValidatreStatus = false;
						}
						if (sValue != null && !sValue.equalsIgnoreCase("")) {
							sValue = sValue.replaceAll(",", "");
							bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected Units Value", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfDecimals")), sValue);
							if(!bStatus){
								sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
								bValidatreStatus = false;
							}
							if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue"))) {
								sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Actual No Of units : "+sValue+" for Type Cash is not matching with ExpectedNoOfUnits : "+mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue")+" .] \n";
								bValidatreStatus = false;
							}
						}
					}
					if (mapInvestmentDetails.get("CashOrNoOfUnits") != null) {
						String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Cash']//following-sibling::div//input[@type='text']"), "value");
						if (sValue == null || sValue.equalsIgnoreCase("")) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Actual amount : "+sValue+" for Type Cash is not matching with Expected amount : "+mapInvestmentDetails.get("CashOrNoOfUnits")+" .] \n";
							bValidatreStatus = false;
						}
						if (sValue != null && !sValue.equalsIgnoreCase("")) {
							sValue = sValue.replaceAll(",", "");
							bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected Cash Value", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfDecimals")), sValue);
							if(!bStatus){
								sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
								bValidatreStatus = false;
							}
							if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("CashOrNoOfUnits"))) {
								sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Actual amount : "+sValue+" for Type Cash is not matching with Expected amount : "+mapInvestmentDetails.get("CashOrNoOfUnits")+" .] \n";
								bValidatreStatus = false;
							}
						}
					}
				}

				//For Type Units.
				if (mapInvestmentDetails.get("Investment Type CashorUnits") != null && mapInvestmentDetails.get("Investment Type CashorUnits").equalsIgnoreCase("Units")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='isUnits']//parent::span[@class='checked']"), 3);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Investment Type Units is not Checked.] \n";
						bValidatreStatus = false;
					}

					if (mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue") != null) {
						String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Cash']//following-sibling::div//input[@type='text']"), "value");
						if (sValue == null || sValue.equalsIgnoreCase("")) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Actual amount : "+sValue+" for Type Units is not matching with Expected amount : "+mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue")+" .] \n";
							bValidatreStatus = false;
						}
						if (sValue != null && !sValue.equalsIgnoreCase("")) {
							sValue = sValue.replaceAll(",", "");
							bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected Cash Value", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfDecimals")), sValue);
							if(!bStatus){
								sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
								bValidatreStatus = false;
							}
							if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue"))) {
								sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Actual amount : "+sValue+" for Type Units is not matching with Expected amount : "+mapInvestmentDetails.get("ExpectedNoOfUnitsOrCashValue")+" .] \n";
								bValidatreStatus = false;
							}
						}
					}
					if (mapInvestmentDetails.get("CashOrNoOfUnits") != null) {
						String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='No of Units']//following-sibling::div//input"), "value");
						if (sValue == null || sValue.equalsIgnoreCase("")) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Actual units : "+sValue+" for Type Units is not matching with Expected Units : "+mapInvestmentDetails.get("CashOrNoOfUnits")+" .] \n";
							bValidatreStatus = false;
						}
						if (sValue != null && !sValue.equalsIgnoreCase("")) {
							sValue = sValue.replaceAll(",", "");
							bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected Units Value", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfDecimals")), sValue);
							if(!bStatus){
								sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
								bValidatreStatus = false;
							}
							if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("CashOrNoOfUnits"))) {
								sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Checker verification : Actual units : "+sValue+" for Type Units is not matching with Expected Units : "+mapInvestmentDetails.get("CashOrNoOfUnits")+" .] \n";
								bValidatreStatus = false;
							}
						}
					}
				}

				//common fields to verify.
				if (mapInvestmentDetails.get("ExpectedClassCurrency") != null) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.id("currencyName"), 3);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg+"[ ERROR : Currency Name is not visible.] \n";
						bValidatreStatus = false;
					}
					if (bStatus) {
						String sCurrencyName = Elements.getText(Global.driver, By.id("currencyName"));
						if (sCurrencyName == null || sCurrencyName.equalsIgnoreCase("") || !sCurrencyName.trim().toLowerCase().equalsIgnoreCase(mapInvestmentDetails.get("ExpectedClassCurrency").toLowerCase())) {
							sAppendErrorMsg = sAppendErrorMsg+"[ ERROR : Expected Currency Name : "+mapInvestmentDetails.get("ExpectedClassCurrency")+", is not matching with expected : "+sCurrencyName+" .] \n";
							bValidatreStatus = false;
						}
					}					
				}

				if (mapInvestmentDetails.get("ExpectedNAVPerUnit") != null) {
					String sValue = Elements.getText(Global.driver, By.xpath("//label[normalize-space()='NAV Per Unit']//following-sibling::label"));
					if (sValue == null || sValue.equalsIgnoreCase("")) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual NAVPerUnit : "+sValue+" is Empty not matching with ExpectedNAVPerUnit : "+mapInvestmentDetails.get("ExpectedNAVPerUnit")+" .] \n";
						bValidatreStatus = false;
					}
					if (sValue != null && !sValue.equalsIgnoreCase("") && !sValue.equalsIgnoreCase("NA")) {
						sValue = sValue.replaceAll(",", "");
						bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected NAVPerUnit Value", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfDecimals")), sValue);
						if(!bStatus){
							sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
							bValidatreStatus = false;
						}
						if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("ExpectedNAVPerUnit"))) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual NAVPerUnit : "+sValue+" is not matching with ExpectedNAVPerUnit : "+mapInvestmentDetails.get("ExpectedNAVPerUnit")+" .] \n";
							bValidatreStatus = false;
						}
					}
				}	

				if (mapInvestmentDetails.get("ExpectedNAVDate") != null) {
					String sValue = Global.driver.findElement(By.xpath("//label[normalize-space()='NAV Date']//following-sibling::label")).getText();
					//bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//label[normalize-space()='NAV Date']//following-sibling::label"));
					//bStatus = Global.driver.findElement(By.xpath("//label[normalize-space()='NAV Date']//following-sibling::label")).isDisplayed();
					//String sValue = NewUICommonFunctions.jsGetElementValueWithXpath("//label[normalize-space()='NAV Date']//following-sibling::label");
					//String sValue = Elements.getText(Global.driver, By.id("//label[normalize-space()='NAV Date']//following-sibling::label"));
					sValue = formatDate(sValue);
					if (sValue == null || sValue.equalsIgnoreCase("")) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual NAVDate : "+sValue+" is not matching with ExpectedNAVDate : "+mapInvestmentDetails.get("ExpectedNAVDate")+" .] \n";
						bValidatreStatus = false;
					}
					if (sValue != null && !sValue.equalsIgnoreCase("")) {
						if (!sValue.equalsIgnoreCase(mapInvestmentDetails.get("ExpectedNAVDate"))) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : NAVDate Actual Date "+sValue+" is not Matching with the Expected Date "+mapInvestmentDetails.get("ExpectedNAVDate")+" ]";
							bValidatreStatus = false;
						}
					}
				}

				if (mapInvestmentDetails.get("ExpectedHighWaterMark") != null) {
					String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='High Water Mark']//following-sibling::div//input"), "value");
					if (sValue == null || sValue.equalsIgnoreCase("")) {
						sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual HighWaterMark : "+sValue+" is not matching with Expected HighWaterMark : "+mapInvestmentDetails.get("ExpectedHighWaterMark")+" .] \n";
						bValidatreStatus = false;
					}
					if (sValue != null && !sValue.equalsIgnoreCase("")) {
						sValue = sValue.replaceAll(",", "");
						bStatus = NewUICommonFunctions.verifyDecimalsToDisplay("Expected High Water Mark ", Integer.parseInt(mapInvestmentDetails.get("ExpectedNumberOfDecimals")), sValue);
						if(!bStatus){
							sAppendErrorMsg = sAppendErrorMsg+"[ "+Messages.errorMsg+" ]";
							bValidatreStatus = false;
						}
						if (Float.parseFloat(sValue) != Float.parseFloat(mapInvestmentDetails.get("ExpectedHighWaterMark"))) {
							sAppendErrorMsg = sAppendErrorMsg+"[ERROR : Actual HighWaterMark : "+sValue+" is not matching with Expected HighWaterMark : "+mapInvestmentDetails.get("ExpectedHighWaterMark")+" .] \n";
							bValidatreStatus = false;
						}
					}
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = sAppendErrorMsg;
		return bValidatreStatus;
	}


	private static boolean doVeifyFundDetailsinChecker(Map<String, String> mapSubscriptionDetails) {
		try {

			String appndErrMsg = "";
			boolean ValidbStatus = true;;
			if(mapSubscriptionDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Client Name",mapSubscriptionDetails.get("Client Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus  = false;
				}
			}

			if(mapSubscriptionDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Fund Family Name",mapSubscriptionDetails.get("Fund Family Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Legal Entity Name",mapSubscriptionDetails.get("Legal Entity Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("Class Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Class Name",mapSubscriptionDetails.get("Class Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}


			if(mapSubscriptionDetails.get("Series Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Series Name",mapSubscriptionDetails.get("Series Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
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


	public static boolean verifyInvestorDetailsInChecker(Map<String, String> mapSubscriptionDetails){
		try{		
			String appndErrMsg = "";
			boolean ValidbStatus = true;

			if(mapSubscriptionDetails.get("Investor Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Investor Name",mapSubscriptionDetails.get("Investor Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("Holder Name")!=null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Holder Name", By.xpath("//label[text()='Holder Name' or normalize-space(text())='Holder Name']/..//input"), mapSubscriptionDetails.get("Holder Name"), "No", false);
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Holder Name",mapSubscriptionDetails.get("Holder Name"));
				if(!bStatus){
					appndErrMsg = appndErrMsg +"["+Messages.errorMsg+"]";
					ValidbStatus = false;
				}
			}

			if(mapSubscriptionDetails.get("Account ID")!=null){
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapSubscriptionDetails.get("Account ID"));
				if (mapXMLSubscriptionDetails != null && !mapXMLSubscriptionDetails.isEmpty() && mapXMLSubscriptionDetails.get("AccountID") != null) {
					mapSubscriptionDetails.put("Account ID", mapXMLSubscriptionDetails.get("AccountID"));
				}
				else {
					Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapSubscriptionDetails.get("Account ID"));
					if (mapXMLAccountMasterDetails != null && !mapXMLAccountMasterDetails.isEmpty() && mapXMLAccountMasterDetails.get("AccountID") != null) {
						mapSubscriptionDetails.put("Account ID", mapXMLAccountMasterDetails.get("AccountID"));
					}
				}
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Account ID",mapSubscriptionDetails.get("Account ID"));
				if(!bStatus){
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


	public static boolean performOperationsOnTableWithTransactionID(String TradeType,dashboardMainDropdownToSelect menu , dashboardSubDropdownToSelect subMenu ,String ID, Map<String, String> subscriptionTradingMapDetails){
		try{
			for (int i = 0; i < 8; i++) {
				bStatus = NewUICommonFunctions.searchValueinMastersAndTransactionTableWithTransactionID(menu ,subMenu, ID);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;					
				}
				if(bStatus){
					break;
				}				
			}
			if (!bStatus) {
				Messages.errorMsg = " ERROR : The Expected values : "+ID+" is not visible in the serach filter after 10 iterations.";
				return false;
			}

			String MasterType = "";
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
			case "Side Pocket Subscription":
				MasterType = "Side Pocket Subscription";
				break;
			case "Side Pocket Redemption":
				MasterType = "Side Pocket Redemption";
				break;
			}


			//div[@role='gridcell']//div[normalize-space(text())='+sMasterType']//..//parent::div[@role='row']//a[normalize-space(text())='"+ID+"']
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@role='gridcell']//div[normalize-space(text())='"+MasterType+"']//..//parent::div[@role='row']//a[normalize-space(text())='"+ID+"']"), 5);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Record wasn't identified with Txn ID : "+ID+" ,and Master Type : "+MasterType+" ] ";
				return false;
			}
			bStatus = Elements.click(Global.driver, By.xpath("//div[@role='gridcell']//div[normalize-space(text())='"+MasterType+"']//..//parent::div[@role='row']//a[normalize-space(text())='"+ID+"']"));
			if(!bStatus){
				Messages.errorMsg = "Transaction ID is Not clickable";
				return false;
			}


			/*//Master Type table locator
			String MasterTypeLocator ="//div[contains(@id,'contenttable') and contains(@id,'TransactionToOperate')]/div";
			boolean bvalidStatus = false;
			//Xpathcount of Master Types
			int MasterTypeCount = Elements.getXpathCount(Global.driver, By.xpath(MasterTypeLocator+"/div[6]/div"));
			if(MasterTypeCount == 0){
				Messages.errorMsg = "No Records are availabl with the Transaction ID :"+ID;
				return false;
			}
			for(int i=1;i<=MasterTypeCount;i++)
			{
				String masterTypevalue = Elements.getText(Global.driver, By.xpath(MasterTypeLocator+"["+i+"]/div[6]/div")).trim();
				if(masterTypevalue.equalsIgnoreCase(MasterType))
				{
					bvalidStatus = true;
					bStatus = Elements.click(Global.driver, By.xpath(MasterTypeLocator+"["+i+"]/div[1]//a[text()='"+ID+"']"));
					if(!bStatus){
						Messages.errorMsg = "Transaction ID is Not clickable";
						return false;
					}
					break;

				}
			}
			if(bvalidStatus == false){
				Messages.errorMsg = "No Records are available for the Trade Type :"+TradeType+" with the Transaction ID :"+ID;
			}*/

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//function for Subscription CheckerActionTypes

	public static boolean doCheckerActionTypesOnTrade(Map<String, String> subscriptionTradingMapDetails)
	{

		bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Detail')]"),Constants.lTimeOut);
		if(!bStatus){
			Messages.errorMsg = "Page is not visible for checker operations";
			return false;
		}

		/*if(isChckerReviewedCheckerOperation){
			bStatus = doOpeartionsonMasterCreatedFromTradeCheckerReviewedScreen(subscriptionTradingMapDetails);
			if(!bStatus){
				Messages.errorMsg ="Cannot Perform Checker Operation for Sub Masters. Error: "+Messages.errorMsg;
				return false;
			}
		}*/

		bStatus = doOpeartionsonMasterCreatedFromTrade(subscriptionTradingMapDetails);
		if(!bStatus){
			Messages.errorMsg ="Cannot Perform Checker Operation for Sub Masters. Error: "+Messages.errorMsg;
			return false;
		}


		//download OA and Delete
		if(subscriptionTradingMapDetails.get("isOAClient") != null){
			bStatus = verifyOrderAcknowledgementDownload(subscriptionTradingMapDetails);
			if(!bStatus && subscriptionTradingMapDetails.get("isOAClient").equalsIgnoreCase("Yes")){
				return false;
			}
			if(bStatus && subscriptionTradingMapDetails.get("isOAClient").equalsIgnoreCase("No")){
				Messages.errorMsg = "Order Acknowldegement Downloaded for Non OA Client";
				return false;
			}
		}


		if(subscriptionTradingMapDetails.get("Approve OA")!=null && subscriptionTradingMapDetails.get("Approve OA").equalsIgnoreCase("Yes")){
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

		if(subscriptionTradingMapDetails.get("CheckerOperations").equalsIgnoreCase("Approve"))
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

		if(subscriptionTradingMapDetails.get("CheckerOperations").equalsIgnoreCase("Reject"))
		{
			bStatus = doCheckerActionTypesForTranSaction(checkerActionTypes.REJECT);
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

		if(subscriptionTradingMapDetails.get("CheckerOperations").equalsIgnoreCase("Return"))
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
		if(subscriptionTradingMapDetails.get("CheckerOperations").contains("Review"))
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

	//function to Perform Operations on MasterCreated From Trade

	private static boolean doOpeartionsonMasterCreatedFromTradeCheckerReviewedScreen(Map<String, String> subscriptionTradingMapDetails) {
		try {
			if(subscriptionTradingMapDetails.get("New Bank")!=null && subscriptionTradingMapDetails.get("Bank Name")!=null)
			{
				List<String> aNewBank = Arrays.asList(subscriptionTradingMapDetails.get("New Bank").split(","));
				List<String> aBankName = Arrays.asList(subscriptionTradingMapDetails.get("Bank Name").split(","));
				for(int i=0;i<aNewBank.size();i++){
					if(aNewBank.get(i).equalsIgnoreCase("Yes")){
						bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+aBankName.get(i)+"']//following-sibling::div[contains(@class,'bankApproveButton')]/a"));
						if(!bStatus){
							Messages.errorMsg = aBankName.get(i)+" Approve button cannot be clicked";
							return false;
						}
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'Bank Details Master') and contains(@onclick,'Approve')]"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "Bank Details Approve button is not visible";
							return false;
						}
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'Bank Details Master') and contains(@onclick,'Approve')]"));
						if(!bStatus){
							Messages.errorMsg = "Bank Master Approve button cannot be clicked";
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


	public static boolean doOpeartionsonMasterCreatedFromTrade(Map<String , String> subscriptionTradingMapDetails)
	{

		//Approve for New Investor Created for Trading


		if(subscriptionTradingMapDetails.get("NewInvestorOperation")!=null)
		{

			bStatus = Elements.click(Global.driver, By.id("approveInvestor"));
			if (!bStatus) 
			{
				Messages.errorMsg = "unable to click on Approve button for Investor";
				return false;
			}
			//call function to approve or return or reject
			if (subscriptionTradingMapDetails.get("NewInvestorOperation")!=null && subscriptionTradingMapDetails.get("NewInvestorOperation").equalsIgnoreCase("Approve")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.APPROVE);
				if (!bStatus) 
				{
					return false;
				}
			}

			if (subscriptionTradingMapDetails.get("NewInvestorOperation")!=null && subscriptionTradingMapDetails.get("NewInvestorOperation").equalsIgnoreCase("Return")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.RETURN);
				if (!bStatus) 
				{
					return false;
				}
			}
			if (subscriptionTradingMapDetails.get("NewInvestorOperation")!=null && subscriptionTradingMapDetails.get("NewInvestorOperation").equalsIgnoreCase("Cancel")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.CANCEL);
				if (!bStatus) 
				{
					return false;
				}

			}

			if (subscriptionTradingMapDetails.get("NewInvestorOperation")!=null && subscriptionTradingMapDetails.get("NewInvestorOperation").equalsIgnoreCase("Reject")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.REJECT);
				if (!bStatus) 
				{
					return false;
				}

			}
		}

		//Approve for New Holder Created for Trading

		if (subscriptionTradingMapDetails.get("NewHolderOperation") != null) {
			bStatus = Elements.click(Global.driver, By.id("approveHolder"));
			if (!bStatus) {
				Messages.errorMsg = "unable to click on Approve button for Holder";
				return false;
			}


			//call function to approve or return or reject
			if (subscriptionTradingMapDetails.get("NewHolderOperation") != null && subscriptionTradingMapDetails.get("NewHolderOperation").equalsIgnoreCase("Approve")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.APPROVE);
				if (!bStatus) {
					return false;
				}

			}

			if (subscriptionTradingMapDetails.get("NewHolderOperation") != null && subscriptionTradingMapDetails.get("NewHolderOperation").equalsIgnoreCase("Return")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.RETURN);
				if (!bStatus) {
					return false;
				}
			}

			if (subscriptionTradingMapDetails.get("NewHolderOperation") != null && subscriptionTradingMapDetails.get("NewHolderOperation").equalsIgnoreCase("Reject")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.REJECT);
				if (!bStatus) {
					return false;
				}
			}

			if (subscriptionTradingMapDetails.get("NewHolderOperation") != null && subscriptionTradingMapDetails.get("NewHolderOperation").equalsIgnoreCase("Cancel")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.CANCEL);
				if (!bStatus) {
					return false;
				}
			}

		}



		// Approve for New Series Created for Trading
		if (subscriptionTradingMapDetails.get("NewSeriesOperation") != null) 
		{
			bStatus = Elements.click(Global.driver, By.id("approveSeries"));
			if (!bStatus) {
				Messages.errorMsg = "unable to click on Approve button for Series";
				return false;
			}
			//call function to approve or return or reject
			if (subscriptionTradingMapDetails.get("NewSeriesOperation") != null && subscriptionTradingMapDetails.get("NewSeriesOperation").equalsIgnoreCase("Approve")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.APPROVE);
				if (!bStatus) 
				{
					return false;
				}
			}

			if (subscriptionTradingMapDetails.get("NewSeriesOperation") != null && subscriptionTradingMapDetails.get("NewSeriesOperation").equalsIgnoreCase("Return")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.RETURN);
				if (!bStatus) 
				{
					return false;
				}
			}

			if (subscriptionTradingMapDetails.get("NewSeriesOperation") != null && subscriptionTradingMapDetails.get("NewSeriesOperation").equalsIgnoreCase("Reject")) 
			{
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.REJECT);
				if (!bStatus) 
				{
					return false;
				}
			}
			if (subscriptionTradingMapDetails.get("NewSeriesOperation") != null && subscriptionTradingMapDetails.get("NewSeriesOperation").equalsIgnoreCase("Cancel")) {
				bStatus=doCheckerActionTypesOnMasters(checkerActionTypes.CANCEL);
				if (!bStatus) {
					return false;
				}
			}


		}

		if(subscriptionTradingMapDetails.get("BankCheckerOperation")!=null && subscriptionTradingMapDetails.get("Bank Name")!=null)
		{
			List<String> aNewBank = Arrays.asList(subscriptionTradingMapDetails.get("BankCheckerOperation").split(","));
			List<String> aBankName = Arrays.asList(subscriptionTradingMapDetails.get("Bank Name").split(","));
			for(int i=0;i<aNewBank.size();i++){
				if(aNewBank.get(i).equalsIgnoreCase("Approve")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+aBankName.get(i)+"']//following-sibling::div[contains(@class,'bankApproveButton')]/a"));
					if(!bStatus){
						Messages.errorMsg = aBankName.get(i)+" Approve button cannot be clicked";
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'Bank Details Master') and contains(@onclick,'Approve')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Bank Details Approve button is not visible";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'Bank Details Master') and contains(@onclick,'Approve')]"));
					if(!bStatus){
						Messages.errorMsg = "Bank Master Approve button cannot be clicked";
						return false;
					}
				}
			}

		}

		return true;
	}

	//function to Click Approve, Reject, Return, Cancel on Masters Approval window in Subscription

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


	public static boolean doFillBankMaster(Map<String, String> mapBankDetails){
		try {

			if (mapBankDetails != null && !mapBankDetails.isEmpty()) {
				if (mapBankDetails.get("Account Type") != null) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[@for='isInvestorFund']"), 10);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Bank Master Details Page is not visible to add new.]\n";
						return false;
					}
					else {
						if (mapBankDetails.get("Account Type").equalsIgnoreCase("Investor")) {
							bStatus = NewUICommonFunctions.jsClick(By.id("rdIsInvestor"));
							if (!bStatus) {						
								Messages.errorMsg = "[ ERROR : Unable to Select the Radio button for Account Type : "+mapBankDetails.get("Account Type")+" .]\n";
								return false;
							}
							if (mapBankDetails.get("Investor Name") != null) {
								String sValue = "";
								if (!NewUICommonFunctions.isBankMasterBeingCreatedFromTrade) {
									bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapBankDetails.get("Investor Name"), By.xpath("//div[@id='s2id_fromInvestorId']//span[contains(@id,'select2-chosen')]"));
									if (!bStatus) {
										return false;
									}
								}

								if (NewUICommonFunctions.isBankMasterBeingCreatedFromTrade) {
									sValue = Elements.getElementAttribute(Global.driver, By.id("investorCompleteName"), "value");
									if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapBankDetails.get("Investor Name"))) {
										Messages.errorMsg = "[ ERROR : Actual Investor Name : "+sValue+" ,is NOT matching with expected : "+mapBankDetails.get("Investor Name")+" ]\n";
										return false;
									}
								}					
							}

							if (mapBankDetails.get("Investor Holder") != null) {
								String sValue = "";
								if (!NewUICommonFunctions.isBankMasterBeingCreatedFromTrade) {
									bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapBankDetails.get("Investor Holder"), By.xpath("//div[@id='s2id_holderId']//span[contains(@id,'select2-chosen')]"));
									if (!bStatus) {
										return false;
									}
								}

								if (NewUICommonFunctions.isBankMasterBeingCreatedFromTrade) {
									sValue = Elements.getElementAttribute(Global.driver, By.id("holderCompleteName"), "value");
									if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.trim().equalsIgnoreCase(mapBankDetails.get("Investor Holder"))) {
										Messages.errorMsg = "[ ERROR : Actual Investor Holder Name : "+sValue+" ,is NOT matching with expected : "+mapBankDetails.get("Investor Holder")+" ]\n";
										return false;
									}
								}					
							}
						}
						else if (mapBankDetails.get("Account Type").equalsIgnoreCase("Fund") && NewUICommonFunctions.isBankMasterBeingCreatedFromTrade != true) {
							bStatus = NewUICommonFunctions.jsClick(By.id("rdIsFund"));
							if (!bStatus) {						
								Messages.errorMsg = "[ ERROR : Unable to Select the Radio button for Account Type : "+mapBankDetails.get("Account Type")+" .]\n";
								return false;
							}

							if (mapBankDetails.get("Client Name") != null) {
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapBankDetails.get("Client Name"), By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"));
								return false;
							}

							if (mapBankDetails.get("Fund Family Name") != null) {
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapBankDetails.get("Fund Family Name"), By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"));
								return false;
							}

							if (mapBankDetails.get("Client Name") != null) {
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapBankDetails.get("Legal Entity Name"), By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"));
								return false;
							}
						}						
					}
				}

				if (mapBankDetails.get("Account Currency") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapBankDetails.get("Account Currency"), By.xpath("//div[@id='s2id_cmbBaseCurrency']//span[contains(@id,'select2-chosen')]"));
					if (!bStatus) {
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Bank Name") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryBankName"), mapBankDetails.get("Beneficiary Bank Name"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Bank Name' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Bank Country") != null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapBankDetails.get("Beneficiary Bank Country"), By.xpath("//div[@id='s2id_beneficiaryFkCountryIdPK.countryIdPk']//span[contains(@id,'select2-chosen')]"));
					if (!bStatus) {
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Branch Name") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryBranchName"), mapBankDetails.get("Beneficiary Branch Name"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Branch Name' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Bank Address") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryBankAddress1"), mapBankDetails.get("Beneficiary Bank Address"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Bank Address' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Bank ZipCode") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryBankZipCode"), mapBankDetails.get("Beneficiary Bank ZipCode"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Bank ZipCode' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Bank State") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryBankState"), mapBankDetails.get("Beneficiary Bank State"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Bank State' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Swift Code") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiarySwiftCode"), mapBankDetails.get("Beneficiary Swift Code"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Swift Code' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Bank ABA/CHIPS Code") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryChipsCode"), mapBankDetails.get("Beneficiary Bank ABA/CHIPS Code"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Bank ABA/CHIPS Code' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Any Other Beneficiary Bank Code") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryOtherBankCode"), mapBankDetails.get("Any Other Beneficiary Bank Code"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Any Other Beneficiary Bank Code' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Intermediary Bank") != null) {
					if (mapBankDetails.get("Intermediary Bank").equalsIgnoreCase("Yes") || mapBankDetails.get("Intermediary Bank").equalsIgnoreCase("No")) {					
						if (mapBankDetails.get("Intermediary Bank").equalsIgnoreCase("Yes")) {
							bStatus = NewUICommonFunctions.jsClick(By.xpath("//input[@id='intermediaryBankYes']"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to select the radio button for 'Intermediary Bank' option 'YES' ]\n";
								return false;
							}

							if (mapBankDetails.get("Intermediary Bank Name") != null) {
								bStatus = Elements.enterText(Global.driver, By.id("intermediaryBankName"), mapBankDetails.get("Intermediary Bank Name"));
								if (!bStatus) {
									Messages.appErrorMsg = "[ ERROR : Unable to input the 'Intermediary Bank Name' into the field.]\n";
									return false;
								}
							}

							if (mapBankDetails.get("Intermediary Branch Name") != null) {
								bStatus = Elements.enterText(Global.driver, By.id("intermediaryBranchName"), mapBankDetails.get("Intermediary Branch Name"));
								if (!bStatus) {
									Messages.appErrorMsg = "[ ERROR : Unable to input the 'Intermediary Branch Name' into the field.]\n";
									return false;
								}
							}

							if (mapBankDetails.get("Intermediary Branch Address") != null) {
								bStatus = Elements.enterText(Global.driver, By.id("intermediaryBankAddress1"), mapBankDetails.get("Intermediary Branch Address"));
								if (!bStatus) {
									Messages.appErrorMsg = "[ ERROR : Unable to input the 'Intermediary Branch Address' into the field.]\n";
									return false;
								}
							}

							if (mapBankDetails.get("Intermediary Bank ZipCode") != null) {
								bStatus = Elements.enterText(Global.driver, By.id("intermediaryBankZipCode"), mapBankDetails.get("Intermediary Bank ZipCode"));
								if (!bStatus) {
									Messages.appErrorMsg = "[ ERROR : Unable to input the 'Intermediary Bank ZipCode' into the field.]\n";
									return false;
								}
							}

							if (mapBankDetails.get("Intermediary Bank State") != null) {
								bStatus = Elements.enterText(Global.driver, By.id("intermediaryBankState"), mapBankDetails.get("Intermediary Bank State"));
								if (!bStatus) {
									Messages.appErrorMsg = "[ ERROR : Unable to input the 'Intermediary Bank State' into the field.]\n";
									return false;
								}
							}

							if (mapBankDetails.get("Intermediary Bank Country") != null) {
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapBankDetails.get("Intermediary Bank Country"), By.xpath("//div[@id='s2id_intermediaryFkCountryIdPk.countryIdPk']//span[contains(@id,'select2-chosen')]"));
								if (!bStatus) {
									return false;
								}
							}

							if (mapBankDetails.get("Intermediary Bank Swift Code") != null) {
								bStatus = Elements.enterText(Global.driver, By.id("intermediarySwiftCode"), mapBankDetails.get("Intermediary Bank Swift Code"));
								if (!bStatus) {
									Messages.appErrorMsg = "[ ERROR : Unable to input the 'Intermediary Bank Swift Code' into the field.]\n";
									return false;
								}
							}

							if (mapBankDetails.get("Intermediary Bank ABA/CHIPS Code") != null) {
								bStatus = Elements.enterText(Global.driver, By.id("intermediaryAbaCode"), mapBankDetails.get("Intermediary Bank ABA/CHIPS Code"));
								if (!bStatus) {
									Messages.appErrorMsg = "[ ERROR : Unable to input the 'Intermediary Bank ABA/CHIPS Code' into the field.]\n";
									return false;
								}
							}

							if (mapBankDetails.get("Any Other Intermediary Bank Code") != null) {
								bStatus = Elements.enterText(Global.driver, By.id("intermediaryOtherBankCode"), mapBankDetails.get("Any Other Intermediary Bank Code"));
								if (!bStatus) {
									Messages.appErrorMsg = "[ ERROR : Unable to input the 'Any Other Intermediary Bank Code' into the field.]\n";
									return false;
								}
							}
						}

						if (mapBankDetails.get("Intermediary Bank").equalsIgnoreCase("No")) {
							bStatus = NewUICommonFunctions.jsClick(By.xpath("//input[@id='intermediaryBankNo']"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to select the radio button for 'Intermediary Bank' option 'NO' ]\n";
								return false;
							}
						}
					}
				}

				if (mapBankDetails.get("Beneficiary Account Name") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryAccountName"), mapBankDetails.get("Beneficiary Account Name"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Account Name' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Beneficiary Account Number") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("beneficiaryAccountNo"), mapBankDetails.get("Beneficiary Account Number"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Beneficiary Account Number' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("F/F/C Account Name") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("ffcAccountName"), mapBankDetails.get("F/F/C Account Name"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'F/F/C Account Name' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("F/F/C Account Number") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("ffcAccountNo"), mapBankDetails.get("F/F/C Account Number"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'F/F/C Account Number' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Reference Id") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("referenceId"), mapBankDetails.get("Reference Id"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Reference Id' into the field.]\n";
						return false;
					}
				}

				if (mapBankDetails.get("Comments") != null) {
					bStatus = Elements.enterText(Global.driver, By.id("comments"), mapBankDetails.get("Comments"));
					if (!bStatus) {
						Messages.appErrorMsg = "[ ERROR : Unable to input the 'Reference Id' into the field.]\n";
						return false;
					}
				}

			}
			if(NewUICommonFunctions.isBankMasterBeingCreatedFromTrade && mapBankDetails.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Bank Master", mapBankDetails.get("OperationType"));

			}
			return bStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}


	public static boolean selectOptionByVisibleText(WebDriver wDriver,String objLocator, String sValue) {
		try {
			By dropDown = By.xpath(objLocator);
			bStatus = Verify.verifyElementPresent(wDriver, dropDown);
			if (!bStatus)
				return false;
			Select dropdown = new Select(wDriver.findElement(dropDown));
			dropdown.selectByVisibleText(sValue);
			return true;	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}


	public static String getBankNameForCheckerVerify(Map<String , String> mapSubscriptionDetails){
		try {
			List<String> aTestCaseName = Arrays.asList(mapSubscriptionDetails.get("New Bank TestCaseName").split(","));
			List<String> aBankName = Arrays.asList(mapSubscriptionDetails.get("Bank Name").split(","));
			String bankName = "";
			for(int i=0;i<aTestCaseName.size();i++){				
				if(!aTestCaseName.get(i).equalsIgnoreCase("None"))
				{
					Map<String,  String> mapBankDetails = ExcelUtils.readDataABasedOnCellName(Global.sSubscriptionTestData,"BankDetailsTestData",aTestCaseName.get(i));
					String newBankNamae = "";
					if(mapBankDetails.get("Beneficiary Bank Name")!=null && mapBankDetails.get("Beneficiary Account Number")!=null){
						newBankNamae = mapBankDetails.get("Beneficiary Bank Name");
						newBankNamae = newBankNamae+" - ";
						newBankNamae = newBankNamae+mapBankDetails.get("Beneficiary Account Number");
						bankName = bankName+","+newBankNamae;
					}
				}else
				{
					bankName = bankName+","+aBankName.get(i);
				}				

			}
			bankName = bankName.replaceFirst("^,","");
			bankName = bankName.replace(",,",",");
			bankName = bankName.replaceAll(",$", "");

			return bankName;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
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

	public static boolean verifyOrderAcknowledgementDownload(Map<String , String> subscriptionTradingMapDetails) 
	{
		try {
			bStatus = Elements.click(Global.driver, By.xpath("//div[contains(@class, 'input-group')]//a[contains(@onclick,'viewDocuments')]//parent::a"));
			if (!bStatus) {
				Messages.errorMsg = "Unable to click View Oder Acknowledgement.";
				return false ;
			}

			TimeUnit.SECONDS.sleep(5);
			File folder = new File(System.getProperty("user.home")+"/Downloads");
			File[] fileList = folder.listFiles();
			String investorName = subscriptionTradingMapDetails.get("Investor Name").replaceAll(" ", "_").replaceAll("\\W", "");
			for (int index = 0; index < fileList.length; index++) {
				if (fileList[index].getName().contains(investorName)) {
					fileList[index].delete();
					return true;
				}
			}
			Messages.errorMsg = "Unable to download the file";
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messages.errorMsg = Messages.errorMsg + e.getMessage();
			return false;
		}

	}

	//Function to Click Approve, Reject, Return, Cancel on Subscription Window

	public static boolean doCheckerActionTypesForTranSaction(checkerActionTypes ActionType)
	{
		try{

			String locator=null;
			switch (ActionType) {
			case APPROVE:
				locator = "//button[contains(@onclick, 'javascript:submitCheckerRequest')and contains(@onclick, 'Done')]";
				break;
			case RETURN:
				locator = "//button[contains(@onclick, 'javascript:submitCheckerRequest')and contains(@onclick, 'Return')]";
				break;
			case REJECT:
				locator = "//button[contains(@onclick, 'javascript:submitCheckerRequest')and contains(@onclick, 'Reject')]";
				break;
			case CANCEL:
				locator = "//button[contains(@onclick, 'javascript:cancelDashboard')]";
				break;
			case CheckerReview:
				locator = "//button[contains(@onclick, 'javascript:submit')and contains(@onclick, 'CRReviewed')]";
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


	public static boolean verifyExceptionsatMaker(String exceptionList){
		try {
			String sAppendErrMsg = "";
			boolean bValidStatus = true;
			String sClubbegExeptionsString = "";
			List<String> sActualExceptions = new ArrayList<String>();
			List<String> aException = Arrays.asList(exceptionList.split(":"));
			int actualExceptionCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@id='exceptionBlock']//div[@class='alert alert-danger']"));
			if(aException.size()!=actualExceptionCount){
				Messages.errorMsg = "Actual Exception count is "+actualExceptionCount+"  not Matching with the Expected Exceptions count "+aException.size()+" Expected Exceptions are  :"+exceptionList;
				return false;
				//			return bValidStatus;
			}
			for(int j =0;j<actualExceptionCount;j++){
				String Actualexception = Elements.getText(Global.driver, By.xpath("//div[@id='exceptionBlock']//div[@class='alert alert-danger']["+(j+1)+"]")).trim();
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

	public static boolean doSubOperationsOnTransactionTrades(String master , String operation){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//button[contains(@onclick,'javascript:submit') and contains(@onclick,'SAVE')]") , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Save Button is Not Visible";
				return false;
			}
			if(operation.equalsIgnoreCase("Save")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submit') and contains(@onclick,'SAVE')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Save button cannot be clicked";
					return false;
				}

			}

			if(operation.contains("Review")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submit') and contains(@onclick,'REVIEW')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Send For Review button cannot be clicked";
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

			if(operation.equalsIgnoreCase("Cancel")){
				if(!isInkindFromCheckerReviewedScreen){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-times-circle']//parent::a[contains(normalize-space(),'Cancel')]"));
					if(!bStatus){
						Messages.errorMsg = master+" Cancel button cannot be clicked";
						return false;
					}
				}else{
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-times-circle']//parent::button[contains(normalize-space(),'Cancel')]"));
					if(!bStatus){
						Messages.errorMsg = "Cancel Button Cannot be clicked";
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

	public static boolean changeOverrideStatus(Map<String , String> mapSubscriptionDetails){
		try {

			if(mapSubscriptionDetails.get("NewTransactionCharges") != null && mapSubscriptionDetails.get("NoticePeriodOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription("Notice Period", mapSubscriptionDetails.get("NoticePeriodOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}
			if(mapSubscriptionDetails.get("NewAmountForNoticePeriod") != null && mapSubscriptionDetails.get("TransactionOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription("Transaction", mapSubscriptionDetails.get("TransactionOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}

			if(mapSubscriptionDetails.get("NewIncentiveFee") != null && mapSubscriptionDetails.get("IncentiveFeeOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription("Incentive Fee", mapSubscriptionDetails.get("IncentiveFeeOverrideAtChecker"));
				if(!bStatus){
					return false;
				}
			}

			if(mapSubscriptionDetails.get("NewManagementFee") != null && mapSubscriptionDetails.get("ManagementFeeOverrideAtChecker")!=null){
				bStatus = doVerifyOrChangeOverridedChargesTypeForTradeTypeSubscription("Management Fee", mapSubscriptionDetails.get("ManagementFeeOverrideAtChecker"));
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

	public static boolean VerifyLabelHiddenText(String LabelName , String Expected , String noOfDecimals){

		try{
			String appendMsg = "";
			boolean bvalidStatus = true;
			String ActualTest= NewUICommonFunctions.jsGetElementValueWithXpath("//label[text()='"+LabelName+"']/following-sibling::div[1]/input");
			if(ActualTest == null || ActualTest.equalsIgnoreCase("")){
				Messages.errorMsg = "Actual value of "+LabelName+"  Contains Null ";
				return false;
			}
			if (ActualTest != null && !ActualTest.equalsIgnoreCase("")){
				bStatus = NewUICommonFunctions.verifyDecimalsToDisplay(LabelName, Integer.parseInt(noOfDecimals), ActualTest);
				if(!bStatus){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]";
					bvalidStatus = false;
				}
				if(Float.parseFloat(ActualTest) != Float.parseFloat(Expected)){
					appendMsg = appendMsg+"[ "+ActualTest +" is actual value for Label: "+LabelName+" which is not matching with Expected value: "+Expected+" ]";
					bvalidStatus = false;
				}
			}

			Messages.errorMsg = appendMsg;
			return bvalidStatus;
		}

		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}



	public static boolean goToCurrentUrl(){
		try {

			String currentUrl = Global.driver.getCurrentUrl();
			Global.driver.get(currentUrl);

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;

		}
	}


}
