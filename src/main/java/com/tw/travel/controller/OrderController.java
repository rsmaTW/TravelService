package com.tw.travel.controller;

import com.tw.travel.controller.request.CreateInvoiceRequest;
import com.tw.travel.service.IOrderService;
import com.tw.travel.service.command.CreateInvoiceCommand;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

  private final IOrderService orderService;

  @PostMapping("/{id}/invoice")
  public void initOrderPaymentSession(@PathVariable("id") Long orderId,
      @RequestBody @Valid CreateInvoiceRequest request) {
    orderService.createInvoice(
        new CreateInvoiceCommand(orderId, request.getCustomerId(), request.getInvoiceType(),
            request.getInvoiceTitle()));
  }

}
