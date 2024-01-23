package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.remind.application.model.RemindAddress;
import com.newbarams.ajaja.module.remind.application.port.out.FindRemindAddressPort;
import com.newbarams.ajaja.module.user.application.port.out.FindUserAddressPort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FindRemindAddressAdapter implements FindRemindAddressPort {
	private final FindUserAddressPort findUserAddressPort;

	@Override
	public RemindAddress findAddressByUserId(Long userId) {
		return findUserAddressPort.findUserAddressByUserId(userId);
	}
}
