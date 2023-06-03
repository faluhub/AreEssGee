package me.quesia.areessgee.config.list;

import java.util.ArrayList;
import java.util.List;

public class StringList extends ArrayList<String> {
    public StringList(String ...values) {
        this.addAll(List.of(values));
    }
}
