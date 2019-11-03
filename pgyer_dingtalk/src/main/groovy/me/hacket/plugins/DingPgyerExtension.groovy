package me.hacket.plugins

import org.gradle.api.Project

class DingPgyerExtension {

    /* Parameters required for pugongying upload. */
    public String pgyerApiKey

    /* Parameters required for Send Message to DingTalk */
    public String apiToken
    public List<String> atMobiles

    public String changeLog

    public transient Closure<Boolean> groovyEnableByVariant

    // For Gradle Groovy DSL
    void enableByVariant(Closure<Boolean> selector) {
        groovyEnableByVariant = selector.dehydrate()
    }

    static DingPgyerExtension getConfig(Project project) {
        DingPgyerExtension config = project.getExtensions().findByType(DingPgyerExtension.class)
        if (config == null) {
            config = new DingPgyerExtension(project)
        }
        return config
    }


    boolean isEnable(String variantName) {
        if (groovyEnableByVariant == null) return true
        return groovyEnableByVariant.call(variantName)
    }

    @Override
    String toString() {
        return Utils.sGson.toJson(this)
    }

    DingPgyerExtension() {
    }

}