package com.test.service;


import static com.test.constants.ConfigConstants.HCL_API_URL;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.test.model.HclTransactionInfo;
import com.test.model.Transaction;


@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<HclTransactionInfo> getHCLTransactionInfo() {

		LOGGER.info("Get all accounts raw data.");

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ParameterizedTypeReference<Map<String, List<Transaction>>> parameterizedTypeReference = new ParameterizedTypeReference<Map<String, List<Transaction>>>() {
			// DO NOTHING here
		};

		ResponseEntity<Map<String, List<Transaction>>> exchange = restTemplate.exchange(HCL_API_URL,
			HttpMethod.GET, entity, parameterizedTypeReference);

		List<Transaction> result = exchange.getBody().get("transactions");

		LOGGER.info("Transform all accounts data to HCL format.");
		return result.stream().map(transaction -> {
			HclTransactionInfo info = new HclTransactionInfo();
			info.setId(transaction.getId());
			info.setAccountId(transaction.getThisAccount() != null ? transaction.getThisAccount().getId() : null);

			info.setCounterpartyAccount(
				transaction.getOtherAccount() != null ? transaction.getOtherAccount().getNumber() : null);

			info.setCounterpartyName(
				(transaction.getOtherAccount() != null && transaction.getOtherAccount().getHolder() != null)
					? transaction.getOtherAccount().getHolder().getName()
					: null);

			info.setCounterPartyLogoPath(
				(transaction.getOtherAccount() != null && transaction.getOtherAccount().getMetadata() != null)
					? transaction.getOtherAccount().getMetadata().getImageUrl()
					: null);

			String amount = (transaction.getDetails() != null && transaction.getDetails().getValue() != null)
				? transaction.getDetails().getValue().getAmount()
				: null;

			String currency = (transaction.getDetails() != null && transaction.getDetails().getValue() != null)
				? transaction.getDetails().getValue().getCurrency()
				: null;

			info.setInstructedAmount(amount);
			info.setInstructedCurrency(currency);
			info.setTransactionAmount(amount);
			info.setTransactionCurrency(currency);
			info.setTransactionType(transaction.getDetails() != null ? transaction.getDetails().getType() : null);
			info.setDescription(
				transaction.getDetails() != null ? transaction.getDetails().getDescription() : null);

			return info;

		}).collect(Collectors.toList());
	}

	@Override
	public List<HclTransactionInfo> getHCLTransactionInfoByTransactionType(
		List<HclTransactionInfo> hclTransactionInfo, String transactionType) {

		LOGGER.info("Get all HCL transactions by transaction type.");

		return hclTransactionInfo.stream()
				.filter(transaction -> Objects.nonNull(transaction.getTransactionType()))
				.filter(transaction -> transaction.getTransactionType().equals(transactionType))
				.collect(Collectors.toList());
	}

	@Override
	public Map<String, Object> getTotalAmountByTransactionType(List<HclTransactionInfo> hclTransactionInfo,
		String transactionType) {

		LOGGER.info("Get total amount by transaction type.");

		//get the list of eligible transactions
		List<HclTransactionInfo> transactionByType = hclTransactionInfo.stream()
				.filter(transaction -> Objects.nonNull(transaction.getTransactionType()))
				.filter(transaction -> transaction.getTransactionType().equals(transactionType))
				.collect(Collectors.toList());

		if (transactionByType == null || transactionByType.isEmpty()) {
			return null;
		}

		//sum the transaction amount
		double total = transactionByType.stream().map(t -> t.getTransactionAmount())
				.mapToDouble(Double::parseDouble).sum();

		//prepare the response json object
		Map<String, Object> result = new HashMap<>();
		result.put("transactionType", transactionType);
		result.put("totalAmount", total);

		Map<String, Object> transactionTotal = new HashMap<>();
		transactionTotal.put("transactionTotal", result);

		return transactionTotal;
	}


}
