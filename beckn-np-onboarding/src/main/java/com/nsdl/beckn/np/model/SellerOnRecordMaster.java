package com.nsdl.beckn.np.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.google.gson.Gson;
import com.nsdl.beckn.np.model.request.SellerOnRecord;
import com.nsdl.beckn.np.model.request.SubscribeBody;
import com.nsdl.beckn.np.utl.CommonUtl;
import com.nsdl.beckn.np.utl.Constants;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Table(name = "seller_on_record_master")
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SellerOnRecordMaster extends CommonModel {

//
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	UUID id;

	@Column(name = "subscriber_id")
	String subscriberId;
	String uniqueKeyId;
	String signingPublicKey;
	String encryptionPublicKey;

	@Column(name = "valid_from", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	OffsetDateTime validFrom;

	@Column(name = "valid_until", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	OffsetDateTime validUntil;

	@Type(type = "jsonb")
	@Column(name = "city_code", columnDefinition = "jsonb")
	List<String> cityCode;
	String status;

	@ManyToOne
	NetworkParticipantMaster networkParticipantMaster;

	public String getKey() {
		return this.subscriberId + ":" + this.getUniqueKeyId();
	}

	public void setAllData(SubscribeBody body, SellerOnRecord req) {
		Gson gson = new Gson();
		this.subscriberId = body.getMessage().getEntity().getSubscriberId();
		this.uniqueKeyId = req.getUniqueKeyId();

		if (req.getKeyPair() != null) {
			this.signingPublicKey = req.getKeyPair().getSigningPublicKey();
			this.encryptionPublicKey = req.getKeyPair().getEncryptionPublicKey();
			this.validFrom = CommonUtl.getDate(req.getKeyPair().getValidFrom());
			this.validUntil = CommonUtl.getDate(req.getKeyPair().getValidUntil());
		}
		if (req.getCityCode() != null) {
			this.cityCode = req.getCityCode();
		}
		if (this.getId() == null) {
			this.status = Constants.STATUS_SUBSCRIBED;
		}

	}
}
