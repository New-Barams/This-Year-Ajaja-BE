package com.newbarams.ajaja.module.remind.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.remind.domain.Remind;

public interface RemindRepository extends JpaRepository<Remind, Long> {

}
