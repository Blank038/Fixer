package com.blank038.fixer.command;

import com.blank038.fixer.Fixer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FixerCommander implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("fixer.admin")) {
            Fixer.getInstance().loadConfig();
            commandSender.sendMessage("§c[Fixer] §f插件配置重载完成!");
        }
        return false;
    }
}