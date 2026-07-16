
package com.shishir.routineplannerpro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String // "Daily", "Class", "Custom"
)

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineId: Long,
    val name: String,
    val startTime: String, // HH:mm
    val endTime: String,   // HH:mm
    val startDate: Long?,  // Epoch millis or null
    val endDate: Long?,    // Epoch millis or null
    val daysOfWeek: String, // e.g., "Mon,Tue,Wed"
    val reminderEnabled: Boolean = false,
    val reminderMinutes: Int = 5,
    val alarmEnabled: Boolean = false,
    val alarmMinutes: Int = 5,
    val detailsJson: String = "{}" // Custom fields like Room, Teacher, etc.
)

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>

    @Insert
    suspend fun insertRoutine(routine: Routine): Long

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Query("SELECT * FROM activities WHERE routineId = :routineId")
    fun getActivitiesForRoutine(routineId: Long): Flow<List<Activity>>
    
    @Query("SELECT * FROM activities")
    fun getAllActivities(): Flow<List<Activity>>

    @Insert
    suspend fun insertActivity(activity: Activity)

    @Update
    suspend fun updateActivity(activity: Activity)

    @Delete
    suspend fun deleteActivity(activity: Activity)
}

@Database(entities = [Routine::class, Activity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
