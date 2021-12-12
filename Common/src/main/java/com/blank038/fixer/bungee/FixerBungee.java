package com.blank038.fixer.bungee;

import com.blank038.fixer.model.log4j.Log4j2Model;
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
            new Log4j2Model();
        } catch (Exception ignored) {
        }
    }
}
