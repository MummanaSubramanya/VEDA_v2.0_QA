package com.viteos.veda.master.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;

import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.ExcelOperations;
import com.viteos.veda.master.lib.NewReporting;



public class ComparisonReportFunctions {

	//****************************************************comparing old and New EDS Reports for individual funds
	public static boolean compareEDSReportsBaasedonSeries(String sNewFilePath, String sNewSheetName, String sOldFilePath, String sOldSheetName, String sNewFileToCreatePath, String sNewFileToCreateName, String sNewFileToCreateSheetName)
	{
		boolean bValidStatus = true ;
		boolean bStatus = true ;
		try {

			Map<String, Map<String, String>> mapAllNewFileData =  ExcelOperations.readMultipleDataFromEDSReport(sNewFilePath, sNewSheetName);
			System.out.println(mapAllNewFileData.size());
			for (int i = 1; i <= mapAllNewFileData.size(); i++) {
				Map<String, String> mapNewFileData = mapAllNewFileData.get("Row"+i);
				System.out.println(mapNewFileData);
				String seriesName = mapNewFileData.get("Series");
				System.out.println(seriesName);
				Map<String, String> mapOldFileData = ExcelOperations.readDataABasedOnCellNameforEDSColumnWise(sOldFilePath, sOldSheetName, seriesName);
				System.out.println(mapOldFileData);
				NewReporting.Testcasename = "Click TO View Results";
				NewReporting.Functionality = seriesName;
				bStatus = compareTotalValuesInSeries(mapNewFileData, mapOldFileData);
				if (!bStatus) {
					Reporting.logResults("Fail", "Comparison of EDS_Report for Series:"+mapNewFileData.get("Series"),"Failed to Compare EDS Reports\n"+Messages.errorMsg);
					bValidStatus =  false;
				}
				Reporting.logResults("Pass", "Comparison of EDS_Report for Series:"+mapNewFileData.get("Series"),"Successful to Compare EDS Reports\n");
			}

			bStatus = ExcelOperations.writeDataToExcel(sNewFileToCreatePath, sNewFileToCreateName, sNewFileToCreateSheetName);
			if (!bStatus) {
				Reporting.logResults("Fail", "Creation of New EDS Comparision Report in Excel in the Path: "+sNewFileToCreatePath, "Failed to create New Excel File Report");
				bValidStatus = false;
			}
			Reporting.logResults("Pass", "Creation of New EDS Comparision Report in Excel in the Path: "+sNewFileToCreatePath, "Successfully created New Excel File Report");


		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
		return bValidStatus;

	}

	//***************************************** function to compare Total values in Series ********************************************************
	public static boolean compareTotalValuesInSeries(Map<String, String> mapNewFileData, Map<String, String> mapOldFileData)
	{
		boolean bValidStatus = true ;
		boolean bStatus = true;
		String appendErrorMsg = "";
		try {
			bStatus = compareValues(mapNewFileData.get("Gross Asset Value (Opening)"), mapOldFileData.get("Opening GROSS ASSETS"), "Gross Asset Value (Opening)");
			if (!bStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus =  false;
			}

			bValidStatus = compareValues(mapNewFileData.get("Additions"), mapOldFileData.get("Net Subscriptions/Redemptions"), "Additions");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

			bValidStatus = compareValues(mapNewFileData.get("Current PTD Gross P/L"), mapOldFileData.get("Allocation of Net Income / (Loss) before Mgt fee"), "Current PTD Gross P/L");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

			bValidStatus = compareValues(mapNewFileData.get("Total Management Fees"), mapOldFileData.get("Allocation of Management Fee"), "Total Management Fees");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

			bValidStatus = compareValues(mapNewFileData.get("Gross Asset Value (Closing)"), mapOldFileData.get("GROSS ASSETS AT END OF CURRENT MONTH"), "Gross Asset Value (Closing)");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

			bValidStatus = compareValues(mapNewFileData.get("Units (Opening)"), mapOldFileData.get("Number of Shares Outstanding at End of Prior Period"), "Units (Opening)");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

			bValidStatus = compareValues(mapNewFileData.get("Added Units"), mapOldFileData.get("Net Subs/Reds"), "Added Units");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

			bValidStatus = compareValues(mapNewFileData.get("Net Asset Value(Closing)"), mapOldFileData.get("NET ASSETS AT END OF CURRENT MONTH"), "Net Asset Value(Closing)");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

			bValidStatus = compareValues(mapNewFileData.get("Units(Closing)"), mapOldFileData.get("Number of Shares Outstanding at End of Current Period"), "Units(Closing)");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

			bValidStatus = compareValues(mapNewFileData.get("NAV per unit"), mapOldFileData.get("NET ASSET VALUE PER SHARE"), "NAV per unit");
			if (!bValidStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapNewFileData.get("Series")+"]\n";
				bValidStatus = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = appendErrorMsg ;
		return bValidStatus;

	}

	//*****************************************function to compare Individual Series values *******************************************************

	public static boolean compareValues(String newFileColName, String oldFileColName, String colNameToDisplayinLog)
	{
		try {
			BigDecimal newValue = new BigDecimal(0.0);
			BigDecimal oldValue = new BigDecimal(0.0);
			BigDecimal diffValue = new BigDecimal(0.0);
			
			newValue = new BigDecimal(newFileColName);
			
			oldValue = new BigDecimal(oldFileColName);
			
			diffValue = newValue.subtract(oldValue);
			
			if (newValue.doubleValue() == oldValue.doubleValue()) {	
				NewReporting.logResults("Pass", colNameToDisplayinLog, String.valueOf(newValue), String.valueOf(oldValue), String.valueOf(diffValue));
			}
			else
			{
				NewReporting.logResults("Fail", colNameToDisplayinLog, String.valueOf(newValue), String.valueOf(oldValue), String.valueOf(diffValue));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
		return true;
	}

	//*************************function to round the double values to specified decimal places
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
	//******************************************** Comparing EDS Reports OLD EDS with NEW EDS ******************************************************************
	public static boolean compareEDSReports(String sFilePath1, String sSheetName1, String sFilePath2, String sSheetName2, String sNewFileToCreatePath, String sNewFileToCreateName, String sNewFileToCreateSheetName)
	{
		boolean bValidStatus = true;
		boolean bStatus = true;
		String appendErrorMsg = "";
		try {
			
			removeEDSReportMergedCells(sFilePath1);
			removeEDSEmptyColumns(sFilePath1);
			FileInputStream fi1 = new FileInputStream(sFilePath1);
			HSSFWorkbook wb1= new HSSFWorkbook(fi1);
			HSSFSheet s1= wb1.getSheet(sSheetName1);

			FileInputStream fi2 =  new FileInputStream(sFilePath2);
			HSSFWorkbook wb2= new HSSFWorkbook(fi2);
			HSSFSheet s2=wb2.getSheet(sSheetName2);
			String sDiff =  null;
			BigDecimal  iDiff ;
			NumberFormat percentFormat = NumberFormat.getPercentInstance();

			for (int row = 10; row <s1.getLastRowNum(); row++) {
				//System.out.println(s1.getLastRowNum());
				if (!s1.getRow(row).getCell(0).getStringCellValue().isEmpty() && !s1.getRow(row).getCell(2).getStringCellValue().isEmpty()) {
					String invName = s1.getRow(row).getCell(0).getStringCellValue();
					System.out.println(invName);
					NewReporting.Functionality = invName ;
					NewReporting.Testcasename = "Click To View Results";
					int colCount= s1.getRow(8).getLastCellNum();
					//System.out.println(colCount);
					for (int col = 0; col < colCount; col++) {
						String colName =  s1.getRow(8).getCell(col).getStringCellValue();
						//System.out.println(colName);
						String sOldValue = "";
						switch (s1.getRow(row).getCell(col).getCellType()) {
						case Cell.CELL_TYPE_STRING:
							String sNewValue = s1.getRow(row).getCell(col).getRichStringCellValue().getString();
							
							if(s2.getRow(row).getCell(col).getCellType() == Cell.CELL_TYPE_STRING ){
								sOldValue = s2.getRow(row).getCell(col).getRichStringCellValue().getString();
							}else{
								BigDecimal iOldvalue = null ;
								if(s2.getRow(row).getCell(col).getCellType() == Cell.CELL_TYPE_NUMERIC ){
									DecimalFormat df = new DecimalFormat("#.0000");
									//System.out.print(df.format(cell.getNumericCellValue()));
									iOldvalue = new BigDecimal(df.format(s2.getRow(row).getCell(col).getNumericCellValue()));
								}
								sOldValue = ""+iOldvalue;
							}
							
							
							if (!sOldValue.equalsIgnoreCase(sNewValue)) {
								NewReporting.logResults("Fail", colName, sNewValue, sOldValue, "NA");
								bValidStatus = false;
							}
							else{
								NewReporting.logResults("Pass", colName, sNewValue, sOldValue, "NA");

							}
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (DateUtil.isCellDateFormatted(s1.getRow(row).getCell(col))) {
								Date dNewValue = s1.getRow(row).getCell(col).getDateCellValue();
								Date dOldValue = s2.getRow(row).getCell(col).getDateCellValue();
								//System.out.println(dOldValue);
								//System.out.println(dNewValue);
							} else {

								DecimalFormat df = new DecimalFormat("#.0000");
								//System.out.print(df.format(cell.getNumericCellValue()));
								BigDecimal iNewValue = new BigDecimal(df.format(s1.getRow(row).getCell(col).getNumericCellValue()));
								BigDecimal iOldvalue = null;
								if(s2.getRow(row).getCell(col).getCellType() == Cell.CELL_TYPE_NUMERIC ){
									iOldvalue = new BigDecimal(df.format(s2.getRow(row).getCell(col).getNumericCellValue()));
									
								}else{									
									if(s2.getRow(row).getCell(col).getCellType() == Cell.CELL_TYPE_STRING ){
										sOldValue = s2.getRow(row).getCell(col).getRichStringCellValue().getString();
										NewReporting.logResults("Fail", colName, ""+iNewValue, sOldValue, "NA");
										break;
									}
								}
								if(iOldvalue == null ){
									NewReporting.logResults("Fail", colName, ""+iNewValue, ""+iOldvalue, "NA");
									break;
								}

								iDiff = iNewValue.subtract(iOldvalue) ;
								System.out.println(iDiff);
								if (colName.contains("%")|| colName.equalsIgnoreCase("Net PTD") || colName.equalsIgnoreCase("Net MTD") || colName.equalsIgnoreCase("Net QTD") || colName.equalsIgnoreCase("Net YTD") || colName.equalsIgnoreCase("Net ITD")|| colName.equalsIgnoreCase("Hurdle Rate")) {
									percentFormat.setMaximumFractionDigits(3);
									String iOld = percentFormat.format(iOldvalue);
									String iNew = percentFormat.format(iNewValue);
									String iDifference = percentFormat.format(iDiff);
									if (iDiff.doubleValue() != 0) {
										NewReporting.logResults("Fail", colName, iNew, iOld, iDifference);
										bValidStatus = false;
									}
									else{
										NewReporting.logResults("Pass", colName, iNew, iOld, iDifference);
									}
								}else{
									if (iDiff.doubleValue() != 0) {

										NewReporting.logResults("Fail", colName, iNewValue.toString(), iOldvalue.toString(), iDiff.toString());
										bValidStatus = false;
									}
									else
									{
										NewReporting.logResults("Pass", colName, iNewValue.toString(), iOldvalue.toString(), iDiff.toString());
									}
								}
							}
							break;
						default:

						}
					}

				}
			}
			if(NewReporting.sResultXMLFilePath.isEmpty()){
				Messages.errorMsg = "Results are not created Please check the EDS Reports";
				return false;
			}
			bStatus = ExcelOperations.writeDataToExcel(sNewFileToCreatePath, sNewFileToCreateName, sNewFileToCreateSheetName);
			if(!bStatus){
				bValidStatus = false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messages.errorMsg = "Exception in Comparing the Results"+appendErrorMsg;
			return false;
		}
		return bValidStatus;
	}

	

	//**************************************function to compare PreSeries Roll up Report with EDS Report **************************************************** 
	public static boolean comparePreSeriesWithEDS(String sEDSFilePath, String sEDSSheetName, String sPreSeriesFilePath, String sPreSeriesSheetName, String sNewFileToCreatePath, String sNewFileToCreateName, String sNewFileToCreateSheetName)
	{

		boolean bValidStatus = true ;
		boolean bStatus = true ;
		try {
			Map<String, Map<String, String>> mapAllData = newEDSFundReport(sEDSFilePath, sEDSSheetName);
			System.out.println(mapAllData.size());
			//Reporting.Functionality = mapAllData.get("Investor Name");
			Reporting.Testcasename = "PreSeriesComparisonReport with EDS Report";
			for (int index= 1; index <=mapAllData.size(); index++) {
				Map<String, String> mapEDSData = mapAllData.get("Row"+index);
				//System.out.println(mapEDSData);
				String seriesName = mapEDSData.get("Series");
				System.out.println(seriesName);
				Map<String, String> mapPreSeriesData = ExcelOperations.readDataABasedOnCellNameforEDSBasedOnRowIndex(sPreSeriesFilePath, sPreSeriesSheetName, seriesName, 5);
				System.out.println(mapPreSeriesData);

				NewReporting.Testcasename = "Click TO View Results";
				NewReporting.Functionality = seriesName;
				bStatus = compareTotalPreSeriesValuesWithEDSValues(mapEDSData, mapPreSeriesData);
				if (!bStatus) {
					Reporting.logResults("Fail", "Comparison of EDS_Report with PreSeries RollUp Daata: "+mapEDSData.get("Series"),"Failed to compare PreSeries Roll up Report with EDS Reports\n"+Messages.errorMsg);
					bValidStatus =  false;
					continue;
				}
				Reporting.logResults("Pass", "Comparison of EDS_Report with PreSeries RollUp:"+mapEDSData.get("Series"),"Successful to Compare Pre Series Roll UP with EDS Reports\n");
			}

			bStatus = ExcelOperations.writeDataToExcel(sNewFileToCreatePath, sNewFileToCreateName, sNewFileToCreateSheetName);
			if (!bStatus) {
				Reporting.logResults("Fail", "Creation of New EDS Comparision Report in Excel in the Path: "+sNewFileToCreatePath, "Failed to create New Excel File Report");
				bValidStatus = false;
			}
			Reporting.logResults("Pass", "Creation of New EDS Comparision Report in Excel in the Path: "+sNewFileToCreatePath, "Successfully created New Excel File Report");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidStatus;

	}
	//***************************************function to compare the values of PreSeries Roll up with EDS Values *******************************************
	public static boolean compareTotalPreSeriesValuesWithEDSValues(Map<String, String> mapEDSData, Map<String, String> mapPreSeriesData)
	{
		boolean bValidStatus = true ;
		boolean bStatus = true;
		String appendErrorMsg = "";
		try {
			bStatus = comparePreSeriesStringValues(mapEDSData.get("Investor Name"), mapPreSeriesData.get("Investor Name"), "Investor Name");
			if (!bStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapEDSData.get("Series")+"]\n";
				bValidStatus =  false;
			}
			bStatus = comparePreSeriesStringValues(mapEDSData.get("Holder Name"), mapPreSeriesData.get("Holder Name"), "Holder Name");
			if (!bStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapEDSData.get("Series")+"]\n";
				bValidStatus =  false;
			}
			/*bStatus = comparePreSeriesValues(mapEDSData.get("Account"), mapPreSeriesData.get("Account ID"), "Account ID");
				if (!bStatus) {
					appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapEDSData.get("Series")+"]\n";
					bValidStatus =  false;
				}*/
			bStatus = comparePreSeriesStringValues(mapEDSData.get("Series"), mapPreSeriesData.get("Series"), "Series");
			if (!bStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapEDSData.get("Series")+"]\n";
				bValidStatus =  false;
			}
			bStatus = comparePreSeriesNumericValues(mapEDSData.get("Nav per unit"), mapPreSeriesData.get("Nav Per Unit"), "Nav Per Unit");
			if (!bStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapEDSData.get("Series")+"]\n";
				bValidStatus =  false;
			}
			bStatus = comparePreSeriesNumericValues(mapEDSData.get("Units(Closing"), mapPreSeriesData.get("Closing Unit"), "Closing Unit");
			if (!bStatus) {
				appendErrorMsg = appendErrorMsg + "[Error: Unable to compare the values for Series: "+mapEDSData.get("Series")+"]\n";
				bValidStatus =  false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Messages.errorMsg = appendErrorMsg ;
		return bValidStatus;
	}

	//*****************************************function to compare Individual Series Numeric values *******************************************************

	public static boolean comparePreSeriesNumericValues(String newFileColName, String oldFileColName, String colNameToDisplayinLog)
	{
		try {
			double newValue = 0;
			double oldValue = 0;
			double diffValue = 0;
			if (newFileColName !=null && oldFileColName !=null) {
				newValue = round(Double.parseDouble(newFileColName), 2);
				oldValue = round(Double.parseDouble(oldFileColName), 2);
				diffValue = newValue - oldValue ;
				diffValue = round(diffValue, 2);
			}
			if (newFileColName == null && oldFileColName != null) {
				newValue = 0;
				oldValue = round(Double.parseDouble(oldFileColName), 2);
				diffValue = newValue - oldValue ;
				diffValue = round(diffValue, 2);
			}
			if (newFileColName != null && oldFileColName == null) {
				newValue = round(Double.parseDouble(newFileColName), 2);
				oldValue = 0;
				diffValue = newValue - oldValue ;
				diffValue = round(diffValue, 2);
			}
			if (round(newValue, 2) == round(oldValue, 2)) {
				NewReporting.logResults("Pass", colNameToDisplayinLog, String.valueOf(newValue), String.valueOf(oldValue), String.valueOf(diffValue));
			}
			else
			{
				NewReporting.logResults("Fail", colNameToDisplayinLog, String.valueOf(newValue), String.valueOf(oldValue), String.valueOf(diffValue));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
		return true;
	}
	//*****************************************function to compare Individual Series String values *******************************************************
	public static boolean comparePreSeriesStringValues(String newFileColName, String oldFileColName, String colNameToDisplayinLog)
	{
		try {
			String newValue = newFileColName;
			String oldValue = oldFileColName;

			if (newValue.equalsIgnoreCase(oldValue)) {
				NewReporting.logResults("Pass", colNameToDisplayinLog, String.valueOf(newValue), String.valueOf(oldValue), "N/A");
			}
			else
			{
				NewReporting.logResults("Fail", colNameToDisplayinLog, String.valueOf(newValue), String.valueOf(oldValue), "N/A");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
		return true;
	}
	//***********************************function to read the data from New VEDA2.0 EDS Fund wise Report ******************************************************
	public static Map<String, Map<String, String>> newEDSFundReport(String sFilePath1, String sSheetName1)
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
					InvestorMap1 = ExcelOperations.readDataABasedOnCellNameforEDSBasedOnRowIndex(sFilePath1, sSheetName1, s1.getCell(4, row).getContents(), 8);
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
			
			removeEDSReportMergedCells(sOldEDSReportFilePath);
			String noOfColumns = "0,39,40,41,42,43,44,45,46,50,51,52,53,54,55,56,57,62,69,70,71";
			removeExtraColumnsInOldEDSReport(sOldEDSReportFilePath,noOfColumns);
			
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
		    
		    List<String> no = Arrays.asList(noOfColumnsToDelete.split(","));
		    for(int i=0;i<no.size();i++){
		    	sheet.removeColumn(Integer.parseInt(no.get(i)));		    	
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

}

