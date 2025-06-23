package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.User.UpdateRoleAccountDTO;
import edu.fpt.customflorist.dtos.User.UpdateUserDTO;
import edu.fpt.customflorist.dtos.User.UserDTO;
import edu.fpt.customflorist.dtos.User.UserLoginDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.User;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.responses.User.CustomerResponse;
import edu.fpt.customflorist.services.User.IUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/api/v1/users")
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class UserController {
    private final IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            User user = userService.createUser(userDTO);

            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Create account successfully")
                            .data(CustomerResponse.fromUser(user))
                            .status(HttpStatus.OK)
                            .build()
            );
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            String token = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());

            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Login successfully")
                            .data(token)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public RedirectView verifyAccount(@RequestParam("code") String verificationCode) {
        RedirectView redirectView = new RedirectView();

        try {
            userService.verifyAccount(verificationCode);
            redirectView = new RedirectView("/verify-success.html");
//            return ResponseEntity.ok(ResponseObject.builder()
//                    .message("Account verified successfully!")
//                    .data(null)
//                    .status(HttpStatus.OK)
//                    .build());
        } catch (DataNotFoundException e) {
//            return ResponseEntity.badRequest().body(ResponseObject.builder()
//                    .message("The verification code is invalid or has already been used.")
//                    .data(null)
//                    .status(HttpStatus.BAD_REQUEST)
//                    .build());
        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(ResponseObject.builder()
//                    .message("An error occurred while verifying the account.")
//                    .data(null)
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .build());
        }
        return redirectView;
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<ResponseObject> requestResetPassword(@RequestParam String email) {
        try {
            userService.requestResetPassword(email);
            return ResponseEntity.ok(new ResponseObject(
                    "Password reset link has been sent to your email.", HttpStatus.OK, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject(
                    e.getMessage(), HttpStatus.BAD_REQUEST, null));
        }
    }

    @PostMapping("/reset-password/confirm")
    public RedirectView confirmResetPassword(@RequestParam String token, @RequestParam String newPassword) {
        RedirectView redirectView = new RedirectView();

        try {
            userService.confirmResetPassword(token, newPassword);
            redirectView = new RedirectView("/reset-password-success.html");
//            return ResponseEntity.ok(new ResponseObject(
//                    "Password has been reset successfully.", HttpStatus.OK, null));
        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new ResponseObject(
//                    e.getMessage(), HttpStatus.BAD_REQUEST, null));
        }

        return redirectView;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String keyword,
                                         @Parameter(description = "Role to filter by (ADMIN, CUSTOMER, SHIPPER, MANAGER)")
                                         @RequestParam(required = false) String role,
                                         @Parameter(description = "Account status to filter by (ACTIVE, BANNED)")
                                             @RequestParam(required = false) String accountStatus,
                                         @Parameter(description = "Gender to filter by (Male, Female, Other)")
                                             @RequestParam(required = false) String gender,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "50") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CustomerResponse> users = userService.findAll(keyword, role, accountStatus, gender, pageable).map(CustomerResponse::fromUser);

            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Fetch users successfully")
                    .data(users)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Fetch user successfully")
                    .data(CustomerResponse.fromUser(user))
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            User updatedUser = userService.updateUser(id, updateUserDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("User updated successfully")
                    .data(CustomerResponse.fromUser(updatedUser))
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        try {
            userService.resetPassword(id, oldPassword, newPassword);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Password reset successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> blockOrEnableUser(@PathVariable Long id, @RequestParam Boolean active) {
        try {
            userService.blockOrEnable(id, active);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(active ? "User enabled successfully" : "User blocked successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id,
                                            @Valid @RequestBody
                                            UpdateRoleAccountDTO newRole)
    {
        try {
            userService.UpdateRoleAccount(id, newRole);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("User role updated successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .message("User not found")
                            .status(HttpStatus.NOT_FOUND)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .message("An error occurred while updating user role")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );
        }
    }
}
