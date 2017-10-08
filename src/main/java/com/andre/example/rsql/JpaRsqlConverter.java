package com.andre.example.rsql;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.GREATER_THAN;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.GREATER_THAN_OR_EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.LESS_THAN;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.LESS_THAN_OR_EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.NOT_EQUAL;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

// WARNING: This is very naive, quick & dirty implementation!
public class JpaRsqlConverter implements RSQLVisitor<Predicate, Root> {

	private final CriteriaBuilder builder;
	private final ConversionService conversionService = new DefaultConversionService();

	public JpaRsqlConverter(CriteriaBuilder builder) {
		this.builder = builder;
	}

	public Predicate visit(AndNode node, Root root) {

		return builder.and(processNodes(node.getChildren(), root));
	}

	public Predicate visit(OrNode node, Root root) {

		return builder.or(processNodes(node.getChildren(), root));
	}

	public Predicate visit(ComparisonNode node, Root root) {

		ComparisonOperator op = node.getOperator();
		Path attrPath = root.get(node.getSelector());

		// RSQL guarantees that node has at least one argument
		Object argument = node.getArguments().get(0);

		if (op.equals(EQUAL)) {
			return builder.equal(attrPath, argument);
		}
		if (op.equals(NOT_EQUAL)) {
			return builder.notEqual(attrPath, argument);
		}

		Attribute attribute = root.getModel().getAttribute(node.getSelector());
		Class type = attribute.getJavaType();

		if (!Comparable.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException(String.format("Operator %s can be used only for Comparables", op));
		}
		Comparable comparable = (Comparable) conversionService.convert(argument, type);

		if (op.equals(GREATER_THAN)) {
			return builder.greaterThan(attrPath, comparable);
		}
		if (op.equals(GREATER_THAN_OR_EQUAL)) {
			return builder.greaterThanOrEqualTo(attrPath, comparable);
		}
		if (op.equals(LESS_THAN)) {
			return builder.lessThan(attrPath, comparable);
		}
		if (op.equals(LESS_THAN_OR_EQUAL)) {
			return builder.lessThanOrEqualTo(attrPath, comparable);
		}

		throw new IllegalArgumentException("Unknown operator: " + op);
	}

	private Predicate[] processNodes(List<Node> nodes, Root root) {

		Predicate[] predicates = new Predicate[nodes.size()];

		for (int i = 0; i < nodes.size(); i++) {
			predicates[i] = nodes.get(i).accept(this, root);
		}
		return predicates;
	}
}