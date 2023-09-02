package id.gdev.regist.ui.screen.main

object MainRouter {
    const val EVENT = "event"
    const val EVENT_DETAIL = "event_detail"
    const val CREATE_EVENT = "create_event"
    const val DETAIL_PARTICIPANT = "detail_participant"
    const val SETUP = "setup"
}

object MainArg{
    fun getArg(arg: String) = "/{$arg}"
    fun setArg(arg: String) = "/${arg}"
    const val EVENT_ID = "event_id"
    const val PARTICIPANT_ID = "participant_id"
    const val SETUP_ARG = "csv_header"
    const val PARTICIPANT_DATA = "participant_data"
}