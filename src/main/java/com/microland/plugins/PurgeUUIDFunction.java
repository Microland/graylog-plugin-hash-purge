package com.microland.plugins;


import org.apache.http.HttpEntity;
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

import java.io.IOException;
import java.util.Base64;


/**
 * This is the plugin. Your class should implement one of the existing plugin
 * interfaces. (i.e. AlarmCallback, MessageInput, MessageOutput)
 */
public class PurgeUUIDFunction implements Function<String> {
    public static final String NAME = "purge_hash";
//    private static final String PARAM = "string";

    private final ParameterDescriptor lookup_table_name = ParameterDescriptor
            .string("lookup_table_name")
            .description("Lookup table name")
            .build();
    private final ParameterDescriptor hash = ParameterDescriptor
            .string("hash")
            .description("Hash value to be purged")
            .build();
    private final ParameterDescriptor baseUrl = ParameterDescriptor
            .string("base_url")
            .description("URL of the graylog instance")
            .build();
    private final ParameterDescriptor key = ParameterDescriptor
            .string("key")
            .description("Auth Token")
            .build();
    private String pwd = "token";

    @Override
    public Object preComputeConstantArgument(FunctionArgs functionArgs, String s, Expression expression) {
        return expression.evaluateUnsafe(EvaluationContext.emptyContext());
    }

    @Override
    public String evaluate(FunctionArgs functionArgs, EvaluationContext evaluationContext) {
        HttpClient httpclient = HttpClients.createDefault();
        String apiUrl  = baseUrl + "/api/system/lookup/tables/"+lookup_table_name+"/purge"+"?key="+hash;
        String encoding = Base64.getEncoder().encodeToString((key.toString()+":"+pwd).getBytes());
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        if (response.getStatusLine().getStatusCode() > 299) {
            return "FAILURE" + response.getStatusLine().toString();
        }
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
