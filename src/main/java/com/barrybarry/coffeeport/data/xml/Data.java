package com.barrybarry.coffeeport.data.xml;

import com.barrybarry.coffeeport.type.CommandType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Data {
    private CommandType cmd;

    private InstallationConfigureData configure;
}
