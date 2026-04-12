package com.example.demo.appointment_service_Testing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.AppointmentRequest;
import com.example.demo.dto.AppointmentResponse;
import com.example.demo.dto.DoctorSlotResponse;
import com.example.demo.dto.WalkInAppointmentRequestDto;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.DoctorSchedule;
import com.example.demo.entities.Patient;
import com.example.demo.entities.User;
import com.example.demo.model.AppointmentStatus;
import com.example.demo.model.Role;
import com.example.demo.model.WorkingDay;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.AppointmentServiceImpl;
import com.example.demo.services.DoctorScheduleService;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DoctorScheduleService doctorScheduleService; // ✅ Walk-in mock

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    // =========================
    // BOOK APPOINTMENT (PATIENT)
    // =========================
    @Test
    void bookAppointment_patient_success() {
        LocalDate today = LocalDate.now();
        LocalTime slotTime = LocalTime.of(10, 30);

        AppointmentRequest request = new AppointmentRequest();
        request.setDoctorId(2L);
        request.setPatientId(1L);
        request.setSlotDate(today);
        request.setSlotStartTime(slotTime);
        request.setDisease("Fever");

        User user = new User();
        user.setId(1L);
        user.setUsername("rahul");
        user.setRole(Role.ROLE_PATIENT);

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setUser(user);
        patient.setName("Rahul");
        patient.setMobile("9876543210");
        patient.setAge(25);
        patient.setGender("Male");
        patient.setAddress("Delhi");

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDays(List.of(
                WorkingDay.MONDAY,
                WorkingDay.TUESDAY,
                WorkingDay.WEDNESDAY,
                WorkingDay.THURSDAY,
                WorkingDay.FRIDAY
        ));
        schedule.setStartTime(LocalTime.of(10, 0));
        schedule.setEndTime(LocalTime.of(12, 0));

        Doctor doctor = new Doctor();
        doctor.setId(2L);
        doctor.setName("Dr. Sharma");
        doctor.setSpecialization("Cardiology");
        doctor.setSchedule(schedule);

        when(userRepository.findByUsername("rahul")).thenReturn(Optional.of(user));
        when(patientRepository.findByUser(user)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        AppointmentResponse response = appointmentService.bookAppointment(request, "rahul");

        assertNotNull(response);
        assertEquals("Rahul", response.getPatientName());
        assertEquals("Dr. Sharma", response.getDoctorName());
        assertEquals(AppointmentStatus.BOOKED, response.getStatus());
       assertEquals(slotTime, response.getSlotStartTime());
        verify(appointmentRepository).save(any());
    }

    // =========================
    // CANCEL APPOINTMENT (PATIENT)
    // =========================
    @Test
    void cancelAppointment_byPatient_success() {
        User user = new User();
        user.setUsername("rahul");

        Patient patient = new Patient();
        patient.setUser(user);

        var appointment = new com.example.demo.entities.Appointment();
        appointment.setPatient(patient);
        appointment.setSlotStartTime(LocalTime.of(10, 30));
        appointment.setStatus(AppointmentStatus.BOOKED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        String result = appointmentService.cancelAppointmentByPatient(1L, "rahul");

        assertEquals("Appointment cancelled successfully", result);
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    // =========================
    // CANCEL APPOINTMENT (ADMIN)
    // =========================
    @Test
    void adminCancelAppointment_success() {
        var appointment = new com.example.demo.entities.Appointment();
        appointment.setSlotStartTime(LocalTime.of(10, 30));
        appointment.setStatus(AppointmentStatus.BOOKED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        String result = appointmentService.cancelAppointmentByAdmin(1L);

        assertEquals("Appointment cancelled successfully by admin", result);
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    // =========================
    // GET APPOINTMENTS BY DOCTOR ID
    // =========================
    @Test
    void getAppointmentsByDoctorId_success() {
        Doctor doctor = new Doctor();
        doctor.setId(2L);
        doctor.setName("Dr. Sharma");
        doctor.setSpecialization("Cardiology");

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setName("Aman");

        var appointment = new com.example.demo.entities.Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setSlotStartTime(LocalTime.of(9, 0));
        appointment.setStatus(AppointmentStatus.BOOKED);

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorId(2L)).thenReturn(List.of(appointment));

        List<AppointmentResponse> result = appointmentService.getAppointmentsByDoctorId(2L);

        assertEquals(1, result.size());
        assertEquals("Dr. Sharma", result.get(0).getDoctorName());
    }

    // =========================
    // GET MY APPOINTMENTS (PATIENT)
    // =========================
    @Test
    void getMyAppointmentsForPatient_success() {
        User user = new User();
        user.setId(5L);
        user.setUsername("amit");
        user.setRole(Role.ROLE_PATIENT);

        Patient patient = new Patient();
        patient.setId(10L);
        patient.setName("Amit");
        patient.setUser(user);

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Verma");

        var appointment = new com.example.demo.entities.Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setSlotStartTime(LocalTime.of(11, 0));

        when(userRepository.findByUsername("amit")).thenReturn(Optional.of(user));
        when(patientRepository.findByUser(user)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientId(10L)).thenReturn(List.of(appointment));

        List<AppointmentResponse> result = appointmentService.getMyAppointmentsForPatient("amit");

        assertEquals(1, result.size());
        assertEquals("Amit", result.get(0).getPatientName());
        assertEquals(appointment.getSlotStartTime(), result.get(0).getSlotStartTime());
    }

    // =========================
    // WALK-IN BOOKING TEST
    // =========================
    @Test
    void bookWalkInAppointment_success() {
        LocalDate today = LocalDate.now();
        LocalTime slotStart = LocalTime.of(11, 0);
        LocalTime slotEnd = LocalTime.of(11, 30);

        WalkInAppointmentRequestDto dto = new WalkInAppointmentRequestDto();
        dto.setDoctorId(3L);
        dto.setMobile("9998887777");
        dto.setPatientName("Sunil");
        dto.setAge(30);
        dto.setGender("Male");
        dto.setAddress("Mumbai");
        dto.setAppointmentDate(today);
        dto.setSlotStartTime(slotStart);
        dto.setSlotEndTime(slotEnd);
        dto.setDisease("Cold");

        Doctor doctor = new Doctor();
        doctor.setId(3L);
        doctor.setName("Dr. Gupta");
        doctor.setSpecialization("General");

        // ✅ Mock DoctorScheduleService
        when(doctorRepository.findById(3L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findByMobile("9998887777")).thenReturn(Optional.empty());
        when(patientRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(doctorScheduleService.getAvailableSlots(3L, today))
                .thenReturn(List.of(new DoctorSlotResponse(today, slotStart, slotEnd, false)));

        AppointmentResponse response = appointmentService.bookWalkInAppointment(dto);

        assertNotNull(response);
        assertEquals("Sunil", response.getPatientName());
        assertEquals("Dr. Gupta", response.getDoctorName());
        assertEquals(slotStart, response.getSlotStartTime());
        assertEquals(slotEnd, response.getSlotEndTime());
        assertEquals("Cold", response.getDisease());
    }
}