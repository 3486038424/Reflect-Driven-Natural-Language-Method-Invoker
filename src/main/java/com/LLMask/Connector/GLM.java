package com.LLMask.Connector;

import com.LLMask.LLMConnector;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GLM implements LLMConnector {
    public String getExec(String prompt)  {
        try {
            String urlString = "http://localhost:11434/api/generate";
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求方法为POST
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            // 创建要发送的JSON对象
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("model", "llava");
            jsonInput.put("prompt", prompt);
            jsonInput.put("stream", false);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            // 读取响应内容
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // 解析JSON响应并提取response字段
                JSONObject jsonResponse = JSONObject.parseObject(response.toString());
                return jsonResponse.getString("response");
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new GLM().getExec("你好，今天天气怎么样？"));
    }
}
