package org.superboot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b> HTML工具类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class HtmlUtils {
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    private static final String regEx_html = "<[^>]+>";

    /**
     * 清除HTML标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", 2);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");

        Pattern p_style = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>", 2);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");

        Pattern p_html = Pattern.compile("<[^>]+>", 2);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");

        return htmlStr.trim();
    }

    /**
     * 转换特殊的HTML标签
     *
     * @param content
     * @return
     */
    public static String changeTag(String content) {
        content = content.replaceAll("&", "&amp;");
        content = content.replaceAll("<", "&lt;");
        content = content.replaceAll(">", "&gt;");
        content = content.replaceAll("\"", "&quot;");
        content = content.replaceAll("'", "&#x27;");
        content = content.replaceAll("/", "&#x2f;");
        return content;
    }
}
