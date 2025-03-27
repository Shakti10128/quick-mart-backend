package com.ecommerce.service;

import com.ecommerce.Dto.UserDTO;
import com.ecommerce.entity.User;
import com.ecommerce.exception.user.UserAlreadyExistException;
import com.ecommerce.exception.user.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDTO registerUser(User user) throws UserAlreadyExistException;

    UserDTO login(String email, String password) throws UserNotFoundException;

    UserDTO getUserByEmail(String Email) throws UserNotFoundException;

    UserDTO getUserById(Integer user_id) throws UserNotFoundException;

    List<UserDTO> getAllUsers();

    UserDTO updateProfilePicture(Integer user_id, MultipartFile profile) throws UserNotFoundException;

    void deleteUser(Integer user_id) throws UserNotFoundException;

}
