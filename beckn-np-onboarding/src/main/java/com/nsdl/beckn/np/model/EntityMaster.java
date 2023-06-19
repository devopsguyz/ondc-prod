package com.nsdl.beckn.np.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.nsdl.beckn.np.model.request.SubscribeBody;
import com.nsdl.beckn.np.utl.CommonUtl;
import com.nsdl.beckn.np.utl.Constants;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Table(name = "entity_master")
@Data

//@TypeDefs({
//    @TypeDef(name = "string-array", typeClass = StringArrayType.class),
//    @TypeDef(name = "int-array", typeClass = IntArrayType.class),
//    @TypeDef(name = "json", typeClass = JsonStringType.class)
//})

@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class EntityMaster extends CommonModel {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	UUID id;

	@Column(name = "subscriber_id", unique = true)
	String subscriberId;

	String panNumber;
	String nameAsPerPan;
	LocalDate dateOfIncorporation;
	String gstNumber;
	@Type(type = "jsonb")
	@Column(name = "city_code", columnDefinition = "jsonb")
	List<String> cityCode;

	String legalEntityName;
	// Chk in request
	@Column(name = "effective_date_of_registration", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	LocalDate effectiveDateOfRegistration;
	String businessAddress;
	String nameOfAuthorisedSignatory;
	String addressOfAuthorisedSignatory;
	String emailId;
	String mobileNo;
	String country;
	String callbackUrl;
	String signingPublicKey;
	String uniqueKeyId;
	String encryptionPublicKey;
	@Column(name = "valid_from", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	OffsetDateTime validFrom;
	@Column(name = "valid_until", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	OffsetDateTime validUntil;
	String status;

	String panStatus;
	String panStatusError;
	String gstStatus;
	String gstStatusError;
	
	// @JsonInclude()
	// @javax.persistence.Transient
	@OneToMany(mappedBy = "entityMaster")
	List<NetworkParticipantMaster> networkParticipantMasters = new ArrayList<>();

	// @JsonInclude()
//	@javax.persistence.Transient
	// List<SellerOnRecordMaster> sellerOnRecordMasters;

	public void setAllData(SubscribeBody req) {
//		Gson gson = new Gson();
		this.subscriberId = req.getMessage().getEntity().getSubscriberId();
		if (!req.isEdit()) {
			this.panNumber = req.getMessage().getEntity().getPan().getPanNo();
			this.nameAsPerPan = req.getMessage().getEntity().getPan().getNameAsPerPan();
			this.dateOfIncorporation = CommonUtl
					.getLocalDate(req.getMessage().getEntity().getPan().getDateOfIncorporation());
		}
		if (!req.isEdit() && (req.getMessage().getEntity().getGst() != null)) {
			this.gstNumber = req.getMessage().getEntity().getGst().getGstNo();
			this.cityCode = req.getMessage().getEntity().getGst().getCityCode();// gson.toJson(req.getMessage().getEntity().getGst().getCityCode());
			this.legalEntityName = req.getMessage().getEntity().getGst().getLegalEntityName();
			this.businessAddress = req.getMessage().getEntity().getGst().getBusinessAddress();
		}
		if ((this.getId() == null) && (req.getMessage().getEntity().getNameOfAuthorisedSignatory() != null)) {
			this.nameOfAuthorisedSignatory = req.getMessage().getEntity().getNameOfAuthorisedSignatory();
		}
		if ((this.getId() == null) && (req.getMessage().getEntity().getAddressOfAuthorisedSignatory() != null)) {
			this.addressOfAuthorisedSignatory = req.getMessage().getEntity().getAddressOfAuthorisedSignatory();
		}
		if ((this.getId() == null) && (req.getMessage().getEntity().getEmailId() != null)) {
			this.emailId = req.getMessage().getEntity().getEmailId();
		}
		if ((this.getId() == null) && (req.getMessage().getEntity().getMobileNo() != null)) {
			this.mobileNo = req.getMessage().getEntity().getMobileNo();
		}
		if (req.getMessage().getEntity().getKeyPair() != null) {
			this.signingPublicKey = req.getMessage().getEntity().getKeyPair().getSigningPublicKey();
			this.encryptionPublicKey = req.getMessage().getEntity().getKeyPair().getEncryptionPublicKey();
			this.validFrom = CommonUtl.getDate(req.getMessage().getEntity().getKeyPair().getValidFrom());
			this.validUntil = CommonUtl.getDate(req.getMessage().getEntity().getKeyPair().getValidUntil());
		}
		if ((this.getId() == null) && (req.getMessage().getEntity().getEffectiveDateOfRegistration() != null)) {
			this.effectiveDateOfRegistration = CommonUtl
					.getLocalDate(req.getMessage().getEntity().getEffectiveDateOfRegistration());
		}
		if (this.getId() == null) {
			this.status = Constants.STATUS_SUBSCRIBED;
		}
		if ((this.getId() == null) && (req.getMessage().getEntity().getCountry() != null)) {
			this.country = req.getMessage().getEntity().getCountry();
		}
		if ((this.getId() == null) && (req.getMessage().getEntity().getCallbackUrl() != null)) {
			this.callbackUrl = req.getMessage().getEntity().getCallbackUrl();
		}
		if ((req.getMessage().getEntity().getUniqueKeyId() != null)) {
			this.uniqueKeyId = req.getMessage().getEntity().getUniqueKeyId();
		}

	}
}
