package me.hacket.plugins.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.gradle.api.Project

import java.lang.reflect.Type
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class Utils {
    public static final String LOG_PREFIX = "------------>>>>>>>>>>>>"
    public static final String LOG_SUFFIX = "------------<<<<<<<<<<<<"

    public static final String br = "\n\n"

    public static Gson sGson = new Gson()

    static void log(String msg) {
        println("${LOG_PREFIX} $msg ${LOG_SUFFIX}")
    }

    static <T> HashMap<String, String> toMap(T bodyRequest) {
        if (bodyRequest == null) {
            return new HashMap<>()
        }
        String bodyJson = new Gson().toJson(bodyRequest)
        Type type = new TypeToken<HashMap<String, String>>() { // 为HashMap<String, T>会报错
        }.getType()
        return Utils.sGson.fromJson(bodyJson, type)
    }

    /**
     * 是否有xxx插件
     * @param project
     * @param pluginId
     * @return
     */
    static boolean hasPlugin(Project project, String pluginId) {
        return project.plugins.hasPlugin(pluginId)
    }

    /**
     * 格式化apk大小为MB，保留2位小数
     * @param size
     * @return
     */
    static String formatFileSize(long size) {
        // App 文件大小
        def buildFileSize = size / (1024F * 1024F)
        def decimalFormat = new DecimalFormat(".00")
        def buildFileSizeStr = decimalFormat.format(buildFileSize)
        return buildFileSizeStr + "M"
    }

    /**
     * 获取当前时间
     * @return
     */
    static def getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        Calendar lastDate = Calendar.getInstance()
        return sdf.format(lastDate.getTime())
    }

    /**
     * 获取git当前HEAD的revision
     * @return
     */
    static def getGitRevision() {
        return 'git rev-parse --short HEAD'.execute().text.trim()
    }

    /**
     * 获取git当前分支名
     * @return
     */
    static def getGitBranch() {
        return 'git rev-parse --abbrev-ref --symbolic-full-name @{u}'.execute().text.trim()
    }

    /**
     * 获取appName
     */
    static def getAppName(Project project) {
        try {
            def stringsFile = new File("${project.projectDir}/src/res/values/strings.xml")
            def parser = new XmlParser()
            parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
//        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
            def root = parser.parse(stringsFile)
            def iterator = root.iterator()
            while (iterator.hasNext()) {
                def it = iterator.next()
                if (it.@name == "app_name") {
                    println("----------- 获取到appName=${it.text()}.")
                    return it.text()
                }
            }
            println("----------- 未获取到appName=${stringsFile}, exists=${stringsFile.exists()}")
        } catch (Exception e) {
            println("----------- 获取appName失败=${e.getMessage()}.")
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Compares two version strings. // from walle
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("-")[0].split("\\.");
        String[] vals2 = str2.split("-")[0].split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }

        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }

    /**
     * get Android Gradle Plugin version // from walle
     * @return  agp version eg: 2.2.0
     */
    static String getAndroidGradlePluginVersion() {
        String version = null
        try {
            def clazz = Class.forName("com.android.builder.Version")
            def field = clazz.getDeclaredField("ANDROID_GRADLE_PLUGIN_VERSION")
            field.setAccessible(true)
            version = field.get(null)
        } catch (ClassNotFoundException ignore) {
        } catch (NoSuchFieldException ignore) {
        }
        if (version == null) {
            try {
                def clazz = Class.forName("com.android.builder.model.Version")
                def field = clazz.getDeclaredField("ANDROID_GRADLE_PLUGIN_VERSION")
                field.setAccessible(true)
                version = field.get(null)
            } catch (ClassNotFoundException ignore) {
            } catch (NoSuchFieldException ignore) {
            }
        }
        return version
    }

}

