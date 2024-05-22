package com.example.finflow.room

import NotificationHelper
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDatabase
import com.example.finflow.transactionHistory.TransactionRepository
import kotlinx.coroutines.launch

class DebitAppsViewModel(private val repository: DebitAppsRepository ): ViewModel(),Observable{

    val debitApps = repository.allDebitApps
    lateinit var contextOfMain : Context
    lateinit var contextOfApplicaton:Context
     var isUpdateOrDelete = false
     lateinit var userToUpdateOrDelete: EnjoyEntity

    @Bindable
    val inputName = MutableLiveData<String?>()
    @Bindable
    val inputDesc = MutableLiveData<String?>()
    @Bindable
    val rate = MutableLiveData<String?>()
    @Bindable
    val inputfreq = MutableLiveData<String?>()
    @Bindable
    val inputTime = MutableLiveData<String?>()
    @Bindable
    val saveOrUpdateButton = MutableLiveData<String?>()
    @Bindable
    val clearAllOrDeleteButton = MutableLiveData<String?>()

    init{
        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"
        rate.value = "175"
        inputTime.value = "1"
        inputfreq.value = "0"
    }

    fun saveOrUpdate(){
        if(isUpdateOrDelete){
            //Make Update
            userToUpdateOrDelete.name = inputName.value!!
            userToUpdateOrDelete.desc = inputDesc.value!!
            userToUpdateOrDelete.rate = rate.value!!.toFloat()
            userToUpdateOrDelete.freq = inputfreq.value!!.toInt()
            update(userToUpdateOrDelete)
        }else{
            val name = inputName.value!!
            val desc = inputDesc.value!!
            val rate2 = rate.value!!
            val freq = inputfreq.value!!
            insert(EnjoyEntity(0,name,desc,rate2.toFloat(), freq = freq.toInt()))//TODO(Why we have used 0 here for id)

            inputName.value = null
            inputDesc.value = null
            rate.value = null
            inputfreq.value = null
        }
    }


    fun insert(enjoyEntity: EnjoyEntity) = viewModelScope.launch {
        repository.insert(enjoyEntity)
    }


    fun use(time: Long){
        if(time != 1L && time != 5L && time != 10L && time != 15L && time != 30L)
            return
        val balance = Calculations(contextOfMain).getBalanceInSharedPref()
        Log.e("Tag", "use: ${userToUpdateOrDelete.name} $balance ${(userToUpdateOrDelete.name == "YouTube" || userToUpdateOrDelete.name == "Youtube")} ${balance < 0.0F}", )
        if(((userToUpdateOrDelete.name?.trim()
                ?: "") == "YouTube" || userToUpdateOrDelete.name == "Youtube") && (balance < 0.0F)
        )
            return
        if(balance < 0 && time >= 15.0F)
            return
        if(isUpdateOrDelete) {
            //TODO("Implement the onClick of the use button")
            //Note the time
            val cal = Calculations(contextOfMain)
            val amt = cal.debitCalculations(time,if(userToUpdateOrDelete.rate==null)
            0.0F else userToUpdateOrDelete.rate.toString().toFloat())
            //Updating the frequency
            var f = inputfreq.value!!.toInt()
            inputfreq.value = (++f).toString()
            //Adding the transaction history
            val dao = TransactionDatabase.getInstance(contextOfApplicaton).transDAOObject
            val repository = TransactionRepository(dao)
            viewModelScope.launch {
                repository.insert(
                    TransEntity(
                        0, userToUpdateOrDelete.name.toString(),userToUpdateOrDelete.rate.toString().toFloat(), time,
                        Logic().currentDateAndTime(), false, amt
                    )
                )
            }
            //send the notification
            val helper=NotificationHelper(contextOfMain)
            helper.showNotification("Open ${userToUpdateOrDelete.name} opening for $time minutes")
            //update the UI
            saveOrUpdate()
        }else if(time != 0L){
            Toast.makeText(contextOfMain,"SELECT THE APP FIRST",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(contextOfMain,"Enter Time first",Toast.LENGTH_SHORT).show()
        }
    }

    fun update(enjoyEntity: EnjoyEntity)= viewModelScope.launch {
        repository.update(enjoyEntity)
        //After updating we are resetting all the variables and making them null

        inputDesc.value = null
        inputName.value = null
        inputfreq.value = null
        rate.value = null

        isUpdateOrDelete = false

        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"
    }

    fun delete(enjoyEntity: EnjoyEntity)= viewModelScope.launch {
        repository.delete(enjoyEntity)

        inputDesc.value = null
        inputName.value = null
        inputfreq.value = null
        rate.value = null
        isUpdateOrDelete = false
        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"

    }


    fun initUpdateOrDelete(enjoyEntity: EnjoyEntity){
        inputName.value = enjoyEntity.name
        inputDesc.value = enjoyEntity.desc
        rate.value = enjoyEntity.rate.toString()
        inputfreq.value = enjoyEntity.freq.toString()
        isUpdateOrDelete = true
        userToUpdateOrDelete = enjoyEntity
        saveOrUpdateButton.value = "Update"
        clearAllOrDeleteButton.value = "Delete"
    }


    //No need to update these because of observable
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}