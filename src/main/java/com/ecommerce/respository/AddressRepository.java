package com.ecommerce.respository;

import com.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByUserId(int userId);

    boolean existsAddressesById(Integer id);
}
