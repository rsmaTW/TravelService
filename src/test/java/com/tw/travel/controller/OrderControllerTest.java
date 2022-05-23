package com.tw.travel.controller;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.travel.common.exception.ErrorCode;
import com.tw.travel.common.response.ErrorResponse;
import com.tw.travel.controller.request.CreateInvoiceRequest;
import com.tw.travel.service.IOrderService;
import com.tw.travel.service.dto.InvoiceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

class OrderControllerTest extends BaseControllerTest {

  @MockBean
  private IOrderService orderService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void should_return_error_given_null_value_when_create_invoice() throws Exception {
    CreateInvoiceRequest request = new CreateInvoiceRequest(null, null, null);
    RequestBuilder builder = post("/api/v1/order/1/invoice")
        .header("Content-Type", "application/json")
        .content(objectMapper.writeValueAsString(request));
    String contentAsString = mockMvc.perform(builder)
        .andExpect(status().is(400))
        .andReturn()
        .getResponse().getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(contentAsString, ErrorResponse.class);
    assertThat(errorResponse.getCode()).isEqualTo(ErrorCode.VALIDATION_FAILED.getCode());
  }

  @Test
  void should_return_error_given_blank_value_when_create_invoice() throws Exception {
    CreateInvoiceRequest request = new CreateInvoiceRequest("", InvoiceType.COMPANY, "");
    RequestBuilder builder = post("/api/v1/order/1/invoice")
        .header("Content-Type", "application/json")
        .content(objectMapper.writeValueAsString(request));
    String contentAsString = mockMvc.perform(builder)
        .andExpect(status().is(400))
        .andReturn()
        .getResponse().getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(contentAsString, ErrorResponse.class);
    assertThat(errorResponse.getCode()).isEqualTo(ErrorCode.VALIDATION_FAILED.getCode());
  }

  @Test
  void should_return_200_when_create_invoice() throws Exception {
    CreateInvoiceRequest request = new CreateInvoiceRequest("customerId", InvoiceType.COMPANY,
        "title");
    RequestBuilder builder = post("/api/v1/order/1/invoice")
        .header("Content-Type", "application/json")
        .content(objectMapper.writeValueAsString(request));
    mockMvc.perform(builder)
        .andExpect(status().is(200));
  }

}