package com.andre.example.rsql;

import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;
import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;

@SuppressWarnings("unchecked")
public class RsqlJpaCriteriaQueryVisitor<T> extends JpaCriteriaQueryVisitor<T> {

	private final RsqlJpaPredicateVisitor<T> predicateVisitor;

	public RsqlJpaCriteriaQueryVisitor(T... t) {
		super(t);
		this.predicateVisitor = new RsqlJpaPredicateVisitor<>();
		this.predicateVisitor.setEntityClass((Class<T>) t.getClass());
	}

	@Override
	protected JpaPredicateVisitor<T> getPredicateVisitor() {

		this.predicateVisitor.setBuilderTools(builderTools);

		return this.predicateVisitor;
	}

}
