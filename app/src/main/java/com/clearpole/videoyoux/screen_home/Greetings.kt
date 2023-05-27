package com.clearpole.videoyoux.screen_home

import com.blankj.utilcode.util.TimeUtils

class Greetings {
    companion object {
        fun text(): String {
            return when (TimeUtils.getChineseWeek(TimeUtils.getNowDate())) {
                "周日" -> {
                    when ((0..3).random()) {
                        0 -> "明 天 周 一"
                        1 -> "工作做了吗"
                        2 -> "哼哼啊啊啊啊"
                        else -> "天气如何？"
                    }
                }

                "周六" -> {
                    when ((0..3).random()) {
                        0 -> "出去玩吧！"
                        1 -> "风景正好！"
                        2 -> "还好明天周日"
                        else -> "没事，还有一天时间~"
                    }
                }

                "周五" -> {
                    when ((0..3).random()) {
                        0 -> "坚持即胜利"
                        1 -> "这一周过得真快..."
                        2 -> "其实还不如直接放假"
                        else -> "再摆一天就好了（倒"
                    }
                }

                "周四" -> {
                    when ((0..3).random()) {
                        0 -> "怎么还有一天？！"
                        1 -> "也就还剩一天了"
                        2 -> "亚历山大"
                        else -> "今天周四"
                    }
                }

                "周三" -> {
                    when ((0..3).random()) {
                        0 -> "一半一半嘛"
                        1 -> "动力加持中"
                        2 -> "驼峰日快乐"
                        else -> "耐力赛而已"
                    }
                }

                "周二" -> {
                    when ((0..3).random()) {
                        0 -> "稳步推进ing"
                        1 -> "失去梦想的咸鱼"
                        2 -> "数数还剩几天"
                        else -> "周二怎么这么累"
                    }
                }

                "周一" -> {
                    when ((0..3).random()) {
                        0 -> "卷死我了"
                        1 -> "/RESTART"
                        2 -> "加油加油加油"
                        else -> "周一忧郁症‍️"
                    }
                }

                else -> "你貌似不在地球"
            }
        }
    }
}