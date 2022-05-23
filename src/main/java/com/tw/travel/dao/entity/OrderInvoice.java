package com.tw.travel.dao.entity;

import static com.tw.travel.service.dto.InvoiceStatus.GENERATED;
import static com.tw.travel.service.dto.InvoiceStatus.GENERATE_FAILED;

import com.tw.travel.service.dto.InvoiceStatus;
import com.tw.travel.service.dto.InvoiceType;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderInvoice {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private Long orderId;

  @Enumerated(EnumType.STRING)
  private InvoiceStatus status;

  @Enumerated(EnumType.STRING)
  private InvoiceType type;

  private String title;
  private Double amount;
  private String fileLocation;

  private LocalDateTime createDatetime;
  private LocalDateTime updateDatetime;

  public static OrderInvoice createGeneratedInvoice(TravelOrder order, InvoiceType type, String title,
      String fileLocation) {
    OrderInvoice orderInvoice = new OrderInvoice();
    orderInvoice.setOrderId(order.getId());
    orderInvoice.setType(type);
    orderInvoice.setStatus(GENERATED);
    orderInvoice.setTitle(title);
    orderInvoice.setAmount(order.getActuallyPaidAmount());
    orderInvoice.setFileLocation(fileLocation);

    LocalDateTime now = LocalDateTime.now();
    orderInvoice.setCreateDatetime(now);
    orderInvoice.setUpdateDatetime(now);

    return orderInvoice;
  }

  public static OrderInvoice createGeneratedFailedInvoice(TravelOrder order, InvoiceType type,
      String title) {
    OrderInvoice orderInvoice = new OrderInvoice();
    orderInvoice.setOrderId(order.getId());
    orderInvoice.setType(type);
    orderInvoice.setStatus(GENERATE_FAILED);
    orderInvoice.setTitle(title);
    orderInvoice.setAmount(order.getActuallyPaidAmount());

    LocalDateTime now = LocalDateTime.now();
    orderInvoice.setCreateDatetime(now);
    orderInvoice.setUpdateDatetime(now);

    return orderInvoice;
  }
}
