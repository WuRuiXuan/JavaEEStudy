package com.itheima.consumer;

import com.itheima.pojo.Invocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;

public class HttpClient {

    /**
     * 远程方法调用
     * @param hostname 远程主机名
     * @param port 远程端口号
     * @param invocation 封装远程调用的信息
     * @return
     */
    public String post(String hostname, int port, Invocation invocation) {
        try {

            // 1. 进行连接
            URL url = new URL("http", hostname, port, "/client");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // 必填项

            // 2. 发送调用信息
            OutputStream os = connection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(invocation);
            oos.flush();
            oos.close();

            // 3. 将输入流转换成字符串，获取远程调用的结果
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
