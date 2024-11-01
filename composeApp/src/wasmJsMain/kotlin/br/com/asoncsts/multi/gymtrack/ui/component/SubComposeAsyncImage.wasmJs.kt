package br.com.asoncsts.multi.gymtrack.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import br.com.asoncsts.multi.gymtrack.data.image.ImageRepository
import br.com.asoncsts.multi.gymtrack.extension.error
import br.com.asoncsts.multi.gymtrack.extension.log
import br.com.asoncsts.multi.gymtrack.ui._app.TAG_APP
import org.koin.compose.koinInject

@Composable
actual fun PlatformSubComposeAsyncImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    error: @Composable (() -> Unit)?,
    loading: @Composable (() -> Unit)?
) {
    val repository = koinInject<ImageRepository>()
    var state: ImageState by remember {
        mutableStateOf(ImageState.Loading)
    }

    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        state.let { state ->
            when (state) {
                ImageState.Error -> error
                    ?.invoke()

                ImageState.Loading -> loading
                    ?.invoke()

                is ImageState.Success -> {
                    Image(
                        state.image,
                        contentDescription,
                        Modifier
                            .matchParentSize(),
                        contentScale = contentScale
                    )
                }
            }
        }
    }

    LaunchedEffect(model) {
        runCatching {
            TAG_APP.log("TODO Fix loading images on web. $model")
            repository
                .getImage(model)
        }.onFailure {
            TAG_APP.error("SubComposeAsyncImage.getImage", it)
            state = ImageState.Error
        }.onSuccess {
            state = if (it != null)
                ImageState.Success(it)
            else
                ImageState.Error
        }
    }
}

private sealed class ImageState {
    data object Error : ImageState()
    data object Loading : ImageState()
    data class Success(
        val image: ImageBitmap
    ) : ImageState()
}
