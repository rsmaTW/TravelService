package com.tw.travel.service;

import com.tw.travel.dao.entity.TravelOrder;
import com.tw.travel.service.command.CreateInvoiceCommand;

public interface IInvoiceService {

  void createInvoice(CreateInvoiceCommand command, TravelOrder order);
}
