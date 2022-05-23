package com.tw.travel.service.adapter.invoice;

import com.tw.travel.dao.entity.TravelOrder;
import com.tw.travel.service.adapter.invoice.request.CreateInvoiceRequest;
import com.tw.travel.service.dto.InvoiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class InvoiceAdapter {

  private final InvoiceClient invoiceClient;

  public String createInvoice(InvoiceType invoiceType, String invoiceTitle, TravelOrder order) {
    CreateInvoiceRequest request = new CreateInvoiceRequest(order.getId(), invoiceType,
        invoiceTitle, order.getActuallyPaidAmount());

    return invoiceClient.createInvoice(request).getFileLocation();

  }
}
