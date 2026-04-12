package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DoctorScheduleRequest;
import com.example.demo.dto.DoctorSlotResponse;
import com.example.demo.entities.DoctorSchedule;
import com.example.demo.model.WorkingDay;
import com.example.demo.services.DoctorScheduleService;

@RestController
@RequestMapping("/api/doctor-schedules")
@CrossOrigin("*")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

  	public DoctorScheduleController(DoctorScheduleService doctorScheduleService) {
		super();
		this.doctorScheduleService = doctorScheduleService;
	}

	// ================= CREATE =================
    @PostMapping
    public ResponseEntity<DoctorSchedule> create(@RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.ok(doctorScheduleService.create(request));
    }
    // 🔥 BULK
    @PostMapping("/bulk")
    public ResponseEntity<List<DoctorSchedule>> createBulk(
            @RequestBody List<DoctorScheduleRequest> requests) {
        return ResponseEntity.ok(doctorScheduleService.createBulk(requests));
    }

    // ================= UPDATE =================
    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorSchedule> update(
            @PathVariable Long doctorId,
            @RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.ok(doctorScheduleService.update(doctorId, request));
    }

    // ================= GET ONE =================
    @GetMapping("/{doctorId}")
    public ResponseEntity<?> getOne(@PathVariable Long doctorId) {

        DoctorSchedule schedule = doctorScheduleService.getByDoctorId(doctorId);

        if(schedule == null){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(schedule);
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<DoctorSchedule>> getAll() {
        return ResponseEntity.ok(doctorScheduleService.getAll());
    }

    // ================= DELETE =================
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<String> delete(@PathVariable Long doctorId) {
        doctorScheduleService.deleteByDoctorId(doctorId);
        return ResponseEntity.ok("Doctor schedule deleted successfully");
    }

 // ================= CHECK AVAILABILITY =================
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<Boolean> availability(
            @PathVariable Long doctorId,
            @RequestParam String date) {  // String, not LocalDate
        try {
            LocalDate localDate = LocalDate.parse(date); // parse as yyyy-MM-dd
            boolean available = doctorScheduleService.isDoctorAvailable(doctorId, localDate);
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

 // ================= GET GENERATED SLOTS =================
    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<DoctorSlotResponse>> getSlotsForDate(
            @PathVariable Long doctorId,
            @RequestParam String date) {  // String, not LocalDate
        try {
            LocalDate localDate = LocalDate.parse(date); // parse as yyyy-MM-dd
            List<DoctorSlotResponse> slots = doctorScheduleService.getAvailableSlots(doctorId, localDate);
            return ResponseEntity.ok(slots);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    @GetMapping("/{doctorId}/available-dates")
    public List<LocalDate> getAvailableDates(@PathVariable Long doctorId) {

        DoctorSchedule schedule = doctorScheduleService.getByDoctorId(doctorId);

        List<WorkingDay> workingDays = schedule.getDays();

        Set<LocalDate> dates = new HashSet<>();

        LocalDate today = LocalDate.now();

        for (int i = 0; i < 30; i++) {   // next 30 days
            LocalDate date = today.plusDays(i);

            WorkingDay day = WorkingDay.valueOf(
                    date.getDayOfWeek().name()
            );

            if (workingDays.contains(day)) {
                dates.add(date);
            }
        }

        return dates.stream().sorted().toList();
    }

    
}
