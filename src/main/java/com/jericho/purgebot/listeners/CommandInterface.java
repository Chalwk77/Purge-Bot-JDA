// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.purgebot.listeners;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public interface CommandInterface {

    String getName();

    String getDescription();

    List<OptionData> getOptions();

    void execute(SlashCommandInteractionEvent event);
}
