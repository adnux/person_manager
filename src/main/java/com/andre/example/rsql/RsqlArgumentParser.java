package com.andre.example.rsql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.github.tennaito.rsql.misc.DefaultArgumentParser;

public class RsqlArgumentParser extends DefaultArgumentParser {

	protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

	protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> T parse(String argument, Class<T> type) {

		if (argument == null || "null".equals(argument.trim().toLowerCase(Locale.getDefault()))) {
			return (T) null;

		}

		if (type.equals(BigDecimal.class)) {

			return (T) new BigDecimal(argument);

		} else if (type.equals(LocalDate.class)) {

			return (T) LocalDate.parse(argument, DATE_FORMATTER);

		} else if (type.equals(LocalDateTime.class)) {

			return (T) LocalDateTime.parse(argument, DATE_TIME_FORMATTER);

		}
		// else if (EnumConverter.class.isAssignableFrom(type)) {
		//
		// Class<?> typeOfEnum = EnumUtils.getTypeOf((Class<EnumConverter>) type);
		//
		// Object value = super.parse(argument, typeOfEnum);
		//
		// return (T) EnumUtils.getEnumFromValue((Class) type, value);
		// }

		return super.parse(argument, type);
	}

}
