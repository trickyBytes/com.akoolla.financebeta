package com.akoolla.financebeta.transaction;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.google.common.collect.ImmutableSet;

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
	public boolean equals(Object obj) {
		boolean isSame = false;
		
		if(obj != null && obj instanceof ITransaction){
			ITransaction transaction2 = (ITransaction) obj;
			if (date != null && transaction2.getTransactionDate() != null){
				if(date.equals(transaction2.getTransactionDate())){
					if (amount == transaction2.getAmount()){
						if (tags.size() == transaction2.listTags().size()){
							for (String tag : tags){
								if (transaction2.hasTag(tag) == false){
									break;
								}
								isSame = true;
							}
						}
					}
				}
			}
		} 
		
		return isSame;
	}

	@Override
	public int compareTo(ITransaction tranaction) {
		if (this.equals(tranaction)){
			return 0;
		} else {
			//TODO implement comparable properly
			return 1;
		}
	}

	@Override
	public Set<String> listTags() {
		return ImmutableSet.<String>builder().addAll(tags).build();
	}
}
