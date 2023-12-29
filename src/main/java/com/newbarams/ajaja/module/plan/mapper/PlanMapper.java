package com.newbarams.ajaja.module.plan.mapper;

import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.newbarams.ajaja.module.ajaja.infra.AjajaEntity;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanStatus;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanParam;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.infra.PlanEntity;
import com.newbarams.ajaja.module.remind.application.model.RemindMessageInfo;

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
	@Mapping(target = "ajajas", ignore = true)
	PlanEntity toEntity(Plan plan);

	@Mapping(source = "planEntity", target = "content", qualifiedByName = "toContent")
	@Mapping(source = "planEntity", target = "info", qualifiedByName = "toRemindInfo")
	@Mapping(source = "planEntity", target = "status", qualifiedByName = "toPlanStatus")
	Plan toDomain(PlanEntity planEntity);

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
		return new PlanStatus(planEntity.isPublic(), planEntity.isCanRemind(), planEntity.isCanAjaja(),
			planEntity.isDeleted());
	}

	@Mapping(source = "request.public", target = "isPublic")
	@Mapping(source = "request", target = "content", qualifiedByName = "toContent")
	@Mapping(source = "request", target = "info", qualifiedByName = "toRemindInfo")
	@Mapping(source = "request.messages", target = "messages", qualifiedByName = "toMessages")
	PlanParam.Create toParam(Long userId, PlanRequest.Create request, int month);

	@Named("toContent")
	static Content toContent(PlanRequest.Create request) {
		return new Content(request.getTitle(), request.getDescription());
	}

	@Named("toRemindInfo")
	static RemindInfo toRemindInfo(PlanRequest.Create request) {
		return new RemindInfo(request.getRemindTotalPeriod(), request.getRemindTerm(),
			request.getRemindDate(), request.getRemindTime());
	}

	@Mapping(source = "plan.content.title", target = "title")
	@Mapping(source = "plan.content.description", target = "description")
	@Mapping(source = "plan.status.public", target = "isPublic")
	@Mapping(source = "plan.status.canRemind", target = "canRemind")
	@Mapping(source = "plan.status.canAjaja", target = "canAjaja")
	PlanResponse.Create toResponse(Plan plan, List<String> tags);

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
	PlanResponse.MainInfo toResponse(int planYear, int totalAchieveRate,
		List<PlanResponse.PlanInfo> planInfos);

	default List<PlanResponse.PlanInfo> createPlanList(List<PlanResponse.PlanInfo> planInfos, int planYear) {
		return planInfos.stream()
			.filter(plan -> plan.getYear() == planYear)
			.toList();
	}

	@InheritConfiguration(name = "toDomain")
	RemindMessageInfo toModel(PlanEntity plan, String email);
}
