package com.ecommerce.service;

import com.ecommerce.Dto.AddressDTO;
import com.ecommerce.entity.Address;
import java.util.List;
import java.util.Optional;

public interface AddressService {


    /**
     * Adds a new address for a user.
     *
     * @param addressDTO The address object to be added.
     * @return The saved Address object.
     */
    AddressDTO addAddress(AddressDTO addressDTO);

    /**
     * Retrieves an address by its ID.
     *
     * @param id The ID of the address.
     * @return An Optional containing the Address if found, otherwise empty.
     */
    AddressDTO getAddressById(Integer id);

    /**
     * Retrieves all addresses associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of addresses belonging to the user.
     */
    List<AddressDTO> getAddressesByUserId(Integer userId);

    /**
     * Updates an existing address.
     *
     * @param addressDTO The new address details.
     * @return The updated Address object.
     */
    AddressDTO updateAddress(AddressDTO addressDTO);

    /**
     * Deletes an address by its ID.
     *
     * @param id The ID of the address to be deleted.
     */
    void deleteAddress(Integer id);
}
