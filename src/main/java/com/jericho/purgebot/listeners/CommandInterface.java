// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.purgebot.listeners;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public interface CommandInterface {

    String getName(); // Get the command name.

    String getDescription(); // Get the command description.

    List<OptionData> getOptions(); // Get the command options.

    void execute(SlashCommandInteractionEvent event); // Execute the command.
}
