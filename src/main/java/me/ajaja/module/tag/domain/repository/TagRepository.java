package me.ajaja.module.tag.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.ajaja.module.tag.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	Optional<Tag> findByName(String name);
}
