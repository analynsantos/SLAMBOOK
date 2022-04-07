package com.example.slambook;

import java.util.ArrayList;

public class ForRegister_ErrorHandler {
    public ArrayList<String> errorResult = new ArrayList<String>();

    public String getErrorResult(){
        String output = "";
        for (String s : errorResult){
            output = output + s + "\n";
        }

        return output;
    }
}
