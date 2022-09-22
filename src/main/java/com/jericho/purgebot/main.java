// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.purgebot;

import com.jericho.purgebot.commands.PurgeHelp;
import com.jericho.purgebot.listeners.CommandManager;
import com.jericho.purgebot.commands.Purge;
import com.jericho.purgebot.listeners.EventListeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import org.json.JSONObject;

import javax.security.auth.login.LoginException;

import java.io.IOException;

import static com.jericho.purgebot.Utilities.FileIO.loadJSONObject;

public class main {

    public static JSONObject settings;

    static {
        try {
            settings = loadJSONObject("settings.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void cprint(String message) {
        System.out.println(message);
    }

    // Return the bot token:
    public static String getToken() {
        return String.valueOf(settings.getString("token"));
    }

    public main() throws LoginException {
        String token = getToken();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Your messages..."));
        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.MESSAGE_CONTENT
        );
        ShardManager shardManager = builder.build();
        shardManager.addEventListener(new EventListeners());

        CommandManager manager = new CommandManager();
        manager.add(new Purge());
        manager.add(new PurgeHelp());

        shardManager.addEventListener(manager);
    }

    public static void main(String[] args) {
        try {
            new main();
        } catch (LoginException e) {
            cprint("ERROR: Provided bot token is invalid");
        }
    }
}
