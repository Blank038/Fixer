package com.blank038.fixer.bungee;

import com.blank038.fixer.model.log4j.Log4jModel;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Blank038
 * @since 2021-12-10
 */
public class FixerBungee extends Plugin {

    @Override
    public void onEnable() {
        try {
            Class.forName("org.apache.logging.log4j.core.filter.AbstractFilter");
            new Log4jModel();
        } catch (Exception ignored) {
        }
    }
}
