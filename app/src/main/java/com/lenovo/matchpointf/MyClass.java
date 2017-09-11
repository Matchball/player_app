package com.lenovo.matchpointf;

import java.util.Objects;

/**
 * Created by atharva vyas on 22-07-2017.
 * This class contains the data which is sored in 'enrolled' players list with eah tournament database node
 */

public class MyClass {

    public String playername;
    public String category;

    public MyClass() {

    }

    public MyClass(String playername,String category) {
        this.playername=playername;
        this.category=category;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual= false;

        if (object != null && object instanceof MyClass)
        {
            isEqual = (this.playername.equals(((MyClass) object).playername));
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.playername.hashCode();
    }

}
