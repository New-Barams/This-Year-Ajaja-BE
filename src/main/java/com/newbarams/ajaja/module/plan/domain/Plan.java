package com.newbarams.ajaja.module.plan.domain;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.ajaja.infra.AjajaEntity;
import com.newbarams.ajaja.module.plan.dto.PlanParam;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Plan {
	private static final int MODIFIABLE_MONTH = 1;
	private static final int ONE_MONTH_TERM = 1;

	private Long id;

	private Long userId;

	private int iconNumber;

	private Content content;
	private RemindInfo info;
	private PlanStatus status;

	private List<Message> messages;

	private List<AjajaEntity> ajajas;

	Plan(Long userId, Content content, RemindInfo info, PlanStatus status,
		int iconNumber, List<Message> messages) {
		this.userId = userId;
		this.content = content;
		this.info = info;
		this.status = status;
		this.iconNumber = iconNumber;
		this.messages = messages;
		this.ajajas = new ArrayList<>();
	}

	public static Plan create(PlanParam.Create param) {
		validateModifiableMonth(param.getMonth());

		return new Plan(param.getUserId(), param.getContent(), param.getInfo(), param.getStatus(),
			param.getIconNumber(), param.getMessages());
	}

	public void delete(Long userId, int month) {
		validateModifiableMonth(month);
		validateUser(userId);
		this.status.toDeleted();
	}

	private static void validateModifiableMonth(int month) {
		if (month != MODIFIABLE_MONTH) {
			throw new AjajaException(INVALID_UPDATABLE_DATE);
		}
	}

	private void validateUser(Long userId) {
		if (!this.userId.equals(userId)) {
			throw new AjajaException(INVALID_USER_ACCESS);
		}
	}

	public void updatePublicStatus(Long userId) {
		validateUser(userId);
		this.status.switchPublic();
	}

	public void updateRemindStatus(Long userId) {
		validateUser(userId);
		this.status.switchRemind();
	}

	public void updateAjajaStatus(Long userId) {
		validateUser(userId);
		this.status.switchAjaja();
	}

	public void update(PlanParam.Update param) {
		validateModifiableMonth(param.getMonth());
		validateUser(param.getUserId());
		this.iconNumber = param.getIconNumber();
		this.content = param.getContent();
		this.status = status.update(param.isPublic(), param.isCanAjaja());
	}

	public void updateRemind(RemindInfo info, List<Message> messages) {
		if (new TimeValue().getMonth() != MODIFIABLE_MONTH) {
			throw new AjajaException(INVALID_UPDATABLE_DATE);
		}

		this.info = info;
		this.messages = messages;
	}

	public int getRemindTime() {
		return this.info.getRemindTime();
	}

	public String getRemindTimeName() {
		return this.info.getRemindTimeName();
	}

	public int getRemindMonth() {
		return this.info.getRemindMonth();
	}

	public int getRemindDate() {
		return this.info.getRemindDate();
	}

	public int getRemindTerm() {
		return this.info.getRemindTerm();
	}

	public int getRemindTotalPeriod() {
		return this.info.getRemindTotalPeriod();
	}

	public boolean getIsRemindable() {
		return this.status.isCanRemind();
	}

	public int getTotalRemindNumber() {
		return this.info.getTotalRemindNumber();
	}

	public String getPlanTitle() {
		return this.content.getTitle();
	}

	public String getMessage(int currentMonth) {
		int messageIdx = getMessageIdx(this.info.getRemindTerm(), currentMonth);
		return this.messages.get(messageIdx).getContent();
	}

	private int getMessageIdx(int remindTerm, int currentMonth) {
		return remindTerm == ONE_MONTH_TERM ? (currentMonth - 2) : currentMonth / remindTerm - 1;
	}

	public void disable() {
		this.status = status.disable();
	}

	public RemindDate getFeedbackPeriod(TimeValue current) {
		Optional<Message> currentRemindInfo = this.messages.stream().filter(message -> {
			RemindDate feedbackPeriod = message.getRemindDate();
			return current.isBetween(
				feedbackPeriod.getRemindMonth(), feedbackPeriod.getRemindDay(), this.getRemindTime()
			);
		}).findAny();

		if (currentRemindInfo.isEmpty()) {
			throw new AjajaException(EXPIRED_FEEDBACK);
		}

		return currentRemindInfo.get().getRemindDate();
	}
}
