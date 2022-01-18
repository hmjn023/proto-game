import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

/*
fun main()= singleWindowApplication(
    title = "Falling",
) {
    game()
}
*/

fun main(){
    application {
        Window(onCloseRequest = ::exitApplication,
            state = WindowState(size= DpSize(1000.dp, 800.dp)),
            title = "chrome://dino"
        //state = rememberWindowState(WindowPlacement.Maximized)){
        ){
            game()
        }
    }
}