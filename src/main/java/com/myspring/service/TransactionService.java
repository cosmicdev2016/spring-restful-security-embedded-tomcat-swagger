package com.myspring.service;


import java.util.List;
import java.util.Map;

import com.myspring.model.HclTransactionInfo;


public interface TransactionService {

	/**
	 * Get all the HCL transactions
	 * 
	 * @return
	 */
	public List<HclTransactionInfo> getHCLTransactionInfo();

	/**
	 * Filter transactions based on Transaction Type
	 * 
	 * @param hclTransactionInfo
	 * @param transactionType
	 * @return
	 */
	public List<HclTransactionInfo> getHCLTransactionInfoByTransactionType(
		List<HclTransactionInfo> hclTransactionInfo, String transactionType);

	/**
	 * Get total amount of transactions based on Transaction Type
	 * 
	 * @param hclTransactionInfo
	 * @param transactionType
	 * @return
	 */
	public Map<String, Object> getTotalAmountByTransactionType(List<HclTransactionInfo> hclTransactionInfo,
		String transactionType);
}
