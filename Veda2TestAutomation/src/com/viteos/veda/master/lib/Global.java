package com.viteos.veda.master.lib;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.viteos.veda.config.ExcelUtils;

public class Global {

	public static WebDriver driver;

	//config variables
	//public static String sVedaURL = "http://192.168.174.144:7070/veda2/";
	//public static String sVedaURL = "http://192.168.170.39:7070/veda2/";
	//define the file path in which test data files contains
	public static String folderPath = "testdata";
	public static String sBrowserName = "chrome";
	public static String globalTestDataFilePath = folderPath+"//GlobalTestData.xls";
	
	public static Map<String ,String> mapCredentials = ExcelUtils.readDataABasedOnCellName(globalTestDataFilePath, "GlobalTestData", "Y");
	
	public static String sVedaURL = mapCredentials.get("VEDA2_URL");

	public static String sMakerUserName = mapCredentials.get("MakerUserName");
	public static String sMakerPassword = mapCredentials.get("MakerPassword");

	public static String sCheckerUserName = mapCredentials.get("CheckerUserName");
	public static String sCheckerPassword = mapCredentials.get("CheckerPassword");

	//driver paths
	public static String sIEDriverPath = "drivers//IEDriverServer.exe";
	public static String sChromeDriverPath = "drivers//chromedriver.exe";


	//testdata file path
	public static String sClientTestDataFilePath = folderPath+"//ClientTestData.xls";
	public static String sFundFamilyTestDataFilePath = folderPath+"//FundFamilyTestData.xls" ; 
	public static String sFormulaSetupTestDataFilePath = folderPath+"//FormulaSetupTestData.xls";
	public static String sSeriesTestDataFilePath = folderPath+"//SeriesTestData.xls";
	public static String sLegalEntityTestDataFilePath = folderPath+"//LegalEntityTestData.xls";
	public static String sParameterTestDataFilePath = folderPath+"//ParameterTestData.xls";
	public static String sManagementFeeTestDataFilePath = folderPath+"//ManagementFeeTestData.xls";

	public static String sJointHolderTestDataFilePath = folderPath+"//JointHolderTestData.xls";

	public static String sRoleTestDataFilePath  = folderPath+"//RoleTestData.xls";
	public static String sGroupTestDataFilePath  = folderPath+"//GroupTestData.xls";
	public static String sUserTestDataFilePath = folderPath+"//UserTestData.xls";

	public static String sSubscriptionTestData = folderPath+"//SubscriptionTestData.xls";
	public static String sRedemptionTestData = folderPath+"//RedemptionTestData.xls";
	public static String sTransferTestData = folderPath+"//TransferTestData.xls";
	public static String sExchangeTestData = folderPath+"//ExchangeTestData.xls";
	public static String sSwitchTestData = folderPath+"//SwitchTestData.xls";
	public static String sTradeSearchTestData = folderPath+"//TradeSearchTestData.xls";
	public static String sSidePocketSUBTestData = folderPath+"//SidePocketSubscriptionTestData.xls";
	public static String sSidePocketREDTestData = folderPath+"//SidePocketRedemptionTestData.xls";
	
	public static String sAllocationTestDataPath = folderPath+"//AllocationTestData.xls";
	
	public static String sAllocationReportsTestDataPath = folderPath+"//AllocationReportTestData.xls";

	public static String sXMLTestFolderPath = "XMLMessages";

	public static String sModifyLegalEntityTestDataFilePath = folderPath+"//ModifyLegalEntityTestData.xls";

	public static String sClassTestDataFilePath = folderPath+"//ClassMasterTestData.xls";

	public static String sModifyClassTestDataFilePath = folderPath+"//ModifyClassMasterTestData.xls";

	public static String sIncentiveFeeTestDataFilePath = folderPath+"//IncentiveFeeTestData.xls";

	public static String sInvestorAccountTestDataFilePath = folderPath+"//InvestorAccountTestData.xls";

	public static String sInvestorTestDataFilePath = folderPath+"//InvestorsTestData.xls";

	public static String sInvestorModifyTestDataFilePath = folderPath+"//ModifyInvestorDetails.xls";

	public static String sHolderTestDataFilePath = folderPath+"//HolderTestData.xls";

	public static String sHolderModifyTestDataFilePath = folderPath+"//HolderModifyTestData.xls";

	public static String sFeeDistributionTestDataFilePath = folderPath+"//FeeDistributionTestData.xls";

	public static String sFeederSubscriptionTestDataFilePath = folderPath+"//FeederSubscriptionDetails.xls";

	public static String sFeedeerRedemptionTestDataFilePath = folderPath+"//FeederRedemptionDetails.xls";

	public static String sOpeningBalanceTestDataFilePath = folderPath+"//OpeningBalanceTestData.xls";

	public static String sTradeSearchFilePath = folderPath+"//TradeSearchTestData.xls";
}
