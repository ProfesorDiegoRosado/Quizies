package com.example.quizziesclient.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizziesclient.ui.GameUiState
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompMessage

class GameViewModel: ViewModel() {

    val TAG: String = "MainActivity"

    //lateinit var mStompClient: StompClient

    var compositeDisposable: CompositeDisposable = CompositeDisposable()


    private var _uiState = MutableStateFlow( GameUiState() )
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow() // export as read-only variable


    var mStompClient: StompClient = Stomp.over(
    Stomp.ConnectionProvider.OKHTTP,
    "ws://10.0.2.2:8080/quizies"
    )
    init {
        mStompClient.connect()

        val subscribe: Disposable = mStompClient.topic("/topic/gameevent").subscribe { topicMessage: StompMessage ->
            val jsonString = topicMessage.payload
            val data: StartGameInputMessage = Gson().fromJson<StartGameInputMessage>(jsonString, StartGameInputMessage::class.java)

            //updateData(data)
            when(data.type) {
                "StartGame" -> {
                    Log.i("StartGame Event", data.toString())
                    _uiState.value.gameCategories = data.categories.map{ it.getGameCategory() }
                    loadNextQuestion(uiState.value.gameCategories.map { it.name })
                }
                "Question" -> {
                    Log.i("Question Event", data.toString())
                    _uiState.value.currentQuestion = data.question
                }
                else -> {
                    throw Exception("${data.type} is not a valid type")
                }
            }

            Log.d(TAG, topicMessage.getPayload())
        }
    }

    fun subscribeToMessages() {

        viewModelScope.launch {

            /*
            mStompClient = Stomp.over(
                Stomp.ConnectionProvider.OKHTTP,
                "ws://10.0.2.2:8080/quizies"
            )
            mStompClient.connect()

            val subscribe: Disposable = mStompClient.topic("/topic/gameevent").subscribe { topicMessage: StompMessage ->
                val jsonString = topicMessage.payload
                val data: StartGameInputMessage = Gson().fromJson<StartGameInputMessage>(jsonString, StartGameInputMessage::class.java)

                //updateData(data)
                when(data.type) {
                    "StartGame" -> {
                        _uiState.value.gameCategories = data.categories.map{ it.getGameCategory() }
                    }
                    "Question" -> {
                        //_uiState.value.currentQuestion = data.question
                    }
                    else -> {
                        throw Exception("${data.type} is not a valid type")
                    }
                }

                Log.d(TAG, topicMessage.getPayload())
            }

             */

            compositeDisposable.add(
                mStompClient.send(
                    "/app/gameevent",
                    """{"event": "StartGame"}""""
                )
                    //.compose(applySchedulers())
                    .doOnError {
                            e -> Log.e(TAG, "error $e")
                    }
                    .subscribe({
                        Log.i(TAG, "STOMP echo send successfully")
                    }, { throwable: Throwable ->
                        Log.e(TAG, "Error send STOMP echo", throwable)
                        //toast(throwable.message)
                    })
            )

            val connected: Boolean = mStompClient.isConnected

            Log.i(TAG, "Done ....")

            //loadNextQuestion(uiState.value.gameCategories.map { it.name })

        }
    }
    fun loadNextQuestion(questions_categories: List<String>) {
        val catetoriesString = "[" + questions_categories.joinToString("\",\"", "\"", "\"") + "]"
        viewModelScope.launch {
            compositeDisposable.add(
                mStompClient.send(
                    "/app/gameevent",
                    """{"event": "Question",
                    "arguments": ${catetoriesString} } """
                )
                    //.compose(applySchedulers())
                    .doOnError {
                            e -> Log.e(TAG, "error $e")
                    }
                    .subscribe({
                        Log.i(TAG, "STOMP echo send successfully")
                    }, { throwable: Throwable ->
                        Log.e(TAG, "Error send STOMP echo", throwable)
                        //toast(throwable.message)
                    })
            )

            val connected: Boolean = mStompClient.isConnected

            Log.i(TAG, "Done ....")
        }
    }

}