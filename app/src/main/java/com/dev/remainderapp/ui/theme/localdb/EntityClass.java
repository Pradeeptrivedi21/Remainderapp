package com.dev.remainderapp.ui.theme.localdb;


import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_DATE;
import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_EVENT_NOTES;
import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_EVENT_TIME;
import static com.dev.remainderapp.apputils.appconfig.DATABASE_TABLE;
import static com.dev.remainderapp.apputils.appconfig.FRIEND_NAME;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =DATABASE_TABLE)
public class EntityClass {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = FRIEND_NAME)
    String friendname;
    @ColumnInfo(name =BIRTHDAY_DATE)
    String birthdaydate;
    @ColumnInfo(name =BIRTHDAY_EVENT_TIME)
    String eventtime;
    @ColumnInfo(name =BIRTHDAY_EVENT_NOTES)
    String birthday_notes;


    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public String getBirthdaydate() {
        return birthdaydate;
    }

    public void setBirthdaydate(String birthdaydate) {
        this.birthdaydate = birthdaydate;
    }

    public String getBirthday_notes() {
        return birthday_notes;
    }

    public void setBirthday_notes(String birthday_notes) {
        this.birthday_notes = birthday_notes;
    }

    public String getEventtime() {
        return eventtime;
    }

    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }
}
