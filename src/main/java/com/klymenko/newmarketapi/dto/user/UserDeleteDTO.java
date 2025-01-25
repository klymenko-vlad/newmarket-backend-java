package com.klymenko.newmarketapi.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDeleteDTO {
    @Length(min = 5, message = "Password have to be at least 5 characters")
    @NotBlank(message = "You have to provide a password")
    private String password;
}
