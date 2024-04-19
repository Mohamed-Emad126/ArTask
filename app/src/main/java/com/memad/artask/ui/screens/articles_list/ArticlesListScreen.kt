package com.memad.artask.ui.screens.articles_list

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.memad.artask.R
import com.memad.artask.data.local.entities.ArticleEntity
import com.memad.artask.ui.placeholders.Loading
import com.memad.artask.ui.placeholders.Placeholder
import com.memad.artask.ui.screens._list.ArticlesViewModel
import com.memad.artask.utils.bounceClickable
import com.memad.artask.utils.convertIsoDateToReadableFormat

@Composable
fun ArticlesListScreen() {

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = "News App",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .padding(top = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            NewsSearchBar(modifier = Modifier)
            NewsFeed()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSearchBar(
    modifier: Modifier,
    viewModel: ArticlesViewModel = hiltViewModel()
) {
    val searchText by viewModel.searchText.collectAsState("")
    val isSearching by viewModel.isSearching.collectAsState(false)
    val searchHistory by viewModel.searchHistory.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }


    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                onClick = {
                    viewModel.onToggleSearch(true)
                },
                interactionSource = interactionSource,
                indication = null
            ),
        query = searchText,
        onQueryChange = {
            Log.d("NewsSearchBar", "onQueryChange: $it $searchText")
            viewModel.onSearchTextChange(it)
        },
        onSearch = {
            if (searchText.isNotBlank()) {
                viewModel.onSearchPerformed(searchText)
                viewModel.onToggleSearch(false)
            }
        },
        active = isSearching,
        onActiveChange = {
            viewModel.onSearchTextChange(searchText)
            viewModel.onToggleSearch(it)
        },
        placeholder = { Text("Search for articles") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (isSearching) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (searchText.isBlank()) {
//                                viewModel.onSearchTextChange("")
                                viewModel.onToggleSearch(false)
                            } else {
                                viewModel.onSearchTextChange("")
                            }
                        }
                )
            }
        }
    ) {
        TextButton(
            onClick = { viewModel.onClearHistory() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
        ) {
            Text(
                "Clear History",
                modifier = Modifier
                    .padding(8.dp)
            )
        }
        LazyColumn {
            items(searchHistory) {
                TextButton(
                    onClick = { viewModel.onSearchTextChange(it.searchQuery) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.CenterVertically)
                                .weight(1f)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f)
                        ) {
                            Text(
                                it.searchQuery,
                                modifier = Modifier,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Start,
                                maxLines = 1
                            )
                            Text(
                                it.timeAgo(),
                                modifier = Modifier,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Start,
                                maxLines = 1
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    viewModel.onClearOneHistoryItem(it.id!!)
                                }
                                .padding(8.dp)
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun NewsCard(
    modifier: Modifier,
    article: ArticleEntity,
    viewModel: ArticlesViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val favorites = viewModel.favoriteArticles.collectAsStateWithLifecycle()
    var isFavorite by rememberSaveable { mutableStateOf(article.toFavoriteArticle() in favorites.value) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(article.url)
                context.startActivity(openURL)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .error(R.drawable.broken_image)
                    .build(),
                contentDescription = article.title,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .fillMaxHeight(0.4f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.description.trim(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.wrapContentSize(),
                color = Color(0xFFD1D5E1)
            ) {
                Text(
                    text = article.author ?: "Unknown Author",
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = convertIsoDateToReadableFormat(
                    article.publishedAt ?: "2024-03-27T11:00:00Z"
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Read more",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            val openURL = Intent(Intent.ACTION_VIEW)
                            openURL.data = Uri.parse(article.url)
                            context.startActivity(openURL)
                        }
                        .align(Alignment.CenterVertically),
                    textDecoration = TextDecoration.Underline
                )
                Icon(
                    imageVector = if (article.toFavoriteArticle() in favorites.value) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                        .bounceClickable(
                            minScale = 0.5f,
                            onAnimationFinished = {
                                // Used to change the icon
                                isFavorite = !isFavorite
                            }
                        ) {
                            if (!isFavorite) {
                                viewModel.favoriteArticle(article)
                            } else {
                                viewModel.unFavoriteArticle(article)
                            }
                        }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsFeed(viewModel: ArticlesViewModel = hiltViewModel()) {
    val articlesList by viewModel.articlesList.collectAsStateWithLifecycle()
    val endReached by viewModel.endReached.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    Log.d("NewsFeed", " --> articlesList: ${articlesList.size}")

    LazyColumn {
        items(articlesList, key = { it.id!! }) { article ->
            NewsCard(
                modifier = Modifier.animateItemPlacement(),
                article = article
            )
            if (articlesList.indexOf(article) >= articlesList.size - 1 && !endReached && !isLoading) {
                Log.d("NewsFeed", "item ---> ${articlesList.indexOf(article)}  ${article.title}")
                LaunchedEffect(key1 = article.id!!) {
                    viewModel.getNextPage()
                }
                Loading(modifier = Modifier.fillMaxWidth())
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading && articlesList.isEmpty()) {
            Placeholder()
        }
        if (error.isNotEmpty() && articlesList.isEmpty()) {
            Placeholder(
                text = error,
                rawRes = R.raw.no_result,
            )
        } else if (error.isNotEmpty()) {
            Toast.makeText(
                LocalContext.current,
                error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
