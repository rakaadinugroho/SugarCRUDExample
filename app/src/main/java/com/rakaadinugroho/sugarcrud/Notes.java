package com.rakaadinugroho.sugarcrud;

import com.orm.SugarRecord;

/**
 * Created by Raka Adi Nugroho on 1/27/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class Notes extends SugarRecord {
    String title;
    String content;
    long time;

    public Notes(){

    }
    public Notes(String title, String content, long time){
        this.title  = title;
        this.content = content;
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }
}
