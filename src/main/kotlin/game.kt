import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun game() {
    var game_state = remember { game_data() }

    Column {
        Text(game_state.clock.toString())
    }

    if (!game_state.start) {
        start_buttom(game_state)
    }

    if (game_state.start && !game_state.finish) {
        bbox(game_state.me_state, game_state)
        for (enemies in game_state.enemy_state) {
            enemy(enemies)
        }
        land_surface(game_state)
    }

    if(game_state.start && game_state.finish){
        Column {
            Text("result test")
            start_buttom(game_state)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos() {
                if (game_state.start && !game_state.finish) {
                    //Thread.sleep(1000/60)
                    for (enemies in game_state.enemy_state) {
                        touch(game_state.me_state, enemies, game_state)
                        enemies.straight()
                    }

                    for(i in 0 until  game_state.enemy_state.size ){
                        //println(game_state.enemy_state[i])
                        if(game_state.enemy_state[i].x+game_state.enemy_state[i].size_x <0){
                            game_state.enemy_state.remove(game_state.enemy_state[i])
                        }
                    }
                    game_state.me_state.drop()
                    game_state.clock += 1
                }
                    if(game_state.clock!=0 && game_state.clock%300==0 ) {
                        for (enemies in game_state.enemy_state) {
                            if(enemies.x<0) {
                                enemies.init(fy = game_state.land_h)
                            }
                        }
                    }
            }
        }
    }
}
@Composable
@Preview
fun me(me_state: me_data) {
    Box(
        Modifier
            .offset(me_state.x.dp,me_state.y.dp)
            .size(me_state.size_x.dp,me_state.size_y.dp)
            .background(Color.Blue)
    ) {
        Column {
            //Text(me_state.x.toString())
            //Text(me_state.y.toString())
            //Text(me_state.state_y.toString())
            Image(
                painter=painterResource("win.png"),
                contentDescription = "win",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview
fun enemy(enemy_state:enemy_data){
    Box(
      Modifier
          .offset(enemy_state.x.dp,enemy_state.y.dp)
          .size(enemy_state.size_x.dp,enemy_state.size_y.dp)
          .background(Color.Red)
    ){
        Column {
            //Text(enemy_state.x.toString())
            //Text(enemy_state.y.toString())
            Image(
                painter = painterResource("BSW.jpg"),
                contentDescription ="sample",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview
@OptIn(ExperimentalComposeUiApi::class)
fun bbox(me_state:me_data,game_state: game_data) {
    val requester = FocusRequester()
    Column {
        Box(
            Modifier
                .onKeyEvent {
                    when {
                        (it.key == Key.W && it.type == KeyEventType.KeyDown) -> {
                            me_state.up()
                            true
                        }
                        (it.key == Key.S && it.type == KeyEventType.KeyDown) -> {
                            me_state.down()
                            true
                        }
                        (it.key == Key.A && it.type == KeyEventType.KeyDown) -> {
                            me_state.left()
                            true
                        }
                        (it.key == Key.D && it.type == KeyEventType.KeyDown) -> {
                            me_state.right()
                            true
                        }
                        else -> false
                    }
                }
                .focusRequester(requester)
                .focusable()
        ) {
            me(me_state)
            LaunchedEffect(Unit) {
                requester.requestFocus()
            }
        }
    }
}

fun touch(me_state: me_data,enemy_state: enemy_data,game_state:game_data){

    val me_point = listOf(listOf<Int>(me_state.x,me_state.y), listOf<Int>(me_state.x+me_state.size_x,me_state.y+me_state.size_y))
    val enemy_point =listOf(listOf<Int>(enemy_state.x,enemy_state.y), listOf<Int>(enemy_state.x+enemy_state.size_x,enemy_state.y+enemy_state.size_y))

    val xkasanari = (me_point[1][0]>=enemy_point[0][0] && me_point[1][0]<=enemy_point[1][0]) || (me_point[0][0]>=enemy_point[0][0] && me_point[0][0]<=enemy_point[1][0])
    val ykasanari = (me_point[1][1]>=enemy_point[0][1] && me_point[1][1]<=enemy_point[1][1]) || (me_point[0][1]>=enemy_point[0][1] && me_point[0][1]<=enemy_point[1][1])

    if(xkasanari && ykasanari){
            game_state.finishing()
            println(game_state.finish)
        }
}

@Composable
@Preview
fun start_buttom(game_state: game_data){
    Button(modifier = Modifier.offset(500.dp,500.dp),
        onClick ={
            game_state.reset()
            game_state.starting()
        }
    ){
            Text("start")
    }
}

@Composable
@Preview
fun land_surface(game_state: game_data){
    Box(modifier = Modifier.offset(0.dp,game_state.land_h.dp)
        .size(1000.dp,200.dp)
        .background(Color.Black)
    )
}
