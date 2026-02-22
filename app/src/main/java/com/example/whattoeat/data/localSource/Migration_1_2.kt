package com.example.whattoeat.data.localSource

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration_1_2: Migration(startVersion = 1, endVersion = 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.delete(
            table = "cached_recipes_complex",
            whereArgs = null,
            whereClause = null
        )
    }
}