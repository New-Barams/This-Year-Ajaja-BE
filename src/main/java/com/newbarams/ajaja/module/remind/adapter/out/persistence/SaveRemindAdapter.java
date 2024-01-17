package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.remind.adapter.out.persistence.model.RemindEntity;
import com.newbarams.ajaja.module.remind.application.port.out.SaveRemindPort;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.mapper.RemindMapper;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class SaveRemindAdapter implements SaveRemindPort {
	private final RemindJpaRepository remindJpaRepository;
	private final RemindMapper mapper;

	@Override
	public Remind save(Remind remind) {
		RemindEntity entity = remindJpaRepository.save(mapper.toEntity(remind));
		return mapper.toDomain(entity);
	}
}
