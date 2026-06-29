package com.cinesphere.main.dto;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Integer id;

    private String name;

    private String email;

    private String phone;

    private String role;

    private LocalDateTime createdAt;
}