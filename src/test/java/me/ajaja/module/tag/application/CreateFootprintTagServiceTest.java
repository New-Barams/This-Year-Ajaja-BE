package me.ajaja.module.tag.application;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.tag.adapter.out.persistence.FootprintTagRepository;
import me.ajaja.module.tag.adapter.out.persistence.TagRepository;
import me.ajaja.module.tag.adapter.out.persistence.model.FootprintTag;
import me.ajaja.module.tag.adapter.out.persistence.model.Tag;

class CreateFootprintTagServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateFootprintTagService createFootprintTagService;

	@Mock
	private TagRepository tagRepository;

	@Mock
	FootprintTagRepository footprintTagRepository;

	@Test
	@DisplayName("생성 되었던 태그가 없을 때 태그 데이터를 생성하고 발자취-태그 중간 테이블 데이터를 생성한다.")
	void createTag_And_FootprintTag_When_Tag_NotExist() {
		Tag tag = sut.giveMeOne(Tag.class);
		FootprintTag footprintTag = sut.giveMeOne(FootprintTag.class);

		when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
		when(tagRepository.save(any())).thenReturn(tag);
		when(footprintTagRepository.save(any())).thenReturn(footprintTag);

		Long footprintId = 1L;
		List<String> tagNames = List.of("tag1");
		createFootprintTagService.createTags(footprintId, tagNames);

		verify(tagRepository, times(1)).findByName(anyString());
		verify(tagRepository, times(1)).save(any());
		verify(footprintTagRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("생성 되었던 태그가 있을 때 발자취-태그 중간 테이블 데이터만 생성한다.")
	void create_Only_FootprintTag_When_Tag_Exist() {
		Tag tag = sut.giveMeOne(Tag.class);
		FootprintTag footprintTag = sut.giveMeOne(FootprintTag.class);

		when(tagRepository.findByName(anyString())).thenReturn(Optional.of(new Tag("tag")));
		when(footprintTagRepository.save(any())).thenReturn(footprintTag);

		Long footprintId = 1L;
		List<String> tagNames = List.of("tag");
		createFootprintTagService.createTags(footprintId, tagNames);

		verify(tagRepository, times(1)).findByName(anyString());
		verify(footprintTagRepository, times(1)).save(any());
	}
}
