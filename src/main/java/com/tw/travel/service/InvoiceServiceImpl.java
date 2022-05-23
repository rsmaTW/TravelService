package com.tw.travel.service;

import com.tw.travel.dao.entity.TravelOrder;
import com.tw.travel.dao.entity.OrderInvoice;
import com.tw.travel.dao.repository.OrderInvoiceRepository;
import com.tw.travel.service.adapter.invoice.InvoiceAdapter;
import com.tw.travel.service.command.CreateInvoiceCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

  private final InvoiceAdapter invoiceAdapter;
  private final OrderInvoiceRepository repository;

  @Override
  public void createInvoice(CreateInvoiceCommand command, TravelOrder order) {
    try {
      String invoiceLocation = invoiceAdapter
          .createInvoice(command.getInvoiceType(), command.getInvoiceTitle(), order);
      OrderInvoice orderInvoice = OrderInvoice
          .createGeneratedInvoice(order, command.getInvoiceType(), command.getInvoiceTitle(),
              invoiceLocation);
      repository.save(orderInvoice);
    } catch (Exception e) {
      log.error("create invoice error, order id is {}", order.getId(), e);
      OrderInvoice orderInvoice = OrderInvoice
          .createGeneratedFailedInvoice(order, command.getInvoiceType(), command.getInvoiceTitle());
      repository.save(orderInvoice);
    }

  }
}
