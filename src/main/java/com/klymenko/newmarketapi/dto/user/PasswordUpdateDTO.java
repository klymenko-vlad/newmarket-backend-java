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
public class PasswordUpdateDTO {
    @Length(min = 5, message = "Your old password have to be at least 5 characters")
    @NotBlank(message = "You have to provide your current password")
    private String oldPassword;

    @Length(min = 5, message = "Your new password have to be at least 5 characters")
    @NotBlank(message = "You have to provide your new password")
    private String newPassword;
}
