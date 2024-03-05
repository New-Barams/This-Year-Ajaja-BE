package me.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.remind.adapter.out.persistence.model.RemindEntity;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.mapper.RemindMapper;

@Repository
@Transactional
@RequiredArgsConstructor
public class SaveAjajaRemindAdapter implements SaveAjajaRemindPort {
	private final RemindJpaRepository remindJpaRepository;
	private final RemindMapper mapper;

	@Override
	public Remind save(Long userId, String endPoint, Long planId, String message, BaseTime now) {
		Remind remind = Remind.ajaja(userId, endPoint, planId, message, now); // todo: 도메인 의존성
		RemindEntity entity = remindJpaRepository.save(mapper.toEntity(remind));
		return mapper.toDomain(entity);
	}
}
