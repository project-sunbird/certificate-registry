package validators;

import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * this is a validator class for adding certificates
 * @author anmolgupta
 */
public class CertAddRequestValidator implements IRequestValidator {
    static Logger logger=Logger.getLogger(CertAddRequestValidator.class);
    private Request request;
    static List<String> mandatoryParamsList = Lists.newArrayList(JsonKeys.ID, JsonKeys.ACCESS_CODE, JsonKeys.PDF_URL, JsonKeys.USER_ID,JsonKeys.RECIPIENT_TYPE);
    static List<String>allowedRelatedTypes=Lists.newArrayList(JsonKeys.COURSE_COMPLETION, JsonKeys.ASSESSMENT,JsonKeys.COURSE_PERFORMANCE);
    static List<String> allowedRecipientsType=Lists.newArrayList(JsonKeys.INDIVIDUAL,JsonKeys.ENTITY);
    public CertAddRequestValidator() {
    }

    @Override
    public void validate(Request request) throws BaseException {
        this.request=request;
        logger.info("CertAddRequestValidator:validate:started validating the request with request id "+request.getRequest());
        validateMandatoryParams();
        validateMandatoryJsonData();
        validateRecipientType();
        if(request.getRequest().containsKey(JsonKeys.RELATED)){
            validateRelatedObject();
        }
        logger.info("CertAddRequestValidator:validateMandatoryParams:correct request provided");
    }

    private void validateMandatoryJsonData() throws BaseException {
        if(MapUtils.isEmpty((Map)request.getRequest().get(JsonKeys.JSON_DATA))){
            logger.error("CertAddRequestValidator:validateMandatoryJsonData:incorrect request provided");
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.EMPTY_MANDATORY_PARAM,JsonKeys.JSON_DATA), ResponseCode.CLIENT_ERROR.getCode());
        }
        validateDataType();
    }
    private void validateDataType() throws BaseException {
        if (!(request.get(JsonKeys.JSON_DATA) instanceof Map)) {
            logger.error("CertAddRequestValidator:validateDataType:incorrect request provided");
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.DATA_TYPE_ERROR,JsonKeys.JSON_DATA,"map"), ResponseCode.CLIENT_ERROR.getCode());

        }
    }

    private void validateMandatoryParams() throws BaseException {
        Map<String,Object>certAddReqMap=request.getRequest();
        if(MapUtils.isEmpty(request.getRequest())){
            logger.error("CertAddRequestValidator:validateMandatoryParams:incorrect request provided");
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, IResponseMessage.INVALID_REQUESTED_DATA, ResponseCode.CLIENT_ERROR.getCode());
        }
        for (String param :mandatoryParamsList) {
            if(!certAddReqMap.containsKey(param)){
                logger.error("CertAddRequestValidator:validateMandatoryParams:incorrect request provided");
                throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.MISSING_MANADATORY_PARAMS,param), ResponseCode.CLIENT_ERROR.getCode());
            }
            if(!(certAddReqMap.get(param) instanceof String)){
                logger.error("CertAddRequestValidator:validateMandatoryParams:incorrect request provided");
                throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.DATA_TYPE_ERROR,param,"string"), ResponseCode.CLIENT_ERROR.getCode());
            }
            validatePresence(param,(String)certAddReqMap.get(param));
        }
    }

    private void validatePresence(String key,String value) throws BaseException {
        if (StringUtils.isBlank(value)) {
            logger.error("CertAddRequestValidator:validatePresence:incorrect request provided");
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.EMPTY_MANDATORY_PARAM,key), ResponseCode.CLIENT_ERROR.getCode());
        }
    }


    private void validateRelatedObject() throws BaseException {
        if(!(request.getRequest().get(JsonKeys.RELATED) instanceof Map)){
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.DATA_TYPE_ERROR,JsonKeys.RELATED,"map"), ResponseCode.CLIENT_ERROR.getCode());
        }
        Map<String,Object>relatedMap=(Map)request.getRequest().get(JsonKeys.RELATED);
        if(!relatedMap.containsKey(JsonKeys.TYPE)){
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.MISSING_MANADATORY_PARAMS, JsonKeys.TYPE.concat(" inside related map")), ResponseCode.CLIENT_ERROR.getCode());
        }
        String relatedType=(String)relatedMap.get(JsonKeys.TYPE);
        if(!allowedRelatedTypes.contains(relatedType)){
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.INVALID_RELATED_TYPE,relatedType,allowedRelatedTypes), ResponseCode.CLIENT_ERROR.getCode());
        }
    }

    private void validateRecipientType() throws BaseException {

        String recType=(String)request.getRequest().get(JsonKeys.RECIPIENT_TYPE);
        if(!allowedRecipientsType.contains(recType)){
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.INVALID_RECIPIENT_TYPE,recType,allowedRecipientsType), ResponseCode.CLIENT_ERROR.getCode());
        }
    }
}