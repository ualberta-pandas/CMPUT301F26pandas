package com.example.campsiteapp.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

/**
 * Displays a styled popup
 *
 * Display a popup with a screen overlay
 * Optional "X" button
 * Can pass in custom content to display
 *
 * @param modifier to alter formatting or behavior
 * @param title text at the top of box
 * @param conditional logic that disables popup visibility (if boolean, write as variable=!variable)
 * @param hasXButton display x button in corner if true
 * @param customContent content to display below title (usually, pass in an anonymous @Composable function)
 */
@Composable
fun CustomPopup(
    modifier: Modifier,
    title: String,
    conditional: () -> Unit,
    hasXButton: Boolean,
    customContent: @Composable () -> Unit
){
    Box(
        modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
    )
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = {conditional()}
    ) {
        Box(
            modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier
                    .padding(16.dp)
            ) {
                Box(
                    modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        title,
                        modifier
                            .padding(4.dp)
                            .align(Alignment.Center)
                    )
                    if(hasXButton){
                        Icon(
                            imageVector = Icons.Default.Close,
                            "Close button",
                            modifier
                                .size(25.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .padding(1.dp)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    conditional()
                                },
                            tint = Color.DarkGray
                        )
                    }
                }
                customContent()
            }
        }
    }
}

/**
 * Display a pair of buttons
 *
 * Display two buttons side by side
 * The first has default color, the second is colored
 *
 * @param modifier to alter formatting or behavior
 * @param button1Text label for the first button
 * @param button2Text label for the second button
 * @param button1Action Unit function that the first button will execute (usually, code within {})
 * @param button2Action Unit function that the second button will execute (usually, code within {})
 * @param button2Color background color of the second button
 */
@Composable
fun CancelOrProceedTextButtonsPair(
    modifier: Modifier,
    button1Text: String,
    button2Text: String,
    button1Action: () -> Unit,
    button2Action: () -> Unit,
    button2Color: Color
){
    Row(
        modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                button1Action()
            }
        ) {
            Text(button1Text)
        }
        Button(
            onClick = {
                button2Action()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = button2Color,
                contentColor = Color.White
            )
        ) {
            Text(button2Text)
        }
    }
}

/**
* Display a user's profile picture
*
* Display profile picture if it is valid
* Display default profile picture if it is null
*
* @param modifier to alter formatting or behavior
* @param image the image to display
* @param size the size of the image
* @param contentDescription the image content description
* @param customContent content to display within image's parent Box
*/
@Composable
fun ProfilePicture(
    modifier: Modifier,
    image: String,
    size: Dp,
    contentDescription: String,
    customContent: @Composable () -> Unit
){

    Box(
        modifier
            .size(size)
            .padding(size*(1f/24f))
    ) {
        if((image=="null")or(image=="")){
            Box(
                modifier
                    .size(size)
                    .clip(CircleShape)
                    .clipToBounds()
                    .wrapContentSize(unbounded = true)
                    .background(Color.LightGray)
            ){
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    "Default profile image",
                    modifier = Modifier
                        .size(size*1.5f)
                        .align(Alignment.Center),
                    tint = Color.Gray
                )
            }
        }else{
            val context=LocalContext.current
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(image)
                        .build()
                ),
                contentDescription = contentDescription,
                modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        customContent()
    }
}



/**
 * A small button containing an icon
 *
 * Use as button in TopAppBar
 *
 * @param onClick action performed when pressing button
 * @param backgroundColor background color of the button
 * @param contentColor content color of the button
 * @param icon icon displayed in the button
 * @param contentDescription description of button
 */
@Composable
fun TopAppBarButton(
    onClick: () -> Unit ,
    backgroundColor: Color,
    contentColor: Color,
    icon: ImageVector,
    contentDescription: String
){
    FilledIconButton(
        onClick = { onClick() },
        modifier = Modifier.size(36.dp),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
    ) {
        Icon(icon, contentDescription = contentDescription)
    }
}

/**
 * A small button containing an icon
 *
 * Use as button in Box
 * Specifically intended for buttons overlapping with profile picture
 *
 * @param onClick action performed when pressing button
 * @param backgroundColor background color of the button
 * @param contentColor content color of the button
 * @param icon icon displayed in the button
 * @param contentDescription description of button
 * @param alignment alignment within parent Box
 */
@Composable
fun BoxScope.ProfilePictureOptionButton(
    onClick: () -> Unit ,
    backgroundColor: Color,
    contentColor: Color,
    icon: ImageVector,
    contentDescription: String,
    alignment: Alignment
){
    IconButton(
        {
            onClick()
        },
        Modifier
            .size(48.dp)
            .align(alignment),
        colors = IconButtonDefaults.filledIconButtonColors(
            backgroundColor,
            contentColor
        )
    ) {
        Icon(
            icon,
            contentDescription,
            Modifier
                .size(40.dp)
                .padding(8.dp)
        )
    }
}
