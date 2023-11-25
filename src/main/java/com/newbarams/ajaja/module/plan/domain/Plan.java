package com.newbarams.ajaja.module.plan.domain;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;
import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.ajaja.domain.Ajaja;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "plans")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity<Plan> {
	private static final int MODIFIABLE_MONTH = 1;
	private static final int ONE_MONTH_TERM = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_id")
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Integer achieveRate;

	private int iconNumber;

	@Embedded
	private Content content;
	private RemindInfo info;
	private PlanStatus status;

	@ElementCollection(fetch = FetchType.EAGER) // todo:메세지 로딩 오류로 인한 임시 코드 (나중에 지우기)
	@CollectionTable(name = "remind_messages", joinColumns = @JoinColumn(name = "plan_id"))
	@OrderColumn(name = "message_idx")
	private List<Message> messages = new ArrayList<>();

	@Size
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "target_id")
	private List<Ajaja> ajajas = new ArrayList<>();

	@Builder
	public Plan(int month, Long userId, Content content, RemindInfo info, boolean isPublic,
		int iconNumber, List<Message> messages) {
		validateModifiableMonth(month);
		this.userId = userId;
		this.content = content;
		this.info = info;
		this.status = new PlanStatus(isPublic);
		this.iconNumber = iconNumber;
		this.messages = messages;
		this.achieveRate = 0;
		this.validateSelf();
	}

	public void delete(Long userId, int month) {
		validateModifiableMonth(month);
		validateUser(userId);
		this.status.toDeleted();
	}

	private void validateModifiableMonth(int month) {
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

	public void update(
		Long userId,
		int month,
		String title,
		String description,
		int remindTotalPeriod,
		int remindTerm,
		int remindDate,
		String remindTime,
		boolean isPublic,
		boolean canRemind,
		boolean canAjaja,
		List<Message> messages
	) {
		validateModifiableMonth(month);
		validateUser(userId);
		this.content = content.update(title, description);
		this.info = info.update(remindTotalPeriod, remindTerm, remindDate, remindTime);
		this.status = status.update(isPublic, canRemind, canAjaja);
		this.messages = messages;
		this.validateSelf();
	}

	public void updateAchieve(int achieveRate) {
		this.achieveRate = achieveRate;
	}

	public void addAjaja(Ajaja ajaja) {
		this.ajajas.add(ajaja);
	}

	public Ajaja getAjajaByUserId(Long userId) {
		return ajajas.stream()
			.filter((ajaja -> ajaja.getUserId().equals(userId)))
			.findFirst()
			.orElseGet(Ajaja::defaultValue);
	}

	public String getTimeName() {
		return this.info.getTimeName();
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

	public boolean getRemindStatus() {
		return this.status.isCanRemind();
	}

	public String getMessage(int remindTerm, int currentMonth) {
		int messageIdx = getMessageIdx(remindTerm, currentMonth);
		return this.messages.get(messageIdx).getContent();
	}

	private int getMessageIdx(int remindTerm, int currentMonth) {
		return remindTerm == ONE_MONTH_TERM ? (currentMonth - 2) : currentMonth / remindTerm;
	}

	public void disable() {
		this.status = status.disable();
	}
}
