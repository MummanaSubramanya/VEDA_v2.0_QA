package com.viteos.veda.unusedwork.templibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jxl.CellView;
import jxl.Range;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.jetty.websocket.common.io.AbstractWebSocketConnection.OnDisconnectCallback;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.AllocationWizardFunctions;
import com.viteos.veda.master.lib.ComparisonReportFunctions;
import com.viteos.veda.master.lib.Constants;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.OpeningBalanceMasterAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class GaffurClass {
	public static boolean bStatus = true;
	public static void main(String k[]) throws IOException, WriteException{
		Workbook workbook = null;
		WritableSheet sheet = null;
		WritableWorkbook copy = null;
		try {
			//removeEDSReportMergedCells("D:\\Funds Data\\OldData\\Belmont\\EDS_Reports\\veda1.5\\Dec_2013.xls");
			
			removeExtraColumnsAndAddHolderAccountInOldEDSReport("D:\\Funds Data\\OldData\\Belmont\\EDS_Reports\\veda1.5\\Nov_2013.xls");
			workbook = Workbook.getWorkbook(new File("D:\\EDS_Reports\\2015\\Belmont Partners LLC_31-Oct-2015.xls"));

			copy = Workbook.createWorkbook(new File("D:\\EDS_Reports\\2015\\Belmont Partners LLC_31-Oct-2015.xls"), workbook);

			sheet = copy.getSheet(0);

			/*Range[] w = sheet.getMergedCells();
		    System.out.println(w.toString());
		    for(int i=0;i<w.length;i++){
		    	sheet.unmergeCells(w[i]);
		    }*/
			copy.write();
			copy.close();
			sheet.removeColumn(8);
			/*sheet.insertRow(6);
		    sheet.insertRow(6);
		    sheet.insertRow(9);*/
			//sheet.removeColumn(0);
			//sheet.insertRow(10);

			//sheet.removeColumn(0);
			// sheet.addRowPageBreak(6);
			//sheet.addRowPageBreak(7);


			//NewUICommonFunctions.isLoginFromAllocation = true;
			bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
			if(!bStatus){
				Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
				Assert.fail(Messages.errorMsg);
			}

			downloadAFileTotheRequiredPath("", "");

			Map<String, String> mapFeeAdjustmentDetails = new HashMap<>();
			mapFeeAdjustmentDetails.put("LEsToNAVFreezeOrUndo", "SeriesMasterRollupFund:SeriesMasterRollupFund:SeriesMasterRollupFund");
			mapFeeAdjustmentDetails.put("ActionFinalizeOrUndoNAV", "Undo:Finalize:Update");
			mapFeeAdjustmentDetails.put("NAVFreezeStatus", "Not Finalized:Finalized:Finalized");
			mapFeeAdjustmentDetails.put("OrderIdToUpdateNAV", "None:TRA0578,TRA0577:TRA0580,TRA1557,TRA1560");
			mapFeeAdjustmentDetails.put("IsFeeAdjustMentScreenAvailable", "Yes");
			mapFeeAdjustmentDetails.put("LegalEntitiesToUpdateFee", "A42:A42:A42");
			mapFeeAdjustmentDetails.put("FeeTypeForTheLegalEntity", "Management Fee:Management Fee:Management Fee");
			mapFeeAdjustmentDetails.put("AccountIdsToUpdateFee", "AC0002,AC0002:AC0002:AC0002");
			mapFeeAdjustmentDetails.put("ClassNameForTheGivenAccount", "Class A,Class A:Class A:Class A");
			mapFeeAdjustmentDetails.put("SeriesNameForTheGivenAccount", "Series 8/14,Series 9/12:Series 5/10:Series 6/14");
			mapFeeAdjustmentDetails.put("FeeToEnterForTheInvestor", "10,30:60:50");
			AllocationWizardFunctions.doFillFeeAdjustmentDetails(mapFeeAdjustmentDetails);
			/*
			 * Update NAV Function
			 * 
			 * Map<String, String> mapNAVUpdateDetails = new HashMap<>();
			mapNAVUpdateDetails.put("OrderIdsToUpdateNAV", "TRA0577,TRA0578,TRA1567,TRA0580");
			bStatus = doSelectTheTardesToUpdateTheNAV(mapNAVUpdateDetails );
			if(!bStatus){
				Messages.errorMsg = "";
			}*/

			Map<String, String> mapSeriesRollupData = new HashMap<>();

			/*	mapSeriesRollupData.put("LegalEntityToSelectRollUp", "SeriesMasterRollupFund:SeriesRollupStatndAloneFund");
			mapSeriesRollupData.put("ClassNameOfRollUpSeries", "SeriesRollupClass X,SeriesRollupClass Y,SeriesRollupClass Z:SAClassA,SAClassB");
			mapSeriesRollupData.put("BenchMarkSeriesToSelect", "SeriesRp_BenchMark_JAN,SeriesRP_JAN,SeriesRP_Z_JAN:SASeriesA,SASeriesB");
			mapSeriesRollupData.put("SeriesTobeRollup", "SeriesRP_BenchMark_FEB|Series Roll Up Jan Series|Series Roll Up Feb Series,SeriesRP_JAN,SeriesRP_Z_JAN:SASeriesC,SASeriesD,SASeriesE");
			mapSeriesRollupData.put("SeriesRollupOperation", "Previous");
			doFillOrVerifySeriesRollupScreen(mapSeriesRollupData );
			 */

			doPerformOperationsOnNAVFreezeScreen(mapSeriesRollupData);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			copy.write();
			copy.close();
			return;
		}
	}

	private static void removeColumns(String filePath) throws IOException, WriteException {
		Workbook workbook = null;
		WritableSheet sheet = null;
		WritableWorkbook copy = null;
		try {

			workbook = Workbook.getWorkbook(new File(filePath));

			copy = Workbook.createWorkbook(new File(filePath), workbook);

			sheet = copy.getSheet(0);

			sheet.removeColumn(8);

			copy.write();
			copy.close();
		}
		catch(Exception e){
			e.printStackTrace();
			copy.write();
			copy.close();
		}


	}

	private static void removeMergedCells(String filePath) throws IOException, WriteException {
		Workbook workbook = null;
		WritableSheet sheet = null;
		WritableWorkbook copy = null;
		try {

			workbook = Workbook.getWorkbook(new File(filePath));

			copy = Workbook.createWorkbook(new File(filePath), workbook);

			sheet = copy.getSheet(0);

			Range[] w = sheet.getMergedCells();
			System.out.println(w.toString());
			for(int i=0;i<w.length;i++){
				sheet.unmergeCells(w[i]);
			}
			copy.write();
			copy.close();
		}
		catch(Exception e){
			e.printStackTrace();
			copy.write();
			copy.close();
		}

	}

	private static boolean downloadAFileTotheRequiredPath(String filePath,String fileNameWithExtension) {
		try {

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//td[normalize-space()=\"SeriesRollupStatndAloneFund\"]//following-sibling::td//a[contains(@onclick,'viewReport')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to click View button for Legal Entity : 'SeriesRollupStatndAloneFund']\n";
				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static String getValue() {
		return null;
	}

	private static void updateAttributeValue(Document doc) {
		NodeList employees = doc.getElementsByTagName("TradeTypeEXCN");
		Element emp = null;
		//loop for each employee
		String trId= "12345";
		for(int i=0; i<employees.getLength();i++){
			emp = (Element) employees.item(i);
			String testCaseName = "ExchangeSendForReview_TC2";
			if(testCaseName.equalsIgnoreCase(emp.getAttribute("TestcaseName"))){
				//prefix id attribute with M
				emp.setAttribute("TransactionID", trId);
			}
		}
	}

	public static void updatedAttributeValueInCreatedXMLFile(String filePath,String attributeNameToUpdate,String newAttributeValue,String testCaseAttributeName,String testCaseNameToUpdate,String tagName){
		try {
			File inputFile = new File(filePath); 	    	
			DocumentBuilderFactory docFactory =	DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(inputFile);

			//Update Values
			NodeList employees = doc.getElementsByTagName(tagName);
			Element emp = null;
			//loop for each employee
			for(int i=0; i<employees.getLength();i++){
				emp = (Element) employees.item(i);
				if(testCaseNameToUpdate.equalsIgnoreCase(emp.getAttribute(testCaseAttributeName))){
					emp.setAttribute(attributeNameToUpdate, newAttributeValue);
				}
			}

			//Write to file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult consoleResult = new StreamResult(new File(filePath));
			transformer.transform(source, consoleResult);

			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
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

	//Side Pocket Subscription Checker Functions
	public static boolean doVerifyCheckerForSidePocketSubscription(Map<String,String> mapSidePocketSubscriptionDetails){
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;


			bStatus = doVerifyFundDetailsAtChecker(mapSidePocketSubscriptionDetails);
			if(!bStatus){
				appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
				bValidStatus = false;
			}

			bStatus = doVerifyRequestDetailsAtChecker(mapSidePocketSubscriptionDetails);


			Messages.errorMsg = appendErrMsg;
			return bValidStatus; 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;

		}

	}

	private static boolean doVerifyRequestDetailsAtChecker(Map<String, String> mapSidePocketSubscriptionDetails) {
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;

			if(mapSidePocketSubscriptionDetails.get("Effective Date") != null){
				String actualDate = Elements.getElementAttribute(Global.driver, By.xpath("//label[text()='Effective Date' or normalize-space(text())='Effective Date']/..//input"), "value");
				bStatus = TradeTypeSubscriptionAppFunctions.formateAndCompareDates(actualDate, mapSidePocketSubscriptionDetails.get("Effective Date"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}



			Messages.errorMsg = appendErrMsg;
			return bValidStatus; 
		} catch (Exception e) {
			// TODO: handle exception
			return false;

		}
	}

	public static boolean doVerifyFundDetailsAtChecker(Map<String, String> mapSidePocketSubscriptionDetails) {
		try {
			String appendErrMsg = "";
			boolean bValidStatus = true;

			if(mapSidePocketSubscriptionDetails.get("Client Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Client Name", mapSidePocketSubscriptionDetails.get("Client Name"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}

			if(mapSidePocketSubscriptionDetails.get("Fund Family Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Fund Family Name", mapSidePocketSubscriptionDetails.get("Fund Family Name"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}

			if(mapSidePocketSubscriptionDetails.get("Legal Entity Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Legal Entity Name", mapSidePocketSubscriptionDetails.get("Legal Entity Name"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}

			if(mapSidePocketSubscriptionDetails.get("Side Pocket Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Side Pocket Name", mapSidePocketSubscriptionDetails.get("Side Pocket Name"));
				if(!bStatus){
					appendErrMsg = appendErrMsg+"["+Messages.errorMsg+"]";
					bValidStatus = false;
				}
			}			

			Messages.errorMsg = appendErrMsg;
			return bValidStatus; 
		} catch (Exception e) {
			// TODO: handle exception
			return false;

		}
	}


	public static boolean doSelectTheTardesToUpdateTheNAV(Map<String,String> mapNAVUpdateDetails,int leIndex,String legalEntity,String aOrderIdList){
		try {

			if (mapNAVUpdateDetails.get("Client Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Client Name' in Trial Balance Screen ", By.id("clientName"), mapNAVUpdateDetails.get("Client Name"), "No", false);
				if (!bStatus) {
					return false;
				}
			}
			if (mapNAVUpdateDetails.get("Fund Family Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Fund Family Name' in Trial Balance Screen ", By.id("fundFamilyName"), mapNAVUpdateDetails.get("Fund Family Name"), "No", false);
				if (!bStatus) {
					return false;
				}
			}
			if(mapNAVUpdateDetails.get("Break Period Date")!=null){
				//String sActualvalue = Elements.getElementAttribute(Global.driver, By.id("breakPeriodNavDate"), "value").trim();
				String sActualvalue = Elements.getText(Global.driver, By.xpath("//input[@id='breakPeriodDate']//parent::label"));
				sActualvalue = TradeTypeSubscriptionAppFunctions.formatDate(sActualvalue);
				if (sActualvalue == null || !sActualvalue.equalsIgnoreCase(mapNAVUpdateDetails.get("Break Period Date")) ) {
					Messages.errorMsg = "[ ERROR : '"+sActualvalue+"' : is actual value for field : Break Period in Trial Balance Screen which is not matching with the Expected : '"+mapNAVUpdateDetails.get("Break Period Date")+"']\n";
					return false;
				}
				//Verify To Date in Date From Row
				String sVal = NewUICommonFunctions.jsGetElementAttribute("toDate");
				if(!sVal.equalsIgnoreCase(mapNAVUpdateDetails.get("Break Period Date"))){
					Messages.errorMsg = "[ ERROR : Break Period Date : '"+mapNAVUpdateDetails.get("Break Period Date")+"' ,given in Break Period Screen is Not Matching with the 'To Date' in Trial Balance screen : '"+sVal+"']\n";
					return false;
				}
			}
			if(mapNAVUpdateDetails.get("NAV Type") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'NAV Type' in Trial Balance Screen, ", By.xpath("//label[contains(normalize-space(),'NAV Type')]//following-sibling::label"), mapNAVUpdateDetails.get("NAV Type"), "Yes", false);
				if (!bStatus) {
					return false;
				}
			}

			//wait for the Series Roll up Page for that specific Legal Entity
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Legal Entity :']//following-sibling::label[normalize-space()='"+legalEntity+"']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Legal Entity Name :"+legalEntity+",is Not visible in Series Rollup Screen";
				return false;
			}

			if(aOrderIdList != null && !aOrderIdList.equalsIgnoreCase("None")){
				List<String> orderIdList = Arrays.asList(aOrderIdList.split(","));	
				bStatus = removeAllCheckedInNAVUpdateScreen();
				for(int i=0;i<orderIdList.size();i++){
					String orderIdLocator = "//tbody//tr//td/a[contains(normalize-space(),'"+getTheTransactionIDFromTradesXMlFiles(orderIdList.get(i))+"')]";
					String checkBoxLocator = "//parent::td//preceding-sibling::td//span/input";
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath(orderIdLocator+checkBoxLocator));
					if(!bStatus){						
						bStatus = Elements.click(Global.driver, By.xpath(orderIdLocator+checkBoxLocator));
						if(!bStatus){
							bStatus = NewUICommonFunctions.jsClick(By.xpath(orderIdLocator+checkBoxLocator));
							if(!bStatus){
								Messages.errorMsg = "Unable to select the Check box for Order ID: "+orderIdList.get(i);
								return false;
							}							
						}
					}
				}
			}
			String buttonLocator = "//button[contains(@onclick,'confirmUpdate') and contains(normalize-space(),'Update')]";
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(buttonLocator), Constants.lTimeOut);
			if(bStatus){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath(buttonLocator));

				bStatus = Elements.click(Global.driver, By.xpath(buttonLocator));
				if(!bStatus){
					bStatus = NewUICommonFunctions.jsClick(By.xpath(buttonLocator));
					if(!bStatus){
						Messages.errorMsg = "unable to click the Update in NAv Update Screen";
						return false;
					}					
				}

				bStatus = Elements.click(Global.driver, By.xpath("//div[contains(normalize-space(),'All selected trades will be updated to Final')]//button[contains(@data-bb-handler,'confirm') and normalize-space()='OK']"));
				if(!bStatus){
					Messages.errorMsg = "Unable to click on OK button to Updated the NAV to Final";
					return false;
				}
			}else{
				Messages.errorMsg = "Update Button is not visible in NAV Update Screen";
				return false;
			}


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean removeAllCheckedInNAVUpdateScreen() {
		try {

			String checkBoxLocator = "//tbody//td//span";
			int xpathCount = Elements.getXpathCount(Global.driver, By.xpath(checkBoxLocator));

			for(int i=0;i<xpathCount;i++){
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath(checkBoxLocator+"/input[@id='checkTrade_"+i+"']"));
				if(bStatus){
					bStatus = Elements.click(Global.driver, By.xpath(checkBoxLocator+"/input[@id='checkTrade_"+i+"']"));
					if(!bStatus){
						bStatus = NewUICommonFunctions.jsClick(By.xpath(checkBoxLocator+"/input[@id='checkTrade_"+i+"']"));
						if(!bStatus){
							Messages.errorMsg = "Check box Cannot be Unchecked";
							return false;
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


	public static boolean doFillOrVerifySeriesRollupScreen(Map<String,String> mapSeriesRollupData){
		try {

			if(mapSeriesRollupData.get("LegalEntityToSelectRollUp") != null){
				List<String> aLeList = Arrays.asList(mapSeriesRollupData.get("LegalEntityToSelectRollUp").split(":"));
				List<String> aClassList = Arrays.asList(mapSeriesRollupData.get("ClassNameOfRollUpSeries").split(":"));
				List<String> aBenchMarkSeriesList = Arrays.asList(mapSeriesRollupData.get("BenchMarkSeriesToSelect").split(":"));
				List<String> aSeriesTobeRolledUpList = Arrays.asList(mapSeriesRollupData.get("SeriesTobeRollup").split(":"));
				List<String> aSRPOperationList = Arrays.asList(mapSeriesRollupData.get("SeriesRollupOperation").split(":"));

				for(int leIndex = 0;leIndex<aLeList.size();leIndex++){
					//Click the series Roll up button based on the Legal Entity Name
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+aLeList.get(leIndex)+"']//parent::td//following-sibling::td//button[contains(@onclick,'seriesRollup') and normalize-space()='Rollup']"));
					if(!bStatus){
						Messages.errorMsg = "Unable click the Series Rollup button for Legal Entity :"+aLeList.get(leIndex);
						return false;
					}

					//wait for the Series Roll up Page for that specific Legal Entity
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Legal Entity :']//following-sibling::label[normalize-space()='"+aLeList.get(leIndex)+"']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Legal Entity Name :"+aLeList.get(leIndex)+",is Not visible in Series Rollup Screen";
						return false;
					}

					//Split the Class and Series based on the Legal Entity Index
					List<String> indvClassList = Arrays.asList(aClassList.get(leIndex).split(","));
					List<String> indvBMSeriesList = Arrays.asList(aBenchMarkSeriesList.get(leIndex).split(","));
					List<String> indvSeriesRupList = Arrays.asList(aSeriesTobeRolledUpList.get(leIndex).split(","));
					for(int classIndex = 0; classIndex<indvClassList.size() ; classIndex++){
						String seriesLabelLocator = "//div//label[normalize-space()='"+indvClassList.get(classIndex)+"']";
						String seriesDropdownLoc = seriesLabelLocator+"//parent::div//parent::div/following-sibling::div[1]//span[contains(@id,'select')]";

						//Select the Rollup Series Dropdown based on the Class Name
						NewUICommonFunctions.scrollToView(By.xpath(seriesDropdownLoc));
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(indvBMSeriesList.get(classIndex), By.xpath(seriesDropdownLoc));
						if(!bStatus){
							Messages.errorMsg = "Series "+indvBMSeriesList.get(classIndex)+" Not selected from Drop down :"+Messages.errorMsg;
							return false;
						}

						//Split the Series to be rolled up based on the Class index
						List<String> aSeriesToBeSelect = Arrays.asList(indvSeriesRupList.get(classIndex).split("\\|"));
						for(int rpSeriesIndex = 0; rpSeriesIndex < aSeriesToBeSelect.size(); rpSeriesIndex++){
							String checkBoxLocator = "//div[contains(@id,'contenttablejqxgrid')]//div/span[normalize-space()='"+aSeriesToBeSelect.get(rpSeriesIndex)+"']//parent::div//preceding-sibling::div";

							bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(checkBoxLocator+"//span[contains(@class,'checked')]"), 2);
							//bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath(checkBoxLocator+"//span"));
							if(!bStatus){
								bStatus = Elements.click(Global.driver, By.xpath(checkBoxLocator+"//span//parent::div"));
								if(!bStatus){
									Messages.errorMsg = "Unable to Select the Check box for the Series :"+aSeriesToBeSelect.get(rpSeriesIndex);
									return false;
								}
							}
						}
					}

					NewUICommonFunctions.scrollToView(By.xpath("//h4"));
					if(aSRPOperationList.get(leIndex).equalsIgnoreCase("Submit")){
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'Series') and normalize-space()='Submit']"));
						if(!bStatus){
							Messages.errorMsg = "Unable to select the Submit Button for the Legal Entity: "+aLeList.get(leIndex);
							return false;
						}
					}
					if(aSRPOperationList.get(leIndex).equalsIgnoreCase("Previous")){
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'redirect') and normalize-space()='Previous']"));
						if(!bStatus){
							Messages.errorMsg = "Unable to select the Previous Button for the Legal Entity: "+aLeList.get(leIndex);
							return false;
						}
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4/p[contains(normalize-space(),'NAV Freeze')]"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "Page is not navigated to NAV Freeze Screen afeter clicking the Cancel Button for the Legal Entity"+aLeList.get(leIndex);
							return false;
						}
					}
					if(aSRPOperationList.get(leIndex).equalsIgnoreCase("Cancel")){
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'Home') and normalize-space()='Cancel']"));
						if(!bStatus){
							Messages.errorMsg = "Unable to select the Cancel Button for the Legal Entity: "+aLeList.get(leIndex);
							return false;
						}
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4/p[contains(normalize-space(),'Choose Portfolio')]"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "Page is not navigated to Choose Portfolio Screen afeter clicking the Cancel Button for the Legal Entity"+aLeList.get(leIndex);
							return false;
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


	//Do Finalize / Undo on NAV Freeze screen.
	public static boolean doPerformOperationsOnNAVFreezeScreen(Map<String,String> mapAllocationDetails){
		try {
			mapAllocationDetails.put("LEsToNAVFreezeOrUndo", "SeriesMasterRollupFund:SeriesMasterRollupFund:SeriesMasterRollupFund");
			mapAllocationDetails.put("ActionFinalizeOrUndoNAV", "Undo:Finalize:Update");
			mapAllocationDetails.put("NAVFreezeStatus", "Not Finalized:Finalized:Finalized");
			mapAllocationDetails.put("OrderIdToUpdateNAV", "None:TRA0578,TRA0577:TRA0580,TRA1557,TRA1560");
			if (mapAllocationDetails.get("LEsToNAVFreezeOrUndo") != null) {
				List<String> sLEsToProcessAllocation = Arrays.asList(mapAllocationDetails.get("LEsToNAVFreezeOrUndo").split(":"));
				List<String> sOperationsOnLE = Arrays.asList(mapAllocationDetails.get("ActionFinalizeOrUndoNAV").split(":"));
				List<String> sExpectedStatusAfterLEsOperations = Arrays.asList(mapAllocationDetails.get("NAVFreezeStatus").split(":"));
				List<String> aOrderIdToUpdateNAV = null;
				if(mapAllocationDetails.get("OrderIdToUpdateNAV") != null){
					aOrderIdToUpdateNAV = Arrays.asList(mapAllocationDetails.get("OrderIdToUpdateNAV").split(":"));
				}
				if (sExpectedStatusAfterLEsOperations.size() != sLEsToProcessAllocation.size()) {
					Messages.errorMsg = "[ ERROR : Test data wasn't given properly please do crosscheck the number of LE's given to process NAV Freeze actions and their respective Expected status's.]\n";
					return false;
				}
				for (int indexOfLE = 0; indexOfLE < sLEsToProcessAllocation.size(); indexOfLE++) {
					if (sOperationsOnLE != null && !sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("None")) {

						if(sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("Finalize")){							
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//button[contains(@onclick,'freeze') and contains(normalize-space(),'"+sOperationsOnLE.get(indexOfLE).trim()+"')]"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to perform Operation : '"+sOperationsOnLE.get(indexOfLE).trim()+"' , on Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' .]\n";
								return false;
							}

							bStatus = Elements.click(Global.driver, By.xpath("//div[@class='modal-body' and contains(normalize-space(),'Are you sure you want to finalize this NAV?')]//following-sibling::div/button[normalize-space()='OK']"));
							if(!bStatus){
								Messages.errorMsg = "Unable to select the OK button in Finalizing alert pop up for the Legal Entity ;"+sLEsToProcessAllocation.get(indexOfLE);
								return false;
							}

							TimeUnit.SECONDS.sleep(2);

							//After finalizing the Fund screen Navigates to NAV Update Screen Calling the NAV Update Screen Functions
							if(aOrderIdToUpdateNAV != null){
								bStatus = doSelectTheTardesToUpdateTheNAV(mapAllocationDetails, indexOfLE ,sLEsToProcessAllocation.get(indexOfLE),aOrderIdToUpdateNAV.get(indexOfLE));
								if(!bStatus){
									return false;
								}
							}

						}

						if(sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("Update")){
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//button[contains(@onclick,'update') and contains(normalize-space(),'"+sOperationsOnLE.get(indexOfLE).trim()+"')]"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to perform Operation : '"+sOperationsOnLE.get(indexOfLE).trim()+"' , on Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' .]\n";
								return false;
							}
							TimeUnit.SECONDS.sleep(2);

							bStatus = doSelectTheTardesToUpdateTheNAV(mapAllocationDetails, indexOfLE ,sLEsToProcessAllocation.get(indexOfLE),aOrderIdToUpdateNAV.get(indexOfLE));
							if(!bStatus){
								return false;
							}
						}

						if(sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("Undo")){
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//button[contains(@onclick,'freeze') and contains(normalize-space(),'"+sOperationsOnLE.get(indexOfLE).trim()+"')]"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to perform Operation : '"+sOperationsOnLE.get(indexOfLE).trim()+"' , on Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' .]\n";
								return false;
							}
							TimeUnit.SECONDS.sleep(2);

							bStatus = Elements.click(Global.driver, By.xpath("//div[@class='modal-body' and contains(normalize-space(),'Are you sure you want to undo this NAV?')]//following-sibling::div/button[normalize-space()='OK']"));
							if(!bStatus){
								Messages.errorMsg = "Unable to select the OK button in Undo this NAV alert pop up for the Legal Entity ;"+sLEsToProcessAllocation.get(indexOfLE);
								return false;
							}
						}
						NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 25);
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tbody//tr//td//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//span[contains(@id,'allocationStatus') and normalize-space()='"+sExpectedStatusAfterLEsOperations.get(indexOfLE).trim()+"']"), Constants.lTimeOut);
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Expected Status : '"+sExpectedStatusAfterLEsOperations.get(indexOfLE).trim()+"' ,for Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' ,wasn't visible.]\n";
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
	}


	public static String getTheTransactionIDFromTradesXMlFiles(String testCaseId){
		try {
			String sTransactionId = "";
			Map<String ,String> mapXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", testCaseId);
			if(mapXMLSPSUBDetails != null){
				sTransactionId = mapXMLSPSUBDetails.get("TransactionID");
			}
			else
			{
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "TradeTypeRED", testCaseId);
				if(mapXMLSubscriptionDetails != null)
				{
					sTransactionId = mapXMLSubscriptionDetails.get("TransactionID");
				}
				else
				{
					Map<String ,String> mapXMLTransferDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "TradeTypeTRAN", testCaseId);
					if(mapXMLTransferDetails != null)
					{
						sTransactionId = mapXMLTransferDetails.get("TransactionID");
					}
					else
					{
						Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "TradeTypeEXCN", testCaseId);
						if(mapXMLExchangeDetails != null){
							sTransactionId = mapXMLExchangeDetails.get("TransactionID");
						}
						else
						{
							Map<String ,String> mapXMLSwitchDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TradeTypeSWITCH", testCaseId);
							if(mapXMLSwitchDetails != null){
								sTransactionId = mapXMLSwitchDetails.get("TransactionID");
							}else{
								sTransactionId = testCaseId;
							}
						}
					}
				}
			}
			return sTransactionId;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static void removeData(String filePath) throws FileNotFoundException, IOException{

		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
		HSSFSheet sheet = wb.getSheetAt(0);
		int no = sheet.getNumMergedRegions();

		// List<CellRangeAddress> no = sheet.getMergedRegions();
		for(int i=0;i<no;i++){	    	
			sheet.removeMergedRegion(i);
		}   

		/* Iterator<Row> rowIter = sheet.iterator();
	    while (rowIter.hasNext()) {
	      row = (HSSFRow)rowIter.next();
	      cell = row.getCell(colToRemove);

	      row.removeCell(cell);
	   }*/

		wb.write(new FileOutputStream(filePath));
		wb.close();
	}

	public static void removeEDSEmptyColumns(String filePath) throws IOException, WriteException {
		Workbook workbook = null;
		WritableSheet sheet = null;
		WritableWorkbook copy = null;
		try {

			workbook = Workbook.getWorkbook(new File(filePath));

			copy = Workbook.createWorkbook(new File(filePath), workbook);

			sheet = copy.getSheet(0);


			sheet.removeColumn(8);

			copy.write();
			copy.close();
		}
		catch(Exception e){
			e.printStackTrace();
			copy.write();
			copy.close();
		}


	}

	private static void removeEDSReportMergedCells(String filePath) throws IOException, WriteException {
		Workbook workbook = null;
		WritableSheet sheet = null;
		WritableWorkbook copy = null;
		try {

			workbook = Workbook.getWorkbook(new File(filePath));

			copy = Workbook.createWorkbook(new File(filePath), workbook);

			sheet = copy.getSheet(0);

			Range[] w = sheet.getMergedCells();
			System.out.println(w.toString());
			for(int i=0;i<w.length;i++){
				sheet.unmergeCells(w[i]);
			}
			copy.write();
			copy.close();
		}
		catch(Exception e){
			e.printStackTrace();
			copy.write();
			copy.close();
		}

	}

	public static void removeExtraColumnsAndAddHolderAccountInOldEDSReport(String sOldEDSReportFilePath) {
		try {

			//removeEDSReportMergedCells(sOldEDSReportFilePath);
			sheetSettings(sOldEDSReportFilePath);
			String noOfColumns = "0,39,40,41,42,43,44,45,46,50,51,52,53,54,55,56,57,62,69,70,71";
			//removeExtraColumnsInOldEDSReport(sOldEDSReportFilePath,noOfColumns);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private static void removeExtraColumnsInOldEDSReport(String sOldEDSReportFilePath,String noOfColumnsToDelete) throws IOException, WriteException {
		Workbook workbook = null;
		WritableSheet sheet = null;
		WritableWorkbook copy = null;
		try {

			workbook = Workbook.getWorkbook(new File(sOldEDSReportFilePath));

			copy = Workbook.createWorkbook(new File(sOldEDSReportFilePath), workbook);

			sheet = copy.getSheet(0);
			/*SheetSettings settings = sheet.getSettings();

		    int row = settings.getVerticalFreeze();

		    settings.setVerticalFreeze(0);*/

			//sheet.removeColumn(6);
			/*sheet.removeRow(3);
		    sheet.removeRow(4);*/
			/* List<String> no = Arrays.asList(noOfColumnsToDelete.split(","));
		    for(int i=0;i<no.size();i++){
		    	sheet.removeColumn(Integer.parseInt(no.get(i)));		    	
		    }*/


			copy.write();
			copy.close();
		}
		catch(Exception e){
			e.printStackTrace();
			copy.write();
			copy.close();
		}

	}
	private static void sheetSettings(String sOldEDSReportFilePath) throws IOException, WriteException {
		Workbook workbook = null;
		WritableSheet sheet = null;
		WritableWorkbook copy = null;
		try {

			workbook = Workbook.getWorkbook(new File(sOldEDSReportFilePath));

			copy = Workbook.createWorkbook(new File(sOldEDSReportFilePath), workbook);

			sheet = copy.getSheet(0);
			SheetSettings settings = sheet.getSettings();

		    int row = settings.getVerticalFreeze();

		    settings.setVerticalFreeze(0);
		    settings.setHorizontalFreeze(0);
		    boolean isHidden = settings.isHidden();
		    System.out.println("isHidden"+isHidden);
		    settings.setHidden(true);
		    settings.setHidden(false);
		    int col = settings.getDefaultColumnWidth();
		    int rowHight = settings.getDefaultRowHeight();
		    System.out.println("colWidth "+col+" Row Hieght "+rowHight);
		    
			/*sheet.removeRow(3);
		    sheet.removeRow(4);*/
			/* List<String> no = Arrays.asList(noOfColumnsToDelete.split(","));
		    for(int i=0;i<no.size();i++){
		    	sheet.removeColumn(Integer.parseInt(no.get(i)));		    	
		    }*/


			copy.write();
			copy.close();
		}
		catch(Exception e){
			e.printStackTrace();
			copy.write();
			copy.close();
		}

	}

	public static boolean selectDate(String buttonToOpenCalender,String moveToNextYearLink,String middleLink,String moveToPreviousYearLink,String headerOfYearAfterClickOnMiddle,String totalMonthsPageLocator,String totalDayPageLocator,String dateToSelect){
		try {
			String[] sMonths = {"January","February","March","April","May","June","July","August","September","October","November","December"};
			
			String sMonthIndex = Arrays.asList(dateToSelect.split("//")).get(0);
			
			String sOnMonth = sMonths[Integer.parseInt(sMonthIndex)-1];
			
			String sOnDay = Arrays.asList(dateToSelect.split("//")).get(1);
			
			String sOnYear = Arrays.asList(dateToSelect.split("//")).get(2);
			
			String sOnMonthAndYear = sOnMonth+" "+sOnYear;
			
			String sMiddleLinkToSwitchToYears = "//th[@class='datepicker-switch']";
			
			String sMiddleLinkLocatorWhenYearRageActive = "//th[@class='datepicker-switch' and contains(normalize-space(),'-')]";
			
			String sMiddleLinkYEAR_BACKLocatorWhenYearRageActive = "//th[@class='datepicker-switch' and contains(normalize-space(),'-')]//preceding-sibling::th[@class='prev']";
			
			String sMiddleLinkYEAR_NEXTLocatorWhenYearRageActive = "//th[@class='datepicker-switch' and contains(normalize-space(),'-')]//following-sibling::th[@class='next']";
			
			//DAte and Time to be set in textbox
			String dateTime = dateToSelect;
			WebDriver driver = Global.driver;
			//button to open calendar
			bStatus = Elements.click(Global.driver, By.xpath(buttonToOpenCalender));
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
			
			//button to move next in calendar
			WebElement nextLink = driver.findElement(By.xpath(moveToNextYearLink));
			//button to click in center of calendar header
			WebElement midLink = driver.findElement(By.xpath(middleLink));
			//button to move previous month in calendar
			WebElement previousLink = driver.findElement(By.xpath(moveToPreviousYearLink));
			//Split the date time to get only the date part
			String date_dd_MM_yyyy[] = dateTime.split("/");
			//get the year difference between current year and year to set in calander
			midLink.click();
			String currentYearValueInDatePicker = Elements.getText(Global.driver, By.xpath(headerOfYearAfterClickOnMiddle));
			int yearDiff = Integer.parseInt(date_dd_MM_yyyy[2])- Calendar.getInstance().get(Calendar.YEAR);
			midLink.click();
			if(yearDiff!=0){
				//if you have to move next year
				if(yearDiff>0){
					for(int i=0;i< yearDiff;i++){
						System.out.println("Year Diff->"+i);
						nextLink.click();
					}
				}
				//if you have to move previous year
				else if(yearDiff<0){
					for(int i=0;i< (yearDiff*(-1));i++){
						System.out.println("Year Diff->"+i);
						previousLink.click();
					}
				}
			}
			//Get all months from calendar to select correct one
			List<WebElement> list_AllMonthToBook = driver.findElements(By.xpath(totalMonthsPageLocator));
			list_AllMonthToBook.get(Integer.parseInt(date_dd_MM_yyyy[0])-1).click();
			//get all dates from calendar to select correct one
			List<WebElement> list_AllDateToBook = driver.findElements(By.xpath(totalDayPageLocator));
			list_AllDateToBook.get(Integer.parseInt(date_dd_MM_yyyy[1])-1).click();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			Messages.errorMsg = "[ ERROR : Wasn't able to select the given date : '"+dateToSelect+"' from the date picker.]\n";
			return false;
		}		
	}
}
