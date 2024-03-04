package me.ajaja.module.tag.adapter.out.persistence;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
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
		Set<String> tagNameSet = new LinkedHashSet<>(tagNames);
		List<Tag> savedTags = tagNameSet.stream().map(this::getOrCreateTagIfNotExists).toList();

		switch (type) {
			case FOOTPRINT -> saveFootprintTag(targetId, savedTags);
			case PLAN -> savePlanTag(targetId, savedTags);
		}
	}

	private Tag getOrCreateTagIfNotExists(String name) {
		return tagRepository.findByName(name)
			.orElseGet(() -> tagRepository.save(new Tag(name)));
	}

	private void saveFootprintTag(Long footprintId, List<Tag> savedTags) {
		savedTags.stream().map(tag -> new FootprintTag(footprintId, tag.getId())).forEach(
			footprintTag -> footprintTagRepository.save(footprintTag)
		);
	}

	private void savePlanTag(Long planId, List<Tag> savedTags) {
		savedTags.stream().map(tag -> new PlanTag(planId, tag.getId())).forEach(
			planTag -> planTagRepository.save(planTag)
		);
	}
}
