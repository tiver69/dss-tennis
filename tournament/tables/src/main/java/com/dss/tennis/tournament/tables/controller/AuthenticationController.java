package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.model.request.PostAuthentication;
import com.dss.tennis.tournament.tables.model.response.v1.AuthenticationResponse;
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
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody PostAuthentication authenticationRequest) {
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
        return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
    }
}
