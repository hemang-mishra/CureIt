package com.example.finflow.room

class DebitAppsRepository(val dao:DebitAppDAO) {

    val allDebitApps = dao.getAllAppsInDB()

    suspend fun insert(entity: EnjoyEntity): Long{
        return dao.insert(entity)
    }

    suspend fun delete(entity: EnjoyEntity){
        return dao.deleteDebit(entity)
    }

    suspend fun update(entity: EnjoyEntity){
        return dao.updateDebit(entity)
    }

    suspend fun deleteAll(){
        return dao.deleteAll()
    }
}