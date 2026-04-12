package com.example.demo.doctor_Controller_Testing;

import com.example.demo.controllers.DoctorController;
import com.example.demo.services.DoctorService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @Test
    void testGetDoctors() {
        when(doctorService.getAllDoctors()).thenReturn(List.of());

        var result = doctorController.getAllDoctors();

        assertNotNull(result);
        verify(doctorService, times(1)).getAllDoctors();
    }
}
