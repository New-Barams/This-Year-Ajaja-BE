package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.remind.application.model.RemindAddress;
import com.newbarams.ajaja.module.remind.application.port.out.FindRemindAddressPort;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.mapper.RemindMapper;
import com.newbarams.ajaja.module.user.application.port.out.FindUserAddressPort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FindRemindAddressAdapter implements FindRemindAddressPort {
	private final FindUserAddressPort findUserAddressPort;
	private final RemindMapper remindMapper;

	@Override
	public Remind findAddressByUserId(Long userId) {
		RemindAddress address = findUserAddressPort.findUserAddressByUserId(userId);
		return remindMapper.toMockDomain(address);
	}
}
