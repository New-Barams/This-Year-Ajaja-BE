package com.newbarams.ajaja.common.util;

import static java.util.Objects.*;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.ResultActions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Builders class to prevent human error such as typos
 * When class safely created can call `generateDocs()` to create `RestDocumentationResultHandler`
 * @see RestDocsGenerator
 * @author hejow
 */
public class RestDocument {
	private final String identifier;
	private final String tag;
	private final String summary;
	private final String description;
	private final boolean secured;
	private final MockHttpServletRequest request;
	private final MockHttpServletResponse response;

	private RestDocument(
		String identifier,
		String tag,
		String summary,
		String description,
		boolean secured,
		ResultActions result
	) {
		requireNonBlank(identifier);
		requireNonNull(tag, "Tag cannot be null");
		requireNonNull(result, "ResultActions cannot be null");
		this.identifier = identifier;
		this.tag = tag;
		this.summary = summary;
		this.description = description;
		this.secured = secured;
		this.request = result.andReturn().getRequest();
		this.response = result.andReturn().getResponse();
	}

	public static RestDocumentBuilder builder() {
		return new RestDocumentBuilder();
	}

	public RestDocumentationResultHandler generateDocs() {
		return RestDocsGenerator.generate(identifier, tag, summary, description, secured, request, response);
	}

	private void requireNonBlank(String identifier) {
		if (identifier == null || identifier.isBlank()) {
			throw new IllegalArgumentException("ID cannot be empty");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class RestDocumentBuilder {
		private String identifier;
		private ApiTag tag;
		private String summary;
		private String description;
		private boolean secured = false;
		private ResultActions result;

		public RestDocumentBuilder identifier(String identifier) {
			this.identifier = identifier;
			return this;
		}

		public RestDocumentBuilder tag(ApiTag tag) {
			this.tag = tag;
			return this;
		}

		public RestDocumentBuilder summary(String summary) {
			this.summary = summary;
			return this;
		}

		public RestDocumentBuilder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * If secured is TRUE, will generate authentication required rest-docs.
		 * Default is false
		 */
		public RestDocumentBuilder secured(boolean secured) {
			this.secured = secured;
			return this;
		}

		public RestDocumentBuilder result(ResultActions result) {
			this.result = result;
			return this;
		}

		public RestDocument build() {
			return new RestDocument(identifier, tag.content, summary, description, secured, result);
		}

		public RestDocumentationResultHandler generateDocs() {
			return build().generateDocs();
		}
	}
}
