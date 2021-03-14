package com.udacity


sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()


    fun next() = when (this) {
        ButtonState.Clicked -> ButtonState.Loading
        ButtonState.Loading -> ButtonState.Completed
        ButtonState.Completed -> ButtonState.Clicked
    }

    fun getLabel() = when (this) {
        ButtonState.Clicked -> "Download"
        ButtonState.Loading -> "File is downloading"
        ButtonState.Completed -> "File is downloaded"
    }
}