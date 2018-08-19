package com.myspring.controller;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myspring.model.HclTransactionInfo;
import com.myspring.service.TransactionService;


@RestController
public class TransactionRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRestController.class);

	@Autowired
	TransactionService transactionService;

	@RequestMapping(value = "/hcltransaction/", method = RequestMethod.GET)
	public ResponseEntity<List<HclTransactionInfo>> getHCLTransactionInfo() {

		LOGGER.info("Get all HCL transactions.");
		List<HclTransactionInfo> hclTransactionInfo = transactionService.getHCLTransactionInfo();

		if (hclTransactionInfo.isEmpty()) {
			return new ResponseEntity<List<HclTransactionInfo>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<HclTransactionInfo>>(hclTransactionInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/hcltransaction/{transactionType}", method = RequestMethod.GET)
	public ResponseEntity<List<HclTransactionInfo>> getHCLTransactionInfoByTransactionType(
		@PathVariable("transactionType") String transactionType) {

		List<HclTransactionInfo> hclTransactionInfo = transactionService.getHCLTransactionInfo();

		List<HclTransactionInfo> result = transactionService
				.getHCLTransactionInfoByTransactionType(hclTransactionInfo, transactionType);

		if (result.isEmpty()) {
			LOGGER.info("Transaction type - " + transactionType + ", not found.");
			return new ResponseEntity<List<HclTransactionInfo>>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<HclTransactionInfo>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/hcltransaction/{transactionType}/total", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getTotalAmountByTransactionType(
		@PathVariable("transactionType") String transactionType) {

		List<HclTransactionInfo> hclTransactionInfo = transactionService.getHCLTransactionInfo();

		Map<String, Object> transactionTotal = transactionService
				.getTotalAmountByTransactionType(hclTransactionInfo, transactionType);

		if (transactionTotal == null) {
			LOGGER.info("Transaction type - " + transactionType + ", not found.");
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Map<String, Object>>(transactionTotal, HttpStatus.OK);
	}

}