package com.barrybarry.coffeeport.data.xml;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class UiUpdateData implements Serializable {
    private String func = "";
    private ArrayList<Object> args = new ArrayList<>();
}
