package com.tw.travel.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tw.travel.common.exception.BizException;
import com.tw.travel.common.exception.ErrorCode;
import com.tw.travel.dao.entity.OrderInvoice;
import com.tw.travel.dao.entity.TravelOrder;
import com.tw.travel.dao.repository.OrderInvoiceRepository;
import com.tw.travel.service.adapter.invoice.InvoiceAdapter;
import com.tw.travel.service.command.CreateInvoiceCommand;
import com.tw.travel.service.dto.InvoiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class InvoiceServiceImplTest {

  private InvoiceAdapter invoiceAdapter;
  private OrderInvoiceRepository repository;

  private InvoiceServiceImpl invoiceService;

  @BeforeEach
  void setUp() {
    invoiceAdapter = mock(InvoiceAdapter.class);
    repository = mock(OrderInvoiceRepository.class);

    invoiceService = new InvoiceServiceImpl(invoiceAdapter, repository);
  }

  @Test
  void should_save_generated_invoice_when_create_invoice_success() {

    when(invoiceAdapter.createInvoice(any(), any(), any())).thenReturn("fileLocation");

    invoiceService.createInvoice(mock(CreateInvoiceCommand.class), mock(TravelOrder.class));

    verify(repository, times(1)).save(argThat((OrderInvoice invoice) -> invoice.getStatus().equals(
        InvoiceStatus.GENERATED)));
  }

  @Test
  void should_save_generate_failed_invoice_when_create_invoice_success() {

    doThrow(new BizException(HttpStatus.GATEWAY_TIMEOUT, ErrorCode.SYS_ERROR)).when(invoiceAdapter)
        .createInvoice(any(), any(), any());

    invoiceService.createInvoice(mock(CreateInvoiceCommand.class), mock(TravelOrder.class));

    verify(repository, times(1)).save(argThat((OrderInvoice invoice) -> invoice.getStatus().equals(
        InvoiceStatus.GENERATE_FAILED)));
  }
}