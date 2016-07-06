package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;

public class SidePocketSubscriptionAppFunctions {

	static boolean bStatus;
	static boolean bValidateStatus;
	public static String isSPAmountOrPercentage = "";
	public static float fTotalAmount = 0;

	/******************** Side Pocket 'SUBSCRIPTION' , MAKER functions *************************/

	//Main Function that integrates all the sub functions of Side pocket Subscription.
	public static boolean doPlaceSidePocketSubscriptionOrder(Map<String,String> mapSidePocketSubscriptionDetails){
		try {
			if (mapSidePocketSubscriptionDetails != null && !mapSidePocketSubscriptionDetails.isEmpty()) {
				bStatus = doFillSidePocketSubscriptionDetails(mapSidePocketSubscriptionDetails);
				if (!bStatus) {
					return false;
				}
				bStatus = doAddAndVerifySidePocketSUBValues(mapSidePocketSubscriptionDetails);
				if (!bStatus) {
					return false;
				}
				if (mapSidePocketSubscriptionDetails.get("OperationType") != null) {
					bStatus = doOperationsOnSidePocketMasters(mapSidePocketSubscriptionDetails.get("OperationType"));
					if (!bStatus) {
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

	//This processes the selection of fund details and request details.
	public static boolean doFillSidePocketSubscriptionDetails(Map<String,String> mapSidePocketSubscriptionDetails){
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Fund Details");
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Expand / Collapse the 'Fund Details' Tab.]\n";
				return false;
			}
			/*bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='effectiveDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Effective Date Field not available Page is Not visible";
				return false;
			}*/

			if(mapSidePocketSubscriptionDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSidePocketSubscriptionDetails.get("Client Name"), By.xpath("//div[@id='s2id_clientId']//span[contains(@class,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Client Name Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
			}

			if(mapSidePocketSubscriptionDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSidePocketSubscriptionDetails.get("Fund Family Name"), By.xpath("//div[@id='s2id_fundFamilyId']//span[contains(@class,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Fund Family Name Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
				TimeUnit.SECONDS.sleep(2);
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			}

			if(mapSidePocketSubscriptionDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSidePocketSubscriptionDetails.get("Legal Entity Name"), By.xpath("//div[@id='s2id_legalEntityId']//span[contains(@class,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Name Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
				TimeUnit.SECONDS.sleep(2);
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			}

			if(mapSidePocketSubscriptionDetails.get("Side Pocket Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSidePocketSubscriptionDetails.get("Side Pocket Name"), By.xpath("//div[@id='s2id_sidePocketClassId']//span[contains(@class,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Side Pocket Name Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
				TimeUnit.SECONDS.sleep(2);
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			}
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Request Details");
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Expand / Collapse the 'Fund Details' Tab.]\n";
				return false;
			}
			if(mapSidePocketSubscriptionDetails.get("Effective Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='effectiveDate']"), mapSidePocketSubscriptionDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "Effective Date Cannot be Entered.";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@class='sub-caption' and contains(normalize-space(),'Request Details')]"));
				TimeUnit.SECONDS.sleep(2);
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			}

			if(mapSidePocketSubscriptionDetails.get("Break Period")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSidePocketSubscriptionDetails.get("Break Period"), By.xpath("//div[@id='s2id_breakPeriod']//span[contains(@class,'select2-chosen')]"));
				if(!bStatus){
					Messages.errorMsg = "Break Period Dropdown Not selected ."+Messages.errorMsg;
					return false;
				}
			}

			if(mapSidePocketSubscriptionDetails.get("Performance Fee On Crystalization") != null){
				if(mapSidePocketSubscriptionDetails.get("Performance Fee On Crystalization").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isPerFeeCrystalization' and @value='1']//parent::span"));
					if(!bStatus){
						Messages.errorMsg = "Performance Fee On Crystalization 'Yes' Radio button cannot be selected";
						return false;
					}
				}

				if(mapSidePocketSubscriptionDetails.get("Performance Fee On Crystalization").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isPerFeeCrystalization' and @value='0']//parent::span"));
					if(!bStatus){
						Messages.errorMsg = "Performance Fee On Crystalization 'No' Radio button cannot be selected";
						return false;
					}
				}
			}

			if(mapSidePocketSubscriptionDetails.get("GAV") != null){
				if(mapSidePocketSubscriptionDetails.get("GAV").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-isGAVYes']//span"));
					if(!bStatus){
						Messages.errorMsg = "GAV 'Yes' Radio button cannot be selected";
						return false;
					}
				}

				if(mapSidePocketSubscriptionDetails.get("GAV").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-isGAVNo']//span"));
					if(!bStatus){
						Messages.errorMsg = "GAV 'No' Radio button cannot be selected";
						return false;
					}
				}
			}

			if(mapSidePocketSubscriptionDetails.get("AmountOrPercentage") != null){
				if(mapSidePocketSubscriptionDetails.get("AmountOrPercentage").equalsIgnoreCase("Amount")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-isAmountYes']//span"));
					if(!bStatus){
						Messages.errorMsg = "Amount Radio button cannot be selected";
						return false;
					}
					if(mapSidePocketSubscriptionDetails.get("AmountValue")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapSidePocketSubscriptionDetails.get("AmountValue"));
						if(!bStatus){
							Messages.errorMsg = "Amount Value cannot be Entered";
							return false;
						}
					}
					isSPAmountOrPercentage = "spAmount";
				}
				if(mapSidePocketSubscriptionDetails.get("AmountOrPercentage").equalsIgnoreCase("Percentage")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-isAmountNo']//span"));
					if(!bStatus){
						Messages.errorMsg = "Percentage Radio button cannot be selected";
						return false;
					}
					if(mapSidePocketSubscriptionDetails.get("PercentageValue")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='percentage']"), mapSidePocketSubscriptionDetails.get("PercentageValue"));
						if(!bStatus){
							Messages.errorMsg = "Percentage Value cannot be Entered";
							return false;
						}
					}
					isSPAmountOrPercentage = "spPercentage";
				}
			}

			if(mapSidePocketSubscriptionDetails.get("AutoOrManual") != null){
				if(mapSidePocketSubscriptionDetails.get("AutoOrManual").equalsIgnoreCase("Auto")){
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isAutoYes' and @value='1']//parent::span"));
					if(!bStatus){
						Messages.errorMsg = "Auto Radio button cannot be selected";
						return false;
					}
				}

				if(mapSidePocketSubscriptionDetails.get("AutoOrManual").equalsIgnoreCase("Manual")){
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isAutoYes' and @value='0']//parent::span"));
					if(!bStatus){
						Messages.errorMsg = "Manual Radio button cannot be selected";
						return false;
					}
				}
			}

			if(mapSidePocketSubscriptionDetails.get("PerformGetInvestorRecordsOperation") != null && mapSidePocketSubscriptionDetails.get("PerformGetInvestorRecordsOperation").equalsIgnoreCase("Yes")){
				bStatus = doOperationsOnSidePocketMasters("Get Investors Records");
				if (!bStatus) {
					return false;
				}
			}
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Request Details");
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Expand / Collpase Request Details Tab.]\n";
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//To Perform Operations realted to Side Pocket Subscription screen (EX : 'Get Investors Records' / 'Clear' / 'Update Allocations' / 'Cancel' / 'Save').
	public static boolean doOperationsOnSidePocketMasters(String sOperationType){
		try {
			if(sOperationType.contains("Get Investors Records")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver,By.xpath("//button[contains(@onclick,'applySubmit') and contains(normalize-space(),'Investor')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Get Investors Records button cannot be Clicked.]\n";
					return false;
				}
			}
			if(sOperationType.contains("Update Allocation")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver,By.xpath("//button[contains(@onclick,'applySubmit') and contains(normalize-space(),'Allocation')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Update Allocation button cannot be Clicked.]\n";
					return false;
				}
			}
			if(sOperationType.contains("Clear")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver,By.xpath("//a[contains(normalize-space(),'Clear')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Clear button cannot be Clicked.]\n";
					return false;
				}
			}
			if (sOperationType.equalsIgnoreCase("Save")) {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'saveSubmit')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click 'Save' button.]\n";
					return false;
				}
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if (!bStatus) {
					return false;
				}
			}			
			if (sOperationType.equalsIgnoreCase("Cancel")) {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(normalize-space(),'Cancel')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click 'Cancel' button.]\n";
					return false;
				}
			}
			if (sOperationType.equalsIgnoreCase("Approve")) {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'Approve') and normalize-space() = 'Approve']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click 'APPROVE' button.]\n";
					return false;
				}
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
				if (!bStatus) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//This function integrates both functions to process the selection of side pockets on different modes (EX : 'Manual' / 'Auto').
	public static boolean doAddAndVerifySidePocketSUBValues(Map<String,String> mapSidePocketSubscriptionDetails){
		try {
			if (mapSidePocketSubscriptionDetails != null) {
				if (mapSidePocketSubscriptionDetails.get("Transaction ID") != null) {
					if (mapSidePocketSubscriptionDetails.get("AutoOrManual") != null) {
						if (mapSidePocketSubscriptionDetails.get("AutoOrManual").equalsIgnoreCase("Auto")) {
							bStatus = doVerifyAutoDistributedSidePocketsValues(mapSidePocketSubscriptionDetails);
							if (!bStatus) {
								return false;
							}
						}
						if (mapSidePocketSubscriptionDetails.get("AutoOrManual").equalsIgnoreCase("Manual")) {
							bStatus = doManuallyDistributeAndVerifySidePocketsValues(mapSidePocketSubscriptionDetails);
							if (!bStatus) {
								return false;
							}
						}
					}					
				}
				if (mapSidePocketSubscriptionDetails.get("PerformUpdateAllocationOperation") != null && mapSidePocketSubscriptionDetails.get("PerformUpdateAllocationOperation").equalsIgnoreCase("Yes")) {
					bStatus = doOperationsOnSidePocketMasters("Update Allocation");
					if (!bStatus) {
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

	//This function will modify / verifies Auto Distributed Side Pocket values for respective tax-lots.
	public static boolean doVerifyAutoDistributedSidePocketsValues(Map<String,String> mapSidePocketSubscriptionDetails){
		try {			
			if (mapSidePocketSubscriptionDetails.get("Transaction ID") != null) {
				List<String> sTxnIDs = Arrays.asList(mapSidePocketSubscriptionDetails.get("Transaction ID").split(","));
				List<String> sSPPercentages = Arrays.asList(mapSidePocketSubscriptionDetails.get("SidePocketPercentages").split(","));
				List<String> sSPAmountsAllocated = Arrays.asList(mapSidePocketSubscriptionDetails.get("SPAmountsAllocated").split(","));
				List<String> sSPSharesAllocated = Arrays.asList(mapSidePocketSubscriptionDetails.get("SPAllocatedShares").split(","));
				List<String> sSPExpectedNoOfDecimalsForAmounts = Arrays.asList(mapSidePocketSubscriptionDetails.get("NoOfDecimalsExpectedForAmounts").split(","));
				List<String> sSPExpectedNoOfDecimalsForShares = Arrays.asList(mapSidePocketSubscriptionDetails.get("NoOfDecimalsExpectedForShares").split(","));

				//Calling this function to Unselect all the tax lots and then select tax-lots with given Txn-Id's. Then Update Allocations.
				bStatus = doUnselectUnwantedTaxlotsExceptGivenTxnIdsAndUpdateAllocations(mapSidePocketSubscriptionDetails);
				if (!bStatus) {
					return false;
				}
				for (int i = 0; i < sTxnIDs.size(); i++) {
					if (mapSidePocketSubscriptionDetails.get("AutoOrManual") != null) {
						if (mapSidePocketSubscriptionDetails.get("AutoOrManual").equalsIgnoreCase("Auto")) {
							bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//preceding-sibling::td//div//span[@class='checked']"), Constants.lTimeOut);
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Auto Distributed Tax-lot wasn't present with Txn ID : '"+sTxnIDs.get(i)+"' .]\n";
								return false;
							}							
							bStatus = NewUICommonFunctions.scrollToView(By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//preceding-sibling::td//div//span"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Wasn't able to scroll on to the view of tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' .]\n";
								return false;
							}
							bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Percentage when selected 'Auto' for tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' ,", By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//div//input[contains(@id,'spPercentage')]"), sSPPercentages.get(i).trim(), "No", true);
							if (!bStatus) {
								return false;
							}
							bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Amount when selected 'Auto' for tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' ,", By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//div//input[contains(@id,'spAmount')]"), sSPAmountsAllocated.get(i).trim(), "No", true, Integer.parseInt(sSPExpectedNoOfDecimalsForAmounts.get(i).trim()));
							if (!bStatus) {
								return false;
							}

							/*fTotalAmount = fTotalAmount + Float.parseFloat(NewUICommonFunctions.jsGetElementValueWithXpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//div//input[contains(@id,'spAmount')]").replaceAll(",", ""));*/

							if (sSPSharesAllocated != null && !sSPSharesAllocated.isEmpty() && sSPSharesAllocated.size() != 0 && !sSPSharesAllocated.get(i).equalsIgnoreCase("None")) {
								bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Shares when selected 'Auto' for tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' ,", By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//input[contains(@id,'spShares')]"), sSPSharesAllocated.get(i).trim(), "No", true, Integer.parseInt(sSPExpectedNoOfDecimalsForShares.get(i).trim()));
								if (!bStatus) {
									return false;
								}
							}
						}
					}
				}
				/*if (mapSidePocketSubscriptionDetails.get("TotalAmount") != null && !mapSidePocketSubscriptionDetails.get("TotalAmount").equalsIgnoreCase("None")) {
					if (fTotalAmount != Float.parseFloat(mapSidePocketSubscriptionDetails.get("TotalAmount"))) {
						Messages.errorMsg = "[ ERROR : The total amount : '"+mapSidePocketSubscriptionDetails.get("TotalAmount")+"' ,is not matching with the summation of picked tax-lotas amounts i.e : '"+fTotalAmount+"' .]\n";
						return false;
					}
				}*/
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//This function will modify / verifies Manually Distributed Side Pocket values for respective tax-lots.
	private static boolean doManuallyDistributeAndVerifySidePocketsValues(Map<String,String> mapSidePocketSubscriptionDetails) {
		try {			
			if (mapSidePocketSubscriptionDetails.get("Transaction ID") != null) {
				List<String> sTxnIDs = Arrays.asList(mapSidePocketSubscriptionDetails.get("Transaction ID").split(","));
				List<String> sSPPercentages = Arrays.asList(mapSidePocketSubscriptionDetails.get("SidePocketPercentages").split(","));
				List<String> sSPAmountsAllocated = Arrays.asList(mapSidePocketSubscriptionDetails.get("SPAmountsAllocated").split(","));
				List<String> sSPSharesAllocated = Arrays.asList(mapSidePocketSubscriptionDetails.get("SPAllocatedShares").split(","));
				List<String> sSPExpectedNoOfDecimalsForAmounts = Arrays.asList(mapSidePocketSubscriptionDetails.get("NoOfDecimalsExpectedForAmounts").split(","));
				List<String> sSPExpectedNoOfDecimalsForShares = Arrays.asList(mapSidePocketSubscriptionDetails.get("NoOfDecimalsExpectedForShares").split(","));

				//Calling this function to Unselect all the tax lots and then select tax-lots with given Txn-Id's. Then Update Allocations.
				bStatus = doUnselectUnwantedTaxlotsExceptGivenTxnIdsAndUpdateAllocations(mapSidePocketSubscriptionDetails);
				if (!bStatus) {
					return false;
				}
				for (int i = 0; i < sTxnIDs.size(); i++) {
					if (mapSidePocketSubscriptionDetails.get("AutoOrManual") != null) {
						if (mapSidePocketSubscriptionDetails.get("AutoOrManual").equalsIgnoreCase("Manual")) {							
							if (!isSPAmountOrPercentage.equalsIgnoreCase("") && isSPAmountOrPercentage.equalsIgnoreCase("spAmount")) {
								bStatus = NewUICommonFunctions.scrollToView(By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//preceding-sibling::td//div//span"));
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Wasn't able to scroll on to the view of tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' .]\n";
									return false;
								}								
								bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//preceding-sibling::td//div//span[@class='checked']"));
								if (!bStatus) {
									bStatus = Elements.click(Global.driver, By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//preceding-sibling::td//div//span"));
									if (!bStatus) {
										Messages.errorMsg = "[ ERROR : Unable to select the checkbox for the tax-lot with Txn ID : '"+sTxnIDs.get(i)+"'.]\n";
										return false;
									}
								}
								//Entering Amount in to the field.
								bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//div//input[contains(@id,'spAmount')]"), sSPAmountsAllocated.get(i).trim());
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Unable to enter Amount : '"+sSPAmountsAllocated.get(i).trim()+"' in to the field for tax lot with Txn ID : '"+sTxnIDs.get(i)+"'.]\n";
									return false;
								}

								NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//input[contains(@id,'spShares')]//parent::td"));

								//Verifying percentage for the given amount of respective Tax-lot.
								bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Percentage when selected 'Manual' and 'Amount' for tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' ,", By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//div//input[contains(@id,'spPercentage')]"), sSPPercentages.get(i).trim(), "No", true);
								if (!bStatus) {
									return false;
								}								
								//Verifying Shares Allocated for the given amount of respective Tax-lot if the fund has Unitized structure.
								if (sSPSharesAllocated != null && !sSPSharesAllocated.isEmpty() && sSPSharesAllocated.size() != 0 && !sSPSharesAllocated.get(i).equalsIgnoreCase("None")) {
									bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Shares when selected 'Manual' and 'Amount' for tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' ,", By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//input[contains(@id,'spShares')]"), sSPSharesAllocated.get(i).trim(), "No", true, Integer.parseInt(sSPExpectedNoOfDecimalsForShares.get(i).trim()));
									if (!bStatus) {
										return false;
									}
								}
							}
							if (!isSPAmountOrPercentage.equalsIgnoreCase("") && isSPAmountOrPercentage.equalsIgnoreCase("spPercentage")) {
								bStatus = NewUICommonFunctions.scrollToView(By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//preceding-sibling::td//div//span"));
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Wasn't able to scroll on to the view of tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' .]\n";
									return false;
								}
								//Entering Percentage in to the field.
								bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//div//input[contains(@id,'spPercentage')]"), sSPPercentages.get(i).trim());
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Unable to enter Percentage : '"+sSPPercentages.get(i).trim()+"' in to the field for tax lot with Txn ID : '"+sTxnIDs.get(i)+"'.]\n";
									return false;
								}

								NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//input[contains(@id,'spShares')]//parent::td"));

								//Verifying Amount for the given Percentage of respective Tax-lot.
								bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Amount when selected 'Manual' and 'Percentage' for tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' ,", By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//div//input[contains(@id,'spAmount')]"), sSPAmountsAllocated.get(i).trim(), "No", true, Integer.parseInt(sSPExpectedNoOfDecimalsForAmounts.get(i).trim()));
								if (!bStatus) {
									return false;
								}								
								//Verifying Shares Allocated for the given amount of respective Tax-lot if the fund has Unitized structure.
								if (sSPSharesAllocated != null && !sSPSharesAllocated.isEmpty() && sSPSharesAllocated.size() != 0 && !sSPSharesAllocated.get(i).equalsIgnoreCase("None")) {
									bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Shares when selected 'Manual' and 'Amount' for tax-lot with Txn ID : '"+sTxnIDs.get(i)+"' ,", By.xpath("//td//input[@value='"+sTxnIDs.get(i)+"']//parent::td//following-sibling::td//input[contains(@id,'spShares')]"), sSPSharesAllocated.get(i).trim(), "No", true, Integer.parseInt(sSPExpectedNoOfDecimalsForShares.get(i).trim()));
									if (!bStatus) {
										return false;
									}
								}
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

	//This function is to Unselect all the unwanted tax-lots records and then select tax-lots with the given Txn-Id's.
	private static boolean doUnselectUnwantedTaxlotsExceptGivenTxnIdsAndUpdateAllocations(Map<String,String> mapSidePocketSubscriptionDetails){
		try {
			int iTotalSelectionCount = 0;
			String sReadTxnID = "";
			if (mapSidePocketSubscriptionDetails != null && !mapSidePocketSubscriptionDetails.isEmpty() && mapSidePocketSubscriptionDetails.get("Transaction ID") != null) {
				List<String> sTxnIDsList = Arrays.asList(mapSidePocketSubscriptionDetails.get("Transaction ID").split(","));
				iTotalSelectionCount = Elements.getXpathCount(Global.driver, By.xpath("//input[contains(@id,'isSelect')]//parent::span[@class='checked']"));
				if (iTotalSelectionCount == sTxnIDsList.size()) {
					return true;
				}
				//Unselect all tax lots when Auto Mode is selected.
				for (int i = 0; i < iTotalSelectionCount; i++) {
					sReadTxnID = Elements.getElementAttribute(Global.driver, By.xpath("//input[contains(@id,'isSelect')]//parent::span[@class='checked']//parent::div//parent::td//following-sibling::td//input[contains(@id,'transactionId')]"), "value").trim();
					if (sReadTxnID == null || sReadTxnID.equalsIgnoreCase("")) {
						Messages.errorMsg = "[ ERROR : Unable to read the transaction ID .]\n";
						return false;
					}
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//td//input[@value='"+sReadTxnID+"']//parent::td//preceding-sibling::td//div//span"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to scroll on to the view of tax-lot with Txn ID : '"+sReadTxnID+"' .]\n";
						return false;
					}					
					bStatus = Elements.click(Global.driver, By.xpath("//td//input[@value='"+sReadTxnID+"']//parent::td//preceding-sibling::td//div//span"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to Uncheck the tax lot with Txn ID : '"+sReadTxnID+"' .]\n";
						return false;
					}					
				}
				//Select Given tax lots with Txn Id's when Auto Mode is selected then update the allocation.
				for (int iSelectTaxLot = 0; iSelectTaxLot < sTxnIDsList.size(); iSelectTaxLot++) {
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//td//input[@value='"+sTxnIDsList.get(iSelectTaxLot)+"']//parent::td//preceding-sibling::td//div//span"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to scroll on to the view of tax-lot with Txn ID : '"+sTxnIDsList.get(iSelectTaxLot)+"' .]\n";
						return false;
					}					
					bStatus = Elements.click(Global.driver, By.xpath("//td//input[@value='"+sTxnIDsList.get(iSelectTaxLot)+"']//parent::td//preceding-sibling::td//div//span"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to check the tax lot with Txn ID : '"+sTxnIDsList.get(iSelectTaxLot)+"' .]\n";
						return false;
					}										
				}
				//Updating allocation for selected tax-lots when Auto mode selected.
				bStatus = doOperationsOnSidePocketMasters("Update Allocation");
				if (!bStatus) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/******************** Side Pocket 'SUBSCRIPTION' , CHECKER functions *************************/

	//Main function to club all CHECKER functions.
	public static boolean doCheckerPerformOperationsOnSidePocketSubscriptionOrder(Map<String,String> mapSidePocketSubscriptionDetails){
		String sAppendErrorMsg = "";
		boolean bValidateStatus = true;
		try {			
			bStatus = doCheckerVerifyFundAndReqDetails(mapSidePocketSubscriptionDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			bStatus = doCheckerVerifyTaxlotsDetails(mapSidePocketSubscriptionDetails);
			if (!bStatus) {
				sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			if (mapSidePocketSubscriptionDetails.get("CheckerOperations") != null) {
				bStatus = SidePocketRedemptionAppFunctions.performCheckerOperationsOnSidePocketTrade(mapSidePocketSubscriptionDetails.get("CheckerOperations"));
				if (!bStatus) {
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

	//Verifying Fund and Request Details.
	public static boolean doCheckerVerifyFundAndReqDetails(Map<String,String> mapSidePocketSubscriptionDetails){
		boolean bValidateStatus = true;
		String sAppendErorMsg = "";
		try {
			bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Fund Details");
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Expand / Collapse the 'Fund Details' Tab.]\n";
				return false;
			}
			if (mapSidePocketSubscriptionDetails != null && !mapSidePocketSubscriptionDetails.isEmpty()) {
				if (mapSidePocketSubscriptionDetails.get("Client Name") != null) {
					bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Client Name' at Checker screen ,", By.xpath("//label[normalize-space()='Client Name']//following-sibling::label//input"), mapSidePocketSubscriptionDetails.get("Client Name"), "No", false);
					if (!bStatus) {
						sAppendErorMsg = Messages.errorMsg + sAppendErorMsg;
						bValidateStatus = false;
					}
				}
				if (mapSidePocketSubscriptionDetails.get("Fund Family Name") != null) {
					bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Fund Family Name' at Checker screen ,", By.xpath("//label[normalize-space()='Fund Family Name']//following-sibling::label//input"), mapSidePocketSubscriptionDetails.get("Fund Family Name"), "No", false);
					if (!bStatus) {
						sAppendErorMsg = Messages.errorMsg + sAppendErorMsg;
						bValidateStatus = false;
					}
				}
				if (mapSidePocketSubscriptionDetails.get("Legal Entity Name") != null) {
					bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Legal Entity Name' at Checker screen ,", By.xpath("//label[normalize-space()='Legal Entity Name']//following-sibling::label//input"), mapSidePocketSubscriptionDetails.get("Legal Entity Name"), "No", false);
					if (!bStatus) {
						sAppendErorMsg = Messages.errorMsg + sAppendErorMsg;
						bValidateStatus = false;
					}
				}
				if (mapSidePocketSubscriptionDetails.get("Side Pocket Name") != null) {
					bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Side Pocket Name' at Checker screen ,", By.xpath("//label[normalize-space()='Side Pocket Name']//following-sibling::label//input"), mapSidePocketSubscriptionDetails.get("Side Pocket Name"), "No", false);
					if (!bStatus) {
						sAppendErorMsg = Messages.errorMsg + sAppendErorMsg;
						bValidateStatus = false;
					}
				}
				bStatus = TradeTypeSwitchAppFunctions.doClickOnExpandOrCollapseButtonsBasedOnTabName("Yes", "Request Details");
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Expand / Collapse the 'Request Details' Tab.]\n";
					return false;
				}
				if (mapSidePocketSubscriptionDetails.get("Effective Date") != null) {
					String sEffectiveDate = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Effective Date']//following-sibling::label//input"), "value");
					sEffectiveDate = TradeTypeSubscriptionAppFunctions.formatDate(sEffectiveDate);
					if (!sEffectiveDate.equalsIgnoreCase(mapSidePocketSubscriptionDetails.get("Effective Date"))) {
						sAppendErorMsg = sAppendErorMsg + "[ ERROR : Actual 'Effective Date' : '"+sEffectiveDate+"' is not matching with expected date : '"+mapSidePocketSubscriptionDetails.get("Effective Date")+"' .]\n";
						bValidateStatus = false;
					}
				}					
				if (mapSidePocketSubscriptionDetails.get("Break Period") != null) {
					String sBreakPeriod = Elements.getElementAttribute(Global.driver, By.xpath("//label[normalize-space()='Break Period']//following-sibling::label//input"), "value");
					sBreakPeriod = TradeTypeSubscriptionAppFunctions.formatDate(sBreakPeriod);
					if (!sBreakPeriod.equalsIgnoreCase(mapSidePocketSubscriptionDetails.get("Break Period"))) {
						sAppendErorMsg = sAppendErorMsg + "[ ERROR : Actual 'Break Period' : '"+sBreakPeriod+"' is not matching with expected date : '"+mapSidePocketSubscriptionDetails.get("Break Period")+"' .]\n";
						bValidateStatus = false;
					}
				}
				if (mapSidePocketSubscriptionDetails.get("Performance Fee On Crystalization") != null){
					if(mapSidePocketSubscriptionDetails.get("Performance Fee On Crystalization").equalsIgnoreCase("Yes")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[normalize-space()='Performance Fee On Crystalization']//following-sibling::label[normalize-space()='Yes']"));
						if (!bStatus) {
							sAppendErorMsg = sAppendErorMsg + "[ ERROR : Actual 'Performance Fee On Crystalization' : 'Yes', is not matching with expected : '"+mapSidePocketSubscriptionDetails.get("Performance Fee On Crystalization")+"' .]\n";
							bValidateStatus = false;
						}
					}
					if(mapSidePocketSubscriptionDetails.get("Performance Fee On Crystalization").equalsIgnoreCase("No")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[normalize-space()='Performance Fee On Crystalization']//following-sibling::label[normalize-space()='No']"));
						if (!bStatus) {
							sAppendErorMsg = sAppendErorMsg + "[ ERROR : Actual 'Performance Fee On Crystalization' : 'No', is not matching with expected : '"+mapSidePocketSubscriptionDetails.get("Performance Fee On Crystalization")+"' .]\n";
							bValidateStatus = false;
						}
					}
				}
				if (mapSidePocketSubscriptionDetails.get("GAV") != null){
					if(mapSidePocketSubscriptionDetails.get("GAV").equalsIgnoreCase("Yes")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[normalize-space()='GAV']//following-sibling::label[normalize-space()='Yes']"));
						if (!bStatus) {
							sAppendErorMsg = sAppendErorMsg + "[ ERROR : Actual 'GAV' : 'Yes', is not matching with expected : '"+mapSidePocketSubscriptionDetails.get("GAV")+"' .]\n";
							bValidateStatus = false;
						}
					}
					if(mapSidePocketSubscriptionDetails.get("GAV").equalsIgnoreCase("No")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[normalize-space()='GAV']//following-sibling::label[normalize-space()='No']"));
						if (!bStatus) {
							sAppendErorMsg = sAppendErorMsg + "[ ERROR : Actual 'GAV' : 'No', is not matching with expected : '"+mapSidePocketSubscriptionDetails.get("GAV")+"' .]\n";
							bValidateStatus = false;
						}
					}
				}
				if (mapSidePocketSubscriptionDetails.get("AmountOrPercentage") != null) {
					if (mapSidePocketSubscriptionDetails.get("AmountOrPercentage").equalsIgnoreCase("Amount")) {
						if (mapSidePocketSubscriptionDetails.get("AmountValue") != null) {
							bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Amount' entered into the field ,", By.xpath("//label[normalize-space()='Amount']//following-sibling::label//input"), mapSidePocketSubscriptionDetails.get("AmountValue"), "No", true);
							if (!bStatus) {
								sAppendErorMsg = sAppendErorMsg + Messages.errorMsg;
								bValidateStatus = false;
							}
						}						
					}
					if (mapSidePocketSubscriptionDetails.get("AmountOrPercentage").equalsIgnoreCase("Percentage")) {
						if (mapSidePocketSubscriptionDetails.get("PercentageValue") != null) {
							bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Percentage' entered into the field ,", By.xpath("//label[normalize-space()='Percentage']//following-sibling::label//input"), mapSidePocketSubscriptionDetails.get("PercentageValue"), "No", true);
							if (!bStatus) {
								sAppendErorMsg = sAppendErorMsg + Messages.errorMsg;
								bValidateStatus = false;
							}
						}						
					}
				}				
			}
			Messages.errorMsg = sAppendErorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Verifying the tax-lots details.
	public static boolean doCheckerVerifyTaxlotsDetails(Map<String,String> mapSidePocketSubscriptionDetails){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (mapSidePocketSubscriptionDetails.get("Transaction ID") != null) {
				List<String> sTxnIDs = Arrays.asList(mapSidePocketSubscriptionDetails.get("Transaction ID").split(","));
				List<String> sSPPercentages = Arrays.asList(mapSidePocketSubscriptionDetails.get("SidePocketPercentages").split(","));
				List<String> sSPAmountsAllocated = Arrays.asList(mapSidePocketSubscriptionDetails.get("SPAmountsAllocated").split(","));
				List<String> sSPSharesAllocated = Arrays.asList(mapSidePocketSubscriptionDetails.get("SPAllocatedShares").split(","));
				List<String> sSPExpectedNoOfDecimalsForAmounts = Arrays.asList(mapSidePocketSubscriptionDetails.get("NoOfDecimalsExpectedForAmounts").split(","));
				List<String> sSPExpectedNoOfDecimalsForShares = Arrays.asList(mapSidePocketSubscriptionDetails.get("NoOfDecimalsExpectedForShares").split(","));

				for (int iTaxLotIndex = 0; iTaxLotIndex < sTxnIDs.size(); iTaxLotIndex++) {
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//td[normalize-space() = '"+sTxnIDs.get(iTaxLotIndex)+"']//preceding-sibling::td//span[@class='checked']"));
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to Scroll on to the tax-lot view located by Txn ID : '"+sTxnIDs.get(iTaxLotIndex)+"', tax-lot may not present with given ID please do cross check.]\n";
						bValidateStatus = false;
						continue;
					}
					//Verifying SP SUB Percentages.
					if (sSPPercentages.get(iTaxLotIndex) != null && !sSPPercentages.get(iTaxLotIndex).equalsIgnoreCase("None")) {
						bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Side Pocket Percentage' for tax-lot with Txn ID : '"+sTxnIDs.get(iTaxLotIndex)+"' ,", By.xpath("//td[normalize-space() = '"+sTxnIDs.get(iTaxLotIndex)+"']//following-sibling::td[6]//input"), sSPPercentages.get(iTaxLotIndex), "No", true);
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
					//Verifying SP SUB Amounts.
					if (sSPAmountsAllocated.get(iTaxLotIndex) != null && !sSPAmountsAllocated.get(iTaxLotIndex).equalsIgnoreCase("None")) {
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Side Pocket Amount Allocated' for tax-lot with Txn ID : '"+sTxnIDs.get(iTaxLotIndex)+"' ,", By.xpath("//td[normalize-space() = '"+sTxnIDs.get(iTaxLotIndex)+"']//following-sibling::td[7]//input"), sSPAmountsAllocated.get(iTaxLotIndex), "No", true, Integer.parseInt(sSPExpectedNoOfDecimalsForAmounts.get(iTaxLotIndex)));
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
					//Verifying SP SUB Shares.
					if (sSPSharesAllocated.get(iTaxLotIndex) != null && !sSPSharesAllocated.get(iTaxLotIndex).equalsIgnoreCase("None")) {
						bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("'Side Pocket Shares Allocated' for tax-lot with Txn ID : '"+sTxnIDs.get(iTaxLotIndex)+"' ,", By.xpath("//td[normalize-space() = '"+sTxnIDs.get(iTaxLotIndex)+"']//following-sibling::td[8]"), sSPSharesAllocated.get(iTaxLotIndex), "Yes", true, Integer.parseInt(sSPExpectedNoOfDecimalsForShares.get(iTaxLotIndex)));
						if (!bStatus) {
							sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
							bValidateStatus = false;
						}
					}
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/*public static boolean doPerformCheckerOperationsOnSP_SUB_Order(Map<String,String> mapSidePocketSubscriptionDetails){
		try {
			if (mapSidePocketSubscriptionDetails != null && !mapSidePocketSubscriptionDetails.isEmpty()) {
				if (mapSidePocketSubscriptionDetails.get("CheckerOperations") != null && !mapSidePocketSubscriptionDetails.get("CheckerOperations").equalsIgnoreCase("None")) {
					String sOperation = mapSidePocketSubscriptionDetails.get("CheckerOperations");
					if (sOperation.equalsIgnoreCase("Approve")) {
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'Approve') and normalize-space()='Approve']"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to Click 'Approve' button on Checker Screen .]\n";
							return false;
						}
					}
					if (sOperation.equalsIgnoreCase("Return")) {
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'Return') and normalize-space()='Return']"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to Click 'Return' button on Checker Screen .]\n";
							return false;
						}
						NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.lTimeOut);
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : When performed Checker 'Return', Popup wasn't displayed for comments.]\n";
							return false;
						}
					}
					if (sOperation.equalsIgnoreCase("Reject")) {
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'Reject') and normalize-space()='Reject']"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to Click 'Reject' button on Checker Screen .]\n";
							return false;
						}
						NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
						bStatus = Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"), "Rejected By QA Automation.");
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to enter text into 'Comments' popup box.]";
							return false;
						}						
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/
}
