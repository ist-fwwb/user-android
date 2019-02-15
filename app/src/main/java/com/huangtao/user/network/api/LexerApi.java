package com.huangtao.user.network.api;

import com.huangtao.user.model.LexerResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LexerApi {

    @GET("{text}")
    Call<LexerResult> lexer(@Path("text") String text);

}
