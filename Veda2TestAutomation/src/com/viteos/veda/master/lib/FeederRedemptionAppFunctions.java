package com.viteos.veda.master.lib;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;

public class FeederRedemptionAppFunctions {
	public static boolean bStatus = true;
	public static boolean bvalidateStatus = true;
	public static String sAppendErrorMsg = "";
	public static String sAccountNo = "";

	public static boolean doVerifyFeederRedemptionDetails(Map<String, String> mapFeederRedemptionDetails){
		try {
			String sValue = "";
			bvalidateStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4//p[contains(text(),'Feeder Redemption')]"), 10);
			if (!bvalidateStatus) {
				Messages.errorMsg = "[ ERROR : Wasn't navigated to the checker's Feeder Redemption entry details verification page.]\n";
				return false;
			}

			//Verify Client Name
			if (mapFeederRedemptionDetails.get("Client Name") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Client Name']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederRedemptionDetails.get("Client Name"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Client Name : "+sValue+" , wasn't matching with Expected : "+mapFeederRedemptionDetails.get("Client Name")+"]\n";
					bvalidateStatus = false;
				}		
			}

			//Verify Fund Family Name
			if (mapFeederRedemptionDetails.get("Fund Family Name") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Fund Family Name']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederRedemptionDetails.get("Fund Family Name"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Fund Family Name : "+sValue+" , wasn't matching with Expected : "+mapFeederRedemptionDetails.get("Fund Family Name")+"]\n";
					bvalidateStatus = false;
				}
			}


			//Verify From Fund
			if (mapFeederRedemptionDetails.get("From Fund") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='From Fund']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederRedemptionDetails.get("From Fund"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : From Fund : "+sValue+" , wasn't matching with Expected : "+mapFeederRedemptionDetails.get("From Fund")+"]\n";
					bvalidateStatus = false;
				}
			}

			//Verify To Fund
			if (mapFeederRedemptionDetails.get("To Fund") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='To Fund']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederRedemptionDetails.get("To Fund"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : To Fund : "+sValue+" , wasn't matching with Expected : "+mapFeederRedemptionDetails.get("To Fund")+"]\n";
					bvalidateStatus = false;
				}
			}

			//Verify To Class*
			if (mapFeederRedemptionDetails.get("To Class") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='To Class']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederRedemptionDetails.get("To Class"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : To Class : "+sValue+" , wasn't matching with Expected : "+mapFeederRedemptionDetails.get("To Class")+"]\n";
					bvalidateStatus = false;
				}
			}


			//Verify Fund Account
			if (mapFeederRedemptionDetails.get("Fund Account") != null) {
				String fundAccount = getTheFundAccountNumber(mapFeederRedemptionDetails.get("Fund Account"));
				if(fundAccount != null){
					mapFeederRedemptionDetails.put("Fund Account", fundAccount);
				}
				sAccountNo = mapFeederRedemptionDetails.get("Fund Account");
			}
			/*else if (mapFeederRedemptionDetails.get("Add New Account") != null && mapFeederRedemptionDetails.get("Add New Account").equalsIgnoreCase("Yes")) {
				Map<String, String> mapFeederSUBAccountXMLDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", mapFeederRedemptionDetails.get("TestCaseName"));
				//Map<String, String> mapFeederSUBAccountXMLDetails = XMLLibrary.getCreatedMasterDataFromXML("XMLMessages\\FeederSUBAccount02-11-2016231756.xml", "FeederSUBAccount", mapFeederRedemptionDetails.get("TestCaseName"));

				sAccountNo = mapFeederSUBAccountXMLDetails.get("AccountID");
			}*/

			if (sAccountNo != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Fund Account']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(sAccountNo)) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Fund Account : "+sValue+" , wasn't matching with Expected : "+sAccountNo+"]\n";
					bvalidateStatus = false;
				}
			}



			//Verify Strategy
			if (mapFeederRedemptionDetails.get("Strategy") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Strategy']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !sValue.trim().equals(mapFeederRedemptionDetails.get("Strategy"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Strategy : "+sValue+" , wasn't matching with Expected : "+mapFeederRedemptionDetails.get("Strategy")+"]\n";
					bvalidateStatus = false;
				}
			}


			//Verify Effective Date
			if (mapFeederRedemptionDetails.get("Effective Date") != null) {
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Effective Date']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || !TradeTypeSubscriptionAppFunctions.formatDate(sValue.trim()).equalsIgnoreCase(mapFeederRedemptionDetails.get("Effective Date"))) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Effective Date : "+sValue+" , wasn't equal to Expected : "+mapFeederRedemptionDetails.get("Effective Date")+"]\n";
					bvalidateStatus = false;
				}
			}


			//Verify Amount
			if (mapFeederRedemptionDetails.get("Cash") != null || mapFeederRedemptionDetails.get("Expected Cash") != null) {
				String sCashToBeVerified = "";
				if (mapFeederRedemptionDetails.get("Cash") != null) {
					sCashToBeVerified = mapFeederRedemptionDetails.get("Cash");
				}
				else if (mapFeederRedemptionDetails.get("Expected Cash") != null) {
					sCashToBeVerified = mapFeederRedemptionDetails.get("Expected Cash");
				}
				sValue = Elements.getText(Global.driver, By.xpath("//div[normalize-space()='Amount']//following-sibling::div[1]"));
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || Float.parseFloat(sValue.trim().replaceAll(",", "")) != Float.parseFloat(sCashToBeVerified)) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Actual Cash : "+Float.parseFloat(sValue.trim().replaceAll(",", ""))+" , wasn't matching with Expected Cash: "+sCashToBeVerified+"]\n";
					bvalidateStatus = false;
				}
			}


			//Verify Units
			if (mapFeederRedemptionDetails.get("Units") != null || mapFeederRedemptionDetails.get("Expected Units") != null) {
				String sUnitsToBeVerified = "";
				if (mapFeederRedemptionDetails.get("Units") != null) {
					sUnitsToBeVerified = mapFeederRedemptionDetails.get("Units");
				}
				else if (mapFeederRedemptionDetails.get("Expected Units") != null) {
					sUnitsToBeVerified = mapFeederRedemptionDetails.get("Expected Units");
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

	public static boolean doCheckerOperationsForFeederRedemption(String sActionType){
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

				if (sActionType.equalsIgnoreCase("Reject")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'CRReject')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to perform checker operation : "+sActionType+"]";
						return false;
					}
					TimeUnit.SECONDS.sleep(2);
					bStatus = Elements.enterText(Global.driver, By.id("comments"), "Rejected By QA");
					if (!bStatus) {
						return false;					
					}
					TimeUnit.SECONDS.sleep(2);
					Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'saveReasonForRejection')]"));
					return true;
				}


				if (sActionType.equalsIgnoreCase("Approve")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'Done')]"));
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

	public static boolean AddFeederRedemptionDetails(Map<String, String> mapFeederRedemptionMapDetails)
	{ 
		boolean bStatus;		 
		try {
			/*	bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick, 'getAddMode')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ERROR: Unable to Click Add New Button in Feeder Redemption]";
				return false;
			}*/
			if (mapFeederRedemptionMapDetails != null && !mapFeederRedemptionMapDetails.isEmpty()) {
				if (mapFeederRedemptionMapDetails.get("Client Name") !=null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", mapFeederRedemptionMapDetails.get("Client Name"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederRedemptionMapDetails.get("Client Name")+ "In Feeder Redemption]";
						return false;
					}
				}

				if (mapFeederRedemptionMapDetails.get("Fund Family Name") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", mapFeederRedemptionMapDetails.get("Fund Family Name"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederRedemptionMapDetails.get("Fund Family Name")+ "In Feeder Redemption]";
						return false;
					}
				}
				if (mapFeederRedemptionMapDetails.get("From Fund") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("From Fund", mapFeederRedemptionMapDetails.get("From Fund"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederRedemptionMapDetails.get("From Fund")+ "In Feeder Redemption]";
						return false;
					}
				}
				if (mapFeederRedemptionMapDetails.get("To Fund") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("To Fund", mapFeederRedemptionMapDetails.get("To Fund"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederRedemptionMapDetails.get("To Fund")+ "In Feeder Redemption]";
						return false;
					}					
				}
				if (mapFeederRedemptionMapDetails.get("To Class") != null) {
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("To Class", mapFeederRedemptionMapDetails.get("To Class"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederRedemptionMapDetails.get("To Class")+ "In Feeder Redemption]";
						return false;
					}
				}

				if (mapFeederRedemptionMapDetails.get("Fund Account") != null) {
					TimeUnit.SECONDS.sleep(2);
					String fundAccount = getTheFundAccountNumber(mapFeederRedemptionMapDetails.get("Fund Account"));
					if(fundAccount != null){
						mapFeederRedemptionMapDetails.put("Fund Account", fundAccount);
					}
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Account", mapFeederRedemptionMapDetails.get("Fund Account"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select" +mapFeederRedemptionMapDetails.get("Fund Account")+ "In Feeder Redemption]";
						return false;
					}
				}

			/*	if (mapFeederRedemptionMapDetails.get("Add New Account") != null && mapFeederRedemptionMapDetails.get("Add New Account").trim().equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//a[contains(@onclick,'accountAdd')]"));
					TimeUnit.SECONDS.sleep(2);
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to click on New Account From Add New Feeder Redemption Screen.]";
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
				}*/

				if (mapFeederRedemptionMapDetails.get("Strategy") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Strategy", mapFeederRedemptionMapDetails.get("Strategy"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to select Strategy : " +mapFeederRedemptionMapDetails.get("Strategy")+ ", From the field on Feeder Redemption screen.]";
						return false;
					}
				}
				if (mapFeederRedemptionMapDetails.get("Effective Date") != null) {
					Global.driver.findElement(By.id("datepicker1")).clear();
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("datepicker1"), mapFeederRedemptionMapDetails.get("Effective Date"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to Enter Effective Date : " +mapFeederRedemptionMapDetails.get("Effective Date")+ ",Into the field on Feeder Redemption screen.]";
						return false;
					}
				}
				if (mapFeederRedemptionMapDetails.get("Cash") != null) {
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapFeederRedemptionMapDetails.get("Cash"));
					Elements.click(Global.driver, By.xpath("//h4/p"));
					TimeUnit.SECONDS.sleep(5);
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to Enter Cash : " +mapFeederRedemptionMapDetails.get("Cash")+ ",Into the field on Feeder Redemption screen.]";
						return false;
					}
					if(mapFeederRedemptionMapDetails.get("Expected Units") != null){
						String actualValue = NewUICommonFunctions.jsGetElementAttribute("units");
						if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapFeederRedemptionMapDetails.get("Expected Units"))){
							Messages.errorMsg = "ActualValue For Units :'"+actualValue+"' is Not matching with the Expected Value :"+mapFeederRedemptionMapDetails.get("Expected Units");
							return false;
						}
					}
				}

				if (mapFeederRedemptionMapDetails.get("Units") != null) {
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='units']"), mapFeederRedemptionMapDetails.get("Units"));
					Elements.click(Global.driver, By.xpath("//h4/p"));
					if (!bStatus) {
						Messages.errorMsg ="[ERROR: Unable to Enter Units : " +mapFeederRedemptionMapDetails.get("Units")+ ",Into the field on Feeder Redemption screen.]";
						return false;
					}
					if(mapFeederRedemptionMapDetails.get("Expected Cash") != null){
						String actualValue = NewUICommonFunctions.jsGetElementAttribute("amount");
						if(actualValue == null || actualValue.equalsIgnoreCase("") || Float.parseFloat(actualValue) != Float.parseFloat(mapFeederRedemptionMapDetails.get("Expected Cash"))){
							Messages.errorMsg = "ActualValue For Cash :'"+actualValue+"' is Not matching with the Expected Value :"+mapFeederRedemptionMapDetails.get("Expected Cash");
							return false;
						}
								
					}
				}

				if (mapFeederRedemptionMapDetails.get("OperationType") != null) {

					bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Feeder-Redemption", mapFeederRedemptionMapDetails.get("OperationType"));
					if (!bStatus) {
						Messages.errorMsg = "[ERROR: Unable to perform : "+mapFeederRedemptionMapDetails.get("OperationType")+", In Feeder Redemption window]";
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

	public static String getTheFundAccountNumber(String sTestCaseName){
		try {
			String accountID = "";
			Map<String ,String> mapXMLAccountMasterDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath, "InvestorAccount", sTestCaseName);
			if (mapXMLAccountMasterDetails != null && !mapXMLAccountMasterDetails.isEmpty() && mapXMLAccountMasterDetails.get("AccountID") != null) {
				accountID = mapXMLAccountMasterDetails.get("AccountID");
			}else{
				Map<String, String> mapFeederSUBAccountXMLDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", sTestCaseName);
				if(mapFeederSUBAccountXMLDetails != null && mapFeederSUBAccountXMLDetails.get("AccountID") != null){
					accountID = mapFeederSUBAccountXMLDetails.get("AccountID");
				}else{
					accountID = sTestCaseName;
				}
			}			
			
			return accountID;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
}


