package io.cess.comm.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 * @date 21/06/2017.
 */

public interface HttpClientResponse {

    Map<String,List<String>> headers();

    String getHeader(String name);

    List<String> getHeaders(String name);

    int getStatusCode();

    byte[] getData();

    String getMessage();
}
