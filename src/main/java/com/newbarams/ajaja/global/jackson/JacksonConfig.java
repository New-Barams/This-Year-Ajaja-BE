package com.newbarams.ajaja.global.jackson;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.type.ClassKey;

@Configuration
public class JacksonConfig {

	@Bean
	public Module customEnumModule() {
		return new Module() {
			@Override
			public String getModuleName() {
				return "customEnum";
			}

			@Override
			public Version version() {
				return Version.unknownVersion();
			}

			@Override
			public void setupModule(SetupContext context) {
				context.addDeserializers(customDeserializer());
			}
		};
	}

	private Deserializers customDeserializer() {
		return new Deserializers.Base() {
			final Map<ClassKey, JsonDeserializer<?>> cache = new ConcurrentHashMap<>();

			@Override
			public JsonDeserializer<?> findEnumDeserializer(
				Class<?> type,
				DeserializationConfig config,
				BeanDescription beanDesc
			) {
				JsonDeserializer<?> customEnumJsonDeserializer = new CustomEnumJsonDeserializer();
				addDeserializer(type, customEnumJsonDeserializer);
				return customEnumJsonDeserializer;
			}

			@Override
			public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
				return cache.containsKey(new ClassKey(valueType));
			}

			private void addDeserializer(Class<?> type, JsonDeserializer<?> deserializer) {
				ClassKey key = new ClassKey(type);
				cache.put(key, deserializer);
			}
		};
	}
}
