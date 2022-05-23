package com.tw.travel.dao.repository;

import com.tw.travel.dao.entity.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInvoiceRepository extends JpaRepository<OrderInvoice, Long> {

}
