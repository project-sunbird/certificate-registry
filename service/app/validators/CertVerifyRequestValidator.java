package validators;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;

import java.text.MessageFormat;
import java.util.Map;

/**
 * this class will be used to validate the cert verify API request.
 * @author anmolgupta
 */
public class CertVerifyRequestValidator implements IRequestValidator {

    private boolean error=false;
    private Request request;

    @Override
    public void validate(Request request) throws BaseException {
        this.request=request;
        validateData();
        validateId();
    }


    private void validateData(){
        Map<String, Object> dataMap = (Map) request.getRequest().get(JsonKeys.DATA);
        if (!(request.getRequest().get(JsonKeys.DATA) instanceof Map)) {
            error=true;
        }
        if(MapUtils.isEmpty(dataMap)){
            error=true;
        }
    }


    private void validateId() throws BaseException {
        String id=(String)request.getRequest().get(JsonKeys.ID);
        if(StringUtils.isBlank(id) && error){
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.MISSING_MANADATORY_PARAMS,"either:"+JsonKeys.DATA+":or:"+JsonKeys.ID), ResponseCode.CLIENT_ERROR.getCode());
        }}
}
