package com.akoolla.financebeta;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.akoolla.financebeta.scheduling.ScheduleType;
import com.akoolla.financebeta.transaction.ITransaction;
import com.akoolla.financebeta.transaction.Transaction;

@RunWith(MockitoJUnitRunner.class)
public class DailyTransactionScheduleTest {

	final DateTime firstOfJan2013 =  new DateTime(2013,1,1,0,0).withZone(DateTimeZone.UTC);
	final ITransaction transaction =  new Transaction(100d, new String[] {"xzxx"});
	
	@Test
	public void CanScheduleATransactionToOccurForOneMonthOnly() throws Exception {
		ITransactionSchedule transactionSchedule = new TransactionSchedule(transaction, ScheduleType.DAY, 1, firstOfJan2013, new DateTime(2013,9,30,0,0, DateTimeZone.UTC));
		
		Set<ITransaction> monthlyTransactions = transactionSchedule.getTransactionsForMonth(2013, 9, new DateTime(2013,9,1,0,0));
		assertThat("Transaction list for September", monthlyTransactions, notNullValue());
		assertThat("Num of transactions in September", monthlyTransactions.size(), equalTo(30));
		
		//Test some days in schedule
		assertThat("Transaction list for 1,1,2013",  transactionSchedule.getTransactionsForDay(2013, 1, 1), notNullValue());
		assertThat("Transaction list for 1,1,2013",  transactionSchedule.getTransactionsForDay(2013, 1, 1).size(), equalTo(1));
		
		assertThat("Transaction list for 1,1,2013",  transactionSchedule.getTransactionsForDay(2013, 9, 30), notNullValue());
		assertThat("Transaction list for 1,1,2013",  transactionSchedule.getTransactionsForDay(2013, 9, 30).size(), equalTo(1));
		
		//Test get next transaction
		assertThat("Next transaction from 1,1,2013",  transactionSchedule.getNextTransaction(firstOfJan2013), notNullValue());
		assertThat("Next transaction from 30,9,2013",  transactionSchedule.getNextTransaction(new DateTime(2013,9,30,0,0)), notNullValue());
		assertThat("Next transaction from 30,9,2013",  transactionSchedule.getNextTransaction(new DateTime(2013,10,1,0,0)), equalTo(null));
	}
	
	@Test
	public void CanScheduleATransactionToOccurForOneYearOnly(){
		ITransactionSchedule transactionSchedule = new TransactionSchedule(transaction, ScheduleType.DAY, 1, firstOfJan2013, new DateTime(2014,1,1,0,0));
		
		Set<ITransaction> yearlyTransactions = transactionSchedule.getTransactionsForYear(2013, firstOfJan2013);
		assertThat("Number of transactions in the year", yearlyTransactions.size(), equalTo(365));
	}
}
