package com.akoolla.financebeta.reporting;

import java.util.Set;

import org.joda.time.DateTime;

public interface ITransactionQuery {
	DateTime getFromDate();
	DateTime getToDate();
	Set<String> getTransactionTags();
	double transactionAmount();
	//Range of transactionAmmounts();
	double minimumTransactionAmount();
	double maximumTransactionAmount();
	boolean showSceduledTransactionsOnly();
}
