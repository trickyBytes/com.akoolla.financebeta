package com.akoolla.financebeta;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.akoolla.financebeta.scheduling.ScheduleType;
import com.akoolla.financebeta.transaction.ITransaction;
import com.akoolla.financebeta.transaction.Transaction;


@RunWith(MockitoJUnitRunner.class)
public class SchedulingTest {
	
	//TODO:
		//Schedule start/end dates
		//Test once a second, minute, hour, day, week, month, year
	
	@Test
	public void ICanScheduleATransactionToHappenJustOnce() throws Exception{ 
		DateTime transactionDate = new DateTime(2013,9,30,0,0);
		ITransaction transaction = new Transaction(transactionDate, 100d, new String[] {"xzxx"});
		
		IScheduler schedule = new Scheduler(false, transactionDate);
		Set<ITransaction> transactions = schedule.createTransactions(
				transaction, ScheduleType.ONCE, transactionDate);
		
		assertThat("No transactions returned", transactions, notNullValue());
		assertThat("No transactions returned", transactions.size(), equalTo(1));
		
		ITransaction scheduleITransaction = transactions.toArray(new ITransaction[0])[0];
		assertThat(scheduleITransaction.getTransactionDate(), equalTo(transactionDate));
	}
}
