package controllers;

import org.sunbird.JsonKeys;
import play.mvc.Result;
import validators.*;

import java.util.concurrent.CompletionStage;

/**
 * this controller will help you in understanding the process of passing request to Actors with operation.
 * @author anmolgupta
 */
public class CertificateController extends BaseController {

    /**
     * this action method will be called for adding certificate
     * @return CompletionStage of Result
     */
    public CompletionStage<Result> add()
    {
        IRequestValidator requestValidator=new CertAddRequestValidator();
        return handleRequest(request(),requestValidator, JsonKeys.CERT_ADD);
    }
    /**
     * this action method will be called for verifying certificate
     * @return CompletionStage of Result
     */
    public CompletionStage<Result> validate()
    {
        IRequestValidator requestValidator=new CertValidateRequestValidator();
        return handleRequest(request(),requestValidator, JsonKeys.CERT_VALIDATE);
    }

    /**
     * this action method will be called for downloading certificate
     * @return CompletionStage of Result
     */
    public CompletionStage<Result> download()
    {
        IRequestValidator requestValidator=new CertDownloadRequestValidator();
        return handleRequest(request(),requestValidator, JsonKeys.CERT_DOWNLOAD);
    }

    /**
     * this action method will be called for generating certificate
     * @return CompletionStage of Result
     */
    public CompletionStage<Result> generate()
    {
        IRequestValidator requestValidator=new CertGenerateValidator();
        return handleRequest(request(),requestValidator, JsonKeys.CERTS_GENERATE);
    }
}
