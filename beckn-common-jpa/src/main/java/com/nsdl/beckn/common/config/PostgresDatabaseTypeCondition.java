package com.nsdl.beckn.common.config;

import static com.nsdl.beckn.common.constant.ApplicationConstant.ALLOWED_PERSISTENCE_TYPES;
import static com.nsdl.beckn.common.constant.ApplicationConstant.DB_POSTGRES;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import com.nsdl.beckn.common.model.yml.YmlApplicationModel;
import com.nsdl.beckn.common.model.yml.YmlPersistence;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgresDatabaseTypeCondition extends SpringBootCondition {
	private static final String FILE_NAME = "application.yml";

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		boolean database = false;
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_NAME);

			Representer representer = new Representer();
			representer.getPropertyUtils().setSkipMissingProperties(true);
			Yaml yaml = new Yaml(new Constructor(YmlApplicationModel.class), representer);
			YmlApplicationModel model = yaml.load(is);
			log.warn("YmlApplicationModel is {}", model);

			YmlPersistence persistence = model.getBeckn().getPersistence();

			if (persistence != null && StringUtils.isNotBlank(persistence.getType())) {
				String[] types = persistence.getType().split("\\|");
				if (types != null && types.length > 0) {
					database = validateAndFindIfDbPersistence(types);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("in ConditionOutcome................");
		if (database) {
			return ConditionOutcome.noMatch("No database configuration required");
		}
		return ConditionOutcome.match();
	}

	private boolean validateAndFindIfDbPersistence(String[] types) {
		for (String type : types) {
			log.info("Checking persistence type {}", type);
			if (!ALLOWED_PERSISTENCE_TYPES.contains(type)) {
				throw new RuntimeException("Invalid persistence type configured");
			}
			if (DB_POSTGRES.equals(type)) {
				return true;
			}
		}

		return false;
	}

}