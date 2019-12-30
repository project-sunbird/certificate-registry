package org.sunbird.utilities;

import akka.actor.ActorRef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.sunbird.ActorOperations;
import org.sunbird.Application;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.common.ElasticSearchHelper;
import org.sunbird.common.factory.EsClientFactory;
import org.sunbird.common.inf.ElasticSearchService;
import org.sunbird.dto.SearchDTO;
import org.sunbird.helper.ServiceFactory;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.Localizer;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;
import org.sunbird.response.Response;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * this class will be used to add , retrive certificates from ES
 * @author anmolgupta
 */
public class CertificateUtil {
    private static final ElasticSearchService elasticSearchService= EsClientFactory.getInstance();
    private static final CassandraOperation cassandraOperation = ServiceFactory.getInstance();
    private static Logger logger=Logger.getLogger(CertificateUtil.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private static Localizer localizer = Localizer.getInstance();

    public static boolean isIdPresent(String certificateId) {
        logger.info("CertificateUtil:isIdPresent:get id to search in ES:"+certificateId);
        Map<String,Object> response = (Map)ElasticSearchHelper.getResponseFromFuture(elasticSearchService.getDataByIdentifier(JsonKeys.CERT,certificateId));
        logger.info("CertificateUtil:isIdPresent:got response from ES:"+response);
        if (MapUtils.isNotEmpty(response)) {
                return true;
        }
        return false;
    }
    public static Response deleteRecord(String id) throws BaseException {
        Response response = cassandraOperation.deleteRecord(JsonKeys.SUNBIRD,JsonKeys.CERT_REGISTRY,id);
        //Delete the data from ES
        Request req = new Request();
        req.setOperation(ActorOperations.DELETE_CERT_ES.getOperation());
        req.getRequest().put(JsonKeys.ID,id);
        Application.getInstance().getActorRef(ActorOperations.DELETE_CERT_ES.getOperation()).tell(req, ActorRef.noSender());
        return response;
    }


    public static Response insertRecord(Map<String,Object>certAddReqMap) throws BaseException {
        Map<String,Object>certMap = new HashMap<>();
        long createdAt = System.currentTimeMillis();
        certAddReqMap.put(JsonKeys.CREATED_AT,createdAt);
        certAddReqMap.put(JsonKeys.UPDATED_AT,null);
        certMap.putAll(certAddReqMap);

        try{
        certMap.put(JsonKeys.CREATED_AT,new Timestamp(createdAt));
        certMap.put(JsonKeys.DATA,mapper.writeValueAsString(certAddReqMap.get(JsonKeys.DATA)));
        certMap.put(JsonKeys.RELATED,mapper.writeValueAsString(certAddReqMap.get(JsonKeys.RELATED)));
        certMap.put(JsonKeys.RECIPIENT,mapper.writeValueAsString(certAddReqMap.get(JsonKeys.RECIPIENT)));
        } catch (Exception ex) {
            logger.error("CertificateUtil:insertRecord: JsonProcessingException occurred.",ex);
            throw new BaseException(IResponseMessage.INVALID_REQUESTED_DATA,getLocalizedMessage(IResponseMessage.INVALID_REQUESTED_DATA,null), ResponseCode.CLIENT_ERROR.getCode());
        }
        Response response = cassandraOperation.insertRecord(JsonKeys.SUNBIRD,JsonKeys.CERT_REGISTRY,certMap);
        logger.info("CertificateUtil:insertRecord: record successfully inserted with id"+certAddReqMap.get(JsonKeys.ID));
        //index data to ES
        Request req = new Request();
        req.setOperation(ActorOperations.ADD_CERT_ES.getOperation());
        req.getRequest().put(JsonKeys.REQUEST,certAddReqMap);
        Application.getInstance().getActorRef(ActorOperations.ADD_CERT_ES.getOperation()).tell(req, ActorRef.noSender());
        return response;

    }


    public static  Map<String,Object> getCertificate(SearchDTO searchDTO) {
        logger.info("CertificateUtil:isIdPresent:get id to search in ES:"+searchDTO);
        Map<String,Object> response = (Map)ElasticSearchHelper.getResponseFromFuture(elasticSearchService.search(searchDTO,JsonKeys.CERT));
        logger.info("CertificateUtil:isIdPresent:got response from ES:"+response);
        return response;
    }
    public static  Map<String,Object> getCertificate(String certificateId) {
        logger.info("CertificateUtil:isIdPresent:get id to search in ES:"+certificateId);
        Map<String,Object> response = (Map)ElasticSearchHelper.getResponseFromFuture(elasticSearchService.getDataByIdentifier(JsonKeys.CERT,certificateId));
        logger.info("CertificateUtil:isIdPresent:got response from ES:"+response);
        return response;
    }

    public static Future<HttpResponse<JsonNode>> makeAsyncPostCall(String apiToCall,String requestBody,Map<String,String>headerMap){
        logger.info("CertificateUtil:makePostCall:get request to make post call for API:"+apiToCall+":"+requestBody);
        Future<HttpResponse<JsonNode>> jsonResponse
                    = Unirest.post(apiToCall)
                    .headers(headerMap)
                    .body(requestBody)
                    .asJsonAsync();
            return jsonResponse;
        }

    public static SimpleDateFormat getDateFormatter() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSZ");
        simpleDateFormat.setLenient(false);
        return simpleDateFormat;
    }

    private static String getLocalizedMessage(String key, Locale locale){
        return localizer.getMessage(key, locale);
    }
}
