package org.stevetribe.sidewalkpaver.template;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class Templates {
    static Map<String, Template> templates = new HashMap<>();   // <userName, templates>

    @Nullable
    static public Template getTemplateByName(String name) {
        System.out.println(templates);

        return templates.get(name);
    }

    static public void addNewTemplate(String name, Template template) {
        if(templates.get(name) != null) {
            templates.remove(name);
        }
        templates.put(name, template);
        System.out.println(templates);
    }
}
