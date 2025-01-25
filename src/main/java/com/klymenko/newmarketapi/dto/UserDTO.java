package com.klymenko.newmarketapi.dto;

import com.klymenko.newmarketapi.dto.validators.ValidRole;
import com.klymenko.newmarketapi.enums.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @Length(min = 2, max = 80, message = "Name have to be in between 2 and 80 characters")
    @NotBlank(message = "You have to provide a name")
    private String name;

    @Email(message = "You have to provide an valid email")
    @NotBlank(message = "You have to provide an email")
    private String email;

    @Length(min = 5, message = "Password have to be at least 5 characters")
    @NotBlank(message = "You have to provide a password")
    private String password;

    private String pictureUrl;

    @ValidRole
    @NotNull(message = "You have to provide a role")
    private String role;
}
