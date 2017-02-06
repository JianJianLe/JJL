package com.xy.jjl.utils;

import java.io.File;
import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by dongjunkun on 2015/8/31.
 */

//Test by Star 20170101
public class ProgressUploadFile {
    private static final OkHttpClient okHttpClient = new OkHttpClient();

    private void run() {

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        File file = new File("D:\\file.jpg");
        builder.addFormDataPart("file", file.getName(), createCustomRequestBody(MultipartBuilder.FORM, file, new ProgressListener() {
            @Override public void onProgress(long totalBytes, long remainingBytes, boolean done) {
                System.out.print((totalBytes - remainingBytes) * 100 / totalBytes + "%");
            }
        }));

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url("http://localhost:8080/upload") //µÿ÷∑
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Request request, IOException e) {

            }

            @Override public void onResponse(Response response) throws IOException {
                System.out.println("response.body().string() = " + response.body().string());

            }
        });
    }

    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return file.length();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
    }

    public static void main(String[] args) {
        new ProgressUploadFile().run();

    }

}
