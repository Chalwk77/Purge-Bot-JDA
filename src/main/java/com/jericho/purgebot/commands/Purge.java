// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.purgebot.commands;

import com.jericho.purgebot.listeners.CommandInterface;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Purge implements CommandInterface {

    // Get the command name:
    @Override
    public String getName() {
        return "purge";
    }

    // Get the command description:
    @Override
    public String getDescription() {
        return "Purge user messages in a defined time frame.";
    }

    /**
     * Get the command options:
     *
     * @return List of OptionData.
     * @see OptionData
     * @see OptionType
     * @see OptionMapping
     */
    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();

        // Add command options:
        OptionData user = new OptionData(OptionType.USER, "user", "The user to purge messages from.", true);
        OptionData time = new OptionData(OptionType.INTEGER, "time", "Time frame", true);
        OptionData flag = new OptionData(OptionType.STRING, "flag", "[-y, -mo, -wk, -d, -hr, -min, -sec]", true);
        OptionData channel = new OptionData(OptionType.CHANNEL, "channel", "Channel ID", false);

        // Set choices for the flag option:
        flag.addChoice("Years", "-y");
        flag.addChoice("Months", "-mo");
        flag.addChoice("Weeks", "-wk");
        flag.addChoice("Days", "-d");
        flag.addChoice("Hours", "-hr");
        flag.addChoice("Minutes", "-min");
        flag.addChoice("Seconds", "-sec");

        // Add options to the list:
        data.add(user);
        data.add(time);
        data.add(flag);
        data.add(channel);

        // Return the list:
        return data;
    }

    /**
     * Create a new hash map to store the days in each month:
     *
     * @return HashMap of months.
     */
    private static final Map<Integer, Integer> months = new HashMap<>() {{
        put(1, 31);
        put(2, 28);
        put(3, 31);
        put(4, 30);
        put(5, 31);
        put(6, 30);
        put(7, 31);
        put(8, 31);
        put(9, 30);
        put(10, 31);
        put(11, 30);
        put(12, 31);
    }};

    /**
     * Checks if the year is a leap year:
     * @param year The year to check.
     * @return True if the year is a leap year, false if not.
     * @see Purge#months
     */
    public static boolean leapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    /**
     * Returns the number of days for the current month:
     * @return months The month to check.
     * @see Purge#months
     * @see Purge#leapYear(int)
     */
    public static int getDaysInMonth() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        if (month == 2 && leapYear(year)) {
            return 29;
        }
        return months.get(month);
    }

    /**
     * Execute the command:
     *
     * @param event SlashCommandInteractionEvent.
     * @see SlashCommandInteractionEvent
     * @see Permission
     * @see OptionMapping
     * @see OptionType
     * @see Calendar
     * @see AtomicInteger
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {

        // Check if the user has the ADMINISTRATOR permission:
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {


            // Get command options:
            OptionMapping option_user = event.getOption("user");
            OptionMapping option_time = event.getOption("time");
            OptionMapping option_flag = event.getOption("flag");
            OptionMapping option_channel = event.getOption("channel");

            String user = option_user.getAsString();
            String time = option_time.getAsString();
            String flag = option_flag.getAsString();
            String channel = option_channel != null ? option_channel.getAsString() : event.getChannel().getId();

            // Set default channel to the current channel if no channel is specified:
            if (option_channel == null) {
                channel = event.getChannel().getId();
            }

            // Get time format:
            int time_frame = Integer.parseInt(time);
            switch (flag.toLowerCase()) {
                case "-y":
                    time_frame = time_frame * (60 * 60 * 24 * 365);
                    break;
                case "-mo":
                    time_frame = time_frame * (60 * 60 * 24 * getDaysInMonth());
                    break;
                case "-wk":
                    time_frame = time_frame * (60 * 60 * 24 * 7);
                    break;
                case "-d":
                    time_frame = time_frame * (60 * 60 * 24);
                    break;
                case "-hr":
                    time_frame = time_frame * (60 * 60);
                    break;
                case "-min":
                    time_frame = time_frame * 60;
                    break;
            }

            // Get name of user to purge:
            String name = event.getGuild().getMemberById(user).getEffectiveName();

            int finalTime_frame = time_frame;

            String finalChannel = channel;
            event.getGuild().getTextChannelById(channel).getIterableHistory().takeAsync(100).thenAcceptAsync(messages -> {

                // Create a new AtomicInteger to count the number of messages deleted:
                AtomicInteger deleted = new AtomicInteger();
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getAuthor().getId().equals(user)) {
                        if (System.currentTimeMillis() - messages.get(i).getTimeCreated().toInstant().toEpochMilli() < finalTime_frame * 1000L) {
                            messages.get(i).delete().queue();
                            deleted.getAndIncrement();
                        }
                    }
                }

                // Inform the admin:
                if (deleted.get() == 0) {
                    event.reply("No messages were deleted for " + name).setEphemeral(true).queue();
                } else {
                    String channel_name = event.getGuild().getTextChannelById(finalChannel).getName();
                    event.reply("Deleted " + deleted.get() + " messages for " + name + " in " + channel_name).setEphemeral(true).queue();
                }

            });
        } else {
            event.reply("You do not have permission to execute this command.").setEphemeral(true).queue();
        }
    }
}
