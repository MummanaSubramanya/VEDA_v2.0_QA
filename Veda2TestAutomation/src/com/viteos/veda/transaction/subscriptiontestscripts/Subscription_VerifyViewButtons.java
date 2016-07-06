package com.viteos.veda.transaction.subscriptiontestscripts;

import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Constants;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;

public class Subscription_VerifyViewButtons {

	static boolean bStatus;
	static String sSheetName = "verifyViewButtonsData";
	//public static String sSubscriptionTestData = "testdataold//SubscriptionViewButtonsData.xls";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Subscription";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}


	@org.testng.annotations.Test
	public static void test(){

		Map<String, Map<String, String>> mapAllSubscriptionDetails = Utilities.readMultipleTestData(Global.sSubscriptionTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllSubscriptionDetails.size();index++){
			Map<String, String> mapSubscriptionDetails = mapAllSubscriptionDetails.get("Row"+index);

			Reporting.Testcasename = mapSubscriptionDetails.get("TestCaseName");

			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Subscription");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Subscription Master", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Subscription Master", "Subscription Menu selected succesfully");
			
			verifyViewButtonsFunctionality(mapSubscriptionDetails);

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
	
	public static void verifyViewButtonsFunctionality(Map<String, String> mapSubscriptionDetails){
		try{
			
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Reporting.logResults("Fail","Wait for Subscription Page","Subscription Page is not visible");
				return;
			}else{
				Reporting.logResults("Pass","Wait for Subscription Page","Subacription Page is displayed");
			}
			
			
			bStatus = TradeTypeSubscriptionAppFunctions.doFillRequestDetails(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Request Details","Request Details cannot be filled. Error: "+Messages.errorMsg);
				return;
			}else{
				Reporting.logResults("Pass","Fill Request Details","Request details filled succesfully");
			}
								
			bStatus = TradeTypeSubscriptionAppFunctions.doFillInvestorDetails(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Investor Details","Investor Details cannot be filled. Error: "+Messages.errorMsg);
				return;
				//return false;
			}else{
				Reporting.logResults("Pass","Fill Investor Details","Investor details filled succesfully");
			}
						
			bStatus = TradeTypeSubscriptionAppFunctions.doFillFundDetails(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Fund Details","Fund Details cannot be filled. Error: "+Messages.errorMsg);
				return;
			}else{
				Reporting.logResults("Pass","Fill Fund Details","Fund details filled succesfully");
			}
					
			bStatus = TradeTypeSubscriptionAppFunctions.doFillInvestmentDetails(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Fill Investment Details","Investment details filled. Error: "+Messages.errorMsg);
				return;
			}else{
				Reporting.logResults("Pass","Fill Investment Details","Investment details filled successfully");
			}
			
			bStatus = TradeTypeSubscriptionAppFunctions.verifyInvestmentDetailsAtMaker(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Investment Details","Investment details Verified. Error: "+Messages.errorMsg);
				return;
			}else{
				Reporting.logResults("Pass","Verify Investment Details","Investment details Verified successfully");
			}
			
			
			bStatus = verifyViewButtonForNoticePeriods(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Notice period details","Verification Failed. Error: "+Messages.errorMsg);
				if(Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewNoticeCharges')]/em"), Constants.lTimeOut)){
					Elements.click(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewNoticeCharges')]/em"));
				}			
			}
			if(mapSubscriptionDetails.get("NPView")!= null && bStatus){
				if(Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewNoticeCharges')]/em"), Constants.lTimeOut)){
					Elements.click(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewNoticeCharges')]/em"));
				}
				Reporting.logResults("Pass","Verify Notice period details","Notice period Charges verified successfully");
			}
						
			bStatus = verifyViewButtonForTransactionCharge(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Transaction period details","Verification Failed. Error: "+Messages.errorMsg);
				if(Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewTransactionCharges')]/em"), Constants.lTimeOut)){
					Elements.click(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewTransactionCharges')]/em"));
				}
			}
			if(bStatus && mapSubscriptionDetails.get("TCEffDate")!=null){
				if(Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewTransactionCharges')]/em"), Constants.lTimeOut)){
					Elements.click(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewTransactionCharges')]/em"));
				}
				Reporting.logResults("Pass","Verify Transaction period details","Transaction period Charges verified successfully");
			}
			
			
			bStatus = verifyViewButtonForManagementFee(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Management Fee details","Verification Failed. Error: "+Messages.errorMsg);
				if(Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewManagementFee')]/em"), Constants.lTimeOut)){
					Elements.click(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewManagementFee')]/em"));
				}				
			}
			if(bStatus && mapSubscriptionDetails.get("MFCalculationType")!=null){
				if(Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewManagementFee')]/em"), Constants.lTimeOut)){
					Elements.click(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewManagementFee')]/em"));
				}
				Reporting.logResults("Pass","Verify Management Fee Details","Management Fee Details verified successfully");
			}
					
			bStatus = verifyViewButtonForIncentiveFee(mapSubscriptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Incentive Fee details","Verification Failed. Error: "+Messages.errorMsg);
				if(Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewManagementFee')]/em"), Constants.lTimeOut)){
					Elements.click(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewManagementFee')]/em"));
				}					
				return;
			}
			if(bStatus && mapSubscriptionDetails.get("IFCalculationType")!=null){
				if(Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewManagementFee')]/em"), Constants.lTimeOut)){
					Elements.click(Global.driver, By.xpath("//button[contains(@ng-click,'closeNewManagementFee')]/em"));
				}
				Reporting.logResults("Pass","Verify Incentive Fee details","Incentive period Charges verified successfully");
			}
			return;
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
	
	
	public static boolean verifyViewButtonForNoticePeriods(Map<String, String> ViewMapDetails){
		try{
			
			if(ViewMapDetails.get("NPView")== null){
				return true;
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
			
			String sValue = Elements.getText(Global.driver, By.xpath("//div[@ng-show='showNoticePeriodDefaultBlock']//p[@id='noticePeriodView']"));
			if(sValue!=null && !sValue.equalsIgnoreCase(ViewMapDetails.get("NPView"))){
				Messages.errorMsg = "Atual value Notice period value is "+sValue+" which is not equal to expected value: "+ViewMapDetails.get("NPView");
				return false;
			}
			
			String sType = Elements.getText(Global.driver, By.xpath("//div[@ng-show='showNoticePeriodDefaultBlock']//p[@id='noticePeriodTypeView']"));
			if(sType!=null && !sType.equalsIgnoreCase(ViewMapDetails.get("NPType"))){
				Messages.errorMsg = "Atual value Notice period Type is "+sType+" which is not equal to expected value: "+ViewMapDetails.get("NPType");
				return false;
			}
			
			String sCalendar = Elements.getText(Global.driver, By.xpath("//div[@ng-show='showNoticePeriodDefaultBlock']//p[@id='noticePeriodCalendarView']"));
			if(sCalendar!=null && !sCalendar.equalsIgnoreCase(ViewMapDetails.get("NPCalendarView"))){
				Messages.errorMsg = "Atual value Notice period Calendar type is "+sCalendar+" which is not equal to expected value: "+ViewMapDetails.get("NPCalendarView");
				return false;
			}
			
			String ChargesType = Elements.getText(Global.driver, By.xpath("//div[@ng-show='showNoticePeriodTypeDefaultBlock']//p[@id='noticePeriodChargesTypeView']"));
			if(ChargesType!=null && !ChargesType.equalsIgnoreCase(ViewMapDetails.get("NPChargesType"))){
				Messages.errorMsg = "Atual value Notice period Charges Type is "+ChargesType+" which is not equal to expected value: "+ViewMapDetails.get("NPChargesType");
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static boolean verifyViewButtonForTransactionCharge(Map<String, String> ViewMapDetails){
		try{
			
			if(ViewMapDetails.get("TCEffDate")==null){
				return true;
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
			
			String sEffDate = Elements.getText(Global.driver, By.xpath("//div[@ng-show='showEffectiveDateDefaultBlock']//p[@id='effectiveDateView']"));
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
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static boolean verifyViewButtonForManagementFee(Map<String, String> ViewMapDetails){
		try{
			
			if(ViewMapDetails.get("MFCalculationType")==null){
				return true;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.id("viewMngmntFee"));
			if(!bStatus){
				Messages.errorMsg = "Page cannot scroll to view Management Fee view Button";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Management Fee %']"));
			if(!bStatus){
				return false;
			}
					
			bStatus = Elements.click(Global.driver, By.id("viewMngmntFee"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on View button for Management Fee";
				return false;
			}
			
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//h4[text()='Management Fee']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "View Management Fee pop-up is not displayed";
				return false;
			}
			
			String sCalMethod = Elements.getText(Global.driver, By.xpath("//p[@id='managementFeeCalculationMethodView']"));
			if(sCalMethod!=null && !sCalMethod.equalsIgnoreCase(ViewMapDetails.get("MFCalculationType"))){
				Messages.errorMsg = "Actual value CalculationType is "+sCalMethod+" which is not equal to expected value: "+ViewMapDetails.get("MFCalculationType");
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static boolean verifyViewButtonForIncentiveFee(Map<String, String> ViewMapDetails){
		try{
			
			if(ViewMapDetails.get("IFCalculationType")==null){
				return true;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.id("viewIncntvFee"));
			if(!bStatus){
				Messages.errorMsg = "Page cannot scroll to view Incentive Fee view Button";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Incentive Fee %']"));
			if(!bStatus){
				return false;
			}
			bStatus = Elements.click(Global.driver, By.id("viewIncntvFee"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on View button for Management Fee";
				return false;
			}
			
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//h4[text()='Incentive Fee']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "View Transaction charges pop-up is not displayed";
				return false;
			}
			
			String sCalMethod = Elements.getText(Global.driver, By.xpath("//p[@id='managementFeeCalculationMethodView']"));
			if(sCalMethod!=null && !sCalMethod.equalsIgnoreCase(ViewMapDetails.get("IFCalculationType"))){
				Messages.errorMsg = "Actual value CalculationType is "+sCalMethod+" which is not equal to expected value: "+ViewMapDetails.get("IFCalculationType");
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
