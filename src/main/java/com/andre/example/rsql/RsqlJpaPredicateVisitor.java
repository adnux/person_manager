package com.andre.example.rsql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tennaito.rsql.builder.BuilderTools;
import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;
import com.github.tennaito.rsql.jpa.PredicateBuilder;
import com.github.tennaito.rsql.parser.ast.ComparisonOperatorProxy;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.OrNode;

@SuppressWarnings("unchecked")
public class RsqlJpaPredicateVisitor<T> extends JpaPredicateVisitor<T> {

	private static final char PERCENT_CHAR = '%';

	private static final String WHITE_SPACE = " ";

	private From<?, ?> root;

	// private Optional<RsqlMapper> rsqlMapper = Optional.empty();

	private final transient Logger log = LoggerFactory.getLogger(getClass());

	public void setEntityClass(Class<T> clazz) {
		entityClass = clazz;
	}

	@Override
	public Predicate visit(ComparisonNode node, EntityManager entityManager) {

		return createComparisonPredicate(node, entityManager);

	}

	@Override
	public Predicate visit(AndNode node, EntityManager entityManager) {
		return createLogicalPredicate(node, root, entityClass, entityManager, builderTools);
	}

	@Override
	public Predicate visit(OrNode node, EntityManager entityManager) {
		return createLogicalPredicate(node, root, entityClass, entityManager, builderTools);
	}

	private Predicate createLikeWithSpace(Expression<String> propertyPath, String argument, EntityManager manager) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		Predicate predicate = null;
		List<String> args = Arrays.asList(argument.split(WHITE_SPACE));

		for (String arg : args) {

			StringBuffer buffer = new StringBuffer(arg.replace(PredicateBuilder.LIKE_WILDCARD, PERCENT_CHAR));

			if (args.size() > 1) {

				buffer.insert(0, PERCENT_CHAR);
				buffer.append(PERCENT_CHAR);
			}

			Predicate likePredicate = builder.like(builder.lower(propertyPath),
					buffer.toString().toLowerCase(Locale.getDefault()));

			if (predicate == null) {
				predicate = likePredicate;
			} else {
				predicate = builder.and(predicate, likePredicate);
			}

		}

		return predicate;
	}

	private Predicate createComparisonPredicate(ComparisonNode node, EntityManager entityManager) {
		try {

			ComparisonOperatorProxy operator = getOperatorEnum(node);
			Object arg = node.getArguments().get(0);

			if (isEqualStringWithSpace(operator, arg)) {

				log.info("Property graph path : {0}", node.getSelector());

				@SuppressWarnings("rawtypes")
				Expression propertyPath = PredicateBuilder.findPropertyPath(node.getSelector(), root, entityManager,
						getBuilderTools());

				return createLikeWithSpace(propertyPath, (String) arg, entityManager);
			}

			ComparisonNode newNode = node;
			// if (this.rsqlMapper.isPresent()) {
			// String translatedSelector =
			// this.rsqlMapper.get().translate(node.getSelector(), entityClass);
			// newNode = node.withSelector(translatedSelector);
			// }
			return PredicateBuilder.<T>createPredicate(newNode, root, entityClass, entityManager, getBuilderTools());

		} catch (

		ClassCastException e) {
			if (builderTools.getPredicateBuilder() != null) {
				return builderTools.getPredicateBuilder().createPredicate(node, root, entityClass, entityManager,
						getBuilderTools());
			}
			throw e;
		}

	}

	private boolean isEqualStringWithSpace(ComparisonOperatorProxy operator, Object arg) {
		if (operator != null && arg != null && operator.equals(ComparisonOperatorProxy.EQUAL)
				&& String.class.isAssignableFrom(arg.getClass())) {

			String strArg = (String) arg;

			return strArg.contains(WHITE_SPACE);

		}
		return false;
	}

	private ComparisonOperatorProxy getOperatorEnum(ComparisonNode node) {
		return ComparisonOperatorProxy.asEnum(node.getOperator());
	}

	@Override
	public JpaPredicateVisitor<T> defineRoot(@SuppressWarnings("rawtypes") From root) {
		this.root = root;
		return super.defineRoot(root);
	}

	public Predicate createLogicalPredicate(LogicalNode logical, @SuppressWarnings("rawtypes") From root,
			Class<T> entity, EntityManager entityManager, BuilderTools misc) {

		log.debug("Creating Predicate for logical node: {0}", logical);

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		List<Predicate> predicates = new ArrayList<Predicate>();

		log.debug("Creating Predicates from all children nodes.");

		for (Node node : logical.getChildren()) {

			if (LogicalNode.class.isAssignableFrom(node.getClass())) {

				predicates.add(createLogicalPredicate((LogicalNode) node, root, entity, entityManager, misc));

			} else if (ComparisonNode.class.isAssignableFrom(node.getClass())) {

				predicates.add(createComparisonPredicate((ComparisonNode) node, entityManager));

			}

		}

		if (logical.getOperator() == LogicalOperator.AND) {
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		}

		if (logical.getOperator() == LogicalOperator.OR) {
			return builder.or(predicates.toArray(new Predicate[predicates.size()]));
		}

		throw new IllegalArgumentException("Unknown operator: " + logical.getOperator());
	}

	// public void setRsqlMapper(Optional<RsqlMapper> rsqlMapper) {
	// this.rsqlMapper = rsqlMapper;
	// }
}
