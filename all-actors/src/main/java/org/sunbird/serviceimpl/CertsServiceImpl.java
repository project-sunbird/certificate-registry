package org.sunbird.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.sunbird.BaseException;
import org.sunbird.CertVars;
import org.sunbird.JsonKeys;
import org.sunbird.builders.Certificate;
import org.sunbird.builders.Recipient;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;
import org.sunbird.response.Response;
import org.sunbird.service.ICertService;
import org.sunbird.utilities.CertificateUtil;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * this is an implementaion class for the cert service , it will handel all the certificate related transactions
 * @author anmolgupta
 */
public class CertsServiceImpl implements ICertService {
    static Logger logger = Logger.getLogger(CertsServiceImpl.class);
    private static ObjectMapper requestMapper = new ObjectMapper();
    static Map<String, String> headerMap = new HashMap<>();
    static {
        headerMap.put("Content-Type", "application/json");
    }

    @Override
    public String add(Request request) throws BaseException {
        Map<String, Object> certAddReqMap = request.getRequest();
        assureUniqueCertId((String) certAddReqMap.get(JsonKeys.ID));
        processRecord(certAddReqMap);
        logger.info("CertsServiceImpl:add:record successfully processed with request:"+certAddReqMap);
        return (String)certAddReqMap.get(JsonKeys.ID);
    }

    private void assureUniqueCertId(String certificatedId) throws BaseException {
        if (CertificateUtil.isIdPresent(certificatedId)) {
            logger.error(
                    "CertificateActor:addCertificate:provided certificateId exists in record:" + certificatedId);
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, IResponseMessage.ID_ALREADY_EXISTS, ResponseCode.CLIENT_ERROR.getCode());
        }
        logger.info("CertificateActor:addCertificate:successfully certId not found in records creating new record");
    }


    private void processRecord(Map<String, Object> certReqAddMap){
        Certificate certificate=getCertificate(certReqAddMap);
        Map<String,Object>recordMap= requestMapper.convertValue(certificate,Map.class);
        recordMap.put(JsonKeys.CREATED_AT,System.currentTimeMillis());
        recordMap.put(JsonKeys.UPDATED_AT,null);
        CertificateUtil.insertRecord(recordMap);
    }
    private Certificate getCertificate(Map<String, Object> certReqAddMap) {
        Certificate certificate = new Certificate.Builder()
                .setId((String) certReqAddMap.get(JsonKeys.ID))
                .setData(getData(certReqAddMap))
                .setPdfUrl((String)certReqAddMap.get(JsonKeys.PDF_URL))
                .setRevoked(false)
                .setAccessCode((String)certReqAddMap.get(JsonKeys.ACCESS_CODE))
                .setJsonUrl((String)certReqAddMap.get(JsonKeys.JSON_URL))
                .setRecipient(getCompositeReciepientObject(certReqAddMap))
                .setRelated((Map)certReqAddMap.get(JsonKeys.RELATED))
                .setReason((String)certReqAddMap.get(JsonKeys.REASON))
                .build();
        logger.info("CertsServiceImpl:getCertificate:certificate object formed: "+certificate);
        return certificate;
    }
    private Recipient getCompositeReciepientObject(Map<String, Object> certAddRequestMap) {
        Recipient recipient = new Recipient.Builder()
                .setName((String) certAddRequestMap.get(JsonKeys.RECIPIENT_NAME))
                .setId((String) certAddRequestMap.get(JsonKeys.RECIPIENT_ID))
                .setType((String) certAddRequestMap.get(JsonKeys.RECIPIENT_TYPE))
                .build();
    return recipient;
    }

    private Map<String, Object> getData(Map<String, Object> certAddRequestMap) {
        return (Map) certAddRequestMap.get(JsonKeys.JSON_DATA);
    }

    @Override
    public Response validate(Request request) throws BaseException {
        Map<String,Object> valCertReq = request.getRequest();
        String certificatedId = (String) valCertReq.get(JsonKeys.CERT_ID);
        String accessCode = (String) valCertReq.get(JsonKeys.ACCESS_CODE);
        Map<String,Object>esCertData=CertificateUtil.getCertificate(certificatedId);
        if (MapUtils.isNotEmpty(esCertData) && StringUtils.equalsIgnoreCase((String)esCertData.get(JsonKeys.ACCESS_CODE),accessCode)) {
            Certificate certificate=getCertObject(esCertData);
            Map<String,Object>responseMap=new HashMap<>();
            responseMap.put(JsonKeys.JSON,certificate.getData());
            responseMap.put(JsonKeys.PDF,certificate.getPdfUrl());
            responseMap.put(JsonKeys.RELATED,certificate.getRelated());
            Response response=new Response();
            response.put(JsonKeys.RESPONSE,responseMap);
            return response;
        }
        else{
            logger.error("NO valid record found with provided certificate Id and accessCode respectively:"+certificatedId+":"+accessCode);
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.INVALID_ID_PROVIDED,certificatedId,accessCode), ResponseCode.CLIENT_ERROR.getCode());
        }

    }
    private Certificate getCertObject(Map<String,Object>esCertMap){
        Certificate certificate=requestMapper.convertValue(esCertMap,Certificate.class);
        return certificate;
    }


    @Override
    public Response download(Request request) throws BaseException {
        Response response = new Response();
        try {
            Map<String, Object> certReqMap = new HashMap<>();
            Map<String,String>requestMap=new HashMap<>();
            requestMap.put(JsonKeys.PDF_URL,(String)request.getRequest().get(JsonKeys.PDF_URL));
            certReqMap.put(JsonKeys.REQUEST,requestMap);
            String requestBody = requestMapper.writeValueAsString(certReqMap);
            logger.info("CertsServiceImpl:download:request body found:" + requestBody);
            String apiToCall = CertVars.getSERVICE_BASE_URL().concat(CertVars.getDOWNLOAD_URI());
            logger.info("CertsServiceImpl:download:complete url found:" + apiToCall);
            Future<HttpResponse<JsonNode>>responseFuture=CertificateUtil.makeAsyncPostCall(apiToCall,requestBody,headerMap);
            HttpResponse<JsonNode> jsonResponse = responseFuture.get();
            if (jsonResponse != null && jsonResponse.getStatus() == HttpStatus.SC_OK) {
                String signedUrl=jsonResponse.getBody().getObject().getJSONObject(JsonKeys.RESULT).getString(JsonKeys.SIGNED_URL);
                response.put(JsonKeys.SIGNED_URL,signedUrl);
            } else {
                throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.INVALID_PROVIDED_URL,(String)request.getRequest().get(JsonKeys.PDF_URL)), ResponseCode.CLIENT_ERROR.getCode());
            }

        } catch (Exception e) {
            logger.error("CertsServiceImpl:download:exception occurred:" + e);
            throw new BaseException(IResponseMessage.INTERNAL_ERROR, IResponseMessage.INTERNAL_ERROR, ResponseCode.SERVER_ERROR.getCode());
        }
        return response;
    }

    @Override
    public Response generate(Request request) throws BaseException {
        Response response = new Response();
        try {
            Map<String, Object> certReqMap = new HashMap<>();
            certReqMap.put(JsonKeys.REQUEST,request.getRequest());
            String requestBody = requestMapper.writeValueAsString(certReqMap);
            logger.info("CertsServiceImpl:generate:request body found:" + requestBody);
            String apiToCall = CertVars.getSERVICE_BASE_URL().concat(CertVars.getGenerateUri());
            logger.info("CertsServiceImpl:generate:complete url found:" + apiToCall);
            Future<HttpResponse<JsonNode>>responseFuture=CertificateUtil.makeAsyncPostCall(apiToCall,requestBody,headerMap);
            HttpResponse<JsonNode> jsonResponse = responseFuture.get();
            if (jsonResponse != null && jsonResponse.getStatus() == HttpStatus.SC_OK) {
                String stringifyResponse=jsonResponse.getBody().getObject().getJSONObject(JsonKeys.RESULT).get(JsonKeys.RESPONSE).toString();
                List<Map<String,Object>> apiRespList=requestMapper.readValue(stringifyResponse,List.class);
                response.put(JsonKeys.RESPONSE,apiRespList);
            } else {
                throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA, MessageFormat.format(IResponseMessage.INVALID_PROVIDED_URL,"2222"), ResponseCode.CLIENT_ERROR.getCode());
            }

        } catch (Exception e) {
            logger.error("CertsServiceImpl:generate:exception occurred:" + e);
            throw new BaseException(IResponseMessage.INTERNAL_ERROR, IResponseMessage.INTERNAL_ERROR, ResponseCode.SERVER_ERROR.getCode());
        }
        return response;
    }

    @Override
    public Response verify(Request request) throws BaseException {
        Response response = new Response();
        try {
            Map<String, Object> certVerifyReqMap = new HashMap<>();
            certVerifyReqMap.put(JsonKeys.REQUEST,composeCertVerifyRequest(request));
            String requestBody = requestMapper.writeValueAsString(certVerifyReqMap);
            logger.info("CertsServiceImpl:verify:request body prepared:" + requestBody);
            String apiToCall = CertVars.getSERVICE_BASE_URL().concat(CertVars.getVerifyUri());
            logger.info("CertsServiceImpl:verify:complete url prepared:" + apiToCall);
            Future<HttpResponse<JsonNode>>responseFuture=CertificateUtil.makeAsyncPostCall(apiToCall,requestBody,headerMap);
            HttpResponse<JsonNode> jsonResponse = responseFuture.get();
            if (jsonResponse != null && jsonResponse.getStatus() == HttpStatus.SC_OK) {
                String stringifyResponse=jsonResponse.getBody().getObject().getJSONObject(JsonKeys.RESULT).get(JsonKeys.RESPONSE).toString();
                Map<String,Object> apiResp=requestMapper.readValue(stringifyResponse,Map.class);
                response.put(JsonKeys.RESPONSE,apiResp);
            }
            else if(jsonResponse!=null && jsonResponse.getStatus() == HttpStatus.SC_BAD_REQUEST){
                String stringifyResponse=jsonResponse.getBody().getObject().getJSONObject(JsonKeys.RESULT).get(JsonKeys.MESSAGE).toString();
                throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA,stringifyResponse, ResponseCode.CLIENT_ERROR.getCode());
            }
            else {
                throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA,IResponseMessage.INVALID_REQUESTED_DATA, ResponseCode.CLIENT_ERROR.getCode());
            }

        } catch (Exception e) {
            logger.error("CertsServiceImpl:verify:exception occurred:" + e);
            throw new BaseException(IResponseMessage.INTERNAL_ERROR, e.getMessage(), ResponseCode.SERVER_ERROR.getCode());
        }
        return response;
    }

    private Map<String,Object> composeCertVerifyRequest(Request request){
        Map<String,Object>certificate=new HashMap<>();
        Map<String,Object>certVerifyMap=new HashMap<>();
        if(MapUtils.isNotEmpty((Map)request.getRequest().get(JsonKeys.DATA))) {
            certificate.put(JsonKeys.DATA, request.getRequest().get(JsonKeys.DATA));
        }
        if(StringUtils.isNotBlank((String)request.getRequest().get(JsonKeys.ID))) {
            certificate.put(JsonKeys.ID, request.getRequest().get(JsonKeys.ID));
        }
        certVerifyMap.put(JsonKeys.CERTIFICATE,certificate);
        return certVerifyMap;
    }
}