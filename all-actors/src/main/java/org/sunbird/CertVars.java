package org.sunbird;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * this class will help in getting the env values for calling the cert service
 * @author anmolgupta
 */
public class CertVars {

    private static Logger logger=Logger.getLogger(CertVars.class);
    private  static String SERVICE_BASE_URL="http://localhost:9100";
    private static String DOWNLOAD_URI= "/v1/user/certs/download";
    private static String GENERATE_URI="/v1/certs/generate";


    public static String getGenerateUri() {
        return GENERATE_URI;
    }


    public static String getSERVICE_BASE_URL() {
        return SERVICE_BASE_URL;

    }

    public static String getDOWNLOAD_URI() {
        return DOWNLOAD_URI;
    }


    private static String getPropsFromEnvs(String props){
        String propValue=System.getenv(props);
        if(StringUtils.isBlank(propValue)){
            logger.error("CertVars:getPropsFromEnvs:no suitable env found for downloadUri");
            System.exit(-1);
        }
        return propValue;
    }

}
