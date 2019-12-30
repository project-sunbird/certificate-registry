package org.sunbird.actor;

import org.apache.log4j.Logger;
import org.sunbird.BaseActor;
import org.sunbird.JsonKeys;
import org.sunbird.actor.core.ActorConfig;
import org.sunbird.common.ElasticSearchHelper;
import org.sunbird.common.factory.EsClientFactory;
import org.sunbird.common.inf.ElasticSearchService;
import org.sunbird.request.Request;

import java.util.Map;

@ActorConfig(
        tasks = {"add_cert_es"},
        dispatcher = "",
        asyncTasks = {}
)
public class CertBackgroundActor extends BaseActor {
    static Logger logger = Logger.getLogger(CertBackgroundActor.class);
    private ElasticSearchService elasticSearchService = getESService();
    private static ElasticSearchService getESService(){
        return EsClientFactory.getInstance();
    }
    @Override
    public void onReceive(Request request) throws Throwable {
        logger.info("CertificationActor:onReceive:request arrived with operation" + request.getOperation());
        String operation = request.getOperation();
        switch (operation) {
            case "add_cert_es":
                add(request);
                break;

            default:
                onReceiveUnsupportedMessage("CertificationActor");
        }
    }

    private void add(Request request) {
        Map<String,Object> certAddReqMap = (Map<String, Object>) request.getRequest().get(JsonKeys.REQUEST);
        String id = (String)ElasticSearchHelper.getResponseFromFuture(elasticSearchService.save(JsonKeys.CERT,(String)certAddReqMap.get(JsonKeys.ID),certAddReqMap));
        logger.info("ES save response for id "+id);
    }
}