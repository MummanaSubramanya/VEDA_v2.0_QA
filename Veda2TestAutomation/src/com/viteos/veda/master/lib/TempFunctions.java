package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;


public class TempFunctions {

	static boolean bStatus;
	public static boolean verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument(String FieldName,String sValue,String sStatus,String sAdditionalColNameArg, String sAdditionalColValueArg ,String sTableName,int time){
		try {
			bStatus = NewUICommonFunctions.searchInSearchPanel(FieldName,sValue,sStatus,sTableName,time);
			if(!bStatus){
				return false;
			}

			int columnIndex = NewUICommonFunctions.getTheColumnHeaderCount(sAdditionalColNameArg, sTableName);
			if(columnIndex<1){
				return false;
			}

			for (int i = 0; i < 8; i++) {				
				bStatus = NewUICommonFunctions.enterTextAndVerifyinFilterBoxOfGridTable(columnIndex,sAdditionalColValueArg,sTableName);
				if(!bStatus){
					NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
					continue;
				}
				return true;				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = sAdditionalColValueArg+" is not visible in the table even after 15 iterations";
		return bStatus;		
	}

	public static boolean modifyFeeTypeDetailsScreen(String sFieldName,String sName,String sAdditionalColNameArg,String sAdditionalColValueArg,String sTableName,Map<String, String> ModifiedFeeDetails){
		try{
			bStatus = verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument(sFieldName,sName,"active",sAdditionalColNameArg,sAdditionalColValueArg ,sTableName,10);
			if(!bStatus){
				return false;
			}
			Thread.sleep(2500);
			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-pencil']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Modify operation";
				return false;
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Management Fee Id")){
				bStatus = ManagementFeeAppFunctions.createNewManagementFee(ModifiedFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Fee Rule Name")){
				bStatus = FormulaSetupAppFunctions.addNewFormulas(ModifiedFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Parameter Id")){
				bStatus = ParameterAppFunctions.addNewParameter(ModifiedFeeDetails);
				if(!bStatus){
					return false;
				}
			}
			if(sAdditionalColNameArg.equalsIgnoreCase("Incentive Fee Id")){
				bStatus = IncentiveFeeAppFunctions.createNewIncentiveFee(ModifiedFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Investor Account Id")){
				bStatus = InvestorAccountMasterAppFunctions.addNewAccount(ModifiedFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Fee Distribution Id")){
				bStatus = FeeDistributionAppFunction.addFeeDistribution(ModifiedFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();return false;
		}
	}


	public static boolean verifyFeeTypeDetailsScreen(String sFieldName,String sName,String sAdditionalColNameArg,String sAdditionalColValueArg,String sTableName,Map<String, String> ValidateFeeDetails){
		try{
			bStatus = verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument(sFieldName,sName,"active",sAdditionalColNameArg,sAdditionalColValueArg ,sTableName,10);
			if(!bStatus){
				return false;
			}
			Thread.sleep(2500);
			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-pencil']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Modify operation";
				return false;
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Management Fee Id")){
				bStatus = ManagementFeeAppFunctions.doVerifyManagementFeeOnEditScreen(ValidateFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Fee Rule Name")){
				bStatus = FormulaSetupAppFunctions.verifycreatedFormulaDetailsInEditScreen(sName, ValidateFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Parameter Id")){
				bStatus = ParameterAppFunctions.verifyParameterDetailsInEditScreen(ValidateFeeDetails);
				if(!bStatus){
					return false;
				}
			}
			if(sAdditionalColNameArg.equalsIgnoreCase("Incentive Fee Id")){
				bStatus = IncentiveFeeAppFunctions.doVerifyIncentiveFeeOnEditScreen(ValidateFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Investor Account Id")){
				bStatus = InvestorAccountMasterAppFunctions.verifyInvestorAccountDetailsInEditScreen(ValidateFeeDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sAdditionalColNameArg.equalsIgnoreCase("Fee Distribution Id")){
				bStatus = FeeDistributionAppFunction.doVerifyFeeDistributioneOnEditScreen(ValidateFeeDetails);
				if(!bStatus){
					return false;
				}
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();return false;
		}
	}
	public static boolean deActivateFeeTye(String sFieldName,String sName,String sAdditionalColNameArg,String sAdditionalColValueArg,String sTableName){
		try{

			bStatus = verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument(sFieldName,sName,"active",sAdditionalColNameArg,sAdditionalColValueArg ,sTableName,10);
			if(!bStatus){
				return false;
			}
			Thread.sleep(2500);
			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-ban']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Deactivate operation";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg = sAdditionalColValueArg+" cannot be deactivated";
				return false;
			}
			return true;


		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean activateFeeTye(String sFieldName,String sName,String sAdditionalColNameArg,String sAdditionalColValueArg,String sTableName){
		try{

			bStatus = verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument(sFieldName,sName,"inactive",sAdditionalColNameArg,sAdditionalColValueArg ,sTableName,10);
			if(!bStatus){
				return false;
			}
			Thread.sleep(2500);
			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-check-circle-o']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on activate operation";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg =sName+" cannot be activated";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}





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

			if(mapRedemptionDetails.get("AmountorShares")!=null){
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
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amountValue']"), mapRedemptionDetails.get("Share Value"));
						if(!bStatus){
							Messages.errorMsg = "Share Value  Not Entered";
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

	private static boolean doOverrideOrVerifyChargeDetails(Map<String, String> mapRedemptionDetails ) {
		try {

			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsNo']/span"));
			if(!bStatus){
				Messages.errorMsg = "Page Not Scrolled to Notice Period Charges Radio Button fields";
				return false;
			}
			if(mapRedemptionDetails.get("NewAmountForNoticePeriod")!=null)
			{

				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsNo']/span"));
				if(!bStatus){
					Messages.errorMsg = "Notice Period Charges No Radio Button Not Clicked";
					return false;
				}
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

			if(mapRedemptionDetails.get("NewTransactionCharges")!=null)
			{
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsNo']/span"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Transaction Charges Radio Buttons are Not visible";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsNo']/span"));
				//bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
				if(!bStatus){
					Messages.errorMsg = "Transaction Charges No Radio Button Not Clicked";
					return false;
				}
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

			if(mapRedemptionDetails.get("NewPenaltyAmount")!=null)
			{
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-fixedLockupChargesDetailsNo']/span"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Penalty Charges Radio Buttons are Not visible";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='uniform-fixedLockupChargesDetailsNo']/span"));
				//bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span"));
				if(!bStatus){
					Messages.errorMsg = "Penalty Charges No Radio Button Not Clicked";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Penalty Charges New Charges field not visible";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='newTransactionCharges']"), mapRedemptionDetails.get("NewPenaltyAmount"));
				if(!bStatus){
					Messages.errorMsg = "New Penalty Charges not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@class,'showNewTransactionCharges') and contains(text(),'Save')]"));
				if(!bStatus){
					Messages.errorMsg = "Penalty charges Charges Save butotn not clicked";
					return false;
				}


			}

			if(mapRedemptionDetails.get("Adhoc Charges")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='adHoCharges']"), mapRedemptionDetails.get("Adhoc Charges"));
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

	public static boolean doVerifyChargesDetail(Map<String,String>mapVerifyRedemptionDetails){
		try
		{
			boolean validateStatus = true;
			String appendMsg="";

			//Thread.sleep(2000);

			// Verify Notice Charges  with out modification

			if(mapVerifyRedemptionDetails.get("ExpectedNoticeCharges") != null )
			{
				// Verify Notice Charge Default

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+ "[ Notice Charge Default Radio button Yes is not selected  ]\n";
				}


				// Verify Notice Charges 
				String ActualNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("noticeChargesAmount");

				String ExpectedNoticeCharge = mapVerifyRedemptionDetails.get("ExpectedNoticeCharges");

				if(ActualNoticeCharges != null){

					if(!ActualNoticeCharges.equalsIgnoreCase(ExpectedNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Notice Charges are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedNoticeCharges")+" ]\n";
					}
				}
			}

			// Verify Notice Charges  with modification

			if(mapVerifyRedemptionDetails.get("NewAmountForNoticePeriod") !=null && mapVerifyRedemptionDetails.get("ExpectedNewNoticeCharges") !=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedNoticeChargesDetailsNo']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "[ Notice Charge Default Radio button No is not selected  ]\n";
				}

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("noticeChargesAmount");
				String ExpectedNewNoticeCharge = mapVerifyRedemptionDetails.get("ExpectedNewNoticeCharges");
				if(ActualNewNoticeCharges != null){

					if(!ActualNewNoticeCharges.equalsIgnoreCase(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Notice Charges are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedNewNoticeCharges")+" ]\n";
					}
				}
			}
			//Verify the Transaction Charges with out modification

			if(mapVerifyRedemptionDetails.get("ExpectedTransactionCharges") != null ){
				// Verify Notice Charge Default

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[Transaction Charge Default Radio button Yes is not selected  ]\n";
				}


				// Verify Notice Charges 
				String ActualTransCharges = NewUICommonFunctions.jsGetElementAttribute("fixedTransactionChargesAmount");

				String ExpectedTransNoticeCharge = mapVerifyRedemptionDetails.get("ExpectedTransactionCharges");

				if(ActualTransCharges != null){

					if(!ActualTransCharges.equalsIgnoreCase(ExpectedTransNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Transaction  Charges are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedTransactionCharges")+" ]\n";
					}
				}
			}

			// Verify Transaction  Charges  with modification

			if(mapVerifyRedemptionDetails.get("NewTransactionCharges") !=null && mapVerifyRedemptionDetails.get("NewExpectedTransactionCharges") !=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedTransactionChargesDetailsNo']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "[ Transaction  Charge Default Radio button No Radio button is not selected ]\n";
				}

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("fixedTransactionChargesAmount");
				String ExpectedNewNoticeCharge = mapVerifyRedemptionDetails.get("NewExpectedTransactionCharges");

				if( ActualNewNoticeCharges != null){

					if(!ActualNewNoticeCharges.equalsIgnoreCase(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Transaction  Charges are Not matched with Expected "+mapVerifyRedemptionDetails.get("NewExpectedTransactionCharges")+" ]\n";
					}
				}
			}

			if(mapVerifyRedemptionDetails.get("ExpectedPenaltyAmount") != null ){
				// Verify Notice Charge Default

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedLockupChargesDetailsYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[Penalty Charge Default Radio button Yes is not selected  ]\n";
				}


				// Verify Notice Charges 
				String ActualTransCharges = NewUICommonFunctions.jsGetElementAttribute("penaltyAmountId");

				String ExpectedTransNoticeCharge = mapVerifyRedemptionDetails.get("ExpectedPenaltyAmount");

				if(ActualTransCharges != null){

					if(!ActualTransCharges.equalsIgnoreCase(ExpectedTransNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Penalty  Charges are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedPenaltyAmount")+" ]\n";
					}
				}
			}

			// Verify Transaction  Charges  with modification

			if(mapVerifyRedemptionDetails.get("NewPenaltyAmount") !=null && mapVerifyRedemptionDetails.get("NewExpectedPenaltyAmount") !=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-fixedLockupChargesDetailsNo']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "[ Penalty  Charge Default Radio button No Radio button is not selected ]\n";
				}

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("penaltyAmountId");
				String ExpectedNewNoticeCharge = mapVerifyRedemptionDetails.get("NewExpectedPenaltyAmount");

				if( ActualNewNoticeCharges != null){

					if(!ActualNewNoticeCharges.equalsIgnoreCase(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Penalty  Charges are Not matched with Expected "+mapVerifyRedemptionDetails.get("NewExpectedPenaltyAmount")+" ]\n";
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
					if(!ActualToatal.equalsIgnoreCase(ExpectedCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Total  Charges are Not matched with Expected "+mapVerifyRedemptionDetails.get("ExpectedTotalCharges")+" ]\n";
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

	public static void verifyViewButtons(Map<String, String> mapRedemptionDetails){
		try {


			bStatus = verifyViewButtonForNoticePeriods(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Notice period details","Verification Failed. Error: "+Messages.errorMsg);
				return;
			}
			if(mapRedemptionDetails.get("NPNumberOfDaysOrMonthOrYear")!= null && bStatus){
				Elements.click(Global.driver, By.xpath("//h4[normalize-space()='Notice Period Charges']//following::div[@class='modal-footer']//button[contains(text(),'Close')]"));
				Reporting.logResults("Pass","Verify Notice period details","Notice period Charges verified successfully");
			}

			bStatus = verifyViewButtonForTransactionCharge(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Transaction period details","Verification Failed. Error: "+Messages.errorMsg);
				return;
			}
			if(bStatus && mapRedemptionDetails.get("TCEffDate")!=null){
				Elements.click(Global.driver, By.xpath("//h4[normalize-space()='Transaction Charges']//following::div//button[contains(text(),'Close')]"));
				Reporting.logResults("Pass","Verify Transaction period details","Transaction period Charges verified successfully");
			}


			bStatus = verifyViewButtonForPenalty(mapRedemptionDetails);
			if(!bStatus){
				Reporting.logResults("Fail","Verify Lockup charge details","Verification Failed. Error: "+Messages.errorMsg);
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


	public static boolean doVerifyAvailableBalanceDetailsAtMaker(Map<String , String> mapRedemptionDetails){
		try {
			boolean bValidStatus = true;
			String appendErrMsg = "";
			if(mapRedemptionDetails.get("ExcpectedActualBalance") != null && mapRedemptionDetails.get("ExcpectedActualShares") != null && mapRedemptionDetails.get("ExcpectedAvailableAmount") != null && mapRedemptionDetails.get("ExcpectedAvailableShares") != null)
			{
				List<String> actualBalance = Arrays.asList(mapRedemptionDetails.get("ExcpectedActualBalance").split(","));
				List<String> actualShares = Arrays.asList(mapRedemptionDetails.get("ExcpectedActualShares").split(","));
				List<String> availableAmount = Arrays.asList(mapRedemptionDetails.get("ExcpectedAvailableAmount").split(","));
				List<String> avaialableShares = Arrays.asList(mapRedemptionDetails.get("ExcpectedAvailableShares").split(","));

				int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//input[contains(@id,'orderRedemptionMasters0.orderRedemptionDetails') and contains(@id,'actualAmount')]//parent::td"));
				if(xpathCount != actualBalance.size()){
					Messages.errorMsg = "Actual Count of Tax lots is "+xpathCount+" is not matching with the Expected Atual Balance Count "+actualBalance.size()+" ";
					return false;
				}

				for(int i=0 ; i<actualBalance.size() ; i++){
					if(!actualBalance.get(i).equalsIgnoreCase("None")){
						String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='orderRedemptionMasters0.orderRedemptionDetails"+i+".actualAmount']//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !actualBalance.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Actual Balance Expected value "+actualBalance.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}

					}

					if(!actualShares.get(i).equalsIgnoreCase("None")){
						String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='orderRedemptionMasters0.orderRedemptionDetails"+i+".actualUnits']//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !actualShares.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Actual Shares Expected value "+actualShares.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}
					}
					if(!availableAmount.get(i).equalsIgnoreCase("None")){
						String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='availableAmount_"+i+"']//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !availableAmount.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Available Amount Expected value "+actualShares.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}
					}
					if(!avaialableShares.get(i).equalsIgnoreCase("None")){
						String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='availableUnits_"+i+"']//parent::td")).trim().replaceAll(",", "");
						if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !avaialableShares.get(i).equalsIgnoreCase(actualValue)){
							appendErrMsg = appendErrMsg+"[ Available Shares Expected value "+actualShares.get(i)+" is Not matching with the Actual "+actualValue+" value ]";
							bValidStatus = false;
						}
					}
				}

			}

			if(mapRedemptionDetails.get("ExcpectedTotalActualBalance") != null && mapRedemptionDetails.get("ExcpectedTotalActualShares") != null && mapRedemptionDetails.get("ExcpectedTotalAvailableAmount") != null && mapRedemptionDetails.get("ExcpectedTotalAvailableShares") != null){
				if(!mapRedemptionDetails.get("ExcpectedTotalActualBalance").equalsIgnoreCase("None")){
					String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='totalAmount']//parent::td")).trim().replaceAll(",", "");
					if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !mapRedemptionDetails.get("ExcpectedTotalActualBalance").equalsIgnoreCase(actualValue)){
						appendErrMsg = appendErrMsg+"[ Total Actual Balance Expected value "+mapRedemptionDetails.get("ExcpectedTotalActualBalance")+" is Not matching with the Actual "+actualValue+" value ]";
						bValidStatus = false;
					}
				}
				if(!mapRedemptionDetails.get("ExcpectedTotalActualShares").equalsIgnoreCase("None")){
					String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='totalUnits']//parent::td")).trim().replaceAll(",", "");
					if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !mapRedemptionDetails.get("ExcpectedTotalActualShares").equalsIgnoreCase(actualValue)){
						appendErrMsg = appendErrMsg+"[ Total Actual Shares Expected value "+mapRedemptionDetails.get("ExcpectedTotalActualShares")+" is Not matching with the Actual "+actualValue+" value ]";
						bValidStatus = false;
					}
				}
				if(!mapRedemptionDetails.get("ExcpectedTotalAvailableAmount").equalsIgnoreCase("None")){
					String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='totalAmountAvailable']//parent::td")).trim().replaceAll(",", "");
					if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !mapRedemptionDetails.get("ExcpectedTotalAvailableAmount").equalsIgnoreCase(actualValue)){
						appendErrMsg = appendErrMsg+"[ Total Available Amount Expected value "+mapRedemptionDetails.get("ExcpectedTotalAvailableAmount")+" is Not matching with the Actual "+actualValue+" value ]";
						bValidStatus = false;
					}
				}
				if(!mapRedemptionDetails.get("ExcpectedTotalAvailableShares").equalsIgnoreCase("None")){
					String actualValue = Elements.getText(Global.driver, By.xpath("//input[@id='totalUnitsAvailable']//parent::td")).trim().replaceAll(",", "");
					if(actualValue !=null && !actualValue.equalsIgnoreCase("") && !mapRedemptionDetails.get("ExcpectedTotalAvailableShares").equalsIgnoreCase(actualValue)){
						appendErrMsg = appendErrMsg+"[ Total Available Shares Expected value "+mapRedemptionDetails.get("ExcpectedTotalAvailableShares")+" is Not matching with the Actual "+actualValue+" value ]";
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

}