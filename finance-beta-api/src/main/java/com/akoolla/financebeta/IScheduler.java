package com.akoolla.financebeta;

import java.util.Set;

import org.joda.time.DateTime;

import com.akoolla.financebeta.scheduling.ScheduleType;
import com.akoolla.financebeta.transaction.ITransaction;

public interface IScheduler {
	Set<ITransaction> createTransactions(ITransaction tranactions, ScheduleType scheduleType, DateTime date);
}
