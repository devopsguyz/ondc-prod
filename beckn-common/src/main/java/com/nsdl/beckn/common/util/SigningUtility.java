package com.nsdl.beckn.common.util;

import static com.nsdl.beckn.common.exception.ErrorCode.CERTIFICATE_ALIAS_ERROR;
import static com.nsdl.beckn.common.exception.ErrorCode.CERTIFICATE_ERROR;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import com.nsdl.beckn.common.dto.KeyIdDto;
import com.nsdl.beckn.common.exception.ApplicationException;
import com.nsdl.beckn.common.exception.ErrorCode;
import com.nsdl.beckn.common.model.HeaderParams;
import com.nsdl.beckn.common.model.SigningModel;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SigningUtility {

	public String generateSignature(String req, SigningModel model) {
		String sign = null;
		try {
			if (model.isCertificateUsed()) {
				PrivateKey privateKey = getPrivateKeyFromP12(model);

				Signature rsa = Signature.getInstance("SHA1withRSA");
				rsa.initSign(privateKey);
				rsa.update(req.getBytes());

				byte[] str = rsa.sign();
				sign = Base64.getEncoder().encodeToString(str);

			} else if (StringUtils.isNoneBlank(model.getPrivateKey())) {
				Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(Base64.getDecoder().decode(model.getPrivateKey().getBytes()), 0);
				Ed25519Signer sig = new Ed25519Signer();
				sig.init(true, privateKey);
				sig.update(req.getBytes(), 0, req.length());

				byte[] s1 = sig.generateSignature();
				sign = Base64.getEncoder().encodeToString(s1);
			} else {
				log.error("neither certificate nor private key has been set for signature");
				throw new ApplicationException(ErrorCode.SIGNATURE_ERROR, ErrorCode.SIGNATURE_ERROR.getMessage());
			}
		} catch (Exception e) {
			log.error("error while generating the signature", e);
			throw new ApplicationException(ErrorCode.SIGNATURE_ERROR, ErrorCode.SIGNATURE_ERROR.getMessage());
		}

		log.info("Signature Generated From Data : " + sign);
		return sign;
	}

	public boolean verifySignature(String signature, String requestData, String publicKey) throws ApplicationException {
		boolean isVerified = false;

		try {
			Ed25519PublicKeyParameters publicKeyParams = new Ed25519PublicKeyParameters(Base64.getDecoder().decode(publicKey), 0);
			Ed25519Signer sv = new Ed25519Signer();
			sv.init(false, publicKeyParams);
			sv.update(requestData.getBytes(), 0, requestData.length());

			byte[] decodedSign = Base64.getDecoder().decode(signature);
			isVerified = sv.verifySignature(decodedSign);
			log.info("Is signature verified ? {}", isVerified);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return isVerified;
	}

	public boolean verifyWithP12PublicKey(String signature, String requestData, String publicKey) throws ApplicationException {
		boolean isVerified = false;
		try {
			log.info("Verifying with public key from p12 certificate");

			byte[] decryptPubKey = Base64.getDecoder().decode(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decryptPubKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			Signature sig = Signature.getInstance("SHA1withRSA");
			sig.initVerify(pubKey);
			sig.update(requestData.getBytes());
			byte[] decodedSign = Base64.getDecoder().decode(signature);
			isVerified = sig.verify(decodedSign);

		} catch (Exception e) {
			log.info("exception while verifing p12 certificate signature:", e);
			throw new ApplicationException(e);

		}
		return isVerified;
	}

	public Map<String, String> parseAuthorizationHeader(String authHeader) {
		Map<String, String> holder = new HashMap<>();
		if (authHeader.contains("Signature ")) {
			authHeader = authHeader.replace("Signature ", "");
			String[] keyVals = authHeader.split(",");
			for (String keyVal : keyVals) {
				String[] parts = keyVal.split("=", 2);
				if (parts[0] != null && parts[1] != null) {
					holder.put(parts[0].trim(), parts[1].trim());
				}
			}
			return holder;
		}
		return null;
	}

	public KeyIdDto splitKeyId(String kid) throws ApplicationException {
		KeyIdDto keyIdDto = null;
		try {
			if (kid != null && !kid.isEmpty()) {
				kid = kid.replace("\"", "");
				keyIdDto = new KeyIdDto();

				String[] a = kid.split("[|]");
				keyIdDto.setKeyId(a[0]);
				keyIdDto.setUniqueKeyId(a[1]);
				keyIdDto.setAlgo(a[2]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.INVALID_AUTH_HEADER, ErrorCode.INVALID_AUTH_HEADER.getMessage());
		}
		return keyIdDto;
	}

	public HeaderParams splitHeadersParam(String headers) throws ApplicationException {
		HeaderParams headerParams = null;
		try {
			if (headers != null && !headers.isEmpty()) {
				headers = headers.replace("\"", "");
				headerParams = new HeaderParams();

				String[] a = headers.split(" ");
				if ((a == null) || (a.length <= 2)) {
					log.error("Invalid Header");
					throw new ApplicationException(ErrorCode.INVALID_AUTH_HEADER, ErrorCode.INVALID_AUTH_HEADER.getMessage());
				}
				headerParams.setCreated(a[0].replace("(", "").replace(")", ""));
				headerParams.setExpires(a[1].replace("(", "").replace(")", ""));
				headerParams.setDiagest(a[2].trim());
				if (((headerParams.getCreated() == null) || !"created".equalsIgnoreCase(headerParams.getCreated()) || (headerParams.getExpires() == null)
						|| !"expires".equalsIgnoreCase(headerParams.getExpires()) || (headerParams.getDiagest() == null)
						|| !"digest".equalsIgnoreCase(headerParams.getDiagest()))) {
					log.error("Header sequense mismatch");
					throw new ApplicationException(ErrorCode.HEADER_SEQ_MISMATCH, ErrorCode.HEADER_SEQ_MISMATCH.getMessage());
				}

			}
		} catch (Exception e) {
			log.error("Header parsing Failed");
			throw new ApplicationException(ErrorCode.HEADER_PARSING_FAILED, ErrorCode.HEADER_PARSING_FAILED.getMessage());
		}
		return headerParams;
	}

	public String generateBlakeHash(String req) {
		Blake2bDigest digest = new Blake2bDigest(512);

		byte[] test = req.getBytes();
		digest.update(test, 0, test.length);

		byte[] hash = new byte[digest.getDigestSize()];
		digest.doFinal(hash, 0);

		String hex = Hex.toHexString(hash);

		// log.info("Base64 URL Encoded : " + bs64);
		return Base64.getUrlEncoder().encodeToString(hex.getBytes());
	}

	public boolean validateTime(String crt, String exp) {

		boolean isValid = false;
		try {

			if (crt != null && exp != null) {
				crt = crt.replace("\"", "");
				exp = exp.replace("\"", "");
				long created = Long.parseLong(crt);
				long expiry = Long.parseLong(exp);
				long now = System.currentTimeMillis() / 1000L;
				long diffInSec = expiry - created;

				if (((diffInSec > 0) && (created <= now) && (expiry > now) && (expiry >= created))) {
					isValid = true;
				}
			} else {
				log.error("created or expires timestamp value is null.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		log.debug("Is request valid with respect to sign header timestamp? {}", isValid);
		return isValid;
	}

	public PrivateKey getPrivateKeyFromP12(SigningModel model) {
		log.info("The SigningModel is: {}", model);
		// char[] pwd = model.getCertificatePwd().toCharArray(); // this is plane text password
		String decodedPwd = new String(Base64.getDecoder().decode(model.getCertificatePwd()));
		log.info("decoded certificate pwd is: {}", decodedPwd);
		char[] pwd = decodedPwd.toCharArray();
		PrivateKey userCertPrivateKey = null;

		try {
			KeyStore ks = KeyStore.getInstance(model.getCertificateType());

			String certificateAlias = model.getCertificateAlias();
			String path = model.getCertificatePath();
			log.info("certificate complete path is: {}", path);

			FileInputStream fileInputStream = new FileInputStream(path);

			ks.load(fileInputStream, pwd);

			Enumeration<String> e = ks.aliases();

			while (e.hasMoreElements()) {
				String alias = e.nextElement();
				if (StringUtils.isNoneBlank(certificateAlias) && alias.equals(certificateAlias.trim())) {
					userCertPrivateKey = (PrivateKey) ks.getKey(alias, pwd);
					log.info("matching certificate alias {} found in the certificate", certificateAlias);
					break;
				}
			}

			if (userCertPrivateKey == null) {
				throw new ApplicationException(CERTIFICATE_ALIAS_ERROR, CERTIFICATE_ALIAS_ERROR.getMessage());
			}

		} catch (Exception e) {
			log.error("error while reading the signature from certificate", e);
			throw new ApplicationException(CERTIFICATE_ERROR, CERTIFICATE_ERROR.getMessage());
		}
		return userCertPrivateKey;
	}
}
