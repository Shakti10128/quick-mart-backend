package com.ecommerce.service.Impl;

import com.ecommerce.Dto.UserDTO;
import com.ecommerce.entity.User;
import com.ecommerce.enums.UserTypes;
import com.ecommerce.exception.ImageUploadException;
import com.ecommerce.exception.user.UserAlreadyExistException;
import com.ecommerce.exception.user.UserNotFoundException;
import com.ecommerce.respository.UserRepository;
import com.ecommerce.service.CloudinaryImageService;
import com.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryImageService cloudinaryImageService;


    @Override
    public UserDTO registerUser(User user) throws UserAlreadyExistException {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserDTO.fromEntity(userRepository.save(user));
    }

    @Override
    public UserDTO login(String email, String password) throws UserNotFoundException {
        Optional<User> optional = userRepository.findByEmail(email);
        if(optional.isPresent() && passwordEncoder.matches(password, optional.get().getPassword())) {
            return UserDTO.fromEntity(optional.get());
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public UserDTO getUserByEmail(String Email) throws UserNotFoundException {
        if(userRepository.existsByEmail(Email)) {
            return UserDTO.fromEntity(userRepository.findByEmail(Email).get());
        }
        else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public UserDTO getUserById(Integer user_id) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(user_id);
        if(optional.isPresent()) {
            optional.get().setPassword(null);
            return UserDTO.fromEntity(optional.get());
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == UserTypes.USER) // Keep only users
                .map(UserDTO::fromEntity) // Convert User -> UserDTO
                .collect(Collectors.toList()); // Convert back to List
    }



    @Override
    public UserDTO updateProfilePicture(Integer user_id, MultipartFile profile) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(user_id);
        if(optional.isPresent()) {
            Map result = cloudinaryImageService.upload(profile);
            if (result != null && result.containsKey("url")) {
                optional.get().setProfileUrl(result.get("url").toString());
                userRepository.save(optional.get());
                return UserDTO.fromEntity(optional.get());
            }
            else{
                throw new ImageUploadException("Image upload failed");
            }
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public void deleteUser(Integer user_id) throws UserNotFoundException {
        boolean isUserExists = userRepository.existsById(user_id);
        if(isUserExists) {
            userRepository.deleteById(user_id);
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }
}
