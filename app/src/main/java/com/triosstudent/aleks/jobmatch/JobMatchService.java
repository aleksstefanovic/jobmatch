package com.triosstudent.aleks.jobmatch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JobMatchService {

    public static String buildCreateUserCall (String email, String password, String type) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", email);
            payload.put("password", password);
            payload.put("type", type);

            System.out.println(payload.toString());
            return payload.toString();
        }
        catch (JSONException ex) {
            System.out.println(ex.getStackTrace());
            return(null);
        }
    }

    public static String buildCreateQuestionnaireCall (String user_id, String question1, String question2, String question3, String question4, String title) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("user_id", user_id);
            payload.put("question1", question1);
            payload.put("question2", question2);
            payload.put("question3", question3);
            payload.put("question4", question4);
            payload.put("title", title);

            System.out.println(payload.toString());
            return payload.toString();
        }
        catch (JSONException ex) {
            System.out.println(ex.getStackTrace());
            return(null);
        }
    }

    public static String executePost(String targetURL, String payload) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String executeGet(String targetURL) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
