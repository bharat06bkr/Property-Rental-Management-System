package com.propertyrental.service;

import com.propertyrental.dto.request.LoginRequest;
import com.propertyrental.dto.request.RegisterRequest;
import com.propertyrental.dto.response.JwtResponse;
import com.propertyrental.dto.response.UserResponse;
import com.propertyrental.entity.Enums.OwnerStatus;
import com.propertyrental.entity.Enums.Role;
import com.propertyrental.entity.User;
import com.propertyrental.exception.CustomExceptions.BadRequestException;
import com.propertyrental.exception.CustomExceptions.DuplicateResourceException;
import com.propertyrental.exception.CustomExceptions.ResourceNotFoundException;
import com.propertyrental.mapper.EntityDtoMapper;
import com.propertyrental.repository.UserRepository;
import com.propertyrental.security.JwtTokenProvider;
import com.propertyrental.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email address is already in use: " + request.getEmail());
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        if (request.getRole() == Role.OWNER) {
            user.setOwnerStatus(OwnerStatus.PENDING);
        }

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == Role.OWNER) {
            notificationService.createNotification(
                    savedUser,
                    "Owner Registration Received",
                    "Your owner account has been registered and is pending admin approval.",
                    "OWNER_REGISTRATION"
            );
        } else {
            notificationService.createNotification(
                    savedUser,
                    "Welcome to Property Rental Portal",
                    "Your tenant account is active. You can start searching and applying for properties.",
                    "WELCOME"
            );
        }

        return EntityDtoMapper.toUserResponse(savedUser);
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new BadRequestException("Your account has been deactivated. Please contact support.");
        }

        return new JwtResponse(
                jwt,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getOwnerStatus()
        );
    }

    public UserResponse getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return EntityDtoMapper.toUserResponse(user);
    }
}
