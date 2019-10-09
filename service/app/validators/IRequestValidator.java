package validators;

import org.sunbird.BaseException;
import org.sunbird.request.Request;

public interface IRequestValidator {
    void validate(Request request) throws BaseException;

}
