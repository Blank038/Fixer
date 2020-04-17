package com.blank038.fixer.data;

import com.blank038.fixer.Fixer;

import java.util.ArrayList;
import java.util.List;

public class CheckList {
    public List<String> apricornList, machineryList, sakuraGrowList;

    public CheckList() {
        apricornList = new ArrayList<>(Fixer.getConfiguration().getStringList("message.pixelmon.apricorn.check"));
        machineryList = new ArrayList<>(Fixer.getConfiguration().getStringList("message.pams.machinery.check"));
        sakuraGrowList = new ArrayList<>(Fixer.getConfiguration().getStringList("message.sakura.grow.check"));
    }
}
