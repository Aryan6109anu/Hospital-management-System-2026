package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LabStaffDto;
import com.example.demo.entities.LabStaff;
import com.example.demo.entities.User;
import com.example.demo.mapper.LabStaffMapper;
import com.example.demo.model.Role;
import com.example.demo.repository.LabStaffRepository;
import com.example.demo.repository.UserRepository;

@Service
public class LabStaffServiceImpl implements LabStaffService {

    private final LabStaffRepository staffRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public LabStaffServiceImpl(LabStaffRepository staffRepo,
                               UserRepository userRepo,
                               PasswordEncoder passwordEncoder) {
        this.staffRepo = staffRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    @Override
    public LabStaffDto createStaff(LabStaffDto dto) {

        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail()); 
        user.setRole(Role.ROLE_LAB);

        userRepo.save(user);

        LabStaff staff = new LabStaff();
        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());
        staff.setUser(user);

        return LabStaffMapper.toDto(staffRepo.save(staff));
    }

    // UPDATE
    @Override
    public LabStaffDto updateStaff(Long id, LabStaffDto dto) {

        LabStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab Staff not found"));

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

        return LabStaffMapper.toDto(staffRepo.save(staff));
    }

    // GET BY ID
    @Override
    public LabStaffDto getById(Long id) {

        LabStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab Staff not found"));

        return LabStaffMapper.toDto(staff);
    }

    // GET ALL
    @Override
    public List<LabStaffDto> getAll() {

        return staffRepo.findAll()
                .stream()
                .map(LabStaffMapper::toDto)
                .collect(Collectors.toList());
    }

    // DELETE
    @Override
    public void deleteById(Long id) {

        LabStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab Staff not found"));

        userRepo.delete(staff.getUser());
        staffRepo.delete(staff);
    }

    // GET BY USERNAME
    @Override
    public LabStaffDto getByUsername(String username) {

        LabStaff staff = staffRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Lab Staff not found"));

        return LabStaffMapper.toDto(staff);
    }
}