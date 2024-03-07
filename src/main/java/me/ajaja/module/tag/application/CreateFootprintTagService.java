package me.ajaja.module.tag.application;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.tag.adapter.out.persistence.FootprintTagRepository;
import me.ajaja.module.tag.adapter.out.persistence.TagRepository;
import me.ajaja.module.tag.adapter.out.persistence.model.FootprintTag;
import me.ajaja.module.tag.adapter.out.persistence.model.Tag;
import me.ajaja.module.tag.application.port.out.CreateTagPort;

@Service
@Qualifier("footprint")
@Transactional
@RequiredArgsConstructor
public class CreateFootprintTagService implements CreateTagPort {
	private final TagRepository tagRepository;
	private final FootprintTagRepository footprintTagRepository;

	public List<String> create(Long footprintId, List<String> tagNames) {
		if (tagNames == null) {
			return null;
		}
		Set<String> tagNameSet = new LinkedHashSet<>(tagNames);

		return tagNameSet.stream()
			.map(tagName -> saveFootprintTag(footprintId, tagName))
			.toList();
	}

	private String saveFootprintTag(Long footprintId, String tagName) {
		Tag tag = getOrCreateTagIfNotExists(tagName);
		FootprintTag footprintTag = new FootprintTag(footprintId, tag.getId());
		footprintTagRepository.save(footprintTag);

		return tag.getName();
	}

	private Tag getOrCreateTagIfNotExists(String name) {
		return tagRepository.findByName(name)
			.orElseGet(() -> tagRepository.save(new Tag(name)));
	}
}
