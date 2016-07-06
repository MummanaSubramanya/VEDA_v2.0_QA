package com.viteos.veda.master.lib;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.tenx.framework.lib.Alerts;
import com.tenx.framework.lib.Browser;
import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.UserActions;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.locators.Locators;

public class NewUICommonFunctions {
	static boolean bStatus;
	public static boolean bTradeSubscription = false;
	public static boolean isBankMasterBeingCreatedFromTrade = false;
	public static boolean isLoginFromAllocation = false;
	public static boolean loginToApplication(String sUserName,String sPassword){

		Global.driver = openChromeBrowser(Global.sVedaURL,Global.sChromeDriverPath);
		if(Global.driver == null){
			return false;
		}

		bStatus = Elements.enterText(Global.driver,Locators.LoginPage.TextBox.txtUser,sUserName);
		if(!bStatus){
			Messages.errorMsg = "Cannot enter the UserName in the textbox";
			return false;
		}	

		bStatus = Elements.enterText(Global.driver, Locators.LoginPage.TextBox.txtPassword,sPassword);
		if(!bStatus){
			Messages.errorMsg = "Cannot enter the Password in the textbox";
			return false;
		}

		bStatus = Elements.click(Global.driver, Locators.LoginPage.Button.btnLogin);
		if(!bStatus){
			Messages.errorMsg = "Cannot click the SignIn Button";
			return false;
		}

		bStatus = Wait.waitForElementVisibility(Global.driver,Locators.HomePage.Label.lblUserNameLabel, Constants.lTimeOut);
		if(!bStatus){
			Messages.errorMsg = "Dashboard page is not visible";
			return false;
		}

		return true;
	}

	public static WebDriver openChromeBrowser(String sUrl,String sPathOfDriver) {
		try {
			System.setProperty("webdriver.chrome.driver", sPathOfDriver);
			if(isLoginFromAllocation && Global.mapCredentials.get("EDS_ReportPathToDownload") != null){
				String downloadFilepath = Global.mapCredentials.get("EDS_ReportPathToDownload");
				File file = new File(downloadFilepath);
				if (!file.exists()) {
					if (file.mkdir()) {
						System.out.println("Directory is created!");
					} 
				}

				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", downloadFilepath);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePrefs);
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				cap.setCapability(ChromeOptions.CAPABILITY, options);
				Global.driver = new ChromeDriver(cap);
			}else{
				Global.driver = new ChromeDriver();
			}


			Global.driver.get(sUrl);

			Global.driver.manage().window().maximize();

			return Global.driver;

		} catch (Exception e) {
			Messages.errorMsg = e.getMessage();
			return null;
		}
	}

	public static boolean logoutFromApplication(){
		try{
			refreshThePage();
			bStatus = UserActions.mouseOver(Global.driver, By.xpath("//li[@class='dropdown dropdown-user']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot mouse hover on User Link";
				return false;
			}

			bStatus = Elements.click(Global.driver, By.xpath("//a[@href='/veda2/veda2/j_spring_security_logout']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Logout button";
				return false;
			}
			Global.driver.quit();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean selectMenu(String sMenu,String sSubMenu){
		try{
			if(sMenu.equalsIgnoreCase("User Management")){
				sMenu = "User";
				sSubMenu = "User - Role";
			}
			sMenu = sMenu.toUpperCase();
			String sMenuLocator = Locators.HomePage.Label.lblMenu.replace("menuName", sMenu);
			By objMenuLocator = By.xpath(sMenuLocator);

			String sSubMenuListLocator = sMenuLocator+"//parent::li[@class='menu-dropdown classic-menu-dropdown open' or @class='menu-dropdown classic-menu-dropdown  active open' or @class='menu-dropdown classic-menu-dropdown active open']";
			By objSubMenuListLocator = By.xpath(sSubMenuListLocator);


			String sSubMenuLocator = Locators.HomePage.Label.lblSubMenuName.replace("menuName", sMenu).replace("subMenuName", sSubMenu);
			By objSubMenuLocator = By.xpath(sSubMenuLocator);

			if(sMenu.equalsIgnoreCase("DashBoard")){
				NewUICommonFunctions.refreshThePage();
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//i[contains(@class, 'icon-home')]"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = sMenu+" is not displayed on the page";
					return false;
				}

				bStatus = Elements.clickButton(Global.driver, By.xpath("//i[contains(@class, 'icon-home')]"));
				if(!bStatus){
					Messages.errorMsg = "Click on "+sMenu+" cannot be done. Please check";
					return false;
				}
				return true;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver,objMenuLocator, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = sMenu+" is not displayed on the page";
				return false;
			}



			for (int i = 0; i < 3; i++) {
				bStatus = UserActions.mouseOver(Global.driver, objMenuLocator);
				if(!bStatus){
					continue;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver,objSubMenuListLocator,Constants.iSubMenuTime);
				if(bStatus){
					break;
				}
				refreshThePage();
			}

			if(!bStatus){
				Messages.errorMsg = "Cannot mouseover on menu. Please have look";
				return false;
			}

			bStatus = Elements.click(Global.driver, objSubMenuLocator);
			if(!bStatus){
				Messages.errorMsg = sSubMenu+" SubMenu Not Clicked";
				return false;
			}	

			if(sSubMenu.equalsIgnoreCase("Formula Setup")){
				sSubMenu = "Formula";
			}
			if(sSubMenu.equalsIgnoreCase("User - Role")){
				sSubMenu = "User";
			}
			if(sSubMenu.equalsIgnoreCase("Process")){
				sSubMenu = "Allocation";
			}
			String sBreadCrumbMenu = Locators.CommonLocators.BreadCrumbs.crumbMenu.replace("subMenuName", sSubMenu);
			By objBreadCrumbMenu = By.xpath(sBreadCrumbMenu);

			bStatus = Wait.waitForElementVisibility(Global.driver, objBreadCrumbMenu, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page Not navigated to "+sSubMenu;
				return false;
			}
			return bStatus;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doOperationsOnMasterPage(masterOperationTypes operationtype){
		try{

			String sOperationLocator ="";
			switch (operationtype) {
			case SEARCH:
				sOperationLocator = Locators.CommonLocators.MasterCommonOperationsButtons.btnOperation.replace("operationName", "fa fa-search");
				break;
			case CLEAR:
				sOperationLocator = Locators.CommonLocators.MasterCommonOperationsButtons.btnOperation.replace("operationName", "fa fa-times-circle");
				break;
			case ADDNEW:
				sOperationLocator = Locators.CommonLocators.MasterCommonOperationsButtons.btnOperation.replace("operationName", "fa fa-plus");
				break;
			}

			By objOperationLocator = By.xpath(sOperationLocator);
			bStatus = Wait.waitForElementVisibility(Global.driver,objOperationLocator,Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = operationtype+" :Operation Button Wasn't Visible";
				return false;
			}
			bStatus = Elements.click(Global.driver,objOperationLocator);
			if(!bStatus){
				Messages.errorMsg = operationtype+" Button Not Clicked";
				return false;
			}
			return true;

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean spinnerClick(WebDriver wDriver,By objLocator){
		try{
			int iValue = 0;
			while (iValue < Constants.iSpinnerTime) {
				bStatus = Elements.click(wDriver, objLocator);
				if(bStatus){
					return true;
				}
				iValue++;
				Thread.sleep(2000);
			}
			Messages.errorMsg = "Spinner is still Displayed";
			return false;
		}
		catch(Exception e){
			//e.printStackTrace();
			return false;
		}
	}

	public static boolean selectFromDropDownOfApplication(String sDropDownName,String sDropDownValue){
		try{
			bStatus = spinnerClick(Global.driver,By.xpath("//label[normalize-space(text())='"+sDropDownName+"']/following-sibling::div//span[contains(@id,'select2-chosen')]"));
			if(!bStatus){
				Messages.errorMsg = sDropDownName+" dropdown is not Clickable";
				return false;
			}

			String seletDropDownValueLocator = Locators.ClientMaster.Dropdown.ddlSelectValue.replace("selectDropDownValue",sDropDownValue);
			By objDroDownOption = By.xpath(seletDropDownValueLocator);

			bStatus = Wait.waitForElementVisibility(Global.driver,objDroDownOption,Constants.iddTimeout);
			if(!bStatus){
				Messages.errorMsg = sDropDownValue+ "is not visible in the dropdown field :"+sDropDownName;
				return false;
			}
			bStatus = spinnerClick(Global.driver, objDroDownOption);
			if(!bStatus){
				Messages.errorMsg = sDropDownValue+ "is not clicked in dropdown";
				return false;
			}
			return true;
		}
		catch(Exception e){
			Messages.errorMsg="Exception raised in key word selectFromDropDownOfApplication";
			e.printStackTrace();
			return false;
		}
	}

	public static boolean selectFromDropDownOfApplication(String sDropDownName,String sDropDownValue, int iIterations){
		try{
			//Modified By Muni
			for (int i = 0; i < iIterations; i++) {			
				bStatus = selectFromDropDownOfApplication(sDropDownName, sDropDownValue);
				if (bStatus) {
					return true;					
				}
				Robot r = new Robot();
				r.keyPress(KeyEvent.VK_ESCAPE);
				r.keyRelease(KeyEvent.VK_ESCAPE);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}

	public enum checkerActionTypes{
		APPROVE,RETURN,REJECT,CANCEL, SAVE, CheckerReview;
	}

	public enum dashboardMainDropdownToSelect{
		TRANSACTIONS, MASTERS;
	}

	public enum dashboardSubDropdownToSelect{
		NEW, IN_PROGRESS, DRAFTS, REJECTED;
	}

	public static boolean doCheckerActionType(checkerActionTypes ActionType){
		try{

			//String locator =  "//div[contains(@class,'form-actions')]//button[index]";
			String locator = "//div[contains(@class,'form-actions')]//button[contains(@onclick,'javascript') and normalize-space()='OperationName']";
			switch (ActionType) {
			case APPROVE:
				locator = locator.replace("OperationName", "Approve");
				break;
			case RETURN:
				locator = locator.replace("OperationName", "Return");
				break;
			case REJECT:
				locator = locator.replace("OperationName", "Reject");
				break;
			case CANCEL:
				locator = locator.replace("OperationName", "Cancel");
				break;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//strong[text()='General Details' or text()='Class Details']"),Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not visible for checker operations";
				return false;
			}
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//i[@class='fa fa-home']"));
			if(!bStatus){
				Messages.errorMsg = "Page cannot be scrolled to Checker operation buttons";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(locator));
			if(!bStatus){
				Messages.errorMsg = ActionType+" button is not visible/Clickable";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doSelectDashboardMainCategoryToBringUpRespectiveSubMenu(dashboardMainDropdownToSelect DASHBORAD_MAIN_DROPDOWN){
		try{
			By sTransactionsOrMastersLocator = null ;
			switch (DASHBORAD_MAIN_DROPDOWN) 
			{
			case TRANSACTIONS:
				sTransactionsOrMastersLocator = Locators.CommonLocators.DashBoardMenuDropDowns.objTransactionMainMenu ;
				break;
			case MASTERS:
				sTransactionsOrMastersLocator = Locators.CommonLocators.DashBoardMenuDropDowns.objMasterMainMenu;
				break;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, sTransactionsOrMastersLocator);
			if(!bStatus){
				Messages.errorMsg = "cannot select the menu: "+DASHBORAD_MAIN_DROPDOWN.toString();
				return false;
			}
			return true;

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean searchValueinMastersAndTransactionTableWithFilters(dashboardMainDropdownToSelect DASHBORAD_MAIN_DROPDOWN, dashboardSubDropdownToSelect DASHBORAD_SUB_DROPDOWN,String sValue){
		try {
			bStatus = doSelectDashboardMainCategoryToBringUpRespectiveSubMenu(DASHBORAD_MAIN_DROPDOWN);
			if(!bStatus){
				return false;
			}
			if(DASHBORAD_MAIN_DROPDOWN.toString().equalsIgnoreCase("TRANSACTIONS")){
				String sTransactionSubMenuLocator = "";
				switch (DASHBORAD_SUB_DROPDOWN) 
				{
				case NEW:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "1");							
					break;
				case IN_PROGRESS:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "2");					
					break;
				case DRAFTS:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "3");					
					break;
				case REJECTED:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "4");				
					break;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(sTransactionSubMenuLocator));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable To Select Sub Menu : "+DASHBORAD_SUB_DROPDOWN+" Under : "+DASHBORAD_MAIN_DROPDOWN+" in Dashboard. ";
					return false;
				}
				return true;
			}

			if(DASHBORAD_MAIN_DROPDOWN.toString().equalsIgnoreCase("MASTERS"))
			{
				String sMastersSubMenuLocator = "";
				String sTableFilterLocator = "//div[@id='filterrow.jqxgridForMastersOperation']/div/div[2]//input";
				String sTableContentLocator = "//div[@id='contenttablejqxgridForMastersOperation']//div[2]/div[text()=\"Value\"]";
				switch (DASHBORAD_SUB_DROPDOWN) 
				{
				case NEW:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "1");
					sTableFilterLocator = "//div[@id='filterrow.jqxgridMastersOperation']/div/div[2]//input";
					sTableContentLocator = "//div[@id='contenttablejqxgridMastersOperation']//div[text()=\"Value\"]";
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "ToOperate");
					sTableContentLocator = sTableContentLocator.replace("Operation", "ToOperate");
					break;
				case IN_PROGRESS:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "2");
					sTableFilterLocator = "//div[@id='filterrow.jqxgridMastersOperation']/div/div[2]//input";
					sTableContentLocator = "//div[@id='contenttablejqxgridMastersOperation']//div[text()=\"Value\"]";
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "Created");
					sTableContentLocator = sTableContentLocator.replace("Operation", "Created");
					break;
				case DRAFTS:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "3");	
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "SaveAsDraft");
					sTableContentLocator = sTableContentLocator.replace("Operation", "SaveAsDraft");
					break;
				case REJECTED:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "4");
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "Rejected");
					sTableContentLocator = sTableContentLocator.replace("Operation", "Rejected");
					break;
				}

				//wait for the sub menu
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sMastersSubMenuLocator), 10);
				if(!bStatus){
					Messages.errorMsg = "Submenu: "+DASHBORAD_SUB_DROPDOWN+" is not visible in 10 secs";
					return false;
				}

				//spinner click
				bStatus = spinnerClick(Global.driver, By.xpath(sMastersSubMenuLocator));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable To Select Sub Menu : "+DASHBORAD_SUB_DROPDOWN+" Under : "+DASHBORAD_MAIN_DROPDOWN+" in Dashboard. ";
					return false;
				}


				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableFilterLocator), 10);
				if(!bStatus){
					Messages.errorMsg = " ERROR : Dashboard grid table is not visible in Dashboard page with in 10 secs";
					return false;
				}

				//enter the value in the text box
				bStatus = Elements.enterText(Global.driver,By.xpath(sTableFilterLocator) ,sValue);
				if(!bStatus){
					Messages.errorMsg = sValue+" : cannot be entered in grid table";
					return false;
				}

				sTableContentLocator = sTableContentLocator.replace("Value", sValue);
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableContentLocator),Constants.iSearchActionTable);
				if(!bStatus){
					Messages.errorMsg = "ERROR : Record is not visible in the Table in DashBoard page";
					return false;
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean selectdashBoardMasterOrTradeSubMenu(dashboardMainDropdownToSelect DASHBORAD_MAIN_DROPDOWN,dashboardSubDropdownToSelect DASHBORAD_SUB_DROPDOWN) {
		try {
			bStatus = doSelectDashboardMainCategoryToBringUpRespectiveSubMenu(DASHBORAD_MAIN_DROPDOWN);
			if(!bStatus){
				return false;
			}
			if(DASHBORAD_MAIN_DROPDOWN.toString().equalsIgnoreCase("TRANSACTIONS")){
				String sTransactionSubMenuLocator = "";
				switch (DASHBORAD_SUB_DROPDOWN) 
				{
				case NEW:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "1");							
					break;
				case IN_PROGRESS:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "2");					
					break;
				case DRAFTS:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "3");					
					break;
				case REJECTED:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "4");				
					break;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(sTransactionSubMenuLocator));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable To Select Sub Menu : "+DASHBORAD_SUB_DROPDOWN+" Under : "+DASHBORAD_MAIN_DROPDOWN+" in Dashboard. ";
					return false;
				}
				return true;
			}

			if(DASHBORAD_MAIN_DROPDOWN.toString().equalsIgnoreCase("MASTERS"))
			{
				String sMastersSubMenuLocator = "";
				String sTableFilterLocator = "//div[@id='filterrow.jqxgridForMastersOperation']/div/div[2]//input";
				String sTableContentLocator = "//div[@id='contenttablejqxgridForMastersOperation']//div[2]/div[text()='Value']";
				switch (DASHBORAD_SUB_DROPDOWN) 
				{
				case NEW:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "1");
					sTableFilterLocator = "//div[@id='filterrow.jqxgridMastersOperation']/div/div[2]//input";
					sTableContentLocator = "//div[@id='contenttablejqxgridMastersOperation']//div[text()='Value']";
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "ToOperate");
					sTableContentLocator = sTableContentLocator.replace("Operation", "ToOperate");
					break;
				case IN_PROGRESS:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "2");
					sTableFilterLocator = "//div[@id='filterrow.jqxgridMastersOperation']/div/div[2]//input";
					sTableContentLocator = "//div[@id='contenttablejqxgridMastersOperation']//div[text()='Value']";
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "Created");
					sTableContentLocator = sTableContentLocator.replace("Operation", "Created");
					break;
				case DRAFTS:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "3");	
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "SaveAsDraft");
					sTableContentLocator = sTableContentLocator.replace("Operation", "SaveAsDraft");
					break;
				case REJECTED:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "4");
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "Rejected");
					sTableContentLocator = sTableContentLocator.replace("Operation", "Rejected");
					break;
				}

				//wait for the sub menu
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sMastersSubMenuLocator), 10);
				if(!bStatus){
					Messages.errorMsg = "Submenu: "+DASHBORAD_SUB_DROPDOWN+" is not visible in 10 secs";
					return false;
				}

				//spinner click
				bStatus = spinnerClick(Global.driver, By.xpath(sMastersSubMenuLocator));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable To Select Sub Menu : "+DASHBORAD_SUB_DROPDOWN+" Under : "+DASHBORAD_MAIN_DROPDOWN+" in Dashboard. ";
					return false;
				}


				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableFilterLocator), 10);
				if(!bStatus){
					Messages.errorMsg = " ERROR : Dashboard grid table is not visible in Dashboard page with in 10 secs";
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

	public static boolean searchValueinMastersAndTransactionTableWithTransactionID(dashboardMainDropdownToSelect DASHBORAD_MAIN_DROPDOWN, dashboardSubDropdownToSelect DASHBORAD_SUB_DROPDOWN,String sValue){
		try {
			bStatus = doSelectDashboardMainCategoryToBringUpRespectiveSubMenu(DASHBORAD_MAIN_DROPDOWN);
			if(!bStatus){
				return false;
			}

			if(DASHBORAD_MAIN_DROPDOWN.toString().equalsIgnoreCase("TRANSACTIONS")){
				String sTransactionSubMenuLocator = "";
				String sTableFilterLocator = "";
				String sTableContentLocator="";

				switch (DASHBORAD_SUB_DROPDOWN) 
				{
				case NEW:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "1");
					sTableFilterLocator = "//div[@id='filterrow.jqxgridTransactionOperation']/div/div[1]//input";
					sTableContentLocator = "//div[@id='contenttablejqxgridTransactionOperation']/div[1]/div[1]//a[text()='Value']";
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "ToOperate");
					sTableContentLocator = sTableContentLocator.replace("Operation", "ToOperate");

					break;
				case IN_PROGRESS:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "2");					
					break;
				case DRAFTS:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "3");					
					break;
				case REJECTED:
					sTransactionSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sTransactionSubMenu.replace("ReplaceIndex", "4");				
					break;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(sTransactionSubMenuLocator));
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + " EROOR : Unable To Select Sub Menu : "+DASHBORAD_SUB_DROPDOWN+" Under : "+DASHBORAD_MAIN_DROPDOWN+" in Dashboard. ";
					return false;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableFilterLocator), 10);
				if(!bStatus){
					Messages.errorMsg = " ERORR : Dashboard table is not visible in Dashboard page with in 10 secs";
					return false;
				}
				Elements.enterText(Global.driver,By.xpath(sTableFilterLocator) ,sValue);

				sTableContentLocator = sTableContentLocator.replace("Value", sValue);
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableContentLocator),Constants.iSearchActionTable);
				if(!bStatus){
					Messages.errorMsg = " ERORR : Record is not visible in the Table in DashBoard page";
					return false;
				}
				return true;

			}

			if(DASHBORAD_MAIN_DROPDOWN.toString().equalsIgnoreCase("MASTERS"))
			{
				String sMastersSubMenuLocator = "";
				String sTableFilterLocator = "//div[@id='filterrow.jqxgridForMastersOperation']/div/div[1]//input";
				String sTableContentLocator = "//div[@id='contenttablejqxgridForMastersOperation']/div[1]/div[1]//a[text()='Value']";
				switch (DASHBORAD_SUB_DROPDOWN) 
				{
				case NEW:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "1");
					sTableFilterLocator = "//div[@id='filterrow.jqxgridMastersOperation']/div/div[1]//input";
					sTableContentLocator = "//div[@id='contenttablejqxgridMastersOperation']/div[1]/div[1]//a[text()='Value']";
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "ToOperate");
					sTableContentLocator = sTableContentLocator.replace("Operation", "ToOperate");
					break;
				case IN_PROGRESS:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "2");
					sTableFilterLocator = "//div[@id='filterrow.jqxgridMastersOperation']/div/div[1]//input";
					sTableContentLocator = "//div[@id='contenttablejqxgridMastersOperation']/div[1]/div[1]//a[text()='Value']";
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "Created");
					sTableContentLocator = sTableContentLocator.replace("Operation", "Created");
					break;
				case DRAFTS:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "3");	
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "SaveAsDraft");
					sTableContentLocator = sTableContentLocator.replace("Operation", "SaveAsDraft");
					break;
				case REJECTED:
					sMastersSubMenuLocator = Locators.CommonLocators.DashBoardMenuDropDowns.sMasterSubMenu.replace("ReplaceIndex", "4");
					sTableFilterLocator = sTableFilterLocator.replace("Operation", "Rejected");
					sTableContentLocator = sTableContentLocator.replace("Operation", "Rejected");
					break;
				}

				Wait.waitForElementVisibility(Global.driver, By.xpath(sMastersSubMenuLocator), 10);
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(sMastersSubMenuLocator));
				if (!bStatus) {
					Messages.errorMsg = " ERORR : Unable To Select Sub Menu : "+DASHBORAD_SUB_DROPDOWN+" Under : "+DASHBORAD_MAIN_DROPDOWN+" in Dashboard. ";
					return false;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableFilterLocator), 10);
				if(!bStatus){
					Messages.errorMsg = " ERORR : Dashboard table is not visible in Dashboard page with in 10 secs";
					return false;
				}
				Elements.enterText(Global.driver,By.xpath(sTableFilterLocator) ,sValue);

				sTableContentLocator = sTableContentLocator.replace("Value", sValue);
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sTableContentLocator),Constants.iSearchActionTable);
				if(!bStatus){
					Messages.errorMsg = " ERORR : Record is not visible in the Table in DashBoard page";
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Messages.errorMsg = Messages.errorMsg + e.getMessage();
			return false;
		}
	}

	public static void refreshThePage(){
		try{
			Browser.reloadPage(Global.driver);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static boolean performOperationsOnTable(dashboardMainDropdownToSelect menu , dashboardSubDropdownToSelect subMenu ,String sName,String sActionType){
		try{
			//search value in the dash borad table with loop incremention
			for (int i = 0; i < 15; i++) {
				bStatus = searchValueinMastersAndTransactionTableWithFilters(menu,subMenu, sName);
				if(!bStatus){
					refreshThePage();
					continue;					
				}
				if(bStatus){
					break;
				}				
			}
			if (!bStatus) {
				Messages.errorMsg = "ERROR : The Expected values : "+sName+" is not visible in the search filter even after 15 iterations.";
				return false;
			}

			//if the values is visible click on the ID
			bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[text()=\""+sName+"\"]//..//preceding-sibling::div[1]//a"));
			if(!bStatus){
				Messages.errorMsg = "cannot click on the value: "+sName;
				return false;
			}

			if(sActionType.equalsIgnoreCase("")){
				return true;
			}

			if(sActionType.equalsIgnoreCase("Approve")){
				bStatus = doCheckerActionType(checkerActionTypes.APPROVE);
				if(!bStatus){
					return false;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "Approved Successfully but success message Not Displayed ";
					return false;
				}
				return true;
			}

			if(sActionType.equalsIgnoreCase("Reject")){
				bStatus = doCheckerActionType(checkerActionTypes.REJECT);
				if(!bStatus){
					return false;
				}

				//wait for textbox and enetr the comments
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Rejected - Comments By QA");
				Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));

				//wait for the success msg after rejection
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = " Rejected Successfully Message Not Displayed ";
					return false;
				}
				return true;
			}

			if(sActionType.equalsIgnoreCase("Return")){
				bStatus = doCheckerActionType(checkerActionTypes.RETURN);
				if(!bStatus){
					return false;
				}
				//wait for the text box and ente rthe comments
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Returned - Comments By QA");
				Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));

				//wait for the success msg
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = " Returned Successfully Message Not Displayed ";
					return false;
				}
				return true;
			}

			if(sActionType.equalsIgnoreCase("Cancel")){
				bStatus = doCheckerActionType(checkerActionTypes.CANCEL);
				if(!bStatus){
					return false;
				}
				return true;
			}
			return false; 
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect menu , dashboardSubDropdownToSelect subMenu ,String ID,String sActionType){
		try{

			for (int i = 0; i < 8; i++) {
				bStatus = searchValueinMastersAndTransactionTableWithTransactionID(menu,subMenu, ID);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;					
				}
				if(bStatus){
					break;
				}				
			}
			if (!bStatus) {
				Messages.errorMsg = " ERROR : The Expected values : "+ID+" is not visible in the serach filter after 10 iterations.";
				return false;
			}

			Elements.click(Global.driver, By.xpath("//div[1]/div[1]//a[text()='"+ID+"']"));

			if(sActionType.equalsIgnoreCase("")){
				return true;
			}

			if(sActionType.equalsIgnoreCase("Approve")){
				doCheckerActionType(checkerActionTypes.APPROVE);
				return true;
			}

			if(sActionType.equalsIgnoreCase("Reject")){
				doCheckerActionType(checkerActionTypes.REJECT);
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Rejected - Comments By QA");
				Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				return bStatus;
			}

			if(sActionType.equalsIgnoreCase("Return")){
				doCheckerActionType(checkerActionTypes.RETURN);
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Returned - Comments By QA");
				Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				return bStatus;
			}

			return true; 
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean performOperationsOnTableWithTransactionIDAndMasterType(dashboardMainDropdownToSelect menu, dashboardSubDropdownToSelect subMenu, String ID, String sMasterType, String sActionType){
		try{

			for (int i = 0; i < 8; i++) {
				bStatus = searchValueinMastersAndTransactionTableWithTransactionID(menu,subMenu, ID);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;					
				}
				if(bStatus){
					break;
				}				
			}
			if (!bStatus) {
				Messages.errorMsg = " ERROR : The Expected values : "+ID+" is not visible in the serach filter after 10 iterations.";
				return false;
			}
			//div[@role='gridcell']//div[normalize-space(text())='+sMasterType']//..//parent::div[@role='row']//a[normalize-space(text())='"+ID+"']
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@role='gridcell']//div[normalize-space(text())='"+sMasterType+"']//..//parent::div[@role='row']//a[normalize-space(text())='"+ID+"']"), 5);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Record wasnt identified with Txn ID : "+ID+" ,and Master Type : "+sMasterType+" ] ";
				return false;
			}
			Elements.click(Global.driver, By.xpath("//div[@role='gridcell']//div[normalize-space(text())='"+sMasterType+"']//..//parent::div[@role='row']//a[normalize-space(text())='"+ID+"']"));

			if(sActionType.equalsIgnoreCase("")){
				return true;
			}

			if(sActionType.equalsIgnoreCase("Approve")){
				doCheckerActionType(checkerActionTypes.APPROVE);
				return true;
			}

			if(sActionType.equalsIgnoreCase("Reject")){
				doCheckerActionType(checkerActionTypes.REJECT);
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Rejected - Comments By QA");
				Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				return bStatus;
			}

			if(sActionType.equalsIgnoreCase("Return")){
				doCheckerActionType(checkerActionTypes.RETURN);
				Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
				Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Returned - Comments By QA");
				Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));
				bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
				return bStatus;
			}

			return true; 
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean searchInSearchPanel(String FieldName,String sValue,String sStatus,String sTableName,int time){
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSearchPanel, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Search panel for the module is not displayed.";
				return false;
			}

			if(!sStatus.equalsIgnoreCase("inactive")){
				bStatus = WaitUntilValueAvailableInDropDown(FieldName,sValue, time);
				if (!bStatus) {
					return false;
				}
			}

			bStatus = spinnerClick(Global.driver, Locators.ClientMaster.Label.lblSearchPanel);
			if (!bStatus) {
				return false;
			}

			bStatus = selectFromDropDownOfApplication("Status",sStatus);
			if (!bStatus) {
				return false;
			}

			bStatus = doOperationsOnMasterPage(masterOperationTypes.SEARCH);
			if (!bStatus) {
				return false;
			}

			String tableLocator = "//div[@id='contenttablejqxgridsearch"+sTableName+"']/div[1]";
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath(tableLocator),Constants.iSearchTableTimeout);
			if(!bStatus){
				Messages.errorMsg = "Table with search data wasn't displayed";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return true;
		}

	}

	public static boolean verifyRecordIsDisplayedInTheGridTable(String FieldName,String sValue,String sStatus,String sTableName,int time){
		bStatus = searchInSearchPanel(FieldName,sValue,sStatus,sTableName,time);
		if(!bStatus){
			return false;
		}

		int iColCount = getTheColumnHeaderCount(FieldName,sTableName);
		if(iColCount == 0){
			Messages.errorMsg = "Column header with name "+FieldName+" is not avaibale";
			return false;
		}
		time = 8;
		for (int i = 0; i < time; i++) {
			bStatus = enterTextAndVerifyinFilterBoxOfGridTable(iColCount,sValue,sTableName);
			if(!bStatus){
				doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				continue;
			}
			return true;
		}

		return false;
	}

	public static boolean modifyMasterDetails(String sFieldName,String sName,String sTableName,Map<String, Map<String, String>> ModifiedMasterDetails){
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel(sFieldName,sName,"active",sTableName, 15);
			if(!bStatus){
				return false;
			}

			Thread.sleep(2500);

			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-pencil']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Modify operation";
				return false;
			}

			bStatus = Wait.waitForAlert(Global.driver, 5);
			if(bStatus){
				Alerts.closeAlert(Global.driver);
				Messages.errorMsg = "Canot modify WorkFLow in progress";
				return false;
			}

			if(sFieldName.equalsIgnoreCase("Client Name")){
				bStatus = ClientAppFunctions.addNewClient(ModifiedMasterDetails.get("1"));
				if(!bStatus){
					return false;
				}
			}

			if(sFieldName.equalsIgnoreCase("Fund Family Name")){
				bStatus = FundFamilyAppFunctions.addNewFundfamily(ModifiedMasterDetails.get("1"));
				if(!bStatus){
					return false;
				}
			}

			if(sFieldName.equalsIgnoreCase("Series Name")){
				bStatus = SeriesAppFunctions.addNewSeries(ModifiedMasterDetails.get("1"));
				if(!bStatus){
					return false;
				}
			}

			if(sFieldName.equalsIgnoreCase("Legal Entity Name")){
				bStatus = LegalEntityAppFunctions.AddNewLegalEntity(ModifiedMasterDetails);
				if(!bStatus){
					return false;
				}
			}
			if(sFieldName.equalsIgnoreCase("Class Name")){
				bStatus = ClassAppFunctions.addNewClass(ModifiedMasterDetails);
				if(!bStatus){
					return false;
				}
			}

			if (sFieldName.equalsIgnoreCase("Investor Name")) {
				InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag = true;
				bStatus = InvestorMasterAppFunctions.createNewInvestor(ModifiedMasterDetails);
				if(!bStatus){
					return false;
				}
			}

			if (sFieldName.equalsIgnoreCase("Investor Holder Name")) {
				HolderMasterAppFunctions.isHolderGeneralDetailsModifyFlag = true;
				bStatus = HolderMasterAppFunctions.createNewHolder(ModifiedMasterDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sFieldName.equals("Joint Holder Name")){
				bStatus = JointHolderAppFunctions .addNewJointHolder(ModifiedMasterDetails.get("1"));
				if( !bStatus ){
					return false;
				}
			}			

			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyMasterDetails(String sFieldName,String sName,String sTableName,Map<String, Map<String, String>> verifyMasterDetails){
		try {
			bStatus = NewUICommonFunctions.searchInSearchPanel(sFieldName,sName,"active",sTableName, 15);
			if(!bStatus){
				return false;
			}

			Thread.sleep(2500);
			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-pencil']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Modify operation";
				return false;
			}

			bStatus = Wait.waitForAlert(Global.driver, 5);
			if(bStatus){
				Alerts.closeAlert(Global.driver);
				Messages.errorMsg = "Canot modify WorkFLow in progress";
				return false;
			}

			//Verify client details
			if(sFieldName.equalsIgnoreCase("Client Name")){
				bStatus = ClientAppFunctions.verifyClientDetailsInEditScreen(verifyMasterDetails.get("1"));
				if(!bStatus){
					return false;
				}
			}

			if(sFieldName.equalsIgnoreCase("Fund Family Name")){
				bStatus = FundFamilyAppFunctions.verifyFundFamilyDetailsInEditScreen(verifyMasterDetails.get("1"));
				if(!bStatus){
					return false;
				}
			}

			if(sFieldName.equalsIgnoreCase("Series Name")){
				bStatus = SeriesAppFunctions.verifySeriesDetailsInEditScreen(verifyMasterDetails.get("1"));
				if(!bStatus){
					return false;
				}
			}
			if(sFieldName.equalsIgnoreCase("Legal Entity Name")){
				bStatus = LegalEntityAppFunctions.verifyAllTabsInLegalEntityDetailsEditScreen(verifyMasterDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sFieldName.equalsIgnoreCase("Class Name")){
				bStatus = ClassAppFunctions.verifyAllTabsInClassDetailsEditScreen(verifyMasterDetails);
				if(!bStatus){
					return false;
				}
			}

			if (sFieldName.equalsIgnoreCase("Investor Name")) {				
				bStatus = InvestorMasterAppFunctions.verifyAllTabsOfInvestorMaster(verifyMasterDetails);
				if(!bStatus){
					return false;
				}
			}

			if (sFieldName.equalsIgnoreCase("Investor Holder Name")) {				
				bStatus = HolderMasterAppFunctions.verifyAllTabsOfHolderMaster(verifyMasterDetails);
				if(!bStatus){
					return false;
				}
			}

			if(sFieldName.equalsIgnoreCase("Joint Holder Name")){
				bStatus = JointHolderAppFunctions .verifyJointHolderDetailsInEditScreen(verifyMasterDetails.get("1"));
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

	public static boolean deactivateMaster(String sFieldName,String sName,String sTableName){
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel(sFieldName , sName, "active",sTableName,10);
			if(!bStatus){
				return false;
			}
			Thread.sleep(2500);
			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-ban']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Deactivate operation";
				return false;
			}
			bStatus = Wait.waitForAlert(Global.driver, 5);
			if(bStatus){
				Alerts.closeAlert(Global.driver);
				Messages.errorMsg = "Canot modify WorkFLow in progress";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg = sName+" cannot be deactivated";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean activateMaster(String sFieldName,String sName,String sTableName){
		try{
			bStatus = verifyRecordIsDisplayedInTheGridTable(sFieldName, sName, "inactive", sTableName, 15);
			if(!bStatus){
				return false;
			}
			Thread.sleep(2500);
			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-check-circle-o']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on activate operation";
				return false;
			}

			bStatus = Wait.waitForAlert(Global.driver, 5);
			if(bStatus){
				Alerts.closeAlert(Global.driver);
				Messages.errorMsg = "Canot modify WorkFLow in progress";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg =sName+" cannot be activated";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static int getTheColumnHeaderCount(String colHeaderName,String sTableName){
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='columntablejqxgridsearch"+sTableName+"']"), 15);
			if (!bStatus) {
				Messages.errorMsg = "[ LOADING ISSUE : Grid table wasn't displayed even after 15s]";
			}
			int colCount = Elements.getXpathCount(Global.driver, By.xpath("//div[@id='columntablejqxgridsearch"+sTableName+"']/div"));
			for (int i = 1; i <= colCount; i++) {
				String colTempName = Elements.getText(Global.driver,By.xpath("//div[@id='columntablejqxgridsearch"+sTableName+"']/div["+i+"]/div/div/span"));
				if(colTempName.equalsIgnoreCase(colHeaderName)){
					return i;
				}
			}
			return 0;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	public static boolean enterTextAndVerifyinFilterBoxOfGridTable(int colIndex,String sValue,String sTableName){
		try{

			spinnerClick(Global.driver, Locators.ClientMaster.Label.lblSearchPanel);

			bStatus = Elements.enterText(Global.driver,By.xpath("//div[@id='filterrow.jqxgridsearch"+sTableName+"']/div/div["+colIndex+"]//input"), sValue);
			if(!bStatus){
				Messages.errorMsg = sValue+": text cannot be entered in the field";
				return false;
			}
			TimeUnit.SECONDS.sleep(2);
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='contenttablejqxgridsearch"+sTableName+"']/div/div["+colIndex+"]/div[normalize-space(text())=\""+sValue+"\"]"),5);
			if(!bStatus){
				Messages.errorMsg = sValue+" is not displayed in the grid table after filtering";
				return false;
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean WaitUntilValueAvailableInDropDown(String sFieldName,String sValue,int iTime){
		try {
			for (int i = 0; i < iTime; i++) {
				bStatus = selectFromDropDownOfApplication(sFieldName, sValue);
				if(!bStatus){
					refreshThePage();
					continue;
				}
				else{
					return true;
				}
			}
			Messages.errorMsg = Messages.errorMsg+" Even after waiting "+iTime;
			return false;
		} 
		catch (Exception e) {
			return false;
		}

	}

	public static boolean verifyTextInDropDown(String LabelName,String sExpectedText){
		try{
			String xpath = "//label[text()='Label' or normalize-space(text())='Label']/following-sibling::div[1]//span[normalize-space()=\"sExpectedText\" or text()=\"sExpectedText\" or normalize-space(.)=\"sExpectedText\"]";
			String textXpath = "//label[text()='Label' or normalize-space(text())='Label']/following-sibling::div[1]//span[contains(@id,'select2-chosen')]";
			xpath = xpath.replace("Label", LabelName).replace("sExpectedText", sExpectedText);
			textXpath = textXpath.replace("Label", LabelName).replace("sExpectedText", sExpectedText);
			bStatus = Verify.verifyElementPresent(Global.driver, By.xpath(xpath));
			if(!bStatus){
				String sActualvalue = Elements.getText(Global.driver, By.xpath(textXpath));
				Messages.errorMsg = sActualvalue+" is actual value for Label: "+LabelName+" which is not matching with Expected value: "+sExpectedText;
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyTextInTextBox(String LabelName,String sExpectedText){
		try{
			String xpath = "//label[text()='Label' or normalize-space(text())='Label']/..//input[@value=\"sExpectedText\"]";
			String textXpath = "//label[text()='Label' or normalize-space(text())='Label']//..//input[1]";
			xpath = xpath.replace("Label", LabelName).replace("sExpectedText", sExpectedText);
			textXpath = textXpath.replace("Label", LabelName).replace("sExpectedText", sExpectedText);
			bStatus = Verify.verifyElementVisible(Global.driver, By.xpath(xpath));
			//bStatus = false;
			if(!bStatus){
				String sActualvalue = Elements.getElementAttribute(Global.driver, By.xpath(textXpath),"value");
				Messages.errorMsg = "'"+sActualvalue+"' is actual value for Label: "+LabelName+" which is not matching with Expected value: "+sExpectedText;
				return false;
			}
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifySelectedRadioButton(String LabelName,String sExpectedText){
		try{
			String xpath = "//label[text()='Label' or normalize-space(text())='Label']/following-sibling::div/div//label[normalize-space()='sExpectedText' or text()='sExpectedText' or normalize-space(.)='sExpectedText']//span[@class='checked']";
			xpath = xpath.replace("Label", LabelName).replace("sExpectedText", sExpectedText);
			bStatus = Verify.verifyElementVisible(Global.driver, By.xpath(xpath));
			if(!bStatus){
				Messages.errorMsg = "Expected Value is not checked for Label: "+LabelName;
				return false;
			}
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyChecked(WebDriver wDriver, By objLocator) {
		try{
			bStatus = wDriver.findElement(objLocator).isSelected();
			if (bStatus) {
				return true;
			}
			//Messages.errorMsg = objLocator + " is not selected";
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean selectNumberOfRecordsPerPageInSearchGrid(String sNoOfRecordsPerPage, int iTimeOut){
		try{
			bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//b[text()='Search Result ']/../div/a"));
			if(!bStatus){
				Messages.errorMsg = "Selecting the Number of records dropdown is not visible in dropdown";
				return false;
			}

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//b[text()='Search Result ']/../div/a"));
			Wait.waitForElementVisibility(Global.driver,  By.xpath("//ul//li//div[contains(text(),'"+sNoOfRecordsPerPage+"')]"), 15);
			bStatus = Elements.click(Global.driver, By.xpath("//ul//li//div[contains(text(),'"+sNoOfRecordsPerPage+"')]"));
			if (!bStatus) {
				Messages.errorMsg = " Unable to select the All Option in search table to avoid details grid paging";
			}
			Thread.sleep(iTimeOut);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Map<String, String> cloneExisitngMapDetailsWithNewMap(Map<String, String> mapClonedDetails,Map<String, String> mapOverideDetails) {
		Map<String, String> finalMap = new HashMap<String, String>();
		for (Entry<String,String> test : mapClonedDetails.entrySet()) {

			String KeyValue = test.getKey();
			if(mapOverideDetails.containsKey(KeyValue)){
				finalMap.put(KeyValue, mapOverideDetails.get(KeyValue));
			}
			else{
				finalMap.put(KeyValue, test.getValue());
			}
		}

		for (Entry<String,String> test : mapOverideDetails.entrySet()) {

			String KeyValue = test.getKey();
			if(!mapClonedDetails.containsKey(KeyValue)){
				finalMap.put(KeyValue, test.getValue());
			}
		}
		return finalMap;
	}

	public static boolean doSubOperationsUnderAddNewMasterType(String sMasterType, String sOperation){
		try {
			scrollToView(By.xpath("//div[@class='page-bar']"));
			if(sOperation.equalsIgnoreCase("Approve"))
			{
				bStatus = Elements.click(Global.driver, Locators.FundFamilyMaster.Button.btnApprove);
				if(!bStatus){
					Messages.errorMsg = "Cannot click on Approve button";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.FundFamilyMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
				if(!bStatus)
				{
					Messages.errorMsg = sMasterType+" is Not Approved After click on Save Button";
					return false;
				}
				return true;
			}

			if(sOperation.equalsIgnoreCase("Save"))
			{
				bStatus = Elements.click(Global.driver, Locators.FundFamilyMaster.Button.btnSave);
				if(!bStatus){
					for (int i = 0; i < 3; i++) {
						bStatus = NewUICommonFunctions.jsClick(Locators.FundFamilyMaster.Button.btnSave);
						if (!bStatus) {
							TimeUnit.SECONDS.sleep(2);
							continue;
						}
						else {
							break;
						}
					}
					if (!bStatus) {
						Messages.errorMsg = "Cannot click on Save button/ Success message is not displayed";
						return false;
					}
				}
				if(bTradeSubscription || isBankMasterBeingCreatedFromTrade){
					return true;
				}
				waitUntilSpinnerGoInvisible(1, 15);
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.FundFamilyMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
				if(!bStatus)
				{
					Messages.errorMsg = sMasterType+" is Not Saved After click on Save Button";
					return false;
				}
				return true;
			}

			if(sOperation.equalsIgnoreCase("Save As Draft"))
			{
				bStatus = Elements.click(Global.driver,Locators.FundFamilyMaster.Button.btnSaveAsDraft);
				if(!bStatus){
					Messages.errorMsg = "Cannot click on Save As Draft button";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.FundFamilyMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
				if(!bStatus)
				{
					Messages.errorMsg = sMasterType+" is Not Saved After click on Save Draft Button";
					return false;
				}
				return true;
			}

			if(sOperation.equalsIgnoreCase("Cancel"))
			{
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver,Locators.FundFamilyMaster.Button.btnCancel);
				if(!bStatus){
					bStatus = Elements.click(Global.driver,By.xpath("//a[normalize-space()='Cancel']"));
					if(!bStatus){
						Messages.errorMsg = sMasterType+" is not Canceled After click on Cancel button";
						return false;
					}
				}
				waitUntilSpinnerGoInvisible(1, 15);

				return true;
			}
			return false;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	public static boolean sortTableByColumnName(String tableLocatorId,String ColumnName,String sortType){
		try{

			if(sortType.equalsIgnoreCase("ascending")){
				Elements.click(Global.driver,By.xpath("//table[@id='"+tableLocatorId+"']/thead//th[normalize-space(text())='"+ColumnName+"']"));
			}

			if(sortType.equalsIgnoreCase("descending")){

				Elements.click(Global.driver,By.xpath("//table[@id='"+tableLocatorId+"']/thead//th[normalize-space(text())='"+ColumnName+"']"));
				Elements.click(Global.driver,By.xpath("//table[@id='"+tableLocatorId+"']/thead//th[normalize-space(text())='"+ColumnName+"']"));
			}
			return false;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static void scrollbarKeyDown(By objElementForScrollOnTo){
		try {
			Actions clicker = new Actions(Global.driver);
			clicker.sendKeys(Keys.PAGE_DOWN);
			clicker.perform();

			Thread.sleep(3000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/*	public static boolean searchItemsinPanelbasedonIDGenerated(String GeneratedID,String searchLabel,String labelvalue,String Status,int iTimeout){
		try{

			bStatus = searchInSearchPanel(searchLabel, labelvalue, Status, 5);
			if(!bStatus){
				return false;
			}

			boolean bSearch1 = false;
			boolean bSearch2 = false;

			for (int i = 0; i < iTimeout; i++) {
				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
				if (!bStatus) {
					return false;
				}

				//wait until table is visible
				NewUICommonFunctions.isSerachTableDisplayed(Locators.CommonLocators.MasterSearchTable.tableMasterSearch);
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.ClientMaster.Label.lblSearchPanel);
				if (!bStatus) {
					return false;
				}

				//verify element is present is 
				bSearch1 = Verify.verifyElementVisible(Global.driver, By.xpath("//td[text()='"+GeneratedID+"']/following-sibling::td[normalize-space(text())='"+labelvalue+"']"));
				if(!bSearch1){
					bSearch2 = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+GeneratedID+"']/../following-sibling::td[normalize-space(text())='"+labelvalue+"']"));
					if(!bSearch2){						
						continue;
					}
					break;
				}
				break;
			}

			if(!bSearch1 && !bSearch2){
				Messages.errorMsg = GeneratedID+" is not available in the search table";
				return false;
			}
			return true;
		}
		catch(Exception e){
			Messages.errorMsg = e.getMessage();
			e.printStackTrace();
			return false;
		}
	}
	 */
	public static String getIDFromSuccessMessage() {
		try{
			String id = Elements.getText(Global.driver, By.xpath("//div[@class='alert alert-success']"));

			id = id.toUpperCase();
			if(!id.contains("ID")){
				return null;
			}
			if(id.split("ID").length < 2){
				return "None";
			}
			id = id.split("ID")[1].trim();
			return id;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static String getOrderIDFromSuccessMessage() {
		try{
			String id = Elements.getText(Global.driver, By.xpath("//div[@class='alert alert-success']"));

			id = id.toUpperCase();
			if(id.split("ORDER ID").length < 2){
				Messages.errorMsg = "Order Id is Not Available";
			}
			id = id.split("ORDER ID")[1].trim();
			return id;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static boolean selectTheTab(String stabName){
		try{
			bStatus = Elements.click(Global.driver, By.xpath("//a[normalize-space(text())='"+stabName+"']"));
			if(!bStatus){
				Messages.errorMsg = stabName+" tab is not selected";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean jsClick(By MYelement){
		try {
			WebElement element = Global.driver.findElement(MYelement);
			JavascriptExecutor executor = (JavascriptExecutor)Global.driver;
			executor.executeScript("arguments[0].click();", element);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Messages.errorMsg = "Java Script executor failed";
			e.printStackTrace();
			return false;
		}

	}

	public static boolean isTheNumbersMatchingInFloatingPoint(String fActual, String fExpected)
	{
		try {
			int isEqual = Float.compare(Float.parseFloat(fActual), Float.parseFloat(fExpected));
			if (isEqual == 0 ) {
				return true;				
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addNewFormulasForManagementOrIncentiveFee(Map<String, String> newFormulaDetailsMap, String sOperationType){
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//p[contains(text(),'Formula')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Rule computation page is not displayed";
				return false;
			}


			Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'callClear')]"));
			if(newFormulaDetailsMap.get("FeeType")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Type",newFormulaDetailsMap.get("FeeType"));
				if(!bStatus)
					return false;
			}

			if(newFormulaDetailsMap.get("FeeRuleName")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='feeMethod']"),newFormulaDetailsMap.get("FeeRuleName"));
				if(!bStatus){
					return false;
				}
			}
			if(newFormulaDetailsMap.get("Rule")!=null){
				bStatus = NewUICommonFunctions.createNewRule(newFormulaDetailsMap.get("Rule"));
				if(!bStatus){
					return false;
				}
			}
			if(sOperationType != null){
				if(sOperationType.equalsIgnoreCase("Save")){
					bStatus = Elements.clickButton(Global.driver, Locators.FundFamilyMaster.Button.btnSave);
					if(!bStatus){
						Messages.errorMsg = "Failed to click Save Button ";
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ManagementFee.TextBox.txtEffectiveStartDate, 5);
					if(!bStatus){
						Messages.errorMsg = "Unable to create a New Formula";
						return false;
					}
				}
				if(sOperationType.equalsIgnoreCase("Cancel")){
					bStatus = Elements.clickButton(Global.driver, Locators.FundFamilyMaster.Button.btnCancel);	
					if (!bStatus) {
						Messages.errorMsg = "Failed to click Cancel Button ";
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ManagementFee.TextBox.txtEffectiveStartDate, 5);
					if(!bStatus){
						Messages.errorMsg = "Unable to create a New Formula";
						return false;
					}
				}							
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean addManagementOrIncentiveFeeChargesForSlabAndTier(Map<String ,String> mapDetails, String sAddMoreSlabInFeeType){
		try {
			LegalEntityAppFunctions.removeTransactionCharges("tierSlabOption");
			String sAddLocator = "";
			if (sAddMoreSlabInFeeType.equalsIgnoreCase("Management Fee")) {
				sAddLocator = "addFeeCalculationSlab";
			}
			if (sAddMoreSlabInFeeType.equalsIgnoreCase("Incentive Fee")) {
				sAddLocator = "addSlabDetails";
			}			
			String sFromAmountLocator = "feeSlabDetailsReplaceIndexValue.amountFrom";
			String sAmountToLocator = "feeSlabDetailsReplaceIndexValue.amountTo";
			String sChargesLocator = "feeSlabDetailsReplaceIndexValue.slabPercentage";	

			By objAddMore = By.xpath("//button[contains(@onclick,'"+sAddLocator+"')]");

			String sAmountFrom, sAmountTo, sCharges;
			List<String> aAmountFrom = null, aAmountTo = null, aCharges = null;
			if (mapDetails.get("Amount From") != null) {
				sAmountFrom = mapDetails.get("Amount From");
				aAmountFrom = Arrays.asList(sAmountFrom.split(","));
			}
			if (mapDetails.get("Amount To") != null) {
				sAmountTo = mapDetails.get("Amount To");
				aAmountTo = Arrays.asList(sAmountTo.split(","));
			}
			if (mapDetails.get("Percentage") != null) {
				sCharges = mapDetails.get("Percentage");
				aCharges = Arrays.asList(sCharges.split(","));
			}		

			for(int i=0;i<aAmountFrom.size();i++){
				if(mapDetails.get("Amount From") != null && !aAmountFrom.get(i).equalsIgnoreCase("none")){
					String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = Elements.enterText(Global.driver, By.id(sObjAmountFrom), aAmountFrom.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Amount From Not Entered in "+i+" index";
						return false;
					}
				}
				if(mapDetails.get("Amount To") != null && !aAmountTo.get(i).equalsIgnoreCase("none")){
					String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = Elements.enterText(Global.driver, By.id(sObjAmountTo), aAmountTo.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Amount To Not Entered in "+i+" index";
						return false;
					}
				}
				if(mapDetails.get("Percentage") != null && !aCharges.get(i).equalsIgnoreCase("none")){
					String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = Elements.enterText(Global.driver, By.id(sObjCharges), aCharges.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Charges Not Entered in "+i+" index";
						return false;
					}
				}	
				if(i<aAmountFrom.size()-1){
					bStatus = Elements.click(Global.driver, objAddMore );
					if(!bStatus){
						Messages.errorMsg = "Add More Button Not Clicked in Tab : "+sAddMoreSlabInFeeType+"";
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

	public static boolean selectFromDropDownOfApplication(String sDropDownValue,By dropDownArrow){
		try{
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, dropDownArrow);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Search dropdown Wasn't Clickable ]\n";
				return false;
			}

			String seletDropDownValueLocator = Locators.ClientMaster.Dropdown.ddlSelectValue.replaceAll("selectDropDownValue",sDropDownValue);
			By objDroDownOption = By.xpath(seletDropDownValueLocator);

			bStatus = Wait.waitForElementVisibility(Global.driver,objDroDownOption,Constants.iddTimeout);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Dropdown list item : '"+sDropDownValue+"' ,is not visible ]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, objDroDownOption);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : value : '"+sDropDownValue+"' Wasn't selected in dropdown ]\n";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean selectMultipleValuesFromDropDown(String LabelName,String sCommaSepValus) {
		try{

			List<String> valuesToSelect = Arrays.asList(sCommaSepValus.split(",")); 	

			for (int index = 0; index < valuesToSelect.size(); index++) {
				bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'"+LabelName+"')]/following-sibling::div[1]//li[contains(@class,'search-field')]"));
				if(!bStatus)
				{
					Messages.errorMsg = "Failed to click the multiselection";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//ul[@class='select2-results']//li//div[normalize-space()=\""+valuesToSelect.get(index)+"\"]"), Constants.iDropdown);
				if(!bStatus)
				{
					Messages.errorMsg = "Holiday Calendar wasn't visible in the multiselection list : " +"//select[@id='leHolidayMapList']//option[contains(text(),'"+valuesToSelect.get(index)+"')]";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//ul[@class='select2-results']//li//div[normalize-space()=\""+valuesToSelect.get(index)+"\"]"));
				if(!bStatus)
				{
					Messages.errorMsg = "Holiday Calendar is not clicked in the multiselection list : " +"//select[@id='leHolidayMapList']//option[contains(text(),'"+valuesToSelect.get(index)+"')]";
					return false;
				}
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();return false;
		}

	}

	public static boolean selectDropDownOfApplication(String sDropDownValue,By dropDownArrow, String sLocator)
	{
		try{
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, dropDownArrow);
			if(!bStatus){
				Messages.errorMsg = "Search dropdown Wasn't Clickable";
				return false;
			}

			String seletDropDownValueLocator = sLocator.replace("selectDropDownValue",sDropDownValue);
			By objDroDownOption = By.xpath(seletDropDownValueLocator);

			bStatus = Wait.waitForElementVisibility(Global.driver,objDroDownOption,Constants.iddTimeout);
			if(!bStatus){
				Messages.errorMsg = "Dropdown list is not visible";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, objDroDownOption);
			if(!bStatus){
				Messages.errorMsg = sDropDownValue+ " Wasn't selected in dropdown";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//Adding Slab or Tiered Rate Details for Transaction Charges 
	public static boolean addTransactionChargesSlaborTieredAmountDetails(Map<String ,String> mapDetails,String sTabNameLowerCase ,String sAddMoreTabName){
		try {
			String sFromAmountLocator = ""+sTabNameLowerCase+"ChargesList0.chargeDetailsListReplaceIndexValue.fromAmount";
			String sAmountToLocator = ""+sTabNameLowerCase+"ChargesList0.chargeDetailsListReplaceIndexValue.toAmount";
			String sChargesLocator = ""+sTabNameLowerCase+"ChargesList0.chargeDetailsListReplaceIndexValue.rate";
			By objAddMore = By.xpath("//em[@class='fa fa-plus']//parent::button[contains(@onclick,'"+sAddMoreTabName+"ChargeDetailsList')]");
			String sAmountFrom = mapDetails.get("Amount From");
			List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
			String sAmountTo = mapDetails.get("Amount To");
			List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
			String sCharges = mapDetails.get("Charges");
			List<String> aCharges = Arrays.asList(sCharges.split(","));
			for(int i=0;i<aAmountFrom.size();i++){
				if(!aAmountFrom.get(i).equalsIgnoreCase("none")){
					String sObjAmountFrom = sFromAmountLocator.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = Elements.enterText(Global.driver, By.id(sObjAmountFrom), aAmountFrom.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Amount From Not Entered in "+i+" index";
						return false;
					}
				}
				if(!aAmountTo.get(i).equalsIgnoreCase("none")){
					String sObjAmountTo = sAmountToLocator.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = Elements.enterText(Global.driver, By.id(sObjAmountTo), aAmountTo.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Amount To Not Entered in "+i+" index";
						return false;
					}
				}
				if(!aCharges.get(i).equalsIgnoreCase("none")){
					String sObjCharges = sChargesLocator.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = Elements.enterText(Global.driver, By.id(sObjCharges), aCharges.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Amount To Not Entered in "+i+" index";
						return false;
					}
				}	
				if(i<aAmountFrom.size()-1){
					bStatus = Elements.click(Global.driver, objAddMore );
					if(!bStatus){
						Messages.errorMsg = "Add More Button Not Clicked in Tab "+sAddMoreTabName+"";
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

	public static boolean NavigateToTab(String tabName)
	{
		//Navigate To Tab function created by Gaffur
		//Add String in Locators.CommonLocators.BreadCrumbs(public static String NavigateTo = "//ul[@class='nav nav-tabs']//a[contains(text(),'tabNameValue')]";)
		try{
			String sTabtoSelect = Locators.CommonLocators.BreadCrumbs.NavigateTo.replace("tabNameValue", tabName);
			By objTabtoSelect = By.xpath(sTabtoSelect);
			bStatus = Elements.click(Global.driver, objTabtoSelect);
			if(!bStatus)
			{
				Messages.errorMsg="Not Navigated to "+tabName;
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}

	public static boolean addLockupRateMethodDetails(Map<String, String> mapClassRedemptionDetails) {
		try {
			String sAmountFrom = mapClassRedemptionDetails.get("LockupSlabFrom");
			List<String> aAmountFrom = Arrays.asList(sAmountFrom.split(","));
			String sAmountTo = mapClassRedemptionDetails.get("LockupSlabTo");
			List<String> aAmountTo = Arrays.asList(sAmountTo.split(","));
			String sCharges = mapClassRedemptionDetails.get("LockupSlabCharges");
			List<String> aCharges = Arrays.asList(sCharges.split(","));
			int listSize = Math.max(aAmountFrom.size(), Math.max(aAmountTo.size(), aCharges.size()));
			for(int i=0;i<listSize;i++){
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
				if(i<listSize-1){
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

	public static boolean addPartialRateMethodDetails(Map<String, String> mapClassRedemptionDetails) {
		try {
			String sPartialAmountFrom = mapClassRedemptionDetails.get("PartialRedChargeSlabOrTierFrom");
			List<String> aAmountFrom = Arrays.asList(sPartialAmountFrom.split(","));
			String sPaartialAmountTo = mapClassRedemptionDetails.get("PartialRedChargeSlabOrTierTo");
			List<String> aAmountTo = Arrays.asList(sPaartialAmountTo.split(","));
			String sCharges = mapClassRedemptionDetails.get("PartialRedSlabOrTierCharge");
			List<String> aCharges = Arrays.asList(sCharges.split(","));
			int listSize = Math.max(aAmountFrom.size(), Math.max(aAmountTo.size(), aCharges.size()));
			for(int i=0;i<listSize;i++){
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
				if(i<listSize-1){
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

	//Adding More Trading Restrictions in Other Details Tab for Legal Entity or Class
	public static boolean addTradingRestrictions(Map<String,String> mapOtherDetails){
		try{
			String sTransactionType = mapOtherDetails.get("Transaction Type");
			List<String> aTransactionType= Arrays.asList(sTransactionType.split(","));
			String sComments = mapOtherDetails.get("Comments");
			String[] aComments= sComments.split(",");
			/*String sGatingProvision = mapOtherDetails.get("Gating Provision");
			List<String> aGatingProvision= Arrays.asList(sGatingProvision.split(","));
			String sClosed = mapOtherDetails.get("Closed");
			List<String> aClosed= Arrays.asList(sClosed.split(","));*/
			for(int i=0;i<aTransactionType.size();i++){
				if(!aTransactionType.get(i).equalsIgnoreCase("None")){
					String sReplaceTransactionType = Locators.LegalEntityMaster.OtherDetails.DropDown.sTransactionType.replace("ReplaceValue", String.valueOf(i));
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aTransactionType.get(i).toString(), By.xpath(sReplaceTransactionType));
					if(!bStatus){
						Messages.errorMsg = "Transaction Type dropdown value Not Selected";
						return false;
					}
				}			
				if(mapOtherDetails.get("Date From")!=null && mapOtherDetails.get("To Date")!=null){
					//Add More Dates Function
					bStatus = NewUICommonFunctions.addMoreDates(mapOtherDetails, i);
					if(!bStatus){
						Messages.errorMsg = "Trading Restriction Dates Not Entered";
						return false;
					}
				}	
				//Entering Comments in Trading Restrictions CommentBox
				if(mapOtherDetails.get("Comments")!=null){
					if(!aComments[i].equalsIgnoreCase("None")){
						String sCommentLocator = Locators.LegalEntityMaster.OtherDetails.TextBox.trComments.replace("ReplaceComments", String.valueOf(i));
						bStatus = Elements.enterText(Global.driver, By.id(sCommentLocator), aComments[i].toString());
						if(!bStatus){
							Messages.errorMsg = "Trading Restriction Comments  Not Entered";
							return false;
						}
					}
				}
				//Clicking the Add More Trading Restriction Button
				if(i<aTransactionType.size()-1){
					bStatus = Elements.click(Global.driver, Locators.LegalEntityMaster.OtherDetails.Buttons.addMoreTradingRestrictions);
					if(!bStatus){
						Messages.errorMsg = "Add More Button Not Clicked ";
						return false;
					}
				}
				//Selecting the Gating Provision Radio Button
				/*if(mapOtherDetails.get("Gating Provision")!=null){
					if(!aGatingProvision.get(i).equalsIgnoreCase("None")){
						if(aGatingProvision.get(i).toString().equalsIgnoreCase("Yes")){
							String sGPYesLocator = Locators.LegalEntityMaster.OtherDetails.RadioButton.tRgatingProvisionYes.replace("ReplaceGatingProvisionYes", String.valueOf(i));
							bStatus = Elements.click(Global.driver, By.id(sGPYesLocator));
							if(!bStatus){
								Messages.errorMsg = "Gating Provision Yes Radio button Not Selected";
								return false;
							}
						}
						if(aGatingProvision.get(i).toString().equalsIgnoreCase("No")){
							String sGPNoLocator = Locators.LegalEntityMaster.OtherDetails.RadioButton.tRgatingProvisionNo.replace("ReplaceGatingProvisionNo", String.valueOf(i));
							bStatus = Elements.click(Global.driver, By.id(sGPNoLocator));
							if(!bStatus){
								Messages.errorMsg = "Gating Provision No Radio button Not Selected";
								return false;
							}
						}
					}			
				}
				//Selecting the Closed Radio Button
				if(mapOtherDetails.get("Closed")!=null){
					if(!aClosed.get(i).equalsIgnoreCase("None")){
						if(aClosed.get(i).toString().equalsIgnoreCase("Yes")){
							String sClosedYes = Locators.LegalEntityMaster.OtherDetails.RadioButton.tRClosedYes.replace("ReplaceClosedYes", String.valueOf(i));
							bStatus = Elements.click(Global.driver, By.id(sClosedYes));
							if(!bStatus){
								Messages.errorMsg = "Closed Yes Radio button Not Selected";
								return false;
							}
						}
						if(aClosed.get(i).toString().equalsIgnoreCase("No")){
							String sClosedNo = Locators.LegalEntityMaster.OtherDetails.RadioButton.tRClosedNo.replace("ReplaceClosedNo", String.valueOf(i));
							bStatus = Elements.click(Global.driver, By.id(sClosedNo));
							if(!bStatus){
								Messages.errorMsg = "Closed No Radio button Not Selected";
								return false;
							}
						}
					}

				}*/
			}
			return true;
		}
		catch(Exception e){

			e.printStackTrace();
			return false;
		}
	}
	//Adding More Dates for Trading Restrictions in OtherDetails Tab in Legal Entity or Class
	public static boolean addMoreDates(Map<String,String> mapOtherDetails,int i){
		try {
			String sFromDate = mapOtherDetails.get("Date From");
			List<String> aFromDate= Arrays.asList(sFromDate.split(","));
			String sToDate = mapOtherDetails.get("To Date");
			List<String> aToDate= Arrays.asList(sToDate.split(","));
			List<String> arrIndidualFromDate = Arrays.asList(aFromDate.get(i).split(":"));
			List<String> arrIndidualToDate = Arrays.asList(aToDate.get(i).split(":"));
			for(int k=0;k<arrIndidualFromDate.size();k++){
				if(!arrIndidualFromDate.get(k).equalsIgnoreCase("None")){
					NewUICommonFunctions.typeCharactersIntoTextBox(By.id("tradingRestrictionsList"+i+"fromDateRange"+k+""), arrIndidualFromDate.get(k));
					//Elements.enterText(Global.driver, By.id("tradingRestrictionsList"+i+"fromDateRange"+k+""), arrIndidualFromDate.get(k));
					Elements.click(Global.driver, By.xpath("//h4"));
				}
				if(!arrIndidualToDate.get(k).equalsIgnoreCase("None")){
					NewUICommonFunctions.typeCharactersIntoTextBox(By.id("tradingRestrictionsList"+i+"toDateRange"+k+""), arrIndidualToDate.get(k));
					//Elements.enterText(Global.driver, By.id("tradingRestrictionsList"+i+"toDateRange"+k+""), arrIndidualToDate.get(k));
					Elements.click(Global.driver, By.xpath("//h4"));
				}				
				if(k<arrIndidualFromDate.size()-1){
					String objAddMoreDate = "//div[@id='dateRangeAddMore_"+i+"']//button[@id='addMoreDates']";
					Elements.click(Global.driver, By.xpath(objAddMoreDate));
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	public static boolean WaitUntilValueAvailableInDropDown(String sDropDownValue,By dropDownArrow,int iTime){
		try {
			for (int i = 0; i < iTime; i++) {
				bStatus = selectFromDropDownOfApplication(sDropDownValue, dropDownArrow);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;
				}
				else{
					return true;
				}
			}
			return false;
		} 
		catch (Exception e) {
			return false;
		}

	}


	public enum masterOperationTypes{
		SEARCH,CLEAR,ADDNEW;
	}


	public static boolean isSerachTableDisplayed(By ObjTableLocator){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, ObjTableLocator,Constants.iSearchTableTimeout);
			if(!bStatus){
				Messages.errorMsg = "Table with search data wasn't displayed";
				return false;
			}
			return bStatus;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean createNewRule(String sFormula){
		try{

			String[] arrFormulaValues = sFormula.split(",");
			Elements.click(Global.driver, By.xpath("//button[@onclick='callClear();']"));
			for (int i = 0; i < arrFormulaValues.length; i++) {
				String sValue = arrFormulaValues[i].trim();
				if(sValue.contains("[") && sValue.contains("]")){

					sValue = sValue.replace("[", "");
					sValue = sValue.replace("]", "");
					bStatus = selectValueFromAttributes(sValue);
					if(!bStatus)
						return false;
					continue;
				}
				String sKeyBoard = "//button[@onclick=concat('callOperator(',\"'\",'value',\"')\")]";
				sKeyBoard = sKeyBoard.replace("value", sValue);
				By objKeyboard = By.xpath(sKeyBoard);
				Elements.click(Global.driver,objKeyboard);

			}
			//bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Formula Setup",act);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean selectValueFromAttributes(String sValue){
		try{
			bStatus = selectFromDropDownOfApplication("Attributes", sValue);
			//bStatus=true;
			if(!bStatus){

				return false;
			}
			Elements.click(Global.driver, By.xpath("//i[@class='icon-plus']"));	
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	public static void performJavascriptExecutorToScrollOnToElement(By objElementForScrollOnTo){
		try {
			WebElement element = Global.driver.findElement(objElementForScrollOnTo);
			((JavascriptExecutor) Global.driver).executeScript("arguments[0].scrollIntoView(true);", element);
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean selectPortalinUserManagement(String sPortalname){
		try{
			//wait until page is displayed
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//li[contains(text(), 'User')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "User Management page is not visible";
				return false;
			}

			String ExpandLocator = "//span[normalize-space(text())='MenuName']/../following-sibling::div[1]/a";
			ExpandLocator = ExpandLocator.replace("MenuName", sPortalname);

			By xpathObjLoc = By.xpath(ExpandLocator);
			//next click on expand button of the portal selected
			bStatus = Elements.click(Global.driver, xpathObjLoc);
			if(!bStatus){
				Messages.errorMsg = sPortalname+" is not displayed in the page";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//li[text()='"+sPortalname+"']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = sPortalname+" is not visible on clicking on expand button";
				return false;
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doOperationOnUserManagementSubMasters(String id , String operationType){
		try {

			String buttonLocator = "//div[@id='"+id+"' and contains(@style , 'display: block')]//button[contains(normalize-space(),'"+operationType+"')]";
			NewUICommonFunctions.scrollToView(By.xpath(buttonLocator));
			if(operationType.equalsIgnoreCase("Save")){
				bStatus = Elements.click(Global.driver , By.xpath(buttonLocator));
				if(!bStatus){
					bStatus = NewUICommonFunctions.jsClick(By.xpath(buttonLocator));
					if(!bStatus){
						Messages.errorMsg = operationType+" Button click failed";
						return false;
					}
				}

				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//body[@class='page-header-fixed page-quick-sidebar-over-content page-sidebar-closed']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "After Saving Page is Not navigated to User Management Page after "+Constants.lTimeOut+" Seconds";
					return false;
				}

				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.FundFamilyMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
				if(!bStatus)
				{
					Messages.errorMsg = "Succesfull Message is Not visible After click on Save Button";
					return false;
				}

				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='alert alert-success']//button"));
				if(!bStatus){
					Messages.errorMsg = "Successfull Message Not Closed";
					return false;
				}
				return true;
			}

			if(operationType.equalsIgnoreCase("Add New")){
				bStatus = Elements.click(Global.driver , By.xpath(buttonLocator));
				if(!bStatus){
					bStatus = NewUICommonFunctions.jsClick(By.xpath(buttonLocator));
					if(!bStatus){
						Messages.errorMsg = operationType+" Button click failed";
						return false;
					}
					return true;
				}
				return true;
			}

			if(operationType.equalsIgnoreCase("Cancel")){
				bStatus = Elements.click(Global.driver, By.xpath(buttonLocator));
				if(!bStatus){
					Messages.errorMsg = operationType+" Button click failed";
					return false;
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	public static boolean activatePortal(String createdName , String portalName){
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createUserLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createRoleLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createGroupLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}

			String activateLocator = "//div[@id='create"+portalName+"Loader']//following-sibling::table/tbody//tr/td[normalize-space(text())=\""+createdName+"\"]//following-sibling::td";
			String disableValue = Elements.getElementAttribute(Global.driver, By.xpath(activateLocator+"//a[@class='btn btn-xs default tooltips'][3]"), "disabled");
			if(disableValue!=null){
				Messages.errorMsg = portalName+" Name "+createdName+" Already in active State";
				return false;
			}
			bStatus = NewUICommonFunctions.jsClick(By.xpath(activateLocator+"//a/em[@class='fa fa-check-circle-o']"));
			if(!bStatus){
				Messages.errorMsg = portalName+" , "+createdName+" Activate Button cannot be clicked";
				return false;
			}

			bStatus = Wait.waitForAlert(Global.driver, 5);
			if(bStatus){
				Alerts.closeAlert(Global.driver);
				Messages.errorMsg = "Canot modify WorkFLow in progress or Alert Present";
				return false;
			}

			bStatus = Wait.waitForElementPresence(Global.driver, Locators.FundFamilyMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus)
			{
				Messages.errorMsg = "Succesfull Message is Not visible After click on Activate Button";
				return false;
			}
			String activeStatus = "//div[@id='create"+portalName+"Loader']//following-sibling::table/tbody//tr/td[normalize-space(text())=\""+createdName+"\"]//following-sibling::td//a[@class='btn btn-xs green']";
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(activeStatus), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = portalName+" , "+createdName+" Active Status Not Changed to YES";
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deactivatePortal(String createdName , String portalName){
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createUserLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createRoleLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createGroupLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}

			String deActivateLocator = "//div[@id='create"+portalName+"Loader']//following-sibling::table/tbody//tr/td[normalize-space(text())=\""+createdName+"\"]//following-sibling::td";
			String disableValue = Elements.getElementAttribute(Global.driver, By.xpath(deActivateLocator+"//a[@class='btn btn-xs default tooltips'][4]"), "disabled");
			if(disableValue!=null){
				Messages.errorMsg = portalName+" Name "+createdName+" Already in Deactive State";
				return false;
			}
			bStatus = NewUICommonFunctions.jsClick(By.xpath(deActivateLocator+"//a/em[@class='fa fa-ban']"));
			if(!bStatus){
				Messages.errorMsg = portalName+" , "+createdName+" DeActivate Button cannot be clicked";
				return false;
			}

			bStatus = Wait.waitForAlert(Global.driver, 5);
			if(bStatus){
				Alerts.closeAlert(Global.driver);
				Messages.errorMsg = "Canot modify WorkFLow in progress or Alert Present";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.FundFamilyMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus)
			{
				Messages.errorMsg = "Succesfull Message is Not visible After click on DeActivate Button";
				return false;
			}

			//wait for Active column status change to NO
			String activeStatus = "//div[@id='create"+portalName+"Loader']//following-sibling::table/tbody//tr/td[normalize-space(text())=\""+createdName+"\"]//following-sibling::td//a[@class='label bg-grey-gallery']";
			//Global.driver.manage().
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(activeStatus), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = portalName+" , "+createdName+" Active Status Not Changed to NO";
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean SelectEditForCreatedUserManagementPortal(String createdName , String portalName) {
		// TODO Auto-generated method stub
		try {
			String editLocator = "//div[@id='create"+portalName+"Loader']//following-sibling::table/tbody//tr/td[normalize-space(text())=\""+createdName+"\"]//following-sibling::td//a/em[@class='fa fa-pencil']";
			Thread.sleep(2000);
			bStatus = NewUICommonFunctions.jsClick(By.xpath(editLocator));
			Thread.sleep(2000);
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
			if(!bStatus){
				Messages.errorMsg = createdName+" Edit Button failed to click";
				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}


	public static boolean modifyUserManagementPortal(Map<String , String> mapUserManagementPortalDetails , String createdPortalName,String portalName){
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createUserLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createRoleLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createGroupLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			Thread.sleep(3000);
			if(portalName.equalsIgnoreCase("Role")){

				//Click on Edit for the Created RoleCode
				bStatus = NewUICommonFunctions.SelectEditForCreatedUserManagementPortal(createdPortalName, portalName);
				if(!bStatus){
					return false;
				}
				Thread.sleep(2000);
				//modify Role Details
				bStatus = RoleAppFunctions.createaNewRole(mapUserManagementPortalDetails);
				if(!bStatus){
					return false;
				}

			}

			if(portalName.equalsIgnoreCase("Group")){

				//Click on Edit for the Created RoleCode
				bStatus = NewUICommonFunctions.SelectEditForCreatedUserManagementPortal(createdPortalName, portalName);
				if(!bStatus){
					return false;
				}
				Thread.sleep(2000);
				//modify Role Details
				bStatus = GroupAppFunctions.createAGroup(mapUserManagementPortalDetails);
				if(!bStatus){
					return false;
				}

			}

			if(portalName.equalsIgnoreCase("User")){

				bStatus = NewUICommonFunctions.SelectEditForCreatedUserManagementPortal(createdPortalName, portalName);
				if(!bStatus){
					return false;
				}
				Thread.sleep(2000);
				bStatus = UserAppFunctios.editCreatedUser(mapUserManagementPortalDetails);
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


	public static boolean verifyCreatedUserManagementPortal(Map<String ,String> userManagementPortalDetails, String createdPortalName , String portalName){
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createUserLoader' and contains(@style , 'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createRoleLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createGroupLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			if(portalName.equalsIgnoreCase("Role")){

				bStatus = NewUICommonFunctions.SelectEditForCreatedUserManagementPortal(createdPortalName , portalName);
				if(!bStatus){
					return false;
				}

				bStatus = RoleAppFunctions.verifyTheCreatedRole(userManagementPortalDetails);
				if(!bStatus){
					Messages.errorMsg = "Details are not matching with the Actual ERROR: "+Messages.errorMsg;
					return false;
				}

			}

			if(portalName.equalsIgnoreCase("Group")){

				bStatus = NewUICommonFunctions.SelectEditForCreatedUserManagementPortal(createdPortalName , portalName);
				if(!bStatus){
					return false;
				}

				bStatus = GroupAppFunctions.verifyTheCreatedGroup(userManagementPortalDetails);
				if(!bStatus){
					Messages.errorMsg = "Details are not matching with the Actual ERROR: "+Messages.errorMsg;
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


	public static boolean addNewUserManagementPortal(Map<String ,String> mapUserManamentPortal , String portalName){
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createUserLoader' and contains(@style , 'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createRoleLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='createGroupLoader' and contains(@style ,'display: none')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Page is not loaded after waiting for"+Constants.lTimeOut+" Seconds" ;
				return false;
			}
			Thread.sleep(3000);
			if(portalName.equalsIgnoreCase("Role")){

				bStatus = NewUICommonFunctions.selectPortalinUserManagement(portalName);
				if(!bStatus){
					return false;
				}

				//Click on Add New button for  Role
				bStatus = doOperationOnUserManagementSubMasters("roleButtonDiv", "Add New");
				if(!bStatus){
					Messages.errorMsg = "Cannot Click on Add New Button For Role";
					return false;
				}

				bStatus = RoleAppFunctions.createaNewRole(mapUserManamentPortal);
				if(!bStatus){
					return false;
				}

			}

			if(portalName.equalsIgnoreCase("Group")){

				bStatus = NewUICommonFunctions.selectPortalinUserManagement(portalName);
				if(!bStatus){
					return false;
				}

				//Click on Add New button for  Group
				bStatus = doOperationOnUserManagementSubMasters("groupButtonDiv", "Add New");
				if(!bStatus){
					Messages.errorMsg = "Cannot Click on Add New Button For Group";
					return false;
				}

				bStatus = GroupAppFunctions.createAGroup(mapUserManamentPortal);
				if(!bStatus){
					return false;
				}

			}

			if(portalName.equalsIgnoreCase("User")){

				bStatus = NewUICommonFunctions.selectPortalinUserManagement(portalName);
				if(!bStatus){
					return false;
				}

				//Click on Add New button for  User
				bStatus = doOperationOnUserManagementSubMasters("userButtonDiv", "Add New");
				if(!bStatus){
					Messages.errorMsg = "Cannot Click on Add New Button For User";
					return false;
				}

				bStatus = UserAppFunctios.editCreatedUser(mapUserManamentPortal);
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


	public static String jsGetElementAttribute(String byValue , String name){
		try {

			JavascriptExecutor js = (JavascriptExecutor) Global.driver;
			String strValue = js.executeScript("return document.getElementsBy"+byValue+"('"+name+"')[0].value").toString();
			return strValue;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String jsGetElementAttribute( String id){
		try {

			JavascriptExecutor js = (JavascriptExecutor) Global.driver;
			String strValue = js.executeScript("return document.getElementById('"+id+"').value").toString();
			return strValue;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static boolean scrollToView(By objLocator){
		try {

			WebElement element = Global.driver.findElement(objLocator);
			((JavascriptExecutor) Global.driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500); 
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean selectMultipleValuesInSelectBox(String sLabelName,String[] arrValues,String iLabelNameIndex){
		try{

			for (int i = 0; i < arrValues.length; i++) {
				/*	bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//select[@id='"+iLabelNameIndex+"']/option[@selected='selected' and normalize-space(text())='"+arrValues[i]+"']"), 3);
				if(bStatus){
					continue;
				}*/
				//String eleLocator = "//div[@id='leRoleListHtml']//label[normalize-space(text())='"+sLabelName+"']/following-sibling::div[1]//select[@id='"+iLabelNameIndex+"']/option[text()=\""+arrValues[i]+"\"]";
				String eleLocator = "//div[@id='leRoleListHtml']//label[normalize-space(text())='"+sLabelName+"']/following-sibling::select[@id='"+iLabelNameIndex+"']/option[text()=\""+arrValues[i]+"\"]";
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(eleLocator), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = sLabelName+" ,"+arrValues[i]+" is Not Visible";
					return false;
				}

				WebElement onElement = Elements.getWebElement(Global.driver,By.xpath(eleLocator));
				//WebElement onElement = Elements.getWebElement(Global.driver,By.xpath("//div[@id='leRoleListHtml']//label[normalize-space(text())='"+sLabelName+"']/following-sibling::div[1]//select[@id='"+iLabelNameIndex+"']/option[text()='"+arrValues[i]+"']"));
				new Actions(Global.driver).keyDown(Keys.CONTROL).click(onElement).keyDown(Keys.CONTROL).perform();
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean typeCharactersIntoTextBox(By objLocator, String sText){
		try {
			//Added by Muni
			Global.driver.findElement(objLocator).sendKeys("12");
			Global.driver.findElement(objLocator).clear();
			List<String> charStringsList = Arrays.asList(sText.split(""));
			for (String charString : charStringsList) {
				Global.driver.findElement(objLocator).sendKeys(charString.trim());				
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
		return true;
	}


	public static boolean doSubOperationsUnderAddNewMasterTypeAndNotCheckMessage(String sMasterType, String sOperation){
		try {
			if(sOperation.equalsIgnoreCase("Approve"))
			{
				bStatus = NewUICommonFunctions.jsClick(Locators.FundFamilyMaster.Button.btnApprove);
				if(!bStatus){
					Messages.errorMsg = "Cannot click on Approve button";
					return false;
				}
				return true;
			}

			if(sOperation.equalsIgnoreCase("Save"))
			{
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'submit')]"));
				if(!bStatus){
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'submit')]"));
					if (!bStatus) {
						Messages.errorMsg = "Cannot click on Save button";
						return false;
					}
				}
				return true;
			}

			if(sOperation.equalsIgnoreCase("Save As Draft"))
			{
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'save')]"));
				if(!bStatus){
					Messages.errorMsg = "Cannot click on Save As Draft button";
					return false;
				}
				return true;
			}

			if(sOperation.equalsIgnoreCase("Cancel"))
			{
				bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'cancel')]"));
				if(!bStatus){
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'cancel')]"));
					if(!bStatus){
						Messages.errorMsg = sMasterType+" is not Cancel After click on Cancel button";
						return false;
					}
				}
				return true;
			}
			return false;
		} 
		catch (Exception e) 
		{
			Messages.errorMsg = e.getMessage();
			e.printStackTrace();
			return false;
		}
	}


	public static boolean verifyValuesInSearchTableInAllMenus(String sTableName,String labelName ,String sNames){
		try{

			bStatus = doOperationsOnMasterPage(masterOperationTypes.SEARCH);
			if (!bStatus) {
				return false;
			}

			String tableLocator = "//div[@id='contenttablejqxgridsearch"+sTableName+"']/div";
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath(tableLocator),Constants.iSearchTableTimeout);
			if(!bStatus){
				Messages.errorMsg = "Table with search data wasn't displayed";
				return false;
			}

			int count = getTheColumnHeaderCount(labelName, sTableName);

			int NoOfSearchItems = Elements.getXpathCount(Global.driver, By.xpath(tableLocator));
			String values ="";
			for(int i=1;i<=NoOfSearchItems;i++){
				String getValues = Elements.getText(Global.driver, By.xpath(tableLocator+"["+i+"]//div["+count+"]"));
				if(getValues!=null && !getValues.equalsIgnoreCase("")){
					values = values+getValues+",";
				}

			}
			values = values.replaceAll(",$", "");
			if(values.split(",").length != sNames.split(",").length)
			{
				Messages.errorMsg = "Actual Values "+values+" are not matching with the Expected Values "+sNames+" in Table";
				return false;
			}
			else
			{
				List<String> expectedCount = Arrays.asList(sNames.split(","));
				for(int j=0;j<expectedCount.size();j++){
					if(!values.contains(expectedCount.get(j))){
						Messages.errorMsg = "Expected Value "+expectedCount.get(j)+" is Not Matching with the Actual in Table";
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

	public static boolean verifyActionsButton(String sActivities , String sTableName) {
		try {
			String AppendErrorMsg = "";
			boolean validStatus = true;
			//sActivities = sActivities.replace("Search", "").replace("Add", "").replace("Approve", "");

			if(sActivities.contains("Search")||sActivities.contains("Add")||sActivities.contains("Approve") ||sActivities.contains("View")){
				sActivities = sActivities.replace("Search", "").replace("Add", "").replace("Approve", "").replace("View","");
				sActivities = sActivities.replaceFirst("^,","");
				sActivities = sActivities.replace(",,",",");
				sActivities = sActivities.replaceAll(",$", "");
			}

			//For other Activities like DeActivate ,Activate,Edit Search and View Check boxes should be default Selected So Search and View is Removed above and View is adding here
			sActivities = sActivities +",View";
			sActivities = sActivities.replaceFirst("^,","");
			String recordButtonLocator = "";

			//locator value search is small in all masters except Trade Search 
			if(sActivities.contains("Trade")){
				recordButtonLocator = "//div[@id='contenttablejqxgridSearch"+sTableName+"']/div[1]//div/a";
			}else{
				recordButtonLocator = "//div[@id='contenttablejqxgridsearch"+sTableName+"']/div[1]//div/a";
			}
			sActivities = sActivities.replace("Trade", "");

			int buttonXpathCount = Elements.getXpathCount(Global.driver, By.xpath(recordButtonLocator));
			String actualButtonsinApp = "";
			for(int i=1;i<=buttonXpathCount;i++){
				String buttonAttribute = Elements.getElementAttribute(Global.driver, By.xpath(recordButtonLocator+"["+i+"]"), "href");
				buttonAttribute = buttonAttribute.toLowerCase();
				if(buttonAttribute!=null){
					if(buttonAttribute.contains("modify")){
						actualButtonsinApp = actualButtonsinApp+"Edit,";
						continue;
					}
					if(buttonAttribute.contains("view")){
						actualButtonsinApp = actualButtonsinApp+"View,";
						continue;
					}
					if(buttonAttribute.contains("activate")){
						actualButtonsinApp = actualButtonsinApp+"Activate,";
						continue;
					}
					if(buttonAttribute.contains("deactivate")){
						actualButtonsinApp = actualButtonsinApp+"Deactivate,";
						continue;
					}
					if(buttonAttribute.contains("delete")){
						actualButtonsinApp = actualButtonsinApp+"Delete,";
						continue;
					}
					if(buttonAttribute.contains("cancel")){
						actualButtonsinApp = actualButtonsinApp+"Cancel,";
						continue;
					}
				}
			}
			actualButtonsinApp = actualButtonsinApp.replaceAll(",$", "");

			if(actualButtonsinApp.split(",").length!=sActivities.split(",").length)
			{
				AppendErrorMsg = "Expected Values "+sActivities+" are Not matching with the Actual Values "+actualButtonsinApp;
				validStatus = false;
			}
			else
			{
				List<String> expectedValues = Arrays.asList(sActivities.split(","));
				for(int k=0 ; k<expectedValues.size() ; k++){
					if(!actualButtonsinApp.contains(expectedValues.get(k))){
						AppendErrorMsg = "[ "+AppendErrorMsg+"Expected button "+expectedValues.get(k)+" is Not there in Table ]";
						validStatus = false;
					}
				}

			}			
			Messages.errorMsg =AppendErrorMsg;
			return validStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean waitUntilSpinnerGoInvisible(int iSecondsToSleep, int iNoOfIterations){
		try {
			boolean bSpinnerVisibility = true;
			for (int i = 0; i <= iNoOfIterations; i++) {
				bSpinnerVisibility = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='loading-indicator-activity_pane']"));
				if (bSpinnerVisibility) {
					TimeUnit.SECONDS.sleep(iSecondsToSleep);
					continue;
				}
				break;
			}
			if (bSpinnerVisibility) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static boolean doSubOperationsOnTransactionTrades(String master , String operation){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//button[contains(@onclick,'javascript:submitForm') and contains(@onclick,'SAVE')]") , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Save Button is Not Visible";
				return false;
			}
			if(operation.equalsIgnoreCase("Save")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitForm') and contains(@onclick,'SAVE')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Save button cannot be clicked";
					return false;
				}

			}

			if(operation.contains("Review")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitForm') and contains(@onclick,'REVIEW')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Send For Review button cannot be clicked";
					return false;
				}
			}

			if(operation.equalsIgnoreCase("Clear")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-times-circle']//parent::a[contains(normalize-space(),'Clear')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Clear button cannot be clicked";
					return false;
				}
			}

			if(operation.equalsIgnoreCase("Cancel")){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-times-circle']//parent::a[contains(normalize-space(),'Cancel')]"));
				if(!bStatus){
					Messages.errorMsg = master+" Cancel button cannot be clicked";
					return false;
				}
			}
			if(operation.equalsIgnoreCase("Save") || operation.contains("Review")){
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitForm') and contains(@onclick,'ExceptionApproved')]"), Constants.lTimeOut);
				if(bStatus){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'javascript:submitForm') and contains(@onclick,'ExceptionApproved')]"));
					if(!bStatus){
						Messages.errorMsg = "Proceed Button Cannot be clicked";
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Succesfull Message is Not visible";
						return false;
					}
				}else{
					Messages.errorMsg = "Proceed Button is Not Visible";
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

	public static String jsGetElementValueWithXpath(String xpath){
		try {
			JavascriptExecutor js = (JavascriptExecutor) Global.driver;
			WebElement element = Global.driver.findElement(By.xpath(xpath));

			String strValue = js.executeScript("return arguments[0].value", element).toString();
			return strValue.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public static boolean VerifyLabelHiddenText(String LabelName , String Expected){

		try{

			String ActualTest= NewUICommonFunctions.jsGetElementValueWithXpath("//label[text()='"+LabelName+"']/following-sibling::div[1]/input");
			if(ActualTest == null){
				Messages.errorMsg = "Actual value of String Contains Null ";
				return false;
			}
			if(Float.parseFloat(ActualTest) == Float.parseFloat(Expected)){
				return true;  
			}
			Messages.errorMsg = ActualTest +" is actual value for Label: "+LabelName+" which is not matching with Expected value: "+Expected;
			return false;
		}

		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	//If any operation has to be performed send 'sOperationToPerform' as operation name else send ""/null value.
	public static boolean verifyTheRecordInSearchPanelWithStatusAndTxnIDAlongWithOperation(String sStatus, String sGridColNameOfTxnID, String sTxnID, String sTableName, int time, String sOperationToPerform){
		//send 'sOperationToPerform' value as : null/""/Operation.
		try{
			for (int i = 0; i < 8; i++) {
				bStatus = searchInSearchPanel("Status", sStatus, sStatus, sTableName, time);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;					
				}
				if(bStatus){
					break;
				}				
			}
			if (!bStatus) {
				Messages.errorMsg = " ERROR : The Expected values : "+sTxnID+" is not visible in the serach filter after 10 iterations.";
				return false;
			}
			TimeUnit.SECONDS.sleep(2);
			waitUntilSpinnerGoInvisible(1, 15);

			int iColHeaderIndexNo = getTheColumnHeaderCount(sGridColNameOfTxnID, sTableName);
			for (int i = 0; i < 8; i++) {				
				bStatus = enterTextAndVerifyinFilterBoxOfGridTable(iColHeaderIndexNo, sTxnID, sTableName);
				if (bStatus) {
					break;
				}
				Global.driver.navigate().refresh();
				waitUntilSpinnerGoInvisible(1, 15);
			}			
			if (!bStatus) {
				return false;
			}
			if (sOperationToPerform != null && !sOperationToPerform.equalsIgnoreCase("")) {
				bStatus = performOperationOnIdentifiedGridRecord(sOperationToPerform);
				if (!bStatus) {
					return false;
				}
			}
			return true; 
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean performOperationOnIdentifiedGridRecord(String sOperation){
		try {
			if (sOperation.equalsIgnoreCase("Deactivate")) {
				bStatus = spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-ban']"));
				if(!bStatus){
					Messages.errorMsg = "Cannot click on 'Deactivate' operation";
					return false;
				}
				else {
					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.FundFamilyMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
					if(!bStatus)
					{
						Messages.errorMsg = "Deactivate request is not raised after click on 'Deactivate' icon.";
						return false;
					}
				}
			}

			if (sOperation.equalsIgnoreCase("Activate")) {
				bStatus = spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-check-circle-o']"));
				if(!bStatus){
					Messages.errorMsg = "Cannot click on 'Activate' operation";
					return false;
				}
				else {
					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.FundFamilyMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
					if(!bStatus)
					{
						Messages.errorMsg = "Activate request is not raised after click on 'Activate' icon.";
						return false;
					}
				}
			}

			if (sOperation.equalsIgnoreCase("Modify") || sOperation.equalsIgnoreCase("Validate")) {
				bStatus = spinnerClick(Global.driver, By.xpath("//em[@class='fa fa-pencil']"));
				if(!bStatus){
					Messages.errorMsg = "Cannot click on 'Edit' operation";
					return false;
				}
			}
			return true;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay(String sLblName, By txtLocator, String sExpectedText, String YesPlaceHolderNoAttrVal, boolean isDecimalComparison, int iNoOfDecimalsExpected){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try{
			String sActualvalue ="";
			if (YesPlaceHolderNoAttrVal.equalsIgnoreCase("No")) {				
				sActualvalue = Elements.getElementAttribute(Global.driver, txtLocator, "value");
				if (isDecimalComparison == false) {
					if (sActualvalue == null || !sActualvalue.trim().equalsIgnoreCase(sExpectedText) ) {
						Messages.errorMsg = " [ ERROR : "+sActualvalue+" : is actual value for field : "+sLblName+" which is not matching with Expected value : "+sExpectedText+" . ] \n";
						return false;
					}
				}
				else {
					if (sActualvalue != null){
						sActualvalue = sActualvalue.replaceAll(",", "");
						if(iNoOfDecimalsExpected != 0 && iNoOfDecimalsExpected != -1 && !sExpectedText.equalsIgnoreCase("0")){
							List<String> splitDecimalsPoint = Arrays.asList(sActualvalue.split("\\."));
							if (splitDecimalsPoint.size()<2 || splitDecimalsPoint.get(1).length() != iNoOfDecimalsExpected) {
								sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : No Of Decimals in Actual VALUE : '"+sActualvalue+"' ,is not matching with the length Expected : '"+iNoOfDecimalsExpected+"'] \n";
								bValidateStatus = false;
							}
						}
						if(iNoOfDecimalsExpected == 0){
							List<String> splitDecimalsPoint = Arrays.asList(sActualvalue.split("\\."));
							if(splitDecimalsPoint.size()<2){
								return true;
							}
							sAppendErrorMsg = sAppendErrorMsg+"[ ERROR : "+sLblName+" No Of Decimals in Actual value : '"+sActualvalue+"' , is not matching with the length Expected : '"+iNoOfDecimalsExpected+"'] \n";
							bValidateStatus =  false;
						}
						if(iNoOfDecimalsExpected == -1){
							bValidateStatus = true;
						}
					}
					if (sActualvalue == null || sActualvalue.equalsIgnoreCase("") || Float.parseFloat(sActualvalue.trim()) != Float.parseFloat(sExpectedText) ) {
						sAppendErrorMsg = sAppendErrorMsg + " [ ERROR : "+sActualvalue+" : is actual value for field : "+sLblName+" which is not matching with Expected value : "+sExpectedText+" . ] \n";
						bValidateStatus = false;
					}
				}						
			}
			if (YesPlaceHolderNoAttrVal.equalsIgnoreCase("Yes")) {
				sActualvalue = Elements.getText(Global.driver, txtLocator);
				if (isDecimalComparison == false) {
					if (sActualvalue == null || !sActualvalue.trim().equalsIgnoreCase(sExpectedText) ) {
						Messages.errorMsg = " [ ERROR : "+sActualvalue+" : is actual value for field : "+sLblName+" which is not matching with Expected value : "+sExpectedText+" . ] \n";
						return false;
					}
				}
				else {
					if (sActualvalue != null){
						sActualvalue = sActualvalue.replaceAll(",", "");
						if(iNoOfDecimalsExpected != 0 && iNoOfDecimalsExpected != -1 && !sExpectedText.equalsIgnoreCase("0")){
							List<String> splitDecimalsPoint = Arrays.asList(sActualvalue.split("\\."));
							if (splitDecimalsPoint.size()<2 || splitDecimalsPoint.get(1).length() != iNoOfDecimalsExpected) {
								sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : No Of Decimals in Actual VALUE : '"+sActualvalue+"' ,is not matching with the length Expected : '"+iNoOfDecimalsExpected+"'] \n";
								bValidateStatus = false;
							}
						}
						if(iNoOfDecimalsExpected == 0){
							List<String> splitDecimalsPoint = Arrays.asList(sActualvalue.split("\\."));
							if(splitDecimalsPoint.size()<2){
								return true;
							}
							sAppendErrorMsg = sAppendErrorMsg+"[ ERROR : "+sLblName+" No Of Decimals in Actual value : '"+sActualvalue+"' , is not matching with the length Expected : '"+iNoOfDecimalsExpected+"'] \n";
							bValidateStatus =  false;
						}
						if(iNoOfDecimalsExpected == -1){
							bValidateStatus = true;
						}
					}
					if (sActualvalue == null || sActualvalue.equalsIgnoreCase("") || Float.parseFloat(sActualvalue.trim()) != Float.parseFloat(sExpectedText) ) {
						sAppendErrorMsg = sAppendErrorMsg + " [ ERROR : "+sActualvalue+" : is actual value for field : "+sLblName+" which is not matching with Expected value : "+sExpectedText+" . ] \n";
						bValidateStatus = false;
					}
				}		
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}


	public static boolean verifyDecimalsToDisplay(String lableName ,int numberOfDecimalsToDisplay , String valueToVerify ){
		try {
			String sAppendErrorMsg = "";
			boolean bValidateStatus = true;
			if (valueToVerify != null){
				valueToVerify = valueToVerify.replaceAll(",", "");
				if(numberOfDecimalsToDisplay != 0 && numberOfDecimalsToDisplay != -1 && !valueToVerify.equalsIgnoreCase("0")){
					List<String> splitDecimalsPoint = Arrays.asList(valueToVerify.split("\\."));
					if (splitDecimalsPoint.size()<2 || splitDecimalsPoint.get(1).length() != numberOfDecimalsToDisplay) {
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : "+lableName+" No Of Decimals in Actual value : '"+valueToVerify+"' , is not matching with the length Expected : '"+numberOfDecimalsToDisplay+"'] \n";
						bValidateStatus = false;
					}
				}
			}
			if(numberOfDecimalsToDisplay == 0){
				List<String> splitDecimalsPoint = Arrays.asList(valueToVerify.split("\\."));
				if(splitDecimalsPoint.size()<2){
					return true;
				}
				Messages.errorMsg = "[ ERROR : "+lableName+" No Of Decimals in Actual value : '"+valueToVerify+"' , is not matching with the length Expected : '"+numberOfDecimalsToDisplay+"'] \n";
				return false;
			}
			if(numberOfDecimalsToDisplay == -1){
				return true;
			}

			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	public static void threadSleep(){
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

	}

	public static boolean handleAlert(){
		try{
			bStatus = Wait.waitForAlert(Global.driver, 3);
			if(bStatus){
				String alertText = Alerts.getAlertMessage(Global.driver);
				bStatus = Alerts.closeAlert(Global.driver);
				if(!bStatus){
					Messages.errorMsg = "Cannot Close Alert ";
					return false;
				}
				Messages.errorMsg = "Alert Present, Alert Text : "+alertText;
				return false;
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static void DeleteFileIfExists(String filePath){
		try {
			File file = new File(filePath);
			if(file.exists()){
				file.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static boolean navigateToSidePocket(String sSubscriptionOrRedemption){
		try {			
			bStatus = NewUICommonFunctions.selectMenu("TRADES", "Side Pocket");
			if(!bStatus){
				return false;
			}
			waitUntilSpinnerGoInvisible(1, 30);
			String buttonLocator = "";
			if(sSubscriptionOrRedemption.toLowerCase().contains("subscription")){
				buttonLocator = "//button[contains(@onclick,'getSPSubAdd') and contains(normalize-space(),'Subscription')]";
			}
			if(sSubscriptionOrRedemption.toLowerCase().contains("redemption")){
				buttonLocator = "//button[contains(@onclick,'getSPRedAdd') and contains(normalize-space(),'Redemption')]";
			}

			bStatus = Elements.click(Global.driver, By.xpath(buttonLocator));
			if(!bStatus){
				Messages.errorMsg = " Cannot click on Add button of "+sSubscriptionOrRedemption;
				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	public static boolean doPickDateFromCalender(String dateToSelect, String sLocatorOfDateField){
		try {
			String[] sMonths = {"January","February","March","April","May","June","July","August","September","October","November","December"};
			
			String[] sShortMonths = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

			String sMonthIndex = Arrays.asList(dateToSelect.split("/")).get(0);

			String sOnMonth = sMonths[Integer.parseInt(sMonthIndex)-1];

			String sOnDay = Arrays.asList(dateToSelect.split("/")).get(1);
			if (sOnDay.startsWith("0")) {
				sOnDay = sOnDay.replace("0", "");
			}
			//System.out.println(sOnDay);
			String sOnYear = Arrays.asList(dateToSelect.split("/")).get(2);

			String sMiddleLinkToSwitchToYears = "//div[@class='datepicker-days' and contains(@style,'block')]//th[@class='datepicker-switch']";
			//DAte and Time to be set in textbox
			String dateTime = dateToSelect;
			WebDriver driver = Global.driver;
			//button to open calendar
			bStatus = Elements.click(Global.driver, By.xpath(sLocatorOfDateField));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Failed to click on calender text box to open calender.]\n";
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sMiddleLinkToSwitchToYears), Constants.iDropdown);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Calender wasn't opened.]\n";
				return false;
			}
			String sActiveMonthAndYear = Elements.getText(Global.driver, By.xpath(sMiddleLinkToSwitchToYears));
			if (sActiveMonthAndYear == null || sActiveMonthAndYear.isEmpty() || sActiveMonthAndYear.equalsIgnoreCase("")) {
				Messages.errorMsg = "[ ERROR : wasn't able to retrieve the opened calender currently active month and date.]\n";
				return false;
			}
			if (sActiveMonthAndYear.contains(sOnMonth) && sActiveMonthAndYear.contains(sOnYear)) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days']//tbody//tr//td[@class='day' and normalize-space()='"+sOnDay+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to select the day : '"+sOnDay+"' for the year and month : '"+sActiveMonthAndYear+"'.]\n";
					return false;
				}
				return true;
			}
			if (sActiveMonthAndYear.contains(sOnYear) && !sActiveMonthAndYear.contains(sOnMonth)) {
				int sActiveCalenderMonthIndex = Arrays.asList(sMonths).indexOf(Arrays.asList(sActiveMonthAndYear.split(" ")).get(0).trim()) + 1;
				int sRequiredCalenderMonthIndex = Integer.parseInt(sMonthIndex);
				if (sActiveCalenderMonthIndex < sRequiredCalenderMonthIndex) {
					for (int i = 0; i < sRequiredCalenderMonthIndex - sActiveCalenderMonthIndex ; i++) {
						bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days' and contains(@style,'block')]//thead//th[@class='next']"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Wasn't able to move the month to next till the given month appears.]\n";
							return false;
						}
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days']//tbody//tr//td[@class='day' and normalize-space()='"+sOnDay+"']"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to select the day : '"+sOnDay+"' for the year and month : '"+sActiveMonthAndYear+"'.]\n";
						return false;
					}
					return true;
				}

				if (sActiveCalenderMonthIndex > sRequiredCalenderMonthIndex) {
					for (int i = 0; i < sActiveCalenderMonthIndex - sRequiredCalenderMonthIndex; i++) {
						bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days' and contains(@style,'block')]//thead//th[@class='prev']"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Wasn't able to move the month to next till the given month appears.]\n";
							return false;
						}
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days']//tbody//tr//td[@class='day' and normalize-space()='"+sOnDay+"']"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to select the day : '"+sOnDay+"' for the year and month : '"+sActiveMonthAndYear+"'.]\n";
						return false;
					}
					return true;
				}
			}
			if (!sActiveMonthAndYear.contains(sOnYear)) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days' and contains(@style,'block')]//thead//th[@class='datepicker-switch']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on calender 'datepicker-days' view header to bring the months view.]\n";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-months' and contains(@style,'block')]//thead//th[@class='datepicker-switch']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on calender 'datepicker-months' view header to bring the years view.]\n";
					return false;
				}
				bStatus = doSelectGivenYearBySortingRange(sOnYear);
				if (!bStatus) {
					return false;
				}
				String sMonthToPick = Arrays.asList(sShortMonths).get(Integer.parseInt(sMonthIndex) - 1);
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-months']//tbody//tr//td//span[@class='month' and normalize-space()='"+sMonthToPick+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on month : '"+sMonthToPick+"' from calender 'datepicker-months' view.]\n";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-days']//tbody//tr//td[@class='day' and normalize-space()='"+sOnDay+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on day : '"+sOnDay+"' from calender 'datepicker-days' view.]\n";
					return false;
				}
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			Messages.errorMsg = "[ ERROR : Wasn't able to select the given date : '"+dateToSelect+"' from the date picker.]\n";
			return false;
		}		
	}
	
	private static boolean doSelectGivenYearBySortingRange(String sOnYear){
		for ( ; ; ) {
			String sYearsRange = Elements.getText(Global.driver, By.xpath("//div[@class='datepicker-years' and contains(@style,'block')]//thead//th[@class='datepicker-switch']"));
			if (sYearsRange == null || sYearsRange.equalsIgnoreCase("")) {
				Messages.errorMsg = "[ ERROR : Wasn't able to retrieve the years range from the calender]\n";
				return false;
			}
			int iYearsAppearFrom = Integer.parseInt(Arrays.asList(sYearsRange.split("-")).get(0).trim());
			int iYearsAppearTo = Integer.parseInt(Arrays.asList(sYearsRange.split("-")).get(1).trim());
			if (Integer.parseInt(sOnYear) >= iYearsAppearFrom && Integer.parseInt(sOnYear) <= iYearsAppearTo) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-years' and contains(@style,'block')]//tbody//td//span[@class='year' and normalize-space()='"+sOnYear+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on the year : '"+sOnYear+"' on calender 'datepicker-years' view.]\n";
					return false;
				}
				return true;
			}
			if (iYearsAppearFrom > Integer.parseInt(sOnYear)) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-years' and contains(@style,'block')]//thead//th[@class='prev' and contains(@style,'visible')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on the left arrow to go down in year range.]\n";
					return false;
				}
			}
			if (iYearsAppearTo < Integer.parseInt(sOnYear)) {
				bStatus = Elements.click(Global.driver, By.xpath("//div[@class='datepicker-years' and contains(@style,'block')]//thead//th[@class='next' and contains(@style,'visible')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Wasn't able to click on the right arrow to go up in year range.]\n";
					return false;
				}
			}
		}
	}
}