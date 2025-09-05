package br.com.tcc.oauth2app.common

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface

fun Context.showCustomDialog(
    title: String,
    message: String,
    cancelable: Boolean,
    positiveBtnMsgId: Int = -1,
    negativeBtnMsgId: Int = -1,
    positiveClickListener: DialogInterface.OnClickListener? = null,
    negativeClickListener: DialogInterface.OnClickListener? = null,
    themeResId: Int? = null
) {
    val builder = themeResId?.let {
        AlertDialog.Builder(this, themeResId)
    } ?: AlertDialog.Builder(this)

    builder.setTitle(title)
    builder.setMessage(message)

    positiveClickListener?.let { listener ->
        builder.setPositiveButton(positiveBtnMsgId, listener)
    }

    negativeClickListener?.let { listener ->
        builder.setNegativeButton(negativeBtnMsgId, listener)
    }

    val dialog = builder.create()
    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(cancelable)
    dialog.show()
}

fun Context.copyToClipboard(label: String, text: String) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
        ClipData.newPlainText(label, text)
    )
}