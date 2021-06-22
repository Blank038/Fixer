package com.blank038.fixer.data;

import com.blank038.fixer.Fixer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Blank038
 * @date 2021-04-03
 */
public class CheckList {
    public final List<String> apricornList, machineryList, sakuraGrowList,
            leftClickLimit, rightCLickLimit;

    public CheckList() {
        this.apricornList = Fixer.getConfiguration().getStringList("message.pixelmon.apricorn.check");
        this.machineryList = Fixer.getConfiguration().getStringList("message.pams.machinery.check");
        this.sakuraGrowList = Fixer.getConfiguration().getStringList("message.sakura.grow.check");
        this.leftClickLimit = Fixer.getConfiguration().getStringList("message.common.interact_limit.left_click");
        this.rightCLickLimit = Fixer.getConfiguration().getStringList("message.common.interact_limit.right_click");
    }
}
