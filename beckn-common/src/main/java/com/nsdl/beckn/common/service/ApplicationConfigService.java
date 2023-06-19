package com.nsdl.beckn.common.service;

import static com.nsdl.beckn.common.constant.ApplicationConstant.BECKN_API_COMMON_CACHE;
import static com.nsdl.beckn.common.exception.ErrorCode.SUBSCRIBER_NOT_FOUND;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.beckn.common.exception.ApplicationException;
import com.nsdl.beckn.common.model.ApiParamModel;
import com.nsdl.beckn.common.model.ConfigModel;
import com.nsdl.beckn.common.model.SigningModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApplicationConfigService {

	@Autowired
	@Value("classpath:config-${beckn.entity.type}.json")
	private Resource resource;

	@Autowired
	private ObjectMapper mapper;

	@Cacheable(value = BECKN_API_COMMON_CACHE)
	public ConfigModel loadApplicationConfiguration(String subscriberId, String apiName) {

		log.info("going to load the configuration for subscriberid: {} and api: {}", subscriberId, apiName);

		try {
			List<ConfigModel> configModels = this.mapper.readValue(this.resource.getInputStream(),
					this.mapper.getTypeFactory().constructCollectionType(List.class, ConfigModel.class));

			ConfigModel configModel = findConfigById(subscriberId, configModels);

			configModel.setMatchedApi(findMatchingApi(configModel, apiName));

			log.info("The content of init config file is {}", configModel);
			return configModel;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while loading the config json file");
		}

	}

	private ConfigModel findConfigById(String subscriberId, List<ConfigModel> configModels) {
		ConfigModel configModel = configModels
				.stream()
				.filter(model -> model.getSubscriberId().equalsIgnoreCase(subscriberId))
				.findFirst()
				.orElseThrow(() -> {
					String error = "not able to find the subscriberId [" + subscriberId + "] in the config json file";
					throw new ApplicationException(SUBSCRIBER_NOT_FOUND, error);
					// new RuntimeException("not able to find the subscriberId [" + subscriberId + "] in the config json file");
				});

		log.info("the matched config model for the id {} is  {}", subscriberId, configModel);
		return configModel;
	}

	@Cacheable(value = BECKN_API_COMMON_CACHE)
	public ConfigModel loadApplicationConfiguration1(String apiName) {
		ConfigModel configModel = null;
		try {
			configModel = this.mapper.readValue(this.resource.getInputStream(), ConfigModel.class);
			configModel.setMatchedApi(findMatchingApi(configModel, apiName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("The content of init config file is {}", configModel);
		return configModel;
	}

	private ApiParamModel findMatchingApi(ConfigModel configModel, String apiName) {
		return configModel.getApi()
				.stream()
				.filter(model -> apiName.equalsIgnoreCase(model.getName()))
				.findFirst()
				.orElseThrow(() -> {
					throw new RuntimeException("Invalid API name configured. Please configure json file correctly");
				});

	}

	@Cacheable(value = BECKN_API_COMMON_CACHE)
	public SigningModel getSigningConfiguration(String subscriberId) {
		try {
			List<ConfigModel> configModels = this.mapper.readValue(this.resource.getInputStream(),
					this.mapper.getTypeFactory().constructCollectionType(List.class, ConfigModel.class));

			ConfigModel configModel = findConfigById(subscriberId, configModels);

			log.info("The content of init config file is {}", configModel);
			return configModel.getSigning();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while loading the config json file");
		}
	}

}
