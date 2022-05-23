package com.tw.travel.controller.request;

import com.tw.travel.service.dto.InvoiceType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateInvoiceRequest {

  @NotBlank(message = "customer id can not be blank")
  private String customerId;

  @NotNull(message = "invoice type can not be null")
  private InvoiceType invoiceType;

  @NotBlank(message = "invoice title can not be blank")
  private String invoiceTitle;

}
