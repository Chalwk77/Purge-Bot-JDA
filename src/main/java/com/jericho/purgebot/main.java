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

public class Main {

    public static JSONObject settings;

    static {
        try {
            settings = loadJSONObject("settings.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Custom print method:
     * Receives a string and prints it to the console.
     */
    public static void cprint(String message) {
        System.out.println(message);
    }

    /**
     * Returns the bot token from the settings.json file.
     * @return The bot token.
     */
    public static String getToken() {
        return String.valueOf(settings.getString("token"));
    }

    /**
     * Loads environment variables, builds the shard manager and starts the bot:
     * @throws LoginException if the bot token is invalid.
     */
    public Main() throws LoginException {

        // Get token:
        String token = getToken();

        // Create builder and set status and activity:
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Your messages..."));

        // Add event listeners:
        ShardManager shardManager = builder.build();
        shardManager.addEventListener(new EventListeners());

        CommandManager manager = new CommandManager();
        manager.add(new Purge());
        manager.add(new PurgeHelp());

        shardManager.addEventListener(manager);
    }

    /**
     * Main method:
     */
    public static void main(String[] args) {
        try {
            new Main();
        } catch (LoginException e) {
            cprint("ERROR: Provided bot token is invalid");
        }
    }
}
