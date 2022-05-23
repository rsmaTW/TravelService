package com.tw.travel.dao.entity;

import static com.tw.travel.dao.entity.TravelOrder.OrderStatus.PAYMENT_SETTLED;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "travel_order")
@Getter
@Setter
public class TravelOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String customerId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private String startingPoint;
  private String destination;
  private Double actuallyPaidAmount;

  private LocalDateTime createDatetime;
  private LocalDateTime updateDatetime;

  public boolean canCreateInvoice() {
    return this.status == PAYMENT_SETTLED;
  }

  public void invoiceCreated() {
    this.status = OrderStatus.INVOICED;
    this.updateDatetime = LocalDateTime.now();
  }

  public enum OrderStatus {
    INITIATED, IN_TRAVEL, PENDING_FOR_PAY, PAYMENT_SETTLED, CANCELLED, INVOICED
  }
}
