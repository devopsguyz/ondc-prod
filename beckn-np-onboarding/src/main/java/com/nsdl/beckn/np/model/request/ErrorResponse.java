/*
 * ONDC Registry 
 * ONDC Participant Management API
 *
 * OpenAPI spec version: 2.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.nsdl.beckn.np.model.request;

import lombok.Data;

@Data
public class ErrorResponse {
	private ErrorResponseACK message = new ErrorResponseACK();

	private Error error = new Error();

}
