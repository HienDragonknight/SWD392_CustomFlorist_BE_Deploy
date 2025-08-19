package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.User.UserLoginDTO;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.User.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/api/v1")
public class LoginAliasController {

    private final IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAlias(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                        BindingResult result) {
        try {
            if (result.hasErrors()) {
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
}


