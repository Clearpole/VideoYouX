package com.clearpole.videoyoux._utils

class SubStringX {
    companion object{
        fun String.subStringX(beforeString: String?, afterString: String?): String? {
            return if (beforeString == null && afterString != null) {
                this.substring(0, this.indexOf(afterString))
            } else if (beforeString != null && afterString == null) {
                this.substring(this.indexOf(beforeString) + beforeString.length, this.length)
            } else if (beforeString != null && afterString != null) {
                this.subStringX(beforeString, null)!!.subStringX(null, afterString)
            } else {
                null
            }
        }
    }
}