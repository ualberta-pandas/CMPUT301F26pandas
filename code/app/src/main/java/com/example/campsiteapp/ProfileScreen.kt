package com.example.campsiteapp
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import kotlin.String

enum class  ProfileScreenState{
    LOADING,
    CREATEPROMPT,
    CREATE,
    EDIT,
    VIEWOWN,
    VIEW,
}

@Composable
fun ProfileScreen(
    navController: NavController,
    //deviceId is probably temporary but works better to pass in test values for now
    deviceId: String,
    profile: User?,
    onAddUser: (User) -> Unit,
    onDeleteUser: (User) -> Unit,
    onUpdateUser: (User, User) -> Unit,
){
    val scope = rememberCoroutineScope()
    //UI state (see possible states in enum class)
    var currentProfileScreenState by remember {mutableStateOf(ProfileScreenState.LOADING)}
    var profileScreenMode by remember {mutableStateOf("")}
    var userProfile by remember { mutableStateOf<User?>(null) }
    var profileToDisplay:User by remember{mutableStateOf(User())}

    var newUserName by remember {mutableStateOf("")}
    var newUserEmail by remember {mutableStateOf("")}
    var newUserPhoneNumber by remember {mutableStateOf("")}
    var newUserProfileImage by remember { mutableStateOf("")}
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var openDeletionConfirmation by remember { mutableStateOf(false) }



    LaunchedEffect(key1=deviceId){
        //Return profile with specified device id if it exists
        userProfile=getDocument("profiles",deviceId)

        //Display profile if something is passed in, display own profile if it exists, else, prompt to create profile
        profileToDisplay=profile ?: userProfile ?: User()


        newUserName = if(profileToDisplay.userName==""){
            "Name"
        }else{
            profileToDisplay.userName
        }

        newUserEmail = if(profileToDisplay.userEmail==""){
            "Email"
        }else{
            profileToDisplay.userEmail
        }

        newUserPhoneNumber = if(profileToDisplay.userPhoneNumber==""){
            "Phone number"
        }else{
            profileToDisplay.userPhoneNumber
        }

        if(profileToDisplay.userProfileImage!=""){
            newUserProfileImage=profileToDisplay.userProfileImage
        }

        //set state and title
        if(profile!=null){
            // Display messaging option if is someone else's profile (something is passed in for profile)
            profileScreenMode="View Profile"
            currentProfileScreenState=ProfileScreenState.VIEW
        }else if(userProfile != null){
            // Display editing options, settings if is user's own profile
            profileScreenMode="My Profile"
            currentProfileScreenState=ProfileScreenState.VIEWOWN
        }else{
            // In create mode if no profile with device id is found
            profileScreenMode="Create Profile"
            currentProfileScreenState=ProfileScreenState.CREATEPROMPT
        }

    }
    //All composables are below

    Box(){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if((currentProfileScreenState!=ProfileScreenState.CREATEPROMPT)and(currentProfileScreenState!=ProfileScreenState.LOADING)) {
                Text(
                    profileScreenMode,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            //Display create options if user has no profile
            when(currentProfileScreenState) {
                ProfileScreenState.LOADING->{

                }
                ProfileScreenState.CREATEPROMPT -> {
                    Row() {
                        Text(
                            "You have no profile."
                        )
                    }
                    Row() {
                        Button(
                            onClick = { currentProfileScreenState = ProfileScreenState.CREATE }
                        ) {
                            Text("Create profile")
                        }
                    }
                }

                (ProfileScreenState.CREATE), (ProfileScreenState.EDIT) -> {

                    val imagePicker = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri ->
                        imageUri = uri
                    }
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        if (currentProfileScreenState == ProfileScreenState.EDIT) {
                            Icon(
                                painter = painterResource(id = R.drawable.chevron_left),
                                "Back button",
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.DarkGray)
                                    .padding(8.dp)
                                    .clickable {
                                        profileScreenMode = "My Profile"
                                        currentProfileScreenState = ProfileScreenState.VIEWOWN
                                    },
                                tint = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.delete),
                                "Delete button",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .padding(8.dp)
                                    .clickable {
                                        openDeletionConfirmation = true
                                    },
                                tint = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(120.dp)
                        ) {
                            if (imageUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(imageUri),
                                    contentDescription = "Selected image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }else if (profileToDisplay.userProfileImage!=""){
                                Image(
                                    painter = rememberAsyncImagePainter(profileToDisplay.userProfileImage),
                                    contentDescription = "Profile image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.account_circle),
                                    "Default profile image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center),
                                    tint = Color.Gray
                                )
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.edit),
                                "Edit button",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.DarkGray)
                                    .padding(8.dp)
                                    .align(Alignment.BottomEnd)
                                    .clickable {
                                        imagePicker.launch("image/*")
                                    },
                                tint = Color.White
                            )
                        }
                    }


                    Row(modifier = Modifier
                        .padding(3.dp)
                    ) {
                        OutlinedTextField(
                            value = newUserName,
                            onValueChange = { newUserName = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                    Row(modifier = Modifier
                        .padding(3.dp)
                    ) {
                        OutlinedTextField(
                            value = newUserEmail,
                            onValueChange = { newUserEmail = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                    Row(modifier = Modifier
                        .padding(3.dp)
                    ) {
                        OutlinedTextField(
                            value = newUserPhoneNumber,
                            onValueChange = { newUserPhoneNumber = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    val context = LocalContext.current
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = "Edit button",
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray)
                                .padding(8.dp)
                                .clickable {

                                    scope.launch {
                                        if (imageUri != null) {
                                            imageUri?.let {
                                                newUserProfileImage = uploadImage(context, it)
                                            }
                                        } else {
                                            newUserProfileImage = ""
                                        }
                                        val newProfile = User()
                                        newProfile.userDeviceId = deviceId
                                        newProfile.userName = newUserName
                                        newProfile.userEmail = newUserEmail
                                        newProfile.userPhoneNumber = newUserPhoneNumber
                                        newProfile.userProfileImage = newUserProfileImage
                                        newProfile.notificationPreference = true
                                        if (profileScreenMode == "Create Profile") {
                                            onAddUser(newProfile)
                                        } else {
                                            onUpdateUser(profileToDisplay, newProfile)
                                        }
                                        profileScreenMode = "My Profile"
                                        profileToDisplay = newProfile
                                        currentProfileScreenState = ProfileScreenState.VIEWOWN
                                    }
                                },
                            tint = Color.White
                        )

                    }
                }

                ProfileScreenState.VIEW, ProfileScreenState.VIEWOWN -> {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ){
                            if(currentProfileScreenState==ProfileScreenState.VIEWOWN){
                                Icon(
                                    painter = painterResource(id = R.drawable.edit),
                                    "Edit button",
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                        .padding(8.dp)
                                        .clickable {
                                            profileScreenMode = "Edit Profile"
                                            currentProfileScreenState = ProfileScreenState.EDIT
                                        },
                                    tint = Color.White
                                )
                            } else if (currentProfileScreenState==ProfileScreenState.VIEW){
                                Icon(
                                    painter = painterResource(id = R.drawable.mail),
                                    "Message button",
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                        .padding(8.dp)
                                        .clickable {
                                            //Messaging not implemented
                                        },
                                    tint = Color.White
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.chevron_left),
                                    "Back button",
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                        .padding(8.dp)
                                        .clickable {
                                            //navHost not implemented, should pop back stack
                                        },
                                    tint = Color.White
                                )
                            }
                            //profile picture
                            if (profileToDisplay.userProfileImage != "") {
                                Image(
                                    painter = rememberAsyncImagePainter(profileToDisplay.userProfileImage),
                                    contentDescription = "Profile image",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.account_circle),
                                    "Default profile image",
                                    modifier = Modifier.size(120.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                    }

                    //profile name
                    Row() {
                        Text(
                            profileToDisplay.userName,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    //profile email
                    Row() {
                        Text(
                            profileToDisplay.userEmail,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    //profile phone number
                    Row() {
                        Text(
                            profileToDisplay.userPhoneNumber,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if(openDeletionConfirmation){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { openDeletionConfirmation = false }
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                ) {
                    Column(){
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Text(
                                "Delete Profile",
                                //modifier = Modifier.fillMaxHeight()
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Text(
                                "Are you sure you want to delete your profile?"
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Button(
                                onClick={
                                    openDeletionConfirmation=false
                                }
                            ) {
                                Text("Cancel")
                            }
                            Button(
                                onClick={
                                    onDeleteUser(profileToDisplay)
                                    profileToDisplay = User()
                                    profileScreenMode = "Create Profile"
                                    currentProfileScreenState = ProfileScreenState.CREATEPROMPT
                                    openDeletionConfirmation=false
                                },
                                colors= ButtonDefaults.buttonColors(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }


    }
}