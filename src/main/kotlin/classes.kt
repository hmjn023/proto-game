import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.Key

class me_data() {
    var size_x by mutableStateOf(50)
    var size_y by mutableStateOf(100)

    var y by mutableStateOf(0)
    var x by mutableStateOf(0)

    var state_x:Int = 0
    var state_y:Int = 0

    var land by mutableStateOf(0)

    val speed=10
    var clock_tmp=0
    var state_jump=0
    var state_right=0
    var state_left=0
    var jumps=30

    fun up() {
        if(state_y==-1){
            y-=90
            state_y+=1
        }
        else if(state_y != 5 && state_y <=5) {
            y -= 50
            state_y+=1
            //println("up")
        }
    }

    fun down() {
        if (state_y ==0) {
            y += 90
            state_y-=1
            //println("down")
        }
        if(state_y>0){
            y+=speed
        }
    }

    fun right() {
        if (state_x != 40) {
            x += speed
            state_x+=1
            //println("right")
        }
    }
    fun left() {
        if (state_x != -10) {
            x -= speed
            state_x-=1
            //println("left")
        }
    }

    fun jump(game_state: game_data){
        if(state_jump==0){
            state_jump=1
            clock_tmp=game_state.clock
        }
        if(state_jump==1){
            y -= jumps
            Thread.sleep(80)
            jumps-=game_state.gravity*(game_state.clock-clock_tmp)
            if(y>=game_state.land_h-size_y){
                state_jump=0
                jumps=35
                y=land-size_y
                println("$jumps $y")
            }
        }
    }

    fun drop(){
        if(state_y>0 && y+size_y<land){
            y+=1
        }
        if(state_y>0 && y+size_y>land){
            y=land-size_y
            state_y=0
        }
        if(y+size_y==land){
            state_y=0
        }
    }

    fun init(fx:Int=100,fy:Int){
        println("me init")
        x=fx
        y=fy-size_y
        land=fy
        state_x=0
        state_y=0
    }
}

class enemy_data(typein:String="land",modein:String="straight"){
    var x by mutableStateOf(0)
    var y by mutableStateOf(0)

    var size_x by mutableStateOf(130)
    var size_y by mutableStateOf(70)

    var land by mutableStateOf(0)

    val speed =3
    val type=typein
    val mode=modein


    fun init(fx:Int=500, fy:Int){
        //println("enemy init")
        if(type=="land") {
            x = fx+size_x
            y = fy - size_y
            land = fy
        }
        else if(type=="sky"){
            x = fx+size_x
            y = fy - size_y-80
            land = fy
        }else if(type=="under") {
            x = fx + size_x
            y = fy - size_y + 60
            land = fy
            if(mode=="upper"){
                x = fx
            }
        }
    }

    fun move(modefun:String=mode){
        if(modefun=="straight"){
            straight()
        }
        if(modefun=="upper"){
            upper()
        }
    }

    fun straight(){
        x+=-speed
    }
    fun upper(){
        x-=speed*2
        y-=speed
    }
}

class game_data() {
    var clock by mutableStateOf(0)
    var start by mutableStateOf(false)
    var finish by mutableStateOf(false)
    val land_h by mutableStateOf(680)

    var me_state = me_data()
    var enemy_state= mutableStateListOf<enemy_data>()
    var last_e_clock=0
    var interval=0
    var interval_shrink=0

    val gravity=1

    var times=0

    fun starting() {
        start = true
        clock=0
        me_state.init(100,land_h)
        last_e_clock=0
        interval=(100..400).random()
        enemy_state.add(enemy_data())
        for(enemies in enemy_state){
            enemies.init(fy=land_h)
        }
        //println(enemy_state[0])
    }

    fun finishing() {
        finish = true
        enemy_state.clear()
    }

    fun reset() {
        start = false
        finish = false
    }
}