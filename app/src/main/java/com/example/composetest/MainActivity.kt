package com.example.composetest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composetest.model.User
import com.example.composetest.ui.theme.ComposeTestTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.composetest.model.UserResponse
import com.example.composetest.util.previewparameterprovider.UsersPreviewParameterProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityScreen(mainViewModel = mainViewModel) {
                        startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
private fun MainActivityScreen(mainViewModel: MainViewModel, onActionClick: () -> Unit = {}) {
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

    val users = mainViewModel.getUsers().collectAsLazyPagingItems()

    UsersAndButton(users, onActionClick)
}

@Composable
fun UsersAndButton(
    @PreviewParameter(UsersPreviewParameterProvider::class) list: LazyPagingItems<UserResponse>,
    onActionClick: () -> Unit = {}
) {

    ConstraintLayout {
        val (listOfUsers, progressBar, tvRetry, button) = createRefs()

        Users(Modifier.constrainAs(listOfUsers) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(progressBar.top, margin = 8.dp)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }, list)

        val isRefreshLoading = list.loadState.refresh is LoadState.Loading
        val isAppendLoading = list.loadState.append is LoadState.Loading
        val isLoading = isRefreshLoading || isAppendLoading
        LinearProgressIndicator(modifier = Modifier
            .constrainAs(progressBar) {
                start.linkTo(parent.start, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
                bottom.linkTo(tvRetry.top, margin = 8.dp)
                width = Dimension.fillToConstraints
                visibility = if (isLoading) Visibility.Visible else Visibility.Gone
            }
            .height(4.0.dp))

        val isRefreshHavingNetworkError = list.loadState.refresh is LoadState.Error
        val isAppendHavingNetworkError = list.loadState.append is LoadState.Error
        val isNetworkError = isRefreshHavingNetworkError || isAppendHavingNetworkError
        Text(text = "Network Error, click here to try again",
            color = Color.Red,
            modifier = Modifier.constrainAs(tvRetry) {
                start.linkTo(parent.start, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
                bottom.linkTo(button.top, margin = 8.dp)
                width = Dimension.fillToConstraints
                visibility = if (isNetworkError) Visibility.Visible else Visibility.Gone
            }.padding(16.0.dp).clickable { list.retry() }
        )

        Button(onClick = onActionClick, modifier = Modifier.constrainAs(button) {
            start.linkTo(parent.start, margin = 8.dp)
            end.linkTo(parent.end, margin = 8.dp)
            bottom.linkTo(parent.bottom, margin = 8.dp)
            width = Dimension.fillToConstraints
        }) {

            Text(text = "Open next Page")
        }
    }
}

@Composable
fun Users(
    modifier: Modifier,
    @PreviewParameter(UsersPreviewParameterProvider::class) list: LazyPagingItems<UserResponse>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)) {
        items(list) { userResponse ->
            User(userResponse?.toUser())
        }
    }
}

@Composable
fun User(user: User?) {
    if (user != null) {
        Row(modifier = Modifier.padding(start = 8.0.dp, end = 8.0.dp)) {
            AsyncImage(
                modifier = Modifier.clip(CircleShape),
                model = user.imageUrl,
                contentDescription = "Translated description of what the image contains"
            )
            Column(modifier = Modifier.padding(start = 8.0.dp)) {
                Text(text = user.firstName ?: "")
                Text(text = user.lastName ?: "")
            }
        }
    } else {
        Text(text = "No user found")
    }

}