package com.lukes.microservices.currencyconversionservice.controller;

import com.lukes.microservices.currencyconversionservice.feign.CurrencyExchangeClient;
import com.lukes.microservices.currencyconversionservice.model.CurrencyConversionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyaegel on 07/27/2019
 */
@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeClient client;

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal quantity){

        Map<String, String> map = new HashMap<>();
        map.put("from", from);
        map.put("to", to);

        CurrencyConversionBean conversionBean = new RestTemplate().getForObject("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversionBean.class, map);
        conversionBean.setQuantity(quantity);
        conversionBean.setTotalCalculatedAmount(quantity.multiply(conversionBean.getConversionMultiple()));

        return conversionBean;
    }

    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal quantity){

        CurrencyConversionBean conversionBean = client.retrieveExchangeValue(from, to);
        conversionBean.setQuantity(quantity);
        conversionBean.setTotalCalculatedAmount(quantity.multiply(conversionBean.getConversionMultiple()));

        return conversionBean;
    }


}
