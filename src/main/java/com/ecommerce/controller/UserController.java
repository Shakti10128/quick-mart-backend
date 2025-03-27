package com.ecommerce.controller;

import com.ecommerce.Dto.UserDTO;
import com.ecommerce.Utils.JwtUtils;
import com.ecommerce.entity.User;
import com.ecommerce.service.CloudinaryImageService;
import com.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final CloudinaryImageService cloudinaryImageService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String,Object>> signup(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(user);
        UserDTO SavedUser =  userService.registerUser(user);
        String token = JwtUtils.generateTokenAndSetCookie(request,response,user.getEmail(),user.getRole());
        Map<String, Object> apiResponse = Map.of(
                "success", true,
                "message", "User registered successfully",
                "token",token,
                "data", SavedUser
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody User user, HttpServletResponse response,HttpServletRequest request) {
        UserDTO loggeduser = userService.login(user.getEmail(), user.getPassword());
        String token = JwtUtils.generateTokenAndSetCookie(request,response, user.getEmail(),user.getRole());
        System.out.println(token);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","User logged in successfully",
                "token",token,
                "data", loggeduser
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/get-login-user")
    public ResponseEntity<Map<String,Object>> getLoginUser(HttpServletRequest request) {
        String token = JwtUtils.getTokenFromCookie(request);
        String Email = JwtUtils.getUsernameFromToken(token);
        UserDTO loggeduser = userService.getUserByEmail(Email);

        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","Details fetched successfully",
                "token",token,
                "data", loggeduser
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/logout")
    public ResponseEntity<Map<String,Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        boolean isLoggedOut = JwtUtils.removeTokenFromCookie(request,response);
        Map<String,Object> apiResponse = Map.of(
                "success", isLoggedOut,
                "message", isLoggedOut ?  "logged out successfully" : "internal server error"
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/update/profile")
    public ResponseEntity<Map<String,Object>> updateProfile(@RequestParam("profile") MultipartFile profile, @RequestParam Integer user_id) {
        UserDTO user = userService.updateProfilePicture(user_id, profile);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","profile updated successfully",
                "data", user
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userid}")
    public ResponseEntity<Map<String,Object>> getUserById(@PathVariable("userid") Integer user_id) {
        UserDTO user = userService.getUserById(user_id);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","user fetched successfully",
                "data", user
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/allusers")
    public ResponseEntity<Map<String,Object>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "messages", users.isEmpty() ? "No user found" : "all user fetched",
                "data", users
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userid}")
    public ResponseEntity<Map<String,Object>> deleteUser(@PathVariable("userid") Integer user_id) {
        userService.deleteUser(user_id);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","user deleted successfully"
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

}
