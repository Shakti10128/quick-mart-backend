package com.ecommerce.controller;

import com.ecommerce.Dto.AddressDTO;
import com.ecommerce.entity.Address;
import com.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AdressController {
    private final AddressService addressService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create-address")
    public ResponseEntity<Object> createAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO saveAddress = addressService.addAddress(addressDTO);
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message","Address Created Successfully",
                "data", saveAddress
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{addressid}")
    public ResponseEntity<Object> getAddressById(@PathVariable("addressid") Integer addressid) {
        AddressDTO addressDTO = addressService.getAddressById(addressid);
        assert addressDTO != null;
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message", "Address Fetched Successfully",
                "data", addressDTO
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{userid}")
    public ResponseEntity<Object> getAddressByUserID(@PathVariable("userid") Integer userid) {
        List<AddressDTO> addressDTOList = addressService.getAddressesByUserId(userid);
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message", addressDTOList.isEmpty() ? "No Address Found":"Address Fetched Successfully",
                "data", addressDTOList
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update-address")
    public ResponseEntity<Object> updateAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO saveAddress = addressService.updateAddress(addressDTO);
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message","Address Updated Successfully",
                "data", saveAddress
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/{addressid}")
    public ResponseEntity<Object> deleteAddress(@PathVariable("addressid") Integer addressid) {
        addressService.deleteAddress(addressid);
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message","Address Deleted Successfully"
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
