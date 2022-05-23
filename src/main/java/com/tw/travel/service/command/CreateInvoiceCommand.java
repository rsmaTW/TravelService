package com.tw.travel.service.command;

import com.tw.travel.service.dto.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateInvoiceCommand {

  private Long orderId;
  private String customerId;
  private InvoiceType invoiceType;
  private String invoiceTitle;

}
