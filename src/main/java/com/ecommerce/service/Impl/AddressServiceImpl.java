package com.ecommerce.service.Impl;

import com.ecommerce.Dto.AddressDTO;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.User;
import com.ecommerce.exception.AddressNotFoundException;
import com.ecommerce.exception.user.UserNotFoundException;
import com.ecommerce.respository.AddressRepository;
import com.ecommerce.respository.UserRepository;
import com.ecommerce.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    @Override
    public AddressDTO addAddress(AddressDTO addressDTO) {
        User user = userRepository.findById(addressDTO.getUserid())
                .orElseThrow(()-> new UserNotFoundException("User Not Found with id: " + addressDTO.getUserid()));
        Address address = convertToEntity(addressDTO);
        address.setUser(user);
        addressRepository.save(address);
        return convertToDTO(address);
    }

    @Override
    public AddressDTO getAddressById(Integer id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(()-> new AddressNotFoundException("Address Not Found with id: " + id));
        return convertToDTO(address);
    }

    @Override
    public List<AddressDTO> getAddressesByUserId(Integer userId) {
        if(!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User Not Found with id: " + userId);
        }
        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO) {
        // Fetch the existing address from the database
        Address existingAddress = addressRepository.findById(addressDTO.getId())
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressDTO.getId()));

        // Fetch the associated user
        User user = userRepository.findById(addressDTO.getUserid())
                .orElseThrow(() -> new UserNotFoundException("User Not Found with id: " + addressDTO.getUserid()));

        // Update the existing address fields
        existingAddress.setHouseNumber(addressDTO.getHouseNumber());
        existingAddress.setPincode(addressDTO.getPincode());
        existingAddress.setLandmark(addressDTO.getLandmark());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setUser(user);

        // Save the updated address
        addressRepository.save(existingAddress);

        // Convert to DTO and return
        return convertToDTO(existingAddress);
    }


    @Override
    @Transactional
    public void deleteAddress(Integer id) {
        if(!addressRepository.existsById(id)) {
            throw new AddressNotFoundException("Address Not Found with id: " + id);
        }
        addressRepository.deleteById(id);
    }

    private AddressDTO convertToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setHouseNumber(address.getHouseNumber());
        dto.setPincode(address.getPincode());
        dto.setLandmark(address.getLandmark());
        dto.setCity(address.getCity());
        dto.setUserid(address.getUser().getId()); // Extract user ID
        return dto;
    }

    private Address convertToEntity(AddressDTO dto) {
        Address address = new Address();
        address.setHouseNumber(dto.getHouseNumber());
        address.setPincode(dto.getPincode());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        return address;
    }
}
