package org.example.bugtrackerlabjuv25g;

public enum Development {
    FRONTEND,
    BACKEND;

    public static Development fromString(String area){
        if(area == null)
            return Development.BACKEND;
        if(area.equalsIgnoreCase("frontend")){
            return Development.FRONTEND;
        }else{
            return Development.BACKEND;
        }
    }
}
