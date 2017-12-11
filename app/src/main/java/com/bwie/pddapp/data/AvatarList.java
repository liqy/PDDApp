package com.bwie.pddapp.data;

import java.util.List;

/**
 * Created by liqy on 2017/12/11.
 */

public class AvatarList {
    public List<GroupAvatar> list;
    public String bubble;

    @Override
    public String toString() {
        return "AvatarList{" +
                "list=" + list +
                ", bubble='" + bubble + '\'' +
                '}';
    }
}
