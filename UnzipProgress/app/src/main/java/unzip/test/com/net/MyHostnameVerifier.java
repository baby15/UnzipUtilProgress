package unzip.test.com.net;



import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import unzip.test.com.utils.L;
import unzip.test.com.utils.SPUtils;

public class MyHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        L.e("校验hostname:" + hostname);
        if (SPUtils.getIp().equals(hostname)) {
            return true;
        }
        return false;

    }
}
