package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.LabOrderItem;

public interface LabOrderItemRepository extends JpaRepository<LabOrderItem, Long> {
	
}