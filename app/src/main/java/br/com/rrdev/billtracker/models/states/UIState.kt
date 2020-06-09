package br.com.rrdev.billtracker.models.states

import androidx.annotation.StringRes

sealed class UIState

object Loading : UIState()

class LoadingProgress(val progress: Int): UIState()

object CompleteWithNoData : UIState()

class Complete<T>(val data: T? = null) : UIState()

class Error(@StringRes val errorMsgId: Int) : UIState()