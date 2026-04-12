package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BillingStaffDto;
import com.example.demo.entities.BillingStaff;
import com.example.demo.entities.User;
import com.example.demo.mapper.BillingStaffMapper;
import com.example.demo.model.Role;
import com.example.demo.repository.BillingStaffRepository;
import com.example.demo.repository.UserRepository;

@Service
public class BillingStaffServiceImpl implements BillingStaffService {

    private final BillingStaffRepository staffRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public BillingStaffServiceImpl(BillingStaffRepository staffRepo,
                                   UserRepository userRepo,
                                   PasswordEncoder passwordEncoder) {
        this.staffRepo = staffRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= CREATE =================
    @Override
    public BillingStaffDto createStaff(BillingStaffDto dto) {

        // 🔥 Duplicate username check
        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // 1️⃣ Create User
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(Role.ROLE_BILLING);

        userRepo.save(user);

        // 2️⃣ Create BillingStaff
        BillingStaff staff = new BillingStaff();
        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());
        staff.setUser(user);

        return BillingStaffMapper.toDto(staffRepo.save(staff));
    }

    // ================= UPDATE =================
    @Override
    public BillingStaffDto updateStaff(Long id, BillingStaffDto dto) {

        BillingStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Billing Staff not found"));

        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());

        // 🔥 Optional: Update username
        if (dto.getUsername() != null &&
                !dto.getUsername().equals(staff.getUser().getUsername())) {

            if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            staff.getUser().setUsername(dto.getUsername());
        }

        return BillingStaffMapper.toDto(staffRepo.save(staff));
    }

    // ================= GET BY ID =================
    @Override
    public BillingStaffDto getById(Long id) {

        BillingStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Billing Staff not found"));

        return BillingStaffMapper.toDto(staff);
    }

    // ================= GET ALL =================
    @Override
    public List<BillingStaffDto> getAll() {

        return staffRepo.findAll()
                .stream()
                .map(BillingStaffMapper::toDto)
                .collect(Collectors.toList());
    }

    // ================= DELETE =================
    @Override
    public void deleteById(Long id) {

        BillingStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Billing Staff not found"));

        // delete linked user first
        userRepo.delete(staff.getUser());

        staffRepo.delete(staff);
    }

    // ================= GET BY USERNAME =================
    @Override
    public BillingStaffDto getByUsername(String username) {

        BillingStaff staff = staffRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Billing Staff not found"));

        return BillingStaffMapper.toDto(staff);
    }
}