// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.purgebot.commands;

import com.jericho.purgebot.listeners.CommandInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PurgeHelp implements CommandInterface {

    // Get the command name:
    @Override
    public String getName() {
        return "purgehelp";
    }

    // Get the command description:
    @Override
    public String getDescription() {
        return "Get help with Purge Bot.";
    }

    // Get the command options:
    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    // Execute the command:
    @Override
    public void execute(SlashCommandInteractionEvent event) {

        // Check if the user has the ADMINISTRATOR permission:
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {

            // Create an embed message and send it the admin:
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("Purge")
                    .setDescription("""
                            Commands:\s
                            **/purge** Purge user messages in a defined time frame.
                            **/purgehelp** Get help with Purge Bot.
                            Syntax: `/purge <user> <time> <flag> <channel (optional)>`


                            Flag Usage:
                            `-y` for years, `-mo` for months, `-wk` for weeks, `-d` for days, `-hr` for hours, `-min` for minutes, `-sec` for seconds.

                            Example: `/purge @ExampleUser 1 -d #general`
                            """
                    )
                    .setColor(Color.GREEN)
                    .build()).setEphemeral(true).queue();
        } else {
            event.reply("You do not have permission to execute this command.").setEphemeral(true).queue();
        }
    }
}
