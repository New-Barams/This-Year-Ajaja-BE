package com.newbarams.ajaja.module.remind.infra;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.RemindRepository;
import com.newbarams.ajaja.module.remind.mapper.RemindMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class RemindRepositoryImpl implements RemindRepository {
	private final RemindJpaRepository remindJpaRepository;
	private final RemindMapper mapper;

	public Remind save(Remind remind) {
		RemindEntity entity = remindJpaRepository.save(mapper.toEntity(remind));

		return mapper.toDomain(entity);
	}
}
