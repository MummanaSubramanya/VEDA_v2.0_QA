package com.viteos.veda.unusedwork.templibrary;

import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.master.lib.Constants;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.OpeningBalanceMasterAppFunctions;

public class SrihariClass {
	
	static boolean bStatus;

	public static boolean addSubscriptionTrade(Map<String , String> mapSubscriptionDetails){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='requestDate']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Received Date Field is Not Visible";
				return false;
			}

			if(mapSubscriptionDetails.get("Request Received Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='requestDate']"), mapSubscriptionDetails.get("Request Received Date"));
				if(!bStatus){
					Messages.errorMsg = "Request Received Date Not Entered";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Request Received Time")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='requestTime']"), mapSubscriptionDetails.get("Request Received Time"));
				if(!bStatus){
					Messages.errorMsg = "Request Received Time Not Entered";
					return false;
				}
			}
			if(mapSubscriptionDetails.get("Order Received Office")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='orderReceivedOffice']"), mapSubscriptionDetails.get("Order Received Office"));
				if(!bStatus){
					Messages.errorMsg = "Order Received Office Not Entered";
					return false;
				}
			}
			if(mapSubscriptionDetails.get("Time Zone")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@placeholder='Time Zone']"), mapSubscriptionDetails.get("Time Zone"));
				if(!bStatus){
					Messages.errorMsg = "Time Zone Not Entered";
					return false;
				}
			}
			if(mapSubscriptionDetails.get("Source")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='sourceValue']"), mapSubscriptionDetails.get("Source"));
				if(!bStatus){
					Messages.errorMsg = "Source Not Entered";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Mode of Request")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Mode of Request"), "//div[@id='s2id_fkModeOfRequestIdPk']//span[contains(text(),'Mode of Request')]");
				if(!bStatus){
					Messages.errorMsg = "Mode of Request Drop down Not Selected";
					return false;
				}
			}


			if(mapSubscriptionDetails.get("External ID")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='externalId']"), mapSubscriptionDetails.get("External ID"));
				if(!bStatus){
					Messages.errorMsg = "External ID Not Entered";
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

	public static boolean doVerifyChargesDetail(Map<String,String>mapVerifySubscriptionDetails){
		try
		{
			boolean validateStatus = true;
			String appendMsg="";

			// Verify Notice Charges  with out modification

			if(mapVerifySubscriptionDetails.get("ExpectedNoticeCharges") != null ){
				// Verify Notice Charge Default

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-noticeChargeYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  "[ Notice Charge Default Radio button Yes is not selected  ]\n";
				}


				// Verify Notice Charges 
				String ActualNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("orderDetails0.noticeCharges");

				String ExpectedNoticeCharge = mapVerifySubscriptionDetails.get("ExpectedNoticeCharges");

				if(ActualNoticeCharges != null){

					if(!ActualNoticeCharges.equalsIgnoreCase(ExpectedNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Notice Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("ExpectedNoticeCharges")+" ]\n";
					}
				}
			}

			// Verify Notice Charges  with modification

			if(mapVerifySubscriptionDetails.get("NewAmountForNoticePeriod") !=null && mapVerifySubscriptionDetails.get("ExpectedNewNoticeCharges") !=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-noticeChargeNo']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "[ Notice Charge Default Radio button No is not selected  ]\n";
				}

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("orderDetails0.noticeCharges");
				String ExpectedNewNoticeCharge = mapVerifySubscriptionDetails.get("NewAmountForNoticePeriod");
				if(mapVerifySubscriptionDetails.get("ExpectedNewNoticeCharges") != null && ActualNewNoticeCharges != null){

					if(!ActualNewNoticeCharges.equalsIgnoreCase(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Notice Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("ExpectedNewNoticeCharges")+" ]\n";
					}
				}
			}
			//Verify the Transaction Charges with out modification

			if(mapVerifySubscriptionDetails.get("ExpectedTransactionCharges") != null ){
				// Verify Notice Charge Default

				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-transactionChargesYes']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[Notice Charge Default Radio button Yes is not selected  ]\n";
				}


				// Verify Notice Charges 
				String ActualTransCharges = NewUICommonFunctions.jsGetElementAttribute("orderDetails0.transactionCharges");

				String ExpectedTransNoticeCharge = mapVerifySubscriptionDetails.get("ExpectedTransactionCharges");

				if(ActualTransCharges != null){

					if(!ActualTransCharges.equalsIgnoreCase(ExpectedTransNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Transaction  Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("ExpectedTransactionCharges")+" ]\n";
					}
				}
			}

			// Verify Transaction  Charges  with modification

			if(mapVerifySubscriptionDetails.get("NewTransactionCharges") !=null && mapVerifySubscriptionDetails.get("NewExpectedTransactionCharges") !=null){
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-transactionChargesNo']/span[@class='checked']"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "[ Transaction  Charge Default Radio button No Radio button is not selected ]\n";
				}

				String ActualNewNoticeCharges = NewUICommonFunctions.jsGetElementAttribute("orderDetails0.transactionCharges");
				String ExpectedNewNoticeCharge = mapVerifySubscriptionDetails.get("NewExpectedTransactionCharges");

				if( ActualNewNoticeCharges != null){

					if(ActualNewNoticeCharges.equalsIgnoreCase(ExpectedNewNoticeCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Transaction  Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("NewExpectedTransactionCharges")+" ]\n";
					}
				}
			}


			//verify Adhoc charges

			if(mapVerifySubscriptionDetails.get("Adhoc Charges") != null){
				String ActualAdhoc =  NewUICommonFunctions.jsGetElementAttribute("orderDetails0.adhocCharges");
				String ExpectedNewAdhocCharge = mapVerifySubscriptionDetails.get("Adhoc Charges");

				if(ActualAdhoc != null){
					if(ActualAdhoc.equalsIgnoreCase(ExpectedNewAdhocCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Adhoc  Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("Adhoc Charges")+" ]\n";
					}

				}
			}


			//Verify  Total charges
			if(mapVerifySubscriptionDetails.get("ExpectedTotalCharges")!=null){
				String ActualToatal =  NewUICommonFunctions.jsGetElementAttribute("orderDetails0.charges");
				String ExpectedCharge = mapVerifySubscriptionDetails.get("ExpectedTotalCharges");

				if(ActualToatal != null){
					if(!ActualToatal.equalsIgnoreCase(ExpectedCharge)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Total  Charges are Not matched with Expected "+mapVerifySubscriptionDetails.get("ExpectedTotalCharges")+" ]\n";
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

	public static boolean doVerifyAmountDetails(Map<String,String>mapVerifyAmountDetail){
		try{
			boolean validateStatus = true;
			String appendMsg="";

			// Verify Charges

			if(mapVerifyAmountDetail.get("ExpectedAmountDetailCharges")!= null){

				String ActualCharges = NewUICommonFunctions.jsGetElementAttribute("ng-model", "charges");
				String ExpCharges = mapVerifyAmountDetail.get("ExpectedAmountDetailCharges");

				if(ActualCharges != null){
					if(!ActualCharges.equalsIgnoreCase(ExpCharges)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ AmountDetail Charges are Not matched with Expected "+mapVerifyAmountDetail.get("ExpectedAmountDetailCharges")+" ]\n";
					}
				}
				else{
					validateStatus = false;
					appendMsg =  appendMsg+"Actual Amount charges are Null \n";
				}
			}

			//Verify Interest

			if(mapVerifyAmountDetail.get("ExpectedAmountDetailInterest")!= null){

				String ActualInterest = NewUICommonFunctions.jsGetElementAttribute("Value", mapVerifyAmountDetail.get("Interest"));
				String ExpInterest = mapVerifyAmountDetail.get("ExpectedAmountDetailInterest");

				if(ActualInterest != null){
					if(!ActualInterest.equalsIgnoreCase(ExpInterest)){
						validateStatus = false;
						appendMsg =  appendMsg+"[ Actual Interest are Not matched with Expected "+mapVerifyAmountDetail.get("ExpectedAmountDetailInterest")+" ]\n";
					}
				}
				else{
					validateStatus = false;
					appendMsg =  appendMsg+"Actual Interest charges are Null \n";
				}
			}

			//Verify Amount Payable





			Messages.errorMsg = appendMsg;
			return validateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}




	// Redemption function is stared............................................

	///.......................................................................	

	public static boolean doFillRequestDetails(Map<String, String> mapRedemptionDetails) {
		try {

			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[normalize-space()='Request Details']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Request Details are Not visible";
				return false;
			}


			if(mapRedemptionDetails.get("Request Received Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Received Date']/following::div/input[contains(@id,'requestDate')]"), mapRedemptionDetails.get("Request Received Date"));
				if(!bStatus){
					Messages.errorMsg = "Request Received Date Not Entered";
					return false;
				}
			}

			if(mapRedemptionDetails.get("Request Received Time")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Received Time']/following::div/input[contains(@id,'requestTime')]"), mapRedemptionDetails.get("Request Received Time"));
				if(!bStatus){
					Messages.errorMsg = "Request Received Time Not Entered";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Redemption')]"));
				if(!bStatus){
					Messages.errorMsg = "Failed to click on subscription Label";
					return false;
				}
			}
			if(mapRedemptionDetails.get("Order Received Office")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='Received Office']/following::div/input[contains(@id,'ReceivedOffice')]"), mapRedemptionDetails.get("Order Received Office"));
				if(!bStatus){
					Messages.errorMsg = "Order Received Office Not Entered";
					return false;
				}
			}
			if(mapRedemptionDetails.get("Time Zone")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@placeholder='Time Zone']"), mapRedemptionDetails.get("Time Zone"));
				if(!bStatus){
					Messages.errorMsg = "Time Zone Not Entered";
					return false;
				}

			}
			if(mapRedemptionDetails.get("Mode of Request")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapRedemptionDetails.get("Mode of Request"), By.xpath("//label[normalize-space()='Mode of Request']/following::div[contains(@id,'modeOfRequest')]"));
				if(!bStatus){
					Messages.errorMsg = "Mode of Request Drop down Not Selected";
					return false;
				}
			}


			if(mapRedemptionDetails.get("External ID")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()='External ID']/following::input[contains(@id,'externalId')]"), mapRedemptionDetails.get("External ID"));
				if(!bStatus){
					Messages.errorMsg = "External ID Not Entered";
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



	public static boolean doFillInvestorDetails(Map<String, String> mapInvestorDetails){
		try{
			if(mapInvestorDetails.get("Investor Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapInvestorDetails.get("Investor Name"), By.xpath("//label[normalize-space()='Investor Name']/following::div[@id='s2id_investorID']"));
				if(!bStatus){
					Messages.errorMsg = "Investor Name is Not Entered";
					return false;
				}

				//View Investor Details
				if(mapInvestorDetails.get("Investor KYC")!=null && mapInvestorDetails.get("Investor KYC").equalsIgnoreCase("Yes")){

					Global.driver.findElement(By.xpath("//a[contains(@onclick,'viewInvestorKyc')]")).click();
					java.util.Set<String> AllWindowHandles = Global.driver.getWindowHandles(); 
					String window1 = (String) AllWindowHandles.toArray()[0]; 
					System.out.print("window1 handle code = "+AllWindowHandles.toArray()[0]); 
					String window2 = (String) AllWindowHandles.toArray()[1]; 
					System.out.print("\nwindow2 handle code = "+AllWindowHandles.toArray()[1]);

					Global.driver.switchTo().window(window2);
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//h4/p[contains(text(),'KYC Details')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "View Investor KYC Page is Not Displayed";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'close')]"));
					if(!bStatus){
						Messages.errorMsg = "View Investor KYC Page is Not Closed";
						return false;
					}
					Global.driver.switchTo().window(window1);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Holder Name']/following::div[@id='s2id_holderNameDropdown']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Page is not able to Navigate to Redemption Page";
						return false;
					}		
				}
			}


			if(mapInvestorDetails.get("Holder Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapInvestorDetails.get("Holder Name"), By.xpath("//label[normalize-space()='Holder Name']/following::div[@id='s2id_holderNameDropdown']"));
				if(!bStatus){
					Messages.errorMsg = "Holder Name is Not Selected";
					return false;
				}
				//View Holder Kyc Details
				if(mapInvestorDetails.get("Holder KYC")!=null && mapInvestorDetails.get("Holder KYC").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//a[contains(@onclick,'viewHolderKyc')]"));
					if(!bStatus){
						Messages.errorMsg = "View Holder KYC link is Not clicked";
						return false;
					}
					java.util.Set<String> AllWindowHandles = Global.driver.getWindowHandles(); 
					String window1 = (String) AllWindowHandles.toArray()[0]; 
					System.out.print("window1 handle code = "+AllWindowHandles.toArray()[0]); 
					String window2 = (String) AllWindowHandles.toArray()[1]; 
					System.out.print("\nwindow2 handle code = "+AllWindowHandles.toArray()[1]);

					Global.driver.switchTo().window(window2);
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//p[contains(text(),'KYC Details')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "View Holder KYC Page is Not Displayed";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'close')]"));
					if(!bStatus){
						Messages.errorMsg = "View Holder KYC Page is Not Closed";
						return false;
					}
					Global.driver.switchTo().window(window1);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Account Id']/following::div[@id='s2id_accountNameDropDown']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Page is not able to Navigate to Redemption Page";
						return false;
					}		
				}
			}
			if(mapInvestorDetails.get("Account ID")!=null){
				bStatus =NewUICommonFunctions.selectFromDropDownOfApplication(mapInvestorDetails.get("Account ID"), By.xpath("//label[normalize-space()='Account Id']/following::div[@id='s2id_accountNameDropDown']"));
				if(!bStatus){
					Messages.errorMsg = "Account Id is Not Selected";
					return false;
				}						
			}

			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	public static boolean doFillFundDetails(Map<String, String> mapSubscriptionDetails) {
		try {

			if(mapSubscriptionDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Client Name"), By.xpath("//label[normalize-space()='Client Name']/following::div[@id='s2id_cmbClientName']"));				
				if(!bStatus){
					Messages.errorMsg = "Client Name is not Selected";
					return false;
				}
			}


			if(mapSubscriptionDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Fund Family Name"), By.xpath("//label[normalize-space()='Fund Family Name']/following::div[@id='s2id_cmbFundFamilyName']"));
				if(!bStatus){
					Messages.errorMsg = "Fund Family Name is not Selected";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Legal Entity Name"), By.xpath("//label[normalize-space()='Legal Entity Name']/following::div[@id='s2id_cmbFundName']"));
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Name is not selected";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Class Name"), By.xpath("//label[normalize-space()='Class Name']/following::div[@id='s2id_cmbClassName']"));
				if(!bStatus){
					Messages.appErrorMsg = "Class Name is Not Selected";
					return false;
				}
			}

			if(mapSubscriptionDetails.get("Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapSubscriptionDetails.get("Series Name"), By.xpath("//label[normalize-space()='Series Name']/following::div[@id='s2id_cmbSeriesName']"));
				if(!bStatus){
					Messages.errorMsg = "Series Name is not selected  from drop down";
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




	//--------------------------------------------------------------------------------------------------------------

	//---- Transfer------


	public static boolean doFillFundDetailsTransfer(Map<String, String> mapTransferDetails) {
		try {
			//select Client Name
			if(mapTransferDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Client Name"), By.xpath("//label[normalize-space()='Client Name']/following::div[@id='s2id_cmbClientName']"));				
				if(!bStatus){
					Messages.errorMsg = "Client Name is not Selected";
					return false;
				}
			}

			//Select Fund Family
			if(mapTransferDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Fund Family Name"), By.xpath("//label[normalize-space()='Fund Family Name']/following::div[@id='s2id_cmbFundFamilyName']"));
				if(!bStatus){
					Messages.errorMsg = "Fund Family Name is not Selected";
					return false;
				}
			}
			//Select Legal Entity
			if(mapTransferDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Legal Entity Name"), By.xpath("//label[normalize-space()='Legal Entity']/following::div[@id='s2id_cmbFundName']"));
				if(!bStatus){
					Messages.errorMsg = "Legal Entity Name is not selected";
					return false;
				}
			}

			//Select Class Name
			if(mapTransferDetails.get("Class Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Class Name"), By.xpath("//label[normalize-space()='Class']/following::div[@id='s2id_cmbClassName']"));
				if(!bStatus){
					Messages.appErrorMsg = "Class Name is Not Selected";
					return false;
				}
			}

			// Select the Sereies Name
			if(mapTransferDetails.get("Series Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapTransferDetails.get("Series Name"), By.xpath("//label[normalize-space()='Series']/following::div[@id='s2id_cmbSeriesName']"));
				if(!bStatus){
					Messages.errorMsg = "Series Name is not selected  from drop down";
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

	public static boolean doFillTransferDetails(Map<String, String> mapTransferDetails){
		try{

			//Select Full Transfer  Radio button

			if(mapTransferDetails.get("Full Transfer")!=null){
				if(mapTransferDetails.get("Full Transfer").equalsIgnoreCase("Yes")){

					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-fullTransferYesId']/span/input[@id='fullTransferYesId']"));
					if(!bStatus){
						Messages.errorMsg = "Full Transfer Radio button Yes is Not clicked";
						return false;
					}
					String percentageValue = "100";
					String actualValue = NewUICommonFunctions.jsGetElementValueWithXpath("//input[@id='transferPercentage']");
					if(!actualValue.equalsIgnoreCase(percentageValue)){
						Messages.errorMsg = "Percentage Value is not Changed to 100 when Full redemption Yes button selected";
						return false;
					}
				}
				if(mapTransferDetails.get("Full Transfer").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-fullTransferNoId']/span/input[@id='fullTransferNoId']"));
					if(!bStatus){
						Messages.errorMsg = "Full Transfer Radio button No is Not clicked";
						return false;
					}
				}
			}

			//Enter Effective Date
			if( mapTransferDetails.get("Effective Date")!=null){
				bStatus = Elements.enterText(Global.driver, By.id("effectiveDate"), mapTransferDetails.get("Effective Date"));
				if(!bStatus){
					Messages.errorMsg = "Effective Date is Not Entered";
					return false;
				}
			}

			//Enter Percentage
			if(mapTransferDetails.get("Percentage")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='transferPercentage']"), mapTransferDetails.get("Percentage"));
				if(!bStatus){
					Messages.errorMsg = "Percentage Cannot be entered";
					return false;
				}
			}

			//Select Amount/Share Radio button

			if(mapTransferDetails.get("AmountorShares")!=null){
				if(mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Amount")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-TORA_radioButton']"));
					if(!bStatus){
						Messages.errorMsg = "Amount Radio button cannot be selected";
						return false;
					}
					if(mapTransferDetails.get("Cash")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='amount']"), mapTransferDetails.get("Cash"));
						if(!bStatus){
							Messages.errorMsg = "Cash is  Not Entered";
							return false;
						}
					}
				}
				if(mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Shares")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-TORS_radioButton']"));
					if(!bStatus){
						Messages.errorMsg = "Shares Radio button cannot be selected";
						return false;
					}
					if(mapTransferDetails.get("Share")!=null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='shares']"), mapTransferDetails.get("Share"));
						if(!bStatus){
							Messages.errorMsg = "Share Value  Not Entered";
							return false;
						}
					}
				}
			}
			return true;
		}
		catch(Exception e){

			return false;
		}
	}

	public static boolean doClickOnExpandOrCollapseButtonsOnTransferSecondTab(String sYesForExpandNoCollapse, String sCollapsableTabIndexNumber){
		try {
			boolean bExpandStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='expand']"), 3);
			boolean bCollpaseStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='collapse']"), 3);
			if (bExpandStatus == true && bCollpaseStatus == false && sYesForExpandNoCollapse.equalsIgnoreCase("Yes")) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='expand']"));
				if (!bStatus) {					
					return false;
				}
				return true;
			}
			if (bExpandStatus == false && bCollpaseStatus == true && sYesForExpandNoCollapse.equalsIgnoreCase("No")) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//span[@class='badge badge-danger' and normalize-space()='"+sCollapsableTabIndexNumber+"']//..//following-sibling::div//a[@class='collapse']"));
				if (!bStatus) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}	

	public static boolean doMakerVerifyRequestDetailsTab(Map<String, String> mapTransferDetails){
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnTransferSecondTab("Yes", "1");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Request Details' Tab.]\n";
				return false;
			}

			//Verify Request Date
			if (mapTransferDetails.get("Request Received Date") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.id("requestDate"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Request Received Date"))) {
					Messages.errorMsg = "[ERROR : Actual Request Recieved Date : "+sValue+", is not matching with expected : "+mapTransferDetails.get("Request Received Date")+"] \n";
					return false;
				}
			}

			//Verify Received Time
			if (mapTransferDetails.get("Request Received Time") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[contains(@id,'requestTime')]"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Request Received Time"))) {
					Messages.errorMsg = "[ERROR : Actual Request Received Time : "+sValue+", is not matching with expected : "+mapTransferDetails.get("Request Received Time")+"] \n";
					return false;
				}
			}

			//Verify Received office
			if (mapTransferDetails.get("Order Received Office") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='orderRecievedOffice']"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Order Received Office"))) {
					Messages.errorMsg = "[ERROR : Actual Order Received Office : "+sValue+", is not matching with expected : "+mapTransferDetails.get("Order Received Office")+"] \n";
					return false;
				}
			}

			//Verify Time Zone
			if (mapTransferDetails.get("Time Zone") != null) {
				String sValue = Elements.getElementAttribute(Global.driver, By.xpath("//input[@placeholder='Time Zone']"), "value");
				if (sValue == null || sValue.equalsIgnoreCase("") || !sValue.equalsIgnoreCase(mapTransferDetails.get("Time Zone"))) {
					Messages.errorMsg = "[ERROR : Actual Time Zone : "+sValue+", is not matching with expected : "+mapTransferDetails.get("Time Zone")+"] \n";
					return false;
				}
			}

			//Verify Source
			if (mapTransferDetails.get("Mode of Request") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Mode of Request", By.xpath("//div[contains(@id,'modeOfRequest')]//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Mode of Request"), "Yes", false)) {
					return false;
				}
			}
			//Verify External Id
			if (mapTransferDetails.get("External ID") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("External ID", By.xpath("//input[contains(@id,'externalId')]"), mapTransferDetails.get("External ID"), "No", false)) {
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean doMakerVerifyFromInvestorDetailsTab(Map<String, String> mapTransferDetails){
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnTransferSecondTab("Yes", "2");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Investor Details' Tab.]\n";
				return false;
			}
			if (mapTransferDetails.get("From Investor Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Investor Name", By.xpath("//div[@id='s2id_fromInvestorId']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Investor Name"), "Yes", false)) {
					return false;
				}
			}
			if (mapTransferDetails.get("From Holder Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Holder Name", By.xpath("//div[@id='s2id_fromHolderId']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Holder Name"), "Yes", false)) {
					return false;
				}
			}
			if (mapTransferDetails.get("From Account Id") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Account ID", By.xpath("//div[@id='s2id_fromAccountId']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Account ID"), "Yes", false)) {
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean doMakerVerifyFundDetailsTab(Map<String, String> mapTransferDetails){
		try {
			bStatus = doClickOnExpandOrCollapseButtonsOnTransferSecondTab("Yes", "4");
			if (!bStatus) {
				Messages.errorMsg = Messages.errorMsg + "[ ERROR : Failed to Perform Expnad/Collpase Operation on 'Fund Details' Tab.]\n";
				return false;
			}
			//Verify Client Name
			if (mapTransferDetails.get("Client Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Client Name", By.xpath("//div[@id='s2id_cmbClientName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Client Name"), "Yes", false)) {
					return false;
				}
			}

			//Verify Fund Family Name
			if (mapTransferDetails.get("Fund Family Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Fund Family Name", By.xpath("//div[@id='s2id_cmbFundFamilyName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Fund Family Name"), "Yes", false)) {
					return false;
				}
			}

			//Verify Legal Entity Name
			if (mapTransferDetails.get("Legal Entity Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Legal Entity Name", By.xpath("//div[@id='s2id_cmbFundName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Legal Entity Name"), "Yes", false)) {
					return false;
				}
			}

			//Verify Class Name
			if (mapTransferDetails.get("Class Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Class Name", By.xpath("//div[@id='s2id_cmbClassName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Class Name"), "Yes", false)) {
					return false;
				}
			}

			//Verify Series Name
			if (mapTransferDetails.get("Series Name") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Series Name", By.xpath("//div[@id='s2id_cmbSeriesName']//span[contains(@id,'select2-chosen')]"), mapTransferDetails.get("Series Name"), "Yes", false)) {
					return false;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

	public static boolean doMakerVerifyTransferDetailsTab(Map<String, String> mapTransferDetails){
		try {		
			//Verify Full Transfer Radio button
			if (mapTransferDetails.get("Full Transfer") != null) {
				if (mapTransferDetails.get("Full Transfer").equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-fullTransferYesId']//span[@class='checked']"), 2);
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : Full Transfer wasn't marked to 'Yes'.]\n";
						return false;
					}
				}
				if (mapTransferDetails.get("Full Transfer").equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-fullTransferNoId']//span[@class='checked']"), 2);
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : Full Transfer wasn't marked to 'No'.]\n";
						return false;
					}
				}
			}
			//Verify Amount or shares

			if (mapTransferDetails.get("AmountorShares") != null) {
				if (mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Amount")) {

					//Verify the Amount
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-TORS_radioButton']//span[@class='checked']]"), 2);
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : AmountorShares wasn't marked to 'Amount'.]\n";
						return false;
					}
					if (mapTransferDetails.get("Cash") != null) {				
						if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Cash", By.xpath("//input[@id='amount']"), mapTransferDetails.get("Cash"), "No", true)) {
							return false;
						}
					}

				}
				if (mapTransferDetails.get("AmountorShares").equalsIgnoreCase("Shares")) {

					//Verify the Shares
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='uniform-TORS_radioButton']//span[@class='checked']"), 2);
					if (!bStatus) {
						Messages.errorMsg = "[ERROR : AmountorShares wasn't marked to 'Shares'.]\n";
						return false;
					}
					if (mapTransferDetails.get("Share") != null) {				
						if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Share", By.xpath("//input[@id='shares']"), mapTransferDetails.get("Share"), "No", true)) {
							return false;
						}
					}
				}
			}


			//Verify Effective Date
			if (mapTransferDetails.get("Effective Date") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Effective Date", By.xpath("//input[@id='effectiveDate']"), mapTransferDetails.get("Effective Date"), "No", false)) {
					return false;
				}
			}

			//Verify Percentage 
			if (mapTransferDetails.get("Percentage") != null) {				
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Percentage", By.xpath("//input[@id='transferPercentage']"), mapTransferDetails.get("Percentage"), "No", false)) {
					return false;
				}
			}

			if (mapTransferDetails.get("Expected NAV Date") != null) {	

				bStatus = Verify.verifyElementPresent(Global.driver, By.xpath("//input[@id='navDate' and @value='"+mapTransferDetails.get("Expected NAV Date")+"']/preceding-sibling::label"));
				if(!bStatus){
					Messages.appErrorMsg = "[ERROR : Actual Nav Date is not Matched with Expected Nav Date" +mapTransferDetails.get("Expected NAV Date")+" ] \n";
					return false;

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static boolean doMakerVerifyAmountDetailsTab(Map<String, String> mapTransferDetails){
		try {
			if (mapTransferDetails.get("Gross Amount") != null) {
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Gross Amount", By.xpath("//input[@id='grossAmount']"), mapTransferDetails.get("Gross Amount"), "No", true)) {
					return false;
				}
			}
			if (mapTransferDetails.get("Charges") != null) {
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Charges", By.xpath("//input[@id='amountCharges']"), mapTransferDetails.get("Charges"), "No", true)) {
					return false;
				}
			}
			if (mapTransferDetails.get("Transfer Amount") != null) {
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Transfer Amount", By.xpath("//input[@id='finalAmount']"), mapTransferDetails.get("Transfer Amount"), "No", true)) {
					return false;
				}
			}
			if (mapTransferDetails.get("Net Units") != null) {
				if (!OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Net Units", By.xpath("//input[@id='finalUnits']"), mapTransferDetails.get("Net Units"), "No", true)) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean doMakerFillOtherDetailsTab(Map<String,String>mapTransferDetails){
		try{
			if(mapTransferDetails.get("Crystalize Fee")!=null){

				if(mapTransferDetails.get("Crystalize Fee").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-crystalizeFeeYes']"));
					if(!bStatus){
						Messages.errorMsg = "Crystalize Fee Radio button 'Yes' is not clicked";
						return false;
					}
				}
				if(mapTransferDetails.get("Crystalize Fee").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-crystalizeFeeNo']"));
					if(!bStatus){
						Messages.errorMsg = "Crystalize Fee Radio button 'No' is not clicked";
						return false;
					}
				}
			}


			if(mapTransferDetails.get("Cumulative Return")!=null){

				if(mapTransferDetails.get("Cumulative Return").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver , By.xpath("//div[@id='uniform-cumulativeReturnYes']"));
					if(!bStatus){
						Messages.errorMsg = "Cumulative Return Radio button 'Yes' is not clicked";
						return false;
					}	
				}
				if(mapTransferDetails.get("Cumulative Return").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver , By.xpath("//div[@id='uniform-cumulativeReturnNo']"));
					if(!bStatus){
						Messages.errorMsg = "Cumulative Return Radio button 'NO' is not clicked";
						return false;
					}
				}
			}

			return true;
		}
		catch(Exception e){
			return false;
		}
	}
}
