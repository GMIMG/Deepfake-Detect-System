package com.gmimg.multicampus.springboot.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {
    private int iditem;
    private int memIdx;
    private String filename;
    private float acc;
    private int del;
}