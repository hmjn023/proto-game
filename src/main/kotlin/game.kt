import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun game() {
    var game_state = remember { game_data() }

    Column (){
        //Text(game_state.clock.toString())
    }

    if (!game_state.start) {
        Column(modifier = Modifier.offset(250.dp, 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            if (game_state.times == 0) {
                Text(
                    text = "ORIGINAL\nGAME",
                    textAlign = TextAlign.Center,
                    fontSize = 100.sp
                )
            }
            start_buttom(game_state)
        }
    }

    if (game_state.start && !game_state.finish) {
        Image(
            painter=painterResource("forestback1.png"),
            contentDescription = "forest",
            modifier = Modifier.fillMaxSize()
        )
        bbox(game_state.me_state, game_state)
        for (enemies in game_state.enemy_state.filterNotNull()) {
            enemy(enemies)
        }

    }

    if(game_state.start && game_state.finish){
        Column (Modifier.offset(80.dp,300.dp)
        , horizontalAlignment =Alignment.CenterHorizontally){
            Text(text = "YOUR SCORE:${game_state.clock.toString()}",
                fontSize = 95.sp
            )
            start_buttom(game_state)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos() {
                if (game_state.start && !game_state.finish) {
                    Thread.sleep(1000/60)
                    for (enemies in game_state.enemy_state.filterNotNull()) {
                        touch(game_state.me_state, enemies, game_state)
                        enemies.move()
                    }
                    enemy_remove(game_state)
                    enemy_pop(game_state)
                    game_state.me_state.drop()
                    game_state.clock += 1
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
            .background(Color.LightGray)
    ) {
        Column {
            Image(
                painter=painterResource("president.png"),
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
    ){
        Column {
            if(enemy_state.type=="land") {
                Image(
                    painter = painterResource("pig.png"),
                    contentDescription = "sample",
                    modifier = Modifier.fillMaxSize()
                )
            }
            else if(enemy_state.type=="sky"){
                Image(
                    painter = painterResource("tuna.png"),
                    contentDescription = "sky",
                    modifier = Modifier.fillMaxSize()
                )
            }
            else if(enemy_state.type=="under") {
                Image(
                    painter = painterResource("armajiro.png"),
                    contentDescription = "sample",
                    modifier = Modifier.fillMaxSize()
                )
            }
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

    val me_point = listOf(listOf(me_state.x,me_state.y), listOf(me_state.x+me_state.size_x,me_state.y+me_state.size_y))
    val enemy_point =listOf(listOf(enemy_state.x,enemy_state.y), listOf(enemy_state.x+enemy_state.size_x,enemy_state.y+enemy_state.size_y))

    val xkasanari = (me_point[1][0]>=enemy_point[0][0] && me_point[1][0]<=enemy_point[1][0]) || (me_point[0][0]>=enemy_point[0][0] && me_point[0][0]<=enemy_point[1][0])
    val ykasanari = (me_point[1][1]>=enemy_point[0][1] && me_point[1][1]<=enemy_point[1][1]) || (me_point[0][1]>=enemy_point[0][1] && me_point[0][1]<=enemy_point[1][1])

    if(xkasanari && ykasanari){
            game_state.finishing()
        }
}

@Composable
@Preview
fun start_buttom(game_state: game_data){
    Button(
        modifier = Modifier.size(width = 200.dp,height = 100.dp ),
        onClick ={
            game_state.reset()
            game_state.starting()
        }
    ){
            Text(text = "start",
            fontSize = 40.sp)
    }
}

@Composable
@Preview
fun land_surface(game_state: game_data){
    Box(modifier = Modifier.offset(0.dp,game_state.land_h.dp)
        .size(1000.dp,200.dp)
        .background(Color.LightGray)
    )
}

fun enemy_remove(game_state: game_data){
    if(game_state.enemy_state.size!=0){
        for(enemies in game_state.enemy_state.filterNotNull()){
            if(enemies.x+enemies.size_x<0){
                game_state.enemy_state.remove(enemies)
                //println("removed!!!")
            }
        }
    }
}

fun enemy_pop(game_state: game_data){
    if(game_state.clock!=0 && game_state.clock-game_state.last_e_clock==game_state.interval) {
        val rand=(0..2).random()
        if(rand==0){
            game_state.enemy_state.add(enemy_data("land",))
        }
        else if(rand==1){
            game_state.enemy_state.add(enemy_data("sky"))
        }
        else if(rand==2){
            if((0..2).random()<=1){
                game_state.enemy_state.add(enemy_data("under"))
            }else {
                game_state.enemy_state.add(enemy_data("under", "upper"))
            }
        }
        game_state.last_e_clock=game_state.clock
        game_state.interval=(100..400-game_state.interval_shrink).random()
        game_state.enemy_state[game_state.enemy_state.size - 1].init(fy=game_state.land_h)
    }
    if(game_state.clock!=0 && game_state.clock%50==0){
        if(game_state.interval_shrink<300) {
            game_state.interval_shrink += 3
        }
        //println("shrink now ${game_state.interval_shrink}")
    }
}