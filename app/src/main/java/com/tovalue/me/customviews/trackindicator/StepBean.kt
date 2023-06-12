package com.tovalue.me.customviews.trackindicator

class StepBean {
    var name: String? = null
    var state = 0

    constructor() {}
    constructor(name: String?, state: Int) {
        this.name = name
        this.state = state
    }

    companion object {
        const val STEP_UNDO = -1 //  undo step
        const val STEP_CURRENT = 0 //current step
        const val STEP_COMPLETED = 1 // completed step
    }
}