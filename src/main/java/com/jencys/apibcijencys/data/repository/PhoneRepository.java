package com.jencys.apibcijencys.data.repository;

import com.jencys.apibcijencys.data.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, String> {
}
