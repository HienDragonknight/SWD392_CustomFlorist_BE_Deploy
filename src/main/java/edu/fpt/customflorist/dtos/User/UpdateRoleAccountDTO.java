package edu.fpt.customflorist.dtos.User;

import edu.fpt.customflorist.models.Enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleAccountDTO {
    @NotNull(message = "Role cannot be null")
    @Schema(
            description = "New role to assign to the user",
            example = "Available values: ADMIN, CUSTOMER, SHIPPER, MANAGER"
    )
    private Role newRole;
}
