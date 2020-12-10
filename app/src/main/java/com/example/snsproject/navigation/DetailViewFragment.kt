package com.example.snsproject.navigation

import android.content.Intent
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
import com.example.snsproject.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailViewFragment : Fragment() {

    var firestore: FirebaseFirestore? = null
    var uid : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)

        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        view.detailviewfragment_recyclerview.adapter = DetailViewRecyclerViewAdapter()
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class DetailViewRecyclerViewAdapter :
        RecyclerView.Adapter<DetailViewRecyclerViewAdapter.CustomViewHolder>() {

        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")?.orderBy("timestamp", Query.Direction.DESCENDING)
                ?.addSnapshotListener { value, error ->
                    contentDTOs.clear()
                    contentUidList.clear()
                    if(value == null) return@addSnapshotListener
                    for (snapshot in value!!.documents) {
                        val item = snapshot.toObject(ContentDTO::class.java)
                        contentDTOs.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)


        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            val viewHolder = holder.itemView

            //UserId
            viewHolder.detailviewitem_profile_textview.text = contentDTOs!![position].userId

            //Image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUri)
                .into(viewHolder.detailviewitem_imageview_content)

            //Explain of content
            viewHolder.detailviewitem_explain_textview.text = contentDTOs!![position].explain

            //like
            viewHolder.detailviewitem_favorite_counter_textview.text =
                "Likes : " + contentDTOs!![position].favoriteCount

            //오류 발생중
            //ProfileImage
//            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUri)
//                .into(viewHolder.detailviewitem_profile_image)
            firestore?.collection("profileImages")?.document(uid!!)
                ?.addSnapshotListener { value, error ->
                    if (value == null) return@addSnapshotListener
                    else {
                        if (value.data != null) {
                            var url = value?.data!!["image"]
                            Glide.with(holder.itemView.context).load(url).apply(RequestOptions().circleCrop())
                                .into(viewHolder.detailviewitem_profile_image)
                        }
                    }
                }
            //좋아요 버튼 클릭 시
            viewHolder.detailviewitem_favorite_imageview.setOnClickListener {
                favoriteEvent(position)
            }
            //좋아요 하트 리스너

            if(contentDTOs!![position].favorites.containsKey(uid)){
                //좋아요 활성화상태
                viewHolder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite)

            }else{
                //좋아요 비활성화 상태
                viewHolder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite_border)

            }
            //프로필 이미지 눌렀을 때
            viewHolder.detailviewitem_profile_image.setOnClickListener {
                var fragment = UserFragment()
                var bundle = Bundle()
                bundle.putString("destinationUid",contentDTOs[position].uid)
                bundle.putString("userId",contentDTOs[position].userId)
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_content,fragment)?.commit()

            }
            viewHolder.detailviewitem_comment_imageview.setOnClickListener {
                var intent = Intent(it.context,CommentActivity::class.java)
                intent.putExtra("contentUid",contentUidList[position])
                startActivity(intent)
            }

        }

        fun favoriteEvent(position: Int) {
            var tsDoc = firestore?.collection("images")?.document(contentUidList[position])
            firestore?.runTransaction { transaction ->


                var contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

                if (contentDTO!!.favorites.containsKey(uid)) {
                    //버튼 클릭되어 있을 때
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount - 1
                    contentDTO?.favorites.remove(uid)

                } else {
                    //버튼 클릭 안 되어 있을 때
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount + 1
                    contentDTO?.favorites[uid!!] = true

                }
                transaction.set(tsDoc, contentDTO)
            }
        }
    }

}
