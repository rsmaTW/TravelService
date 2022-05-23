package com.tw.travel.dao.repository;

import com.tw.travel.dao.entity.TravelOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<TravelOrder, Long> {

  TravelOrder getById(Long orderId);
}
