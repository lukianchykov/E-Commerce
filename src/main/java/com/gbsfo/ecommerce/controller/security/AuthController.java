package com.gbsfo.ecommerce.controller.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.gbsfo.ecommerce.domain.security.ERole;
import com.gbsfo.ecommerce.domain.security.Role;
import com.gbsfo.ecommerce.domain.security.User;
import com.gbsfo.ecommerce.dto.payload.request.LoginRequest;
import com.gbsfo.ecommerce.dto.payload.request.SignupRequest;
import com.gbsfo.ecommerce.dto.payload.response.JwtResponse;
import com.gbsfo.ecommerce.dto.payload.response.MessageResponse;
import com.gbsfo.ecommerce.repository.security.RoleRepository;
import com.gbsfo.ecommerce.repository.security.UserRepository;
import com.gbsfo.ecommerce.security.jwt.JwtUtils;
import com.gbsfo.ecommerce.service.security.UserDetailsImpl;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gbsfo.ecommerce.utils.Constants.API_VERSION_PREFIX_V1;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(API_VERSION_PREFIX_V1 + "/auth")
@ApiResponses(value = {
    @ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
    @ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
    @ApiResponse(code = 500, message = "The server is down. Please bear with us."),
})
public class AuthController {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            JwtResponse.builder().token(jwt).id(userDetails.getId()).username(userDetails.getUsername()).email(userDetails.getEmail()).roles(roles)
                .build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Email is already in use!"));
        }

        Set<Role> roles = getRoles(signUpRequest);

        User user = new User(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()),
            roles
        );
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private Set<Role> getRoles(SignupRequest signUpRequest) {
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            throw new RuntimeException("Error: Roles are missing in the request.");
        }

        strRoles.forEach(role -> {
            Role foundRole;
            switch (role.toUpperCase()) {
                case "ADMIN":
                    foundRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
                    break;
                case "USER":
                    foundRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                    break;
                case "MOD":
                    foundRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                        .orElseThrow(() -> new RuntimeException("Error: Role MOD is not found."));
                    break;
                default:
                    throw new RuntimeException("Error: Invalid role provided.");
            }
            roles.add(foundRole);
        });
        return roles;
    }
}
