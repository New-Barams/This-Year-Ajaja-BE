package me.ajaja.module.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import me.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import me.ajaja.module.user.domain.Email;
import me.ajaja.module.user.domain.User;
import me.ajaja.module.user.domain.UserId;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(source = "userEntity", target = "userId", qualifiedByName = "toUserId")
	@Mapping(target = "nickname", expression = "java(new Nickname(userEntity.getNickname()))")
	@Mapping(target = "phoneNumber", expression = "java(new PhoneNumber(userEntity.getPhoneNumber()))")
	@Mapping(source = "userEntity", target = "email", qualifiedByName = "toEmail")
	User toDomain(UserEntity userEntity);

	@Named("toUserId")
	static UserId toUserId(UserEntity userEntity) {
		return new UserId(userEntity.getId(), userEntity.getOauthInfo().getOauthId());
	}

	@Named("toEmail")
	static Email toEmail(UserEntity userEntity) {
		return new Email(userEntity.getSignUpEmail(), userEntity.getRemindEmail(), userEntity.isVerified());
	}

	@Mapping(source = "userId.id", target = "id")
	@Mapping(source = "nickname.nickname", target = "nickname")
	@Mapping(target = "phoneNumber", expression = "java(user.getPhoneNumber())") // getter overrided
	@Mapping(target = "signUpEmail", expression = "java(user.getEmail())") // getter overrided
	@Mapping(target = "remindEmail", expression = "java(user.getRemindEmail())") // getter overrided
	@Mapping(target = "verified", expression = "java(user.isVerified())") // getter overrided
	@Mapping(target = "oauthInfo", expression = "java(OauthInfo.kakao(user.getOauthId()))")
	UserEntity toEntity(User user);
}
