package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.inject.Inject;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sunbird.Application;
import org.sunbird.BaseException;
import org.sunbird.message.Localizer;
import org.sunbird.request.Request;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import utils.JsonKey;
import utils.RequestMapper;
import utils.RequestValidatorFunction;
import validators.IRequestValidator;

/**
 * This controller we can use for writing some common method to handel api request.
 * CompletableFuture: A Future that may be explicitly completed (setting its value and status), and
 * may be used as a CompletionStage, supporting dependent functions and actions that trigger upon
 * its completion. CompletionStage: A stage of a possibly asynchronous computation, that performs an
 * action or computes a value when another CompletionStage completes
 *
 * @author Anmol
 */
public class BaseController extends Controller {

    protected static ObjectMapper mapper = new ObjectMapper();
    /**
     * We injected HttpExecutionContext to decrease the response time of APIs.
     */
    @Inject
    private HttpExecutionContext httpExecutionContext;
    protected static Localizer localizerObject = Localizer.getInstance();

    /**
     * This method will return the current timestamp.
     *
     * @return long
     */
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    protected ActorRef getActorRef(String operation) throws BaseException {
        return Application.getInstance().getActorRef(operation);
    }

    /**
     * this method will take org.sunbird.Request and a validation function and lastly operation(Actor operation)
     * this method is validating the request and ,
     * this method is used to handle all the request type which has requestBody
     *
     * @param request
     * @param requestValidator
     * @param operation
     * @return
     */
    public CompletionStage<Result> handleRequest(Request request, IRequestValidator requestValidator, String operation) {
        try {
            if (requestValidator != null) {
                requestValidator.validate(request);
            }
            return new RequestHandler().handleRequest(request, httpExecutionContext, operation);
        } catch (BaseException ex) {
            return RequestHandler.handleFailureResponse(ex, httpExecutionContext);
        } catch (Exception ex) {
            return RequestHandler.handleFailureResponse(ex, httpExecutionContext);
        }
    }

    /**
     * this method will take play.mv.http request and a validation function and lastly operation(Actor operation)
     * this method is validating the request and ,
     * it will map the request to our sunbird Request class and make a call to requestHandler which is internally calling ask to actor
     * this method is used to handle all the request type which has requestBody
     *
     * @param req
     * @param requestValidator
     * @param operation
     * @return
     */
    public CompletionStage<Result> handleRequest(play.mvc.Http.Request req, IRequestValidator requestValidator, String operation) {
        try {
            Request request = (Request) RequestMapper.mapRequest(req, Request.class);
            if (requestValidator != null) {
                requestValidator.validate(request);
            }
            return new RequestHandler().handleRequest(request, httpExecutionContext, operation);
        } catch (BaseException ex) {
            return RequestHandler.handleFailureResponse(ex, httpExecutionContext);
        } catch (Exception ex) {
            return RequestHandler.handleFailureResponse(ex, httpExecutionContext);
        }
    }

    /**
     * this method is used to handle the only GET requests.
     *
     * @param req
     * @param operation
     * @return
     */
    public CompletionStage<Result> handleRequest(Request req, String operation) {
        try {
            return new RequestHandler().handleRequest(req, httpExecutionContext, operation);
        } catch (BaseException ex) {
            return RequestHandler.handleFailureResponse(ex, httpExecutionContext);
        } catch (Exception ex) {
            return RequestHandler.handleFailureResponse(ex, httpExecutionContext);
        }
    }



    public CompletionStage<Result> handleRequest() {
        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.complete("helloOk");
        return cf.thenApplyAsync(Results::ok);
    }

    /**
     * This method is responsible to convert Response object into json
     *
     * @param response
     * @return string
     */
    public static String jsonify(Object response) {
        try {
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return JsonKey.EMPTY_STRING;
        }
    }
}
