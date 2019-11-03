package me.hacket.plugins.dingtalk.api

/**
 * https://developers.dingtalk.com/document/robots/custom-robot-access/title-nfv-794-g71#section-e4x-4y8-9k0
 */
abstract class DingRequest {

//    {
//        "msgtype": "markdown",
//        "markdown": {
//        "title":"杭州天气",
//        "text": "#### 杭州天气 @150XXXXXXXX \n > 9度，西北风1级，空气良89，相对温度73%\n > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n > ###### 10点20分发布 [天气](https://www.dingtalk.com) \n"
//        },
//        "at": {
//            "atMobiles": [
//                    "150XXXXXXXX"
//            ],
//            "atUserIds": [
//                    "user123"
//            ],
//            "isAtAll": false
//        }
//    }

    /**
     *
     * 消息类型，有如下几种：
     *
     * text: text类型
     *
     * markdown: markdown类型
     *
     * link: link类型
     *
     * actionCard: 整体跳转ActionCard类型/独立跳转ActionCard类型
     *
     * feedCard: FeedCard类型
     *
     */
    public String msgtype

    static DingRequestMarkdown markdown(String title, String text, List<String> at) {
        DingRequestMarkdown request = new DingRequestMarkdown()
        request.msgtype = "markdown"

        DingRequestMarkdownContent content = new DingRequestMarkdownContent()
        content.title = title
        content.text = text
        request.markdown = content

        if (at != null && !at.isEmpty()) {
            DingRequestAt dingRequestAt = new DingRequestAt()
            dingRequestAt.setAt(at)
            request.at = dingRequestAt
        }
        return request
    }
}

// ======  markdown ======

class DingRequestMarkdown extends DingRequest {

    public DingRequestMarkdownContent markdown
    public DingRequestAt at

    @Override
    String toString() {
        return Utils.sGson.toJson(this)
    }
}

class DingRequestMarkdownContent {
    public String title // 首屏会话透出的展示内容。
    public String text // markdown格式的消息。
    @Override
    String toString() {
        return Utils.sGson.toJson(this)
    }
}

class DingRequestAt {
    public List<String> atMobiles // 被@人的手机号。注意 在text内容里要有@人的手机号。
    public List<String> atUserIds // 被@人的用户userid。
    public boolean isAtAll // 是否@所有人。

    void setAt(List<String> at) {
        this.atMobiles = at
    }

    @Override
    String toString() {
        return Utils.sGson.toJson(this)
    }
}


// ======  actionCard ======
class DingRequestActionCard extends DingRequest {

    public DingRequestActionCardContent actionCard
}

class DingRequestActionCardContent {
    public String title;
    public String text;
    public int hideAvatar = 0;
    public int btOrientation = 0;
    public String singleTitle;
    public String singleURL;
}
