package com.akoolla.financebeta.transaction;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * @author richardt
 * @since 1.0
 */
public class Transaction implements ITransaction {
	
	private DateTime date;
	private double amount;
	private Set<String> tags;
	
	/**
	 * @return
	 * @since 1.0
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Every transaction must have an amount and at least one tag
	 * @param amount
	 * @param tags
	 */
	public Transaction(double amount, String... tags) {
		this.amount = amount;
		this.date = new DateTime();
		
		if (tags == null || tags.length < 1){
			throw new IllegalArgumentException("Transaction must have at least one tag");
		}
		this.tags = new TreeSet<String>();
		for (String tag : tags){
			tag = StringUtils.lowerCase(tag);
			this.tags.add(StringUtils.lowerCase(tag));
		}
	}
	
	public Transaction(DateTime transactionDate, double amount, String... tags){
		this(amount, tags);
		this.date = transactionDate;
	}

	@Override
	public boolean hasTag(final String tag) {
		return tags.contains(StringUtils.lowerCase(tag));
	}

	@Override
	public void removeTag(String tag) {
		tags.remove(StringUtils.lowerCase(tag));
	}

	@Override
	public DateTime getTransactionDate() {
		return this.date;
	}

	@Override
	public int compareTo(ITransaction arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
