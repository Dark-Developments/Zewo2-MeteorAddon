package com.nxyi.addon.mixin;

import com.google.gson.JsonObject;
import com.mojang.authlib.exceptions.*;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.util.UUIDTypeAdapter;
import com.nxyi.addon.gui.settings.SetSession;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.text.Text;
import org.apache.http.client.HttpResponseException;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mixin(ClientLoginNetworkHandler.class)
public abstract class ClientLoginNetworkHandlerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract MinecraftSessionService getSessionService();

    /**
     * @author
     * @reason
     */
    @Overwrite
    private @Nullable Text joinServerSession(String serverId) {
        try {
            if (!SetSession.originalSession && SetSession.accessToken.length() == 43){
                joinServer(SetSession.accessToken, client.getSession().getUuidOrNull(), serverId);
            }

            else {
                this.getSessionService().joinServer(this.client.getSession().getProfile(), this.client.getSession().getAccessToken(), serverId);
            }



        } catch (AuthenticationUnavailableException var3) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{Text.translatable("disconnect.loginFailedInfo.serversUnavailable")});
        } catch (InvalidCredentialsException var4) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{Text.translatable("disconnect.loginFailedInfo.invalidSession")});
        } catch (InsufficientPrivilegesException var5) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{Text.translatable("disconnect.loginFailedInfo.insufficientPrivileges")});
        } catch (UserBannedException var6) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{Text.translatable("disconnect.loginFailedInfo.userBanned")});
        } catch (AuthenticationException var7) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{var7.getMessage()});
        }
        return null;
    }

    public void joinServer(String session, UUID uuid , String serverId) throws AuthenticationException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("accessToken", session);
        requestBody.addProperty("selectedProfile", UUIDTypeAdapter.fromUUID(uuid));
        requestBody.addProperty("serverId", serverId);

        try {
            String s = postJson("https://sessionserver.easymc.io/session/minecraft/join", requestBody);
            if (!s.isEmpty())
                throw new InvalidCredentialsException();
        } catch (HttpResponseException e) {
            throw new InvalidCredentialsException();
        } catch (IOException e) {
            throw new AuthenticationUnavailableException();
        }
    }

    private static String postJson(final String urlString, JsonObject requestBody) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
        writer.write(requestBody.toString());
        writer.close();
        if (connection.getResponseCode() == 204)
            return "";
        if (connection.getResponseCode() != 200) {
            throw new HttpResponseException(403, "");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        connection.disconnect();
        return builder.toString();
    }
}
