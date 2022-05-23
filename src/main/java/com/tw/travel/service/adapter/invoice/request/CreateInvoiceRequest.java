package com.tw.travel.service.adapter.invoice.request;

import com.tw.travel.service.dto.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateInvoiceRequest {

  private Long orderId;
  private InvoiceType invoiceType;
  private String invoiceTitle;
  private Double amount;

}
