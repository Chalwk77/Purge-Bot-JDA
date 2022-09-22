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

    @Override
    public String getName() {
        return "purgehelp";
    }

    @Override
    public String getDescription() {
        return "Get help with Purge Bot.";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
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
