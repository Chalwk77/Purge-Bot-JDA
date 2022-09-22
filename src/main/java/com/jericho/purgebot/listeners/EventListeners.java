// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.purgebot.listeners;

import com.jericho.purgebot.Main;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class EventListeners extends ListenerAdapter {

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        Main.cprint("Guild ready: " + event.getGuild().getName());
        Main.cprint("Bot name: " + event.getJDA().getSelfUser().getName());
    }
}