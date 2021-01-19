package org.sunbird;

import akka.actor.UntypedAbstractActor;
import org.sunbird.message.IResponseMessage;
import org.sunbird.message.Localizer;
import org.sunbird.message.ResponseCode;
import org.sunbird.request.Request;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Amit Kumar
 */
public abstract class BaseActor extends UntypedAbstractActor {
    public LoggerUtil logger = new LoggerUtil(this.getClass());
    public abstract void onReceive(Request request) throws Throwable;
    protected Localizer localizer = Localizer.getInstance();

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof Request) {
            Request request = (Request) message;
            Map<String, Object> trace = new HashMap<>();
            if (request.getHeaders().containsKey(JsonKeys.REQUEST_MESSAGE_ID)) {
                ArrayList<String> requestIds =
                        (ArrayList<String>) request.getHeaders().get(JsonKeys.REQUEST_MESSAGE_ID);
                trace.put(JsonKeys.REQUEST_MESSAGE_ID, requestIds.get(0));
            }
            String operation = request.getOperation();
            logger.info(request.getRequestContext(), "onReceive called for operation: {}", operation);
            try {
                logger.info(request.getRequestContext(), "onReceive:method {} started at {}", operation);
                onReceive(request);
                logger.info(request.getRequestContext(), "onReceive:method {} ended at {}", operation);
            } catch (Exception e) {
                logger.error(request.getRequestContext(), e.getMessage(), e);
                onReceiveException(request, operation, e);
            }
        } else {
            logger.info(null, "onReceive called with invalid type of request.");
        }
    }

    /**
     * this method will handle the exception
     * @param callerName
     * @param exception
     * @throws Exception
     */
    protected void onReceiveException(Request request, String callerName, Exception exception) throws Exception {
        logger.error(request.getRequestContext(), "Exception in message processing for: " + callerName + " :: message: " + exception.getMessage(), exception);
        sender().tell(exception, self());
    }


    /**
     * this message will handle the unsupported actor operation
     * @param callerName
     */
    protected void onReceiveUnsupportedMessage(RequestContext requestContext, String callerName) {
        logger.info(requestContext, callerName + ": unsupported operation");
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
