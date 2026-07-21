package ir.ayeh.taamal.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.ayeh.taamal.navigation.Routes
import ir.ayeh.taamal.presentation.about.AboutScreen
import ir.ayeh.taamal.presentation.ayah.AyahDetailScreen
import ir.ayeh.taamal.presentation.challenges.ChallengeDetailScreen
import ir.ayeh.taamal.presentation.challenges.ChallengesScreen
import ir.ayeh.taamal.presentation.challenges.ChallengesViewModel
import ir.ayeh.taamal.presentation.games.GamesScreen
import ir.ayeh.taamal.presentation.help.HelpScreen
import ir.ayeh.taamal.presentation.home.HomeScreen
import ir.ayeh.taamal.presentation.journal.JournalScreen
import ir.ayeh.taamal.presentation.more.MoreScreen
import ir.ayeh.taamal.presentation.onboarding.OnboardingScreen
import ir.ayeh.taamal.presentation.paths.PathDetailScreen
import ir.ayeh.taamal.presentation.paths.PathsScreen
import ir.ayeh.taamal.presentation.privacy.PrivacyScreen
import ir.ayeh.taamal.presentation.profile.ProfileScreen
import ir.ayeh.taamal.presentation.quran.QuranScreen
import ir.ayeh.taamal.presentation.quran.SurahScreen
import ir.ayeh.taamal.presentation.quiz.QuizScreen
import ir.ayeh.taamal.presentation.reports.ReportsScreen
import ir.ayeh.taamal.presentation.scenarios.ScenarioDetailScreen
import ir.ayeh.taamal.presentation.scenarios.ScenariosScreen
import ir.ayeh.taamal.presentation.scenarios.ScenariosViewModel
import ir.ayeh.taamal.presentation.search.SearchScreen
import ir.ayeh.taamal.presentation.settings.SettingsScreen
import ir.ayeh.taamal.presentation.situations.SituationDetailScreen
import ir.ayeh.taamal.presentation.situations.SituationsScreen
import ir.ayeh.taamal.presentation.situations.SituationsViewModel
import ir.ayeh.taamal.presentation.splash.SplashScreen
import ir.ayeh.taamal.ui.components.ScreenScaffold

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun AppRoot(viewModel: RootViewModel = hiltViewModel()) {
    val ready by viewModel.ready.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    if (!ready) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            SplashScreen {
                val next = if (settings.onboardingDone) Routes.Main else Routes.Onboarding
                navController.navigate(next) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            }
        }
        composable(Routes.Onboarding) {
            OnboardingScreen(
                settings = settings,
                onUpdate = { next -> viewModel.updateSettings { next } },
                onFinished = {
                    viewModel.completeOnboarding()
                    navController.navigate(Routes.Main) {
                        popUpTo(Routes.Onboarding) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Main) {
            MainTabs(onNavigate = { route -> navController.navigate(route) })
        }
        composable(Routes.PathDetail, arguments = listOf(navArgument("pathId") { type = NavType.LongType })) { entry ->
            ScreenScaffold("جزئیات مسیر", { navController.popBackStack() }) {
                PathDetailScreen(
                    pathId = entry.arguments?.getLong("pathId") ?: 0L,
                    onOpenAyah = { navController.navigate(Routes.ayah(it)) }
                )
            }
        }
        composable(Routes.ChallengeDetail, arguments = listOf(navArgument("challengeId") { type = NavType.LongType })) { entry ->
            val id = entry.arguments?.getLong("challengeId") ?: 0L
            val vm: ChallengesViewModel = hiltViewModel()
            val challenges by vm.challenges.collectAsStateWithLifecycle()
            ScreenScaffold("جزئیات چالش", { navController.popBackStack() }) {
                ChallengeDetailScreen(
                    challenge = challenges.find { it.id == id },
                    onOpenAyah = { navController.navigate(Routes.ayah(it)) },
                    onCompleteDay = { vm.completeDay() }
                )
            }
        }
        composable(Routes.Quran) {
            ScreenScaffold("قرآن کریم", { navController.popBackStack() }) {
                QuranScreen(onOpenSurah = { navController.navigate(Routes.surah(it)) })
            }
        }
        composable(Routes.Surah, arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })) { entry ->
            ScreenScaffold("سوره", { navController.popBackStack() }) {
                SurahScreen(
                    surahNumber = entry.arguments?.getInt("surahNumber") ?: 1,
                    onOpenAyah = { navController.navigate(Routes.ayah(it)) }
                )
            }
        }
        composable(Routes.Ayah, arguments = listOf(navArgument("ayahId") { type = NavType.LongType })) { entry ->
            ScreenScaffold("جزئیات آیه", { navController.popBackStack() }) {
                AyahDetailScreen(ayahId = entry.arguments?.getLong("ayahId") ?: 0L)
            }
        }
        composable(Routes.Search) {
            ScreenScaffold("جست‌وجو", { navController.popBackStack() }) {
                SearchScreen(onOpenAyah = { navController.navigate(Routes.ayah(it)) })
            }
        }
        composable(Routes.Situations) {
            ScreenScaffold("موقعیت زندگی", { navController.popBackStack() }) {
                SituationsScreen(onOpenSituation = { navController.navigate(Routes.situation(it)) })
            }
        }
        composable(Routes.SituationDetail, arguments = listOf(navArgument("situationId") { type = NavType.LongType })) { entry ->
            val id = entry.arguments?.getLong("situationId") ?: 0L
            val vm: SituationsViewModel = hiltViewModel()
            val list by vm.situations.collectAsStateWithLifecycle()
            ScreenScaffold("موقعیت", { navController.popBackStack() }) {
                SituationDetailScreen(
                    situation = list.find { it.id == id },
                    onOpenAyah = { navController.navigate(Routes.ayah(it)) }
                )
            }
        }
        composable(Routes.Scenarios) {
            ScreenScaffold("سناریوها", { navController.popBackStack() }) {
                ScenariosScreen(onOpenScenario = { navController.navigate(Routes.scenario(it)) })
            }
        }
        composable(Routes.ScenarioDetail, arguments = listOf(navArgument("scenarioId") { type = NavType.LongType })) { entry ->
            val id = entry.arguments?.getLong("scenarioId") ?: 0L
            val vm: ScenariosViewModel = hiltViewModel()
            val list by vm.scenarios.collectAsStateWithLifecycle()
            ScreenScaffold("سناریو", { navController.popBackStack() }) {
                ScenarioDetailScreen(
                    scenario = list.find { it.id == id },
                    onOpenAyah = { navController.navigate(Routes.ayah(it)) }
                )
            }
        }
        composable(Routes.Quiz) {
            ScreenScaffold("آزمون", { navController.popBackStack() }) { QuizScreen() }
        }
        composable(Routes.Games) {
            ScreenScaffold("بازی‌ها", { navController.popBackStack() }) { GamesScreen() }
        }
        composable(Routes.Settings) {
            ScreenScaffold("تنظیمات", { navController.popBackStack() }) { SettingsScreen() }
        }
        composable(Routes.Reports) {
            ScreenScaffold("گزارش پیشرفت", { navController.popBackStack() }) { ReportsScreen() }
        }
        composable(Routes.Privacy) {
            ScreenScaffold("حریم خصوصی", { navController.popBackStack() }) { PrivacyScreen() }
        }
        composable(Routes.About) {
            ScreenScaffold("درباره ما", { navController.popBackStack() }) { AboutScreen(onPrivacy = { navController.navigate(Routes.Privacy) }) }
        }
        composable(Routes.Help) {
            ScreenScaffold("راهنما", { navController.popBackStack() }) { HelpScreen() }
        }
        composable(Routes.More) {
            ScreenScaffold("بیشتر", { navController.popBackStack() }) {
                MoreScreen(
                    onQuran = { navController.navigate(Routes.Quran) },
                    onSearch = { navController.navigate(Routes.Search) },
                    onSituations = { navController.navigate(Routes.Situations) },
                    onQuiz = { navController.navigate(Routes.Quiz) },
                    onScenarios = { navController.navigate(Routes.Scenarios) },
                    onGames = { navController.navigate(Routes.Games) },
                    onReports = { navController.navigate(Routes.Reports) },
                    onSettings = { navController.navigate(Routes.Settings) },
                    onHelp = { navController.navigate(Routes.Help) },
                    onAbout = { navController.navigate(Routes.About) },
                    onPrivacy = { navController.navigate(Routes.Privacy) }
                )
            }
        }
    }
}

@Composable
private fun MainTabs(onNavigate: (String) -> Unit) {
    val tabs = remember {
        listOf(
            TabItem(Routes.Home, "خانه", Icons.Outlined.Home),
            TabItem(Routes.Paths, "مسیرها", Icons.Outlined.AutoStories),
            TabItem(Routes.Challenges, "چالش‌ها", Icons.Outlined.EmojiEvents),
            TabItem(Routes.Journal, "دفتر تدبر", Icons.Outlined.MenuBook),
            TabItem(Routes.Profile, "پروفایل", Icons.Outlined.Person)
        )
    }
    val tabNav = rememberNavController()
    val backStack by tabNav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = current == tab.route,
                        onClick = {
                            tabNav.navigate(tab.route) {
                                popUpTo(tabNav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = tabNav,
            startDestination = Routes.Home,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.Home) {
                HomeScreen(
                    onOpenAyah = { onNavigate(Routes.ayah(it)) },
                    onOpenPath = { onNavigate(Routes.pathDetail(it)) },
                    onOpenChallenge = { onNavigate(Routes.challengeDetail(it)) },
                    onOpenQuran = { onNavigate(Routes.Quran) },
                    onOpenSearch = { onNavigate(Routes.Search) },
                    onOpenQuiz = { onNavigate(Routes.Quiz) },
                    onOpenJournal = {
                        tabNav.navigate(Routes.Journal) {
                            popUpTo(tabNav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onOpenSituations = { onNavigate(Routes.Situations) },
                    onOpenMore = { onNavigate(Routes.More) }
                )
            }
            composable(Routes.Paths) {
                PathsScreen(onOpenPath = { onNavigate(Routes.pathDetail(it)) })
            }
            composable(Routes.Challenges) {
                ChallengesScreen(onOpenChallenge = { onNavigate(Routes.challengeDetail(it)) })
            }
            composable(Routes.Journal) { JournalScreen() }
            composable(Routes.Profile) {
                ProfileScreen(
                    onSettings = { onNavigate(Routes.Settings) },
                    onAbout = { onNavigate(Routes.About) },
                    onHelp = { onNavigate(Routes.Help) },
                    onReports = { onNavigate(Routes.Reports) }
                )
            }
        }
    }
}
