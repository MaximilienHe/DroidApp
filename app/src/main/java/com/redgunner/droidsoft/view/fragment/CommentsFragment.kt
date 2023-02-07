package com.redgunner.droidsoft.view.fragment

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.redgunner.droidsoft.R
import com.redgunner.droidsoft.adapter.CommentsListAdapter
import com.redgunner.droidsoft.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



import android.widget.EditText





class CommentsFragment : Fragment(R.layout.fragment_comments) {

    private val viewModel: SharedViewModel by activityViewModels()

    private val navArgs: CommentsFragmentArgs by navArgs()

    private val commentAdapter = CommentsListAdapter()


    override fun onStart() {
        super.onStart()
        setUpRecyclerView()
        viewModel.getPostComments(navArgs.postId)

    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            viewModel.comments.collect { Comments ->


                if (Comments.isNotEmpty()) {

                    NoComments.isVisible = false
                    CommentsList.isVisible = true

                    commentAdapter.submitList(Comments)


                } else {

                    NoComments.isVisible = true
                    CommentsList.isVisible = false

                }

            }
        }


        commentTopAppBar.setNavigationOnClickListener {

            findNavController().popBackStack()

        }

        addCommentButton.setOnClickListener() {
            addComment()
        }
    }


    private fun setUpRecyclerView() {


        CommentsList.apply {

            this.adapter = commentAdapter

        }
    }

    private fun addComment() {
        val nameField = view?.findViewById<EditText>(R.id.nameField)
        val emailField = view?.findViewById<EditText>(R.id.emailField)
        val messageField = view?.findViewById<EditText>(R.id.commentField)

        val name = nameField?.text.toString()
        val email = emailField?.text.toString()
        val message = messageField?.text.toString()
        val postId = navArgs.postId

        viewModel.addComment(postId, name, email, message)

        Log.d("TAG", "addComment: $name $email $message $postId")

    }


}