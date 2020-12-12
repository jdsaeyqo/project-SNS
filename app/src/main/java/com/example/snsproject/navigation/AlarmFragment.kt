package com.example.snsproject.navigation

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.snsproject.R
import com.example.snsproject.navigation.model.AlarmDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

class AlarmFragment :Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_alarm,container,false)
        view. alarmfragment_recyclerview.adapter = AlarmRecyclerviewAdapter()
        view.alarmfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class AlarmRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var alarmDTOList : ArrayList<AlarmDTO> = arrayListOf()

        init {
            var uid = FirebaseAuth.getInstance().currentUser?.uid

            FirebaseFirestore.getInstance().collection("alarms").
            whereEqualTo("destinationUid",uid).addSnapshotListener { value, error ->
                alarmDTOList.clear()
                if(value == null) return@addSnapshotListener

                for (snapshot in value.documents){
                    alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                }
                notifyDataSetChanged()
            }
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false)

            return CustomViewHolder(view)

        }
        inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return alarmDTOList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

//            FirebaseFirestore.getInstance().collection("profileImages").document(alarmDTOList[position].uid!!).get().addOnCompleteListener {
//                if(it.isSuccessful){
//                    var url = it.result!!["image"]
////                    if((view!!.context as Activity).isFinishing) return@addOnCompleteListener
//                    Glide.with(view!!.context).load(url)
//                        .apply(RequestOptions().circleCrop())
//                        .into(view!!.commentviewitem_imageview_profile)
//                }
//            }

            //알람 리스트 프로필 사진 반영 제대로 안됨.
            var firestore = FirebaseFirestore.getInstance()
            var uid = FirebaseAuth.getInstance().currentUser?.uid

            val pf = firestore?.collection("profileImages")
            if (uid == alarmDTOList!![position].uid) {
                pf?.document(uid!!)?.addSnapshotListener { value, error ->
                    if (value == null) return@addSnapshotListener
                    else {
                        if (value.data != null) {
                            var url = value?.data!!["image"]
                            if((holder.itemView.context as Activity).isFinishing) return@addSnapshotListener

                            Glide.with(holder.itemView.context as Activity).load(url)
                                .apply(RequestOptions().circleCrop())
                                .into(view!!.commentviewitem_imageview_profile)
                        }
                    }
                }
            }else{
                pf?.document(alarmDTOList!![position].uid!!)?.addSnapshotListener { value, error ->
                    if (value == null) return@addSnapshotListener
                    else {
                        if (value.data != null) {
                            var url = value?.data!!["image"]
                            if((holder.itemView.context as Activity).isFinishing) return@addSnapshotListener

                            Glide.with(holder.itemView.context as Activity).load(url)
                                .apply(RequestOptions().circleCrop())
                                .into(view!!.commentviewitem_imageview_profile)
                        }
                    }
                }

            }


            var view = holder.itemView

            when(alarmDTOList[position].kind){
                0 -> {
                    var str_0 = alarmDTOList[position].userId + " " + getString(R.string.alarm_favorite)
                    view.commentviewitem_textview_profile.text = str_0
                }1 -> {
                    var str_0 = alarmDTOList[position].userId + " " + getString(R.string.alarm_comment) + " : " +alarmDTOList[position].message

                    view.commentviewitem_textview_profile.text = str_0
                }2 -> {
                    var str_0 = alarmDTOList[position].userId + " " + getString(R.string.alarm_follow)
                    view.commentviewitem_textview_profile.text = str_0
                }
            }
            view.commentviewitem_textview_comment.visibility = View.INVISIBLE

        }

    }
}