package com.nsdl.beckn.np.utl;

import java.util.HashMap;
import java.util.UUID;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.np.model.request.Logs;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SendError {

	@Value("${send.server.type}")
	String serverName;

	@Value("${send.server.url}")
	String url;
	@Value("${send.error.flag}")
	Boolean errorFlag;

	@Autowired
	SecurityUtils securityUtils;
	static HashMap<String, Boolean> msg = new HashMap();

	@Async(value = "sendErrorExecutor")
	public void sendError(Exception responseError, Object body, String priroty) {
		CloseableHttpResponse response = null;

		try(CloseableHttpClient httpClient = HttpClients.createDefault();) {
			log.info("EKey:"+this.securityUtils.getLogs().getId());
			if (msg.get(this.securityUtils.getLogs().getId().toString()) == null) {
				msg.put(this.securityUtils.getLogs().getId().toString(),true);
				if (errorFlag) {
					String uuid = UUID.randomUUID().toString();

					String bodyStr = CommonUtl.toJsonStr(body);
					Logs logs = Logs.builder().request(bodyStr).exception(responseError.getMessage()).priority(priroty)
							.requestId(uuid).javaStackTrace(CommonUtl.getPrintStackString(responseError))
							.serverName(serverName).build();

					log.info("sendError: sending logs:{}", CommonUtl.toJsonStr(logs));

					HttpPost request = new HttpPost(url);

					StringEntity entity = new StringEntity(CommonUtl.toJsonStr(logs));
					request.setEntity(entity);
					request.setHeader("Accept", "application/json");
					request.setHeader("Content-type", "application/json");
					response = httpClient.execute(request);
					System.out.println(response.getStatusLine().getStatusCode());
					log.info("sendError: sending logs:{}, status:{}", CommonUtl.toJsonStr(logs),
							response.getStatusLine().getStatusCode());
					httpClient.close();
					response.close();
				} 
			}else {
				msg.remove(this.securityUtils.getLogs().getId().toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("sendErrorException: sending logs:{}, Exception:{}", CommonUtl.toJsonStr(body),
					CommonUtl.getPrintStackString(e));

		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
