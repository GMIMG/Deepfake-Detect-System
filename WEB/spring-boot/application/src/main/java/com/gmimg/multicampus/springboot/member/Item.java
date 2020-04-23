package com.gmimg.multicampus.springboot.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    private int iditem;
    private int memIdx;
    private String filename;
    private float acc;
}