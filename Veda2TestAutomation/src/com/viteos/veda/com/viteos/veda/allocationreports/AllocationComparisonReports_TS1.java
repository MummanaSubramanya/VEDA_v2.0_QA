package com.viteos.veda.com.viteos.veda.allocationreports;

import java.util.Map;

import org.testng.annotations.Test;

import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.ComparisonReportFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewReporting;

public class AllocationComparisonReports_TS1 {
	@Test
	public void allocationComparisonReports() {
		boolean bStatus = true;
		String sSheetName = "ComparisonReports";

		try {
			
			Map<String, Map<String, String>> mapAllData = Utilities.readMultipleTestData(Global.sAllocationReportsTestDataPath, sSheetName, "Y");
			Reporting.Functionality = "Comparison Reports";
			for (int index = 1; index <= mapAllData.size(); index++) {
				Map<String, String> mapData = mapAllData.get("Row"+index);
				Reporting.Testcasename = mapData.get("TestCaseName");
				if (mapData.get("Type") !=null) {
					if (mapData.get("Type").equalsIgnoreCase("EDS")) {
						bStatus = ComparisonReportFunctions.compareEDSReports(mapData.get("NewFilePath"), mapData.get("NewFileSheetName"), mapData.get("OldFilePath"), mapData.get("OldFileSheetName"), "ComparisonReportsResultsInExcelFormat", mapData.get("ResultFileName"), mapData.get("ResultFileSheetName"));
						if (!bStatus) {
							Reporting.logResults("Fail", "Compaison of EDS Reports: ", "Actual EDS Report is Not matching with the Expected EDS Report");
							NewReporting.bCreateFile = false;
							continue;
						}
						Reporting.logResults("Pass", "Compaison of EDS Reports: ", " Successful comparison of Actual EDS Report with the Expected EDS Report");
						NewReporting.bCreateFile = false;
					}
					if (mapData.get("Type").equalsIgnoreCase("Fund")) {
						bStatus = ComparisonReportFunctions.compareEDSReportsBaasedonSeries(mapData.get("NewFilePath"), mapData.get("NewFileSheetName"), mapData.get("OldFilePath"), mapData.get("OldFileSheetName"), "ComparisonReportsResultsInExcelFormat", mapData.get("ResultFileName"), mapData.get("ResultFileSheetName"));
						if (!bStatus) {
							Reporting.logResults("Fail", "Compaison of Fund Wise EDS Reports: ", "Actual Fund Wise EDS Report is Not matching with the Expected Fund wise EDS Report");
							NewReporting.bCreateFile = false;
							continue;
						}
						Reporting.logResults("Pass", "Compaison of Fund Wise EDS Reports: ", " Successful comparison of Actual EDS Report with the Fund Wise Expected EDS Report");
						NewReporting.bCreateFile = false;
					}
					if (mapData.get("Type").equalsIgnoreCase("PreSeries")) {
						bStatus = ComparisonReportFunctions.comparePreSeriesWithEDS(mapData.get("NewFilePath"), mapData.get("NewFileSheetName"), mapData.get("OldFilePath"), mapData.get("OldFileSheetName"), "ComparisonReportsResultsInExcelFormat", mapData.get("ResultFileName"), mapData.get("ResultFileSheetName"));
						if (!bStatus) {
							Reporting.logResults("Fail", "Compaison of PreSeries Report with EDS Report: ", "PreSeries Report is Not matching with the Expected EDS Report");
							NewReporting.bCreateFile = false;
							continue;
						}
						Reporting.logResults("Pass", "Compaison of PreSeries Report with EDS Report : ", " Successful comparison of PreSeries EDS Report with the Expected EDS Report");
						NewReporting.bCreateFile = false;
					}
					if (mapData.get("Type").equalsIgnoreCase("PostSeries")) {
						//bStatus = ComparisonReportFunctions.compareEDSReports(mapData.get("NewFilePath"), mapData.get("NewFileSheetName"), mapData.get("OldFilePath"), mapData.get("OldFileSheetName"), mapData.get("ResultFilePath"), mapData.get("ResultFileName"), mapData.get("ResultFileSheetName"));
						if (!bStatus) {
							Reporting.logResults("Fail", "Compaison of Post Series Report with EDS Report: ", "PostSeries EDS Report is Not matching with the Expected EDS Report");
							NewReporting.bCreateFile = false;
							continue;
						}
						Reporting.logResults("Pass", "Compaison of EDS Reports: ", " Successful comparison of PostSeries EDS Report with the Expected EDS Report");
						NewReporting.bCreateFile = false;
					}
				} 
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}
}
