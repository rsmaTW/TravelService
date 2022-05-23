package com.tw.travel.service;

import static com.tw.travel.common.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.tw.travel.common.exception.ErrorCode.ORDER_STATUS_CAN_NOT_CREATE_INVOICE;
import static com.tw.travel.dao.entity.TravelOrder.OrderStatus.IN_TRAVEL;
import static com.tw.travel.dao.entity.TravelOrder.OrderStatus.PAYMENT_SETTLED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tw.travel.common.exception.BizException;
import com.tw.travel.dao.entity.TravelOrder;
import com.tw.travel.dao.entity.TravelOrder.OrderStatus;
import com.tw.travel.dao.repository.OrderRepository;
import com.tw.travel.service.command.CreateInvoiceCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderServiceImplTest {

  private IInvoiceService invoiceService;
  private OrderRepository orderRepository;

  private OrderServiceImpl orderService;

  @BeforeEach
  void setUp() {
    invoiceService = mock(IInvoiceService.class);
    orderRepository = mock(OrderRepository.class);
    orderService = new OrderServiceImpl(invoiceService, orderRepository);
  }

  @Test
  void should_throw_exception_given_not_exist_order_id_when_create_invoice() {
    Long orderId = 1L;
    CreateInvoiceCommand mockCommand = mock(CreateInvoiceCommand.class);
    when(mockCommand.getOrderId()).thenReturn(orderId);

    when(orderRepository.getById(orderId)).thenReturn(null);

    assertThatThrownBy(() -> orderService.createInvoice(mockCommand))
        .isInstanceOf(BizException.class)
        .hasMessage(String.format(ORDER_NOT_FOUND.getMessage(), orderId));
  }

  @Test
  void should_throw_exception_given_order_not_belong_to_customer_when_create_invoice() {
    Long orderId = 1L;
    String commandCustomerId = "commandCustomerId";
    String orderCustomerId = "orderCustomerId";
    CreateInvoiceCommand mockCommand = mock(CreateInvoiceCommand.class);
    when(mockCommand.getOrderId()).thenReturn(orderId);
    when(mockCommand.getCustomerId()).thenReturn(commandCustomerId);

    TravelOrder mockOrder = mock(TravelOrder.class);
    when(mockOrder.getCustomerId()).thenReturn(orderCustomerId);
    when(orderRepository.getById(orderId)).thenReturn(mockOrder);

    assertThatThrownBy(() -> orderService.createInvoice(mockCommand))
        .isInstanceOf(BizException.class)
        .hasMessage(String.format(ORDER_NOT_FOUND.getMessage(), orderId));
  }

  @Test
  void should_throw_exception_given_order_status_not_support_create_invoice_when_create_invoice() {
    Long orderId = 1L;
    String customerId = "customerId";
    CreateInvoiceCommand mockCommand = mock(CreateInvoiceCommand.class);
    when(mockCommand.getOrderId()).thenReturn(orderId);
    when(mockCommand.getCustomerId()).thenReturn(customerId);

    TravelOrder mockOrder = new TravelOrder();
    mockOrder.setCustomerId(customerId);
    mockOrder.setStatus(IN_TRAVEL);
    when(orderRepository.getById(orderId)).thenReturn(mockOrder);

    assertThatThrownBy(() -> orderService.createInvoice(mockCommand))
        .isInstanceOf(BizException.class)
        .hasMessage(
            String.format(ORDER_STATUS_CAN_NOT_CREATE_INVOICE.getMessage(), orderId, IN_TRAVEL));
  }

  @Test
  void should_create_invoice_success() {
    Long orderId = 1L;
    String customerId = "customerId";
    CreateInvoiceCommand mockCommand = mock(CreateInvoiceCommand.class);
    when(mockCommand.getOrderId()).thenReturn(orderId);
    when(mockCommand.getCustomerId()).thenReturn(customerId);

    TravelOrder mockOrder = new TravelOrder();
    mockOrder.setCustomerId(customerId);
    mockOrder.setStatus(PAYMENT_SETTLED);
    when(orderRepository.getById(orderId)).thenReturn(mockOrder);

    orderService.createInvoice(mockCommand);

    verify(invoiceService, times(1)).createInvoice(mockCommand, mockOrder);
    assertThat(mockOrder.getStatus()).isEqualTo(OrderStatus.INVOICED);
    verify(orderRepository, times(1)).save(mockOrder);
  }
}