package com.nsdl.beckn.np.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.google.gson.Gson;
import com.nsdl.beckn.np.model.request.NetworkParticipant;
import com.nsdl.beckn.np.model.request.SubscribeBody;
import com.nsdl.beckn.np.utl.Constants;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Table(name = "network_participant_master")
@Data

@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class NetworkParticipantMaster extends CommonModel {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	UUID id;

	@Column(name = "subscriber_id")
	String subscriberId;

	String subscriberUrl;
	String domain;

	String type;
	boolean msn;

	@Type(type = "jsonb")
	@Column(name = "city_code", columnDefinition = "jsonb")
	List<String> cityCode;

	String status;

	@ManyToOne
	EntityMaster entityMaster;

	@OneToMany(mappedBy = "networkParticipantMaster")
	List<SellerOnRecordMaster> sellerOnRecordMasters = new ArrayList<>();

	public String getKey() {
		return this.subscriberId + ":" + this.getDomain() + ":" + this.getType() + ":" + this.getSubscriberUrl();
	}

	public void setAllData(SubscribeBody body, NetworkParticipant req) {
		Gson gson = new Gson();

		this.subscriberId = body.getMessage().getEntity().getSubscriberId();

		if (req.getSubscriberUrl() != null) {
			this.subscriberUrl = req.getSubscriberUrl();
		}
		if (req.getDomain() != null) {
			this.domain = req.getDomain();
		}
//		if (req.getCallbackUrl() != null) {
//			this.callbackUrl = req.getCallbackUrl();
//		}
		if (req.getType() != null) {
			this.type = req.getType();
		}

		this.msn = req.isMsn();

		if (req.getCityCode() != null) {
			this.cityCode = req.getCityCode();
		}
		if (this.getId() == null) {
			this.status = Constants.STATUS_SUBSCRIBED;
		}
	}
}
