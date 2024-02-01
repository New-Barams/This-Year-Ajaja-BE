package me.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.remind.application.model.RemindAddress;
import me.ajaja.module.remind.application.port.out.FindRemindAddressPort;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.mapper.RemindMapper;
import me.ajaja.module.user.application.port.out.FindUserAddressPort;

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
