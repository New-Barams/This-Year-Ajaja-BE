package com.newbarams.ajaja.module.plan.domain;

import static com.newbarams.ajaja.module.plan.exception.ErrorMessage.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;
import com.newbarams.ajaja.module.ajaja.Ajaja;
import com.newbarams.ajaja.module.tag.domain.Tag;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

	@Embedded
	private Content content;
	private RemindInfo info;
	private PlanStatus status;

	@Size
	@OneToMany
	@JoinColumn(name = "plan_id")
	private Set<Tag> tags = new HashSet<>();

	@NotEmpty
	@ElementCollection
	@CollectionTable(name = "remind_messages", joinColumns = @JoinColumn(name = "plan_id"))
	@OrderColumn(name = "message_idx")
	private List<Message> messages = new ArrayList<>();

	@Size
	@ElementCollection
	@CollectionTable(name = "ajajas", joinColumns = @JoinColumn(name = "target_id"))
	private List<Ajaja> ajajas = new ArrayList<>();

	@Builder
	public Plan(Long userId, Content content, RemindInfo info, boolean isPublic,
		List<Message> messages, Set<Tag> tags) {
		this.userId = userId;
		this.content = content;
		this.info = info;
		this.status = new PlanStatus(isPublic);
		this.messages = messages;
		this.tags = tags;
		this.validateSelf();
	}

	public void delete(String date) {
		validateDate(date);
		this.status.changeToDeleted();
	}

	private void validateDate(String date) {
		String[] dateString = date.split(" ");
		String month = dateString[1];

		if (!month.equals("JAN")) {
			throw new IllegalStateException(INVALID_UPDATABLE_DATE.getMessage());
		}
	}

	public void updatePublicStatus() {
		this.status.changePublicOrNot();
	}

	public void updateRemindStatus() {
		this.status.changeCanRemindOrNot();
	}

	public void updateAjajaStatus() {
		this.status.changeCanAjajaOrNot();
	}

	public void update(
		String date,
		String title,
		String description,
		int remindTotalPeriod,
		int remindTerm,
		int remindDate,
		String remindTime,
		boolean isPublic,
		boolean canRemind,
		Set<Tag> tags,
		List<Message> messages
	) {
		validateDate(date);
		this.content.update(title, description);
		this.info.update(remindTotalPeriod, remindTerm, remindDate, remindTime);
		this.status.update(isPublic, canRemind);
		this.tags = tags;
		this.messages = messages;
		this.validateSelf();
	}
}
