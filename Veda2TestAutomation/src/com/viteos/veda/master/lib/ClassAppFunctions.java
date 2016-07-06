package com.viteos.veda.master.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class ClassAppFunctions 
{
	static boolean bStatus;
	public static boolean doFillClassTransferTab(Map<String, String> mapTransferDetails){
		try{
			
			bStatus =Elements.click(Global.driver , Locators .ClassMaster .Transfer.Label .lblTransfer );
			if(!bStatus){
				Messages .errorMsg ="Switch Menu Tab 	 is not Available";
				Assert.fail("Switch Menu Tab 	 is not Available");
				return false;
			}
			Thread.sleep(2000);
			Wait.waitForElementVisibility(Global.driver, Locators .ClassMaster .Transfer.Dropdown .ddlFrequencyClick, Constants.lTimeOut);
			if(mapTransferDetails.get("Incentive Fee Crystallization")!=null){
				if(mapTransferDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Elements.click(Global.driver,Locators.ClassMaster.Transfer .RadioButton .rbtnIncentiveFeeYes );
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization button Yes is not clicked";
						return false;
					}
				}
				if(mapTransferDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Elements.click(Global.driver, Locators.ClassMaster .Transfer .RadioButton .rbtnIncentiveFeeNO );
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization button No is not clicked";
						return false;
					}
				}
			}
			if(mapTransferDetails.get("Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapTransferDetails.get("Frequency"), Locators .ClassMaster .Transfer.Dropdown .ddlFrequencyClick , Locators.ClassMaster.commonDropdownForYearlySelect);
				//bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Frequency"), Locators .ClassMaster .Transfer.Dropdown .ddlFrequencyClick ,Locators .ClassMaster .Transfer.Dropdown.ddlSelectValue );
				if(!bStatus ){
					Messages .errorMsg ="Frequency Drop down  is not Selected";
					return false;
				}
			}
			/*if(mapTransferDetails .get("Change In UBO")!=null){
			if(mapTransferDetails.get("Change In UBO").equalsIgnoreCase("YES")){
				bStatus = Elements.click(Global.driver,Locators.ClassMaster.Transfer .RadioButton.rbtnChangeUBOYes );
				if(!bStatus){
					Messages.errorMsg ="Change In UBO button Yes is not clicked";
					return false;
				}
			}
			if(mapTransferDetails.get("Change In UBO").equalsIgnoreCase("NO")){
				bStatus = Elements.click(Global.driver, Locators.ClassMaster .Transfer .RadioButton.btnChangeUBONo);
				if(!bStatus){
					Messages.errorMsg ="Change In UBO button No is not clicked";
					return false;
				}
			}
		}*/

			if(mapTransferDetails.get("Notice Period Applicable")!=null){
				if(mapTransferDetails .get("Notice Period Applicable").equalsIgnoreCase("YES")){
					bStatus =Elements .click(Global.driver, Locators .ClassMaster .Transfer .RadioButton .rbtnNoticePeriodYes );
					if(!bStatus){
						Messages.errorMsg ="Notice Period Applicable Radio button Yes is not clicked";
						return false;
					}
					if(mapTransferDetails.get("Notice Period")!=null){	
						bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Transfer .TextBox .txtNoticePeriod ,mapTransferDetails.get("Notice Period"));
						if(!bStatus){
							Messages .errorMsg  =" Notice Period Date is not Entered";
							return false;
						}
					}
					if(mapTransferDetails.get("NoticeMonth")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("NoticeMonth"), Locators .ClassMaster .Transfer.Dropdown.ddlNoticePeroidMonthClick ,Locators .ClassMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticeMonth Drop down  is not Selected";
							return false;
						}
					}
					if(mapTransferDetails.get("NoticePeriodType")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("NoticePeriodType"), Locators .ClassMaster .Transfer.Dropdown.ddlNoticePeroidCalenderClick,Locators .ClassMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticePeriodType Drop down  is not Selected";
							return false;
						}
						if(mapTransferDetails.get("NoticePeriodType").equalsIgnoreCase("Business") && mapTransferDetails.get("Include Holidays")!=null)
						{
							if(mapTransferDetails.get("Include Holidays").equalsIgnoreCase("YES")){
								bStatus=Elements .click(Global.driver , Locators .ClassMaster .Transfer .RadioButton .rbtnIncludeHolidayYes );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday Yes Radio button is not clicked";
									return false;
								}
							}
							if(mapTransferDetails.get("Include Holidays").equalsIgnoreCase("NO")){
								bStatus = Elements.click(Global .driver , Locators .ClassMaster .Transfer.RadioButton .rbtnIncludeHolidayNo );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday NO Radio button is not clicked";
									return false;
								}

							}
						}
					}


					if(mapTransferDetails.get("Notice Period Charges")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Notice Period Charges"), Locators .ClassMaster .Transfer.Dropdown.ddlNoticePeroidChargesClick ,Locators .ClassMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Notice Period Charges  is not Selected";
							return false;
						}
						if(!mapTransferDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapTransferDetails.get("Amount/BPS/Percent")!=null)
						{
							bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Transfer .TextBox.txtAmountBpsPerc ,mapTransferDetails.get("Amount/BPS/Percent"));
							if(!bStatus){
								Messages .errorMsg  =" Notice Period Date is not Entered";
								return false;
							}

						}
					}
				}

				if(mapTransferDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus=Elements .click(Global.driver , Locators .ClassMaster .Transfer.RadioButton .rbtnNoticePeriodNo );
					if(!bStatus ){
						Messages.errorMsg ="Notice Period Radio button No is not clicked";
						return false;
					}
				}

			}
			if(mapTransferDetails.get("Transaction Charges")!=null){
				if(mapTransferDetails.get("Transaction Charges").equalsIgnoreCase("Yes")){
					bStatus = Elements .click(Global.driver , Locators .ClassMaster .Transfer.RadioButton .rbtnTransactionChargesYes );
					if(!bStatus ){
						Messages .errorMsg ="Transaction Charges Radio button YES is not clicked";
						return false;
					}
					if(mapTransferDetails.get("Effective Date")!=null){
						bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Transfer .TextBox.txtEffectiveDate ,mapTransferDetails.get("Effective Date"));
						if(!bStatus){
							Messages .errorMsg  ="Effective Date Period Date is not Entered";
							return false;
						}
						Elements.click(Global.driver, By.xpath("//h4//p[contains(text(),'Class')]"));

					}

					if(mapTransferDetails.get("Charges Type")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Charges Type"), Locators .ClassMaster .Transfer.Dropdown .ddlTransactionChargesTypeClick ,Locators .ClassMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Charges Type Drop down  is not Selected";
							return false;
						}
					}



					if(mapTransferDetails.get("Calculation Base")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Calculation Base"), Locators .ClassMaster .Transfer.Dropdown.ddlTransactionCalulationBase,Locators .ClassMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Calculation Base Drop down  is not Selected";
							return false;
						}
					}
					if(mapTransferDetails.get("Rate Method")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Rate Method"), Locators .ClassMaster .Transfer.Dropdown .ddlTransactionRateMethodClick ,Locators .ClassMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Rate Method Drop down  is not Selected";
							return false;
						}
						if(mapTransferDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							if(mapTransferDetails.get("Fixed Fee Rate")!=null){
								bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Transfer .TextBox.txtFixedFeeRate ,mapTransferDetails.get("Fixed Fee Rate"));
								if(!bStatus){
									Messages .errorMsg  ="Fixed Fee Rate Text is not Entered";
									return false;
								}
							}
						}
						if(mapTransferDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapTransferDetails.get("Rate Method").equalsIgnoreCase("Tiered"))
						{
							if(mapTransferDetails.get("Amount From")!=null && mapTransferDetails.get("Amount To")!=null && mapTransferDetails.get("Charges")!=null){
								bStatus = ClassAppFunctions.removeTransactionCharges("charges-tired-fee-tra");
								if(!bStatus){
									return false;
								}
								bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapTransferDetails,"transfer" , "Transfer");
								if(!bStatus){
									Messages.errorMsg = "Rate Methode Amount Details Entery Failed";
									return false;
								}
							}
						}
					}
				}
				if(mapTransferDetails.get("Transaction Charges")!=null){
					if(mapTransferDetails.get("Transaction Charges").equalsIgnoreCase("No")){
						bStatus =Elements.click(Global.driver , Locators .ClassMaster .Transfer.RadioButton .rbtnTransactionChargesNO );
						if(!bStatus ){
							Messages .errorMsg ="Transaction Charges Radio button NO is not clicked";
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
	public static boolean doFillClassSwitchTab(Map<String, String> mapSearchSwitchDetails){
		try
		{
			
			bStatus =Elements.click(Global.driver , Locators .ClassMaster .Switch.Label .lblSwitch );
			if(!bStatus){
				Messages .errorMsg ="Switch Menu Tab 	 is not Available";
				Assert.fail("Switch Menu Tab 	 is not Available");
				return false;
			}
			Thread.sleep(2000);
			Wait.waitForElementVisibility(Global.driver, Locators .ClassMaster .Switch.Dropdown .ddlFrequencyClick, Constants.lTimeOut);
			if(mapSearchSwitchDetails.get("Incentive Fee Crystallization")!=null){
				if(mapSearchSwitchDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Elements.click(Global.driver,Locators.ClassMaster.Switch .RadioButton .rbtnIncentiveFeeYes );
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization Radio button Yes is not clicked";
						return false;
					}
				}
				if(mapSearchSwitchDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Elements.click(Global.driver, Locators.ClassMaster .Switch .RadioButton .rbtnIncentiveFeeNO );
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization Radio button No is not clicked";
						return false;
					}
				}
			}
			if(mapSearchSwitchDetails.get("Frequency")!=null){
				bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Frequency"), Locators .ClassMaster .Switch.Dropdown .ddlFrequencyClick ,Locators.ClassMaster.commonDropdownForYearlySelect );
				if(!bStatus ){
					Messages .errorMsg ="Frequency Drop down  is not Selected";
					return false;
				}
			}

			
			if(mapSearchSwitchDetails.get("Notice Period Applicable")!=null){
				if(mapSearchSwitchDetails .get("Notice Period Applicable").equalsIgnoreCase("YES")){
					bStatus = Elements .click(Global.driver, Locators .ClassMaster .Switch .RadioButton .rbtnNoticePeriodYes );
					if(!bStatus){
						Messages.errorMsg ="Notice Period Applicable Radio button Yes is not clicked";
						return false;
					}
					if(mapSearchSwitchDetails.get("Notice Period")!=null){	
						bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Switch .TextBox .txtNoticePeriod ,mapSearchSwitchDetails.get("Notice Period"));
						if(!bStatus){
							Messages .errorMsg  =" Notice Period Date is not Entered";
							return false;
						}
					}
					if(mapSearchSwitchDetails.get("NoticeMonth")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("NoticeMonth"), Locators .ClassMaster .Switch.Dropdown.ddlNoticePeroidMonthClick ,Locators .ClassMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticeMonth Drop down  is not Selected";
							return false;
						}
					}
					if(mapSearchSwitchDetails.get("NoticePeriodType")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("NoticePeriodType"), Locators .ClassMaster .Switch.Dropdown.ddlNoticePeroidCalenderClick,Locators .ClassMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticePeriodType Drop down  is not Selected";
							return false;
						}
						if(mapSearchSwitchDetails.get("NoticePeriodType").equalsIgnoreCase("Business") && mapSearchSwitchDetails.get("Include Holidays")!=null)
						{
							if(mapSearchSwitchDetails.get("Include Holidays").equalsIgnoreCase("YES")){
								bStatus=Elements .click(Global.driver , Locators .ClassMaster .Switch .RadioButton .rbtnIncludeHolidayYes );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday Yes Radio button is not clicked";
									return false;
								}
							}
							if(mapSearchSwitchDetails.get("Include Holidays").equalsIgnoreCase("NO")){
								bStatus = Elements.click(Global .driver , Locators .ClassMaster .Switch.RadioButton .rbtnIncludeHolidayNo );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday NO Radio button is not clicked";
									return false;
								}

							}
						}
					}
					if(mapSearchSwitchDetails.get("Notice Period Charges")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Notice Period Charges"), Locators .ClassMaster .Switch.Dropdown.ddlNoticePeroidChargesClick ,Locators .ClassMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Notice Period Charges  is not Selected";
							return false;
						}
						if(!mapSearchSwitchDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapSearchSwitchDetails.get("Amount/BPS/Percent")!=null){
							bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Switch .TextBox.txtAmountBpsPerc ,mapSearchSwitchDetails.get("Amount/BPS/Percent"));
							if(!bStatus){
								Messages .errorMsg  =" Notice Period Date is not Entered";
								return false;
							}

						}
					}		
				}

				if(mapSearchSwitchDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus=Elements .click(Global.driver , Locators .ClassMaster .Switch.RadioButton .rbtnNoticePeriodNo );
					if(!bStatus ){
						Messages.errorMsg ="Notice Period Radio button No is not clicked";
						return false;
					}
				}

			}
			if(mapSearchSwitchDetails.get("Transaction Charges")!=null)
			{
				if(mapSearchSwitchDetails.get("Transaction Charges").equalsIgnoreCase("Yes")){
					bStatus = Elements .click(Global.driver , Locators .ClassMaster .Switch.RadioButton .rbtnTransactionChargesYes );
					if(!bStatus ){
						Messages .errorMsg ="Transaction Charges Radio button YES is not clicked";
						return false;
					}
					if(mapSearchSwitchDetails.get("Effective Date")!=null){
						bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Switch .TextBox.txtEffectiveDate ,mapSearchSwitchDetails.get("Effective Date"));
						if(!bStatus){
							Messages .errorMsg  ="Effective Date Period Date is not Entered";
							return false;
						}
						Elements.click(Global.driver, By.xpath("//h4//p[contains(text(),'Class')]"));
					}

					if(mapSearchSwitchDetails.get("Charges Type")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Charges Type"), Locators .ClassMaster .Switch.Dropdown .ddlTransactionChargesTypeClick ,Locators .ClassMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Charges Type Drop down  is not Selected";
							return false;
						}
					}

					if(mapSearchSwitchDetails.get("Calculation Base")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Calculation Base"), Locators .ClassMaster .Switch.Dropdown.ddlTransactionCalulationBase,Locators .ClassMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Calculation Base Drop down  is not Selected";
							return false;
						}
					}
					if(mapSearchSwitchDetails.get("Rate Method")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Rate Method"), Locators .ClassMaster .Switch.Dropdown .ddlTransactionRateMethodClick ,Locators .ClassMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Rate Method Drop down  is not Selected";
							return false;
						}
						if(mapSearchSwitchDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							if(mapSearchSwitchDetails.get("Fixed Fee Rate")!=null){
								bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Switch .TextBox.txtFixedFeeRate ,mapSearchSwitchDetails.get("Fixed Fee Rate"));
								if(!bStatus){
									Messages .errorMsg  ="Fixed Fee Rate Text is not Entered";
									return false;
								}
							}
						}
						if(mapSearchSwitchDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapSearchSwitchDetails.get("Rate Method").equalsIgnoreCase("Tiered"))
						{
							if(mapSearchSwitchDetails.get("Amount From")!=null&&mapSearchSwitchDetails.get("Amount To")!=null&&mapSearchSwitchDetails.get("Charges")!=null){
								bStatus = ClassAppFunctions.removeTransactionCharges("charges-tired-fee-swi");
								if(!bStatus){
									return false;
								}
								bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapSearchSwitchDetails, "switch", "Switch");
								if(!bStatus){
									Messages.errorMsg = "Transaction Charges Rate Methode Details Entry Failed";
									return false;
								}
							}
						}
					}

				}
				if(mapSearchSwitchDetails.get("Transaction Charges")!=null){
					if(mapSearchSwitchDetails.get("Transaction Charges").equalsIgnoreCase("No")){
						bStatus =Elements.click(Global.driver , Locators .ClassMaster .Switch.RadioButton .rbtnTransactionChargesNO );
						if(!bStatus ){
							Messages .errorMsg ="Transaction Charges Radio button NO is not clicked";
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
	public static boolean doFillClassExchangeTab(Map<String, String> mapExchangeDetails){
		try{
			
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.Exchange.Label.lblExchange, Constants.lTimeOut);
			if(!bStatus){
				return false;
			}
			bStatus =Elements.click(Global.driver , Locators.ClassMaster.Exchange.Label.lblExchange );
			if(!bStatus){
				Messages .errorMsg ="Exchange Menu Tab 	 is not Available";
				Assert.fail("Exchange Menu Tab 	 is not Available");
				return false;
			}
			Thread.sleep(2000);

			Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.Exchange.Dropdown.ddlFrequencyClick , Constants.lTimeOut);
			if(mapExchangeDetails.get("Incentive Fee Crystallization")!=null){
				if(mapExchangeDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Elements.click(Global.driver,Locators.ClassMaster.Exchange.RadioButton.rbtnIncentiveFeeYes);
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization Radio button Yes is not clicked";
						return false;
					}
				}
				if(mapExchangeDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.Exchange.RadioButton.rbtnIncentiveFeeNO );
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization Radio button No is not clicked";
						return false;
					}
				}
			}
			
			if(mapExchangeDetails.get("Frequency")!=null){
				bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Frequency"), Locators.ClassMaster.Exchange.Dropdown.ddlFrequencyClick ,Locators.ClassMaster.commonDropdownForYearlySelect );
				if(!bStatus ){
					Messages .errorMsg ="Frequency Drop down  is not Selected";
					return false;
				}
			}

		

			//Filling Out Notice Period Details.

			if(mapExchangeDetails.get("Notice Period Applicable")!=null){
				if(mapExchangeDetails .get("Notice Period Applicable").equalsIgnoreCase("YES")){
					bStatus = Elements .click(Global.driver, Locators.ClassMaster.Exchange.RadioButton.rbtnNoticePeriodYes );
					if(!bStatus){
						Messages.errorMsg ="Notice Period Applicable Radio button Yes is not clicked";
						return false;
					}
					if(mapExchangeDetails.get("Notice Period")!=null){	
						bStatus =Elements .enterText(Global.driver, Locators.ClassMaster.Exchange.TextBox.txtNoticePeriod ,mapExchangeDetails.get("Notice Period"));
						if(!bStatus){
							Messages .errorMsg  =" Notice Period Date is not Entered";
							return false;
						}
					}
					if(mapExchangeDetails.get("NoticeMonth")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("NoticeMonth"), Locators.ClassMaster.Exchange.Dropdown.ddlNoticePeroidMonthClick ,Locators.ClassMaster.Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticeMonth Drop down  is not Selected";
							return false;
						}
					}
					if(mapExchangeDetails.get("NoticePeriodType")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("NoticePeriodType"), Locators.ClassMaster.Exchange.Dropdown.ddlNoticePeroidCalenderClick,Locators.ClassMaster.Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticePeriodType Drop down  is not Selected";
							return false;
						}
						if(mapExchangeDetails.get("NoticePeriodType").equalsIgnoreCase("Business") && mapExchangeDetails.get("Include Holidays")!=null)
						{
							if(mapExchangeDetails.get("Include Holidays").equalsIgnoreCase("YES")){
								bStatus=Elements .click(Global.driver , Locators.ClassMaster.Exchange.RadioButton .rbtnIncludeHolidayYes );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday Yes Radio button is not clicked";
									return false;
								}
							}
							if(mapExchangeDetails.get("Include Holidays").equalsIgnoreCase("NO")){
								bStatus = Elements.click(Global .driver , Locators.ClassMaster.Exchange.RadioButton.rbtnIncludeHolidayNo );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday NO Radio button is not clicked";
									return false;
								}

							}
						}
					}
					if(mapExchangeDetails.get("Notice Period Charges")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Notice Period Charges"), Locators.ClassMaster.Exchange.Dropdown.ddlNoticePeroidChargesClick ,Locators.ClassMaster.Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Notice Period Charges  is not Selected";
							return false;
						}
						if(!mapExchangeDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapExchangeDetails.get("Amount/BPS/Percent")!=null){
							bStatus =Elements .enterText(Global.driver, Locators.ClassMaster.Exchange.TextBox.txtAmountBpsPerc ,mapExchangeDetails.get("Amount/BPS/Percent"));
							if(!bStatus){
								Messages .errorMsg  =" Notice Period Date is not Entered";
								return false;
							}
						}
					}				
				}
				if(mapExchangeDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus=Elements .click(Global.driver , Locators.ClassMaster.Exchange.RadioButton.rbtnNoticePeriodNo );
					if(!bStatus ){
						Messages.errorMsg ="Notice Period Radio button No is not clicked";
						return false;
					}
				}

			}
			//Filling Out Transaction Charges Details.

			if(mapExchangeDetails.get("Transaction Charges")!=null)
			{
				if(mapExchangeDetails.get("Transaction Charges").equalsIgnoreCase("Yes")){
					bStatus = Elements .click(Global.driver , Locators.ClassMaster.Exchange.RadioButton.rbtnTransactionChargesYes );
					if(!bStatus ){
						Messages .errorMsg ="Transaction Charges Radio button YES is not clicked";
						return false;
					}
					if(mapExchangeDetails.get("Effective Date")!=null){
						bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Exchange .TextBox.txtEffectiveDate ,mapExchangeDetails.get("Effective Date"));
						if(!bStatus){
							Messages .errorMsg  ="Effective Date Period Date is not Entered";
							return false;
						}
						Elements.click(Global.driver, By.xpath("//h4//p[contains(text(),'Class')]"));
					}
					if(mapExchangeDetails.get("Charges Type")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Charges Type"), Locators .ClassMaster .Exchange.Dropdown .ddlTransactionChargesTypeClick ,Locators .ClassMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Charges Type Drop down  is not Selected";
							return false;
						}
					}
					if(mapExchangeDetails.get("Calculation Base")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Calculation Base"), Locators .ClassMaster .Exchange.Dropdown.ddlTransactionCalulationBase,Locators .ClassMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Calculation Base Drop down  is not Selected";
							return false;
						}
					}
					if(mapExchangeDetails.get("Rate Method")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Rate Method"), Locators .ClassMaster .Exchange.Dropdown .ddlTransactionRateMethodClick ,Locators .ClassMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Rate Method Drop down  is not Selected";
							return false;
						}
						if(mapExchangeDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							if(mapExchangeDetails.get("Fixed Fee Rate")!=null){
								bStatus =Elements .enterText(Global.driver, Locators .ClassMaster .Exchange .TextBox.txtFixedFeeRate ,mapExchangeDetails.get("Fixed Fee Rate"));
								if(!bStatus){
									Messages .errorMsg  ="Fixed Fee Rate Text is not Entered";
									return false;
								}
							}
						}
						if(mapExchangeDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapExchangeDetails.get("Rate Method").equalsIgnoreCase("Tiered"))
						{
							if(mapExchangeDetails.get("Amount From")!=null && mapExchangeDetails.get("Amount To")!=null && mapExchangeDetails.get("Charges")!=null){
								bStatus = ClassAppFunctions.removeTransactionCharges("charges-tired-fee-exc");
								if(!bStatus){
									return false;
								}
								bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapExchangeDetails, "exchange", "Exchange");
								if(!bStatus){
									Messages.errorMsg = "Transaction Charges Rate Method Details Entry Failed";
									return false;
								}
							}			
						}
					}
				}
				if(mapExchangeDetails.get("Transaction Charges").equalsIgnoreCase("No")){
					bStatus =Elements.click(Global.driver , Locators .ClassMaster .Exchange .RadioButton .rbtnTransactionChargesNO );
					if(!bStatus ){
						Messages .errorMsg ="Transaction Charges Radio button NO is not clicked";
						return false;
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
	public static boolean doFillClassRedemptionTab(Map<String, String> mapClassRedemptionDetails)
	{
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//li[5]//a[contains(text(),'Redemption')]"), Constants.lTimeOut);
			if(!bStatus)
			{
				return false;
			}
			bStatus=NewUICommonFunctions.spinnerClick(Global.driver , By.xpath("//li[5]//a[contains(text(),'Redemption')]"));
			if(!bStatus)
			{
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtMinimumAmount);
			if(!bStatus)
			{
				return false;
			}
			if(mapClassRedemptionDetails.get("Minimum Amount") != null)
			{
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtMinimumAmount, mapClassRedemptionDetails.get("Minimum Amount"));
				if(!bStatus)
				{
					Messages.errorMsg = "Minimum Amount wasn't inputed into the field in Redemption Tab";
					return false;
				}
			}
			if(mapClassRedemptionDetails.get("Minimum Shares") != null)
			{
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtMinimumShares, mapClassRedemptionDetails.get("Minimum Shares"));
				if(!bStatus)
				{
					Messages.errorMsg = "Minimum Shares wasn't inputed into the field in Redemption Tab";
					return false;
				}
			}
			if(mapClassRedemptionDetails.get("Redemption Type") != null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("Redemption Type"), Locators.LegalEntityMaster.RedemptionTab.DropDown.dllClickRedemptionType);
				if(!bStatus)
				{
					return false;
				}
			}
			if(mapClassRedemptionDetails.get("Redemption Frequency") != null)
			{
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapClassRedemptionDetails.get("Redemption Frequency"), Locators.LegalEntityMaster.RedemptionTab.DropDown.dllClickRedemptionFrequency, Locators.ClassMaster.commonDropdownForYearlySelect);
				//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("Redemption Frequency"), Locators.LegalEntityMaster.RedemptionTab.DropDown.dllClickRedemptionFrequency);
				if(!bStatus)
				{
					return false;
				}
			}
			// Filling Out Notice Period Details		
			if(mapClassRedemptionDetails.get("IsNoticePeriodApplicable")!= null)
			{
				if(mapClassRedemptionDetails.get("IsNoticePeriodApplicable").equalsIgnoreCase("Yes"))
				{
					String sRadioLocatorYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isNoticePeriodApplicable.replace("YesOrNoReplaceValue", "Yes");
					bStatus = Elements.click(Global.driver, By.xpath(sRadioLocatorYes));
					if(!bStatus)
					{
						Messages.errorMsg = "Radio button wasn't selected for isNoticePeriodApplicable" + sRadioLocatorYes;
						return false;
					}
					if(mapClassRedemptionDetails.get("NoticePeriodQuantity") != null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtNoticePeriodQuantity, mapClassRedemptionDetails.get("NoticePeriodQuantity"));
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period for No of Days/Months/Years wasn't inputed into text field";
							return false;
						}
					}
					if(mapClassRedemptionDetails.get("NoticePeriodQuantityType") != null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("NoticePeriodQuantityType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickNoticePeriodQuantityType);
						if(!bStatus)
						{
							return false;
						}
					}
					if(mapClassRedemptionDetails.get("CalendarOrBusinessNoticeDays") != null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("CalendarOrBusinessNoticeDays"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickCalendarOrBusinessNoticeDays);
						if(!bStatus)
						{
							return false;
						}
						if(mapClassRedemptionDetails.get("CalendarOrBusinessNoticeDays").equalsIgnoreCase("Business"))
						{
							if(mapClassRedemptionDetails.get("IncludeHolidays") != null){
								if(mapClassRedemptionDetails.get("IncludeHolidays").equalsIgnoreCase("Yes"))
								{
									String sIncludeHolidaysRadioYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isBusinessHolidaysToIncludeYes;
									bStatus = Elements.click(Global.driver, By.xpath(sIncludeHolidaysRadioYes));
									if(!bStatus)
									{
										Messages.errorMsg = "Radio button wasn't selected for isBusinessHolidaysToInclude " + sIncludeHolidaysRadioYes;
										return false;
									}
								}
								if(mapClassRedemptionDetails.get("IncludeHolidays").equalsIgnoreCase("No"))
								{
									String sIncludeHolidaysRadioNo = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isBusinessHolidaysToIncludeNo;
									bStatus = Elements.click(Global.driver, By.xpath(sIncludeHolidaysRadioNo));
									if(!bStatus)
									{
										Messages.errorMsg = "Radio button wasn't selected for isBusinessHolidaysToInclude " + sIncludeHolidaysRadioNo;
										return false;
									}
								}
							}
						}
					}
					if (mapClassRedemptionDetails.get("NoticePeriodChargesType") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("NoticePeriodChargesType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickNoticePeriodChargesType);
						if(!bStatus)
						{
							return false;
						}
						if (!mapClassRedemptionDetails.get("NoticePeriodChargesType").equalsIgnoreCase("None") && mapClassRedemptionDetails.get("ChargesAmountOrBPSOrPercent") != null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtAmountOrBPSOrPercent, mapClassRedemptionDetails.get("ChargesAmountOrBPSOrPercent"));
							if(!bStatus)
							{
								Messages.errorMsg = "ChargesAmountOrBPSOrPercent wasn't entered into the text field text box may not present";
								return false;
							}
						}
					}
				}
				if(mapClassRedemptionDetails.get("IsNoticePeriodApplicable").equalsIgnoreCase("No"))
				{
					String sRadioLocatorNo = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isNoticePeriodApplicable.replace("YesOrNoReplaceValue", "No");
					bStatus = Elements.click(Global.driver, By.xpath(sRadioLocatorNo));
					if (!bStatus) 
					{
						Messages.errorMsg = "Radio button wasn't selected for isNoticePeriodApplicable" + sRadioLocatorNo;
						return false;												
					}
				}
			}
			if (mapClassRedemptionDetails.get("IsLockupApplying") != null)
			{
				if (mapClassRedemptionDetails.get("IsLockupApplying").equalsIgnoreCase("Yes")) 
				{
					String sIsLockupApplyingYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isLockup.replace("YesOrNoReplaceValue", "Yes");
					bStatus = Elements.click(Global.driver, By.xpath(sIsLockupApplyingYes));
					if (!bStatus) 
					{
						Messages.errorMsg = "Radio button wasn't selected for IsLockupApplying" + sIsLockupApplyingYes;
						return false;												
					}
					if(mapClassRedemptionDetails.get("LockupPeriodQuantity") != null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtLockupPeriodQuantity, mapClassRedemptionDetails.get("LockupPeriodQuantity"));
						if(!bStatus)
						{
							Messages.errorMsg = "Lockup Period for No of Days/Months/Years wasn't inputed into text field";
							return false;
						}
					}
					if(mapClassRedemptionDetails.get("LockupPeriodQuantityType") != null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("LockupPeriodQuantityType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.dllClickLockupPeriodQuantityType);
						if(!bStatus)
						{
							return false;
						}
					}
					if(mapClassRedemptionDetails.get("CalendarOrBusinessLockupDays") != null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("CalendarOrBusinessLockupDays"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickCalendarOrBusinessLockupDays);
						if(!bStatus)
						{
							return false;
						}
						if(mapClassRedemptionDetails.get("CalendarOrBusinessLockupDays").equalsIgnoreCase("Business"))
						{
							if(mapClassRedemptionDetails.get("LockupIncludeHolidays") != null){
								if(mapClassRedemptionDetails.get("LockupIncludeHolidays").equalsIgnoreCase("Yes"))
								{
									String sLockupIncludeHolidaysRadioYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isLockupBusinessHolidaysToInclude.replace("ReplaceIndexValue", "1");
									bStatus = Elements.click(Global.driver, By.xpath(sLockupIncludeHolidaysRadioYes));
									if(!bStatus)
									{
										Messages.errorMsg = "Radio button wasn't selected for isBusinessHolidaysToInclude " + sLockupIncludeHolidaysRadioYes;
										return false;
									}
								}
								if(mapClassRedemptionDetails.get("LockupIncludeHolidays").equalsIgnoreCase("No"))
								{
									String sLockupIncludeHolidaysRadioNo = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isLockupBusinessHolidaysToInclude.replace("ReplaceIndexValue", "2");
									bStatus = Elements.click(Global.driver, By.xpath(sLockupIncludeHolidaysRadioNo));
									if(!bStatus)
									{
										Messages.errorMsg = "Radio button wasn't selected for isBusinessHolidaysToInclude " + sLockupIncludeHolidaysRadioNo;
										return false;
									}
								}
							}
						}
					}
					// Filling Out Lockup Period Section Details.
					if (mapClassRedemptionDetails.get("LockupRateMethod") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("LockupRateMethod"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickLockupRateMethod);
						if (!bStatus) 
						{
							return false;							
						}
						if (mapClassRedemptionDetails.get("LockupChargesType") != null) 
						{
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("LockupChargesType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickLockupChargesType);
							if (!bStatus) 
							{
								return false;							
							}
						}
						if (!mapClassRedemptionDetails.get("LockupRateMethod").equalsIgnoreCase("None")) 
						{
							if (mapClassRedemptionDetails.get("LockupRateMethod").equalsIgnoreCase("Fixed") && mapClassRedemptionDetails.get("LockupFixedAmountOrPercent") != null) 
							{
								bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtLockupFixedAmountOrPercent, mapClassRedemptionDetails.get("LockupFixedAmountOrPercent"));
								if (!bStatus) 
								{
									Messages.errorMsg = "Lockup Fixed Amount Or Percent text field wasn't visible";
									return false;							
								}
							}
							if (mapClassRedemptionDetails.get("LockupRateMethod").equalsIgnoreCase("Slab")) 
							{
								if(mapClassRedemptionDetails.get("LockupSlabFrom")!=null && mapClassRedemptionDetails.get("LockupSlabTo")!=null && mapClassRedemptionDetails.get("LockupSlabCharges")!=null){

									//Un Comment the Below code after resolving the Bug of Remove Button Not Clickable 	

									bStatus = ClassAppFunctions.removeRateMethodElements("charges-tired-fee-lockup");
									if(!bStatus){
										return false;
									}
									bStatus = NewUICommonFunctions.addLockupRateMethodDetails(mapClassRedemptionDetails);
									if(!bStatus){
										return false;
									}
								}
							}
						}
					}
				}
				if (mapClassRedemptionDetails.get("IsLockupApplying").equalsIgnoreCase("No")) 
				{
					String sIsLockupApplyingNo = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isLockup.replace("YesOrNoReplaceValue", "No");
					bStatus = Elements.click(Global.driver, By.xpath(sIsLockupApplyingNo));
					if (!bStatus) 
					{
						Messages.errorMsg = "Radio button wasn't selected for IsLockupApplying" + sIsLockupApplyingNo;
						return false;												
					}
				}
			}
			//Filling Out TxnCharges Applicable section Details.
			if (mapClassRedemptionDetails.get("IsTxnChargesApplicable") != null) 
			{
				if (mapClassRedemptionDetails.get("IsTxnChargesApplicable").equalsIgnoreCase("Yes")) 
				{
					String sIsTxnChargesApplicableYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isTransactionChargesApplying.replace("YesOrNoReplaceValue", "Yes");
					bStatus = Elements.click(Global.driver, By.xpath(sIsTxnChargesApplicableYes));
					if (!bStatus) 
					{
						Messages.errorMsg = "Radio button wasn't selected for IsTxnChargesApplicable" + sIsTxnChargesApplicableYes;
						return false;												
					}
					if (mapClassRedemptionDetails.get("EffectiveDate") != null) 
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtTxnChargesEffectiveDate, mapClassRedemptionDetails.get("EffectiveDate"));
						if (!bStatus) 
						{
							Messages.errorMsg = "Effective Date wasn't inputed for EffectiveDate filed";
							return false;												
						}
						Elements.click(Global.driver, By.xpath("//h4//p[contains(text(),'Class')]"));
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LeagalEntitySearchPanel.lblClass);
					if(!bStatus)
					{
						return false;
					}
					if (mapClassRedemptionDetails.get("TxnChrgesType") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("TxnChrgesType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickTransactionChargesType);
						if (!bStatus) 
						{
							Messages.errorMsg = "TransactionChargesType wasn't selected";
							return false;												
						}
					}
					if (mapClassRedemptionDetails.get("TxnCalcBaseType") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("TxnCalcBaseType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickTrnsactionChargesCalcBase);
						if (!bStatus) 
						{
							Messages.errorMsg = "TxnCalcBaseType wasn't selected";
							return false;												
						}
					}
					if (mapClassRedemptionDetails.get("TxnRateMethod") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("TxnRateMethod"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickTransactionRateMethod);
						if (!bStatus) 
						{
							Messages.errorMsg = "TransactionRateMethod wasn't selected";
							return false;												
						}
						if(!mapClassRedemptionDetails.get("TxnRateMethod").equalsIgnoreCase("None"))
						{
							if (mapClassRedemptionDetails.get("TxnRateMethod").equalsIgnoreCase("Fixed") && mapClassRedemptionDetails.get("TxnFixedFeeRate") != null) 
							{
								bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtTxnFixedFeeRate, mapClassRedemptionDetails.get("TxnFixedFeeRate"));
								if (!bStatus) 
								{
									Messages.errorMsg = "FixedFeeRate text field wasn't visible";
									return false;												
								}
							}
							if (mapClassRedemptionDetails.get("TxnRateMethod").equalsIgnoreCase("Slab") || mapClassRedemptionDetails.get("TxnRateMethod").equalsIgnoreCase("Tiered")) 
							{
								if(mapClassRedemptionDetails.get("Amount From")!=null&&mapClassRedemptionDetails.get("Amount To")!=null&&mapClassRedemptionDetails.get("Charges")!=null){
									bStatus = ClassAppFunctions.removeTransactionCharges("charges-tired-fee-red");
									if(!bStatus){
										return false;
									}
									bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapClassRedemptionDetails, "redemption", "Red");
									if(!bStatus){
										Messages.errorMsg = "Transaction Charges Rate Methode Details Entry Failed";
										return false;
									}
								}
							}
							if (mapClassRedemptionDetails.get("PartialRedChargesIsAsTxnCharges") != null) 
							{
								if (mapClassRedemptionDetails.get("PartialRedChargesIsAsTxnCharges").equalsIgnoreCase("Yes")) 
								{
									String sPartialRedChargesIsAsTxnChargesYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isRedChargesSameAsTxnCharges.replace("YesOrNoReplaceValue", mapClassRedemptionDetails.get("PartialRedChargesIsAsTxnCharges"));
									bStatus = Elements.click(Global.driver, By.xpath(sPartialRedChargesIsAsTxnChargesYes));
									if (!bStatus) 
									{
										Messages.errorMsg = "PartialRedChargesIsAsTxnCharges radio button wasn't visible";
										return false;												
									}
								}
								if (mapClassRedemptionDetails.get("PartialRedChargesIsAsTxnCharges").equalsIgnoreCase("No")) 
								{
									bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LeagalEntitySearchPanel.lblClass);
									if(!bStatus)
									{
										return false;
									}
									String sPartialRedChargesIsAsTxnChargesNo = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isRedChargesSameAsTxnCharges.replace("YesOrNoReplaceValue", mapClassRedemptionDetails.get("PartialRedChargesIsAsTxnCharges"));
									bStatus = Elements.click(Global.driver, By.xpath(sPartialRedChargesIsAsTxnChargesNo));
									if (!bStatus) 
									{
										Messages.errorMsg = "PartialRedChargesIsAsTxnCharges radio button wasn't visible";
										return false;												
									}
									if (mapClassRedemptionDetails.get("PartialRedChargesEffectiveDate") != null) 
									{
										//NewUICommonFunctions.scrollToView(Locators.LegalEntityMaster.RedemptionTab.TextBox.txtRedPartialChargeAsTxnChargeEffectiveDate);
										bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtRedPartialChargeAsTxnChargeEffectiveDate, mapClassRedemptionDetails.get("PartialRedChargesEffectiveDate"));
										if (!bStatus) 
										{
											Messages.errorMsg = "PartialRedChargesEffectiveDate field wasn't visible";
											return false;												
										}
									}
									bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LeagalEntitySearchPanel.lblClass);
									if(!bStatus)
									{
										return false;
									}
									if (mapClassRedemptionDetails.get("PartialRedChargeCalcBase") != null) 
									{
										bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("PartialRedChargeCalcBase"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickPartialRedChargeRateCalcBase);
										if (!bStatus) 
										{
											Messages.errorMsg = "PartialRedChargeCalcBase DropDown wasn't Selected";
											return false;												
										}
									}										
									if (mapClassRedemptionDetails.get("PartialRedChargeRateMethod") != null) 
									{
										bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassRedemptionDetails.get("PartialRedChargeRateMethod"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickPartialRedChargeRateMethod);
										if (!bStatus) 
										{
											Messages.errorMsg = "PartialRedChargeRateMethod DropDown wasn't Selected";
											return false;												
										}
										if(!mapClassRedemptionDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("None"))
										{
											if (mapClassRedemptionDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Fixed") && mapClassRedemptionDetails.get("PartRedFixedFeeRate") != null)
											{
												bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtPartRedFixedFeeRate, mapClassRedemptionDetails.get("PartRedFixedFeeRate"));
												if (!bStatus) 
												{
													Messages.errorMsg = "PartRedFixedFeeRate field wasn't visible";
													return false;												
												}
											}
											if (mapClassRedemptionDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Slab") || mapClassRedemptionDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Tiered"))
											{
												if(mapClassRedemptionDetails.get("PartialRedChargeSlabOrTierFrom")!= null && mapClassRedemptionDetails.get("PartialRedChargeSlabOrTierTo") != null && mapClassRedemptionDetails.get("PartialRedSlabOrTierCharge") != null){
													bStatus = ClassAppFunctions.removeRateMethodElements("redemPartialAmountTieredSlabRate");
													if(!bStatus){
														return false;
													}
													bStatus = NewUICommonFunctions.addPartialRateMethodDetails(mapClassRedemptionDetails);
													if(!bStatus){
														Messages.errorMsg = "Partial Rate Method Details failed Add";
														return false;
													}
												}
											}
										}
									}
								}			
							}
						}
					}
				}
				if (mapClassRedemptionDetails.get("IsTxnChargesApplicable").equalsIgnoreCase("No")) 
				{
					String sIsTxnChargesApplicableNo = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isTransactionChargesApplying.replace("YesOrNoReplaceValue", "No");
					bStatus = Elements.click(Global.driver, By.xpath(sIsTxnChargesApplicableNo));
					if (!bStatus) 
					{
						Messages.errorMsg = "Radio button wasn't selected for IsTxnChargesApplicable" + sIsTxnChargesApplicableNo;
						return false;												
					}
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean doFillClassDetails(Map<String, String> mapClassDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.ClassDetails.Dropdown.clientNameClick, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Client Name Dropdown is not visible in Class Details Page";
				return false;
			}

			if(mapClassDetails.get("Side Pocket Class")!=null)
			{
				if(mapClassDetails.get("Side Pocket Class").equalsIgnoreCase("Yes"))
				{
					bStatus=Elements.click(Global.driver, Locators.ClassMaster.ClassDetails.RadioButton.isSidePocketClassYes);
					if(!bStatus)
					{
						Messages.errorMsg = "Side Pocket Investment YES radio button Not Selected";
						return false;
					}
					if(mapClassDetails.get("Side Pocket Management Fee")!=null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassDetails.get("Side Pocket Management Fee"), Locators.ClassMaster.ClassDetails.Dropdown.sidePocketManagementFeeClick);
						if(!bStatus){
							Messages.errorMsg = "Side Pocket Management Fee dropdown value  Not selected";
							return false;
						}
					}
					if(mapClassDetails.get("Side Pocket Administration Fee")!=null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassDetails.get("Side Pocket Administration Fee"), Locators.ClassMaster.ClassDetails.Dropdown.sidePocketAdministrationFeeClick);
						if(!bStatus){
							Messages.errorMsg = "Side Pocket Administration Fee dropdown value  Not selected";
							return false;
						}
					}
				}

				//Un Comment the Below code after Resolving the Auto Clear of Values when selected the Side Pocket Class Radio Buttons
				if(mapClassDetails.get("Side Pocket Class").equalsIgnoreCase("No"))
				{
					bStatus=Elements.click(Global.driver,  Locators.ClassMaster.ClassDetails.RadioButton.isSidePocketClassNo);
					if(!bStatus)
					{
						Messages.errorMsg = "Side Pocket Classt No radio button Not Selected";
						return false;
					}
				}
			}

			if(mapClassDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassDetails.get("Client Name"), Locators.ClassMaster.ClassDetails.Dropdown.clientNameClick);
				if(!bStatus){
					Messages.errorMsg = "Client Name dropdown value  Not selected";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.ClassDetails.Dropdown.fundFamilyNameClick, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Fund Family Dropdown Not Visible";
				}
			}
			if(mapClassDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassDetails.get("Fund Family Name"), Locators.ClassMaster.ClassDetails.Dropdown.fundFamilyNameClick);
				if(!bStatus){
					Messages.errorMsg = "Fund Family Name dropdown value  Not selected";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.ClassDetails.Dropdown.legalEntityNameClick, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Dropdown Not Visible";
				}
			}
			if(mapClassDetails.get("Legal Entity Name")!=null){

				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassDetails.get("Legal Entity Name"), Locators.ClassMaster.ClassDetails.Dropdown.legalEntityNameClick);
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Name dropdown value  Not selected";
					return false;
				}
			}
			if(mapClassDetails.get("Class Name")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.ClassDetails.TextBox.className, mapClassDetails.get("Class Name"));
				if(!bStatus){
					Messages.errorMsg = "Class Name  Not Entered";
					return false;
				}
			}
			if(mapClassDetails.get("Class Code")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.ClassDetails.TextBox.classShortName, mapClassDetails.get("Class Code"));
				if(!bStatus){
					Messages.errorMsg = "Class Code Not Entered";
					return false;
				}
			}
			if(mapClassDetails.get("Clone")!=null)
			{
				if(mapClassDetails.get("Clone").equalsIgnoreCase("Yes"))
				{
					bStatus=Elements.click(Global.driver, Locators.ClassMaster.ClassDetails.RadioButton.isCloneYes);
					if(!bStatus)
					{
						Messages.errorMsg = "Clone YES radio button Not Selected";
						return false;
					}
					if(mapClassDetails.get("Class Name for Clone")!=null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassDetails.get("Class Name for Clone"), Locators.ClassMaster.ClassDetails.Dropdown.classforCloneClick);
						if(!bStatus){
							Messages.errorMsg = "Class Name for Clone Not Selected";
							return false;
						}
					}
					if(mapClassDetails.get("Clone Button")!=null){
						if(mapClassDetails.get("Clone Button").equalsIgnoreCase("Yes")){
							bStatus = Elements.clickButton(Global.driver, Locators.ClassMaster.ClassDetails.Button.btnClone);
							if(!bStatus){
								Messages.errorMsg = "Clone Button Click was Failed";
								return false;
							}
						}
					}
					if(mapClassDetails.get("View Button")!=null){
						if(mapClassDetails.get("View Button").equalsIgnoreCase("Yes")){
							bStatus = Elements.clickButton(Global.driver, Locators.ClassMaster.ClassDetails.Button.btnView);
							if(!bStatus){
								Messages.errorMsg = "View Button Click was Failed";
								return false;
							}
						}
					}	
				}
				if(mapClassDetails.get("Clone").equalsIgnoreCase("No"))
				{
					bStatus=Elements.click(Global.driver,  Locators.ClassMaster.ClassDetails.RadioButton.isCloneNo);
					if(!bStatus)
					{
						Messages.errorMsg = "Clone No radio button Not Selected";
						return false;
					}
				}
			}
			if(mapClassDetails.get("Investment Strategy")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassDetails.get("Investment Strategy"), Locators.ClassMaster.ClassDetails.Dropdown.investmetnStratefyClick);
				if(!bStatus){
					Messages.errorMsg = "Investment Strategy dropdown value  Not selected";
					return false;
				}
			}
			if(mapClassDetails.get("Class Currency")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapClassDetails.get("Class Currency"), Locators.ClassMaster.ClassDetails.Dropdown.classCurrencyClick);
				if(!bStatus){
					Messages.errorMsg = "Class Currency dropdown value  Not selected";
					return false;
				}
			}


			if(mapClassDetails.get("External Id1")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.ClassMaster.ClassDetails.TextBox.externalId1, mapClassDetails.get("External Id1"));
				if(!bStatus){
					Messages.errorMsg = "External Id1 Entry Failed";
					return false;
				}

			}
			if(mapClassDetails.get("External Id2")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.ClassMaster.ClassDetails.TextBox.externalId2, mapClassDetails.get("External Id2"));
				if(!bStatus){
					Messages.errorMsg = "External Id2 Entry Failed";
					return false;
				}

			}
			if(mapClassDetails.get("External Id3")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.ClassMaster.ClassDetails.TextBox.externalId3, mapClassDetails.get("External Id3"));
				if(!bStatus){
					Messages.errorMsg = "External Id3 Entry Failed";
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
	public static boolean doFillClassGeneralDetails(Map<String, String> mapGeneralDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.GeneralDetails.Dropdown.unitizedDrpdwnClick, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Unitized Dropdown is not Visible";
				return false;
			}
			if(mapGeneralDetails.get("Unitized")!=null)
			{
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized"))
				{
					Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.Dropdown.unitizedDrpdwnClick);
					bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClassMaster.GeneralDetails.Dropdown.unitizedValue,Constants.iddTimeout);
					if(!bStatus){
						Messages.errorMsg = "Dropdown list is not visible";
						return false;
					}
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.Dropdown.unitizedValue);
					if(!bStatus){
						Messages.errorMsg = "Unitized ropdown Value Not Selected";
						return false;
					}
					if(mapGeneralDetails.get("Unit Description")!=null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Unit Description"), Locators.ClassMaster.GeneralDetails.Dropdown.unitDescription);
						if(!bStatus){
							Messages.errorMsg = "Unit Description dropdown value Not Selected";
							return false;
						}
					}
					if(mapGeneralDetails.get("Unit Value")!=null){
						bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.GeneralDetails.TextBox.unitValue, mapGeneralDetails.get("Unit Value"));
						if(!bStatus){
							Messages.errorMsg = "Unit Value Not Entered";
							return false;
						}
					}
					if(mapGeneralDetails.get("Voting Shares")!=null){
						if(mapGeneralDetails.get("Voting Shares").equalsIgnoreCase("Yes")){
							bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.votingSharesYes);
							if(!bStatus){
								Messages.errorMsg = "Voting Shares Yes Radio Button is Not Selected";
								return false;
							}
						}
						if(mapGeneralDetails.get("Voting Shares").equalsIgnoreCase("No")){
							bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.votingSharesNo);
							if(!bStatus){
								Messages.errorMsg = "Voting Shares No Radio Button is Not Selected";
								return false;
							}
						}
					}
					if(mapGeneralDetails.get("Issued In Series")!=null){
						if(mapGeneralDetails.get("Issued In Series").equalsIgnoreCase("Yes"))
						{
							bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.issuedInSeriesYes);
							if(!bStatus){
								Messages.errorMsg = "Issued In Series Yes Radio Button Not Selected";
								return false;
							}
						}
						if(mapGeneralDetails.get("Issued In Series").equalsIgnoreCase("No"))
						{
							bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.issuedInSeriesNo);
							if(!bStatus){
								Messages.errorMsg = "Issued In Series No Radio Button Not Selected";
								return false;
							}
						}
					}
				}
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Non Unitized")){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Unitized"), Locators.ClassMaster.GeneralDetails.Dropdown.unitizedDrpdwnClick);
					if(!bStatus){
						Messages.errorMsg = "Unitized Dropdown value Not Selected";
						return false;
					}
				}
			}
			if(mapGeneralDetails.get("Initial Offering Price")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.GeneralDetails.TextBox.initialOfferingPrice, mapGeneralDetails.get("Initial Offering Price"));
				if(!bStatus){
					Messages.errorMsg = "Initial Offering Price Not Entered";
					return false;
				}
			}
			if(mapGeneralDetails.get("Start Date")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.GeneralDetails.TextBox.startDate, mapGeneralDetails.get("Start Date"));
				if(!bStatus){
					Messages.errorMsg = "Start Date Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.commonLabel);
			}
			if(mapGeneralDetails.get("New Issue Eligible")!=null){
				if(mapGeneralDetails.get("New Issue Eligible").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.newIssueEligibleYes);
					if(!bStatus){
						Messages.errorMsg = "New Issue Eligible Yes Radio Button Not Selected";
						return false;
					}
				}
				if(mapGeneralDetails.get("New Issue Eligible").equalsIgnoreCase("No"))
				{
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.newIssueEligibleNo);
					if(!bStatus){
						Messages.errorMsg = "New Issue Eligible No Radio Button Not Selected";
						return false;
					}
				}
			}
			if(mapGeneralDetails.get("Series RollUp") != null){
				if(mapGeneralDetails.get("Series RollUp").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='seriesRollUpYes']//parent::span"));
					if(!bStatus){
						Messages.errorMsg = "Unable to click on the Series Rollup 'Yes' button";
						return false;
					}
					if(mapGeneralDetails.get("Series RollUp Frequency") != null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Series RollUp Frequency", mapGeneralDetails.get("Series RollUp Frequency"));
						if(!bStatus){
							Messages.errorMsg = "Unable select the Series Rollup Frequency dropdown"+Messages.errorMsg;
							return false;
						}
					}
				}
				if(mapGeneralDetails.get("Series RollUp").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='seriesRollUpNo']//parent::span"));
					if(!bStatus){
						Messages.errorMsg = "Unable to click on the Series Rollup 'No' button";
						return false;
					}
					if(mapGeneralDetails.get("Equalization") != null){
						if(mapGeneralDetails.get("Equalization").equalsIgnoreCase("Yes")){
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='equalizationYes']//parent::span"));
							if(!bStatus){
								Messages.errorMsg = "Unable to Click on Equalization 'Yes' Radio button";
								return false;
							}
							if(mapGeneralDetails.get("Equalization Method") != null){
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Equalization Method", mapGeneralDetails.get("Equalization Method"));
								if(!bStatus){
									Messages.errorMsg = "Unable select the Equalization Method dropdown "+Messages.errorMsg;
									return false;
								}
							}
						}
						if(mapGeneralDetails.get("Equalization").equalsIgnoreCase("No")){
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='equalizationNo']//parent::span"));
							if(!bStatus){
								Messages.errorMsg = "Unable to Click on Equalization 'No' Radio button";
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
	public static boolean doFillClassOtherDetails(Map<String, String> mapOtherDetails , Map<String , String> mapGeneralDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.OtherDetails.TextBox.shareDecimalsForCalculationsClass, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Share Decimals For Calculations Element Not Visible";
				return false;
			}
			if(mapOtherDetails.get("Share Decimals For Calculation")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.OtherDetails.TextBox.shareDecimalsForCalculationsClass, mapOtherDetails.get("Share Decimals For Calculation"));
				if(!bStatus){
					Messages.errorMsg = "Share Decimals For Calculations Not Enetered";
					return false;
				}
			}
			if(mapOtherDetails.get("Share Decimals For Display")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.OtherDetails.TextBox.shareDecimalsDisplayClass, mapOtherDetails.get("Share Decimals For Display"));
				if(!bStatus){
					Messages.errorMsg = "Share Decimals Display Not Enetered";
					return false;
				}
			}
			if(mapOtherDetails.get("NAV Decimals For Calculation")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.OtherDetails.TextBox.navDecimalsForCalculationClass, mapOtherDetails.get("NAV Decimals For Calculation"));
				if(!bStatus){
					Messages.errorMsg = "NAV Decimals For Calculation Not Enetered";
					return false;
				}
			}
			if(mapOtherDetails.get("NAV Decimal For Display")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.OtherDetails.TextBox.navDecimalsDisplayClass, mapOtherDetails.get("NAV Decimal For Display"));
				if(!bStatus){
					Messages.errorMsg = "NAV Decimal For Display Not Enetered";
					return false;
				}
			}
			if(mapGeneralDetails.get("Unitized") != null && mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized")){
				if(mapOtherDetails.get("Dividend Intended")!=null){
					if(mapOtherDetails.get("Dividend Intended").equalsIgnoreCase("Yes"))
					{
						bStatus = Elements.click(Global.driver, Locators.ClassMaster.OtherDetails.RadioButton.isDividendIntendedYes);
						if(!bStatus){
							Messages.errorMsg = "Dividend Intended Yes Radio Button Not Selected";
							return false;
						}
						if(mapOtherDetails.get("Dividend Frequency")!=null){
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.OtherDetails.DateBox.dividendFrequency, mapOtherDetails.get("Dividend Frequency"));
							if(!bStatus){
								Messages.errorMsg = "Dividend Frequency Not Enetered";
								return false;
							}
							bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.commonLabel);
						}
						if(mapOtherDetails.get("Dividend Base NAV Value")!=null){
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.OtherDetails.TextBox.dividendBaseNavValue, mapOtherDetails.get("Dividend Base NAV Value"));
							if(!bStatus){
								Messages.errorMsg = "Dividend Base NAV Value Not Enetered";
								return false;
							}
						}
						if(mapOtherDetails.get("Minimum Re-Investment Amount")!=null){
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.OtherDetails.TextBox.minimumReinvestmentAmount, mapOtherDetails.get("Minimum Re-Investment Amount"));
							if(!bStatus){
								Messages.errorMsg = "Minimum Re-Investment Amount Not Enetered";
								return false;
							}
						}
						if(mapOtherDetails.get("Dividend Determination Basis")!=null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Dividend Determination Basis"), Locators.ClassMaster.OtherDetails.Dropdown.dividendDeterminationBasisClick);
							if(!bStatus){
								Messages.errorMsg = "Dividend Determination Basis dropdown value Not Selected";
								return false;
							}
						}
						if(mapOtherDetails.get("Dividend Re-Investment Method")!=null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Dividend Re-Investment Method"), Locators.ClassMaster.OtherDetails.Dropdown.dividendReinvestmentMethodClick);
							if(!bStatus){
								Messages.errorMsg = "Dividend Re-Investment Method dropdown value Not Selected";
								return false;
							}
						}
						if(mapOtherDetails.get("Dividend Reference")!=null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Dividend Reference"), Locators.ClassMaster.OtherDetails.Dropdown.DividendReferenceClick);
							if(!bStatus){
								Messages.errorMsg = "Dividend Reference dropdown value Not Selected";
								return false;
							}
						}
					}
					if(mapOtherDetails.get("Dividend Intended").equalsIgnoreCase("No")){
						bStatus = Elements.click(Global.driver, Locators.ClassMaster.OtherDetails.RadioButton.isDividendIntendedNo);
						if(!bStatus){
							Messages.errorMsg = "Dividend Intended No Radio Button Not Selected";
							return false;
						}
					}
				}
			}
			if(mapOtherDetails.get("Trading Restriction Applicable")!=null){
				if(mapOtherDetails.get("Trading Restriction Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.OtherDetails.RadioButton.tradingRestriction_Yes);
					if(!bStatus)
					{
						Messages.errorMsg = "Trading Restriction Applicable Yes Radio Button Not Selected";
						return false;
					}
					if(mapOtherDetails.get("Transaction Type")!=null && mapOtherDetails.get("Date From")!=null && mapOtherDetails.get("To Date")!=null)
					{

						//Un Comment the below code after Resolving the "Transaction Type Not Editable after Removing the other Trading Restriction"					
						bStatus = ClassAppFunctions.removeTrades();
						if(!bStatus){
							return false;
						}
						bStatus = ClassAppFunctions.removeTradeDates();
						if(!bStatus){
							return false;
						}
						//Adding Trading Restrictions
						bStatus = NewUICommonFunctions.addTradingRestrictions(mapOtherDetails);
						if(!bStatus)
						{
							Messages.errorMsg = "Failed to Enter Trading Restrictions ";
							return false;
						}
					}
				}
				if(mapOtherDetails.get("Trading Restriction Applicable").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.OtherDetails.RadioButton.tradingRestriction_No);
					if(!bStatus){
						Messages.errorMsg = "Trading Restriction Applicable No Radio Button Not Selected";
						return false;
					}
				}
			}
			/*		if(mapOtherDetails.get("Geo Based Restriction For Investor")!=null){
				if(mapOtherDetails.get("Geo Based Restriction For Investor").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.OtherDetails.RadioButton.geoBasedRestictionForInvestorYes);
					if(!bStatus){
						Messages.errorMsg = "Geo Based Restriction For Investor Radio button Yes Not Selected";
						return false;
					}
					if(mapOtherDetails.get("Country Code")!=null && mapOtherDetails.get("Investor Restrictions Date From")!=null && mapOtherDetails.get("Investor Restrictions To Date")!=null){
						bStatus = ClassAppFunctions.addMoreInvestorRestrictions(mapOtherDetails.get("Country Code"), mapOtherDetails.get("Investor Restrictions Date From"), mapOtherDetails.get("Investor Restrictions To Date"));
						if(!bStatus){
							Messages.errorMsg = "Invester Restriction Details Failed to Add";
						}
					}
				}
				if(mapOtherDetails.get("Geo Based Restriction For Investor").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.OtherDetails.RadioButton.geoBasedRestictionForInvestorNo);
					if(!bStatus){
						Messages.errorMsg = "Geo Based Restriction For Investor Radio button No Not Selected";
						return false;
					}
				}
			}*/
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	public static boolean doFillClassSubscriptionDetails(Map<String, String> mapSubscriptionDetails , Map<String , String> mapGeneralDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.SubscriptionTab.DropDown.subscriptionFrequencyClick, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Subscription Frequency Dropdown Not Visible";
				return false;
			}
			if(mapSubscriptionDetails.get("Investment Permitted")!=null)
			{
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized"))
				{
					if(mapSubscriptionDetails.get("Investment Permitted").equalsIgnoreCase("Amount"))
					{
						bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedAmount);
						if(!bStatus)
						{
							Messages.errorMsg = "Amount Radio Button is Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Minimum Initial Amount")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minInitialSubAmount , mapSubscriptionDetails.get("Minimum Initial Amount"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Initial Amount Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Subsequent Amount")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minSubsequentAmount , mapSubscriptionDetails.get("Minimum Subsequent Amount"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Subsequent Amount Entry failed";
								return false;
							}	
						}
					}
					if(mapSubscriptionDetails.get("Investment Permitted").equalsIgnoreCase("Shares"))
					{
						bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedShares);
						if(!bStatus)
						{
							Messages.errorMsg = "Shares Radio Button is Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Minimum Initial Shares")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minInitialSubShares , mapSubscriptionDetails.get("Minimum Initial Shares"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Initial Shares Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Subsequent Shares")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minSubsequentSubShares , mapSubscriptionDetails.get("Minimum Subsequent Shares"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Subsequent Shares Entry failed";
								return false;
							}	
						}
					}
					if(mapSubscriptionDetails.get("Investment Permitted").equalsIgnoreCase("Both"))
					{
						bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedBoth);
						if(!bStatus)
						{
							Messages.errorMsg = "Both Radio Button is Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Minimum Initial Amount")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minInitialSubAmount , mapSubscriptionDetails.get("Minimum Initial Amount"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Initial Amount Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Subsequent Amount")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minSubsequentAmount , mapSubscriptionDetails.get("Minimum Subsequent Amount"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Subsequent Amount Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Initial Shares")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minInitialSubShares , mapSubscriptionDetails.get("Minimum Initial Shares"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Initial Shares Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Subsequent Shares")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minSubsequentSubShares , mapSubscriptionDetails.get("Minimum Subsequent Shares"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Subsequent Shares Entry failed";
								return false;
							}	
						}
					}	
				}
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Non Unitized"))
				{
					if(!mapSubscriptionDetails.get("Investment Permitted").equalsIgnoreCase("Amount")){
						Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedBoth);	
						bStatus=Verify.verifyChecked(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedBoth);
						if(bStatus){
							Messages.errorMsg = "Both Radio Button should not allow to click as General Details Selected as Non Unitized";
							return false;
						}
						Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedShares);
						bStatus=Verify.verifyChecked(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedShares);
						if(bStatus){
							Messages.errorMsg = "Shares Radio Button should not allow to click as General Details Selected as Non Unitized";
							return false;
						}
					}
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedAmount);
					if(mapSubscriptionDetails.get("Minimum Initial Amount")!=null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minInitialSubAmount , mapSubscriptionDetails.get("Minimum Initial Amount"));
						if(!bStatus)
						{
							Messages.errorMsg = "Minimum Initial Amount Entry failed";
							return false;
						}	
					}
					if(mapSubscriptionDetails.get("Minimum Subsequent Amount")!=null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.minSubsequentAmount , mapSubscriptionDetails.get("Minimum Subsequent Amount"));
						if(!bStatus)
						{
							Messages.errorMsg = "Minimum Subseqent Amount Entry failed";
							return false;
						}	
					}
				}
			}
			if(mapSubscriptionDetails.get("Subscription Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapSubscriptionDetails.get("Subscription Frequency"), Locators.ClassMaster.SubscriptionTab.DropDown.subscriptionFrequencyClick, Locators.ClassMaster.commonDropdownForYearlySelect);
				//	bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Subscription Frequency"), Locators.ClassMaster.SubscriptionTab.DropDown.subscriptionFrequencyClick);
				if(!bStatus){
					Messages.errorMsg = "Subscription Frequency Dropdown Value Not Selected";
					return false;
				}
			}
			if(mapSubscriptionDetails.get("Notice Period Applicable")!=null)
			{
				if(mapSubscriptionDetails.get("Notice Period Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.noticePeriodYes);
					if(!bStatus)
					{
						Messages.errorMsg = "Notice Period Applicable Yes Radio button not selected";
						return false;
					}
					if(mapSubscriptionDetails.get("Notice Period")!=null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.npNoticePeriod, mapSubscriptionDetails.get("Notice Period"));
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period not Entered";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Notice Period Day or Month or Year")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Notice Period Day or Month or Year"), Locators.ClassMaster.SubscriptionTab.DropDown.noticePeriodDayMonthsClick);
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period "+mapSubscriptionDetails.get("Notice Period Day or Month or Year")+" Not Selected";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Notice Period Calender Type")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Notice Period Calender Type"), Locators.ClassMaster.SubscriptionTab.DropDown.noticePeriodCalenderTypeClick);
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period Calender Type Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Notice Period Calender Type").equalsIgnoreCase("Business"))
						{
							if(mapSubscriptionDetails.get("Include Holidays")!=null)
							{
								if(mapSubscriptionDetails.get("Include Holidays").equalsIgnoreCase("Yes"))
								{
									bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.includeHolidaysYes);
									if(!bStatus)
									{
										Messages.errorMsg = "Include Holidays Radio Button Yes Not Selected";
										return false;
									}
								}
								if(mapSubscriptionDetails.get("Include Holidays").equalsIgnoreCase("No"))
								{
									bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.includeHolidaysNo);
									if(!bStatus)
									{
										Messages.errorMsg = "Include Holidays Radio Button No Not Selected";
										return false;
									}
								}
							}
						}
					}
					if(mapSubscriptionDetails.get("Notice Period Charges")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Notice Period Charges"), Locators.ClassMaster.SubscriptionTab.DropDown.noticePeriodCharges);
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period Charges value Not Selected from drop down";
							return false;
						}
						if(!mapSubscriptionDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapSubscriptionDetails.get("Amount or BPS or Percent")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.npAmountorBPSorPercent, mapSubscriptionDetails.get("Amount or BPS or Percent"));
							if(!bStatus)
							{
								Messages.errorMsg = "Amount or BPS or Percent is Not entered";
								return false;
							}
						}
					}
				}
				if(mapSubscriptionDetails.get("Notice Period Applicable").equalsIgnoreCase("No"))
				{
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.noticePeriodNo);
					if(!bStatus)
					{
						Messages.errorMsg = "Notice Period Applicable No Radio button not selected";
						return false;
					}
				}
			}
			if(mapSubscriptionDetails.get("Transaction Charges")!=null)
			{
				if(mapSubscriptionDetails.get("Transaction Charges").equalsIgnoreCase("No"))
				{
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.transactionChargesNo);
					if(!bStatus)
					{
						Messages.errorMsg = "Transaction Charges No Radio button not selected";
						return false;
					}
				}
				if(mapSubscriptionDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.ClassMaster.SubscriptionTab.RaioButtons.transactionChargesYes);
					if(!bStatus)
					{
						Messages.errorMsg = "Transaction Charges Yes Radio button not selected";
						return false;
					}
					if(mapSubscriptionDetails.get("Effective Date")!=null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.tcEffectiveDate, mapSubscriptionDetails.get("Effective Date"));
						if(!bStatus)
						{
							Messages.errorMsg = "Transaction Charges Effective Date Not Entered";
							return false;
						}
						bStatus = Elements.click(Global.driver, Locators.ClassMaster.GeneralDetails.RadioButton.commonLabel);
					}
					if(mapSubscriptionDetails.get("Charges Type")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Charges Type"), Locators.ClassMaster.SubscriptionTab.DropDown.chargesType);
						if(!bStatus)
						{
							Messages.errorMsg = "Transaction Charges Type Dropdown Value Not Selected";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Calculation Base")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Calculation Base"), Locators.ClassMaster.SubscriptionTab.DropDown.calculationBase);
						if(!bStatus)
						{
							Messages.errorMsg = "Transaction Charges Calculation Base Dropdown Value Not Selected";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Rate Method")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Rate Method"), Locators.ClassMaster.SubscriptionTab.DropDown.rateMethod);
						if(!bStatus)
						{
							Messages.errorMsg = "Transaction Charges Rate Method Dropdown Value Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							if(mapSubscriptionDetails.get("Fixed Fee Rate")!=null){
								bStatus = Elements.enterText(Global.driver, Locators.ClassMaster.SubscriptionTab.TextBox.tcFixedFeeRate, mapSubscriptionDetails.get("Fixed Fee Rate"));
								if(!bStatus)
								{
									Messages.errorMsg = "Transaction Charges Fixed Fee Rate Not Entered";
									return false;
								}
							}
						}
						if(mapSubscriptionDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapSubscriptionDetails.get("Rate Method").equalsIgnoreCase("Tiered"))
						{
							if(mapSubscriptionDetails.get("Amount From")!=null&&mapSubscriptionDetails.get("Amount To")!=null&&mapSubscriptionDetails.get("Charges")!=null){
								bStatus = ClassAppFunctions.removeTransactionCharges("tiredFee");
								if(!bStatus){
									return false;
								}
								bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapSubscriptionDetails, "subscription", "add");
								if(!bStatus){
									Messages.errorMsg = "Transaction Charges Rate Method Details Failed to Enter";
									return false;
								}
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
	public static boolean addNewClass(Map<String, Map<String, String>> objMapAllTabsClassCreationDetails){
		try {
			bStatus = ClassAppFunctions.doFillClassDetails(objMapAllTabsClassCreationDetails.get("ClassDetails"));
			if(!bStatus){
				Messages.errorMsg ="Class Details Not Entered.ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = NewUICommonFunctions.NavigateToTab("General Details");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To General Details Tab.ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = ClassAppFunctions.doFillClassGeneralDetails(objMapAllTabsClassCreationDetails.get("GeneralDetails"));
			if(!bStatus){
				Messages.errorMsg ="General Details Not Entered .ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = NewUICommonFunctions.NavigateToTab("Other Details");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To Other Details Tab.ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = ClassAppFunctions.doFillClassOtherDetails(objMapAllTabsClassCreationDetails.get("OtherDetails"), objMapAllTabsClassCreationDetails.get("GeneralDetails"));
			if(!bStatus){
				Messages.errorMsg ="Other Details Not Entered .ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = NewUICommonFunctions.NavigateToTab("Subscription");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To Subscription Tab.ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = ClassAppFunctions.doFillClassSubscriptionDetails(objMapAllTabsClassCreationDetails.get("SubscriptionDetails"), objMapAllTabsClassCreationDetails.get("GeneralDetails"));
			if(!bStatus){
				Messages.errorMsg ="Subscription Details Not Entered .ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = ClassAppFunctions.doFillClassRedemptionTab(objMapAllTabsClassCreationDetails.get("RedemptionDetails"));
			if(!bStatus){
				Messages.errorMsg ="Redemption Details Not Entered .ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = ClassAppFunctions.doFillClassTransferTab(objMapAllTabsClassCreationDetails.get("TransferDetails"));
			if(!bStatus){
				Messages.errorMsg ="Transfer Details Not Entered .ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = ClassAppFunctions.doFillClassSwitchTab(objMapAllTabsClassCreationDetails.get("SwitchDetails"));
			if(!bStatus){
				Messages.errorMsg ="Switch Details Not Entered .ERROR:"+Messages.errorMsg;
				return false;
			}
			bStatus = ClassAppFunctions.doFillClassExchangeTab(objMapAllTabsClassCreationDetails.get("ExchangeDetails"));
			if(!bStatus){
				Messages.errorMsg ="Exchange Details Not Entered .ERROR:"+Messages.errorMsg;
				return false;
			}

			if(objMapAllTabsClassCreationDetails.get("ClassDetails").get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Class", objMapAllTabsClassCreationDetails.get("ClassDetails").get("OperationType"));
				
				if(!bStatus){
					return false;
				}
				return true;
				/*if(objMapAllTabsClassCreationDetails.get("ClassDetails").get("OperationType").equalsIgnoreCase("Save")){
					Elements.clickButton(Global.driver, Locators.LegalEntityMaster.Button.btnSave);

					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
					if(!bStatus){
						Messages.errorMsg ="Class is Not Saved After click on Save Button";
						return false;

					}
					return true;
				}

				if(objMapAllTabsClassCreationDetails.get("ClassDetails").get("OperationType").equalsIgnoreCase("Approve")){
					Elements.clickButton(Global.driver, Locators.FundFamilyMaster.Button.btnApprove);

					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
					if(!bStatus){
						Messages.errorMsg ="Class is Not Approved After click on Save Button";
						return false;
					}
					return true;
				}

				if(objMapAllTabsClassCreationDetails.get("ClassDetails").get("OperationType").equalsIgnoreCase("Save As Draft")){
					Elements.clickButton(Global.driver,Locators.LegalEntityMaster.Button.btnSaveAsDraft);
					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
					if(!bStatus){
						Messages.errorMsg ="Class is Not Saved  After click on Save Draft Button";
						return false;
					}
					return true;
				}

				if(objMapAllTabsClassCreationDetails.get("ClassDetails").get("OperationType").equalsIgnoreCase("Cancel")){
					Elements.clickButton(Global.driver,Locators.LegalEntityMaster.Button.btnCancel);
					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSearchPanel, Constants.iSaveTime);
					if(!bStatus){
						Messages.errorMsg ="Data Entered is not cleared";
						return false;
					}
					return true;
				}*/
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addMoreInvestorRestrictions(String scountryCode,String sfromDate,String stoDate){
		try {
			List<String> aCountryCode = Arrays.asList(scountryCode.split(","));
			List<String> aFromDate = Arrays.asList(sfromDate.split(","));
			List<String> aToDate = Arrays.asList(stoDate.split(","));
			for(int i=0 ; i<aCountryCode.size() ; i++){
				if(!aCountryCode.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aCountryCode.get(i), By.xpath("//div[@id='s2id_cIR_"+i+"']//span[contains(@id,'select2-chosen')]"));
					if(!bStatus){
						Messages.errorMsg = "Country code "+i+" Not Entered";
						return false;
					}
				}
				if(!aFromDate.get(i).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.id("fromDateRange"+i+""), aFromDate.get(i));
					if(!bStatus){
						Messages.errorMsg = "From Date "+i+" of Investor Restrictions Not Entered";
					}
				}
				if(!aToDate.get(i).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.id("toDateRange0"+i+""), aToDate.get(i));
					if(!bStatus){
						Messages.errorMsg = "To Date "+i+" of Investor Restrictions Not Entered";
					}
				}
				if(i<aCountryCode.size()-1){
					bStatus = Elements.click(Global.driver, By.id("addMoreIRD"));
					if(!bStatus){
						Messages.errorMsg = "AddMore Investor Restrictions Not Clicked";
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

	//Removing Trading Restrictions
	/*public static boolean removeTradingRestrictions(String name ,String className){
		try {
			int sRemoveTradingRestrictions = Elements.getXpathCount(Global.driver, By.xpath("//span[@id='tradResLogLEinnerHTML']/input"));
			for(int j=2; j<=sRemoveTradingRestrictions ;j++){
				bStatus = Elements.click(Global.driver, By.xpath("//button[@name='"+name+"']//em[@class='"+className+"']["+j+"]"));
				if(!bStatus){
					Messages.errorMsg = "Trading Restrictions Not Removed";
					return false;
				}
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}*/


	public static boolean removeTrades(){
		try {
			//int sRemovecount = Elements.getXpathCount(Global.driver, By.xpath("//span[@id='tradResLogLEinnerHTML']/input")); 
			int sRemovecount = 0;
			try{
				sRemovecount = Global.driver.findElements(By.xpath("//span[@id='tradResLogLEinnerHTML']/input")).size();
			}
			catch(Exception e){
				return false;
			}
			//System.out.println(count.size());
			if(sRemovecount<2){
				return true;
			}
			for(int j=sRemovecount;j>=2;j--){
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//span[@id='tradResLogLEinnerHTML']/input["+j+"]/following-sibling::div[@class='form-group']//button[@id='removeTradingRestrictions']"));
				if(!bStatus){
					Messages.errorMsg = "Trades are Not Removed";
					return false;
				}
			}
			
			sRemovecount = Global.driver.findElements(By.xpath("//span[@id='tradResLogLEinnerHTML']/input")).size();
			if(sRemovecount>1){
				Messages.errorMsg ="Remove button not removed";
				return false;
			}
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	public static boolean removeTradeDates(){
		try {

			int sRemovecount = 0;
			try{
				sRemovecount = Global.driver.findElements(By.xpath("//div[@id='dateRangeAddMore_0']/div")).size();
			}
			catch(Exception e){
				return false;
			}
			//System.out.println(count.size());
			if(sRemovecount<2){
				return true;
			}
			for(int j=sRemovecount ; j>=2 ; j-- ){
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='dateRangeAddMore_0']/div["+j+"]//button"));
				if(!bStatus){
					Messages.errorMsg = "Dates are not removed From Trading Restrictions";
					return false;
				}
			}
			
			sRemovecount = Global.driver.findElements(By.xpath("//div[@id='dateRangeAddMore_0']/div")).size();
			if(sRemovecount>1){
				Messages.errorMsg = "Remove button Not removed";
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean removeTransactionCharges(String id){
		try {
			int elementCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@id='"+id+"']/div//button"));
			for(int i=elementCount ; i>1 ; i--){
				bStatus = Elements.click(Global.driver, By.xpath("//div[@id='"+id+"']/div["+i+"]//button"));
				if(!bStatus){
					Messages.errorMsg = "Transaction Charges Not Removed";
					return false;
				}
			}
			int RemoveelementCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@id='"+id+"']/div//button"));
			if(RemoveelementCount>1){
				Messages.errorMsg = "Remove button failed to remove";
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean removeRateMethodElements(String id){
		try {
			String sReomveButtonLocator = "//div[@id='"+id+"']/div[index]//button";
			int xpathCount =Elements.getXpathCount(Global.driver, By.xpath("//div[@id='"+id+"']/div//button"));
			for(int i=xpathCount; i>1 ; i--){
				bStatus = Elements.click(Global.driver, By.xpath(sReomveButtonLocator.replace("index", String.valueOf(i))));
				if(!bStatus){
					Messages.errorMsg = "Remove button not clicked";
					return false;
				}
			}
			xpathCount =Elements.getXpathCount(Global.driver, By.xpath("//div[@id='"+id+"']/div//button"));
			if(xpathCount>1){
				Messages.errorMsg = "Remove button Not Removed";
				return false;
			}
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean searchClassMaster(String sClassName, String sStatus, int iTime) {		
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//label[normalize-space(text())='Class Name']/following-sibling::div[1]//span[contains(@id,'select2-chosen')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Legal Entity HomePage is not displayed.";
				return false;
			}

			bStatus = NewUICommonFunctions.WaitUntilValueAvailableInDropDown(sClassName, By.xpath("//label[normalize-space(text())='Class Name']/following-sibling::div[1]//span[contains(@id,'select2-chosen')]"),iTime);
			if (!bStatus) {
				return false;
			}		

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(sStatus,Locators.FundFamilyMaster.Ddn.objStatusClick);
			if (!bStatus) {
				return false;
			}

			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
			if (!bStatus) {
				Messages.errorMsg = " Search opeartion Button Wasn't Visible";
				return false;
			}

			bStatus = NewUICommonFunctions.isSerachTableDisplayed(Locators.CommonLocators.MasterSearchTable.tableMasterSearch);

			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*public static boolean modifyClassDetails(Map<String, Map<String, String>> objClassModifyTabsMaps, String sClassName) {
		// TODO Auto-generated method stub
		try{

			bStatus = NewUICommonFunctions.searchInSearchPanel("Class Name",sClassName, "active",7); 
			if(!bStatus){
				Messages.errorMsg = "Class is Not active yet";
				return false;
			}

			Elements.click(Global.driver, By.xpath("//a[@data-original-title='Edit']"));

			bStatus = ClassAppFunctions.addNewClass(objClassModifyTabsMaps);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean verifyAllTabsInClassDetailsEditScreen(Map<String, Map<String, String>> objClassCreationTabsMaps) {
		try {	
			boolean bValidationStatus = true;

			//Verify Class Details tab
			bStatus = ClassAppFunctions.doVerifyClassDetailsOfClassDetailsTab(objClassCreationTabsMaps.get("ClassDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Class Details Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			//Verify General Details tab
			bStatus = ClassAppFunctions.doVerifyClassDetailsOfGeneralDetailsTab(objClassCreationTabsMaps.get("GeneralDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Class GeneralDetails Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyClassDetailsOfGeneralDetailsTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Other Details tab
			bStatus = ClassAppFunctions.doVerifyClassDetailsOfOtherDetailsTab(objClassCreationTabsMaps.get("OtherDetails"), objClassCreationTabsMaps.get("GeneralDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Class  OtherDetails Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyClassDetailsOfOtherDetailsTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Subscription Details tab

			bStatus = ClassAppFunctions.doVerifyClassDetailsOfSubscriptionTab(objClassCreationTabsMaps.get("SubscriptionDetails"), objClassCreationTabsMaps.get("GeneralDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Class Subscription Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyClassDetailsOfSubscriptionTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Redemption Details tab

			bStatus = ClassAppFunctions.doVerifyClassDetailsOfRedemptionTab(objClassCreationTabsMaps.get("RedemptionDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Class Redemption Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyClassDetailsOfRedemptionTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Transfer Details tab

			bStatus = ClassAppFunctions.doVerifyClassDetailsOfTransferTab(objClassCreationTabsMaps.get("TransferDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Class Transfer Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyClassDetailsOfTransferTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Switch Details tab

			bStatus = ClassAppFunctions.doVerifyClassDetailsOfSwitchTab(objClassCreationTabsMaps.get("SwitchDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Class Switch Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyClassDetailsOfSwitchTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Exchange Details tab

			bStatus = ClassAppFunctions.doVerifyClassDetailsOfExchangeTab(objClassCreationTabsMaps.get("ExchangeDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Class Exchange Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyClassDetailsOfExchangeTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			return bValidationStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean modifyReturnClassDetails(Map<String, Map<String, String>> objMapClassTabsModificationDetailsMaps, String sClassName) {
		try{

			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,sClassName,"");
			if(!bStatus){
				return false;
			}

			bStatus = ClassAppFunctions.addNewClass(objMapClassTabsModificationDetailsMaps);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*public static boolean verifyDataInClassEditScreen(Map<String, Map<String, String>> validateMap){
		try{
			System.out.println(validateMap.get("ClassDetails"));
			bStatus = NewUICommonFunctions.searchInSearchPanel("Class Name",validateMap.get("ClassDetails").get("Class Name"), "active", 5);
			if (!bStatus) {
				Reporting.logResults("Fail", "Search the active Class to edit and verify the pre populated details", Messages.errorMsg);
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@data-original-title='Edit']"));

			return verifyAllTabsInClassDetailsEditScreen(validateMap);

			//return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean verifyReturnDataInClassEditScreen(Map<String, Map<String, String>> validateMap){
		try{
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,validateMap.get("ClassDetails").get("Class Name"),"");
			if(!bStatus){
				return false;
			}

			bStatus = verifyAllTabsInClassDetailsEditScreen(validateMap);
			return bStatus;

			//return verifyAllTabsInClassDetailsEditScreen(validateMap);

			//return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*public static boolean deactivateClass(String sClassName) {
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel("Class Name",sClassName, "active", 5); 
			if(!bStatus){
				Messages.errorMsg = "Class is Not in active state yet : "+sClassName;
				return false;
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@data-original-title='Deactivate']"));

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Class cannot be deactivated";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	/*public static boolean activateClass(String sClassName) {
		try{

			bStatus = NewUICommonFunctions.searchInSearchPanel("",sClassName, "inactive",7);
			if(!bStatus){
				return false;
			}

			NewUICommonFunctions.selectNumberOfRecordsPerPageInSearchGrid("All",10);
			NewUICommonFunctions.sortTableByColumnName("sample_2", "Class ID", "descending");
			Wait.waitForElementPresence(Global.driver, By.xpath("//td[normalize-space(text())='"+sClassName+"']//..//a[@data-original-title='Activate']"), 5);
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//td[normalize-space(text())='"+sClassName+"']//..//a[@data-original-title='Activate']"));
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Class Name cannot be Activated";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean doVerifyClassDetailsOfClassDetailsTab(Map<String , String> mapClassDetails){	
		try {
			String appendMsg = "";
			boolean validStatus = true;
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab1']"));
			bStatus =Wait.waitForElementPresence(Global.driver, By.xpath("//a[contains(text(),'Class Details')]"), Constants.lTimeOut);
			if(!bStatus){
				return false;
			}
			if(mapClassDetails.get("Side Pocket Class")!=null){
				if(mapClassDetails.get("Side Pocket Class").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifySelectedRadioButton("Side Pocket Class", mapClassDetails.get("Side Pocket Class"));
					if(!bStatus){
						appendMsg = appendMsg+ " [ERROR : "+mapClassDetails.get("Side Pocket Class")+" : is Not Matched with the Actual Side Pocket Class Radio Button]";
						validStatus = false;
					}
					if(mapClassDetails.get("Side Pocket Management Fee")!=null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Side Pocket Management Fee", mapClassDetails.get("Side Pocket Management Fee"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Side Pocket Management Fee")+" : is Not Matched with the Actual Side Pocket Management Fee ]";
							validStatus = false;
						}
					}
					if(mapClassDetails.get("Side Pocket Administration Fee")!=null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Side Pocket Administration Fee", mapClassDetails.get("Side Pocket Administration Fee"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Side Pocket Administration Fee")+" : is Not Matched with the Actual Side Pocket Administration Fee ]";
							validStatus = false;
						}
					}
				}
				if(mapClassDetails.get("Side Pocket Class").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifySelectedRadioButton("Side Pocket Class", mapClassDetails.get("Side Pocket Class"));
					if(!bStatus){
						appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Side Pocket Class")+" :  is Not Matched with the Actual Side Pocket Class Radio Button ]";
						validStatus = false;
					}
				}
			}
			if(mapClassDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Client Name", mapClassDetails.get("Client Name"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Client Name")+" : is Not Matched with the Actual Client Name ]\n";
					validStatus = false;
				}
			}
			if(mapClassDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Fund Family Name", mapClassDetails.get("Fund Family Name"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Fund Family Name")+" : is Not Matched with the Actual Fund Family Name ]\n";
					validStatus = false;
				}
			}
			if(mapClassDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Legal Entity Name", mapClassDetails.get("Legal Entity Name"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Legal Entity Name")+" : is Not Matched with the Actual Legal Entity Name ]\n";
					validStatus = false;
				}
			}
			if(mapClassDetails.get("Class Code")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Class Code", mapClassDetails.get("Class Code"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Class Code")+" : is Not Matched with the Actual Class Code ]\n ";
					validStatus = false;
				}
			}
			if(mapClassDetails.get("Class Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Class Name", mapClassDetails.get("Class Name"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Class Name")+" : is Not Matched with the Actual Class Name ]\n ";
					validStatus = false;
				}
			}			
			if(mapClassDetails.get("Investment Strategy")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Investment Strategy", mapClassDetails.get("Investment Strategy"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Investment Strategy")+" : is Not Matched with the Actual Investment Strategy ]\n ";
					validStatus = false;
				}
			}
			if(mapClassDetails.get("Class Currency")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Class Currency", mapClassDetails.get("Class Currency"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("Class Currency")+" : is Not Matched with the Actual Class Currency ]\n";
					validStatus = false;
				}
			}
			if(mapClassDetails.get("External Id1")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id1", mapClassDetails.get("External Id1"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("External Id1")+" : is Not Matched with the Actual External Id1 Value ]\n";
					validStatus = false;
				}
			}	
			if(mapClassDetails.get("External Id2")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id2", mapClassDetails.get("External Id2"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("External Id2")+" : is Not Matched with the Actual External Id2 Value ]\n";
					validStatus = false;
				}
			}	
			if(mapClassDetails.get("External Id3")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id3", mapClassDetails.get("External Id3"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapClassDetails.get("External Id3")+" : is Not Matched with the Actual External Id3 Value ]\n";
					validStatus = false;
				}
			}
			Messages.errorMsg = appendMsg;
			return validStatus;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	public static boolean doVerifyClassDetailsOfGeneralDetailsTab(Map<String , String> mapGeneralDetails){
		try {
			String appendMsg = "";
			boolean validStatus = true;
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab2']"));
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.GeneralDetails.Dropdown.unitizedDrpdwnClick, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Unitized Dropdown is not Visible";
				return false;
			}
			if(mapGeneralDetails.get("Unitized")!=null){
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized")){
					bStatus = NewUICommonFunctions.verifyTextInDropDown("Unitized", mapGeneralDetails.get("Unitized"));
					if(!bStatus){
						appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Unitized")+" : is Not Matched with the Actual Unitized Dropdown Value ]\n";
						validStatus = false;
					}
					if(mapGeneralDetails.get("Unit Description")!=null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Unit Description", mapGeneralDetails.get("Unit Description"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Unit Description")+" : is  Not Matched with the Actual Unit Description Dropdown Value ]\n";
							validStatus = false;
						}
					}
					String actualUnitValue = Elements.getElementAttribute(Global.driver, By.id("unitValue"), "value");
					if(actualUnitValue != null && mapGeneralDetails.get("Unit Value")!=null){
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualUnitValue, mapGeneralDetails.get("Unit Value"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Unit Value")+" Unit Value Not Matched with the "+actualUnitValue+" ]";
							validStatus = false;
						}
					}
					if(mapGeneralDetails.get("Voting Shares")!=null){
						bStatus = NewUICommonFunctions.verifySelectedRadioButton("Voting Shares", mapGeneralDetails.get("Voting Shares"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Voting Shares")+" : is  Not Matched with the Actual Voting Shares Radio Dropdown Value ]\n";
							validStatus = false;
						}
					}
					if(mapGeneralDetails.get("Issued In Series")!=null){
						bStatus = NewUICommonFunctions.verifySelectedRadioButton("Issued In Series", mapGeneralDetails.get("Issued In Series"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Issued In Series")+" : is  Not Matched with the Actual Issued In Series Radio Dropdown Value ]\n";
							validStatus = false;
						}
					}
				}
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Non Unitized")){
					bStatus = NewUICommonFunctions.verifyTextInDropDown("Unitized", mapGeneralDetails.get("Unitized"));
					if(!bStatus){
						appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Unitized")+" : is Not Matched with the Actual Unitized Dropdown Value ]\n";
						validStatus = false;
					}
				}
			}
			if(mapGeneralDetails.get("Series RollUp") != null){
				bStatus = NewUICommonFunctions.verifySelectedRadioButton("Series RollUp", mapGeneralDetails.get("Series RollUp"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Series RollUp")+" : is  Not Matched with the Actual Series RollUp Radio Dropdown Value ]\n";
					validStatus = false;
				}
				if(mapGeneralDetails.get("Series RollUp").equalsIgnoreCase("Yes")){
					if(mapGeneralDetails.get("Series RollUp Frequency") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Series RollUp Frequency", mapGeneralDetails.get("Series RollUp Frequency"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Series RollUp Frequency")+" : is Not Matched with the Actual Series RollUp Frequency Dropdown Value ]\n";
							validStatus = false;
						}
					}
				}
				if(mapGeneralDetails.get("Series RollUp").equalsIgnoreCase("No")){
					if(mapGeneralDetails.get("Equalization") != null){
						bStatus = NewUICommonFunctions.verifySelectedRadioButton("Equalization", mapGeneralDetails.get("Equalization"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Equalization")+" : is  Not Matched with the Actual Equalization Method Radio Dropdown Value ]\n";
							validStatus = false;
						}
						if(mapGeneralDetails.get("Equalization").equalsIgnoreCase("Yes")){						
							if(mapGeneralDetails.get("Equalization Method") != null){
								bStatus = NewUICommonFunctions.verifyTextInDropDown("Equalization Method", mapGeneralDetails.get("Equalization Method"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Equalization Method")+" : is Not Matched with the Actual Equalization Method Dropdown Value ]\n";
									validStatus = false;
								}
							}
						}
					}
				}
			}
			
			if (mapGeneralDetails.get("Initial Offering Price") != null)
			{
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='initialOfferingPrice']"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("")) {
					appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Initial Offering Price")+" : is  Not Matched with the Actual Initial Offering Price ]\n";
					validStatus = false;
				}
				if (sValue != null && !sValue.equalsIgnoreCase("")) {
					sValue = sValue.replaceAll(",", "");
					bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sValue, mapGeneralDetails.get("Initial Offering Price"));
					if(!bStatus){
						appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Initial Offering Price")+" : is  Not Matched with the Actual Initial Offering Price ]\n";
						validStatus = false;
					}
				}
			}	
			
			/*String sactualInitialOffering = Elements.getElementAttribute(Global.driver, By.id("initialOfferingPrice"), "value");
			if(sactualInitialOffering != null && mapGeneralDetails.get("Initial Offering Price") != null){
				bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sactualInitialOffering, mapGeneralDetails.get("Initial Offering Price"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Initial Offering Price")+" : is  Not Matched with the Actual Initial Offering Price ]\n";
					validStatus = false;
				}
			}*/
			if(mapGeneralDetails.get("Start Date")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Start Date", mapGeneralDetails.get("Start Date"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("Start Date")+" Not Matched with the Actual Start Date ]\n";
					validStatus = false;
				}
			}
			if(mapGeneralDetails.get("New Issue Eligible")!=null){
				if(mapGeneralDetails.get("New Issue Eligible").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifySelectedRadioButton("New Issue Eligible", mapGeneralDetails.get("New Issue Eligible"));
					if(!bStatus){
						appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("New Issue Eligible")+" : is  Not Matched with the Actual New Issue Eligible Radio Button ]\n";
						validStatus = false;
					}
				}
				if(mapGeneralDetails.get("New Issue Eligible").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifySelectedRadioButton("New Issue Eligible", mapGeneralDetails.get("New Issue Eligible"));
					if(!bStatus){
						appendMsg = appendMsg+ "[ ERROR : "+mapGeneralDetails.get("New Issue Eligible")+" : is  Not Matched with the Actual New Issue Eligible Radio Button ]\n";
						validStatus = false;
					}
				}
			}
			Messages.errorMsg = appendMsg+"\n";
			return validStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyClassDetailsOfOtherDetailsTab(Map<String , String> mapOtherDetails , Map<String , String> mapGeneralDetails){
		try {
			String appendMsg = "";
			boolean validStatus = true;

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab3']"));
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.OtherDetails.TextBox.shareDecimalsForCalculationsClass, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Share Decimal for Calculation is not Visible";
				return false;
			}
			if(mapOtherDetails.get("Share Decimals For Calculation")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Share Decimals For Calculation", mapOtherDetails.get("Share Decimals For Calculation"));
				if(!bStatus){
					appendMsg = appendMsg +"[ ERROR : "+mapOtherDetails.get("Share Decimals For Calculation")+" : is  Not match with the Actual Share Decimals For Calculation Value ]\n";
					validStatus = false;
				}
			}
			if(mapOtherDetails.get("Share Decimals For Display")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Share Decimals For Display", mapOtherDetails.get("Share Decimals For Display"));
				if(!bStatus){
					appendMsg = appendMsg +"[ ERROR : "+mapOtherDetails.get("Share Decimals For Display")+" : is  Not match with the Actual Share Decimals For Display Value ]\n";
					validStatus = false;
				}
			}
			if(mapOtherDetails.get("NAV Decimals For Calculation")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("NAV Decimals For Calculation", mapOtherDetails.get("NAV Decimals For Calculation"));
				if(!bStatus){
					appendMsg = appendMsg +"[ ERROR : "+mapOtherDetails.get("NAV Decimals For Calculation")+" : is  Not match with the Actual NAV Decimals For Calculation Value ]\n";
					validStatus = false;
				}
			}
			if(mapOtherDetails.get("NAV Decimal For Display")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("NAV Decimal For Display", mapOtherDetails.get("NAV Decimal For Display"));
				if(!bStatus){
					appendMsg = appendMsg +"[ ERROR : "+mapOtherDetails.get("NAV Decimal For Display")+" : is  Not match with the Actual NAV Decimal For Display Value ]\n";
					validStatus = false;
				}
			}
			if(mapGeneralDetails.get("Unitized")!=null){
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized")){
					if(mapOtherDetails.get("Dividend Intended")!=null){
						bStatus = NewUICommonFunctions.verifySelectedRadioButton("Dividend Intended", mapOtherDetails.get("Dividend Intended"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+" : is  Dividend Intended Radio Button "+mapOtherDetails.get("Dividend Intended")+" Not Matched with the Expected ]\n";
							validStatus = false;
						}
						if(mapOtherDetails.get("Dividend Intended").equalsIgnoreCase("Yes")){
							if(mapOtherDetails.get("Dividend Frequency")!=null){
								bStatus = NewUICommonFunctions.verifyTextInTextBox("Dividend Frequency", mapOtherDetails.get("Dividend Frequency"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapOtherDetails.get("Dividend Frequency")+" : is Not Matched with the Actual Dividend Frequency value ]\n";
									validStatus = false;
								}
							}
							String sActualDBNavValue = Elements.getElementAttribute(Global.driver, By.id("dividendList0.dividendBaseNavValue"), "value");
							if(sActualDBNavValue != null && mapOtherDetails.get("Dividend Base NAV Value")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sActualDBNavValue, mapOtherDetails.get("Dividend Base NAV Value"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapOtherDetails.get("Dividend Base NAV Value")+" : is Not Matched with the Actual Dividend Base NAV Value ]\n";
									validStatus = false;
								}
							}
							String sActualMinReInv = Elements.getElementAttribute(Global.driver, By.id("dividendList0.minimumReinvestmentAmount"), "value");
							if(sActualMinReInv != null && mapOtherDetails.get("Minimum Re-Investment Amount")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sActualMinReInv, mapOtherDetails.get("Minimum Re-Investment Amount"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapOtherDetails.get("Minimum Re-Investment Amount")+" : is Not Matched with the Actual Minimum Re-Investment Amount value ]\n";
									validStatus = false;
								}
							}
							if(mapOtherDetails.get("Dividend Determination Basis")!=null){
								bStatus = NewUICommonFunctions.verifyTextInDropDown("Dividend Determination Basis", mapOtherDetails.get("Dividend Determination Basis"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapOtherDetails.get("Dividend Determination Basis")+" : is Not Matched with the Actual Dividend Determination Basis value ]\n";
									validStatus = false;
								}
							}
							if(mapOtherDetails.get("Dividend Re-Investment Method")!=null){
								bStatus = NewUICommonFunctions.verifyTextInDropDown("Dividend Re-Investment Method", mapOtherDetails.get("Dividend Re-Investment Method"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapOtherDetails.get("Dividend Re-Investment Method")+" : is Not Matched with the Actual Dividend Re-Investment Method value ]\n";
									validStatus = false;
								}
							}
							if(mapOtherDetails.get("Dividend Reference")!=null){
								bStatus = NewUICommonFunctions.verifyTextInDropDown("Dividend Reference", mapOtherDetails.get("Dividend Reference"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapOtherDetails.get("Dividend Reference")+" : is Not Matched with the Actual Dividend Reference value ]\n";
									validStatus = false;
								}
							}
						}
					}
				}
			}
			if(mapOtherDetails.get("Trading Restriction Applicable")!=null){
				bStatus = NewUICommonFunctions.verifySelectedRadioButton("Trading Restriction Applicable", mapOtherDetails.get("Trading Restriction Applicable"));
				if(!bStatus){
					appendMsg = appendMsg +"[ ERROR : "+mapOtherDetails.get("Trading Restriction Applicable")+" : is  Not match with the Actual Trading Restriction Applicable Radio Button ]\n";
					validStatus = false;
				}
				if(mapOtherDetails.get("Trading Restriction Applicable").equalsIgnoreCase("Yes")){
					if(mapOtherDetails.get("Transaction Type")!= null && mapOtherDetails.get("Comments") !=null ){
						bStatus = verifyTradingRestrictions(mapOtherDetails);
						if(!bStatus){
							appendMsg = appendMsg+"[ ERROR:"+" Trading Restrictions Verification Failed. "+Messages.errorMsg+" ]\n";
							validStatus = false;
						}
					}				
				}
			}
			/*	if(mapOtherDetails.get("Geo Based Restriction For Investor")!=null){
				bStatus = NewUICommonFunctions.verifySelectedRadioButton("Geo Based Restriction For Investor", mapOtherDetails.get("Geo Based Restriction For Investor"));
				if(!bStatus){
					appendMsg = appendMsg+ "[ ERROR : "+" : is Geo Based Restriction For Investor Expected Radio Button"+mapOtherDetails.get("Geo Based Restriction For Investor")+"  Not Checked ]\n";
					validStatus = false;
				}
				if(mapOtherDetails.get("Geo Based Restriction For Investor").equalsIgnoreCase("Yes")){
					if(mapOtherDetails.get("Country Code")!=null && mapOtherDetails.get("Investor Restrictions Date From")!=null && mapOtherDetails.get("Investor Restrictions To Date")!=null){
						bStatus = verifyInvestorRestrictionDetails(mapOtherDetails);
						if(!bStatus){
							appendMsg = appendMsg+"\n"+"[ Investor Restriction Details Not Matched with the Expected Values. ERROR:"+Messages.errorMsg+" ]\n";
							validStatus = false;
						}
					}
				}			
			}*/
			Messages.errorMsg = appendMsg+ "\n";
			return validStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyClassDetailsOfSubscriptionTab(Map<String , String> mapSubScriptionDetails , Map<String , String> mapGeneralDetails){
		try {
			String appendMsg = "";
			boolean validStatus = true;		
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab4']"));
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClassMaster.SubscriptionTab.DropDown.subscriptionFrequencyClick , Constants.lTimeOut);
			if(!bStatus){
				appendMsg = "Subscription Frequency Dropdown not Visible";
				return false;
			}
			if(mapGeneralDetails.get("Unitized")!=null)
			{
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized"))
				{
					if(mapSubScriptionDetails.get("Investment Permitted")!=null)
					{
						bStatus = NewUICommonFunctions.verifySelectedRadioButton("Investment Permitted", mapSubScriptionDetails.get("Investment Permitted"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+ mapSubScriptionDetails.get("Investment Permitted")+" : is Investment Permitted value Not Matched with the Expected Value ]\n";
							validStatus = false;
						}
						if(mapSubScriptionDetails.get("Investment Permitted").equalsIgnoreCase("Amount"))
						{
							String actualminInitialSubAmount = Elements.getElementAttribute(Global.driver, By.id("minInitialSubAmount"), "value");
							if(actualminInitialSubAmount!=null&&mapSubScriptionDetails.get("Minimum Initial Amount")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminInitialSubAmount, mapSubScriptionDetails.get("Minimum Initial Amount"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Initial Amount")+" : is  Minimum Initial Amount Not Matched with the "+actualminInitialSubAmount+" ]\n";
									validStatus = false;
								}
							}
							String actualminSubsequentSubAmount = Elements.getElementAttribute(Global.driver, By.id("minSubsequentSubAmount"), "value");
							if(actualminSubsequentSubAmount!=null&&mapSubScriptionDetails.get("Minimum Subsequent Amount")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminSubsequentSubAmount, mapSubScriptionDetails.get("Minimum Subsequent Amount"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Subsequent Amount")+" : is  Minimum Subsequent Amount Not Matched with the "+actualminSubsequentSubAmount+" ]\n";
									validStatus = false;
								}
							}
						}
						if(mapSubScriptionDetails.get("Investment Permitted").equalsIgnoreCase("Shares"))
						{
							String actualminInitialSubShares = Elements.getElementAttribute(Global.driver, By.id("minInitialSubShares"), "value");
							if(actualminInitialSubShares!=null&&mapSubScriptionDetails.get("Minimum Initial Shares")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminInitialSubShares, mapSubScriptionDetails.get("Minimum Initial Shares"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Initial Shares")+" : is  Minimum Initial Shares Not Matched with the "+actualminInitialSubShares+" ]\n";
									validStatus = false;
								}
							}
							String actualminSubsequentSubShares = Elements.getElementAttribute(Global.driver, By.id("minSubsequentSubShares"), "value");
							if(actualminSubsequentSubShares!=null&&mapSubScriptionDetails.get("Minimum Subsequent Shares")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminSubsequentSubShares, mapSubScriptionDetails.get("Minimum Subsequent Shares"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Subsequent Shares")+" : is  Minimum Subsequent Shares Not Matched with the "+actualminSubsequentSubShares+" ]\n";
									validStatus = false;
								}
							}
						}
						if(mapSubScriptionDetails.get("Investment Permitted").equalsIgnoreCase("Both"))
						{
							String actualminInitialSubAmount = Elements.getElementAttribute(Global.driver, By.id("minInitialSubAmount"), "value");
							if(actualminInitialSubAmount!=null&&mapSubScriptionDetails.get("Minimum Initial Amount")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminInitialSubAmount, mapSubScriptionDetails.get("Minimum Initial Amount"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Initial Amount")+" : is  Minimum Initial Amount Not Matched with the "+actualminInitialSubAmount+" ]\n";
									validStatus = false;
								}
							}
							String actualminSubsequentSubAmount = Elements.getElementAttribute(Global.driver, By.id("minSubsequentSubAmount"), "value");
							if(actualminSubsequentSubAmount!=null&&mapSubScriptionDetails.get("Minimum Subsequent Amount")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminSubsequentSubAmount, mapSubScriptionDetails.get("Minimum Subsequent Amount"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Subsequent Amount")+" : is  Minimum Subsequent Amount Not Matched with the "+actualminSubsequentSubAmount+" ]\n";
									validStatus = false;
								}
							}
							String actualminInitialSubShares = Elements.getElementAttribute(Global.driver, By.id("minInitialSubShares"), "value");
							if(actualminInitialSubShares!=null&&mapSubScriptionDetails.get("Minimum Initial Shares")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminInitialSubShares, mapSubScriptionDetails.get("Minimum Initial Shares"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Initial Shares")+" : is  Minimum Initial Shares Not Matched with the "+actualminInitialSubShares+" ]\n";
									validStatus = false;
								}
							}
							String actualminSubsequentSubShares = Elements.getElementAttribute(Global.driver, By.id("minSubsequentSubShares"), "value");
							if(actualminSubsequentSubShares!=null&&mapSubScriptionDetails.get("Minimum Subsequent Shares")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminSubsequentSubShares, mapSubScriptionDetails.get("Minimum Subsequent Shares"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Subsequent Shares")+" : is  Minimum Subsequent Shares Not Matched with the "+actualminSubsequentSubShares+" ]\n";
									validStatus = false;
								}
							}
						}
					}
				}
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Non Unitized"))
				{
					if(mapSubScriptionDetails.get("Investment Permitted")!=null)
					{
						bStatus = NewUICommonFunctions.verifySelectedRadioButton("Investment Permitted", mapSubScriptionDetails.get("Investment Permitted"));
						if(!bStatus){
							appendMsg = appendMsg+ "[ ERROR : "+ mapSubScriptionDetails.get("Investment Permitted")+" : is Investment Permitted value Not Matched with the Expected Value ]\n";
							validStatus = false;
						}
						if(mapSubScriptionDetails.get("Investment Permitted").equalsIgnoreCase("Amount"))
						{
							String actualminInitialSubAmount = Elements.getElementAttribute(Global.driver, By.id("minInitialSubAmount"), "value");
							if(actualminInitialSubAmount!=null&&mapSubScriptionDetails.get("Minimum Initial Amount")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminInitialSubAmount, mapSubScriptionDetails.get("Minimum Initial Amount"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Initial Amount")+" : is  Minimum Initial Amount Not Matched with the "+actualminInitialSubAmount+" ]\n";
									validStatus = false;
								}
							}
							String actualminSubsequentSubAmount = Elements.getElementAttribute(Global.driver, By.id("minSubsequentSubAmount"), "value");
							if(actualminSubsequentSubAmount!=null&&mapSubScriptionDetails.get("Minimum Subsequent Amount")!=null){
								bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualminSubsequentSubAmount, mapSubScriptionDetails.get("Minimum Subsequent Amount"));
								if(!bStatus){
									appendMsg = appendMsg+ "[ ERROR : "+mapSubScriptionDetails.get("Minimum Subsequent Amount")+" : is  Minimum Subsequent Amount Not Matched with the "+actualminSubsequentSubAmount+" ]\n";
									validStatus = false;
								}
							}
						}
					}
				}
			}
			if(mapSubScriptionDetails.get("Subscription Frequency") != null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Subscription Frequency", mapSubScriptionDetails.get("Subscription Frequency"));
				if(!bStatus){
					appendMsg = appendMsg + "[ ERROR  : " +" : is Given Subscription Frequency i.e. "+mapSubScriptionDetails.get("Subscription Frequency")+" is not matching with the actual ] \n";
					validStatus = false;
				}
			}
			if(mapSubScriptionDetails.get("Notice Period Applicable")!=null){
				if(mapSubScriptionDetails .get("Notice Period Applicable").equalsIgnoreCase("YES")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsSubscriptionNoticePeriodYes']//parent::span[@class='checked']"));
					if (!bStatus) {
						appendMsg = appendMsg + "[ ERROR  : " +"Given Notice Period Applicable i.e. "+mapSubScriptionDetails.get("Notice Period Applicable")+" is not matching with the actual  ]\n";
						validStatus = false;
					}
					if(mapSubScriptionDetails.get("Notice Period")!=null){	
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='subscriptionNoticePeriodList0.period' and @value='"+mapSubScriptionDetails.get("Notice Period")+"']"));
						if(!bStatus){
							appendMsg = appendMsg + "[ ERROR  : " +"Given Notice Period i.e. "+mapSubScriptionDetails.get("Notice Period")+"  : is  not matching with the actual  ]\n";
							validStatus = false;
						}
					}
					if(mapSubScriptionDetails.get("Notice Period Day or Month or Year")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_subscriptionNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSubScriptionDetails.get("Notice Period Day or Month or Year")+"')]"));
						if(!bStatus){
							appendMsg = appendMsg + "[ ERROR  : " +"Given Notice Month i.e. "+mapSubScriptionDetails.get("Notice Period Day or Month or Year")+" is not matching with the actual  ]\n";
							validStatus = false;
						}
					}
					if(mapSubScriptionDetails.get("Notice Period Calender Type")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_Notice_Period_SubscriptionsBusinessCalendar']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSubScriptionDetails.get("Notice Period Calender Type")+"')]"));
						if(!bStatus){
							appendMsg = appendMsg + "[ ERROR  : " +"Given NoticePeriodType i.e. "+mapSubScriptionDetails.get("Notice Period Calender Type")+" is not matching with the actual  ]\n";
							validStatus = false;
						}
						if(mapSubScriptionDetails.get("Include Holidays")!=null && mapSubScriptionDetails.get("Notice Period Calender Type").equalsIgnoreCase("Business"))
						{
							if(mapSubScriptionDetails.get("Include Holidays").equalsIgnoreCase("Yes")){
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='isHolidaysIncludeSubscription']//label//div[@id='uniform-subscriptionNoticePeriodList0.isHolidaysInclude1']//span[@class='checked']"));
								if(!bStatus){
									appendMsg = appendMsg + "[ ERROR  : " +"Given Include Holidays i.e. "+mapSubScriptionDetails.get("Include Holidays")+"  : is  not matching with the actual  ]\n";
									validStatus = false;
								}
							}
							if(mapSubScriptionDetails.get("Include Holidays").equalsIgnoreCase("No")){
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='isHolidaysIncludeSubscription']//label//div[@id='uniform-subscriptionNoticePeriodList0.isHolidaysInclude2']//span[@class='checked']"));
								if(!bStatus){
									appendMsg = appendMsg + "[ ERROR  : " +"Given Include Holidays i.e. "+mapSubScriptionDetails.get("Include Holidays")+"  : is  not matching with the actual  ]\n";
									validStatus = false;
								}
							}
						}
					}
					if(mapSubScriptionDetails.get("Notice Period Charges")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodTypeSUB']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSubScriptionDetails.get("Notice Period Charges")+"')]"));
						if(!bStatus){
							appendMsg = appendMsg + "[ ERROR  : " +"Given Notice Period Charges i.e. "+mapSubScriptionDetails.get("Notice Period Charges")+"  : is  not matching with the actual  ]\n";
							validStatus = false;
						}
						if(mapSubScriptionDetails.get("Amount or BPS or Percent")!=null && !mapSubScriptionDetails.get("Notice Period Charges").equalsIgnoreCase("None")){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='subscriptionNoticePeriodList0.noticePeriodLockupDetailsList0.rate' and contains(@value,'"+mapSubScriptionDetails.get("Amount or BPS or Percent")+"')]"));
							if(!bStatus){
								appendMsg = appendMsg + "[ ERROR  : " +"Given Amount/BPS/Percent i.e. "+mapSubScriptionDetails.get("Amount or BPS or Percent")+"  : is  not matching with the actual  ]\n";
								validStatus = false;
							}
						}
					}				
				}
				if(mapSubScriptionDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsSubscriptionNoticePeriodNo']//parent::span[@class='checked']"));
					if (!bStatus) {
						appendMsg = appendMsg + "[ ERROR  : " +"Given Notice Period Applicable i.e. "+mapSubScriptionDetails.get("Notice Period Applicable")+"  : is  not matching with the actual ] \n";
						validStatus = false;
					}
				}
			}
			if (mapSubScriptionDetails.get("Transaction Charges") != null) 
			{
				if (mapSubScriptionDetails.get("Transaction Charges").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='Subscription_Charges_No']//parent::span[@class='checked']"));
					if(!bStatus ){
						appendMsg = appendMsg + "[ ERROR  : " +"Given IsTxnChargesApplicable i.e. "+mapSubScriptionDetails.get("Transaction Charges")+"  : is  not matching with actual  ]\n";
						validStatus = false;
					}
				}
				if (mapSubScriptionDetails.get("Transaction Charges").equalsIgnoreCase("Yes")) 
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='Subscription_Charges_Yes']//parent::span[@class='checked']"));
					if(!bStatus ){
						appendMsg = appendMsg + "[ ERROR  : " +"Given IsTxnChargesApplicable i.e. "+mapSubScriptionDetails.get("Transaction Charges")+"  : is  not matching with actual ] \n";
						validStatus = false;
					}
					if (mapSubScriptionDetails.get("Effective Date") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='subscriptionChargesList0.wef' and contains(@value,'"+mapSubScriptionDetails.get("Effective Date")+"')]"));
						if(!bStatus ){
							appendMsg = appendMsg + "[ ERROR  : " +"Given EffectiveDate i.e. "+mapSubScriptionDetails.get("Effective Date")+" is not matching with actual  ]\n";
							validStatus = false;
						}
					}
					if (mapSubScriptionDetails.get("Charges Type") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_subscriptionChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSubScriptionDetails.get("Charges Type")+"')]"));
						if(!bStatus ){
							appendMsg = appendMsg + "[ ERROR  : " +"Given TxnChrgesType i.e. "+mapSubScriptionDetails.get("Charges Type")+" is not matching with actual  ]\n";
							validStatus = false;
						}
					}
					if (mapSubScriptionDetails.get("Calculation Base") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_subscriptionChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSubScriptionDetails.get("Calculation Base")+"')]"));
						if(!bStatus ){
							appendMsg = appendMsg + "[ ERROR  : " +"Given TxnCalcBaseType i.e. "+mapSubScriptionDetails.get("Calculation Base")+" is not matching with actual  ]\n";
							validStatus = false;
						}
					}
					if (mapSubScriptionDetails.get("Rate Method") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_Rate_Method_SUB_NormalCharges']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSubScriptionDetails.get("Rate Method")+"')]"));
						if(!bStatus ){
							appendMsg = appendMsg + "[ ERROR  : " +"Given TxnRateMethod i.e. "+mapSubScriptionDetails.get("Rate Method")+" is not matching with actual  ]\n";
							validStatus = false;
						}
						if(!mapSubScriptionDetails.get("Rate Method").equalsIgnoreCase("None"))
						{
							if (mapSubScriptionDetails.get("Rate Method").equalsIgnoreCase("Fixed") && mapSubScriptionDetails.get("Fixed Fee Rate") != null) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='subscriptionChargesList0.chargeDetailsList0.fixedFeeRate' and contains(@value,'"+mapSubScriptionDetails.get("Fixed Fee Rate")+"')]"));
								if(!bStatus ){
									appendMsg = appendMsg + "[ ERROR  : " +"Given Fixed Fee Rate i.e. "+mapSubScriptionDetails.get("Fixed Fee Rate")+" is not matching with actual  ]\n";
									validStatus = false;
								}
							}
							if (mapSubScriptionDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapSubScriptionDetails.get("Rate Method").equalsIgnoreCase("Tiered")) 
							{
								if(mapSubScriptionDetails.get("Amount From") != null && mapSubScriptionDetails.get("Amount To") != null && mapSubScriptionDetails.get("Charges") != null)
								{
									String sFromAmountLocator = "subscriptionChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
									String sAmountToLocator = "subscriptionChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
									String sChargesLocator = "subscriptionChargesList0.chargeDetailsListReplaceIndexValue.rate";
									//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
									String sAmountFrom = mapSubScriptionDetails.get("Amount From");
									List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									String sAmountTo = mapSubScriptionDetails.get("Amount To");
									List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
									String sCharges = mapSubScriptionDetails.get("Charges");
									List<String> aCharges = Arrays.asList(sCharges.split(","));
									for(int i=0;i<aAmountFrom.size();i++){
										if(!aAmountFrom.get(i).equalsIgnoreCase("None"))
										{
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												appendMsg = appendMsg+ "[ ERROR : "+ "Amount From is Not matching with expected in "+i+" index"+" ]\n";
												validStatus = false;
											}
										}
										if(!aAmountTo.get(i).equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												appendMsg = appendMsg + "[ ERROR  : " +"Amount To is Not matching with expected in "+i+" index"+" ]\n";
												validStatus = false;
											}
										}
										if(!aCharges.get(i).equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												appendMsg = appendMsg + "[ ERROR  : " +"Charges is Not matching with expected in "+i+" index i.e. expected = "+aCharges.get(i).toString()+" ]\n";
												validStatus = false;
											}
										}		
									}
								}
							}
						}
					}
				}
			}
			Messages.errorMsg = appendMsg+ "\n";
			return validStatus;
		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyClassDetailsOfRedemptionTab(Map<String, String> mapRedemptionTabDetails) {
		try{
			String sAppendMessage = "";
			boolean bValidateStatus = true;

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab5']"));
			if(!bStatus)
			{
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtMinimumAmount);
			if(!bStatus)
			{
				return false;
			}
			if(mapRedemptionTabDetails.get("Minimum Amount") != null)
			{
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionList0.minRedemAmount' and contains(@value,'"+mapRedemptionTabDetails.get("Minimum Amount")+"')]"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Minimum Amount i.e. "+mapRedemptionTabDetails.get("Minimum Amount")+" is not matching with actual  ]\n";
					bValidateStatus = false;
				}
			}
			if(mapRedemptionTabDetails.get("Minimum Shares") != null)
			{
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionList0.minRedemShares' and contains(@value,'"+mapRedemptionTabDetails.get("Minimum Shares")+"')]"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Minimum Shares i.e. "+mapRedemptionTabDetails.get("Minimum Shares")+" is not matching with actual  ]\n";
					bValidateStatus = false;
				}
			}
			if(mapRedemptionTabDetails.get("Redemption Type") != null)
			{
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redemptionMethodRED']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("Redemption Type")+"')]"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Redemption Type i.e. "+mapRedemptionTabDetails.get("Redemption Type")+" is not matching with actual  ]\n";
					bValidateStatus = false;
				}
			}
			if(mapRedemptionTabDetails.get("Redemption Frequency") != null)
			{
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redemptionList0.redemptionFrequency.frequencyIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("Redemption Frequency")+"')]"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Redemption Frequency i.e. "+mapRedemptionTabDetails.get("Redemption Frequency")+" is not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			// Filling Out Notice Period Details		
			if(mapRedemptionTabDetails.get("IsNoticePeriodApplicable")!= null)
			{
				if(mapRedemptionTabDetails.get("IsNoticePeriodApplicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsRedemptionNoticePeriodYes']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Given IsNoticePeriodApplicable i.e. "+mapRedemptionTabDetails.get("IsNoticePeriodApplicable")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
					if(mapRedemptionTabDetails.get("NoticePeriodQuantity") != null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionNoticePeriodList0.period' and contains(@value,'"+mapRedemptionTabDetails.get("NoticePeriodQuantity")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given NoticePeriodQuantity i.e. "+mapRedemptionTabDetails.get("NoticePeriodQuantity")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapRedemptionTabDetails.get("NoticePeriodQuantityType") != null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redemptionNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("NoticePeriodQuantityType")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given NoticePeriodQuantityType i.e. "+mapRedemptionTabDetails.get("NoticePeriodQuantityType")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapRedemptionTabDetails.get("CalendarOrBusinessNoticeDays") != null)
					{
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Calender Type", mapRedemptionTabDetails.get("CalendarOrBusinessNoticeDays"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given CalendarOrBusinessNoticeDays i.e. "+mapRedemptionTabDetails.get("CalendarOrBusinessNoticeDays")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
						if(mapRedemptionTabDetails.get("CalendarOrBusinessNoticeDays").equalsIgnoreCase("Business") && mapRedemptionTabDetails.get("IncludeHolidays")!=null)
						{
							if(mapRedemptionTabDetails.get("IncludeHolidays").equalsIgnoreCase("Yes"))
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-redemptionNoticePeriodList0.isHolidaysInclude1']//span[@class='checked']"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given IncludeHolidays i.e. "+mapRedemptionTabDetails.get("IncludeHolidays")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}
							if(mapRedemptionTabDetails.get("IncludeHolidays").equalsIgnoreCase("No"))
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-redemptionNoticePeriodList0.isHolidaysInclude2']//span[@class='checked']"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given IncludeHolidays i.e. "+mapRedemptionTabDetails.get("IncludeHolidays")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}
						}
					}
					if (mapRedemptionTabDetails.get("NoticePeriodChargesType") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodTypeRED']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("NoticePeriodChargesType")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given NoticePeriodChargesType i.e. "+mapRedemptionTabDetails.get("NoticePeriodChargesType")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
						if (!mapRedemptionTabDetails.get("NoticePeriodChargesType").equalsIgnoreCase("None") && mapRedemptionTabDetails.get("ChargesAmountOrBPSOrPercent") != null)
						{
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionNoticePeriodList0.noticePeriodLockupDetailsList0.rate' and contains(@value,'"+mapRedemptionTabDetails.get("ChargesAmountOrBPSOrPercent")+"')]"));
							if(!bStatus ){
								sAppendMessage = sAppendMessage + "[ Given ChargesAmountOrBPSOrPercent i.e. "+mapRedemptionTabDetails.get("ChargesAmountOrBPSOrPercent")+" is not matching with actual ] \n";
								bValidateStatus = false;
							}
						}
					}
				}
				if(mapRedemptionTabDetails.get("IsNoticePeriodApplicable").equalsIgnoreCase("No"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsRedemptionNoticePeriodNo']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Given IsNoticePeriodApplicable i.e. "+mapRedemptionTabDetails.get("IsNoticePeriodApplicable")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
			}
			if (mapRedemptionTabDetails.get("IsLockupApplying") != null)
			{
				if (mapRedemptionTabDetails.get("IsLockupApplying").equalsIgnoreCase("Yes")) 
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rdIsHardLockupYes']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Given IsLockupApplying i.e. "+mapRedemptionTabDetails.get("IsLockupApplying")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
					if(mapRedemptionTabDetails.get("LockupPeriodQuantity") != null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionHardLockupList0.period' and contains(@value,'"+mapRedemptionTabDetails.get("LockupPeriodQuantity")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given LockupPeriodQuantity i.e. "+mapRedemptionTabDetails.get("LockupPeriodQuantity")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapRedemptionTabDetails.get("LockupPeriodQuantityType") != null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redemptionHardLockupList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("LockupPeriodQuantityType")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given LockupPeriodQuantityType i.e. "+mapRedemptionTabDetails.get("LockupPeriodQuantityType")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapRedemptionTabDetails.get("CalendarOrBusinessLockupDays") != null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_hardLockupRedBusCal']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("CalendarOrBusinessLockupDays")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given CalendarOrBusinessLockupDays i.e. "+mapRedemptionTabDetails.get("CalendarOrBusinessLockupDays")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
						if(mapRedemptionTabDetails.get("CalendarOrBusinessLockupDays").equalsIgnoreCase("Business") && mapRedemptionTabDetails.get("LockupIncludeHolidays")!=null)
						{
							if(mapRedemptionTabDetails.get("LockupIncludeHolidays").equalsIgnoreCase("Yes"))
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-redemptionHardLockupList0.isHolidaysInclude1']//input[@name='redemptionHardLockupList[0].isHolidaysInclude']//parent::span[@class='checked']"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given LockupIncludeHolidays i.e. "+mapRedemptionTabDetails.get("LockupIncludeHolidays")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}
							if(mapRedemptionTabDetails.get("LockupIncludeHolidays").equalsIgnoreCase("No"))
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-redemptionHardLockupList0.isHolidaysInclude2']//input[@name='redemptionHardLockupList[0].isHolidaysInclude']//parent::span[@class='checked']"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given LockupIncludeHolidays i.e. "+mapRedemptionTabDetails.get("LockupIncludeHolidays")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}
						}
					}
					if (mapRedemptionTabDetails.get("LockupChargesType") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_hardLockupChargesType']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("LockupChargesType")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given LockupChargesType i.e. "+mapRedemptionTabDetails.get("LockupRateMethod")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
					}
					if (mapRedemptionTabDetails.get("LockupRateMethod") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redChargesLockupRateMethodRED']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("LockupRateMethod")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given LockupRateMethod i.e. "+mapRedemptionTabDetails.get("LockupRateMethod")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
						if (!mapRedemptionTabDetails.get("LockupRateMethod").equalsIgnoreCase("None")) 
						{
							if (mapRedemptionTabDetails.get("LockupRateMethod").equalsIgnoreCase("Fixed") && mapRedemptionTabDetails.get("LockupFixedAmountOrPercent") != null) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionHardLockupList0.noticePeriodLockupDetailsList0.fixedFeeRate' and contains(@value,'"+mapRedemptionTabDetails.get("LockupFixedAmountOrPercent")+"')]"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given LockupFixedAmountOrPercent i.e. "+mapRedemptionTabDetails.get("LockupFixedAmountOrPercent")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}
							if (mapRedemptionTabDetails.get("LockupRateMethod").equalsIgnoreCase("Slab")) 
							{
								if(mapRedemptionTabDetails.get("LockupSlabFrom") != null && mapRedemptionTabDetails.get("LockupSlabTo") != null && mapRedemptionTabDetails.get("LockupSlabCharges") != null)
								{
									String sFromAmountLocator = "redemptionHardLockupList0.noticePeriodLockupDetailsListReplaceIndexValue.fromAmount";
									String sAmountToLocator = "redemptionHardLockupList0.noticePeriodLockupDetailsListReplaceIndexValue.toAmount";
									String sChargesLocator = "redemptionHardLockupList0.noticePeriodLockupDetailsListReplaceIndexValue.rate";
									String sAmountFrom, sAmountTo, sCharges;
									List<String> aAmountFrom = null, aAmountTo = null, aCharges = null;
									if(mapRedemptionTabDetails.get("LockupSlabFrom") != null){
										sAmountFrom = mapRedemptionTabDetails.get("LockupSlabFrom");
										aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									}
									if(mapRedemptionTabDetails.get("LockupSlabTo") != null){
										sAmountTo = mapRedemptionTabDetails.get("LockupSlabTo");
										aAmountTo = Arrays.asList(sAmountTo.split(","));
									}
									if(mapRedemptionTabDetails.get("LockupSlabCharges") != null){
										sCharges = mapRedemptionTabDetails.get("LockupSlabCharges");
										aCharges = Arrays.asList(sCharges.split(","));
									}
									for(int i=0;i<aAmountFrom.size();i++){
										if(mapRedemptionTabDetails.get("LockupSlabFrom") != null && !aAmountFrom.get(i).toString().equalsIgnoreCase("None")){
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage+"[ **ERROR : "+ "LockupSlabFrom Amount is Not matching with expected in "+i+" index"+" ]\n";
												bValidateStatus = false;
											}
										}
										if(mapRedemptionTabDetails.get("LockupSlabTo") != null && !aAmountTo.get(i).toString().equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ LockupSlabTo Amount is Not matching with expected in "+i+" index"+" ]\n";
												bValidateStatus = false;
											}
										}
										if(mapRedemptionTabDetails.get("LockupSlabCharges") != null && !aCharges.get(i).toString().equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ LockupSlabCharges is Not matching with expected in "+i+" index"+" ]\n";
												bValidateStatus = false;
											}
										}
									}							
								}
							}
						}
					}
				}
				if (mapRedemptionTabDetails.get("IsLockupApplying").equalsIgnoreCase("No")) 
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rdIsHardLockupNo']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Given IsLockupApplying i.e. "+mapRedemptionTabDetails.get("IsLockupApplying")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
			}
			if (mapRedemptionTabDetails.get("IsTxnChargesApplicable") != null) 
			{
				if (mapRedemptionTabDetails.get("IsTxnChargesApplicable").equalsIgnoreCase("Yes")) 
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsRedemptionChargeYes']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Given IsTxnChargesApplicable i.e. "+mapRedemptionTabDetails.get("IsTxnChargesApplicable")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
					if (mapRedemptionTabDetails.get("EffectiveDate") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionChargesList0.wef' and contains(@value,'"+mapRedemptionTabDetails.get("EffectiveDate")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given EffectiveDate i.e. "+mapRedemptionTabDetails.get("EffectiveDate")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
					}
					if (mapRedemptionTabDetails.get("TxnChrgesType") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redemptionChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("TxnChrgesType")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given TxnChrgesType i.e. "+mapRedemptionTabDetails.get("TxnChrgesType")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
					}
					if (mapRedemptionTabDetails.get("TxnCalcBaseType") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redemptionChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("TxnCalcBaseType")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given TxnCalcBaseType i.e. "+mapRedemptionTabDetails.get("TxnCalcBaseType")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
					}
					if (mapRedemptionTabDetails.get("TxnRateMethod") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redChargesRateMethodRED']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("TxnRateMethod")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given TxnRateMethod i.e. "+mapRedemptionTabDetails.get("TxnRateMethod")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
						if(!mapRedemptionTabDetails.get("TxnRateMethod").equalsIgnoreCase("None"))
						{
							if (mapRedemptionTabDetails.get("TxnRateMethod").equalsIgnoreCase("Fixed") && mapRedemptionTabDetails.get("TxnFixedFeeRate") != null) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionChargesList0.chargeDetailsList0.fixedFeeRate'and contains(@value,'"+mapRedemptionTabDetails.get("TxnFixedFeeRate")+"')]"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given TxnRateMethod i.e. "+mapRedemptionTabDetails.get("TxnRateMethod")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}
							if (mapRedemptionTabDetails.get("TxnRateMethod").equalsIgnoreCase("Slab") || mapRedemptionTabDetails.get("TxnRateMethod").equalsIgnoreCase("Tiered")) 
							{
								if(mapRedemptionTabDetails.get("Amount From") != null && mapRedemptionTabDetails.get("Amount To") != null && mapRedemptionTabDetails.get("Charges") != null)
								{
									String sFromAmountLocator = "redemptionChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
									String sAmountToLocator = "redemptionChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
									String sChargesLocator = "redemptionChargesList0.chargeDetailsListReplaceIndexValue.rate";
									String sAmountFrom, sAmountTo, sCharges;
									List<String> aAmountFrom = null, aAmountTo = null, aCharges = null;
									//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
									if(mapRedemptionTabDetails.get("Amount From") != null){
										sAmountFrom = mapRedemptionTabDetails.get("Amount From");
										aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									}
									if(mapRedemptionTabDetails.get("Amount To") != null){
										sAmountTo = mapRedemptionTabDetails.get("Amount To");
										aAmountTo = Arrays.asList(sAmountTo.split(","));
									}
									if(mapRedemptionTabDetails.get("Charges") != null){
										sCharges = mapRedemptionTabDetails.get("Charges");
										aCharges = Arrays.asList(sCharges.split(","));
									}
									for(int i=0;i<aAmountFrom.size();i++){
										if(mapRedemptionTabDetails.get("Amount From") != null && !aAmountFrom.get(i).toString().equalsIgnoreCase("None")){
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage+"[ **ERROR : "+ "TxnSlabOrTierRange Amount From is Not matching with expected in "+i+" index"+" ]\n";
												bValidateStatus = false;
											}
										}
										if(mapRedemptionTabDetails.get("Amount To") != null && !aAmountTo.get(i).toString().equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ TxnSlabOrTierRange Amount To is Not matching with expected in "+i+" index"+" ]\n";
												bValidateStatus = false;
											}
										}
										if(mapRedemptionTabDetails.get("Charges") != null && !aCharges.get(i).toString().equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ TxnSlabOrTierRange Charges is Not matching with expected in "+i+" index i.e. expected = "+aCharges.get(i).toString()+" ]\n";
												bValidateStatus = false;
											}
										}
									}
								}
							}
						}
					}
					if (mapRedemptionTabDetails.get("PartialRedChargesIsAsTxnCharges") != null) 
					{
						if (mapRedemptionTabDetails.get("PartialRedChargesIsAsTxnCharges").equalsIgnoreCase("Yes")) 
						{
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsPartialAmountRedemptionApplicableYes']//parent::span[@class='checked']"));
							if(!bStatus ){
								sAppendMessage = sAppendMessage + "[ Given PartialRedChargesIsAsTxnCharges i.e. "+mapRedemptionTabDetails.get("PartialRedChargesIsAsTxnCharges")+" is not matching with actual  ]\n";
								bValidateStatus = false;
							}
						}
						if (mapRedemptionTabDetails.get("PartialRedChargesIsAsTxnCharges").equalsIgnoreCase("No")) 
						{
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsPartialAmountRedemptionApplicableNo']//parent::span[@class='checked']"));
							if(!bStatus ){
								sAppendMessage = sAppendMessage + "[ Given PartialRedChargesIsAsTxnCharges i.e. "+mapRedemptionTabDetails.get("PartialRedChargesIsAsTxnCharges")+" is not matching with actual  ]\n";
								bValidateStatus = false;
							}
							if (mapRedemptionTabDetails.get("PartialRedChargesEffectiveDate") != null) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionPartialAmountList0.wef' and contains(@value,'"+mapRedemptionTabDetails.get("PartialRedChargesEffectiveDate")+"')]"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given PartialRedChargesIsAsTxnCharges i.e. "+mapRedemptionTabDetails.get("PartialRedChargesEffectiveDate")+" is not matching with actual  ]n";
									bValidateStatus = false;
								}
							}
							if (mapRedemptionTabDetails.get("PartialRedChargeCalcBase") != null) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redPartialChargesCalMethodRED']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("PartialRedChargeCalcBase")+"')]"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given PartialRedChargeCalcBase i.e. "+mapRedemptionTabDetails.get("PartialRedChargeCalcBase")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}										
							if (mapRedemptionTabDetails.get("PartialRedChargeRateMethod") != null) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redPartialChargesRateMethodRED']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("PartialRedChargeRateMethod")+"')]"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given PartialRedChargeRateMethod i.e. "+mapRedemptionTabDetails.get("PartialRedChargeRateMethod")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
								if(!mapRedemptionTabDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("None"))
								{
									if (mapRedemptionTabDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Fixed") && mapRedemptionTabDetails.get("PartRedFixedFeeRate") != null)
									{
										bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionPartialAmountList0.chargeDetailsList0.fixedFeeRate'and contains(@value,'"+mapRedemptionTabDetails.get("PartRedFixedFeeRate")+"')]"));
										if(!bStatus ){
											sAppendMessage = sAppendMessage + "[ Given FixedFeeRate i.e. "+mapRedemptionTabDetails.get("PartRedFixedFeeRate")+" is not matching with actual ] \n";
											bValidateStatus = false;
										}
									}
									if (mapRedemptionTabDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Slab") || mapRedemptionTabDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Tiered"))
									{
										if(mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierFrom") != null && mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierTo") != null && mapRedemptionTabDetails.get("PartialRedSlabOrTierCharge") != null)
										{														
											String sFromAmountLocator = "redemptionPartialAmountList0.chargeDetailsListReplaceIndexValue.fromAmount";
											String sAmountToLocator = "redemptionPartialAmountList0.chargeDetailsListReplaceIndexValue.toAmount";
											String sChargesLocator = "redemptionPartialAmountList0.chargeDetailsListReplaceIndexValue.rate";
											String sAmountFrom, sAmountTo, sCharges;
											List<String> aAmountFrom = null, aAmountTo = null, aCharges = null;

											if(mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierFrom") != null){
												sAmountFrom = mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierFrom");
												aAmountFrom = Arrays.asList(sAmountFrom.split(","));
											}
											if(mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierTo") != null){
												sAmountTo = mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierTo");
												aAmountTo = Arrays.asList(sAmountTo.split(","));
											}
											if(mapRedemptionTabDetails.get("PartialRedSlabOrTierCharge") != null){
												sCharges = mapRedemptionTabDetails.get("PartialRedSlabOrTierCharge");
												aCharges = Arrays.asList(sCharges.split(","));
											}
											for(int i=0;i<aAmountFrom.size();i++){
												if(mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierFrom") != null && !aAmountFrom.get(i).equalsIgnoreCase("None")){
													String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
													bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
													if(!bStatus){
														sAppendMessage = sAppendMessage+"[ **ERROR : "+ "PartialRedChargeSlabOrTier Amount From is Not matching with expected in "+i+" index"+" ]\n";
														bValidateStatus = false;
													}
												}
												if(mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierTo") != null && !aAmountTo.get(i).equalsIgnoreCase("None")){
													String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
													bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
													if(!bStatus){
														sAppendMessage = sAppendMessage + "[ PartialRedChargeSlabOrTier Amount To is Not matching with expected in "+i+" index"+" ]\n";
														bValidateStatus = false;
													}
												}
												if(mapRedemptionTabDetails.get("PartialRedSlabOrTierCharge") != null && !aCharges.get(i).equalsIgnoreCase("None")){
													String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
													bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
													if(!bStatus){
														sAppendMessage = sAppendMessage + "[ PartialRedSlabOrTier Charges is Not matching with expected in "+i+" index"+" ]\n";
														bValidateStatus = false;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				if (mapRedemptionTabDetails.get("IsTxnChargesApplicable").equalsIgnoreCase("No")) 
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsRedemptionChargeNo']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Given IsTxnChargesApplicable i.e. "+mapRedemptionTabDetails.get("IsTxnChargesApplicable")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
			}			
			Messages.errorMsg = sAppendMessage+"\n";
			return bValidateStatus;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyClassDetailsOfTransferTab(Map<String, String> mapTransferTabDetails) {
		try{
			String sAppendMessage = "";
			boolean bValidateStatus = true;

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab6']"));
			if(!bStatus){
				Messages .errorMsg ="Transfer Menu Tab is not Available";
				Assert.fail("Transfer Menu Tab is not Available");
				return false;
			}

			Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.Transfer.Label.lblswitchwait, Constants.lTimeOut);

			if(mapTransferTabDetails.get("Frequency") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_transferList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen') and text()='"+mapTransferTabDetails.get("Frequency").trim()+"']"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Frequency i.e. "+mapTransferTabDetails.get("Frequency")+" is not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			/*	if(mapTransferTabDetails .get("Change In UBO")!=null){
				if(mapTransferTabDetails.get("Change In UBO").equalsIgnoreCase("YES")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='transferList0.isChangeInUBO1']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Given Change In UBO i.e. "+mapTransferTabDetails.get("Change In UBO")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
				if(mapTransferTabDetails.get("Change In UBO").equalsIgnoreCase("NO")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='transferList0.isChangeInUBO2']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Given Change In UBO i.e. "+mapTransferTabDetails.get("Change In UBO")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
			}*/
			if(mapTransferTabDetails.get("Incentive Fee Crystallization")!=null){
				if(mapTransferTabDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='transferList0.isTransactionCrystallized1']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Incentive Fee Crystallization i.e. "+mapTransferTabDetails.get("Incentive Fee Crystallization")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
				if(mapTransferTabDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='transferList0.isTransactionCrystallized2']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Incentive Fee Crystallization i.e. "+mapTransferTabDetails.get("Incentive Fee Crystallization")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
			}
			if(mapTransferTabDetails.get("Notice Period Applicable")!=null){
				if(mapTransferTabDetails .get("Notice Period Applicable").equalsIgnoreCase("YES")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsTransferNoticePeriodYes']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Notice Period Applicable i.e. "+mapTransferTabDetails.get("Notice Period Applicable")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
					if(mapTransferTabDetails.get("Notice Period")!=null){	
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='transferNoticePeriodList0.period' and @value='"+mapTransferTabDetails.get("Notice Period")+"']"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given Notice Period i.e. "+mapTransferTabDetails.get("Notice Period")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapTransferTabDetails.get("NoticeMonth")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_transferNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapTransferTabDetails.get("NoticeMonth")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given Notice Month i.e. "+mapTransferTabDetails.get("NoticeMonth")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapTransferTabDetails.get("NoticePeriodType")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodTransferBusCal']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapTransferTabDetails.get("NoticePeriodType")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given NoticePeriodType i.e. "+mapTransferTabDetails.get("NoticePeriodType")+" is not matching with the actual  ]\n";
							bValidateStatus = false;
						}
						if(mapTransferTabDetails.get("NoticePeriodType").equalsIgnoreCase("Business") && mapTransferTabDetails.get("Include Holidays")!=null)
						{
							if(mapTransferTabDetails.get("Include Holidays").equalsIgnoreCase("YES")){
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='isHolidaysIncludeTransferNoticePeriod']//label//div[@id='uniform-rbIsHolidaysIncludeYes']//span[@class='checked']"));
								if(!bStatus){
									sAppendMessage = sAppendMessage + "[ Given Include Holidays i.e. "+mapTransferTabDetails.get("Include Holidays")+" is not matching with the actual ] \n";
									bValidateStatus = false;
								}
							}
							if(mapTransferTabDetails.get("Include Holidays").equalsIgnoreCase("NO")){
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='isHolidaysIncludeTransferNoticePeriod']//label//div[@id='uniform-rbIsHolidaysIncludeNo']//span[@class='checked']"));
								if(!bStatus){
									sAppendMessage = sAppendMessage + "[ Given Include Holidays i.e. "+mapTransferTabDetails.get("Include Holidays")+" is not matching with the actual ] \n";
									bValidateStatus = false;
								}
							}
						}
					}
					if(mapTransferTabDetails.get("Notice Period Charges")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodTypeTRANSFER']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapTransferTabDetails.get("Notice Period Charges")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "Given Notice Period Charges i.e. "+mapTransferTabDetails.get("Notice Period Charges")+" is not matching with the actual  \n";
							bValidateStatus = false;
						}
						if(!mapTransferTabDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapTransferTabDetails.get("Amount/BPS/Percent")!=null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='transferNoticePeriodList0.noticePeriodLockupDetailsList0.rate' and contains(@value,'"+mapTransferTabDetails.get("Amount/BPS/Percent")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Given Amount/BPS/Percent i.e. "+mapTransferTabDetails.get("Amount/BPS/Percent")+" is not matching with the actual ] \n";
								bValidateStatus = false;
							}
						}
					}	
				}
				if(mapTransferTabDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsTransferNoticePeriodNo']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Notice Period Applicable i.e. "+mapTransferTabDetails.get("Notice Period Applicable")+" is not matching with the actual  ]\n";
						bValidateStatus = false;
					}
				}
			}
			if(mapTransferTabDetails.get("Transaction Charges")!=null){
				if(mapTransferTabDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsTransferChargeYes']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Transaction Charges Applicable i.e. "+mapTransferTabDetails.get("Transaction Charges")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
					if(mapTransferTabDetails.get("Effective Date")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='transferChargesList0.wef' and contains(@value,'"+mapTransferTabDetails.get("Effective Date")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "Given Effective Date i.e. "+mapTransferTabDetails.get("Effective Date")+" is not matching with the actual  \n";
							bValidateStatus = false;
						}
					}
					if(mapTransferTabDetails.get("Charges Type")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_transferChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapTransferTabDetails.get("Charges Type")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Charges Type i.e. "+mapTransferTabDetails.get("Charges Type")+" is not matching with the actual  ]\n";
							bValidateStatus = false;
						}
					}
					if(mapTransferTabDetails.get("Calculation Base")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_transferChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapTransferTabDetails.get("Calculation Base")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Calculation Base i.e. "+mapTransferTabDetails.get("Calculation Base")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapTransferTabDetails.get("Rate Method")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_chargesRateMethodTRA']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapTransferTabDetails.get("Rate Method")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Rate Method i.e. "+mapTransferTabDetails.get("Rate Method")+" is not matching with the actual  ]\n";
							bValidateStatus = false;
						}
					}
					if(mapTransferTabDetails.get("Rate Method") != null && !mapTransferTabDetails.get("Rate Method").equalsIgnoreCase("None"))
					{
						if(mapTransferTabDetails.get("Fixed Fee Rate") != null && mapTransferTabDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.fixedFeeRate' and contains(@value,'"+mapTransferTabDetails.get("Fixed Fee Rate")+"')]"));
							if (!bStatus) {
								sAppendMessage = sAppendMessage + "[ Given Fixed Fee Rate i.e. "+mapTransferTabDetails.get("Fixed Fee Rate")+" is not matching with the actual ] \n";
								bValidateStatus = false;
							}
						}
						if (mapTransferTabDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapTransferTabDetails.get("Rate Method").equalsIgnoreCase("Tiered")) 
						{
							String sFromAmountLocator = "transferChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
							String sAmountToLocator = "transferChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
							String sChargesLocator = "transferChargesList0.chargeDetailsListReplaceIndexValue.rate";
							String sAmountFrom, sAmountTo, sCharges;
							List<String> aAmountFrom = null, aAmountTo = null, aCharges = null;

							if(mapTransferTabDetails.get("Amount From") != null){
								sAmountFrom = mapTransferTabDetails.get("Amount From");
								aAmountFrom = Arrays.asList(sAmountFrom.split(","));
							}
							if(mapTransferTabDetails.get("Amount To") != null){
								sAmountTo = mapTransferTabDetails.get("Amount To");
								aAmountTo = Arrays.asList(sAmountTo.split(","));
							}
							if(mapTransferTabDetails.get("Charges") != null){
								sCharges = mapTransferTabDetails.get("Charges");
								aCharges = Arrays.asList(sCharges.split(","));
							}
							for(int i=0;i<aAmountFrom.size();i++)
							{
								if(mapTransferTabDetails.get("Amount From") != null && !aAmountFrom.get(i).toString().equalsIgnoreCase("None")){
									String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
									bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
									if(!bStatus){
										sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index"+" ]\n";
										bValidateStatus = false;
									}
								}
								if(mapTransferTabDetails.get("Amount To") != null && !aAmountTo.get(i).toString().equalsIgnoreCase("None")){
									String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
									bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
									if(!bStatus){
										sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index"+" ]\n";
										bValidateStatus = false;
									}
								}
								if(mapTransferTabDetails.get("Charges") != null && !aCharges.get(i).toString().equalsIgnoreCase("None")){
									String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
									bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
									if(!bStatus){
										sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index with value : "+aCharges.get(i).toString()+" ]\n";
										bValidateStatus = false;
									}
								}
							}
						}
					}
				}
				if(mapTransferTabDetails.get("Transaction Charges").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsTransferChargeNo']//parent::span[@class='checked']"));
					if(!bStatus ){
						sAppendMessage = sAppendMessage + "[ Transaction charges radio value is not matching with expected ]\n";
						bValidateStatus = false;
					}
				}
			}
			Messages.errorMsg = sAppendMessage+"\n";
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyClassDetailsOfSwitchTab(Map<String, String> mapSwitchTabDetails) {
		try{
			String sAppendMessage = "";
			boolean bValidateStatus = true;

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab7']"));
			if(!bStatus){
				Messages .errorMsg ="Switch Menu Tab is not Available";
				Assert.fail("Switch Menu Tab is not Available");
				return false;
			}
			Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.Switch.Label.lblswitchwait, Constants.lTimeOut);
			if(mapSwitchTabDetails.get("Frequency") != null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_switchList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen') and text()='"+mapSwitchTabDetails.get("Frequency").trim()+"']"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Frequency i.e. "+mapSwitchTabDetails.get("Frequency")+" is not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			if(mapSwitchTabDetails.get("Incentive Fee Crystallization")!=null)
			{
				if(mapSwitchTabDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='switchList0.isTransactionCrystallized1']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Incentive Fee Crystallization i.e. "+mapSwitchTabDetails.get("Incentive Fee Crystallization")+" is not matching with actual  ]\n";
						bValidateStatus = false;
					}
				}
				if(mapSwitchTabDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='switchList0.isTransactionCrystallized2']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Incentive Fee Crystallization i.e. "+mapSwitchTabDetails.get("Incentive Fee Crystallization")+" is not matching with actual  ]\n";
						bValidateStatus = false;
					}
				}
			}
			if(mapSwitchTabDetails.get("Notice Period Applicable")!=null)
			{
				if(mapSwitchTabDetails .get("Notice Period Applicable").equalsIgnoreCase("YES"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsSwitchNoticePeriodYes']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Notice Period Applicable i.e. "+mapSwitchTabDetails.get("Notice Period Applicable")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
					if(mapSwitchTabDetails.get("Notice Period")!=null){	
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='switchNoticePeriodList0.period' and @value='"+mapSwitchTabDetails.get("Notice Period")+"']"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given Notice Period i.e. "+mapSwitchTabDetails.get("Notice Period")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapSwitchTabDetails.get("NoticeMonth")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_switchNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSwitchTabDetails.get("NoticeMonth")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given Notice Month i.e. "+mapSwitchTabDetails.get("NoticeMonth")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapSwitchTabDetails.get("NoticePeriodType")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodSwitchBusCal']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSwitchTabDetails.get("NoticePeriodType")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given NoticePeriodType i.e. "+mapSwitchTabDetails.get("NoticePeriodType")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
						if(mapSwitchTabDetails.get("NoticePeriodType").equalsIgnoreCase("Business") && mapSwitchTabDetails.get("Include Holidays")!=null)
						{
							if(mapSwitchTabDetails.get("Include Holidays").equalsIgnoreCase("YES")){
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='isHolidaysIncludeSwitchNoticePeriod']//label//div[@id='uniform-rbIsHolidaysIncludeYes']//span[@class='checked']"));
								if(!bStatus){
									sAppendMessage = sAppendMessage + "[ Given Include Holidays i.e. "+mapSwitchTabDetails.get("Include Holidays")+" is not matching with the actual ] \n";
									bValidateStatus = false;
								}
							}
							if(mapSwitchTabDetails.get("Include Holidays").equalsIgnoreCase("NO")){
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='isHolidaysIncludeSwitchNoticePeriod']//label//div[@id='uniform-rbIsHolidaysIncludeNo']//span[@class='checked']"));
								if(!bStatus){
									sAppendMessage = sAppendMessage + "[ Given Include Holidays i.e. "+mapSwitchTabDetails.get("Include Holidays")+" is not matching with the actual ] \n";
									bValidateStatus = false;
								}
							}
						}
					}
					if(mapSwitchTabDetails.get("Notice Period Charges")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodTypeSWITCH']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSwitchTabDetails.get("Notice Period Charges")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given Notice Period Charges i.e. "+mapSwitchTabDetails.get("Notice Period Charges")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
						if(!mapSwitchTabDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapSwitchTabDetails.get("Amount/BPS/Percent")!=null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='switchNoticePeriodList0.noticePeriodLockupDetailsList0.rate' and contains(@value,'"+mapSwitchTabDetails.get("Amount/BPS/Percent")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Given Amount/BPS/Percent i.e. "+mapSwitchTabDetails.get("Amount/BPS/Percent")+" is not matching with the actual ] \n";
								bValidateStatus = false;
							}
						}
					}
				}
				if(mapSwitchTabDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsSwitchNoticePeriodNo']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Notice Period Applicable i.e. "+mapSwitchTabDetails.get("Notice Period Applicable")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
				}
			}
			if(mapSwitchTabDetails.get("Transaction Charges")!=null)
			{
				if(mapSwitchTabDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsSwitchChargeYes']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Transaction Charges Applicable i.e. "+mapSwitchTabDetails.get("Transaction Charges")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
					if(mapSwitchTabDetails.get("Effective Date")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='switchChargesList0.wef' and contains(@value,'"+mapSwitchTabDetails.get("Effective Date")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Effective Date i.e. "+mapSwitchTabDetails.get("Effective Date")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapSwitchTabDetails.get("Charges Type")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_switchChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSwitchTabDetails.get("Charges Type")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Charges Type i.e. "+mapSwitchTabDetails.get("Charges Type")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapSwitchTabDetails.get("Calculation Base")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_switchChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSwitchTabDetails.get("Calculation Base")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Calculation Base i.e. "+mapSwitchTabDetails.get("Calculation Base")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapSwitchTabDetails.get("Rate Method")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_chargesRateMethodSWI']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSwitchTabDetails.get("Rate Method")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Rate Method i.e. "+mapSwitchTabDetails.get("Rate Method")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapSwitchTabDetails.get("Rate Method") != null && !mapSwitchTabDetails.get("Rate Method").equalsIgnoreCase("None"))
					{
						if(mapSwitchTabDetails.get("Fixed Fee Rate") != null && mapSwitchTabDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.fixedFeeRate' and contains(@value,'"+mapSwitchTabDetails.get("Fixed Fee Rate")+"')]"));
							if (!bStatus) {
								sAppendMessage = sAppendMessage + "[ Given Fixed Fee Rate i.e. "+mapSwitchTabDetails.get("Fixed Fee Rate")+" is not matching with the actual ] \n";
								bValidateStatus = false;
							}
						}
						if (mapSwitchTabDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapSwitchTabDetails.get("Rate Method").equalsIgnoreCase("Tiered")) 
						{
							String sFromAmountLocator = "switchChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
							String sAmountToLocator = "switchChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
							String sChargesLocator = "switchChargesList0.chargeDetailsListReplaceIndexValue.rate";
							String sAmountFrom, sAmountTo, sCharges;
							List<String> aAmountFrom = null, aAmountTo = null, aCharges = null;
							if(mapSwitchTabDetails.get("Amount From") != null){							
								sAmountFrom = mapSwitchTabDetails.get("Amount From");
								aAmountFrom = Arrays.asList(sAmountFrom.split(","));
							}
							if(mapSwitchTabDetails.get("Amount To") != null){
								sAmountTo = mapSwitchTabDetails.get("Amount To");
								aAmountTo = Arrays.asList(sAmountTo.split(","));
							}
							if(mapSwitchTabDetails.get("Charges") != null){
								sCharges = mapSwitchTabDetails.get("Charges");
								aCharges = Arrays.asList(sCharges.split(","));
							}
							for(int i=0;i<aAmountFrom.size();i++){
								if(mapSwitchTabDetails.get("Amount From") != null && !aAmountFrom.get(i).toString().equalsIgnoreCase("None")){	
									String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
									bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
									if(!bStatus){
										sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index"+" ]\n";
										bValidateStatus = false;
									}
								}
								if(mapSwitchTabDetails.get("Amount To") != null && !aAmountTo.get(i).toString().equalsIgnoreCase("None")){
									String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
									bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
									if(!bStatus){
										sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index"+" ]\n";
										bValidateStatus = false;
									}
								}
								if(mapSwitchTabDetails.get("Charges") != null && !aCharges.get(i).toString().equalsIgnoreCase("None")){
									String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
									bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
									if(!bStatus){
										sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index"+" ]\n";
										bValidateStatus = false;
									}
								}
							}
						}
					}
				}
				if(mapSwitchTabDetails.get("Transaction Charges")!=null){
					if(mapSwitchTabDetails.get("Transaction Charges").equalsIgnoreCase("No")){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsSwitchChargeNo']//parent::span[@class='checked']"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Transaction charges radio value is not matching with expected ]\n";
							bValidateStatus = false;
						}

					}
				}
			}
			Messages.errorMsg = sAppendMessage+"\n";
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyClassDetailsOfExchangeTab(Map<String, String> mapExchangeTabDetails) {
		try{
			String sAppendMessage = "";
			boolean bValidateStatus = true;

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab8']"));
			if(!bStatus){
				Messages.errorMsg = "Exchange Menu Tab 	is not Available";
				Assert.fail("Exchange Menu Tab 	 is not Available");
				return false;
			}
			if(mapExchangeTabDetails.get("Frequency") != null){
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='s2id_exchangeList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen') and text()='"+mapExchangeTabDetails.get("Frequency").trim()+"']"), 5);
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Frequency i.e. "+mapExchangeTabDetails.get("Frequency")+" is not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			if(mapExchangeTabDetails.get("Incentive Fee Crystallization")!=null)
			{
				if(mapExchangeTabDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='exchangeList0.isTransactionCrystallized1']//parent::span[@class='checked']"),5);
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Incentive Fee Crystallization i.e. "+mapExchangeTabDetails.get("Incentive Fee Crystallization")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
				if(mapExchangeTabDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='exchangeList0.isTransactionCrystallized2']//parent::span[@class='checked']"), 5);
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Incentive Fee Crystallization i.e. "+mapExchangeTabDetails.get("Incentive Fee Crystallization")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
			}
			if(mapExchangeTabDetails.get("Notice Period Applicable")!=null)
			{
				if(mapExchangeTabDetails .get("Notice Period Applicable").equalsIgnoreCase("YES"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsExchangeNoticePeriodYes']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Notice Period Applicable i.e. "+mapExchangeTabDetails.get("Notice Period Applicable")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
					if(mapExchangeTabDetails.get("Notice Period")!=null){	
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='exchangeNoticePeriodList0.period' and @value='"+mapExchangeTabDetails.get("Notice Period")+"']"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given Notice Period i.e. "+mapExchangeTabDetails.get("Notice Period")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapExchangeTabDetails.get("NoticeMonth")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_exchangeNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapExchangeTabDetails.get("NoticeMonth")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given Notice Month i.e. "+mapExchangeTabDetails.get("NoticeMonth")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapExchangeTabDetails.get("NoticePeriodType")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodExchangeBusCal']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapExchangeTabDetails.get("NoticePeriodType")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given NoticePeriodType i.e. "+mapExchangeTabDetails.get("NoticePeriodType")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
						//Notice Period Type Business
						if(mapExchangeTabDetails.get("NoticePeriodType").equalsIgnoreCase("Business"))
						{
							if(mapExchangeTabDetails.get("Include Holidays")!=null)
							{
								if(mapExchangeTabDetails.get("Include Holidays").equalsIgnoreCase("YES")){
									bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='isHolidaysIncludeExchangeNoticePeriod']//label//div[@id='uniform-rbIsHolidaysIncludeYes']//span[@class='checked']"));
									if(!bStatus){
										sAppendMessage = sAppendMessage + "[ Given Include Holidays i.e. "+mapExchangeTabDetails.get("Include Holidays")+" is not matching with the actual ] \n";
										bValidateStatus = false;
									}
								}
								if(mapExchangeTabDetails.get("Include Holidays").equalsIgnoreCase("NO")){
									bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='isHolidaysIncludeExchangeNoticePeriod']//label//div[@id='uniform-rbIsHolidaysIncludeNo']//span[@class='checked']"));
									if(!bStatus){
										sAppendMessage = sAppendMessage + "[ Given Include Holidays i.e. "+mapExchangeTabDetails.get("Include Holidays")+" is not matching with the actual ] \n";
										bValidateStatus = false;
									}
								}
							}
						}
					}
					//Notice Period Charges..

					if(mapExchangeTabDetails.get("Notice Period Charges")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global .driver , By.xpath("//div[@id='s2id_noticePeriodTypeEXCHANGE']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapExchangeTabDetails.get("Notice Period Charges")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage+ "[ Given Notice Period Charges "+mapExchangeTabDetails.get("Notice Period Charges")+" is not matching with the actual ] \n";
						}
						if(!mapExchangeTabDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapExchangeTabDetails .get("Amount/BPS/Percent")!=null)
						{
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='exchangeNoticePeriodList0.noticePeriodLockupDetailsList0.rate' and contains(@value,'"+mapExchangeTabDetails.get("Amount/BPS/Percent")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Given Amount/BPS/Percent i.e. "+mapExchangeTabDetails.get("Amount/BPS/Percent")+" is not matching with the actual ] \n";
								bValidateStatus = false;
							}
						}
					}
				}
				if(mapExchangeTabDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsExchangeNoticePeriodNo']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Notice Period Applicable i.e. "+mapExchangeTabDetails.get("Notice Period Applicable")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
				}
			}
			if(mapExchangeTabDetails.get("Transaction Charges")!=null)
			{
				if(mapExchangeTabDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsExchangeChargeYes']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Transaction Charges Applicable i.e. "+mapExchangeTabDetails.get("Transaction Charges")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
					if(mapExchangeTabDetails.get("Effective Date")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='exchangeChargesList0.wef' and contains(@value,'"+mapExchangeTabDetails.get("Effective Date")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Effective Date i.e. "+mapExchangeTabDetails.get("Effective Date")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapExchangeTabDetails.get("Charges Type")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_exchangeChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapExchangeTabDetails.get("Charges Type")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Charges Type i.e. "+mapExchangeTabDetails.get("Charges Type")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}

					}
					if(mapExchangeTabDetails.get("Rate Method")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_chargesRateMethodEXC']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapExchangeTabDetails.get("Rate Method")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Rate Method i.e. "+mapExchangeTabDetails.get("Rate Method")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
						if(mapExchangeTabDetails.get("Rate Method") != null && !mapExchangeTabDetails.get("Rate Method").equalsIgnoreCase("None"))
						{
							if(mapExchangeTabDetails.get("Fixed Fee Rate") != null && mapExchangeTabDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.fixedFeeRate' and contains(@value,'"+mapExchangeTabDetails.get("Fixed Fee Rate")+"')]"));
								if (!bStatus) {
									sAppendMessage = sAppendMessage + "[ Given Fixed Fee Rate i.e. "+mapExchangeTabDetails.get("Fixed Fee Rate")+" is not matching with the actual ] \n";
									bValidateStatus = false;
								}
							}
							if (mapExchangeTabDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapExchangeTabDetails.get("Rate Method").equalsIgnoreCase("Tiered")) 
							{
								String sFromAmountLocator = "exchangeChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
								String sAmountToLocator = "exchangeChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
								String sChargesLocator = "exchangeChargesList0.chargeDetailsListReplaceIndexValue.rate";

								String sAmountFrom, sAmountTo, sCharges;
								List<String> aAmountFrom = null, aAmountTo = null, aCharges = null;
								if(mapExchangeTabDetails.get("Amount From") != null){
									sAmountFrom = mapExchangeTabDetails.get("Amount From");
									aAmountFrom = Arrays.asList(sAmountFrom.split(","));
								}
								if(mapExchangeTabDetails.get("Amount To") != null){
									sAmountTo = mapExchangeTabDetails.get("Amount To");
									aAmountTo = Arrays.asList(sAmountTo.split(","));
								}
								if(mapExchangeTabDetails.get("Charges") != null){
									sCharges = mapExchangeTabDetails.get("Charges");
									aCharges = Arrays.asList(sCharges.split(","));
								}
								for(int i=0;i<aAmountFrom.size();i++){
									if(mapExchangeTabDetails.get("Amount From") != null && !aAmountFrom.get(i).toString().equalsIgnoreCase("None")){
										String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
										bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
										if(!bStatus){
											sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index"+" ]\n";
											bValidateStatus = false;
										}
									}
									if(mapExchangeTabDetails.get("Amount To") != null && !aAmountTo.get(i).toString().equalsIgnoreCase("None")){
										String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
										bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
										if(!bStatus){
											sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index"+" ]\n";
											bValidateStatus = false;
										}
									}
									if(mapExchangeTabDetails.get("Charges") != null && !aCharges.get(i).toString().equalsIgnoreCase("None")){
										String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
										bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
										if(!bStatus){
											sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index"+" ]\n";
											bValidateStatus = false;
										}
									}
								}
							}
						}
					}
				}
				if(mapExchangeTabDetails.get("Transaction Charges")!=null){
					if(mapExchangeTabDetails.get("Transaction Charges").equalsIgnoreCase("No")){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsExchangeChargeNo']//parent::span[@class='checked']"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Transaction Charges Applicable i.e. "+mapExchangeTabDetails.get("Transaction Charges")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
				}
			}
			Messages.errorMsg = sAppendMessage+"\n";
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyTradingRestrictions(Map<String, String> mapOtherDetails) {
		try {
			String sAppendMsg = "";
			String sTransactionType = mapOtherDetails.get("Transaction Type");
			List<String> aTransactionType= Arrays.asList(sTransactionType.split(","));
			String sComments = mapOtherDetails.get("Comments");
			String[] aComments= sComments.split(",");
			/*String sGatingProvision = mapOtherDetails.get("Gating Provision");
			List<String> aGatingProvision= Arrays.asList(sGatingProvision.split(","));
			String sClosed = mapOtherDetails.get("Closed");
			List<String> aClosed= Arrays.asList(sClosed.split(","));*/
			boolean bValidateStatus =true;
			List<String> tranTypeValuesList = new ArrayList<String>();
			int x = Elements.getXpathCount(Global.driver, By.xpath("//div[contains(@id,'s2id_tradResLog_')]//span[contains(@id,'chosen')]"));
			for(int j=0 ;j<x ; j++){
				tranTypeValuesList.add(Elements.getText(Global.driver, By.xpath("//div[contains(@id,'s2id_tradResLog_"+j+"')]//span[contains(@id,'chosen')]")).trim());
			}
			for(int i=0;i<aTransactionType.size();i++){
				int transTypeIndex = tranTypeValuesList.indexOf(aTransactionType.get(i));
				if(!aTransactionType.get(transTypeIndex).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//select[@id='tradResLog_"+String.valueOf(transTypeIndex)+"']//parent::div//span[contains(@id,'select2') and contains(text(),'"+aTransactionType.get(i).toString()+"')]"));
					if(!bStatus){
						sAppendMsg = sAppendMsg+" Transaction dropdown selection value i.e. "+aTransactionType.get(i).toString()+" is Not matching with actual \n";
						bValidateStatus = false;
					}
				}
				if(mapOtherDetails.get("Date From") != null && mapOtherDetails.get("To Date") != null){
					//Verify More Dates Function
					bStatus = verifyDates(mapOtherDetails, i ,transTypeIndex);
					if(!bStatus){
						sAppendMsg = sAppendMsg+" Trading Restriction Dates Not Entered. ERROR: "+Messages.errorMsg;
						bValidateStatus = false;
					}
				}	
				//Verifying Comments in Trading Restrictions CommentBox
				if(mapOtherDetails.get("Comments")!=null){
					if(!aComments[i].equalsIgnoreCase("None")){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//textarea[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".comments' and contains(text(),'"+aComments[i].toString()+"')]"));
						if(!bStatus){
							sAppendMsg = sAppendMsg+" Trading Restriction to Comments value is Not matching with actual at given Comments at index "+transTypeIndex+"\n";
							bValidateStatus = false;
						}
					}
				}
				/*//Selecting the Gating Provision Radio Button
				if(mapOtherDetails.get("Gating Provision")!=null){
					if(!aGatingProvision.get(i).equalsIgnoreCase("None")){
						if(aGatingProvision.get(i).toString().equalsIgnoreCase("Yes")){									
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".gatingProvision1']//parent::span[@class='checked']"));									
							if(!bStatus){
								sAppendMsg = sAppendMsg+" Trading Restrictions Gating Provision "+transTypeIndex+"Yes Radio Button Not Matched with the Expected";
								bValidateStatus = false;
							}
						}
						if(aGatingProvision.get(i).toString().equalsIgnoreCase("No")){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".gatingProvision2']//parent::span[@class='checked']"));									
							if(!bStatus){
								sAppendMsg = sAppendMsg+" Trading Restrictions Gating Provision "+transTypeIndex+" No Radio Button Not Matched with the Expected";
								bValidateStatus = false;
							}
						}
					}
				}
				//Selecting the Closed Radio Button
				if(mapOtherDetails.get("Closed")!=null){
					if(!aClosed.get(i).equalsIgnoreCase("None")){
						if(aClosed.get(i).toString().equalsIgnoreCase("Yes")){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".closed1']//parent::span[@class='checked']"));									
							if(!bStatus){
								sAppendMsg = sAppendMsg+" Trading Restriction to closed Yes value Not matching with actualat index "+transTypeIndex+"\n";
								bValidateStatus = false;
							}
						}
						if(aClosed.get(i).toString().equalsIgnoreCase("No")){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".closed2']//parent::span[@class='checked']"));									
							if(!bStatus){
								sAppendMsg = sAppendMsg+" Trading Restriction to closed No value Not matching with actual at index "+transTypeIndex+"\n";
								bValidateStatus = false;
							}
						}
					}
				}*/
			}
			Messages.errorMsg = sAppendMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	public static boolean verifyDates(Map<String,String> mapOtherDetails,int i,int transTypeIndex){
		try {
			String sFromDate = mapOtherDetails.get("Date From");
			List<String> aFromDate= Arrays.asList(sFromDate.split(","));
			String sToDate = mapOtherDetails.get("To Date");
			List<String> aToDate= Arrays.asList(sToDate.split(","));
			List<String> arrIndidualFromDate = Arrays.asList(aFromDate.get(i).split(":"));
			List<String> arrIndidualToDate = Arrays.asList(aToDate.get(i).split(":"));
			String appendMsg ="";
			boolean bValidateStatus = true;
			for(int k=0;k<arrIndidualFromDate.size();k++){
				if(!arrIndidualFromDate.get(k).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+transTypeIndex+"fromDateRange"+k+"' and contains(@value,'"+arrIndidualFromDate.get(k)+"')]"));
					if(!bStatus){
						appendMsg = appendMsg+" Date From Not Matched with the Expected Date"+arrIndidualFromDate.get(k)+" at Index "+transTypeIndex+","+k+"\n";
						bValidateStatus = false;
					}
				}
				if(!arrIndidualToDate.get(k).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+transTypeIndex+"toDateRange"+k+"' and contains(@value,'"+arrIndidualToDate.get(k)+"')]"));
					if(!bStatus){
						appendMsg = appendMsg+" To Date Not Matched with the Expected Date"+arrIndidualToDate.get(k)+" at Index "+transTypeIndex+","+k+"\n";
						bValidateStatus = false;
					}
				}
			}
			Messages.errorMsg = appendMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	public static boolean verifyInvestorRestrictionDetails(Map<String ,String> mapOtherDetails){
		try {
			List<String> aCountryCode = Arrays.asList(mapOtherDetails.get("Country Code").split(","));
			List<String> aDateFrom = Arrays.asList(mapOtherDetails.get("Investor Restrictions Date From").split(","));
			List<String> aToDate = Arrays.asList(mapOtherDetails.get("Investor Restrictions To Date").split(","));
			String appendMsg = "";
			boolean validStatus = true;
			for(int i=0;i<Math.max(aCountryCode.size(), Math.max(aDateFrom.size(), aToDate.size())) ; i++){
				if(!aCountryCode.get(i).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_cIR_"+i+"']/a/span[contains(text(),'"+aCountryCode.get(i)+"')]"));
					if(!bStatus){
						appendMsg = appendMsg+aCountryCode.get(i)+" Value not matched with the Expected Country Code Value \n";
						validStatus = false;
					}
				}
				if(!aDateFrom.get(i).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='fromDateRange"+i+"' and @value='"+aDateFrom.get(i)+"']"));
					if(!bStatus){
						appendMsg = appendMsg+aCountryCode.get(i)+" Value not matched with the Expected From Date Value \n";
						validStatus = false;
					}
				}
				if(!aToDate.get(i).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='toDateRange"+i+"' and @value='"+aToDate.get(i)+"']"));
					if(!bStatus){
						appendMsg = appendMsg+aCountryCode.get(i)+" Value not matched with the Expected To Date Value \n";
						validStatus = false;
					}
				}
			}
			Messages.errorMsg = appendMsg+"\n";
			return validStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

}


