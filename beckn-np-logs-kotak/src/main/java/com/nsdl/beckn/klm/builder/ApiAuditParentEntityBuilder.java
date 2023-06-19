package com.nsdl.beckn.klm.builder;

import java.util.List;
import java.util.stream.Collectors;

import com.nsdl.beckn.klm.dto.ApiAuditParentEntityDTO;
import com.nsdl.beckn.klm.model.adapter.ApiAuditEntity;
import com.nsdl.beckn.klm.model.archvl.ApiAuditArchvlEntity;
import com.nsdl.beckn.klm.model.archvltemp.ApiAuditArchvlTempEntity;

public class ApiAuditParentEntityBuilder {
	
	public static ApiAuditParentEntityBuilder aApiAuditParentEntityBuilder() {
		return new ApiAuditParentEntityBuilder();
	}
	
    /**
     * This method convert ApiAuditEntity to ApiAuditParentEntityDTO by ApiAuditEntity.
     * @param apiAuditEntity Ii is a entity.
     * @return Its returns ApiAuditParentEntityDTO.
     */
	public ApiAuditParentEntityDTO convertApiAuditEntityToApiAuditParentEntityDTO(ApiAuditEntity apiAuditEntity) {

		return ApiAuditParentEntityDTO.builder().action(apiAuditEntity.getAction()).buyerId(apiAuditEntity.getBuyerId())
				.coreVersion(apiAuditEntity.getCoreVersion()).createdOn(apiAuditEntity.getCreatedOn())
				.domain(apiAuditEntity.getDomain()).error(apiAuditEntity.getError())
				.headers(apiAuditEntity.getHeaders()).hostId(apiAuditEntity.getHostId()).id(apiAuditEntity.getId())
				.json(apiAuditEntity.getJson()).messageId(apiAuditEntity.getMessageId())
				.remoteHost(apiAuditEntity.getRemoteHost()).sellerId(apiAuditEntity.getSellerId())
				.status(apiAuditEntity.getStatus()).timeTaken(apiAuditEntity.getTimeTaken())
				.transactionId(apiAuditEntity.getTransactionId()).type(apiAuditEntity.getType()).build();
	}
	
    /**
     * This method convert list of ApiAuditEntity to ApiAuditParentEntityDTO by list.
     * @param list ApiAuditEntity list.
     * @return Its returns list of ApiAuditParentEntityDTO.
     */
	public List<ApiAuditParentEntityDTO> convertListOfApiAuditEntityToListApiAuditParentEntityDTO(List<ApiAuditEntity> list) {
		return list.stream()
				.map(apiAuditEntity -> ApiAuditParentEntityDTO.builder().action(apiAuditEntity.getAction())
						.buyerId(apiAuditEntity.getBuyerId()).coreVersion(apiAuditEntity.getCoreVersion())
						.createdOn(apiAuditEntity.getCreatedOn()).domain(apiAuditEntity.getDomain())
						.error(apiAuditEntity.getError()).headers(apiAuditEntity.getHeaders())
						.hostId(apiAuditEntity.getHostId()).id(apiAuditEntity.getId()).json(apiAuditEntity.getJson())
						.messageId(apiAuditEntity.getMessageId()).remoteHost(apiAuditEntity.getRemoteHost())
						.sellerId(apiAuditEntity.getSellerId()).status(apiAuditEntity.getStatus())
						.timeTaken(apiAuditEntity.getTimeTaken()).transactionId(apiAuditEntity.getTransactionId())
						.type(apiAuditEntity.getType()).build())
				.collect(Collectors.toList());
	}
	/**
    * This method convert ApiAuditArchvlEntity to ApiAuditParentEntityDTO by apiAuditArchvlEntity. 
    * @param apiAuditArchvlEntity It is a Entity.
    * @return Its returns list of ApiAuditParentEntityDTO.
    */
	public ApiAuditParentEntityDTO convertApiAuditArchvlEntityToApiAuditParentEntityDTO(ApiAuditArchvlEntity apiAuditArchvlEntity) {

		return ApiAuditParentEntityDTO.builder().action(apiAuditArchvlEntity.getAction()).buyerId(apiAuditArchvlEntity.getBuyerId())
				.coreVersion(apiAuditArchvlEntity.getCoreVersion()).createdOn(apiAuditArchvlEntity.getCreatedOn())
				.domain(apiAuditArchvlEntity.getDomain()).error(apiAuditArchvlEntity.getError())
				.headers(apiAuditArchvlEntity.getHeaders()).hostId(apiAuditArchvlEntity.getHostId()).id(apiAuditArchvlEntity.getId())
				.json(apiAuditArchvlEntity.getJson()).messageId(apiAuditArchvlEntity.getMessageId())
				.remoteHost(apiAuditArchvlEntity.getRemoteHost()).sellerId(apiAuditArchvlEntity.getSellerId())
				.status(apiAuditArchvlEntity.getStatus()).timeTaken(apiAuditArchvlEntity.getTimeTaken())
				.transactionId(apiAuditArchvlEntity.getTransactionId()).type(apiAuditArchvlEntity.getType()).build();
	}
    /**
     * This method convert list of ApiAuditArchvlEntity to ApiAuditParentEntityDTO by list. 
     * @param list It is a ApiAuditArchvlEntity.
     * @return Its returns list of ApiAuditParentEntityDTO.
     */
	public List<ApiAuditParentEntityDTO> convertListOfApiAuditArchvlEntityToListApiAuditParentEntityDTO(List<ApiAuditArchvlEntity> list) {
		return list.stream()
				.map(apiAuditArchvlEntity -> ApiAuditParentEntityDTO.builder().action(apiAuditArchvlEntity.getAction())
						.buyerId(apiAuditArchvlEntity.getBuyerId()).coreVersion(apiAuditArchvlEntity.getCoreVersion())
						.createdOn(apiAuditArchvlEntity.getCreatedOn()).domain(apiAuditArchvlEntity.getDomain())
						.error(apiAuditArchvlEntity.getError()).headers(apiAuditArchvlEntity.getHeaders())
						.hostId(apiAuditArchvlEntity.getHostId()).id(apiAuditArchvlEntity.getId()).json(apiAuditArchvlEntity.getJson())
						.messageId(apiAuditArchvlEntity.getMessageId()).remoteHost(apiAuditArchvlEntity.getRemoteHost())
						.sellerId(apiAuditArchvlEntity.getSellerId()).status(apiAuditArchvlEntity.getStatus())
						.timeTaken(apiAuditArchvlEntity.getTimeTaken()).transactionId(apiAuditArchvlEntity.getTransactionId())
						.type(apiAuditArchvlEntity.getType()).build())
				.collect(Collectors.toList());
	}
	/**    
	 * This method convert ApiAuditArchvlTempEntity to ApiAuditParentEntityDTO by apiAuditArchvlTempEntity.
     * @param apiAuditArchvlTempEntity It is a Entity.
     * @return Its returns list of ApiAuditParentEntityDTO.
     */
	public ApiAuditParentEntityDTO convertApiAuditArchvlTempEntityToApiAuditParentEntityDTO(ApiAuditArchvlTempEntity apiAuditArchvlTempEntity) {

		return ApiAuditParentEntityDTO.builder().action(apiAuditArchvlTempEntity.getAction()).buyerId(apiAuditArchvlTempEntity.getBuyerId())
				.coreVersion(apiAuditArchvlTempEntity.getCoreVersion()).createdOn(apiAuditArchvlTempEntity.getCreatedOn())
				.domain(apiAuditArchvlTempEntity.getDomain()).error(apiAuditArchvlTempEntity.getError())
				.headers(apiAuditArchvlTempEntity.getHeaders()).hostId(apiAuditArchvlTempEntity.getHostId()).id(apiAuditArchvlTempEntity.getId())
				.json(apiAuditArchvlTempEntity.getJson()).messageId(apiAuditArchvlTempEntity.getMessageId())
				.remoteHost(apiAuditArchvlTempEntity.getRemoteHost()).sellerId(apiAuditArchvlTempEntity.getSellerId())
				.status(apiAuditArchvlTempEntity.getStatus()).timeTaken(apiAuditArchvlTempEntity.getTimeTaken())
				.transactionId(apiAuditArchvlTempEntity.getTransactionId()).type(apiAuditArchvlTempEntity.getType()).build();
	}
    /**
     * This method converts ApiAuditArchvlTempEntity to ApiAuditParentEntityDTO by list.
     * @param list It is ApiAuditArchvlTempEntity.
     * @return Its returns list of  ApiAuditParentEntityDTO.
     */
	public List<ApiAuditParentEntityDTO> convertListOfApiAuditArchvlTempEntityToListApiAuditParentEntityDTO(List<ApiAuditArchvlTempEntity> list) {
		return list.stream()
				.map(apiAuditArchvlTempEntity -> ApiAuditParentEntityDTO.builder().action(apiAuditArchvlTempEntity.getAction())
						.buyerId(apiAuditArchvlTempEntity.getBuyerId()).coreVersion(apiAuditArchvlTempEntity.getCoreVersion())
						.createdOn(apiAuditArchvlTempEntity.getCreatedOn()).domain(apiAuditArchvlTempEntity.getDomain())
						.error(apiAuditArchvlTempEntity.getError()).headers(apiAuditArchvlTempEntity.getHeaders())
						.hostId(apiAuditArchvlTempEntity.getHostId()).id(apiAuditArchvlTempEntity.getId()).json(apiAuditArchvlTempEntity.getJson())
						.messageId(apiAuditArchvlTempEntity.getMessageId()).remoteHost(apiAuditArchvlTempEntity.getRemoteHost())
						.sellerId(apiAuditArchvlTempEntity.getSellerId()).status(apiAuditArchvlTempEntity.getStatus())
						.timeTaken(apiAuditArchvlTempEntity.getTimeTaken()).transactionId(apiAuditArchvlTempEntity.getTransactionId())
						.type(apiAuditArchvlTempEntity.getType()).build())
				.collect(Collectors.toList());
	}
}
