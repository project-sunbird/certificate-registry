package validators;

import org.apache.commons.collections.MapUtils;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;

import java.text.MessageFormat;

public class CertSearchRequestValidator implements IRequestValidator {
    @Override
    public void validate(Request request) throws BaseException {

        if(MapUtils.isEmpty(request.getRequest())){
            throw new BaseException("MANDATORY_PARAMETER_MISSING",
                    MessageFormat.format(IResponseMessage.MISSING_MANDATORY_PARAMS, JsonKeys.REQUEST),
                    ResponseCode.CLIENT_ERROR.getCode());
        }
    }
}
