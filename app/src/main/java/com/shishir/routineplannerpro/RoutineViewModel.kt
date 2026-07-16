
package com.shishir.routineplannerpro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.shishir.routineplannerpro.data.Activity
import com.shishir.routineplannerpro.data.AppDatabase
import com.shishir.routineplannerpro.data.Routine
import com.shishir.routineplannerpro.data.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "routine-database"
    ).build()
    
    private val repository = RoutineRepository(db.routineDao())
    
    val routines: Flow<List<Routine>> = repository.allRoutines
    val allActivities: Flow<List<Activity>> = repository.allActivities

    fun addActivity(activity: Activity) {
        viewModelScope.launch { repository.insertActivity(activity) }
    }

    fun addRoutine(routine: Routine) {
        viewModelScope.launch { repository.insertRoutine(routine) }
    }
}
