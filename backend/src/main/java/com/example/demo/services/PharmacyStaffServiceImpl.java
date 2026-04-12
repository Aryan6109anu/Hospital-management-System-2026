package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PharmacyStaffDto;
import com.example.demo.entities.PharmacyStaff;
import com.example.demo.entities.User;
import com.example.demo.mapper.PharmacyStaffMapper;
import com.example.demo.model.Role;
import com.example.demo.repository.PharmacyStaffRepository;
import com.example.demo.repository.UserRepository;

@Service
public class PharmacyStaffServiceImpl implements PharmacyStaffService {

    private final PharmacyStaffRepository staffRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public PharmacyStaffServiceImpl(PharmacyStaffRepository staffRepo,
                                    UserRepository userRepo,
                                    PasswordEncoder passwordEncoder) {
        this.staffRepo = staffRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    @Override
    public PharmacyStaffDto createStaff(PharmacyStaffDto dto) {

        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(Role.ROLE_PHARMACY);

        userRepo.save(user);

        PharmacyStaff staff = new PharmacyStaff();
        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());
        staff.setUser(user);

        return PharmacyStaffMapper.toDto(staffRepo.save(staff));
    }

    // UPDATE
    @Override
    public PharmacyStaffDto updateStaff(Long id, PharmacyStaffDto dto) {

        PharmacyStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacy Staff not found"));

        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());

        if (dto.getUsername() != null &&
                !dto.getUsername().equals(staff.getUser().getUsername())) {

            if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            staff.getUser().setUsername(dto.getUsername());
        }

        return PharmacyStaffMapper.toDto(staffRepo.save(staff));
    }

    // GET BY ID
    @Override
    public PharmacyStaffDto getById(Long id) {

        PharmacyStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacy Staff not found"));

        return PharmacyStaffMapper.toDto(staff);
    }

    // GET ALL
    @Override
    public List<PharmacyStaffDto> getAll() {

        return staffRepo.findAll()
                .stream()
                .map(PharmacyStaffMapper::toDto)
                .collect(Collectors.toList());
    }

    // DELETE
    @Override
    public void deleteById(Long id) {

        PharmacyStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacy Staff not found"));

        userRepo.delete(staff.getUser());
        staffRepo.delete(staff);
    }

    // GET BY USERNAME
    @Override
    public PharmacyStaffDto getByUsername(String username) {

        PharmacyStaff staff = staffRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Pharmacy Staff not found"));

        return PharmacyStaffMapper.toDto(staff);
    }
}