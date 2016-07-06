package com.viteos.veda.unusedwork.templibrary;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.Format;
import jxl.write.WritableCellFormat;

import com.gargoylesoftware.htmlunit.javascript.host.intl.NumberFormat;
import com.tenx.framework.lib.Messages;
import com.viteos.veda.master.lib.ExcelOperations;
import com.viteos.veda.master.lib.NewReporting;

public class SampleEDS1 {
	public static boolean test(String sFilePath1, String sSheetName1, String sFilePath2, String sSheetName2)
	{
		try {
			Workbook wb1= Workbook.getWorkbook(new File(sFilePath1));
			Sheet s1=wb1.getSheet(sSheetName1);
			
			Workbook wb2= Workbook.getWorkbook(new File(sFilePath2));
			Sheet s2=wb2.getSheet(sSheetName2);
			
			
			for (int row = 10; row < s1.getRows(); row++) {
				if (!s1.getCell(0, row).getContents().isEmpty() && !s1.getCell(1, row).getContents().isEmpty()) {
					String investorName= s1.getCell(0, row).getContents();
					System.out.println(s1.getCell(0, row).getContents());
					//NewReporting.Functionality = investorName ;
					//NewReporting.Testcasename = "Click To View Results" ;
					//Map<String, String> InvestorMap1 = readDataABasedOnCellNameforEDS(sFilePath1, sSheetName1, s1.getCell(0, row).getContents());
					//System.out.println(InvestorMap1);
					//Map<String, String> InvestorMap2 = readDataABasedOnCellNameforEDS(sFilePath2, sSheetName2, s2.getCell(0, row).getContents());
					//System.out.println(InvestorMap2);
					for (int colIndex = 0; colIndex < s1.getColumns(); colIndex++) {
						s1.getCell(colIndex, 8).toString();
						String colName = s1.getCell(colIndex, 8).getContents();
						s1.getCell(colIndex, row).toString();
						String actualValue = s1.getCell(colIndex, row).getContents();
						s2.getCell(colIndex, row).getContents().trim();
						String expectedValue = s2.getCell(colIndex, row).getContents();
						//System.out.println("******Column Formate ***************: "+s2.getSettings().toString());
						
						System.out.println(colName+"\t\t"+actualValue+"\t\t"+expectedValue);
					}
					
					//compare(InvestorMap1, InvestorMap2);
					//ExcelOperations.writeDataToExcel("D://Sample", "SampleResults", "SampleData");
				}
			}


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test("D:\\Sample\\SeriesMasterRollupFund_31-Mar-2016.xls", "EDS_Report", "D:\\Sample\\SeriesMasterRollupFund_31-Mar-2016_Duplicate.xls", "EDS_Report");

	}
	//*******************************************************************************************************************************
	public static boolean compare(Map<String, String> newDataMap, Map<String, String> oldDataMap)
	{
		try {
			for (String k : newDataMap.keySet())
			{
				if (!newDataMap.get(k).equalsIgnoreCase(oldDataMap.get(k))) 
				{
					NewReporting.logResults("Fail", k, newDataMap.get(k), oldDataMap.get(k), "");
					
				}else{
					NewReporting.logResults("Pass", k, newDataMap.get(k), oldDataMap.get(k), "");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//***************************************************************************************************************************************
	private static Map<String, String> objMap = null;
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
