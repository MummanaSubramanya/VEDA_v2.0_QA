package com.viteos.veda.master.lib;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;

public class FeederSubscriptionAppFunctions {
	public static boolean bStatus = true;
	public static boolean bvalidateStatus = true;
	public static String sAppendErrorMsg = "";
	public static String sAccountNo = "";

	public static boolean AddFeederSubscriptionDetails(Map<String, String> mapFeederSubscriptionDetails)
	{ 
		boolean bStatus;		 
		try {
			if (mapFeederSubscriptionDetails != null && !mapFeederSubscriptionDetails.isEmpty()) {
				if (mapFeederSubscriptionDetails.get("Client Name") !=null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", mapFeederSubscriptionDetails.get("Client Name"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederSubscriptionDetails.get("Client Name")+ "In Feeder Subscription]";
						return false;
					}
				}

				if (mapFeederSubscriptionDetails.get("Fund Family Name") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", mapFeederSubscriptionDetails.get("Fund Family Name"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederSubscriptionDetails.get("Fund Family Name")+ "In Feeder Subscription]";
						return false;
					}
				}
				if (mapFeederSubscriptionDetails.get("From Fund") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("From Fund", mapFeederSubscriptionDetails.get("From Fund"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederSubscriptionDetails.get("From Fund")+ "In Feeder Subscription]";
						return false;
					}
				}
				if (mapFeederSubscriptionDetails.get("From Class") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("From Class", mapFeederSubscriptionDetails.get("From Class"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederSubscriptionDetails.get("From Class")+ "In Feeder Subscription]";
						return false;
					}
				}
				if (mapFeederSubscriptionDetails.get("To Fund") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("To Fund", mapFeederSubscriptionDetails.get("To Fund"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederSubscriptionDetails.get("To Fund")+ "In Feeder Subscription]";
						return false;
					}					
				}
				if (mapFeederSubscriptionDetails.get("Fund Account") != null) {
					TimeUnit.SECONDS.sleep(2);
					String accountID = FeederRedemptionAppFunctions.getTheFundAccountNumber(mapFeederSubscriptionDetails.get("Fund Account"));
					if(accountID != null){
						mapFeederSubscriptionDetails.put("Fund Account", accountID);
						
					}
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Account", mapFeederSubscriptionDetails.get("Fund Account"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederSubscriptionDetails.get("Fund Account")+ "In Feeder Subscription]";
						return false;
					}
				}

				NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
				
				if (mapFeederSubscriptionDetails.get("Add New Account") != null && mapFeederSubscriptionDetails.get("Add New Account").trim().equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//a[@id='newAccountLink']"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to click on New Account From Add New Feeder Subscription Screen.]";
						return false;
					}
					else {
						String sValue = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_cmbAccountFund']//span[contains(@id,'select2-chosen')]"));
						if (sValue == null || sValue.trim().equalsIgnoreCase("")) {
							Messages.errorMsg = "[ ERROR : New Account wasn't Generated when clicked on 'Add New Account'.]";
							return false;
						}
						else {
							sAccountNo = sValue.trim();
						}
					}
				}
				
				if (mapFeederSubscriptionDetails.get("Strategy") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Strategy", mapFeederSubscriptionDetails.get("Strategy"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select Strategy : " +mapFeederSubscriptionDetails.get("Strategy")+ ", From the field on Feeder Subscription screen.]";
						return false;
					}
				}
				if (mapFeederSubscriptionDetails.get("Effective Date") != null) {
					Global.driver.findElement(By.id("datepicker1")).clear();
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("datepicker1"), mapFeederSubscriptionDetails.get("Effective Date"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to Enter Effective Date : " +mapFeederSubscriptionDetails.get("Effective Date")+ ",Into the field on Feeder Subscription screen.]";
						return false;
					}
				}
				if (mapFeederSubscriptionDetails.get("Cash") != null) {
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapFeederSubscriptionDetails.get("Cash"));
					Elements.click(Global.driver, By.xpath("//h4/p"));
					TimeUnit.SECONDS.sleep(5);
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to Enter Cash : " +mapFeederSubscriptionDetails.get("Cash")+ ",Into the field on Feeder Subscription screen.]";
						return false;
					}
					if(mapFeederSubscriptionDetails.get("Expected Units") != null){
						String actualValue = NewUICommonFunctions.jsGetElementAttribute("units");
						if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapFeederSubscriptionDetails.get("Expected Units"))){
							Messages.errorMsg = "ActualValue For Units :'"+actualValue+"' is Not matching with the Expected Value :"+mapFeederSubscriptionDetails.get("Expected Units");
							return false;
						}
					}
				}
				
				if (mapFeederSubscriptionDetails.get("Units") != null) {
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='units']"), mapFeederSubscriptionDetails.get("Units"));
					Elements.click(Global.driver, By.xpath("//h4/p"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to Enter Units : " +mapFeederSubscriptionDetails.get("Units")+ ",Into the field on Feeder Subscription screen.]";
						return false;
					}
					if(mapFeederSubscriptionDetails.get("Expected Cash") != null){
						String actualValue = NewUICommonFunctions.jsGetElementAttribute("amount");
						if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapFeederSubscriptionDetails.get("Expected Cash"))){
							Messages.errorMsg = "ActualValue For Cash :'"+actualValue+"' is Not matching with the Expected Value :"+mapFeederSubscriptionDetails.get("Expected Cash");
							return false;
						}								
					}
				}

				if (mapFeederSubscriptionDetails.get("OperationType") != null) {
					bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Feeder-Subscription", mapFeederSubscriptionDetails.get("OperationType"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: Unable to perform : "+mapFeederSubscriptionDetails.get("OperationType")+", In Feeder Subscription window]";
						return false;
					}					
				}
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean doVerifyFeederSubscriptionDetails(Map<String, String> mapFeederSubscriptionDetails){
		try {
			String sValue = "";
			bvalidateStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4//p[contains(text(),'Feeder Subscription')]"), 10);
			if (!bvalidateStatus) {
				Messages.errorMsg = "[ ERROR : Wasn't navigated to the checker's Feeder Subscription entry details verification page.]\n";
				return false;
			}
			if (mapFeederSubscriptionDetails.get("Client Name") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Client Name']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederSubscriptionDetails.get("Client Name"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Client Name : "+sValue+" , wasn't matching with Expected : "+mapFeederSubscriptionDetails.get("Client Name")+"]\n";
					bvalidateStatus = false;
				}		
			}

			if (mapFeederSubscriptionDetails.get("Fund Family Name") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Fund Family Name']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederSubscriptionDetails.get("Fund Family Name"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Fund Family Name : "+sValue+" , wasn't matching with Expected : "+mapFeederSubscriptionDetails.get("Fund Family Name")+"]\n";
					bvalidateStatus = false;
				}
			}

			if (mapFeederSubscriptionDetails.get("From Fund") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='From Fund']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederSubscriptionDetails.get("From Fund"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : From Fund : "+sValue+" , wasn't matching with Expected : "+mapFeederSubscriptionDetails.get("From Fund")+"]\n";
					bvalidateStatus = false;
				}
			}

			if (mapFeederSubscriptionDetails.get("From Class") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='From Class']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederSubscriptionDetails.get("From Class"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : From Class : "+sValue+" , wasn't matching with Expected : "+mapFeederSubscriptionDetails.get("From Class")+"]\n";
					bvalidateStatus = false;
				}
			}

			if (mapFeederSubscriptionDetails.get("Fund Account") != null) {
				String accountID = FeederRedemptionAppFunctions.getTheFundAccountNumber(mapFeederSubscriptionDetails.get("Fund Account"));
				if(accountID != null){
					mapFeederSubscriptionDetails.put("Fund Account", accountID);
					
				}
				sAccountNo = mapFeederSubscriptionDetails.get("Fund Account");
			}
			else if (mapFeederSubscriptionDetails.get("Add New Account") != null && mapFeederSubscriptionDetails.get("Add New Account").equalsIgnoreCase("Yes")) {
				Map<String, String> mapFeederSUBAccountXMLDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", mapFeederSubscriptionDetails.get("TestCaseName"));
				sAccountNo = mapFeederSUBAccountXMLDetails.get("AccountID");
			}
			
			if (sAccountNo != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Fund Account']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(sAccountNo)) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Fund Account : "+sValue+" , wasn't matching with Expected : "+sAccountNo+"]\n";
					bvalidateStatus = false;
				}
			}

			if (mapFeederSubscriptionDetails.get("To Fund") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='To Fund']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederSubscriptionDetails.get("To Fund"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : To Fund : "+sValue+" , wasn't matching with Expected : "+mapFeederSubscriptionDetails.get("To Fund")+"]\n";
					bvalidateStatus = false;
				}
			}			

			if (mapFeederSubscriptionDetails.get("Strategy") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Strategy']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederSubscriptionDetails.get("Strategy"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Strategy : "+sValue+" , wasn't matching with Expected : "+mapFeederSubscriptionDetails.get("Strategy")+"]\n";
					bvalidateStatus = false;
				}
			}
			
			if (mapFeederSubscriptionDetails.get("Effective Date") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Effective Date']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !TradeTypeSubscriptionAppFunctions.formatDate(sValue.trim()).equalsIgnoreCase(mapFeederSubscriptionDetails.get("Effective Date"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Effective Date : "+sValue+" , wasn't equal to Expected : "+mapFeederSubscriptionDetails.get("Effective Date")+"]\n";
					bvalidateStatus = false;
				}
			}
			
			if (mapFeederSubscriptionDetails.get("Cash") != null || mapFeederSubscriptionDetails.get("Expected Cash") != null) {
				String sCashToBeVerified = "";
				if (mapFeederSubscriptionDetails.get("Cash") != null) {
					sCashToBeVerified = mapFeederSubscriptionDetails.get("Cash");
				}
				else if (mapFeederSubscriptionDetails.get("Expected Cash") != null) {
					sCashToBeVerified = mapFeederSubscriptionDetails.get("Expected Cash");
				}
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Amount']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || Float.parseFloat(sValue.trim().replaceAll(",", "")) != Float.parseFloat(sCashToBeVerified)) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Cash : "+Float.parseFloat(sValue.trim().replaceAll(",", ""))+" , wasn't matching with Expected Cash: "+sCashToBeVerified+"]\n";
					bvalidateStatus = false;
				}
			}
			
			if (mapFeederSubscriptionDetails.get("Units") != null || mapFeederSubscriptionDetails.get("Expected Units") != null) {
				String sUnitsToBeVerified = "";
				if (mapFeederSubscriptionDetails.get("Units") != null) {
					sUnitsToBeVerified = mapFeederSubscriptionDetails.get("Units");
				}
				else if (mapFeederSubscriptionDetails.get("Expected Units") != null) {
					sUnitsToBeVerified = mapFeederSubscriptionDetails.get("Expected Units");
				}
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Units']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || Float.parseFloat(sValue.trim().replaceAll(",", "")) != Float.parseFloat(sUnitsToBeVerified)) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Units : "+Float.parseFloat(sValue.trim().replaceAll(",", ""))+" , wasn't matching with Expected : "+sUnitsToBeVerified+"]\n";
					bvalidateStatus = false;
				}
			}
			
			Messages.errorMsg = sAppendErrorMsg;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bvalidateStatus;
	}

	public static boolean doCheckerOperationsForFeederSubscription(String sActionType){
		try {
			if (sActionType != null && !sActionType.equalsIgnoreCase("")) {
				if (sActionType.equalsIgnoreCase("Return")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'Return')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to perform checker operation : "+sActionType+"]";
						return false;
					}
					TimeUnit.SECONDS.sleep(2);
					bStatus = Elements.enterText(Global.driver, By.id("comments"), "Returned By QA");
					if (!bStatus) {
						return false;					
					}
					TimeUnit.SECONDS.sleep(2);
					Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'saveReasonForRejection')]"));
					return true;
				}
				if (sActionType.equalsIgnoreCase("Approve")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'Final')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to perform checker operation : "+sActionType+"]";
						return false;
					}
					return true;
				}
				if (sActionType.equalsIgnoreCase("Cancel")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'Dashboard')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to perform checker operation : "+sActionType+"]";
						return false;
					}
					return true;
				}
			}
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
