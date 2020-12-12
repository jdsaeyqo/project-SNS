package com.example.snsproject.navigation.model

data class AlarmDTO (

    var destinationUid : String? = null,
    var userId : String? = null,
    var uid : String? = null,
    var kind : Int? = null,
    // kind : 0 = 좋아요 알람
    // kind : 1 = 댓글 알람
    // kind : 2 = 팔로우 알람
    var message : String? = null,
    var timestamp : Long? = null


)

