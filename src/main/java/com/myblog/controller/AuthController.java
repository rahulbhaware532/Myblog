package com.myblog.controller;

import com.myblog.entity.Role;
import com.myblog.entity.User;
import com.myblog.payload.JWTAuthResponse;
import com.myblog.payload.LoginDto;
import com.myblog.payload.SignUpDto;
import com.myblog.repository.RoleRepository;
import com.myblog.repository.UserRepository;
import com.myblog.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    /**
     * //Authentication => Is nothing but to verify username and password
     * Token => Token are nothing but an alternate way to perform a login
     * Advantages os JWT token are :
     * It makes the server stateless
     * We can set the validity of the token also we can set the session timeout on that
     * Authorization => Is giving access of features based on the roles
     */

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;


    @PostMapping("/signup")
    public ResponseEntity<?> resisterUser(@RequestBody SignUpDto signUpDto) {

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email already exists: " + signUpDto.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username already exists: " + signUpDto.getUsername(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // Check if the "ROLE_ADMIN" exists, create it if not
        Role roles = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role newAdminRole = new Role("ROLE_ADMIN");
                    roleRepository.save(newAdminRole);
                    return newAdminRole;
                });

        Set<Role> role = new HashSet<>();
        role.add(roles);
        user.setRoles(role);

        userRepository.save(user);

        return new ResponseEntity<>("User is registered !!!! ", HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication); //is like a session variable its is central class
            // Get token from TokenProvider

            String token = tokenProvider.generateToken(authentication);
            System.out.println(token);
            return ResponseEntity.ok(new JWTAuthResponse(token));

        } catch (AuthenticationException e) {
            // Authentication failed
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

    }
}
