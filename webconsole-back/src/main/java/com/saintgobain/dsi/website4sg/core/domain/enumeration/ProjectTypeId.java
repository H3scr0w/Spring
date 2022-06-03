package com.saintgobain.dsi.website4sg.core.domain.enumeration;

import com.saintgobain.dsi.website4sg.core.exception.NotImplementedException;

public enum ProjectTypeId {
    D_DOCROOTCORE("ddc"), D_WEBSITE("w");

    private final String name;

    ProjectTypeId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ProjectTypeId getEnum(String value) throws NotImplementedException {
        for (ProjectTypeId v : values())
            if (v.getName().equalsIgnoreCase(value))
                return v;
        throw new NotImplementedException();
    }
}
