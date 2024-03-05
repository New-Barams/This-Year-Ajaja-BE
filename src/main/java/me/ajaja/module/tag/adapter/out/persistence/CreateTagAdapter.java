package me.ajaja.module.tag.adapter.out.persistence;

import static me.ajaja.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.tag.application.port.in.CreateTagUseCase;
import me.ajaja.module.tag.application.port.out.CreateTagPort;
import me.ajaja.module.tag.domain.FootprintTag;
import me.ajaja.module.tag.domain.PlanTag;
import me.ajaja.module.tag.domain.Tag;

@Repository
@Transactional
@RequiredArgsConstructor
public class CreateTagAdapter implements CreateTagPort {
	private final TagRepository tagRepository;
	private final FootprintTagRepository footprintTagRepository;
	private final PlanTagRepository planTagRepository;

	@Override
	public void create(CreateTagUseCase.Type type, Long targetId, List<String> tagNames) {
		List<Tag> tags = tagNames.stream()
			.distinct()
			.map(this::getOrCreateIfNotExists)
			.toList();

		switch (type) {
			case FOOTPRINT -> saveFootprintTag(targetId, tags);
			case PLAN -> savePlanTag(targetId, tags);
			default -> throw new AjajaException(INVALID_TAG_TYPE);
		}
	}

	private Tag getOrCreateIfNotExists(String name) {
		return tagRepository.findByName(name)
			.orElseGet(() -> tagRepository.save(new Tag(name)));
	}

	private void saveFootprintTag(Long footprintId, List<Tag> tags) {
		List<FootprintTag> footprintTags = tags.stream()
			.map(tag -> new FootprintTag(footprintId, tag.getId()))
			.toList();

		footprintTagRepository.saveAll(footprintTags);
	}

	private void savePlanTag(Long planId, List<Tag> tags) {
		List<PlanTag> planTags = tags.stream()
			.map(tag -> new PlanTag(planId, tag.getId()))
			.toList();

		planTagRepository.saveAll(planTags);
	}
}
