package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.model.definitions.auth.AuthenticationRequest;
import com.dss.tennis.tournament.tables.model.definitions.auth.AuthenticationResponse;
import com.dss.tennis.tournament.tables.model.definitions.auth.RefreshTokenRequest;
import com.dss.tennis.tournament.tables.security.Admin;
import com.dss.tennis.tournament.tables.security.AdminDetailsService;
import com.dss.tennis.tournament.tables.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AdminDetailsService adminDetailsService;

    @PostMapping
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        adminDetailsService.validateUnsuccessfulCounter(authenticationRequest.getUsername());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest
                            .getPassword())
            );
        } catch (BadCredentialsException exception) {
            adminDetailsService.increaseUnsuccessfulCounter(authenticationRequest.getUsername());
            throw exception;
        }

        adminDetailsService.resetUnsuccessfulCounter(authenticationRequest.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        String refreshToken = ((Admin) authentication.getPrincipal()).getRefreshToken();
        return new ResponseEntity<>(new AuthenticationResponse(jwt, refreshToken), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAuthenticationToken(@RequestBody RefreshTokenRequest refreshRequest) {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (admin.getRefreshToken().equals(refreshRequest.getRefreshToken()) && LocalDateTime.now()
                .isBefore(admin.getRefreshTokenExpirationTime())) {
            String jwt = jwtTokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication());
            return new ResponseEntity<>(new AuthenticationResponse(jwt, admin.getRefreshToken()), HttpStatus.OK);
        }
        throw new DetailedException(ErrorConstants.ErrorKey.AUTHENTICATION_FAILED);
    }
}
