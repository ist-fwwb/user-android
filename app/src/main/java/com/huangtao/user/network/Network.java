package com.huangtao.user.network;

import com.huangtao.user.network.api.Api;
import com.huangtao.user.network.api.LexerApi;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static String base_url = "http://47.106.8.44";
//    private static String base_url = "http://10.0.0.188";
    private static int base_port = 31000;
//    private static int base_port = 8080;

    private static String lexer_url = "http://47.106.8.44";
    private static int lexer_port = 4000;

    private static Api api;
    private static LexerApi lexerApi;

    public static Api getInstance() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    //设置数据解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置网络请求的Url地址
                    .baseUrl(base_url + ":" + base_port + "/")
                    .build();
            // 创建网络请求接口的实例
            api = retrofit.create(Api.class);
        }
        return api;
    }

    public static Api getInstance(Converter.Factory factory) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(factory)
                .baseUrl(base_url + ":" + base_port + "/")
                .build();
        return retrofit.create(Api.class);
    }

    public static LexerApi getLexerInstance() {
        if (lexerApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(lexer_url + ":" + lexer_port + "/lexer/")
                    .build();
            lexerApi = retrofit.create(LexerApi.class);
        }
        return lexerApi;
    }

}
