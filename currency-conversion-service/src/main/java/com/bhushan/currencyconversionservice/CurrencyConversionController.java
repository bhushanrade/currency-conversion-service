package com.bhushan.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {
	@Autowired
	CurrencyExchangeServiceProxy proxy;
	Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);
	@GetMapping("/currency-convertor/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean converCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);
		CurrencyConversionBean response = responseEntity.getBody();
		logger.info("Logger info "+response.toString() );
		System.out.println("********************************************"+response.toString());
		//return new CurrencyConversionBean(1L, from, to, BigDecimal.ONE, quantity, BigDecimal.ONE,8000);
		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()), response.getPort());
	
	}
	
	//with feign
	
	@GetMapping("/currency-convertor-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean converCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
/*		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);*/
		CurrencyConversionBean response = proxy.retriveExchangeValue(from, to); 
				
			/*	responseEntity.getBody();*/
		logger.info("Logger info "+response.toString() );
		System.out.println("********************************************"+response.toString());
		//return new CurrencyConversionBean(1L, from, to, BigDecimal.ONE, quantity, BigDecimal.ONE,8000);
		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()), response.getPort());
	
	}
	
}
