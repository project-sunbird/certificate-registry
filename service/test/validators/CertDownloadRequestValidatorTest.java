package validators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.request.Request;
import utils.JsonKey;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CertDownloadRequestValidatorTest {

    private Request request;
    private IRequestValidator requestValidator;
    private Map<String, Object> reqMap;
    private Map<String, Object> reqContext;

    @Before
    public void setUp(){
        reqMap= new HashMap<>();
        request = new Request();
        request.setRequest(reqMap);
        reqContext = new HashMap<>();
        reqContext.put(JsonKeys.VERSION, JsonKeys.VERSION_1);
        request.setContext(reqContext);
        requestValidator=new CertDownloadRequestValidator();
    }

    @After
    public void tearDown(){
        requestValidator=null;
        reqMap.clear();
        request=null;
    }
    @Test(expected = BaseException.class)
    public void testValidateWhenMandatoryParamMissing() throws BaseException {
        requestValidator.validate(request);
    }

    @Test(expected = Test.None.class)
    public void testValidateWhenMandatoryParamIsPresent() throws BaseException {
        reqMap.put(JsonKeys.PDF_URL,"/pdf_uRL");
        request.setRequest(reqMap);
        requestValidator.validate(request);

    }

    @Test(expected = BaseException.class)
    public void testValidateWhenMandatoryParamMissingV2() throws BaseException {
        request.setRequest(reqMap);
        request.getContext().put(JsonKeys.VERSION, JsonKeys.VERSION_2);
        requestValidator.validate(request);

    }
}