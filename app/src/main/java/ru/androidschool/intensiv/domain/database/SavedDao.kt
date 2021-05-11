package com.flametech.vaytoday.utils.saved

import androidx.room.*
import androidx.room.Query

@Dao
interface SavedDao {
    //Добавление токена в бд
    @Insert
    fun insertSaved(saved: Saved)

    //Удаление токена
    @Delete
    fun delete(saved: Saved)

    //Получение всех токенов
    @Query("SELECT * FROM Saved")
    fun getAllSaved(): List<Saved>

    @Query("SELECT * FROM Saved WHERE businessId = :businessId")
    fun getByCompany(businessId: Int): Saved?

    //Обновить токен по  айди
    @Update
    fun update(saved: Saved)


}