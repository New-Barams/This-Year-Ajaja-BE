package me.ajaja.module.tag.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.tag.domain.FootprintTag;
import me.ajaja.module.tag.domain.FootprintTagRepository;
import me.ajaja.module.tag.domain.Tag;
import me.ajaja.module.tag.domain.TagRepository;

class CreateFootprintTagsServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateFootprintTagsService createTagsService;

	@Mock
	private TagRepository tagRepository;

	@Mock
	private FootprintTagRepository footprintTagRepository;

	@Test
	@DisplayName("기존에 저장되지 않은 태그는 발자취 태그에 반영 한다")
	void create_FootprintTag_Success() {
		Long footprintId = 1L;
		List<String> tags = List.of("tag1", "tag2", "tag3");
		Tag tag = sut.giveMeOne(Tag.class);
		FootprintTag footprintTag = sut.giveMeOne(FootprintTag.class);

		when(tagRepository.findByName("tag1")).thenReturn(Optional.empty());
		when(tagRepository.findByName("tag2")).thenReturn(Optional.empty());
		when(tagRepository.findByName("tag3")).thenReturn(Optional.of(tag));
		when(tagRepository.save(any())).thenReturn(tag);
		when(footprintTagRepository.save(any())).thenReturn(footprintTag);

		createTagsService.create(footprintId, tags);

		assertAll("Verify multiple statements",
			() -> verify(tagRepository, times(3)).findByName(any()),
			() -> verify(tagRepository, times(2)).save(any()),
			() -> verify(footprintTagRepository, times(3)).save(any())
		);
	}
}
