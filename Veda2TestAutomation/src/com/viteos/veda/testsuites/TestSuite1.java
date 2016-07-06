package com.viteos.veda.testsuites;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestSuite1 {


	public static void main(String[] args) {
		XmlSuite suite = new XmlSuite();
		suite.setName("FundSetupSuite");
		XmlTest test = new XmlTest(suite);
		test.setName("SeriesTests");
		List<XmlClass> classes = new ArrayList<XmlClass>();

		/*classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_CheckerOperations_TC2"));


		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_CheckerOperations_TC2"));*/


		/*classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_CheckerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityFeeder_MakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityFeeder_CheckerOperations_TC4"));*/



		/*classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerOperations_TC2"));*/

		/*classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_CheckerOperations_TC2"));*/


		/*classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_CheckerOperations_TC2"));*/

		/*classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS4"));*/


		/*classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerOperations_TS2"));*/


		/*classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerOperations_TS2"));*/


	   /* classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_CheckerOperations_TS2"));*/

		/*classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_CheckerOperations_TS2"));*/


	   //classes.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_MakerOperations_TC1"));
		
		//classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_MakerOperations_TS1"));
		//classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_CheckerOperations_TS2"));
		
		/*classes.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CreateSubscription_TestScript_TS1"));
		classes.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.SubscriptionCheckerOperation_TS2"));*/
		/*classes.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CheckerReviewdSubscriptionMakerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CheckerReviewedSubscriptionCheckerOperations_TC5"));*/


		/*classes.add(new XmlClass("com.viteos.veda.master.feedersubscriptiontestscripts.FeederSubscriptionMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.feedersubscriptiontestscripts.FeederSubscriptionMaster_CheckerOperations_TS2"));*/

/*
		classes.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.CreatingMakerRedemption_TC1"));
		classes.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.RedemptionCheckerOperation_TC2"));
		classes.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.CheckerReviewdRedemptionMaker_TC4"));
		classes.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.CheckerReviewdRedemptionCheckerOperation_TC5"));
		
		classes.add(new XmlClass("com.viteos.veda.transaction.transferscripts.Transfer_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.transaction.transferscripts.Transfer_CheckerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.transaction.transferscripts.TransferCheckerReviewed_MakerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.transaction.transferscripts.TransferCheckerReviewed_CheckerOperations_TC5"));
		
		classes.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CreatingMakerExchange_TC1"));
		classes.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.ExchangeCheckerOperation_TC2"));
		classes.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CheckerReviewdExchangeMakerOperation_TC3"));
		classes.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CheckerReviewedExchangeCheckerOperations_TC4"));
*/
		
                                                                                                                                 

		test.setXmlClasses(classes);

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);

		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.run();

		//int test  = Float.valueOf("8.0").intValue();
	}
}
