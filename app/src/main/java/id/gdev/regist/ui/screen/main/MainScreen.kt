package id.gdev.regist.ui.screen.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.gdev.regist.ui.screen.createevent.CreateEventScreen
import id.gdev.regist.ui.screen.detailevent.DetailEventScreen
import id.gdev.regist.ui.screen.detailparticipant.DetailParticipantScreen
import id.gdev.regist.ui.screen.event.EventScreen
import id.gdev.regist.ui.screen.setup.SetupScreen


@Composable
fun MainScreen(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MainRouter.EVENT){
        composable(MainRouter.EVENT){
            EventScreen(navController)
        }
        composable(MainRouter.EVENT_DETAIL.plus(MainArg.getArg(MainArg.EVENT_ID))){
            DetailEventScreen(navController, it.arguments?.getString(MainArg.EVENT_ID))
        }
        composable(MainRouter.CREATE_EVENT){
            CreateEventScreen(navController)
        }
        composable(MainRouter.SETUP){
            SetupScreen(navController)
        }
        composable(MainRouter.DETAIL_PARTICIPANT){
            DetailParticipantScreen(navController)
        }
    }
}

