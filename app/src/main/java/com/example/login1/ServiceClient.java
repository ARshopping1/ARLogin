package com.example.login1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceClient {
    public static String BASE_URL="http://192.168.43.185:8084";

    public static byte [] read(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte []b=new byte[10240];
        int n;

        while((n=is.read(b))!=-1)
        {
            baos.write(b,0,n);
        }

        b=baos.toByteArray();
        baos.close();
        is.close();
        return b;
    }

    public static String get(String url) throws IOException {
        URL u=new URL(BASE_URL+url);
        HttpURLConnection con= (HttpURLConnection) u.openConnection();
        InputStream is=con.getInputStream();

        byte []b=read(is);

        return new String(b,"UTF-8");
    }

    public static String post(String url, String data) throws IOException {
        URL u=new URL(BASE_URL+url);
        HttpURLConnection con= (HttpURLConnection) u.openConnection();
        con.setDoOutput(true);

        OutputStream os=con.getOutputStream();
        os.write(data.getBytes("UTF-8"));
        os.flush();
        os.close();

        InputStream is=con.getInputStream();

        byte []b=read(is);

        return new String(b,"UTF-8");
    }
}
