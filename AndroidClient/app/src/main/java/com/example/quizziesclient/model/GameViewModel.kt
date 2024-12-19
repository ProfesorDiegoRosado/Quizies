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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompMessage

private const val START_EVENT_NAME_TYPE = "StartGame"

private const val QUESTION_EVENT_NAME_TYPE = "Question"

class GameViewModel: ViewModel() {

    //val SERVER = "alumnoportada.com.es"
    val SERVER = "10.0.2.2"

    val TAG: String = "MainActivity"
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _uiState = MutableStateFlow( GameUiState() )
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow() // export as read-only variable


    /*
    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT))
        .build()
     */

    //val okHttpClient: OkHttpClient = OkHttpClient(builder)

    var mStompClient: StompClient = Stomp.over(
        Stomp.ConnectionProvider.OKHTTP,
        "ws://$SERVER:8080/quizies" //,
        //null, // headers
        //okHttpClient
    )

    init {
        mStompClient.connect()

        val subscribe: Disposable = mStompClient.topic("/topic/gameevent").subscribe { topicMessage: StompMessage ->
            val jsonString = topicMessage.payload
            val data: StartGameInputMessage = Gson().fromJson<StartGameInputMessage>(jsonString, StartGameInputMessage::class.java)

            when(data.type) {
                START_EVENT_NAME_TYPE -> {
                    Log.i("StartGame Event", data.toString())
                    // _uiState.value = _uiState.value.updateCategoriesCopy(data.categories.map{ it.getGameCategory() })
                    _uiState.update {
                        it.copy(gameCategories = data.categories.map{ it.getGameCategory() } )
                    }
                    loadNextQuestionEvent(uiState.value.gameCategories.map { it.name })
                }
                QUESTION_EVENT_NAME_TYPE -> {
                    Log.i("Question Event", data.toString())
                    //_uiState.value = _uiState.value.updateQuestionCopy(data.question)
                    _uiState.update { currentState ->
                        currentState.copy(currentQuestion = data.question)
                    }
                    Log.i("Question Event", data.toString())
                }
                else -> {
                    throw Exception("${data.type} is not a valid type")
                }
            }

            Log.d(TAG, topicMessage.getPayload())
        }
    }

    fun sendStartEvent() {
        sendEvent(START_EVENT_NAME_TYPE, null)
    }

    fun loadNextQuestionEvent(questionsCategories: List<String>) {
        val categoriesString = "[" + questionsCategories.joinToString("\",\"", "\"", "\"") + "]"
        sendEvent(QUESTION_EVENT_NAME_TYPE, categoriesString)
    }

    private fun sendEvent(event: String, arguments: String?) {
        val dataString: String =
            if (arguments==null) {
                """{"event": "$event"}"""
            } else {
                """{"event": "$event",
                            "arguments": $arguments } """
            }

        viewModelScope.launch {
            compositeDisposable.add(
                mStompClient.send(
                    "/app/gameevent",
                    dataString
                )
                    .doOnError {
                            e -> Log.e(TAG, "error $e")
                    }
                    .subscribe({
                        Log.i(TAG, "STOMP echo send successfully")
                    }, { throwable: Throwable ->
                        Log.e(TAG, "Error send STOMP echo", throwable)
                    })
            )

            val connected: Boolean = mStompClient.isConnected

            Log.d("GameViewMode: Send event", "Done ....")
        }
    }

    fun incRound() {
        _uiState.update {
            it.copy(round = _uiState.value.round+1)
        }
    }

    fun gameDone() {
        _uiState.update {
            it.copy(gameDone = true)
        }
    }

}