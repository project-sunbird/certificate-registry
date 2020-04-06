package org.sunbird;

import akka.actor.UntypedAbstractActor;
import org.apache.log4j.Logger;
import org.sunbird.common.factory.EsClientFactory;
import org.sunbird.common.inf.ElasticSearchService;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.Localizer;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;
import org.sunbird.service.ICertService;
import org.sunbird.serviceimpl.CertsServiceImpl;

import java.util.Locale;

/**
 * @author Amit Kumar
 */
public abstract class BaseActor extends UntypedAbstractActor {
    private Logger logger = Logger.getLogger(BaseActor.class);
    public abstract void onReceive(Request request) throws Throwable;
    protected Localizer localizer = Localizer.getInstance();

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Request) {
            Request request = (Request) message;
            String operation = request.getOperation();
            logger.info("BaseActor:onReceive called for operation:" + operation);
            try {
                logger.info("BaseActor:onReceive:method started at"+System.currentTimeMillis());
                onReceive(request);
                logger.info("BaseActor:onReceive:method ended at"+System.currentTimeMillis());
            } catch (Exception e) {
                onReceiveException(operation, e);
            }
        } else {
            logger.info("BaseActor: onReceive called with invalid type of request.");
        }
    }

    /**
     * this method will handle the exception
     * @param callerName
     * @param exception
     * @throws Exception
     */
    protected void onReceiveException(String callerName, Exception exception) throws Exception {
        logger.error("Exception in message processing for: " + callerName + " :: message: " + exception.getMessage(), exception);
        sender().tell(exception, self());
    }


    /**
     * this message will handle the unsupported actor operation
     * @param callerName
     */
    protected void onReceiveUnsupportedMessage(String callerName) {
        logger.info(callerName + ": unsupported operation");
        /**
         * TODO Need to replace null reference from getLocalized method and replace with requested local.
         */
        BaseException exception =
                new ActorServiceException.InvalidOperationName(
                        IResponseMessage.INVALID_OPERATION_NAME,
                        getLocalizedMessage(IResponseMessage.INVALID_OPERATION_NAME,null),
                        ResponseCode.CLIENT_ERROR.getCode());
        sender().tell(exception, self());
    }


    /**
     * this is method is used get message in different different locales
     * @param key
     * @param locale
     * @return
     */

    protected String getLocalizedMessage(String key, Locale locale){
        return localizer.getMessage(key, locale);
    }

    /**
     * This method will return the current timestamp.
     *
     * @return long
     */
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

}
