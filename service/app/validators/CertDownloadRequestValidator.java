package validators;

import org.apache.commons.lang3.StringUtils;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.Localizer;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * this is a validator class for downloading certificate
 * @author anmolgupta
 */
public class CertDownloadRequestValidator implements IRequestValidator {

    private Localizer localizer = Localizer.getInstance();
    private Request request;
    @Override
    public void validate(Request request) throws BaseException {
        this.request=request;
        if(request.getContext().get(JsonKeys.VERSION).equals(JsonKeys.VERSION_2)){
            validateDownloadV2FileData();
        }else {
            validateDownlaodFileData();
        }
    }


    private void validateDownlaodFileData() throws BaseException {
        if (StringUtils.isBlank((String)this.request.getRequest().get(JsonKeys.PDF_URL))) {
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(getLocalizedMessage(IResponseMessage.MISSING_MANDATORY_PARAMS,null), JsonKeys.PDF_URL), ResponseCode.CLIENT_ERROR.getCode());
        }
    }

    private void validateDownloadV2FileData() throws BaseException {
        if (StringUtils.isBlank((String)this.request.getRequest().get(JsonKeys.PDF_URL)) && StringUtils.isBlank((String)this.request.getRequest().get(JsonKeys.CERT_URL))) {
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA
                    , getLocalizedMessage(IResponseMessage.INVALID_REQUESTED_DATA,null)
                    , ResponseCode.CLIENT_ERROR.getCode());
        }
    }

    private String getLocalizedMessage(String key, Locale locale){
        return localizer.getMessage(key, locale);
    }
}
