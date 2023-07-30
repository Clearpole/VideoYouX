package com.clearpole.videoyoux._utils

import android.content.Context
import com.blankj.utilcode.util.TimeUtils
import com.clearpole.videoyoux.R

class Greetings {
    companion object {
        fun text(context:Context): String {
            return when (TimeUtils.getChineseWeek(TimeUtils.getNowDate())) {
                "周日" -> {
                    when ((0..3).random()) {
                        0 -> context.getString(R.string.zr_0)
                        1 -> context.getString(R.string.zr_1)
                        2 -> context.getString(R.string.zr_2)
                        else -> context.getString(R.string.zr_3)
                    }
                }

                "周六" -> {
                    when ((0..3).random()) {
                        0 -> context.getString(R.string.z6_1)
                        1 -> context.getString(R.string.z6_2)
                        2 -> context.getString(R.string.z6_3)
                        else -> context.getString(R.string.z6_4)
                    }
                }

                "周五" -> {
                    when ((0..3).random()) {
                        0 -> context.getString(R.string.z5_1)
                        1 -> context.getString(R.string.z5_2)
                        2 -> context.getString(R.string.z5_3)
                        else -> context.getString(R.string.z5_4)
                    }
                }

                "周四" -> {
                    when ((0..3).random()) {
                        0 -> context.getString(R.string.z4_1)
                        1 -> context.getString(R.string.z4_2)
                        2 -> context.getString(R.string.z4_3)
                        else -> context.getString(R.string.z4_4)
                    }
                }

                "周三" -> {
                    when ((0..3).random()) {
                        0 -> context.getString(R.string.z3_1)
                        1 -> context.getString(R.string.z3_2)
                        2 -> context.getString(R.string.z3_3)
                        else -> context.getString(R.string.z3_4)
                    }
                }

                "周二" -> {
                    when ((0..3).random()) {
                        0 -> context.getString(R.string.z2_1)
                        1 -> context.getString(R.string.z2_2)
                        2 -> context.getString(R.string.z2_3)
                        else -> context.getString(R.string.z2_4)
                    }
                }

                "周一" -> {
                    when ((0..3).random()) {
                        0 -> context.getString(R.string.z1_1)
                        1 -> context.getString(R.string.z1_2)
                        2 -> context.getString(R.string.z1_3)
                        else -> context.getString(R.string.z1_4)
                    }
                }

                else -> context.getString(R.string.seem_null)
            }
        }
    }
}