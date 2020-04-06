package validators;

import org.apache.commons.lang3.StringUtils;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.Localizer;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;

import java.text.MessageFormat;

/**
 * this class will be used to validate the cert read API request.
 * @author anmolgupta
 */
public class CertReadRequestValidator implements IRequestValidator {

    private Localizer localizer = Localizer.getInstance();
    private Request request;

    @Override
    public void validate(Request request) throws BaseException {
        this.request=request;
        validateId();
    }

    private void validateId() throws BaseException {
        String id=(String)request.getRequest().get(JsonKeys.ID);
        if(StringUtils.isBlank(id)){
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(localizer.getMessage(IResponseMessage.MISSING_MANDATORY_PARAMS,null),JsonKeys.ID), ResponseCode.CLIENT_ERROR.getCode());
        }}
}
