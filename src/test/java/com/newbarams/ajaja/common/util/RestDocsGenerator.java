package com.newbarams.ajaja.common.util;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static jakarta.servlet.http.HttpServletResponse.*;
import static java.util.Collections.*;
import static java.util.Spliterator.*;
import static java.util.Spliterators.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.web.servlet.HandlerMapping;

import com.epages.restdocs.apispec.HeaderDescriptorWithType;
import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class RestDocsGenerator {
	private static final List<HeaderDescriptorWithType> BEARER_TOKEN_HEADER =
		List.of(headerWithName(AUTHORIZATION).description("Bearer Token"));

	private static final String TOKEN_REQUIRED = "[토큰 필요] ";
	private static final String INITIAL_PATH = "";

	public static RestDocumentationResultHandler generate(
		String identifier,
		String tag,
		String summary,
		String description,
		boolean secured,
		MockHttpServletRequest request,
		MockHttpServletResponse response
	) {
		List<HeaderDescriptorWithType> requestHeaders = secured ? BEARER_TOKEN_HEADER : emptyList();

		List<FieldDescriptor> requestFields = generateRequestFields(request);
		List<FieldDescriptor> responseFields = generateResponseFields(response);

		List<ParameterDescriptorWithType> queryParameters = generateQueryParameters(request);
		List<ParameterDescriptorWithType> pathVariables = generatePathVariables(request);

		return document(
			identifier,
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			resource(ResourceSnippetParameters.builder()
				.tag(tag)
				.summary(addPrefixIfSecured(secured, summary))
				.description(addPrefixIfSecured(secured, description))
				.requestHeaders(requestHeaders)
				.requestFields(requestFields)
				.responseFields(responseFields)
				.queryParameters(queryParameters)
				.pathParameters(pathVariables)
				.build()
			)
		);
	}

	private static String addPrefixIfSecured(boolean secured, String target) {
		return target != null && secured ? TOKEN_REQUIRED.concat(target) : target;
	}

	private static List<FieldDescriptor> generateRequestFields(MockHttpServletRequest request) {
		JsonNode tree = JsonParser.readTree(request::getContentAsString);
		return tree != null ? generateDescriptors(tree, INITIAL_PATH) : emptyList();
	}

	private static List<FieldDescriptor> generateResponseFields(MockHttpServletResponse response) {
		return response.getStatus() == SC_NO_CONTENT ? emptyList() : generateNonNullResponseDescriptors(response);
	}

	private static List<FieldDescriptor> generateNonNullResponseDescriptors(MockHttpServletResponse response) {
		JsonNode tree = JsonParser.readTree(response::getContentAsString);
		Objects.requireNonNull(tree, "Response Body is NULL");
		return generateDescriptors(tree, INITIAL_PATH);
	}

	private static List<FieldDescriptor> generateDescriptors(JsonNode tree, String parentPath) {
		return StreamSupport.stream(spliteratorUnknownSize(tree.fields(), ORDERED), false)
			.flatMap(entry -> {
				String key = entry.getKey();
				JsonNode value = entry.getValue();
				JsonNodeType type = value.getNodeType();
				String path = parentPath.isBlank() ? key : parentPath.concat(String.format(".%s", key));
				return createDescriptorByType(value, path, type);
			})
			.toList();
	}

	private static Stream<FieldDescriptor> createDescriptorByType(JsonNode value, String path, JsonNodeType type) {
		return switch (type) {
			case OBJECT -> generateDescriptors(value, path).stream();
			case ARRAY -> createArrayDescriptors(value, path);
			case BOOLEAN, NUMBER, STRING -> Stream.of(fieldWithPath(path).description(value.asText()));
			default -> throw new UnsupportedOperationException();
		};
	}

	private static Stream<FieldDescriptor> createArrayDescriptors(JsonNode value, String path) {
		return StreamSupport.stream(spliteratorUnknownSize(value.elements(), ORDERED), false)
			.flatMap(item -> item.isObject()
				? generateDescriptors(item, path.concat(".[].")).stream()
				: Stream.of(fieldWithPath(path.concat(".[].")).description("element of array"))
			);
	}

	private static List<ParameterDescriptorWithType> generateQueryParameters(MockHttpServletRequest request) {
		Map<String, String[]> queryParameters = request.getParameterMap();
		return queryParameters.entrySet().stream()
			.map(entry -> parameterWithName(entry.getKey()).description(String.join("", entry.getValue())))
			.toList();
	}

	private static List<ParameterDescriptorWithType> generatePathVariables(MockHttpServletRequest request) {
		Object pathVariables = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		return ((Map<?, ?>)pathVariables).entrySet().stream()
			.map(entry -> parameterWithName(String.valueOf(entry.getKey())).description(entry.getValue()))
			.toList();
	}
}
