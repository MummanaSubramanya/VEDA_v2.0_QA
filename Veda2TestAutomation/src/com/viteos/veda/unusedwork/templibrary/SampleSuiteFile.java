package com.viteos.veda.unusedwork.templibrary;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class SampleSuiteFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		XmlSuite suite = new XmlSuite();
		suite.setName("FundSetupSuite");
		XmlTest test = new XmlTest(suite);
		test.setName("SeriesTests");
		List<XmlClass> classes = new ArrayList<XmlClass>();


		/*classes.add(new XmlClass("com.viteos.veda.master.feederredemptiontestscripts.FeederRedemptionMaster_MakerOperations_TS1"));
			classes.add(new XmlClass("com.viteos.veda.master.feederredemptiontestscripts.FeederRedemptionMaster_CheckerOperations_TS2"));*/

		classes.add(new XmlClass("com.viteos.veda.transaction.transferscripts.Transfer_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.transaction.transferscripts.Transfer_CheckerOperations_TC2"));
		//classes.add(new XmlClass("com.viteos.veda.transaction.transferscripts.Transfer_VerifyViewButtons_TC3"));
		/*classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ModifyMakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ModifyCheckerOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_DeactivateMakerOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_DeactivateCheckerOperations_TS6"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ActivateMakerOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ActivateCheckerOperations_TS8"));*/
		test.setXmlClasses(classes);

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);

		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.run();

		//int test  = Float.valueOf("8.0").intValue();

	}

}
