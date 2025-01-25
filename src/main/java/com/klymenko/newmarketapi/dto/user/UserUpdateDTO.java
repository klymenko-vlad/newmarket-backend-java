package com.klymenko.newmarketapi.dto.user;

import com.klymenko.newmarketapi.dto.validators.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {
    @Length(min = 2, max = 80, message = "Name have to be in between 2 and 80 characters")
    private String name;

    @Email(message = "You have to provide an valid email")
    private String email;

    private String pictureUrl;

}
