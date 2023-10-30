package com.newbarams.ajaja.module.plan.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;
import com.newbarams.ajaja.module.ajaja.Ajaja;
import com.newbarams.ajaja.module.tag.domain.Tag;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plans")
@Where(clause = "isDeleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity<Plan> {
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
	private List<Message> messages = new ArrayList();

	@Size
	@ElementCollection
	@CollectionTable(name = "ajajas", joinColumns = @JoinColumn(name = "target_id"))
	private List<Ajaja> ajajas = new ArrayList();

	@Builder
	public Plan(Long userId, Content content, RemindInfo info, boolean isPublic,
		List<Message> messages, Set<Tag> tags, List<Ajaja> ajajas) {
		this.userId = userId;
		this.content = content;
		this.info = info;
		this.status = new PlanStatus(isPublic);
		this.messages = messages;
		this.tags = tags;
		this.ajajas = ajajas;
		this.validateSelf();
	}
}
