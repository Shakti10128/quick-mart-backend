package com.ecommerce.Dto;

import com.ecommerce.entity.User;
import com.ecommerce.enums.UserTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private UserTypes role;
    private String profileUrl;


    // Convert User entity to UserDTO
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getProfileUrl()
        );
    }
}
