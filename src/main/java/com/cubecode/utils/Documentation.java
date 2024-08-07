package com.cubecode.utils;

import java.util.List;

public class Documentation {
    public static class Chapter {
        String description;
        List<Method> methods;
    }

    public static class Method {
        public String name;
        public String description;
        public String script;
        public List<Argemunt> arguments;
        public String returnType;

        public Method(String name, String description, String script, List<Argemunt> arguments, String returnType) {
            this.name = name;
            this.description = description;
            this.script = script;
            this.arguments = arguments;
            this.returnType = returnType;
        }
    }

    public static class Argemunt {
        public String name;
        public String type;

        public Argemunt(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }
}
