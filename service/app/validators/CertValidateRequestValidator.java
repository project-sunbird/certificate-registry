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
 * this is a validator class for validating certificate
 * @author anmolgupta
 */
public class CertValidateRequestValidator implements IRequestValidator {


    static Logger logger=Logger.getLogger(CertValidateRequestValidator.class);
    private Request request;
    static List<String> mandatoryParamsList = Lists.newArrayList(JsonKeys.CERT_ID, JsonKeys.ACCESS_CODE);
    @Override
    public void validate(Request request) throws BaseException {
        this.request=request;
        logger.error("CertValidateRequestValidator:validate:started validating request");
        validateMandatoryParams();
    }
    private void validateMandatoryParams() throws BaseException {
        Map<String,Object> certValMap=request.getRequest();
        if(MapUtils.isEmpty(request.getRequest())){
            logger.error("CertValidateRequestValidator:validateMandatoryParams:incorrect request provided");
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, IResponseMessage.INVALID_REQUESTED_DATA, ResponseCode.CLIENT_ERROR.getCode());
        }
        for (String param :mandatoryParamsList) {
            if(!certValMap.containsKey(param)){
                logger.error("CertValidateRequestValidator:validateMandatoryParams:incorrect request provided");
                throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.MISSING_MANDATORY_PARAMS,param), ResponseCode.CLIENT_ERROR.getCode());
            }
            if(!(certValMap.get(param) instanceof String)){
                logger.error("CertValidateRequestValidator:validateMandatoryParams:incorrect request provided");
                throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.DATA_TYPE_ERROR,param,"string"), ResponseCode.CLIENT_ERROR.getCode());
            }
            validatePresence(param,(String)certValMap.get(param));
        }

        logger.info("CertValidateRequestValidator:validateMandatoryParams:correct request provided");
    }

    private void validatePresence(String key,String value) throws BaseException {
        if (StringUtils.isBlank(value)) {
            logger.error("CertValidateRequestValidator:validateMandatoryParams:incorrect request provided");
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.EMPTY_MANDATORY_PARAM,key), ResponseCode.CLIENT_ERROR.getCode());
        }
    }
}
