package com.memad.artask.ui.screens.favorites_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.memad.artask.R
import com.memad.artask.data.local.entities.FavoriteArticleEntity
import com.memad.artask.ui.placeholders.Loading
import com.memad.artask.ui.placeholders.Placeholder
import com.memad.artask.utils.bounceClickable
import com.memad.artask.utils.convertIsoDateToReadableFormat

@Composable
fun FavoritesScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
          FavoritesList()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesList(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favoriteArticles by viewModel.favoriteArticles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Loading()
    } else {
        if (favoriteArticles.isEmpty()) {
            Placeholder(
                text = "No Favourites Yet!",
                rawRes = R.raw.favourites
            )
        } else {
            LazyColumn {
                items(favoriteArticles, key = { it.id }) { article ->
                    FavoritesCard(article = article, modifier = Modifier.animateItemPlacement())
                }
            }
        }

    }
}


@Composable
fun FavoritesCard(
    modifier: Modifier,
    article: FavoriteArticleEntity,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    var isFavorite by rememberSaveable {
        mutableStateOf(true)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { /* Handle click to open article */ },
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
                    .align(Alignment.CenterHorizontally)
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
                        .clickable { /* Handle click to open article */ }
                        .align(Alignment.CenterVertically),
                    textDecoration = TextDecoration.Underline
                )
                Icon(
                    imageVector = if (isFavorite) {
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
                                isFavorite = !isFavorite
                            }
                        ) {
                            viewModel.unFavoriteArticle(article)
                        }
                )
            }
        }
    }
}