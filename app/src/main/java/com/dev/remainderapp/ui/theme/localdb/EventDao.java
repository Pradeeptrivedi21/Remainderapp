package com.dev.remainderapp.ui.theme.localdb;




import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insertAll(EntityClass entityClass);

    @Query("SELECT * FROM BIRTHDAY_TABLE")
    List<EntityClass> getAllData();

    @Query("DELETE FROM BIRTHDAY_TABLE WHERE columfriend_name = :name")
    void deleteById(String name);

}
