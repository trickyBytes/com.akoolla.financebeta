package com.akoolla.financebeta;

import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;

import com.akoolla.financebeta.scheduling.ScheduleType;
import com.akoolla.financebeta.transaction.ITransaction;

public class Scheduler implements IScheduler {

	public Scheduler(boolean repeats, DateTime date) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<ITransaction> createTransactions(ITransaction transaction,
			ScheduleType scheduleType, DateTime date) {
		
		Set<ITransaction> transactions = new TreeSet<>();
		transactions.add(transaction);
		
		return transactions;
	}
}
