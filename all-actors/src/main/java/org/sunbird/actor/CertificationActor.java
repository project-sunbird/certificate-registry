package org.sunbird.actor;

import akka.dispatch.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.sunbird.BaseActor;
import org.sunbird.BaseException;
import org.sunbird.JsonKeys;
import org.sunbird.actor.core.ActorConfig;
import org.sunbird.common.factory.EsClientFactory;
import org.sunbird.common.inf.ElasticSearchService;
import org.sunbird.dto.SearchDTO;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;
import org.sunbird.response.Response;
import org.sunbird.service.ICertService;
import org.sunbird.serviceimpl.CertsServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;

@ActorConfig(
        tasks = {"add","validate","download","generate","verify"},
        dispatcher = "",
        asyncTasks = {}
)
public class CertificationActor extends BaseActor {
    static Logger logger = Logger.getLogger(CertificationActor.class);
    private ICertService certService = getCertServiceImpl();

    private ICertService getCertServiceImpl(){
        return new CertsServiceImpl();
    }

    @Override
    public void onReceive(Request request) throws BaseException {
        logger.info("CertificationActor:onReceive:request arrived with operation" + request.getOperation());
        String operation = request.getOperation();
        switch (operation) {
            case "add" :
                add(request);
                break;

            case  "validate" :
                validate(request);
                break;

            case "download" :
                download(request);
                break;

            case "generate" :
                generate(request);
                break;

            case "verify" :
                verify(request);
                break;

            default:
                onReceiveUnsupportedMessage("CertificationActor");
        }
    }

    private void add(Request request) throws BaseException {
        String id = certService.add(request);
        Response response = new Response();
        response.put(JsonKeys.ID, id);
        sender().tell(response, self());
    }

    private void validate(Request request) throws BaseException {
        sender().tell(certService.validate(request), self());
    }

    private void download(Request request) throws BaseException
    {
        sender().tell(certService.download(request),self());

    }

    private void generate(Request request) throws BaseException {
        sender().tell(certService.generate(request),self());
    }

    private void verify(Request request) throws BaseException{
        sender().tell(certService.verify(request),self());
    }
}
