package com.andre.example;

import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.util.ClassUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class SpringDataRestConfiguration extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		String packageName = Application.class.getPackage().getName();
		Set<BeanDefinition> converters = scanner.findCandidateComponents(packageName);

		converters.stream().map(BeanDefinition::getBeanClassName).map(this::getClass).forEach(config::exposeIdsFor);
	}

	private Class<?> getClass(String className) {
		return ClassUtils.getUserClass(className);
	}

	@Override
	public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
		Validator validator = validator();
		validatingListener.addValidator("beforeCreate", validator);
		validatingListener.addValidator("beforeSave", validator);
	}

	@Bean
	@Primary
	Validator validator() {
		return new LocalValidatorFactoryBean();
	}
}
