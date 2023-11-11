package com.newbarams.ajaja.module.tag.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.tag.domain.Tag;
import com.newbarams.ajaja.module.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
	private final TagRepository tagRepository;

	public Set<Tag> getTags(List<String> tagNames) {
		if (tagNames == null) {
			return null;
		}

		Set<Tag> tags = new HashSet<>(tagNames.size());

		for (String name : tagNames) {
			Tag tag = getOrCreateIfNotExists(name);
			tags.add(tag);
		}

		return tags;
	}

	private Tag getOrCreateIfNotExists(String name) {
		return tagRepository.findByName(name)
			.orElseGet(() -> tagRepository.save(new Tag(name)));
	}
}
