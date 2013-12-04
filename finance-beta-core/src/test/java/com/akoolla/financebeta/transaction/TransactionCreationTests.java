package com.akoolla.financebeta.transaction;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.junit.Test;

public class TransactionCreationTests {

	@Test
	public void AnAmountCanBeAddedToATransaction() throws Exception {
		ITransaction transaction = new Transaction(100d, new String[] {"xxx"});
		assertThat("Transaction amount", transaction.getAmount(), equalTo(100d));
	}
	
	@Test
	public void CanAddTagsToATraction(){
		ITransaction transaction = new Transaction(100d, "Vehicle");
		transaction = new Transaction(100d, new String[] {"Vehicle", "Z4", "Tax"});
		
		assertThat("Has tag vehicle", transaction.hasTag("Vehicle"), is(true));
		assertThat("Has tag Z4", transaction.hasTag("Z4"), is(true));
		assertThat("Has tag Tax", transaction.hasTag("Tax"), is(true));
	}
	
	@Test
	public void CanRemoveTagsFromATransaction() throws Exception {
		ITransaction transaction = new Transaction(100d, "Vehicle");
		transaction = new Transaction(100d, new String[] {"Vehicle", "Z4", "Tax"});
		
		transaction.removeTag("Z4");
		assertThat("Has tag Z4", transaction.hasTag("Z4"), is(false));
		
		transaction.removeTag("vehicle");
		assertThat("Removel shouold not be not case sensitive", 
				transaction.hasTag("Vehicle"), is(false));
	}
	
	@Test
	public void TransactionDateIsSetByDefault() throws Exception {
		ITransaction transaction = new Transaction(100d, "Vehicle");
		assertThat("Transcation does not have date", 
				transaction.getTransactionDate(), is(notNullValue()));
	}

	@Test
	public void CanSetTransactionDate() throws Exception {
		DateTime transactionDate = new DateTime(2013, 9, 30, 0, 0);
		ITransaction transaction = new Transaction(transactionDate, 100d, "Vehicle");
		
		assertThat(transactionDate, equalTo(transaction.getTransactionDate()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void AllTransactionsMustHaveAtLeastOneTag() {
		new Transaction(100d, new String[0]);
	}
	
	
	@Test
	public void TransactionsShouldNotBeEqualUnlessDateAndAmountAreTheSame() throws Exception {
		ITransaction firstTransaction = new Transaction(new DateTime(2013,1,1,1,1), 100d, "XXX");
		ITransaction secondTransaction = new Transaction(new DateTime(2013,2,1,1,1), 100d, "XXX");
		assertThat("Should be false if dates are different", firstTransaction.equals(secondTransaction), is(false));
		
		firstTransaction = new Transaction(new DateTime(2013,1,1,1,1), 100d, "XXX");
		secondTransaction = new Transaction(new DateTime(2013,1,1,1,1), 150d, "XXX");
		assertThat("Should be different if amounts are different", firstTransaction.equals(secondTransaction), is(false));
		
		firstTransaction = new Transaction(new DateTime(2013,1,1,1,1), 100d, "XXX");
		secondTransaction = new Transaction(new DateTime(2013,1,1,1,1), 100d, "XXXX");
		assertThat("Should be different if tags are different", firstTransaction.equals(secondTransaction), is(false));
		
		firstTransaction = new Transaction(new DateTime(2013,1,1,1,1), 100d, "XXX", "xx");
		secondTransaction = new Transaction(new DateTime(2013,1,1,1,1), 100d, "XXX", "xx");
		assertThat("Should be the same if tags and dates are the same", firstTransaction.equals(secondTransaction), is(true));
	}
	
	@Test
	public void TransactionsWithDifferentDatesShouldNotBeTheEqual() throws Exception {
		ITransaction firstTransaction = new Transaction(new DateTime(2013,1,1,1,1), 100d, "XXX");
		ITransaction secondTransaction = new Transaction(new DateTime(2013,2,1,1,1), 100d, "XXX");
		
		assertThat(firstTransaction.equals(secondTransaction), is(false));
		
		Set<ITransaction> transactions = new TreeSet<>();
		transactions.add(firstTransaction);
		transactions.add(secondTransaction);
		
		assertThat(transactions.size(), equalTo(2));
	}
}
