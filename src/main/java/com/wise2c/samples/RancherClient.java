package com.wise2c.samples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wise2c.samples.entity.Service;
import com.wise2c.samples.entity.Stack;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Optional;

public class RancherClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RancherClient.class);

    private final String endpoint;
    private final String accesskey;
    private final String secretKey;

    public RancherClient(String endpoint, String accesskey, String secretKey) {
        this.endpoint = endpoint;
        this.accesskey = accesskey;
        this.secretKey = secretKey;
    }

    public Optional<Stack> createStack(Stack stack) throws IOException {
        PostMethod postMethod = getPostMethod(String.format("%s/stack", endpoint), stack);
        return Optional.ofNullable(getObjectMapper().readValue(execute(postMethod), Stack.class));
    }

    public Optional<Service> createService(Service service, String stackId) throws IOException {
        service.setStackId(stackId);
        PostMethod postMethod = getPostMethod(endpoint + "/service", service);
        return Optional.ofNullable(getObjectMapper().readValue(execute(postMethod), Service.class));
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        return objectMapper;
    }

    public void deleteStack(String id) {
        DeleteMethod deleteMethod = getDeleteMethod(String.format("%s/stacks/%s", endpoint, id));
        String response = execute(deleteMethod);
        System.out.println(response);
    }

    private String execute(HttpMethod method) {
        try {
            LOGGER.info("request:" + method.getURI().toString());
            int statusCode = new HttpClient().executeMethod(method);
            String responseBody = new String(method.getResponseBody());
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_ACCEPTED && statusCode != HttpStatus.SC_CREATED) {
                throw new RuntimeException(String.format("Some Error Happen statusCode %d response: %s", statusCode, responseBody));
            }
            LOGGER.info("Rancher Response" + responseBody);
            return responseBody;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection to Rancher Failed Please check deploy configuration");
        } finally {
            method.releaseConnection();
        }
    }

    private HttpMethod getGetMethod(String endpoint) {
        GetMethod method = new GetMethod(endpoint);
        method.addRequestHeader("Authorization", getAuthorization());
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        return method;
    }

    private DeleteMethod getDeleteMethod(String uri) {
        DeleteMethod method = new DeleteMethod(uri);
        method.addRequestHeader("Authorization", getAuthorization());
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        return method;
    }

    private PostMethod getPostMethod(String uri, Object data) throws UnsupportedEncodingException, JsonProcessingException {
        PostMethod method = new PostMethod(uri);
        method.addRequestHeader("Authorization", getAuthorization());
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        method.setRequestEntity(getRequestBody(data));
        return method;
    }

    private StringRequestEntity getRequestBody(Object stack) throws JsonProcessingException, UnsupportedEncodingException {
        String requestBody = getObjectMapper().writeValueAsString(stack);
        return new StringRequestEntity(requestBody, "application/json", "UTF-8");
    }

    private String getAuthorization() {
        byte[] encodedAuth = Base64.encodeBase64((accesskey + ":" + secretKey).getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }


}
