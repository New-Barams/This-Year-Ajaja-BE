package me.ajaja.module.footprint.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;

public interface FootprintJpaRepository extends JpaRepository<FootprintEntity, Long> {
}
