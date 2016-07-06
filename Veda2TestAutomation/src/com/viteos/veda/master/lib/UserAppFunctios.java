package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.UserActions;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class UserAppFunctios {

	static boolean bStatus;

	public static boolean editCreatedUser(Map<String, String> userDetailsMap){
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.id("userName"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "User Edit page is not displayed";
				return false;
			}

			if(userDetailsMap.get("User Name") != null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='userName']"), userDetailsMap.get("User Name"));
				if(!bStatus){
					Messages.errorMsg = "User Name Cannot be Entered";
					return false;
				}
			}

			if(userDetailsMap.get("Select User") != null){
				Thread.sleep(2000);
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Email Id", userDetailsMap.get("Select User"));
				if(!bStatus){
					Messages.errorMsg = "User is not Selected from Dropdown :"+Messages.errorMsg;
					return false;
				}
			}
			Thread.sleep(2000);
			/*if(userDetailsMap.get("LoginID")!=null){
				bStatus = NewUICommonFunctions.SelectEditForCreatedUserManagementPortal(userDetailsMap.get("LoginID"), "User");
				if(!bStatus){
					return false;
				}
			}*/



			if(userDetailsMap.get("Department")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Department",userDetailsMap.get("Department"));
				if(!bStatus){
					return false;
				}
			}

			if(userDetailsMap.get("Location")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Location",userDetailsMap.get("Location"));
				if(!bStatus){
					return false;
				}
			}

			if(userDetailsMap.get("Groups")!=null){
				bStatus = selectMultipleValuesInSelectBox("Group", userDetailsMap.get("Groups"), "groupId");
				if(!bStatus){
					return false;
				}
			}

			bStatus = NewUICommonFunctions.doOperationOnUserManagementSubMasters("userButtonDiv","Save");
			if(!bStatus){
				return false;
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private static boolean selectMultipleValuesInSelectBox(String listBoxName,String groupValues, String listBoxID) {
		try {
			/*	String elementLocator1 = "//label[normalize-space(text())='"+listBoxName+"']/following-sibling::div[1]//select[@id='"+listBoxID+"']/option[1]";
			bStatus = Elements.click(Global.driver, By.xpath(elementLocator1));
			if(!bStatus){
				Messages.errorMsg = "Failed to Select the Group ";
				return false;
			}*/

			String[] agroupValues = groupValues.split(",");
			if(agroupValues.length==0){
				return false;
			}
			int index = 0;
			String elementLocator = "//label[normalize-space(text())='"+listBoxName+"']/following-sibling::select[@id='"+listBoxID+"']/option[text()='"+agroupValues[index]+"']";

			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(elementLocator), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = agroupValues[index]+" Group is not present in listbox";
				return false;
			}

			bStatus = UserActions.click(Global.driver, By.xpath(elementLocator));
			if(!bStatus){
				Messages.errorMsg = "Failed to Select the Group "+agroupValues[index];
				return false;
			}	

			if(agroupValues.length>1){

				for (int i = 1; i < agroupValues.length; i++) {
					String eleLocator = "//label[normalize-space(text())='"+listBoxName+"']/following-sibling::select[@id='"+listBoxID+"']/option[text()='"+agroupValues[i]+"']";
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(eleLocator), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = listBoxName+" ,"+agroupValues[i]+" is Not Visible";
						return false;
					}

					WebElement onElement = Elements.getWebElement(Global.driver,By.xpath(eleLocator));
					new Actions(Global.driver).keyDown(Keys.CONTROL).click(onElement).keyDown(Keys.CONTROL).perform();
				}
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

			return false;
		}
	}

	public static boolean verifyFundSetupScreensAndLegalEntitiesAssignedToUser(Map<String, String> userMapDetails){
		try{

			String groupName = userMapDetails.get("Groups");

			Map<String, String> createdGroupMap = ExcelUtils.readDataABasedOnCellName(Global.sGroupTestDataFilePath, "Group", groupName);

			String roleName = createdGroupMap.get("Role");
			Map<String, String> createdRoleMap = ExcelUtils.readDataABasedOnCellName(Global.sRoleTestDataFilePath, "Role",roleName);

			bStatus = checkFundSetupScreensAndLegalEntitiesAssignedInRole(createdRoleMap,createdGroupMap);
			if(!bStatus){
				return false;
			}

			return true;		
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean checkFundSetupScreensAndLegalEntitiesAssignedInRole(Map<String, String> createdRoleMap,Map<String, String> createdGroupMap ){
		try{
			boolean bValidStatus = true;
			String sAppendErrMsg = "";
			String dashBoardActivites = createdRoleMap.get("DashBoard");

			String parentValuesStringList = "";
			String ClientActivites = createdRoleMap.get("Client");
			if(ClientActivites!=null && ClientActivites.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Client";
			}
			String FundFamilyActivites = createdRoleMap.get("Fund Family");
			if(FundFamilyActivites!=null && FundFamilyActivites.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Fund Family";
			}
			String LegalEntityActivities = createdRoleMap.get("Legal Entity");
			if(LegalEntityActivities!=null && LegalEntityActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Legal Entity";
			}
			String ClassActivities = createdRoleMap.get("Class");
			if(ClassActivities!=null && ClassActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Class";
			}
			String SeriesActivities = createdRoleMap.get("Series");
			if(SeriesActivities!=null && SeriesActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Series";
			}
			String ParameterActivities = createdRoleMap.get("Parameter");
			if(ParameterActivities!=null && ParameterActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Parameter";
			}
			String IncentiveFeeActivities = createdRoleMap.get("Incentive Fee");
			if(IncentiveFeeActivities!=null && IncentiveFeeActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Incentive Fee";
			}
			String ManagementFeeActivities = createdRoleMap.get("Management Fee");
			if(ManagementFeeActivities!=null && ManagementFeeActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Management Fee";
			}
			String FormulaActivities = createdRoleMap.get("Formula");
			if(FormulaActivities!=null && FormulaActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Formula";
			}
			String FeeDistributionActivities = createdRoleMap.get("Fee Distribution");
			if(FeeDistributionActivities!=null && FeeDistributionActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Fee Distribution";
			}
			String FeeSubscriptionActivities = createdRoleMap.get("Feeder Subscription");
			if(FeeSubscriptionActivities!=null && FeeSubscriptionActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Feeder SUB";
			}
			String FeederRedemptionActivities = createdRoleMap.get("Feeder Redemption");
			if(FeederRedemptionActivities!=null && FeederRedemptionActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Feeder RED";
			}
			String SubscriptionActivities = createdRoleMap.get("Subscription");
			if(SubscriptionActivities!=null && SubscriptionActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Transaction_SUB";
			}
			String RedemptionActivities = createdRoleMap.get("Redemption");
			if(RedemptionActivities!=null && RedemptionActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Transaction_RED";
			}
			String TransferActivities = createdRoleMap.get("Transfer");
			if(TransferActivities!=null && TransferActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Transaction_TRA";
			}
			String ExchangeActivities = createdRoleMap.get("Exchange");
			if(ExchangeActivities!=null && ExchangeActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Transaction_EXC";
			}

			String SwitchActivities = createdRoleMap.get("Switch");
			if(SwitchActivities!=null && SwitchActivities.contains("Approve")){
				parentValuesStringList =parentValuesStringList +  "Transaction_SWI";
			}

			if(dashBoardActivites!=null){
				bStatus = verifyDashBoardItems(dashBoardActivites);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify DashBoard Screen", "Dash Board screen Verification Failed.ERROR: "+Messages.errorMsg);

					bValidStatus = false;
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
				}

			}

			if(ClientActivites!=null){
				bStatus = verifyClientItems(ClientActivites,createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Client Screen", "Client screen Verification Failed.ERROR: "+Messages.errorMsg);
					bValidStatus = false;
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
				}
			}


			if(FundFamilyActivites!=null){
				bStatus = verifyFundFamilyItems(FundFamilyActivites,createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Fund Family Screen", "Fund Family screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(LegalEntityActivities!=null){
				bStatus = verifyLegalEntityItems(LegalEntityActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Legal Entity Screen", "Legal Entity screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(ClassActivities!=null){
				bStatus = verifyClassMasterItems(ClassActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Class Screen", "Class screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(SeriesActivities!=null){
				bStatus = verifySeriesMasterItems(SeriesActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Series Screen", "Series screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(ParameterActivities!=null){
				bStatus = verifyParameterItems(ParameterActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Parameter Screen", "Parameter screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(ManagementFeeActivities!=null){
				bStatus = verifyManagementFeeActivitiesItems(ManagementFeeActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Managemetn Fee Screen", "Management Fee screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(IncentiveFeeActivities!=null){
				bStatus = verifyIncentiveFeeActivitiesItems(IncentiveFeeActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Incentive Fee Screen", "Incentive Fee screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(FormulaActivities!=null){
				bStatus = verifyFormulaActivitiesActivitiesItems(parentValuesStringList,FormulaActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Formula Screen", "Formula screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(FeeDistributionActivities!=null){
				bStatus = verifyFeeDistributionActivitiesItems(FeeDistributionActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Fee Distribution Screen", "Fee Distribution screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(FeeSubscriptionActivities!=null){
				bStatus = verifyFeeSubscriptionActivitiesItems(FeeSubscriptionActivities , createdGroupMap,parentValuesStringList);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Feeder Subscription Screen", "Feeder Subscription screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(FeederRedemptionActivities!=null){
				bStatus = verifyFeederRedemptionActivitiesItems(FeederRedemptionActivities , createdGroupMap,parentValuesStringList);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Feeder Redemption Screen", "Feeder Redemption screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(SubscriptionActivities!=null){
				bStatus = verifySubscriptionTradeItems(parentValuesStringList,SubscriptionActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Subscription Screen", "Subscription screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(RedemptionActivities!=null){
				bStatus = verifyRedemptionTradeItems(parentValuesStringList,RedemptionActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Redemption Screen", "Redemption screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(TransferActivities!=null){
				bStatus = verifyTransferActivitiesTradeItems(parentValuesStringList,TransferActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Transfer Screen", "Transfer screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}


			if(ExchangeActivities!=null){
				bStatus = verifyExchangeActivitiesTradeItems(parentValuesStringList,ExchangeActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Exchange Screen", "Exchange screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}

			if(SwitchActivities!=null){
				bStatus = verifySwitchActivitiesTradeItems(parentValuesStringList,SwitchActivities , createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Switch Screen", "Switch screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}
			String tradeSearchActivities = createdRoleMap.get("Trade");
			if(tradeSearchActivities != null){
				bStatus = verifyTradeSearchAssignedRoles(tradeSearchActivities,createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Trade Search Screen", "Trade Search screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}

			String allocationActivities = createdRoleMap.get("Allocation");
			if(allocationActivities != null){
				bStatus = verifyAllocationActivities(tradeSearchActivities,createdGroupMap);
				if(!bStatus){
					Reporting.logResults("Fail", "Verify Allocation Screen", "Allocation screen Verification Failed.ERROR: "+Messages.errorMsg);
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidStatus = false;			
				}
			}

			Messages.errorMsg = sAppendErrMsg;
			return bValidStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyAllocationActivities(String tradeSearchActivities, Map<String, String> createdGroupMap) {
		try {

			bStatus = NewUICommonFunctions.selectMenu("ALLOCATION", "Process");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to ALLOCATION Screen", "Failed to Navigate to ALLOCATION Screen :"+Messages.errorMsg);
				return false;
			}

			//Verifying the Clients available in Drop down
			List<String> clientValues=Arrays.asList(createdGroupMap.get("Client").split(":"));
			int clientsCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='client']/option"));			
			if (clientsCount-1!=clientValues.size()) {
				Messages.errorMsg = "Actual no of Clients are not equal to Expected Clients";
				return false ;
			}

			for (int iCount = 2; iCount <= clientsCount; iCount++) {
				String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='client']/option["+iCount+"]"));
				if(!clientValues.contains(entityNames)){
					Messages.errorMsg = "Clients "+entityNames+" is Not matching with the Expected";
					return false;
				}

			}

			//Select Clinet to get the Fund Family values in Drop down and to Verify Fund Family
			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(createdGroupMap.get("Client"), By.xpath("//div[@id='s2id_client']//span[contains(@id,'select2-chosen')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Failed to select the Client Name : '"+createdGroupMap.get("Client")+"']\n";
				return false;
			}
			TimeUnit.SECONDS.sleep(2);

			//Verifying the Fund Families available in Drop down
			List<String> fundFamilyValues=Arrays.asList(createdGroupMap.get("Fund Family").split(":"));
			int fundFamilysCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='fundFamilyId']/option"));			
			if (fundFamilysCount-1!=fundFamilyValues.size()) {
				Messages.errorMsg = "Actual no of Fund Family are not equal to Expected Fund Family";
				return false ;
			}

			for (int iCount = 2; iCount <= fundFamilysCount; iCount++) {
				String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='fundFamilyId']/option["+iCount+"]"));
				if(!fundFamilyValues.contains(entityNames)){
					Messages.errorMsg = "Fund Family "+entityNames+" is Not matching with the Expected";
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

	private static boolean verifyTradeSearchAssignedRoles(String tradeSearchActivities, Map<String, String> createdGroupMap) {
		try {

			bStatus = NewUICommonFunctions.selectMenu("TRADES", "Trade Search");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Trade Search Screen", "Failed to Navigate to Trade Search Screen :"+Messages.errorMsg);
				return false;
			}

			bStatus = verifyLegalEntitiesInTradeSearch(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));

			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
			if(!bStatus){
				Messages.errorMsg = "[ Class Add Button  Verification Failed.ERROR: "+Messages.errorMsg+" ]";
				return false;
			}

			if(tradeSearchActivities.contains("View") || tradeSearchActivities.contains("Edit") ){
				tradeSearchActivities = tradeSearchActivities+"Trade";
				bStatus = NewUICommonFunctions.verifyActionsButton(tradeSearchActivities ,"ForAllTrades");
				if(!bStatus){
					Messages.errorMsg = "[ Trade Search "+tradeSearchActivities+" Buttons Verifications failed ]"+Messages.errorMsg+" \n";
					return false;
				}
			}
			
			if(tradeSearchActivities.contains("Delete") || tradeSearchActivities.contains("Cancel")){
				bStatus = verifyDeleteAndCancelOperationButtons(tradeSearchActivities);
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyDeleteAndCancelOperationButtons(String tradeSearchActivities) {
		try {
			
			String tableViewLocator = "//div[@id='contenttablejqxgridSearchForAllTrades']//div//div[contains(normalize-space(),'Checker Done')]//following-sibling::div//a[contains(@href,'viewTrade')]";
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(tableViewLocator), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "No Records are available with the Status Contains Checker Done";
				return false;
			}
			if(tradeSearchActivities.contains("Delete")){
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'deleteTrade')]"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Delete Button is not available in View Screen of Trade";
					return false;
				}
			}
			if(tradeSearchActivities.contains("Cancel")){
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'cancelTrade')]"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Cancel Button is not available in View Screen of Trade";
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public static boolean verifyLegalEntitiesInTradeSearch(String sClientName, String sFundFamilyName, String sLegalEntities)
	{
		try {

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", sClientName);
			if(!bStatus){
				Messages.errorMsg = "No Client Name is Available to Select";
				return false;
			}

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", sFundFamilyName );
			if(!bStatus){
				Messages.errorMsg = "No Fund Family Name is Available to Select";
				return false;
			}

			//bstatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", "");
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space(text())='Legal Entity Name']/following-sibling::div//span[contains(@id,'select2-chosen')]"));
			if (!bStatus) {
				Messages.errorMsg = "Unable to Click Legal Entity Dropdonw";
				return false;
			}

			int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='cmbFundName']/option"));

			String[] arrlegalEntity=sLegalEntities.split(",");
			if (legalEntitiesCount-1!=arrlegalEntity.length) {
				Messages.errorMsg = "Actual no of Legal Entities are not equal to Expected Legal Entities";
				return false ;
			}

			for (int iCount = 2; iCount <= legalEntitiesCount; iCount++) {
				String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='cmbFundName']/option["+iCount+"]"));

				if(!sLegalEntities.contains(entityNames)){
					Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected";
					return false;
				}

			}

			NewUICommonFunctions.refreshThePage();

			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}


	}

	private static boolean verifySwitchActivitiesTradeItems(String approveRoleContainedMasters, String switchActivities,Map<String, String> createdGroupMap) {
		try {

			if(switchActivities.contains("Add")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES","Switch");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Switch Trade", "Menu cannot be selected Error: "+Messages.errorMsg);
					return false;
				}

				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Request Details are Not visible";
					return false;
				}

				bStatus = verifyLegalEntitiesInSwitch(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					return false;
				}
			}


			if(switchActivities.contains("Approve")){
				bStatus = navigateToDashBoard();
				if(!bStatus){
					return false;
				}

				bStatus = selectFilterValueForMasterTypeForTransactions("Transaction_SWI",approveRoleContainedMasters);
				if(!bStatus){
					return false;
				}

				bStatus = verifyInDashBoardForTradeType("Switch", "Transaction_SWI", approveRoleContainedMasters,createdGroupMap);
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

	private static boolean verifyLegalEntitiesInSwitch(String clientName,String fundFamilyName, String legalEntityNames) {

		try {

			String testCaseName = getTheTestCaseNameToFillTheInvestorData(legalEntityNames, Global.sSwitchTestData,"SwitchTestData", "TestCaseName", "From Legal Entity Name");
			if(testCaseName != null && !testCaseName.equalsIgnoreCase(""))
			{
				List<String> aLegalEntities = Arrays.asList(testCaseName.split(","));
				for(int l=0;l<aLegalEntities.size() ; l++)
				{
					Map<String, String> mapRedemptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sSwitchTestData,"SwitchTestData",aLegalEntities.get(l));

					if(mapRedemptionDetails!=null && !mapRedemptionDetails.isEmpty())
					{
						bStatus = TradeTypeSwitchAppFunctions.doMakerFillSwitchFromInvestorDetails(mapRedemptionDetails);
						if(!bStatus){
							return false;
						}

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
						if(!bStatus){
							return false;
						}
						if(clientName != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(clientName, By.xpath("//div[@id='s2id_fromClient']//span[contains(@id,'select2-chosen')]"));				
							if(!bStatus){
								Messages.errorMsg = "Client Name is not Selected in Switch";
								return false;
							}
						}


						if(fundFamilyName != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(fundFamilyName, By.xpath("//div[@id='s2id_fromFundFamily']//span[contains(@id,'select2-chosen')]"));
							if(!bStatus){
								Messages.errorMsg = "Fund Family Name is not Selected in Switch";
								return false;
							}
						}

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='s2id_fromLegalEntity']//span[contains(@id,'select2-chosen')]"));
						if(!bStatus){
							Messages.errorMsg = "Legal Entity Dropdown is not clickable in Switch";
							return false;
						}

						int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='fromLegalEntity']/option"));

						for (int iCount = 2; iCount <= legalEntitiesCount; iCount++) {
							String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='fromLegalEntity']/option["+iCount+"]"));

							if(!legalEntityNames.contains(entityNames)){
								Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected in Switch";
								return false;
							}

						}
					}	
					NewUICommonFunctions.refreshThePage();
				}
				
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	private static boolean verifyFormulaActivitiesActivitiesItems(String parentValuesStringList, String formulaActivities, Map<String, String> createdGroupMap) {
		try {			


			if(formulaActivities.contains("View") || formulaActivities.contains("Edit") || formulaActivities.contains("Activate") || formulaActivities.contains("Deactivate")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Formula");
				if(!bStatus){
					Messages.errorMsg = " [Error: Formula menu Cannot be selected : "+Messages.errorMsg+" ]";
					return false;
				}

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				bStatus = NewUICommonFunctions.verifyActionsButton(formulaActivities ,"Rule");
				if(!bStatus){
					Messages.errorMsg = "[ Formula "+formulaActivities+" Buttons Verifications failed ]. "+Messages.errorMsg+" \n";
					return false;
				}
			}
			if(formulaActivities.contains("Add")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Formula");
				if(!bStatus){
					Messages.errorMsg = " [Error: Formula menu Cannot be selected : "+Messages.errorMsg+" ]";
					return false;
				}

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					Messages.errorMsg = "[ Formula Add Button  Verification Failed.ERROR: "+Messages.errorMsg+" ]";
					return false;
				}

			}
			if(formulaActivities.contains("Approve")){

				bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
				if(!bStatus){
					Messages.errorMsg = "[Error: DashBoard menu Cannot be selected: "+Messages.errorMsg+" ]";
					return false;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='contentjqxgridTransactionToOperate']"),Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[Error: DashBoard Table is not visible]";
					return false;
				}
				bStatus = verifyInDashBoardForMasters("Formula","Formula",parentValuesStringList);
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

	private static boolean verifyInDashBoardForMasters(String labelName,String MainMaster, String approveRoleContainedMasters) {
		try {

			bStatus = NewUICommonFunctions.selectdashBoardMasterOrTradeSubMenu(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW);
			if(!bStatus){
				return false;
			}

			String MasterType ="//div[contains(@id,'contenttable') and contains(@id,'MastersToOperate')]/div";

			//Xpath count of Master Types
			int MasterTypeCount = Elements.getXpathCount(Global.driver, By.xpath(MasterType+"/div[5]/div"));
			for(int j=1;j<=MasterTypeCount;j++){
				String mastervalue = Elements.getText(Global.driver, By.xpath(MasterType+"["+j+"]/div[5]/div")).trim();
				if(!approveRoleContainedMasters.contains(mastervalue))
				{
					Messages.errorMsg = "'Approve' Role is Not assigned for the Master "+labelName+" for this User But it is showing in Dashboard";
					return false;
				}
			}
			for(int i=1;i<=MasterTypeCount;i++)
			{
				String masterTypevalue = Elements.getText(Global.driver, By.xpath(MasterType+"["+i+"]/div[5]/div")).trim();
				if(masterTypevalue.equalsIgnoreCase(MainMaster))
				{
					String statusvalue = Elements.getText(Global.driver, By.xpath(MasterType+"["+i+"]/div[5]//following-sibling::div/div")).trim();
					if(statusvalue.equalsIgnoreCase("Maker Done") || statusvalue.equalsIgnoreCase("Maker Reviewed"))
					{			
						bStatus = Elements.click(Global.driver, By.xpath(MasterType+"["+i+"]/div[1]//a"));
						if(!bStatus){
							Messages.errorMsg = "Transaction ID is Not clickable";
							return false;
						}
						if(statusvalue.equalsIgnoreCase("Maker Done")){
							String locator = "//button[contains(normalize-space(), 'Approve')]";
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(locator), Constants.lTimeOut);
							if(!bStatus){
								Messages.errorMsg = "Approve button is not visible for the checker operations";
								return false;
							}
						}						
						break;
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

	private static boolean verifyExchangeActivitiesTradeItems(String parentValuesStringList,String exchangeActivities, Map<String, String> createdGroupMap) {
		try {

			if(exchangeActivities.contains("Add")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES","Exchange");
				if(!bStatus){
					Messages.errorMsg = Messages.errorMsg+" [Error: Exchange menu is not visible] \n";
					return false;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Request Received Date Field is Not Visible in Transfer screen";
					return false;
				}

				bStatus = verifyLegalEntitiesInExchange(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					return false;
				}
			}


			if(exchangeActivities.contains("Approve")){
				bStatus = navigateToDashBoard();
				if(!bStatus){
					return false;
				}

				bStatus = selectFilterValueForMasterTypeForTransactions("Transaction_EXC",parentValuesStringList);
				if(!bStatus){
					return false;
				}

				bStatus = verifyInDashBoardForTradeType("Exchange", "Transaction_EXC", parentValuesStringList,createdGroupMap);
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

	private static boolean verifyLegalEntitiesInExchange(String clientName,String fundFamilyName, String legalEntityNames) {

		try {
			String testCaseName = getTheTestCaseNameToFillTheInvestorData(legalEntityNames, Global.sExchangeTestData,"ExchangeTestData", "TestCaseName", "From Legal Entity Name");
			if(testCaseName != null && !testCaseName.equalsIgnoreCase(""))
			{
				List<String> aLegalEntities = Arrays.asList(testCaseName.split(","));
				for(int l=0;l<aLegalEntities.size() ; l++)
				{
					Map<String, String> mapRedemptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sExchangeTestData,"ExchangeTestData",aLegalEntities.get(l));

					if(mapRedemptionDetails!=null && !mapRedemptionDetails.isEmpty())
					{
						bStatus = TradeTypeExchangeAppFunction.doFillInvestorDetails(mapRedemptionDetails);
						if(!bStatus){
							return false;
						}

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Exchange')]"));
						if(!bStatus){
							return false;
						}
						if(clientName != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(clientName, By.xpath("//div[@id='s2id_fromClient']//span[contains(@id,'select2-chosen')]"));				
							if(!bStatus){
								Messages.errorMsg = "Client Name is not Selected";
								return false;
							}
						}


						if(fundFamilyName != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(fundFamilyName, By.xpath("//div[@id='s2id_fromFundFamily']//span[contains(@id,'select2-chosen')]"));
							if(!bStatus){
								Messages.errorMsg = "Fund Family Name is not Selected";
								return false;
							}
						}

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='s2id_fromLegalEntity']//span[contains(@id,'select2-chosen')]"));
						if(!bStatus){
							Messages.errorMsg = "Legal Entity Dropdown is not clickable in Subscription";
							return false;
						}

						int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='fromLegalEntity']/option"));

						for (int iCount = 2; iCount <= legalEntitiesCount; iCount++) {
							String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='fromLegalEntity']/option["+iCount+"]"));

							if(!legalEntityNames.contains(entityNames)){
								Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected";
								return false;
							}

						}
						NewUICommonFunctions.refreshThePage();
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

	private static boolean verifyInDashBoardForTradeType(String labelName,String MainMaster,String approveRoleContainedMasters,Map<String,String>createdGroupMap) {
		try {

			bStatus = Elements.click(Global.driver, By.xpath("//h4/p"));
			String MasterType ="//div[contains(@id,'contenttable') and contains(@id,'TransactionToOperate')]/div";

			//Xpathcount of Master Types
			int MasterTypeCount = Elements.getXpathCount(Global.driver, By.xpath(MasterType+"/div[6]/div"));
			/*for(int j=1;j<=MasterTypeCount;j++){
				String mastervalue = Elements.getText(Global.driver, By.xpath(MasterType+"["+j+"]/div[5]/div")).trim();
				if(!approveRoleContainedMasters.contains(mastervalue))
				{
					Messages.errorMsg = "'Approve' Role is Not assigned for the Trade Type "+labelName+" for this User But it is showing in Dashboard";
					return false;
				}
			}*/
			String expectedfund = createdGroupMap.get("Legal Entity");
			for(int i=1;i<=MasterTypeCount;i++)
			{

				String fundName = Elements.getText(Global.driver, By.xpath(MasterType+"["+i+"]/div[3]/div")).trim();
				if(!expectedfund.contains(fundName)){
					Messages.errorMsg = fundName+" Fund is Not matching with the expected Funds :"+expectedfund+" which is assigned to the Group";
					return false;
				}
			}
			for(int i=1;i<=MasterTypeCount;i++)
			{				
				//Getting the Master Values
				String masterTypevalue = Elements.getText(Global.driver, By.xpath(MasterType+"["+i+"]/div[6]/div")).trim();
				if(masterTypevalue.equalsIgnoreCase(MainMaster))
				{
					String statusvalue = Elements.getText(Global.driver, By.xpath(MasterType+"["+i+"]/div[6]//following-sibling::div/div")).trim();
					if(statusvalue.equalsIgnoreCase("Maker Done") || statusvalue.equalsIgnoreCase("Maker Reviewed"))
					{

						bStatus = Elements.click(Global.driver, By.xpath(MasterType+"["+i+"]/div[1]//a"));
						if(!bStatus){
							Messages.errorMsg = "Transaction ID is Not clickable";
							return false;
						}
						if(statusvalue.equalsIgnoreCase("Maker Done")){
							String locator = "//button[contains(@onclick, 'Done')]";
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(locator), Constants.lTimeOut);
							if(!bStatus){
								Messages.errorMsg = "Approve button is not visible for the checker operations";
								return false;
							}
						}
						if(statusvalue.equalsIgnoreCase("Maker Reviewed")){
							String locator = "//button[contains(@onclick, 'CRReviewed')]";
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(locator), Constants.lTimeOut);
							if(!bStatus){
								Messages.errorMsg = "Checker Review button is not visible for the checker operations";
								return false;
							}
						}

						break;
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

	private static boolean verifyTransferActivitiesTradeItems(String parentValuesStringList,String transferActivities, Map<String, String> createdGroupMap) {
		try {

			if(transferActivities.contains("Add")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES","Transfer");
				if(!bStatus){
					return false;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Request Received Date Field is Not Visible in Transfer screen";
					return false;
				}

				bStatus = verifyLegalEntitiesInTransfer(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					return false;
				}
			}

			if(transferActivities.contains("Approve")){
				bStatus = navigateToDashBoard();
				if(!bStatus){
					return false;
				}

				bStatus = selectFilterValueForMasterTypeForTransactions("Transaction_TRA",parentValuesStringList);
				if(!bStatus){
					return false;
				}

				bStatus = verifyInDashBoardForTradeType("Transfer", "Transaction_TRA", parentValuesStringList,createdGroupMap);
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

	private static boolean verifyLegalEntitiesInTransfer(String clientName,String fundFamilyName, String legalEntityNames) {

		try {
			
			String testCaseName = getTheTestCaseNameToFillTheInvestorData(legalEntityNames, Global.sTransferTestData,"TransferTestData", "TestCaseName", "Legal Entity Name");
			if(testCaseName != null && !testCaseName.equalsIgnoreCase(""))
			{
				List<String> aLegalEntities = Arrays.asList(testCaseName.split(","));

				for(int l=0;l<aLegalEntities.size() ; l++)
				{
					Map<String, String> mapRedemptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sTransferTestData,"TransferTestData",aLegalEntities.get(l));

					if(mapRedemptionDetails!=null && !mapRedemptionDetails.isEmpty())
					{
						bStatus = TradeTypeTransferAppFunctions.doFillFromInvestorDetails(mapRedemptionDetails);
						if(!bStatus){
							return false;
						}

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Transfer')]"));
						if(!bStatus){
							return false;
						}
						if(clientName != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(clientName, By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"));				
							if(!bStatus){
								Messages.errorMsg = "Client Name is not Selected";
								return false;
							}
						}


						if(fundFamilyName != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(fundFamilyName, By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"));
							if(!bStatus){
								Messages.errorMsg = "Fund Family Name is not Selected";
								return false;
							}
						}

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"));
						if(!bStatus){
							Messages.errorMsg = "Legal Entity Dropdown is not clickable in Subscription";
							return false;
						}

						int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='cmbFundName']/option"));

						for (int iCount = 2; iCount <= legalEntitiesCount; iCount++) {
							String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='cmbFundName']/option["+iCount+"]"));

							if(!legalEntityNames.contains(entityNames)){
								Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected";
								return false;
							}

						}
						NewUICommonFunctions.refreshThePage();
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

	private static boolean verifyFeederRedemptionActivitiesItems(String feederRedemptionActivities,	Map<String, String> createdGroupMap,String parentValuesStringList) {
		try {
			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			if(feederRedemptionActivities.contains("Add")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Feeder Redemption");
				if(!bStatus){
					Messages.errorMsg = Messages.errorMsg+" [Error: Feeder Redemption menu is not visible] \n";
					return false;
				}

				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//h4/p[contains(text(),'Feeder Redemption')]"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Feeder Subscription Label is Not visible";
					return false;
				}

				bStatus = verifyLegalEntitiesInFeederRedemption(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					return false;
				}
			}

			/*if(feederRedemptionActivities.contains("View") || feederRedemptionActivities.contains("Edit") || feederRedemptionActivities.contains("Activate") || feederRedemptionActivities.contains("Deactivate")){

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				bStatus = NewUICommonFunctions.verifyActionsButton(feederRedemptionActivities ,"FeederRedMaster");
				if(!bStatus){
					sAppendErrMsg = "[ Feeder Redemption "+feederRedemptionActivities+" Buttons Verifications failed ]"+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}*/
			if(feederRedemptionActivities.contains("Approve")){
				bStatus = navigateToDashBoard();
				if(!bStatus){
					return false;
				}

				bStatus = selectFilterValueForMasterTypeForTransactions("Feeder RED",parentValuesStringList);
				if(!bStatus){
					return false;
				}

				bStatus = verifyInDashBoardForTradeType("Feeder Redemption", "Feeder RED", parentValuesStringList,createdGroupMap);
				if(!bStatus){
					return false;
				}
			}




			Messages.errorMsg =sAppendErrMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}



	private static boolean verifyLegalEntitiesInFeederRedemption(String sClientName, String sFundFamilyName, String sLegalEntities) {

		try {

			int clientCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='cmbClientName']/option"));
			for (int iCount = 2; iCount <= clientCount; iCount++) {
				String clientName = Elements.getText(Global.driver,By.xpath("//select[@id='cmbClientName']/option["+iCount+"]"));

				if(sClientName.equalsIgnoreCase(clientName)){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", sClientName);
					if(!bStatus){
						Messages.errorMsg = "No Client Name is Available to Select";
						return false;
					}
					Thread.sleep(3000);
					int fundfamilyCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='cmbFundFamilyName']/option"));
					for (int ffCount = 2; ffCount <= fundfamilyCount; ffCount++) {
						String ffName = Elements.getText(Global.driver,By.xpath("//select[@id='cmbFundFamilyName']/option["+ffCount+"]"));

						if(sFundFamilyName.equalsIgnoreCase(ffName)){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", sFundFamilyName );
							if(!bStatus){
								Messages.errorMsg = "No Fund Family Name is Available to Select";
								return false;
							}
							//bstatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", "");
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space(text())='From Fund']/following-sibling::div//span[contains(@id,'select2-chosen')]"));
							if (!bStatus) {
								Messages.errorMsg = "Unable to Click Legal Entity Dropdonw";
								return false;
							}

							int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='cmbFundName']/option"));

							for (int leCount = 2; leCount <= legalEntitiesCount; leCount++) {
								String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='cmbFundName']/option["+leCount+"]"));

								if(!sLegalEntities.contains(entityNames)){
									Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected";
									return false;
								}

							}

							NewUICommonFunctions.refreshThePage();
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


	private static boolean verifyFeeSubscriptionActivitiesItems(String feeSubscriptionActivities,Map<String, String> createdGroupMap,String parentValuesStringList) {
		try {
			if(feeSubscriptionActivities.contains("Add")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Feeder Subscription");
				if(!bStatus){
					Messages.errorMsg = Messages.errorMsg+" [Error: Feeder Subscription menu is not visible] \n";
					return false;
				}

				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//h4/p[contains(text(),'Feeder Subscription')]"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Feeder Subscription Label is Not visible";
					return false;
				}

				bStatus = verifyLegalEntitiesInFeederSubscription(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					return false;
				}

			}

			if(feeSubscriptionActivities.contains("Approve")){
				bStatus = navigateToDashBoard();
				if(!bStatus){
					return false;
				}

				bStatus = selectFilterValueForMasterTypeForTransactions("Feeder SUB",parentValuesStringList);
				if(!bStatus){
					return false;
				}

				bStatus = verifyInDashBoardForTradeType("Feeder Subscription", "Feeder SUB", parentValuesStringList,createdGroupMap);
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

	private static boolean verifyLegalEntitiesInFeederSubscription(String sClientName, String sFundFamilyName, String sLegalEntities) {

		try {

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", sClientName);
			if(!bStatus){
				Messages.errorMsg = "No Client Name is Available to Select";
				return false;
			}

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", sFundFamilyName );
			if(!bStatus){
				Messages.errorMsg = "No Fund Family Name is Available to Select";
				return false;
			}

			//bstatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", "");
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space(text())='From Fund']/following-sibling::div//span[contains(@id,'select2-chosen')]"));
			if (!bStatus) {
				Messages.errorMsg = "Unable to Click Legal Entity Dropdonw";
				return false;
			}

			int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='cmbFundName']/option"));

			for (int iCount = 2; iCount <= legalEntitiesCount; iCount++) {
				String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='cmbFundName']/option["+iCount+"]"));

				if(!sLegalEntities.contains(entityNames)){
					Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected";
					return false;
				}

			}

			NewUICommonFunctions.refreshThePage();

			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	private static boolean verifyFeeDistributionActivitiesItems(String feeDistributionActivities,Map<String, String> createdGroupMap) {
		try {

			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Fee Distribution");
			if(!bStatus){
				Messages.errorMsg = Messages.errorMsg+" [Error: Fee Distribution menu is not visible] \n";
				return false;
			}

			if(feeDistributionActivities.contains("View") || feeDistributionActivities.contains("Edit") || feeDistributionActivities.contains("Activate") || feeDistributionActivities.contains("Deactivate")){

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				bStatus = NewUICommonFunctions.verifyActionsButton(feeDistributionActivities ,"FeeDist");
				if(!bStatus){
					sAppendErrMsg = "[ Fee Distribution "+feeDistributionActivities+" Buttons Verifications failed ]. "+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}

			if(feeDistributionActivities.contains("Add")){
				bStatus = verifyLegalEntitiesInClass(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					sAppendErrMsg = "[ Selected Group Legal Entities  Verification failed in Fee Distribution Master "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					sAppendErrMsg = "[ Fee Distribution Add Button  Verification Failed.ERROR: "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

			}
			Messages.errorMsg = sAppendErrMsg;
			return bValidateStatus;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyRedemptionTradeItems(String parentValuesStringList,String redemptionActivities, Map<String, String> createdGroupMap) {
		try {

			if(redemptionActivities.contains("Add")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES","Redemption");
				if(!bStatus){
					return false;
				}

				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[normalize-space()='Request Details']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Request Details are Not visible";
					return false;
				}

				bStatus = verifyLegalEntitiesInRedemption(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					return false;
				}
			}

			if(redemptionActivities.contains("Approve")){
				bStatus = navigateToDashBoard();
				if(!bStatus){
					return false;
				}
				bStatus = selectFilterValueForMasterTypeForTransactions("Transaction_RED",parentValuesStringList);
				if(!bStatus){
					return false;
				}

				bStatus = verifyInDashBoardForTradeType("Redemption", "Transaction_RED", parentValuesStringList,createdGroupMap);
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

	private static boolean verifyLegalEntitiesInRedemption(String clientName,String fundFamilyName, String legalEntityNames) {

		try {
			
			//If the Fund Family Name and Legal Entity name is Same than it will pick up the Fund Family Test data So To get the Exact test data below function is used
			String testCaseName = getTheTestCaseNameToFillTheInvestorData(legalEntityNames, Global.sRedemptionTestData,"RedemptionTestData", "TestCaseName", "Legal Entity Name");
			if(testCaseName != null && !testCaseName.equalsIgnoreCase("")){
				List<String> aLegalEntities = Arrays.asList(testCaseName.split(","));
				for(int l=0;l<aLegalEntities.size() ; l++)
				{
					Map<String, String> mapRedemptionDetails = ExcelUtils.readDataABasedOnCellName(Global.sRedemptionTestData,"RedemptionTestData",aLegalEntities.get(l));

					if(mapRedemptionDetails!=null && !mapRedemptionDetails.isEmpty())
					{
						bStatus = TradeTypeRedemptionAppFunctions.doFillInvestorDetails(mapRedemptionDetails);
						if(!bStatus){
							return false;
						}

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
						if(!bStatus){
							return false;
						}
						if(clientName != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(clientName, By.xpath("//label[normalize-space()='Client Name']/following::div[@id='s2id_cmbClientName']"));				
							if(!bStatus){
								Messages.errorMsg = "Client Name is not Selected";
								return false;
							}
						}


						if(fundFamilyName != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(fundFamilyName, By.xpath("//label[normalize-space()='Fund Family Name']/following::div[@id='s2id_cmbFundFamilyName']"));
							if(!bStatus){
								Messages.errorMsg = "Fund Family Name is not Selected";
								return false;
							}
						}

						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()='Legal Entity Name']/following::div[@id='s2id_cmbFundName']"));
						if(!bStatus){
							Messages.errorMsg = "Legal Entity Dropdown is not clickable in Subscription";
							return false;
						}

						int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='cmbFundName']/option"));

						for (int iCount = 2; iCount <= legalEntitiesCount; iCount++) {
							String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='cmbFundName']/option["+iCount+"]"));

							if(!legalEntityNames.contains(entityNames)){
								Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected";
								return false;
							}

						}
						NewUICommonFunctions.refreshThePage();
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

	private static String getTheTestCaseNameToFillTheInvestorData(String legalEntityNames,String filePath,String shhetName,String testCaseNameKey,String legalEntityKey) {
		try {
			String testCaseName = "";
			
			List<String >alegalEntityNames = Arrays.asList(legalEntityNames.split(","));
			for(int l=0;l<alegalEntityNames.size() ; l++)
			{
				Map<String, Map<String, String>> mapAllRedemptionDetails = Utilities.readMultipleTestData(filePath,shhetName,"Y");

				for(int index = 1;index <= mapAllRedemptionDetails.size();index++){
					Map<String, String> mapRedemptionDetails = mapAllRedemptionDetails.get("Row"+index);
					if(alegalEntityNames.get(l).contains(mapRedemptionDetails.get(legalEntityKey))){
						testCaseName = testCaseName+mapRedemptionDetails.get(testCaseNameKey)+",";
						break;
					}
				}
			}
			testCaseName = RoleAppFunctions.removeUnwantedCommas(testCaseName);
			
			
			
			return testCaseName;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static boolean verifySubscriptionTradeItems(String parentValuesStringList,String subscriptionActivities, Map<String, String> createdGroupMap) {
		try {

			if(subscriptionActivities.contains("Add")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES","Subscription");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Subscription Trade", "Menu cannot be selected Error: "+Messages.errorMsg);
					Messages.errorMsg = Messages.errorMsg+" [Error: Subscription menu is not visible] \n";
					return false;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Request Received Date Field is Not Visible in Subscription screen";
					return false;
				}

				bStatus = verifyLegalEntitiesInSUbscription(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					return false;
				}
			}

			if(subscriptionActivities.contains("Approve")){
				bStatus = navigateToDashBoard();
				if(!bStatus){
					return false;
				}

				bStatus = selectFilterValueForMasterTypeForTransactions("Transaction_SUB",parentValuesStringList);
				if(!bStatus){
					return false;
				}

				bStatus = verifyInDashBoardForTradeType("Subscription", "Transaction_SUB", parentValuesStringList,createdGroupMap);
				if(!bStatus){
					return false;
				}
			}

			/*	//Click the Master Type select filter dropdown 
				bStatus = Elements.click(Global.driver, By.xpath("//div[@id='filterrow.jqxgridTransactionToOperate']/div/div[5]//div[contains(@id,'dropdownlistWrapper')]"));
				if(!bStatus){
					Messages.errorMsg = "Master Type Select filter Dropdown cannot be clicked";
					return false;
				}

				//Verify Select All checkbox and if Checked Un check the
				String selectAllLocator = "//div[contains(@id,'listBox')][4]//span[contains(text(),'Select All')]//preceding-sibling::div//span";
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(selectAllLocator+"[contains(@class,'checked')]"), Constants.lTimeOut);
				if(bStatus){
					bStatus = Elements.click(Global.driver, By.xpath(selectAllLocator));
					if(!bStatus){
						Messages.errorMsg = "Select All Check box not unchecked";
						return false;						
					}
				}else{
					bStatus = Elements.click(Global.driver, By.xpath(selectAllLocator));
					if(!bStatus){
						Messages.errorMsg = "Select All Check box not checked";
						return false;						
					}
					bStatus = Elements.click(Global.driver, By.xpath(selectAllLocator));
					if(!bStatus){
						Messages.errorMsg = "Select All Check box not unchecked";
						return false;						
					}
				}

				//Check the Subscription Checkbox
				bStatus = Elements.click(Global.driver, By.xpath("//div[contains(@id,'listBox')][4]//span[contains(text(),'Transaction_SUB')]//preceding-sibling::div//span"));
				if(!bStatus){
					Messages.errorMsg = "Transaction Sub Check box not checked in Master Type";
					return false;
				}*/

			/*
				//Master Type table locator
				String MasterType ="//div[contains(@id,'contenttable') and contains(@id,'TransactionToOperate')]/div";

				//Xpathcount of Master Types
				int MasterTypeCount = Elements.getXpathCount(Global.driver, By.xpath(MasterType+"/div[5]/div"));
				for(int j=1;j<=MasterTypeCount;j++){

				}
				for(int i=1;i<=MasterTypeCount;i++)
				{
					String masterTypevalue = Elements.getText(Global.driver, By.xpath(MasterType+"["+i+"]/div[5]/div")).trim();
					if(masterTypevalue.equalsIgnoreCase("Transaction_SUB"))
					{
						String statusvalue = Elements.getText(Global.driver, By.xpath(MasterType+"["+i+"]/div[5]//following-sibling::div/div")).trim();
						if(statusvalue.equalsIgnoreCase("Maker Done"))
						{
							bStatus = Elements.click(Global.driver, By.xpath(MasterType+"["+i+"]/div[1]//a"));
							if(!bStatus){
								Messages.errorMsg = "Transaction ID is Not clickable";
								return false;
							}
							String locator = "//button[contains(@onclick, 'javascript:submitCheckerRequestForSub')and contains(@onclick, 'Done')]";
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(locator), Constants.lTimeOut);
							if(!bStatus){
								Messages.errorMsg = "Approve button is not visible for the checker operations";
								return false;
							}
							break;
						}
					}
				}
			 */

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyLegalEntitiesInSUbscription(String clientName,String fundFamilyName, String legalEntityNames) {
		try {

			if(clientName != null){				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(clientName, By.xpath("//div[@id='s2id_clientNameDropdown']//span[contains(@id,'select2-chosen')]"));				
				if(!bStatus){
					Messages.errorMsg = "Client Name"+ Messages.errorMsg;
					return false;
				}
			}


			if(fundFamilyName != null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(fundFamilyName, By.xpath("//div[@id='s2id_fundFamilyNameDropdown']//span[contains(@id,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Fund Family Name"+ Messages.errorMsg;
					return false;
				}
			}

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='s2id_legalEntityNameDropdown']//span[contains(@id,'select2-chosen')]"));
			if(!bStatus){
				Messages.errorMsg = "Legal Entity Dropdown is not clickable in Subscription";
				return false;
			}

			//List<String> aLegalEntities = Arrays.asList(legalEntityNames.split(","));

			int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='legalEntityNameDropdown']/option"));

			String[] arrlegalEntity=legalEntityNames.split(",");
			if (legalEntitiesCount-1!=arrlegalEntity.length) {
				Messages.errorMsg = "Actual no of Legal Entities are not equal to Expected Legal Entities";
				return false ;
			}

			for (int iCount = 2; iCount <= legalEntitiesCount; iCount++) {
				String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='legalEntityNameDropdown']/option["+iCount+"]"));

				if(!legalEntityNames.contains(entityNames)){
					Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected";
					return false;
				}

			}

			NewUICommonFunctions.refreshThePage();


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyDashBoardItems(String activities){
		try{

			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if(!bStatus){
				Messages.errorMsg = "[Error: DashBoard menu is not visible] \n";
				return false;
			}

			bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='contentjqxgridTransactionToOperate']"));
			if(!bStatus){
				Messages.errorMsg = "[Error: DashBoard Table is not visible]";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyClientItems(String activities,Map<String, String> GroupMap){
		try{
			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Client");
			if(!bStatus){
				Messages.errorMsg = " [Error: Client menu Cannot be selected : "+Messages.errorMsg+" ]";
				return false;
			}

			if(activities.contains("Search")){
				bStatus = NewUICommonFunctions.verifyValuesInSearchTableInAllMenus("ClientMaster", "Client Name" ,GroupMap.get("Client"));
				if(!bStatus){
					sAppendErrMsg = "[ Search Verification Failed in Client Master ERROR:" +Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(activities.contains("View") || activities.contains("Edit") || activities.contains("Activate") || activities.contains("Deactivate")){
				bStatus = NewUICommonFunctions.verifyActionsButton(activities ,"ClientMaster");
				if(!bStatus){
					sAppendErrMsg = "[ Client "+activities+" Buttons Verifications failed ]"+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}


			Messages.errorMsg =sAppendErrMsg;
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyFundFamilyItems(String fundFamilyActivites,Map<String, String> createdGroupMap) {
		try {

			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Fund Family");
			if(!bStatus){
				Messages.errorMsg = " [Error: Fund Family menu Cannot be selected : "+Messages.errorMsg+" ]";
				return false;
			}

			if(fundFamilyActivites.contains("Search")){
				bStatus = NewUICommonFunctions.verifyValuesInSearchTableInAllMenus("FundFamily", "Fund Family Name" ,createdGroupMap.get("Fund Family"));
				if(!bStatus){
					sAppendErrMsg = "[ Search Verification Failed in Fund Family Master ERROR:" +Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(fundFamilyActivites.contains("View") || fundFamilyActivites.contains("Edit") || fundFamilyActivites.contains("Activate") || fundFamilyActivites.contains("Deactivate")){
				bStatus = NewUICommonFunctions.verifyActionsButton(fundFamilyActivites ,"FundFamily");
				if(!bStatus){
					sAppendErrMsg = "[ Fund Family "+fundFamilyActivites+" Buttons Verifications failed ]"+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}


			Messages.errorMsg =sAppendErrMsg;
			return bValidateStatus;


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyLegalEntityItems(String legalEntityActivities,Map<String, String> createdGroupMap) {
		try {

			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Legal Entity");
			if(!bStatus){
				Messages.errorMsg = " [Error: Legal Entity menu Cannot be selected : "+Messages.errorMsg+" ]";
				return false;
			}

			if(legalEntityActivities.contains("Search")){
				bStatus = NewUICommonFunctions.verifyValuesInSearchTableInAllMenus("LegalEntity", "Legal Entity Name" ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					sAppendErrMsg = "[ Search Verification Failed in Legal Entity Master ERROR:" +Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(legalEntityActivities.contains("View") || legalEntityActivities.contains("Edit") || legalEntityActivities.contains("Activate") || legalEntityActivities.contains("Deactivate")){
				bStatus = NewUICommonFunctions.verifyActionsButton(legalEntityActivities ,"LegalEntity");
				if(!bStatus){
					sAppendErrMsg = "[ LegalEntity "+legalEntityActivities+" Buttons Verifications failed ]"+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}


			Messages.errorMsg =sAppendErrMsg;
			return bValidateStatus;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyClassMasterItems(String classActivities,Map<String, String> createdGroupMap) {
		try {

			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Class");
			if(!bStatus){
				Messages.errorMsg = " [Error: Class menu Cannot be selected : "+Messages.errorMsg+" ]";
				return false;
			}

			if(classActivities.contains("View") || classActivities.contains("Edit") || classActivities.contains("Activate") || classActivities.contains("Deactivate")){

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				bStatus = NewUICommonFunctions.verifyActionsButton(classActivities ,"Class");
				if(!bStatus){
					sAppendErrMsg = "[ Class "+classActivities+" Buttons Verifications failed ]"+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}


			if(classActivities.contains("Add")){
				bStatus = verifyLegalEntitiesInClass(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					sAppendErrMsg = "[ selected Group Legal Entities  Verification failed in Class Master "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					sAppendErrMsg = "[ Class Add Button  Verification Failed.ERROR: "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

			}

			Messages.errorMsg =sAppendErrMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifySeriesMasterItems(String seriesActivities,Map<String, String> createdGroupMap) {
		try {

			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Series");
			if(!bStatus){
				Messages.errorMsg = " [Error: Series menu Cannot be selected : "+Messages.errorMsg+" ]";
				return false;
			}

			if(seriesActivities.contains("View") || seriesActivities.contains("Edit") || seriesActivities.contains("Activate") || seriesActivities.contains("Deactivate")){

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				bStatus = NewUICommonFunctions.verifyActionsButton(seriesActivities ,"Series");
				if(!bStatus){
					sAppendErrMsg = "[ Series "+seriesActivities+" Buttons Verifications failed ]. "+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}

			if(seriesActivities.contains("Add")){
				bStatus = verifyLegalEntitiesInClass(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					sAppendErrMsg = "[ selected Group Legal Entities  Verification failed in Series Master "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					sAppendErrMsg = "[ Series Add Button  Verification Failed.ERROR: "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

			}

			Messages.errorMsg = sAppendErrMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyParameterItems(String parameterActivities,Map<String, String> createdGroupMap) {
		try {

			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Parameter");
			if(!bStatus){
				Messages.errorMsg = " [Error: Parameter menu Cannot be selected : "+Messages.errorMsg+" ]";
				return false;
			}

			if(parameterActivities.contains("View") || parameterActivities.contains("Edit") || parameterActivities.contains("Activate") || parameterActivities.contains("Deactivate")){

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				bStatus = NewUICommonFunctions.verifyActionsButton(parameterActivities ,"Parameter");
				if(!bStatus){
					sAppendErrMsg = "[ Parameter "+parameterActivities+" Buttons Verifications failed ]. "+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}

			if(parameterActivities.contains("Add")){
				bStatus = verifyLegalEntitiesInClass(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					sAppendErrMsg = "[ selected Group Legal Entities  Verification failed in Parameter Master "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					sAppendErrMsg = "[ Parameter Add Button  Verification Failed.ERROR: "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

			}

			Messages.errorMsg = sAppendErrMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	private static boolean verifyManagementFeeActivitiesItems(String managementFeeActivities, Map<String, String> createdGroupMap) {
		try {

			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Management Fee");
			if(!bStatus){
				Messages.errorMsg = " [Error: Management Fee menu Cannot be selected : "+Messages.errorMsg+" ]";
				return false;
			}

			if(managementFeeActivities.contains("View") || managementFeeActivities.contains("Edit") || managementFeeActivities.contains("Activate") || managementFeeActivities.contains("Deactivate")){

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				bStatus = NewUICommonFunctions.verifyActionsButton(managementFeeActivities ,"ManagementFee");
				if(!bStatus){
					sAppendErrMsg = "[ ManagementFee "+managementFeeActivities+" Buttons Verifications failed ]. "+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}

			if(managementFeeActivities.contains("Add")){
				bStatus = verifyLegalEntitiesInClass(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					sAppendErrMsg = "[ selected Group Legal Entities  Verification failed in Management Fee Master "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					sAppendErrMsg = "[ Management Fee Add Button  Verification Failed.ERROR: "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

			}

			Messages.errorMsg = sAppendErrMsg;
			return bValidateStatus;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyIncentiveFeeActivitiesItems(String incentiveFeeActivities, Map<String, String> createdGroupMap) {
		try {

			boolean bValidateStatus = true;
			String sAppendErrMsg = "";
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Incentive Fee");
			if(!bStatus){
				Messages.errorMsg = " [Error: Incentive Fee menu Cannot be selected : "+Messages.errorMsg+" ]";
				return false;
			}

			if(incentiveFeeActivities.contains("View") || incentiveFeeActivities.contains("Edit") || incentiveFeeActivities.contains("Activate") || incentiveFeeActivities.contains("Deactivate")){

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				bStatus = NewUICommonFunctions.verifyActionsButton(incentiveFeeActivities ,"IncentiveFee");
				if(!bStatus){
					sAppendErrMsg = "[ IncentiveFee "+incentiveFeeActivities+" Buttons Verifications failed ]. "+Messages.errorMsg+" \n";
					bValidateStatus =  false;
				}
			}

			if(incentiveFeeActivities.contains("Add")){
				bStatus = verifyLegalEntitiesInClass(createdGroupMap.get("Client") ,createdGroupMap.get("Fund Family") ,createdGroupMap.get("Legal Entity"));
				if(!bStatus){
					sAppendErrMsg = "[ selected Group Legal Entities  Verification failed in Incentive Fee Master "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					sAppendErrMsg = "[ Incentive Fee Add Button  Verification Failed.ERROR: "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}

			}

			Messages.errorMsg = sAppendErrMsg;
			return bValidateStatus;


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return true;
		}
	}


	public static boolean verifyLegalEntitiesInClass(String sClientName, String sFundFamilyName, String sLegalEntities)
	{
		try {

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", sClientName);
			if(!bStatus){
				Messages.errorMsg = "No Client Name is Available to Select";
				return false;
			}

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", sFundFamilyName );
			if(!bStatus){
				Messages.errorMsg = "No Fund Family Name is Available to Select";
				return false;
			}

			//bstatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", "");
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space(text())='Legal Entity Name']/following-sibling::div//span[contains(@id,'select2-chosen')]"));
			if (!bStatus) {
				Messages.errorMsg = "Unable to Click Legal Entity Dropdonw";
				return false;
			}

			int legalEntitiesCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='cmbFundName']/option"));

			String[] arrlegalEntity=sLegalEntities.split(",");
			if (legalEntitiesCount-1!=arrlegalEntity.length) {
				Messages.errorMsg = "Actual no of Legal Entities are not equal to Expected Legal Entities";
				return false ;
			}

			for (int iCount = 2; iCount <= legalEntitiesCount; iCount++) {
				String entityNames = Elements.getText(Global.driver,By.xpath("//select[@id='cmbFundName']/option["+iCount+"]"));

				if(!sLegalEntities.contains(entityNames)){
					Messages.errorMsg = "Legal Entity "+entityNames+" is Not matching with the Expected";
					return false;
				}

			}

			NewUICommonFunctions.refreshThePage();

			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}


	}


	public static String getParentValuesFromRoles(Map<String ,String> mapUserDetails){
		try {

			String groupName = mapUserDetails.get("Groups");
			String parentValuesStringList = "";
			List<String> aGroupName = Arrays.asList(groupName.split(","));

			for(int i=0;i<aGroupName.size();i++){
				Map<String, String> createdGroupMap = new HashMap<String, String>();
				Map<String, String> createdRoleMap = new HashMap<>();
				Map<String, String> ModifiedRoleMap = new HashMap<>();
				createdGroupMap = ExcelUtils.readDataABasedOnCellName(Global.sGroupTestDataFilePath, "Group", aGroupName.get(i));
				String roleName = createdGroupMap.get("Role");
				createdRoleMap = ExcelUtils.readDataABasedOnCellName(Global.sRoleTestDataFilePath, "Role",roleName);
				ModifiedRoleMap = ExcelUtils.readDataABasedOnCellName(Global.sRoleTestDataFilePath, "ModifyRole",createdRoleMap.get("TestCaseName"));
				if(ModifiedRoleMap.get("Operation")!=null ){
					if( ModifiedRoleMap.get("Operation").equals("Modify")){
						createdRoleMap.putAll(ModifiedRoleMap);
					}
				}
				if(createdRoleMap.containsKey("DashBoard")){
					parentValuesStringList = parentValuesStringList+",DashBoard";
				}
				if(createdRoleMap.containsKey("Client")){
					parentValuesStringList = parentValuesStringList+",Client";
				}

				if(createdRoleMap.containsKey("Fund Family")){
					parentValuesStringList = parentValuesStringList+",Fund Family";
				}

				if(createdRoleMap.containsKey("Legal Entity")){
					parentValuesStringList = parentValuesStringList+",Legal Entity";
				}

				if(createdRoleMap.containsKey("Class")){
					parentValuesStringList = parentValuesStringList+",Class";
				}

				if(createdRoleMap.containsKey("Series")){
					parentValuesStringList = parentValuesStringList+",Series";
				}

				if(createdRoleMap.containsKey("Parameter")){
					parentValuesStringList = parentValuesStringList+",Parameter";
				}

				if(createdRoleMap.containsKey("Formula")){
					parentValuesStringList = parentValuesStringList+",Formula";
				}

				if(createdRoleMap.containsKey("Fee Distribution")){
					parentValuesStringList = parentValuesStringList+",Fee Distribution";
				}

				if(createdRoleMap.containsKey("Management Fee")){
					parentValuesStringList = parentValuesStringList+",Management Fee";
				}

				if(createdRoleMap.containsKey("Incentive Fee")){
					parentValuesStringList = parentValuesStringList+",Incentive Fee";
				}

				String FeeSubscriptionActivities = createdRoleMap.get("Feeder Subscription");
				if(FeeSubscriptionActivities!=null && FeeSubscriptionActivities.contains("Add")){
					parentValuesStringList = parentValuesStringList +  ",Feeder Subscription";
				}
				String FeederRedemptionActivities = createdRoleMap.get("Feeder Redemption");
				if(FeederRedemptionActivities!=null && FeederRedemptionActivities.contains("Add")){
					parentValuesStringList = parentValuesStringList +  ",Feeder Redemption";
				}
				String SubscriptionActivities = createdRoleMap.get("Subscription");
				if(SubscriptionActivities!=null && SubscriptionActivities.contains("Add")){
					parentValuesStringList = parentValuesStringList +  ",Subscription";
				}
				String RedemptionActivities = createdRoleMap.get("Redemption");
				if(RedemptionActivities!=null && RedemptionActivities.contains("Add")){
					parentValuesStringList = parentValuesStringList +  ",Redemption";
				}
				String TransferActivities = createdRoleMap.get("Transfer");
				if(TransferActivities!=null && TransferActivities.contains("Add")){
					parentValuesStringList = parentValuesStringList +  ",Transfer";
				}
				String ExchangeActivities = createdRoleMap.get("Exchange");
				if(ExchangeActivities!=null && ExchangeActivities.contains("Add")){
					parentValuesStringList = parentValuesStringList +  ",Exchange";
				}

				String SwitchActivities = createdRoleMap.get("Switch");
				if(SwitchActivities!=null && SwitchActivities.contains("Add")){
					parentValuesStringList = parentValuesStringList +  "Switch";
				}

			}
			parentValuesStringList = parentValuesStringList.replaceAll(",$", "");
			parentValuesStringList = parentValuesStringList.replaceFirst("^,","");

			return parentValuesStringList;


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static boolean verifyMenus(Map<String ,String> mapUserDetails){
		try {

			String parentList = getParentValuesFromRoles(mapUserDetails);
			if(parentList==null || parentList.equalsIgnoreCase("")){
				Messages.errorMsg = "";
				return false;
			}

			String sAppendErrMsg = "";
			boolean bValidateStatus = true;
			String sMenuLocator = Locators.HomePage.Label.lblMenu;

			if(parentList.contains("DashBoard")){
				bStatus = NewUICommonFunctions.selectMenu("DashBoard", "None");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}	
			}

			if(parentList.contains("Client")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Client");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}


			if(parentList.contains("Fund Family")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Fund Family");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Legal Entity")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Legal Entity");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Class")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Class");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Series")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Series");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Formula")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Formula");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Parameter")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Parameter");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Management Fee")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Management Fee");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Incentive Fee")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Incentive Fee");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Fee Distribution")){
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Fee Distribution");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Feeder Subscription")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Feeder Subscription");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Feeder Redemption")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Feeder Redemption");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Subscription")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Subscription");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Redemption")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Redemption");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Transfer")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Transfer");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Exchange")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Exchange");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			if(parentList.contains("Switch")){
				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Switch");
				if(!bStatus){
					sAppendErrMsg = sAppendErrMsg+"[ "+Messages.errorMsg+" ]";
					bValidateStatus = false;
				}
			}

			Messages.errorMsg = sAppendErrMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean masterTypeFilterValues(String labelName,String ApproveButtonContainedScreens,String TransactionOrMasters){
		try {

			String valuesLocatotor = "//div[contains(@id,'listBox')][4]//div[contains(@id,'innerListBox')]//div[contains(@id,'listitem')]/span";

			//click Master type Select dropdown
			bStatus = Elements.click(Global.driver, By.xpath("//div[@id='filterrow.jqxgrid"+TransactionOrMasters+"ToOperate']/div/div[5]//div[contains(@id,'dropdownlistWrapper')]"));
			if(!bStatus){
				Messages.errorMsg = "Master Type Select filter Dropdown cannot be clicked";
				return false;
			}

			int xpathCount = Elements.getXpathCount(Global.driver, By.xpath(valuesLocatotor));
			if(xpathCount == 1){
				return true;
			}
			for(int i=1;i<=xpathCount;i++){
				String value = Elements.getText(Global.driver, By.xpath("//div[contains(@id,'listBox')][4]//div[contains(@id,'innerListBox')]//div[contains(@id,'listitem"+i+"')]/span"));
				if(!ApproveButtonContainedScreens.contains(value)){
					Messages.errorMsg = "'Approve' Role is Not assigned for the Trade Type "+labelName+" for this User But it is showing in Dashboard";
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


	public static boolean selectFilterValueForMasterTypeForTransactions(String filterValue , String approveRoleContaiedTrades){
		try {

			//Click the Master Type select filter dropdown 
			bStatus = Elements.click(Global.driver, By.xpath("//div[@id='filterrow.jqxgridTransactionToOperate']/div/div[6]//div[contains(@id,'dropdownlistWrapper')]"));
			if(!bStatus){
				Messages.errorMsg = "Master Type Select filter Dropdown cannot be clicked";
				return false;
			}


			//	String value =        "//div[contains(@id,'listBox') and contains(@style,'display: block')]//span[contains(text(),'Select All')]//preceding-sibling::div//span";
			//Verify Select All check box and if Checked Un check the
			String selectAllLocator = "//div[contains(@id,'listBox') and contains(@style,'display: block')]//span[contains(text(),'Select All')]//preceding-sibling::div//span";
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(selectAllLocator+"[contains(@class,'checked')]"), Constants.lTimeOut);
			if(bStatus){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(selectAllLocator));
				if(!bStatus){
					Messages.errorMsg = "Select All Check box not unchecked in Master Type filter";
					return false;						
				}
			}else{
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(selectAllLocator));
				if(!bStatus){
					Messages.errorMsg = "Select All Check box not checked in Master Type filter";
					return false;						
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(selectAllLocator));
				if(!bStatus){
					Messages.errorMsg = "Select All Check box not unchecked in Master Type filter";
					return false;						
				}
			}

			int xpathCountOfMasters = Elements.getXpathCount(Global.driver, By.xpath("//div[contains(@id,'listBox') and contains(@style,'display: block')]//div[contains(@id,'listitem')]/span"));

			for(int i=1;i<xpathCountOfMasters;i++){
				String Mastervalue = Elements.getText(Global.driver, By.xpath("//div[contains(@id,'listBox') and contains(@style,'display: block')]//div[contains(@id,'listitem"+i+"')]/span"));
				if(!approveRoleContaiedTrades.contains(Mastervalue)){
					Messages.errorMsg = "'Approve' Role is Not assigned for the Trade Type "+filterValue+" But it is showing in Dashboard";
					return false;
				}
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'listBox') and contains(@style,'display: block')]//span[contains(text(),'"+filterValue+"')]//preceding-sibling::div//span//parent::div"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = filterValue+" value is not available in Master Type Filter";
				return false;
			}

			//Check the Subscription Checkbox
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'listBox') and contains(@style,'display: block')]//span[contains(text(),'"+filterValue+"')]//preceding-sibling::div//span//parent::div"));
			if(!bStatus){
				Messages.errorMsg = filterValue+" Check box not checked in Master Type";
				return false;
			}



			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean navigateToDashBoard(){
		try {

			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if(!bStatus){
				Messages.errorMsg = "[Error: DashBoard menu is not visible] \n";
				return false;
			}
			bStatus = NewUICommonFunctions.selectdashBoardMasterOrTradeSubMenu(dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW);
			if(!bStatus){
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='contentjqxgridTransactionToOperate']"),Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[Error: DashBoard Table is not visible]";
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






