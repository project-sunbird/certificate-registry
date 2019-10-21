package org.sunbird;

import org.apache.log4j.Logger;
import org.sunbird.actor.core.ActorConfig;
import org.sunbird.request.Request;
import org.sunbird.response.Response;
import org.sunbird.service.ICertService;
import org.sunbird.serviceimpl.CertsServiceImpl;

@ActorConfig(
        tasks = {"add","validate","download","generate","verify"},
        dispatcher = "",
        asyncTasks = {}
)
public class Certification extends BaseActor {
    static Logger logger = Logger.getLogger(Certification.class);
    private static ICertService certService = new CertsServiceImpl();

    @Override
    public void onReceive(Request request) throws BaseException {
        logger.info("Certification:onReceive:request arrived with operation" + request.getOperation());
        if (ActorOperations.ADD.getOperation().equalsIgnoreCase(request.getOperation())) {
            add(request);
        } else if (ActorOperations.VALIDATE.getOperation().equalsIgnoreCase(request.getOperation()))
        {
            validate(request);
        }
        else if(ActorOperations.DOWNLOAD.getOperation().equalsIgnoreCase(request.getOperation()))
        {
            download(request);
        }
        else if(ActorOperations.GENERATE.getOperation().equalsIgnoreCase(request.getOperation()))
        {
            generate(request);
        }
        else if(ActorOperations.VERIFY.getOperation().equalsIgnoreCase(request.getOperation()))
        {
            verify(request);
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
