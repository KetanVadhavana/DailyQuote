package com.example.dailyquote.data.local

import android.content.Context

class SharedPreferences(context: Context) {

    private var mPref = context.getSharedPreferences("daily_quote", Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        val mEditor = mPref.edit()
        mEditor.putString(key, value)
        mEditor.apply()
    }

    fun getString(key: String): String? {
        return mPref.getString(key, null)
    }

    fun putBoolean(key: String, value: Boolean) {
        val mEditor = mPref.edit()
        mEditor.putBoolean(key, value)
        mEditor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return mPref.getBoolean(key, false)
    }

}