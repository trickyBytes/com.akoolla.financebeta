package com.akoolla.financebeta.transaction;

import java.util.Set;

import org.joda.time.DateTime;

/**
 * @author richardt
 * @since 1.0
 */
public interface ITransaction extends Comparable<ITransaction> {
	/**
	 * @return
	 * @since 1.0
	 */
	double getAmount();
	
	/**
	 * @param tag
	 * @return true if transaction
	 */
	boolean hasTag(final String tag);
	
	/**
	 * Removes the current tag from the transaction, tags are not case.
	 * @param tag
	 */
	void removeTag(final String tag);
	
	/**
	 * @return {@link DateTime}
	 * @since 1.0
	 */
	DateTime getTransactionDate();
	
	Set<String> listTags();
}
