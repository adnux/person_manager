package com.andre.example.rsql;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.Set;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;

public abstract class RSQLCustomOperators {

	public static final ComparisonOperator REMOVE_ACCENT = new ComparisonOperator("=acc=");

	public static Set<ComparisonOperator> operators() {
		return new HashSet<>(asList(REMOVE_ACCENT));
	}

}
