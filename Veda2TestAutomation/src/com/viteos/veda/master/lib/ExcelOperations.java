package com.viteos.veda.master.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.tenx.framework.lib.Messages;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Label;
import jxl.write.WriteException;


public class ExcelOperations {
	static FileOutputStream fo;
	static Workbook wbook;
	static WritableWorkbook wwbCopy;
	static String ExecutedTestCasesSheet;
	static WritableSheet shSheet;
	static WritableSheet wshTemp;



	public static void main(String[] args) throws WriteException, IOException
	{

		NewReporting.Functionality = "Investor 1";
		NewReporting.Testcasename = "ClickToViewResults";
		NewReporting.logResults("Pass", "ColumnName1", "New Value1", "Old Value1", "Difference1");
		NewReporting.logResults("Pass", "ColumnName2", "New Value2", "Old Value2", "Difference2");
		NewReporting.logResults("Pass", "ColumnName3", "New Value3", "Old Value3", "Difference3");
		NewReporting.Functionality = "Investor 2";
		NewReporting.Testcasename = "ClickToViewResults";
		NewReporting.logResults("Fail", "ColumnName4", "New Value4", "Old Value4", "Difference4");
		NewReporting.logResults("Pass", "ColumnName5", "New Value5", "Old Value5", "Difference5");
		NewReporting.logResults("Fail", "ColumnName6", "New Value6", "Old Value6", "Difference6");

		NewReporting.Functionality = "Investor 1";
		NewReporting.Testcasename = "ClickToViewResults";

		NewReporting.logResults("Fail", "ColumnName7", "New Value7", "Old Value7", "Difference7");
		NewReporting.logResults("Pass", "ColumnName8", "New Value8", "Old Value8", "Difference8");

		writeDataToExcel("D:","ConnectiveCapital","Data");
	}

	public static boolean writeDataToExcel(String filePathToCreate,String fileName,String sSheetName) throws IOException, WriteException{
		try{
			NewUICommonFunctions.DeleteFileIfExists(filePathToCreate+"\\"+fileName+".xls");
			Map<String,Map<String,Map<String,String>>> mapInvestorData = new HashMap<>();
			int rowCountercount = 1;

			fo = new FileOutputStream(filePathToCreate+"\\"+fileName+".xls");
			wwbCopy = Workbook.createWorkbook(fo);
			CellView cell; 
			//Add the Main Column Names
			wshTemp = wwbCopy.createSheet(sSheetName, 0);
			Label labTemp = null ;
			labTemp = new Label(0, 0, "Investor Name");
			wshTemp.addCell(labTemp);
			labTemp = new Label(1, 0, "Column Name");
			wshTemp.addCell(labTemp);
			labTemp = new Label(2, 0, "VEDA2.0");
			wshTemp.addCell(labTemp);
			labTemp = new Label(3, 0, "VEDA1.5");
			wshTemp.addCell(labTemp);
			labTemp = new Label(4, 0, "Difference");
			wshTemp.addCell(labTemp);
			labTemp = new Label(5, 0, "Status");
			wshTemp.addCell(labTemp);

			mapInvestorData = NewReporting.getXMLDetailsMap(NewReporting.sResultXMLFilePath);
			for(int invCount = 0;invCount<mapInvestorData.size();invCount++){
				Map<String,Map<String,String>> mapTotalColumnsData = mapInvestorData.get("InvestorRow"+invCount);
				for(int columnCount = 0;columnCount <mapTotalColumnsData.size();columnCount++){
					Map<String,String> mapIndVColumndata = mapTotalColumnsData.get("Row"+columnCount);

					for (String keyname : mapIndVColumndata.keySet()) {
						switch (keyname){
						case "Investor Name" : labTemp = new Label(0, rowCountercount, mapIndVColumndata.get(keyname));
						cell=wshTemp.getColumnView(0);
						cell.setAutosize(true);
						wshTemp.setColumnView(0, cell);
						break;
						case "stepname" : labTemp = new Label(1, rowCountercount, mapIndVColumndata.get(keyname));
						cell=wshTemp.getColumnView(1);
						cell.setAutosize(true);
						wshTemp.setColumnView(1, cell);
						break;
						case "VEDA2.0" : labTemp = new Label(2, rowCountercount, mapIndVColumndata.get(keyname));
						cell=wshTemp.getColumnView(2);
						cell.setAutosize(true);
						wshTemp.setColumnView(2, cell);
						break;
						case "VEDA1.5" : labTemp = new Label(3, rowCountercount, mapIndVColumndata.get(keyname));
						cell=wshTemp.getColumnView(3);
						cell.setAutosize(true);
						wshTemp.setColumnView(3, cell);
						break;
						case "Difference" : labTemp = new Label(4, rowCountercount, mapIndVColumndata.get(keyname));
						cell=wshTemp.getColumnView(4);
						cell.setAutosize(true);
						wshTemp.setColumnView(4, cell);
						break;
						case "status" : labTemp = new Label(5, rowCountercount, mapIndVColumndata.get(keyname));
						cell=wshTemp.getColumnView(5);
						cell.setAutosize(true);
						wshTemp.setColumnView(5, cell);
						break;
						default : continue;
						}
						wshTemp.addCell(labTemp);

					}
					rowCountercount++;
				}
			}		
			// Closing the writable work book
			wwbCopy.write();
			wwbCopy.close();

			return true;
		}
		catch(Exception e){ 
			e.printStackTrace();
			wwbCopy.write();
			wwbCopy.close();
			return false;
		}
	}
	//***************************************************************************************************************************************************
	public static Map<String, String> objMap = null;
	public static Map<String, String> readDataABasedOnCellNameforEDS(String sFilePath,String sSheetName, String sTestCaseName){
		String sKey = null;
		String sValue = null;
		try {
			objMap = new HashMap<String, String>();
			Workbook objWorkbook = Workbook.getWorkbook(new File(sFilePath));
			Sheet objSheet = objWorkbook.getSheet(sSheetName);
			int iRowCount = objSheet.getRows();
			int iColCount = objSheet.getColumns();
			for (int iRowCounter = 8; iRowCounter < iRowCount; iRowCounter++) {
				for (int iColCounter = 0; iColCounter < iColCount; iColCounter++) {
					sValue = objSheet.getCell(iColCounter, iRowCounter).getContents();
					if(sValue.equalsIgnoreCase(sTestCaseName)){
						for (int iColCounter1 = 0; iColCounter1 < iColCount; iColCounter1++) {
							sKey = objSheet.getCell(iColCounter1,8).getContents();
							System.out.println(sKey);
							sValue = objSheet.getCell(iColCounter1, iRowCounter).getContents();
							System.out.println(sValue);
							sValue = sValue.trim();
							if ((!sValue.equalsIgnoreCase("Null"))&& (sValue.trim().length() != 0)) {
								objMap.put(sKey, sValue);
								//System.out.println(sKey+"--->"+sValue);
							}
						}
						return objMap;
						//break;
					}
				}
				//break;
			}
		}
		catch (Exception e) {
			Messages.errorMsg = "Exception occured.." + e.getMessage();
		}
		return objMap;
	}

	//***********************************************************************************************************************************************************

	public static Map<String, String> readDataABasedOnCellNameforEDSColumnWise(String sFilePath,String sSheetName, String sTestCaseName){
		String sKey = null;
		String sValue =null;
		double sValue1 = 0;
		try {
			objMap = new HashMap<String, String>();
			FileInputStream fi1 = new FileInputStream(sFilePath);
			HSSFWorkbook objWorkbook = new HSSFWorkbook(fi1);
			HSSFSheet objSheet = objWorkbook.getSheet(sSheetName);
			int iColCount = 8;
			for (int iColCounter = 3; iColCounter < iColCount; iColCounter++) {
				sValue = objSheet.getRow(5).getCell(iColCounter).getStringCellValue();
				if (sValue.equalsIgnoreCase("null")) {
					continue;
				}
				if(sValue.equalsIgnoreCase(sTestCaseName)){
					for (int iRowCounter1 = 7; iRowCounter1 < 60; iRowCounter1++) {

						try {
							sKey = objSheet.getRow(iRowCounter1).getCell(1).getStringCellValue();
						} catch (Exception e) {
							//e.printStackTrace();
							continue;
						}

						try {
							sValue1 = new BigDecimal(objSheet.getRow(iRowCounter1).getCell(iColCounter).getNumericCellValue()).setScale(10, RoundingMode.HALF_UP).doubleValue();
						} catch (Exception e) {
							//e.printStackTrace();
							continue;
						}
						objMap.put(sKey, String.valueOf(sValue1));
						System.out.println(sKey+"--->"+sValue1);
					}
					return objMap;
					//break;
				}

			}
		}
		catch (Exception e) {
			Messages.errorMsg = "Exception occured.." + e.getMessage();
		}
		return objMap;
	}
	//***********************************function to read multiple data from New VEDA2.0 EDS Fund wise Report ******************************************************
	public static Map<String, Map<String, String>> readMultipleDataFromEDSReport(String sFilePath1, String sSheetName1)
	{
		Map<String,Map<String,String>> mapTotalData = null;
		try {
			Workbook wb1= Workbook.getWorkbook(new File(sFilePath1));
			Sheet s1=wb1.getSheet(sSheetName1);
			Map<String, String> InvestorMap1 = new HashMap<>();
			mapTotalData = new HashMap<>();
			int count = 0;
			for (int row = 10; row < s1.getRows(); row++) {
				if (!s1.getCell(0, row).getContents().isEmpty() && !s1.getCell(1, row).getContents().isEmpty()) {
					String seriesName= s1.getCell(4, row).getContents();
					InvestorMap1 = readDataABasedOnCellNameforEDS(sFilePath1, sSheetName1, s1.getCell(4, row).getContents());
					//System.out.println(InvestorMap1);
					count++;
					mapTotalData.put("Row"+count, InvestorMap1);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapTotalData ;

	}

	//************************************************************* Read data based on cell Name and RowIndex ****************************************
	public static Map<String, String> readDataABasedOnCellNameforEDSBasedOnRowIndex(String sFilePath,String sSheetName, String sTestCaseName, int rowIndex){
		String sKey = null;
		String sValue = null;
		try {
			objMap = new HashMap<String, String>();
			Workbook objWorkbook = Workbook.getWorkbook(new File(sFilePath));
			Sheet objSheet = objWorkbook.getSheet(sSheetName);
			int iRowCount = objSheet.getRows();
			int iColCount = objSheet.getColumns();
			for (int iRowCounter = rowIndex; iRowCounter < iRowCount; iRowCounter++) {
				for (int iColCounter = 0; iColCounter < iColCount; iColCounter++) {
					sValue = objSheet.getCell(iColCounter, iRowCounter).getContents();
					if(sValue.equalsIgnoreCase(sTestCaseName)){
						for (int iColCounter1 = 0; iColCounter1 < iColCount; iColCounter1++) {
							sKey = objSheet.getCell(iColCounter1,rowIndex).getContents();
							sValue = objSheet.getCell(iColCounter1, iRowCounter).getContents();
							sValue = sValue.trim();
							if ((!sValue.equalsIgnoreCase("Null"))&& (sValue.trim().length() != 0)) {
								objMap.put(sKey, sValue);
								//System.out.println(sKey+"--->"+sValue);
							}
						}
						return objMap;
						//break;
					}
				}
				//break;
			}
		}
		catch (Exception e) {
			Messages.errorMsg = "Exception occured.." + e.getMessage();
		}
		return objMap;
	}



}
