package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.VisitRequestDto;
import com.example.demo.dto.VisitResponseDto;
import com.example.demo.dto.VisitUpdateDto;
import com.example.demo.model.Department;

public interface VisitService {

	// 1. Naya visit start karne ke liye
    VisitResponseDto startVisit(VisitRequestDto dto);

    // 2. Visit ID (Primary Key) se data nikalne ke liye (Jisme error aa raha tha)
    VisitResponseDto getVisitById(Long visitId);

    // 3. Appointment ID se visit dhoondne ke liye (Ye kaafi kaam aayega)
    VisitResponseDto getVisitByAppointmentId(Long appointmentId);

    // 4. Visit update karne ke liye
    VisitResponseDto updateVisit(Long visitId, VisitUpdateDto dto);

    // 5. Patient ki purani history dekhne ke liye
    List<VisitResponseDto> getPatientHistory(Long patientId);

    // 6. Check karne ke liye ki patient pehle aaya hai ya nahi
    boolean isPatientAlreadyVisited(Long patientId);

    // 7. Department wise check karne ke liye
    boolean isPatientAlreadyVisitedInDepartment(Long patientId, Department department);


}