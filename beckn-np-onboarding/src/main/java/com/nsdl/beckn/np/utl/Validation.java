package com.nsdl.beckn.np.utl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.nsdl.beckn.np.model.request.ErrorResponse;
import com.nsdl.beckn.np.model.response.SchemaError;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Validation {

	@Autowired
	Gson gson;

//	@Value("${registry.security.validation.path}")
//	String dir;
//	public String readFile(String file) {
////		URL resource = getClass().getClassLoader().getResource("validation/" + file + ".json");
//		ClassPathResource classPathResource = new ClassPathResource("validation/" + file + ".json");
//		if (classPathResource != null) {
//
//			try {
//				byte[] binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
//				return new String(binaryData, StandardCharsets.UTF_8);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			// return Files.readString(Path.of(resource.toURI()));
//
//		}
//		return null;
//	}

	public static Properties fetchProperties(String path) {
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {

			ClassPathResource classPathResource = new ClassPathResource("validation/" + path);
//			if (classPathResource != null) {
//	
//				try {
//					byte[] binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
//					return new String(binaryData, StandardCharsets.UTF_8);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//	
//				// return Files.readString(Path.of(resource.toURI()));
//	
//			}

			// File file = ResourceUtils.getFile("classpath:validation/" + path);
			// InputStream in = new FileInputStream(file);
			inputStream = classPathResource.getInputStream();
			properties.load(inputStream);
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return properties;
	}

	public void validateElement(Object v, Map<String, Object> keys, Properties prop, SchemaError error,
			String actualKey, String schemaKey) {
		if (prop.getProperty(schemaKey.toString()) == null) {
			error.push(actualKey + ": is not required.");
		} else {
			if (!matchRule(v, keys, prop, schemaKey, schemaKey, actualKey, true)) {
				error.push(actualKey + ": is required or Invalid.");
			}
		}
	}

	public void collectKeys(Map<String, Object> keys, Map<String, Object> map, String parent, Properties prop,
			SchemaError error) {
		map.forEach((k, v) -> {

			System.out.println(parent + k);
			// arrayKey.add(parent + k)
			String key = parent + k;
			key = key.replaceAll("\\[[0-9]+\\]", "[]");
			keys.put(parent + k, v);
			keys.put(key, v);

			if (v instanceof Map) {
				collectKeys(keys, (Map) v, parent + k + ".", prop, error);

			} else if (v instanceof List) {
				boolean stringElement = false;
				for (int i = 0; i < ((List) v).size(); i++) {
					if (((List) v).get(i) instanceof Map) {
						collectKeys(keys, (Map) ((List) v).get(i), parent + k + "[" + i + "]" + ".", prop, error);
						matchArraysKeys(keys, key + "[]", parent + k + "[" + i + "]", prop, error);
					} else {

						stringElement = true;
					}
				}
				if (stringElement)
					validateElement(v, keys, prop, error, parent + k, key);

			} else {
				validateElement(v, keys, prop, error, parent + k, key);

			}
		});

	}

	public static boolean patternMatches(String value, String regexPattern) {
		return Pattern.compile(regexPattern).matcher(value).matches();
	}

	boolean matchRule(Object value, Map<String, Object> keys, Properties prop, String actualKey, String schemaKey,
			String datakey, boolean checkValidate) {
		boolean flag = true;
		boolean optionalFlag = false;
		String valRule = prop.getProperty(actualKey);
		if (valRule != null) {
			String[] valRuleArray = valRule.split(",,");

			for (int i = 0; i < valRuleArray.length; i++) {
				if (flag) {
					if ("required".equals(valRuleArray[i])) {
						if (value == null || "".equals(value.toString()) || "[]".equals(value.toString())) {
							flag = false;
						}
					} else if ((optionalFlag && "NOTEMPTY".equals(valRuleArray[i]))) {
						if (value != null && "".equals(value.toString())) {
							flag = false;
						}
					} else if (checkValidate && "date".equals(valRuleArray[i])) {
						if (value == null || !CommonUtl.isValidDate(value.toString())) {
							flag = false;
						}
					} else if (checkValidate && "city".equals(valRuleArray[i])) {
						if (value == null || CommonUtl.isValidCity(value.toString())) {
							flag = false;
						}
					} else if (checkValidate && "encryptkey".equals(valRuleArray[i])) {
						if (value == null || !CommonUtl.isValidEncrypt(value.toString())) {
							flag = false;
						}
					} else if (checkValidate && "signkey".equals(valRuleArray[i])) {
						if (value == null || !CommonUtl.isValidSign(value.toString())) {
							flag = false;
						}
					} else if (checkValidate && "from1".equals(valRuleArray[i])) {
						System.out.println(
								" Test2 from1 valRuleArray :: " + valRuleArray + " value:: " + value.toString());
						if (value == null || !CommonUtl.isValidfrom1(value.toString())) {
							flag = false;
						}
					} else if (checkValidate && "until1".equals(valRuleArray[i])) {
						System.out.println(
								" Test2 until1 valRuleArray :: " + valRuleArray + " value:: " + value.toString());
						if (value == null || !CommonUtl.isValidfrom1AndUntil(value.toString(), keys, actualKey)) {
							flag = false;
						}
					}
//					}
//					else if (checkValidate && "until1".equals(valRuleArray[i])) {
					// if (value == null || !CommonUtl.isValiduntil1(value.toString())) {
//							flag = false;
//						}
//					}
					else if (checkValidate && "optional".equals(valRuleArray[i])) {
						System.out.println(valRuleArray[i]);
						optionalFlag = true;
					} else if ("if".equals(valRuleArray[i].substring(0, 2))) {
						String valueData[] = valRuleArray[i].substring(2).trim().split(":");
						valueData[0] = valueData[0].replace(schemaKey, datakey);
						if (keys.get(valueData[0]) != null) {
							if (keys.get(valueData[0]).toString().equals(valueData[1])) {
								if (value == null || "".equals(value.toString()) || "[]".equals(value.toString())) {
									flag = false;
								}
							} else {
								checkValidate = false;
							}
						}

					} else if (checkValidate) {
						String rex = valRuleArray[i].trim();
//						if (value instanceof ArrayList) {
//							for (int j = 0; j < ((ArrayList) value).size(); j++) {
//								if (flag)
//									flag = patternMatches(((ArrayList) value).get(j).toString(), rex);
//						 	}
//
//						} else 
						{
							flag = patternMatches(value.toString(), rex);
						}

					}
				} else {
					break;
				}
			}
		}
		return flag;
	}

//	public String matchKeys(Map mapSchema, Map mapData, Properties prop) {
//		SchemaError error = new SchemaError();
//		mapData.forEach((k, v) -> {
//			if (!mapSchema.containsKey(k)) {
//				error.push(k + ": is not required.");
//			} else {
//				String valRule = prop.getProperty(k.toString());
//				if (valRule != null) {
//					String[] valRuleArray = valRule.split(",,");
//					if (!matchRule(valRuleArray, v)) {
//						error.push(k + ": is Invalid.");
//					}
//				}
//			}
//		});
//		mapSchema.forEach((k, v) -> {
//			if (!mapData.containsKey(k)) {
//				if (prop.getProperty(k.toString()).indexOf("required") != -1) {
//					error.push(k + ": is required.");
//				}
//			}
//		});
//
//		return error.getError();
//	}

	public String matchArraysKeys(Map<String, Object> keys, String schemaKey, String dataKey, Properties prop,
			SchemaError error) {
		prop.forEach((k, v) -> {
			if (k.toString().indexOf(schemaKey) != -1) {
				String k1 = k.toString().replace(schemaKey, dataKey);

				if (k1.indexOf("[]") == -1
						&& !matchRule(keys.get(k1), keys, prop, k.toString(), schemaKey, dataKey, false)) {
					error.push(k1 + ": is required.");
				}
			}

		});

		return error.getError();
	}

	public String matchKeys(Map mapData, Properties prop, SchemaError error) {
		prop.forEach((k, v) -> {
			if (!mapData.containsKey(k)) {
				if (v.toString().indexOf("required") != -1) {
					error.push(k + ": is required.");
				}
			}
		});

		return error.getError();
	}

	public Map<String, Object> getMap(String data) {
		Map<String, Object> map = new HashMap<>();
		map = gson.fromJson(data, Map.class);
		return map;
	}

	public String validate(String data, String schema) {
		try {
			Properties prop = fetchProperties(schema + "_validation.properties");
			SchemaError error = new SchemaError();
			Map<String, Object> mapSchema = new HashMap<>();
			System.out.println("Schema ----------");
			// collectKeys(mapSchema, getMap(readFile(schema)), "");
			System.out.println("-----------------");
			Map<String, Object> mapData = new HashMap<>();
			collectKeys(mapData, getMap(data), "", prop, error);

			matchKeys(mapData, prop, error);
			return error.getError();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("validate Schema:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public ResponseEntity<ErrorResponse> getValidationMsg(String errorMsg) {
		ErrorResponse error = new ErrorResponse();
		error.getMessage().getAck().setStatus("NACK");
		error.getError().setType(Constants.CONTEXT_ERROR);
		error.getError().setCode(Constants.VALIDSCHEMA[0]);
		error.getError().setMessage(Constants.VALIDSCHEMA[1] + " ERROR : " + errorMsg);
		log.info("subscribe: of response body: {}", gson.toJson(error));
		return ResponseEntity.ok(error);
	}
}
