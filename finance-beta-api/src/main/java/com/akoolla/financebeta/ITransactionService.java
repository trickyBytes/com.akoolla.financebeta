package com.akoolla.financebeta;

import java.util.Set;

import com.akoolla.financebeta.reporting.ITransactionQuery;
import com.akoolla.financebeta.transaction.ITransaction;

public interface ITransactionService {
	void saveTransaction(ITransaction transaction);
	void scheduleTransaction(ITransactionSchedule schedule);
	Set<ITransaction> listTransactions(ITransactionQuery query);
}
