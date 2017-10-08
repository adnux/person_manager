package com.andre.example.rsql;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

public final class RsqlUtils {

	private RsqlUtils() {

	}

	public static <T> Specification<T> createSpecFrom(EntityManager em, Class<T> clazz, String rsqlFilter) {
		return createSpecFrom(em, clazz, rsqlFilter);
	}

	public static <T> CriteriaQuery<T> createQueryFrom(EntityManager em, Class<T> clazz, String rsqlFilter) {

		@SuppressWarnings("unchecked")
		final RsqlJpaCriteriaQueryVisitor<T> visitor = new RsqlJpaCriteriaQueryVisitor<T>(BeanUtils.instantiate(clazz));

		visitor.getBuilderTools().setArgumentParser(new RsqlArgumentParser());
		visitor.getBuilderTools().setPredicateBuilder(new RsqlPredicateBuilder());

		final Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
		operators.addAll(RSQLCustomOperators.operators());

		final Node rootNode = new RSQLParser(operators).parse(rsqlFilter);

		return rootNode.accept(visitor, em);

	}
}
