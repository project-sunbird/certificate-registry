package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.HeaderParam;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import utils.module.OnRequestHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@PrepareForTest({OnRequestHandler.class,BaseController.class})
public class CertificateControllerTest extends BaseApplicationTest {

    public static Map<String, List<String>> headerMap;
    @Before
    public void before() {
        setup(DummyActor.class);
        headerMap = new HashMap<>();
        headerMap.put(HeaderParam.X_Consumer_ID.getName(), Arrays.asList("Some consumer ID"));
        headerMap.put(HeaderParam.X_Device_ID.getName(), Arrays.asList("Some device ID"));
        headerMap.put(
                HeaderParam.X_Authenticated_Userid.getName(), Arrays.asList("Some authenticated user ID"));
        headerMap.put(HeaderParam.X_APP_ID.getName(), Arrays.asList("Some app Id"));
    }

   // @Test
    public void testAddCertificateSuccess() {
        Result result =
                performTest(
                        "/certs/v1/registry/add",
                        "POST",
                        createCertRequest());
        assertEquals(getResponseCode(result), ResponseCode.getResponseCode(200));
    }

    private Map<String,Object> createCertRequest() {
        Map<String,Object> reqMap = new HashMap<>();
        Map<String,Object> innerMap = new HashMap<>();
        innerMap.put("recipientId", "b69b056d-1d7d-47a9-a3b6-584fce840fd3");
        innerMap.put("recipientName", "Akash Kumar");
        innerMap.put("id", "id5dd4");
        innerMap.put("accessCode", "EANAB13");
        Map<String,Object> jsonData = new HashMap<>();
        jsonData.put("id", "http://localhost:8080/_schemas/Certificate/d5a28280-98ac-4294-a508-21075dc7d475");
        jsonData.put("type",new String[]{"Assertion","Extension"});
        jsonData.put("issuedOn", "2019-08-31T12:52:25Z");
        Map<String,Object> recipient = new HashMap<>();
        recipient.put("identity", "ntptest103");
        recipient.put("name", "Aishwarya");
        jsonData.put("recipient", recipient);
        Map<String,Object> badge = new HashMap<>();
        badge.put("id", "http://localhost:8080/_schemas/Badge.json");
        badge.put("name", "Sunbird installation");
        badge.put("description", "Certificate of Appreciation in National Level ITI Grading");
        jsonData.put("badge",badge);
        innerMap.put("pdfUrl", "djdjd/r01284093466818969624/275acee4-1964-4986-9dc3-06e08e9a1aa0.pdf");
        Map<String,Object> related = new HashMap<>();
        related.put("courseId", "course-idsd");
        related.put("type", "hell");
        related.put("batchId", "batchId");
        related.put("introUrl", "intro url");
        related.put("completionUrl", "completionUrl");
        innerMap.put("related", related);
        innerMap.put("jsonData", jsonData);
        reqMap.put("request",innerMap);
        return reqMap;
    }

    public Result performTest(String url, String method, Map map) {
        String data = mapToJson(map);
        Http.RequestBuilder req;
        if (StringUtils.isNotBlank(data)) {
            JsonNode json = Json.parse(data);
            req = new Http.RequestBuilder().bodyJson(json).uri(url).method(method);
        } else {
            req = new Http.RequestBuilder().uri(url).method(method);
        }
        //req.headers(new Http.Headers(headerMap));
        Result result = Helpers.route(application, req);
        return result;
    }

    public String mapToJson(Map map) {
        ObjectMapper mapperObj = new ObjectMapper();
        String jsonResp = "";

        if (map != null) {
            try {
                jsonResp = mapperObj.writeValueAsString(map);
            } catch (IOException e) {
            }
        }
        return jsonResp;
    }

}
