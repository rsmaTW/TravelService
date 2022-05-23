package com.tw.travel.service.adapter.invoice;

import com.tw.travel.service.adapter.invoice.request.CreateInvoiceRequest;
import com.tw.travel.service.adapter.invoice.response.CreateInvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "invoice", url = "${integration.invoice.url}")
public interface InvoiceClient {

  @RequestMapping(value = "/invoice", method = RequestMethod.POST)
  CreateInvoiceResponse createInvoice(@RequestBody CreateInvoiceRequest request);

}
