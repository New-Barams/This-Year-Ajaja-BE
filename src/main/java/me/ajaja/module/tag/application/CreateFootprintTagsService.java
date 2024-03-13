package me.ajaja.module.tag.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.tag.domain.FootprintTag;
import me.ajaja.module.tag.domain.FootprintTagRepository;
import me.ajaja.module.tag.domain.Tag;
import me.ajaja.module.tag.domain.TagRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateFootprintTagsService {
	private final TagRepository tagRepository;
	private final FootprintTagRepository footprintTagRepository;

	public void create(Long targetId, List<String> tagNames) {
		List<Tag> tags = tagNames.stream()
			.distinct()
			.map(this::getOrCreateIfNotExists)
			.toList();

		saveFootprintTag(targetId, tags);
	}

	private Tag getOrCreateIfNotExists(String name) {
		return tagRepository.findByName(name).orElseGet(() -> tagRepository.save(new Tag(name)));
	}

	private void saveFootprintTag(Long footprintId, List<Tag> tags) {
		for (Tag tag : tags) {
			footprintTagRepository.save(new FootprintTag(footprintId, tag.getId()));
		}
	}
}
