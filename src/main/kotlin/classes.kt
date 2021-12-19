import androidx.compose.runtime.*
import java.lang.reflect.Type

class me_data() {
    var size_x by mutableStateOf(50)
    var size_y by mutableStateOf(100)

    var y by mutableStateOf(0)
    var x by mutableStateOf(0)

    var state_x:Int = 0
    var state_y:Int = 0

    var land by mutableStateOf(0)

    val speed=5

    fun up() {
        if(state_y==-1){
            y-=90
            state_y+=1
        }
        else if(state_y != 1) {
            y -= 150
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
        if(state_y==1){
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
        if (state_x != -40) {
            x -= speed
            state_x-=1
            //println("left")
        }
    }

    fun drop(){
        if(state_y==1 && y<land){
            y+=1
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

open class enemy_data(type:String="land"){
    var x by mutableStateOf(0)
    var y by mutableStateOf(0)

    var size_x by mutableStateOf(150)
    var size_y by mutableStateOf(70)

    var land by mutableStateOf(0)

    val speed =3

    val mode=type


    fun init(fx:Int=500,fy:Int){
        println("enemy init")
        if(mode=="land") {
            x = fx
            y = fy - size_y
            land = fy
        }
        else if(mode=="sky"){
            x = fx
            y = fy - size_y-100
            land = fy
        }
    }

    fun straight(){
        x+=-speed
    }
}

class game_data() {
    var clock: Int = 0
    var start by mutableStateOf(false)
    var finish by mutableStateOf(false)
    val land_h by mutableStateOf(350)

    var me_state = me_data()
    var enemy_state= mutableStateListOf<enemy_data>()

    fun starting() {
        start = true
        enemy_state+=(enemy_data())
        me_state.init(100,land_h)
        for(enemies in enemy_state){
            enemies.init(600,land_h)
        }
    }

    fun finishing() {
        finish = true
    }

    fun reset() {
        start = false
        finish = false
    }
}