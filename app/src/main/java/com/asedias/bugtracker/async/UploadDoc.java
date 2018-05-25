package com.asedias.bugtracker.async;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.async.methods.GetUploadServer;
import com.vkontakte.android.upload.UploadUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by rorom on 20.05.2018.
 */

public abstract class UploadDoc {

    protected String file;
    protected String server;
    public static boolean crashed = false;

    public UploadDoc(String file, String server) {
        this.file = file;
        this.server = server;
    }

    public UploadDoc(String path) {

        this.file = path;
        if(!crashed)
        new GetUploadServer().setCallback(new DocumentRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String obj) {
                server(obj);
                doUpload();
            }
        }).run();
    }

    private void server(String server) {
        this.server = server;
    }

    protected class UploadEntity extends RequestBody {
        String field;
        String file;
        final String fileHeaderTemplate = "\r\n--VK-FILE-UPLOAD-BOUNDARY\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n";
        byte[] footer = "\r\n--VK-FILE-UPLOAD-BOUNDARY--\r\n".getBytes();
        byte[] header = null;
        int offset = 0;

        public UploadEntity(String _file, String _field) {
            try {
                this.file = _file;
                this.field = _field;
                if (this.file.startsWith("/")) {
                    this.file = new Uri.Builder().scheme("content").path(this.file).build().toString();
                }
                this.header = String.format(Locale.US, fileHeaderTemplate, new Object[]{this.field, getFileName(Uri.parse(this.file)), URLConnection.guessContentTypeFromName(this.file)}).getBytes("UTF-8");
                Log.d("vk", "Will upload " + this.file);
            } catch (Exception x) {
                Log.w("vk", x);
            }
        }

        public MediaType contentType() {
            return MediaType.parse("multipart/form-data; boundary=VK-FILE-UPLOAD-BOUNDARY");
        }

        public void writeTo(BufferedSink bufferedSink) throws IOException {
            FileInputStream is = null;
            AssetFileDescriptor f = null;
            OutputStream os = bufferedSink.outputStream();
            try {
                f = BugTrackerApp.context.getContentResolver().openAssetFileDescriptor(Uri.parse(this.file), "r");
                int total = (int) Math.ceil(((double) f.getLength()) / 1024.0d);
                int loaded = 0;
                long last = 0;
                byte[] buffer = new byte[1024];
                os.write(this.header);
                is = f.createInputStream();
                while (is.available() > 0) {
                    int nread = is.read(buffer);
                    if (nread == -1) {
                        break;
                    }
                    os.write(buffer, 0, nread);
                    os.flush();
                    if (System.currentTimeMillis() - last >= 150) {
                        int _loaded = loaded;
                        Log.d("UPLOAD", _loaded + "/" + total);
                        //HTTPFileUploadTask.this.onProgress(_loaded, total, false);
                        last = System.currentTimeMillis();
                    }
                    this.offset += 1024;
                    loaded++;
                }
                //HTTPFileUploadTask.this.onProgress(10, 10, true);
                os.write(this.footer);
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }
                if (f != null) {
                    f.close();
                }
            } catch (IOException x) {
                Log.w("vk", x);
                throw x;
            } catch (Exception x2) {
                Log.w("vk", x2);
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e2) {
                    }
                }
                if (f != null) {
                    f.close();
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e3) {
                    }
                }
                if (f != null) {
                    f.close();
                }
            }
        }

        public long contentLength() {
            try {
                AssetFileDescriptor f = BugTrackerApp.context.getContentResolver().openAssetFileDescriptor(Uri.parse(this.file), "r");
                long l = ((long) (this.header.length + this.footer.length)) + f.getLength();
                f.close();
                return l;
            } catch (Exception e) {
                return 0;
            }
        }
    }

    protected String getFileName(Uri uri) {
        if (uri.getScheme().equals("content")) {
            return UploadUtils.resolveName(uri);
        }
        return uri.getLastPathSegment();
    }


    protected abstract String getPostFieldName();

    public void doUpload() {
        String targetFileName = this.file;
        if (targetFileName.startsWith("/")) {
            targetFileName = "file://" + targetFileName;
        }

        long timeBeforeCall = System.currentTimeMillis();
        int totalTime = 0;
        Call call = new OkHttpClient().newCall(new Request.Builder().url(this.server).post(new UploadEntity(targetFileName, getPostFieldName())).build());

        try {
            Response response = call.execute();
            String str = response.body().string();
            long timeAfterCall = System.currentTimeMillis();
            if (timeAfterCall - timeBeforeCall > 0 && timeAfterCall - timeBeforeCall < 2147483647L) {
                totalTime = (int) (timeAfterCall - timeBeforeCall);
            }
            Log.d("vk", response.code() + "");
            Log.d("vk", totalTime + "");
            Log.d("vk", str);

        } catch (IOException ignored) {
            crashed = true;
        }
    }

}
