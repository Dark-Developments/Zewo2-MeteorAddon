package com.nxyi.addon.login.easymc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;

public class EasyMCRequest {

    public AltTokenResponse getResponse(String token) throws URISyntaxException, IOException, InterruptedException {
        AltTokenResponse altTokenResponse;
        AltTokenRequest altTokenRequest = new AltTokenRequest();
        altTokenRequest.setToken(token);

        Gson gson = new Gson();
        String requestJson = gson.toJson(altTokenRequest);

        URL apiURL = new URL("https://api.easymc.io/v1/token/redeem");
        HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = requestJson.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            altTokenResponse = gson.fromJson(response.toString(), AltTokenResponse.class);
        }
        return altTokenResponse;
    }
}
