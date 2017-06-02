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

    /**
     * 创建Stack实例
     */
    public Optional<Stack> createStack(Stack stack) throws IOException {
        return Optional.ofNullable(post(String.format("%s/stack", this.endpoint), stack, Stack.class));
    }

    /**
     * 在Stack下创建Service
     */
    public Optional<Service> createService(Service service, String stackId) throws IOException {
        service.setStackId(stackId);
        return Optional.ofNullable(post(String.format("%s/service", this.endpoint), service, Service.class));
    }

    public void deleteStack(String id) throws IOException {
        delete(String.format("%s/stacks/%s", this.endpoint, id), Stack.class);
    }

    private <T> T get(String url, Class<T> responseClass) throws IOException {
        GetMethod deleteMethod = new GetMethod(url);
        return execute(deleteMethod, responseClass);
    }

    private <T> T delete(String url, Class<T> responseClass) throws IOException {
        DeleteMethod deleteMethod = new DeleteMethod(url);
        return execute(deleteMethod, responseClass);
    }

    private <T> T post(String url, Object data, Class<T> responseClass) throws IOException {
        PostMethod method = new PostMethod(url);
        method.setRequestEntity(getRequestBody(data));
        return this.execute(method, responseClass);
    }

    private <T> T execute(HttpMethod method, Class<T> responseClass) {
        try {
            method.addRequestHeader("Authorization", getAuthorization());
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
            LOGGER.info("request:" + method.getURI().toString());
            int statusCode = new HttpClient().executeMethod(method);
            String responseBody = new String(method.getResponseBody());
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_ACCEPTED && statusCode != HttpStatus.SC_CREATED) {
                throw new RuntimeException(String.format("Some Error Happen statusCode %d response: %s", statusCode, responseBody));
            }
            LOGGER.info("Rancher Response" + responseBody);
            return getObjectMapper().readValue(responseBody, responseClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection to Rancher Failed Please check deploy configuration");
        } finally {
            method.releaseConnection();
        }
    }

    private StringRequestEntity getRequestBody(Object stack) throws JsonProcessingException, UnsupportedEncodingException {
        String requestBody = getObjectMapper().writeValueAsString(stack);
        return new StringRequestEntity(requestBody, "application/json", "UTF-8");
    }

    private String getAuthorization() {
        byte[] encodedAuth = Base64.encodeBase64((accesskey + ":" + secretKey).getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        return objectMapper;
    }


}
