package com.viteos.veda.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import com.tenx.framework.lib.Messages;

public class ExcelUtils {


	private static Map<String, String> objMap = null;
	public static Map<String, String> readDataABasedOnCellName(String sFilePath,String sSheetName, String sTestCaseName){
		String sKey = null;
		String sValue = null;
		try {
			objMap = new HashMap<String, String>();
			Workbook objWorkbook = Workbook.getWorkbook(new File(sFilePath));
			Sheet objSheet = objWorkbook.getSheet(sSheetName);
			int iRowCount = objSheet.getRows();
			int iColCount = objSheet.getColumns();
			for (int iRowCounter = 0; iRowCounter < iRowCount; iRowCounter++) {
				for (int iColCounter = 0; iColCounter < iColCount; iColCounter++) {
					sValue = objSheet.getCell(iColCounter, iRowCounter).getContents();
					if(sValue.equalsIgnoreCase(sTestCaseName)){
						for (int iColCounter1 = 0; iColCounter1 < iColCount; iColCounter1++) {
							sKey = objSheet.getCell(iColCounter1,0).getContents();
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
