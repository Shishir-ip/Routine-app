
package com.shishir.routineplannerpro.data

import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val dao: RoutineDao) {
    val allRoutines: Flow<List<Routine>> = dao.getAllRoutines()
    val allActivities: Flow<List<Activity>> = dao.getAllActivities()

    fun getActivitiesForRoutine(id: Long): Flow<List<Activity>> = dao.getActivitiesForRoutine(id)

    suspend fun insertRoutine(routine: Routine) = dao.insertRoutine(routine)
    suspend fun deleteRoutine(routine: Routine) = dao.deleteRoutine(routine)
    suspend fun insertActivity(activity: Activity) = dao.insertActivity(activity)
    suspend fun updateActivity(activity: Activity) = dao.updateActivity(activity)
    suspend fun deleteActivity(activity: Activity) = dao.deleteActivity(activity)
}
