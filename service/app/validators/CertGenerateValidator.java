package validators;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;
import utils.StorageType;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class contains method to validate certificate api request
 * @author anmolgupta
 */
public class CertGenerateValidator implements IRequestValidator{

    private Request request;
    @Override
    public void validate(Request request) throws BaseException {
        this.request=request;
        validateGenerateCertRequest(request);
    }

    /**
     * This method will validate generate certificate request
     *
     * @param request
     * @throws BaseException
     */
    public static void validateGenerateCertRequest(Request request) throws BaseException {

        Map<String, Object> certReq = (Map<String, Object>) request.getRequest().get(JsonKeys.CERTIFICATE);
        checkMandatoryParamsPresent(certReq, JsonKeys.CERTIFICATE, Arrays.asList(JsonKeys.COURSE_NAME, JsonKeys.NAME, JsonKeys.HTML_TEMPLATE));
        validateCertData((List<Map<String, Object>>) certReq.get(JsonKeys.DATA));
        validateCertIssuer((Map<String, Object>) certReq.get(JsonKeys.ISSUER));
        validateCertSignatoryList((List<Map<String, Object>>) certReq.get(JsonKeys.SIGNATORY_LIST));
        if(certReq.containsKey(JsonKeys.STORE)) {
            validateStore((Map<String, Object>) certReq.get(JsonKeys.STORE));
        }
        if (certReq.containsKey(JsonKeys.KEYS)) {
            validateKeys((Map<String, Object>) certReq.get(JsonKeys.KEYS));
        }
    }

    private static void validateCertSignatoryList(List<Map<String, Object>> signatoryList) throws BaseException {
        checkMandatoryParamsPresent(signatoryList, JsonKeys.CERTIFICATE + "." + JsonKeys.SIGNATORY_LIST, Arrays.asList(JsonKeys.NAME, JsonKeys.ID, JsonKeys.DESIGNATION, JsonKeys.SIGNATORY_IMAGE));
    }

    private static void validateCertIssuer(Map<String, Object> issuer) throws BaseException {
        checkMandatoryParamsPresent(issuer, JsonKeys.CERTIFICATE + "." + JsonKeys.ISSUER, Arrays.asList(JsonKeys.NAME, JsonKeys.URL));
    }

    private static void validateCertData(List<Map<String, Object>> data) throws BaseException {
        checkMandatoryParamsPresent(data, JsonKeys.CERTIFICATE + "." + JsonKeys.DATA, Arrays.asList(JsonKeys.RECIPIENT_NAME));
    }

    private static void validateKeys(Map<String, Object> keys) throws BaseException {
        checkMandatoryParamsPresent(keys, JsonKeys.CERTIFICATE + "." + JsonKeys.KEYS, Arrays.asList(JsonKeys.ID));

    }

    private static void checkMandatoryParamsPresent(
            List<Map<String, Object>> data, String parentKey, List<String> keys) throws BaseException {
        if (CollectionUtils.isEmpty(data)) {
            throw new BaseException("MANDATORY_PARAMETER_MISSING",
                    MessageFormat.format(IResponseMessage.MISSING_MANADATORY_PARAMS, parentKey),
                    ResponseCode.CLIENT_ERROR.getCode());
        }
        for (Map<String, Object> map : data) {
            checkChildrenMapMandatoryParams(map, keys, parentKey);
        }

    }

    private static void checkMandatoryParamsPresent(
            Map<String, Object> data, String parentKey, List<String> keys) throws BaseException {
        if (MapUtils.isEmpty(data)) {
            throw new BaseException("MANDATORY_PARAMETER_MISSING",
                    MessageFormat.format(IResponseMessage.MISSING_MANADATORY_PARAMS, parentKey),
                    ResponseCode.CLIENT_ERROR.getCode());
        }
        checkChildrenMapMandatoryParams(data, keys, parentKey);
    }

    private static void checkChildrenMapMandatoryParams(Map<String, Object> data, List<String> keys, String parentKey) throws BaseException {

        for (String key : keys) {
            if (StringUtils.isBlank((String) data.get(key))) {
                throw new BaseException("MANDATORY_PARAMETER_MISSING",
                        MessageFormat.format(IResponseMessage.MISSING_MANADATORY_PARAMS, parentKey + "." + key),
                        ResponseCode.CLIENT_ERROR.getCode());
            }
        }
    }

    private static void validateStore(Map<String, Object> store)  throws   BaseException{
        checkMandatoryParamsPresent(store, JsonKeys.CERTIFICATE + "." + JsonKeys.STORE, Arrays.asList(JsonKeys.TYPE));
        validateStorageType(store, JsonKeys.CERTIFICATE + "." + JsonKeys.STORE);
        checkMandatoryParamsPresent((Map<String, Object>)store.get(store.get(JsonKeys.TYPE)), JsonKeys.CERTIFICATE + "." + JsonKeys.STORE + "."
                + store.get(JsonKeys.TYPE), Arrays.asList(JsonKeys.containerName, JsonKeys.ACCOUNT, JsonKeys.key));
    }

    private static void validateStorageType(Map<String, Object> data, String parentKey) throws BaseException {
        if(!StorageType.get().contains(data.get(JsonKeys.TYPE)))  {
            throw new BaseException("INVALID_PARAM_VALUE",
                    MessageFormat.format(IResponseMessage.INVALID_REQUESTED_DATA, data.get(JsonKeys.TYPE), parentKey + "." + JsonKeys.TYPE),
                    ResponseCode.CLIENT_ERROR.getCode());
        }
    }


}

