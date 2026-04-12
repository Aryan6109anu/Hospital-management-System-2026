package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.dto.DoctorScheduleRequest;
import com.example.demo.dto.DoctorSlotResponse;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.DoctorSchedule;
import com.example.demo.model.WorkingDay;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.DoctorScheduleRepository;
import com.example.demo.repository.DoctorUnavailableDateRepository;

@Service
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepo;
    private final DoctorRepository doctorRepo;
    private final DoctorUnavailableDateRepository unavailableRepo;
    private final AppointmentRepository appointmentRepository;

    public DoctorScheduleServiceImpl(
            DoctorScheduleRepository scheduleRepo,
            DoctorRepository doctorRepo,
            DoctorUnavailableDateRepository unavailableRepo,
            AppointmentRepository appointmentRepository) {
        this.scheduleRepo = scheduleRepo;
        this.doctorRepo = doctorRepo;
        this.unavailableRepo = unavailableRepo;
        this.appointmentRepository= appointmentRepository;
    }

    // ================= CREATE =================
    @Override
    public DoctorSchedule create(DoctorScheduleRequest request) {

        Doctor doctor = doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // agar pehle se schedule hai → update
        Optional<DoctorSchedule> existing =
                scheduleRepo.findByDoctorId(request.getDoctorId());

        DoctorSchedule schedule = existing.orElseGet(DoctorSchedule::new);

        schedule.setDoctor(doctor);
        schedule.setDays(request.getDays());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setMaxPatientsPerDay(request.getMaxPatientsPerDay());
        schedule.setSlotDuration(request.getSlotDuration());

        return scheduleRepo.save(schedule);
    }
                      // 🔥 BULK INSERT
    @Override
    public List<DoctorSchedule> createBulk(List<DoctorScheduleRequest> requests) {
        List<DoctorSchedule> schedules = new ArrayList<>();
        for (DoctorScheduleRequest request : requests) {
            schedules.add(create(request));   // create() me ab setDays use hoga
        }
        return schedules;
    }


    // ================= UPDATE =================
    @Override
    public DoctorSchedule update(Long doctorId, DoctorScheduleRequest request) {
        DoctorSchedule schedule = scheduleRepo.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor schedule not found"));

        // ✅ updated line
        schedule.setDays(request.getDays());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setMaxPatientsPerDay(request.getMaxPatientsPerDay());
        schedule.setSlotDuration(request.getSlotDuration());

        return scheduleRepo.save(schedule);
    }


    // ================= GET ONE =================
    @Override
    public DoctorSchedule getByDoctorId(Long doctorId) {
        return scheduleRepo.findByDoctorId(doctorId).orElse(null);
    }

    // ================= GET ALL =================
    @Override
    public List<DoctorSchedule> getAll() {
        return scheduleRepo.findAll();
    }

    // ================= DELETE =================
    @Override
    public void deleteByDoctorId(Long doctorId) {
        if (!scheduleRepo.findByDoctorId(doctorId).isPresent()) {
            throw new RuntimeException("Doctor schedule not found");
        }
        scheduleRepo.deleteByDoctorId(doctorId);
    }

    // ================= AVAILABILITY =================
    @Override
    public boolean isDoctorAvailable(Long doctorId, LocalDate date) {

        DoctorSchedule schedule = getByDoctorId(doctorId);

        // Enum directly nikalo
        WorkingDay today = WorkingDay.valueOf(
                date.getDayOfWeek().name()
        );

        // Ab direct enum list se check
        boolean worksThatDay = schedule.getDays().contains(today);

        boolean notUnavailable =
                !unavailableRepo.existsByDoctorIdAndUnavailableDate(doctorId, date);

        return worksThatDay && notUnavailable;
    }


 // ================= AVAILABLE SLOTS =================
    @Override
    public List<DoctorSlotResponse> getAvailableSlots(Long doctorId, LocalDate date) {

        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));

        // Schedule check
        if (doctor.getSchedule() == null || doctor.getSchedule().getDays() == null)
            return List.of();

        List<WorkingDay> workingDays = doctor.getSchedule().getDays();
        WorkingDay today = WorkingDay.valueOf(date.getDayOfWeek().name());

        System.out.println("APPOINTMENT DATE = " + date);
        System.out.println("DAY = " + today);
        System.out.println("DOCTOR WORKING DAYS = " + workingDays);

        // Doctor not working today
        if (!workingDays.contains(today)) {
            return List.of();
        }

        LocalTime start = doctor.getSchedule().getStartTime();
        LocalTime end = doctor.getSchedule().getEndTime();
        int duration = doctor.getSchedule().getSlotDuration();

        int slotsCount = (int) ((end.toSecondOfDay() - start.toSecondOfDay()) / (duration * 60));

        // Fetch already booked times and normalize seconds/nanos
        List<LocalTime> bookedTimes = appointmentRepository
                .findByDoctorId(doctorId)
                .stream()
                .map(a -> a.getSlotStartTime().withSecond(0).withNano(0))
                .collect(Collectors.toList());

        System.out.println("SCHEDULE START = " + start);
        System.out.println("SCHEDULE END = " + end);
        System.out.println("DURATION = " + duration);
        System.out.println("SLOTS COUNT = " + slotsCount);
        System.out.println("BOOKED TIMES = " + bookedTimes);

        // Generate all possible slots
     // Generate all possible slots
        List<DoctorSlotResponse> slots = IntStream.range(0, slotsCount)
                .mapToObj(i -> start.plusMinutes(i * duration))
                .map(time -> {
                    LocalTime cleanTime = time.withSecond(0).withNano(0);
                    boolean isBooked = bookedTimes.stream()
                            .anyMatch(bt -> bt.equals(cleanTime));
                    return new DoctorSlotResponse(date, cleanTime, cleanTime.plusMinutes(duration), isBooked);
                })
                .collect(Collectors.toList());

        // ✅ ONLY AVAILABLE SLOTS RETURN
        return slots.stream()
                .filter(s -> !s.isBooked())
                .collect(Collectors.toList());

    }

  

}
