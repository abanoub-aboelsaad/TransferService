package org.transferservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.dto.UpdatePasswordDTO; // Import UpdatePasswordDTO
import org.transferservice.exception.custom.CustomerAlreadyExistException;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.exception.response.ErrorDetails;
import org.transferservice.service.security.IAuthenticator;
import org.transferservice.service.security.JwtUtils;
import org.transferservice.service.security.UserActivityService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Validated
@Tag(name = "Customer Auth Controller", description = "Customer Auth controller")
public class AuthController {

    private final IAuthenticator authenticatorService;
    private final JwtUtils jwtUtils;
    private final UserActivityService userActivityService;

    @Operation(summary = "Register new Customer")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/register")
    public ResponseEntity<CustomerDTO> register(@RequestBody @Valid CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException {
        CustomerDTO customerDTO = this.authenticatorService.register(createCustomerDTO);
        return new ResponseEntity<>(customerDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Login and generate JWT")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = LoginResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = this.authenticatorService.login(loginRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Logout and invalidate JWT")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Extract token from header

            if (jwtUtils.validateJwtToken(token)) { // Validate token
                authenticatorService.logout(token); // Invalidate refresh token
                return ResponseEntity.ok("Logout successful");
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header is missing or invalid");
        }
    }

    @Operation(summary = "Update Customer Password")
    @ApiResponse(responseCode = "200", description = "Password updated successfully",
            content = {@Content(schema = @Schema(type = "string"), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Extract token from header
            String email = jwtUtils.getEmailFromJwtToken(token); // Extract email from token

            try {
                authenticatorService.updatePassword(email, updatePasswordDTO.getOldPassword(), updatePasswordDTO.getNewPassword());
                return ResponseEntity.ok("Password updated successfully");
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
            } catch (CustomerNotFoundException e) {
                return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header is missing or invalid");
        }
    }

}