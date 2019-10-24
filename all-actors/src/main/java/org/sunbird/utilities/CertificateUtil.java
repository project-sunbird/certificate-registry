package org.sunbird.utilities;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.sunbird.JsonKeys;
import org.sunbird.common.ElasticSearchHelper;
import org.sunbird.common.factory.EsClientFactory;
import org.sunbird.common.inf.ElasticSearchService;
import org.sunbird.dto.SearchDTO;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * this class will be used to add , retrive certificates from ES
 * @author anmolgupta
 */
public class CertificateUtil {
    private static ElasticSearchService elasticSearchService= EsClientFactory.getInstance();
    static Logger logger=Logger.getLogger(CertificateUtil.class);

    public static boolean isIdPresent(String certificateId) {
        logger.info("CertificateUtil:isIdPresent:get id to search in ES:"+certificateId);
        Map<String,Object> response = (Map)ElasticSearchHelper.getResponseFromFuture(elasticSearchService.getDataByIdentifier(JsonKeys.CERT,certificateId));
        logger.info("CertificateUtil:isIdPresent:got response from ES:"+response);
        if (MapUtils.isNotEmpty(response)) {
                return true;
        }
        return false;
    }


    public static void insertRecord(Map<String,Object>certAddReqMap){
        ElasticSearchHelper.getResponseFromFuture(elasticSearchService.save(JsonKeys.CERT,(String)certAddReqMap.get(JsonKeys.ID),certAddReqMap));
        logger.info("CertificateUtil:insertRecord: record successfully inserted with id"+certAddReqMap.get(JsonKeys.ID));
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
}
