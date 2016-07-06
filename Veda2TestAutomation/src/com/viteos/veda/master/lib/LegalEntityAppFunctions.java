package com.viteos.veda.master.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class LegalEntityAppFunctions 
{


	/*	public static boolean searchLegalEntityMaster(String sLeagalEntityName, String Status, int iTime) {
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//label[normalize-space(text())='Legal Entity Name']/following-sibling::div[1]//span[contains(@id,'select2-chosen')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Legal Entity HomePage is not displayed.";
				return false;
			}

			bStatus = NewUICommonFunctions.WaitUntilValueAvailableInDropDown(sLeagalEntityName, By.xpath("//label[normalize-space(text())='Legal Entity Name']/following-sibling::div[1]//span[contains(@id,'select2-chosen')]"),iTime);
			if (!bStatus) {
				return false;
			}		

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(Status,Locators.FundFamilyMaster.Ddn.objStatusClick);
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
	}*/
	static boolean  bStatus;
	//Enter all the Legal Entity Details in Legal Entity Details tab in Legal Entity Master
	public static boolean doFillLegalEntityDetails(Map<String , String> mapLEDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.clientNameClick, Constants.lTimeOut);
			if(mapLEDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Client Name"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.clientNameClick);
				if(!bStatus){
					Messages.errorMsg = "Client Name  Entry Failed";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LeagalEntitySearchPanel.lblLegalEntity);
				if(!bStatus)
				{
					return false;
				}
			}
			if(mapLEDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Fund Family Name"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.fundFamilyNameClick);
				if(!bStatus){
					Messages.errorMsg = "Fund Family Name Entry Failed";
					return false;
				}
			}
			if(mapLEDetails.get("Legal Entity Name")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.TextBox.legalEntityName, mapLEDetails.get("Legal Entity Name"));
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Name Not Entry Failed";
					return false;
				}
			}
			if(mapLEDetails.get("Legal Entity Code")!=null){
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@name='legalEntityCode']"), mapLEDetails.get("Legal Entity Code"));
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Name Not Entry Failed";
					return false;
				}
			}
			
			if(mapLEDetails.get("Clone")!=null)
			{
				if(mapLEDetails.get("Clone").equalsIgnoreCase("Yes"))
				{
					bStatus=Elements.click(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.RadioButtons.isCloneYes);
					if(!bStatus)
					{
						Messages.errorMsg = "Clone YES radio button Not Selected";
						return false;
					}
					if(mapLEDetails.get("Legal Entity Name for Clone")!=null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Legal Entity Name for Clone"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.legalEntityForClone);
						if(!bStatus){
							Messages.errorMsg = "Legal Entity Name for Clone Not Selected";
							return false;
						}
					}
					if(mapLEDetails.get("Clone Button") != null && mapLEDetails.get("Clone Button").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.Buttons.btnClone);
						if(!bStatus){
							Messages.errorMsg = "Clone Button Click was Failed";
							return false;
						}
						Thread.sleep(5);
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LeagalEntitySearchPanel.lblLegalEntity);
					}
					if(mapLEDetails.get("View Button") != null && mapLEDetails.get("View Button").equalsIgnoreCase("Yes")){
						bStatus = Elements.clickButton(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.Buttons.btnView);
						if(!bStatus){
							Messages.errorMsg = "View Button Click was Failed";
							return false;
						}
					}
				}

				if(mapLEDetails.get("Clone") != null && mapLEDetails.get("Clone").equalsIgnoreCase("No"))
				{
					bStatus=Elements.click(Global.driver,  Locators.LegalEntityMaster.LegalEntityDetailsTab.RadioButtons.isCloneNo);
					if(!bStatus)
					{
						Messages.errorMsg = "Clone No radio button Not Selected";
						return false;
					}
				}
			}
			if(mapLEDetails.get("Legal Entity Type")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Legal Entity Type"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.legalEntityTypeClick);
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Type Entry Failed";
					return false;
				}
			}
			if(mapLEDetails.get("Currency")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Currency"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.currencyClick);
				if(!bStatus){
					Messages.errorMsg = "Currency drop down Entry Failed";
					return false;
				}
			}
			//Verify Accounting System

			if(mapLEDetails.get("Accounting System") != null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Accounting System/Version", mapLEDetails.get("Accounting System"));
				if(!bStatus){
					Messages.errorMsg = "Accounting System/Version actual value not match with Expected :: "+ mapLEDetails.get("Accounting System")+" value";
				}
				
			}
			if(mapLEDetails.get("Accounting Id")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.TextBox.accountingId, mapLEDetails.get("Accounting Id"));
				if(!bStatus){
					Messages.errorMsg = "Accounting Id Entry Failed";
					return false;
				}
			}
			if(mapLEDetails.get("Legal Entity Domicile")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Legal Entity Domicile"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.legalEntityDomicileClick);
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Domicile Entry Failed";
					return false;
				}
			}
			if(mapLEDetails.get("Billing Codes")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.TextBox.billingCodes, mapLEDetails.get("Billing Codes"));
				if(!bStatus){
					Messages.errorMsg = "Billing Codes Entry Failed";
					return false;
				}
			}

			if(mapLEDetails.get("Accounting Method")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Accounting Method"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.accountingMethodClick);
				if(!bStatus){
					Messages.errorMsg = "Accounting Method Entry Failed";
					return false;
				}
			}
			if(mapLEDetails.get("Investment Strategy")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Investment Strategy"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.investmentStarategyClick);
				if(!bStatus){
					Messages.errorMsg = "Investment Strategy Entry Failed";
					return false;
				}
			}
			
			//Enter FATCA GIIN 
			if(mapLEDetails.get("FATCA GIIN")!=null){
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@id='giin']"), mapLEDetails.get("FATCA GIIN"));
				if(!bStatus){
					Messages.errorMsg = "FATCA GIIN Entry Failed";
					return false;
				}
			}
			
			if(mapLEDetails.get("Side Pocket Investment")!=null)
			{
				if(mapLEDetails.get("Side Pocket Investment").equalsIgnoreCase("Yes"))
				{
					bStatus=Elements.click(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.RadioButtons.isSidePocketInvestmentYes);
					if(!bStatus)
					{
						Messages.errorMsg = "Side Pocket Investment YES radio button Not Selected";
						return false;
					}
				}

				if(mapLEDetails.get("Side Pocket Investment").equalsIgnoreCase("No"))
				{
					bStatus=Elements.click(Global.driver,  Locators.LegalEntityMaster.LegalEntityDetailsTab.RadioButtons.isSidePocketInvestmentNo);
					if(!bStatus)
					{
						Messages.errorMsg = "Side Pocket Investment No radio button Not Selected";
						return false;
					}
				}
			}

			if(mapLEDetails.get("Entity Type")!=null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapLEDetails.get("Entity Type"), Locators.LegalEntityMaster.LegalEntityDetailsTab.DropDown.entityTypeClick);
				if(!bStatus)
				{
					Messages.errorMsg = "Entity Type Entry Failed";
					return false;
				}
				if(mapLEDetails.get("Entity Type").equalsIgnoreCase("Feeder"))
				{
					if(mapLEDetails.get("Master Fund")!=null)
					{
						bStatus = removeMasterFund();
						if(!bStatus){
							return false;
						}
						bStatus = NewUICommonFunctions.selectMultipleValuesFromDropDown("Master Fund",mapLEDetails.get("Master Fund"));
						if(!bStatus){
							return false;
						}
					}
				}		
			}
			if(mapLEDetails.get("External Id1")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.TextBox.externalId1, mapLEDetails.get("External Id1"));
				if(!bStatus){
					Messages.errorMsg = "External Id1 Entry Failed";
					return false;
				}
			}
			if(mapLEDetails.get("External Id2")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.TextBox.externalId2, mapLEDetails.get("External Id2"));
				if(!bStatus){
					Messages.errorMsg = "External Id2 Entry Failed";
					return false;
				}
			}
			if(mapLEDetails.get("External Id3")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.LegalEntityMaster.LegalEntityDetailsTab.TextBox.externalId3, mapLEDetails.get("External Id3"));
				if(!bStatus){
					Messages.errorMsg = "External Id3 Entry Failed";
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

	//Enter all General Details in Legal Entity Master 
	public static boolean doFillGeneralDetails(Map<String , String> mapGeneralDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.unitizedDrpdwnClick , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Element Unitized Dropdown is not visible in General Details Tab";
				return false;
			}
			if(mapGeneralDetails.get("Unitized")!=null)
			{
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.unitizedDrpdwnClick);
					if(!bStatus){
						Messages.errorMsg = "Unitized Dropdown Not Clicked";
						return false;
					}
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
					if(mapGeneralDetails.get("Unit Description")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Unit Description"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.unitDescription);
						if(!bStatus){
							Messages.errorMsg = "Unit Description dropdown value Not Selected";
							return false;
						}
					}
					if(mapGeneralDetails.get("Unit Value")!=null){
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.unitValue, mapGeneralDetails.get("Unit Value"));
						if(!bStatus){
							Messages.errorMsg = "Unit Value Not Entered";
							return false;
						}
					}
					if(mapGeneralDetails.get("New Issue Percentage")!=null){
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.newIssuePercentage, mapGeneralDetails.get("New Issue Percentage"));
						if(!bStatus){
							Messages.errorMsg = "New Issue Percentage Not Entered";
							return false;
						}
					}
					if(mapGeneralDetails.get("Series RollUp")!=null){
						if(mapGeneralDetails.get("Series RollUp").equalsIgnoreCase("No"))
						{
							bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.seriesRollUpNo);
							if(!bStatus){
								Messages.errorMsg = "Series RollUp No Radio Button Not Selected";
								return false;
							}
							if(mapGeneralDetails.get("Equalization")!=null)
							{
								if(mapGeneralDetails.get("Equalization").equalsIgnoreCase("Yes"))
								{
									bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.equalizationYes);
									if(!bStatus){
										Messages.errorMsg = "Equalization Yes Radio Button Not Selected";
										return false;
									}
									if(mapGeneralDetails.get("Equalization Method")!=null)
									{
										bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Equalization Method"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.equalizationMethod);
										if(!bStatus)
										{
											Messages.errorMsg = "Equalization Method Dropdown Value Not Selected";
											return false;
										}
									}
								}
								if(mapGeneralDetails.get("Equalization").equalsIgnoreCase("No"))
								{
									bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.equalizationNo);
									if(!bStatus){
										Messages.errorMsg = "Equalization No Radio Button Not Selected";
										return false;
									}
								}
							}
						}
						if(mapGeneralDetails.get("Series RollUp").equalsIgnoreCase("Yes"))
						{
							bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.seriesRollUpYes);
							if(!bStatus){
								Messages.errorMsg = "Series RollUp Yes Radio Button Not Selected";
								return false;
							}
							if(mapGeneralDetails.get("Series RollUp Frequency")!=null){

								bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapGeneralDetails.get("Series RollUp Frequency"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.seriesRollUpFrequency, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");
								//	bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Series RollUp Frequency"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.seriesRollUpFrequency);
								if(!bStatus){
									Messages.errorMsg = "Series RollUp Frequency dropdown value Not Selected";
									return false;
								}
							}
						}
					}
				}
				if(mapGeneralDetails.get("Unitized").equalsIgnoreCase("Non Unitized")){
					Thread.sleep(2000);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Unitized"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.unitizedDrpdwnClick);
					if(!bStatus){
						Messages.errorMsg = "Unitized Dropdown value Not Selected";
						return false;
					}
				}
			}
			if(mapGeneralDetails.get("Reporting Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapGeneralDetails.get("Reporting Frequency"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.reportingFrequency, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");
				if(!bStatus){
					Messages.errorMsg = "Reporting Frequency dropdown value Not Selected";
					return false;
				}			
			}
			if(mapGeneralDetails.get("Estimation Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapGeneralDetails.get("Estimation Frequency"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.estimationFrequency, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");
				//	bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Estimation Frequency"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.estimationFrequency);
				if(!bStatus){
					Messages.errorMsg = "Estimation Frequency dropdown value Not Selected";
					return false;
				}
			}
			if(mapGeneralDetails.get("Crystalization Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapGeneralDetails.get("Crystalization Frequency"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.crystalizationFrequency, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");
				//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Crystalization Frequency"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.crystalizationFrequency);
				if(!bStatus){
					Messages.errorMsg = "Crystalization Frequency dropdown value Not Selected";
					return false;
				}
			}
			if(mapGeneralDetails.get("Fee to be posted in Master") != null){
				if(mapGeneralDetails.get("Fee to be posted in Master").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(normalize-space(),'Fee to be posted in Master')]//following::div//input[@id='seriesRollUpYes']//parent::span"));
					if(!bStatus){
						Messages.errorMsg = "Unable to click on Fee to be Posted in Master 'Yes' Radio button";
						return false;
					}
					if(mapGeneralDetails.get("FeeTypeManagement") != null){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='isManagementFee1']"));
						if(!bStatus && mapGeneralDetails.get("FeeTypeManagement").equalsIgnoreCase("Yes")){
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isManagementFee1']"));
							if(!bStatus){
								Messages.errorMsg = "Management Fee Check box is not Selected";
								return  false;
							}
						}
						if(bStatus && mapGeneralDetails.get("FeeTypeManagement").equalsIgnoreCase("No")){
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isManagementFee1']"));
							if(!bStatus){
								Messages.errorMsg = "Management Fee Check box is not UnSelected";
								return  false;
							}
						}
					}
					if(mapGeneralDetails.get("FeeTypeIncentive") != null){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='isIncentiveFee1']"));
						if(!bStatus && mapGeneralDetails.get("FeeTypeIncentive").equalsIgnoreCase("Yes")){
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isIncentiveFee1']"));
							if(!bStatus){
								Messages.errorMsg = "Incentive Fee Check box is not Selected";
								return  false;
							}
						}
						if(bStatus && mapGeneralDetails.get("FeeTypeIncentive").equalsIgnoreCase("No")){
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isIncentiveFee1']"));
							if(!bStatus){
								Messages.errorMsg = "Incentive Fee Check box is not UnSelected";
								return  false;
							}
						}
					}
				}
				if(mapGeneralDetails.get("Fee to be posted in Master").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(normalize-space(),'Fee to be posted in Master')]//following::div//input[@id='seriesRollUpNo']//parent::span"));
					if(!bStatus){
						Messages.errorMsg = "Unable to click on Fee to be Posted in Master  'No' Radio button";
						return false;
					}
				}
			}
			if(mapGeneralDetails.get("Initial Offering Date")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.initialOfferingDate, mapGeneralDetails.get("Initial Offering Date"));
				if(!bStatus){
					Messages.errorMsg = "Initial Offering Date Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
			}
			if(mapGeneralDetails.get("Start Date")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.startDate, mapGeneralDetails.get("Start Date"));
				if(!bStatus){
					Messages.errorMsg = "Start Date Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
			}
			if(mapGeneralDetails.get("Fiscal Year End")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.fiscalYearEnd, mapGeneralDetails.get("Fiscal Year End"));
				if(!bStatus){
					Messages.errorMsg = "Fiscal Year End Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
			}
			if(mapGeneralDetails.get("First Fiscal Year End")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.firstFiscalYearEnd, mapGeneralDetails.get("First Fiscal Year End"));
				if(!bStatus){
					Messages.errorMsg = "First Fiscal Year End Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
			}
			if(mapGeneralDetails.get("Open or Closed Ended")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGeneralDetails.get("Open or Closed Ended"), Locators.LegalEntityMaster.GeneralDetailsTab.DropDown.openOrCloseEnded);
				if(!bStatus){
					Messages.errorMsg = "Open or Closed Ended dropdown value Not Selected";
					return false;
				}
				bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
			}
			if(mapGeneralDetails.get("Is Claw Back")!=null){
				if(mapGeneralDetails.get("Is Claw Back").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.isClawBackYes);
					if(!bStatus){
						Messages.errorMsg = "Is Claw Back Yes Radio Button Not Selected";
						return false;
					}
					if(mapGeneralDetails.get("Capital in Percentage")!=null){
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.capitalPercentage, mapGeneralDetails.get("Capital in Percentage"));
						if(!bStatus){
							Messages.errorMsg = "Capital in Percentage Not Entered";
							return false;
						}
					}
					if(mapGeneralDetails.get("Number of Years")!=null){
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.noOfYears, mapGeneralDetails.get("Number of Years"));
						if(!bStatus){
							Messages.errorMsg = "Number of Years Not Entered";
							return false;
						}
					}
					if(mapGeneralDetails.get("Is Accrued Claw back Amount")!=null)
					{
						if(mapGeneralDetails.get("Is Accrued Claw back Amount").equalsIgnoreCase("Yes"))
						{
							bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.isAccuredClawbackAmountYes);
							if(!bStatus){
								Messages.errorMsg = "Is Accrued Claw back Amount Yes Radio Button Not Selected";
								return false;
							}
						}
						if(mapGeneralDetails.get("Is Accrued Claw back Amount").equalsIgnoreCase("No"))
						{
							bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.isAccuredClawbackAmountNo);
							if(!bStatus){
								Messages.errorMsg = "Is Accrued Claw back Amount No Radio Button Not Selected";
								return false;
							}
						}
					}

					if(mapGeneralDetails.get("Percentage")!=null && mapGeneralDetails.get("Number of Years")!=null){
						bStatus = Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.TextBox.percentage1, Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "Percentage  field not Visible";
							return false;
						}
						int count =Integer.parseInt(mapGeneralDetails.get("Number of Years"));
						List<String> aPercentage = Arrays.asList(mapGeneralDetails.get("Percentage").split(","));
						for(int i=0 ; i<count ; i++){
							bStatus = Elements.enterText(Global.driver, By.id("legalEntitClawbackMappingList"+i+".percentage"), aPercentage.get(i));
							if(!bStatus){
								Messages.errorMsg = "Percentage "+i+" Not Entered";
								return false;
							}
						}
					}
				}
				if(mapGeneralDetails.get("Is Claw Back").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.isClawBackNo);
					if(!bStatus){
						Messages.errorMsg = "Is Claw Back No Radio Button Not Selected";
						return false;
					}
				}
			}
			if(mapGeneralDetails.get("Holiday Calendar")!=null){
				NewUICommonFunctions.jsClick(By.xpath("//label[normalize-space(text())='Holiday Calendar']"));
				bStatus = LegalEntityAppFunctions.removeHolidayCalendar();
				if(!bStatus){
					return false;
				}
				NewUICommonFunctions.jsClick(By.xpath("//label[normalize-space(text())='Holiday Calendar']"));
				bStatus = NewUICommonFunctions.selectMultipleValuesFromDropDown("Holiday Calendar",mapGeneralDetails.get("Holiday Calendar"));
				if(!bStatus){
					return false;
				}
			}
			
				
			
			if (mapGeneralDetails.get("WeeklyOffDaysToCheck") != null) {
				if(mapGeneralDetails.get("WeeklyOffDaysToCheck").equalsIgnoreCase("None")){
					bStatus = removeAllChecboxesForWeeklyOff();
				}
				bStatus = checkTheWeeklyOffDays(mapGeneralDetails.get("WeeklyOffDaysToCheck"));
				if(!bStatus)
				{
					Messages.errorMsg = "Weekly Off Days wasn't selected for : " +mapGeneralDetails.get("WeeklyOffDaysToCheck");
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

	private static boolean removeAllChecboxesForWeeklyOff() {
		try {
			
			List<String> sTotalDays = Arrays.asList("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday");
			for(int index = 0; index < sTotalDays.size(); index++){
				bStatus =  Verify.verifyElementPresent(Global.driver, By.xpath("//label[contains(text(),'"+sTotalDays.get(index)+"')]/preceding-sibling::div//span[@class='checked']"));
				if(bStatus){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'"+sTotalDays.get(index)+"')]/preceding-sibling::div//input"));
					if(!bStatus){
						Messages.errorMsg = sTotalDays.get(index)+"Checkbox Not Unchecked for Weekly Off";
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

	//Function to Check the Required Check boxes and Un check the Other Check boxes
	public static boolean checkTheWeeklyOffDays(String sDays) {
		try {
			List<String> sDaysList = Arrays.asList(sDays.split(","));
			List<String> sTotalDays = Arrays.asList("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday");
			//bStatus = LegalEntityAppFunctions.unCheckallDays();

			for (int index = 0; index < sTotalDays.size(); index++) {
				bStatus =  Verify.verifyElementPresent(Global.driver, By.xpath("//label[contains(text(),'"+sTotalDays.get(index)+"')]/preceding-sibling::div//span[@class='checked']"));
				if(sDaysList.contains(sTotalDays.get(index)) && !bStatus){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'"+sTotalDays.get(index)+"')]/preceding-sibling::div//input"));
					if(!bStatus){
						Messages.errorMsg = "Checkbox Selection Failed";
						return false;
					}
				}
				else if(!sDaysList.contains(sTotalDays.get(index)) && bStatus){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'"+sTotalDays.get(index)+"')]/preceding-sibling::div//input"));
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}		
	}


	/*private static boolean unCheckallDays() {
		try {
			List<String> sTotalDays = Arrays.asList("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturady");
			for(int i=0; i<sTotalDays.size();i++)
			{
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[contains(text(),'"+sTotalDays.get(i)+"')]/preceding-sibling::div//span[@class='checked']"));
				if(bStatus)
				{
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'"+sTotalDays.get(i)+"')]/preceding-sibling::div//input"));
					if (!bStatus) {
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
	}*/

	//Enter all the Other Details of Legal Entity Master
	public static boolean doFillOtherDetails(Map<String , String> mapOtherDetails , Map<String , String> mapGeneralDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.shareDecimalsForCalculations, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Share Decimals For Calculations element is not visisble";
				return false;
			}
			if(mapOtherDetails.get("Share Decimals For Calculation")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.shareDecimalsForCalculations, mapOtherDetails.get("Share Decimals For Calculation"));
				if(!bStatus){
					Messages.errorMsg = "Share Decimals For Calculations Not Enetered";
					return false;
				}
			}
			if(mapOtherDetails.get("Share Decimals For Display")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.shareDecimalsDisplay, mapOtherDetails.get("Share Decimals For Display"));
				if(!bStatus){
					Messages.errorMsg = "Share Decimals Display Not Enetered";
					return false;
				}
			}
			if(mapOtherDetails.get("NAV Decimals For Calculation")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.navDecimalsForCalculation, mapOtherDetails.get("NAV Decimals For Calculation"));
				if(!bStatus){
					Messages.errorMsg = "NAV Decimals For Calculation Not Enetered";
					return false;
				}
			}
			if(mapOtherDetails.get("NAV Decimal For Display")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.navDecimalsDisplay, mapOtherDetails.get("NAV Decimal For Display"));
				if(!bStatus){
					Messages.errorMsg = "NAV Decimals Display Not Enetered";
					return false;
				}
			}
			if(mapGeneralDetails.get("Unitized") != null && mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized")){
				if(mapOtherDetails.get("Dividend Intended")!=null)
				{
					if(mapOtherDetails.get("Dividend Intended").equalsIgnoreCase("Yes"))
					{
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.RadioButton.isDividendIntendedYes);
						if(!bStatus){
							Messages.errorMsg = "Dividend Intended Yes Radio Button Not Selected";
							return false;
						}
						if(mapOtherDetails.get("Dividend Frequency")!=null){
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.DateBox.dividendFrequency, mapOtherDetails.get("Dividend Frequency"));
							if(!bStatus){
								Messages.errorMsg = "Dividend Frequency Not Enetered";
								return false;
							}
							bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
						}
						if(mapOtherDetails.get("Dividend Base NAV Value")!=null){
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.dividendBaseNavValue, mapOtherDetails.get("Dividend Base NAV Value"));
							if(!bStatus){
								Messages.errorMsg = "Dividend Base NAV Value Not Enetered";
								return false;
							}
						}
						if(mapOtherDetails.get("Minimum Re-Investment Amount")!=null){
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.minimumReinvestmentAmount, mapOtherDetails.get("Minimum Re-Investment Amount"));
							if(!bStatus){
								Messages.errorMsg = "Minimum Re-Investment Amount Not Enetered";
								return false;
							}
						}
						if(mapOtherDetails.get("Dividend Determination Basis")!=null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Dividend Determination Basis"), Locators.LegalEntityMaster.OtherDetails.DropDown.dividendDeterminationBasisClick);
							if(!bStatus){
								Messages.errorMsg = "Dividend Determination Basis dropdown value Not Selected";
								return false;
							}
						}
						if(mapOtherDetails.get("Dividend Re-Investment Method")!=null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Dividend Re-Investment Method"), Locators.LegalEntityMaster.OtherDetails.DropDown.dividendReinvestmentMethodClick);
							if(!bStatus){
								Messages.errorMsg = "Dividend Re-Investment Method dropdown value Not Selected";
								return false;
							}
						}
						if(mapOtherDetails.get("Dividend Reference")!=null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Dividend Reference"), Locators.LegalEntityMaster.OtherDetails.DropDown.DividendReferenceClick);
							if(!bStatus){
								Messages.errorMsg = "Dividend Reference dropdown value Not Selected";
								return false;
							}
						}
					}
					if(mapOtherDetails.get("Dividend Intended").equalsIgnoreCase("No")){
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.RadioButton.isDividendIntendedNo);
						if(!bStatus){
							Messages.errorMsg = "Dividend Intended No Radio Button Not Selected";
							return false;
						}
					}
				}
			}
			if(mapOtherDetails.get("ERISA Applicable")!=null)
			{
				if(mapOtherDetails.get("ERISA Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.RadioButton.isErisaApplicableYes);
					if(!bStatus){
						Messages.errorMsg = "ERISA Applicable Yes Radio Button Not Selected";
						return false;
					}
					if(mapOtherDetails.get("ERISA Percentage")!=null){
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.eRISAPersentage, mapOtherDetails.get("ERISA Percentage"));
						if(!bStatus){
							Messages.errorMsg = "ERISA Percentage Not Entered";
							return false;
						}
					}
				}
				if(mapOtherDetails.get("ERISA Applicable").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.RadioButton.isErisaApplicableNo);
					if(!bStatus){
						Messages.errorMsg = "ERISA Applicable No Radio Button Not Selected";
						return false;
					}
				}
			}
			if(mapOtherDetails.get("Trading Restriction Applicable")!=null)
			{
				if(mapOtherDetails.get("Trading Restriction Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.RadioButton.tradingRestriction_Yes);
					if(!bStatus)
					{
						Messages.errorMsg = "Trading Restriction Applicable Yes Radio Button Not Selected";
						return false;
					}
					if(mapOtherDetails.get("Transaction Type")!=null)
					{
						bStatus = removeTrades();
						if(!bStatus){
							return false;
						}
						bStatus = removeTradeDates();
						if(!bStatus){
							return false;
						}
						bStatus = NewUICommonFunctions.addTradingRestrictions(mapOtherDetails);
						if(!bStatus)
						{
							Messages.errorMsg = "Failed to Enter Trading Restrictions . ERROR: "+Messages.errorMsg;
							return false;
						}
					}
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
				}
				if(mapOtherDetails.get("Trading Restriction Applicable").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.RadioButton.tradingRestriction_No);
					if(!bStatus)
					{
						Messages.errorMsg = "Trading Restriction Applicable No Radio Button Not Selected";
						return false;
					}
				}
			}
			if(mapOtherDetails.get("Authorized Share Applicable")!=null){
				if(mapOtherDetails.get("Authorized Share Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.RadioButton.authorizedShare_Yes);
					if(!bStatus){
						Messages.errorMsg = "Authorized Share Applicable Yes Radio Button Not Selected";
						return false;
					}
					if(mapOtherDetails.get("Authorized Shares")!=null){
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.authorizedShares, mapOtherDetails.get("Authorized Shares"));
						if(!bStatus){
							Messages.errorMsg = "Authorized Shares Not Entered";
							return false;
						}
					}
				}
				if(mapOtherDetails.get("Authorized Share Applicable").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.RadioButton.authorizedShare_No);
					if(!bStatus){
						Messages.errorMsg = "Authorized Share Applicable No Radio Button Not Selected";
						return false;
					}
				}
			}
			if(mapOtherDetails.get("Accounting Location")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Accounting Location"), Locators.LegalEntityMaster.OtherDetails.DropDown.accountingLocationClick);
				if(!bStatus){
					Messages.errorMsg = "Accounting Location dropdown value Not Selected";
					return false;
				}
				bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
			}
			if(mapOtherDetails.get("Administrator Services Location")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Administrator Services Location"), Locators.LegalEntityMaster.OtherDetails.DropDown.administratorSerivicesLocationClick);
				if(!bStatus){
					Messages.errorMsg = "Administrator Services Location dropdown value Not Selected";
					return false;
				}
			}
			if(mapOtherDetails.get("Investor Services Location")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Investor Services Location"), Locators.LegalEntityMaster.OtherDetails.DropDown.investorServicesLocationClick);
				if(!bStatus){
					Messages.errorMsg = "Investor Services Location dropdown value Not Selected";
					return false;
				}
			}
			if(mapOtherDetails.get("Registration")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOtherDetails.get("Registration"), Locators.LegalEntityMaster.OtherDetails.DropDown.registrationClick);
				if(!bStatus){
					Messages.errorMsg = "Registration dropdown value Not Selected";
					return false;
				}
			}
			if(mapOtherDetails.get("Upload PPM")!=null){
				if(mapOtherDetails.get("Upload PPM").equalsIgnoreCase("Yes")){
					if(mapOtherDetails.get("Upload PPM FilePath")!=null){
						JavascriptExecutor jse = (JavascriptExecutor)Global.driver;
						jse.executeScript("document.getElementsByName('file')[0].setAttribute('style', 'display:block');");
						Global.driver.findElement(By.xpath("//input[@name='file']")).sendKeys(mapOtherDetails.get("Upload PPM FilePath"));
					}
				}
			}
			if(mapOtherDetails.get("Upload One Pager")!=null){
				if(mapOtherDetails.get("Upload One Pager").equalsIgnoreCase("Yes")){
					if(mapOtherDetails.get("Upload One Pager FilePath")!=null){
						JavascriptExecutor jse = (JavascriptExecutor)Global.driver;
						jse.executeScript("document.getElementsByName('file')[1].setAttribute('style', 'display:block');");
						Global.driver.findElement(By.xpath("//input[@id='file2']")).sendKeys(mapOtherDetails.get("Upload One Pager FilePath"));
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

	//Enter all the Subscription Details of Legal Entity Master
	public static boolean doFillSubscriptionDetails(Map<String , String> mapSubscriptionDetails,Map<String , String> mapGeneralDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.DropDown.newSubscriptionComputationClick, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Element New Subscription Computation is Not Visisble in Subscription Details Tab";
			}
			if(mapSubscriptionDetails.get("Investment Permitted")!=null)
			{
				if(mapGeneralDetails.get("Unitized")!=null && mapGeneralDetails.get("Unitized").equalsIgnoreCase("Unitized"))
				{
					if(mapSubscriptionDetails.get("Investment Permitted").equalsIgnoreCase("Amount"))
					{
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedAmount);
						if(!bStatus)
						{
							Messages.errorMsg = "Amount Radio Button is Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Minimum Initial Amount")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minInitialSubAmount , mapSubscriptionDetails.get("Minimum Initial Amount"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Initial Amount Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Subsequent Amount")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minSubsequentAmount , mapSubscriptionDetails.get("Minimum Subsequent Amount"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Subsequent Amount Entry failed";
								return false;
							}	
						}
					}
					if(mapSubscriptionDetails.get("Investment Permitted").equalsIgnoreCase("Shares"))
					{
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedShares);
						if(!bStatus)
						{
							Messages.errorMsg = "Shares Radio Button is Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Minimum Initial Shares")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minInitialSubShares , mapSubscriptionDetails.get("Minimum Initial Shares"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Initial Shares Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Subsequent Shares")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minSubsequentSubShares , mapSubscriptionDetails.get("Minimum Subsequent Shares"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Subsequent Shares Entry failed";
								return false;
							}	
						}
					}
					if(mapSubscriptionDetails.get("Investment Permitted").equalsIgnoreCase("Both"))
					{
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedBoth);
						if(!bStatus)
						{
							Messages.errorMsg = "Both Radio Button is Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Minimum Initial Amount")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minInitialSubAmount , mapSubscriptionDetails.get("Minimum Initial Amount"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Initial Amount Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Subsequent Amount")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minSubsequentAmount , mapSubscriptionDetails.get("Minimum Subsequent Amount"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Subsequent Amount Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Initial Shares")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minInitialSubShares , mapSubscriptionDetails.get("Minimum Initial Shares"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Initial Shares Entry failed";
								return false;
							}	
						}
						if(mapSubscriptionDetails.get("Minimum Subsequent Shares")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minSubsequentSubShares , mapSubscriptionDetails.get("Minimum Subsequent Shares"));
							if(!bStatus)
							{
								Messages.errorMsg = "Minimum Subsequent Shares Entry failed";
								return false;
							}	
						}
					}	
				}
				if(mapGeneralDetails.get("Unitized")!=null && mapGeneralDetails.get("Unitized").equalsIgnoreCase("Non Unitized"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.InvestmenetPermittedAmount);
					if(!bStatus){
						Messages.errorMsg = "Investment Permitted Amount Radio Button Failed to Select in Subscription Tab";
						return false;
					}
					if(mapSubscriptionDetails.get("Minimum Initial Amount")!=null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minInitialSubAmount , mapSubscriptionDetails.get("Minimum Initial Amount"));
						if(!bStatus)
						{
							Messages.errorMsg = "Minimum Initial Amount Entry failed";
							return false;
						}	
					}
					if(mapSubscriptionDetails.get("Minimum Subsequent Amount")!=null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.minSubsequentAmount , mapSubscriptionDetails.get("Minimum Subsequent Amount"));
						if(!bStatus)
						{
							Messages.errorMsg = "Minimum Subseqent Amount Entry failed";
							return false;
						}	
					}
				}
			}
			if(mapSubscriptionDetails.get("Subscription Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapSubscriptionDetails.get("Subscription Frequency"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.subscriptionFrequencyClick, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");

				if(!bStatus){
					Messages.errorMsg = "Subscription Frequency Dropdown Value Not Selected";
					return false;
				}
			}
			if(mapSubscriptionDetails.get("New Subscription Computation")!=null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("New Subscription Computation"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.newSubscriptionComputationClick);
				if(!bStatus){
					Messages.errorMsg = "New Subscription Computation Dropdown Value Not Selected";
					return false;
				}					
			}
			if(mapSubscriptionDetails.get("Presentation Method")!=null){
				/*if(mapSubscriptionDetails.get("New Subscription Computation").equalsIgnoreCase("Cumulative"))
				{
					if(mapSubscriptionDetails.get("Presentation Method").equalsIgnoreCase("Non Cumulative"))
					{
						Messages.errorMsg = "Presentation Method Should be Cumulative if New Subscription Compution is Cumulative";
						return false;
					}
				}*/
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Presentation Method"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.presentationMethodClick);
				if(!bStatus)
				{
					Messages.errorMsg = "Presentation Method Dropdown Value Not Selected";
					return false;
				}
			}
			if(mapSubscriptionDetails.get("Notice Period Applicable")!=null)
			{
				if(mapSubscriptionDetails.get("Notice Period Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.noticePeriodYes);
					if(!bStatus)
					{
						Messages.errorMsg = "Notice Period Applicable Yes Radio button not selected";
						return false;
					}
					if(mapSubscriptionDetails.get("Notice Period")!=null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.npNoticePeriod, mapSubscriptionDetails.get("Notice Period"));
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period not Entered";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Notice Period Day or Month or Year")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Notice Period Day or Month or Year"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.noticePeriodDayMonthsClick);
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period "+mapSubscriptionDetails.get("Notice Period Day or Month or Year")+" Not Selected";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Notice Period Calender Type")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Notice Period Calender Type"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.noticePeriodCalenderTypeClick);
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
									bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.includeHolidaysYes);
									if(!bStatus)
									{
										Messages.errorMsg = "Include Holidays Radio Button Yes Not Selected";
										return false;
									}
								}
								if(mapSubscriptionDetails.get("Include Holidays").equalsIgnoreCase("No"))
								{
									bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.includeHolidaysNo);
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
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Notice Period Charges"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.noticePeriodCharges);
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period Charges value Not Selected from drop down";
							return false;
						}
						if(!mapSubscriptionDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapSubscriptionDetails.get("Amount or BPS or Percent")!=null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.npAmountorBPSorPercent, mapSubscriptionDetails.get("Amount or BPS or Percent"));
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
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.noticePeriodNo);
					if(!bStatus)
					{
						Messages.errorMsg = "Notice Period Applicable No Radio button not selected";
						return false;
					}
				}
			}
			if(mapSubscriptionDetails.get("Transaction Charges")!=null){
				if(mapSubscriptionDetails.get("Transaction Charges").equalsIgnoreCase("No"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.transactionChargesNo);
					if(!bStatus)
					{
						Messages.errorMsg = "Transaction Charges No Radio button not selected";
						return false;
					}
				}
				if(mapSubscriptionDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.RaioButtons.transactionChargesYes);
					if(!bStatus)
					{
						Messages.errorMsg = "Transaction Charges Yes Radio button not selected";
						return false;
					}
					if(mapSubscriptionDetails.get("Effective Date")!=null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.tcEffectiveDate, mapSubscriptionDetails.get("Effective Date"));
						if(!bStatus)
						{
							Messages.errorMsg = "Transaction Charges Effective Date Not Entered";
							return false;
						}
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
					}
					if(mapSubscriptionDetails.get("Charges Type")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Charges Type"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.chargesType);
						//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Charges Type", mapSubscriptionDetails.get("Charges Type"));
						if(!bStatus)
						{
							Messages.errorMsg = "Transaction Charges Type Dropdown Value Not Selected";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Calculation Base")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Calculation Base"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.calculationBase);
						if(!bStatus)
						{
							Messages.errorMsg = "Transaction Charges Calculation Base Dropdown Value Not Selected";
							return false;
						}
					}
					if(mapSubscriptionDetails.get("Rate Method")!=null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Rate Method"), Locators.LegalEntityMaster.SubscriptionTab.DropDown.rateMethod);
						if(!bStatus)
						{
							Messages.errorMsg = "Transaction Charges Rate Method Dropdown Value Not Selected";
							return false;
						}
						if(mapSubscriptionDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							if(mapSubscriptionDetails.get("Fixed Fee Rate")!=null){
								bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.SubscriptionTab.TextBox.tcFixedFeeRate, mapSubscriptionDetails.get("Fixed Fee Rate"));
								if(!bStatus)
								{
									Messages.errorMsg = "Transaction Charges Fixed Fee Rate Not Entered";
									return false;
								}
							}
						}
						if(mapSubscriptionDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapSubscriptionDetails.get("Rate Method").equalsIgnoreCase("Tiered"))
						{
							if(mapSubscriptionDetails.get("Amount From")!=null && mapSubscriptionDetails.get("Amount To")!=null && mapSubscriptionDetails.get("Charges")!=null){
								bStatus = removeTransactionCharges("tiredFee");
								if(!bStatus){
									Messages.errorMsg = "Rate Methode Remove Button Not Clicked for Subscription Tab";
									return false;
								}
								bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapSubscriptionDetails,"subscription" , "add");
								if(!bStatus){
									Messages.errorMsg = "Rate Methode Amount Details Entery Failed for Subscription Tab";
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

	//Enter all the Redemption Details of Legal Entity Master
	public static boolean doFillRedemptionTabDetails(Map<String, String> mapDetails)
	{
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//a[text()=' Redemption']"), Constants.lTimeOut);
			if(!bStatus)
			{
				return false;
			}
			bStatus=Elements.click(Global.driver , By.xpath("//a[text()=' Redemption']"));
			if(!bStatus)
			{
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtMinimumAmount);
			if(!bStatus)
			{
				return false;
			}
			if(mapDetails.get("Minimum Amount") != null)
			{
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtMinimumAmount, mapDetails.get("Minimum Amount"));
				if(!bStatus)
				{
					Messages.errorMsg = "Minimum Amount wasn't inputed into the field in Redemption Tab";
					return false;
				}
			}
			if(mapDetails.get("Minimum Shares") != null)
			{
				bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtMinimumShares, mapDetails.get("Minimum Shares"));
				if(!bStatus)
				{
					Messages.errorMsg = "Minimum Shares wasn't inputed into the field in Redemption Tab";
					return false;
				}
			}
			if(mapDetails.get("Redemption Type") != null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("Redemption Type"), Locators.LegalEntityMaster.RedemptionTab.DropDown.dllClickRedemptionType);
				if(!bStatus)
				{
					return false;
				}
			}
			if(mapDetails.get("Redemption Frequency") != null)
			{
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapDetails.get("Redemption Frequency"), Locators.LegalEntityMaster.RedemptionTab.DropDown.dllClickRedemptionFrequency, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");
				if(!bStatus)
				{
					return false;
				}
			}
			// Filling Out Notice Period Details		
			if(mapDetails.get("IsNoticePeriodApplicable")!= null)
			{
				if(mapDetails.get("IsNoticePeriodApplicable").equalsIgnoreCase("Yes"))
				{
					String sRadioLocatorYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isNoticePeriodApplicable.replace("YesOrNoReplaceValue", "Yes");
					bStatus = Elements.click(Global.driver, By.xpath(sRadioLocatorYes));
					if(!bStatus)
					{
						Messages.errorMsg = "Radio button wasn't selected for isNoticePeriodApplicable" + sRadioLocatorYes;
						return false;
					}
					if(mapDetails.get("NoticePeriodQuantity") != null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtNoticePeriodQuantity, mapDetails.get("NoticePeriodQuantity"));
						if(!bStatus)
						{
							Messages.errorMsg = "Notice Period for No of Days/Months/Years wasn't inputed into text field";
							return false;
						}
					}
					if(mapDetails.get("NoticePeriodQuantityType") != null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("NoticePeriodQuantityType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickNoticePeriodQuantityType);
						if(!bStatus)
						{
							return false;
						}
					}
					if(mapDetails.get("CalendarOrBusinessNoticeDays") != null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("CalendarOrBusinessNoticeDays"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickCalendarOrBusinessNoticeDays);
						if(!bStatus)
						{
							return false;
						}
						if(mapDetails.get("CalendarOrBusinessNoticeDays").equalsIgnoreCase("Business") && mapDetails.get("IncludeHolidays") != null)
						{
							if(mapDetails.get("IncludeHolidays").equalsIgnoreCase("Yes"))
							{
								String sIncludeHolidaysRadioYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isBusinessHolidaysToIncludeYes;
								bStatus = Elements.click(Global.driver, By.xpath(sIncludeHolidaysRadioYes));
								if(!bStatus)
								{
									Messages.errorMsg = "Radio button wasn't selected for isBusinessHolidaysToInclude " + sIncludeHolidaysRadioYes;
									return false;
								}
							}
							if(mapDetails.get("IncludeHolidays").equalsIgnoreCase("No"))
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
					if (mapDetails.get("NoticePeriodChargesType") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("NoticePeriodChargesType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickNoticePeriodChargesType);
						if(!bStatus)
						{
							return false;
						}
						if (!mapDetails.get("NoticePeriodChargesType").equalsIgnoreCase("None") && mapDetails.get("ChargesAmountOrBPSOrPercent") != null)
						{
							bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtAmountOrBPSOrPercent, mapDetails.get("ChargesAmountOrBPSOrPercent"));
							if(!bStatus)
							{
								Messages.errorMsg = "ChargesAmountOrBPSOrPercent wasn't entered into the text field text box may not present";
								return false;
							}
						}
					}
				}
				if(mapDetails.get("IsNoticePeriodApplicable").equalsIgnoreCase("No"))
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
			if (mapDetails.get("IsLockupApplying") != null)
			{
				if (mapDetails.get("IsLockupApplying").equalsIgnoreCase("Yes")) 
				{
					String sIsLockupApplyingYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isLockup.replace("YesOrNoReplaceValue", "Yes");
					bStatus = Elements.click(Global.driver, By.xpath(sIsLockupApplyingYes));
					if (!bStatus) 
					{
						Messages.errorMsg = "Radio button wasn't selected for IsLockupApplying" + sIsLockupApplyingYes;
						return false;												
					}
					if(mapDetails.get("LockupPeriodQuantity") != null)
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtLockupPeriodQuantity, mapDetails.get("LockupPeriodQuantity"));
						if(!bStatus)
						{
							Messages.errorMsg = "Lockup Period for No of Days/Months/Years wasn't inputed into text field";
							return false;
						}
					}
					if(mapDetails.get("LockupPeriodQuantityType") != null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("LockupPeriodQuantityType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.dllClickLockupPeriodQuantityType);
						if(!bStatus)
						{
							return false;
						}
					}
					if(mapDetails.get("CalendarOrBusinessLockupDays") != null)
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("CalendarOrBusinessLockupDays"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickCalendarOrBusinessLockupDays);
						if(!bStatus)
						{
							return false;
						}
						if(mapDetails.get("CalendarOrBusinessLockupDays").equalsIgnoreCase("Business") && mapDetails.get("LockupIncludeHolidays") != null)
						{
							if(mapDetails.get("LockupIncludeHolidays").equalsIgnoreCase("Yes"))
							{
								String sLockupIncludeHolidaysRadioYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isLockupBusinessHolidaysToInclude.replace("ReplaceIndexValue", "1");
								bStatus = Elements.click(Global.driver, By.xpath(sLockupIncludeHolidaysRadioYes));
								if(!bStatus)
								{
									Messages.errorMsg = "Radio button wasn't selected for isBusinessHolidaysToInclude " + sLockupIncludeHolidaysRadioYes;
									return false;
								}
							}
							if(mapDetails.get("LockupIncludeHolidays").equalsIgnoreCase("No"))
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
					if (mapDetails.get("LockupChargesType") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("LockupChargesType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickLockupChargesType);
						if (!bStatus) 
						{
							return false;							
						}
					}
					if (mapDetails.get("LockupRateMethod") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("LockupRateMethod"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickLockupRateMethod);
						if (!bStatus) 
						{
							return false;							
						}
						if (!mapDetails.get("LockupRateMethod").equalsIgnoreCase("None")) 
						{
							if (mapDetails.get("LockupRateMethod").equalsIgnoreCase("Fixed") && mapDetails.get("LockupFixedAmountOrPercent") != null) 
							{
								bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtLockupFixedAmountOrPercent, mapDetails.get("LockupFixedAmountOrPercent"));
								if (!bStatus) 
								{
									Messages.errorMsg = "Lockup Fixed Amount Or Percent text field wasn't visible";
									return false;							
								}
							}
							if (mapDetails.get("LockupRateMethod").equalsIgnoreCase("Slab")) 
							{
								if(mapDetails.get("LockupSlabFrom") != null && mapDetails.get("LockupSlabTo") != null && mapDetails.get("LockupSlabCharges") != null)
								{
									bStatus = removeTransactionCharges("charges-tired-fee-lockup");
									if(!bStatus){
										Messages.errorMsg = "Cannot Remove Lockup Rate Method Details";
										return false;
									}
									bStatus = LegalEntityAppFunctions.addLockupRateMethodDetails(mapDetails);
									if(!bStatus){
										Messages.errorMsg = "Lockup Rate Method Details Not Added";
										return false;
									}
								}
							}
						}
					}
				}
				if (mapDetails.get("IsLockupApplying").equalsIgnoreCase("No")) 
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
			if (mapDetails.get("IsTxnChargesApplicable") != null) 
			{
				if (mapDetails.get("IsTxnChargesApplicable").equalsIgnoreCase("Yes")) 
				{
					String sIsTxnChargesApplicableYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isTransactionChargesApplying.replace("YesOrNoReplaceValue", "Yes");
					bStatus = Elements.click(Global.driver, By.xpath(sIsTxnChargesApplicableYes));
					if (!bStatus) 
					{
						Messages.errorMsg = "Radio button wasn't selected for IsTxnChargesApplicable" + sIsTxnChargesApplicableYes;
						return false;												
					}
					if (mapDetails.get("EffectiveDate") != null) 
					{
						bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtTxnChargesEffectiveDate, mapDetails.get("EffectiveDate"));
						if (!bStatus) 
						{
							Messages.errorMsg = "Effective Date wasn't inputed for EffectiveDate filed";
							return false;												
						}
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LeagalEntitySearchPanel.lblLegalEntity);
					if(!bStatus)
					{
						return false;
					}
					if (mapDetails.get("TxnChrgesType") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("TxnChrgesType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickTransactionChargesType);
						if (!bStatus) 
						{
							Messages.errorMsg = "TransactionChargesType wasn't selected";
							return false;												
						}
					}
					if (mapDetails.get("TxnCalcBaseType") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("TxnCalcBaseType"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickTrnsactionChargesCalcBase);
						if (!bStatus) 
						{
							Messages.errorMsg = "TxnCalcBaseType wasn't selected";
							return false;												
						}
					}
					if (mapDetails.get("TxnRateMethod") != null) 
					{
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("TxnRateMethod"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickTransactionRateMethod);
						if (!bStatus) 
						{
							Messages.errorMsg = "TransactionRateMethod wasn't selected";
							return false;												
						}
						if(!mapDetails.get("TxnRateMethod").equalsIgnoreCase("None"))
						{
							if (mapDetails.get("TxnRateMethod").equalsIgnoreCase("Fixed") && mapDetails.get("TxnFixedFeeRate") != null) 
							{
								bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtTxnFixedFeeRate, mapDetails.get("TxnFixedFeeRate"));
								if (!bStatus) 
								{
									Messages.errorMsg = "FixedFeeRate text field wasn't visible";
									return false;												
								}
							}
							if (mapDetails.get("TxnRateMethod").equalsIgnoreCase("Slab") || mapDetails.get("TxnRateMethod").equalsIgnoreCase("Tiered")) 
							{
								if(mapDetails.get("Amount From")!=null && mapDetails.get("Amount To") != null && mapDetails.get("Charges") != null)
								{
									bStatus = removeTransactionCharges("charges-tired-fee-red");
									if(!bStatus){
										return false;
									}
									bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapDetails,"redemption" , "Red");
									if(!bStatus){
										Messages.errorMsg = "Rate Methode Amount Details Entery Failed for Redemption Tab";
										return false;
									}
								}
							}
						}
					}

					if (mapDetails.get("PartialRedChargesIsAsTxnCharges") != null) 
					{
						if (mapDetails.get("PartialRedChargesIsAsTxnCharges").equalsIgnoreCase("Yes")) 
						{
							String sPartialRedChargesIsAsTxnChargesYes = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isRedChargesSameAsTxnCharges.replace("YesOrNoReplaceValue", mapDetails.get("PartialRedChargesIsAsTxnCharges"));
							bStatus = Elements.click(Global.driver, By.xpath(sPartialRedChargesIsAsTxnChargesYes));
							if (!bStatus) 
							{
								Messages.errorMsg = "PartialRedChargesIsAsTxnCharges radio button wasn't visible";
								return false;												
							}
						}
						if (mapDetails.get("PartialRedChargesIsAsTxnCharges").equalsIgnoreCase("No")) 
						{
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LeagalEntitySearchPanel.lblLegalEntity);
							if(!bStatus)
							{
								return false;
							}
							String sPartialRedChargesIsAsTxnChargesNo = Locators.LegalEntityMaster.RedemptionTab.RadioButtons.isRedChargesSameAsTxnCharges.replace("YesOrNoReplaceValue", mapDetails.get("PartialRedChargesIsAsTxnCharges"));
							bStatus = Elements.click(Global.driver, By.xpath(sPartialRedChargesIsAsTxnChargesNo));
							if (!bStatus) 
							{
								Messages.errorMsg = "PartialRedChargesIsAsTxnCharges radio button wasn't visible";
								return false;												
							}
							if (mapDetails.get("PartialRedChargesEffectiveDate") != null) 
							{
								bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtRedPartialChargeAsTxnChargeEffectiveDate, mapDetails.get("PartialRedChargesEffectiveDate"));
								if (!bStatus) 
								{
									Messages.errorMsg = "PartialRedChargesEffectiveDate field wasn't visible";
									return false;												
								}
							}
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.LegalEntityMaster.LeagalEntitySearchPanel.lblLegalEntity);
							if(!bStatus)
							{
								return false;
							}
							if (mapDetails.get("PartialRedChargeCalcBase") != null) 
							{
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("PartialRedChargeCalcBase"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickPartialRedChargeRateCalcBase);
								if (!bStatus) 
								{
									Messages.errorMsg = "PartialRedChargeCalcBase DropDown wasn't Selected";
									return false;												
								}
							}										
							if (mapDetails.get("PartialRedChargeRateMethod") != null) 
							{
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapDetails.get("PartialRedChargeRateMethod"), Locators.LegalEntityMaster.RedemptionTab.DropDown.ddlClickPartialRedChargeRateMethod);
								if (!bStatus) 
								{
									Messages.errorMsg = "PartialRedChargeRateMethod DropDown wasn't Selected";
									return false;												
								}
								if(!mapDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("None"))
								{
									if (mapDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Fixed") && mapDetails.get("PartRedFixedFeeRate") != null)
									{
										bStatus = Elements.enterText(Global.driver, Locators.LegalEntityMaster.RedemptionTab.TextBox.txtPartRedFixedFeeRate, mapDetails.get("PartRedFixedFeeRate"));
										if (!bStatus) 
										{
											Messages.errorMsg = "PartRedFixedFeeRate field wasn't visible";
											return false;												
										}
									}
									if (mapDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Slab") || mapDetails.get("PartialRedChargeRateMethod").equalsIgnoreCase("Tiered"))
									{
										if(mapDetails.get("PartialRedChargeSlabOrTierFrom") != null && mapDetails.get("PartialRedChargeSlabOrTierTo") != null && mapDetails.get("PartialRedSlabOrTierCharge") != null){
											bStatus = removeTransactionCharges("redemPartialAmountTieredSlabRate");
											if(!bStatus){
												Messages.errorMsg = "Partial Rate Method Details Remove Button Fail to Click";
												return false;
											}
											bStatus = LegalEntityAppFunctions.addPartialRateMethodDetails(mapDetails);
											if(!bStatus){
												Messages.errorMsg = "Partial Rate Method Details Not Added";
												return false;
											}
										}
									}
								}
							}
						}			
					}
				}
				if (mapDetails.get("IsTxnChargesApplicable").equalsIgnoreCase("No")) 
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
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}	
	}

	//Enter all Exchange Details of Legal Entity Master
	public static boolean doFillExchageTab(Map<String, String> mapExchangeDetails){
		try{
			Thread.sleep(2000);
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators .LegalEntityMaster .Exchange .Label .lblExchange, Constants.lTimeOut);
			if(!bStatus){
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));		
			if(!bStatus){		
				Messages.errorMsg = "Legale Entity label Cannot Clickable";		
				return false;		
			}
			if(mapExchangeDetails.get("Incentive Fee Crystallization")!=null){
				if(mapExchangeDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Elements.click(Global.driver,Locators.LegalEntityMaster.Exchange.RadioButton.rbtnIncentiveFeeYes);
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization Radio button Yes is not clicked";
						return false;
					}
				}
				if(mapExchangeDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster .Exchange .RadioButton .rbtnIncentiveFeeNO );
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization Radio button No is not clicked";
						return false;
					}
				}
			}
			if(mapExchangeDetails.get("Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapExchangeDetails.get("Frequency"), Locators .LegalEntityMaster .Exchange.Dropdown .ddlFrequencyClick, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");
				if(!bStatus ){
					Messages .errorMsg ="Frequency Drop down  is not Selected";
					return false;
				}
			}

			if(mapExchangeDetails.get("Notice Period Applicable")!=null){
				if(mapExchangeDetails .get("Notice Period Applicable").equalsIgnoreCase("YES"))
				{
					bStatus =Elements .click(Global.driver, Locators .LegalEntityMaster .Exchange .RadioButton .rbtnNoticePeriodYes );
					if(!bStatus){
						Messages.errorMsg ="Notice Period Applicable Radio button Yes is not clicked";
						return false;
					}
					if(mapExchangeDetails.get("Notice Period")!=null){	
						bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Exchange .TextBox .txtNoticePeriod ,mapExchangeDetails.get("Notice Period"));
						if(!bStatus){
							Messages .errorMsg  =" Notice Period Date is not Entered";
							return false;
						}
					}
					if(mapExchangeDetails.get("NoticeMonth")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("NoticeMonth"), Locators .LegalEntityMaster .Exchange.Dropdown.ddlNoticePeroidMonthClick ,Locators .LegalEntityMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticeMonth Drop down  is not Selected";
							return false;
						}
					}
					if(mapExchangeDetails.get("NoticePeriodType")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("NoticePeriodType"), Locators .LegalEntityMaster .Exchange.Dropdown.ddlNoticePeroidCalenderClick,Locators .LegalEntityMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticePeriodType Drop down  is not Selected";
							return false;
						}
						if(mapExchangeDetails.get("NoticePeriodType").equalsIgnoreCase("Business") && mapExchangeDetails.get("Include Holidays")!=null)
						{
							if(mapExchangeDetails.get("Include Holidays").equalsIgnoreCase("YES")){
								bStatus=Elements .click(Global.driver , Locators .LegalEntityMaster .Exchange .RadioButton .rbtnIncludeHolidayYes );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday Yes Radio button is not clicked";
									return false;
								}
							}
							if(mapExchangeDetails.get("Include Holidays").equalsIgnoreCase("NO")){
								bStatus = Elements.click(Global .driver , Locators .LegalEntityMaster .Exchange .RadioButton .rbtnIncludeHolidayNo );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday NO Radio button is not clicked";
									return false;
								}
							}
						}
					}
					if(mapExchangeDetails.get("Notice Period Charges")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Notice Period Charges"), Locators .LegalEntityMaster .Exchange.Dropdown.ddlNoticePeroidChargesClick ,Locators .LegalEntityMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Notice Period Charges  is not Selected";
							return false;
						}
						if(!mapExchangeDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapExchangeDetails.get("Amount/BPS/Percent")!=null){
							bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Exchange .TextBox.txtAmountBpsPerc ,mapExchangeDetails.get("Amount/BPS/Percent"));
							if(!bStatus){
								Messages .errorMsg  =" Notice Period Date is not Entered";
								return false;
							}
						}
					}

				}
				if(mapExchangeDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus=Elements .click(Global.driver , Locators .LegalEntityMaster .Exchange .RadioButton .rbtnNoticePeriodNo );
					if(!bStatus ){
						Messages.errorMsg ="Notice Period Radio button No is not clicked";
						return false;
					}
				}
			}
			if(mapExchangeDetails.get("Transaction Charges")!=null)
			{
				if(mapExchangeDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements .click(Global.driver , Locators .LegalEntityMaster .Exchange .RadioButton .rbtnTransactionChargesYes );
					if(!bStatus ){
						Messages .errorMsg ="Transaction Charges Radio button YES is not clicked";
						return false;
					}
					if(mapExchangeDetails.get("Effective Date")!=null){
						bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Exchange .TextBox.txtEffectiveDate ,mapExchangeDetails.get("Effective Date"));
						if(!bStatus){
							Messages .errorMsg  ="Effective Date Period Date is not Entered";
							return false;
						}
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
					}
					if(mapExchangeDetails.get("Charges Type")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Charges Type"), Locators .LegalEntityMaster .Exchange.Dropdown .ddlTransactionChargesTypeClick ,Locators .LegalEntityMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Charges Type Drop down  is not Selected";
							return false;
						}
					}
					if(mapExchangeDetails.get("Calculation Base")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Calculation Base"), Locators .LegalEntityMaster .Exchange.Dropdown.ddlTransactionCalulationBase,Locators .LegalEntityMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Calculation Base Drop down  is not Selected";
							return false;
						}
					}
					if(mapExchangeDetails.get("Rate Method")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapExchangeDetails.get("Rate Method"), Locators .LegalEntityMaster .Exchange.Dropdown .ddlTransactionRateMethodClick ,Locators .LegalEntityMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Rate Method Drop down  is not Selected";
							return false;
						}
						if(mapExchangeDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							if(mapExchangeDetails.get("Fixed Fee Rate")!=null){
								bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Exchange .TextBox.txtFixedFeeRate ,mapExchangeDetails.get("Fixed Fee Rate"));
								if(!bStatus){
									Messages .errorMsg  ="Fixed Fee Rate Text is not Entered";
									return false;
								}
							}
						}
						if(mapExchangeDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapExchangeDetails.get("Rate Method").equalsIgnoreCase("Tiered"))
						{
							if(mapExchangeDetails.get("Amount From") != null && mapExchangeDetails.get("Amount From") != null && mapExchangeDetails.get("Amount From") != null)
							{
								bStatus = removeTransactionCharges("charges-tired-fee-exc");
								if(!bStatus){
									Messages.errorMsg = "Rate Methode Remove Button Failed to Click in Exchange Tab";
									return false;
								}
								bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapExchangeDetails,"exchange" , "Exchange");
								if(!bStatus){
									Messages.errorMsg = "Rate Methode Amount Details Entery Failed for Exchange Tab";
									return false;
								}
							}	
						}
					}
				}
				if(mapExchangeDetails.get("Transaction Charges")!=null){
					if(mapExchangeDetails.get("Transaction Charges").equalsIgnoreCase("No")){
						bStatus =Elements.click(Global.driver , Locators .LegalEntityMaster .Exchange .RadioButton .rbtnTransactionChargesNO );
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

	//Enter all Switch Details of Legal Entity Master
	public static boolean doFillSwitchTab(Map<String, String> mapSearchSwitchDetails){
		try{
			Thread.sleep(2000);
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators .LegalEntityMaster .Switch.Label .lblSwitch, Constants.lTimeOut);
			if(!bStatus){
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));		
			if(!bStatus){		
				Messages.errorMsg = "Legale Entity label Cannot Clickable";		
				return false;		
			}
			if(mapSearchSwitchDetails.get("Incentive Fee Crystallization")!=null){
				if(mapSearchSwitchDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Elements.click(Global.driver,Locators.LegalEntityMaster.Switch .RadioButton .rbtnIncentiveFeeYes );
					if(!bStatus){
						Messages .errorMsg  =" Incentive Fee Crystallization YES Radio Button Not Selected ";
						return false;
					}
				}
				if(mapSearchSwitchDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster .Switch .RadioButton .rbtnIncentiveFeeNO );
					if(!bStatus){
						Messages .errorMsg  =" Incentive Fee Crystallization No Radio Button Not Selected ";
						return false;
					}
				}
			}
			if(mapSearchSwitchDetails.get("Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapSearchSwitchDetails.get("Frequency"), Locators .LegalEntityMaster .Switch.Dropdown .ddlFrequencyClick, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");
				if(!bStatus ){
					Messages .errorMsg ="Frequency Drop down  is not Selected";
					return false;
				}
			}

			if(mapSearchSwitchDetails.get("Notice Period Applicable")!=null){
				if(mapSearchSwitchDetails .get("Notice Period Applicable").equalsIgnoreCase("YES")){
					bStatus =Elements .click(Global.driver, Locators .LegalEntityMaster .Switch .RadioButton .rbtnNoticePeriodYes );
					if(!bStatus){
						Messages.errorMsg ="Notice Period Applicable Radio button Yes is not clicked";
						return false;
					}
					if(mapSearchSwitchDetails.get("Notice Period")!=null){	
						bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Switch .TextBox .txtNoticePeriod ,mapSearchSwitchDetails.get("Notice Period"));
						if(!bStatus){
							Messages .errorMsg  =" Notice Period Date is not Entered";
							return false;
						}
					}
					if(mapSearchSwitchDetails.get("NoticeMonth")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("NoticeMonth"), Locators .LegalEntityMaster .Switch.Dropdown.ddlNoticePeroidMonthClick ,Locators .LegalEntityMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticeMonth Drop down  is not Selected";
							return false;
						}
					}
					if(mapSearchSwitchDetails.get("NoticePeriodType")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("NoticePeriodType"), Locators .LegalEntityMaster .Switch.Dropdown.ddlNoticePeroidCalenderClick,Locators .LegalEntityMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticePeriodType Drop down  is not Selected";
							return false;
						}
						if(mapSearchSwitchDetails.get("Include Holidays")!=null && mapSearchSwitchDetails.get("NoticePeriodType").equalsIgnoreCase("Business"))
						{
							if(mapSearchSwitchDetails.get("Include Holidays").equalsIgnoreCase("YES")){
								bStatus=Elements .click(Global.driver , Locators .LegalEntityMaster .Switch .RadioButton .rbtnIncludeHolidayYes );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday Yes Radio button is not clicked";
									return false;
								}
							}
							if(mapSearchSwitchDetails.get("Include Holidays").equalsIgnoreCase("NO")){
								bStatus = Elements.click(Global .driver , Locators .LegalEntityMaster .Switch.RadioButton .rbtnIncludeHolidayNo );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday NO Radio button is not clicked";
									return false;
								}
							}
						}
					}
					if(mapSearchSwitchDetails.get("Notice Period Charges")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Notice Period Charges"), Locators .LegalEntityMaster .Switch.Dropdown.ddlNoticePeroidChargesClick ,Locators .LegalEntityMaster .Exchange.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Notice Period Charges  is not Selected";
							return false;
						}
						if(!mapSearchSwitchDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapSearchSwitchDetails.get("Amount/BPS/Percent")!=null)
						{
							bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Switch .TextBox.txtAmountBpsPerc ,mapSearchSwitchDetails.get("Amount/BPS/Percent"));
							if(!bStatus){
								Messages .errorMsg  =" Notice Period Date is not Entered";
								return false;
							}
						}
					}
				}
				if(mapSearchSwitchDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus=Elements .click(Global.driver , Locators .LegalEntityMaster .Switch.RadioButton .rbtnNoticePeriodNo );
					if(!bStatus ){
						Messages.errorMsg ="Notice Period Radio button No is not clicked";
						return false;
					}
				}
			}
			if(mapSearchSwitchDetails.get("Transaction Charges")!=null)
			{
				if(mapSearchSwitchDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements .click(Global.driver , Locators .LegalEntityMaster .Switch.RadioButton .rbtnTransactionChargesYes );
					if(!bStatus ){
						Messages .errorMsg ="Transaction Charges Radio button YES is not clicked";
						return false;
					}
					if(mapSearchSwitchDetails.get("Effective Date")!=null){
						bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Switch .TextBox.txtEffectiveDate ,mapSearchSwitchDetails.get("Effective Date"));
						if(!bStatus){
							Messages .errorMsg  ="Effective Date Period Date is not Entered";
							return false;
						}
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
					}
					if(mapSearchSwitchDetails.get("Charges Type")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Charges Type"), Locators .LegalEntityMaster .Switch.Dropdown .ddlTransactionChargesTypeClick ,Locators .LegalEntityMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Charges Type Drop down  is not Selected";
							return false;
						}
					}
					if(mapSearchSwitchDetails.get("Calculation Base")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Calculation Base"), Locators .LegalEntityMaster .Switch.Dropdown.ddlTransactionCalulationBase,Locators .LegalEntityMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Calculation Base Drop down  is not Selected";
							return false;
						}
					}
					if(mapSearchSwitchDetails.get("Rate Method")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapSearchSwitchDetails.get("Rate Method"), Locators .LegalEntityMaster .Switch.Dropdown .ddlTransactionRateMethodClick ,Locators .LegalEntityMaster .Switch.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Rate Method Drop down  is not Selected";
							return false;
						}
						if(mapSearchSwitchDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							if(mapSearchSwitchDetails.get("Fixed Fee Rate")!=null){
								bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Switch .TextBox.txtFixedFeeRate ,mapSearchSwitchDetails.get("Fixed Fee Rate"));
								if(!bStatus){
									Messages .errorMsg  ="Fixed Fee Rate Text is not Entered";
									return false;
								}
							}
						}
						if(mapSearchSwitchDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapSearchSwitchDetails.get("Rate Method").equalsIgnoreCase("Tiered"))
						{
							if(mapSearchSwitchDetails.get("Amount From") != null && mapSearchSwitchDetails.get("Amount To") != null && mapSearchSwitchDetails.get("Charges") != null)
							{
								bStatus = removeTransactionCharges("charges-tired-fee-swi");
								if(!bStatus){
									Messages.errorMsg = "Rate Methode Remove Button Failed to Click for Switch Tab";
									return false;
								}
								bStatus = NewUICommonFunctions.addTransactionChargesSlaborTieredAmountDetails(mapSearchSwitchDetails,"switch" , "Switch");
								if(!bStatus){
									Messages.errorMsg = "Rate Methode Amount Details Entery Failed for Switch Tab";
									return false;
								}
							}		
						}
					}	
				}
				if(mapSearchSwitchDetails.get("Transaction Charges").equalsIgnoreCase("No"))
				{
					bStatus =Elements.click(Global.driver , Locators .LegalEntityMaster .Switch.RadioButton .rbtnTransactionChargesNO );
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

	//Enter all Transfer Details of Legal Entity Master
	public static boolean doFillTransferTab(Map<String, String> mapTransferDetails){
		try{
			Thread.sleep(2000);
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators .LegalEntityMaster.Transfer.Label .lblTransfer, Constants.lTimeOut);
			if(!bStatus){
				return false;
			}
			bStatus = NewUICommonFunctions.jsClick(By.xpath("//h4/p"));		
			if(!bStatus){		
				Messages.errorMsg = "Frequency label Not Clickable";		
				return false;		
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators .LegalEntityMaster .Transfer.Dropdown .ddlFrequencyClick, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Frequency Drop down is Not Visible";
				return false;
			}
			if(mapTransferDetails.get("Incentive Fee Crystallization")!=null){
				if(mapTransferDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Elements.click(Global.driver,Locators.LegalEntityMaster.Transfer .RadioButton .rbtnIncentiveFeeYes );
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization Radio button Yes is not clicked";
						return false;
					}
				}
				if(mapTransferDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					Elements.click(Global.driver, Locators.LegalEntityMaster .Transfer .RadioButton .rbtnIncentiveFeeNO );
					if(!bStatus){
						Messages.errorMsg ="Incentive Fee Crystallization Radio button No is not clicked";
						return false;
					}
				}
			}
			if(mapTransferDetails.get("Frequency")!=null){
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(mapTransferDetails.get("Frequency"), Locators .LegalEntityMaster .Transfer.Dropdown .ddlFrequencyClick, "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']");
				if(!bStatus ){
					Messages .errorMsg ="Frequency Drop down  is not Selected";
					return false;
				}
			}

			if(mapTransferDetails .get("Change In UBO")!=null){
				if(mapTransferDetails.get("Change In UBO").equalsIgnoreCase("YES")){
					bStatus = Elements.click(Global.driver,Locators.LegalEntityMaster.Transfer .RadioButton.rbtnChangeUBOYes );
					if(!bStatus){
						Messages.errorMsg ="Change In UBO Radio button Yes is not clicked";
						return false;
					}
				}
				if(mapTransferDetails.get("Change In UBO").equalsIgnoreCase("NO")){
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster .Transfer .RadioButton.btnChangeUBONo);
					if(!bStatus){
						Messages.errorMsg ="Change In UBO Radio button No is not clicked";
						return false;
					}
				}
			}

			if(mapTransferDetails.get("Notice Period Applicable")!=null)
			{
				if(mapTransferDetails .get("Notice Period Applicable").equalsIgnoreCase("YES"))
				{
					bStatus =Elements .click(Global.driver, Locators .LegalEntityMaster .Transfer .RadioButton .rbtnNoticePeriodYes );
					if(!bStatus){
						Messages.errorMsg ="Notice Period Applicable Radio button Yes is not clicked";
						return false;
					}
					if(mapTransferDetails.get("Notice Period")!=null){	
						bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Transfer .TextBox .txtNoticePeriod ,mapTransferDetails.get("Notice Period"));
						if(!bStatus){
							Messages .errorMsg  =" Notice Period Date is not Entered";
							return false;
						}
					}
					if(mapTransferDetails.get("NoticeMonth")!=null){
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("NoticeMonth"), Locators .LegalEntityMaster .Transfer.Dropdown.ddlNoticePeroidMonthClick ,Locators .LegalEntityMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticeMonth Drop down  is not Selected";
							return false;
						}
					}
					if(mapTransferDetails.get("NoticePeriodType")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("NoticePeriodType"), Locators .LegalEntityMaster .Transfer.Dropdown.ddlNoticePeroidCalenderClick,Locators .LegalEntityMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="NoticePeriodType Drop down  is not Selected";
							return false;
						}
						if(mapTransferDetails.get("Include Holidays")!=null && mapTransferDetails.get("NoticePeriodType").equalsIgnoreCase("Business"))
						{
							if(mapTransferDetails.get("Include Holidays").equalsIgnoreCase("YES")){
								bStatus=Elements .click(Global.driver , Locators .LegalEntityMaster .Transfer .RadioButton .rbtnIncludeHolidayYes );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday Yes Radio button is not clicked";
									return false;
								}
							}
							if(mapTransferDetails.get("Include Holidays").equalsIgnoreCase("NO")){
								bStatus = Elements.click(Global .driver , Locators .LegalEntityMaster .Transfer.RadioButton .rbtnIncludeHolidayNo );
								if(!bStatus ){
									Messages .errorMsg ="Include Holiday NO Radio button is not clicked";
									return false;
								}
							}
						}
					}	
					if(mapTransferDetails.get("Notice Period Charges")!=null)
					{
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Notice Period Charges"), Locators .LegalEntityMaster .Transfer.Dropdown.ddlNoticePeroidChargesClick ,Locators .LegalEntityMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Notice Period Charges  is not Selected";
							return false;
						}
						if(!mapTransferDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapTransferDetails.get("Amount/BPS/Percent")!=null){
							bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Transfer .TextBox.txtAmountBpsPerc ,mapTransferDetails.get("Amount/BPS/Percent"));
							if(!bStatus){
								Messages .errorMsg  =" Notice Period Date is not Entered";
								return false;
							}
						}
					}
				}
				if(mapTransferDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus=Elements .click(Global.driver , Locators .LegalEntityMaster .Transfer.RadioButton .rbtnNoticePeriodNo );
					if(!bStatus ){
						Messages.errorMsg ="Notice Period Radio button No is not clicked";
						return false;
					}
				}
			}
			if(mapTransferDetails.get("Transaction Charges")!=null)
			{
				if(mapTransferDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Elements .click(Global.driver , Locators .LegalEntityMaster .Transfer.RadioButton .rbtnTransactionChargesYes );
					if(!bStatus ){
						Messages .errorMsg ="Transaction Charges Radio button YES is not clicked";
						return false;
					}
					if(mapTransferDetails.get("Effective Date")!=null){
						bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Transfer .TextBox.txtEffectiveDate ,mapTransferDetails.get("Effective Date"));
						if(!bStatus){
							Messages .errorMsg  ="Effective Date Period Date is not Entered";
							return false;
						}
						bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.GeneralDetailsTab.RadioButton.commonLabel);
					}
					if(mapTransferDetails.get("Charges Type")!=null){
						//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(Locators.LegalEntityMaster.Transfer.Dropdown.ddlTransactionChargesTypeClick, mapTransferDetails.get("Charges Type"));
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Charges Type"), Locators .LegalEntityMaster .Transfer.Dropdown .ddlTransactionChargesTypeClick ,Locators .LegalEntityMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Charges Type Drop down  is not Selected";
							return false;
						}
					}
					if(mapTransferDetails.get("Calculation Base")!=null){
						//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(Locators.LegalEntityMaster.Transfer.Dropdown.ddlTransactionCalulationBase, mapTransferDetails.get("Calculation Base"));
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Calculation Base"), Locators .LegalEntityMaster .Transfer.Dropdown.ddlTransactionCalulationBase,Locators .LegalEntityMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Calculation Base Drop down  is not Selected";
							return false;
						}
					}
					if(mapTransferDetails.get("Rate Method")!=null)
					{
						//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(Locators.LegalEntityMaster.Transfer.Dropdown.ddlTransactionRateMethodClick, mapTransferDetails.get("Rate Method"));
						bStatus=NewUICommonFunctions .selectDropDownOfApplication(mapTransferDetails.get("Rate Method"), Locators .LegalEntityMaster .Transfer.Dropdown .ddlTransactionRateMethodClick ,Locators .LegalEntityMaster .Transfer.Dropdown.ddlSelectValue );
						if(!bStatus ){
							Messages .errorMsg ="Rate Method Drop down  is not Selected";
							return false;
						}

						if(mapTransferDetails.get("Rate Method").equalsIgnoreCase("Fixed"))
						{
							if(mapTransferDetails.get("Fixed Fee Rate")!=null){
								bStatus =Elements .enterText(Global.driver, Locators .LegalEntityMaster .Transfer .TextBox.txtFixedFeeRate ,mapTransferDetails.get("Fixed Fee Rate"));
								if(!bStatus){
									Messages .errorMsg  ="Fixed Fee Rate Text is not Entered";
									return false;
								}
							}
						}
						if(mapTransferDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapTransferDetails.get("Rate Method").equalsIgnoreCase("Tiered"))
						{	
							if(mapTransferDetails.get("Amount From")!=null && mapTransferDetails.get("Amount To")!=null && mapTransferDetails.get("Charges")!=null)
							{
								bStatus = removeTransactionCharges("charges-tired-fee-tra");
								if(!bStatus){
									Messages.errorMsg = "Rate Methode Remove Button Failed to Click";
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
				if(mapTransferDetails.get("Transaction Charges").equalsIgnoreCase("No")){
					bStatus =Elements.click(Global.driver , Locators .LegalEntityMaster .Transfer.RadioButton .rbtnTransactionChargesNO );
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

	//Adding New Legal Entity Master
	public static boolean AddNewLegalEntity(Map<String, Map<String, String>> objLECreationTabsMaps)
	{
		// FILLING OUT ALL THE TABS DETAILS DOWN HERE IN THE FOLLOWING METHODS.

		try {

			//Filling Legal Entity Details.

			bStatus = LegalEntityAppFunctions.doFillLegalEntityDetails(objLECreationTabsMaps.get("LegalEntityDetails"));
			if(!bStatus){
				Messages.errorMsg ="Legal Entity Details Not Entered. ERROR :"+Messages.errorMsg;
				return false;
			}

			//Filling General Details.

			bStatus = NewUICommonFunctions.NavigateToTab("General Details");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To General Details Tab.ERROR :"+Messages.errorMsg;
				return false;
			}
			bStatus = LegalEntityAppFunctions.doFillGeneralDetails(objLECreationTabsMaps.get("GeneralDetails"));
			if(!bStatus){
				Messages.errorMsg ="General Details Not Entered . ERROR :"+Messages.errorMsg;
				return false;
			}

			//Filling Other Details.

			bStatus = NewUICommonFunctions.NavigateToTab("Other Details");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To Other Details Tab.ERROR :"+Messages.errorMsg;
				return false;
			}
			bStatus = LegalEntityAppFunctions.doFillOtherDetails(objLECreationTabsMaps.get("OtherDetails"), objLECreationTabsMaps.get("GeneralDetails"));
			if(!bStatus){
				Messages.errorMsg ="Other Details Not Entered . ERROR :"+Messages.errorMsg;
				return false;
			}

			//Filling Subscription Details.

			bStatus = NewUICommonFunctions.NavigateToTab("Subscription");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To Subscription Tab.ERROR :"+Messages.errorMsg;
				return false;
			}
			bStatus = LegalEntityAppFunctions.doFillSubscriptionDetails(objLECreationTabsMaps.get("SubscriptionDetails"), objLECreationTabsMaps.get("GeneralDetails"));
			if(!bStatus){
				Messages.errorMsg ="Subscription Details Not Entered . ERROR :"+Messages.errorMsg;
				return false;
			}

			//Filling Redemption Details.

			bStatus = LegalEntityAppFunctions.doFillRedemptionTabDetails(objLECreationTabsMaps.get("RedemptionDetails"));
			if(!bStatus){
				Messages.errorMsg ="Redemption Details Not Entered . ERROR :"+Messages.errorMsg;
				return false;
			}

			bStatus = NewUICommonFunctions.NavigateToTab("Transfer");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To Other Details Tab.ERROR :"+Messages.errorMsg;
				return false;
			}

			//Filling Transfer Details.

			bStatus = LegalEntityAppFunctions.doFillTransferTab(objLECreationTabsMaps.get("TransferDetails"));
			if(!bStatus){
				Messages.errorMsg ="Transfer Details Not Entered . ERROR :"+Messages.errorMsg;
				return false;
			}

			bStatus = NewUICommonFunctions.NavigateToTab("Switch");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To Other Details Tab.ERROR :"+Messages.errorMsg;
				return false;
			}

			//Filling Switch Details.

			bStatus = LegalEntityAppFunctions.doFillSwitchTab(objLECreationTabsMaps.get("SwitchDetails"));
			if(!bStatus){
				Messages.errorMsg ="Switch Details Not Entered . ERROR :"+Messages.errorMsg;
				return false;
			}

			bStatus = NewUICommonFunctions.NavigateToTab("Exchange");
			if(!bStatus){
				Messages.errorMsg ="Not Navigated To Other Details Tab.ERROR :"+Messages.errorMsg;
				return false;
			}

			//Filling Exchange Details.

			bStatus = LegalEntityAppFunctions.doFillExchageTab(objLECreationTabsMaps.get("ExchangeDetails"));
			if(!bStatus){
				Messages.errorMsg ="Exchange Details Not Entered . ERROR :"+Messages.errorMsg;
				return false;
			}

			// PERFORMING OPERATION i.e. "SAVE" / "SAVE AS DRAFT" / "CANCEL" ON WHOLE LEGAL ENTITY SCREEN.

			if(objLECreationTabsMaps.get("LegalEntityDetails").get("OperationType")!=null){


				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Legal Entity", objLECreationTabsMaps.get("LegalEntityDetails").get("OperationType"));

				if(!bStatus){
					return false;
				}
				return true;

				/*if(objLECreationTabsMaps.get("LegalEntityDetails").get("OperationType").equalsIgnoreCase("Approve")){
					Elements.clickButton(Global.driver, Locators.FundFamilyMaster.Button.btnApprove);

					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
					if(!bStatus){
						Messages.errorMsg ="Legal Entity is Not Approved After click on Save Button";
						return false;
					}
					return true;
				}

				if(objLECreationTabsMaps.get("LegalEntityDetails").get("OperationType").equalsIgnoreCase("Save")){
					Elements.clickButton(Global.driver, Locators.LegalEntityMaster.Button.btnSave);

					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
					if(!bStatus){
						Messages.errorMsg ="Legal Entity is Not Saved After click on Save Button";
						return false;
					}
					return true;
				}
				if(objLECreationTabsMaps.get("LegalEntityDetails").get("OperationType").equalsIgnoreCase("Save As Draft")){
					Elements.clickButton(Global.driver,Locators.LegalEntityMaster.Button.btnSaveAsDraft);
					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
					if(!bStatus){
						Messages.errorMsg ="Client is Not Saved  After click on Save Draft Button";
						return false;
					}
					return true;
				}
				if(objLECreationTabsMaps.get("LegalEntityDetails").get("OperationType").equalsIgnoreCase("Cancel")){
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


	public static boolean addLockupRateMethodDetails(Map<String,String> mapDetails){
		try {
			String sAmountFrom = mapDetails.get("LockupSlabFrom");
			List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
			String sAmountTo = mapDetails.get("LockupSlabTo");
			List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
			String sCharges = mapDetails.get("LockupSlabCharges");
			List<String> aCharges = Arrays.asList(sCharges.split(","));
			for(int i=0;i<aAmountFrom.size();i++){
				if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
					String sLockupSlabFrom = Locators.LegalEntityMaster.RedemptionTab.TextBox.txtLockupSlabRangeFrom.replace("ReplaceIndexValue", ""+i+"");
					bStatus = Elements.enterText(Global.driver, By.xpath(sLockupSlabFrom), aAmountFrom.get(i).toString());
					if (!bStatus) 
					{
						Messages.errorMsg = "Lockup Slab 'From' text field wasn't visible";
						return false;							
					}
				}
				if(!aAmountTo.get(i).equalsIgnoreCase("None")){
					String sLockupSlabTo = Locators.LegalEntityMaster.RedemptionTab.TextBox.txtLockupSlabRangeTo.replace("ReplaceIndexValue", ""+i+"");
					bStatus = Elements.enterText(Global.driver, By.xpath(sLockupSlabTo), aAmountTo.get(i).toString());
					if (!bStatus) 
					{
						Messages.errorMsg = "Lockup Slab 'To' text field wasn't visible";
						return false;							
					}
				}
				if(!aCharges.get(i).equalsIgnoreCase("None")){
					String sLockupSlabCharges = Locators.LegalEntityMaster.RedemptionTab.TextBox.txtLockupSlabCharges.replace("ReplaceIndexValue", ""+i+"");
					bStatus = Elements.enterText(Global.driver, By.xpath(sLockupSlabCharges), aCharges.get(i).toString());
					if (!bStatus) 
					{
						Messages.errorMsg = "Lockup Slab 'To' text field wasn't visible";
						return false;							
					}
				}	
				if(i<aAmountFrom.size()-1){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='charges-tired-fee-lockup']//button[contains(@onclick,'addRedRateMethodList')]") );
					if(!bStatus){
						Messages.errorMsg = "Add More Button Not Clicked in Tab ";
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

	public static boolean addPartialRateMethodDetails(Map<String , String> mapDetails){
		try {
			String sPartialAmountFrom = mapDetails.get("PartialRedChargeSlabOrTierFrom");
			List<String> aAmountFrom = Arrays.asList(sPartialAmountFrom.split(","));
			String sPaartialAmountTo = mapDetails.get("PartialRedChargeSlabOrTierTo");
			List<String> aAmountTo = Arrays.asList(sPaartialAmountTo.split(","));
			String sCharges = mapDetails.get("PartialRedSlabOrTierCharge");
			List<String> aCharges = Arrays.asList(sCharges.split(","));
			for(int i=0;i<aAmountFrom.size();i++){
				if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
					String sPartialRedChargeSlabOrTierFrom = Locators.LegalEntityMaster.RedemptionTab.TextBox.txtTxnPartRedSlabOrTierRangeFrom.replace("ReplaceIndexValue", ""+i+"");
					bStatus = Elements.enterText(Global.driver, By.xpath(sPartialRedChargeSlabOrTierFrom), aAmountFrom.get(i).toString());
					if (!bStatus) 
					{
						Messages.errorMsg = "Lockup Slab 'From' text field wasn't visible";
						return false;							
					}
				}
				if(!aAmountTo.get(i).equalsIgnoreCase("None")){
					String sPartialRedChargeSlabOrTierTo = Locators.LegalEntityMaster.RedemptionTab.TextBox.txtTxnPartRedSlabOrTierRangeTo.replace("ReplaceIndexValue", ""+i+"");
					bStatus = Elements.enterText(Global.driver, By.xpath(sPartialRedChargeSlabOrTierTo), aAmountTo.get(i).toString());
					if (!bStatus) 
					{
						Messages.errorMsg = "Lockup Slab 'To' text field wasn't visible";
						return false;							
					}
				}
				if(!aCharges.get(i).equalsIgnoreCase("None")){
					String sPartialRedChargeSlabOrTierCharge = Locators.LegalEntityMaster.RedemptionTab.TextBox.txtTxnPartRedSlabOrTierCharges.replace("ReplaceIndexValue", ""+i+"");
					bStatus = Elements.enterText(Global.driver, By.xpath(sPartialRedChargeSlabOrTierCharge), aCharges.get(i).toString());
					if (!bStatus) 
					{
						Messages.errorMsg = "Lockup Slab 'To' text field wasn't visible";
						return false;							
					}	
				}		
				if(i<aAmountFrom.size()-1){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='redemPartialAmountTieredSlabRate']//button[contains(@onclick,'addRedPartialChargeDetailsList')]") );
					if(!bStatus){
						Messages.errorMsg = "Add More Button Not Clicked in Tab ";
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

	/*	public static boolean removeTradingRestrictions(String name ,String className){
		try {
			int sRemoveTradingRestrictions = Elements.getXpathCount(Global.driver, By.xpath("//button[@name='"+name+"']//em[@class='"+className+"']"));
			for(int j=1; j<=sRemoveTradingRestrictions ;j++){
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
				Messages.errorMsg = "Remove button failed Remove";
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Legal Entity Details Validation in Edit Screen Function.

	public static boolean verifyAllTabsInLegalEntityDetailsEditScreen(Map<String, Map<String, String>> objLECreationTabsMaps) {
		// TODO Auto-generated method stub
		try {	
			boolean bValidationStatus = true;

			//Verify LE Details tab

			bStatus = LegalEntityAppFunctions.doVerifyDetailsOfLegalEntityDetailsTab(objLECreationTabsMaps.get("LegalEntityDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify LegalEntityDetails Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}

			//Verify General Details tab

			bStatus = LegalEntityAppFunctions.doVerifyDetailsOfLegalEntityGeneralDetailsTab(objLECreationTabsMaps.get("GeneralDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Legal Entity GeneralDetails Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyDetailsOfLegalEntityGeneralDetailsTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Other Details tab

			bStatus = LegalEntityAppFunctions.doVerifyDetailsOfLegalEntityOtherDetailsTab(objLECreationTabsMaps.get("OtherDetails"), objLECreationTabsMaps.get("GeneralDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Legal Entity OtherDetails Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyDetailsOfLegalEntityOtherDetailsTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Subscription Details tab

			bStatus = LegalEntityAppFunctions.doVerifyDetailsOfSubscriptionTab(objLECreationTabsMaps.get("SubscriptionDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Legal Entity Subscription Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyDetailsOfSubscriptionTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Redemption Details tab

			bStatus = LegalEntityAppFunctions.doVerifyDetailsOfLegalEntityRedemptionTab(objLECreationTabsMaps.get("RedemptionDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Legal Entity Redemption Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyDetailsOfLegalEntityRedemptionTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Transfer Details tab

			bStatus = LegalEntityAppFunctions.doVerifyDetailsOfLegalEntityTransferTab(objLECreationTabsMaps.get("TransferDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Legal Entity Transfer Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyDetailsOfLegalEntityTransferTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Switch Details tab

			bStatus = LegalEntityAppFunctions.doVerifyDetailsOfLegalEntitySwitchTab(objLECreationTabsMaps.get("SwitchDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Legal Entity Switch Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyDetailsOfLegalEntitySwitchTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			//Verify Exchange Details tab

			bStatus = LegalEntityAppFunctions.doVerifyDetailsOfLegalEntityExchangeTab(objLECreationTabsMaps.get("ExchangeDetails"));
			if (!bStatus) {
				Reporting.logResults("Fail", "Verify Legal Entity Exchange Tab Details with actual", Messages.errorMsg);
				bValidationStatus = false;
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>doVerifyDetailsOfLegalEntityExchangeTab<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
			return bValidationStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	//Legal Entity Details Validation in Edit Screen of Other Details Tab.

	public static boolean doVerifyDetailsOfLegalEntityOtherDetailsTab(Map<String, String> mapOtherDetailsTab, Map<String, String> mapGeneralDetailsTab) {
		try {
			String sAppendMessage = "";
			boolean bValidateStatus = true;
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab3']"));
			if(!bStatus){
				sAppendMessage = sAppendMessage + " Wasn't navigated to Other Details Tab in the edit screen \n";
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.OtherDetails.TextBox.shareDecimalsForCalculations, Constants.lTimeOut);
			if(!bStatus){
				sAppendMessage = "[ Share Decimals For Calculations element is not visisble ]";
				return false;
			}
			if(mapOtherDetailsTab.get("Share Decimals For Calculation")!=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='shareDecimalsForCalculations' and contains(@value,'"+mapOtherDetailsTab.get("Share Decimals For Calculation")+"')]"));
				if(!bStatus){
					sAppendMessage = sAppendMessage + "[ Share Decimals For Calculations i.e. "+mapOtherDetailsTab.get("Share Decimals For Calculation")+" is Not matching with actual ]\n";
					bValidateStatus = false;
				}
			}
			if(mapOtherDetailsTab.get("Share Decimals For Display")!=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='shareDecimalsDisplay' and contains(@value,'"+mapOtherDetailsTab.get("Share Decimals For Display")+"')]"));
				if(!bStatus){
					sAppendMessage = sAppendMessage + "[ Share Decimals Display i.e. "+mapOtherDetailsTab.get("Share Decimals For Display")+" is Not matching with actual ]\n";
					bValidateStatus = false;
				}
			}
			if(mapOtherDetailsTab.get("NAV Decimals For Calculation")!=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='navDecimalsForCalculation' and contains(@value,'"+mapOtherDetailsTab.get("NAV Decimals For Calculation")+"')]"));
				if(!bStatus){
					sAppendMessage = sAppendMessage + "[ NAV Decimals For Calculation i.e. "+mapOtherDetailsTab.get("NAV Decimals For Calculation")+" is Not matching with actual ]\n";
					bValidateStatus = false;
				}
			}
			if(mapOtherDetailsTab.get("NAV Decimal For Display")!=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='navDecimalsDisplay' and contains(@value,'"+mapOtherDetailsTab.get("NAV Decimal For Display")+"')]"));
				if(!bStatus){
					sAppendMessage = sAppendMessage + "[ NAV Decimals Display i.e. "+mapOtherDetailsTab.get("NAV Decimal For Display")+" is Not matching with actual ]\n";
					bValidateStatus = false;
				}
			}
			if(mapGeneralDetailsTab.get("Unitized")!=null && mapGeneralDetailsTab.get("Unitized").equalsIgnoreCase("Unitized"))
			{
				if(mapOtherDetailsTab.get("Dividend Intended")!=null)
				{
					if(mapOtherDetailsTab.get("Dividend Intended").equalsIgnoreCase("Yes"))
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isDividendIntendedYes']//parent::span[@class='checked']"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Dividend Intended i.e. "+mapOtherDetailsTab.get("Dividend Intended")+" is Not matching with actual ]\n";
							bValidateStatus = false;
						}
						if(mapOtherDetailsTab.get("Dividend Frequency")!=null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='dividendList0.dividendFrequency' and contains(@value,'"+mapOtherDetailsTab.get("Dividend Frequency")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Dividend Frequency i.e. "+mapOtherDetailsTab.get("Dividend Frequency")+" is Not matching with actual ]\n";
								bValidateStatus = false;
							}
						}
						if(mapOtherDetailsTab.get("Dividend Base NAV Value")!=null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='dividendList0.dividendBaseNavValue' and contains(@value,'"+mapOtherDetailsTab.get("Dividend Base NAV Value")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Dividend Base NAV Value i.e. "+mapOtherDetailsTab.get("Dividend Base NAV Value")+" is Not matching with actual ]\n";
								bValidateStatus = false;
							}
						}
						if(mapOtherDetailsTab.get("Minimum Re-Investment Amount")!=null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='dividendList0.minimumReinvestmentAmount' and contains(@value,'"+mapOtherDetailsTab.get("Minimum Re-Investment Amount")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Minimum Re-Investment Amount i.e. "+mapOtherDetailsTab.get("Minimum Re-Investment Amount")+" is Not matching with actual ]\n";
								bValidateStatus = false;
							}
						}
						if(mapOtherDetailsTab.get("Dividend Determination Basis")!=null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_dividendList0.fkDividendDeterminationBasisIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapOtherDetailsTab.get("Dividend Determination Basis")+"')]"));						
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Dividend Determination Basis i.e. "+mapOtherDetailsTab.get("Dividend Determination Basis")+" is Not matching with actual ]\n";
								bValidateStatus = false;
							}
						}
						if(mapOtherDetailsTab.get("Dividend Re-Investment Method")!=null){
							//bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_dividendList0.fkDividendDeterminationBasisIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapOtherDetailsTab.get("Dividend Re-Investment Method")+"')]"));
							bStatus = NewUICommonFunctions.verifyTextInDropDown("Dividend Re-Investment Method", mapOtherDetailsTab.get("Dividend Re-Investment Method"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Dividend Re-Investment Method i.e. "+mapOtherDetailsTab.get("Dividend Re-Investment Method")+" is Not matching with actual ]\n";
								bValidateStatus = false;
							}
						}
						if(mapOtherDetailsTab.get("Dividend Reference")!=null)
						{
							//bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@id='select2-chosen-22' and contains(text(),'"+mapOtherDetailsTab.get("Dividend Reference")+"')]"));
							bStatus = NewUICommonFunctions.verifyTextInDropDown("Dividend Reference", mapOtherDetailsTab.get("Dividend Reference"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Dividend Referenc i.e. "+mapOtherDetailsTab.get("Dividend Reference")+" is Not matching with actual ]\n";
								bValidateStatus = false;
							}
						}
					}
					if(mapOtherDetailsTab.get("Dividend Intended").equalsIgnoreCase("No")){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isDividendIntendedNo']//parent::span[@class='checked']"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Dividend Intended i.e. "+mapOtherDetailsTab.get("Dividend Intended")+" is Not matching with actual ]\n";
							bValidateStatus = false;
						}
					}
				}
			}
			if(mapOtherDetailsTab.get("ERISA Applicable")!=null)
			{
				if(mapOtherDetailsTab.get("ERISA Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isErisaApplicableYes']//parent::span[@class='checked']"));
					if(!bStatus){
						sAppendMessage = sAppendMessage + "[ ERISA Applicable i.e. "+mapOtherDetailsTab.get("ERISA Applicable")+" is Not matching with actual ]\n";
						bValidateStatus = false;
					}
					if(mapOtherDetailsTab.get("ERISA Percentage")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='erisaTrackingList0.erisaPercentage' and contains(@value,'"+mapOtherDetailsTab.get("ERISA Percentage")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ ERISA Percentage i.e. "+mapOtherDetailsTab.get("ERISA Percentage")+" is Not matching with actual ]\n";
							bValidateStatus = false;
						}
					}
				}
				if(mapOtherDetailsTab.get("ERISA Applicable").equalsIgnoreCase("No"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isErisaApplicableNo']//parent::span[@class='checked']"));
					if(!bStatus){
						sAppendMessage = sAppendMessage + "[ ERISA Applicable i.e. "+mapOtherDetailsTab.get("ERISA Applicable")+" is Not matching with actual ]\n";
						bValidateStatus = false;
					}
				}
			}
			if(mapOtherDetailsTab.get("Trading Restriction Applicable")!=null)
			{
				if(mapOtherDetailsTab.get("Trading Restriction Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='TradingRestriction_Yes']//parent::span[@class='checked']"));
					if(!bStatus){
						sAppendMessage = sAppendMessage + "[ Trading Restriction Applicable i.e. "+mapOtherDetailsTab.get("Trading Restriction Applicable")+" is Not matching with actual ]\n";
						bValidateStatus = false;
					}
					if(mapOtherDetailsTab.get("Transaction Type") != null && mapOtherDetailsTab.get("Comments")!=null)
					{
						bStatus = verifyTradingRestriction(mapOtherDetailsTab);
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Trading Restriction Details is Not matching with actual "+Messages.errorMsg+" ]";
							bValidateStatus = false;
						}				
					}					
				}
				if(mapOtherDetailsTab.get("Trading Restriction Applicable").equalsIgnoreCase("No"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='TradingRestriction_No']//parent::span[@class='checked']"));
					if(!bStatus){
						sAppendMessage = sAppendMessage + "[ Trading Restriction Applicable i.e. "+mapOtherDetailsTab.get("Trading Restriction Applicable")+" is Not matching with actual ]\n";
						bValidateStatus = false;
					}
				}
			}
			if(mapOtherDetailsTab.get("Authorized Share Applicable")!=null)
			{
				if(mapOtherDetailsTab.get("Authorized Share Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='AuthorizedShare_Yes']//parent::span[@class='checked']"));
					if(!bStatus){
						sAppendMessage = sAppendMessage + "[ Authorized Share Applicable value i.e. "+mapOtherDetailsTab.get("Authorized Share Applicable")+" is Not matching with actual ] \n";
						bValidateStatus = false;
					}
					if(mapOtherDetailsTab.get("Authorized Shares")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='authorizedSharesId' and contains(@value,'"+mapOtherDetailsTab.get("Authorized Shares")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Authorized Share  value i.e. "+mapOtherDetailsTab.get("Authorized Shares")+" is Not matching with actual ] \n";
							bValidateStatus = false;
						}
					}	
				}
				if(mapOtherDetailsTab.get("Authorized Share Applicable").equalsIgnoreCase("No"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='AuthorizedShare_No']//parent::span[@class='checked']"));
					if(!bStatus){
						sAppendMessage = sAppendMessage + "[ Authorized Share Applicable value i.e. "+mapOtherDetailsTab.get("Authorized Share Applicable")+" is Not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
			}
			if(mapOtherDetailsTab.get("Accounting Location")!=null){
				//bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//span[@id='select2-chosen-24' and contains(text(),'"+mapOtherDetailsTab.get("Accounting Location")+"')]"));
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Accounting Location", mapOtherDetailsTab.get("Accounting Location"));
				if(!bStatus){
					sAppendMessage = sAppendMessage + "[ Accounting Location value i.e. "+mapOtherDetailsTab.get("Accounting Location")+" is Not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			if(mapOtherDetailsTab.get("Administrator Services Location")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Administrator Services Location", mapOtherDetailsTab.get("Administrator Services Location"));
				if(!bStatus){
					sAppendMessage = sAppendMessage + "[ Administrator Services Location value i.e. "+mapOtherDetailsTab.get("Administrator Services Location")+" is Not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			if(mapOtherDetailsTab.get("Investor Services Location")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Investor Services Location", mapOtherDetailsTab.get("Investor Services Location"));
				if(!bStatus){
					sAppendMessage = sAppendMessage + "[ Investor Services Location value i.e. "+mapOtherDetailsTab.get("Investor Services Location")+" is Not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			if(mapOtherDetailsTab.get("Registration")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Registration", mapOtherDetailsTab.get("Registration"));
				if(!bStatus){
					sAppendMessage = sAppendMessage + "[ Registration value i.e. "+mapOtherDetailsTab.get("Registration")+" is Not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendMessage;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Legal Entity Details Validation in Edit Screen of Redemption Tab.

	public static boolean doVerifyDetailsOfLegalEntityRedemptionTab(Map<String, String> mapRedemptionTabDetails) {
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
					sAppendMessage = sAppendMessage + "[ Given Minimum Amount i.e. "+mapRedemptionTabDetails.get("Minimum Amount")+" is not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			if(mapRedemptionTabDetails.get("Minimum Shares") != null)
			{
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionList0.minRedemShares' and contains(@value,'"+mapRedemptionTabDetails.get("Minimum Shares")+"')]"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Minimum Shares i.e. "+mapRedemptionTabDetails.get("Minimum Shares")+" is not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			if(mapRedemptionTabDetails.get("Redemption Type") != null)
			{
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_redemptionMethodRED']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("Redemption Type")+"')]"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Redemption Type i.e. "+mapRedemptionTabDetails.get("Redemption Type")+" is not matching with actual ] \n";
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
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodRedBusCal']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapRedemptionTabDetails.get("CalendarOrBusinessNoticeDays")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given CalendarOrBusinessNoticeDays i.e. "+mapRedemptionTabDetails.get("CalendarOrBusinessNoticeDays")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
						if(mapRedemptionTabDetails.get("CalendarOrBusinessNoticeDays").equalsIgnoreCase("Business") && mapRedemptionTabDetails.get("IncludeHolidays")!=null)
						{
							if(mapRedemptionTabDetails.get("IncludeHolidays").equalsIgnoreCase("Yes"))
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-redemptionNoticePeriodList0.isHolidaysInclude1']//parent::span[@class='checked']"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given IncludeHolidays i.e. "+mapRedemptionTabDetails.get("IncludeHolidays")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}
							if(mapRedemptionTabDetails.get("IncludeHolidays").equalsIgnoreCase("No"))
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-redemptionNoticePeriodList0.isHolidaysInclude2']//parent::span[@class='checked']"));
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
						if(mapRedemptionTabDetails.get("CalendarOrBusinessLockupDays").equalsIgnoreCase("Business"))
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
									//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
									String sAmountFrom = mapRedemptionTabDetails.get("LockupSlabFrom");
									List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									String sAmountTo = mapRedemptionTabDetails.get("LockupSlabTo");
									List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
									String sCharges = mapRedemptionTabDetails.get("LockupSlabCharges");
									List<String> aCharges = Arrays.asList(sCharges.split(","));
									for(int i=0;i<aAmountFrom.size();i++){

										if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+aAmountFrom.get(i)+" index ]";
												bValidateStatus = false;
											}	
										}

										if(!aAmountTo.get(i).equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+aAmountTo.get(i)+" index ";
												bValidateStatus = false;
											}
										}

										if(!aCharges.get(i).equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+aCharges.get(i)+" index ]";
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
						sAppendMessage = sAppendMessage + "[Given IsTxnChargesApplicable i.e. "+mapRedemptionTabDetails.get("IsTxnChargesApplicable")+" is not matching with actual ] \n";
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
									sAppendMessage = sAppendMessage + "[ Given TxnFixedFeeRate i.e. "+mapRedemptionTabDetails.get("TxnFixedFeeRate")+" is not matching with actual ] \n";
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
									//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
									String sAmountFrom = mapRedemptionTabDetails.get("Amount From");
									List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									String sAmountTo = mapRedemptionTabDetails.get("Amount To");
									List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
									String sCharges = mapRedemptionTabDetails.get("Charges");
									List<String> aCharges = Arrays.asList(sCharges.split(","));
									for(int i=0;i<aAmountFrom.size();i++)
									{
										if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aAmountTo.get(i).equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aCharges.get(i).equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index i.e. expected = "+aCharges.get(i).toString()+" ]";
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
								sAppendMessage = sAppendMessage + "[ Given PartialRedChargesIsAsTxnCharges i.e. "+mapRedemptionTabDetails.get("PartialRedChargesIsAsTxnCharges")+" is not matching with actual ] \n";
								bValidateStatus = false;
							}
						}
						if (mapRedemptionTabDetails.get("PartialRedChargesIsAsTxnCharges").equalsIgnoreCase("No")) 
						{
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsPartialAmountRedemptionApplicableNo']//parent::span[@class='checked']"));
							if(!bStatus ){
								sAppendMessage = sAppendMessage + "[ Given PartialRedChargesIsAsTxnCharges i.e. "+mapRedemptionTabDetails.get("PartialRedChargesIsAsTxnCharges")+" is not matching with actual  \n";
								bValidateStatus = false;
							}
							if (mapRedemptionTabDetails.get("PartialRedChargesEffectiveDate") != null) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='redemptionPartialAmountList0.wef' and contains(@value,'"+mapRedemptionTabDetails.get("PartialRedChargesEffectiveDate")+"')]"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given PartialRedChargesIsAsTxnCharges i.e. "+mapRedemptionTabDetails.get("PartialRedChargesEffectiveDate")+" is not matching with actual ] \n";
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
											sAppendMessage = sAppendMessage + "[ Given PartRedFixedFeeRate i.e. "+mapRedemptionTabDetails.get("PartRedFixedFeeRate")+" is not matching with actual ] \n";
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
											//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
											String sAmountFrom = mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierFrom");
											List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
											String sAmountTo = mapRedemptionTabDetails.get("PartialRedChargeSlabOrTierTo");
											List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
											String sCharges = mapRedemptionTabDetails.get("PartialRedSlabOrTierCharge");
											List<String> aCharges = Arrays.asList(sCharges.split(","));
											for(int i=0;i<aAmountFrom.size();i++){
												if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
													String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
													bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
													if(!bStatus){
														sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index ]";
														bValidateStatus = false;
													}
												}
												if(!aAmountTo.get(i).equalsIgnoreCase("None")){
													String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
													bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
													if(!bStatus){
														sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index ]";
														bValidateStatus = false;
													}
												}
												if(!aCharges.get(i).equalsIgnoreCase("None")){
													String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
													bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
													if(!bStatus){
														sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index ]";
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
			}
			if (mapRedemptionTabDetails.get("IsTxnChargesApplicable")!=null && mapRedemptionTabDetails.get("IsTxnChargesApplicable").equalsIgnoreCase("No")) 
			{
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsRedemptionChargeNo']//parent::span[@class='checked']"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given IsTxnChargesApplicable i.e. "+mapRedemptionTabDetails.get("IsTxnChargesApplicable")+" is not matching with actual ] \n";
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendMessage;
			return bValidateStatus;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Legal Entity Details Validation in Edit Screen of Transfer Tab.

	public static boolean doVerifyDetailsOfLegalEntityTransferTab(Map<String, String> mapTransferTabDetails) {
		try{
			String sAppendMessage = "";
			boolean bValidateStatus = true;

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab6']"));
			if(!bStatus){
				Messages .errorMsg ="Transfer Menu Tab is not Available";
				Assert.fail("Transfer Menu Tab is not Available");
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='s2id_transferList0.frequency.frequencyIdPk']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Frequency Dropdown Not visible";
				return false;
			}
			if(mapTransferTabDetails.get("Frequency") != null){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Legal Entity')]"));
				String actulalValue = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_transferList0.frequency.frequencyIdPk']//span"));
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_transferList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen') and text()='"+mapTransferTabDetails.get("Frequency").trim()+"']"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Frequency i.e. "+mapTransferTabDetails.get("Frequency")+" is not matching with actual "+actulalValue+" ] \n";
					bValidateStatus = false;
				}
			}
			if(mapTransferTabDetails .get("Change In UBO")!=null)
			{
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
			}

			if(mapTransferTabDetails.get("Incentive Fee Crystallization")!=null)
			{
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Legal Entity')]"));
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

			if(mapTransferTabDetails.get("Notice Period Applicable")!=null)
			{
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Legal Entity')]"));
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
							sAppendMessage = sAppendMessage + "[ Given NoticePeriodType i.e. "+mapTransferTabDetails.get("NoticePeriodType")+" is not matching with the actual ] \n";
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
							sAppendMessage = sAppendMessage + "[ Given Notice Period Charges i.e. "+mapTransferTabDetails.get("Notice Period Charges")+" is not matching with the actual ] \n";
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
						sAppendMessage = sAppendMessage + "[ Given Notice Period Applicable i.e. "+mapTransferTabDetails.get("Notice Period Applicable")+" is not matching with the actual ] \n";
						bValidateStatus = false;
					}
				}
			}
			if(mapTransferTabDetails.get("Transaction Charges")!=null)
			{
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
							sAppendMessage = sAppendMessage + "[ Given Effective Date i.e. "+mapTransferTabDetails.get("Effective Date")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
					}
					if(mapTransferTabDetails.get("Charges Type")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_transferChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapTransferTabDetails.get("Charges Type")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Charges Type i.e. "+mapTransferTabDetails.get("Charges Type")+" is not matching with the actual ] \n";
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
					if(mapTransferTabDetails.get("Rate Method")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_chargesRateMethodTRA']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapTransferTabDetails.get("Rate Method")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given Rate Method i.e. "+mapTransferTabDetails.get("Rate Method")+" is not matching with the actual ] \n";
							bValidateStatus = false;
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
							if(mapTransferTabDetails.get("Amount From")!=null && mapTransferTabDetails.get("Amount To")!=null && mapTransferTabDetails.get("Charges")!=null)
							{
								if (mapTransferTabDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapTransferTabDetails.get("Rate Method").equalsIgnoreCase("Tiered")) 
								{
									String sFromAmountLocator = "transferChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
									String sAmountToLocator = "transferChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
									String sChargesLocator = "transferChargesList0.chargeDetailsListReplaceIndexValue.rate";
									//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
									String sAmountFrom = mapTransferTabDetails.get("Amount From");
									List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									String sAmountTo = mapTransferTabDetails.get("Amount To");
									List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
									String sCharges = mapTransferTabDetails.get("Charges");
									List<String> aCharges = Arrays.asList(sCharges.split(","));
									for(int i=0;i<aAmountFrom.size();i++)
									{
										if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aAmountTo.get(i).equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aCharges.get(i).equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index with value : "+aCharges.get(i).toString()+" ]";
												bValidateStatus = false;
											}
										}
									}
								}
							}
						}
					}
				}
				if(mapTransferTabDetails.get("Transaction Charges")!=null){
					if(mapTransferTabDetails.get("Transaction Charges").equalsIgnoreCase("No")){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsTransferChargeNo']//parent::span[@class='checked']"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Transaction charges radio value is not matching with expected ]\n";
							bValidateStatus = false;
						}
					}
				}
			}
			Messages.errorMsg = sAppendMessage;
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//Legal Entity Details Validation in Edit Screen of Switch Tab.

	public static boolean doVerifyDetailsOfLegalEntitySwitchTab(Map<String, String> mapSwitchTabDetails) {
		try{
			String sAppendMessage = "";
			boolean bValidateStatus = true;

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab7']"));
			if(!bStatus){
				Messages .errorMsg ="Switch Menu Tab is not Available";
				Assert.fail("Switch Menu Tab is not Available");
				return false;
			}

			Wait.waitForElementVisibility(Global.driver, Locators.LegalEntityMaster.Switch.Label.lblSwitch, Constants.lTimeOut);
			
			if(mapSwitchTabDetails.get("Frequency") != null){
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Legal Entity')]"));
				Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='s2id_switchList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen') and text()='"+mapSwitchTabDetails.get("Frequency").trim()+"']"), 2);
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_switchList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen') and text()='"+mapSwitchTabDetails.get("Frequency").trim()+"']"));
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Frequency i.e. "+mapSwitchTabDetails.get("Frequency")+" is not matching with actual ] \n";
					bValidateStatus = false;
				}
			}

			if(mapSwitchTabDetails.get("Incentive Fee Crystallization")!=null)
			{
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Legal Entity')]"));
				if(mapSwitchTabDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("YES")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='switchList0.isTransactionCrystallized1']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Incentive Fee Crystallization i.e. "+mapSwitchTabDetails.get("Incentive Fee Crystallization")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
				if(mapSwitchTabDetails.get("Incentive Fee Crystallization").equalsIgnoreCase("NO")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='switchList0.isTransactionCrystallized2']//parent::span[@class='checked']"));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + "[ Given Incentive Fee Crystallization i.e. "+mapSwitchTabDetails.get("Incentive Fee Crystallization")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
				}
			}

			if(mapSwitchTabDetails.get("Notice Period Applicable")!=null)
			{
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Legal Entity')]"));
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
						if(!mapSwitchTabDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapSwitchTabDetails.get("Amount/BPS/Percent")!=null)
						{
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
					if(mapSwitchTabDetails.get("Rate Method")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_chargesRateMethodSWI']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSwitchTabDetails.get("Rate Method")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "Given  i.e. "+mapSwitchTabDetails.get("Rate Method")+" is not matching with the actual  \n";
							bValidateStatus = false;
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
							if(mapSwitchTabDetails.get("Amount From")!=null && mapSwitchTabDetails.get("Amount From")!=null && mapSwitchTabDetails.get("Amount From")!=null)
							{
								if (mapSwitchTabDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapSwitchTabDetails.get("Rate Method").equalsIgnoreCase("Tiered")) 
								{
									String sFromAmountLocator = "switchChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
									String sAmountToLocator = "switchChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
									String sChargesLocator = "switchChargesList0.chargeDetailsListReplaceIndexValue.rate";
									//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
									String sAmountFrom = mapSwitchTabDetails.get("Amount From");
									List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									String sAmountTo = mapSwitchTabDetails.get("Amount To");
									List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
									String sCharges = mapSwitchTabDetails.get("Charges");
									List<String> aCharges = Arrays.asList(sCharges.split(","));
									for(int i=0;i<aAmountFrom.size();i++)
									{
										if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aAmountTo.get(i).equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aCharges.get(i).equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
									}
								}
							}
						}
					}	
				}
				if(mapSwitchTabDetails.get("Transaction Charges")!=null)
				{
					if(mapSwitchTabDetails.get("Transaction Charges").equalsIgnoreCase("No")){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='rbIsSwitchChargeNo']//parent::span[@class='checked']"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Transaction charges radio value is not matching with expected ]\n";
							bValidateStatus = false;
						}
					}
				}
			}
			Messages.errorMsg = sAppendMessage;
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//Legal Entity Details Validation in Edit Screen of Exchange Tab.

	public static boolean doVerifyDetailsOfLegalEntityExchangeTab(Map<String, String> mapExchangeTabDetails) {
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
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Legal Entity')]"));
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='s2id_exchangeList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen') and text()='"+mapExchangeTabDetails.get("Frequency").trim()+"']"), 5);
				if(!bStatus ){
					sAppendMessage = sAppendMessage + "[ Given Frequency i.e. "+mapExchangeTabDetails.get("Frequency")+" is not matching with actual ]\n";
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
					if(mapExchangeTabDetails.get("NoticePeriodType")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodExchangeBusCal']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapExchangeTabDetails.get("NoticePeriodType")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given NoticePeriodType i.e. "+mapExchangeTabDetails.get("NoticePeriodType")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
						if(mapExchangeTabDetails.get("NoticePeriodType").equalsIgnoreCase("Business") && mapExchangeTabDetails.get("Include Holidays")!=null)
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
					if(mapExchangeTabDetails.get("Notice Period Charges")!=null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_noticePeriodTypeEXCHANGE']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapExchangeTabDetails.get("Notice Period Charges")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage + "[ Given Notice Period Charges i.e. "+mapExchangeTabDetails.get("Notice Period Charges")+" is not matching with the actual ] \n";
							bValidateStatus = false;
						}
						if(!mapExchangeTabDetails.get("Notice Period Charges").equalsIgnoreCase("None") && mapExchangeTabDetails.get("Amount/BPS/Percent")!=null)
						{
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='exchangeNoticePeriodList0.noticePeriodLockupDetailsList0.rate' and contains(@value,'"+mapExchangeTabDetails.get("Amount/BPS/Percent")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage + "[ Given Amount/BPS/Percent i.e. "+mapExchangeTabDetails.get("Amount/BPS/Percent")+" is not matching with the actual ] \n";
								bValidateStatus = false;
							}
						}
					}	
				}
				if(mapExchangeTabDetails.get("Notice Period Applicable").equalsIgnoreCase("No"))
				{
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

					if(mapExchangeTabDetails.get("Rate Method") != null)
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_chargesRateMethodEXC']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapExchangeTabDetails.get("Rate Method")+"')]"));
						if (!bStatus) {
							sAppendMessage = sAppendMessage + "[ Given  i.e. "+mapExchangeTabDetails.get("Rate Method")+" is not matching with the actual ] \n";
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
							if(mapExchangeTabDetails.get("Amount From")!=null && mapExchangeTabDetails.get("Amount To")!=null && mapExchangeTabDetails.get("Charges")!=null)
							{
								if (mapExchangeTabDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapExchangeTabDetails.get("Rate Method").equalsIgnoreCase("Tiered")) 
								{
									String sFromAmountLocator = "exchangeChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
									String sAmountToLocator = "exchangeChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
									String sChargesLocator = "exchangeChargesList0.chargeDetailsListReplaceIndexValue.rate";
									//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
									String sAmountFrom = mapExchangeTabDetails.get("Amount From");
									List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									String sAmountTo = mapExchangeTabDetails.get("Amount To");
									List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
									String sCharges = mapExchangeTabDetails.get("Charges");
									List<String> aCharges = Arrays.asList(sCharges.split(","));
									for(int i=0;i<aAmountFrom.size();i++)
									{
										if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index]";
												bValidateStatus = false;
											}
										}
										if(!aAmountTo.get(i).equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aCharges.get(i).equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
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
			Messages.errorMsg = sAppendMessage;
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}	

	//Legal Entity Details Validation in Edit Screen of LegalEntity Details Tab.

	public static boolean doVerifyDetailsOfLegalEntityDetailsTab(Map<String, String> mapLegalEntityDetailsTab){
		try{			
			boolean validateStatus = true;

			String appendMsg = "";
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab1']"));
			bStatus =Wait.waitForElementVisibility(Global.driver, By.xpath("//a[contains(text(),'Legal Entity Details')]"), Constants.lTimeOut);
			if(!bStatus){
				return false;
			}

			//Verify Client Name

			if(mapLegalEntityDetailsTab.get("Client Name") != null){
				String sClientName = Elements.getText(Global.driver, By.xpath("//select[@id='clientDefinition.clientId']/../div//span[1]"));
				if (sClientName == null || !mapLegalEntityDetailsTab.get("Client Name").equalsIgnoreCase(sClientName.trim())) {
					validateStatus = false;
					appendMsg = appendMsg + "[ **ERROR : "+ sClientName+" Client Name actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Client Name")+" ]\n";
				}	
			}

			//Verify FundFamily Name


			if(mapLegalEntityDetailsTab.get("Fund Family Name") != null){
				String sFundFamilyName = Elements.getText(Global.driver, By.xpath("//select[@id='cmbFundFamilyName']/../div//span[1]"));
				if (sFundFamilyName == null ||!mapLegalEntityDetailsTab.get("Fund Family Name").equalsIgnoreCase(sFundFamilyName.trim())) {
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sFundFamilyName+" Fund Family actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Client Name")+" ]\n";
				}	
			}

			//Verify Legal Entity Name


			if(mapLegalEntityDetailsTab.get("Legal Entity Name") != null){
				String sLegalEnityName = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='legalEntityDescription']"),"value");
				if (sLegalEnityName == null ||!mapLegalEntityDetailsTab.get("Legal Entity Name").equalsIgnoreCase(sLegalEnityName.trim())) {
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sLegalEnityName+" Legal Entity Name actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Legal Entity Name")+" ]\n";
				}	
			}
			
			//Verify Legal Entity code
			if(mapLegalEntityDetailsTab.get("Legal Entity Code")!=null){
				String sLegalEnityCode = Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='legalEntityCode']"),"value");
				if (sLegalEnityCode == null ||!mapLegalEntityDetailsTab.get("Legal Entity Code").equalsIgnoreCase(sLegalEnityCode.trim())) {
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sLegalEnityCode+" Legal Entity Code actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Legal Entity Code")+" ]\n";
				}	
			}

			//Verify Legal Entity Type


			if(mapLegalEntityDetailsTab.get("Legal Entity Type") != null){
				String sLegalEntityType = Elements.getText(Global.driver, By.xpath("//select[@id='fkLegalEntityType']/../div//span[1]"));
				if (sLegalEntityType == null || !mapLegalEntityDetailsTab.get("Legal Entity Type").equalsIgnoreCase(sLegalEntityType.trim())) {
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sLegalEntityType+"  Legal Entity Type actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Legal Entity Type")+" ]\n";
				}	
			}

			//Verify Currency

			if(mapLegalEntityDetailsTab.get("Currency") != null){
				String sCurrency = Elements.getText(Global.driver, By.xpath("//select[@id='cmbBaseCurrency']/../div//span[1]"));
				if (sCurrency == null || !mapLegalEntityDetailsTab.get("Currency").equalsIgnoreCase(sCurrency.trim())) {
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sCurrency+" Currency   actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Currency")+" ]\n";
				}	
			}
			
			if(mapLegalEntityDetailsTab.get("Accounting Id")!=null){
				// Verify Accounting Id
				String sAccoutID = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='accountingId']"),"value");
				if(sAccoutID==null || !sAccoutID.equalsIgnoreCase(mapLegalEntityDetailsTab.get("Accounting Id"))){
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sAccoutID+"Accounting Id  actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Accounting Id")+" ]\n";
				}
			}

			
			if(mapLegalEntityDetailsTab.get("Billing Codes")!=null){
				//Verify Billing codes
				String sBillingcode = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='billingCodes']"),"value");
				if(sBillingcode==null || !sBillingcode.equalsIgnoreCase(mapLegalEntityDetailsTab.get("Billing Codes"))){
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sBillingcode+"Billing codes  actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Billing Codes")+" ]\n";
				}
			}

			//Verify Accounting System

			if(mapLegalEntityDetailsTab.get("Accounting System") != null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Accounting System/Version", mapLegalEntityDetailsTab.get("Accounting System"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR :  Accounting System actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Accounting System")+" ]\n";
				}	
			}
			//Verify Legal Entity Domicile

			if(mapLegalEntityDetailsTab.get("Legal Entity Domicile") != null){
				String sLegalDomicileType= Elements.getText(Global.driver, By.xpath("//select[@id='investmentRegion.countryIdPk']/../div//span[1]"));
				if (sLegalDomicileType == null || !mapLegalEntityDetailsTab.get("Legal Entity Domicile").equalsIgnoreCase(sLegalDomicileType.trim())) {
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sLegalDomicileType+" Legal Entity Domicile  actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Legal Entity Domicile")+" ]\n";
				}	
			}
			
			//Verify  FATCA GIIN 
			if(mapLegalEntityDetailsTab.get("FATCA GIIN")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("FATCA GIIN", mapLegalEntityDetailsTab.get("FATCA GIIN"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+"[**ERROR : FATCA GIIN actual is not equal to Expected:    "+mapLegalEntityDetailsTab.get("FATCA GIIN")+" ]\n";
				}
			}
			
			//Verify Entity Type

			if(mapLegalEntityDetailsTab.get("Entity Type") != null){
				String SEntityType=Elements .getText(Global .driver , By.xpath("//select[@id='masterFeeder']/../div//span[1]"));
				if (SEntityType == null || !mapLegalEntityDetailsTab.get("Entity Type").equalsIgnoreCase(SEntityType.trim())) {
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+SEntityType+"Entity Type  actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Entity Type")+" ]\n";
				}
				if(mapLegalEntityDetailsTab.get("Entity Type").equalsIgnoreCase("Feeder")){
					List<String> sNoofMastersSelected = Arrays.asList(mapLegalEntityDetailsTab.get("Master Fund").split(",")); 
					for (int index=1; index <= sNoofMastersSelected.size(); index++) 
					{
						//System.out.println(sNoOfHolidyCalendarsToSelect.get(index-1)+" <<<<<<<<<<<<<<<<<<<<<<<");

						String sIndvMaster = Elements.getText(Global .driver , By.xpath("//select[@id='masterFundList']/../div/ul//li["+index+"]//div"));
						//System.out.println(SHolidayCalender+"  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

						if (sIndvMaster == null || !sNoofMastersSelected.contains(sIndvMaster.trim())) {
							validateStatus = false;
							appendMsg = appendMsg +"[ **ERROR : "+ sIndvMaster + " ....Master Fund  actual is not equal to Expected: "+sNoofMastersSelected.get(index-1)+" ]\n";
						}
					}
				}
			}
			
			if(mapLegalEntityDetailsTab.get("Accounting Method") != null){
				//Verify Accounting Method
				String SAccoutMethod=Elements .getText(Global .driver , By.xpath("//select[@id='accountingMethod.referenceIdPk']/../div//span[1]"));
				if (SAccoutMethod == null || !mapLegalEntityDetailsTab.get("Accounting Method").equalsIgnoreCase(SAccoutMethod.trim())) {
					validateStatus = false;
					appendMsg =appendMsg+"[ **ERROR : "+SAccoutMethod+"Accounting Method  actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Accounting Method")+" ]\n";
				}	
			}

			// Verify Investment Strategy

			if(mapLegalEntityDetailsTab.get("Investment Strategy") != null){
				String sInvestmentStrategy=Elements .getText(Global .driver , By.xpath("//select[@id='investmentStarategy']/../div//span[1]"));
				if (sInvestmentStrategy == null || !mapLegalEntityDetailsTab.get("Investment Strategy").equalsIgnoreCase(sInvestmentStrategy.trim())) {
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sInvestmentStrategy+"Verify Investment Strategy  actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("Investment Strategy")+" ]\n";
				}	
			}

			// verify Side Pocket Investment
			if(mapLegalEntityDetailsTab.get("Side Pocket Investment") != null){
				if(mapLegalEntityDetailsTab.get("Side Pocket Investment").equalsIgnoreCase("Yes")){
					bStatus = Verify.verifyElementPresent(Global.driver,By.xpath("//div[@id='uniform-isSidePocketInvestmentYes']//parent::span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ **ERROR : "+"Side Pocket Investment actual is not equal to Expected ]\n";
					}
				}
				if(mapLegalEntityDetailsTab.get("Side Pocket Investment").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementPresent(Global.driver,By.xpath("//div[@id='uniform-isSidePocketInvestmentNo']//parent::span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ Side Pocket Investment actual is not equal to Expected ]\n";
					}	
				}
			}

			//Verify External ID'S
			if(mapLegalEntityDetailsTab.get("External Id1")!=null){
				String sExt1 = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='externalId1']"),"value");
				if(sExt1==null || !sExt1.equalsIgnoreCase(mapLegalEntityDetailsTab.get("External Id1"))){
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sExt1+" External ID1 actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("External Id1")+" ]\n";
				}
			}
			if(mapLegalEntityDetailsTab.get("External Id2")!=null){
				String sExt2 = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='externalId2']"),"value");
				if(sExt2==null || !sExt2.equalsIgnoreCase(mapLegalEntityDetailsTab.get("External Id2"))){
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sExt2+" External ID2  actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("ExtId2")+" ]\n";
				}
			}
			if(mapLegalEntityDetailsTab.get("External Id3")!=null){
				String sExt3 = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='externalId3']"),"value");
				if(sExt3==null || !sExt3.equalsIgnoreCase(mapLegalEntityDetailsTab.get("External Id3"))){
					validateStatus = false;
					appendMsg = appendMsg+"[ **ERROR : "+sExt3+" External ID3 actual is not equal to Expected: "+mapLegalEntityDetailsTab.get("External Id3")+" ]\n";
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

	//Legal Entity Details Validation in Edit Screen of General Details Tab.

	public static boolean doVerifyDetailsOfLegalEntityGeneralDetailsTab(Map<String, String> mapGeneralDetailsTab){
		try{
			boolean validateStatus = true;
			String appendMsg1 = "";
			//Thread.sleep(1000);

			Wait.waitForElementPresence(Global.driver, By.xpath("//a[text()=' General Details']"),Constants.lTimeOut);
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab2']"));	

			//Thread.sleep(1000);
			Wait.waitForElementVisibility(Global.driver, By.xpath("//select[@id='fkIsUnitizeOrNonUnitize']/../div//span[1]"),Constants.lTimeOut);

			//Verify Unitized 

			if(mapGeneralDetailsTab.get("Unitized") != null)
			{
				if(mapGeneralDetailsTab.get("Unitized").equalsIgnoreCase("Unitized"))
				{
					String SUnitized=Elements .getText(Global .driver , By.xpath("//select[@id='fkIsUnitizeOrNonUnitize']/../div//span[1]"));
					if (!mapGeneralDetailsTab.get("Unitized").equalsIgnoreCase(SUnitized.trim())) {
						validateStatus = false;
						appendMsg1 =appendMsg1+"[ **ERROR : "+ SUnitized+"...Unitized  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Unitized")+" ]\n";
					}	

					Wait.waitForElementVisibility(Global.driver, By.xpath("//select[@id='fkIsUnitizeOrNonUnitize']/../div//span[1]"),Constants.lTimeOut);
					String sUnitDesp=Elements .getText(Global .driver , By.xpath("//select[@id='unitsDescription.referenceIdPk']/../div//span[1]"));
					if(mapGeneralDetailsTab.get("Unit Description") != null){
						if (!mapGeneralDetailsTab.get("Unit Description").equalsIgnoreCase(sUnitDesp.trim())) {
							validateStatus = false;
							appendMsg1 = appendMsg1+"[ **ERROR : "+sUnitDesp+"...Unit Description  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Unit Description")+" ]\n";
						}	
					}

					String SUnitValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='unitValue']"),"value");

					if(SUnitValue != null && mapGeneralDetailsTab.get("Unit Value") != null){	
						int isEqual = Float.compare(Float.parseFloat(SUnitValue), Float.parseFloat(mapGeneralDetailsTab.get("Unit Value")));

						if (isEqual > 0 || isEqual < 0) {
							validateStatus = false;
							appendMsg1 = appendMsg1+"[ **ERROR : "+SUnitValue+"...Unit Value  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Unit Value")+" ]\n";
						}	
					}


					String SNewIssuePer = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='deMinimusPercentageValue']"),"value");
					if(SNewIssuePer != null &&  mapGeneralDetailsTab.get("New Issue Percentage") != null){

						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(SNewIssuePer, mapGeneralDetailsTab.get("New Issue Percentage"));

						if (!bStatus) {
							validateStatus = false;
							appendMsg1 = appendMsg1+"[ **ERROR : "+SNewIssuePer+"....New Issue Percentage  actual is not equal to Expected: "+mapGeneralDetailsTab.get("New Issue Percentage")+" ]\n";
						}	
					}


					if(mapGeneralDetailsTab.get("Series RollUp") != null){
						if(mapGeneralDetailsTab.get("Series RollUp").equalsIgnoreCase("Yes"))
						{
							bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-seriesRollUpYes']//parent::span[@class='checked']"));
							if(!bStatus){
								validateStatus = false;
								appendMsg1 = appendMsg1+"[ **ERROR : "+" ..Series RollUp LegalEntities actual is not equal to Expected ]\n";
							}

							String SSeriesFreq=Elements .getText(Global .driver , By.xpath("//select[@id='seriesRollUpFrequency.frequencyIdPk']/../div//span[1]"));
							if(SSeriesFreq!=null && mapGeneralDetailsTab.get("Series RollUp Frequency") != null){
								if (!mapGeneralDetailsTab.get("Series RollUp Frequency").equalsIgnoreCase(SSeriesFreq.trim())) {
									validateStatus = false;
									appendMsg1 =appendMsg1+"[ **ERROR : "+ SSeriesFreq+"...Series RollUp Frequency  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Series RollUp Frequency")+" ]\n";
								}	
							}
						}
						if(mapGeneralDetailsTab.get("Series RollUp").equalsIgnoreCase("No"))
						{
							bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-seriesRollUpNo']//parent::span[@class='checked']"));
							if(!bStatus){
								validateStatus = false;
								appendMsg1 = appendMsg1+"[ **ERROR : "+"... Series Roll Up ...actual is not equal to Expected ]\n";
							}
							if(mapGeneralDetailsTab.get("Equalization") != null)
							{
								if(mapGeneralDetailsTab.get("Equalization").equalsIgnoreCase("Yes"))
								{
									bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-equalizationYes']//parent::span[@class='checked']"));
									if(!bStatus){
										validateStatus = false;
										appendMsg1 = appendMsg1+"[ **ERROR : "+"... Equalization ... actual is not equal to Expected ]\n";
									}
									
									
									if(mapGeneralDetailsTab.get("Equalization Method")!=null){
										bStatus=NewUICommonFunctions.verifyTextInDropDown("Equalization Method", mapGeneralDetailsTab.get("Equalization Method"));
										if (!bStatus) {
											validateStatus = false;
											appendMsg1 =appendMsg1+"[ **ERROR : Equalization Method  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Equalization Method")+" ]\n";
										}	
									}
								}
								if(mapGeneralDetailsTab.get("Equalization").equalsIgnoreCase("No"))
								{
									bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-equalizationNo']//parent::span[@class='checked']"));
									if(!bStatus){
										validateStatus = false;
										appendMsg1 = appendMsg1+"[ **ERROR : "+"....Equalization actual is not equal to Expected ]\n";
									}
								}
							}
						}
					}
				}
				if(mapGeneralDetailsTab.get("Unitized").equalsIgnoreCase("Non Unitized"))
				{
					String SUnitized=Elements .getText(Global .driver , By.xpath("//select[@id='fkIsUnitizeOrNonUnitize']/../div//span[1]"));
					if (!mapGeneralDetailsTab.get("Unitized").equalsIgnoreCase(SUnitized.trim())) {
						validateStatus = false;
						appendMsg1 =appendMsg1+"[ **ERROR : "+ SUnitized+"...Unitized  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Unitized")+" ]\n";
					}
				}
			}
			//Verify Frequency Details
			String ReportingFreq=Elements .getText(Global .driver , By.xpath("//select[@id='reportingFrequency.frequencyIdPk']/../div//span[1]"));
			if(mapGeneralDetailsTab.get("Reporting Frequency") != null){
				if (ReportingFreq==null || !mapGeneralDetailsTab.get("Reporting Frequency").equalsIgnoreCase(ReportingFreq.trim())) {
					validateStatus = false;
					appendMsg1 = appendMsg1+"[ **ERROR : "+ReportingFreq+".....Reporting Frequency actual is not equal to Expected: "+mapGeneralDetailsTab.get("Reporting Frequency")+" ]\n";
				}	
			}

			String sEstimationFreq=Elements .getText(Global .driver , By.xpath("//select[@id='estimationFrequency']/../div//span[1]"));
			if(mapGeneralDetailsTab.get("Estimation Frequency") != null){
				if (sEstimationFreq==null ||!mapGeneralDetailsTab.get("Estimation Frequency").equalsIgnoreCase(sEstimationFreq.trim())) {
					validateStatus = false;
					appendMsg1 = appendMsg1+"[ **ERROR : "+sEstimationFreq+".....Estimation Frequency actual is not equal to Expected: "+mapGeneralDetailsTab.get("Estimation Frequency")+" ]\n";
				}	
			}

			String sCrystalizationFreq=Elements .getText(Global .driver , By.xpath("//select[@id='crystalizationFrequency.frequencyIdPk']/../div//span[1]"));
			if(mapGeneralDetailsTab.get("Crystalization Frequency") != null){
				if (sCrystalizationFreq==null || !mapGeneralDetailsTab.get("Crystalization Frequency").equalsIgnoreCase(sCrystalizationFreq.trim())) {
					validateStatus = false;
					appendMsg1 = appendMsg1+"[ **ERROR : "+sCrystalizationFreq+"....Crystalization Frequency actual is not equal to Expected: "+mapGeneralDetailsTab.get("Crystalization Frequency")+" ]\n";
				}	
			}
			
			if(mapGeneralDetailsTab.get("Fee to be posted in Master") != null){
				if(mapGeneralDetailsTab.get("Fee to be posted in Master").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Fee to be posted in Master')]//following::div//input[@id='seriesRollUpYes']//parent::span[@class='checked']"),Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Fee to be Posted in Master 'Yes' Radio button should be selected but Actual is not Selected";
						//return false;
					}
					if(mapGeneralDetailsTab.get("FeeTypeManagement") != null){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='isManagementFee1']"));
						if(mapGeneralDetailsTab.get("FeeTypeManagement").equalsIgnoreCase("Yes")){
							if(!bStatus){
								Messages.errorMsg = "Management Fee Check box should be checked but Actual is not chekced";
								return  false;
							}
						}
						if(mapGeneralDetailsTab.get("FeeTypeManagement").equalsIgnoreCase("No")){
							if(bStatus){
								Messages.errorMsg = "Management Fee Check box should not checked but Actual is Checked";
								return  false;
							}
						}
					}
					if(mapGeneralDetailsTab.get("FeeTypeIncentive") != null){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@id='isIncentiveFee1']"));
						if(mapGeneralDetailsTab.get("FeeTypeIncentive").equalsIgnoreCase("Yes")){
							if(!bStatus){
								Messages.errorMsg = "Incentive Fee Check box should be checked but Actual is not chekced";
								return  false;
							}
						}
						if(mapGeneralDetailsTab.get("FeeTypeIncentive").equalsIgnoreCase("No")){
							if(bStatus){
								Messages.errorMsg = "Incentive Fee Check box should not checked but Actual is Checked";
								return  false;
							}
						}
					}
				}
				if(mapGeneralDetailsTab.get("Fee to be posted in Master").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[contains(normalize-space(),'Fee to be posted in Master')]//following::div//input[@id='seriesRollUpNo']//parent::span[@class='checked']"),Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Fee to be Posted in Master 'No' Radio button should be selected but Actual is not Selected";
						//return false;
					}
				}
			}

			
			String sInitialOffering = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='incorporationDate']"),"value");
			if(mapGeneralDetailsTab.get("Initial Offering Date") != null){
				if (sInitialOffering==null || !mapGeneralDetailsTab.get("Initial Offering Date").equalsIgnoreCase(sInitialOffering.trim())) {
					validateStatus = false;
					appendMsg1 = appendMsg1+"[ **ERROR : "+sInitialOffering+"......Initial Offering Date  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Initial Offering Date")+" ]\n";
				}	
			}

			//Verify Dates
			String sStartDate = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='initialOfferingDate']"),"value");
			if(mapGeneralDetailsTab.get("Start Date") != null){
				if (sStartDate==null ||  !mapGeneralDetailsTab.get("Start Date").equalsIgnoreCase(sStartDate.trim())) {
					validateStatus = false;
					appendMsg1 = appendMsg1+"[ **ERROR : "+sStartDate+".....Start Date  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Start Date")+" ]\n";
				}	
			}

			String sFiscalYearEnd = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='fiscalYearEnd']"),"value");
			if(mapGeneralDetailsTab.get("Fiscal Year End") != null){
				if (sFiscalYearEnd == null || !mapGeneralDetailsTab.get("Fiscal Year End").equalsIgnoreCase(sFiscalYearEnd.trim())) {
					validateStatus = false;
					appendMsg1 = appendMsg1+"[ **ERROR : "+sFiscalYearEnd+".....Fiscal Year End actual is not equal to Expected: "+mapGeneralDetailsTab.get("Fiscal Year End")+" ]\n";
				}	
			}

			String sFiscalEnd = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='firstFiscalYearEnd']"),"value");
			if(mapGeneralDetailsTab.get("First Fiscal Year End") != null){
				if (sFiscalEnd == null || !mapGeneralDetailsTab.get("First Fiscal Year End").equalsIgnoreCase(sFiscalEnd.trim())) {
					validateStatus = false;
					appendMsg1 = appendMsg1+"[ **ERROR : "+sFiscalEnd+"....First Fiscal Year End actual is not equal to Expected: "+mapGeneralDetailsTab.get("First Fiscal Year End")+" ]\n";
				}	
			}

			String sOpenedClosedEnded =Elements .getText(Global .driver , By.xpath("//select[@id='openClosedEnded.referenceIdPk']/../div//span[1]"));
			if(mapGeneralDetailsTab.get("Open or Closed Ended") != null)
			{
				if (sOpenedClosedEnded==null || !mapGeneralDetailsTab.get("Open or Closed Ended").equalsIgnoreCase(sOpenedClosedEnded.trim())) {
					validateStatus = false;
					appendMsg1 = appendMsg1+"[ **ERROR : "+sOpenedClosedEnded+"....Open or Closed Ended  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Open or Closed Ended")+" ]\n";
				}	
			}


			//verify Is Claw Back
			//Verify the Percentage Inside Loop
			if(mapGeneralDetailsTab.get("Is Claw Back") != null)
			{
				if(mapGeneralDetailsTab.get("Is Claw Back").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-isClawBackYes']//parent::span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg1 = appendMsg1+"[ **ERROR : "+".....Is Claw Back.. actual is not equal to Expected ]\n";
					}
					String sCaptialPercentage=Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='capitalPercentage']"),"value");

					if(mapGeneralDetailsTab.get("Capital in Percentage") != null)
					{
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sCaptialPercentage, mapGeneralDetailsTab.get("Capital in Percentage"));
						if (!bStatus) {
							validateStatus = false;
							appendMsg1 = appendMsg1+"[ **ERROR : "+sCaptialPercentage+"......Capital in Percentage  actual is not equal to Expected: "+mapGeneralDetailsTab.get("Capital in Percentage")+" ]\n";
						}	
					}

					String sNumbersofYears=Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='noOfYears']"),"value");
					if(mapGeneralDetailsTab.get("Number of Years") != null)
					{
						if (sNumbersofYears==null || !mapGeneralDetailsTab.get("Number of Years").equalsIgnoreCase(sNumbersofYears.trim())) 
						{
							validateStatus = false;
							appendMsg1 = appendMsg1+"[ **ERROR : "+sNumbersofYears+"......Number of Years actual is not equal to Expected: "+mapGeneralDetailsTab.get("Number of Years")+" ]\n";
						}
						/*	if(GeneralDetails.get("YearPercentage") != null){
								List<String> sYearPercen = Arrays.asList(GeneralDetails.get("YearPercentage").split(",")); 
								for (int index = 0; index < sYearPercen.size(); index++) 
								{
									String sYearPercentage=Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='legalEntitClawbackMappingList"+index+".percentage']"),"value");
									if (!GeneralDetails.get("YearPercentage").equalsIgnoreCase(sYearPercentage.trim())) {
										validateStatus = false;
										appendMsg1 = appendMsg1+sYearPercentage+"......YearPercentage  actual is not equal to Expected: "+GeneralDetails.get("YearPercentage")+"\n";
									}		
								}
							}*/
						if(mapGeneralDetailsTab.get("Percentage") != null)
						{
							List<String> sYearPercen = Arrays.asList(mapGeneralDetailsTab.get("Percentage").split(",")); 
							for (int index = 0; index < sYearPercen.size(); index++) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//input[@id='legalEntitClawbackMappingList"+index+".percentage' and contains(@value,'"+sYearPercen.get(index)+"')]"));
								if(!bStatus){
									validateStatus = false;
									appendMsg1 = appendMsg1+"[ **ERROR : "+".....Year Percencentage. actual is not equal to Expected ]\n";
								}
							}
						}
					}
					if(mapGeneralDetailsTab.get("Is Accrued Claw back Amount") != null)
					{
						if(mapGeneralDetailsTab.get("Is Accrued Claw back Amount").equalsIgnoreCase("Yes"))
						{
							bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-isAccuredClawbackAmountYes']//parent::span[@class='checked']"));
							if(!bStatus){
								validateStatus = false;
								appendMsg1 = appendMsg1+"[ **ERROR : "+"Is Accrued Claw back Amount Yes . actual is not equal to Expected ]\n";
							}
						}
						if(mapGeneralDetailsTab.get("Is Accrued Claw back Amount").equalsIgnoreCase("No"))
						{
							bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-isAccuredClawbackAmountNo']//parent::span[@class='checked']"));
							if(!bStatus){
								validateStatus = false;
								appendMsg1 = appendMsg1+"[ **ERROR : "+"....Is Accrued Claw back Amount . actual is not equal to Expected ]\n";
							}
						}
					}
				}
				if(mapGeneralDetailsTab.get("Is Claw Back").equalsIgnoreCase("No"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-isClawBackNo']//parent::span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg1 = appendMsg1+"[ **ERROR : "+".....Is Claw Back.. actual is not equal to Expected ]\n";
					}
				}
			}
			//Verify Holiday Calendar
			if(mapGeneralDetailsTab.get("Holiday Calendar") != null)
			{
				List<String> sNoOfHolidyCalendarsToSelect = Arrays.asList(mapGeneralDetailsTab.get("Holiday Calendar").split(",")); 
				for (int index=1; index <= sNoOfHolidyCalendarsToSelect.size(); index++) 
				{
					//System.out.println(sNoOfHolidyCalendarsToSelect.get(index-1)+" <<<<<<<<<<<<<<<<<<<<<<<");

					String SHolidayCalender = Elements.getText(Global .driver , By.xpath("//select[@id='leHolidayMapList']/../div/ul//li["+index+"]//div"));
					//System.out.println(SHolidayCalender+"  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

					if (SHolidayCalender != null && !sNoOfHolidyCalendarsToSelect.contains(SHolidayCalender.trim())) {
						validateStatus = false;
						appendMsg1 = appendMsg1 +"[ **ERROR : "+ SHolidayCalender + " ....Holiday Calendar  actual is not equal to Expected: "+sNoOfHolidyCalendarsToSelect.get(index-1)+" ]\n";
					}
				}
			}
			if(mapGeneralDetailsTab.get("WeeklyOffDaysToCheck") != null)
			{
				List<String> sweekofDays = Arrays.asList(mapGeneralDetailsTab.get("WeeklyOffDaysToCheck").split(",")); 
				for (int index = 0; index < sweekofDays.size(); index++) 
				{
					bStatus= Verify.verifyElementVisible(Global.driver, By.xpath("//label[contains(text(),'"+sweekofDays.get(index)+"')]/preceding-sibling::div//span[@class='checked']"));
					if(!bStatus){
						validateStatus = false;
						appendMsg1 = appendMsg1+"[ **ERROR : "+sweekofDays+"....WeeklyOffDaysToCheck  actual is not equal to Expected: "+mapGeneralDetailsTab.get("WeeklyOffDaysToCheck")+" ]\n";
					}
				}
			}
			Messages.errorMsg = appendMsg1;
			System.out.println(appendMsg1);
			return validateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//Legal Entity Details Validation in Edit Screen of Subscription Tab.

	public static boolean doVerifyDetailsOfSubscriptionTab(Map<String, String> mapSubscriptionTabDetails){
		try{
			String sAppendMessage = "";
			boolean bValidateStatus = true;

			//Thread Removed
			Wait.waitForElementPresence(Global.driver, By.xpath("//a[text()=' Subscription']"),Constants.lTimeOut);

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@href='#tab4']"));

			//Verify the Investment Permitted

			if(mapSubscriptionTabDetails.get("Investment Permitted")!=null)
			{  
				//Verify Amount
				if(mapSubscriptionTabDetails.get("Investment Permitted").equalsIgnoreCase("Amount"))
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-rdIsAmountYes']//parent::span[@class='checked']"), 5);
					if(!bStatus)
					{
						sAppendMessage = sAppendMessage +"[ **ERROR : "+ "Given Change In Investment Permitted i.e. "+mapSubscriptionTabDetails.get("Investment Permitted")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}

					String sMiniumIniticalAmount = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='minInitialSubAmount']"),"value");
					if(mapSubscriptionTabDetails.get("Minimum Initial Amount") != null){
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sMiniumIniticalAmount, mapSubscriptionTabDetails.get("Minimum Initial Amount"));
						if (!bStatus) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage +"[ **ERROR : "+ sMiniumIniticalAmount + "..Minimun Initial Amout actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Minimum Initial Amount")+" ]\n";
						}	
					}

					String sMiniumSubsequent = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='minSubsequentSubAmount']"),"value");
					if(mapSubscriptionTabDetails.get("Minimum Subsequent Amount")!=null)
					{
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sMiniumSubsequent, mapSubscriptionTabDetails.get("Minimum Subsequent Amount"));
						if (!bStatus) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage +"[ **ERROR : "+ sMiniumSubsequent + "..Minimum Subsequent Amount actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Minimum Subsequent Amount")+" ]\n";
						}
					}
				}

				//Verify the shares

				if(mapSubscriptionTabDetails.get("Investment Permitted").equalsIgnoreCase("Shares"))
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-rdIsAmountNo']//parent::span[@class='checked']"), 5);
					if(!bStatus)
					{
						sAppendMessage = sAppendMessage +"[ **ERROR : "+ " Shares Given Change In Investment Permitted i.e. "+mapSubscriptionTabDetails.get("Investment Permitted")+" is not matching with actual ] \n";
						bValidateStatus = false;
					}
					String sMiniumInitialshares = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='minInitialSubShares']"),"value");

					if(mapSubscriptionTabDetails.get("Minimum Initial Shares")!=null)
					{
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sMiniumInitialshares, mapSubscriptionTabDetails.get("Minimum Initial Shares"));
						if (!bStatus) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage +"[ **ERROR : "+ sMiniumInitialshares + "..Minimun Inicial Share actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Minimum Initial Shares")+" ]\n";
						}
					}

					String sMiniumSubseqShares = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='minSubsequentSubShares']"),"value");

					if(mapSubscriptionTabDetails.get("Minimum Subsequent Shares")!=null)
					{
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sMiniumSubseqShares, mapSubscriptionTabDetails.get("Minimum Subsequent Shares"));
						if (!bStatus) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage+"[ **ERROR : "+sMiniumSubseqShares+" **ERROR : "+"..Minimun Subsequent Share actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Minimum Subsequent Shares")+" ]\n";
						}
					}
				}

				//Verify BOTH

				if(mapSubscriptionTabDetails.get("Investment Permitted").equalsIgnoreCase("Both"))
				{
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='rdIsAmountBoth']//parent::span[@class='checked']"), 5);
					if(!bStatus)
					{
						sAppendMessage = sAppendMessage +"[ **ERROR : "+ "..Given Change In Investment Permitted..option ..Both "+mapSubscriptionTabDetails.get("Investment Permitted")+" is not matching with actual  ]\n";
						bValidateStatus = false;
					}

					String sMiniumIniticalAmount = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='minInitialSubAmount']"),"value");
					if(mapSubscriptionTabDetails.get("Minimum Initial Amount") != null){
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sMiniumIniticalAmount, mapSubscriptionTabDetails.get("Minimum Initial Amount"));
						if (!bStatus) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage +"[ **ERROR : "+ sMiniumIniticalAmount + "..Minimun Initial Amout actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Minimum Initial Amount")+" ]\n";
						}	
					}

					String sMiniumSubsequent = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='minSubsequentSubAmount']"),"value");
					if(mapSubscriptionTabDetails.get("Minimum Subsequent Amount")!=null)
					{
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sMiniumSubsequent, mapSubscriptionTabDetails.get("Minimum Subsequent Amount"));
						if (!bStatus) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage +"[ **ERROR : "+ sMiniumSubsequent + "..Minimum Subsequent Amount actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Minimum Subsequent Amount")+" ]\n";
						}
					}
					String sMiniumInitialshares = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='minInitialSubShares']"),"value");

					if(mapSubscriptionTabDetails.get("Minimum Initial Shares")!=null)
					{
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sMiniumInitialshares, mapSubscriptionTabDetails.get("Minimum Initial Shares"));
						if (!bStatus) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage +"[ **ERROR : "+ sMiniumInitialshares + "..Minimun Initial Share actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Minimum Initial Shares")+" ]\n";
						}
					}

					String sMiniumSubseqShares = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='minSubsequentSubShares']"),"value");

					if(mapSubscriptionTabDetails.get("Minimum Subsequent Shares")!=null)
					{
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sMiniumSubseqShares, mapSubscriptionTabDetails.get("Minimum Subsequent Shares"));
						if (!bStatus) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage+"[ **ERROR : "+" **ERROR : "+sMiniumSubseqShares+"..Minimun Subsequent Share actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Minimum Subsequent Shares")+" ]\n";
						}
					}
				}
			}
			String sSubscriptionFrequency =Elements .getText(Global .driver , By.xpath("//select[@id='subscriptionList0.subscriptionContributionFrequency.frequencyIdPk']/../div//span[1]"));
			if(mapSubscriptionTabDetails.get("Subscription Frequency") != null){
				if (!mapSubscriptionTabDetails.get("Subscription Frequency").equalsIgnoreCase(sSubscriptionFrequency.trim())) {
					bValidateStatus = false;
					sAppendMessage = sAppendMessage+"[ **ERROR : "+sSubscriptionFrequency+"...Subscription Frequency actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Subscription Frequency")+" ]\n";
				}	
			}
			String sNewSubscriptionFrequency =Elements .getText(Global .driver , By.xpath("//select[@id='subscriptionComputationMethodId']/../div//span[1]"));
			if(mapSubscriptionTabDetails.get("New Subscription Computation") != null)
			{
				if (!mapSubscriptionTabDetails.get("New Subscription Computation").equalsIgnoreCase(sNewSubscriptionFrequency.trim())) {
					bValidateStatus = false;
					sAppendMessage = sAppendMessage+"[ **ERROR : "+sNewSubscriptionFrequency+"...New Subscription Computation actual is not equal to Expected: "+mapSubscriptionTabDetails.get("New Subscription Computation")+" ]\n";
				}	
			}
			String sPresentationMethod =Elements .getText(Global .driver , By.xpath("//select[@id='subscriptionPresentationMethodId']/../div//span[1]"));
			if(mapSubscriptionTabDetails.get("Presentation Method") != null)
			{
				if (!mapSubscriptionTabDetails.get("Presentation Method").equalsIgnoreCase(sPresentationMethod.trim())) {
					bValidateStatus = false;
					sAppendMessage = sAppendMessage+"[ **ERROR : "+sPresentationMethod+"...sPresentationMethod  actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Presentation Method")+" ]\n";
				}	
			}
			//Verify  the Notice Period setup/charges
			if(mapSubscriptionTabDetails.get("Notice Period Applicable")!=null)
			{ 
				if(mapSubscriptionTabDetails.get("Notice Period Applicable").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-rbIsSubscriptionNoticePeriodYes']//parent::span[@class='checked']"));
					if(!bStatus){
						bValidateStatus = false;
						sAppendMessage = sAppendMessage+"[ **ERROR : "+"Notice Period Applicable  actual is not equal to Expected ]\n";
					}
					//Notice Period set up
					String sNoticeDate = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='subscriptionNoticePeriodList0.period']"),"value");

					if(sNoticeDate != null && mapSubscriptionTabDetails.get("Notice Period")!=null)
					{
						if (!mapSubscriptionTabDetails.get("Notice Period").equalsIgnoreCase(sNoticeDate.trim())) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage+"[ **ERROR : "+sNoticeDate+"..Notice Period Date .. actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Notice Period")+" ]\n";
						}
					}

					if(mapSubscriptionTabDetails.get("Notice Period Day or Month or Year") != null)
					{
						bStatus =Verify .verifyElementVisible(Global .driver , By.xpath("//select[@id='subscriptionNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']/../div//span[text()='"+mapSubscriptionTabDetails.get("Notice Period Day or Month or Year")+"']"));
						if (!bStatus) 
						{
							bValidateStatus = false;
							sAppendMessage = sAppendMessage+"[ **ERROR :NoticePeriodDay  actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Notice Period Day or Month or Year")+" ]\n";
						}	
					}
					//Set Up Business or calendar

					if(mapSubscriptionTabDetails.get("Notice Period Calender Type")!=null)
					{
						//select calendar Type Business
						if(mapSubscriptionTabDetails.get("Notice Period Calender Type").equalsIgnoreCase("Business"))
						{

							if(mapSubscriptionTabDetails.get("Notice Period Calender Type") != null)
							{
								bStatus = Verify .verifyElementVisible(Global .driver , By.xpath("//select[@id='Notice_Period_SubscriptionsBusinessCalendar']/../div//span[text()='"+mapSubscriptionTabDetails.get("Notice Period Calender Type")+"']"));
								if (!bStatus) 
								{
									bValidateStatus = false;
									sAppendMessage = sAppendMessage+"[ **ERROR : Notice Period Calender Type actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Notice Period Calender Type")+" ]\n";
								}	
							}

							//Holiday Details
							if(mapSubscriptionTabDetails .get("Include Holidays")!=null)
							{
								if(mapSubscriptionTabDetails.get("Include Holidays").equalsIgnoreCase("Yes"))
								{
									bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-subscriptionNoticePeriodList0.isHolidaysInclude1']/span[@class='checked']"));
									if(!bStatus){
										bValidateStatus = false;
										sAppendMessage = sAppendMessage+"[ **ERROR : "+"Include Holidays actual is not equal to Expected ]\n";
									}
								}
								if(mapSubscriptionTabDetails.get("Include Holidays").equalsIgnoreCase("No"))
								{
									bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-subscriptionNoticePeriodList0.isHolidaysInclude2']/span[@class='checked']"));
									if(!bStatus){
										bValidateStatus = false;
										sAppendMessage = sAppendMessage+"[ **ERROR : "+" Include Holidays actual is not equal to Expected ]\n";
									}	
								}
							}
						}
						if(mapSubscriptionTabDetails.get("Notice Period Calender Type").equalsIgnoreCase("Calendar"))
						{
							bStatus = Verify .verifyElementVisible(Global .driver , By.xpath("//select[@id='Notice_Period_SubscriptionsBusinessCalendar']/../div//span[text()='Calendar']"));
							if (!bStatus) {
								bValidateStatus = false;
								sAppendMessage = sAppendMessage+"[ **ERROR : Notice Period Calender Type actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Notice Period Calender Type")+" ]\n";
							}	
						}
					}

					//Notice Period Charges
					if(mapSubscriptionTabDetails.get("Notice Period Charges")!=null)
					{ 
						if(mapSubscriptionTabDetails.get("Notice Period Charges").contains("Amount") || mapSubscriptionTabDetails.get("Notice Period Charges").contains("BPS") || mapSubscriptionTabDetails.get("Notice Period Charges").contains("Percentage")){
							String sNoticePeriodChaarges=Elements .getText(Global .driver , By.xpath("//select[@id='noticePeriodTypeSUB']/../div//span[1]"));
							if(mapSubscriptionTabDetails.get("Notice Period Charges") != null){
								if (!mapSubscriptionTabDetails.get("Notice Period Charges").equalsIgnoreCase(sNoticePeriodChaarges.trim())) {
									bValidateStatus = false;
									sAppendMessage = sAppendMessage+"[ **ERROR : "+sNoticePeriodChaarges+"...Notice Period Charges actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Notice Period Charges")+" ]\n";
								}	
							}
							if(mapSubscriptionTabDetails .get("Amount or BPS or Percent")!=null){
								String sAMBPP = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='subscriptionNoticePeriodList0.noticePeriodLockupDetailsList0.rate']"),"value");
								if(sAMBPP!=null&&mapSubscriptionTabDetails.get("Amount or BPS or Percent")!=null)
								{
									bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sAMBPP, mapSubscriptionTabDetails.get("Amount or BPS or Percent"));
									if (!bStatus) {
										bValidateStatus = false;
										sAppendMessage = sAppendMessage+"[ **ERROR : "+sAMBPP+"..Amount/BPs/Percentage is  actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Amount or BPS or Percent")+" ]\n";
									}
								}	
							}
						}
						if(mapSubscriptionTabDetails.get("Notice Period Charges").equalsIgnoreCase("None"))
						{
							String sNoticePeriodChaarges=Elements .getText(Global .driver , By.xpath("//select[@id='noticePeriodTypeSUB']/../div//span[1]"));
							if(mapSubscriptionTabDetails.get("Notice Period Charges") != null){
								if (!mapSubscriptionTabDetails.get("Notice Period Charges").equalsIgnoreCase(sNoticePeriodChaarges.trim())) {
									bValidateStatus = false;
									sAppendMessage = sAppendMessage+"[ **ERROR : "+sNoticePeriodChaarges+"...Notice Period Charges actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Notice Period Charges")+" ]\n";
								}	
							}
						}
					}
				}

				//Notice Period option No
				if(mapSubscriptionTabDetails.get("Notice Period Applicable").equalsIgnoreCase("No")){
					bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-rbIsSubscriptionNoticePeriodNo']//parent::span[@class='checked']"));
					if(!bStatus){
						bValidateStatus = false;
						sAppendMessage = sAppendMessage+"[ **ERROR : "+" Notice Period Applicable actual is not equal to Expected ]\n";
					}	
				}
			}

			//Verify Transaction Charges

			if(mapSubscriptionTabDetails.get("Transaction Charges")!=null)
			{ 
				if(mapSubscriptionTabDetails.get("Transaction Charges").equalsIgnoreCase("Yes"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-Subscription_Charges_Yes']//parent::span[@class='checked']"));
					if(!bStatus){
						bValidateStatus = false;
						sAppendMessage = sAppendMessage+"[ **ERROR : "+"Transaction Charges  actual is not equal to Expected ]\n";
					}

					String sEffectiveDate = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='subscriptionChargesList0.wef']"),"value");
					if(sEffectiveDate!=null && mapSubscriptionTabDetails.get("Effective Date")!=null)
					{
						if (!mapSubscriptionTabDetails.get("Effective Date").equalsIgnoreCase(sEffectiveDate.trim())) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage+"[ **ERROR : "+sEffectiveDate+"..sEffectiveDate Date .. actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Effective Date")+" ]\n";
						}
					}

					String sChargesTypes =Elements .getText(Global .driver , By.xpath("//select[@id='subscriptionChargesList0.fkChargeTypeIdPk.referenceIdPk']/../div//span[1]"));
					if(mapSubscriptionTabDetails.get("Charges Type") != null){
						if (!mapSubscriptionTabDetails.get("Charges Type").equalsIgnoreCase(sChargesTypes.trim())) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage+"[ **ERROR : "+sChargesTypes+"...Charges Types actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Charges Type")+" ]\n";
						}	
					}

					String sCalculationBase =Elements .getText(Global .driver , By.xpath("//select[@id='subscriptionChargesList0.fkCalculationBaseIdPk.referenceIdPk']/../div//span[1]"));
					if(mapSubscriptionTabDetails.get("Calculation Base") != null){
						if (!mapSubscriptionTabDetails.get("Calculation Base").equalsIgnoreCase(sCalculationBase.trim())) {
							bValidateStatus = false;
							sAppendMessage = sAppendMessage+"[ **ERROR : "+sCalculationBase+"...Calculation Base actual is not equal to Expected: "+mapSubscriptionTabDetails.get("Calculation Base")+" ]\n";
						}	
					}
					if (mapSubscriptionTabDetails.get("Rate Method") != null) 
					{
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='s2id_Rate_Method_SUB_NormalCharges']//span[contains(@id,'select2-chosen') and contains(text(),'"+mapSubscriptionTabDetails.get("Rate Method")+"')]"));
						if(!bStatus ){
							sAppendMessage = sAppendMessage + "[ Given TxnRateMethod i.e. "+mapSubscriptionTabDetails.get("Rate Method")+" is not matching with actual ] \n";
							bValidateStatus = false;
						}
						if(!mapSubscriptionTabDetails.get("Rate Method").equalsIgnoreCase("None"))
						{
							if (mapSubscriptionTabDetails.get("Rate Method").equalsIgnoreCase("Fixed") && mapSubscriptionTabDetails.get("Fixed Fee Rate") != null) 
							{
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='subscriptionChargesList0.chargeDetailsList0.fixedFeeRate' and contains(@value,'"+mapSubscriptionTabDetails.get("Fixed Fee Rate")+"')]"));
								if(!bStatus ){
									sAppendMessage = sAppendMessage + "[ Given Fixed Fee Rate i.e. "+mapSubscriptionTabDetails.get("Fixed Fee Rate")+" is not matching with actual ] \n";
									bValidateStatus = false;
								}
							}
							if (mapSubscriptionTabDetails.get("Rate Method").equalsIgnoreCase("Slab") || mapSubscriptionTabDetails.get("Rate Method").equalsIgnoreCase("Tiered")) 
							{
								if(mapSubscriptionTabDetails.get("Amount From") != null && mapSubscriptionTabDetails.get("Amount To") != null && mapSubscriptionTabDetails.get("Charges") != null)
								{
									String sFromAmountLocator = "subscriptionChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
									String sAmountToLocator = "subscriptionChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
									String sChargesLocator = "subscriptionChargesList0.chargeDetailsListReplaceIndexValue.rate";
									//By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'addExchangeChargeDetailsList')]");
									String sAmountFrom = mapSubscriptionTabDetails.get("Amount From");
									List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
									String sAmountTo = mapSubscriptionTabDetails.get("Amount To");
									List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
									String sCharges = mapSubscriptionTabDetails.get("Charges");
									List<String> aCharges = Arrays.asList(sCharges.split(","));
									for(int i=0;i<aAmountFrom.size();i++){
										if(!aAmountFrom.get(i).equalsIgnoreCase("None"))
										{
											String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountFrom+"'and contains(@value,'"+aAmountFrom.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage+"[ **ERROR : "+ "Amount From is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aAmountTo.get(i).equalsIgnoreCase("None")){
											String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjAmountTo+"'and contains(@value,'"+aAmountTo.get(i).toString()+"')]"));							
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Amount To is Not matching with expected in "+i+" index ]";
												bValidateStatus = false;
											}
										}
										if(!aCharges.get(i).equalsIgnoreCase("None")){
											String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
											bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+sObjCharges+"'and contains(@value,'"+aCharges.get(i).toString()+"')]"));
											if(!bStatus){
												sAppendMessage = sAppendMessage + "[ Charges is Not matching with expected in "+i+" index i.e. expected = "+aCharges.get(i).toString()+" ]\n";
												bValidateStatus = false;
											}
										}		
									}
								}
							}
						}
					}
				}
				//Transaction charges No
				if(mapSubscriptionTabDetails.get("Transaction Charges").equalsIgnoreCase("No"))
				{
					bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//div[@id='uniform-Subscription_Charges_No']//parent::span[@class='checked']"));
					if(!bStatus){
						bValidateStatus = false;
						sAppendMessage = sAppendMessage+"[ **ERROR : "+" Transaction Charges actual is not equal to Expected ]\n";
					}	
				}
			}
			Messages.errorMsg = sAppendMessage;
			return bValidateStatus;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}


	/*public static boolean modifyLegalEntityDetails(Map<String, Map<String, String>> objMapLegalEntityTabsModificationDetailsMaps, String sLegalEntityName) {
		try{

			//bStatus = LegalEntityAppFunctions.searchLegalEntityMaster(sLegalEntityName, "active",2); 
			bStatus = NewUICommonFunctions.searchInSearchPanel("Legal Entity Name",sLegalEntityName, "active",7);
			if(!bStatus){
				return false;
			}

			Elements.click(Global.driver, By.xpath("//a[@data-original-title='Edit']"));

			bStatus = LegalEntityAppFunctions.AddNewLegalEntity(objMapLegalEntityTabsModificationDetailsMaps);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean modifyReturnLegalEntityDetails(Map<String, Map<String, String>> objMapLegalEntityTabsModificationDetailsMaps, String sLegalEntityName) {
		try{

			//bStatus = NewUICommonFunctions.viewDetailsOfItemInActionTable(sLegalEntityName);
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,sLegalEntityName,"");
			if(!bStatus){
				return false;
			}

			bStatus = LegalEntityAppFunctions.AddNewLegalEntity(objMapLegalEntityTabsModificationDetailsMaps);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyReturnLegalEntityDetails(Map<String, Map<String, String>> objMapLegalEntityTabsModificationDetailsMaps, String sLegalEntityName) {
		try{

			//bStatus = NewUICommonFunctions.viewDetailsOfItemInActionTable(sLegalEntityName);
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,sLegalEntityName,"");
			if(!bStatus){
				return false;
			}

			bStatus = LegalEntityAppFunctions.verifyAllTabsInLegalEntityDetailsEditScreen(objMapLegalEntityTabsModificationDetailsMaps);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*public static boolean deactivateLegalEntity(String sLegalEntityName){
		try{

			bStatus = NewUICommonFunctions.searchInSearchPanel("Legal Entity Name",sLegalEntityName, "active",7);
			if(!bStatus){
				return false;
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@data-original-title='Deactivate']"));

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Legal Entity cannot be deactivated";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	/*public static boolean verifyDataInLegalEntityScreen(Map<String, Map<String, String>> validateMap){
		try{

			bStatus = NewUICommonFunctions.searchInSearchPanel("Legal Entity Name", validateMap.get("LegalEntityDetails").get("Legal Entity Name"), "active", 10);
			if (!bStatus) {
				Reporting.logResults("Fail", "Search the active Legal Entity to edit and verify the pre populated details", Messages.errorMsg);
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@data-original-title='Edit']"));

			return verifyAllTabsInLegalEntityDetailsEditScreen(validateMap);

			//return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/


	public static boolean verifyTradingRestriction(Map<String, String> mapOtherDetails){
		try {
			boolean validateStatus = true;
			String appendMsg="";
			
			String sTransactionType = mapOtherDetails.get("Transaction Type");
			List<String> aTransactionType= Arrays.asList(sTransactionType.split(","));
			String sComments = mapOtherDetails.get("Comments");
			String[] aComments= sComments.split(",");
			//String sGatingProvision = mapOtherDetails.get("Gating Provision");
			//List<String> aGatingProvision= Arrays.asList(sGatingProvision.split(","));
			//String sClosed = mapOtherDetails.get("Closed");
			//List<String> aClosed= Arrays.asList(sClosed.split(","));
			List<String> tranTypeValuesList = new ArrayList<String>();
			int x = Elements.getXpathCount(Global.driver, By.xpath("//div[contains(@id,'s2id_tradResLog_')]//span[contains(@id,'chosen')]"));
			for(int j=0 ;j<x ; j++){
				tranTypeValuesList.add(Elements.getText(Global.driver, By.xpath("//div[contains(@id,'s2id_tradResLog_"+j+"')]//span[contains(@id,'chosen')]")).trim());
			}
			for(int i=0;i<aTransactionType.size();i++)
			{
				int transTypeIndex = tranTypeValuesList.indexOf(aTransactionType.get(i));
				if(!aTransactionType.get(transTypeIndex).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//select[@id='tradResLog_"+String.valueOf(transTypeIndex)+"']//parent::div//span[contains(@id,'select2') and contains(text(),'"+aTransactionType.get(i).toString()+"')]"));
					if(!bStatus){
						appendMsg =  appendMsg+"[ "+ "Transaction dropdown selection value i.e. "+aTransactionType.get(i).toString()+" is Not matching with actual ] \n";
						validateStatus = false;
					}
				}
				if(mapOtherDetails.get("Date From") != null && mapOtherDetails.get("To Date") != null){
					//Verify More Dates Function
					bStatus = verifyDates(mapOtherDetails, i ,transTypeIndex);
					if(!bStatus){
						appendMsg =  appendMsg+Messages.errorMsg;
						validateStatus = false;
					}
				}	
				//Verifying Comments in Trading Restrictions CommentBox
				if(mapOtherDetails.get("Comments")!=null){
					if(!aComments[i].equalsIgnoreCase("None")){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//textarea[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".comments' and contains(text(),'"+aComments[i].toString()+"')]"));
						if(!bStatus){
							appendMsg =  appendMsg+"[" + " Trading Restriction to Comments value is Not matching with actual at given Comments at index ] "  +transTypeIndex+ "\n";
							validateStatus = false;
						}
					}
				}
				//Selecting the Gating Provision Radio Button
				/*if(mapOtherDetails.get("Gating Provision")!=null){
					if(!aGatingProvision.get(i).equalsIgnoreCase("None")){
						if(aGatingProvision.get(i).toString().equalsIgnoreCase("Yes")){									
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".gatingProvision1']//parent::span[@class='checked']"));									
							if(!bStatus){
								appendMsg =  appendMsg+"[" + " Trading Restrictions Gating Provision "+transTypeIndex+"Yes Radio Button Not Matched with the Expected ] ";
								validateStatus = false;
							}
						}
						if(aGatingProvision.get(i).toString().equalsIgnoreCase("No")){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".gatingProvision2']//parent::span[@class='checked']"));									
							if(!bStatus){
								appendMsg =  appendMsg+"[" + "Trading Restrictions Gating Provision "+transTypeIndex+" No Radio Button Not Matched with the Expected ]";
								validateStatus = false;
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
								appendMsg =  appendMsg+"[" + " Trading Restriction to closed Yes value Not matching with actualat index ]"+transTypeIndex+"\n";
								validateStatus = false;
							}
						}
						if(aClosed.get(i).toString().equalsIgnoreCase("No")){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+String.valueOf(transTypeIndex)+".closed2']//parent::span[@class='checked']"));									
							if(!bStatus){
								appendMsg =  appendMsg+"[" + " Trading Restriction to closed No value Not matching with actual at index ] "+transTypeIndex+"\n";
								validateStatus = false;
							}
						}
					}
				}*/

			}	
			Messages.errorMsg = appendMsg;
			return validateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	public static boolean verifyDates(Map<String,String> mapOtherDetails,int i,int transTypeIndex){
		try {
			boolean validateStatus = true;
			String appendMsg="";
			
			String sFromDate = mapOtherDetails.get("Date From");
			List<String> aFromDate= Arrays.asList(sFromDate.split(","));
			String sToDate = mapOtherDetails.get("To Date");
			List<String> aToDate= Arrays.asList(sToDate.split(","));
			List<String> arrIndidualFromDate = Arrays.asList(aFromDate.get(i).split(":"));
			List<String> arrIndidualToDate = Arrays.asList(aToDate.get(i).split(":"));
			
			for(int k=0;k<arrIndidualFromDate.size();k++){
				if(!arrIndidualFromDate.get(k).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+transTypeIndex+"fromDateRange"+k+"' and contains(@value,'"+arrIndidualFromDate.get(k)+"')]"));
					if(!bStatus){
						appendMsg =  appendMsg+"[" + " Date From Not Matched with the Expected Date ]"+arrIndidualFromDate.get(k)+ " at Index "+transTypeIndex+","+k+"\n";
						validateStatus = false;
					}
				}
				if(!arrIndidualToDate.get(k).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='tradingRestrictionsList"+transTypeIndex+"toDateRange"+k+"' and contains(@value,'"+arrIndidualToDate.get(k)+"')]"));
					if(!bStatus){
						appendMsg =  appendMsg+"[" + " To Date Not Matched with the Expected Date"+arrIndidualToDate.get(k)+" at Index "+transTypeIndex+","+k+"\n";
						validateStatus = false;
					}
				}
			}
			Messages.errorMsg = appendMsg;
			return validateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	public static boolean removeHolidayCalendar() {
		try {

			int xpathCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@id='s2id_leHolidayMapList']/ul/li"));
			for(int i=xpathCount-1;i>=1;i--){
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='s2id_leHolidayMapList']/ul/li["+i+"]/a")); 
				if(!bStatus){
					Messages.errorMsg = "Selected Radio Calendar Not Removed";
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		// TODO Auto-generated method stub

	}

	/*public static boolean activateLegalEntity(String sLegalEntityName){
		try{

			bStatus = NewUICommonFunctions.searchInSearchPanel("",sLegalEntityName, "inactive",7);
			if(!bStatus){
				return false;
			}

			NewUICommonFunctions.selectNumberOfRecordsPerPageInSearchGrid("All",10);
			NewUICommonFunctions.sortTableByColumnName("sample_2", "Legal Entity ID", "descending");
			Wait.waitForElementPresence(Global.driver, By.xpath("//td[normalize-space(text())='"+sLegalEntityName+"']//..//a[@data-original-title='Activate']"), 5);
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//td[normalize-space(text())='"+sLegalEntityName+"']//..//a[@data-original-title='Activate']"));
			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Legal Entity cannot be Activated";
				return false;
			}
			return true;

		}
		catch(Exception e){
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

	public static boolean removeMasterFund(){
		try {	
			int xpathCount = 0;
			xpathCount = Global.driver.findElements(By.xpath("//div[@id='s2id_masterFundList']/ul/li")).size();

			for(int i=xpathCount-1 ; i>=1 ; i--){
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='s2id_masterFundList']/ul/li["+i+"]/a"));
				if(!bStatus){
					Messages.errorMsg = "Master Funds Not Cleared";
					return false;
				}
			}
			xpathCount = Global.driver.findElements(By.xpath("//div[@id='s2id_masterFundList']/ul/li")).size();
			if(xpathCount>1){
				Messages.errorMsg = "Mster fundds Not Removed";
				return false;
			}
			
			return true;


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

}

