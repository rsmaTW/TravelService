package com.tw.travel.service;

import com.tw.travel.service.command.CreateInvoiceCommand;

public interface IOrderService {

  void createInvoice(CreateInvoiceCommand command);
}
