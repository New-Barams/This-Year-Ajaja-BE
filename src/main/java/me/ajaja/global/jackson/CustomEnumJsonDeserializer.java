package me.ajaja.global.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

class CustomEnumJsonDeserializer extends StdDeserializer<Enum<?>> implements ContextualDeserializer {

	protected CustomEnumJsonDeserializer(Class<?> vc) {
		super(vc);
	}

	public CustomEnumJsonDeserializer() {
		this(null);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Enum<?> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
		String target = parser.getValueAsString();
		Class<? extends Enum> enumClass = (Class<? extends Enum>)this._valueClass;
		return Enum.valueOf(enumClass, target.toUpperCase());
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		return new CustomEnumJsonDeserializer(property.getType().getRawClass());
	}
}
