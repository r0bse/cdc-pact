package de.schroeder.androidconsumer.superheroes.boundary

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun SuperheroView(viewModel: SuperheroViewModel) {

    val padding = 2.dp

    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.getSuperheroes()
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(padding)
        ) {
            Button(
                onClick = { viewModel.getSuperheroes() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.onBackground
                )
            ) {
                Text(
                    text = "reload",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }

            val fontSizeVerySmall = 12.sp

            uiState.heroes.forEach{
                superhero ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = padding)
                ) {
                    Text(
                        text = superhero.name,
                        modifier = Modifier
                            .padding(start = 5.dp),
                        color = Color.Black,
                        fontSize = fontSizeVerySmall
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = superhero.affiliation,
                        modifier = Modifier.padding(start = 5.dp),
                        color = Color.Gray,
                        fontSize = fontSizeVerySmall
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = superhero.secretIdentity,
                        modifier = Modifier.padding(start = 5.dp),
                        color = Color.Gray,
                        fontSize = fontSizeVerySmall
                    )
                }
            }
        }
    }
}