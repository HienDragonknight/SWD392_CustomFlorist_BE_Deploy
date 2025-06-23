package edu.fpt.customflorist.dtos.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    @JsonProperty("email")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    private String name;

    @JsonProperty("address")
    @NotBlank(message = "Address number is required")
    private String address;

    @JsonProperty("phone")
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(0|\\+84)(\\s)?([2-9])([0-9]{8})$", message = "Invalid phone number format")
    private String phone;

    @JsonProperty("gender")
    @NotBlank(message = "Gender is required")
    @Schema(description = "User's gender (Male/Female/Other)", example = "Choose one of 3 Male/Female/Other")
    private String gender;

}
