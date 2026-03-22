package org.example.bugtrackerlabjuv25g.model;

/**
 * Represents the areas of software development in a system.
 * This enumeration contains the possible development areas, such as FRONTEND and BACKEND.
 * It also provides utility methods to work with these areas.
 */
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
