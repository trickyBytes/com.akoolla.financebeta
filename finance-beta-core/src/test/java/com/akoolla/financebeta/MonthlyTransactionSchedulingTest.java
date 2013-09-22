package com.akoolla.financebeta;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.akoolla.financebeta.scheduling.ScheduleType;
import com.akoolla.financebeta.transaction.ITransaction;
import com.akoolla.financebeta.transaction.Transaction;


@RunWith(MockitoJUnitRunner.class)
public class MonthlyTransactionSchedulingTest {
	
	final DateTime firstOfJan2013 =  new DateTime(2013,1,1,0,0);
	final ITransaction transaction =  new Transaction(100d, new String[] {"xzxx"});
	
	@Test
	public void NoTransactionsReturnedIfFromDateIsAfterEndDate() throws Exception {
		final DateTime afterTransactionEndDate = new DateTime(2014,1,1,0,0);
		ITransactionSchedule transactionSchedule = new TransactionSchedule(transaction, ScheduleType.MONTH, 1, new DateTime(2013,9,30,0,0), new DateTime(2013,9,30,0,0));
		
		assertThat("Next transaction from " + afterTransactionEndDate, transactionSchedule.getNextTransaction(afterTransactionEndDate), equalTo(null));
		assertThat("Transactions for 2013", transactionSchedule.getTransactionsForYear(2013, afterTransactionEndDate).size(), equalTo(0));
	}
	
	@Test
	public void IfTransactionIsScheduledAsMonthlyThenOnlyOneTransacionShouldBeReturnedForAMonth() throws Exception {
		ITransactionSchedule transactionSchedule = new TransactionSchedule(transaction, ScheduleType.MONTH, 1, new DateTime(2013,9,1,0,0), new DateTime(2013,9,30,0,0));
		
		Set<ITransaction> monthlyTransactions = transactionSchedule.getTransactionsForMonth(2013, 9, new DateTime(2013,9,1,0,0));
		assertThat("Transactions in month", monthlyTransactions.size(), equalTo(1));
	}
	
	@Test
	public void CanScheduleATransactionToHappenOnlyOnce() throws Exception {
		ITransactionSchedule transactionSchedule = new TransactionSchedule(transaction, ScheduleType.MONTH, 1, new DateTime(2013,9,30,0,0), new DateTime(2013,9,30,0,0));
	
		Set<ITransaction> transactions = new TreeSet<>();
		for(int year = 2013; year <= 3013; year++){
			transactions.addAll(transactionSchedule.getTransactionsForYear(year, firstOfJan2013));
		}
		
		assertThat("Transaction list", transactions, notNullValue());
		assertThat("Transaction list size", transactions.size(), equalTo(1));

		assertThat("Get next transaction should return the correct transaction", transactionSchedule.getNextTransaction(firstOfJan2013), notNullValue());
		assertThat("Expected next transaction date", new DateTime(2013,9,1,0,0), equalTo(transactionSchedule.getNextTransaction(firstOfJan2013).getTransactionDate()));
	}
	
	@Test
	public void CanSchedulaATransactionToHappenOnceEveryMonthUntilAGivenEndDate() throws Exception {
		DateTime scheduledEndDate = new DateTime(2015,6,30,0,0);
		ITransactionSchedule transactionSchedule = new TransactionSchedule(transaction, ScheduleType.MONTH, 1, firstOfJan2013, scheduledEndDate);
		
		assertThat("Expected transactions in 2013", transactionSchedule.getTransactionsForYear(2013, firstOfJan2013).size(), equalTo(12));
		assertThat("Expected transactions in 2014", transactionSchedule.getTransactionsForYear(2014, firstOfJan2013).size(), equalTo(12));
		assertThat("Expected transactions in 2015 if end date is:" + scheduledEndDate, transactionSchedule.getTransactionsForYear(2015, firstOfJan2013).size(), equalTo(6));
	}
	
	@Test
	public void CanScheduleAnOpenEndedTransactionToRunMonthlyFromAGivenStartDate() throws Exception {
		ITransactionSchedule transactionSchedule = new TransactionSchedule(transaction, ScheduleType.MONTH, 1, firstOfJan2013);
		
		for(int year = 2013; year <= 3013; year++){
			assertThat("Number of transactions in year:" + year, transactionSchedule.getTransactionsForYear(year, firstOfJan2013).size(), equalTo(12));
		}
	}
}
