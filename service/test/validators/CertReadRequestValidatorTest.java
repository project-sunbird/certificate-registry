package validators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.request.Request;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CertReadRequestValidatorTest {
    private Request request;
    private IRequestValidator requestValidator;
    private Map<String, Object> reqMap;


    @Before
    public void setUp(){
        reqMap= new HashMap<>();
        request = new Request();
        request.setRequest(reqMap);
        requestValidator=new CertReadRequestValidator();
    }

    @After
    public void tearDown(){
        requestValidator=null;
        reqMap.clear();
        request=null;
    }
    @Test(expected = BaseException.class)
    public void testReadWhenMandatoryParamMissing() throws BaseException {
        requestValidator.validate(request);
    }

    @Test(expected = Test.None.class)
    public void testReadWhenMandatoryParamIsPresent() throws BaseException {
        reqMap.put(JsonKeys.ID,"anyMockId");
        request.setRequest(reqMap);
        requestValidator.validate(request);

    }
}