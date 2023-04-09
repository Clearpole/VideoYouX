package com.clearpole.videoyoux.screen_home

import com.blankj.utilcode.util.TimeUtils

class Greetings {
    companion object {
        fun text(): String {
            return when (TimeUtils.getChineseWeek(TimeUtils.getNowDate())) {
                "周日" -> {
                    when ((0..3).random()) {
                        0 -> "痛苦要降临了😭"
                        1 -> "工作做了吗❓"
                        2 -> "哼哼啊啊啊啊"
                        else -> "天气如何？"
                    }
                }

                "周六" -> {
                    when ((0..3).random()) {
                        0 -> "出去玩吧！🐾"
                        1 -> "风景正好！🪸"
                        2 -> "哈哈哈哈哈✌"
                        else -> "没事，还有一天时间~"
                    }
                }

                "周五" -> {
                    when ((0..3).random()) {
                        0 -> "坚持即胜利✌"
                        1 -> "过得真快..."
                        2 -> "还不如直接放假🤦‍♂️"
                        else -> "再摆一天就好了（倒"
                    }
                }

                "周四" -> {
                    when ((0..3).random()) {
                        0 -> "怎么还有一天？！"
                        1 -> "坚定决心！😁"
                        2 -> "亚历山大😨"
                        else -> "好像看到了点光？😎"
                    }
                }

                "周三" -> {
                    when ((0..3).random()) {
                        0 -> "一半一半嘛😍"
                        1 -> "动力加持中🐣"
                        2 -> "驼峰日快乐😆"
                        else -> "耐力赛而已😎"
                    }
                }

                "周二" -> {
                    when ((0..3).random()) {
                        0 -> "稳步推进ing😣"
                        1 -> "失去梦想的咸鱼"
                        2 -> "想重开了家人们🥴"
                        else -> "疲惫不堪🫠"
                    }
                }

                "周一" -> {
                    when ((0..3).random()) {
                        0 -> "卷死我了🐔"
                        1 -> "/RESTART"
                        2 -> "加油💪加油💪加油"
                        else -> "周一忧郁症😶‍🌫️"
                    }
                }

                else -> "美好的一天💕"
            }
        }
    }
}