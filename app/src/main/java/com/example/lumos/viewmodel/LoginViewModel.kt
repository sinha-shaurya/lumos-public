package com.example.lumos.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.local.LocalUser
import com.example.lumos.network.dataclasses.login.LoginUserData
import com.example.lumos.network.dataclasses.login.UserData
import com.example.lumos.network.dataclasses.practice.QuestionResponse
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoadingStatus
import com.example.lumos.utils.LoginStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: NetworkRepository) : ViewModel() {
    val userCurrent = MutableLiveData<UserData>()
    val localData = MutableLiveData<UserData>()
    val loginStatus = MutableLiveData<LoginStatus>()
    val questionResponse = MutableLiveData<QuestionResponse>()
    val error = MutableLiveData<Exception>()
    val questionStatus=MutableLiveData<LoadingStatus>()

    init {
        Log.i(TAG, "LoginViewModel created")
        userCurrent.value = null
        localData.value = UserData(status = "unsuccessful")
        loginStatus.value = LoginStatus.LOADING
        questionResponse.value = null
        error.value = null
        questionStatus.value=LoadingStatus.LOADING
        getUserDataCount()
    }

    //function to login user using API
    fun loginUser(username: String, password: String) {
        val loginUserData = LoginUserData(username, password)
        loginStatus.value=LoginStatus.LOADING
        //run coroutine on IO Dispatcher
        viewModelScope.launch(Dispatchers.IO) {

            //run async but await till result is available
            val receivedUser = async {
                try {
                    //catch all exceptions related to network request
                    //loginStatus.postValue(LoginStatus.LOADING)
                    repository.loginUser(loginUserData)
                } catch (e: Exception) {
                    error.postValue(e)
                    loginStatus.postValue(LoginStatus.FAILURE)
                    UserData(status = "failed")
                }
            }.await()
            //user has successfully logged in
            if (receivedUser.status.equals("successful")) {
                //change login status
                userCurrent.postValue(receivedUser)
                loginStatus.postValue(LoginStatus.SUCCESS)
                //also trigger database call
                addLocalUser(receivedUser)
            } else if (receivedUser.status.equals("unsuccessful")) {
                Log.i(TAG, "Login failed")
                userCurrent.postValue(receivedUser)
                loginStatus.postValue(LoginStatus.FAILURE)
                error.postValue(null)

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "LoginViewModel destroyed")
    }

    //get details of user stored if any
    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val localUser = repository.getUserData()
            val user = UserData(
                localUser.status,
                localUser.token,
                localUser.userName,
                localUser.email,
                localUser.firstName,
                localUser.lastName
            )
            localData.postValue(user)
        }
    }

    //check if userdata is available
    fun getUserDataCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = repository.getUserDataCount()
            if (users >= 1)
                loginStatus.postValue(LoginStatus.SUCCESS)
            else
                loginStatus.postValue(LoginStatus.NOT_LOGGED_IN)

        }
    }

    //call repository function to add new user to database
    fun addUser(localUser: LocalUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(localUser)
        }
    }

    //log out user from database and destroy all cached data
    fun logoutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val localUser = async {
                repository.getUserData()
            }.await()
            async {
                repository.logoutUser(localUser)
            }.await()
            loginStatus.postValue(LoginStatus.NOT_LOGGED_IN)
            questionResponse.postValue(null)
            questionStatus.postValue(LoadingStatus.LOADING)
        }
    }

    //add to local user
    fun addLocalUser(receivedUser: UserData) {
        val localUser = LocalUser(
            0,
            receivedUser.status,
            receivedUser.token!!,
            receivedUser.userName!!,
            receivedUser.email!!,
            receivedUser.firstName!!,
            receivedUser.lastName!!
        )
        addUser(localUser)
    }

    //get questions
    fun getQuestions() {
        //check if questions are previously loaded
        if(questionResponse.value?.questionList?.size ?:-1 >0){
            questionStatus.value=LoadingStatus.SUCCESS
        }
        else{
            viewModelScope.launch {
                //handle HTTP Errors
                val headerMap = mutableMapOf<String, String?>()
                headerMap["Authorization"] = async {
                    val token = repository.getAuthToken()
                    if (token == null)
                        null
                    else
                        "Token " + token
                }.await()
                if (headerMap["Authorization"] != null) {
                    val response =
                        async {
                            try {
                                repository.getQuestions(headerMap)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                questionStatus.postValue(LoadingStatus.FAILURE)
                                QuestionResponse()
                            }
                        }.await()
                    questionResponse.postValue(response)
                    //check if response has not failed
                    if(response.questionList!=null)
                        questionStatus.postValue(LoadingStatus.SUCCESS)
                    else
                        questionStatus.postValue(LoadingStatus.FAILURE)
                }
                else
                    questionStatus.postValue(LoadingStatus.FAILURE)
            }
        }
    }


    companion object {
        const val TAG = "LoginViewModel"
    }

}