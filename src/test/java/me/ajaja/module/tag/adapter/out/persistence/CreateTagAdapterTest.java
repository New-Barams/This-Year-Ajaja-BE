package me.ajaja.module.tag.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.tag.application.port.in.CreateTagUseCase;
import me.ajaja.module.tag.domain.FootprintTag;
import me.ajaja.module.tag.domain.Tag;

class CreateTagAdapterTest extends MockTestSupport {
	@InjectMocks
	private CreateTagAdapter createTagAdapter;
	@Mock
	private TagRepository tagRepository;
	@Mock
	private FootprintTagRepository footprintTagRepository;

	@Test
	@DisplayName("발자취 태그 저장시 기존 태그가 있으면 불러 오고 없으면 태그를 저장 하고 불러와 발자취 태그를 저장 한다")
	void createFootprintTag_Success() {
		Tag tag = sut.giveMeOne(Tag.class);
		FootprintTag footprintTag = sut.giveMeOne(FootprintTag.class);

		when(tagRepository.findByName("tag1")).thenReturn(Optional.of(tag));
		when(tagRepository.findByName("tag2")).thenReturn(Optional.empty());
		when(tagRepository.findByName("tag3")).thenReturn(Optional.empty());
		when(tagRepository.save(any())).thenReturn(tag);
		when(footprintTagRepository.save(any())).thenReturn(footprintTag);

		createTagAdapter.create(CreateTagUseCase.Type.FOOTPRINT, 1L, List.of("tag1", "tag2", "tag3"));

		assertAll(
			() -> verify(tagRepository, times(1)).findByName("tag1"),
			() -> verify(tagRepository, times(1)).findByName("tag2"),
			() -> verify(tagRepository, times(1)).findByName("tag3"),
			() -> verify(tagRepository, times(2)).save(any()),
			() -> verify(footprintTagRepository, times(3)).save(any())
		);
	}
}
