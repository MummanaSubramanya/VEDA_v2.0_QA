package com.viteos.veda.locators;

import org.openqa.selenium.By;

public class Locators {

	public static class LoginPage{

		public static class TextBox{
			public static By txtUser = By.id("j_username");
			public static By txtPassword = By.id("j_password");
		}

		public static class Button{
			public static By btnLogin = By.xpath("//button[contains(text(),'Login')]");
		}

	}

	public static class CommonLocators{
		
		public static class DashBoardMenuDropDowns{
			public static By objTransactionMainMenu = By.xpath("//a[@href='#' and contains(text(),'Transactions')]");
			public static By objMasterMainMenu = By.xpath("//a[@href='#' and contains(text(),'Masters')]");
			public static String sTransactionSubMenu = "//a[@href='#tra_ReplaceIndex']";
			public static String sMasterSubMenu = "//a[@href='#masters_ReplaceIndex']";
		}


		public static class BreadCrumbs{
			public static String crumbMenu = "//ul[@class='page-breadcrumb pull-right']/*[text()='subMenuName']";
			public static String NavigateTo = "//ul[@class='nav nav-tabs']//a[contains(text(),'tabNameValue')]";
		}

		public static class MasterCommonOperationsButtons{
			public static String btnOperation = "//em[@class='operationName']//parent::button";
		}
		public static class MasterSearchTable{
			public static By tableMasterSearch = By.xpath("//div[@id='dataDiv']");
		}
		public static class MasterCommonDropDowns
		{
			public static String ddlSelect = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),\"selectDropDownValue\")]";
			public static By ddlClientName = By.xpath("//div[contains(@id,'s2id_cmbClientName')]//a");
			public static By ddlStatus = By.xpath("//div[contains(@id,'s2id_isActive')]//a");
			public static By ddlFundFamilyName = By.xpath("//div[contains(@id,'s2id_cmbFundFamilyName')]//a");

		}
		public static class MasterSearchLable
		{
			public static By lblSearch = By.xpath("//span[text()='Search panel']");
		}
	}

	public static class ClientMaster{
		public static class TextBox{
			public static By txtClientName = By.id("clientName");
			public static By txtPreviousAdministrator = By.id("entity");
			public static By txtExternalId1 = By.id("externalId1");
			public static By txtExternalId2 = By.id("externalId2");
			public static By txtExternalId3 = By.id("externalId3");	
		}
		public static class RadioButton{
			public static By rBtnSwitchFundFamiliesYes = By.id("rdIsSwithAllowedYes");	
			public static By rBtnSwitchFundFamiliesNO = By.id("rdIsSwithAllowedNo");
		}
		public static class CheckBox{
			public static By chkBoxOrderACK = By.id("isOrderAckYes");	
			public static By chkBoxInitialContract = By.xpath("//input[@id='clientReportTypeList1.reportTypeId.referenceIdPk1']");
			public static By chkBoxFinalContract = By.xpath("//input[@id='clientReportTypeList2.reportTypeId.referenceIdPk1']");
		}
		public static class Button{
			public static By btnSave=By.xpath("//div[contains(@class,'form-actions')]/button[1]");
			public static By btnSaveAsDraft=By.xpath("//div[contains(@class,'form-actions')]/button[2]");
			public static By btnCancel=By.xpath("//div[contains(@class,'form-actions')]/button[3]");

		}
		public static class Label{
			public static By lblSearchPanel = By.xpath("//div[@class='caption']/span[contains(text(),'Search panel')]");
			public static By lblSuccessfullyMsg = By.xpath("//div[@class='alert alert-success']");
		}

		public static class Dropdown{
			public static String ddlSelectValue = "//ul[contains(@id,'select2-results')]//li//div[normalize-space(text())=\"selectDropDownValue\" or text()=\"selectDropDownValue\"]";
			public static By ddlClientClick = By.id("s2id_clientId");
			public static By ddlClientStatusClick = By.xpath("//span[@id='select2-chosen-2']");
		}


	}

	public static class HomePage{
		public static class TextBox{

		}
		public static class Label{
			public static By lblDashboard = By.xpath("//p[contains(text(),'Dashboard')]");
			public static By lblUserNameLabel = By.xpath("//li[@id='header_notification_bar']//following-sibling::li/a");
			public static String lblMenu = "//ul[@class='nav navbar-nav']//a[contains(text(),'menuName')]";
			public static String lblSubMenuName = "//a[contains(text(),'menuName')]//parent::li[@class='menu-dropdown classic-menu-dropdown open' or @class='menu-dropdown classic-menu-dropdown  active open'or @class='menu-dropdown classic-menu-dropdown active open']//a[normalize-space(text())='subMenuName']";
			public static String lblActSubMenuName = "//a[contains(text(),'menuName')]//parent::li[@class='menu-dropdown classic-menu-dropdown  active open']//a[contains(text(),'subMenuName')]";

		}
	}

	public static class FundFamilyMaster
	{
		public static class Ddn
		{
			public static By objClientNameClick = By.xpath("//span[@id='select2-chosen-1']");
			//public static String sClientNameValue="//select[@id='tblClientDefinition.clientId']//option[contains(text(),'ClientNameValue')]";
			public static By objFundFamilyTypeClick = By.xpath("//span[@id='select2-chosen-2']");
			//public static String sFundFamilyType="//div[@class='select2-result-label' and contains(text(),'FundFamilyTypeValue')]";
			public static String objInvestmentStrategy="//select[@id='investmentStarategy.strategyIdPk']";
			public static String objInvestmentRegion = "//select[@id='investmentRegion.countryIdPk']";
			public static By objStatusClick = By.xpath("//span[@id='select2-chosen-3']");
			public static By objLegalEntityNameClick = By.xpath("//span[@id='select2-chosen-4']");
		}
		public static class TextBox
		{
			public static By objFundFamilyName=By.xpath("//input[@id='fundFamilyName']");
			public static By objExternalId1=By.xpath("//input[@id='externalId1']");
			public static By objExternalId2=By.xpath("//input[@id='externalId2']");
			public static By objExternalId3=By.xpath("//input[@id='externalId3']");
			public static By objStartDate=By.xpath("//input[@id='initialOfferingDate']");
		}
		public static class RadioButton
		{
			public static By rBtnSwitchAllowedLegalEntityYes = By.id("rdIsSwitchAllowedBetweenLegalEntitesYes");	
			public static By rBtnSwitchAllowedLegalEntityNo = By.id("rdIsSwitchAllowedBetweenLegalEntitesNo");
		}
		public static class Button
		{
			public static By btnApprove =By.xpath("//button[normalize-space()='Approve']");
			public static By btnSaveAsDraft=By.xpath("//div[contains(@class,'form-actions')]/button[2]");
			public static By btnCancel=By.xpath("//button[normalize-space()='Cancel']");
			public static By btnSave=By.xpath("//button[normalize-space()='Save' or contains(normalize-space(),'Save')]");
		}
		public static class Label
		{
			public static By lblGeneralDetails = By.xpath("//a[contains(text(),'General Details')]");
			public static By lblSuccessfullyMsg=By.xpath("//div[@class='alert alert-success']");
			public static By lblSearchPanel=By.xpath("//div[@class='caption']/span[contains(text(),'Search panel')]");	
		}
	}

	
	public static class LegalEntityMaster
	{
		public static class LeagalEntitySearchPanel{
			public static String objSelectDropDown = "//select[@id='selectDropDownValue']";
			public static By lblLegalEntity = By.xpath("//p[contains(text(),'Legal Entity')]");
			public static By lblClass = By.xpath("//p[contains(text(),'Class')]");
		}
		public static class LegalEntityDetailsTab
		{
			public static class TextBox{
				public static By legalEntityName = By.id("legalEntityDescription");
				public static By accountingId = By.id("accountingId");
				public static By billingCodes = By.id("billingCodes");
				public static By externalId1 = By.id("externalId1");
				public static By externalId2 = By.id("externalId2");
				public static By externalId3 = By.id("externalId3");
			}
			public static class DropDown{
				public static String ddlSelectValue = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),'selectDropDownValue')]";
				public static By clientNameClick = By.id("s2id_clientDefinition.clientId");
				public static By fundFamilyNameClick  = By.id("s2id_cmbFundFamilyName");
				public static By legalEntityTypeClick = By.id("s2id_fkLegalEntityType");
				public static By currencyClick = By.id("s2id_cmbBaseCurrency");
				public static By accountingSystemClick = By.id("s2id_accountingSystem");
				public static By legalEntityDomicileClick = By.id("s2id_investmentRegion.countryIdPk");
				public static By entityTypeClick = By.id("s2id_masterFeeder");
				public static By accountingMethodClick = By.id("s2id_accountingMethod.referenceIdPk");
				public static By investmentStarategyClick = By.id("s2id_investmentStarategy");
				public static By masterFundList = By.id("s2id_masterFundList");
				public static By legalEntityForClone = By.id("s2id_tblLegalEntityForClone");

			}
			public static class RadioButtons
			{
				public static By isSidePocketInvestmentYes = By.id("isSidePocketInvestmentYes");
				public static By isSidePocketInvestmentNo = By.id("isSidePocketInvestmentNo");
				public static By isCloneYes = By.xpath("//input[@id='isCloneYes']");
				public static By isCloneNo = By.xpath("//input[@id='isCloneNo']");
			}
			public static class Buttons{
				public static By btnView = By.xpath("//i[@class='fa fa-eye']");
				public static By btnClone = By.xpath("//i[@class='fa fa-check-circle']");
				public static By closeViewWindow = By.xpath("//em[@class='fa fa-times-circle']");
			}
		}
		public static class GeneralDetailsTab{
			public static class DropDown{
				public static String ddlSelectValue = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),'selectDropDownValue')]";
				public static By unitizedDrpdwnClick = By.xpath("//div[@id='s2id_fkIsUnitizeOrNonUnitize']//span[contains(@id,'select2-chosen')]");
				public static By nonUnitizedValue = By.xpath("//ul[contains(@id,'select2-results')]//li//div[contains(text(),'Non Unitized')]");
				public static By unitDescription = By.xpath("//div[@id='s2id_unitsDescription.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By equalizationMethod = By.xpath("//div[@id='s2id_eqmethodId']//span[contains(@id,'select2-chosen')]");
				public static By reportingFrequency = By.xpath("//div[@id='s2id_reportingFrequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By estimationFrequency = By.xpath("//div[@id='s2id_estimationFrequency']//span[contains(@id,'select2-chosen')]");
				public static By crystalizationFrequency = By.xpath("//div[@id='s2id_crystalizationFrequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By seriesRollUpFrequency = By.xpath("//div[@id='s2id_seriesRollUpFrequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By openOrCloseEnded = By.xpath("//div[@id='s2id_openClosedEnded.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By holidayCalendar = By.xpath("//div[@id='s2id_leHolidayMapList']");
			}
			public static class TextBox{
				public static By unitValue = By.id("unitValue");
				public static By newIssuePercentage = By.id("deMinimusPercentageValue");
				public static By initialOfferingDate = By.id("incorporationDate");
				public static By startDate = By.id("initialOfferingDate");
				public static By fiscalYearEnd = By.id("fiscalYearEnd");
				public static By firstFiscalYearEnd = By.id("firstFiscalYearEnd");
				public static By capitalPercentage = By.id("capitalPercentage");
				public static By noOfYears = By.id("noOfYears");
				public static By year1 = By.id("legalEntitClawbackMappingList0.year");
				public static By percentage1 = By.id("legalEntitClawbackMappingList0.percentage");
				public static By year2 = By.id("legalEntitClawbackMappingList1.year");
				public static By percentage2 = By.id("legalEntitClawbackMappingList1.percentage");
			}
			public static class RadioButton{
				public static By seriesRollUpYes = By.id("uniform-seriesRollUpYes");
				public static By seriesRollUpNo = By.id("uniform-seriesRollUpNo");
				public static By equalizationYes = By.id("uniform-equalizationYes");
				public static By equalizationNo = By.id("uniform-equalizationNo");
				public static By isClawBackYes = By.id("isClawBackYes");
				public static By isClawBackNo = By.id("isClawBackNo");
				public static By isAccuredClawbackAmountYes = By.id("isAccuredClawbackAmountYes");
				public static By isAccuredClawbackAmountNo = By.id("isAccuredClawbackAmountNo");
				public static By commonLabel = By.xpath("//h4//p[contains(text(),'Legal Entity')]");
			}
			public static class CkeckBoxes{
				public static String sWeeklyOffDays = "//label[text()='DayToReplace']/preceding-sibling::div//input";
			}
		}
		public static class OtherDetails{
			public static class TextBox{
				public static By shareDecimalsForCalculations = By.id("shareDecimalsForCalculations");
				public static By shareDecimalsDisplay = By.id("shareDecimalsDisplay");
				public static By navDecimalsForCalculation = By.id("navDecimalsForCalculation");
				public static By navDecimalsDisplay = By.id("navDecimalsDisplay");
				public static By dividendBaseNavValue = By.id("dividendList0.dividendBaseNavValue");
				public static By minimumReinvestmentAmount = By.id("dividendList0.minimumReinvestmentAmount");
				public static By eRISAPersentage = By.id("erisaTrackingList0.erisaPercentage");
				public static String trComments = "tradingRestrictionsListReplaceComments.comments";
				public static By authorizedShares = By.id("authorizedSharesId");
			}
			public static class DateBox{
				public static By dividendFrequency = By.id("dividendList0.dividendFrequency");
				public static String tRDateFrom = "tradingRestrictionsListReplaceFromDatefromDateRange0";
				public static String tRDateTo = "tradingRestrictionsListReplaceToDatetoDateRange0";
			}
			public static class RadioButton{
				public static By isDividendIntendedYes = By.id("isDividendIntendedYes");
				public static By isDividendIntendedNo = By.id("isDividendIntendedNo");
				public static By isErisaApplicableYes = By.id("isErisaApplicableYes");
				public static By isErisaApplicableNo = By.id("isErisaApplicableNo");
				public static By tradingRestriction_Yes = By.id("TradingRestriction_Yes");
				public static By tradingRestriction_No = By.id("TradingRestriction_No");
				public static By authorizedShare_Yes = By.id("AuthorizedShare_Yes");
				public static By authorizedShare_No = By.id("AuthorizedShare_No");
				public static String tRgatingProvisionYes = "tradingRestrictionsListReplaceGatingProvisionYes.gatingProvision1";
				public static String tRgatingProvisionNo = "tradingRestrictionsListReplaceGatingProvisionNo.gatingProvision2";
				public static String tRClosedYes = "tradingRestrictionsListReplaceClosedYes.closed1";
				public static String tRClosedNo = "tradingRestrictionsListReplaceClosedNo.closed2";
			}
			public static class DropDown{
				public static String ddlSelect = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),'selectDropDownValue')]";
				public static By dividendDeterminationBasisClick =By.xpath("//div[@id='s2id_dividendList0.fkDividendDeterminationBasisIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By dividendReinvestmentMethodClick = By.xpath("//div[@id='s2id_dividendList0.fkDividendReinvestmentMethodIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By DividendReferenceClick = By.xpath("//div[@id='s2id_dividendList0.fkDividendReferenceIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				//public static By trTransactionTypeClick = By.xpath("//div[@id='aaaa']//span[contains(@id,'select2-chosen')]");
				public static By accountingLocationClick = By.xpath("//div[@id='s2id_accountingLocation.fundLocationTypeIdPk']//span[contains(@id,'select2-chosen')]");
				public static By administratorSerivicesLocationClick = By.xpath("//div[@id='s2id_administratorLocation.fundLocationTypeIdPk']//span[contains(@id,'select2-chosen')]");
				public static By investorServicesLocationClick = By.xpath("//div[@id='s2id_invServicesLocation.fundLocationTypeIdPk']//span[contains(@id,'select2-chosen')]");
				public static By registrationClick = By.xpath("//div[@id='s2id_invAllowed']//span[contains(@id,'select2-chosen')]");
				public static String sTransactionType = "//select[@id='tradResLog_ReplaceValue']//parent::div//span[contains(@id,'select2')]";
			}
			public static class Buttons{
				public static By traddMoreDates = By.id("addMoreDates");
				public static By addMoreTradingRestrictions = By.id("addMoreTradingRestrictions");
				public static By removeTradingRestrictions = By.id("removeTradingRestrictions");
			}
			
		}
		public static class SubscriptionTab{
			public static class RaioButtons{
				public static By InvestmenetPermittedAmount = By.id("rdIsAmountYes");
				public static By InvestmenetPermittedShares = By.id("rdIsAmountNo");
				public static By InvestmenetPermittedBoth = By.id("rdIsAmountBoth");
				public static By noticePeriodYes = By.id("rbIsSubscriptionNoticePeriodYes");
				public static By noticePeriodNo = By.id("rbIsSubscriptionNoticePeriodNo");
				public static By includeHolidaysYes = By.id("subscriptionNoticePeriodList0.isHolidaysInclude1");
				public static By includeHolidaysNo = By.id("subscriptionNoticePeriodList0.isHolidaysInclude2");
				public static By transactionChargesYes = By.id("Subscription_Charges_Yes");
				public static By transactionChargesNo = By.id("Subscription_Charges_No");
			}
			public static class TextBox{
				public static By npAmountorBPSorPercent = By.id("subscriptionNoticePeriodList0.noticePeriodLockupDetailsList0.rate");
				public static By tcEffectiveDate = By.id("subscriptionChargesList0.wef");
				public static By tcFixedFeeRate = By.id("subscriptionChargesList0.chargeDetailsList0.fixedFeeRate");
				public static By tcAmountFrom = By.id("subscriptionChargesList0.chargeDetailsList0.fromAmount");
				public static By tcAmountTo = By.id("subscriptionChargesList0.chargeDetailsList0.toAmount");
				public static By tctiredRate = By.id("subscriptionChargesList0.chargeDetailsList0.rate");
				public static By minInitialSubAmount = By.id("minInitialSubAmount");
				public static By minSubsequentAmount = By.id("minSubsequentSubAmount");
				public static By minInitialSubShares = By.id("minInitialSubShares");
				public static By minSubsequentSubShares = By.id("minSubsequentSubShares");
				public static By npNoticePeriod = By.id("subscriptionNoticePeriodList0.period");
				
			}
			public static class DropDown{
				public static String ddlSelectValue = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),'selectDropDownValue')]";
				public static By subscriptionFrequencyClick = By.xpath("//div[@id='s2id_subscriptionList0.subscriptionContributionFrequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By newSubscriptionComputationClick = By.xpath("//div[@id='s2id_subscriptionComputationMethodId']//span[contains(@id,'select2-chosen')]");
				public static By presentationMethodClick = By.xpath("//div[@id='s2id_subscriptionPresentationMethodId']//span[contains(@id,'select2-chosen')]");
				public static By noticePeriodDayMonthsClick = By.xpath("//div[@id='s2id_subscriptionNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By noticePeriodCalenderTypeClick = By.xpath("//div[@id='s2id_Notice_Period_SubscriptionsBusinessCalendar']//span[contains(@id,'select2-chosen')]");
				public static By noticePeriodCharges = By.xpath("//div[@id='s2id_noticePeriodTypeSUB']//span[contains(@id,'select2-chosen')]");
				public static By chargesType = By.xpath("//div[@id='s2id_subscriptionChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By rateMethod = By.xpath("//div[@id='s2id_Rate_Method_SUB_NormalCharges']//span[contains(@id,'select2-chosen')]");
				public static By calculationBase = By.xpath("//div[@id='s2id_subscriptionChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
			}
			public static class Buttons{
				public static By tcAddMore = By.xpath("//button[contains(text(),'Add More')]");
				public static By btnAddMore = By.xpath("//button[contains(@onclick,'addChargeDetailsList')]");
		}
	}
		public static class RedemptionTab
		{
			public static class TextBox{
				public static By txtMinimumAmount = By.xpath("//input[@id='redemptionList0.minRedemAmount']");
				public static By txtMinimumShares = By.xpath("//input[@id='redemptionList0.minRedemShares']");	    		
				public static By txtNoticePeriodQuantity = By.xpath("//input[@id='redemptionNoticePeriodList0.period']");
				public static By txtAmountOrBPSOrPercent = By.xpath("//input[@id='redemptionNoticePeriodList0.noticePeriodLockupDetailsList0.rate']");
				public static By txtLockupPeriodQuantity = By.xpath("//input[@id='redemptionHardLockupList0.period']");
				public static By txtLockupFixedAmountOrPercent = By.xpath("//input[@id='redemptionHardLockupList0.noticePeriodLockupDetailsList0.fixedFeeRate']");
				public static String txtLockupSlabRangeFrom = "//input[@id='redemptionHardLockupList0.noticePeriodLockupDetailsListReplaceIndexValue.fromAmount']";
				public static String txtLockupSlabRangeTo = "//input[@id='redemptionHardLockupList0.noticePeriodLockupDetailsListReplaceIndexValue.toAmount']";
				public static String txtLockupSlabCharges = "//input[@id='redemptionHardLockupList0.noticePeriodLockupDetailsListReplaceIndexValue.rate']";

				public static By txtTxnChargesEffectiveDate = By.xpath("//input[@id='redemptionChargesList0.wef']");

				public static By txtRedPartialChargeAsTxnChargeEffectiveDate = By.xpath("//input[@id='redemptionPartialAmountList0.wef']");

				public static By txtTxnFixedFeeRate = By.xpath("//input[@id='redemptionChargesList0.chargeDetailsList0.fixedFeeRate']");

				public static By txtPartRedFixedFeeRate = By.xpath("//input[@id='redemptionPartialAmountList0.chargeDetailsList0.fixedFeeRate']");

				public static String txtTxnSlabOrTierRangeFrom = "//input[@id='redemptionChargesList0.chargeDetailsListReplaceIndexValue.fromAmount']";

				public static String txtTxnSlabOrTierRangeTo = "//input[@id='redemptionChargesList0.chargeDetailsListReplaceIndexValue.toAmount']";

				public static String txtTxnSlabOrTierCharges = "//input[@id='redemptionChargesList0.chargeDetailsListReplaceIndexValue.rate']";

				public static String txtTxnPartRedSlabOrTierRangeFrom = "//input[@id='redemptionPartialAmountList0.chargeDetailsListReplaceIndexValue.fromAmount']";

				public static String txtTxnPartRedSlabOrTierRangeTo = "//input[@id='redemptionPartialAmountList0.chargeDetailsListReplaceIndexValue.toAmount']";

				public static String txtTxnPartRedSlabOrTierCharges = "//input[@id='redemptionPartialAmountList0.chargeDetailsListReplaceIndexValue.rate']";

			}
			public static class DropDown{
				public static By dllClickRedemptionType = By.xpath("//select[@id='redemptionMethodRED']//parent::div//span[contains(@id,'select')]");
				public static String ddlSelectRedemptionType = "//select[@id='redemptionMethodRED']//option[text()='ReplaceOptionVal')]";
				public static By dllClickRedemptionFrequency = By.xpath("//select[@id='redemptionList0.redemptionFrequency.frequencyIdPk']//parent::div//span[contains(@id,'select')]");
				public static String ddlSelectRedemptionFrequency = "//select[@id='redemptionList0.redemptionFrequency.frequencyIdPk']//option[text()='ReplaceOptionVal']";
				public static By ddlClickNoticePeriodQuantityType = By.xpath("//select[@id='redemptionNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//parent::div//span[contains(@id,'select')]");
				public static String ddlSelectNoticePeriodQuantityType = "//select[@id='redemptionNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//option[text()='ReplaceOptionVal']";
				public static By ddlClickCalendarOrBusinessNoticeDays = By.xpath("//div[@id='s2id_noticePeriodRedBusCal']//span[contains(@id,'select')]");
				public static String ddlSelectCalendarOrBusinessNoticeDays = "//select[@id='noticePeriodRedBusCal']//option[text()='ReplaceOptionVal']";
				public static By ddlClickNoticePeriodChargesType = By.xpath("//select[@id='noticePeriodTypeRED']//parent::div//span[contains(@id,'select')]");
				public static String ddlSelectNoticePeriodChargesType = "//select[@id='noticePeriodTypeRED']//option[text()='ReplaceOptionValue']";

				public static By dllClickLockupPeriodQuantityType = By.xpath("//div[@id='s2id_redemptionHardLockupList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select')]");
				public static String ddlSelectLockupType = "//select[@id='redemptionHardLockupList0.fkMonthsDaysIdPk.referenceIdPk']//option[text()='ReplaceOptionVal')]";

				public static By ddlClickCalendarOrBusinessLockupDays = By.xpath("//div[@id='s2id_hardLockupRedBusCal']//span[contains(@id,'select')]");
				public static String ddlSelectCalendarOrBusinessLockupDays = "//select[@id='hardLockupRedBusCal']//option[text()='ReplaceOptionVal']";

				public static By ddlClickLockupRateMethod = By.xpath("//div[@id='s2id_redChargesLockupRateMethodRED']//span[contains(@id,'select')]");
				public static String ddlSelectRateMethod = "//select[@id='redChargesLockupRateMethodRED']//option[text()='ReplaceOptionVal']";

				public static By ddlClickLockupChargesType = By.xpath("//div[@id='s2id_hardLockupChargesType']//span[contains(@id,'select')]");
				public static String ddlSelectLockupChargesType = "//select[@id='hardLockupChargesType']//option[contains(text(),'ReplaceOptionVal')]";

				public static By ddlClickTransactionChargesType = By.xpath("//div[@id='s2id_redemptionChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select')]");
				public static String ddlSelectTransactionChargesType = "//select[@id='redemptionChargesList0.fkChargeTypeIdPk.referenceIdPk']//option[contains(text(),'ReplaceOptionValue')]";
				public static By ddlClickTransactionRateMethod = By.xpath("//div[@id='s2id_redChargesRateMethodRED']//span[contains(@id,'select')]");
				public static String ddlSelectTransactionRateMethod = "//select[@id='redChargesRateMethodRED']//option[contains(text(),'ReplaceOptionValue')]";

				public static By ddlClickTrnsactionChargesCalcBase = By.xpath("//div[@id='s2id_redemptionChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select')]");
				public static String ddlSelectTrnsactionChargesCalcBase = "//select[@id='redemptionChargesList0.fkCalculationBaseIdPk.referenceIdPk']//option[contains(text(),'ReplaceOptionValue')]";

				public static By ddlClickPartialRedChargeRateMethod = By.xpath("//div[@id='s2id_redPartialChargesRateMethodRED']//span[contains(@id,'select')]");
				public static By ddlClickPartialRedChargeRateCalcBase = By.xpath("//div[@id='s2id_redPartialChargesCalMethodRED']//span[contains(@id,'select')]");



			}
			public static class RadioButtons
			{
				public static String isNoticePeriodApplicable = "//input[@id='rbIsRedemptionNoticePeriodYesOrNoReplaceValue']";
				public static String isBusinessHolidaysToIncludeYes = "//input[@id='redemptionNoticePeriodList0.isHolidaysInclude1']";
				public static String isBusinessHolidaysToIncludeNo = "//input[@id='redemptionNoticePeriodList0.isHolidaysInclude2']";
				public static String isLockup = "//input[@id='rdIsHardLockupYesOrNoReplaceValue']";
				public static String isLockupBusinessHolidaysToInclude = "//div[@id='uniform-redemptionHardLockupList0.isHolidaysIncludeReplaceIndexValue']//input";
				public static String isTransactionChargesApplying = "//input[@id='rbIsRedemptionChargeYesOrNoReplaceValue']";
				public static String isRedChargesSameAsTxnCharges = "//input[@id='rbIsPartialAmountRedemptionApplicableYesOrNoReplaceValue']";
			}
			public static class Buttons{

				public static By btnAddMoreLockupSlabs = By.xpath("//button[contains(@onclick,'addRedRateMethodList')]");
				public static String btnRemoveLockupSlabs = "//button[contains(@onclick,'removeRedRateMethodList') and contains(@onclick,'ReplaceIndexValue')]";

				public static By btnAddMoreSlabsOrTiers = By.xpath("//button[contains(@onclick,'addRedChargeDetailsList')]");
				public static String btnRemoveSlabsOrTiers = "//button[contains(@onclick,'removeRedChargeDetailsList') and contains(@onclick,'ReplaceIndexValue')]";

				public static By btnTxnPartRedAddMoreSlabsOrTiers = By.xpath("//button[contains(@onclick,'addRedPartialChargeDetailsList')]");
				public static String btnTxnPartRedRemoveSlabsOrTiers = "//button[contains(@onclick,'addRedPartialChargeDetailsList') and contains(@onclick,'ReplaceIndexValue')]";
				public static By btnAddMore = By.xpath("//button[contains(@onclick,'addRedChargeDetailsList')]");

			}

		}
		public static class Exchange
		{
			public static class Dropdown
			{
				public static String ddlSelectValue = "//ul[@class='select2-results']//li//div[contains(text(),'selectDropDownValue')]";
				public static By ddlFrequencyClick = By.xpath("//div[@id='s2id_exchangeList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidMonthClick = By.xpath("//div[@id='s2id_exchangeNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidCalenderClick = By.xpath("//div[@id='s2id_noticePeriodExchangeBusCal']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidChargesClick = By.xpath("//div[@id='s2id_noticePeriodTypeEXCHANGE']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionChargesTypeClick = By.xpath("//div[@id='s2id_exchangeChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionRateMethodClick = By.xpath("//div[@id='s2id_chargesRateMethodEXC']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionCalulationBase = By.xpath("//div[@id='s2id_exchangeChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]"); 	
			}
			public static class RadioButton
			{
				public static By rbtnIncentiveFeeYes = By.xpath("//input[@id='exchangeList0.isTransactionCrystallized1']");
				public static By rbtnIncentiveFeeNO = By.xpath("//input[@id='exchangeList0.isTransactionCrystallized2']");
				public static By rbtnNoticePeriodYes = By.xpath("//input[@id='rbIsExchangeNoticePeriodYes']");
				public static By rbtnNoticePeriodNo = By.xpath("//input[@id='rbIsExchangeNoticePeriodNo']");
				public static By rbtnTransactionChargesYes = By.xpath("//input[@id='rbIsExchangeChargeYes']");
				public static By rbtnTransactionChargesNO = By.xpath("//input[@id='rbIsExchangeChargeNo']");
				public static By rbtnIncludeHolidayYes = By.xpath("//div[@id='isHolidaysIncludeExchangeNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeYes']");
				public static By rbtnIncludeHolidayNo = By.xpath("//div[@id='isHolidaysIncludeExchangeNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeNo']");


			}
			public static class TextBox
			{
				public static By txtNoticePeriod = By.id("exchangeNoticePeriodList0.period");
				public static By txtEffectiveDate = By.id("exchangeChargesList0.wef");
				public static By txtFixedFeeRate = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.fixedFeeRate']");
				public static By txtAmountBpsPerc = By.id("exchangeNoticePeriodList0.noticePeriodLockupDetailsList0.rate");
				public static By txtFixedfee = By.id("exchangeChargesList0.chargeDetailsList0.fixedFeeRate");
				public static By txtAmoutFrom = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.fromAmount']");
				public static By txtAmoutTo = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.toAmount']");
				public static By txtCharges = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.rate']");
				public static By txtAmoutFrom1 = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList1.fromAmount']");
				public static By txtAmoutTo1 = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList1.toAmount']");
				public static By txtCharges1 = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList1.rate']");

			}
			public static class Label
			{
				public static By lblExchange = By.xpath("//a[text()=' Exchange']");
				public static By lblExchangewait = By.xpath("//div[@class='tab-pane fade active in' and @id='tab8']");
			}
			public static class Button
			{
				public static By btnAddmore = By.xpath("//button[contains(text(),'Add More')]");
				public static By btnRemove = By.xpath("//button[contains(text(),'Remove')]");

			}
		}
		public static class Switch
		{
			public static class Dropdown
			{
				public static String ddlSelectValue = "//ul[@class='select2-results']//li//div[contains(text(),'selectDropDownValue')]";
				public static By ddlFrequencyClick = By.xpath("//div[@id='s2id_switchList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidMonthClick= By.xpath("//div[@id='s2id_switchNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidCalenderClick= By.xpath("//div[@id='s2id_noticePeriodSwitchBusCal']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidChargesClick= By.xpath("//div[@id='s2id_noticePeriodTypeSWITCH']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionChargesTypeClick= By.xpath("//div[@id='s2id_switchChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionRateMethodClick = By.xpath("//div[@id='s2id_chargesRateMethodSWI']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionCalulationBase = By.xpath("//div[@id='s2id_switchChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");	

			}
			public static class RadioButton
			{
				public static By rbtnIncentiveFeeYes = By.xpath("//input[@id='switchList0.isTransactionCrystallized1']");
				public static By rbtnIncentiveFeeNO = By.xpath("//input[@id='switchList0.isTransactionCrystallized2']");
				public static By rbtnNoticePeriodYes = By.xpath("//input[@id='rbIsSwitchNoticePeriodYes']");
				public static By rbtnNoticePeriodNo = By.xpath("//input[@id='rbIsSwitchNoticePeriodNo']");
				public static By rbtnTransactionChargesYes = By.xpath("//input[@id='rbIsSwitchChargeYes']");
				public static By rbtnTransactionChargesNO = By.xpath("//input[@id='rbIsSwitchChargeNo']");
				public static By rbtnIncludeHolidayYes = By.xpath("//div[@id='isHolidaysIncludeSwitchNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeYes']");
				public static By rbtnIncludeHolidayNo = By.xpath("//div[@id='isHolidaysIncludeSwitchNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeNo']");

			}
			public static class TextBox
			{
				public static By txtNoticePeriod = By.id("switchNoticePeriodList0.period");
				public static By txtEffectiveDate = By.id("switchChargesList0.wef");
				public static By txtFixedFeeRate = By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.fixedFeeRate']");
				public static By txtAmountBpsPerc = By.id("switchNoticePeriodList0.noticePeriodLockupDetailsList0.rate");
				public static By txtAmoutFrom = By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.fromAmount']");
				public static By txtAmoutTo = By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.toAmount']");
				public static By txtCharges = By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.rate']");
				public static By txtAmoutFrom1 = By.xpath("//input[@id='switchChargesList0.chargeDetailsList1.fromAmount']");
				public static By txtAmoutTo1 = By.xpath("//input[@id='switchChargesList0.chargeDetailsList1.toAmount']");
				public static By txtCharges1 = By.xpath("//input[@id='switchChargesList0.chargeDetailsList1.rate']");

			}
			public static class Label
			{
				public static By lblSwitch = By.xpath("//a[text()=' Switch']");
				public static By lblswitchwait = By.xpath("//div[@class='tab-pane fade active in' and @id='tab7']");
			}
		}
		public static class Transfer
		{
			public static class Dropdown
			{
				public static String ddlSelectValue = "//ul[@class='select2-results']//li//div[contains(text(),'selectDropDownValue')]";
				public static By ddlFrequencyClick = By.xpath("//div[@id='s2id_transferList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidMonthClick= By.xpath("//div[@id='s2id_transferNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidCalenderClick= By.xpath("//div[@id='s2id_noticePeriodTransferBusCal']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidChargesClick= By.xpath("//div[@id='s2id_noticePeriodTypeTRANSFER']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionChargesTypeClick= By.xpath("//div[@id='s2id_transferChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionRateMethodClick= By.xpath("//div[@id='s2id_chargesRateMethodTRA']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionCalulationBase = By.xpath("//div[@id='s2id_transferChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
			}
			public static class RadioButton
			{
				public static By rbtnIncentiveFeeYes = By.xpath("//input[@id='transferList0.isTransactionCrystallized1']");
				public static By rbtnIncentiveFeeNO = By.xpath("//input[@id='transferList0.isTransactionCrystallized2']");
				public static By rbtnNoticePeriodYes = By.xpath("//input[@id='rbIsTransferNoticePeriodYes']");
				public static By rbtnNoticePeriodNo = By.xpath("//input[@id='rbIsTransferNoticePeriodNo']");
				public static By rbtnTransactionChargesYes = By.xpath("//input[@id='rbIsTransferChargeYes']");
				public static By rbtnTransactionChargesNO = By.xpath("//input[@id='rbIsTransferChargeNo']");
				public static By rbtnIncludeHolidayYes = By.xpath("//div[@id='isHolidaysIncludeTransferNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeYes']");
				public static By rbtnIncludeHolidayNo = By.xpath("//div[@id='isHolidaysIncludeTransferNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeNo']");
				public static By rbtnChangeUBOYes = By.id("uniform-transferList0.isChangeInUBO1");
				public static By btnChangeUBONo = By.id("uniform-transferList0.isChangeInUBO2");

			}
			public static class TextBox
			{
				public static By txtNoticePeriod = By.id("transferNoticePeriodList0.period");
				public static By txtEffectiveDate = By.id("transferChargesList0.wef");
				public static By txtFixedFeeRate = By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.fixedFeeRate']");
				public static By txtAmountBpsPerc = By.id("transferNoticePeriodList0.noticePeriodLockupDetailsList0.rate");
				public static By txtAmoutFrom = By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.fromAmount']");
				public static By txtAmoutTo = By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.toAmount']");
				public static By txtCharges = By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.rate']");
				public static By txtAmoutFrom1 = By.xpath("//input[@id='transferChargesList0.chargeDetailsList1.fromAmount']");
				public static By txtAmoutTo1 = By.xpath("//input[@id='transferhargesList0.chargeDetailsList1.toAmount']");
				public static By txtCharges1 = By.xpath("//input[@id='transferChargesList0.chargeDetailsList1.rate']");

			}
			public static class Label
			{
				public static By lblTransfer = By.xpath("//a[text()=' Transfer']");
				public static By lblswitchwait = By.xpath("//div[@class='tab-pane fade active in' and @id='tab6']");
			}

		}
		public static class Button{
			public static By btnSave=By.xpath("//div[contains(@class,'form-actions')]/button[1]");
			public static By btnSaveAsDraft=By.xpath("//div[contains(@class,'form-actions')]/button[2]");
			public static By btnCancel=By.xpath("//div[contains(@class,'form-actions')]/button[3]");
		}
	}
	
	public static class ClassMaster{
		
		public static String commonDropdownForYearlySelect = "//ul[@class='select2-results']//li//div[text()='selectDropDownValue']"; 

		public static class Transfer{
			public static class Dropdown{
				public static String ddlSelectValue = "//ul[@class='select2-results']//li//div[contains(text(),'selectDropDownValue')]";
				public static By ddlFrequencyClick = By.xpath("//div[@id='s2id_transferList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidMonthClick = By.xpath("//div[@id='s2id_transferNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidCalenderClick = By.xpath("//div[@id='s2id_noticePeriodTransferBusCal']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidChargesClick = By.xpath("//div[@id='s2id_noticePeriodTypeTRANSFER']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionChargesTypeClick = By.xpath("//div[@id='s2id_transferChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionRateMethodClick = By.xpath("//div[@id='s2id_chargesRateMethodTRA']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionCalulationBase = By.xpath("//div[@id='s2id_transferChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
			}
			public static class RadioButton{
				public static By rbtnIncentiveFeeYes = By.xpath("//input[@id='transferList0.isTransactionCrystallized1']");
				public static By rbtnIncentiveFeeNO = By.xpath("//input[@id='transferList0.isTransactionCrystallized2']");
				public static By rbtnNoticePeriodYes = By.xpath("//input[@id='rbIsTransferNoticePeriodYes']");
				public static By rbtnNoticePeriodNo = By.xpath("//input[@id='rbIsTransferNoticePeriodNo']");
				public static By rbtnTransactionChargesYes = By.xpath("//input[@id='rbIsTransferChargeYes']");
				public static By rbtnTransactionChargesNO = By.xpath("//input[@id='rbIsTransferChargeNo']");
				public static By rbtnIncludeHolidayYes = By.xpath("//div[@id='isHolidaysIncludeTransferNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeYes']");
				public static By rbtnIncludeHolidayNo = By.xpath("//div[@id='isHolidaysIncludeTransferNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeNo']");
				public static By rbtnChangeUBOYes = By.id("uniform-transferList0.isChangeInUBO1");
				public static By btnChangeUBONo = By.id("uniform-transferList0.isChangeInUBO2");
			}
			public static class TextBox{
				public static By txtNoticePeriod = By.id("transferNoticePeriodList0.period");
				public static By txtEffectiveDate = By.id("transferChargesList0.wef");
				public static By txtFixedFeeRate = By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.fixedFeeRate']");
				public static By txtAmountBpsPerc = By.id("transferNoticePeriodList0.noticePeriodLockupDetailsList0.rate");
				public static By txtAmoutFrom = By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.fromAmount']");
				public static By txtAmoutTo = By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.toAmount']");
				public static By txtCharges = By.xpath("//input[@id='transferChargesList0.chargeDetailsList0.rate']");
				public static By txtAmoutFrom1 = By.xpath("//input[@id='transferChargesList0.chargeDetailsList1.fromAmount']");
				public static By txtAmoutTo1 = By.xpath("//input[@id='transferhargesList0.chargeDetailsList1.toAmount']");
				public static By txtCharges1 = By.xpath("//input[@id='transferChargesList0.chargeDetailsList1.rate']");
			}
			public static class Label{
				public static By lblTransfer = By.xpath("//a[@href='#tab6' and (text()='Transfer')]");
				public static By lblswitchwait = By.xpath("//div[@class='tab-pane fade active in' and @id='tab6']");
			}
		}
		public static class Switch{
			public static class Dropdown{
				public static String ddlSelectValue = "//ul[@class='select2-results']//li//div[contains(text(),'selectDropDownValue')]";
				public static By ddlFrequencyClick = By.xpath("//div[@id='s2id_switchList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidMonthClick = By.xpath("//div[@id='s2id_switchNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidCalenderClick = By.xpath("//div[@id='s2id_noticePeriodSwitchBusCal']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidChargesClick = By.xpath("//div[@id='s2id_noticePeriodTypeSWITCH']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionChargesTypeClick = By.xpath("//div[@id='s2id_switchChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionRateMethodClick = By.xpath("//div[@id='s2id_chargesRateMethodSWI']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionCalulationBase = By.xpath("//div[@id='s2id_switchChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");

			}
			public static class RadioButton{
				public static By rbtnIncentiveFeeYes = By.xpath("//input[@id='switchList0.isTransactionCrystallized1']");
				public static By rbtnIncentiveFeeNO = By.xpath("//input[@id='switchList0.isTransactionCrystallized2']");
				public static By rbtnNoticePeriodYes = By.xpath("//input[@id='rbIsSwitchNoticePeriodYes']");
				public static By rbtnNoticePeriodNo = By.xpath("//input[@id='rbIsSwitchNoticePeriodNo']");
				public static By rbtnTransactionChargesYes = By.xpath("//input[@id='rbIsSwitchChargeYes']");
				public static By rbtnTransactionChargesNO = By.xpath("//input[@id='rbIsSwitchChargeNo']");
				public static By rbtnIncludeHolidayYes = By.xpath("//div[@id='isHolidaysIncludeSwitchNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeYes']");
				public static By rbtnIncludeHolidayNo = By.xpath("//div[@id='isHolidaysIncludeSwitchNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeNo']");

			}
			public static class TextBox{
				public static By txtNoticePeriod = By.id("switchNoticePeriodList0.period");
				public static By txtEffectiveDate = By.id("switchChargesList0.wef");
				public static By txtFixedFeeRate = By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.fixedFeeRate']");
				public static By txtAmountBpsPerc = By.id("switchNoticePeriodList0.noticePeriodLockupDetailsList0.rate");
				public static By txtAmoutFrom = By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.fromAmount']");
				public static By txtAmoutTo = By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.toAmount']");
				public static By txtCharges = By.xpath("//input[@id='switchChargesList0.chargeDetailsList0.rate']");
				public static By txtAmoutFrom1 = By.xpath("//input[@id='switchChargesList0.chargeDetailsList1.fromAmount']");
				public static By txtAmoutTo1 = By.xpath("//input[@id='switchChargesList0.chargeDetailsList1.toAmount']");
				public static By txtCharges1 = By.xpath("//input[@id='switchChargesList0.chargeDetailsList1.rate']");

			}
			public static class Label{
				public static By lblSwitch = By.xpath("//a[@href='#tab7' and (text()='Switch')]");
				public static By lblswitchwait = By.xpath("//div[@class='tab-pane fade active in' and @id='tab7']");
			}
		}
		public static class Exchange{
			public static class Dropdown{
				public static String ddlSelectValue = "//ul[@class='select2-results']//li//div[contains(text(),'selectDropDownValue')]";
				public static By ddlFrequencyClick = By.xpath("//div[@id='s2id_exchangeList0.frequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidMonthClick = By.xpath("//div[@id='s2id_exchangeNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidCalenderClick = By.xpath("//div[@id='s2id_noticePeriodExchangeBusCal']//span[contains(@id,'select2-chosen')]");
				public static By ddlNoticePeroidChargesClick = By.xpath("//div[@id='s2id_noticePeriodTypeEXCHANGE']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionChargesTypeClick = By.xpath("//div[@id='s2id_exchangeChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionRateMethodClick = By.xpath("//div[@id='s2id_chargesRateMethodEXC']//span[contains(@id,'select2-chosen')]");
				public static By ddlTransactionCalulationBase = By.xpath("//div[@id='s2id_exchangeChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");	
			}
			public static class RadioButton{
				public static By rbtnIncentiveFeeYes = By.xpath("//input[@id='exchangeList0.isTransactionCrystallized1']");
				public static By rbtnIncentiveFeeNO = By.xpath("//input[@id='exchangeList0.isTransactionCrystallized2']");
				public static By rbtnNoticePeriodYes = By.xpath("//input[@id='rbIsExchangeNoticePeriodYes']");
				public static By rbtnNoticePeriodNo = By.xpath("//input[@id='rbIsExchangeNoticePeriodNo']");
				public static By rbtnTransactionChargesYes = By.xpath("//input[@id='rbIsExchangeChargeYes']");
				public static By rbtnTransactionChargesNO = By.xpath("//input[@id='rbIsExchangeChargeNo']");
				public static By rbtnIncludeHolidayYes = By.xpath("//div[@id='isHolidaysIncludeExchangeNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeYes']");
				public static By rbtnIncludeHolidayNo = By.xpath("//div[@id='isHolidaysIncludeExchangeNoticePeriod']//label/div[@id='uniform-rbIsHolidaysIncludeNo']");


			}
			public static class TextBox{
				public static By txtNoticePeriod = By.id("exchangeNoticePeriodList0.period");
				public static By txtEffectiveDate = By.id("exchangeChargesList0.wef");
				public static By txtFixedFeeRate = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.fixedFeeRate']");
				public static By txtAmountBpsPerc = By.id("exchangeNoticePeriodList0.noticePeriodLockupDetailsList0.rate");
				public static By txtFixedfee = By.id("exchangeChargesList0.chargeDetailsList0.fixedFeeRate");
				public static By txtAmoutFrom = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.fromAmount']");
				public static By txtAmoutTo = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.toAmount']");
				public static By txtCharges = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList0.rate']");
				public static By txtAmoutFrom1 = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList1.fromAmount']");
				public static By txtAmoutTo1 = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList1.toAmount']");
				public static By txtCharges1 = By.xpath("//input[@id='exchangeChargesList0.chargeDetailsList1.rate']");

			}
			public static class Label{
				public static By lblExchange = By.xpath("//a[@href='#tab8' and (text()='Exchange')]");
				public static By lblExchangewait = By.xpath("//div[@class='tab-pane fade active in' and @id='tab8']");
			}
			public static class Button{
				public static By btnAddmore = By.xpath("//button[contains(text(),'Add More')]");
				public static By btnRemove = By.xpath("//button[contains(text(),'Remove')]");

			}
		}
		public static class ClassDetails{
			public static class Dropdown{
				public static String ddlSelect = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),'selectDropDownValue')]";
				public static By clientNameClick = By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]");
				public static By fundFamilyNameClick  = By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]");
				public static By legalEntityNameClick  = By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]");
				public static By investmetnStratefyClick  = By.xpath("//div[@id='s2id_investmentStrategy']//span[contains(@id,'select2-chosen')]");
				public static By classCurrencyClick  = By.xpath("//div[@id='s2id_classCurrency']//span[contains(@id,'select2-chosen')]");
				public static By classforCloneClick  = By.xpath("//div[@id='s2id_classForClone']//span[contains(@id,'select2-chosen')]");
				public static By sidePocketManagementFeeClick  = By.xpath("//div[@id='s2id_sidePocketMgmtFee']//span[contains(@id,'select2-chosen')]");
				public static By sidePocketAdministrationFeeClick  = By.xpath("//div[@id='s2id_sidePocketAdminFee']//span[contains(@id,'select2-chosen')]");
			}
			public static class TextBox{
				public static By className = By.id("className");
				public static By classShortName = By.id("classCode");
				public static By externalId1 = By.id("externalId1");
				public static By externalId2 = By.id("externalId2");
				public static By externalId3 = By.id("externalId3");
			}
			public static class RadioButton{
				public static By isSidePocketClassYes = By.id("rdIsSidePocketClassYes");
				public static By isSidePocketClassNo = By.id("rdIsSidePocketClassNo");
				public static By isCloneYes = By.id("isCloneYes");
				public static By isCloneNo = By.id("isCloneNo");	
			}
			public static class Button{
				public static By btnView = By.xpath("//i[@class='fa fa-eye']//parent::button");
				public static By btnClone = By.xpath("//i[@class='fa fa-check-circle']//parent::button");
				public static By closeViewWindow = By.xpath("//em[@class='fa fa-times-circle']");
			}
		}
		public static class GeneralDetails{
			public static class  Dropdown{
				public static String ddlSelectValue = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),'selectDropDownValue')]";
				public static By unitizedDrpdwnClick = By.xpath("//div[@id='s2id_fkClassType']//span[contains(@id,'select2-chosen')]");
				public static By nonUnitizedValue = By.xpath("//ul[contains(@id,'select2-results')]//li//div[contains(text(),'Non Unitized')]");
				public static By unitizedValue = By.xpath("//ul[contains(@id,'select2-results')]//li//div[text()='Unitized']");
				public static By unitDescription = By.xpath("//div[@id='s2id_unitDescription']//span[contains(@id,'select2-chosen')]");
			}
			public static class TextBox{
				public static By unitValue = By.id("unitValue");
				public static By initialOfferingPrice = By.id("initialOfferingPrice");
				public static By startDate = By.id("initalOfferingDate");
			}
			public static class RadioButton{
				public static By votingSharesYes = By.id("rdIsVotingSharesYes");
				public static By votingSharesNo = By.id("rdIsVotingSharesNo");
				public static By issuedInSeriesYes = By.id("rdIsIssuedInSeriesYes");
				public static By issuedInSeriesNo = By.id("rdIsIssuedInSeriesNo");
				public static By newIssueEligibleYes = By.id("deMinimusYes");
				public static By newIssueEligibleNo = By.id("deMinimusNo");
				public static By commonLabel = By.xpath("//h4//p[contains(text(),'Class')]");
			}
		}
		public static class OtherDetails{
			public static class TextBox{
				public static By shareDecimalsForCalculationsClass = By.id("shareDecimalsForCalculationsClass");
				public static By shareDecimalsDisplayClass = By.id("shareDecimalsDisplayClass");
				public static By navDecimalsForCalculationClass = By.id("navDecimalsForCalculationClass");
				public static By navDecimalsDisplayClass = By.id("navDecimalsDisplayClass");
				public static By dividendBaseNavValue = By.id("dividendList0.dividendBaseNavValue");
				public static By minimumReinvestmentAmount = By.id("dividendList0.minimumReinvestmentAmount");
				public static By trComments = By.id("tradingRestrictionsList0.comments");
			}
			public static class Dropdown{
				public static String ddlSelect = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),'selectDropDownValue')]";
				public static By dividendDeterminationBasisClick = By.xpath("//div[@id='s2id_dividendList0.fkDividendDeterminationBasisIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By dividendReinvestmentMethodClick = By.xpath("//div[@id='s2id_dividendList0.fkDividendReinvestmentMethodIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By DividendReferenceClick = By.xpath("//div[@id='s2id_dividendList0.fkDividendReferenceIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static String sTransactionType = "//select[@id='tradResLog_ReplaceValue']//parent::div//span[contains(@id,'select2')]";
				//public static By countryCode = By.xpath("//div[@id='aaaa']//span[contains(@id,'select2-chosen')]");
			}
			public static class RadioButton{
				public static By isDividendIntendedYes = By.id("rdIsDividendIntendedYes");
				public static By isDividendIntendedNo = By.id("rdIsDividendIntendedNo");
				public static By tradingRestriction_Yes = By.id("rbIsTradingRestrictionsApplicableYes");
				public static By tradingRestriction_No = By.id("rbIsTradingRestrictionsApplicableNo");
				public static By gatingProvisionYes = By.id("tradingRestrictionsList0.gatingProvision1");
				public static By gatingProvisionNo = By.id("tradingRestrictionsList0.gatingProvision2");
				public static By closedYes = By.id("tradingRestrictionsList0.closed1");
				public static By closedNo = By.id("tradingRestrictionsList0.closed2");
				public static By geoBasedRestictionForInvestorYes = By.id("rbIsGeoBasedRestictionForInvestorYes");
				public static By geoBasedRestictionForInvestorNo = By.id("rbIsGeoBasedRestictionForInvestorNo");
			}
			public static class DateBox{
				public static By dividendFrequency = By.id("dividendList0.dividendFrequency");
				public static By tRDateFrom = By.id("tradingRestrictionsList0fromDateRange0");
				public static By tRDateTo = By.id("tradingRestrictionsList0toDateRange0");
				public static By iRDateFrom = By.id("fromDateRange0");
				public static By iRToDate = By.id("toDateRange0");				
			}
			public static class Button{
				public static By iRAddMore = By.xpath("//em[@class='glyphicon glyphicon-plus']//parent::button");
				public static By addMoreTradingRestrictions = By.id("addMoreTradingRestrictions");
				public static By traddMoreDates = By.id("addMoreDates");
			}
		}
		public static class SubscriptionTab{
			public static class RaioButtons{
				public static By InvestmenetPermittedAmount = By.id("rdIsAmountYes");
				public static By InvestmenetPermittedShares = By.id("rdIsAmountNo");
				public static By InvestmenetPermittedBoth = By.id("rdIsAmountBoth");
				public static By noticePeriodYes = By.id("rbIsSubscriptionNoticePeriodYes");
				public static By noticePeriodNo = By.id("rbIsSubscriptionNoticePeriodNo");
				public static By includeHolidaysYes = By.id("subscriptionNoticePeriodList0.isHolidaysInclude1");
				public static By includeHolidaysNo = By.id("subscriptionNoticePeriodList0.isHolidaysInclude2");
				public static By transactionChargesYes = By.id("Subscription_Charges_Yes");
				public static By transactionChargesNo = By.id("Subscription_Charges_No");
			}
			public static class TextBox{
				public static By minInitialSubAmount = By.id("minInitialSubAmount");
				public static By minSubsequentAmount = By.id("minSubsequentSubAmount");
				public static By minInitialSubShares = By.id("minInitialSubShares");
				public static By minSubsequentSubShares = By.id("minSubsequentSubShares");
				public static By npAmountorBPSorPercent = By.id("subscriptionNoticePeriodList0.noticePeriodLockupDetailsList0.rate");
				public static By tcEffectiveDate = By.id("subscriptionChargesList0.wef");
				public static By tcFixedFeeRate = By.id("subscriptionChargesList0.chargeDetailsList0.fixedFeeRate");
				public static By tcAmountFrom = By.id("subscriptionChargesList0.chargeDetailsList0.fromAmount");
				public static By tcAmountTo = By.id("subscriptionChargesList0.chargeDetailsList0.toAmount");
				public static By tctiredRate = By.id("subscriptionChargesList0.chargeDetailsList0.rate");
				public static By npNoticePeriod = By.id("subscriptionNoticePeriodList0.period");
			}
			public static class DropDown{
				public static String ddlSelectValue = "//ul[contains(@id,'select2-results')]//li//div[contains(text(),'selectDropDownValue')]";
				public static By subscriptionFrequencyClick = By.xpath("//div[@id='s2id_subscriptionList0.subscriptionContributionFrequency.frequencyIdPk']//span[contains(@id,'select2-chosen')]");
				public static By noticePeriodDayMonthsClick = By.xpath("//div[@id='s2id_subscriptionNoticePeriodList0.fkMonthsDaysIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By noticePeriodCalenderTypeClick = By.xpath("//div[@id='s2id_Notice_Period_SubscriptionsBusinessCalendar']//span[contains(@id,'select2-chosen')]");
				public static By noticePeriodCharges = By.xpath("//div[@id='s2id_noticePeriodTypeSUB']//span[contains(@id,'select2-chosen')]");
				public static By chargesType = By.xpath("//div[@id='s2id_subscriptionChargesList0.fkChargeTypeIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
				public static By rateMethod = By.xpath("//div[@id='s2id_Rate_Method_SUB_NormalCharges']//span[contains(@id,'select2-chosen')]");
				public static By calculationBase = By.xpath("//div[@id='s2id_subscriptionChargesList0.fkCalculationBaseIdPk.referenceIdPk']//span[contains(@id,'select2-chosen')]");
			}
		}
	}

	public static class ParameterMaster{
		public static class Dropdown
		{
			public static String ddlSelectValue = "//ul[@class='select2-results']//li//div[text()='selectDropDownValue']";
			public static By ddlClientName = By.xpath("//div[@id='s2id_cmbClientName']");
			public static By ddlFundfamily = By.xpath("//div[@id='s2id_cmbFundFamilyName']");
			public static By ddlLegalEnityName = By.xpath("//div[@id='s2id_cmbFundName']");
			public static By ddlClassName = By.xpath("//div[@id='s2id_cmbClassName']");
			public static By ddlPrameterName = By.xpath("//div[@id='s2id_parameterId']");
			public static By ddlAttributes = By.id("s2id_transactionComponentList");
		}
		public static class TextBox{
			public static By txtEffectiveStartDateNewRule = By.id("edate1");
			public static By txtEffectiveDateAllFund = By.id("edate2");
		}
		public static class RadioButton{
			public static By rbtnNewRule = By.id("uniform-newParameter");
			public static By rbtnExistingRule = By.id("uniform-existing");
			public static By rbtnAllFundsRule= By.id("uniform-allParameters");
		}
	}

	public static class ManagementFee{

		public static class Lables{
			public static By lblManagementFee = By.xpath("//p[contains(text(),'Management Fee')]");
		}
		public static class Links{
			public static By lnkNewRule = By.id("newRule");
		}
		public static class TextBox{
			public static By txtEffectiveStartDate = By.id("effectiveStartDate");
			public static By txtFeePercentage = By.id("feePercentage");
			public static By txtSpreadPercentage = By.id("spreadPercentage");
		}
		public static class RadioButtons{
			public static String sRadioReportingPeriod = "isCalcBaseOnRepoPeriodReplaceIndex";
			public static String sRadioFeePeriodTimeCalculation = "isChargeEvenlyReplaceIndex";
			public static String sRadioIsNewSubscriptionEffectiveDate = "isNewSubscriptionEffectiveDateReplaceIndex";
			public static String sRadioIsApplyMinimumFee = "isApplyMinimumFeeReplaceIndex";
		}
	}

	public static class JointHolder{
		public static class TextBox{
			public static By objFirstName = By.id("individualFirstName");
			public static By objMiddleName = By.id("individualMiddleInitials");
			public static By objLastName  =By.id("individualLastName");
		}
		public static class RadioButton{
			public static By objUSPersonYes = By.id("rdisUsPersonYes");
			public static By objUSPersonNo = By.id("rdisUsPersonNo");
			public static By objInvestorSubTypeAccreditedInvestor = By.xpath("//label[normalize-space()='Accredited Investor']//input");
			public static By objInvestorTypeQualilfiedPurchaser = By.xpath("//label[normalize-space()='Qualified Purchaser']//input");
			public static By objInvestorTypeNA = By.xpath("//label[normalize-space()='NA']//input");
			public static By objErisaInvestorYes = By.id("rdisErisaInvestorYes");
			public static By objErisaInvestorNo = By.id("rdiisErisaInvestorNo");
		}
		
	}


}
