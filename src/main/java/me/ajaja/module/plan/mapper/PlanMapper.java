package me.ajaja.module.plan.mapper;

import java.time.Instant;
import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import me.ajaja.global.common.BaseTime;
import me.ajaja.module.ajaja.infra.AjajaEntity;
import me.ajaja.module.feedback.application.model.FeedbackPeriod;
import me.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import me.ajaja.module.feedback.application.model.UpdatableFeedback;
import me.ajaja.module.plan.adapter.out.persistence.model.PlanEntity;
import me.ajaja.module.plan.domain.Content;
import me.ajaja.module.plan.domain.Message;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanStatus;
import me.ajaja.module.plan.domain.RemindInfo;
import me.ajaja.module.plan.dto.PlanParam;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.remind.application.model.RemindMessageInfo;

@Mapper(componentModel = "spring")
public interface PlanMapper {
	@Mapping(source = "content.title", target = "title")
	@Mapping(source = "content.description", target = "description")
	@Mapping(source = "info.remindTotalPeriod", target = "remindTotalPeriod")
	@Mapping(source = "info.remindTerm", target = "remindTerm")
	@Mapping(source = "info.remindDate", target = "remindDate")
	@Mapping(target = "remindTime", expression = "java(plan.getRemindTimeName())")
	@Mapping(source = "status.public", target = "isPublic")
	@Mapping(source = "status.canRemind", target = "canRemind")
	@Mapping(source = "status.canAjaja", target = "canAjaja")
	@Mapping(source = "status.deleted", target = "deleted")
	PlanEntity toEntity(Plan plan);

	@Mapping(source = "planEntity", target = "content", qualifiedByName = "toContent")
	@Mapping(source = "planEntity", target = "info", qualifiedByName = "toRemindInfo")
	@Mapping(source = "planEntity", target = "status", qualifiedByName = "toPlanStatus")
	@Mapping(target = "createdAt", expression = "java(parseTimeValue(planEntity.getCreatedAt()))")
	Plan toDomain(PlanEntity planEntity);

	default BaseTime parseTimeValue(Instant createdAt) {
		return createdAt == null ? null : new BaseTime(createdAt); // 계획 저장 후 조회 시 작성일 null로 인한 코드
	}

	@Named("toContent")
	static Content toContent(PlanEntity planEntity) {
		return new Content(planEntity.getTitle(), planEntity.getDescription());
	}

	@Named("toRemindInfo")
	static RemindInfo toRemindInfo(PlanEntity planEntity) {
		return new RemindInfo(planEntity.getRemindTotalPeriod(), planEntity.getRemindTerm(),
			planEntity.getRemindDate(), planEntity.getRemindTime());
	}

	@Named("toPlanStatus")
	static PlanStatus toPlanStatus(PlanEntity planEntity) {
		return new PlanStatus(planEntity.isPublic(),
			planEntity.isCanRemind(),
			planEntity.isCanAjaja(),
			planEntity.isDeleted()
		);
	}

	@Mapping(source = "request", target = "content", qualifiedByName = "toContent")
	@Mapping(source = "request", target = "info", qualifiedByName = "toRemindInfo")
	@Mapping(source = "request.messages", target = "messages", qualifiedByName = "toMessages")
	@Mapping(source = "request", target = "status", qualifiedByName = "toPlanStatus")
	PlanParam.Create toParam(Long userId, PlanRequest.Create request, int month);

	@Named("toContent")
	static Content toContent(PlanRequest.Create request) {
		return new Content(request.getTitle(), request.getDescription());
	}

	@Named("toRemindInfo")
	static RemindInfo toRemindInfo(PlanRequest.Create request) {
		return new RemindInfo(
			request.getRemindTotalPeriod(),
			request.getRemindTerm(),
			request.getRemindDate(),
			request.getRemindTime()
		);
	}

	@Named("toPlanStatus")
	static PlanStatus toPlanStatus(PlanRequest.Create request) {
		return new PlanStatus(request.isPublic(), request.isCanAjaja());
	}

	@Mapping(source = "plan.ajajas", target = "ajajas", qualifiedByName = "toAjajaCount")
	PlanResponse.GetAll toResponse(PlanEntity plan, String nickname, List<String> tags);

	@Named("toMessages")
	@Mapping(source = "dto.remindMonth", target = "remindMonth")
	@Mapping(source = "dto.remindDay", target = "remindDay")
	List<Message> toMessages(List<PlanRequest.Message> dto);

	@Named("toAjajaCount")
	static int toAjajaCount(List<AjajaEntity> ajajas) {
		return ajajas.size();
	}

	@Mapping(source = "request.public", target = "isPublic")
	@Mapping(source = "request", target = "content", qualifiedByName = "toContent")
	PlanParam.Update toParam(Long userId, PlanRequest.Update request, int month);

	@Named("toContent")
	static Content toContent(PlanRequest.Update request) {
		return new Content(request.getTitle(), request.getDescription());
	}

	@Mapping(source = "planYear", target = "year")
	@Mapping(target = "getPlanList", expression = "java(createPlanList(planInfos,planYear))")
	PlanResponse.MainInfo toResponse(int planYear, int totalAchieveRate, List<PlanResponse.PlanInfo> planInfos);

	default List<PlanResponse.PlanInfo> createPlanList(List<PlanResponse.PlanInfo> planInfos, int planYear) {
		return planInfos.stream()
			.filter(plan -> plan.getYear() == planYear)
			.toList();
	}

	@InheritConfiguration(name = "toDomain")
	RemindMessageInfo toModel(PlanEntity plan, String remindType, String email, String phoneNumber);

	@Mapping(target = "createdYear", expression = "java(plan.getCreatedAt().getYear())")
	@Mapping(target = "remindMonth", expression = "java(plan.getRemindMonth())")
	@Mapping(target = "periods", expression = "java(getFeedbackPeriods(plan.getMessages()))")
	@Mapping(source = "info.remindDate", target = "remindDate")
	@Mapping(source = "info.remindTotalPeriod", target = "totalPeriod")
	@Mapping(source = "info.remindTerm", target = "remindTerm")
	@Mapping(source = "info.remindTime", target = "remindTime")
	@Mapping(source = "content.title", target = "title")
	PlanFeedbackInfo toModel(Plan plan);

	default List<FeedbackPeriod> getFeedbackPeriods(List<Message> messages) {
		return messages.stream().map(Message::getRemindDate)
			.map(remindDate -> new FeedbackPeriod(remindDate.getRemindMonth(), remindDate.getRemindDay()))
			.toList();
	}

	@Mapping(source = "id", target = "planId")
	@Mapping(source = "content.title", target = "title")
	@Mapping(target = "period", expression = "java(getRemindPeriod(plan))")
	UpdatableFeedback toFeedbackModel(Plan plan);

	default BaseTime getRemindPeriod(Plan plan) {
		return plan.findFeedbackPeriod(BaseTime.now());
	}
}
