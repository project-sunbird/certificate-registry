package org.sunbird.message;

/**
 * This interface will hold all the response key and message
 *
 * @author Amit Kumar
 */
public interface IResponseMessage extends IUserResponseMessage, IOrgResponseMessage {

  String INVALID_REQUESTED_DATA = "INVALID_REQUESTED_DATA";
  String INVALID_OPERATION_NAME = "INVALID_OPERATION_NAME";
  String INTERNAL_ERROR = "INTERNAL_ERROR";
  String UNAUTHORIZED = "UNAUTHORIZED";
  String ID_ALREADY_EXISTS="ID_ALREADY_EXISTS";
  String MISSING_MANADATORY_PARAMS="MANDATORY PARAM {0} IS MISSING";
  String DATA_TYPE_ERROR="{0} PARAM SHOULD BE OF TYPE {1}";
  String EMPTY_MANDATORY_PARAM="value can not be empty for mandatory param {0}";
  String INVALID_ID_PROVIDED="PROVIDED ID {0} AND ACCESSCODE {1} COMBINATION DOES NOT EXISTS";
  String INVALID_PROVIDED_URL="PROVIDED URL {0} DOESN'T EXISITS";
  String INVALID_RELATED_TYPE="invalid related type {0} provided valid related types are {1}";
  String INVALID_RECIPIENT_TYPE="invalid provided recipientType {0} valid recipientTypes are {1}";
}
