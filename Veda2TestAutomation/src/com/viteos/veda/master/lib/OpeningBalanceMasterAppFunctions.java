package com.viteos.veda.master.lib;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class OpeningBalanceMasterAppFunctions {
	public static String sOpeningBalanceOf = "";
	static boolean bStatus;

	public static boolean doAddOpeningBalanceDetails(Map<String, String> mapOpeningBalanceDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.id("uniform-rdIsInvestor"), 15);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Opening Balance AddNew Page wasn't visible.]";
				return false;
			}
			//Fields of Investor.
			if (mapOpeningBalanceDetails.get("Opening Balance") != null && mapOpeningBalanceDetails.get("Opening Balance").equalsIgnoreCase("Investor")) {
				bStatus = NewUICommonFunctions.jsClick(By.id("uniform-rdIsInvestor"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select the Opening Balance Option : 'Investor' on AddNew Page.]";
					return false;
				}
				if (mapOpeningBalanceDetails.get("Investor Name") != null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Investor Name"), By.xpath("//div[@id='s2id_investorNameDropdown']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Investor Holder Name") != null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Investor Holder Name"), By.xpath("//div[@id='s2id_investorHolderListDropdown']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Investor Account") != null) {
					Map<String, String> mapFetchedFromAccountMaster = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapOpeningBalanceDetails.get("Investor Account"));
					if (mapFetchedFromAccountMaster != null && mapFetchedFromAccountMaster.get("AccountID") != null && !mapFetchedFromAccountMaster.get("AccountID").equalsIgnoreCase("")) {
						mapOpeningBalanceDetails.put("Investor Account", mapFetchedFromAccountMaster.get("AccountID"));
					}
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Investor Account"), By.xpath("//div[@id='s2id_investorAccountListDropdown']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Client Name") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Client Name"), By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Fund Family Name") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Fund Family Name"), By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Legal Entity Name") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Legal Entity Name"), By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Class Name") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Class Name"), By.xpath("//div[@id='s2id_cmbClassName']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Series Name") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Series Name"), By.xpath("//div[@id='s2id_cmbSeriesName']//span[contains(@id,'select2-chosen')]"));
					if (!bStatus) {
						return false;
					}
				}
			}
			//Fields Of Fund.
			if (mapOpeningBalanceDetails.get("Opening Balance") != null && mapOpeningBalanceDetails.get("Opening Balance").equalsIgnoreCase("Fund")) {
				bStatus = NewUICommonFunctions.jsClick(By.id("uniform-rdIsFund"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select the Opening Balance Option : 'Fund' on AddNew Page.]";
					return false;
				}				
				if (mapOpeningBalanceDetails.get("Client Name") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Client Name"), By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Fund Family Name") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Fund Family Name"), By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("From Legal Entity") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("From Legal Entity"), By.xpath("//div[@id='s2id_cmbFundNameFrom']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("To Legal Entity") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("To Legal Entity"), By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"));
					if (!bStatus) {
						return false;
					}
				}
				if (mapOpeningBalanceDetails.get("Fund Account") != null) {
					Map<String, String> mapFetchedFromAccountMaster = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapOpeningBalanceDetails.get("Fund Account"));
					if (mapFetchedFromAccountMaster != null && mapFetchedFromAccountMaster.get("AccountID") != null && !mapFetchedFromAccountMaster.get("AccountID").equalsIgnoreCase("")) {
						mapOpeningBalanceDetails.put("Fund Account", mapFetchedFromAccountMaster.get("AccountID"));
					}
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapOpeningBalanceDetails.get("Fund Account"), By.xpath("//div[@id='s2id_fundAccountListDropdown']//span[contains(@id,'select2-chosen')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						return false;
					}
				}
			}
			//Common fields in Basic Info.
			if (mapOpeningBalanceDetails.get("Initial Subscription Amount") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Initial Subscription Amount']//following-sibling::input"), mapOpeningBalanceDetails.get("Initial Subscription Amount"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Initial Subscription Amount")+" , into 'Initial Subscription Amount' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Initial Subscription Date") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Initial Subscription Date']//following-sibling::div//input"), mapOpeningBalanceDetails.get("Initial Subscription Date"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Initial Subscription Date")+" , into 'Initial Subscription Date' field.]";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			}
			if (mapOpeningBalanceDetails.get("Last Closing Date") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Last Closing Date']//following-sibling::div//input"), mapOpeningBalanceDetails.get("Last Closing Date"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Last Closing Date")+" , into 'Last Closing Date' field.]";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			}
			if (mapOpeningBalanceDetails.get("LastClosingDateForVerify") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Last Closing Date", By.id("lastClosingDate1"), mapOpeningBalanceDetails.get("LastClosingDateForVerify"), "No", false);				
				if (!bStatus) {
					return false;
				}
			}
			bStatus = doFillCommonOpeningBalanceDetails(mapOpeningBalanceDetails);
			if (!bStatus) {
				return false;
			}
			if (mapOpeningBalanceDetails.get("OperationType") != null && !mapOpeningBalanceDetails.get("OperationType").equalsIgnoreCase("")) {
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Opening Balance", mapOpeningBalanceDetails.get("OperationType"));
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

	//Common fields for both Investor/Fund.
	public static boolean doFillCommonOpeningBalanceDetails(Map<String, String> mapOpeningBalanceDetails){
		try {
			if (mapOpeningBalanceDetails.get("High Water Mark") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='High Water Mark']//following-sibling::input"), mapOpeningBalanceDetails.get("High Water Mark"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("High Water Mark")+" , into 'High Water Mark' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Closing NAV") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Closing NAV']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing NAV"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Closing NAV")+" , into 'Closing NAV' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Closing Units") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Closing Units']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing Units"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Closing Units")+" , into 'Closing Units' field.]";
					return false;
				}
			}

			//Enter Closing GAV
			if (mapOpeningBalanceDetails.get("Closing GAV") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Closing GAV']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing GAV"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Closing GAV")+" , into 'Closing GAV' field.]";
					return false;
				}
			}

			//Enter Closing NAV Per Unit
			if (mapOpeningBalanceDetails.get("Closing NAV Per Unit") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Closing NAV Per Unit']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing NAV Per Unit"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Closing NAV Per Unit")+" , into 'Closing NAV Per Unit' field.]";
					return false;
				}
			}


			if (mapOpeningBalanceDetails.get("Net MTD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Net MTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net MTD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Net MTD")+" , into 'Net MTD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net QTD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Net QTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net QTD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Net QTD")+" , into 'Net QTD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net YTD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Net YTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net YTD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Net YTD")+" , into 'Net YTD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Management Fee Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Management Fee Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Management Fee Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Management Fee Carry Forward")+" , into 'Management Fee Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Performance Fee Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Performance Fee Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Performance Fee Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Performance Fee Carry Forward")+" , into 'Performance Fee Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net ITD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Net ITD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net ITD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Net ITD")+" , into 'Net ITD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross MTD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Gross MTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross MTD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Gross MTD")+" , into 'Gross MTD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross QTD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Gross QTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross QTD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Gross QTD")+" , into 'Gross QTD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross YTD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Gross YTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross YTD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Gross YTD")+" , into 'Gross YTD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross ITD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Gross ITD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross ITD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Gross ITD")+" , into 'Gross ITD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net P&L Yearly") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Net P&L Yearly']//following-sibling::input"), mapOpeningBalanceDetails.get("Net P&L Yearly"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Net P&L Yearly")+" , into 'Net P&L Yearly' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross P&L Yearly") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Gross P&L Yearly']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross P&L Yearly"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Gross P&L Yearly")+" , into 'Gross P&L Yearly' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net PTD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Net PTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net PTD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Net PTD")+" , into 'Net PTD' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross PTD") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Gross PTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross PTD"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Gross PTD")+" , into 'Gross PTD' field.]";
					return false;
				}
			}


			//Enter Closing Fund Nav Per Unit
			if (mapOpeningBalanceDetails.get("Closing Fund NAV Per Unit") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Closing Fund NAV Per Unit']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing Fund NAV Per Unit"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Closing Fund NAV Per Unit")+" , into 'Closing Fund NAV Per Unit' field.]";
					return false;
				}
			}

			if (mapOpeningBalanceDetails.get("Adjusted GAV Monthly Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Adjusted GAV Monthly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted GAV Monthly Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Adjusted GAV Monthly Carry Forward")+" , into 'Adjusted GAV Monthly Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted GAV Quarterly Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Adjusted GAV Quarterly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted GAV Quarterly Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Adjusted GAV Quarterly Carry Forward")+" , into 'Adjusted GAV Quarterly Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted GAV Half Yearly Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Adjusted GAV Half Yearly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted GAV Half Yearly Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Adjusted GAV Half Yearly Carry Forward")+" , into 'Adjusted GAV Half Yearly Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted GAV Yearly Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Adjusted GAV Yearly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted GAV Yearly Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Adjusted GAV Yearly Carry Forward")+" , into 'Adjusted GAV Yearly Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted NAV Monthly Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Adjusted NAV Monthly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted NAV Monthly Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Adjusted NAV Monthly Carry Forward")+" , into 'Adjusted NAV Monthly Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted NAV Quarterly Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Adjusted NAV Quarterly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted NAV Quarterly Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Adjusted NAV Quarterly Carry Forward")+" , into 'Adjusted NAV Quarterly Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted NAV Half Yearly Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Adjusted NAV Half Yearly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted NAV Half Yearly Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Adjusted NAV Half Yearly Carry Forward")+" , into 'Adjusted NAV Half Yearly Carry Forward' field.]";
					return false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted NAV Yearly Carry Forward") != null) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Adjusted NAV Yearly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted NAV Yearly Carry Forward"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input : "+mapOpeningBalanceDetails.get("Adjusted NAV Yearly Carry Forward")+" , into 'Adjusted NAV Yearly Carry Forward' field.]";
					return false;
				}
			}			

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean modifyReturnedOpeningBalanceDetails(Map<String, String> mapModifyOpeningBalanceDetails, String sTxnID) {
		try {
			bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sTxnID, "");
			if (!bStatus) {
				return false;
			}
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 15);
			bStatus = doAddOpeningBalanceDetails(mapModifyOpeningBalanceDetails);
			if (!bStatus) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean doVerifyOpeningBalanceDetails(Map<String, String> mapOpeningBalanceDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";

		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.id("uniform-rdIsInvestor"), 15);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Opening Balance Edit Page wasn't visible.]";
				return false;
			}
			//Fields of Investor.
			if (mapOpeningBalanceDetails.get("Opening Balance") != null && mapOpeningBalanceDetails.get("Opening Balance").equalsIgnoreCase("Investor")) {
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='rdIsInvestor']//parent::span[@class='checked']"), 5);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Opening Balance Option : 'Investor' is not selected.]";
					bValidateStatus = false;
				}
				if (mapOpeningBalanceDetails.get("Investor Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Investor Name", By.xpath("//div[@id='s2id_investorNameDropdown']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Investor Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Investor Holder Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Investor Holder Name", By.xpath("//div[@id='s2id_investorHolderListDropdown']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Investor Holder Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Investor Account") != null) {
					Map<String, String> mapFetchedFromAccountMaster = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapOpeningBalanceDetails.get("Investor Account"));
					if (mapFetchedFromAccountMaster != null && mapFetchedFromAccountMaster.get("AccountID") != null && !mapFetchedFromAccountMaster.get("AccountID").equalsIgnoreCase("")) {
						mapOpeningBalanceDetails.put("Investor Account", mapFetchedFromAccountMaster.get("AccountID"));
					}
					bStatus = verifyTextIntextBoxOrDropDown("Investor Account", By.xpath("//div[@id='s2id_investorAccountListDropdown']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Investor Account"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Client Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Client Name", By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Client Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Fund Family Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Fund Family Name", By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Fund Family Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Legal Entity Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Legal Entity Name", By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Legal Entity Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Class Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Class Name", By.xpath("//div[@id='s2id_cmbClassName']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Class Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Series Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Series Name", By.xpath("//div[@id='s2id_cmbSeriesName']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Series Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
			}
			//Fields Of Fund.
			if (mapOpeningBalanceDetails.get("Opening Balance") != null && mapOpeningBalanceDetails.get("Opening Balance").equalsIgnoreCase("Fund")) {
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='rdIsFund']//parent::span[@class='checked']"), 5);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Opening Balance Option : 'Fund' is not selected.]";
					bValidateStatus = false;
				}				
				if (mapOpeningBalanceDetails.get("Client Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Client Name", By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Client Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Fund Family Name") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("Fund Family Name", By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Fund Family Name"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("From Legal Entity") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("From Legal Entity", By.xpath("//div[@id='s2id_cmbFundNameFrom']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("From Legal Entity"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("To Legal Entity") != null) {
					bStatus = verifyTextIntextBoxOrDropDown("To Legal Entity", By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("To Legal Entity"), "Yes", false);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (mapOpeningBalanceDetails.get("Fund Account") != null) {
					Map<String, String> mapFetchedFromAccountMaster = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", mapOpeningBalanceDetails.get("Fund Account"));
					if (mapFetchedFromAccountMaster != null && mapFetchedFromAccountMaster.get("AccountID") != null && !mapFetchedFromAccountMaster.get("AccountID").equalsIgnoreCase("")) {
						mapOpeningBalanceDetails.put("Fund Account", mapFetchedFromAccountMaster.get("AccountID"));
					}
					bStatus = verifyTextIntextBoxOrDropDown("Fund Account", By.xpath("//div[@id='s2id_fundAccountListDropdown']//span[contains(@id,'select2-chosen')]"), mapOpeningBalanceDetails.get("Fund Account"), "Yes", false);
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
			}
			//Common fields in Basic Info.
			if (mapOpeningBalanceDetails.get("Initial Subscription Amount") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Initial Subscription Amount", By.xpath("//label[normalize-space()='Initial Subscription Amount']//following-sibling::input"), mapOpeningBalanceDetails.get("Initial Subscription Amount"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Initial Subscription Date") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Initial Subscription Date", By.xpath("//label[normalize-space()='Initial Subscription Date']//following-sibling::div//input"), mapOpeningBalanceDetails.get("Initial Subscription Date"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Last Closing Date") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Last Closing Date", By.id("lastClosingDate1"), mapOpeningBalanceDetails.get("Last Closing Date"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			bStatus = doVerifyCommonOpeningBalanceDetails(mapOpeningBalanceDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			Messages.errorMsg = sAppendErrorMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
		return bValidateStatus;
	}

	public static boolean doVerifyCommonOpeningBalanceDetails(Map<String, String> mapOpeningBalanceDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (mapOpeningBalanceDetails.get("High Water Mark") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("High Water Mark", By.xpath("//label[normalize-space()='High Water Mark']//following-sibling::input"), mapOpeningBalanceDetails.get("High Water Mark"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Closing NAV") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Closing NAV", By.xpath("//label[normalize-space()='Closing NAV']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing NAV"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Closing Units") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Closing Units", By.xpath("//label[normalize-space()='Closing Units']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing Units"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Verify Closing GAV Text box
			if (mapOpeningBalanceDetails.get("Closing GAV") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Closing GAV", By.xpath("//label[normalize-space()='Closing GAV']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing GAV"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//Verify Closing Nav Per Unit
			if (mapOpeningBalanceDetails.get("Closing NAV Per Unit") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Closing NAV Per Unit", By.xpath("//label[normalize-space()='Closing NAV Per Unit']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing NAV Per Unit"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}


			if (mapOpeningBalanceDetails.get("Net MTD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Net MTD", By.xpath("//label[normalize-space()='Net MTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net MTD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net QTD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Net QTD", By.xpath("//label[normalize-space()='Net QTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net QTD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net YTD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Net YTD", By.xpath("//label[normalize-space()='Net YTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net YTD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Management Fee Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Management Fee Carry Forward", By.xpath("//label[normalize-space()='Management Fee Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Management Fee Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Performance Fee Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Performance Fee Carry Forward", By.xpath("//label[normalize-space()='Performance Fee Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Performance Fee Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net ITD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Net ITD", By.xpath("//label[normalize-space()='Net ITD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net ITD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross MTD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Gross MTD", By.xpath("//label[normalize-space()='Gross MTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross MTD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross QTD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Gross QTD", By.xpath("//label[normalize-space()='Gross QTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross QTD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross YTD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Gross YTD", By.xpath("//label[normalize-space()='Gross YTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross YTD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross ITD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Gross ITD", By.xpath("//label[normalize-space()='Gross ITD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross ITD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net P&L Yearly") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Net P&L Yearly", By.xpath("//label[normalize-space()='Net P&L Yearly']//following-sibling::input"), mapOpeningBalanceDetails.get("Net P&L Yearly"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross P&L Yearly") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Gross P&L Yearly", By.xpath("//label[normalize-space()='Gross P&L Yearly']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross P&L Yearly"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Net PTD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Net PTD", By.xpath("//label[normalize-space()='Net PTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Net PTD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Gross PTD") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Gross PTD", By.xpath("//label[normalize-space()='Gross PTD']//following-sibling::input"), mapOpeningBalanceDetails.get("Gross PTD"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			//Verify Closing Fund Nav Per Unit
			if (mapOpeningBalanceDetails.get("Closing Fund NAV Per Unit") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Closing Fund NAV Per Unit", By.xpath("//label[normalize-space()='Closing Fund NAV Per Unit']//following-sibling::input"), mapOpeningBalanceDetails.get("Closing Fund NAV Per Unit"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}

			if (mapOpeningBalanceDetails.get("Adjusted GAV Monthly Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Adjusted GAV Monthly Carry Forward", By.xpath("//label[normalize-space()='Adjusted GAV Monthly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted GAV Monthly Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted GAV Quarterly Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Adjusted GAV Quarterly Carry Forward", By.xpath("//label[normalize-space()='Adjusted GAV Quarterly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted GAV Quarterly Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted GAV Half Yearly Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Adjusted GAV Half Yearly Carry Forward", By.xpath("//label[normalize-space()='Adjusted GAV Half Yearly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted GAV Half Yearly Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted GAV Yearly Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Adjusted GAV Yearly Carry Forward", By.xpath("//label[normalize-space()='Adjusted GAV Yearly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted GAV Yearly Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted NAV Monthly Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Adjusted NAV Monthly Carry Forward", By.xpath("//label[normalize-space()='Adjusted NAV Monthly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted NAV Monthly Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted NAV Quarterly Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Adjusted NAV Quarterly Carry Forward", By.xpath("//label[normalize-space()='Adjusted NAV Quarterly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted NAV Quarterly Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted NAV Half Yearly Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Adjusted NAV Half Yearly Carry Forward", By.xpath("//label[normalize-space()='Adjusted NAV Half Yearly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted NAV Half Yearly Carry Forward"), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (mapOpeningBalanceDetails.get("Adjusted NAV Yearly Carry Forward") != null) {
				bStatus = verifyTextIntextBoxOrDropDown("Adjusted NAV Yearly Carry Forward", By.xpath("//label[normalize-space()='Adjusted NAV Yearly Carry Forward']//following-sibling::input"), mapOpeningBalanceDetails.get("Adjusted NAV Yearly Carry Forward"), "No", true);
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

	public static boolean verifyTextIntextBoxOrDropDown(String sLblName, By txtLocator, String sExpectedText, String YesPlaceHolderNoAttrVal, boolean isFloatsComparison){
		try{
			String sActualvalue ="";
			if (YesPlaceHolderNoAttrVal.equalsIgnoreCase("No")) {				
				sActualvalue = Elements.getElementAttribute(Global.driver, txtLocator, "value");
				if (isFloatsComparison == false) {
					if (sActualvalue == null || !sActualvalue.trim().equalsIgnoreCase(sExpectedText) ) {
						Messages.errorMsg = " [ ERROR : '"+sActualvalue+"' : is actual value for field : "+sLblName+" which is not matching with Expected value : '"+sExpectedText+"' . ] \n";
						return false;
					}
				}
				else {
					if (sActualvalue != null){
						sActualvalue = sActualvalue.replaceAll(",", "");
					}					
					if (sActualvalue == null || Float.parseFloat(sActualvalue.trim()) != Float.parseFloat(sExpectedText) ) {
						Messages.errorMsg = " [ ERROR : '"+sActualvalue+"' : is actual value for field : "+sLblName+" which is not matching with Expected value : '"+sExpectedText+"' . ] \n";
						return false;
					}
				}						
			}
			if (YesPlaceHolderNoAttrVal.equalsIgnoreCase("Yes")) {
				sActualvalue = Elements.getText(Global.driver, txtLocator);
				if (isFloatsComparison == false) {
					if (sActualvalue == null || !sActualvalue.trim().equalsIgnoreCase(sExpectedText) ) {
						Messages.errorMsg = " [ ERROR : '"+sActualvalue+"' : is actual value for field : "+sLblName+" which is not matching with Expected value : '"+sExpectedText+"' . ] \n";
						return false;
					}
				}
				else {
					if (sActualvalue != null){
						sActualvalue = sActualvalue.replaceAll(",", "");
					}
					if (sActualvalue == null || Float.parseFloat(sActualvalue.trim()) != Float.parseFloat(sExpectedText) ) {
						Messages.errorMsg = " [ ERROR : '"+sActualvalue+"' : is actual value for field : "+sLblName+" which is not matching with Expected value : '"+sExpectedText+"' . ] \n";
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

	//Function to input values for Dynamic fields.
	public static boolean doFillDynamicFieldsValues(Map<String, String> mapOpeningBalanceDetails){
		try {
			if (mapOpeningBalanceDetails != null && !mapOpeningBalanceDetails.isEmpty() && mapOpeningBalanceDetails.get("TestCaseName") != null) {
				Map<String, String> mapOBDynamicFieldsDetails = ExcelUtils.readDataABasedOnCellName(Global.sOpeningBalanceTestDataFilePath, "OBDataForDynamicFields", mapOpeningBalanceDetails.get("TestCaseName"));
				if (mapOBDynamicFieldsDetails != null && !mapOBDynamicFieldsDetails.isEmpty()) {
					for (String sFieldName : mapOBDynamicFieldsDetails.keySet()) {
						if (mapOBDynamicFieldsDetails.get(sFieldName) != null && !mapOBDynamicFieldsDetails.get(sFieldName).equalsIgnoreCase("None")) {
							bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()=\""+sFieldName+"\"]//following-sibling::input"), mapOBDynamicFieldsDetails.get(sFieldName).trim());
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to input the Value : '"+mapOBDynamicFieldsDetails.get(sFieldName)+"' ,into the field : '"+sFieldName+"'.]\n";
								return false;
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

	//Function to verify values for Dynamic fields.
	public static boolean doVerifyDynamicFieldsValues(Map<String, String> mapOpeningBalanceDetails){
		try {
			if (mapOpeningBalanceDetails != null && !mapOpeningBalanceDetails.isEmpty() && mapOpeningBalanceDetails.get("TestCaseName") != null) {
				Map<String, String> mapOBDynamicFieldsDetails = ExcelUtils.readDataABasedOnCellName(Global.sOpeningBalanceTestDataFilePath, "OBModifyDataForDynamicFields", mapOpeningBalanceDetails.get("TestCaseName"));
				if (mapOBDynamicFieldsDetails != null && !mapOBDynamicFieldsDetails.isEmpty()) {
					for (String sFiledName : mapOBDynamicFieldsDetails.keySet()) {
						if (mapOBDynamicFieldsDetails.get(sFiledName) != null && !mapOBDynamicFieldsDetails.get(sFiledName).equalsIgnoreCase("None")) {
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'"+sFiledName+"' in Opening Balance,", By.xpath("//label[normalize-space()=\""+sFiledName+"\"]//following-sibling::input"), mapOBDynamicFieldsDetails.get(sFiledName).trim(), "No", true, Integer.parseInt(mapOBDynamicFieldsDetails.get("NoOfDecimalsToDisplay")));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to input the Value : '"+mapOBDynamicFieldsDetails.get(sFiledName)+"' ,into the field : '"+sFiledName+"'.]\n";
								return false;
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
}
