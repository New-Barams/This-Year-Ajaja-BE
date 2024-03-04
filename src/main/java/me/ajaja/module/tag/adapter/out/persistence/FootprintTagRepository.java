package me.ajaja.module.tag.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import me.ajaja.module.tag.adapter.out.persistence.model.FootprintTag;

public interface FootprintTagRepository extends JpaRepository<FootprintTag, Long> {
}
