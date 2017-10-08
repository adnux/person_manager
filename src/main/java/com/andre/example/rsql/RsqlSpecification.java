package com.andre.example.rsql;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

public class RsqlSpecification<T> implements Specification<T> {

	private final String rsqlFilter;
	private final EntityManager em;
	// private final Optional<RsqlMapper> mapper;

	public RsqlSpecification(EntityManager em, String rsqlFilter) {
		// , Optional<RsqlMapper> mapper
		super();
		this.em = em;
		this.rsqlFilter = rsqlFilter;
		// this.mapper = mapper;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		// Create the JPA Predicate Visitor
		RsqlJpaPredicateVisitor<T> visitor = (RsqlJpaPredicateVisitor<T>) new RsqlJpaPredicateVisitor<T>()
				.defineRoot(root);

		// visitor.setRsqlMapper(mapper);

		visitor.setEntityClass((Class<T>) root.getJavaType());
		visitor.getBuilderTools().setArgumentParser(new RsqlArgumentParser());
		visitor.getBuilderTools().setPredicateBuilder(new RsqlPredicateBuilder());

		Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
		operators.addAll(RSQLCustomOperators.operators());

		// Parse a RSQL into a Node
		Node rootNode = new RSQLParser(operators).parse(rsqlFilter);

		// Visit the node to retrieve CriteriaQuery
		return rootNode.accept(visitor, em);

	}

}
