package com.akoolla.financebeta;

import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Months;
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
	 * Construct transaction which starts at a given start date but is open ended, converts date to UTC zone.
	 * @param transaction
	 * @param scheduleType
	 * @param time
	 * @param startDate
	 */
	public TransactionSchedule(ITransaction transaction, ScheduleType scheduleType, int time, DateTime startDate){
		this.scheduleType = scheduleType;
		this.time = time;
		this.startDate = buildToTimeZone(startDate);
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
		this.endDate = buildToTimeZone(endDate);
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
			int startMonth = 1;
			int endMonth = 12;
			
			if (fromDate.isBefore(startDate)){
				startMonth = startDate.getMonthOfYear();
			}
			if(endDate != null && endDate.getYear() <= year){
				endMonth = endDate.getMonthOfYear();
			}
			
			switch (scheduleType) {
				case YEAR:
				case HOUR:
				case MINUTE:
				case SECOND:
				case ONCE:
					//Not implemented yet
					break;
				case MONTH:
					for(;startMonth <= endMonth; startMonth++){
						DateTime scheduledDate = new DateTime(year,startMonth, time, startDate.getMinuteOfHour(), startDate.getSecondOfMinute());
						if (endDate != null && scheduledDate.isAfter(this.endDate)){
							break;
						}
						
						ITransaction scheduledTransaction = new Transaction(scheduledDate, transaction.getAmount(), new String[] {"TEST"});
						transactions.add(scheduledTransaction);
					}
				break;
				case DAY:
					for(;startMonth <= endMonth; startMonth++){
						transactions.addAll(getTransactionsForMonth(year, startMonth, fromDate));
					}
					
					break;
				default:
					break;
			}
		}
		
		return transactions;
	}

	@Override
	public Set<ITransaction> getTransactionsForMonth(int year, int month, DateTime fromDate) {
		Set<ITransaction> transactions = new TreeSet<>();
		
		switch (scheduleType) {
		case YEAR:
			break;
		case MONTH:
			if (endDate != null && endDate.isBefore(fromDate) || fromDate.isBefore(startDate)){
				LOG.debug("From date:{} is after scheduled end date{}, no transactions scheduled", new Object[]{fromDate, endDate});
				break;
			}
			transactions.add(new Transaction(new DateTime(year,month,time,0,0), this.transaction.getAmount(), this.transaction.listTags().toArray(new String[0])));
			break;
		case DAY:
			if (endDate != null && endDate.isBefore(fromDate)){
				LOG.debug("From date:{} is after scheduled end date{}, no transactions scheduled", new Object[]{fromDate, endDate});
			} else {
				int day = fromDate.getDayOfMonth();
				if (fromDate.isBefore(startDate)){
					day = startDate.getDayOfMonth();
				}
				
				DateTime transactionMonth = new DateTime(year, month, day, time, 0);
				DateTime transactionDate;
				for(; day <= transactionMonth.dayOfMonth().getMaximumValue(); day++){
					transactionDate = new DateTime(year, month, day,time,0, DateTimeZone.UTC);
					transactions.add(new Transaction(transactionDate, this.transaction.getAmount(), this.transaction.listTags().toArray(new String[0])));
				}
			}
		default:
			break;
		}
		
		return transactions;
	}
	
	private DateTime buildToTimeZone(DateTime time){
		return time.withZone(DateTimeZone.UTC);
	}

	@Override
	public Set<ITransaction> getTransactionsForDay(int year, int month, int day) {
		// TODO Auto-generated method stub
		return null;
	}
}
