package com.microland.plugins;


import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.graylog.plugins.pipelineprocessor.EvaluationContext;
import org.graylog.plugins.pipelineprocessor.ast.expressions.Expression;
import org.graylog.plugins.pipelineprocessor.ast.functions.Function;
import org.graylog.plugins.pipelineprocessor.ast.functions.FunctionArgs;
import org.graylog.plugins.pipelineprocessor.ast.functions.FunctionDescriptor;
import org.graylog.plugins.pipelineprocessor.ast.functions.ParameterDescriptor;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Base64;


/**
 * This is the plugin. Your class should implement one of the existing plugin
 * interfaces. (i.e. AlarmCallback, MessageInput, MessageOutput)
 */
public class PurgeUUIDFunction implements Function<String> {
    public static final String NAME = "purge_hash";
//    private static final String PARAM = "string";

    private final ParameterDescriptor<String, String> lookup_table_name = ParameterDescriptor
            .string("lookup_table_name")
            .description("Lookup table name")
            .build();
    private final ParameterDescriptor<String, String> hash = ParameterDescriptor
            .string("hash")
            .description("Hash value to be purged")
            .build();
    private final ParameterDescriptor<String, String> baseUrl = ParameterDescriptor
            .string("base_url")
            .description("URL of the graylog instance")
            .build();
    private final ParameterDescriptor<String, String> key = ParameterDescriptor
            .string("key")
            .description("Auth Token")
            .build();

    @Override
    public Object preComputeConstantArgument(FunctionArgs functionArgs, String s, Expression expression) {
        return expression.evaluateUnsafe(EvaluationContext.emptyContext());
    }

    @Override
    public String evaluate(FunctionArgs functionArgs, EvaluationContext evaluationContext) {
        HttpClient httpclient = HttpClients.createDefault();
        String bUrl = baseUrl.required(functionArgs, evaluationContext);
        String table = lookup_table_name.required(functionArgs, evaluationContext);
        String hashVal = hash.required(functionArgs, evaluationContext);
        String keyVal = key.required(functionArgs, evaluationContext);
        String apiUrl  = bUrl + "/api/system/lookup/tables/"+table+"/purge"+"?key=";
        URI uri = null;
        try {
            uri = new URI(apiUrl + URLEncoder.encode(hashVal, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(uri);
        String pwd = "token";
        String encoding = Base64.getEncoder().encodeToString((keyVal+":"+ pwd).getBytes());
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        httpPost.setHeader("X-Requested-By", "client");
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert response != null;
        if (response.getStatusLine().getStatusCode() > 299) {
            System.out.println(response.getStatusLine().toString());
            return "FAILURE" + response.getStatusLine().toString();
        }
//        System.out.println("Success");
        return "SUCCESS";
    }
    @Override
    public FunctionDescriptor<String> descriptor() {
        return FunctionDescriptor.<String>builder()
                .name(NAME)
                .description("Purge hash")
                .params(lookup_table_name, hash, baseUrl, key)
                .returnType(String.class)
                .build();
    }

}
