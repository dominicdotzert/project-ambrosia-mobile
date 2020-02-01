package com.projectambrosia.ambrosia.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    fun insert(obj: T)

    @Insert
    fun insert(list: List<T>)

    @Insert
    fun insert(vararg obj: T)

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)

    @Delete
    fun delete(list: List<T>)

    @Delete
    fun delete(vararg obj: T)
}