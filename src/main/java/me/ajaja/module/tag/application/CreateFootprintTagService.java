package me.ajaja.module.tag.application;

import static me.ajaja.global.exception.ErrorCode.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
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

	public void createTags(Long footprintId, List<String> tagNames) {
		if (tagNames == null) {
			throw new AjajaException(INVALID_REQUEST);
		}

		Set<String> nonDuplicatedTagNames = new HashSet<>(tagNames);
		nonDuplicatedTagNames.forEach(tagName -> createTag(footprintId, tagName));
	}

	private void createTag(Long footprintId, String tagName) {
		tagRepository.findByName(tagName)
			.ifPresentOrElse(tag -> handleExistingTag(tag, footprintId), () -> handleNewTag(tagName, footprintId));
	}

	private void handleExistingTag(Tag existingTag, Long footprintId) {
		FootprintTag footprintTag = new FootprintTag(footprintId, existingTag.getId());
		footprintTagRepository.save(footprintTag);
	}

	private void handleNewTag(String tagName, Long footprintId) {
		Tag tag = new Tag(tagName);
		Tag createdTag = tagRepository.save(tag);
		FootprintTag footprintTag = new FootprintTag(footprintId, createdTag.getId());
		footprintTagRepository.save(footprintTag);
	}
}
