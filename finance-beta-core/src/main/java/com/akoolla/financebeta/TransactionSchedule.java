package com.akoolla.financebeta;

import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akoolla.financebeta.scheduling.ScheduleType;
import com.akoolla.financebeta.transaction.ITransaction;
import com.akoolla.financebeta.transaction.Transaction;

public class TransactionSchedule implements ITransactionSchedule {
	
	private static Logger LOG = LoggerFactory.getLogger(TransactionSchedule.class);
	
	private ScheduleType scheduleType;
	private int time;
	private DateTime startDate;
	private DateTime endDate;
	private ITransaction transaction;
	
	/**
	 * Construct transaction which starts at a given start date but is open ended.
	 * @param transaction
	 * @param scheduleType
	 * @param time
	 * @param startDate
	 */
	public TransactionSchedule(ITransaction transaction, ScheduleType scheduleType, int time, DateTime startDate){
		this.scheduleType = scheduleType;
		this.time = time;
		this.startDate = startDate;
		this.transaction = transaction;
	}

	/**
	 * Construct a transaction with a given start and end date
	 * @param transaction
	 * @param scheduleType
	 * @param time
	 * @param startDate
	 * @param endDate
	 */
	public TransactionSchedule(ITransaction transaction, ScheduleType scheduleType, int time, DateTime startDate, DateTime endDate) {
		this(transaction, scheduleType, time, startDate);
		this.endDate = endDate;
	}
	
	@Override
	public ITransaction getNextTransaction(DateTime fromDate) {
		ITransaction transaction = null;
		DateTime transactionDate = null;
		
		if(fromDate.isBefore(startDate)){
			transactionDate = new DateTime(startDate.getYear(), startDate.getMonthOfYear(), time, 0,0);
		} else if (endDate == null || fromDate.isBefore(endDate)){
			transactionDate = new DateTime(fromDate.getYear(), fromDate.getMonthOfYear(), time, 0,0);
		}
		
		if (transactionDate == null){
			LOG.debug("Not transactions to schedule from date {}, for schedule {}", new Object[]{fromDate,this});
		} else {
			transaction = new Transaction(transactionDate, this.transaction.getAmount(), this.transaction.listTags().toArray(new String[0]));
		}

		return transaction;
	}

	@Override
	public Set<ITransaction> getTransactionsForYear(int year, DateTime fromDate) {
		Set<ITransaction> transactions = new TreeSet<>();
		
		if (endDate != null && endDate.isBefore(fromDate)){
			LOG.debug("From date:{} is after scheduled end date{}, no transactions scheduled", new Object[]{fromDate, endDate});
		} else {
			int month = 1;
			int endMonth = 12;
			if (fromDate.isBefore(startDate)){
				month = startDate.getMonthOfYear();
			}
			if(endDate != null && endDate.getYear() <= year){
				endMonth = endDate.getMonthOfYear();
			}
			
			for(;month <= endMonth; month++){
				DateTime scheduledDate = new DateTime(year,month, time, startDate.getMinuteOfHour(), startDate.getSecondOfMinute());
				if (endDate != null && scheduledDate.isAfter(this.endDate)){
					break;
				}
				
				ITransaction scheduledTransaction = new Transaction(scheduledDate, transaction.getAmount(), new String[] {"TEST"});
				transactions.add(scheduledTransaction);
			}
		}
		
		return transactions;
	}

	@Override
	public Set<ITransaction> getTransactionsForMonth(int year, int month) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ITransaction> getTransactionsForDay(int year, int month, int day) {
		// TODO Auto-generated method stub
		return null;
	}
}
