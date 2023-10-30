package me.fullidle.ficore.ficore.v1_20;

import me.fullidle.ficore.ficore.common.FIData;
import me.fullidle.ficore.ficore.v1_16.V1_16;

public class V1_20 extends V1_16 {
    public V1_20(){
        FIData.V1_version = this;
    }

    @Override
    public String getVersion() {
        return "1.20.1";
    }
}
