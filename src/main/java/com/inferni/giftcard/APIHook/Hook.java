package com.inferni.giftcard.APIHook;


import com.inferni.giftcard.GiftCard;
import com.inferni.giftcard.Util.DataInterface;
import org.apache.commons.codec.Charsets;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Hook {
    private static Hook instance;
    private static String token = GiftCard.config.getString("APIToken");

    public Hook() {
        instance = this;
    }

    public static Hook getInstance() {
        if (instance == null) {
            instance = new Hook();
        }
        return instance;
    }
    public static void deleteCard(String id){
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri("https://api.craftingstore.net" +
                            "/v7/gift-cards/" + id)
                    .addHeader("token", token)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build();

            HttpResponse response = client.execute(request);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static int makeNewCard(int amount){
        try {
            HttpClient client = HttpClientBuilder.create().build();
            String inputJson = "{\"amount\": \"" + amount +"\"}";
            HttpUriRequest request = RequestBuilder.post()
                    .setUri("https://api.craftingstore.net" +
                            "/v7/gift-cards")
                    .addHeader("token", token)
                    .setEntity(new StringEntity(inputJson))
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build();

            HttpResponse response = client.execute(request);

            //This is irrelevant unless you want to get the response
            HttpEntity entity = response.getEntity();
            Header encodingHeader = entity.getContentEncoding();

            Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                    Charsets.toCharset(encodingHeader.getValue());

            String json1 = EntityUtils.toString(entity, StandardCharsets.UTF_8);

            JSONObject o = new JSONObject(json1);
            return o.getJSONObject("data").getInt("id");
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }
    public static JSONObject getCards(){
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.craftingstore.net" +
                            "/v7/gift-cards")
                    .addHeader("token", token)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build();

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            Header encodingHeader = entity.getContentEncoding();

            Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                    Charsets.toCharset(encodingHeader.getValue());

            String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

            JSONObject o = new JSONObject(json);
            return o;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public static String getCardCode(String id){
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.craftingstore.net" +
                            "/v7/gift-cards/"+id)
                    .addHeader("token", token)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build();

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            Header encodingHeader = entity.getContentEncoding();

            Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                    Charsets.toCharset(encodingHeader.getValue());

            String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

            JSONObject o = new JSONObject(json);
            /*System.out.println("JSON DEBUG");
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
            System.out.println(o.toString());*/
            return o.getJSONObject("data").getString("code");
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
