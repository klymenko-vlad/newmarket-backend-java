package com.klymenko.newmarketapi.e2e.user;

import com.klymenko.newmarketapi.dto.user.LoginDTO;
import com.klymenko.newmarketapi.dto.user.UserDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserE2ETest {

    @LocalServerPort
    private int port;

    private String baseUrl;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1";
        restTemplate = new RestTemplate();
    }

    @Test
    public void createUserAndLogin_Success() {
        UserDTO userDTO = UserDTO.builder()
                .name("E2E User")
                .email("e2euser@gmail.com")
                .password("password123")
                .role("USER")
                .build();

        ResponseEntity<String> createUserResponse = restTemplate.postForEntity(
                baseUrl + "/register",
                userDTO,
                String.class
        );

        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        LoginDTO loginDTO = new LoginDTO("e2euser@gmail.com", "password123");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                baseUrl + "/login",
                loginDTO,
                String.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).contains("token");
    }

    @Test
    public void Login_Failed() {
        LoginDTO loginDTO = new LoginDTO("test@gmail.com", "wrongPassword");
        try {
            restTemplate.postForEntity(
                    baseUrl + "/login",
                    loginDTO,
                    String.class
            );
            Assertions.fail("Expected HttpClientErrorException$Unauthorized");
        } catch (HttpClientErrorException.Unauthorized ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(ex.getMessage()).contains("401");
        }
    }
}
