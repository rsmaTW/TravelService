package com.tw.travel.service;

import static com.tw.travel.common.exception.BizException.notFoundErrorException;
import static com.tw.travel.common.exception.BizException.paramErrorException;
import static com.tw.travel.common.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.tw.travel.common.exception.ErrorCode.ORDER_STATUS_CAN_NOT_CREATE_INVOICE;

import com.tw.travel.dao.entity.TravelOrder;
import com.tw.travel.dao.repository.OrderRepository;
import com.tw.travel.service.command.CreateInvoiceCommand;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

  private final IInvoiceService invoiceService;
  private final OrderRepository orderRepository;

  @Override
  public void createInvoice(CreateInvoiceCommand command) {
    TravelOrder order = Optional.ofNullable(orderRepository.getById(command.getOrderId()))
        .orElseThrow(() -> notFoundErrorException(ORDER_NOT_FOUND, command.getOrderId()));

    if (!order.getCustomerId().equals(command.getCustomerId())) {
      throw notFoundErrorException(ORDER_NOT_FOUND, command.getOrderId());
    }

    if (!order.canCreateInvoice()) {
      throw paramErrorException(ORDER_STATUS_CAN_NOT_CREATE_INVOICE, command.getOrderId(),
          order.getStatus());
    }

    invoiceService.createInvoice(command, order);
    order.invoiceCreated();
    orderRepository.save(order);
  }
}
