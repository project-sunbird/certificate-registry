package validators;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sunbird.BaseException;
import org.sunbird.request.Request;
import utils.JsonKey;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CertVerifyRequestValidatorTest {

    private Request request;
    private IRequestValidator requestValidator;
    private Map<String, Object> reqMap;


    @Before
    public void setUp(){
        reqMap= new HashMap<>();
        request = new Request();
        request.setRequest(reqMap);
       requestValidator=new CertVerifyRequestValidator();
    }

    @After
    public void tearDown(){
        requestValidator=null;
        reqMap.clear();
        request=null;
    }

    @Test(expected = BaseException.class)
    public void testVerifyWhenMandatoryParamMissing() throws BaseException {
        requestValidator.validate(request);
    }
}