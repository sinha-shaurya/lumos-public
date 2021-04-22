package com.example.lumos.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.local.LocalUser
import com.example.lumos.network.dataclasses.login.LoginUserData
import com.example.lumos.network.dataclasses.login.UserData
import com.example.lumos.network.dataclasses.practice.Answer
import com.example.lumos.network.dataclasses.practice.AnswerResponse
import com.example.lumos.network.dataclasses.practice.Question
import com.example.lumos.network.dataclasses.practice.QuestionResponse
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoadingStatus
import com.example.lumos.utils.LoginStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: NetworkRepository) : ViewModel() {
    val userCurrent = MutableLiveData<UserData>()
    val localData = MutableLiveData<UserData>()
    val loginStatus = MutableLiveData<LoginStatus>()

    val error = MutableLiveData<Exception>()
    val questionStatus = MutableLiveData<LoadingStatus>()
    val questionError = MutableLiveData<Exception>()

    private val _questionResponse = MutableLiveData<QuestionResponse>()
    val questionResponse: LiveData<QuestionResponse> get() = _questionResponse


    //setup livedata wrapper on _questionList so that fragment cannot make changes
    private val _questionList = MutableLiveData<List<Question>>()
    val questionList: LiveData<List<Question>> get() = _questionList

    //livedata wrapper on answer status
    private val _answerStatus = MutableLiveData<LoadingStatus>()
    val answerStatus: LiveData<LoadingStatus> get() = _answerStatus

    //livedata wrapper on answer response
    private val _answerResponse = MutableLiveData<AnswerResponse>()
    val answerResponse: LiveData<AnswerResponse> get() = _answerResponse

    //initialise viewmodel variables
    init {
        Log.i(TAG, "LoginViewModel created")

        //loading status variables
        loginStatus.value = LoginStatus.LOADING
        questionStatus.value = LoadingStatus.LOADING

        userCurrent.value = null
        localData.value = UserData(status = "unsuccessful")

        _questionResponse.value = QuestionResponse(questionList = null, errorDetails = "init")
        error.value = null

        questionError.value = null
        _questionList.value = emptyList()

        _answerResponse.value= AnswerResponse()

        getUserDataCount()
    }


    //Function definition for Login and account fragments
    //function to login user using API
    fun loginUser(username: String, password: String) {
        val loginUserData = LoginUserData(username, password)
        loginStatus.value = LoginStatus.LOADING
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
                //change login status to success
                userCurrent.postValue(receivedUser)
                loginStatus.postValue(LoginStatus.SUCCESS)
                //also trigger database call
                addLocalUser(receivedUser)
            } else if (receivedUser.status.equals("unsuccessful")) {
                Log.i(TAG, "Login failed")
                userCurrent.postValue(receivedUser)
                loginStatus.postValue(LoginStatus.NOT_LOGGED_IN)
                error.postValue(null)

            }
        }
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
    private fun addUser(localUser: LocalUser) {
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
            repository.deleteAllPosts()
            loginStatus.postValue(LoginStatus.NOT_LOGGED_IN)
            _questionResponse.postValue(null)
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


    //Function definitions for QuestionFragment
    //get questions
    fun getQuestions() {
        //check if questions are previously loaded
        if (_questionResponse.value?.questionList?.size ?: -1 >= 0) {
            questionStatus.value = LoadingStatus.SUCCESS
        } else {
            viewModelScope.launch {
                //handle HTTP Errors
                val headerMap = mutableMapOf<String, String?>()
                headerMap["Authorization"] = async {
                    //can be replaced with localData value of token for faster access
                    val token = repository.getAuthToken()
                    if (token == null)
                        null
                    else
                        "Token $token"
                }.await()
                //do not perform network request if auth token is not available
                if (headerMap["Authorization"] != null) {
                    val response =
                        async {
                            try {
                                repository.getQuestions(headerMap)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                questionStatus.postValue(LoadingStatus.FAILURE)
                                questionError.postValue(e)
                                QuestionResponse()
                            }
                        }.await()
                    _questionResponse.postValue(response)
                    _questionList.postValue(response.questionList)
                    //check if response has not failed
                    if (response.questionList != null)
                        questionStatus.postValue(LoadingStatus.SUCCESS)
                    else
                        questionStatus.postValue(LoadingStatus.FAILURE)
                }
                //in case authorization token is not available
                else
                    questionStatus.postValue(LoadingStatus.FAILURE)
            }
        }
    }


    //Function definitions for Answer Fragment
    fun removeItem(item: Question) =
        viewModelScope.launch(Dispatchers.Default) {
            val list = _questionList.value!!.toCollection(mutableListOf())
            list.remove(item)
            _questionList.postValue(list)
        }

    fun submitAnswer(item: Answer) {
        viewModelScope.launch(Dispatchers.IO) {
            _answerStatus.postValue(LoadingStatus.LOADING)
            val token =
                repository.getAuthToken()


            //do not try to initiate network request if auth token is not found
            if (token != null){
                //construct headers for POST request
                val headerMap= mutableMapOf<String,String>()
                headerMap["Authorization"]= "Token $token"

                //set response value with appropriate error message if network request fails
                val response=try{
                    Log.i(TAG,"Header: $headerMap Answer $item")
                    repository.submitAnswer(headerMap,item)
                }
                catch (e:Exception){
                    AnswerResponse(errors = e.message?:"Unknown error occurred")
                }
                _answerResponse.postValue(response)
                if(response.status.equals("successful",ignoreCase = true)){
                    //set loading status to successful
                    _answerStatus.postValue(LoadingStatus.SUCCESS)
                }
                else
                    _answerStatus.postValue(LoadingStatus.FAILURE)
            }
            //set status to failed if auth token is not found
            else
                _answerStatus.postValue(LoadingStatus.FAILURE)
        }
    }

    fun setAnswerState(state:LoadingStatus){
        _answerStatus.value=state
    }


    //refresh question list as called by swipe to refresh
    fun refreshQuestionList(){
        //launch coroutine in IO Dispatcher
        viewModelScope.launch(Dispatchers.IO) {
            //invalidate the list first
            withContext(Dispatchers.Main){
                //invalidate
                _questionResponse.value = QuestionResponse(questionList = null, errorDetails = "init")
                //_questionList.value= emptyList()
                error.value = null
                questionError.value = null
            }

            //get authentication token to receive response from server
            val token=repository.getAuthToken()
            //if token is present only then try to fetch questions
            if(token!=null){
                //now construct headermap so that auth token can be sent
                val headerMap= mutableMapOf<String,String?>()
                headerMap["Authorization"]="Token $token"

                val response=try {
                    repository.getQuestions(headerMap)
                }
                catch (e:Exception){
                    //in case of any exception set loading status to failure
                    questionStatus.postValue(LoadingStatus.FAILURE)
                    questionError.postValue(e)
                    QuestionResponse()
                }

                //set results for response status
                if(response.questionStatus.equals("successful",ignoreCase = true)){
                    _questionResponse.postValue(response)
                    _questionList.postValue(response.questionList)
                }
                if (response.questionList != null)
                    questionStatus.postValue(LoadingStatus.SUCCESS)
                else
                    questionStatus.postValue(LoadingStatus.FAILURE)
            }
            //in case of failure to get auth token set status to failed
            else
                questionStatus.postValue(LoadingStatus.FAILURE)

        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "LoginViewModel destroyed")
    }

    companion object {
        const val TAG = "LoginViewModel"
    }

}