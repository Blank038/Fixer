package com.blank038.fixer.bukkit.command;

import com.blank038.fixer.bukkit.Fixer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Blank038
 */
public class FixerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("fixer.admin")) {
            Fixer.getInstance().loadConfig();
            commandSender.sendMessage("§c[Fixer] §f插件配置重载完成!");
        }
        return false;
    }
}