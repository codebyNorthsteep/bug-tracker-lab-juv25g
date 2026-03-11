package org.example.bugtrackerlabjuv25g;

public enum DevelopmentArea {
    FRONTEND,
    BACKEND;

    public static DevelopmentArea fromString(String area){
        if(area == null)
            return DevelopmentArea.BACKEND;
        if(area.equalsIgnoreCase("frontend")){
            return DevelopmentArea.FRONTEND;
        }else{
            return DevelopmentArea.BACKEND;
        }
    }
}
