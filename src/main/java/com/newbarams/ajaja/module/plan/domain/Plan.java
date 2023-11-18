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
import jakarta.validation.constraints.NotEmpty;
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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_id")
	private Long id;

	@NotNull
	private Long userId;

	@Getter
	@NotNull
	private int achieveRate;

	@Embedded
	private Content content;
	private RemindInfo info;
	private PlanStatus status;

	@NotEmpty
	@ElementCollection
	@CollectionTable(name = "remind_messages", joinColumns = @JoinColumn(name = "plan_id"))
	@OrderColumn(name = "message_idx")
	private List<Message> messages = new ArrayList<>();

	@Size
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "target_id")
	private List<Ajaja> ajajas = new ArrayList<>();

	@Builder
	public Plan(String date, Long userId, Content content, RemindInfo info, boolean isPublic,
		List<Message> messages) {
		validateDate(date);
		this.userId = userId;
		this.content = content;
		this.info = info;
		this.status = new PlanStatus(isPublic);
		this.messages = messages;
		this.achieveRate = 0;
		this.validateSelf();
	}

	public void delete(Long userId, String date) {
		validateDate(date);
		validateUser(userId);
		this.status.toDeleted();
	}

	private void validateDate(String date) {
		String[] dateString = date.split(" ");
		String month = dateString[1];

		if (!month.equals("JAN")) {
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
		String date,
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
		validateDate(date);
		validateUser(userId);
		this.content.update(title, description);
		this.info.update(remindTotalPeriod, remindTerm, remindDate, remindTime);
		this.status.update(isPublic, canRemind, canAjaja);
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
			.get();
	}
}
