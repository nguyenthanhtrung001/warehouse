package com.example.orderservice.repository;

import com.example.orderservice.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    List<ContactInfo> findByCustomerId(Long customerId);

}
