package com.example.demo.services;

import com.example.demo.dto.BillRequestDTO;
import com.example.demo.dto.BillResponseDTO;

public interface BillingService {
    // Bill generate karne ya dekhne ke liye
    BillResponseDTO getBillByVisitId(Long visitId);
    
    // Payment process karne aur stock kam karne ke liye
    BillResponseDTO processPayment(Long visitId, String paymentMode, BillRequestDTO request);
}