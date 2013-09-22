package com.akoolla.financebeta;

import java.util.Set;

import org.joda.time.DateTime;

import com.akoolla.financebeta.transaction.ITransaction;

public interface ITransactionSchedule {
	ITransaction getNextTransaction(DateTime fromDate);
	Set<ITransaction> getTransactionsForYear(int year, DateTime fromDate);
	Set<ITransaction> getTransactionsForMonth(int year, int month, DateTime fromDate);
	Set<ITransaction> getTransactionsForDay(int year, int month, int day);
}
