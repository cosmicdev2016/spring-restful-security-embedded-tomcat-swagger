package com.test.service;


import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.test.configuration.TransactionConfiguration;
import com.test.constants.ConfigConstants;
import com.test.model.HclTransactionInfo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TransactionConfiguration.class})
@WebAppConfiguration
public class TransactionServiceTest {

	@Autowired
	private TransactionService serviceMock;

	@Autowired
	private RestTemplate restTemplateMock;

	private MockRestServiceServer serverMock;

	private String rawJson;

	@Before
	public void setUp() {
		serverMock = MockRestServiceServer.createServer(restTemplateMock);
		rawJson = readRawJson("testdata.json");
	}

	@Test
	public void whenCallGetHCLTransactionInfo_thenReturnTranformedData() {

		serverMock.expect(requestTo(ConfigConstants.HCL_API_URL)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(rawJson, MediaType.APPLICATION_JSON));

		List<HclTransactionInfo> result = serviceMock.getHCLTransactionInfo();

		serverMock.verify();
		assertEquals("Error in records transformation: ", 3, result.size());
		assertEquals("Transaction Id does not match: ", "897706c1-dcc6-4e70-9d85-8a537c7cbf3e", result.get(0).getId());
		assertEquals("Transaction Account Id does not match: ", "savings-kids-john", result.get(0).getAccountId());
		assertEquals("Transaction Counter party account does not match: ", "savings-kids-john", result.get(0).getCounterpartyAccount());
		assertEquals("Transaction Counter party name does not match: ", "ALIAS_49532E", result.get(0).getCounterpartyName());
		assertEquals("Transaction Counter party logo does not match: ", null, result.get(0).getCounterPartyLogoPath());
		assertEquals("Transaction Instructed amount does not match: ", "-90.00", result.get(0).getInstructedAmount());
		assertEquals("Transaction Instructed currency does not match: ", "GBP", result.get(0).getInstructedCurrency());
		assertEquals("Transaction amount does not match: ", "-90.00", result.get(0).getTransactionAmount());
		assertEquals("Transaction currency does not match: ", "GBP", result.get(0).getTransactionCurrency());
		assertEquals("Transaction type does not match: ", "SANDBOX_TAN", result.get(0).getTransactionType());
		assertEquals("Transaction description does not match: ", "Gift", result.get(0).getDescription());
		
	}

	@Test
	public void whenCallGetHCLTransactionInfoByTransactionType_thenReturnFilteredData() {

		serverMock.expect(requestTo(ConfigConstants.HCL_API_URL)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(rawJson, MediaType.APPLICATION_JSON));

		List<HclTransactionInfo> hclData = serviceMock.getHCLTransactionInfo();
		serverMock.verify();
		
		List<HclTransactionInfo> result = serviceMock.getHCLTransactionInfoByTransactionType(hclData, "SANDBOX_TAN");
		assertEquals("Error in getting Transactions by TransactionType: ", 2, result.size());
	}
	
	@Test
	public void whenCallTotalAmountByTransactionType_thenReturnTotalAmount() {
		serverMock.expect(requestTo(ConfigConstants.HCL_API_URL)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(rawJson, MediaType.APPLICATION_JSON));

		List<HclTransactionInfo> hclData = serviceMock.getHCLTransactionInfo();
		serverMock.verify();

		Map<String, Object> result = serviceMock.getTotalAmountByTransactionType(hclData, "SANDBOX_TAN");
		assertEquals("Error in getting Total Amount by TransactionType: ", 10.0, ((Map<String, Object>)result.get("transactionTotal")).get("totalAmount"));
	}
	
	@Test
	public void whenCallTotalAmountByTransactionTypeDoesNotMatch_thenReturnTotalAmount() {
		serverMock.expect(requestTo(ConfigConstants.HCL_API_URL)).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(rawJson, MediaType.APPLICATION_JSON));

		List<HclTransactionInfo> hclData = serviceMock.getHCLTransactionInfo();
		serverMock.verify();

		Map<String, Object> result = serviceMock.getTotalAmountByTransactionType(hclData, "SANDBOX_TAN1");
		assertEquals("Error in getting Total Amount by TransactionType: ", true, result == null);
	}
	
	private static String readRawJson(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource(filePath).toURI()), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}
		catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		return contentBuilder.toString();
	}
}
