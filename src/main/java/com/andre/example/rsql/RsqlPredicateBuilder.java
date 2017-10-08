package com.andre.example.rsql;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tennaito.rsql.builder.BuilderTools;
import com.github.tennaito.rsql.jpa.PredicateBuilder;
import com.github.tennaito.rsql.jpa.PredicateBuilderStrategy;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

public class RsqlPredicateBuilder implements PredicateBuilderStrategy {

	private static final String FUNCTION_TIRA_ACENTO = "tiraacento";
	private final transient Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public <T> Predicate createPredicate(Node node, @SuppressWarnings("rawtypes") From root, Class<T> entity,
			EntityManager manager, BuilderTools tools) {

		if (root == null) {
			throwIllegalArgument("From root node was undefined.");
		}

		if (!ComparisonNode.class.isAssignableFrom(node.getClass())) {
			throwIllegalArgument("Node must be ComparisonNode.");
		}

		return internalCreatePredicate(node, root, manager, tools);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> Predicate internalCreatePredicate(Node node, From root, EntityManager manager, BuilderTools tools) {
		ComparisonNode comparison = (ComparisonNode) node;

		log.debug("Creating Predicate for comparison node: {0}", node);

		log.debug("Property graph path : {0}", comparison.getSelector());
		Expression propertyPath = PredicateBuilder.findPropertyPath(comparison.getSelector(), root, manager, tools);

		log.debug("Cast all arguments to type {0}.", propertyPath.getJavaType().getName());
		List<Object> castedArguments = tools.getArgumentParser().parse(comparison.getArguments(),
				propertyPath.getJavaType());

		return createBuilderPredicate(comparison.getOperator().getSymbol(), propertyPath,
				(Comparable) castedArguments.get(0), manager);

	}

	private void throwIllegalArgument(String msg) {
		log.error(msg);
		throw new IllegalArgumentException(msg);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <Y extends Comparable<? super Object>> Predicate createBuilderPredicate(String symbol,
			Expression<? extends Comparable> expression, Comparable value, EntityManager manager) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();

		if (symbol.equals(RSQLOperators.GREATER_THAN_OR_EQUAL.getSymbol())) {

			return builder.greaterThanOrEqualTo(expression, value);

		}
		if (symbol.equals(RSQLOperators.GREATER_THAN.getSymbol())) {

			return builder.greaterThan(expression, value);

		}
		if (symbol.equals(RSQLOperators.LESS_THAN_OR_EQUAL.getSymbol())) {

			return builder.lessThanOrEqualTo(expression, value);

		}
		if (symbol.equals(RSQLOperators.LESS_THAN.getSymbol())) {

			return builder.lessThan(expression, value);

		}
		if (symbol.equals(RSQLCustomOperators.REMOVE_ACCENT.getSymbol())) {

			return createlikeRemoveAccent(builder, (Expression<String>) expression, (String) value);

		}

		throw new NotImplementedException("Operator " + symbol);

	}

	public Predicate createlikeRemoveAccent(CriteriaBuilder builder, Expression<String> expression, String value) {

		String like = value.replace(PredicateBuilder.LIKE_WILDCARD, '%');
		return builder.like(builder.lower(builder.function(FUNCTION_TIRA_ACENTO, String.class, expression)),
				StringUtils.retiraAcentos(like.toLowerCase(Locale.getDefault())));
	}

}
