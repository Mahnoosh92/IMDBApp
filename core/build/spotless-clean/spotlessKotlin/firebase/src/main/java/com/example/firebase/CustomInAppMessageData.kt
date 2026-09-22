package com.example.firebase

import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks
import com.google.firebase.inappmessaging.model.Action
import com.google.firebase.inappmessaging.model.BannerMessage
import com.google.firebase.inappmessaging.model.CardMessage
import com.google.firebase.inappmessaging.model.ImageOnlyMessage
import com.google.firebase.inappmessaging.model.InAppMessage
import com.google.firebase.inappmessaging.model.ModalMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CustomInAppMessageData(
    val title: String?,
    val body: String?,
    val imageUrl: String?,
    val actionUrl: String?,
    val rawMessage: InAppMessage,
    val callbacks: FirebaseInAppMessagingDisplayCallbacks,
)

object CustomInAppMessagingManager : FirebaseInAppMessagingDisplay {

    private val _currentMessage = MutableStateFlow<CustomInAppMessageData?>(null)
    val currentMessage = _currentMessage.asStateFlow()

    fun init() {
        FirebaseInAppMessaging.getInstance().setMessageDisplayComponent(this)
    }

    override fun displayMessage(inAppMessage: InAppMessage, callbacks: FirebaseInAppMessagingDisplayCallbacks) {
        _currentMessage.value = CustomInAppMessageData(
            title = inAppMessage.extractedTitle,
            body = inAppMessage.extractedBody,
            imageUrl = inAppMessage.extractedImageUrl,
            actionUrl = inAppMessage.primaryAction?.actionUrl,
            rawMessage = inAppMessage,
            callbacks = callbacks,
        )

        callbacks.impressionDetected()
    }

    fun dismissMessage(messageData: CustomInAppMessageData, dismissType: FirebaseInAppMessagingDisplayCallbacks.InAppMessagingDismissType = FirebaseInAppMessagingDisplayCallbacks.InAppMessagingDismissType.CLICK) {
        messageData.callbacks.messageDismissed(dismissType)
        _currentMessage.value = null
    }

    fun handleActionClick(messageData: CustomInAppMessageData) {
        val action = messageData.rawMessage.primaryAction
        if (action != null) {
            messageData.callbacks.messageClicked(action)
        } else {
            messageData.callbacks.messageDismissed(
                FirebaseInAppMessagingDisplayCallbacks.InAppMessagingDismissType.CLICK,
            )
        }

        _currentMessage.value = null
    }
}

// Private extensions to keep mapping clean and localized
private val InAppMessage.extractedTitle: String?
    get() = when (this) {
        is ModalMessage -> title?.text
        is CardMessage -> title?.text
        is BannerMessage -> title?.text
        else -> null
    }

private val InAppMessage.extractedBody: String?
    get() = when (this) {
        is ModalMessage -> body?.text
        is CardMessage -> body?.text
        is BannerMessage -> body?.text
        else -> null
    }

private val InAppMessage.extractedImageUrl: String?
    get() = when (this) {
        is ModalMessage -> imageData?.imageUrl
        is CardMessage -> portraitImageData?.imageUrl ?: landscapeImageData?.imageUrl
        is BannerMessage -> imageData?.imageUrl
        is ImageOnlyMessage -> imageData?.imageUrl
        else -> null
    }

private val InAppMessage.primaryAction: Action?
    get() = when (this) {
        is ModalMessage -> action
        is CardMessage -> primaryAction
        is BannerMessage -> action
        is ImageOnlyMessage -> action
        else -> null
    }
